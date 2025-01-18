package model.combattantFactory;

import model.Combattant;
import model.Grille;
import model.Position;
import utils.strategyMessage.MessageHandler;

/**
 * Classe représentant un sniper dans le jeu.
 * <p>
 * Le sniper est un type spécifique de combattant avec des capacités ou des caractéristiques particulières.
 */
public class Sniper extends Combattant {

    /**
     * Constructeur de la classe Sniper.
     *
     * @param nom            Le nom du sniper.
     * @param position       La position initiale du sniper sur la grille.
     * @param messageHandler Le gestionnaire de messages pour afficher les informations du jeu.
     * @param grille         La grille de jeu où évolue le sniper.
     */
    public Sniper(String nom, Position position, MessageHandler messageHandler, Grille grille) {
        super(nom, position, messageHandler, grille);
    }

    /**
     * Retourne une représentation textuelle du sniper.
     *
     * @return "K€" si le bouclier est actif, "K" sinon.
     */
    @Override
    public String toString() {
        return super.estBouclierActif() ? "K€" : "K"; 
    }
}
