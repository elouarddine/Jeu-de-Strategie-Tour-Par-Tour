package vue;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import controle.Controleur;

/**
 * La classe Frame représente la fenêtre principale de l'application.
 * Elle utilise un CardLayout pour naviguer entre différentes vues.
 */
public class Frame extends JFrame {

    private static final long serialVersionUID = 1L;
    public static final int LARGEUR = 1000;
    public static final int HAUTEUR = 800;

    private Controleur controleur;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private  Panels panels;

    // Tableau pour représenter les cases (grille d'images)
    private JLabel[][] grilleImages;

    /**
     * Constructeur de la classe Frame.
     *
     * @param controleur Le contrôleur principal pour gérer les interactions.
     */
    public Frame(Controleur controleur) {
        this.controleur = controleur;

        // Configurer la fenêtre principale
        setTitle("Jeu de Combattants");
        setSize(LARGEUR, HAUTEUR);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        ajouterMenuAPropos();
        initialiserGrille(10,10);
        initialiserVues();

        add(cardPanel, BorderLayout.CENTER);
        setResizable(false);

        setVisible(true);
    }

    /**
     * Initialise les différentes vues de l'application et les ajoute au CardLayout.
     */
    private void initialiserVues() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        panels = new Panels(controleur, this);
        cardPanel.add(panels, "Jeu");


        afficherVue("Jeu"); 
    }

    /**
     * Met à jour l'image d'une case spécifiée dans la grille.
     *
     * @param x La coordonnée X de la case.
     * @param y La coordonnée Y de la case.
     * @param imagePath Le chemin de l'image à afficher.
     */
    public void mettreAJourImageCase(int x, int y, String imagePath) {
        if (this.grilleImages == null) {
            System.out.println("Erreur: grilleImages n'est pas initialisé !");
            return;
        }

        // Vérification du chemin de l'image
        if (imagePath == null) {
            System.err.println("Erreur: Chemin d'image invalide pour la case (" + x + ", " + y + ").");
            return;
        }

        // Créer un ImageIcon à partir du chemin de l'image
        ImageIcon imageIcon = new ImageIcon(imagePath);

        // Vérification que l'image a bien été chargée
        if (imageIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.err.println("Erreur: L'image n'a pas pu être chargée pour la position (" + x + ", " + y + ").");
            return;
        }

        // Créer un JLabel et y affecter l'image
        JLabel imageLabel = new JLabel(imageIcon);

        // Récupérer le JPanel de la case
        JPanel cell = (JPanel) panels.getGridPanel().getComponent(x + y * controleur.getLargeurGrille());  
        
        // Vider l'ancien contenu de la cellule
        cell.removeAll();

        // Ajouter le JLabel à la cellule
        cell.add(imageLabel);
        cell.revalidate();
        cell.repaint();

        // Redessiner la grille
        repaint();
    }




    /**
     * Change la vue affichée dans le CardLayout.
     *
     * @param vue Le nom de la vue à afficher (par exemple, "Accueil" ou "Jeu").
     */
    public void afficherVue(String vue) {
        cardLayout.show(cardPanel, vue);
    }

    /**
     * Ajoute un menu "À propos" à la fenêtre principale.
     */
    private void ajouterMenuAPropos() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuAPropos = new JMenu("À propos");
        menuAPropos.setFont(new Font("Arial", Font.BOLD, 14));

        JMenuItem infoItem = new JMenuItem("Informations");
        infoItem.setFont(new Font("Arial", Font.PLAIN, 12));
        infoItem.addActionListener(e -> afficherMessageAPropos());

        menuAPropos.add(infoItem);

        menuBar.add(menuAPropos);

        setJMenuBar(menuBar);
    }

    /**
     * Affiche une boîte de dialogue "À propos" contenant des informations sur l'application.
     */
    private void afficherMessageAPropos() {
        String message = "<html>" +
                "<h1 style='text-align: center;'>À propos</h1>" +
                "<p><b>Jeu de Combattants - Version 1.0</b></p>" +
                "<p>Application développée pour explorer les stratégies de combat.</p>" +
                "<p><b>Contributeurs :</b> <br> - ELOUARDI Salah Eddine 1<br> - EL-AASMI Yassine 2<br> - LA Aimad 3</p>" +
                "<p>Le jeu permet de simuler des combats, gérer des ressources, et développer des stratégies gagnantes.</p>" +
                "</html>";

        JLabel label = new JLabel(message);
        label.setFont(new Font("Arial", Font.PLAIN, 14));

        JOptionPane.showMessageDialog(this, label, "À propos", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Méthode pour initialiser la grille d'images de la fenêtre, par exemple pour les cases du jeu.
     *
     * @param rows Le nombre de lignes de la grille.
     * @param cols Le nombre de colonnes de la grille.
     */
    public void initialiserGrille(int rows, int cols) {
        // Initialisation de la grille d'images
        grilleImages = new JLabel[rows][cols];

        // Créer les labels pour chaque case et les ajouter à la grille
        JPanel grillePanel = new JPanel(new GridLayout(rows, cols));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grilleImages[i][j] = new JLabel();
                grillePanel.add(grilleImages[i][j]);
            }
        }

        // Ajouter la grille au panel principal
        add(grillePanel, BorderLayout.CENTER);
    }
}
