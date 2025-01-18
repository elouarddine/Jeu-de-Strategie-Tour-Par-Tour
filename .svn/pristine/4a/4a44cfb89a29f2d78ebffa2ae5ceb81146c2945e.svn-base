package model;

import utils.configuration.TypeEquipement;

public class Bombe extends Explosif {

	public Bombe(Combattant porteur, TypeEquipement type) {
        super(porteur, type);
    }

    /**
     * Explosion immédiate si un combattant se trouve sur la case.
     *
     * @param combattant Le combattant sur la case (ne peut pas être null).
     * @param caseCible La case où se trouve la bombe.
     */
    private void explosionImmediatement(Combattant combattant, Case caseCible) {
        if (combattant != null) {

        	int degats = combattant.estBouclierActif() ? getCoutEnergieAvecBouclier() : getImpactEnergie();
            combattant.perdreEnergie(degats);

            if (combattant.getMessageHandler() != null) {
                combattant.getMessageHandler().afficherMessage(
                    "Une bombe a explosé directement sur " +
                    combattant.getNom() + " à la position (" + caseCible.getPosition().getX() +
                    ", " + caseCible.getPosition().getY() + "), infligeant " + degats + " points de dégâts."
                );
            }
        }

        impacterVoisins(caseCible);
       
        caseCible.retirerExplosif();
    

    
    }

    /**
     * Explosion différée après un certain délai.
     *
     * @param caseCible La case où se trouve la bombe.
     */
    private void explosionAvecDelai(Case caseCible) {
        if (super.getDelaiExplosion() > 0) {
            super.setDelaiExplosion(super.getDelaiExplosion()-1); 
            return; 
        }

        impacterVoisins(caseCible);

        if (caseCible.getOccupant() != null && caseCible.getOccupant().getMessageHandler() != null) {
            caseCible.getOccupant().getMessageHandler().afficherMessage(
                "La bombe sur la case (" + caseCible.getPosition().getX() +
                ", " + caseCible.getPosition().getY() + ") a explosé aprés expiration de son delai."
            );
        }
      

        caseCible.retirerExplosif();
        caseCible.setExplosif(null);
    }

    /**
     * Implémentation unique de la méthode abstraite `explose`.
     * Gère les deux types d'explosions (immédiate et différée).
     *
     * @param combattant Le combattant sur la case (null si aucun combattant).
     * @param caseCible La case où se trouve la bombe.
     */
    @Override
    public void explose(Combattant combattant, Case caseCible) {
        if (combattant != null) {
            explosionImmediatement(combattant, caseCible);
        } else {
            explosionAvecDelai(caseCible);
        }
    }

    @Override
    public String toString() {
        return "3"; 
    }
}
