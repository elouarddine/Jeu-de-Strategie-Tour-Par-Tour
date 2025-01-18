package model.factoryJoueur;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
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
 * Classe représentant un joueur avec une stratégie aléatoire améliorée.
 */
public class AleatoireJoueur implements JoueurStrategy {
    
    private final Random random = new Random();
    private final MessageHandler messageHandler;
    private ProxyGrille proxyGrille; 
    private final Mode mode;

    /**
     * Constructeur de la classe AleatoireJoueur.
     *
     * @param messageHandler Gestionnaire de messages pour les interactions du combattant.
     */
    public AleatoireJoueur(MessageHandler messageHandler, Mode mode) {
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
     * Applique une stratégie aléatoire en choisissant une action valide parmi les actions disponibles.
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
            return;
        }

        Collections.shuffle(actionsDisponibles, random);

        for (int action : actionsDisponibles) {
            boolean actionReussie = executerAction(combattant, grille, action);
            if (actionReussie) {
                return; 
            }
        }

        combattant.attendre();
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


        return actions;
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
            case 0: // Déplacement
                return deplacerAleatoire(combattant, grille);
            case 1: // Déposer Mine
                return deposerMineAleatoire(combattant, grille);
            case 2: // Déposer Bombe
                return deposerBombeAleatoire(combattant, grille);
            case 3: // Tirer
                return tirerAleatoire(combattant, grille);
            case 4: // Activer Bouclier
                return activerBouclierAleatoire(combattant, grille);
            default:
                return false;
        }
    }

    /**
     * Déplace le combattant dans une direction aléatoire (HORIZONTAL ou VERTICAL) si possible.
     *
     * @param combattant Le combattant à déplacer.
     * @param grille La grille du jeu.
     * @return true si le déplacement a réussi, false sinon.
     */
    private boolean deplacerAleatoire(Combattant combattant, Grille grille) {

    	Direction directionChoice = random.nextBoolean() ? Direction.HORIZONTAL : Direction.VERTICAL;

        Position currentPos = combattant.getPosition();
        Position nouvellePosition;

        if (directionChoice == Direction.HORIZONTAL) {

        	int deltaX = random.nextBoolean() ? 1 : -1;
            nouvellePosition = new Position(currentPos.getX() + deltaX, currentPos.getY());
        } else { 

        	int deltaY = random.nextBoolean() ? 1 : -1;
            nouvellePosition = new Position(currentPos.getX(), currentPos.getY() + deltaY);
        }

        if (!grille.estPositionValide(nouvellePosition)) {
            messageHandler.afficherMessage(combattant.getNom() + " a tenté de se déplacer vers une position invalide.");
            return false;
        }

        Case caseCible = grille.getCase(nouvellePosition);

        if (caseCible != null && !caseCible.estUnMur() && !caseCible.estOccupee(false)) {
            boolean moved = combattant.deplacer(caseCible);
            if (moved) {
              //  messageHandler.afficherMessage(combattant.getNom() + " a déplacé vers " + directionChoice + " avec succès.");
                return true;
            } else {
             //   messageHandler.afficherMessage(combattant.getNom() + " a échoué à se déplacer vers " + directionChoice + ".");
                return false;
            }
        } else {
         //   messageHandler.afficherMessage(combattant.getNom() + " ne peut pas se déplacer vers " + directionChoice + " (case occupée ou mur).");
            return false;
        }
    }

    /**
     * Dépose une mine dans une case libre aléatoire.
     *
     * @param combattant Le combattant qui dépose la mine.
     * @param grille La grille du jeu.
     * @return true si le dépôt a réussi, false sinon.
     */
    private boolean deposerMineAleatoire(Combattant combattant, Grille grille) {

    	List<Case> casesVoisines = grille.getCase(combattant.getPosition()).getCasesVoisines();
        List<Case> casesLibres = filtrerCasesLibres(casesVoisines);

        if (casesLibres.isEmpty()) {

        	messageHandler.afficherMessage(combattant.getNom() + " a tenté de déposer une mine mais aucune case libre n'est disponible.");
            return false;
        }


        Case caseCible = casesLibres.get(random.nextInt(casesLibres.size()));

        boolean success = combattant.deposerMine(caseCible);
        if (success) {
         //   messageHandler.afficherMessage(combattant.getNom() + " a déposé une mine à la position (" 
           //     + caseCible.getPosition().getX() + ", " + caseCible.getPosition().getY() + ").");
            return true;
        } else {
        //    messageHandler.afficherMessage(combattant.getNom() + " a échoué à déposer une mine à la position (" 
        //        + caseCible.getPosition().getX() + ", " + caseCible.getPosition().getY() + ").");
            return false;
        }
    }

    /**
     * Dépose une bombe dans une case libre aléatoire.
     *
     * @param combattant Le combattant qui dépose la bombe.
     * @param grille La grille du jeu.
     * @return true si le dépôt a réussi, false sinon.
     */
    private boolean deposerBombeAleatoire(Combattant combattant, Grille grille) {

    	List<Case> casesVoisines = grille.getCase(combattant.getPosition()).getCasesVoisines();
        List<Case> casesLibres = filtrerCasesLibres(casesVoisines);

        if (casesLibres.isEmpty()) {

        	messageHandler.afficherMessage(combattant.getNom() + " a tenté de déposer une bombe mais aucune case libre n'est disponible.");
            return false;
        }

        Case caseCible = casesLibres.get(random.nextInt(casesLibres.size()));

        boolean success = combattant.deposerBombe(caseCible);
        if (success) {
         //   messageHandler.afficherMessage(combattant.getNom() + " a déposé une bombe à la position (" 
         //       + caseCible.getPosition().getX() + ", " + caseCible.getPosition().getY() + ").");
            return true;
        } else {
         //   messageHandler.afficherMessage(combattant.getNom() + " a échoué à déposer une bombe à la position (" 
          //      + caseCible.getPosition().getX() + ", " + caseCible.getPosition().getY() + ").");
            return false;
        }
    }

    /**
     * Tire avec une arme aléatoire dans une direction aléatoire.
     *
     * @param combattant Le combattant qui tire.
     * @param grille La grille du jeu.
     * @return true si le tir a réussi, false sinon.
     */
    private boolean tirerAleatoire(Combattant combattant, Grille grille) {
    	List<Arme> armesDisponibles = combattant.getArmes().stream()
                .filter(a -> a.getMunition() > 0)
                .collect(Collectors.toList());

        if (armesDisponibles.isEmpty()) {

        	messageHandler.afficherMessage(combattant.getNom() + " a tenté de tirer mais aucune arme avec munition n'est disponible.");
            return false;
        }

        Arme arme = armesDisponibles.get(random.nextInt(armesDisponibles.size()));

        Direction directionChoice = random.nextBoolean() ? Direction.HORIZONTAL : Direction.VERTICAL;

        Position currentPos = combattant.getPosition();
        Position targetPos;

        if (directionChoice == Direction.HORIZONTAL) {

        	int deltaX = random.nextBoolean() ? 1 : -1;
            targetPos = new Position(currentPos.getX() + deltaX * arme.getPortee(), currentPos.getY());
        } else { 
        	
            int deltaY = random.nextBoolean() ? 1 : -1;
            targetPos = new Position(currentPos.getX(), currentPos.getY() + deltaY * arme.getPortee());
        }

        if (!grille.estPositionValide(targetPos)) {
            messageHandler.afficherMessage(combattant.getNom() + " a tenté de tirer vers une position invalide.");
            return false;
        }

        Case caseCible = grille.getCase(targetPos);

        if (caseCible != null) {
            boolean fired = combattant.tirer(arme, caseCible, directionChoice);
            if (fired) {
               // messageHandler.afficherMessage(combattant.getNom() + " a tiré avec " 
               //     + arme.getType() + " vers " + directionChoice + " avec succès.");
                return true;
            } else {
             //   messageHandler.afficherMessage(combattant.getNom() + " a échoué à tirer avec " 
              //      + arme.getType() + " vers " + directionChoice + ".");
                return false;
            }
        } else {
          //  messageHandler.afficherMessage(combattant.getNom() + " a tenté de tirer vers une case invalide.");
            return false;
        }
    }

    /**
     * Active le bouclier si possible.
     *
     * @param combattant Le combattant qui active le bouclier.
     * @param grille La grille du jeu.
     * @return true si le bouclier a été activé, false sinon.
     */
    private boolean activerBouclierAleatoire(Combattant combattant, Grille grille) {
        boolean activated = combattant.activerBouclier();
        if (activated) {
         //   messageHandler.afficherMessage(combattant.getNom() + " a activé son bouclier avec succès.");
            return true;
        } else {
         //   messageHandler.afficherMessage(combattant.getNom() + " a échoué à activer le bouclier.");
            return false;
        }
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
}
