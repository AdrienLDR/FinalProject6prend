package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> cards;

    public Player() {
        this.cards = new ArrayList<>();
    }

    public List<Card> getCards() {
        return cards;
    }
}
