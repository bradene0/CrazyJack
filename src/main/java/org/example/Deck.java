package org.example;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//todo add multiple deck selections
public class Deck {
    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(cards);
    }

    public Card dealCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }
        //else {
            //throw new IllegalStateException("Error, Please try the action again.");
//}
        return cards.remove(0);
    }
}
