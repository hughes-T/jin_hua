package hughes.jin_hua.service;

import com.google.common.collect.Lists;
import hughes.jin_hua.pojo.Card;
import hughes.jin_hua.pojo.Player;
import hughes.jin_hua.pojo.PlayerRoundInfo;
import hughes.jin_hua.pojo.RoundInfo;
import hughes.jin_hua.utils.CardUtils;
import hughes.jin_hua.utils.ParamUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

/**
 * 回合管理器
 *
 * @author hughes
 * @date 2024/03/12 16:43
 */
@Service
public class RoundManager {

    private List<PlayerRoundInfo> playerRoundInfos;

    private PlayerRoundInfo currentPlayerRound;

    private RoundInfo beforeRoundCache;

    private RoundInfo currentRoundCache;

    public List<PlayerRoundInfo> getPlayerRoundInfos() {
        return playerRoundInfos;
    }

    public PlayerRoundInfo findPlayerRoundInfo(Player player) {
        for (PlayerRoundInfo playerRoundInfo : playerRoundInfos) {
            if (playerRoundInfo.getPlayer().equals(player)){
                return playerRoundInfo;
            }
        }
        throw new RuntimeException("未匹配到的玩家回合");
    }

    public PlayerRoundInfo getCurrentPlayerRound() {
        return currentPlayerRound;
    }

    public RoundInfo getBeforeRoundCache() {
        return beforeRoundCache;
    }

    public RoundInfo getCurrentRoundCache() {
        return currentRoundCache;
    }

    @Resource
    private PlayerManager playerManager;


    /**
     * 启动首轮回合
     */
    public void startFirstRound() {
        startRound(null);
    }

    /**
     * 开启对局
     *
     * @param winPlayer 上局胜利的玩家 为 null 时代办首轮对局
     */
    public void startRound(Player winPlayer) {
        List<Player> players = playerManager.getAllPlayer();
        int startIndex = 0;
        if (!ObjectUtils.isEmpty(winPlayer)) {
            beforeRoundCache = currentRoundCache;
            currentRoundCache = null;
            for (int i = 0; i < players.size(); i++) {
                if (winPlayer.equals(players.get(i))) {
                    startIndex = i;
                    break;
                }
            }
        }
        //创建对局信息
        RoundInfo roundInfo = new RoundInfo();
        roundInfo.setPoolNumber(0);
        roundInfo.setRoundNum(0);
        roundInfo.setFightShowContent("");
        currentRoundCache = roundInfo;
        //创建玩家回合信息
        playerRoundInfos = Lists.newArrayList();
        for (int i = startIndex; i < players.size(); i++) {
            Player player = players.get(i);
            PlayerRoundInfo playerRoundInfo = new PlayerRoundInfo();
            playerRoundInfo.setRoundInfo(roundInfo);
            playerRoundInfo.setPlayer(player);
            playerRoundInfos.add(playerRoundInfo);
        }
        for (int i = 0; i < startIndex; i++) {
            Player player = players.get(i);
            PlayerRoundInfo playerRoundInfo = new PlayerRoundInfo();
            playerRoundInfo.setRoundInfo(roundInfo);
            playerRoundInfo.setPlayer(player);
            playerRoundInfos.add(playerRoundInfo);
        }
        //默认当前玩家已经上底
        for (PlayerRoundInfo playerRoundInfo : playerRoundInfos) {
            playerRoundInfo.getPlayer().setChipNumber(
                    playerRoundInfo.getPlayer().getChipNumber() - ParamUtils.getBottomChipNumber());
            playerRoundInfo.setPutIntoCurrent(ParamUtils.getBottomChipNumber());
            playerRoundInfo.setPutIntoTotal(ParamUtils.getBottomChipNumber());
            currentRoundCache.setPoolNumber(currentRoundCache.getPoolNumber() + ParamUtils.getBottomChipNumber());
        }
        currentRoundCache.setFightShowContent("全部玩家已上底");
        //玩家发牌
        initPlayersCard();
        //设置当前执行玩家
        currentPlayerRound = playerRoundInfos.get(0);
        currentRoundCache.setRoundNum(1);
    }


    private void initPlayersCard() {
        for (PlayerRoundInfo playerRoundInfo : playerRoundInfos) {
            playerRoundInfo.setCards(Lists.newArrayList());
            playerRoundInfo.setCardStatus(PlayerRoundInfo.CARD_STATUS_UN_LOOK);

        }
        List<Card> cards = CardUtils.initCards();
        for (int i = 0; i < 3; i++) {
            playerRoundInfos.forEach(playerRoundInfo -> {
                playerRoundInfo.getCards().add(cards.remove(0));
            });
        }
        playerRoundInfos.forEach(playerRoundInfo -> playerRoundInfo.getCards().sort(Comparator.comparing(Card::getLevel).reversed()));
    }


}
