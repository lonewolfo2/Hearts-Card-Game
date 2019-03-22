package main.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import main.constant.GAME_MODE;
import main.controller.OnlineTools;
import main.model.CardUI;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author lonewolf
 */
public class OnlineUI {

    private List<CardUI> my_list = new ArrayList<CardUI>();
    private List<CardUI> play_list = new ArrayList<CardUI>();
    private Boolean waiting_user = true;
    private GAME_MODE pick_card_mode;
    private int picked_card_count;

    private JFrame frmHeartsGame;

    private List<JButton> listButton = new ArrayList<JButton>();
    private List<String> listMessages = new ArrayList<String>();
    private JScrollPane scrollPaneMessages;

    private JTextField textFieldMessage;
    private JList<String> listChat;

    private JPanel panelServer;
    private JButton btnLogin;
    private JButton btnStartServer;
    private JTextField textFieldPort;
    private JTextField textFieldAddress;
    private JTextField textFieldName;

    //
    private int card_width = 75;
    private int card_height = 108;
    private JButton btnAvatarA; // just for fun
    private JLabel lblNameA, lblScoreA, lblNameB, lblScoreB, lblNameC, lblScoreC, lblNameD, lblScoreD, lblNoti;
    private JButton btnCardA, btnCardB, btnCardC, btnCardD;

    private String my_id; // A B C D
    private Client client;
    private Server server;

    /**
     * *
     * if is_server -> disable login button else -> disable start server button
     *
     * if (is_server) if (is_connected) -> stop server ...
     */
    private Boolean is_server = false;
    private Boolean is_connected = false;

