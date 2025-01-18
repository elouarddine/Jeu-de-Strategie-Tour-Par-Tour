package model.factoryJoueur;

import java.util.ArrayList;
import java.util.List;

import model.Combattant;
import model.Grille;
import model.factoryJoueur.JoueurFactory.TypeJoueurEnum;

/**
 * Classe représentant un joueur avec un type spécifique et une stratégie.
 */
public class TypeJoueur {
    private final TypeJoueurEnum type;
    private final List<Combattant> combattants;
    private final JoueurStrategy strategie;

    /**
     * Constructeur de la classe TypeJoueur.
     *
     * @param type Le type de joueur (HUMAIN, ALEATOIRE, AI).
     * @param combattants La liste des combattants associés à ce joueur.
     * @param strategie   La stratégie associée à ce joueur.
     */
    public TypeJoueur(TypeJoueurEnum type, List<Combattant> combattants, JoueurStrategy strategie) {
        if (type == null) {
            throw new IllegalArgumentException("Le type de joueur ne peut pas être nul.");
        }
        if (combattants == null || combattants.isEmpty()) {
            throw new IllegalArgumentException("La liste des combattants ne peut pas être nulle ou vide.");
        }
        if (strategie == null) {
            throw new IllegalArgumentException("La stratégie ne peut pas être nulle.");
        }

        this.type = type;
        this.combattants = new ArrayList<>(combattants);
        this.strategie = strategie;
    }

    /**
     * Ajoute un combattant au joueur.
     *
     * @param combattant Le combattant à ajouter.
     * @throws IllegalArgumentException Si le combattant est nul.
     */
    public void ajouterCombattant(Combattant combattant) {
        if (combattant == null) {
            throw new IllegalArgumentException("Le combattant ne peut pas être nul.");
        }
        this.combattants.add(combattant);
    }

    /**
     * Exécute un tour pour tous les combattants du joueur en appliquant la stratégie.
     *
     * @param grille La grille du jeu.
     */
    public void jouerTour(Grille grille) {
        for (Combattant combattant : combattants) {
            if (combattant.estEnVie()) {
                this.strategie.appliquerStrategie(combattant, grille);
            }
        }
    }

    /**
     * Obtient le type du joueur.
     *
     * @return Le type de joueur.
     */
    public TypeJoueurEnum getType() {
        return type;
    }

    /**
     * Obtient la liste des combattants du joueur.
     *
     * @return La liste des combattants.
     */
    public List<Combattant> getCombattants() {
        return new ArrayList<>(combattants);
    }

    /**
     * Obtient la stratégie du joueur.
     *
     * @return La stratégie.
     */
    public JoueurStrategy getStrategie() {
        return strategie;
    }
}
