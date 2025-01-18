package model.combattantFactory;

import model.Combattant;
import model.Grille;
import model.Position;
import utils.strategyMessage.MessageHandler;

/**
 * Classe représentant un tireur dans le jeu.
 * <p>
 * Le tireur est un type spécifique de combattant avec des capacités ou des caractéristiques particulières.
 */
public class Tireur extends Combattant {

    /**
     * Constructeur de la classe Tireur.
     *
     * @param nom            Le nom du tireur.
     * @param position       La position initiale du tireur sur la grille.
     * @param messageHandler Le gestionnaire de messages pour afficher les informations du jeu.
     * @param grille         La grille de jeu où évolue le tireur.
     */
    public Tireur(String nom, Position position, MessageHandler messageHandler, Grille grille) {
        super(nom, position, messageHandler, grille);
    }

    /**
     * Retourne une représentation textuelle du tireur.
     *
     * @return "R€" si le bouclier est actif, "R" sinon.
     */
    @Override
    public String toString() {
        return super.estBouclierActif() ? "R€" : "R"; 
    }
}
