package model;

import utils.configuration.ConfigLoader;
import utils.strategyMessage.ConsoleMessageHandler;
import utils.strategyMessage.GraphicMessageHandler;
import utils.strategyMessage.MessageHandler;
import controle.Controleur;
import model.combattantFactory.CombattantFactory;
import model.factoryJoueur.JoueurFactory;
import model.factoryJoueur.TypeJoueur;
import model.factoryJoueur.JoueurFactory.TypeJoueurEnum;
import model.factoryJoueur.Mode;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ConsoleMessageHandler generalMessageHandler = new ConsoleMessageHandler();
        Scanner scanner = new Scanner(System.in);

         try {
            generalMessageHandler.afficherMessage("Chargement de la configuration...");
            ConfigLoader.load(); 
            generalMessageHandler.afficherMessage("Configuration chargée avec succès.");
        } catch (Exception e) {
            generalMessageHandler.afficherErreur("Erreur lors du chargement de la configuration : " + e.getMessage());
            return;
        }

        afficherBienvenue();

        generalMessageHandler.afficherMessage("Choisissez un mode d'affichage :");
        generalMessageHandler.afficherMessage("1. Console");
        generalMessageHandler.afficherMessage("2. Graphique");

        int choixMode = getChoixMode(scanner, generalMessageHandler);
        if (choixMode == 1) {
            executerConsole(scanner, generalMessageHandler);
        } else if (choixMode == 2) {
            executerGraphique(new GraphicMessageHandler(true));
        } else {
            generalMessageHandler.afficherErreur("Choix invalide. Terminaison du programme.");
        }
    }

    private static void afficherBienvenue() {
        System.out.println("\n\t\t  *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        System.out.println("\t\t  *       Bienvenue dans le jeu de combat !   *");
        System.out.println("\t\t  *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*\n");
    }

    private static int getChoixMode(Scanner scanner, ConsoleMessageHandler generalMessageHandler) {
        System.out.print("Votre choix : ");
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            generalMessageHandler.afficherErreur("Entrée invalide. Terminaison du programme.");
            return -1;
        }
    }

    public static void executerConsole(Scanner scanner, ConsoleMessageHandler generalMessageHandler) {
        clearConsole();
        afficherModeConsole();
        ConsoleMessageHandler combattantMessageHandler = new ConsoleMessageHandler();
        Grille grille = new Grille(combattantMessageHandler);
        grille.placerMurs();
        grille.placerPastillesEnergie();

        int totalJoueurs = demanderNombreJoueurs(scanner, generalMessageHandler);
        List<TypeJoueur> joueurs = configurerJoueurs(scanner, totalJoueurs, grille, combattantMessageHandler);

        List<Combattant> tousLesCombattants = new ArrayList<>();
        for (TypeJoueur joueur : joueurs) {
            tousLesCombattants.addAll(joueur.getCombattants());
        }

        grille.placerCombattants(tousLesCombattants);

        Orchestrateur orchestrateur = new Orchestrateur(joueurs, grille, generalMessageHandler);
        orchestrateur.demarrerPartie();
    }

    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void afficherModeConsole() {
        System.out.println("\t\t         * * * * * * * * * * * * * * * * * * *  * * * * * * * * * *");
        System.out.println("\t\t         *                                                        *");
        System.out.println("\t\t         *                   Mode Console Activée                 *");
        System.out.println("\t\t         *                                                        *");
        System.out.println("\t\t         * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
    }

    public static void executerGraphique(GraphicMessageHandler messageHandler) {
        new Controleur(messageHandler);
    }

    private static int demanderNombreJoueurs(Scanner scanner, ConsoleMessageHandler messageHandler) {
        int totalJoueurs;
        while (true) {
            messageHandler.afficherMessage("Entrez le nombre total de joueurs (min: 3, max: 5) : ");
            try {
                totalJoueurs = scanner.nextInt();
                scanner.nextLine();
                if (totalJoueurs >= 3 && totalJoueurs <= 5) {
                    break;
                } else {
                    messageHandler.afficherErreur("Le nombre de joueurs doit être entre 3 et 5.");
                }
            } catch (Exception e) {
                messageHandler.afficherErreur("Entrée invalide. Veuillez entrer un nombre entre 3 et 5.");
                scanner.nextLine();
            }
        }
        return totalJoueurs;
    }

    private static List<TypeJoueur> configurerJoueurs(Scanner scanner, int totalJoueurs, Grille grille, ConsoleMessageHandler combattantMessageHandler) {
        List<TypeJoueur> joueurs = new ArrayList<>();
        int nombreAI, nombreAleatoire, nombreHumain;

        while (true) {
            System.out.println("Configurez vos joueurs :");
            nombreAI = demanderNombre(scanner, "Entrez le nombre de joueurs AI : ", scanner, combattantMessageHandler);
            nombreAleatoire = demanderNombre(scanner, "Entrez le nombre de joueurs aléatoires : ", scanner, combattantMessageHandler);
            nombreHumain = demanderNombre(scanner, "Entrez le nombre de joueurs humains : ", scanner, combattantMessageHandler);

            if (nombreAI + nombreAleatoire + nombreHumain == totalJoueurs) {
                break;
            } else {
                System.err.println("La somme des joueurs doit être égale à " + totalJoueurs + ".");
            }
        }

        joueurs.addAll(creerJoueursAvecStrategie(TypeJoueurEnum.AI, nombreAI, grille, combattantMessageHandler));
        joueurs.addAll(creerJoueursAvecStrategie(TypeJoueurEnum.ALEATOIRE, nombreAleatoire, grille, combattantMessageHandler));
        joueurs.addAll(creerJoueursAvecStrategie(TypeJoueurEnum.HUMAIN, nombreHumain, grille, combattantMessageHandler));

        return joueurs;
    }

    private static int demanderNombre(Scanner scanner, String message, Scanner inputScanner, ConsoleMessageHandler messageHandler) {
        int nombre;
        while (true) {
            messageHandler.afficherMessage(message);
            try {
                nombre = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (Exception e) {
                messageHandler.afficherErreur("Entrée invalide. Veuillez entrer un nombre.");
                scanner.nextLine();
            }
        }
        return nombre;
    }

    private static List<TypeJoueur> creerJoueursAvecStrategie(TypeJoueurEnum type, int nombre, Grille grille, MessageHandler combattantMessageHandler) {
        List<TypeJoueur> joueurs = new ArrayList<>();
        for (int i = 1; i <= nombre; i++) {
            List<Combattant> combattants = CombattantFactory.creerListeCombattants(1, grille, combattantMessageHandler);
            TypeJoueur joueur = JoueurFactory.creerJoueurAvecStrategie(type, combattants, Mode.CONSOLE);
            joueurs.add(joueur);
        }
        System.out.println(joueurs.size() + " joueur(s) de type " + type + " ajouté(s).");
        return joueurs;
    }
}
