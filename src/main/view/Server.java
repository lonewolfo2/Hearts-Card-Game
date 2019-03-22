package main.view;

/**
 *
 * @author lonewolf
 */
import java.io.*;
import java.net.*;
import java.util.*;

import main.constant.GAME_MODE;
import main.controller.BossOnline;
import main.controller.OnlineTools;
import main.model.CardUI;

/*
 * The server that can be run both as a console application or a GUI
 */
public class Server {

    // controller
    private BossOnline my_boss;
    private int max_user = 3;

    // a unique ID for each connection
    private static int uniqueId;

    // an ArrayList to keep the list of the Client
    private ArrayList<ClientThread> clientList;

    // if I am in a GUI
    public OnlineUI onlineUI;

    // the port number to listen for connection
    private int port;

    // the boolean that will be turned of to stop the server
    private boolean keepGoing;

    //
    private String serverName;

    private void sendCard() {
        // send card... // rewrite in function
        String start_id = "A";
        // + 1 for server
        for (int i = 0; i < max_user + 1; i++) {
            List<CardUI> list = my_boss.get_my_list(start_id);
            broadcast(new ChatMessage(ChatMessage.GETCARD, OnlineTools.list2message(list)));
            start_id = OnlineTools.increaseID(start_id);
        }
        // // send card... // rewrite in function
        // String start_id = "A";
        // // + 1 for server
        // for (int i = 0; i < max_user + 1; i++) {
        // List<CardUI> list = my_boss.get_my_list(start_id);
        // broadcast(new ChatMessage(ChatMessage.GETCARDNUM, list.size() + ""));
        // // System.out.println(start_id + " ... " + list.size());
        // for (CardUI cardUI : list) {
        // broadcast(new ChatMessage(ChatMessage.GETCARD,
        // OnlineTools.card2message(cardUI)));
        // }
        // start_id = OnlineTools.increaseID(start_id);
        // }
    }

    // logic
    private void initGame() {
        onlineUI.my_id("A");
        clientList.get(0).my_id = "B";
        clientList.get(1).my_id = "C";
        clientList.get(2).my_id = "D";

        // distribution id
        for (int i = clientList.size(); --i >= 0;) {
            ClientThread ct = clientList.get(i);
            if (!ct.writeMsg(new ChatMessage(ChatMessage.SETID, ct.my_id))) {
                clientList.remove(i);
                displayMessage("(i) Disconnected Client " + ct.username + " removed from list.");
            }
        }

        // distribution name
        broadcast(new ChatMessage(ChatMessage.SETNAME, "A#" + serverName));
        for (int i = 0; i < clientList.size(); i++) {
            broadcast(new ChatMessage(ChatMessage.SETNAME, clientList.get(i).my_id + "#" + clientList.get(i).username));
        }

        // start
        my_boss = new BossOnline();
        my_boss.init_boss();
        my_boss.new_game();

        sendCard();

        if (my_boss.pick_card_mode() == GAME_MODE.PASS_3_CARD) {
            broadcast(new ChatMessage(ChatMessage.MESSAGE, "###Pass 3 card to " + my_boss.pass_card_to() + "###"));
            broadcast(new ChatMessage(ChatMessage.WHOPASS, "3#A#B#C#D"));
        } else {
            startGame();
        }
    }

    private void sendTurn(String turn) {
        broadcast(new ChatMessage(ChatMessage.WHOPASS, turn));

        int index = OnlineTools.id2index(turn);
        index--;
        if (index == -1) {
            broadcast(new ChatMessage(ChatMessage.MESSAGE, "###" + serverName + " turn###"));
        } else {
            broadcast(new ChatMessage(ChatMessage.MESSAGE, "###" + clientList.get(index).username + " turn###"));
        }
    }

    // logic
    private void startGame() {
        my_boss.new_round();
        String turn = my_boss.start_game();
        sendCard();
        sendTurn(turn);
    }

