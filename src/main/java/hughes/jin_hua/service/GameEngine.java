package hughes.jin_hua.service;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameEngine {

    @Autowired
    private PlayerManager playerManager;

    /**
     * 初始状态
     */
    private static String GAME_STATUS = "0";
    private static final String GAME_STATUS_START = "2";
    private static final String GAME_STATUS_CREATE_ING = "1";
    private static final String GAME_STATUS_CLOSE = "0";

    public synchronized Boolean changeGameStatus(String status) {
        if (GAME_STATUS_CREATE_ING.equals(status)) {
            Preconditions.checkArgument(GAME_STATUS.equals(GAME_STATUS_CLOSE), "仅能在游戏关闭状态切换到 1");
            //创建游戏-等待玩家加入
            GAME_STATUS = GAME_STATUS_CREATE_ING;
            playerManager.init();
        }else if (GAME_STATUS_START.equals(status)) {
            Preconditions.checkArgument(GAME_STATUS.equals(GAME_STATUS_CREATE_ING), "仅能在游戏等待状态切换到 2");
            //检测玩家加入信息

            GAME_STATUS = GAME_STATUS_START;
        }else if (GAME_STATUS_CLOSE.equals(status)) {
            //关闭游戏清空玩家状态
            playerManager.init();
            GAME_STATUS = GAME_STATUS_START;
        }
        return Boolean.TRUE;
    }


}
