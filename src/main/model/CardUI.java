package main.model;

/**
 *
 * @author lonewolf
 */
public class CardUI extends Card {

    private String owner;
    private Boolean pickable;

    @Override
    public String toString() {
        return super.toString() + "[owner=" + owner + "]";
    }

    public CardUI() {
        super();
        this.pickable = true;
    }

    public CardUI(CardUI card) {
        super(card.getSuit(), card.getValue());
        this.pickable = card.getPickable();
        this.owner = card.getOwner();
    }

    public CardUI(Card card) {
        super(card.getSuit(), card.getValue());
        this.pickable = true;
    }

    public CardUI(Card card, String owner) {
        super(card.getSuit(), card.getValue());
        this.pickable = true;
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getPickable() {
        return pickable;
    }

    public void setPickable(Boolean pickable) {
        this.pickable = pickable;
    }

}
