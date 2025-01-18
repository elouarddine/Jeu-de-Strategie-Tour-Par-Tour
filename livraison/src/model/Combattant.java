package model;

import utils.configuration.Parametres;
import utils.strategyMessage.MessageHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite représentant un combattant dans le jeu.
 * <p>
 * Un combattant possède un nom, une énergie, une position, un bouclier, des armes, des explosifs, etc.
 * Il peut effectuer diverses actions comme se déplacer, tirer, activer son bouclier, etc.
 */
public abstract class Combattant implements CombattantActions {

    /** Le nom du combattant. */
    private String nom;

    /** L'énergie actuelle du combattant. */
    private int energie;

    /** La position actuelle du combattant sur la grille. */
    private Position position;

    /** Indique si le bouclier est actif. */
    private boolean bouclierActif;

    /** Indique si le tir est actif. */
    private boolean tireActif;

    /** La liste des armes possédées par le combattant. */
    private List<Arme> armes = new ArrayList<>();

    /** La liste des explosifs possédés par le combattant. */
    private List<Explosif> explosifs = new ArrayList<>();

    /** Le gestionnaire de messages pour afficher les messages du jeu. */
    private MessageHandler messageHandler;

    /** La grille de jeu où évolue le combattant. */
    private Grille grille;

    /**
     * Constructeur de la classe Combattant.
     *
     * @param nom            Le nom du combattant.
     * @param position       La position initiale du combattant sur la grille.
     * @param messageHandler Le gestionnaire de messages pour afficher les messages du jeu.
     * @param grille         La grille de jeu où évolue le combattant.
     */
    public Combattant(String nom, Position position, MessageHandler messageHandler, Grille grille) {
        this.nom = nom;
        this.energie = Parametres.energieInitiale;
        this.position = position;
        this.messageHandler = messageHandler;
        this.grille = grille;
    }

    // Getters et Setters

    /**
     * Obtient le nom du combattant.
     *
     * @return Le nom du combattant.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Obtient l'énergie actuelle du combattant.
     *
     * @return L'énergie du combattant.
     */
    public int getEnergie() {
        return energie;
    }

    /**
     * Définit l'énergie du combattant.
     *
     * @param energie La nouvelle valeur de l'énergie.
     */
    public void setEnergie(int energie) {
        this.energie = Math.max(energie, 0);
    }

    /**
     * Obtient la position actuelle du combattant.
     *
     * @return La position du combattant.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Définit la position du combattant.
     *
     * @param position La nouvelle position du combattant.
     * @throws IllegalArgumentException si la position est nulle.
     */
    public void setPosition(Position position) {
        if (position != null) {
            this.position = position;
        } else {
            throw new IllegalArgumentException("La position ne peut pas être nulle.");
        }
    }

    /**
     * Vérifie si le bouclier est actif.
     *
     * @return true si le bouclier est actif, false sinon.
     */
    public boolean estBouclierActif() {
        return bouclierActif;
    }

    /**
     * Active ou désactive le bouclier.
     *
     * @param bouclierActif true pour activer le bouclier, false pour le désactiver.
     */
    public void setBouclierActif(boolean bouclierActif) {
        this.bouclierActif = bouclierActif;
    }

    /**
     * Vérifie si le tir est actif.
     *
     * @return true si le tir est actif, false sinon.
     */
    public boolean estTireActif() {
        return tireActif;
    }

    /**
     * Active ou désactive le tir.
     *
     * @param tireActif true pour activer le tir, false pour le désactiver.
     */
    public void setTireActif(boolean tireActif) {
        this.tireActif = tireActif;
    }

    /**
     * Obtient le gestionnaire de messages.
     *
     * @return Le gestionnaire de messages.
     */
    public MessageHandler getMessageHandler() {
        return this.messageHandler;
    }

    /**
     * Définit le gestionnaire de messages.
     *
     * @param messageHandler Le nouveau gestionnaire de messages.
     */
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * Obtient la liste des armes du combattant.
     *
     * @return La liste des armes.
     */
    public List<Arme> getArmes() {
        return armes;
    }

