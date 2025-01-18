package model;

import model.Case;
import model.Grille;
import model.Position;
import model.Combattant;
import model.Explosif;
import utils.strategyMessage.MessageHandler;
import utils.configuration.ConfigLoader;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

public class CaseTest {

    private Grille grille;
    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        
        try {
            ConfigLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Erreur lors du chargement de la configuration : " + e.getMessage());
        }

        // Initialiser la grille et le messageHandler
        messageHandler = mock(MessageHandler.class);
        grille = new Grille(messageHandler);

        assertNotNull("La grille ne doit pas être nulle.", grille);
        assertTrue("La largeur de la grille doit être au moins de 6.", grille.getLargeur() >= 6);
        assertTrue("La longueur de la grille doit être au moins de 6.", grille.getLongueur() >= 6);
    }

    @Test
    public void testGetCasesVoisines_CaseCentrale() {
        // Position centrale
        Position positionCentrale = new Position(5, 5);
        Case caseCentrale = grille.getCase(positionCentrale);
        
        assertNotNull("La case centrale ne doit pas être nulle.", caseCentrale);
        // Obtenir les cases voisines
        List<Case> casesVoisines = caseCentrale.getCasesVoisines();

        // Vérifier qu'il y a 8 cases voisines
        assertEquals("La case centrale doit avoir 8 cases voisines.", 8, casesVoisines.size());
    }

    @Test
    public void testGetCasesVoisines_Coin() {
        // Coin supérieur gauche
        Position positionCoin = new Position(0, 0);
        Case caseCoin = grille.getCase(positionCoin);

        // Obtenir les cases voisines
        List<Case> casesVoisines = caseCoin.getCasesVoisines();

        // Vérifier qu'il y a 3 cases voisines (les coins ont 3 voisins)
        assertEquals("La case dans le coin doit avoir 3 cases voisines.", 3, casesVoisines.size());
    }

    @Test
    public void testCopierAvecFiltrage_ExplosifVisible() {
        // Créer un combattant
        Combattant combattant = mock(Combattant.class);

        // Créer une case avec un explosif visible pour le combattant
        Position position = new Position(5, 5);
        Case caseOriginale = grille.getCase(position);
        Explosif explosif = mock(Explosif.class);
        when(explosif.estVisiblePour(combattant)).thenReturn(true);
        caseOriginale.setExplosif(explosif);

        // Copier la case avec filtrage
        Case caseCopie = caseOriginale.copierAvecFiltrage(combattant);

        // Vérifier que l'explosif est copié
        assertNotNull("L'explosif doit être copié.", caseCopie.getExplosif());
    }

    @Test
    public void testCopierAvecFiltrage_ExplosifNonVisible() {
        // Créer un combattant
        Combattant combattant = mock(Combattant.class);

        // Créer une case avec un explosif non visible pour le combattant
        Position position = new Position(5, 5);
        Case caseOriginale = grille.getCase(position);
        Explosif explosif = mock(Explosif.class);
        when(explosif.estVisiblePour(combattant)).thenReturn(false);
        caseOriginale.setExplosif(explosif);

        // Copier la case avec filtrage
        Case caseCopie = caseOriginale.copierAvecFiltrage(combattant);

        // Vérifier que l'explosif n'est pas copié
        assertNull("L'explosif ne doit pas être copié.", caseCopie.getExplosif());
    }
}
