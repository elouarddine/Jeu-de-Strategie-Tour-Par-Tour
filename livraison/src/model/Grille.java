package model;

import utils.configuration.Parametres;
import utils.strategyMessage.MessageHandler;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import model.proxy.GrilleInterface;

/**
 * Classe représentant la grille de jeu.
 * <p>
 * La grille est composée de cases et gère les combattants, les murs, les pastilles d'énergie, etc.
 * Elle fournit des méthodes pour initialiser le jeu, placer les éléments et gérer les actions au sein de la grille.
 */
public class Grille implements GrilleInterface {

    /** Tableau bidimensionnel représentant les cases de la grille. */
    private Case[][] cases;

    /** Liste des combattants présents sur la grille. */
    private List<Combattant> combattants;

    /** Générateur de nombres aléatoires pour placer les éléments aléatoirement. */
    private Random random;

    /** Gestionnaire de messages pour afficher les informations du jeu. */
    private MessageHandler messageHandler;

    /** Largeur de la grille (nombre de cases en X). */
    private int largeur;

    /** Longueur de la grille (nombre de cases en Y). */
    private int longueur;

    /**
     * Constructeur de la classe Grille.
     *
     * @param messageHandler Le gestionnaire de messages pour afficher les informations du jeu.
     */
    public Grille(MessageHandler messageHandler) {
        this.largeur = Parametres.largeur;
        this.longueur = Parametres.longueur;
        this.cases = new Case[largeur][longueur];
        this.combattants = new ArrayList<>();
        this.random = new Random();
        this.messageHandler = messageHandler;

        // Initialisation des cases de la grille
        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < longueur; y++) {
                cases[x][y] = new Case(new Position(x, y), this);
            }
        }
    }

    /**
     * Obtient la longueur de la grille.
     *
     * @return La longueur de la grille.
     */
    public int getLongueur() {
        return this.longueur;
    }

    /**
     * Vérifie si une position donnée est valide au sein de la grille.
     *
     * @param pos La position à vérifier.
     * @return true si la position est valide, false sinon.
     */
    public boolean estPositionValide(Position pos) {
        if (pos == null) {
            return false;
        }
        int x = pos.getX();
        int y = pos.getY();
        return x >= 0 && x < largeur && y >= 0 && y < longueur;
    }

    /**
     * Obtient la liste de tous les combattants présents sur la grille.
     *
     * @return La liste des combattants.
     */
    public List<Combattant> getAllCombattants() {
        return this.combattants;
    }

    /**
     * Obtient la largeur de la grille.
     *
     * @return La largeur de la grille.
     */
    public int getLargeur() {
        return this.largeur;
    }

    /**
     * Obtient le gestionnaire de messages.
     *
     * @return Le gestionnaire de messages.
     */
    public MessageHandler getMessageHandler() {
        return this.messageHandler;
    }

    /**
     * Définit le gestionnaire de messages.
     *
     * @param messageHandler Le nouveau gestionnaire de messages.
     */
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * Obtient la case à une position donnée.
     *
     * @param position La position de la case.
     * @return La case correspondante, ou null si la position est invalide.
     */
    public Case getCase(Position position) {
        if (position == null || position.getX() < 0 || position.getX() >= largeur
                || position.getY() < 0 || position.getY() >= longueur) {
            return null;
        }
        return cases[position.getX()][position.getY()];
    }

    /**
     * Gère les explosions différées des bombes sur la grille.
     * Parcourt toutes les cases et fait exploser les bombes si nécessaire.
     */
    public void gererExplosionsDifferrees() {
        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < longueur; y++) {
                Case caseCourante = cases[x][y];
                Explosif explosif = caseCourante.getExplosif();

                if (explosif instanceof Bombe) {
                    Bombe bombe = (Bombe) explosif;
                    bombe.explose(null, caseCourante);
                }
            }
        }
    }

    /**
     * Récupère les positions des explosifs (mines, bombes) sur la grille.
     *
     * @return Une liste des positions des explosifs.
     */
    public List<Position> getPositionsExplosif() {
        List<Position> positionsExplosifs = new ArrayList<>();
        
       for (int i = 0; i < this.getLongueur(); i++) {
            for (int j = 0; j < this.getLargeur(); j++) {
                Case caseGrille = this.getCase(new Position(i, j));
                if (caseGrille != null && caseGrille.getExplosif() != null) {
                	positionsExplosifs.add(new Position(i, j));
                }
            }
        }

        return positionsExplosifs;
    }
    
    /**
     * Place des éléments sur la grille selon une condition et une action.
     *
     * @param nombre     Nombre d'éléments à placer.
     * @param nomElement Nom de l'élément (pour les messages).
     * @param condition  Condition pour placer l'élément.
     * @param action     Action à effectuer pour placer l'élément.
     */
    private void placerElements(int nombre, String nomElement, Predicate<Case> condition, Consumer<Case> action) {
        int placementsEffectues = 0;

        while (placementsEffectues < nombre) {
            Position positionAleatoire = new Position(random.nextInt(largeur), random.nextInt(longueur));
            Case caseCible = getCase(positionAleatoire);

            if (caseCible != null && !caseCible.estOccupee(true) && condition.test(caseCible)) {
                action.accept(caseCible);
                placementsEffectues++;
            }
        }
    }

    /**
     * Place des murs aléatoirement sur la grille.
     * Le nombre de murs est déterminé par les paramètres du jeu.
     */
    public void placerMurs() {
        int nombreMurs = random.nextInt(Parametres.nombreMaxMurs - Parametres.nombreMinMurs + 1) + Parametres.nombreMinMurs;
        placerElements(
                nombreMurs,
                "Mur",
                caseCible -> !caseCible.estUnMur(),
                caseCible -> caseCible.setEstMur(true)
        );
    }

    /**
     * Place les combattants sur la grille à des positions aléatoires non occupées.
     *
     * @param combattantsAPlacer La liste des combattants à placer.
     */
    public void placerCombattants(List<Combattant> combattantsAPlacer) {
        for (Combattant combattant : combattantsAPlacer) {
            placerElements(
                    1,
                    "Combattant " + combattant.getNom(),
                    caseCible -> caseCible.getOccupant() == null,
                    caseCible -> {
                        caseCible.setOccupant(combattant);
                        combattant.setPosition(caseCible.getPosition());
                        combattants.add(combattant);
                    }
            );
        }
    }

    /**
     * Place des pastilles d'énergie aléatoirement sur la grille.
     * Le nombre de pastilles est déterminé par les paramètres du jeu.
     */
    public void placerPastillesEnergie() {
        int nombrePastilles = random.nextInt(Parametres.nombreMaxPastilles - Parametres.nombreMinPastilles + 1) + Parametres.nombreMinPastilles;
        placerElements(
                nombrePastilles,
                "Pastille d'énergie",
                caseCible -> !caseCible.contientPastilleEnergie(),
                caseCible -> {
                    Pastille nouvellePastille = new Pastille(caseCible.getPosition(), this);
                    caseCible.setContientPastilleEnergie(nouvellePastille);
                }
        );
    }

    /**
     * Recherche les combattants proches d'une position donnée dans la grille.
     *
     * @param pos    La position de référence.
     * @param portee La portée de détection.
     * @return La liste des combattants proches de la position donnée.
     */
    public List<Combattant> getCombattantsProches(Position pos, int portee) {
        List<Combattant> ennemisProches = new ArrayList<>();

        for (Combattant c : this.getAllCombattants()) {
            if (c.getPosition().equals(pos)) {
                continue;
            }
            double distance = pos.distanceTo(c.getPosition());
            if (distance <= portee) {
                ennemisProches.add(c);
            }
        }

        return ennemisProches;
    }

    /**
     * Obtient la position centrale de la grille.
     *
     * @return La position centrale.
     */
    public Position getPositionCentrale() {
        int centreX = this.getLargeur() / 2;
        int centreY = this.getLongueur() / 2;
        return new Position(centreX, centreY);
    }

    /**
     * Génère la grille en plaçant les murs, les pastilles d'énergie et les combattants.
     *
     * @param combattantsAPlacer La liste des combattants à placer sur la grille.
     */
    public void genererGrille(List<Combattant> combattantsAPlacer) {
        placerMurs();
        placerPastillesEnergie();
        placerCombattants(combattantsAPlacer);
    }

    /**
     * Affiche la grille dans la console.
     * Chaque case est représentée par un caractère spécifique.
     */
    public void afficherGrille() {
        StringBuilder grilleRepresentation = new StringBuilder();
        grilleRepresentation.append("\n");
        grilleRepresentation.append("Grille Réel ");
        grilleRepresentation.append("\n");

        for (int y = 0; y < longueur; y++) {
            for (int x = 0; x < largeur; x++) {
                grilleRepresentation.append(" ").append(cases[x][y].toString()).append(" ");
            }
            grilleRepresentation.append("\n");
        }

        System.out.println(grilleRepresentation);
    }

    /**
     * Vérifie si le jeu est terminé.
     * Le jeu est considéré comme terminé s'il reste un seul combattant en vie ou moins.
     *
     * @return true si le jeu est terminé, false sinon.
     */
    public boolean isOver() {
        long joueursRestants = combattants.stream().filter(Combattant::estEnVie).count();
        return joueursRestants <= 1;
    }

    /**
     * Obtient le gagnant du jeu s'il y en a un.
     *
     * @return Le combattant gagnant, ou null s'il n'y a pas de gagnant.
     */
    public Combattant getWinner() {
        return combattants.stream().filter(Combattant::estEnVie).findFirst().orElse(null);
    }
}
