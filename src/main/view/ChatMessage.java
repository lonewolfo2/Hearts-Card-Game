package main.view;

/**
 *
 * @author lonewolf
 */
import java.io.*;

public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int MESSAGE = 0;
    public static final int WHOISIN = 1;
    public static final int LOGOUT = 2;
    public static final int GETCARD = 3;
    public static final int SETID = 4;
    public static final int SETNAME = 5;

    public static final int PICKEDCARD = 6;
    public static final int PICK3 = 7;// #A#1#2@...@...@
    public static final int PICK1 = 8;

    public static final int NOTI = 9;
    public static final int SCORE = 10;
    public static final int WHOPASS = 11; // block else "3#A#B#C#D" , "1#A"

    private int type;
    private String message;

    public ChatMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
