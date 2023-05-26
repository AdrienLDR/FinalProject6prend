package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {

    private Stage primaryStage;
    private GridPane gridPane;
    private VBox player1Cards;
    private VBox player2Cards;
    private Row[] rows;

    private Player player1; // Déclarer player1 comme propriété de la classe
    private Player player2; // Déclarer player2 comme propriété de la classe

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("6 Qui Prend");

        createModeSelectionScene();

        // Move the call to addCardsToPlayers here
        gridPane = createGridPane();
        addCardsToPlayers(gridPane, 2);
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
        gridPane = createGridPane();
        addPlayerLabels(gridPane);
        addCardsToPlayers(gridPane, numPlayers);

        addRowsToGridPane(gridPane);

        Scene scene = new Scene(gridPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        // Set the constraints for the grid rows and columns
        for (int row = 0; row < 4; row++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(40); // Adjust the height as needed
            gridPane.getRowConstraints().add(rowConstraints);
        }

        for (int col = 0; col < 6; col++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPrefWidth(30); // Adjust the width as needed
            gridPane.getColumnConstraints().add(colConstraints);
        }

        return gridPane;
    }


    private void addRowsToGridPane(GridPane gridPane) {
        // Create a separate GridPane for the board cards
        GridPane boardPane = new GridPane();
        boardPane.setAlignment(Pos.CENTER);
        boardPane.setHgap(10);
        boardPane.setVgap(10);

        // Add the initial columns with randomly flipped cards
        for (int col = 0; col < 4; col++) {
            VBox column = createColumnWithCards();
            gridPane.add(column, col, 0);

            for (int row = 1; row <= 5; row++) {
                Pane dropZone = createDropZone();
                gridPane.add(dropZone, col, row);
            }
        }

        // Add the boardPane to the main gridPane
        gridPane.add(boardPane, 1, 2, 3, 4);

        // Add empty space between player card rows and board rows
        VBox emptySpace = new VBox();
        emptySpace.setMinHeight(20); // Adjust the height of the empty space as needed
        gridPane.add(emptySpace, 1, 6, 1, 1);
    }

    private VBox createColumnWithCards() {
        VBox column = new VBox();
        column.setSpacing(10);

        // Generate random cards for the column
        List<Card> cards = generateRandomCards();

        // Create card panes and add them to the column
        for (Card card : cards) {
            Label cardPane = createCardRectangle(card, null); // Pass null as player since these cards are not associated with any player
            cardPane.setDisable(true); // Disable the card panes initially to indicate that they are flipped
            column.getChildren().add(cardPane);
        }

        return column;
    }

    private List<Card> generateRandomCards() {
        List<Card> cards = new ArrayList<>();

        // Generate 4 random cards
        for (int i = 0; i < 4; i++) {
            int cardNumber = getRandomCardNumber();
            Card card = new Card(cardNumber);
            cards.add(card);
        }

        return cards;
    }

    private int getRandomCardNumber() {
        // Generate a random card number between 1 and 104
        return (int) (Math.random() * 104) + 1;
    }


    private Pane createDropZone() {
        Pane dropZone = new Pane();
        dropZone.getStyleClass().add("drop-zone");
        dropZone.setPrefSize(30, 40); // Ajustez la taille selon vos besoins
        return dropZone;
    }

    private void createRows() {
        rows = new Row[24];
        for (int i = 0; i < 24; i++) {
            rows[i] = new Row();
        }
    }

    private void addPlayerLabels(GridPane gridPane) {
        Label player1Label = createPlayerLabel("Joueur 1");
        Label player2Label = createPlayerLabel("Joueur 2");

        gridPane.add(player1Label, 0, 1);
        gridPane.add(player2Label, 2, 1);
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
            Label cardPane = createCardRectangle(card, player);// Pass player as the second argument

            HBox cardBox = new HBox(cardPane);
            cardBox.setAlignment(Pos.CENTER);

            playerCards.getChildren().add(cardBox);
        }

        return playerCards;
    }



    private Label createCardRectangle(Card card, Player player) {
        Rectangle rectangle = new Rectangle(30, 40);
        rectangle.getStyleClass().add("card-rectangle");

        Text cardNumberText = new Text(String.valueOf(card.getNumber()));
        cardNumberText.getStyleClass().add("card-number");

        StackPane cardPane = new StackPane(rectangle, cardNumberText);
        cardPane.setAlignment(Pos.CENTER);
        cardPane.setUserData(player);

        Label cardLabel = new Label();
        cardLabel.setGraphic(cardPane);
        cardLabel.setMinSize(30, 40);
        cardLabel.getStyleClass().add("card-label");

        setupCardDragAndDrop(cardLabel);

        return cardLabel;
    }


    private void setupCardDragAndDrop(Label cardLabel) {
        cardLabel.setOnDragDetected(event -> {
            Dragboard dragboard = cardLabel.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(cardLabel.getUserData()));
            dragboard.setContent(content);
            event.consume();
        });

        cardLabel.setOnDragOver(event -> {
            if (event.getGestureSource() != cardLabel && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        cardLabel.setOnDragEntered(event -> {
            if (event.getGestureSource() != cardLabel && event.getDragboard().hasString()) {
                cardLabel.setEffect(new DropShadow());
            }
            event.consume();
        });

        cardLabel.setOnDragExited(event -> {
            cardLabel.setEffect(null);
            event.consume();
        });

        cardLabel.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasString()) {
                Row row = (Row) cardLabel.getUserData();
                int cardNumber = Integer.parseInt(dragboard.getString());
                Card droppedCard = new Card(cardNumber);
                if (row.canCardBePlaced(droppedCard)) {
                    row.addCard(droppedCard);
                    VBox playerCards = player1Cards.getChildren().contains(cardLabel) ? player1Cards : player2Cards;
                    playerCards.getChildren().remove(cardLabel);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }



    public static void main(String[] args) {
        launch(args);
    }
}
