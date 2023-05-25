package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Joueur {
    private List<Carte> cartes;

    public Joueur() {
        this.cartes = new ArrayList<>();
    }

    public List<Carte> getCartes() {
        return cartes;
    }
}
