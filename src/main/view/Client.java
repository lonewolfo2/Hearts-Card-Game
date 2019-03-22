package main.view;

/**
 *
 * @author lonewolf
 */
import java.net.*;

import java.io.*;

public class Client {

    // for I/O
    private ObjectInputStream sInput; // to read from the socket
    private ObjectOutputStream sOutput; // to write on the socket
    private Socket socket;

    // if I use a GUI or not
    private OnlineUI onlineUI;

    // server, port and user name
    private String server, username;
    private int port;

    // Constructor call when used from a GUI in console mode the
    Client(String server, int port, String username, OnlineUI onlineUI) {
        this.server = server;
        this.port = port;
        this.username = username;
        this.onlineUI = onlineUI;
    }

    // start client
    public boolean start() {
        // try to connect to the server
        try {
            socket = new Socket(server, port);
        } // if it failed not much I can so
        catch (Exception ec) {
            displayMessage("(e) Error connecting to server");
            return false;
        }

        String message = "(i) Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        displayMessage(message);

        // creating both Data Stream
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eIO) {
            displayMessage("(e) Exception creating new Input/output Streams");
            return false;
        }

        // creates the Thread to listen from the server
        new ListenFromServer().start();
        // Send our user name to the server this is the only message that we
        // will send as a String. All other messages will be ChatMessage objects
        try {
            sOutput.writeObject(username);
        } catch (IOException eIO) {
            displayMessage("(e) Exception doing login");
            disconnect();
            return false;
        }

        // success we inform the caller that it worked
        return true;
    }

    // send message to UI
    private void displayMessage(String message) {
        onlineUI.appendMessage(message);
    }

    // send message to UI
    private void displayMessage(ChatMessage message) {
        if (message.getType() == ChatMessage.MESSAGE) {
            displayMessage(message.getMessage());
        } else if (message.getType() == ChatMessage.SETID) {
            onlineUI.my_id(message.getMessage());
        } else if (message.getType() == ChatMessage.SETNAME) {
            onlineUI.setName(message.getMessage());
        } else if (message.getType() == ChatMessage.GETCARD) {
            onlineUI.addToMyList(message.getMessage());
        } else if (message.getType() == ChatMessage.WHOPASS) {
            onlineUI.setPassMode(message.getMessage());
        } else if (message.getType() == ChatMessage.PICKEDCARD) {
            onlineUI.addToPlayList(message.getMessage());
        } else if (message.getType() == ChatMessage.SCORE) {
            onlineUI.drawScore(message.getMessage());
        }
    }

    // send a message to the server
    void sendMessage(String message) {
        sendMessage(new ChatMessage(ChatMessage.MESSAGE, message));
    }

    // send a message to the server
    void sendMessage(ChatMessage message) {
        try {
            sOutput.writeObject(message);
            System.out.println("client sent mes " + message.getType() + " : " + message.getMessage());
        } catch (IOException e) {
            displayMessage("(e) Exception writing to server");
        }
    }

    // close the Input/Output streams and disconnect
    private void disconnect() {
        try {
            if (sInput != null) {
                sInput.close();
            }
            if (sOutput != null) {
                sOutput.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {

        }

        onlineUI.connectionFailed();
    }

    // wait for the message from the server and append them to the UI
    class ListenFromServer extends Thread {

        public void run() {
            while (true) {
                try {
                    ChatMessage message = (ChatMessage) sInput.readObject();
                    displayMessage(message);
                } catch (IOException e) {
                    displayMessage("(i) Server has close the connection");
                    onlineUI.connectionFailed();
                    break;
                } catch (ClassNotFoundException classNotFoundException) {
                }
            }
        }
    }
}
