package com.example.demo;


import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class HelloController {

    private GameController gameController;
    private List<Row> rows;
    private List<Player> players;
    private Player player1;
    private Player player2;
    private Card lowestCard;

    public void setGameData(GameController gameController, List<Row> rows, List<Player> players, Player player1, Player player2, Card lowestCard) {
        this.gameController = gameController;
        this.rows = rows;
        this.players = players;
        this.player1 = player1;
        this.player2 = player2;
        this.lowestCard = lowestCard;
    }

    @FXML
    private Label welcomeText;

    @FXML
    protected void playSinglePlayer() {
        // Logique pour jouer à un joueur
        welcomeText.setText("1 joueur");
        GameController gameController = new GameController(rows, players);
        gameController.handleLowestCard(player1, lowestCard);
    }

    @FXML
    protected void playMultiplayer() {
        // Logique pour jouer à deux joueurs
        welcomeText.setText("2 joueurs");
        GameController gameController = new GameController(rows, players);
        gameController.handleLowestCard(player2, lowestCard);
    }


}

