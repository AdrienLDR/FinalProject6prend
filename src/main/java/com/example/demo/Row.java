package com.example.demo;

public class Row {


    //Règle 1 : Valeurs croissantes
    public boolean canCardBePlaced(Card card) {
        if (card.isEmpty()) {
            //TODO faire isEmpty dans la classe card
            return true; // La série est vide, n'importe quelle carte peut être placée
        } else {
            Card lastCard = card.get(card.size() - 1);
            //TODO faire size dans classe card
            return card.getNumber() > lastCard.getNumber(); // Vérifie si la carte est plus grande que la dernière carte de la série
        }
    }
}
