package main.model;

import main.constant.CARD_SUIT;
import main.constant.CARD_VALUE;

/**
 *
 * @author lonewolf
 */
public class Card {

    private CARD_SUIT suit;
    private CARD_VALUE value;

    public Card() {
        super();
    }

    public Card(CARD_SUIT suit, CARD_VALUE value) {
        super();
        this.suit = suit;
        this.value = value;
    }

    public int getVirtualValue() {
        return this.suit.ordinal() * 13 + this.value.ordinal();
    }

    public Boolean isHearts() {
        if (this.suit == CARD_SUIT.HEARTS) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isTwoOfClubs() {
        if (this.suit == CARD_SUIT.CLUBS && this.value == CARD_VALUE.TWO) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isQueenOfSpades() {
        if (this.suit == CARD_SUIT.SPADES && this.value == CARD_VALUE.QUEEN) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isSmaller(Card other) {
        if (!this.suit.equals(other.suit)) {
            return false;
        }

        return (this.value.ordinal() < other.getValue().ordinal());
    }

    public Boolean isGreater(Card other) {
        if (!this.suit.equals(other.suit)) {
            return false;
        }

        return (this.value.ordinal() > other.getValue().ordinal());
    }

    public CARD_VALUE getValue() {
        return value;
    }

    public void setValue(CARD_VALUE value) {
        this.value = value;
    }

    public CARD_SUIT getSuit() {
        return suit;
    }

    public void setSuit(CARD_SUIT suit) {
        this.suit = suit;
    }

    @Override
    public String toString() {
        return "Card [value=" + value + ", suit=" + suit + "]";
    }

}
