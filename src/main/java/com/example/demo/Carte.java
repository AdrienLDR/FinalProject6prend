package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Carte {
    private int number;

    public Carte(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public static List<Carte> genererCartes() {
        List<Carte> cartes = new ArrayList<>();
        for (int i = 1; i <= 104; i++) {
            Carte carte = new Carte(i);
            cartes.add(carte);
        }
        return cartes;
    }

    public static void distribuerCartes(List<Joueur> joueurs) {
        List<Carte> deck = genererCartes();
        Collections.shuffle(deck);

        int index = 0;
        for (Joueur joueur : joueurs) {
            for (int i = 0; i < 10; i++) {
                joueur.getCartes().add(deck.get(index));
                index++;
            }
        }
    }
}
