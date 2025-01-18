package model.combattantFactory;

import model.Combattant;
import model.Grille;
import model.Position;
import utils.strategyMessage.MessageHandler;

/**
 * Classe représentant un tank dans le jeu.
 * <p>
 * Le tank est un type spécifique de combattant avec des capacités ou des caractéristiques particulières.
 */
public class Tank extends Combattant {
    
    /**
     * Constructeur de la classe Tank.
     *
     * @param nom            Le nom du tank.
     * @param position       La position initiale du tank sur la grille.
     * @param messageHandler Le gestionnaire de messages pour afficher les informations du jeu.
     * @param grille         La grille de jeu où évolue le tank.
     */
    public Tank(String nom, Position position, MessageHandler messageHandler, Grille grille) {
        super(nom, position, messageHandler, grille);
    }

    /**
     * Retourne une représentation textuelle du tank.
     *
     * @return "T€" si le bouclier est actif, "T" sinon.
     */
    @Override
    public String toString() {
        return super.estBouclierActif() ? "T€" : "T"; 
    }
}
