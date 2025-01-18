package model.proxy;

import java.util.HashMap;
import java.util.Map;

import model.Case;
import model.Combattant;
import model.Grille;
import model.Position;

/**
 * Classe Proxy pour la grille de jeu, implémentant le pattern Proxy.
 * <p>
 * Cette classe permet de contrôler l'accès à la grille réelle en fournissant
 * une vue filtrée pour un joueur spécifique. Elle cache certaines informations
 * selon le contexte du joueur, par exemple pour gérer la visibilité dans le jeu.
 */
public class ProxyGrille implements GrilleInterface {

    /** La grille réelle à laquelle le proxy donne accès. */
    private Grille grilleReelle; 

    /** Le joueur pour lequel la vue filtrée est générée. */
    private Combattant joueur;  

    /** Cache des cases filtrées pour optimiser les performances. */
    private Map<Position, Case> cacheCasesFiltrees = new HashMap<>();

    /**
     * Constructeur de la classe ProxyGrille.
     *
     * @param grilleReelle La grille réelle à laquelle le proxy donne accès.
     * @param joueur       Le joueur pour lequel la vue filtrée est générée.
     */
    public ProxyGrille(Grille grilleReelle, Combattant joueur) {
        this.grilleReelle = grilleReelle;
        this.joueur = joueur;
    }

    /**
     * Obtient la case filtrée à une position donnée pour le joueur.
     *
     * @param position La position de la case à obtenir.
     * @return La case filtrée pour le joueur, ou null si la position est invalide.
     */
    @Override
    public Case getCase(Position position) {
        if (cacheCasesFiltrees.containsKey(position)) {
            return cacheCasesFiltrees.get(position);
        }

        Case caseReelle = grilleReelle.getCase(position);
        if (caseReelle == null) {
            return null; 
        }

        Case caseFiltree = caseReelle.copierAvecFiltrage(joueur);
        cacheCasesFiltrees.put(position, caseFiltree);
        return caseFiltree;
    }

    /**
     * Vide le cache des cases filtrées.
     * À utiliser si l'état de la grille a changé et que le cache doit être mis à jour.
     */
    public void clearCache() {
        cacheCasesFiltrees.clear();
    }

    /**
     * Affiche la grille filtrée dans la console pour le joueur.
     * Seules les informations visibles par le joueur seront affichées.
     */
    @Override
    public void afficherGrille() {
        try {
            StringBuilder grilleRepresentation = new StringBuilder();
            grilleRepresentation.append("\n");
            grilleRepresentation.append("Proxy Grille ");
            grilleRepresentation.append("\n");

            for (int y = 0; y < grilleReelle.getLongueur(); y++) {
                for (int x = 0; x < grilleReelle.getLargeur(); x++) {
                    Case caseFiltree = getCase(new Position(x, y));
                    grilleRepresentation.append(" ").append(caseFiltree).append(" ");
                }
                grilleRepresentation.append("\n");
            }

            joueur.getMessageHandler().afficherMessage("Vue personnalisée pour " + joueur.getNom() + " :\n" + grilleRepresentation);
        } catch (Exception e) {
            joueur.getMessageHandler().afficherErreur("Erreur lors de l'affichage de la grille : " + e.getMessage());
        }
    }

    /**
     * Obtient la grille réelle.
     *
     * @return La grille réelle.
     */
    public Grille getGrilleReelle() {
        return grilleReelle;
    }

    /**
     * Obtient le joueur pour lequel le proxy est créé.
     *
     * @return Le joueur.
     */
    public Combattant getJoueur() {
        return joueur;
    }
}
