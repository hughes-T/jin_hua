package hughes.jin_hua.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import hughes.jin_hua.constants.GameConsts;
import hughes.jin_hua.pojo.ApiResult;
import hughes.jin_hua.pojo.Card;
import hughes.jin_hua.pojo.Player;
import hughes.jin_hua.utils.CardUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class GameEngine {

    /**
     * 玩家信息
     */
    private static final List<Player> PLAYER_LIST = Lists.newCopyOnWriteArrayList();

    /**
     * 初始状态
     */
    private static String GAME_STATUS = "1";
    private static final String GAME_STATUS_START = "2";
    private static final String GAME_STATUS_CREATE_ING = "1";
    private static final String GAME_STATUS_CLOSE = "0";

    public synchronized Boolean changeGameStatus(String status) {
        if (GAME_STATUS_CREATE_ING.equals(status)) {
            Preconditions.checkArgument(GAME_STATUS.equals(GAME_STATUS_CLOSE), "仅能在游戏关闭状态切换到 1");
            //创建游戏-等待玩家加入
            GAME_STATUS = GAME_STATUS_CREATE_ING;
            gameInfoClear();
        } else if (GAME_STATUS_START.equals(status)) {
            Preconditions.checkArgument(GAME_STATUS.equals(GAME_STATUS_CREATE_ING), "仅能在游戏等待状态切换到 2");
            //检测玩家加入信息
            Preconditions.checkArgument(PLAYER_LIST.size() > 1 && PLAYER_LIST.size() < 10, "参与人数不足或超量");
            //初始化玩家未准备状态
            initPlayReadyStatus();
            GAME_STATUS = GAME_STATUS_START;
        } else if (GAME_STATUS_CLOSE.equals(status)) {
            //关闭游戏清空玩家状态
            gameInfoClear();
            GAME_STATUS = GAME_STATUS_START;
        }
        return Boolean.TRUE;
    }

    private void initPlayReadyStatus() {
        PLAYER_LIST.forEach(ele -> ele.setGameStatus(Player.GAME_STATUS_UN_READY));
    }

    private void initPlayersCard() {
        PLAYER_LIST.forEach(player -> player
                .setCards(Lists.newArrayList())
                .setCardStatus(Player.CARD_STATUS_UN_LOOK)
                .setGameStatus(Player.GAME_STATUS_PLAY_ING)
        );
        List<Card> cards = CardUtils.initCards();
        for (int i = 0; i < 3; i++) {
            PLAYER_LIST.forEach(player -> {
                player.getCards().add(cards.remove(0));
            });
        }
        PLAYER_LIST.forEach(player -> player.getCards().sort(Comparator.comparing(Card::getLevel).reversed()));
    }

    private void gameInfoClear() {
        PLAYER_LIST.clear();
    }

    public ApiResult playerRegister(Map<String, String> param) {
        Preconditions.checkArgument(GAME_STATUS.equals(GAME_STATUS_CREATE_ING), "仅能在游戏等待状态注册玩家信息");
        String userName = param.get("userName");
        String password = param.get("password");
        Preconditions.checkArgument(!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password), "用户名或密码不能为空");
        Player player = new Player().setName(userName).setPassword(password);
        synchronized (PLAYER_LIST) {
            if (PLAYER_LIST.stream().anyMatch(ele -> userName.equals(ele.getName()))) {
                return ApiResult.fail("用户名已存在!");
            }
            PLAYER_LIST.add(player);
        }
        //生成唯一标识
        String userToken = String.valueOf(UUID.randomUUID().getLeastSignificantBits() * -1);
        player.setUserToken(userToken);
        return ApiResult.success(userToken);
    }


    public ApiResult playerLogin(Map<String, String> param) {
        String userName = param.get("userName");
        String password = param.get("password");
        Preconditions.checkArgument(!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password), "用户名或密码不能为空");
        Player matchPlayer = null;
        for (Player player : PLAYER_LIST) {
            if (userName.equals(player.getName()) && password.equals(player.getPassword())) {
                matchPlayer = player;
                break;
            }
        }
        if (matchPlayer == null) {
            return ApiResult.fail("用户名或密码不正确");
        }
        //生成唯一标识
        String userToken = String.valueOf(UUID.randomUUID().getLeastSignificantBits() * -1);
        matchPlayer.setUserToken(userToken);
        return ApiResult.success(userToken);
    }


    /**
     * 获取页面信息
     *
     * @return showText 展示区信息 showButtons 操作按钮
     */
    public ApiResult getGamePageInfo(Map<String, String> param) {
        Player player = matchPlayerByToken(param.get("userToken"));
        if (!GAME_STATUS.equals(GAME_STATUS_START)) {
            return ApiResult.success(ImmutableMap.of("showText", "等待开始", "showButtons", Lists.newArrayList()));
        }
        //展示区信息 + 操作按钮
        StringJoiner showText = new StringJoiner("\n");
        Set<String> showButtons = Sets.newHashSet();

        if (PLAYER_LIST.stream().anyMatch(ele -> Player.GAME_STATUS_UN_READY.equals(ele.getGameStatus()))){
            //准备阶段
            if (Player.GAME_STATUS_UN_READY.equals(player.getGameStatus())){
                showText.add("请准备");
                showButtons.add(GameConsts.READY_BUTTON);
            }
            StringJoiner unReadyNames = new StringJoiner("、");
            PLAYER_LIST.forEach(ele -> {
                if (Player.GAME_STATUS_UN_READY.equals(ele.getGameStatus())){
                    unReadyNames.add(ele.getName());
                }
            });
            showText.add("还未准备的玩家有" + unReadyNames.toString());
            return ApiResult.success(ImmutableMap.of("showText", showText.toString(), "showButtons", showButtons));
        }

        showText.add(String.format("你处于%s阶段", player.getCardStatusDesc()));
        if (!Player.CARD_STATUS_UN_LOOK.equals(player.getCardStatus())) {
            //非暗阶段 加载自己的信息
            showText.add(String.format("你的牌型 %s", player.getCardsDesc()));
        } else {
            showButtons.add(GameConsts.LOOK_CARD_BUTTON);
        }

        if (!Player.CARD_STATUS_ABANDON.equals(player.getCardStatus())) {
            //非弃阶段
            showButtons.add(GameConsts.ABANDON_CARD_BUTTON);
        }

        //加载其他人信息
        for (Player otherPlayer : PLAYER_LIST) {
            if (otherPlayer.equals(player)){
                continue;
            }
            if (Player.CARD_STATUS_UN_LOOK.equals(otherPlayer.getCardStatus())){
                showText.add(String.format("玩家 %s 处于焖牌阶段", otherPlayer.getName()));
            } else {
                showText.add(String.format("玩家 %s 处于 %s 阶段，牌型 %s",
                        otherPlayer.getName(), otherPlayer.getCardStatusDesc(), otherPlayer.getCardsDesc()));
            }
        }
        return ApiResult.success(ImmutableMap.of("showText", showText.toString(), "showButtons", showButtons));
    }


    /**
     * 按钮操作
     * 玩家全部完成，进入到待准备阶段(新的一轮)
     */
    public ApiResult buttonReq(Map<String, String> param) {
        Player player = matchPlayerByToken(param.get("userToken"));
        String buttonType = param.get("buttonType");
        Preconditions.checkArgument(!ObjectUtils.isEmpty(buttonType), "buttonType 不能为空!");
        if (GameConsts.READY_BUTTON.equals(buttonType)) {
            return readyOption(player);
        }
        Preconditions.checkArgument(!Player.GAME_STATUS_UN_READY.equals(player.getGameStatus()), "非准备状态不可执行准备之外的操作");
        if (GameConsts.LOOK_CARD_BUTTON.equals(buttonType)) {
            return lookCardOption(player);
        }
        if (GameConsts.ABANDON_CARD_BUTTON.equals(buttonType)) {
            return abandonCardOption(player);
        }
        return ApiResult.fail("未知的操作");
    }

    /**
     * 弃牌操作
     * 全部丢弃, 进入到待准备状态
     */
    private ApiResult abandonCardOption(Player player) {
        Preconditions.checkArgument(!Player.CARD_STATUS_ABANDON.equals(player.getCardStatus()),"你已经弃过牌");
        player.setCardStatus(Player.CARD_STATUS_ABANDON);
        if (PLAYER_LIST.stream().allMatch(ele -> Player.CARD_STATUS_ABANDON.equals(ele.getCardStatus()))) {
            initPlayReadyStatus();
        }
        return ApiResult.success();
    }

    /**
     * 看牌操作
     */
    private ApiResult lookCardOption(Player player) {
        Preconditions.checkArgument(Player.CARD_STATUS_UN_LOOK.equals(player.getCardStatus()),"只有焖牌才可以请求看牌");
        player.setCardStatus(Player.CARD_STATUS_LOOK);
        return ApiResult.success();
    }

    /**
     * 准备操作
     * 全部准备时,开启新一轮游戏
     */
    private ApiResult readyOption(Player player) {
        synchronized (player) {
            if (!Player.GAME_STATUS_UN_READY.equals(player.getGameStatus())) {
                throw new IllegalArgumentException("非待准备状态无法准备");
            }
            player.setGameStatus(Player.GAME_STATUS_READY);
        }
        if (PLAYER_LIST.stream().allMatch(ele -> Player.GAME_STATUS_READY.equals(ele.getGameStatus()))) {
            initPlayersCard();
        }
        return ApiResult.success();
    }


    private Player matchPlayerByToken(String token) throws IllegalArgumentException {
        for (Player player : PLAYER_LIST) {
            if (token.equals(player.getUserToken())) {
                return player;
            }
        }
        throw new IllegalArgumentException(ApiResult.CODE_TOKEN_LOSE);
    }


}
