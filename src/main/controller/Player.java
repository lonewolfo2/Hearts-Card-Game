package main.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import main.model.CardUI;

/**
 *
 * @author lonewolf
 */
public class Player {

    private List<CardUI> my_list = new ArrayList<CardUI>();
    private List<CardUI> play_list = new ArrayList<CardUI>();

    private String my_name;

    public String name() {
        return my_name;
    }

    public void name(String my_name) {
        this.my_name = my_name;
    }

    public void add_my_list(List<CardUI> list) {
        this.my_list.clear();
        this.my_list.addAll(list);

        // update card owner to me
        for (CardUI cardUI : my_list) {
            cardUI.setOwner(my_name);
            cardUI.setPickable(true);
        }
    }

    public void add_my_list(CardUI cardUI) {
        this.my_list.add(cardUI);

        // update card owner to me
        for (CardUI item : my_list) {
            item.setOwner(my_name);
            item.setPickable(true);
        }
    }

    public List<CardUI> get_my_list() {

        my_list.sort(new Comparator<CardUI>() {
            @Override
            public int compare(CardUI arg0, CardUI arg1) {

                if (arg0.getVirtualValue() > arg1.getVirtualValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        for (CardUI cardUI : my_list) {
            cardUI.setOwner(my_name);
        }
        return my_list;
    }

    public CardUI get_my_list_item(int index) {
        try {
            return get_my_list().get(index);
        } catch (Exception e) {
            System.out.println("##########ERROR Player get_my_list_item " + index + " size " + get_my_list().size());
            return null;
        }
    }

    public boolean remove_my_list_item(int index) {
        try {
            get_my_list().remove(index);
            return true;
        } catch (Exception e) {
            System.out.println("##########ERROR Player remove_my_list_item " + index);
            return false;
        }
    }

    public boolean remove_my_list_item(CardUI cardUI) {
        for (int i = 0; i < my_list.size(); i++) {
            if (my_list.get(i).getVirtualValue() == cardUI.getVirtualValue()) {
                my_list.remove(i);
                return true;
            }
        }

        return false;
    }

    public void add_play_list(CardUI cardUI) {
        if (cardUI.getOwner() == null) {
            System.out.println("##########add invalid card to " + my_name);
        }
        play_list.add(cardUI);
    }

    public void set_all_pickable() {
        for (CardUI cardUI : my_list) {
            cardUI.setPickable(true);
        }
    }

    public void reset_play_list() {
        this.play_list.clear();
        this.play_list = new ArrayList<CardUI>();
    }

    public Boolean is_start_first() {
        for (CardUI cardUI : my_list) {
            if (cardUI.isTwoOfClubs()) {
                return true;
            }
        }

        return false;
    }

    public void receive_3_card(List<CardUI> list) {
        // add to my_list
        my_list.addAll(list);

        // update card owner to me
        for (CardUI cardUI : my_list) {
            cardUI.setOwner(my_name);
            cardUI.setPickable(true);
        }
    }

    public void remove_3_card(List<CardUI> list) {

        int count = 0;

        // update card owner to me
        for (CardUI deleteCardUI : list) {
            for (int i = 0; i < my_list.size(); i++) {
                if (my_list.get(i).getVirtualValue() == deleteCardUI.getVirtualValue()) {
                    count++;
                    my_list.remove(i);
                    break;
                }
            }
        }

        if (count != 3) {
            System.out.println("ERROR Player remove_3_card " + count);
        }
    }

    public void update_rule(Boolean is_first_round, Boolean is_heart_broken) {

        // set all false
        for (CardUI cardUI : my_list) {
            cardUI.setPickable(false);
        }

        int pickable_count = 0;

        // start round
        if (play_list.size() == 0) {
            System.out.println("size--0");
            // two clubs, start this
            for (CardUI cardUI : my_list) {
                if (cardUI.isTwoOfClubs()) {
                    cardUI.setPickable(true);
                    return;
                }
            }

            // pick except hearts
            if (!is_heart_broken) {
                for (CardUI cardUI : my_list) {
                    if (!cardUI.isHearts()) {
                        cardUI.setPickable(true);
                        System.out.print("<");
                        pickable_count++;
                    }
                }
            }

            // if first, expect Q of S too
            if (is_first_round) {
                for (CardUI cardUI : my_list) {
                    if (cardUI.isQueenOfSpades()) {
                        cardUI.setPickable(false);
                        pickable_count--;
                    }
                }
            }

        } else {
            System.out.println("size!-0");
            for (CardUI cardUI : my_list) {
                try {
                    if (cardUI.getSuit().equals(play_list.get(0).getSuit())) {
                        cardUI.setPickable(true);
                        pickable_count++;
                    }
                } catch (NullPointerException exception) {
                    System.out.println("########## player " + my_name + " error update_rule");
                }
            }
        }

        // break hearts :(
        if (pickable_count == 0) {
            // free all :)
            for (CardUI cardUI : my_list) {
                cardUI.setPickable(true);
            }
        }
    }

}
