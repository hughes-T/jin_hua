package hughes.jin_hua;

import java.util.*;

public class ZhaJinHuaGame {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println("====第" + (i+1)+ "局========");
            planStart();
        }
    }

    private static void planStart() {
        Scanner scanner = new Scanner(System.in);

        // 初始化牌
        String[] colors = {"红桃", "黑桃", "方块", "梅花"};
        String[] numbers = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        List<String> cards = new ArrayList<>();
        for (String color : colors) {
            for (String number : numbers) {
                cards.add(color + number);
            }
        }

        // 洗牌
        Collections.shuffle(cards);

        // 初始化玩家
        List<String> player1 = new ArrayList<>();
        List<String> player2 = new ArrayList<>();
        List<String> player3 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            player1.add(cards.remove(0));
            player2.add(cards.remove(0));
            player3.add(cards.remove(0));
        }

        // 输出初始牌面
        System.out.println("阿飞的牌：" + player1);
        System.out.println("阿顺的牌：" + player2);
        System.out.println("阿力的牌：" + player3);

        // 比较牌面，并输出结果
//        int result = compare(player1, player2, player3);
//        if (result == 1) {
//            System.out.println("玩家1胜利");
//        } else if (result == 2) {
//            System.out.println("玩家2胜利");
//        } else {
//            System.out.println("玩家3胜利");
//        }

        scanner.close();
    }

    // 比较三个玩家的牌面大小
    public static int compare(List<String> player1, List<String> player2, List<String> player3) {
        int result = 0;
        List<List<String>> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        for (List<String> player : players) {
            // 判断牌型
            int type = getType(player);

            // 如果牌型相同，则比较牌面大小
            if (type == getType(player1) && compareCards(player, player1) > 0) {
                result = players.indexOf(player) + 1;
            } else if (type == getType(player2) && compareCards(player, player2) > 0) {
                result = players.indexOf(player) + 1;
            } else if (type == getType(player3) && compareCards(player, player3) > 0) {
                result = players.indexOf(player) + 1;
            }
        }
        return result;
    }

    // 比较两个玩家的牌面大小
    public static int compareCards(List<String> player1, List<String> player2) {
        int result = 0;
        List<String> p1 = new ArrayList<>(player1);
        List<String> p2 = new ArrayList<>(player2);

        // 判断是否为特殊牌型（豹子）
        if (getType(player1) == 3) {
            if (getType(player2) == 3) {
                result = p1.get(0).compareTo(p2.get(0));
            } else {
                result = 1;
            }
        } else if (getType(player2) == 3) {
            result = 2;
        } else {
            // 普通牌型比较
            Collections.sort(p1);
            Collections.sort(p2);
            result = p1.get(2).compareTo(p2.get(2));
            if (result == 0) {
                result = p1.get(1).compareTo(p2.get(1));
                if (result == 0) {
                    result = p1.get(0).compareTo(p2.get(0));
                }
            }
        }

        return result;
    }

    // 判断牌型
    public static int getType(List<String> player) {
        List<String> cards = new ArrayList<>(player);
        Collections.sort(cards);
        String first = cards.get(0);
        String second = cards.get(1);
        String third = cards.get(2);
        if (first.equals(second) && second.equals(third)) {
            return 3; // 豹子
        } else if (first.substring(1).equals(second.substring(1)) && second.substring(1).equals(third.substring(1))) {
            return 2; // 同花顺
        } else if (Integer.parseInt(first.substring(1)) + 1 == Integer.parseInt(second.substring(1)) && Integer.parseInt(second.substring(1)) + 1 == Integer.parseInt(third.substring(1))) {
            return 1; // 顺子
        } else {
            return 0; // 普通牌型
        }
    }
}
