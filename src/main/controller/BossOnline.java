package main.controller;

import java.util.*;
import java.io.*;
import java.net.*;
import main.constant.GAME_MODE;
import main.model.Card;
import main.model.CardUI;
import main.model.Deck52;

public class BossOnline {

   final int max_score = 100;
   final int max_user = 4;

    private List<CardUI> play_list = new ArrayList<CardUI>();
    private List<UserOnline> user_list = new ArrayList<UserOnline>();
    private List<Integer> score_list = new ArrayList<Integer>();
    private List<Integer> total_score_list = new ArrayList<Integer>();

    private Boolean is_heart_broken;
    private Boolean is_first_round;

    private int pass_card_to; // 0 left 1 right 2 across 3 nothing
    private String who_next; // A B C D

    private GAME_MODE pick_card_mode;
    private int passed_card_user_count;

    public String getScore() {
        String result = "";

        for (int i = 0; i < score_list.size(); i++) {
            result = result + score_list.get(i) + "#";
        }

        for (int i = 0; i < total_score_list.size(); i++) {
            result = result + total_score_list.get(i) + "#";
        }

        return result;
    }

    public List<CardUI> get_my_list(String id) {
        if (pick_card_mode() == GAME_MODE.PASS_3_CARD) {
            for (UserOnline userOnline : user_list) {
                if (userOnline.name().equals(id)) {
                    userOnline.set_all_pickable();
                    return userOnline.get_my_list();
                }
            }
        } else {
            for (UserOnline userOnline : user_list) {
                if (userOnline.name().equals(id)) {
                    userOnline.update_rule(is_first_round, is_heart_broken);
                    return userOnline.get_my_list();
                }
            }
        }

        System.out.println("BossOnline get_my_list return null");
        return null;
    }

    public GAME_MODE pick_card_mode() {
        return pick_card_mode;
    }

    public List<CardUI> get_play_list() {
        return play_list;
    }

    public String who_next() {
        return this.who_next;
    }

    public String pass_card_to() {
        if (pass_card_to == 0) {
            return "Left";
        } else if (pass_card_to == 1) {
            return "Right";
        } else if (pass_card_to == 2) {
            return "Across";
        } else {
            return "";
        }
    }

    public void init_boss() {
        play_list = new ArrayList<CardUI>();
        user_list = new ArrayList<UserOnline>();
        score_list = new ArrayList<Integer>();
        total_score_list = new ArrayList<Integer>();

        for (int i = 0; i < max_user; i++) {
            user_list.add(new UserOnline());
            score_list.add(0);
            total_score_list.add(0);
        }

        user_list.get(0).name("A");
        user_list.get(1).name("B");
        user_list.get(2).name("C");
        user_list.get(3).name("D");

        // pass to left
        pass_card_to = 0;
    }

    public void new_game() {
        is_heart_broken = false;
        is_first_round = true;

        //
        if (pass_card_to != 3) {
            pick_card_mode = GAME_MODE.PASS_3_CARD;
            passed_card_user_count = 0;
        } else {
            pick_card_mode = GAME_MODE.PASS_1_CARD;
        }

        for (int i = 0; i < max_user; i++) {
            score_list.set(i, 0);
        }

        Deck52 deck52 = new Deck52();
        deck52.init52card();

        List<Card> tempList = new ArrayList<Card>();
        List<CardUI> tempListUI = new ArrayList<CardUI>();

        // user
        for (int i = 0; i < max_user; i++) {
            tempList.clear();
            tempListUI.clear();
            if (i == 0) {
                tempList.addAll(deck52.getDeck1());
            } else if (i == 1) {
                tempList.addAll(deck52.getDeck2());
            } else if (i == 2) {
                tempList.addAll(deck52.getDeck3());
            } else if (i == 3) {
                tempList.addAll(deck52.getDeck4());
            }

            for (Card item : tempList) {
                tempListUI.add(new CardUI(item, user_list.get(i).name()));
            }

            user_list.get(i).add_my_list(tempListUI);
        }
    }

