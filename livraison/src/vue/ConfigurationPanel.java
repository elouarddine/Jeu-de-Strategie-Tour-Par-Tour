package vue;

import java.awt.*;
import javax.swing.*;
import javax.swing.JSplitPane;
import controle.Controleur;

/**
 * Classe ConfigurationPanel pour afficher la JTable et la grille de jeu organisées avec JSplitPane.
 */
public class ConfigurationPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
	private Controleur controleur;

    /**
     * Constructeur pour initialiser le ConfigurationPanel.
     *
     * @param controleur Le contrôleur principal.
     * @param largeurDufenetre La largeur de la fenêtre.
     * @param hauteurDufenetre La hauteur de la fenêtre.
     */
    public ConfigurationPanel(Controleur controleur, int largeurDufenetre, int hauteurDufenetre , Frame frame) {
        this.controleur = controleur;
        setLayout(new BorderLayout());

        Panels tablePanel = new Panels(controleur , frame);

        JPanel gridPanel = Panels.creerGrillePanel(controleur);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePanel, gridPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.3);

        add(splitPane, BorderLayout.CENTER);

        setPreferredSize(new Dimension(largeurDufenetre, hauteurDufenetre));
    }

    


}
