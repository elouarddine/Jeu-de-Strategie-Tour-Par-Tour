package model;

import utils.configuration.Parametres;

public class Pastille extends Case {

    private final int valeur;

    public Pastille(Position position, Grille grille) {
        super(position, grille);
        this.valeur = Parametres.energiePastille;
    }

    /**
     * Retourne la valeur de la pastille en points d'énergie.
     *
     * @return La valeur de la pastille.
     */
    public int getValeur() {
        return valeur;
    }

    /**
     * Applique le bonus d'énergie au combattant.
     *
     * @param combattant Le combattant recevant le bonus.
     */
    public void boost(Combattant combattant) {
        if (combattant != null) {
            combattant.setEnergie(combattant.getEnergie() + valeur);
        }
    }

    @Override
    public String toString() {
        return "o";
    }
}
