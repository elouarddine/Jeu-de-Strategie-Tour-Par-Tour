package model.factoryJoueur;

import java.util.List;

import model.Combattant;
import utils.strategyMessage.MessageHandler;

/**
 * Factory pour créer des joueurs avec des stratégies spécifiques.
 */
public class JoueurFactory {

    /**
     * Énumération interne représentant les types de joueurs.
     */
    public enum TypeJoueurEnum {
        HUMAIN,
        ALEATOIRE,
        AI;

        /**
         * Convertit une chaîne de caractères en TypeJoueurEnum.
         *
         * @param typeStr La chaîne représentant le type de joueur (HUMAIN, ALEATOIRE, AI).
         * @return Une valeur TypeJoueurEnum correspondant.
         * @throws IllegalArgumentException Si le type de joueur est inconnu.
         */
        public static TypeJoueurEnum fromString(String typeStr) {
            try {
                return TypeJoueurEnum.valueOf(typeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Type de joueur inconnu : " + typeStr, e);
            }
        }
    }

    /**
     * Crée un joueur avec une stratégie spécifique en fonction de son type.
     *
     * @param type Le type de joueur (TypeJoueurEnum : HUMAIN, ALEATOIRE, AI).
     * @param combattants La liste des combattants associés à ce joueur.
     * @return Une instance de TypeJoueur configurée avec sa stratégie.
     * @throws IllegalArgumentException Si les paramètres sont invalides.
     */
    public static TypeJoueur creerJoueurAvecStrategie(TypeJoueurEnum type, List<Combattant> combattants, Mode mode) {
        if (type == null) {
            throw new IllegalArgumentException("Le type de joueur ne peut pas être nul.");
        }
        if (combattants == null || combattants.isEmpty()) {
            throw new IllegalArgumentException("La liste des combattants ne peut pas être nulle ou vide.");
        }
        if (mode == null) {
            throw new IllegalArgumentException("Le mode ne peut pas être nul.");
        }

        MessageHandler handler = combattants.get(0).getMessageHandler();

        JoueurStrategy strategie;

        switch (type) {
            case AI:
                strategie = new AIJoueur(handler, mode);
                break;
            case HUMAIN:
                strategie = new HumainJoueur(handler, mode);
                break;
            case ALEATOIRE:
                strategie = new AleatoireJoueur(handler, mode);
                break;
            default:
                throw new IllegalArgumentException("Type de joueur non pris en charge : " + type);
        }

        TypeJoueur joueur = new TypeJoueur(type, combattants, strategie);

        return joueur;
    }
}
