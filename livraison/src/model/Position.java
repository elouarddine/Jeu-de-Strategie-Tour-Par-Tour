package model;

import java.util.Objects;

/**
 * Classe représentant une position sur la grille de jeu.
 * <p>
 * Une position est définie par ses coordonnées x et y.
 */
public class Position {
    
    /** La coordonnée x de la position. */
    private int x;
    
    /** La coordonnée y de la position. */
    private int y;

    /**
     * Constructeur de la classe Position.
     *
     * @param x La coordonnée en x.
     * @param y La coordonnée en y.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Obtient la coordonnée x de la position.
     *
     * @return La coordonnée x.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Obtient la coordonnée y de la position.
     *
     * @return La coordonnée y.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Vérifie si une autre position est voisine de cette position.
     *
     * @param autre         L'autre position à comparer.
     * @param huitDirection true pour considérer les cases adjacentes en diagonale, false pour uniquement les cases adjacentes orthogonales.
     * @return true si les positions sont voisines selon le paramètre huitDirection, false sinon.
     */
    public boolean estVoisine(Position autre, boolean huitDirection) {
        int diffX = Math.abs(autre.x - this.x);
        int diffY = Math.abs(autre.y - this.y);

        if (huitDirection) {
            return diffX <= 1 && diffY <= 1 && (diffX != 0 || diffY != 0);
        } else {
            return (diffX == 1 && diffY == 0) || (diffY == 1 && diffX == 0);
        }
    }

    /**
     * Calcule la distance de Manhattan entre cette position et une autre position.
     *
     * @param autrePosition L'autre position à laquelle calculer la distance.
     * @return La distance de Manhattan entre les deux positions.
     * @throws IllegalArgumentException si l'autre position est nulle.
     */
    public int distanceTo(Position autrePosition) {
        if (autrePosition == null) {
            throw new IllegalArgumentException("La position cible ne peut pas être nulle.");
        }

        return Math.abs(this.getX() - autrePosition.getX()) +
               Math.abs(this.getY() - autrePosition.getY());
    }

    /**
     * Vérifie l'égalité entre cette position et un autre objet.
     *
     * @param obj L'objet à comparer avec cette position.
     * @return true si l'objet est une position avec les mêmes coordonnées, false sinon.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; 
        if (obj == null || getClass() != obj.getClass()) return false; 

        Position position = (Position) obj;
        return x == position.x && y == position.y; 
    }

    /**
     * Génère un code de hachage pour cette position.
     *
     * @return Le code de hachage basé sur les coordonnées x et y.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Retourne une représentation en chaîne de caractères de la position.
     *
     * @return Une chaîne de la forme "(x, y)".
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
