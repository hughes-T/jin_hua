package hughes.jin_hua.utils;

import com.google.common.collect.Lists;
import hughes.jin_hua.pojo.Card;

import java.util.Collections;
import java.util.List;

public class CardUtils {

    /**
     * 比较大小
     */


    /**
     * 初始化牌
     */
    public static List<Card> initCards() {
        List<Card> cards = Lists.newArrayList();
        // 初始化牌
        String[] types = {Card.TYPE_SPADE, Card.TYPE_RED_HEART, Card.TYPE_BLOSSOM, Card.TYPE_BLOCK};
        String[] numbers = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        for (String type : types) {
            for (int i = 0; i < numbers.length; i++) {
                cards.add(new Card().setType(type).setLevel(i).setPoint(numbers[i]).setName(type + numbers[i]));
            }
        }
        // 洗牌
        Collections.shuffle(cards);
        return cards;
    }

}
