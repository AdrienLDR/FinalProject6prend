package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Card {
    private int number;
    private int penality;
    private ImageView cardImage;
    private int column;

    public Card(int number, ImageView cardImage) {
        this.number = number;
        this.penality = calculatePenalty(number);
        this.cardImage = cardImage;
    }

    public int getNumber() {
        return number;
    }

    public int getPenality() {
        return penality;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getPoints() {
        return penality;
    }

    public ImageView getCardImage() {
        return cardImage;
    }

    public static List<Card> generateCards() {
        List<Card> cards = new ArrayList<>();
        for (int number = 1; number <= 104; number++) {
            String imagePath = "/com/example/demo/cards/" + number + ".png";
            Image image = new Image(Card.class.getResource(imagePath).toExternalForm());
            ImageView cardImageView = new ImageView(image);
            Card card = new Card(number, cardImageView);
            cards.add(card);
        }
        return cards;
    }

    public boolean isFaceUp(Card card) {
        return card.isFaceUp(card);
    }



    public boolean setFaceUp(Card card) {
        if (!card.isFaceUp(card)) {
            Image backsideImage = new Image(Card.class.getResource("/com/example/demo/cards/backside.png").toExternalForm());
            card.getCardImage().setImage(backsideImage);
            return true; // La carte a été retournée avec succès
        }
        return false; // La carte était déjà retournée
    }

    public static void distributeCards(List<Player> players) {
        List<Card> deck = generateCards();
        Collections.shuffle(deck);

        int index = 0;
        for (Player player : players) {
            for (int number = 0; number < 10; number++) {
                Card card = deck.get(index);
                card.setColumn(number); // Set the column number for the card
                player.getCards().add(card);
                index++;
            }
        }
    }

    // Calculate card penalties
    public static int calculatePenalty(int number) {
        if (number == 55) {
            return 7;
        } else if (number % 10 == 0) {
            return 3;
        } else if (number % 11 == 0) {
            return 5;
        } else if (number % 5 == 0) {
            return 2;
        } else {
            return 1;
        }
    }
}
