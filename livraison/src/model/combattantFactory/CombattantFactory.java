package model.combattantFactory;

import utils.configuration.TypeEquipement;
import utils.strategyMessage.MessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Arme;
import model.Bombe;
import model.Case;
import model.Combattant;
import model.Explosif;
import model.Grille;
import model.Mine;
import model.Position;

/**
 * Factory pour créer des combattants avec des équipements aléatoires.
 */
public class CombattantFactory {

    private static final Random random = new Random();

    /**
     * Enumération représentant les types de combattants.
     */
    public enum TypeCombattant {
        GUERRIER,
        TIREUR,
        SNIPER,
        TANK,
        SABOTEUR,
        ENGINEER
    }

    /**
     * Méthode de création de combattants en fonction de leur type.
     *
     * @param typeCombattant Le type de combattant à créer.
     * @param nom Le nom du combattant.
     * @param position La position initiale du combattant.
     * @param messageHandler Gestionnaire de messages pour les interactions du combattant.
     * @param grille La grille du jeu.
     * @return Une instance de Combattant correspondant au type spécifié.
     * @throws IllegalArgumentException Si le type de combattant est inconnu ou si les paramètres sont invalides.
     */
    public static Combattant creerCombattant(
            TypeCombattant typeCombattant,
            String nom,
            Position position,
            MessageHandler messageHandler,
            Grille grille
    ) {
        if (typeCombattant == null) {
            throw new IllegalArgumentException("Le type de combattant ne peut pas être nul.");
        }
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du combattant ne peut pas être nul ou vide.");
        }
        if (position == null) {
            throw new IllegalArgumentException("La position du combattant ne peut pas être nulle.");
        }
        if (messageHandler == null) {
            throw new IllegalArgumentException("Le MessageHandler ne peut pas être nul.");
        }
        if (grille == null) {
            throw new IllegalArgumentException("La grille ne peut pas être nulle.");
        }

        Combattant combattant;

        switch (typeCombattant) {
            case GUERRIER:
                combattant = new Guerrier(nom, position, messageHandler, grille);
                break;
            case TIREUR:
                combattant = new Tireur(nom, position, messageHandler, grille);
                break;
            case SNIPER:
                combattant = new Sniper(nom, position, messageHandler, grille);
                break;
            case TANK:
                combattant = new Tank(nom, position, messageHandler, grille);
                break;
            case SABOTEUR:
                combattant = new Saboteur(nom, position, messageHandler, grille);
                break;
            case ENGINEER:
                combattant = new Engineer(nom, position, messageHandler, grille);
                break;
            default:
                throw new IllegalArgumentException("Type de combattant inconnu : " + typeCombattant);
        }

        assignerArmes(combattant);
        assignerExplosifs(combattant);

        return combattant;
    }

    /**
     * Crée une liste aléatoire de combattants.
     *
     * @param nombre Nombre de combattants à créer.
     * @param grille Grille associée aux combattants.
     * @param messageHandler Gestionnaire de messages pour les combattants.
     * @return Une liste aléatoire de combattants.
     * @throws IllegalArgumentException Si grille ou messageHandler est nul, ou si le nombre est invalide.
     */
    public static List<Combattant> creerListeCombattants(int nombre, Grille grille, MessageHandler messageHandler) {
        if (grille == null) {
            throw new IllegalArgumentException("La grille ne peut pas être nulle.");
        }
        if (messageHandler == null) {
            throw new IllegalArgumentException("Le MessageHandler ne peut pas être nul.");
        }
        if (nombre <= 0) {
            throw new IllegalArgumentException("Le nombre de combattants doit être positif.");
        }

        List<Combattant> combattants = new ArrayList<>();
        TypeCombattant[] typesCombattants = TypeCombattant.values();

        for (int i = 1; i <= nombre; i++) {
            TypeCombattant typeAleatoire = typesCombattants[random.nextInt(typesCombattants.length)];
            String nomCombattant = typeAleatoire.name() + i;

            Position position = trouverPositionLibre(grille);
            if (position == null) {
                throw new IllegalStateException("Aucune position libre disponible pour créer un combattant.");
            }

            Combattant combattant = creerCombattant(typeAleatoire, nomCombattant, position, messageHandler, grille);
            combattants.add(combattant);
        }

        return combattants;
    }

    /**
     * Assigne des armes aléatoires au combattant.
     *
     * @param combattant Le combattant auquel assigner les armes.
     * @throws IllegalArgumentException Si le combattant est nul.
     */
    private static void assignerArmes(Combattant combattant) {
        if (combattant == null) {
            throw new IllegalArgumentException("Le combattant ne peut pas être nul.");
        }

        int nombreArmes = random.nextInt(3) + 1; 
        for (int i = 0; i < nombreArmes; i++) {
            TypeEquipement typeArme = choisirTypeArmeAleatoire();
            Arme arme = new Arme(typeArme);
            combattant.ajouterArme(arme);
        }
    }

    /**
     * Assigne des explosifs aléatoires au combattant.
     *
     * @param combattant Le combattant auquel assigner les explosifs.
     * @throws IllegalArgumentException Si le combattant est nul.
     */
    private static void assignerExplosifs(Combattant combattant) {
        if (combattant == null) {
            throw new IllegalArgumentException("Le combattant ne peut pas être nul.");
        }

        int nombreExplosifs = random.nextInt(3) + 1; 
        for (int i = 0; i < nombreExplosifs; i++) {
            TypeEquipement typeExplosif = choisirTypeExplosifAleatoire();
            Explosif explosif;
            if (typeExplosif == TypeEquipement.BOMBE) {
                explosif = new Bombe(combattant, typeExplosif);
            } else {
                explosif = new Mine(combattant, typeExplosif);
            }
            combattant.ajouterExplosif(explosif);
        }
    }

    /**
     * Choisit un type d'arme aléatoire.
     *
     * @return Un TypeEquipement représentant le type d'arme choisi.
     */
    private static TypeEquipement choisirTypeArmeAleatoire() {
        TypeEquipement[] typesArmes = {
                TypeEquipement.PISTOLET,
                TypeEquipement.FUSIL,
                TypeEquipement.MITRAILLETTE,
                TypeEquipement.BAZOOKA
        };
        return typesArmes[random.nextInt(typesArmes.length)];
    }

    /**
     * Choisit un type d'explosif aléatoire.
     *
     * @return Un TypeEquipement représentant le type d'explosif choisi.
     */
    private static TypeEquipement choisirTypeExplosifAleatoire() {
        TypeEquipement[] typesExplosifs = {
                TypeEquipement.BOMBE,
                TypeEquipement.MINE
        };
        return typesExplosifs[random.nextInt(typesExplosifs.length)];
    }

    /**
     * Trouve une position libre sur la grille.
     *
     * @param grille La grille du jeu.
     * @return Une position libre, ou null si aucune n'est disponible.
     */
    private static Position trouverPositionLibre(Grille grille) {

    	for (int x = 0; x < grille.getLargeur(); x++) {
            for (int y = 0; y < grille.getLongueur(); y++) {
                Position pos = new Position(x, y);
                Case currentCase = grille.getCase(pos);
                if (!currentCase.estUnMur() && !currentCase.estOccupee(true)) {
                    return pos;
                }
            }
        }
        return null; 
    }
}
