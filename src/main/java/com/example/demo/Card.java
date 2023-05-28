package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Card {
    private int number;
    private int penality;

    public Card(int number) {
        this.number = number;
        this.penality = calculatePenality(number);
    }

    public int getNumber() {
        return number;
    }

    public int getPenality() {
        return penality;
    }

    public static List<Card> generateCards() {
        List<Card> cards = new ArrayList<>();
        for (int number = 1; number <= 104; number++) {
            Card card = new Card(number);
            cards.add(card);
        }
        return cards;
    }

    public static void distributeCards(List<Player> players) {
        List<Card> deck = generateCards();
        Collections.shuffle(deck);

        int index = 0;
        for (Player player : players) {
            for (int number = 0; number < 10; number++) {
                player.getCards().add(deck.get(index));
                index++;
            }
        }
    }

    //Les pénalités de cartes
    public static int calculatePenality(int number){
        if (number == 55){
            return 7;
        } else if(number % 10 == 0) {
            return 3;
        } else if(number % 11 == 0){
            return 5;
        } else if (number % 5 == 0) {
            return 2;
        } else {
            return 1;
        }
    }

}