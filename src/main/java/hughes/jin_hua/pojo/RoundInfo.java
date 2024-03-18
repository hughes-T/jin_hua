package hughes.jin_hua.pojo;

import hughes.jin_hua.utils.ParamUtils;
import lombok.Data;
import org.springframework.util.ObjectUtils;

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
     * 上一轮焖牌筹码
     */
    private Integer lastUnLookChipNumber;

    /**
     * 上一轮看牌筹码
     */
    private Integer lastLookChipNumber;

    /**
     * 战局提示
     */
    private String fightShowContent;

    /**
     * 战斗结果
     */
    private String resultShowContent;


    /**
     * 获取筹码最低增加限制
     */
    public int getMinAddChipNumber(String cardStatus) {
        if (PlayerRoundInfo.CARD_STATUS_UN_LOOK.equals(cardStatus)) {
            if (!ObjectUtils.isEmpty(lastUnLookChipNumber)) {
                return lastUnLookChipNumber;
            }
            return ParamUtils.getBottomChipNumber();
        }

        if (PlayerRoundInfo.CARD_STATUS_LOOK.equals(cardStatus)) {
            if (!ObjectUtils.isEmpty(lastLookChipNumber)) {
                return lastLookChipNumber;
            }
            return ParamUtils.getBottomChipNumber() * 2;
        }

        throw new IllegalArgumentException("不被允许的牌状态操作");
    }

    public void setMinAddChipNumber(int chipNumber, String cardStatus) {
        if (PlayerRoundInfo.CARD_STATUS_UN_LOOK.equals(cardStatus)) {
            lastUnLookChipNumber = chipNumber;
            return;
        }
        if (PlayerRoundInfo.CARD_STATUS_LOOK.equals(cardStatus)) {
            lastLookChipNumber = chipNumber;
            return;
        }
        throw new IllegalArgumentException("不被允许的牌状态操作");
    }


    /**
     * 校验筹码是否达标
     */
    public void judgeChipNumber(int chipNumber, String cardStatus) {
        int minAddChipNumber = getMinAddChipNumber(cardStatus);
        if (chipNumber < minAddChipNumber) {
            throw new IllegalArgumentException("筹码数量不得低于" + minAddChipNumber);
        }
        if (PlayerRoundInfo.CARD_STATUS_UN_LOOK.equals(cardStatus)) {
            if (chipNumber > ParamUtils.getRoundMaxChipNumber()) {
                throw new IllegalArgumentException("筹码数量不得超过上限");
            }
        }
        if (PlayerRoundInfo.CARD_STATUS_LOOK.equals(cardStatus)) {
            if (chipNumber > (ParamUtils.getRoundMaxChipNumber() * 2)) {
                throw new IllegalArgumentException("筹码数量不得超过上限");
            }
        }
    }


}
