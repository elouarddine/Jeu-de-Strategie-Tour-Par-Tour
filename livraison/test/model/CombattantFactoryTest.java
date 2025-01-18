package model;

import model.combattantFactory.CombattantFactory;
import model.Grille;
import model.Position;
import model.Combattant;
import utils.strategyMessage.MessageHandler;
import utils.configuration.ConfigLoader;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CombattantFactoryTest {

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

        // Initialiser les mocks
        grille = mock(Grille.class);
        messageHandler = mock(MessageHandler.class);
    }

    @Test
    public void testCreerCombattantGuerrier() {
        Position position = new Position(0, 0);
        CombattantFactory.TypeCombattant type = CombattantFactory.TypeCombattant.GUERRIER;

        // Créer le combattant
        Combattant combattant = CombattantFactory.creerCombattant(type, "Guerrier1", position, messageHandler, grille);

        // Vérifications
        assertNotNull("Le combattant doit être créé.", combattant);
        assertEquals("Le nom doit correspondre.", "Guerrier1", combattant.getNom());
        assertEquals("La position doit correspondre.", position, combattant.getPosition());
    }

    @Test
    public void testCreerCombattantSniper() {
        Position position = new Position(1, 1);
        CombattantFactory.TypeCombattant type = CombattantFactory.TypeCombattant.SNIPER;

        Combattant combattant = CombattantFactory.creerCombattant(type, "Sniper1", position, messageHandler, grille);

        assertNotNull("Le combattant doit être créé.", combattant);
        assertEquals("Le nom doit correspondre.", "Sniper1", combattant.getNom());
        assertEquals("La position doit correspondre.", position, combattant.getPosition());
    }

    @Test
    public void testCreerCombattantTank() {
        Position position = new Position(2, 2);
        CombattantFactory.TypeCombattant type = CombattantFactory.TypeCombattant.TANK;

        Combattant combattant = CombattantFactory.creerCombattant(type, "Tank1", position, messageHandler, grille);

        assertNotNull("Le combattant doit être créé.", combattant);
        assertEquals("Le nom doit correspondre.", "Tank1", combattant.getNom());
        assertEquals("La position doit correspondre.", position, combattant.getPosition());
    }

    
}