    /**
     * Obtient la liste des explosifs du combattant.
     *
     * @return La liste des explosifs.
     */
    public List<Explosif> getExplosifs() {
        return explosifs;
    }

    // Méthodes d'action

    /**
     * Réduit l'énergie du combattant d'une valeur spécifiée.
     *
     * @param valeur La quantité d'énergie à soustraire.
     */
    public void perdreEnergie(int valeur) {
        this.energie -= valeur;
        if (this.energie < 0) {
            this.energie = 0;
        }
    }

    /**
     * Vérifie si le combattant est encore en vie (énergie > 0).
     *
     * @return true si le combattant est en vie, false sinon.
     */
    public boolean estEnVie() {
        return this.energie > 0;
    }

    /**
     * Vérifie si le combattant est éliminé (plus d'énergie).
     * Si le combattant est éliminé, il libère sa case sur la grille.
     *
     * @return true si le combattant est éliminé, false sinon.
     */
    public boolean verifierElimination() {
        if (!estEnVie()) {
            grille.getCase(position).setOccupant(null); // Libère la case
            messageHandler.afficherMessage(nom + " a été éliminé et a quitté la case (" +
                    position.getX() + ", " + position.getY() + ").");
            return true;
        }
        return false;
    }

    /**
     * Ajoute une arme à l'inventaire du combattant.
     *
     * @param arme L'arme à ajouter.
     * @throws IllegalArgumentException si l'arme est nulle.
     */
    public void ajouterArme(Arme arme) {
        if (arme != null) {
            this.armes.add(arme);
        } else {
            throw new IllegalArgumentException("L'arme ne peut pas être nulle.");
        }
    }

    /**
     * Ajoute un explosif à l'inventaire du combattant.
     *
     * @param explosif L'explosif à ajouter.
     * @throws IllegalArgumentException si l'explosif est nul.
     */
    public void ajouterExplosif(Explosif explosif) {
        if (explosif != null) {
            this.explosifs.add(explosif);
        } else {
            throw new IllegalArgumentException("L'explosif ne peut pas être nul.");
        }
    }

    /**
     * Vérifie si le combattant possède au moins une arme ou un explosif selon le type spécifié.
     *
     * @param type Le type de possession à vérifier (ARMES, EXPLOSIFS, TOUS).
     * @return true si le combattant possède l'élément spécifié, false sinon.
     * @throws IllegalArgumentException si le type est invalide.
     */
    private boolean verifierPossession(PossessionType type) {
        boolean possedeArmes = !armes.isEmpty();
        boolean possedeExplosifs = !explosifs.isEmpty();

        switch (type) {
            case ARMES:
                if (!possedeArmes) {
                    messageHandler.afficherErreur("Aucune arme disponible !");
                    return false;
                }
                break;

            case EXPLOSIFS:
                if (!possedeExplosifs) {
                    messageHandler.afficherErreur("Aucun explosif disponible !");
                    return false;
                }
                break;

            case TOUS:
                if (!possedeArmes && !possedeExplosifs) {
                    messageHandler.afficherErreur("Aucune arme ou explosif disponible !");
                    return false;
                }
                if (!possedeArmes) {
                    messageHandler.afficherMessage("Aucune arme disponible, utilisez vos explosifs !");
                }
                if (!possedeExplosifs) {
                    messageHandler.afficherMessage("Aucun explosif disponible, utilisez vos armes !");
                }
                break;

            default:
                throw new IllegalArgumentException("Type de vérification invalide : " + type);
        }

        return true;
    }

