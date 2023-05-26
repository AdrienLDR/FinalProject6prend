package com.example.demo;

import java.util.List;

public class GameController {
    private List<Row> rows;
    private List<Player> players;
    private int currentPlayerIndex;

    public GameController(List<Row> rows, List<Player> players) {
        this.rows = rows;
        this.players = players;
        this.currentPlayerIndex = 0;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
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
        Row chosenRow = player.chooseRowToRemove(); // Méthode pour que le joueur choisisse une série à ramasser
        player.addToTas(chosenRow.getCards()); // Ajoute les cartes à son tas
        chosenRow.clearCards(); // Efface les cartes de la série
        chosenRow.addCard(card); // Ajoute la nouvelle carte à la série
    }
}
