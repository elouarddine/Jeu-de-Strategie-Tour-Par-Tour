package model;

import model.Grille;
import model.Combattant;
import utils.strategyMessage.MessageHandler;
import utils.configuration.ConfigLoader;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

public class GrilleTest {

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
    }

    @Test
    public void testIsOver_UnSeulCombattantEnVie() {
        Combattant combattantVivant = mock(Combattant.class);
        when(combattantVivant.estEnVie()).thenReturn(true);

        Combattant combattantMort = mock(Combattant.class);
        when(combattantMort.estEnVie()).thenReturn(false);

        grille.getAllCombattants().addAll(Arrays.asList(combattantVivant, combattantMort));

        assertTrue("Le jeu doit être terminé lorsqu'il ne reste qu'un seul combattant en vie.", grille.isOver());
    }

    @Test
    public void testIsOver_PlusieursCombattantsEnVie() {
        Combattant combattant1 = mock(Combattant.class);
        when(combattant1.estEnVie()).thenReturn(true);

        Combattant combattant2 = mock(Combattant.class);
        when(combattant2.estEnVie()).thenReturn(true);

        grille.getAllCombattants().addAll(Arrays.asList(combattant1, combattant2));

        assertFalse("Le jeu ne doit pas être terminé lorsqu'il reste plusieurs combattants en vie.", grille.isOver());
    }

    @Test
    public void testIsOver_AucunCombattantEnVie() {
        Combattant combattant1 = mock(Combattant.class);
        when(combattant1.estEnVie()).thenReturn(false);

        Combattant combattant2 = mock(Combattant.class);
        when(combattant2.estEnVie()).thenReturn(false);

        grille.getAllCombattants().addAll(Arrays.asList(combattant1, combattant2));

        assertTrue("Le jeu doit être terminé lorsqu'il ne reste aucun combattant en vie.", grille.isOver());
    }
}
