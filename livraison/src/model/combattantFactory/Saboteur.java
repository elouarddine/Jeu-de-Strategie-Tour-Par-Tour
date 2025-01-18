package model.combattantFactory;

import model.Combattant;
import model.Grille;
import model.Position;
import utils.strategyMessage.MessageHandler;

/**
 * Classe représentant un saboteur dans le jeu.
 * <p>
 * Le saboteur est un type spécifique de combattant avec des capacités ou des caractéristiques particulières.
 */
public class Saboteur extends Combattant {

    /**
     * Constructeur de la classe Saboteur.
     *
     * @param nom            Le nom du saboteur.
     * @param position       La position initiale du saboteur sur la grille.
     * @param messageHandler Le gestionnaire de messages pour afficher les informations du jeu.
     * @param grille         La grille de jeu où évolue le saboteur.
     */
    public Saboteur(String nom, Position position, MessageHandler messageHandler, Grille grille) {
        super(nom, position, messageHandler, grille);
    }

    /**
     * Retourne une représentation textuelle du saboteur.
     *
     * @return "S€" si le bouclier est actif, "S" sinon.
     */
    @Override
    public String toString() {
        return super.estBouclierActif() ? "S€" : "S"; 
    }
}