    /**
     * Déplace le combattant vers une case cible si les conditions le permettent.
     *
     * @param caseCible La case cible vers laquelle se déplacer.
     * @return true si le déplacement a réussi, false sinon.
     */
    @Override
    public boolean deplacer(Case caseCible) {
        Position currentPos = this.getPosition();
        Position targetPos = caseCible.getPosition();

        if (!currentPos.estVoisine(targetPos, false) ||
                caseCible.estUnMur() ||
                caseCible.estOccupee(false) ||
                this.getEnergie() < Parametres.coutDeplacement) {

            messageHandler.afficherErreur("Déplacement refusé : Condition invalide (case non voisine, mur, case occupée ou énergie insuffisante) !");
            return false;
        }

        if (caseCible.contientPastilleEnergie()) {
            caseCible.setContientPastilleEnergie(false);
            setEnergie(getEnergie() + Parametres.energiePastille);
            messageHandler.afficherMessage("Pastille récupérée : " + Parametres.energiePastille + " points d'énergie gagnés !");
        }

        if (caseCible.getExplosif() != null) {
            Explosif explosif = caseCible.getExplosif();
            explosif.explose(this, caseCible);
        }

        perdreEnergie(Parametres.coutDeplacement);

        grille.getCase(currentPos).setOccupant(null);

        setPosition(targetPos);
        caseCible.setOccupant(this);

        messageHandler.afficherMessage(this.nom + " s'est déplacé à la position (" +
                caseCible.getPosition().getX() + ", " + caseCible.getPosition().getY() + ").");

        return true;
    }

    /**
     * Dépose un explosif sur une case cible si les conditions le permettent.
     *
     * @param cible    La case sur laquelle déposer l'explosif.
     * @param explosif L'explosif à déposer.
     * @return true si l'explosif a été déposé avec succès, false sinon.
     */
    private boolean deposerExplosif(Case cible, Explosif explosif) {
        if (!verifierPossession(PossessionType.EXPLOSIFS) || !explosifs.contains(explosif)) {
            messageHandler.afficherErreur("Action refusée : Explosif non disponible ou inexistant !");
            return false;
        }

        if (cible.getExplosif() != null) {
            messageHandler.afficherErreur("Action refusée : Un explosif est déjà présent sur cette case !");
            return false;
        }

        explosifs.remove(explosif);
        cible.ajouterExplosif(explosif);
        messageHandler.afficherMessage(this.nom + " a déposé un " + explosif.getClass().getSimpleName().toLowerCase() +
                " à la position (" + cible.getPosition().getX() + ", " + cible.getPosition().getY() + ").");
        return true;
    }

    /**
     * Dépose une mine sur une case cible si le combattant en possède.
     *
     * @param cible La case sur laquelle déposer la mine.
     * @return true si la mine a été déposée avec succès, false sinon.
     */
    @Override
    public boolean deposerMine(Case cible) {
        Mine mine = (Mine) explosifs.stream()
                .filter(e -> e instanceof Mine)
                .findFirst()
                .orElse(null);

        if (mine == null) {
            messageHandler.afficherErreur("Vous n'avez pas de mine disponible !");
            return false;
        }

        return deposerExplosif(cible, mine);
    }

    /**
     * Dépose une bombe sur une case cible si le combattant en possède.
     *
     * @param cible La case sur laquelle déposer la bombe.
     * @return true si la bombe a été déposée avec succès, false sinon.
     */
    @Override
    public boolean deposerBombe(Case cible) {
        Bombe bombe = (Bombe) explosifs.stream()
                .filter(e -> e instanceof Bombe)
                .findFirst()
                .orElse(null);

        if (bombe == null) {
            messageHandler.afficherErreur("Vous n'avez pas de bombe disponible !");
            return false;
        }

        return deposerExplosif(cible, bombe);
    }

