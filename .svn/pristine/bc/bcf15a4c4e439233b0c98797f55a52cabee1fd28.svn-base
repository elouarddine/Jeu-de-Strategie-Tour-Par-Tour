package model;

import model.Position;

import org.junit.Test;
import static org.junit.Assert.*;

public class PositionTest {

    @Test
    public void testEstVoisine_HuitDirections_Vrai() {
        Position pos1 = new Position(5, 5);
        Position pos2 = new Position(6, 6);

        boolean result = pos1.estVoisine(pos2, true);
        assertTrue("Les positions devraient être voisines en huit directions.", result);
    }

    @Test
    public void testEstVoisine_QuatreDirections_Vrai() {
        Position pos1 = new Position(5, 5);
        Position pos2 = new Position(5, 6);

        boolean result = pos1.estVoisine(pos2, false);
        assertTrue("Les positions devraient être voisines en quatre directions.", result);
    }

    @Test
    public void testEstVoisine_NonVoisines() {
        Position pos1 = new Position(5, 5);
        Position pos2 = new Position(7, 7);

        boolean resultHuitDirections = pos1.estVoisine(pos2, true);
        boolean resultQuatreDirections = pos1.estVoisine(pos2, false);

        assertFalse("Les positions ne devraient pas être voisines en huit directions.", resultHuitDirections);
        assertFalse("Les positions ne devraient pas être voisines en quatre directions.", resultQuatreDirections);
    }
}
