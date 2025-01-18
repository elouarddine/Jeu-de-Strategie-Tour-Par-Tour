package model;

/**
 * Interface définissant les actions qu'un combattant peut effectuer.
 */
public interface CombattantActions {
   
   /**
    * Déplace le combattant vers une case cible.
    *
    * @param caseCible La case cible vers laquelle se déplacer.
    * @return true si le déplacement a réussi, false sinon.
    */
   public boolean deplacer(Case caseCible);

   /**
    * Effectue un tir avec une arme vers une direction donnée en ciblant une case.
    *
    * @param arme      L'arme utilisée pour tirer.
    * @param caseCible La case ciblée par le tir.
    * @param direction La direction du tir (HORIZONTAL ou VERTICAL).
    * @return true si le tir a été effectué avec succès, false sinon.
    */
   public boolean tirer(Arme arme, Case caseCible, Direction direction);

   /**
    * Active le bouclier du combattant.
    *
    * @return true si le bouclier a été activé avec succès, false sinon.
    */
   public boolean activerBouclier();

   /**
    * Le combattant choisit d'attendre pour économiser de l'énergie.
    */
   public void attendre();

   /**
    * Dépose une bombe sur une case cible.
    *
    * @param cible La case sur laquelle déposer la bombe.
    * @return true si la bombe a été déposée avec succès, false sinon.
    */
   public boolean deposerBombe(Case cible);

   /**
    * Dépose une mine sur une case cible.
    *
    * @param cible La case sur laquelle déposer la mine.
    * @return true si la mine a été déposée avec succès, false sinon.
    */
   public boolean deposerMine(Case cible);
}