    // logic
    private void pickOne(String message) {
        System.out.println("server pick 1 " + message);
        my_boss.play_round(message);

        // picked
        broadcast(new ChatMessage(ChatMessage.PICKEDCARD, message));

        if (my_boss.complete_round()) {
            broadcast(new ChatMessage(ChatMessage.MESSAGE, "Complete 1 round"));
            my_boss.calc_point();
            broadcast(new ChatMessage(ChatMessage.SCORE, my_boss.getScore()));
            my_boss.new_round();
            String turn = my_boss.who_next();
            sendTurn(turn);

            if (my_boss.complete_game()) {

                String result = my_boss.over_game();
                if (result.equals("")) {
                    my_boss.new_game();
                    my_boss.start_game();
                    startGame();
                } else {
                    broadcast(new ChatMessage(ChatMessage.MESSAGE, "###END GAME###"));
                    int index = OnlineTools.id2index(result);

                    index--;
                    if (index == -1) {
                        broadcast(new ChatMessage(ChatMessage.MESSAGE, "###WINNER IS " + serverName + " ###"));
                    } else {
                        broadcast(new ChatMessage(ChatMessage.MESSAGE,
                                "###WINNER IS " + clientList.get(index).username + "###"));
                    }

                    broadcast(new ChatMessage(ChatMessage.WHOPASS, ""));

                    // new one
                    // initGame();
                }
            }

        } else {
            String turn = my_boss.who_next();
            sendTurn(turn);
        }

        // user card
        sendCard();
    }

    //
    public Server(int port, String serverName, OnlineUI onlineUI) {
        this.onlineUI = onlineUI;
        this.port = port;
        this.serverName = serverName;
        clientList = new ArrayList<ClientThread>();
    }

    // start server
    public void start() {
        keepGoing = true;
        // create socket server and wait for connection requests
        try {
            // the socket used by the server
            ServerSocket serverSocket = new ServerSocket(port);

            // infinite loop to wait for connections
            while (keepGoing) {
                // format message saying we are waiting
                // displayMessage("(i) Server waiting for Clients on port " +
                // port +
                // ".");

                if (clientList.size() == max_user) {
                    Socket socket = serverSocket.accept();
                    ClientThread t = new ClientThread(socket);
                    t.writeMsg("This server is full slot!!!");
                    t.writeMsg("You may want to:");
                    t.writeMsg(" - Logout to try other port");
                    t.writeMsg(" - Play offline");
                    t.writeMsg("Thank you");
                    continue;
                }

                Socket socket = serverSocket.accept(); // accept connection
                // if I was asked to stop
                if (!keepGoing) {
                    break;
                }
                ClientThread t = new ClientThread(socket); // make a thread of
                // it
                clientList.add(t); // save it in the ArrayList

                if (clientList.size() == max_user) {

                    initGame();

                }

                t.start();
            }
            // I was asked to stop
            try {
                serverSocket.close();
                for (int i = 0; i < clientList.size(); ++i) {
                    ClientThread tc = clientList.get(i);
                    try {
                        tc.sInput.close();
                        tc.sOutput.close();
                        tc.socket.close();
                    } catch (IOException ioE) {
                        // not much I can do
                    }
                }
            } catch (Exception e) {
                displayMessage("(e) Exception closing the server and clients");
            }
        } // something went bad
        catch (IOException e) {
            String message = "(e) Exception on new ServerSocket";
            displayMessage(message);
        }
    }

    // for the GUI to stop the server
    protected void stop() {
        keepGoing = false;
        // connect to myself as Client to exit statement
        // Socket socket = serverSocket.accept();
        try {
            Socket socket = new Socket("localhost", port);
            socket.close();
        } catch (Exception e) {
            // nothing I can really do
        }
    }

    // when need to do something
    public void displayMessage(ChatMessage message) {
        // System.out.println("server receive mes " + message.getType() + " - "
        // + message.getMessage());
        if (message.getType() == ChatMessage.MESSAGE) {
            onlineUI.appendMessage(message.getMessage());
        } else if (message.getType() == ChatMessage.SETNAME) {
            onlineUI.setName(message.getMessage());
        } else if (message.getType() == ChatMessage.GETCARD) {
            onlineUI.addToMyList(message.getMessage());
        } else if (message.getType() == ChatMessage.WHOPASS) {
            onlineUI.setPassMode(message.getMessage());
        } else if (message.getType() == ChatMessage.PICKEDCARD) {
            onlineUI.addToPlayList(message.getMessage());
        } else if (message.getType() == ChatMessage.PICK1) {
            pickOne(message.getMessage());
        } else if (message.getType() == ChatMessage.SCORE) {
            onlineUI.drawScore(message.getMessage());
        } else if (message.getType() == ChatMessage.PICK3) {
            int result = my_boss.pass_3_card(message.getMessage());
            if (result == 4) {
                System.out.println("STARTGAMESTARTGAMESTARTGAMESTARTGAMESTARTGAMESTARTGAMESTARTGAME");

                startGame();

            } else if (result >= -1) {
                if (result == -1) {
                    broadcast(new ChatMessage(ChatMessage.MESSAGE, "###" + serverName + " has passed 3 card###"));
                } else {
                    broadcast(new ChatMessage(ChatMessage.MESSAGE,
                            "###" + clientList.get(result).username + " has passed 3 card###"));
                }

            }

        }
    }

