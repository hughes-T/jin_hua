package hughes.jin_hua.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Card {

    private String name;

    //黑红梅方
    public static final String TYPE_SPADE = "黑桃♠";
    public static final String TYPE_RED_HEART = "红心♥";
    public static final String TYPE_BLOSSOM = "梅花♣";
    public static final String TYPE_BLOCK = "方片♦";
    private String type;

    public static final String POINT_A = "A";
    public static final String POINT_K = "K";
    public static final String POINT_Q = "Q";
    public static final String POINT_J = "J";
    private String point;

    private Integer level;

    public String getDesc(){
        return point + type;
    }

}
