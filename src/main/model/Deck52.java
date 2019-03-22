package main.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.constant.*;

/**
 *
 * @author lonewolf
 */
public class Deck52 {

    private List<Card> list = new ArrayList<Card>();

    public void init52card() {

        list.clear();

        list.add(new Card(CARD_SUIT.CLUBS, CARD_VALUE.ACE));
        list.add(new Card(CARD_SUIT.CLUBS, CARD_VALUE.TWO));
        list.add(new Card(CARD_SUIT.CLUBS, CARD_VALUE.THREE));
        list.add(new Card(CARD_SUIT.CLUBS, CARD_VALUE.FOUR));
        list.add(new Card(CARD_SUIT.CLUBS, CARD_VALUE.FIVE));
        list.add(new Card(CARD_SUIT.CLUBS, CARD_VALUE.SIX));
        list.add(new Card(CARD_SUIT.CLUBS, CARD_VALUE.SEVEN));
        list.add(new Card(CARD_SUIT.CLUBS, CARD_VALUE.EIGHT));
        list.add(new Card(CARD_SUIT.CLUBS, CARD_VALUE.NINE));
        list.add(new Card(CARD_SUIT.CLUBS, CARD_VALUE.TEN));
        list.add(new Card(CARD_SUIT.CLUBS, CARD_VALUE.JACK));
        list.add(new Card(CARD_SUIT.CLUBS, CARD_VALUE.QUEEN));
        list.add(new Card(CARD_SUIT.CLUBS, CARD_VALUE.KING));

        list.add(new Card(CARD_SUIT.DIAMONDS, CARD_VALUE.ACE));
        list.add(new Card(CARD_SUIT.DIAMONDS, CARD_VALUE.TWO));
        list.add(new Card(CARD_SUIT.DIAMONDS, CARD_VALUE.THREE));
        list.add(new Card(CARD_SUIT.DIAMONDS, CARD_VALUE.FOUR));
        list.add(new Card(CARD_SUIT.DIAMONDS, CARD_VALUE.FIVE));
        list.add(new Card(CARD_SUIT.DIAMONDS, CARD_VALUE.SIX));
        list.add(new Card(CARD_SUIT.DIAMONDS, CARD_VALUE.SEVEN));
        list.add(new Card(CARD_SUIT.DIAMONDS, CARD_VALUE.EIGHT));
        list.add(new Card(CARD_SUIT.DIAMONDS, CARD_VALUE.NINE));
        list.add(new Card(CARD_SUIT.DIAMONDS, CARD_VALUE.TEN));
        list.add(new Card(CARD_SUIT.DIAMONDS, CARD_VALUE.JACK));
        list.add(new Card(CARD_SUIT.DIAMONDS, CARD_VALUE.QUEEN));
        list.add(new Card(CARD_SUIT.DIAMONDS, CARD_VALUE.KING));

        list.add(new Card(CARD_SUIT.HEARTS, CARD_VALUE.ACE));
        list.add(new Card(CARD_SUIT.HEARTS, CARD_VALUE.TWO));
        list.add(new Card(CARD_SUIT.HEARTS, CARD_VALUE.THREE));
        list.add(new Card(CARD_SUIT.HEARTS, CARD_VALUE.FOUR));
        list.add(new Card(CARD_SUIT.HEARTS, CARD_VALUE.FIVE));
        list.add(new Card(CARD_SUIT.HEARTS, CARD_VALUE.SIX));
        list.add(new Card(CARD_SUIT.HEARTS, CARD_VALUE.SEVEN));
        list.add(new Card(CARD_SUIT.HEARTS, CARD_VALUE.EIGHT));
        list.add(new Card(CARD_SUIT.HEARTS, CARD_VALUE.NINE));
        list.add(new Card(CARD_SUIT.HEARTS, CARD_VALUE.TEN));
        list.add(new Card(CARD_SUIT.HEARTS, CARD_VALUE.JACK));
        list.add(new Card(CARD_SUIT.HEARTS, CARD_VALUE.QUEEN));
        list.add(new Card(CARD_SUIT.HEARTS, CARD_VALUE.KING));

        list.add(new Card(CARD_SUIT.SPADES, CARD_VALUE.ACE));
        list.add(new Card(CARD_SUIT.SPADES, CARD_VALUE.TWO));
        list.add(new Card(CARD_SUIT.SPADES, CARD_VALUE.THREE));
        list.add(new Card(CARD_SUIT.SPADES, CARD_VALUE.FOUR));
        list.add(new Card(CARD_SUIT.SPADES, CARD_VALUE.FIVE));
        list.add(new Card(CARD_SUIT.SPADES, CARD_VALUE.SIX));
        list.add(new Card(CARD_SUIT.SPADES, CARD_VALUE.SEVEN));
        list.add(new Card(CARD_SUIT.SPADES, CARD_VALUE.EIGHT));
        list.add(new Card(CARD_SUIT.SPADES, CARD_VALUE.NINE));
        list.add(new Card(CARD_SUIT.SPADES, CARD_VALUE.TEN));
        list.add(new Card(CARD_SUIT.SPADES, CARD_VALUE.JACK));
        list.add(new Card(CARD_SUIT.SPADES, CARD_VALUE.QUEEN));
        list.add(new Card(CARD_SUIT.SPADES, CARD_VALUE.KING));

        Collections.shuffle(list);
    }

    public List<Card> getList() {
        return list;
    }

    private List<Card> getCard(int deck_num) {
        List<Card> result = new ArrayList<Card>();

        int pos = (deck_num - 1) * 13;

        // 0 - 12
        // 13 - 25
        // 26 - 38
        // 39 - 51
        for (int i = 0; i < 13; i++) {
            result.add(list.get(pos + i));
        }

        return result;
    }

    public List<Card> getDeck1() {
        return getCard(1);
    }

    public List<Card> getDeck2() {
        return getCard(2);
    }

    public List<Card> getDeck3() {
        return getCard(3);
    }

    public List<Card> getDeck4() {
        return getCard(4);
    }
}
