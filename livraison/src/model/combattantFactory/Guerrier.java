package model.combattantFactory;

import model.Combattant;
import model.Grille;
import model.Position;
import utils.strategyMessage.MessageHandler;

/**
 * Classe représentant un guerrier dans le jeu.
 * <p>
 * Le guerrier est un type spécifique de combattant avec des capacités ou des caractéristiques particulières.
 */
public class Guerrier extends Combattant {
   
    /**
     * Constructeur de la classe Guerrier.
     *
     * @param nom Le nom du guerrier.
     * @param position La position initiale du guerrier sur la grille.
     * @param messageHandler Le gestionnaire de messages pour afficher les informations du jeu.
     * @param grille La grille de jeu où évolue le guerrier.
     */
    public Guerrier(String nom, Position position, MessageHandler messageHandler, Grille grille) {
        super(nom, position, messageHandler, grille);
    }

    /**
     * Retourne une représentation textuelle du guerrier.
     *
     * @return "G€" si le bouclier est actif, "G" sinon.
     */
    @Override
    public String toString() {
        return super.estBouclierActif() ? "G€" : "G"; 
    }
}
