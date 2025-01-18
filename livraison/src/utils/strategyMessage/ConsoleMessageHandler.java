package utils.strategyMessage;

import java.util.List;
import java.util.Scanner;

/**
 * Implémentation de l'interface MessageHandler pour afficher les messages dans la console.
 */
public class ConsoleMessageHandler implements MessageHandler {

    private Scanner scanner;      // Scanner pour lire les entrées utilisateur

    /**
     * Constructeur de la classe ConsoleMessageHandler.
     *
     * @param joueurHumain Si true, les messages seront affichés.
     */
    public ConsoleMessageHandler() {
            this.scanner = new Scanner(System.in);
    }

    /**
     * Affiche un message d'information dans la console si le joueur est humain.
     *
     * @param message Le message à afficher.
     */
    @Override
    public void afficherMessage(String message) {
            System.out.println("{Message} : " + message);
    }

    /**
     * Affiche un message d'erreur dans la console si le joueur est humain.
     *
     * @param erreur Le message d'erreur à afficher.
     */
    @Override
    public void afficherErreur(String erreur) {
            System.err.println("{Erreur} : " + erreur);
    }

    /**
     * Demande une entrée utilisateur via la console.
     *
     * @param prompt Le message d'invite à afficher à l'utilisateur.
     * @return La chaîne de caractères saisie par l'utilisateur.
     */
    @Override
    public String demanderInput(String prompt) {
        if (!prompt.isEmpty()) {
            System.out.print("\n"+prompt+"\n");
        }
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return null;
    }


    @Override
    public String demanderChoixParmiOptions(String message, List<String> options) {
        afficherMessage(message);
        for (int i = 0; i < options.size(); i++) {
            afficherMessage((i + 1) + ": " + options.get(i));
        }
        while (true) {
            afficherMessage("Entrez le numéro de votre choix : ");
            String input = scanner.nextLine();
            try {
                int choix = Integer.parseInt(input);
                if (choix >= 1 && choix <= options.size()) {
                    return options.get(choix - 1);
                } else {
                    afficherMessage("Choix invalide. Veuillez réessayer.");
                }
            } catch (NumberFormatException e) {
                afficherMessage("Entrée invalide. Veuillez entrer un numéro.");
            }
        }
    }





}