    public int pass_3_card(String message) {

        List<CardUI> list = OnlineTools.message2list(message);
        if (list.size() != 3) {
            exit_game("ERROR BossOnline pass_card_index size != 3");
            return -1;
        }

        if (pick_card_mode == GAME_MODE.PASS_3_CARD) {
            pick_card_mode = GAME_MODE.PASS_1_CARD;
        }

        String id = list.get(0).getOwner();

        int from_index, to_index;

        from_index = OnlineTools.id2index(id);
        if (pass_card_to == 0) {
            to_index = OnlineTools.id2index(OnlineTools.increaseID(id));
        } else if (pass_card_to == 1) {
            to_index = OnlineTools.id2index(OnlineTools.decreaseID(id));
        } else if (pass_card_to == 2) {
            to_index = OnlineTools.id2index(OnlineTools.increaseID(OnlineTools.increaseID(id)));
        } else {
            exit_game("ERROR BossOnline pass_card_to invalid");
            return -1;
        }

        user_list.get(to_index).receive_3_card(list);
        user_list.get(from_index).remove_3_card(list);

        passed_card_user_count++;

        if (passed_card_user_count == 4) {
            return 4;
        }

        return from_index - 1;
    }

    public String start_game() {

        for (int i = 0; i < user_list.size(); i++) {
            if (user_list.get(i).is_start_first()) {

                who_next = OnlineTools.index2id(i);
                return who_next;
            }
        }

        return "";
    }

    public void new_round() {
        play_list.clear();
        for (UserOnline userOnline : user_list) {
            userOnline.reset_play_list();
        }
    }

    public void play_round(String message) {
        CardUI cardUI = OnlineTools.message2card(message);

        play_list.add(cardUI);
        for (UserOnline userOnline : user_list) {
            if (cardUI.getOwner().equals(userOnline.name())) {
                userOnline.remove_my_list_item(cardUI);
            }

            userOnline.add_play_list(cardUI);
        }

        who_next = OnlineTools.increaseID(who_next);
    }

    public Boolean complete_round() {

        if (play_list.size() > 4) {
            System.out.println("##########" + this.getClass().getName() + " complete_round play_list > 4");
        }

        if (play_list.size() == 4) {
            // is first round
            if (is_first_round) {
                is_first_round = false;
            }
            return true;
        } else {
            return false;
        }
    }

    public void calc_point() {

        // calculate ... point ...
        int point = 0;
        for (CardUI cardUI : play_list) {
            if (cardUI.isHearts()) {
                point += 1;
            }

            if (cardUI.isQueenOfSpades()) {
                point += 13;
            }
        }

        // who get point ?
        // temp = who start
        CardUI max_card = play_list.get(0);
        for (CardUI cardUI : play_list) {
            if (max_card.isSmaller(cardUI)) {
                max_card = cardUI;
            }
        }

        // check broken heart
        if (is_heart_broken) {

        } else {
            for (CardUI cardUI : play_list) {
                if (cardUI.isHearts()) {
                    is_heart_broken = true;
                }
            }
        }

        // set the value
        for (int i = 0; i < user_list.size(); i++) {
            if (max_card.getOwner().equals(user_list.get(i).name())) {
                score_list.set(i, score_list.get(i) + point);
                break;
            }
        }

        // set who's next
        who_next = max_card.getOwner();

        // reset play_list
        // call new_round()
    }

    public Boolean complete_game() {

        if (!user_list.get(0).get_my_list().isEmpty()) {
            return false;
        }

        pass_card_to = pass_card_to + 1;

        if (pass_card_to == 4) {
            pass_card_to = 0;
        }

        // check shoot the moon
        if (score_list.contains(26)) {
            for (int i = 0; i < max_user; i++) {
                total_score_list.set(i, 26);
                if (score_list.get(i) == 26) {
                    total_score_list.set(i, 0);
                }
            }
        } else {
            for (int i = 0; i < max_user; i++) {
                total_score_list.set(i, total_score_list.get(i) + score_list.get(i));
            }
        }

        score_list = new ArrayList<Integer>();
        score_list.add(0);
        score_list.add(0);
        score_list.add(0);
        score_list.add(0);

        return true;
    }

    public String over_game() {
        Boolean check = false;
        for (int i = 0; i < max_user; i++) {
            if (total_score_list.get(i) >= max_score) {
                check = true;
            }
        }

        if (check) {
            int min_score = 1000000;
            for (int i = 0; i < max_user; i++) {
                if (total_score_list.get(i) < min_score) {

                    min_score = total_score_list.get(i);
                    System.out.println("min score " + min_score);
                }
            }

            System.out.println("min score " + min_score);

            for (int i = 0; i < max_user; i++) {
                if (total_score_list.get(i) == min_score) {
                    return OnlineTools.index2id(i);
                }
            }
        }

        return "";
    }

    private void exit_game(String message) {
        System.out.println(message);
    }
}
