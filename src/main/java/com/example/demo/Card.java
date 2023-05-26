package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Card {
    private int number;

    public Card(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public static List<Card> generateCards() {
        List<Card> cards = new ArrayList<>();
        for (int i = 1; i <= 104; i++) {
            Card card = new Card(i);
            cards.add(card);
        }
        return cards;
    }

    public static void distributeCards(List<Player> players) {
        List<Card> deck = generateCards();
        Collections.shuffle(deck);

        int index = 0;
        for (Player player : players) {
            for (int i = 0; i < 10; i++) {
                player.getCards().add(deck.get(index));
                index++;
            }
        }
    }
}