package model;

import java.util.List;
import java.util.Scanner;

import model.factoryJoueur.TypeJoueur;
import utils.strategyMessage.MessageHandler;

/**
 * Classe Orchestrateur qui gère une partie de jeu.
 */
public class Orchestrateur {

    private final Grille grille;
    private final List<TypeJoueur> joueurs;
    private final MessageHandler messageHandler;
    private final Scanner scanner;

    /**
     * Constructeur de la classe Orchestrateur.
     *
     * @param joueurs Liste des joueurs participant à la partie.
     * @param grille La grille du jeu.
     * @param messageHandler Gestionnaire de messages pour l'affichage.
     */
    public Orchestrateur(List<TypeJoueur> joueurs, Grille grille, MessageHandler messageHandler) {
        if (joueurs == null || joueurs.isEmpty()) {
            throw new IllegalArgumentException("La liste des joueurs ne peut pas être nulle ou vide.");
        }
        if (grille == null) {
            throw new IllegalArgumentException("La grille ne peut pas être nulle.");
        }
        if (messageHandler == null) {
            throw new IllegalArgumentException("Le MessageHandler ne peut pas être nul.");
        }

        this.joueurs = joueurs;
        this.grille = grille;
        this.messageHandler = messageHandler;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Démarre et gère la boucle principale du jeu.
     */
    public void demarrerPartie() {
        messageHandler.afficherMessage("Début de la partie !");
        boolean partieEnCours = true;

        while (partieEnCours) {
            

        	for (TypeJoueur joueur : joueurs) {
                messageHandler.afficherMessage("\nTour du joueur de type : " + joueur.getType());

                joueur.jouerTour(grille);
                grille.gererExplosionsDifferrees();

                grille.afficherGrille();

                afficherEnergies();

                attendreEntrer();

                effacerEcran();

                if (grille.isOver()) {
                    partieEnCours = false;
                    break;
                }
            }
        }

        afficherResultatFinal();
    }

    /**
     * Affiche le résultat final de la partie.
     */
    private void afficherResultatFinal() {
        Combattant gagnant = grille.getWinner();
        messageHandler.afficherMessage("\nLa partie est terminée !");
        if (gagnant != null) {
            messageHandler.afficherMessage("Le gagnant est : " + gagnant.getNom());
        } else {
            messageHandler.afficherMessage("Match nul !");
        }
    }

    /**
     * Affiche l'énergie de tous les combattants.
     */
    private void afficherEnergies() {
        StringBuilder sb = new StringBuilder("\nÉnergie des combattants :\n");
        for (TypeJoueur joueur : joueurs) {
            for (Combattant combattant : joueur.getCombattants()) {
                sb.append(combattant.getNom())
                  .append(" (")
                  .append(joueur.getType())
                  .append(") : ")
                  .append(combattant.getEnergie())
                  .append(" Energie\n");
            }
        }
        messageHandler.afficherMessage(sb.toString());
    }

    /**
     * Attend que l'utilisateur appuie sur Entrée pour continuer.
     */
    private void attendreEntrer() {
        messageHandler.afficherMessage("Appuyez sur Entrée pour passer au tour suivant...");
        scanner.nextLine();
    }

    /**
     * Efface l'écran de la console en utilisant des séquences d'échappement ANSI.
     */
    private void effacerEcran() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
