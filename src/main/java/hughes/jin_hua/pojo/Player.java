package hughes.jin_hua.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.StringJoiner;

@Data
@Accessors(chain = true)
public class Player {

    private String name;

    private String password;

    private List<Card> cards;

    public static final String CARD_STATUS_UN_LOOK = "0";
    public static final String CARD_STATUS_LOOK = "1";
    public static final String CARD_STATUS_ABANDON = "2";

    private String cardStatus;

    private String userToken;

    public static final String GAME_STATUS_UN_READY = "0";
    public static final String GAME_STATUS_READY = "1";
    public static final String GAME_STATUS_PLAY_ING = "2";
    private String gameStatus;


    public String getCardStatusDesc(){
        return matchCardStatus(cardStatus);
    }

    public String getCardsDesc(){
        StringJoiner result = new StringJoiner("|","|","|");
        for (Card card : cards) {
            result.add(card.getDesc());
        }
        return result.toString();
    }

    private String matchCardStatus(String cardStatus){
        if (CARD_STATUS_UN_LOOK.equals(cardStatus)){
            return "焖牌";
        }
        if (CARD_STATUS_LOOK.equals(cardStatus)){
            return "看牌";
        }
        if (CARD_STATUS_ABANDON.equals(cardStatus)){
            return "弃牌";
        }
        return null;
    }

}
