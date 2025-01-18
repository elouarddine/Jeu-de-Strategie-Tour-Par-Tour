package model.factoryJoueur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
 * Classe représentant un joueur IA avec une stratégie heuristique avancée améliorée.
 */
public class AIJoueur implements JoueurStrategy {
    
	 private final MessageHandler messageHandler;
	    private final Mode mode;
	    private ProxyGrille proxyGrille;

	    

    /**
     * Constructeur de la classe AIJoueur.
     *
     * @param messageHandler Le gestionnaire de messages pour interagir avec l'utilisateur.
     */
    public AIJoueur(MessageHandler messageHandler , Mode mode) {
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
     * Applique la stratégie IA en fonction de l'état actuel du jeu.
     *
     * @param combattant Le combattant qui exécute l'action.
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

        // Afficher la grille uniquement en mode console
        if (this.mode == Mode.CONSOLE) {
            proxyGrille.afficherGrille();
        }

        Position posIA = combattant.getPosition();

        List<Combattant> ennemisProches = grille.getCombattantsProches(posIA, 5); 

        List<Integer> actionsDisponibles = determineActionsDisponibles(combattant, grille);

        if (!ennemisProches.isEmpty() && actionsDisponibles.contains(0)) { 
            boolean actionEffectuee = tenterTirer(combattant, grille, ennemisProches);
            if (actionEffectuee) {
                return;
            }
        }

        if (combattant.getEnergie() < 20 && !combattant.estBouclierActif() && actionsDisponibles.contains(1)) {
            boolean bouclierActivé = combattant.activerBouclier();
            if (bouclierActivé) {
                // Utilisez MessageHandler pour afficher les messages
                messageHandler.afficherMessage(combattant.getNom() + " a activé son bouclier.");
                return;
            } else {
                messageHandler.afficherMessage(combattant.getNom() + " a tenté d'activer le bouclier mais a échoué.");
            }
        }

        if ((actionsDisponibles.contains(2) || actionsDisponibles.contains(3))) {
            boolean actionExplosif = tenterDeposerExplosif(combattant, grille);
            if (actionExplosif) {
                return;
            }
        }

        if (actionsDisponibles.contains(4)) { 
            boolean deplacementEffectue = tenterDeplacementStrategique(combattant, grille);
            if (deplacementEffectue) {
                return;
            }
        }

        combattant.attendre();
        messageHandler.afficherMessage(combattant.getNom() + " a effectué l'action : Attendre.");
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

        // Action 0: Tirer
        boolean peutTirer = combattant.getArmes().stream().anyMatch(a -> a.getMunition() > 0);
        if (peutTirer) {
            actions.add(0);
        }

        // Action 1: Activer Bouclier
        if (!combattant.estBouclierActif() && combattant.getEnergie() >= 20) { 
            actions.add(1);
        }

        // Action 2: Déposer Bombe
        boolean peutDeposerBombe = combattant.getExplosifs().stream().anyMatch(e -> e instanceof Bombe);
        if (peutDeposerBombe) {
            actions.add(2);
        }

        // Action 3: Déposer Mine
        boolean peutDeposerMine = combattant.getExplosifs().stream().anyMatch(e -> e instanceof Mine);
        if (peutDeposerMine) {
            actions.add(3);
        }

        // Action 4: Déplacer
        actions.add(4);


        return actions;
    }

    /**
     * Tente de tirer sur l'ennemi le plus proche.
     *
     * @param combattant Le combattant qui tire.
     * @param grille La grille du jeu.
     * @param ennemis La liste des ennemis proches.
     * @return true si l'action de tir a été effectuée avec succès, false sinon.
     */
    private boolean tenterTirer(Combattant combattant, Grille grille, List<Combattant> ennemis) {
        if (ennemis.isEmpty()) {
            messageHandler.afficherMessage("DEBUG: Aucun ennemi proche pour tirer.");
            return false;
        }

        Combattant ennemiLePlusProche = ennemis.get(0); 

        Optional<Arme> armeDisponible = combattant.getArmes().stream()
                .filter(a -> a.getMunition() > 0)
                .findFirst();

        if (armeDisponible.isPresent()) {
            Arme arme = armeDisponible.get();
            Position posIA = combattant.getPosition();
            Position posEnnemi = ennemiLePlusProche.getPosition();

            Direction direction;
            int delta;
            if (posEnnemi.getX() > posIA.getX()) {
                direction = Direction.HORIZONTAL;
                delta = 1;
            } else if (posEnnemi.getX() < posIA.getX()) {
                direction = Direction.HORIZONTAL;
                delta = -1;
            } else if (posEnnemi.getY() > posIA.getY()) {
                direction = Direction.VERTICAL;
                delta = 1;
            } else {
                direction = Direction.VERTICAL;
                delta = -1;
            }

            Position targetPos;
            if (direction == Direction.HORIZONTAL) {
                targetPos = new Position(posIA.getX() + delta * arme.getPortee(), posIA.getY());
            } else { 
                targetPos = new Position(posIA.getX(), posIA.getY() + delta * arme.getPortee());
            }

            if (grille.estPositionValide(targetPos)) {
                Case caseCible = grille.getCase(targetPos);
                if (caseCible != null && caseCible.getOccupant() != null && caseCible.getOccupant().equals(ennemiLePlusProche)) {
                    boolean tirEffectue = combattant.tirer(arme, caseCible, direction);
                    if (tirEffectue) {
                      //  messageHandler.afficherMessage(combattant.getNom() + " a tiré avec " 
                           // + arme.getType() + " sur " + ennemiLePlusProche.getNom() + " avec succès.");
                        return true;
                    } else {
                    //    messageHandler.afficherMessage(combattant.getNom() + " a échoué à tirer avec " 
                         //   + arme.getType() + " sur " + ennemiLePlusProche.getNom() + ".");
                        return false;
                    }
                }
            }
        }

       // messageHandler.afficherMessage("DEBUG: Tir échoué ou cible invalide.");
        return false;
    }

