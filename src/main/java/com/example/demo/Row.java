package com.example.demo;

public class Row {


    //Règle 1 : Valeurs croissantes
    public boolean canCardBePlaced(Card card) {
        if (cards.isEmpty()) {
            return true; // La série est vide, n'importe quelle carte peut être placée
        } else {
            Card lastCard = cards.get(cards.size() - 1);
            return card.getNumber() > lastCard.getNumber(); // Vérifie si la carte est plus grande que la dernière carte de la série
        }
    }
}
