package main.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 *
 * @author lonewolf
 */
public class MainUI {

    private JFrame frmHeartsGame;

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
                    MainUI window = new MainUI();
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
    public MainUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmHeartsGame = new JFrame();
        frmHeartsGame.getContentPane().setBackground(SystemColor.desktop);
        frmHeartsGame.setTitle("Hearts Game");
        frmHeartsGame.setBounds(200, 200, 450, 400);
        frmHeartsGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SpringLayout springLayout = new SpringLayout();
        frmHeartsGame.getContentPane().setLayout(springLayout);

        JLabel lblGameName = new JLabel("HEARTS");
        springLayout.putConstraint(SpringLayout.WEST, lblGameName, 80, SpringLayout.WEST,
                frmHeartsGame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, lblGameName, -80, SpringLayout.EAST,
                frmHeartsGame.getContentPane());
        lblGameName.setHorizontalAlignment(SwingConstants.CENTER);
        lblGameName.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblGameName.setForeground(Color.WHITE);
        springLayout.putConstraint(SpringLayout.NORTH, lblGameName, 10, SpringLayout.NORTH,
                frmHeartsGame.getContentPane());
        lblGameName.setFont(new Font("Segoe Script", Font.PLAIN, 30));
        frmHeartsGame.getContentPane().add(lblGameName);

        JButton btnOnline = new JButton("Play");
        btnOnline.setFocusable(false);
        springLayout.putConstraint(SpringLayout.NORTH, btnOnline, 80, SpringLayout.SOUTH, lblGameName);
        springLayout.putConstraint(SpringLayout.WEST, btnOnline, 80, SpringLayout.WEST, frmHeartsGame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, btnOnline, -80, SpringLayout.EAST, frmHeartsGame.getContentPane());
        btnOnline.setForeground(new Color(224, 255, 255));
        btnOnline.setOpaque(false);
        btnOnline.setContentAreaFilled(false);
        btnOnline.setBorderPainted(false);
        btnOnline.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnOnline.setFont(new Font("Segoe Script", Font.PLAIN, 18));
        frmHeartsGame.getContentPane().add(btnOnline);

        JButton btnInfo = new JButton("Rules");
        btnInfo.setFocusable(false);
        springLayout.putConstraint(SpringLayout.NORTH, btnInfo, 130, SpringLayout.SOUTH, lblGameName);
        springLayout.putConstraint(SpringLayout.WEST, btnInfo, 80, SpringLayout.WEST, frmHeartsGame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, btnInfo, -80, SpringLayout.EAST, frmHeartsGame.getContentPane());
        btnInfo.setForeground(Color.WHITE);
        btnInfo.setOpaque(false);
        btnInfo.setContentAreaFilled(false);
        btnInfo.setBorderPainted(false);
        btnInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInfo.setBackground(SystemColor.desktop);
        btnInfo.setFont(new Font("Segoe Script", Font.PLAIN, 18));
        frmHeartsGame.getContentPane().add(btnInfo);

        JButton btnQuit = new JButton("Quit");
        btnQuit.setFocusable(false);
        springLayout.putConstraint(SpringLayout.NORTH, btnQuit, 180, SpringLayout.SOUTH, lblGameName);
        springLayout.putConstraint(SpringLayout.WEST, btnQuit, 80, SpringLayout.WEST, frmHeartsGame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, btnQuit, -80, SpringLayout.EAST, frmHeartsGame.getContentPane());
        btnQuit.setForeground(Color.WHITE);
        btnQuit.setOpaque(false);
        btnQuit.setContentAreaFilled(false);
        btnQuit.setBorderPainted(false);
        btnQuit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnQuit.setBackground(SystemColor.desktop);
        btnQuit.setFont(new Font("Segoe Script", Font.PLAIN, 18));
        frmHeartsGame.getContentPane().add(btnQuit);

        btnOnline.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OnlineUI.main(null);
                frmHeartsGame.setVisible(false);
            }
        });

        btnInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //THIS IS THE ORIGINAL CODE//
                //   JOptionPane.showMessageDialog(frmHeartsGame, "HEARTS GAME MADE BY:\n - Ngô Văn Hùng\n - Đào Phú Hỷ\n - Lương Vĩnh Khang\n\nThanks for playing", "Info", JOptionPane.INFORMATION_MESSAGE);

                JOptionPane.showMessageDialog(frmHeartsGame, "The 52 cards are shuffled and are distributed evenly between the four players (13 cards per player). \n"
                        + "The game of Hearts allows for cards to be exchanged in a certain order at the start of each round. \n"
                        + "They are as follows: \n"
                        + "Round 1: Each player must select 3 cards to give to the player on their left. \n"
                        + "Round 2: Each player must select 3 cards to give to the player on their right.\n"
                        + "Round 3: Each player must select 3 cards to give to the player opposite them.\n"
                        + "Round 4: The round starts without exchanging cards.\n"
                        + "Round 5: Same as round 1 and the pattern follows. \n"
                        + "After the cards are exchanged, the user with 2♣ plays that card in the middle of the table.\n"
                        + "The next player is the player to the left and so on until all 4 players have played a card.\n"
                        + "This is known as a ‘trick’. The person who plays the highest ranked card, takes the cards.\n"
                        + "For example, if player 1 plays ‘2♣’, player 2 plays ‘K♣’,\n"
                        + "player 3 follows with ‘10♣’ and player 4 plays ‘A♣’, then player 4 takes the cards.\n"
                        + "Players must follow suit of the card that leads off the trick unless the player does not possess that card.\n"
                        + "The cards at the end of the trick are not usable for the rest of the round, but their point values are added to the user who collects them.\n"
                        + "Since Clubs ♣, Diamonds ♦ and Spades ♠ (except for Q♠) don’t have any points attributed to them, collecting these cards will not increase a player score.\n"
                        + "The player who wins the trick, leads off the next trick and so on until all 52 cards are used. \n"
                        + "No point cards allowed in the first round: At the start of the game, none of the users can play the Q♠ or any Hearts ♥ cards.\n"
                        + "For example, if a round starts and one of the 4 players does not have the clubs,\n"
                        + "they can play any other suit cards which do not have any point values. \n"
                        + "Hearts cannot lead till broken: After the first trick is completed,\n"
                        + "the player who wins the trick leads the next one with any card except for Hearts.\n"
                        + "If another player who is not leading the trick cannot follow suit,\n"
                        + "they can break the trick with any hearts card.\n"
                        + "For example, if the player leading the trick plays ‘9♣’ and the next user plays ‘6♣’ \n"
                        + "and if the third player cannot follow suit (does not have a ♣ clubs card to continue the trick),\n"
                        + "they can play a card from any suit, even a Hearts ♥ card.\n"
                        + "Shoot the moon: If at the end of the round, a player collects all the point value cards (All the ♥ cards and the Q♠),\n"
                        + "then the player has shot the moon. At the end of this round, the player who shot the moon has the choice to either\n"
                        + "remove 26 points from his score (if his score is above 26), or to increase the score of all the other players by 26. ;", "", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //OnlineUI.main(null);
                //frmHeartsGame.setVisible(false);
                System.exit(0);
            }
        });
    }
}
