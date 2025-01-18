package utils.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe Parametres représentant tous les paramètres statiques utilisés aussi que des méthodes pour ajouter des propriétés ou accéder à ces propriétés dans le jeu.
 */
public class Parametres {

    public static int largeur;
    public static int longueur;

    public static int nombreMinMurs;
    public static int nombreMaxMurs;

    public static int nombreMinPastilles;
    public static int nombreMaxPastilles;
    public static int energiePastille;

    public static int energieInitiale;

    public static int coutDeplacement;
    public static int coutUtilisationBouclier;

    public static int munitionLimite;

    // Équipements (armes, explosifs, pastilles)
    public static Map<TypeEquipement, Map<ProprieteEquipement, Object>> equipements = new HashMap<>();

    /**
     * Ajoute une propriété à un équipement (armes, explosifs, pastilles).
     *
     * @param type Le type de l'équipement (ex: PISTOLET, BOMBE)
     * @param propriete La propriété de l'équipement (ex: PORTEE, ENERGIE)
     * @param valeur La valeur de la propriété
     */
    public static void ajouterPropriete(TypeEquipement type, ProprieteEquipement propriete, Object valeur) {
        if (!equipements.containsKey(type)) {
            equipements.put(type, new HashMap<>());
        }
        equipements.get(type).put(propriete, valeur);
    }

    /**
     * Initialise un équipement dans la map s'il n'existe pas déjà.
     *
     * @param equipement Le type de l'équipement à initialiser.
     */
    public static void initialiserEquipement(TypeEquipement equipement) {
        if (!equipements.containsKey(equipement)) {
            equipements.put(equipement, new HashMap<>());
        }
    }

    /**
     * Récupère la valeur d'une propriété d'un équipement (armes, explosifs, pastilles).
     *
     * @param type Le type de l'équipement
     * @param propriete La propriété à récupérer
     * @return La valeur associée à la propriété, ou null si inexistante
     */
    public static Object getPropriete(TypeEquipement type, ProprieteEquipement propriete) {
        return equipements.getOrDefault(type, new HashMap<>()).get(propriete);
    }
}
