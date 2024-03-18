package hughes.jin_hua.utils;

import com.google.common.collect.Lists;
import hughes.jin_hua.pojo.Card;
import hughes.jin_hua.pojo.HandCard;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CardUtils {

    /**
     * 比较大小
     *
     * @param c1 发起方
     * @param c2 被比较方
     * @return true-c1>c2
     */
    public static boolean compareCard(List<Card> c1, List<Card> c2) {
        HandCard handCard1 = new HandCard(c1);
        HandCard handCard2 = new HandCard(c2);
        BigDecimal calculate1 = handCard1.calculate();
        BigDecimal calculate2 = handCard2.calculate();
        // 特殊情况 比较235和豹子
        if (handCard1.is235() && handCard2.isLeopard()) {
            return true;
        }
        if (handCard2.is235() && handCard1.isLeopard()) {
            return false;
        }
        return calculate1.compareTo(calculate2) > 0;
    }


    /**
     * 是否为喜钱牌
     */
    public static boolean isHappyMoneyCard(List<Card> cards){
        HandCard handCard = new HandCard(cards);
        handCard.calculate();
        return handCard.isLeopard() || handCard.isStraightFlower();
    }


    /**
     * 初始化牌
     */
    public static List<Card> initCards() {
        List<Card> cards = Lists.newArrayList();
        // 初始化牌
        String[] numbers = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        for (Card.TypeEum type : Card.TypeEum.values()) {
            for (int i = 0; i < numbers.length; i++) {
                cards.add(new Card().setType(type.getType()).setTypeLevel(type.getTypePoint()).setPointLevel(i + 1).setPoint(numbers[i]).setName(type + numbers[i]));
            }
        }
        // 洗牌
        Collections.shuffle(cards);
        return cards;
    }

//    public static void main(String[] args) {
//        List<Card> cards = Lists.newArrayList();
//        cards.add(new Card()
//                .setType(Card.TypeEum.TYPE_SPADE.getType())
//                .setTypeLevel(Card.TypeEum.TYPE_SPADE.getTypePoint())
//                .setPointLevel(13)
//                .setPoint("A")
//                .setName(Card.TypeEum.TYPE_SPADE.getType() + "A")
//        );
//        cards.add(new Card()
//                .setType(Card.TypeEum.TYPE_RED_HEART.getType())
//                .setTypeLevel(Card.TypeEum.TYPE_RED_HEART.getTypePoint())
//                .setPointLevel(10)
//                .setPoint("J")
//                .setName(Card.TypeEum.TYPE_RED_HEART.getType() + "J")
//        );
//        cards.add(new Card()
//                .setType(Card.TypeEum.TYPE_SPADE.getType())
//                .setTypeLevel(Card.TypeEum.TYPE_SPADE.getTypePoint())
//                .setPointLevel(7)
//                .setPoint("8")
//                .setName(Card.TypeEum.TYPE_SPADE.getType() + "8")
//        );
//        List<Card> cards2 = Lists.newArrayList();
//        cards2.add(new Card()
//                .setType(Card.TypeEum.TYPE_BLOSSOM.getType())
//                .setTypeLevel(Card.TypeEum.TYPE_BLOSSOM.getTypePoint())
//                .setPointLevel(13)
//                .setPoint("A")
//                .setName(Card.TypeEum.TYPE_BLOSSOM.getType() + "A")
//        );
//        cards2.add(new Card()
//                .setType(Card.TypeEum.TYPE_BLOSSOM.getType())
//                .setTypeLevel(Card.TypeEum.TYPE_BLOSSOM.getTypePoint())
//                .setPointLevel(12)
//                .setPoint("K")
//                .setName(Card.TypeEum.TYPE_BLOSSOM.getType() + "K")
//        );
//        cards2.add(new Card()
//                .setType(Card.TypeEum.TYPE_BLOCK.getType())
//                .setTypeLevel(Card.TypeEum.TYPE_BLOCK.getTypePoint())
//                .setPointLevel(2)
//                .setPoint("3")
//                .setName(Card.TypeEum.TYPE_BLOCK.getType() + "3")
//        );
//        System.out.println(compareCard(cards, cards2));
//    }

}
