package com.example.demo;

import javafx.scene.control.Label;

import java.util.List;

public class GameController {
    private List<Row> rows;
    private List<Player> players;
    private int currentPlayerIndex;
    private List<Label> scoreLabels; // Liste de Labels pour afficher les scores des joueurs

    public GameController(List<Row> rows, List<Player> players) {
        this.rows = rows;
        this.players = players;
        this.currentPlayerIndex = 0;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void setScoreLabels(List<Label> scoreLabels) {
        this.scoreLabels = scoreLabels;
    }
    public void updateScoreLabels() {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            int playerScore = player.getTotalPoints();
            Label scoreLabel = scoreLabels.get(i);
            scoreLabel.setText("Score du joueur " + (i + 1) + ": " + playerScore);
        }
    }
    public void calculatePoints() {
        for (Player player : players) {
            int totalPoints = 0;

            for (Card card : player.getTas()) {
                int points = calculateCardPoints(card);
                totalPoints += points;
            }

            player.setTotalPoints(totalPoints);
        }
    }

    private int calculateCardPoints(Card card) {
        if (card.getNumber() == 55) {
            return 7;
        } else if (card.getNumber() % 11 == 0) {
            return 5;
        } else if (card.getNumber() % 10 == 0) {
            return 3;
        } else if (card.getNumber() % 5 == 0) {
            return 2;
        } else {
            return 1;
        }
    }

    public Row getMinDifferenceRow(Card card) {
        int minDifference = Integer.MAX_VALUE;
        Row minRow = null;

        for (Row row : rows) {
            if (row.canCardBePlaced(card)) {
                Card lastCard = row.getCards().get(row.getCards().size() - 1);
                int difference = card.getNumber() - lastCard.getNumber();
                if (difference < minDifference) {
                    minDifference = difference;
                    minRow = row;
                }
            }
        }

        return minRow;
    }

    public void handleCompletedSeries(Row row, Card card) {
        if (row.isFull()) {
            Player currentPlayer = getCurrentPlayer();
            currentPlayer.addToTas(row.getCards()); // Ajoute les cartes à son tas
            row.clearCards(); // Efface les cartes de la série
            row.addCard(card); // Ajoute la nouvelle carte à la série
        } else {
            row.addCard(card); // Ajoute simplement la nouvelle carte à la série
        }
    }

    public void handleLowestCard(Player player, Card card) {
        Row chosenRow = player.chooseRowToRemove(rows); // Méthode pour que le joueur choisisse une série à ramasser
        player.addToTas(chosenRow.getCards()); // Ajoute les cartes à son tas
        chosenRow.clearCards(); // Efface les cartes de la série
        chosenRow.addCard(card); // Ajoute la nouvelle carte à la série
    }
}
