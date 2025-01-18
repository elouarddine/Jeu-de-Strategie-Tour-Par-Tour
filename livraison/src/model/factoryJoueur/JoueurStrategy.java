package model.factoryJoueur;

import model.Combattant;
import model.Grille;

/**
 * Interface définissant la stratégie d'un joueur.
 * <p>
 * Cette interface permet de définir une stratégie spécifique que le combattant va suivre dans la grille de jeu.
 */
public interface JoueurStrategy {

    /**
     * Applique la stratégie au combattant donné dans le contexte de la grille.
     *
     * @param combattant Le combattant sur lequel appliquer la stratégie.
     * @param grille     La grille de jeu dans laquelle le combattant évolue.
     */
    void appliquerStrategie(Combattant combattant, Grille grille);
}
