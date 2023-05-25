package com.example.demo;

public class GameController {
    //Règle 2 : La plus petite différence
    public Row getMinDifferenceRow(Card card) {
        int minDifference = Integer.MAX_VALUE;
        Row minRow = null;

        for (Row row : rows) {
            if (row.canCardBePlaced(card)) {
                Card lastCard = row.getLastCard();
                int difference = card.getNumber() - lastCard.getNumber();
                if (difference < minDifference) {
                    minDifference = difference;
                    minRow = row;
                }
            }
        }

        return minRow;
    }

    //Règle 3 : Série terminée
    public void handleCompletedSeries(Row row) {
        if (row.isFull()) {
            Player player = getCurrentPlayer();
            player.addToTas(row.getCards()); // Ajoute les cartes à son tas
            row.clearCards(); // Efface les cartes de la série
            row.addCard(card); // Ajoute la nouvelle carte à la série
        } else {
            row.addCard(card); // Ajoute simplement la nouvelle carte à la série
        }
    }

    //Règle 4 : La carte la plus faible
    public void handleLowestCard(Player player, Card card) {
        Row chosenRow = player.chooseRowToRemove(); // Méthode pour que le joueur choisisse une série à ramasser
        player.addToTas(chosenRow.getCards()); // Ajoute les cartes à son tas
        chosenRow.clearCards(); // Efface les cartes de la série
        chosenRow.addCard(card); // Ajoute la nouvelle carte à la série
    }
}
