package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Card {
    private int number;
    private int penality;
    private ImageView cardImage;

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



    public static void distributeCards(List<Player> players) {
        List<Card> deck = generateCards();
        Collections.shuffle(deck);

        int index = 0;
        for (Player player : players) {
            for (int number = 0; number < 10; number++) {
                player.getCards().add(deck.get(index));
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