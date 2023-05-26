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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
        return gridPane;
    }

    private void addRowsToGridPane(GridPane gridPane) {
        // Create a separate GridPane for the board cards
        GridPane boardPane = new GridPane();
        boardPane.setAlignment(Pos.CENTER);
        boardPane.setHgap(10);
        boardPane.setVgap(10);

        // Add the board cards to the boardPane
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 6; col++) {
                int rowIndex = row * 6 + col;
                if (rowIndex < rows.length) {
                    Row currentRow = rows[rowIndex];
                    if (!currentRow.getCards().isEmpty()) {
                        StackPane cardPane = createCardRectangle(currentRow.getCards().get(0), player1);
                        cardPane.setUserData(player1);

                        HBox cardBox = new HBox(cardPane);
                        cardBox.setAlignment(Pos.CENTER);

                        boardPane.add(cardBox, col, row);
                    }
                }
            }
        }

        // Add the boardPane to the main gridPane
        gridPane.add(boardPane, 1, 2, 3, 4);

        // Add empty space between player card rows and board rows
        VBox emptySpace = new VBox();
        emptySpace.setMinHeight(20); // Adjust the height of the empty space as needed
        gridPane.add(emptySpace, 1, 6, 1, 1);
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
            StackPane cardPane = createCardRectangle(card, player);// Pass player as the second argument

            HBox cardBox = new HBox(cardPane);
            cardBox.setAlignment(Pos.CENTER);

            playerCards.getChildren().add(cardBox);
        }

        return playerCards;
    }



    private StackPane createCardRectangle(Card card, Player player) {
        Rectangle rectangle = new Rectangle(30, 40);
        rectangle.getStyleClass().add("card-rectangle");

        Text cardNumberText = new Text(String.valueOf(card.getNumber()));
        cardNumberText.getStyleClass().add("card-number");

        StackPane cardPane = new StackPane(rectangle, cardNumberText);
        cardPane.setAlignment(Pos.CENTER);
        cardPane.setUserData(player); // Stocke le joueur dans la propriété UserData du cardPane

        setupCardDragAndDrop(cardPane); // Configure le glisser-déposer pour la carte

        return cardPane;
    }

    private void setupCardDragAndDrop(StackPane cardPane) {
        // Enable card dragging
        cardPane.setOnDragDetected(event -> {
            Dragboard dragboard = cardPane.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(cardPane.getUserData())); // Utilise la propriété UserData pour stocker le numéro de la carte
            dragboard.setContent(content);
            event.consume();
        });

        // Add drop functionality to the board cards
        cardPane.setOnDragOver(event -> {
            if (event.getGestureSource() != cardPane && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        cardPane.setOnDragEntered(event -> {
            if (event.getGestureSource() != cardPane && event.getDragboard().hasString()) {
                cardPane.getStyleClass().add("card-drag-over");
            }
            event.consume();
        });

        cardPane.setOnDragExited(event -> {
            cardPane.getStyleClass().remove("card-drag-over");
            event.consume();
        });

        cardPane.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasString()) {
                Row row = (Row) cardPane.getUserData(); // Récupère la rangée à partir de la propriété UserData
                int cardNumber = Integer.parseInt(dragboard.getString());
                Card droppedCard = new Card(cardNumber);
                if (row.canCardBePlaced(droppedCard)) {
                    row.addCard(droppedCard);
                    VBox playerCards = player1Cards.getChildren().contains(cardPane) ? player1Cards : player2Cards; // Sélectionne le bon conteneur de cartes du joueur
                    playerCards.getChildren().remove(cardPane);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        cardPane.setOnDragDone(event -> {
            cardPane.getStyleClass().remove("card-drag-over");
            event.consume();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
