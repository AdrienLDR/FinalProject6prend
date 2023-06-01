package com.example.demo;

import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameController {


    private List<Row> rows;
    private List<Player> players;
    private Bot bot;
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


    // Méthode pour jouer une carte (joueur et bot)
    public void playCard(Card playerCard) {
        Player currentPlayer = getCurrentPlayer();

        if (currentPlayer.getCards().size() == 1) {
            // Les deux joueurs ont choisi leur carte
            Card botCard = bot.findBestCard(rows);

            //TODO récupérer les cartes onclick
            //TODO Vérifier quelles cartes est la plus petite entre les deux et la placer la première
            //TODO relier les règles

            // Retourner les cartes choisies
            playerCard.setFaceUp(true);
            botCard.setFaceUp(true);

            // Trouver la rangée appropriée pour chaque carte
            Row playerRow = getMinDifferenceRow(playerCard);
            Row botRow = getMinDifferenceRow(botCard);

            // Gérer les séries complétées pour chaque joueur
            handleCompletedSeries(playerRow, playerCard);
            handleCompletedSeries(botRow, botCard);

            // Gérer la carte la plus basse pour chaque joueur
            if (playerCard.getNumber() < botCard.getNumber()) {
                handleLowestCard(currentPlayer, playerCard);
            } else {
                handleLowestCard(currentPlayer, botCard);
            }

            // Calculer les points et les pénalités
            calculatePoints();
            calculateTotalPenalities();

            // Mettre à jour les labels des scores
            updateScoreLabels();

            // Vérifier si le jeu est terminé
            if (isGameFinished()) {
                endGame();
            } else {
                // Passer au prochain tour
                nextTurn();
            }
        } else {
            // Attendre que l'autre joueur choisisse sa carte
        }
    }


    public void handleLowestCard(Player player, Card card) {
        Row chosenRow = player.chooseRowToRemove(rows); // Méthode pour que le joueur choisisse une série à ramasser
        player.addToTas(chosenRow.getCards()); // Ajoute les cartes à son tas
        chosenRow.clearCards(); // Efface les cartes de la série
        chosenRow.addCard(card); // Ajoute la nouvelle carte à la série
    }

    public void calculateTotalPenalities() {
        for (Player player : players) {
            int totalPenalities = 0;

            for (Card card : player.getCards()) {
                int penalties = card.getPenality();
                totalPenalities += penalties;
            }

            player.setTotalPenalities(totalPenalities);
        }

        Collections.sort(players, Comparator.comparingInt(Player::getTotalPenalities)); //Classement des joueurs par ordre croissant de pénalités
    }

    public void endGame() {
        // Afficher le joueur gagnant avec le score le plus bas
        Player winner = players.get(0);
        int lowestScore = winner.getTotalPoints();
        for (Player player : players) {
            int score = player.getTotalPoints();
            if (score < lowestScore) {
                winner = player;
                lowestScore = score;
            }
        }
    }

    public boolean isGameFinished() {
        // Vérifier si les joueurs n'ont plus de cartes dans leurs mains
        for (Player player : players) {
            if (!player.getCards().isEmpty()) {
                return false;
            }
        }

        return true;
    }


}
