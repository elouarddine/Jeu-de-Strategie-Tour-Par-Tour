package model.factoryJoueur;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import model.Arme;
import model.Bombe;
import model.Case;
import model.Combattant;
import model.Direction;
import model.Grille;
import model.Mine;
import model.Position;
import model.proxy.ProxyGrille;
import utils.strategyMessage.MessageHandler;

/**
 * Classe représentant un joueur humain avec une stratégie basée sur les entrées utilisateur.
 */
public class HumainJoueur implements JoueurStrategy {
    
    private final MessageHandler messageHandler;
    private final Mode mode;
    private ProxyGrille proxyGrille;
    
    /**
     * Constructeur de la classe HumainJoueur.
     *
     * @param messageHandler Gestionnaire de messages pour les interactions du combattant.
     * @param mode Le mode d'affichage (console ou graphique).
     */
    public HumainJoueur(MessageHandler messageHandler, Mode mode) {
        if (messageHandler == null) {
            throw new IllegalArgumentException("Le MessageHandler ne peut pas être nul.");
        }
        if (mode == null) {
            throw new IllegalArgumentException("Le mode ne peut pas être nul.");
        }
        this.messageHandler = messageHandler;
        this.mode = mode;
    }

    /**
     * Initialise le ProxyGrille pour le combattant.
     *
     * @param combattant Le combattant auquel cette stratégie est associée.
     * @param grille La grille réelle du jeu.
     */
    public void initialiserGrille(Combattant combattant, Grille grille) {
        this.proxyGrille = new ProxyGrille(grille, combattant);
    }

    /**
     * Applique la stratégie humaine en demandant à l'utilisateur quelle action effectuer.
     *
     * @param combattant Le combattant à qui appliquer la stratégie.
     * @param grille La grille du jeu.
     */
    @Override
    public void appliquerStrategie(Combattant combattant, Grille grille) {
        if (combattant == null || grille == null) {
            throw new IllegalArgumentException("Le combattant et la grille ne peuvent pas être nuls.");
        }

        if (this.proxyGrille == null || !this.proxyGrille.getGrilleReelle().equals(grille) || !this.proxyGrille.getJoueur().equals(combattant)) {
            initialiserGrille(combattant, grille);
        }

        proxyGrille.clearCache(); 

        if (this.mode == Mode.CONSOLE) {
            proxyGrille.afficherGrille();
        }

        List<Integer> actionsDisponibles = determineActionsDisponibles(combattant, grille);

        if (actionsDisponibles.isEmpty()) {
            combattant.attendre();
            messageHandler.afficherMessage(combattant.getNom() + " a effectué l'action : Attendre.");
            return;
        }

        int choixAction = demanderChoixAction(actionsDisponibles, combattant);

        if (choixAction == -1) {
            // L'utilisateur a annulé ou n'a pas fait de choix valide
            combattant.attendre();
            messageHandler.afficherMessage(combattant.getNom() + " a effectué l'action : Attendre.");
            return;
        }

        boolean actionReussie = executerAction(combattant, grille, choixAction);

        if (!actionReussie) {
            messageHandler.afficherMessage("Action échouée. Le combattant doit attendre.");
            combattant.attendre();
            messageHandler.afficherMessage(combattant.getNom() + " a effectué l'action : Attendre.");
        }
    }

    /**
     * Détermine les actions disponibles pour le combattant en fonction de son état et de l'environnement.
     *
     * @param combattant Le combattant qui décide de l'action.
     * @param grille La grille du jeu.
     * @return Une liste des identifiants des actions disponibles.
     */
    private List<Integer> determineActionsDisponibles(Combattant combattant, Grille grille) {
        List<Integer> actions = new ArrayList<>();

        // Action 0: Déplacement
        actions.add(0);

        // Action 1: Déposer Mine
        boolean peutDeposerMine = combattant.getExplosifs().stream().anyMatch(e -> e instanceof Mine);
        if (peutDeposerMine) {
            actions.add(1);
        }

        // Action 2: Déposer Bombe
        boolean peutDeposerBombe = combattant.getExplosifs().stream().anyMatch(e -> e instanceof Bombe);
        if (peutDeposerBombe) {
            actions.add(2);
        }

        // Action 3: Tirer
        boolean peutTirer = combattant.getArmes().stream().anyMatch(a -> a.getMunition() > 0);
        if (peutTirer) {
            actions.add(3);
        }

        // Action 4: Activer Bouclier
        if (!combattant.estBouclierActif()) {
            actions.add(4);
        }

        // Action 5: Attendre (toujours disponible)
        actions.add(5);

        return actions;
    }

