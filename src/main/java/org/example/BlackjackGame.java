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

    private void deal() {
        deck = new Deck();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        playerHand.add(deck.dealCard());
        playerHand.add(deck.dealCard());
        dealerHand.add(deck.dealCard());
        dealerHand.add(deck.dealCard());
        dealerLabel.setText("Dealer's Hand: " + dealerHand.get(0) + " and [Hidden]");
        playerLabel.setText("Player's Hand: " + playerHand.get(0) + " and " + playerHand.get(1));
        statusLabel.setText("Player's turn. Hit or Stand?");
        updateButtonStatus(false);
    }

    private void playerHits() {
        playerHand.add(deck.dealCard());
        playerLabel.setText("Player's Hand: " + playerHand);
        if (isBusted(playerHand)) {
            statusLabel.setText("Player busts. Dealer wins!");
            updateButtonStatus(true);
        }
    }

    private void playerStands() {
        while (getHandValue(dealerHand) < 17) {
            dealerHand.add(deck.dealCard());
        }
        dealerLabel.setText("Dealer's Hand: " + dealerHand);
        if (isBusted(dealerHand) || getHandValue(playerHand) > getHandValue(dealerHand)) {
            statusLabel.setText("Player wins!");
        } else if (getHandValue(playerHand) < getHandValue(dealerHand)) {
            statusLabel.setText("Dealer wins!");
        } else {
            statusLabel.setText("It's a tie!");
        }
        updateButtonStatus(true);
    }

    private int getHandValue(List<Card> hand) {
        int value = 0;
        int numAces = 0;
        for (Card card : hand) {
            String rank = card.getRank();
            if (rank.equals("Jack") || rank.equals("Queen") || rank.equals("King")) {
                value += 10;
            } else if (rank.equals("Ace")) {
                value += 11;
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

    private boolean isBusted(List<Card> hand) {
        return getHandValue(hand) > 21;
    }

    private void updateButtonStatus(boolean enableDealButton) {
        dealButton.setEnabled(enableDealButton);
        hitButton.setEnabled(!enableDealButton);
        standButton.setEnabled(!enableDealButton);
    }

    private class DealButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            deal();
        }
    }

    private class HitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            playerHits();
        }
    }

    private class StandButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            playerStands();
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