    /**
     * Tente de déposer un explosif (mine ou bombe) à une position stratégique.
     *
     * @param combattant Le combattant qui dépose l'explosif.
     * @param grille La grille du jeu.
     * @return true si un explosif a été déposé, false sinon.
     */
    private boolean tenterDeposerExplosif(Combattant combattant, Grille grille) {
        List<Case> casesVoisines = grille.getCase(combattant.getPosition()).getCasesVoisines();
        List<Case> casesLibres = filtrerCasesLibres(casesVoisines);

        if (casesLibres.isEmpty()) {
            messageHandler.afficherMessage("DEBUG: Aucune case libre pour déposer un explosif.");
            return false;
        }

        List<Case> casesCibles = new ArrayList<>();
        for (Case c : casesLibres) {
            List<Combattant> ennemisProches = grille.getCombattantsProches(c.getPosition(), 3);
            if (!ennemisProches.isEmpty()) {
                casesCibles.add(c);
            }
        }

        if (casesCibles.isEmpty()) {
            messageHandler.afficherMessage("DEBUG: Aucune case stratégique disponible pour déposer un explosif.");
            return false;
        }

        Collections.shuffle(casesCibles, new Random());

        for (Case caseCible : casesCibles) {

        	if (combattant.getExplosifs().stream().anyMatch(e -> e instanceof Bombe)) {
                boolean success = combattant.deposerBombe(caseCible);
                if (success) {
                 //   messageHandler.afficherMessage(combattant.getNom() + " a déposé une bombe à la position (" 
                   //     + caseCible.getPosition().getX() + ", " + caseCible.getPosition().getY() + ").");
                    return true;
                }
            }

            if (combattant.getExplosifs().stream().anyMatch(e -> e instanceof Mine)) {
                boolean success = combattant.deposerMine(caseCible);
                if (success) {
                   // messageHandler.afficherMessage(combattant.getNom() + " a déposé une mine à la position (" 
                 //       + caseCible.getPosition().getX() + ", " + caseCible.getPosition().getY() + ").");
                    return true;
                }
            }
        }

      //  messageHandler.afficherMessage("DEBUG: Dépôt d'explosif échoué pour toutes les cases cibles.");
        return false;
    }

    /**
     * Tente de déplacer le combattant vers une position stratégique.
     *
     * @param combattant Le combattant qui se déplace.
     * @param grille La grille du jeu.
     * @return true si le déplacement a été effectué, false sinon.
     */
    private boolean tenterDeplacementStrategique(Combattant combattant, Grille grille) {

        Position positionCentrale = grille.getPositionCentrale();
        Position posIA = combattant.getPosition();

        Direction direction;
        int delta;

        if (positionCentrale.getX() > posIA.getX()) {
            direction = Direction.HORIZONTAL;
            delta = 1;
        } else if (positionCentrale.getX() < posIA.getX()) {
            direction = Direction.HORIZONTAL;
            delta = -1;
        } else if (positionCentrale.getY() > posIA.getY()) {
            direction = Direction.VERTICAL;
            delta = 1;
        } else {
            direction = Direction.VERTICAL;
            delta = -1;
        }

        Position nouvellePosition = null;
        if (direction == Direction.HORIZONTAL) {
            nouvellePosition = new Position(posIA.getX() + delta, posIA.getY());
        } else {
            nouvellePosition = new Position(posIA.getX(), posIA.getY() + delta);
        }

        if (!grille.estPositionValide(nouvellePosition)) {
            messageHandler.afficherMessage("DEBUG: Déplacement stratégique vers une position invalide.");
            return false;
        }

        Case caseCible = grille.getCase(nouvellePosition);
        if (caseCible != null && !caseCible.estUnMur() && !caseCible.estOccupee(false)) {
            boolean deplace = combattant.deplacer(caseCible);
            if (deplace) {
             //   messageHandler.afficherMessage(combattant.getNom() + " s'est déplacé stratégiquement vers (" 
               //     + nouvellePosition.getX() + ", " + nouvellePosition.getY() + ").");
                return true;
            }
        }

      //  messageHandler.afficherMessage("DEBUG: Déplacement stratégique échoué.");
        return false;
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
