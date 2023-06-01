package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HelloApplication extends Application {

    private BorderPane mainPane;
    private Stage primaryStage;
    private GridPane cardGridPane;
    private HBox deckCard;
    private VBox deckIndivCard;
    private VBox player1Cards;
    private VBox player2Cards;
    private Row[] rows;
    private List<StackPane> availableSlots; // Liste des emplacements disponibles

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("6 Qui Prend");

        createModeSelectionScene();
    }

    private void createModeSelectionScene() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(20));
        Label titleLabel = new Label("6 Qui Prend");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        Button onePlayerButton = new Button("1 Joueur");
        onePlayerButton.setOnAction(e -> startGame(1));
        Button twoPlayersButton = new Button("2 Joueurs");
        twoPlayersButton.setOnAction(e -> startGame(2));
        root.getChildren().addAll(titleLabel, onePlayerButton, twoPlayersButton);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startGame(int numPlayers) {
        createRows();
        // Plan avec l'ensemble des éléments du jeu
        mainPane = new BorderPane();
        addPlayerLabels(mainPane);

        // Box des cartes des joueurs qui sont sur le Board
        cardGridPane = new GridPane();
        cardGridPane.setAlignment(Pos.CENTER);
        addCardsToPlayers(cardGridPane, numPlayers);

        // Box des cartes du deck des joueurs (deck entier)
        deckCard = new HBox();
        deckCard.setPadding(new Insets(10, 10, 10, 10));
        deckCard.setSpacing(10);
        deckCard.setAlignment(Pos.CENTER);
        addCardsToBottom(deckCard);

        // Box des cartes du deck des joueurs (cartes individuelles)
        deckIndivCard = new VBox();
        deckIndivCard.setPadding(new Insets(10, 10, 10, 10));
        deckIndivCard.setSpacing(10);

        mainPane.setBottom(deckCard);
        mainPane.setLeft(deckIndivCard);

        // Créer les emplacements à gauche de l'écran
        VBox leftSlots = createLeftSlots();
        deckIndivCard.getChildren().add(leftSlots);

        // Créer les emplacements au milieu de la fenêtre
        VBox centerSlots = createCenterSlots();
        mainPane.setCenter(centerSlots);

        Scene scene = new Scene(mainPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLeftSlots() {
        VBox leftSlots = new VBox();
        leftSlots.setSpacing(10);
        leftSlots.setAlignment(Pos.CENTER_LEFT);
        leftSlots.setPadding(new Insets(10));

        availableSlots = new ArrayList<>(); // Réinitialiser la liste des emplacements disponibles

        for (int i = 0; i < 2; i++) {
            StackPane slot = createSlot();
            availableSlots.add(slot); // Ajouter l'emplacement à la liste des emplacements disponibles
            leftSlots.getChildren().add(slot);
        }

        return leftSlots;
    }

    private VBox createCenterSlots() {
        VBox centerSlots = new VBox();
        centerSlots.setSpacing(10);
        centerSlots.setAlignment(Pos.CENTER);
        centerSlots.setPadding(new Insets(10));

        List<Card> deck = Card.generateCards(); // Générer le jeu de cartes
        Collections.shuffle(deck); // Mélanger le jeu de cartes

        for (int i = 0; i < 4; i++) {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER);

            // Générer une carte aléatoire pour le début de la ligne
            Card randomCard = deck.remove(0);
            StackPane slot = createSlot();
            StackPane cardPane = createCardRectangle(randomCard);
            slot.getChildren().add(cardPane);
            row.getChildren().add(slot);

            for (int j = 1; j < 6; j++) {
                // Créer un emplacement vide pour les autres colonnes
                StackPane emptySlot = createSlot();
                row.getChildren().add(emptySlot);
            }

            centerSlots.getChildren().add(row);
        }

        return centerSlots;
    }


    private StackPane createSlot() {
        StackPane slot = new StackPane();
        slot.setPrefSize(68, 85);
        slot.getStyleClass().add("slot");
        return slot;
    }

    private void addCardsToBottom(HBox deckCard) {
        List<Card> cards = Card.generateCards();
        Collections.shuffle(cards);
        for (int i = 0; i < 10; i++) {
            Card card = cards.get(i);
            StackPane cardPane = createCardRectangle(card);
            deckCard.getChildren().add(cardPane);
        }
    }

    private Card generateCard() {
        List<Card> cards = Card.generateCards();
        return cards.remove(0);
    }

    private void createRows() {
        rows = new Row[24];
        for (int i = 0; i < 24; i++) {
            rows[i] = new Row();
        }
    }

    private void addPlayerLabels(BorderPane mainPane) {
        Label player1Label = createPlayerLabel("Joueur 1");
        Label player2Label = createPlayerLabel("Joueur 2");

        mainPane.setTop(new HBox(player1Label, player2Label));
    }

    private Label createPlayerLabel(String playerName) {
        Label label = new Label(playerName);
        label.getStyleClass().add("player-label");
        return label;
    }

    private void addCardsToPlayers(GridPane gridPane, int numPlayers) {
        Player player1 = new Player();
        Player player2 = new Player();
        List<Player> players = List.of(player1, player2);
        Card.distributeCards(players);
        player1Cards = createPlayerCards(player1);
        HBox player1Container = new HBox(player1Cards);
        player1Container.setAlignment(Pos.CENTER_LEFT);
        gridPane.add(player1Container, 0, 2, 1, 4);

        player2Cards = new VBox(); // Initialiser player2Cards avec une VBox vide
        if (numPlayers == 2) {
            player2Cards = createPlayerCards(player2);
            HBox player2Container = new HBox(player2Cards);
            player2Container.setAlignment(Pos.CENTER_RIGHT);
            gridPane.add(player2Container, 2, 2, 1, 4);
        }
    }

    private VBox createPlayerCards(Player player) {
        VBox playerCards = new VBox();
        playerCards.setSpacing(10);
        List<Card> cards = player.getCards();
        for (Card card : cards) {
            StackPane cardPane = createCardRectangle(card);
            HBox cardBox = new HBox(cardPane);
            cardBox.setAlignment(Pos.CENTER);
            playerCards.getChildren().add(cardBox);
        }
        return playerCards;
    }

    private StackPane createCardRectangle(Card card) {
        ImageView cardImageView = card.getCardImage();
        cardImageView.setFitWidth(68);
        cardImageView.setFitHeight(85);
        cardImageView.getStyleClass().add("card-image");
        StackPane cardPane = new StackPane(cardImageView);
        cardPane.setAlignment(Pos.CENTER);

        cardImageView.setOnMouseEntered(event -> {
            cardPane.setEffect(new DropShadow());
        });

        cardImageView.setOnMouseExited(event -> {
            cardPane.setEffect(null);
        });

        cardImageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                moveCardToSlot(cardPane); // Appeler la fonction pour déplacer la carte vers un emplacement disponible
            }
        });

        return cardPane;
    }

    private void moveCardToSlot(StackPane cardPane) {
        if (!availableSlots.isEmpty()) {
            StackPane slot = availableSlots.get(0); // Obtenir le premier emplacement disponible de la liste
            availableSlots.remove(0); // Supprimer cet emplacement de la liste des emplacements disponibles

            // Déplacer la carte vers l'emplacement disponible
            slot.getChildren().add(cardPane);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
