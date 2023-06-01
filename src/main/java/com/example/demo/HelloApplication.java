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
import javafx.scene.text.TextAlignment;
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
    private VBox pointBox;
    private Row[] rows;
    private List<StackPane> availableSlots;

    private List<Card> deck;
    private List<Player> players;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("6 Qui Prend");
        String jonhnny = "src/main/resources/com/example/demo/Music.wav";
        com.example.demo.Music music = new com.example.demo.Music();
        music.playMusic(jonhnny);
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
        mainPane = new BorderPane();
        addPlayerLabels(mainPane);

        cardGridPane = new GridPane();
        cardGridPane.setAlignment(Pos.CENTER);
        distributeCardsToPlayers(numPlayers);

        deckCard = new HBox();
        deckCard.setPadding(new Insets(10, 10, 10, 10));
        deckCard.setSpacing(10);
        deckCard.setAlignment(Pos.CENTER);
        addCardsToBottom(deckCard);

        deckIndivCard = new VBox();
        deckIndivCard.setPadding(new Insets(10, 10, 10, 10));
        deckIndivCard.setSpacing(10);

        pointBox = new VBox();
        pointBox.setPadding(new Insets(10, 10, 10, 10));
        pointBox.setSpacing(10);
        pointBox.setAlignment(Pos.CENTER_LEFT);

        Label pointLabel = new Label("Pénalités :");
        pointLabel.setFont(Font.font(25));
        pointLabel.setTextAlignment(TextAlignment.CENTER);

        pointBox.getChildren().addAll(pointLabel);


        mainPane.setBottom(deckCard);
        mainPane.setLeft(deckIndivCard);

        VBox leftSlots = createLeftSlots();
        deckIndivCard.getChildren().add(leftSlots);

        VBox centerSlots = createCenterSlots();
        mainPane.setCenter(centerSlots);

        Scene scene = new Scene(mainPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        mainPane.setRight(pointBox);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLeftSlots() {
        VBox leftSlots = new VBox();
        leftSlots.setSpacing(10);
        leftSlots.setAlignment(Pos.CENTER_LEFT);
        leftSlots.setPadding(new Insets(10));

        availableSlots = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            StackPane slot = createSlot();
            availableSlots.add(slot);
            leftSlots.getChildren().add(slot);
        }

        return leftSlots;
    }

    private VBox createCenterSlots() {
        VBox centerSlots = new VBox();
        centerSlots.setSpacing(10);
        centerSlots.setAlignment(Pos.CENTER);
        centerSlots.setPadding(new Insets(10));

        List<Card> deck = Card.generateCards();
        Collections.shuffle(deck);

        for (int i = 0; i < 4; i++) {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER);

            Card randomCard = deck.remove(0);
            StackPane slot = createSlot();
            StackPane cardPane = createCardRectangle(randomCard);
            slot.getChildren().add(cardPane);
            row.getChildren().add(slot);

            for (int j = 1; j < 6; j++) {
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

    private void distributeCardsToPlayers(int numPlayers) {
        Player player1 = new Player();
        Player player2 = new Player();
        List<Player> players = List.of(player1, player2);

        for (Player player : players) {
            for (int i = 0; i < 10; i++) {
                Card card = generateCard();
                player.addCard(card);
            }
        }

        this.players = players;

        player1Cards = createPlayerCards(player1);
        mainPane.setLeft(player1Cards);

        if (numPlayers == 2) {
            player2Cards = createPlayerCards(player2);
            mainPane.setRight(player2Cards);
        }
    }

    private VBox createPlayerCards(Player player) {
        VBox playerCards = new VBox();
        playerCards.setAlignment(Pos.CENTER_LEFT);
        playerCards.setSpacing(10);
        playerCards.setPadding(new Insets(10));

        List<Card> cards = player.getCards();

        for (Card card : cards) {
            StackPane cardPane = createCardRectangle(card);
            playerCards.getChildren().add(cardPane);
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

    // Méthode de gestion des clics sur une carte
    private void handleCardClick(Card card) {
        int cardValue = card.getValue();
        System.out.println("Carte cliquée : " + cardValue);
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
