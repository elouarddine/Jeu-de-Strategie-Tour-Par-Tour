package utils.strategyMessage;

import java.util.List;

/**
 * Interface MessageHandler définissant une stratégie d'affichage de messages.
 * Cette interface est utilisée pour respecter le modèle MVC en isolant les modes d'affichage des messages (console ou graphique) via le pattern Strategy.
 */
public interface MessageHandler {

    /**
     * Affiche un message d'information.
     *
     * @param message Le message à afficher.
     */
    void afficherMessage(String message);

    /**
     * Affiche un message d'erreur.
     *
     * @param erreur Le message d'erreur à afficher.
     */
    void afficherErreur(String erreur);

   
    /**
     * Demande une entrée utilisateur via une boîte de dialogue graphique.
     *
     * @param prompt Le message d'invite à afficher à l'utilisateur.
     * @return La chaîne de caractères saisie par l'utilisateur.
     */
    String demanderInput(String message);
    public String demanderChoixParmiOptions(String message, List<String> options);

}
