package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Card {
    private int number;
    private int penalty;
    private ImageView cardImage;
    private int column;

    public Card(int number, ImageView cardImage) {
        this.number = number;
        this.penalty = calculatePenalty(number);
        this.cardImage = cardImage;
    }

    public int getNumber() {
        return number;
    }

    public int getPenalty() {
        return penalty;
    }

    public int getColumn() {
        return column;
    }

    public int getPoints() {
        return penalty;
    }

    public Image getImage() {
        return cardImage.getImage();
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

    public int getValue() {
        return number;
    }

    public ImageView getCardImage() {
        return cardImage;
    }
}