    // when only need display
    private void displayMessage(String message) {
        onlineUI.appendMessage(message);
    }

    public void sendMessage(ChatMessage message) {
        if (message.getType() == ChatMessage.WHOISIN) {
            displayMessage("List of the users connected");
            // scan clientList the users connected
            displayMessage(" - " + serverName);
            for (int i = 0; i < clientList.size(); ++i) {
                ClientThread ct = clientList.get(i);
                displayMessage(" - " + ct.username);
            }
        } else if (message.getType() == ChatMessage.MESSAGE) {
            broadcast(new ChatMessage(ChatMessage.MESSAGE, serverName + ": " + message.getMessage()));
        } else {
            broadcast(message);
        }
    }

    // to broadcast a message to all Clients
    private synchronized void broadcast(ChatMessage message) {

        // also send to server
        displayMessage(message);

        // we loop in reverse order in case we would have to remove a Client
        // because it has disconnected
        for (int i = clientList.size(); --i >= 0;) {
            ClientThread ct = clientList.get(i);
            // try to write to the Client if it fails remove it from the list
            if (!ct.writeMsg(message)) {
                clientList.remove(i);
                displayMessage("(i) Disconnected Client " + ct.username + " removed from list.");
            }
        }
    }

    // for a client who log off using the LOGOUT message
    synchronized void remove(int id) {
        // scan the array list until we found the Id
        for (int i = 0; i < clientList.size(); ++i) {
            ClientThread ct = clientList.get(i);
            // found it
            if (ct.id == id) {
                clientList.remove(i);
                return;
            }
        }
    }

    // One instance of this thread will run for each client
    class ClientThread extends Thread {

        // my unique id (easier for reconnection)
        int id;

        // the socket where to listen/talk
        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;

        // the user name of the Client
        String my_id;
        String username;

        // the only type of message a will receive
        ChatMessage cm;

        // Constructor
        ClientThread(Socket socket) {
            // a unique id
            id = ++uniqueId;
            this.socket = socket;
            /* Creating both Data Stream */
            // System.out.println("Thread trying to create Object Input/Output
            // Streams");
            try {
                // create output first
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput = new ObjectInputStream(socket.getInputStream());
                // read the user name
                username = (String) sInput.readObject();

                displayMessage("(i) " + username + " just connected.");
            } catch (IOException e) {
                displayMessage("(e) Exception creating new Streams");
                return;
            } // have to catch ClassNotFoundException
            // but I read a String, I am sure it will work
            catch (ClassNotFoundException e) {
            }
        }

        // what will run forever
        public void run() {
            // to loop until LOGOUT
            boolean keepGoing = true;
            while (keepGoing) {
                // read a String (which is an object)
                try {
                    cm = (ChatMessage) sInput.readObject();
                } catch (IOException e) {
                    displayMessage("(i) " + username + " maybe disconnected.");
                    break;
                } catch (ClassNotFoundException e2) {
                    break;
                }

                // Switch on the type of message receive
                switch (cm.getType()) {
                    case ChatMessage.MESSAGE:
                        broadcast(new ChatMessage(ChatMessage.MESSAGE, username + ": " + cm.getMessage()));
                        break;
                    case ChatMessage.LOGOUT:
                        displayMessage("(i) " + username + " disconnected.");
                        keepGoing = false;
                        break;
                    case ChatMessage.WHOISIN:
                        writeMsg("List of the users connected");
                        // scan clientList the users connected
                        writeMsg(" - " + serverName);
                        for (int i = 0; i < clientList.size(); ++i) {
                            ClientThread ct = clientList.get(i);
                            writeMsg(" - " + ct.username);
                        }
                        break;
                    default:
                        displayMessage(cm);
                        break;

                }
            }

            // remove myself from the arrayList containing the list of the
            // connected Clients
            remove(id);
            close();
        }

        // try to close everything
        private void close() {
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
        }

        // write a String to the Client output stream
        private boolean writeMsg(String message) {
            return writeMsg(new ChatMessage(ChatMessage.MESSAGE, message));
        }

        private boolean writeMsg(ChatMessage message) {
            // if Client is still connected send the message to it
            if (!socket.isConnected()) {
                close();
                return false;
            }
            // write the message to the stream
            try {
                sOutput.writeObject(message);
            } // if an error occurs, do not abort just inform the user
            catch (IOException e) {
                displayMessage("(e) Error sending message to " + username);
                displayMessage(e.toString());
            }
            return true;
        }
    }
}
