package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Bot {
    private List<Card> hand;
    private int score;

    public Bot() {
        this.hand = new ArrayList<>();
        this.score = 0;
    }

    Card findBestCard(List<Row> rows) {
        Card bestCard = null;
        int minPoints = Integer.MAX_VALUE;

        for (Row row : rows) {
            // Récupérer la dernière carte posée dans le rang
            Card lastCard = row.getLastCard();

            for (Card card : hand) {
                // Vérifier si la colonne correspondante a moins de 5 cartes
                if (row.getColumnSize(card.getColumn()) < 5) {
                    // Calculer la distance entre la carte actuelle et la dernière carte posée
                    int distance = Math.abs(card.getNumber() - lastCard.getNumber());

                    if (distance == 0) {
                        // Si la carte a le même numéro que la dernière carte posée, on la sélectionne directement
                        bestCard = card;
                        break;
                    } else {
                        // Calculer les points résultants après l'ajout de la carte au rang
                        int points = row.getPointsAfterAddition(card) - card.getPoints();

                        if (points < minPoints) {
                            bestCard = card;
                            minPoints = points;
                        }
                    }
                }
            }
        }

        return bestCard;
    }
}

