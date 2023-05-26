
package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> cards;
    private List<Card> tas;

    public Player() {
        this.cards = new ArrayList<>();
        this.tas = new ArrayList<>();
    }

    public List<Card> getCards() {
        return cards;
    }

    public List<Card> getTas() {
        return tas;
    }

    public void addToTas(List<Card> cards) {
        tas.addAll(cards);
    }

    public Row chooseRowToRemove() {
        // Logique pour que le joueur choisisse une série à ramasser
        return null;
    }
}