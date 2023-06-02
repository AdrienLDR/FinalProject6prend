package com.example.demo;

import javafx.scene.control.ChoiceDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Player {
    private List<Card> cards;
    private List<Card> tas;
    private int totalPoints;

    public Player() {
        this.cards = new ArrayList<>();
        this.tas = new ArrayList<>();
        this.totalPoints = 0;
    }

    public List<Card> getCards() {
        return cards;
    }

    public List<Card> getTas() {
        return tas;
    }

    public int getTotalPoints() {
        return totalPoints;
    }
    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void addToTas(List<Card> cards) {
        tas.addAll(cards);
    }

    public Row chooseRowToRemove(List<Row> rows) {
        // Afficher une boîte de dialogue pour choisir une série
        ChoiceDialog<Row> dialog = new ChoiceDialog<>(rows.get(0), rows);
        dialog.setTitle("Choisir une série");
        dialog.setHeaderText("Séries disponibles :");
        dialog.setContentText("Choisissez une série à ramasser :");

        Optional<Row> result = dialog.showAndWait();
        return result.orElse(null);
    }



}