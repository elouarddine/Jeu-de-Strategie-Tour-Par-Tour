package model;

import utils.configuration.Parametres;
import utils.configuration.ProprieteEquipement;
import utils.configuration.TypeEquipement;


/**
 * La classe Arme représente une arme utilisée par un combattant dans le jeu.
 * Chaque arme a des propriétés spécifiques définies par son type, telles que
 * la portée, le coût en énergie, le coût en énergie avec bouclier et le nombre de munitions.
 * Les propriétés sont initialisées dynamiquement à partir des paramètres du jeu.
 */
public class Arme {

    private int portee;
    /**
     * Le coût en énergie d'un tir avec cette arme.
     */
    private int coutEnergie;  
    private int coutEnergieAvecBouclier;
    private int munition;
    
    private TypeEquipement type;
    protected Combattant porteur;

    /**
     * Constructeur Initialise les propriétés de l'arme en fonction de son type à partir des paramètres globaux du jeu.
     *
     * @param type    Le type de l'arme (TypeEquipement).
     * @param porteur Le combattant qui porte cette arme.
     */
    public Arme(TypeEquipement type) {
        this.type = type;

        // Initialisation des propriétés à partir des paramètres du jeu
        this.portee = (int) Parametres.getPropriete(type, ProprieteEquipement.PORTEE);
        this.coutEnergie = (int) Parametres.getPropriete(type, ProprieteEquipement.DEGATENERGIE);
        this.coutEnergieAvecBouclier = (int) Parametres.getPropriete(type, ProprieteEquipement.ENERGIE_AVEC_BOUCLIER);
        this.munition = (int) Parametres.getPropriete(type, ProprieteEquipement.MUNITION);
    }

    /**
     * Retourne la portée maximale de l'arme.
     *
     * @return La portée en nombre de cases.
     */
    public int getPortee() {
        return portee;
    }

    /**
     * Retourne le coût en énergie d'un tir avec cette arme.
     *
     * @return Le coût en énergie.
     */
    public int getCoutEnergie() {
        return coutEnergie;
    }

    /**
     * Retourne le coût en énergie d'un tir avec cette arme lorsque le bouclier est actif.
     *
     * @return Le coût en énergie avec bouclier.
     */
    public int getCoutEnergieAvecBouclier() {
        return coutEnergieAvecBouclier;
    }

    /**
     * Retourne le nombre de munitions restantes pour cette arme.
     *
     * @return Le nombre de munitions.
     */
    public int getMunition() {
        return this.munition;
    }

    
    /**
     * Retourne le type de l'arme.
     *
     * @return Le type de l'arme (TypeEquipement).
     */
    public TypeEquipement getType() {
        return this.type;
    }

    /**
     * Modifier la portée de l'arme.
     *
     * @param portee La nouvelle portée en nombre de cases.
     */
    public void setPortee(int portee) {
        this.portee = portee;
    }

    /**
     * Modifier le coût en énergie d'un tir avec cette arme.
     *
     * @param coutEnergie Le nouveau coût en énergie.
     */
    public void setCoutEnergie(int coutEnergie) {
        this.coutEnergie = coutEnergie;
    }

    /**
     * Modifier le nombre de munitions restantes pour cette arme.
     *
     * @param munition Le nouveau nombre de munitions.
     */
    public void setMunition(int munition) {
        this.munition = munition;
    }

   
}
