
package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private List<Card> cards;

    public Row() {
        this.cards = new ArrayList<>();
    }

    public boolean canCardBePlaced(Card card) {
        if (cards.isEmpty()) {
            return true; // La série est vide, n'importe quelle carte peut être placée
        } else {
            Card lastCard = cards.get(cards.size() - 1);
            return card.getNumber() > lastCard.getNumber(); // Vérifie si la carte est plus grande que la dernière carte de la série
        }
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public boolean isFull() {
        return cards.size() == 5; // Vérifie si la série est complète (5 cartes)
    }

    public List<Card> getCards() {
        return cards;
    }

    public void clearCards() {
        cards.clear();
    }
}