    /**
     * Demande à l'utilisateur de choisir une action parmi les actions disponibles.
     *
     * @param actionsDisponibles Liste des identifiants des actions disponibles.
     * @param combattant Le combattant qui décide de l'action.
     * @return L'identifiant de l'action choisie.
     */
    private int demanderChoixAction(List<Integer> actionsDisponibles, Combattant combattant) {
        List<String> options = new ArrayList<>();
        for (int action : actionsDisponibles) {
            options.add(getActionDescription(action));
        }

        String message = "Choisissez une action pour " + combattant.getNom() + " :";
        String selectedOption = messageHandler.demanderChoixParmiOptions(message, options);

        if (selectedOption == null) {
            return -1; // L'utilisateur a annulé
        }

        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).equals(selectedOption)) {
                return actionsDisponibles.get(i);
            }
        }

        return -1; // Aucun choix valide
    }

    /**
     * Exécute une action spécifique et retourne si elle a réussi ou non.
     *
     * @param combattant Le combattant qui exécute l'action.
     * @param grille La grille du jeu.
     * @param action L'identifiant de l'action à exécuter.
     * @return true si l'action a réussi, false sinon.
     */
    private boolean executerAction(Combattant combattant, Grille grille, int action) {
        switch (action) {
            case 0: 
                return deplacerHumain(combattant, grille);
            case 1: 
                return deposerMineHumain(combattant, grille);
            case 2: 
                return deposerBombeHumain(combattant, grille);
            case 3: 
                return tirerHumain(combattant, grille);
            case 4: 
                return activerBouclierHumain(combattant, grille);
            case 5: 
                combattant.attendre();
                messageHandler.afficherMessage(combattant.getNom() + " a choisi d'attendre pour économiser son énergie.");
                return true;
            default:
                return false;
        }
    }

    /**
     * Gère le déplacement du joueur humain en demandant la direction.
     *
     * @param combattant Le combattant à déplacer.
     * @param grille La grille du jeu.
     * @return true si le déplacement a réussi, false sinon.
     */
    private boolean deplacerHumain(Combattant combattant, Grille grille) {
        List<String> directions = List.of("Haut", "Bas", "Gauche", "Droite");

        String message = "Choisissez une direction pour vous déplacer :";
        String selectedDirection = messageHandler.demanderChoixParmiOptions(message, directions);

        if (selectedDirection == null) {
            return false; // L'utilisateur a annulé
        }

        int directionChoisie = directions.indexOf(selectedDirection) + 1;

        Position currentPos = combattant.getPosition();
        Position nouvellePosition = null;

        switch (directionChoisie) {
            case 1:
                nouvellePosition = new Position(currentPos.getX(), currentPos.getY() - 1);
                break;
            case 2:
                nouvellePosition = new Position(currentPos.getX(), currentPos.getY() + 1);
                break;
            case 3:
                nouvellePosition = new Position(currentPos.getX() - 1, currentPos.getY());
                break;
            case 4:
                nouvellePosition = new Position(currentPos.getX() + 1, currentPos.getY());
                break;
            default:
                return false;
        }

        if (!grille.estPositionValide(nouvellePosition)) {
            return false;
        }

        Case caseCible = grille.getCase(nouvellePosition);

        if (caseCible != null && !caseCible.estUnMur() && !caseCible.estOccupee(false)) {
            boolean moved = combattant.deplacer(caseCible);
            return moved;
        } else {
            return false;
        }
    }

    /**
     * Gère le dépôt d'une mine par le joueur humain.
     *
     * @param combattant Le combattant qui dépose la mine.
     * @param grille La grille du jeu.
     * @return true si le dépôt a réussi, false sinon.
     */
    private boolean deposerMineHumain(Combattant combattant, Grille grille) {
        // Trouver les cases voisines libres
        List<Case> casesVoisines = grille.getCase(combattant.getPosition()).getCasesVoisines();
        List<Case> casesLibres = filtrerCasesLibres(casesVoisines);

        if (casesLibres.isEmpty()) {
            messageHandler.afficherMessage("Aucune case libre disponible pour déposer une mine.");
            return false;
        }

        List<String> options = new ArrayList<>();
        for (Case c : casesLibres) {
            Position pos = c.getPosition();
            options.add("(" + pos.getX() + ", " + pos.getY() + ")");
        }

        String message = "Choisissez une case pour déposer une mine :";
        String selectedOption = messageHandler.demanderChoixParmiOptions(message, options);

        if (selectedOption == null) {
            return false; // L'utilisateur a annulé
        }

        Case caseCible = null;
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).equals(selectedOption)) {
                caseCible = casesLibres.get(i);
                break;
            }
        }

        if (caseCible == null) {
            return false; // Aucun choix valide
        }

        boolean success = combattant.deposerMine(caseCible);

        return success;
    }

    /**
     * Gère le dépôt d'une bombe par le joueur humain.
     *
     * @param combattant Le combattant qui dépose la bombe.
     * @param grille La grille du jeu.
     * @return true si le dépôt a réussi, false sinon.
     */
    private boolean deposerBombeHumain(Combattant combattant, Grille grille) {
        // Trouver les cases voisines libres
        List<Case> casesVoisines = grille.getCase(combattant.getPosition()).getCasesVoisines();
        List<Case> casesLibres = filtrerCasesLibres(casesVoisines);

        if (casesLibres.isEmpty()) {
            messageHandler.afficherMessage("Aucune case libre disponible pour déposer une bombe.");
            return false;
        }

        List<String> options = new ArrayList<>();
        for (Case c : casesLibres) {
            Position pos = c.getPosition();
            options.add("(" + pos.getX() + ", " + pos.getY() + ")");
        }

        String message = "Choisissez une case pour déposer une bombe :";
        String selectedOption = messageHandler.demanderChoixParmiOptions(message, options);

        if (selectedOption == null) {
            return false; // L'utilisateur a annulé
        }

        Case caseCible = null;
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).equals(selectedOption)) {
                caseCible = casesLibres.get(i);
                break;
            }
        }

        if (caseCible == null) {
            return false; // Aucun choix valide
        }

        boolean success = combattant.deposerBombe(caseCible);

        return success;
    }

    /**
     * Gère le tir d'une arme par le joueur humain en demandant la direction et la cible.
     *
     * @param combattant Le combattant qui tire.
     * @param grille La grille du jeu.
     * @return true si le tir a réussi, false sinon.
     */
    private boolean tirerHumain(Combattant combattant, Grille grille) {
        List<Arme> armesDisponibles = combattant.getArmes().stream()
                .filter(a -> a.getMunition() > 0)
                .collect(Collectors.toList());

        if (armesDisponibles.isEmpty()) {
            messageHandler.afficherMessage("Aucune arme avec munition disponible pour tirer.");
            return false;
        }

        List<String> armesOptions = new ArrayList<>();
        for (Arme arme : armesDisponibles) {
            armesOptions.add(arme.getType() + " (Munition: " + arme.getMunition() + ", Portée: " + arme.getPortee() + ")");
        }

        String messageArme = "Choisissez une arme pour tirer :";
        String selectedArmeOption = messageHandler.demanderChoixParmiOptions(messageArme, armesOptions);

        if (selectedArmeOption == null) {
            return false; // L'utilisateur a annulé
        }

        Arme armeChoisie = null;
        for (int i = 0; i < armesOptions.size(); i++) {
            if (armesOptions.get(i).equals(selectedArmeOption)) {
                armeChoisie = armesDisponibles.get(i);
                break;
            }
        }

        if (armeChoisie == null) {
            return false; // Aucun choix valide
        }

        List<String> directions = List.of("Haut", "Bas", "Gauche", "Droite");

        String messageDirection = "Choisissez une direction pour tirer :";
        String selectedDirection = messageHandler.demanderChoixParmiOptions(messageDirection, directions);

        if (selectedDirection == null) {
            return false; // L'utilisateur a annulé
        }

        int directionChoisie = directions.indexOf(selectedDirection) + 1;

        Position currentPos = combattant.getPosition();
        Position targetPos = null;

        switch (directionChoisie) {
            case 1:
                targetPos = new Position(currentPos.getX(), currentPos.getY() - armeChoisie.getPortee());
                break;
            case 2:
                targetPos = new Position(currentPos.getX(), currentPos.getY() + armeChoisie.getPortee());
                break;
            case 3:
                targetPos = new Position(currentPos.getX() - armeChoisie.getPortee(), currentPos.getY());
                break;
            case 4:
                targetPos = new Position(currentPos.getX() + armeChoisie.getPortee(), currentPos.getY());
                break;
            default:
                return false;
        }

        if (!grille.estPositionValide(targetPos)) {
            messageHandler.afficherMessage("Position cible invalide. Tir échoué.");
            return false;
        }

        Case caseCible = grille.getCase(targetPos);

        if (caseCible != null) {
            Direction direction = (directionChoisie == 1 || directionChoisie == 2) ? Direction.VERTICAL : Direction.HORIZONTAL;
            boolean tirEffectue = combattant.tirer(armeChoisie, caseCible, direction);
            return tirEffectue;
        } else {
            return false;
        }
    }

    /**
     * Gère l'activation du bouclier par le joueur humain.
     *
     * @param combattant Le combattant qui active le bouclier.
     * @param grille La grille du jeu.
     * @return true si le bouclier a été activé, false sinon.
     */
    private boolean activerBouclierHumain(Combattant combattant, Grille grille) {
        boolean activated = combattant.activerBouclier();
        return activated;
    }

    /**
     * Filtre les cases libres parmi les cases voisines.
     *
     * @param casesVoisines La liste des cases voisines.
     * @return La liste des cases libres.
     */
    private List<Case> filtrerCasesLibres(List<Case> casesVoisines) {
        List<Case> casesLibres = new ArrayList<>();
        for (Case c : casesVoisines) {
            if (!c.estOccupee(true) && !c.estUnMur()) {
                casesLibres.add(c);
            }
        }
        return casesLibres;
    }

    /**
     * Retourne la description d'une action en fonction de son identifiant.
     *
     * @param action L'identifiant de l'action.
     * @return La description de l'action.
     */
    private String getActionDescription(int action) {
        switch (action) {
            case 0:
                return "Déplacement";
            case 1:
                return "Déposer Mine";
            case 2:
                return "Déposer Bombe";
            case 3:
                return "Tirer";
            case 4:
                return "Activer Bouclier";
            case 5:
                return "Attendre";
            default:
                return "Action inconnue";
        }
    }
}
