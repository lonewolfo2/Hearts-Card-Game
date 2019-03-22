package main.controller;

import java.util.ArrayList;
import java.util.List;

import main.constant.CARD_SUIT;
import main.constant.CARD_VALUE;
import main.model.CardUI;

/**
 *
 * @author lonewolf
 */
public class OnlineTools {

    public static String list2message(List<CardUI> list) {
        String result = "";

        for (CardUI cardUI : list) {
            result = result + card2message(cardUI) + "@";
        }

        return result;
    }

    public static String card2message(CardUI cardUI) {
        String message = cardUI.getOwner() + "#" + cardUI.getSuit() + "#" + cardUI.getValue() + "#"
                + cardUI.getPickable();

        return message;
    }

    public static List<CardUI> message2list(String message) {
        List<CardUI> list = new ArrayList<CardUI>();

        String[] arr = message.split("@");

        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals("")) {

            } else {
                CardUI temp = message2card(arr[i]);
                if (temp != null) {
                    list.add(temp);
                }
            }
        }

        return list;
    }

    public static CardUI message2card(String message) {
        CardUI cardUI = new CardUI();
        Boolean exist;
        int a = 0;
        try {
            String[] strArr = message.split("#");
            String id = strArr[0];
            String suit = strArr[1];
            String value = strArr[2];
            String pick = strArr[3];

            if (id.equals("") || suit.equals("") || value.equals("") || pick.equals("")) {
                a = 1 / 0;
            }

            // System.out.println(id + " " + suit + " " + value + " " + pick);
            cardUI.setOwner(id);

            exist = false;
            for (CARD_SUIT item : CARD_SUIT.values()) {
                if (item.toString().equals(suit)) {
                    cardUI.setSuit(item);
                    exist = true;
                    break;
                }
            }

            if (!exist) {
                a = 1 / 0;
            }

            exist = false;
            for (CARD_VALUE item : CARD_VALUE.values()) {
                if (item.toString().equals(value)) {
                    cardUI.setValue(item);
                    exist = true;
                    break;
                }
            }

            if (!exist) {
                a = 1 / 0;
            }

            if (pick.contains("t") || pick.contains("T")) {
                cardUI.setPickable(true);
            } else {
                cardUI.setPickable(false);
            }
        } catch (Exception ex) {
            System.out.println("EROOOR");
            return null;
        }
        return cardUI;
    }

    public static String increaseID(String id) {
        if (id.equals("A")) {
            return "B";
        } else if (id.equals("B")) {
            return "C";
        } else if (id.equals("C")) {
            return "D";
        } else if (id.equals("D")) {
            return "A";
        }

        System.out.println("ERROR increaseID " + id);
        return "";
    }

    public static String decreaseID(String id) {
        if (id.equals("A")) {
            return "D";
        } else if (id.equals("B")) {
            return "A";
        } else if (id.equals("C")) {
            return "B";
        } else if (id.equals("D")) {
            return "C";
        }

        System.out.println("ERROR decreaseID " + id);
        return "";
    }

    public static int id2index(String id) {
        if (id.equals("A")) {
            return 0;
        } else if (id.equals("B")) {
            return 1;
        } else if (id.equals("C")) {
            return 2;
        } else if (id.equals("D")) {
            return 3;
        }

        System.out.println("ERROR id2index " + id);
        return -1;
    }

    public static String index2id(int index) {
        if (index == 0) {
            return "A";
        } else if (index == 1) {
            return "B";
        } else if (index == 2) {
            return "C";
        } else if (index == 3) {
            return "D";
        }

        System.out.println("ERROR id2index " + index);
        return "";
    }
}
