package hughes.jin_hua.pojo;

import lombok.Data;

/**
 * @author hughes
 * @date 2024/03/12 16:52
 */
@Data
public class RoundInfo {

    /**
     * 轮数
     */
    private Integer roundNum;

    /**
     * 池底
     */
    private Integer poolNumber;

    /**
     * 战局提示
     */
    private String fightShowContent;

    /**
     * 战斗结果
     */
    private String resultShowContent;

}
