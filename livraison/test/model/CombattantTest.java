package model;

import model.Grille;
import model.Position;
import model.combattantFactory.Guerrier; 
import utils.strategyMessage.MessageHandler;
import utils.configuration.ConfigLoader;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CombattantTest {

    private Grille grille;
    private MessageHandler messageHandler;
    private Guerrier combattant;

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

        // Créer un guerrier pour les tests
        Position position = new Position(0, 0);
        combattant = new Guerrier("TestGuerrier", position, messageHandler, grille);
    }

    @Test
    public void testEstEnVie() {
        // Vérifier que le combattant est en vie au début
        assertTrue("Le combattant doit être en vie au départ.", combattant.estEnVie());

        // Obtenir les points de vie du combattant
        int vie = combattant.getEnergie(); // Utilisez la méthode correcte pour obtenir l'énergie

        // Infliger des dégâts égaux à ses points de vie
        combattant.perdreEnergie(vie); // Utilisez la méthode correcte pour infliger des dégâts

        // Vérifier que le combattant est mort
        assertFalse("Le combattant doit être mort après avoir reçu des dégâts létaux.", combattant.estEnVie());
    }

}
