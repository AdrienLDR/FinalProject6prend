package com.example.demo;

import javafx.scene.control.Label;

import java.util.Comparator;
import java.util.List;

public class GameController {
    private List<Row> rows;
    private List<Player> players;
    private Bot bot;
    private HelloApplication helloApplication;
    private int currentPlayerIndex;
    private List<Label> scoreLabels; // Liste de Labels pour afficher les scores des joueurs

    public GameController(List<Row> rows, List<Player> players) {
        this.rows = rows;
        this.players = players;
        this.bot = new Bot();
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
                int points = card.getPoints();
                totalPoints += points;
            }

            player.setTotalPoints(totalPoints);
        }
    }

    public void distributeCards() {
        Card.distributeCards(players);
    }

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

    public void calculateTotalPenalties() {
        for (Player player : players) {
            int totalPoints = 0;

            for (Card card : player.getCards()) {
                int points = card.getPoints();
                totalPoints += points;
            }

            player.setTotalPoints(totalPoints);
        }

        players.sort(Comparator.comparingInt(Player::getTotalPoints)); // Classement des joueurs par ordre croissant de pénalités
    }

    public Player endGame() {
        // Afficher le joueur gagnant avec le score le plus bas
        Player winner = players.get(0);
        int lowestScore = winner.getTotalPoints();
        for (Player player : players) {
            int score = player.getTotalPoints();
            if (score < lowestScore) {
                winner = player;
                lowestScore = score;
                return winner;
            }
        }
        return winner;
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



    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public void playCard(Card playerCard) {
        Player currentPlayer = getCurrentPlayer();

        if (currentPlayer.getCards().size() == 1) {
            Card botCard = bot.findBestCard(rows);

            playerCard.setFaceUp();
            botCard.setFaceUp();

            Row playerRow = getMinDifferenceRow(playerCard);
            Row botRow = getMinDifferenceRow(botCard);

            handleCompletedSeries(playerRow, playerCard);
            handleCompletedSeries(botRow, botCard);

            if (playerCard.getNumber() < botCard.getNumber()) {
                handleLowestCard(currentPlayer, playerCard);
            } else {
                handleLowestCard(currentPlayer, botCard);
            }

            calculatePoints();
            calculateTotalPenalties();

            updateScoreLabels();

            if (isGameFinished()) {
                endGame();
            } else {
                nextTurn();
            }
        }
    }

}
