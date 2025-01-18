package model.proxy;

import model.Case;
import model.Position;

/**
 * Interface représentant une grille de jeu.
 * <p>
 * Cette interface fournit des méthodes pour accéder aux cases de la grille et pour afficher la grille.
 */
public interface GrilleInterface {

    /**
     * Obtient la case à une position donnée.
     *
     * @param position La position de la case.
     * @return La case correspondante, ou null si la position est invalide.
     */
    Case getCase(Position position); 

    /**
     * Affiche la grille dans la console.
     */
    void afficherGrille(); 

}
