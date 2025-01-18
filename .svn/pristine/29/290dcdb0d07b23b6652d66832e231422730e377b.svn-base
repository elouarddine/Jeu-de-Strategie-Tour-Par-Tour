package model;

import utils.configuration.TypeEquipement;

public class Mine extends Explosif {

	public Mine(Combattant porteur, TypeEquipement type) {
        super(porteur, type);
    }
    /**
     * Implémentation de l'explosion de la mine.
     * Affecte uniquement le combattant qui marche dessus et impacte ses voisins via `impacterVoisins`.
     *
     * @param combattant Le combattant sur la case (ne peut pas être null pour déclencher l'explosion).
     * @param caseCible La case où se trouve la mine.
     */
    @Override
    public void explose(Combattant combattant, Case caseCible) {
        if (combattant == null) {
            return; 
        }

        int degats = combattant.estBouclierActif() ? getCoutEnergieAvecBouclier() : getImpactEnergie();
        combattant.perdreEnergie(degats);

        if (combattant.getMessageHandler() != null) {
            combattant.getMessageHandler().afficherMessage(
                "Une mine a explosé sur " + combattant.getNom() +
                " à la position (" + caseCible.getPosition().getX() + ", " + caseCible.getPosition().getY() +
                "), infligeant " + degats + " points de dégâts."
            );
        }

        impacterVoisins(caseCible);

        caseCible.retirerExplosif();
    }

    @Override
    public String toString() {
        return "§"; 
    }
}
