package hughes.jin_hua.utils;

import com.google.common.collect.Lists;
import hughes.jin_hua.pojo.Card;
import hughes.jin_hua.pojo.HandCard;

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
        Integer calculate1 = handCard1.calculate();
        Integer calculate2 = handCard2.calculate();
        // 特殊情况 比较235和豹子
        if (handCard1.is235() && handCard2.isLeopard()) {
            return true;
        }
        if (handCard2.is235() && handCard1.isLeopard()) {
            return false;
        }
        return calculate1 > calculate2;
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
//        List<Card> cards = initCards();
//        List<List<Card>> partition = Lists.partition(cards, 3);
//        for (List<Card> cardList : partition) {
//            List<String> collected = cardList.stream().map(Card::getDesc).collect(Collectors.toList());
//            System.out.println(collected);
//            if (cardList.size() == 3) {
//                HandCard handCard = new HandCard(cardList);
//                System.out.println(handCard.calculate());
//            }
//
//        }
//    }

}
