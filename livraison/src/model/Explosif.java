package model;

import utils.configuration.Parametres;
import utils.configuration.ProprieteEquipement;
import utils.configuration.TypeEquipement;

/**
 * La classe abstraite Explosif représente les explosifs du jeu, tels que les bombes et les mines.
 * Les explosifs ont des propriétés spécifiques comme le délai d'explosion, l'impact sur l'énergie,
 * et la visibilité. Ils sont positionnés sur une case et peuvent être déclenchés.
 */
public abstract class Explosif {

    protected int delaiExplosion; 
    protected int impactEnergie; 
    protected int coutEnergieAvecBouclier; 
    protected Visibilite visibilite; 
    protected TypeEquipement type; 
    protected Combattant porteur; 

    /**
     * Constructeur de la classe Explosif.
     * Initialise l'explosif avec ses propriétés spécifiques en fonction de son type.
     *
     * @param porteur Le combattant qui place l'explosif.
     * @param position La position de l'explosif sur la grille.
     * @param type Le type de l'explosif (TypeEquipement.BOMBE ou TypeEquipement.MINE).
     * @param grille La grille sur laquelle est placé l'explosif.
     */
    public Explosif(Combattant porteur, TypeEquipement type) {
        
        this.porteur = porteur;
        this.type = type;

        this.delaiExplosion = (int) Parametres.getPropriete(type, ProprieteEquipement.DELAI_EXPLOSION);
        this.impactEnergie = (int) Parametres.getPropriete(type, ProprieteEquipement.IMPACT_ENERGIE);
        this.coutEnergieAvecBouclier = (int) Parametres.getPropriete(type, ProprieteEquipement.ENERGIE_AVEC_BOUCLIER);
        this.visibilite = Visibilite.valueOf(((String) Parametres.getPropriete(type, ProprieteEquipement.VISIBILITE)).toUpperCase());
    }

    public int getDelaiExplosion() {
        return delaiExplosion;
    }
    

    public void setDelaiExplosion(int delaiExplosion) {
        this.delaiExplosion = Math.max(0, delaiExplosion); 
    }

    public int getImpactEnergie() {
        return impactEnergie;
    }

    public int getCoutEnergieAvecBouclier() {
        return coutEnergieAvecBouclier;
    }

    public Visibilite getVisibilite() {
        return visibilite;
    }

    public TypeEquipement getType() {
        return type;
    }

    public Combattant getPorteur() {
        return porteur;
    }

    /**
     * Vérifie si l'explosif est visible pour un combattant donné.
     *
     * @param combattant Le combattant pour lequel on vérifie la visibilité.
     * @return true si l'explosif est visible, false sinon.
     */
    public boolean estVisiblePour(Combattant combattant) {
        if (visibilite == Visibilite.TOTALE) {
            return true; // Toujours visible
        }
        return porteur.equals(combattant); // Visible uniquement pour le porteur
    }

    /**
     * Calcule les dégâts infligés à un combattant en fonction de l'état de son bouclier.
     *
     * @param cible Le combattant cible.
     * @return Les dégâts infligés.
     */
    private int calculerDegats(Combattant cible) {
        return cible.estBouclierActif() ? coutEnergieAvecBouclier : impactEnergie;
    }

    /**
     * Impacte les cases voisines d'un explosif.
     *
     * @param caseCible La case sur laquelle se trouve l'explosif.
     */
    protected void impacterVoisins(Case caseCible) {
        for (Case voisine : caseCible.getCasesVoisines()) {
            Combattant occupant = voisine.getOccupant();
            if (occupant != null) {
                int degats = calculerDegats(occupant);
                occupant.perdreEnergie(degats);

                if (occupant.getMessageHandler() != null) {
                    occupant.getMessageHandler().afficherMessage(
                            "L'explosif sur la case (" + caseCible.getPosition().getX() + ", " +
                            caseCible.getPosition().getY() + ") a impacté " + occupant.getNom() +
                            ", infligeant " + degats + " points de dégâts."
                    );
                }
            }
        }
    }

    /**
     * Méthode abstraite pour déclencher l'explosion de l'explosif.
     * Chaque type d'explosif implémente son propre comportement lors de l'explosion.
     *
     * @param combattant Le combattant affecté par l'explosion (peut être null).
     * @param caseCible La case sur laquelle se trouve l'explosif.
     */
    public abstract void explose(Combattant combattant, Case caseCible);

    @Override
    public String toString() {
        return "Explosif{" +
                "type=" + type +
                ", visibilite=" + visibilite +
                ", delaiExplosion=" + delaiExplosion +
                ", impactEnergie=" + impactEnergie +
                ", porteur=" + (porteur != null ? porteur.getNom() : "N/A") +
                '}';
    }

    /**
     * Énumération pour la visibilité des explosifs.
     */
    public enum Visibilite {
        TOTALE, PARTIELLE
    }
}
