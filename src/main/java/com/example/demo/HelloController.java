package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    protected void playSinglePlayer() {
        // Logique pour jouer à un joueur
        welcomeText.setText("1 joueur");
    }

    @FXML
    protected void playMultiplayer() {
        // Logique pour jouer à deux joueurs
        welcomeText.setText("2 joueurs");
    }
}
