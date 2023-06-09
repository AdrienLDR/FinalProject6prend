package com.example.demo;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.*;

public class HelloApplication extends Application {

    private Map<Card, String> cardChoices;
    private BorderPane mainPane;
    private Stage primaryStage;
    private GridPane cardGridPane;
    private HBox deckCard;
    private VBox deckIndivCard;
    private VBox pointBox;
    private VBox player1Cards;
    private VBox player2Cards;
    private Row[] rows;
    private List<StackPane> availableSlots; // Liste des emplacements disponibles
    private Map<StackPane, Card> cardPaneMap;
    private List<Card> cardsPlayed;
    private List<Player> players;
    private VBox leftSlots;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("6 Qui Prend");
        createModeSelectionScene();
    }

    private void createModeSelectionScene() {
        StackPane root = new StackPane();

        // Chargement de l'image de fond
        Image backgroundImage = new Image(getClass().getResourceAsStream("/com/example/demo/MenuBackground.png"));

        // Création d'un ImageView pour afficher l'image de fond
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(800);
        backgroundImageView.setFitHeight(600);
        backgroundImageView.setPreserveRatio(true);

        // Contenu de l'interface utilisateur
        VBox contentBox = new VBox();
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setSpacing(20);
        contentBox.setPadding(new Insets(20));

        Label titleLabel = new Label("Bienvenue !");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button onePlayerButton = new Button("1 Joueur");
        onePlayerButton.setOnAction(e -> startGame(1));

        Button twoPlayersButton = new Button("2 Joueurs");
        twoPlayersButton.setOnAction(e -> startGame(2));

        contentBox.getChildren().addAll(titleLabel, onePlayerButton, twoPlayersButton);

        // Ajout des éléments au conteneur principal
        root.getChildren().addAll(backgroundImageView, contentBox);

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

        cardsPlayed = new ArrayList<>();
        cardPaneMap = new HashMap<>();

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
        leftSlots = createLeftSlots();
        deckIndivCard.getChildren().add(leftSlots);

        // Création de l'image "image.png"
        ImageView imageView = new ImageView();
        Image image = new Image(getClass().getResourceAsStream("/com/example/demo/logo2.png"));
        imageView.setImage(image);

        HBox imageBox = new HBox(imageView);
        imageBox.setAlignment(Pos.CENTER);

        VBox centerSlots = createCenterSlots();
        VBox centerBox = new VBox(imageBox, centerSlots); // Ajout de l'image et de la VBox centerSlots dans un conteneur VBox
        centerBox.setAlignment(Pos.CENTER);
        mainPane.setCenter(centerBox);

        Scene scene = new Scene(mainPane, 900, 700);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        mainPane.setRight(pointBox);

        primaryStage.setScene(scene);
        primaryStage.show();

        //créa des joueurs
        Player player1 = new Player();
        Player player2 = numPlayers==2? new Bot() : new Player();

        players = List.of(player1, player2);
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

            // Créer un Label pour afficher la valeur de la carte
            Label cardValueLabel = new Label();
            cardValueLabel.getStyleClass().add("card-value-label");
            cardValueLabel.setVisible(false);
            leftSlots.getChildren().add(cardValueLabel);

            // Écouter les changements dans l'emplacement
            slot.getChildren().addListener((ListChangeListener<Node>) change -> {
                if (slot.getChildren().isEmpty()) {
                    cardValueLabel.setVisible(false);
                } else {
                    // Obtenez la valeur de la carte à partir du CardPane associé
                    StackPane cardPane = (StackPane) slot.getChildren().get(0);
                    Card card = cardPaneMap.get(cardPane);
                    if (card != null) {
                        cardValueLabel.setText(String.valueOf(card.getNumber()));
                        cardValueLabel.setVisible(true);
                        cardsPlayed.add(card);
                        compareLeftSlotCards(); // Appeler la fonction pour comparer les cartes dans les emplacements de gauche


                    }
                }
                compareLeftSlotCards(); // Appeler la fonction pour comparer les cartes dans les emplacements de gauche
            });
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

            // Créer un label pour afficher la valeur de la carte
            Label cardValueLabel = new Label(String.valueOf(randomCard.getNumber()));
            cardValueLabel.getStyleClass().add("card-value-label");
            row.getChildren().add(cardValueLabel);

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

        // Vérifie si l'emplacement est rempli
        slot.getChildren().addListener((ListChangeListener<Node>) change -> {
            if (slot.getChildren().isEmpty()) {
                slot.getStyleClass().remove("filled-slot");
            } else {
                slot.getStyleClass().add("filled-slot");
            }
        });

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
        Card.distributeCards(players);
        player1Cards = createPlayerCards(player1);
        HBox player1Container = new HBox(player1Cards);
        player1Container.setAlignment(Pos.CENTER_LEFT);
        cardGridPane.add(player1Container, 0, 2, 1, 4);

        player2Cards = new VBox(); // Initialiser player2Cards avec une VBox vide
        if (numPlayers == 2) {
            player2Cards = createPlayerCards(player2);
            HBox player2Container = new HBox(player2Cards);
            player2Container.setAlignment(Pos.CENTER_RIGHT);
            cardGridPane.add(player2Container, 2, 2, 1, 4);
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
        cardPaneMap.put(cardPane, card); // Ajouter la correspondance CardPane -> Card

        return cardPane;
    }

    private void moveCardToSlot(StackPane cardPane) {
        if (!availableSlots.isEmpty()) {
            StackPane slot = availableSlots.get(0);
            availableSlots.remove(0);

            Card card = cardPaneMap.get(cardPane); // Récupérer la carte associée au CardPane

            // Déplacer la carte vers l'emplacement disponible
            slot.getChildren().add(cardPane);

            if (availableSlots.isEmpty()) {
                compareLeftSlotCards(); // Appeler la fonction pour comparer les cartes dans les emplacements de gauche
            }
        }
    }

    private void compareLeftSlotCards() {
        if (cardsPlayed.size() >= 2) {
            Card card1 = cardsPlayed.get(0);
            Card card2 = cardsPlayed.get(1);

            System.out.println(card1.getNumber());
            int cardValue1 = card1.getNumber();
            int cardValue2 = card2.getNumber();


            Label comparisonLabel = new Label();
            comparisonLabel.setFont(Font.font(20));

            if (cardValue1 > cardValue2) {
                comparisonLabel.setText("La carte de gauche est plus grande");
                comparisonLabel.setTextFill(Color.BLUE);
            } else if (cardValue1 < cardValue2) {
                comparisonLabel.setText("La carte de droite est plus grande");
                comparisonLabel.setTextFill(Color.BLUE);
            } else {
                comparisonLabel.setText("Les cartes sont de même valeur");
                comparisonLabel.setTextFill(Color.BLACK);
            }

            if (pointBox.getChildren().size() > 1) {
                pointBox.getChildren().remove(1);
            }
            pointBox.getChildren().add(comparisonLabel);
        }
    }


    private int getRowValue(HBox row) {
        int rowValue = 0;
        for (Node slot : row.getChildren()) {
            if (slot instanceof StackPane) {
                StackPane stackPane = (StackPane) slot;
                if (!stackPane.getChildren().isEmpty()) {
                    Card card = cardPaneMap.get(stackPane.getChildren().get(0));
                    rowValue += card.getNumber();
                }
            }
        }
        return rowValue;
    }

    private StackPane getEmptySlotInRow(HBox row) {
        for (Node slot : row.getChildren()) {
            if (slot instanceof StackPane) {
                StackPane stackPane = (StackPane) slot;
                if (stackPane.getChildren().isEmpty()) {
                    return stackPane;
                }
            }
        }
        return null;
    }

    public void winnerPage(Player winner) {
        // Créer la page du gagnant
        VBox winnerPage = new VBox();
        winnerPage.setAlignment(Pos.CENTER);
        winnerPage.setSpacing(20);

        // Label du gagnant
        Label winnerLabel = new Label("Le gagnant est le joueur " + (players.indexOf(winner) + 1));
        winnerLabel.setStyle("-fx-font-size: 24px;");

        // Label du score du gagnant
        Label scoreLabel = new Label("Score: " + winner.getTotalPoints());
        scoreLabel.setStyle("-fx-font-size: 18px;");

        winnerPage.getChildren().addAll(winnerLabel, scoreLabel);

        // Afficher la page du gagnant dans une nouvelle fenêtre
        Stage winnerStage = new Stage();
        winnerStage.setTitle("Fin de la partie");
        winnerStage.setScene(new Scene(winnerPage, 300, 200));
        winnerStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}