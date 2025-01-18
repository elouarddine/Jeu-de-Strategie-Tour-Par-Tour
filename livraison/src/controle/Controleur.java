package controle;

import java.awt.FlowLayout;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import java.io.File;
import java.io.InputStream;
import java.io.FileNotFoundException;

import model.Case;
import model.Combattant;
import model.Explosif;
import model.Grille;
import model.Position;
import model.combattantFactory.CombattantFactory;
import model.factoryJoueur.JoueurFactory;
import model.factoryJoueur.Mode;
import model.factoryJoueur.JoueurFactory.TypeJoueurEnum;
import model.factoryJoueur.TypeJoueur;
import utils.configuration.Parametres;
import utils.configuration.TypeEquipement;
import utils.strategyMessage.MessageHandler;
import vue.Frame;

public class Controleur {

    private MessageHandler message;
    private Frame frame;
    private Grille grille;
    private List<TypeJoueur> joueurs;
    private int indiceJoueurActuel = 0;
    private boolean partieEnCours = true;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public Controleur(MessageHandler message) {
        this.message = message;
        this.joueurs = new ArrayList<>();
        initialiserJeu();
        initialiserFrame();
    }

    /**
     * Initialise le jeu en configurant la grille et les joueurs.
     */
    public void initialiserJeu() {
        joueurs.clear();
        grille = new Grille(message);
        grille.placerMurs();
        grille.placerPastillesEnergie();
        configurerJoueurs();
        placerJoueurs(); 
    }

    /**
     * Lance la partie en commençant par le premier joueur.
     */
    public void lancerPartie() {
        if (!partieEnCours) {
            afficherResultatFinal();
            return;
        }

        jouerTourJoueurCourant();
    }
    
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
    
    /**
     * Gère le tour du joueur courant.
     */
    
    public void jouerTourJoueurCourant() {
        
    	if (!partieEnCours) {
            afficherResultatFinal();
            return;
        }

        TypeJoueur joueurActuel = joueurs.get(indiceJoueurActuel);
        message.afficherMessage("\nTour du joueur de type : " + joueurActuel.getType());

        joueurActuel.jouerTour(grille);
        pcs.firePropertyChange("modeleMisAJour", null, null);

        grille.gererExplosionsDifferrees();
        pcs.firePropertyChange("modeleMisAJour", null, null);

        for (Position positionExplosif : grille.getPositionsExplosif()) {
            afficherExplosion(positionExplosif);
        }

        if (grille.isOver()) {
            partieEnCours = false;
            afficherResultatFinal();
            return;
        }

        indiceJoueurActuel++;
        if (indiceJoueurActuel >= joueurs.size()) {
            indiceJoueurActuel = 0;
        }
    }

