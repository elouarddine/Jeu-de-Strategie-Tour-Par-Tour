package utils.strategyMessage;

import java.util.List;

import javax.swing.JOptionPane;

/**
 * Implémentation de l'interface MessageHandler pour afficher les messages via une interface graphique.
 */
public class GraphicMessageHandler implements MessageHandler {

    private boolean joueurHumain; 

    /**
     * Constructeur de la classe GraphicMessageHandler.
     *
     * @param joueurHumain Si true, les messages seront affichés.
     */
    public GraphicMessageHandler(boolean joueurHumain) {
        this.joueurHumain = joueurHumain;
    }

    /**
     * Affiche un message d'information dans une boîte de dialogue graphique si le joueur est humain.
     *
     * @param message Le message à afficher.
     */
    @Override
    public void afficherMessage(String message) {
        if (joueurHumain) {
            JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Affiche un message d'erreur dans une boîte de dialogue graphique si le joueur est humain.
     *
     * @param erreur Le message d'erreur à afficher.
     */
    @Override
    public void afficherErreur(String erreur) {
        if (joueurHumain) {
            JOptionPane.showMessageDialog(null, erreur, "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Demande une entrée utilisateur via une boîte de dialogue graphique.
     *
     * @param prompt Le message d'invite à afficher à l'utilisateur.
     * @return La chaîne de caractères saisie par l'utilisateur.
     */
    @Override
    public String demanderInput(String prompt) {
        if (!joueurHumain) {
            return "";
        }
        return JOptionPane.showInputDialog(null, prompt);
    }
    
    @Override
    public String demanderChoixParmiOptions(String message, List<String> options) {
        String[] optionsArray = options.toArray(new String[0]);
        String selectedOption = (String) JOptionPane.showInputDialog(
                null,
                message,
                "Sélection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                optionsArray,
                optionsArray[0]);

        return selectedOption; 
    }
    



}
