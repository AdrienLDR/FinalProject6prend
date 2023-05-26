package com.example.demo;

import com.example.demo.Card;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

class Row {
    private List<Card> cards;
    private GridPane boardPane;

    public Row() {
        this.cards = new ArrayList<>();
    }

    public boolean canCardBePlaced(Card card) {
        if (cards.isEmpty()) {
            return true; // La série est vide, n'importe quelle carte peut être placée
        } else {
            Card lastCard = cards.get(cards.size() - 1);
            return card.getNumber() > lastCard.getNumber(); // Vérifie si la carte est plus grande que la dernière carte de la série
        }
    }

    public void addCard(Card card) {
        cards.add(card);
        updateBoardPane();
    }


    public boolean isFull() {
        return cards.size() == 5; // Vérifie si la série est complète (5 cartes)
    }

    public List<Card> getCards() {
        return cards;
    }

    public void clearCards() {
        cards.clear();
        updateBoardPane();
    }

    public void setBoardPane(GridPane boardPane) {
        this.boardPane = boardPane;
        updateBoardPane();
    }

    private void updateBoardPane() {
        if (boardPane != null) {
            boardPane.getChildren().clear();
            if (cards.isEmpty()) {
                StackPane dropZonePane = createDropZone();
                boardPane.add(dropZonePane, 0, 0);
            } else {
                for (int i = 0; i < cards.size(); i++) {
                    StackPane cardPane = createCardRectangle(cards.get(i).getNumber());
                    HBox cardBox = new HBox(cardPane);
                    cardBox.setAlignment(Pos.CENTER);
                    boardPane.add(cardBox, i, 0);
                }
            }
        }
    }


    private StackPane createCardRectangle(int cardNumber) {
        Rectangle rectangle = new Rectangle(30, 40);
        rectangle.getStyleClass().add("card-rectangle");

        Text cardNumberText = new Text(String.valueOf(cardNumber));
        cardNumberText.getStyleClass().add("card-number");

        StackPane cardPane = new StackPane(rectangle, cardNumberText);
        cardPane.setAlignment(Pos.CENTER);

        return cardPane;
    }

    public StackPane createDropZone() {
        Rectangle dropZone = new Rectangle(30, 40);
        dropZone.getStyleClass().add("drop-zone");

        StackPane dropZonePane = new StackPane(dropZone);
        dropZonePane.setAlignment(Pos.CENTER);

        return dropZonePane;
    }

}