    private void afficherExplosion(Position position) {
        SwingUtilities.invokeLater(() -> {

        	String cheminExplosion = "ressources/explosion.png";  
            setImage(position, cheminExplosion);  

            Timer timer = new Timer(1000, e -> {
                String cheminImageOriginale = getImagePourPosition(position.getX(), position.getY());
                setImage(position, cheminImageOriginale); 
                pcs.firePropertyChange("modeleMisAJour", null, null);  
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    



    /**
     * Affiche le résultat final de la partie.
     */
    private void afficherResultatFinal() {
        Combattant gagnant = grille.getWinner();
        if (gagnant != null) {
            JOptionPane.showMessageDialog(null, "La partie est terminée ! Le gagnant est : " + gagnant.getNom());
        } else {
            JOptionPane.showMessageDialog(null, "La partie est terminée ! Match nul !");
        }
    }

    public int getLongueurGrille() {
        return grille.getLongueur();
    }
    
    public int getLargeurGrille() {
        return grille.getLargeur();
    }

    /**
     * Récupère le chemin de l'image pour une position donnée sur la grille.
     *
     * @param x La coordonnée x de la position.
     * @param y La coordonnée y de la position.
     * @return Le chemin de l'image pour cette position.
     */
    public String getImagePourPosition(int x, int y) {
        Position position = new Position(x, y);
        Case caseGrille = grille.getCase(position); // Grille est un attribut de type Grille qui contient toutes les cases

        if (caseGrille == null) {
            return null;
        }

        // Appeler la méthode pour obtenir le chemin de l'image de cette case
        return obtenirImagePourCase(caseGrille);
    }

    /**
     * Récupère le chemin de l'image correspondant à une case.
     *
     * @param caseGrille La case pour laquelle récupérer l'image.
     * @return Le chemin de l'image à afficher pour cette case.
     */
private String obtenirImagePourCase(Case caseGrille) {
     String imagePath = null;

     // Utilisation d'un chemin relatif basé sur le répertoire racine du projet
     String cheminRessources = "ressources/";

     if (caseGrille.estUnMur()) {
         imagePath = cheminRessources + "wall.png";
     } 
     else if (caseGrille.getOccupant() != null) {
         Combattant occupant = caseGrille.getOccupant();
         if (occupant.estBouclierActif()) {
             imagePath = cheminRessources + "player_shield.png";
         } else if (occupant.estTireActif()) {
             imagePath = cheminRessources + "player.png"; 
         } else {
             imagePath = cheminRessources + "player.png"; 
         }
     } 
     else if (caseGrille.getExplosif() != null) {
         Explosif explosif = caseGrille.getExplosif();
         if (explosif.getType().equals(TypeEquipement.MINE)) {
             imagePath = cheminRessources + "mine.png"; 
         } else if (explosif.getType().equals(TypeEquipement.BOMBE)) {
             imagePath = cheminRessources + "bomb.png"; 
         }
     } 
     else if (caseGrille.contientPastilleEnergie()) {
         imagePath = cheminRessources + "baggage.png";
     } 
     else {
         imagePath = cheminRessources + "background.png";
     }

     return imagePath;
 }

    /**
     * Met à jour l'image d'une case en fonction de sa position sur la grille.
     *
     * @param position La position de la case dont l'image doit être mise à jour.
     * @param imagePath Le chemin de l'image à afficher (peut être l'image d'explosion ou l'image d'origine).
     */
    public void setImage(Position position, String imagePath) {
        Case caseGrille = grille.getCase(position);

        if (caseGrille == null) {
            System.err.println("Position invalide : Case introuvable.");
            return;
        }

        if (imagePath != null && !new File(imagePath).exists()) {
            System.err.println("Image introuvable : " + imagePath + " pour la position (" + position.getX() + ", " + position.getY() + ").");
            return; 
        }

        frame.mettreAJourImageCase(position.getX(), position.getY(), imagePath);
    }

    
    /**
     * Initialise la fenêtre principale de l'application.
     */
    public void initialiserFrame() {
        this.frame = new Frame(this);
        this.frame.afficherVue("Jeu"); 
    }

    /**
     * Demande à l'utilisateur le nombre total de joueurs.
     *
     * @return Le nombre total de joueurs.
     */
    public int demanderNombreJoueurs() {
        while (true) {
            try {
                String input = message.demanderInput("Entrez le nombre total de joueurs (min: 3, max: 5) :");
                if (input == null) return -1;
                int totalJoueurs = Integer.parseInt(input.trim());
                if (totalJoueurs >= 3 && totalJoueurs <= 5) {
                    return totalJoueurs;
                } else {
                    JOptionPane.showMessageDialog(null, "Le nombre de joueurs doit être entre 3 et 5.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrée invalide. Veuillez entrer un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Configure les joueurs en demandant leur répartition à l'utilisateur.
     */
    private void configurerJoueurs() {
        int totalJoueurs = demanderNombreJoueurs();
        if (totalJoueurs > 0) {
            List<JTextField> champsTexte;
            do {
                champsTexte = demanderConfigurationJoueurs(totalJoueurs);
            } while (!traiterConfiguration(champsTexte, totalJoueurs));
        }
    }

    /**
     * Demande à l'utilisateur la répartition des types de joueurs.
     *
     * @param totalJoueurs Le nombre total de joueurs.
     * @return Une liste de champs texte contenant les valeurs saisies.
     */
    public List<JTextField> demanderConfigurationJoueurs(int totalJoueurs) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        String[] labels = {"Joueurs AI :", "Joueurs aléatoires :", "Joueurs humains :"};
        List<JTextField> champsTexte = new ArrayList<>();
        for (String label : labels) {
            JPanel row = new JPanel(new FlowLayout());
            row.add(new JLabel(label));
            JTextField textField = new JTextField(5);
            champsTexte.add(textField);
            row.add(textField);
            panel.add(row);
        }

        int result = JOptionPane.showConfirmDialog(null, panel, "Configuration des joueurs", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        return result == JOptionPane.OK_OPTION ? champsTexte : null;
    }

    /**
     * Traite les valeurs saisies par l'utilisateur pour configurer les joueurs.
     *
     * @param champsTexte   Les champs contenant les types de joueurs.
     * @param totalJoueurs  Le nombre total de joueurs.
     * @return True si la configuration est valide, False sinon.
     */
    private boolean traiterConfiguration(List<JTextField> champsTexte, int totalJoueurs) {
        if (champsTexte == null) return false;

        try {
            int nombreAI = Integer.parseInt(champsTexte.get(0).getText().trim());
            int nombreAleatoire = Integer.parseInt(champsTexte.get(1).getText().trim());
            int nombreHumain = Integer.parseInt(champsTexte.get(2).getText().trim());

            if (nombreAI + nombreAleatoire + nombreHumain != totalJoueurs) {
                JOptionPane.showMessageDialog(null, "La somme des joueurs doit être égale à " + totalJoueurs, "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            joueurs.addAll(creerJoueursAvecStrategie(TypeJoueurEnum.AI, nombreAI));
            joueurs.addAll(creerJoueursAvecStrategie(TypeJoueurEnum.ALEATOIRE, nombreAleatoire));
            joueurs.addAll(creerJoueursAvecStrategie(TypeJoueurEnum.HUMAIN, nombreHumain));
            return true;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Toutes les entrées doivent être des nombres entiers.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Place les joueurs sur la grille.
     */
    private void placerJoueurs() {
        joueurs.forEach(joueur -> grille.placerCombattants(joueur.getCombattants()));
    }

    /**
     * Crée des joueurs en fonction du type choisi.
     *
     * @param type   Le type de joueurs.
     * @param nombre Le nombre de joueurs à créer.
     * @return Une liste de joueurs.
     */
    private List<TypeJoueur> creerJoueursAvecStrategie(TypeJoueurEnum type, int nombre) {
        List<TypeJoueur> joueursCrees = new ArrayList<>();
        for (int i = 0; i < nombre; i++) {
            List<Combattant> combattants = CombattantFactory.creerListeCombattants(1, grille, message);
            joueursCrees.add(JoueurFactory.creerJoueurAvecStrategie(type, combattants , Mode.GRAPHIC));
        }
        return joueursCrees;
    }

    /**
     * Récupère les informations des combattants avec leur état d'énergie.
     *
     * @return Une liste d'objets représentant les lignes à afficher dans la JTable.
     */
    public List<Object[]> getInformationsPourJTable() {
        List<Object[]> informations = new ArrayList<>();

        for (TypeJoueur joueur : joueurs) {
            for (Combattant combattant : joueur.getCombattants()) {
                String nom = combattant.getNom();
                Object[] energieInfo = calculerEtatEnergie(combattant);
                int energieActuelle = (int) energieInfo[0];
                String etat = (String) energieInfo[1];

                String armes = combattant.getArmes().isEmpty() 
                    ? "Aucune" 
                    : String.join(", ", combattant.getArmes().stream()
                          .map(arme -> arme.getType().toString())
                          .collect(Collectors.toList()));


                String explosifs = combattant.getExplosifs().isEmpty()
                    ? "Aucun"
                    : String.join(", ", combattant.getExplosifs().stream()
                          .map(explosif -> explosif.getType().toString())
                          .collect(Collectors.toList()));


                informations.add(new Object[]{
                    nom,
                    etat.equals("eliminer") ? "Éliminé" : energieActuelle,
                    armes,
                    explosifs,
                    etat
                });
            }
        }

        return informations;
    }

    /**
     * Calcul l'état d'énergie d'un combattant.
     *
     * @param combattant Le combattant à analyser.
     * @return Un tableau contenant l'énergie restante et un état ("total", "moitie", "eliminer").
     */
    public Object[] calculerEtatEnergie(Combattant combattant) {
        int energieInitiale = Parametres.energieInitiale;
        int energieActuelle = combattant.getEnergie();

        String etat;
        if (energieActuelle == 0) {
            etat = "eliminer";
        } else if (energieActuelle <= energieInitiale / 2) {
            etat = "moitie";
        } else {
            etat = "total";
        }

        return new Object[]{energieActuelle, etat};
    }
    
    
    

    /**
     * Réinitialise le jeu en vidant les joueurs et recréant la grille.
     */
    public void reinitialiserJeu() {
    	if (frame != null) {
            frame.dispose();
        }
    	joueurs.clear();
        grille = new Grille(message);
        partieEnCours = true;
        indiceJoueurActuel = 0;
        initialiserJeu();
        initialiserFrame();
    }

    /**
     * Quitte l'application après confirmation.
     */
    public void quitterApplication() {
        int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment quitter ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
