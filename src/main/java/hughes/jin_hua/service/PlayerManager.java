package hughes.jin_hua.service;

import com.google.common.collect.Lists;
import hughes.jin_hua.pojo.Player;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 玩家管理器
 */
@Service
public class PlayerManager {

    private static final List<Player> PLAYER_LIST = Lists.newArrayList();


    public void init() {
        PLAYER_LIST.clear();
    }

}