    private SpringLayout springLayout;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OnlineUI window = new OnlineUI();
                    window.frmHeartsGame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public OnlineUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmHeartsGame = new JFrame();
        frmHeartsGame.setTitle("Hearts Game ");
        frmHeartsGame.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 15));
        frmHeartsGame.getContentPane().setBackground(SystemColor.desktop);
        frmHeartsGame.setBackground(SystemColor.desktop);
        frmHeartsGame.setResizable(false);
        frmHeartsGame.setBounds(50, 50, 800, 650);
        frmHeartsGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        springLayout = new SpringLayout();
        frmHeartsGame.getContentPane().setLayout(springLayout);

        // start game UI
        initA();
        initB();
        initC();
        initD();

        initNoti();

        initChatPanel();
        initServerPanel();

        // drawNoti(null);
        btnAvatarA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        btnStartServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (is_connected) {
                    stopServer();
                } else {
                    if (checkValidInput()) {
                        startServer();
                    }
                }
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (is_connected) {
                    logout();
                } else {
                    if (checkValidInput()) {
                        login();
                    }
                }
            }
        });
    }

    public String my_id() {
        return my_id;
    }

    public void my_id(String my_id) {
        this.my_id = my_id;
    }

    private Boolean checkValidInput() {

        String port_string = textFieldPort.getText().trim();
        if (port_string.equals("")) {
            JOptionPane.showMessageDialog(frmHeartsGame, "Your port is empty", "WARNING", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        int port_int;
        try {
            port_int = Integer.parseInt(port_string);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frmHeartsGame, "Your port is not a number", "WARNING",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (port_int < 0 || port_int >= 65535) {
            JOptionPane.showMessageDialog(frmHeartsGame, "Your port is invalid", "WARNING",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String name = textFieldName.getText().trim();
        if (name.equals("")) {
            JOptionPane.showMessageDialog(frmHeartsGame, "Your name is empty", "WARNING", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (name.contains("#") || name.contains("@")) {
            JOptionPane.showMessageDialog(frmHeartsGame, "Your name cant contain '#' or '@'", "WARNING",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    public Boolean waiting_user() {
        return waiting_user;
    }

    public void waiting_user(Boolean waiting_user) {
        this.waiting_user = waiting_user;
    }
    // end for constructor

    // start for UI
    private void initChatPanel() {
        scrollPaneMessages = new JScrollPane();
        springLayout.putConstraint(SpringLayout.WEST, scrollPaneMessages, -240, SpringLayout.EAST,
                frmHeartsGame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, scrollPaneMessages, 350, SpringLayout.NORTH,
                frmHeartsGame.getContentPane());
        scrollPaneMessages.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        springLayout.putConstraint(SpringLayout.EAST, scrollPaneMessages, -10, SpringLayout.EAST,
                frmHeartsGame.getContentPane());
        frmHeartsGame.getContentPane().add(scrollPaneMessages);

        listChat = new JList<String>();
        scrollPaneMessages.setViewportView(listChat);

        textFieldMessage = new JTextField();
        springLayout.putConstraint(SpringLayout.NORTH, textFieldMessage, 10, SpringLayout.SOUTH, scrollPaneMessages);
        springLayout.putConstraint(SpringLayout.WEST, textFieldMessage, 0, SpringLayout.WEST, scrollPaneMessages);
        springLayout.putConstraint(SpringLayout.EAST, textFieldMessage, 0, SpringLayout.EAST, scrollPaneMessages);
        textFieldMessage.setText("Write message and press enter");
        textFieldMessage.setForeground(Color.GRAY);
        textFieldMessage.setEnabled(false);
        frmHeartsGame.getContentPane().add(textFieldMessage);
        textFieldMessage.setColumns(10);

        textFieldMessage.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textFieldMessage.getText().equals("Write message and press enter")) {
                    textFieldMessage.setText("");
                    textFieldMessage.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textFieldMessage.getText().isEmpty()) {
                    textFieldMessage.setForeground(Color.GRAY);
                    textFieldMessage.setText("Write message and press enter");
                }
            }
        });

        textFieldMessage.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {

                if (e.getKeyChar() == '\n') {
                    String mes = textFieldMessage.getText();
                    if (mes != null && !mes.equals("")) {
                        if (is_server) {
                            if (mes.equals("Who?")) {
                                server.sendMessage(new ChatMessage(ChatMessage.WHOISIN, mes));
                            } else {
                                server.sendMessage(new ChatMessage(ChatMessage.MESSAGE, mes));
                            }
                        } else {
                            if (mes.equals("Who?")) {
                                client.sendMessage(new ChatMessage(ChatMessage.WHOISIN, mes));
                            } else {
                                client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, mes));
                            }
                        }
                        textFieldMessage.setText("");
                    }
                }

            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });

    }

    private void initServerPanel() {
        panelServer = new JPanel();
        springLayout.putConstraint(SpringLayout.NORTH, scrollPaneMessages, 10, SpringLayout.SOUTH, panelServer);
        springLayout.putConstraint(SpringLayout.WEST, panelServer, -240, SpringLayout.EAST,
                frmHeartsGame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, panelServer, -10, SpringLayout.EAST,
                frmHeartsGame.getContentPane());
        panelServer.setBorder(new LineBorder(new Color(0, 0, 0)));
        panelServer.setBackground(SystemColor.activeCaption);
        springLayout.putConstraint(SpringLayout.NORTH, panelServer, 10, SpringLayout.NORTH,
                frmHeartsGame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, panelServer, 140, SpringLayout.NORTH,
                frmHeartsGame.getContentPane());
        frmHeartsGame.getContentPane().add(panelServer);
        SpringLayout sl_panelServer = new SpringLayout();
        panelServer.setLayout(sl_panelServer);

        JLabel lblAddress = new JLabel("Address");
        panelServer.add(lblAddress);

        textFieldAddress = new JTextField();
        textFieldAddress.setEditable(false);
        textFieldAddress.setText("localhost");
        sl_panelServer.putConstraint(SpringLayout.NORTH, lblAddress, 0, SpringLayout.NORTH, textFieldAddress);
        sl_panelServer.putConstraint(SpringLayout.SOUTH, lblAddress, 0, SpringLayout.SOUTH, textFieldAddress);
        sl_panelServer.putConstraint(SpringLayout.EAST, lblAddress, -10, SpringLayout.WEST, textFieldAddress);
        sl_panelServer.putConstraint(SpringLayout.NORTH, textFieldAddress, 10, SpringLayout.NORTH, panelServer);
        sl_panelServer.putConstraint(SpringLayout.WEST, textFieldAddress, -100, SpringLayout.EAST, panelServer);
        sl_panelServer.putConstraint(SpringLayout.EAST, textFieldAddress, -10, SpringLayout.EAST, panelServer);
        panelServer.add(textFieldAddress);
        textFieldAddress.setColumns(10);

        JLabel lblPort = new JLabel("Port ");
        panelServer.add(lblPort);

        textFieldPort = new JTextField();
        textFieldPort.setText("1500");
        sl_panelServer.putConstraint(SpringLayout.NORTH, lblPort, 0, SpringLayout.NORTH, textFieldPort);
        sl_panelServer.putConstraint(SpringLayout.SOUTH, lblPort, 0, SpringLayout.SOUTH, textFieldPort);
        sl_panelServer.putConstraint(SpringLayout.EAST, lblPort, -10, SpringLayout.WEST, textFieldPort);
        sl_panelServer.putConstraint(SpringLayout.NORTH, textFieldPort, 10, SpringLayout.SOUTH, textFieldAddress);
        sl_panelServer.putConstraint(SpringLayout.WEST, textFieldPort, 0, SpringLayout.WEST, textFieldAddress);
        sl_panelServer.putConstraint(SpringLayout.EAST, textFieldPort, -10, SpringLayout.EAST, panelServer);
        panelServer.add(textFieldPort);
        textFieldPort.setColumns(10);

        JLabel lblName = new JLabel("Name");
        panelServer.add(lblName);

        textFieldName = new JTextField();
        textFieldName.setText("Player A");
        sl_panelServer.putConstraint(SpringLayout.NORTH, lblName, 0, SpringLayout.NORTH, textFieldName);
        sl_panelServer.putConstraint(SpringLayout.SOUTH, lblName, 0, SpringLayout.SOUTH, textFieldName);
        sl_panelServer.putConstraint(SpringLayout.EAST, lblName, -10, SpringLayout.WEST, textFieldName);
        sl_panelServer.putConstraint(SpringLayout.NORTH, textFieldName, 10, SpringLayout.SOUTH, textFieldPort);
        sl_panelServer.putConstraint(SpringLayout.WEST, textFieldName, 0, SpringLayout.WEST, textFieldAddress);
        sl_panelServer.putConstraint(SpringLayout.EAST, textFieldName, 0, SpringLayout.EAST, textFieldAddress);
        panelServer.add(textFieldName);
        textFieldName.setColumns(10);

        btnStartServer = new JButton("Start server");
        panelServer.add(btnStartServer);

        btnLogin = new JButton("Log in");
        sl_panelServer.putConstraint(SpringLayout.NORTH, btnLogin, 10, SpringLayout.SOUTH, textFieldName);
        sl_panelServer.putConstraint(SpringLayout.NORTH, btnStartServer, 0, SpringLayout.NORTH, btnLogin);
        sl_panelServer.putConstraint(SpringLayout.EAST, btnStartServer, -10, SpringLayout.WEST, btnLogin);
        sl_panelServer.putConstraint(SpringLayout.EAST, btnLogin, -10, SpringLayout.EAST, panelServer);
        panelServer.add(btnLogin);
    }

    private void initA() {

        btnAvatarA = new JButton();
        springLayout.putConstraint(SpringLayout.NORTH, btnAvatarA, 400, SpringLayout.NORTH,
                frmHeartsGame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, btnAvatarA, 10, SpringLayout.WEST,
                frmHeartsGame.getContentPane());
        btnAvatarA.setFocusPainted(false);
        try {
            ImageIcon img = new ImageIcon(OnlineUI.class.getResource("/resource/avatar_robot.png"));
            btnAvatarA.setIcon(new ImageIcon(img.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH)));
        } catch (NullPointerException e) {
            System.out.println("Image not found");
        }

        frmHeartsGame.getContentPane().add(btnAvatarA);

        lblNameA = new JLabel("You");
        springLayout.putConstraint(SpringLayout.NORTH, lblNameA, 5, SpringLayout.SOUTH, btnAvatarA);
        springLayout.putConstraint(SpringLayout.WEST, lblNameA, 0, SpringLayout.WEST, btnAvatarA);
        springLayout.putConstraint(SpringLayout.EAST, lblNameA, 0, SpringLayout.EAST, btnAvatarA);
        lblNameA.setHorizontalAlignment(SwingConstants.CENTER);
        lblNameA.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblNameA.setForeground(Color.CYAN);
        lblNameA.setFont(new Font("Tahoma", Font.PLAIN, 15));
        frmHeartsGame.getContentPane().add(lblNameA);

        lblScoreA = new JLabel();
        lblScoreA.setText("0/0");
        lblScoreA.setHorizontalAlignment(SwingConstants.CENTER);
        lblScoreA.setBorder(new LineBorder(Color.ORANGE, 3, true));
        lblScoreA.setForeground(new Color(255, 153, 255));
        lblScoreA.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblScoreA.setBackground(Color.GRAY);
        springLayout.putConstraint(SpringLayout.WEST, lblScoreA, 0, SpringLayout.WEST, btnAvatarA);
        springLayout.putConstraint(SpringLayout.NORTH, lblScoreA, 5, SpringLayout.SOUTH, lblNameA);
        springLayout.putConstraint(SpringLayout.EAST, lblScoreA, 0, SpringLayout.EAST, btnAvatarA);
        frmHeartsGame.getContentPane().add(lblScoreA);

        btnCardA = new JButton();

        int cardA_top = 280;
        int cardA_left = 210;
        springLayout.putConstraint(SpringLayout.NORTH, btnCardA, cardA_top, SpringLayout.NORTH,
                frmHeartsGame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, btnCardA, cardA_top + card_height, SpringLayout.NORTH,
                frmHeartsGame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, btnCardA, cardA_left, SpringLayout.WEST,
                frmHeartsGame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, btnCardA, cardA_left + card_width, SpringLayout.WEST,
                frmHeartsGame.getContentPane());

        btnCardA.setVisible(false);

        frmHeartsGame.getContentPane().add(btnCardA);
    }

    private void initB() {

        JButton btnAvatarB = new JButton();
        springLayout.putConstraint(SpringLayout.NORTH, btnAvatarB, 170, SpringLayout.NORTH,
                frmHeartsGame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, btnAvatarB, 10, SpringLayout.WEST,
                frmHeartsGame.getContentPane());
        btnAvatarB.setFocusPainted(false);
        try {
            ImageIcon img = new ImageIcon(OnlineUI.class.getResource("/resource/avatar_robot.png"));
            btnAvatarB.setIcon(new ImageIcon(img.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH)));
        } catch (NullPointerException e) {
            System.out.println("Image not found");

        }

        frmHeartsGame.getContentPane().add(btnAvatarB);

        lblNameB = new JLabel("Robot B");
        springLayout.putConstraint(SpringLayout.EAST, lblNameB, 0, SpringLayout.EAST, btnAvatarB);
        springLayout.putConstraint(SpringLayout.WEST, lblNameB, 0, SpringLayout.WEST, btnAvatarB);
        springLayout.putConstraint(SpringLayout.NORTH, lblNameB, 5, SpringLayout.SOUTH, btnAvatarB);
        lblNameB.setHorizontalAlignment(SwingConstants.CENTER);
        lblNameB.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNameB.setForeground(Color.CYAN);
        lblNameB.setFont(new Font("Tahoma", Font.PLAIN, 15));
        frmHeartsGame.getContentPane().add(lblNameB);

        lblScoreB = new JLabel();
        lblScoreB.setHorizontalAlignment(SwingConstants.CENTER);
        lblScoreB.setBorder(new LineBorder(Color.ORANGE, 3, true));
        lblScoreB.setForeground(new Color(255, 153, 255));
        lblScoreB.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblScoreB.setText("0/0");
        lblScoreB.setBackground(Color.GRAY);
        springLayout.putConstraint(SpringLayout.WEST, lblScoreB, 0, SpringLayout.WEST, btnAvatarB);
        springLayout.putConstraint(SpringLayout.EAST, lblScoreB, 0, SpringLayout.EAST, btnAvatarB);
        springLayout.putConstraint(SpringLayout.NORTH, lblScoreB, 5, SpringLayout.SOUTH, lblNameB);
        frmHeartsGame.getContentPane().add(lblScoreB);

        btnCardB = new JButton();

        int cardB_top = 5;
        int cardB_left = 20;

        springLayout.putConstraint(SpringLayout.WEST, btnCardB, cardB_left, SpringLayout.EAST, btnAvatarB);
        springLayout.putConstraint(SpringLayout.EAST, btnCardB, cardB_left + card_width, SpringLayout.EAST, btnAvatarB);
        springLayout.putConstraint(SpringLayout.NORTH, btnCardB, cardB_top, SpringLayout.NORTH, btnAvatarB);
        springLayout.putConstraint(SpringLayout.SOUTH, btnCardB, cardB_top + card_height, SpringLayout.NORTH,
                btnAvatarB);

        btnCardB.setVisible(false);

        frmHeartsGame.getContentPane().add(btnCardB);
    }

    private void initC() {

        JButton btnAvatarC = new JButton();
        springLayout.putConstraint(SpringLayout.NORTH, btnAvatarC, 10, SpringLayout.NORTH,
                frmHeartsGame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, btnAvatarC, 160, SpringLayout.WEST,
                frmHeartsGame.getContentPane());
        btnAvatarC.setFocusPainted(false);
        try {
            ImageIcon img = new ImageIcon(OnlineUI.class.getResource("/resource/avatar_robot.png"));
            btnAvatarC.setIcon(new ImageIcon(img.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH)));
        } catch (NullPointerException e) {
            System.out.println("Image not found");

        }

        frmHeartsGame.getContentPane().add(btnAvatarC);
        lblScoreC = new JLabel();
        lblScoreC.setHorizontalAlignment(SwingConstants.CENTER);
        lblScoreC.setBorder(new LineBorder(Color.ORANGE, 3, true));
        lblScoreC.setForeground(new Color(255, 153, 255));
        lblScoreC.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblScoreC.setText("0/0");
        lblScoreC.setBackground(Color.GRAY);
        springLayout.putConstraint(SpringLayout.WEST, lblScoreC, 10, SpringLayout.EAST, btnAvatarC);
        springLayout.putConstraint(SpringLayout.SOUTH, lblScoreC, 0, SpringLayout.SOUTH, btnAvatarC);
        springLayout.putConstraint(SpringLayout.EAST, lblScoreC, 100, SpringLayout.EAST, btnAvatarC);
        frmHeartsGame.getContentPane().add(lblScoreC);

        lblNameC = new JLabel("Robot C");
        springLayout.putConstraint(SpringLayout.NORTH, lblScoreC, 5, SpringLayout.SOUTH, lblNameC);
        springLayout.putConstraint(SpringLayout.NORTH, lblNameC, 5, SpringLayout.NORTH, btnAvatarC);
        springLayout.putConstraint(SpringLayout.WEST, lblNameC, 0, SpringLayout.WEST, lblScoreC);
        lblNameC.setHorizontalAlignment(SwingConstants.CENTER);
        lblNameC.setAlignmentX(Component.CENTER_ALIGNMENT);
        springLayout.putConstraint(SpringLayout.EAST, lblNameC, 0, SpringLayout.EAST, lblScoreC);
        lblNameC.setForeground(Color.CYAN);
        lblNameC.setFont(new Font("Tahoma", Font.PLAIN, 15));
        frmHeartsGame.getContentPane().add(lblNameC);

        btnCardC = new JButton();

        int cardC_top = 10;
        int cardC_left = 50;

        springLayout.putConstraint(SpringLayout.WEST, btnCardC, cardC_left, SpringLayout.WEST, btnAvatarC);
        springLayout.putConstraint(SpringLayout.EAST, btnCardC, cardC_left + card_width, SpringLayout.WEST, btnAvatarC);
        springLayout.putConstraint(SpringLayout.NORTH, btnCardC, cardC_top, SpringLayout.SOUTH, btnAvatarC);
        springLayout.putConstraint(SpringLayout.SOUTH, btnCardC, cardC_top + card_height, SpringLayout.SOUTH,
                btnAvatarC);
        btnCardC.setVisible(false);

        frmHeartsGame.getContentPane().add(btnCardC);
    }

    private void initD() {

        btnCardD = new JButton();
        springLayout.putConstraint(SpringLayout.WEST, btnCardD, 310, SpringLayout.WEST, frmHeartsGame.getContentPane());

        springLayout.putConstraint(SpringLayout.EAST, btnCardD, 385, SpringLayout.WEST, frmHeartsGame.getContentPane());

        int cardD_top = 175;
        // int cardD_left = 450;

        springLayout.putConstraint(SpringLayout.NORTH, btnCardD, cardD_top, SpringLayout.NORTH,
                frmHeartsGame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, btnCardD, cardD_top + card_height, SpringLayout.NORTH,
                frmHeartsGame.getContentPane());

        btnCardD.setVisible(false);

        frmHeartsGame.getContentPane().add(btnCardD);

        JButton btnAvatarD = new JButton();
        springLayout.putConstraint(SpringLayout.NORTH, btnAvatarD, -5, SpringLayout.NORTH, btnCardD);
        springLayout.putConstraint(SpringLayout.WEST, btnAvatarD, 20, SpringLayout.EAST, btnCardD);
        btnAvatarD.setFocusPainted(false);
        try {
            ImageIcon img = new ImageIcon(OnlineUI.class.getResource("/resource/avatar_robot.png"));
            btnAvatarD.setIcon(new ImageIcon(img.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH)));
        } catch (NullPointerException e) {
            System.out.println("Image not found");

        }

        frmHeartsGame.getContentPane().add(btnAvatarD);

        lblNameD = new JLabel("Robot D");
        springLayout.putConstraint(SpringLayout.NORTH, lblNameD, 5, SpringLayout.SOUTH, btnAvatarD);
        springLayout.putConstraint(SpringLayout.WEST, lblNameD, 0, SpringLayout.WEST, btnAvatarD);
        springLayout.putConstraint(SpringLayout.EAST, lblNameD, 0, SpringLayout.EAST, btnAvatarD);
        lblNameD.setHorizontalAlignment(SwingConstants.CENTER);
        lblNameD.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNameD.setForeground(Color.CYAN);
        lblNameD.setFont(new Font("Tahoma", Font.PLAIN, 15));
        frmHeartsGame.getContentPane().add(lblNameD);

        lblScoreD = new JLabel();
        lblScoreD.setHorizontalAlignment(SwingConstants.CENTER);
        lblScoreD.setBorder(new LineBorder(Color.ORANGE, 3, true));
        lblScoreD.setForeground(new Color(255, 153, 255));
        lblScoreD.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblScoreD.setText("0/0");
        lblScoreD.setBackground(Color.GRAY);
        springLayout.putConstraint(SpringLayout.NORTH, lblScoreD, 5, SpringLayout.SOUTH, lblNameD);
        springLayout.putConstraint(SpringLayout.WEST, lblScoreD, 0, SpringLayout.WEST, btnAvatarD);
        springLayout.putConstraint(SpringLayout.EAST, lblScoreD, 0, SpringLayout.EAST, btnAvatarD);
        frmHeartsGame.getContentPane().add(lblScoreD);
    }

    private void initNoti() {
        lblNoti = new JLabel("");
        springLayout.putConstraint(SpringLayout.WEST, lblNoti, 15, SpringLayout.WEST, frmHeartsGame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, lblNoti, -10, SpringLayout.NORTH, btnAvatarA);
        lblNoti.setForeground(new Color(224, 255, 255));
        lblNoti.setFont(new Font("Tahoma", Font.PLAIN, 15));
        frmHeartsGame.getContentPane().add(lblNoti);
    }
    // end for UI

    // start drawing
    private void drawCard() {
        int card_left = 120;
        int card_top = 400;

        float card_index = -1;

        // remove all UI card
        for (JButton item : listButton) {
            frmHeartsGame.getContentPane().remove(item);
        }
        listButton.clear();

        for (int i = 0; i < my_list.size(); i++) {

            card_index++;
            if (i == 7) {
                card_top = card_top + card_height;
                card_index = 0.5f;
            }

            CardUI curCardUI = my_list.get(i);

            JButton btnCard = new JButton();
            btnCard.setFocusPainted(false);
            btnCard.setName(i + "_" + "font");

            springLayout.putConstraint(SpringLayout.WEST, btnCard, (int) (card_left + card_index * card_width),
                    SpringLayout.WEST, frmHeartsGame.getContentPane());
            springLayout.putConstraint(SpringLayout.EAST, btnCard, (int) (card_left + (card_index + 1) * card_width),
                    SpringLayout.WEST, frmHeartsGame.getContentPane());
            springLayout.putConstraint(SpringLayout.NORTH, btnCard, card_top, SpringLayout.NORTH,
                    frmHeartsGame.getContentPane());
            springLayout.putConstraint(SpringLayout.SOUTH, btnCard, card_top + card_height, SpringLayout.NORTH,
                    frmHeartsGame.getContentPane());
            try {
                ImageIcon img = new ImageIcon(OnlineUI.class
                        .getResource("/resource/card/" + curCardUI.getValue() + "_" + curCardUI.getSuit() + ".png"));
                btnCard.setIcon(new ImageIcon(
                        img.getImage().getScaledInstance(card_width, card_height, java.awt.Image.SCALE_SMOOTH)));

            } catch (NullPointerException e) {
                System.out.println("Image not found");
                btnCard.setText("Not found");
            }

            if (curCardUI.getPickable() != true) {
                btnCard.setEnabled(false);
            }

            btnCard.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // System.out.println(btnCard.getName());

                    // if not waiting_user -> block UI
                    if (waiting_user) {
                        String[] data = btnCard.getName().split("_");
                        int index = Integer.parseInt(data[0]);
                        String face = data[1];

                        if (pick_card_mode == GAME_MODE.PASS_3_CARD) {

                            // if is font, flip back
                            if (face.equals("font")) {
                                btnCard.setName(index + "_back");
                                picked_card_count++;
                                ImageIcon _img = new ImageIcon(OnlineUI.class.getResource("/resource/card_back.png"));
                                btnCard.setIcon(new ImageIcon(_img.getImage().getScaledInstance(card_width, card_height,
                                        java.awt.Image.SCALE_SMOOTH)));

                                if (picked_card_count == 3) {
                                    passThreeCard();
                                }

                            } else {
                                btnCard.setName(index + "_font");
                                picked_card_count--;

                                CardUI changCardUI = my_list.get(index);

                                ImageIcon _img = new ImageIcon(OnlineUI.class.getResource("/resource/card/"
                                        + changCardUI.getValue() + "_" + changCardUI.getSuit() + ".png"));
                                btnCard.setIcon(new ImageIcon(_img.getImage().getScaledInstance(card_width, card_height,
                                        java.awt.Image.SCALE_SMOOTH)));
                            }
                        } else {
                            // pass card ...
                            btnCard.setEnabled(false);
                            passOneCard(index);
                        }
                    }
                }
            });

            listButton.add(btnCard);
            frmHeartsGame.getContentPane().add(btnCard);
        }

        frmHeartsGame.validate();
        frmHeartsGame.repaint();
    } // end method

    private void draw4Card() {

        btnCardA.setVisible(false);
        btnCardB.setVisible(false);
        btnCardC.setVisible(false);
        btnCardD.setVisible(false);

        try {
            for (int i = 0; i < play_list.size(); i++) {
                CardUI curCardUI = play_list.get(i);

                // try {
                // curCardUI.getOwner().equals("A");
                // } catch (NullPointerException exception) {
                // System.out.println("#####ERROR#####");
                // System.out.println("play_list.get(0)" + play_list.get(i));
                // System.out.println("play_list.size" + play_list.size());
                // }
                if (curCardUI.getOwner().equals(my_id)) {
                    // card A
                    try {
                        ImageIcon img = new ImageIcon(OnlineUI.class.getResource(
                                "/resource/card/" + curCardUI.getValue() + "_" + curCardUI.getSuit() + ".png"));
                        btnCardA.setIcon(new ImageIcon(img.getImage().getScaledInstance(card_width, card_height,
                                java.awt.Image.SCALE_SMOOTH)));
                    } catch (NullPointerException e) {
                        System.out.println("Image not found");
                    }
                    btnCardA.setVisible(true);
                } else if (curCardUI.getOwner().equals(OnlineTools.increaseID(my_id))) {
                    // card B
                    try {
                        ImageIcon img = new ImageIcon(OnlineUI.class.getResource(
                                "/resource/card/" + curCardUI.getValue() + "_" + curCardUI.getSuit() + ".png"));
                        btnCardB.setIcon(new ImageIcon(img.getImage().getScaledInstance(card_width, card_height,
                                java.awt.Image.SCALE_SMOOTH)));
                    } catch (NullPointerException e) {
                        System.out.println("Image not found");
                    }
                    btnCardB.setVisible(true);
                } else if (curCardUI.getOwner().equals(OnlineTools.increaseID(OnlineTools.increaseID(my_id)))) {
                    // card B
                    try {
                        ImageIcon img = new ImageIcon(OnlineUI.class.getResource(
                                "/resource/card/" + curCardUI.getValue() + "_" + curCardUI.getSuit() + ".png"));
                        btnCardC.setIcon(new ImageIcon(img.getImage().getScaledInstance(card_width, card_height,
                                java.awt.Image.SCALE_SMOOTH)));
                    } catch (NullPointerException e) {
                        System.out.println("Image not found");
                    }
                    btnCardC.setVisible(true);
                } else {
                    // card D
                    try {
                        ImageIcon img = new ImageIcon(OnlineUI.class.getResource(
                                "/resource/card/" + curCardUI.getValue() + "_" + curCardUI.getSuit() + ".png"));
                        btnCardD.setIcon(new ImageIcon(img.getImage().getScaledInstance(card_width, card_height,
                                java.awt.Image.SCALE_SMOOTH)));
                    } catch (NullPointerException e) {
                        System.out.println("Image not found");
                    }
                    btnCardD.setVisible(true);
                }
            }
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    public void drawScore(String message) {
        String arr[] = message.split("#");
        if (my_id.equals("A")) {
            lblScoreA.setText(arr[0] + "/" + arr[4]);
            lblScoreB.setText(arr[1] + "/" + arr[5]);
            lblScoreC.setText(arr[2] + "/" + arr[6]);
            lblScoreD.setText(arr[3] + "/" + arr[7]);
        } else if (my_id.equals("B")) {
            lblScoreD.setText(arr[0] + "/" + arr[4]);
            lblScoreA.setText(arr[1] + "/" + arr[5]);
            lblScoreB.setText(arr[2] + "/" + arr[6]);
            lblScoreC.setText(arr[3] + "/" + arr[7]);
        } else if (my_id.equals("C")) {
            lblScoreC.setText(arr[0] + "/" + arr[4]);
            lblScoreD.setText(arr[1] + "/" + arr[5]);
            lblScoreA.setText(arr[2] + "/" + arr[6]);
            lblScoreB.setText(arr[3] + "/" + arr[7]);
        } else {
            lblScoreB.setText(arr[0] + "/" + arr[4]);
            lblScoreC.setText(arr[1] + "/" + arr[5]);
            lblScoreD.setText(arr[2] + "/" + arr[6]);
            lblScoreA.setText(arr[3] + "/" + arr[7]);

        }
        newRound();
    }
    // end drawing

    // start logic
    // start game call by server
    public void startGame() {

    }

    public void addToPlayList(String message) {
        CardUI cardUI = OnlineTools.message2card(message);
        play_list.add(cardUI);
        draw4Card();
    }

    public void addToMyList(String message) {

        List<CardUI> list = OnlineTools.message2list(message);
        if (list.get(0).getOwner().equals(my_id)) {
            my_list.clear();

            for (CardUI cardUI : list) {
                cardUI.setOwner(my_id);
            }

            my_list.addAll(list);
            drawCard();
        }
    }

    public void setPassMode(String message) {
        if (message.contains("3")) {
            pick_card_mode = GAME_MODE.PASS_3_CARD;
            picked_card_count = 0;
            waiting_user = true;
            return;
        } else {
            if (message.contains(my_id)) {
                pick_card_mode = GAME_MODE.PASS_1_CARD;
                waiting_user = true;
                return;
            }
        }

        waiting_user = false;
    }

    public void setName(String message) {
        try {
            String id, name;

            id = message.split("#")[0];
            name = message.split("#")[1];

            // my_id = id lblA = name
            // my_id.inc = id lblB = name
            if (my_id.equals(id)) {
                lblNameA.setText(name);
            } else if (OnlineTools.increaseID(my_id).equals(id)) {
                lblNameB.setText(name);
            } else if (OnlineTools.increaseID(OnlineTools.increaseID(my_id)).equals(id)) {
                lblNameC.setText(name);
            } else {
                lblNameD.setText(name);
            }

        } catch (Exception e) {
            exit("setName exception");
        }
    }

    private void passOneCard(int index) {

        CardUI cardUI = my_list.get(index);
        String mes = OnlineTools.card2message(cardUI);
        if (is_server) {
            server.displayMessage(new ChatMessage(ChatMessage.PICK1, mes));
        } else {
            client.sendMessage(new ChatMessage(ChatMessage.PICK1, mes));
        }
    }

    private void passThreeCard() {
        List<CardUI> list = new ArrayList<CardUI>();

        // get data from UI name
        int size = listButton.size();
        for (int i = 0; i < size; i++) {
            if (listButton.get(i).getName().contains("_back")) {
                list.add(my_list.get(i));
            }
        }

        if (is_server) {
            server.sendMessage(new ChatMessage(ChatMessage.PICK3, OnlineTools.list2message(list)));
        } else {
            client.sendMessage(new ChatMessage(ChatMessage.PICK3, OnlineTools.list2message(list)));
        }

        waiting_user = false;
        newRound();
    }

    private void newRound() {
        play_list.clear();
        draw4Card();
    }

    private void exit(String message) {
        System.out.println("ERROR at " + message);
    }
    // end logic

    private void login() {

        client = new Client("localhost", Integer.parseInt(textFieldPort.getText()), textFieldName.getText(), this);

        if (client.start()) {
            is_connected = true;
            is_server = false;

            btnLogin.setText("Logout");
            btnStartServer.setEnabled(false);

            textFieldPort.setEnabled(false);
            textFieldName.setEnabled(false);
            textFieldMessage.setEnabled(true);

            listMessages.clear();
            appendMessage("Login to server");
        }
    }

    private void logout() {

        client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));

        if (true) {
            is_connected = false;

            btnLogin.setText("Login");

            btnStartServer.setEnabled(true);
            btnLogin.setEnabled(true);

            textFieldPort.setEnabled(true);
            textFieldName.setEnabled(true);
            textFieldMessage.setEnabled(false);

            listMessages.clear();
            appendMessage("Logout to server");
        }

    }

    private void startServer() {

        server = new Server(Integer.parseInt(textFieldPort.getText()), textFieldName.getText(), this);
        new ServerRunning().start();

        if (true) {
            is_connected = true;
            is_server = true;

            btnStartServer.setText("Stop server");
            btnLogin.setEnabled(false);

            textFieldPort.setEnabled(false);
            textFieldName.setEnabled(false);
            textFieldMessage.setEnabled(true);

            listMessages.clear();
            appendMessage("Started Server");
        }

    }

    private void stopServer() {

        if (server != null) {
            server.stop();
            server = null;
        }

        if (true) {
            is_connected = false;

            btnLogin.setText("Login");
            btnStartServer.setText("Start server");
            btnLogin.setEnabled(true);

            textFieldPort.setEnabled(true);
            textFieldName.setEnabled(true);
            textFieldMessage.setEnabled(false);

            listMessages.clear();
            appendMessage("Stoped Server");
        }
    }

    public void connectionFailed() {
        // disable button //
        // end game ..
    }

    public void appendMessage(String mes) {
        // add to main list
        listMessages.add(mes);

        // add to GUI
        int size = listMessages.size();
        String[] list = new String[size];

        for (int i = 0; i < size; i++) {
            list[i] = listMessages.get(i);
        }

        listChat.setListData(list);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                listChat.setSelectedIndex(size - 1);
                listChat.ensureIndexIsVisible(size - 1);
            }
        });
    }

    /*
     * A thread to run the Server
     */
    class ServerRunning extends Thread {

        public void run() {
            server.start(); // should execute until if fails
            // the server failed

            appendMessage("###Started Server Error");
            appendMessage("###Please use other port");

            if (true) {
                is_connected = false;

                btnLogin.setText("Login");
                btnStartServer.setText("Start server");
                btnLogin.setEnabled(true);

                textFieldPort.setEnabled(true);
                textFieldName.setEnabled(true);
                textFieldMessage.setEnabled(false);
            }

            server = null;
        }
    }
}
