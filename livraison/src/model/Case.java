package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une case dans la grille de jeu.
 * <p>
 * Une case peut contenir un combattant, un explosif, une pastille d'énergie, ou être un mur.
 * Elle est définie par sa position sur la grille et peut fournir des informations sur son état.
 */
public class Case {

    /** La position de la case sur la grille. */
    private Position position;

    /** Le combattant occupant actuellement la case, s'il y en a un. */
    private Combattant occupant;

    /** L'explosif présent sur la case, s'il y en a un (un seul explosif par case). */
    private Explosif explosif; // Un seul explosif par case

    /** La pastille d'énergie présente sur la case, s'il y en a une. */
    private Pastille pastilleEnergie; // Une seule pastille par case

    /** Indique si la case contient une pastille d'énergie. */
    private boolean contientPastilleEnergie;

    /** Indique si la case est un mur. */
    private boolean estMur;

    /** Référence à la grille de jeu. */
    private Grille grille; // Référence à la grille

    /**
     * Constructeur de la classe Case.
     *
     * @param position La position de la case sur la grille.
     * @param grille   La grille de jeu à laquelle appartient la case.
     */
    public Case(Position position, Grille grille) {
        this.position = position;
        this.grille = grille;
    }

    /**
     * Obtient la grille de jeu.
     *
     * @return La grille de jeu.
     */
    public Grille getGrille() {
        return this.grille;
    }

    /**
     * Obtient la position de la case sur la grille.
     *
     * @return La position de la case.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Obtient le combattant occupant actuellement la case.
     *
     * @return Le combattant occupant la case, ou null s'il n'y en a pas.
     */
    public Combattant getOccupant() {
        return occupant;
    }

    /**
     * Définit le combattant occupant la case.
     *
     * @param occupant Le combattant à placer sur la case.
     */
    public void setOccupant(Combattant occupant) {
        this.occupant = occupant;
    }

    /**
     * Ajoute un explosif sur la case.
     *
     * @param explosif L'explosif à ajouter.
     * @throws IllegalStateException si la case contient déjà un explosif.
     */
    public void ajouterExplosif(Explosif explosif) {
        if (this.explosif != null) {
            throw new IllegalStateException("La case contient déjà un explosif !");
        }
        this.explosif = explosif;
    }

    /**
     * Retire l'explosif présent sur la case.
     */
    public void retirerExplosif() {
        this.explosif = null;
    }

    /**
     * Obtient l'explosif présent sur la case.
     *
     * @return L'explosif présent sur la case, ou null s'il n'y en a pas.
     */
    public Explosif getExplosif() {
        return explosif;
    }

    /**
     * Définit l'explosif présent sur la case.
     *
     * @param explosif L'explosif à placer sur la case.
     */
    public void setExplosif(Explosif explosif) {
        this.explosif = explosif;
    }

    /**
     * Obtient la pastille d'énergie présente sur la case.
     *
     * @return La pastille d'énergie présente sur la case, ou null s'il n'y en a pas.
     */
    public Pastille getPastilleEnergie() {
        return this.pastilleEnergie;
    }

    /**
     * Définit la pastille d'énergie présente sur la case.
     *
     * @param pastilleEnergie La pastille d'énergie à placer sur la case.
     */
    public void setContientPastilleEnergie(Pastille pastilleEnergie) {
        this.contientPastilleEnergie = (pastilleEnergie != null);
        this.pastilleEnergie = pastilleEnergie;
    }

    /**
     * Vérifie si la case contient une pastille d'énergie.
     *
     * @return true si la case contient une pastille d'énergie, false sinon.
     */
    public boolean contientPastilleEnergie() {
        return contientPastilleEnergie;
    }

    /**
     * Définit si la case contient une pastille d'énergie.
     *
     * @param contientPastilleEnergie true si la case contient une pastille d'énergie, false sinon.
     */
    public void setContientPastilleEnergie(boolean contientPastilleEnergie) {
        this.contientPastilleEnergie = contientPastilleEnergie;
    }

    /**
     * Vérifie si la case est occupée.
     *
     * @param inclureExplosifs true pour considérer les explosifs comme occupant la case.
     * @return true si la case est occupée (par un mur, un combattant ou un explosif selon le paramètre), false sinon.
     */
    public boolean estOccupee(boolean inclureExplosifs) {
        if (inclureExplosifs) {
            return estMur || occupant != null || explosif != null;
        }
        return estMur || occupant != null;
    }

    /**
     * Vérifie si la case est un mur.
     *
     * @return true si la case est un mur, false sinon.
     */
    public boolean estUnMur() {
        return estMur;
    }

    /**
     * Définit si la case est un mur.
     *
     * @param estMur true si la case est un mur, false sinon.
     */
    public void setEstMur(boolean estMur) {
        this.estMur = estMur;
    }

    /**
     * Obtient la liste des cases voisines de cette case.
     *
     * @return La liste des cases voisines.
     */
    public List<Case> getCasesVoisines() {
        List<Case> casesVoisines = new ArrayList<>();
        Position positionActuelle = this.getPosition();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                Position positionVoisine = new Position(
                        positionActuelle.getX() + dx,
                        positionActuelle.getY() + dy
                );

                Case caseVoisine = grille.getCase(positionVoisine);
                if (caseVoisine != null) {
                    casesVoisines.add(caseVoisine);
                }
            }
        }

        return casesVoisines;
    }

    /**
     * Crée une copie de la case avec filtrage des informations en fonction du combattant.
     * Les explosifs non visibles pour le combattant ne seront pas inclus dans la copie.
     *
     * @param combattant Le combattant pour lequel la copie est créée.
     * @return Une copie de la case avec filtrage des informations.
     */
    public Case copierAvecFiltrage(Combattant combattant) {
        Case copie = new Case(this.position, this.grille);
        copie.contientPastilleEnergie = this.contientPastilleEnergie;
        if (this.contientPastilleEnergie()) {
            copie.setContientPastilleEnergie(true);
        }
        copie.estMur = this.estMur;
        copie.occupant = this.occupant;

        if (explosif != null && explosif.estVisiblePour(combattant)) {
            copie.explosif = this.explosif;
        }

        return copie;
    }

    /**
     * Retourne une représentation textuelle de la case.
     *
     * @return Un caractère représentant l'état de la case.
     */
    @Override
    public String toString() {

        if (estMur) {
            return "#";
        }
        if (this.occupant != null) {
            return occupant.toString();
        }
        if (this.explosif != null) {
            return explosif.toString();
        }
        if (this.pastilleEnergie != null) {
            return pastilleEnergie.toString();
        }
        return ".";
    }

}