    /**
     * Effectue un tir avec une arme vers une direction donnée en ciblant une case.
     *
     * @param arme      L'arme utilisée pour tirer.
     * @param caseCible La case ciblée par le tir.
     * @param direction La direction du tir (HORIZONTAL ou VERTICAL).
     * @return true si le tir a été effectué avec succès, false sinon.
     */
    @Override
    public boolean tirer(Arme arme, Case caseCible, Direction direction) {
        if (!verifierPossession(PossessionType.ARMES) || !armes.contains(arme)) {
            messageHandler.afficherErreur("Action refusée : Arme non disponible ou inexistante !");
            return false;
        }

        if (!consommerMunition(arme)) {
            return false;
        }
        tireActif = true;
        Position currentPosition = this.getPosition();
        int portee = arme.getPortee();

        int deltaX = 0;
        int deltaY = 0;

        if (direction == Direction.HORIZONTAL) {
            deltaX = caseCible.getPosition().getX() > currentPosition.getX() ? 1 : -1;
        } else if (direction == Direction.VERTICAL) {
            deltaY = caseCible.getPosition().getY() > currentPosition.getY() ? 1 : -1;
        }

        Position positionTir = new Position(currentPosition.getX(), currentPosition.getY());

        for (int i = 1; i <= portee; i++) {
            positionTir = new Position(positionTir.getX() + deltaX, positionTir.getY() + deltaY);

            if (!grille.estPositionValide(positionTir)) {
                break;
            }

            Case caseCourante = grille.getCase(positionTir);

            if (caseCourante.getOccupant() != null) {
                Combattant cible = caseCourante.getOccupant();
                int degats = arme.getCoutEnergie();

                cible.perdreEnergie(degats);
                messageHandler.afficherMessage(this.nom + " a tiré avec " + arme.getType() +
                        " et a frappé " + cible.getNom() +
                        " à la position (" + caseCourante.getPosition().getX() + ", " + caseCourante.getPosition().getY() +
                        "), infligeant " + degats + " points de dégâts !");

                cible.verifierElimination();
                return true;
            }

            if (caseCourante.estUnMur()) {
                messageHandler.afficherMessage(this.nom + " a tiré avec " + arme.getType() +
                        " mais le tir a été bloqué par un mur à la position (" +
                        caseCourante.getPosition().getX() + ", " + caseCourante.getPosition().getY() + ").");
                return true;
            }
        }

        messageHandler.afficherMessage(this.nom + " a tiré avec " + arme.getType() +
                " mais n'a touché aucun adversaire.");
        return true;
    }

    /**
     * Active le bouclier du combattant si les conditions le permettent.
     *
     * @return true si le bouclier a été activé avec succès, false sinon.
     */
    @Override
    public boolean activerBouclier() {
        if (this.energie >= Parametres.coutUtilisationBouclier) {
            bouclierActif = true;
            perdreEnergie(Parametres.coutUtilisationBouclier);
            messageHandler.afficherMessage(this.nom + " avez activé votre bouclier.");
            return true;
        } else {
            messageHandler.afficherErreur("Énergie insuffisante pour activer le bouclier !");
            return false;
        }
    }

    /**
     * Le combattant choisit d'attendre pour économiser de l'énergie.
     */
    @Override
    public void attendre() {
        messageHandler.afficherMessage(this.nom + " avez choisi d'attendre pour économiser son énergie.");
    }

    /**
     * Enumération des types de possessions qu'un combattant peut avoir.
     */
    public enum PossessionType {
        ARMES, EXPLOSIFS, TOUS
    }

    /**
     * Consomme une munition de l'arme spécifiée.
     * Si l'arme n'a plus de munitions, elle est retirée de l'inventaire.
     *
     * @param arme L'arme dont on veut consommer une munition.
     * @return true si la munition a été consommée, false si l'arme n'avait plus de munitions.
     */
    private boolean consommerMunition(Arme arme) {
        if (arme.getMunition() <= 0) {
            armes.remove(arme);
            messageHandler.afficherMessage("L'arme " + arme.getType() + " a été retirée car elle n'a plus de munitions.");
            return false;
        } else {
            arme.setMunition(arme.getMunition() - 1);
            return true;
        }
    }

    /**
     * Retourne une représentation textuelle du combattant.
     *
     * @return "€" si le bouclier est actif, "!" sinon.
     */
    @Override
    public String toString() {
        return this.bouclierActif ? "€" : "!";
    }
}
