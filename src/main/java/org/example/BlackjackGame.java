package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class BlackjackGame extends JFrame {

    private JLabel dealerLabel;
    private JLabel playerLabel;
    private JLabel statusLabel;
    private JButton dealButton;
    private JButton hitButton;
    private JButton standButton;
    private Deck deck;
    private List<Card> playerHand;
    private List<Card> dealerHand;

    public BlackjackGame() {
        setTitle("Blackjack Game");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        dealerLabel = new JLabel("Dealer's Hand: ");
        add(dealerLabel, BorderLayout.NORTH);

        playerLabel = new JLabel("Player's Hand: ");
        add(playerLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        statusLabel = new JLabel("Welcome to Blackjack!");
        add(statusLabel, BorderLayout.SOUTH);

        dealButton = new JButton("Deal");
        dealButton.addActionListener(new DealButtonListener());
        buttonPanel.add(dealButton);

        hitButton = new JButton("Hit");
        hitButton.addActionListener(new HitButtonListener());
        buttonPanel.add(hitButton);

        standButton = new JButton("Stand");
        standButton.addActionListener(new StandButtonListener());
        buttonPanel.add(standButton);

        add(buttonPanel, BorderLayout.SOUTH);
        updateButtonStatus(true);
    }

    private boolean gameEnded;
    private void deal() {
        deck = new Deck();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        playerHand.add(deck.dealCard());
        playerHand.add(deck.dealCard());
        dealerHand.add(deck.dealCard());
        dealerHand.add(deck.dealCard());
        updateDealerLabel(true);
        playerLabel.setText("Player's Hand: " + playerHand.get(0) + " and " + playerHand.get(1) +
                " (" + getHandValue(playerHand) + ")");
        statusLabel.setText("Player's turn. Hit or Stand?");
        updateButtonStatus(false);
        gameEnded = false;


    }

    private void playerHits() {
        playerHand.add(deck.dealCard());
        playerLabel.setText("Player's Hand: " + playerHand + " (" + getHandValue(playerHand) + ")");
        if (isBusted(playerHand)) {
            gameEnded = true;
            updateButtonStatus(true);
        } else if (getHandValue(playerHand) == 21) {
            gameEnded = true;
            updateButtonStatus(true);
        }
    }

    private void playerStands() {
        dealerLabel.setText("Dealer's Hand: " + dealerHand + " (" + getVisibleHandValue(dealerHand) + ")");
        while (getHandValue(dealerHand) < 17) {
            dealerHand.add(deck.dealCard());
            updateDealerLabel(false);
        }
        if (!isBusted(dealerHand)) {
            statusLabel.setText("Dealer stands.");
        }
        playerLabel.setText("Player's Hand: " + playerHand + " (" + getHandValue(playerHand) + ")");
        updateButtonStatus(true);
        displayOutcome();
    }

    private void displayOutcome() {
        System.out.println("Displaying outcome");
        String message;
        if (isBusted(playerHand) || getHandValue(playerHand) < getHandValue(dealerHand)) {
            message = "Dealer wins! Better luck next time.";
        } else if (isBusted(dealerHand) || getHandValue(playerHand) > getHandValue(dealerHand)) {
            message = "Player wins! Congratulations!";
        } else {
            message = "It's a tie! Try again.";
        }
        JOptionPane.showMessageDialog(this, message);
    }

    private int getHandValue(List<Card> hand) {
        int value = 0;
        int numAces = 0;
        boolean firstAce = true;
        for (Card card : hand) {
            String rank = card.getRank();
            if (rank.equals("Jack") || rank.equals("Queen") || rank.equals("King")) {
                value += 10;
            } else if (rank.equals("Ace")) {
                if (firstAce) {
                    value += 11;
                    firstAce = false;
                } else {
                    value += 1;
                }
                numAces++;
            } else {
                value += Integer.parseInt(rank);
            }
        }
        while (value > 21 && numAces > 0) {
            value -= 10;
            numAces--;
        }
        return value;
    }

    private int getVisibleHandValue(List<Card> hand) {
        List<Card> visibleCards = new ArrayList<>(hand.subList(1, hand.size()));
        return getHandValue(visibleCards);
    }

    private boolean isBusted(List<Card> hand) {
        return getHandValue(hand) > 21;
    }

    private void updateButtonStatus(boolean enableDealButton) {
        dealButton.setEnabled(enableDealButton);
        hitButton.setEnabled(!enableDealButton);
        standButton.setEnabled(!enableDealButton);
    }
    private void updateDealerLabel(boolean showHidden) {
        if (showHidden) {
            dealerLabel.setText("Dealer's Hand: " + dealerHand.get(0) + " and [Hidden] (" + getHandValue(dealerHand.subList(0, 1)) + ")");
        } else {
            dealerLabel.setText("Dealer's Hand: " + dealerHand + " (" + getHandValue(dealerHand) + ")");
        }

    }

    private class DealButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            deal();
            statusLabel.setForeground(Color.BLACK);
        }
    }

    private class HitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            playerHits();
            if (gameEnded) {
                displayOutcome();
            }
        }
    }

    private class StandButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            playerStands();
            if (gameEnded) {
                displayOutcome();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BlackjackGame().setVisible(true);
            }
        });
    }
}
