package hughes.jin_hua.service;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import hughes.jin_hua.constants.GameConsts;
import hughes.jin_hua.pojo.ApiResult;
import hughes.jin_hua.pojo.Player;
import hughes.jin_hua.pojo.PlayerRoundInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
public class GameEngine {

    @Resource
    private PlayerManager playerManager;

    @Resource
    private RoundManager roundManager;

    /**
     * 初始状态
     */
    private static String GAME_STATUS = "1";
    public static final String GAME_STATUS_START = "2";
    public static final String GAME_STATUS_CREATE_ING = "1";
    public static final String GAME_STATUS_CLOSE = "0";

    public static final Map<String, String> GAME_STATUS_MAP = ImmutableMap.of(
            GAME_STATUS_START, "已开始",
            GAME_STATUS_CREATE_ING, "创建中",
            GAME_STATUS_CLOSE, "已结束");

    public String getGameStatus() {
        return GAME_STATUS;
    }

    public synchronized Boolean changeGameStatus(String status) {
        if (GAME_STATUS_CREATE_ING.equals(status)) {
            Preconditions.checkArgument(GAME_STATUS.equals(GAME_STATUS_CLOSE), "仅能在游戏状态0（已结束） 切换到 1（创建中）");
            //创建游戏-等待玩家加入
            GAME_STATUS = GAME_STATUS_CREATE_ING;
            playerManager.clearPlayer();
        } else if (GAME_STATUS_START.equals(status)) {
            Preconditions.checkArgument(GAME_STATUS.equals(GAME_STATUS_CREATE_ING), "仅能在游戏状态1（创建中）切换到 2");
            playerManager.player2StartGame();
            //开启回合
            roundManager.startFirstRound();
            GAME_STATUS = GAME_STATUS_START;
        } else if (GAME_STATUS_CLOSE.equals(status)) {
            //关闭游戏清空玩家状态
            playerManager.clearPlayer();
            GAME_STATUS = GAME_STATUS_CLOSE;
        }
        return Boolean.TRUE;
    }


    /**
     * 获取页面信息
     *
     * @return showText 展示区信息 showButtons 操作按钮
     */
    public ApiResult getGamePageInfo(Map<String, String> param) {
        Player player = playerManager.matchPlayerByToken(param.get("userToken"));
        //展示区信息 + 操作按钮
        StringJoiner showText = new StringJoiner("\n");
        Set<String> showButtons = Sets.newHashSet();

        boolean isMasterPlayer = player.getUserToken().equals(playerManager.getAllPlayer().get(0).getUserToken());
        if (isMasterPlayer) {
            showButtons.add(GameConsts.RESTART_GAME_BUTTON);
        }
        if (!GAME_STATUS.equals(GAME_STATUS_START)) {
            //未开始阶段
            if (isMasterPlayer) {
                //房主拥有开始 开始游戏按钮权限
                showButtons.add(GameConsts.START_GAME_BUTTON);
            }
            return ApiResult.success(ImmutableMap.of("showText", "等待开始", "showButtons", showButtons));
        }

        if (!ObjectUtils.isEmpty(roundManager.getBeforeRoundCache())) {
            //拼接上局战果
            showText.add("----上一局结果----");
            showText.add(roundManager.getBeforeRoundCache().getResultShowContent());
            showText.add("------------------------");
        }
        //拼接当前战局
        showText.add(String.format("----当前局第%s轮----", roundManager.getCurrentRoundCache().getRoundNum()));
        showText.add(roundManager.getCurrentRoundCache().getFightShowContent());
        showText.add("------------------------");
        //获取对应的回合
        PlayerRoundInfo playerRoundInfo = roundManager.findPlayerRoundInfo(player);
        //阶段、牌型
        showText.add("阶段：" + playerRoundInfo.getCardStatusDesc());
        if (!PlayerRoundInfo.CARD_STATUS_UN_LOOK.equals(playerRoundInfo.getCardStatus())) {
            //非暗阶段 加载自己的信息
            showText.add(String.format("牌型 %s", playerRoundInfo.getCardsDesc()));
        }
        if (roundManager.getCurrentPlayerRound().equals(playerRoundInfo)) {
            //本人回合
            if (PlayerRoundInfo.CARD_STATUS_UN_LOOK.equals(playerRoundInfo.getCardStatus())) {
                showButtons.add(GameConsts.LOOK_CARD_BUTTON);
            }
            showButtons.add(GameConsts.ABANDON_CARD_BUTTON);
            showButtons.add(GameConsts.ADD_CHIP);
            if (roundManager.getCurrentRoundCache().getRoundNum() > 2) {
                //可对拼的玩家
                for (Player otherPlayer : playerManager.getAllPlayer()) {
                    if (otherPlayer.equals(player)) {
                        continue;
                    }
                    PlayerRoundInfo otherPlayerRound = roundManager.findPlayerRoundInfo(otherPlayer);
                    if (PlayerRoundInfo.CARD_STATUS_UN_LOOK.equals(otherPlayerRound.getCardStatus())
                            || PlayerRoundInfo.CARD_STATUS_LOOK.equals(otherPlayerRound.getCardStatus())) {
                        showButtons.add(GameConsts.FIGHT_PLAYER_BUTTON + otherPlayer.getName());
                    }
                }
            }
        } else {
            //非本人回合
            showText.add(String.format("等待 %s 操作", roundManager.getCurrentPlayerRound().getPlayer().getName()));
        }
        //你当前的筹码剩余
        showText.add("你当前的筹码剩余：" + player.getChipNumber());
        //加载其他人信息
        showText.add("------------------------");
        for (Player otherPlayer : playerManager.getAllPlayer()) {
            if (otherPlayer.equals(player)) {
                continue;
            }
            PlayerRoundInfo otherPlayerRound = roundManager.findPlayerRoundInfo(otherPlayer);
            showText.add(String.format("玩家 %s 阶段： %s ，筹码剩余：%s",
                    otherPlayer.getName(), otherPlayerRound.getCardStatusDesc(), otherPlayer.getChipNumber()));
        }
        return ApiResult.success(ImmutableMap.of("showText", showText.toString(), "showButtons", showButtons));
    }


    /**
     * 按钮操作
     */
    public ApiResult buttonReq(Map<String, String> param) {
        String buttonType = param.get("buttonType");
        String userToken = param.get("userToken");
        if (playerManager.isMasterPlayer(userToken)) {
            //房主-房间操作
            if (GameConsts.START_GAME_BUTTON.equals(buttonType)) {
                //开始游戏
                changeGameStatus(GAME_STATUS_START);
                return ApiResult.success();
            }
            if (GameConsts.RESTART_GAME_BUTTON.equals(buttonType)) {
                //重启游戏
                changeGameStatus(GAME_STATUS_CLOSE);
                changeGameStatus(GAME_STATUS_CREATE_ING);
                return ApiResult.success();
            }
        }
        //玩家游戏操作
        playerManager.playerOption(userToken, buttonType, param);
        return ApiResult.success();
    }


    public ApiResult queryGameInfo() {
        return ApiResult.success(String.format("游戏状态：%s，\n 已加入的玩家： %s "
                , GAME_STATUS_MAP.get(GAME_STATUS),
                Joiner.on("、").join(playerManager.getAllPlayer().stream().map(Player::getName).collect(Collectors.toList()))));
    }

}
