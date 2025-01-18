package model.combattantFactory;

import model.Combattant;
import model.Grille;
import model.Position;
import utils.strategyMessage.MessageHandler;

/**
 * Classe représentant un ingénieur dans le jeu.
 * <p>
 * L'ingénieur est un type spécifique de combattant avec des capacités ou des caractéristiques particulières.
 */
public class Engineer extends Combattant {

    /**
     * Constructeur de la classe Engineer.
     *
     * @param nom            Le nom de l'ingénieur.
     * @param position       La position initiale de l'ingénieur sur la grille.
     * @param messageHandler Le gestionnaire de messages pour afficher les informations du jeu.
     * @param grille         La grille de jeu où évolue l'ingénieur.
     */
    public Engineer(String nom, Position position, MessageHandler messageHandler, Grille grille) {
        super(nom, position, messageHandler, grille);
    }

    /**
     * Retourne une représentation textuelle de l'ingénieur.
     *
     * @return "E€" si le bouclier est actif, "E" sinon.
     */
    @Override
    public String toString() {
        return super.estBouclierActif() ? "E€" : "E"; 
    }
}
