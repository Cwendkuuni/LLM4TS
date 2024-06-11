package src.test.java;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import src.actions.*;
import src.actions.exceptions.*;
import src.model.*;

public class VassalRebellionTest {

    @Before
    public void setUp() {
        Map map = new Map();
        Fiefdom testFiefdom = new Fiefdom("testFiefdom", new ArrayList<>(), 10, 2, 5, 0, 5, false, false);
        map.put("testFiefdom", testFiefdom);
    }

    @Test
    public void testPerformWithLoyalVassals() {
        Fiefdom fiefdomObj = Map.map.get("testFiefdom");
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.setFiefdomObj(fiefdomObj);
        vassalRebellion.perform(Arrays.asList("testFiefdom"));
        assertTrue(fiefdomObj.isRebellious());
        assertFalse(fiefdomObj.isKingdom());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerformWithEmptyCollection() {
        Fiefdom fiefdomObj = Map.map.get("testFiefdom");
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.setFiefdomObj(fiefdomObj);
        vassalRebellion.perform(Collections.emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerformWithNullCollection() {
        Fiefdom fiefdomObj = Map.map.get("testFiefdom");
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.setFiefdomObj(fiefdomObj);
        vassalRebellion.perform(null);
    }

    @Test
    public void testPerformWithNoLoyalVassals() {
        Fiefdom fiefdomObj = Map.map.get("testFiefdom");
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.setFiefdomObj(fiefdomObj);
        vassalRebellion.perform(Arrays.asList("testFiefdom"));
        new RollEvent(fiefdomObj.getName());
        assertFalse(fiefdomObj.isRebellious());
        assertFalse(fiefdomObj.isKingdom());
    }

    @Test
    public void testPerformWithPersonalCastlesAndFiefdomCastles() {
        Fiefdom fiefdomObj = Map.map.get("testFiefdom");
        int personalCastles = 1;
        int fiefdomCastles = 2;
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.setFiefdomObj(fiefdomObj);
        vassalRebellion.perform(Arrays.asList("testFiefdom"));
        assertTrue(fiefdomObj.isRebellious());
        assertFalse(fiefdomObj.isKingdom());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerformWithNegativePersonalCastles() {
        Fiefdom fiefdomObj = Map.map.get("testFiefdom");
        int personalCastles = -1;
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.setFiefdomObj(fiefdomObj);
        vassalRebellion.perform(Arrays.asList("testFiefdom"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerformWithNegativeFiefdomCastles() {
        Fiefdom fiefdomObj = Map.map.get("testFiefdom");
        int fiefdomCastles = -1;
        VassalRebellion vassalRebellion = new VassalRebbellion();
        vassalRebellion.setFiefdomObj(fiefdomObj);
        vassalRebbellion.perform(Arrays.asList("testFiefdom"));
    }

    @Test
    public void testGetAndSetLoyalVassalsAndKingdom() {
        Fiefdom fiefdomObj = Map.map.get("testFiefdom");
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.setFiefdomObj(fiefdomObj);

        List<Vassal> expectedLoyalVassals = new ArrayList<>();
        Vassal vassal = new Vassal("Vassal1", 1, true);
        expectedLoyalVassals.add(vassal);
        when(fiefdomObj.getLoyalVassals()).thenReturn(expectedLoyalVassals);
        List<Vassal> actualLoyalVassals = vassalRebellion.getLoyalVassals();
        assertEquals(expectedLoyalVassals, actualLoyalVassals);

        List<Vassal> newLoyalVassals = new ArrayList<>();
        vassal = new Vassal("Vassal2", 2, true);
        newLoyalVassals.add(vassal);
        vassalRebellion.setLoyalVassals(newLoyalVassals);
        List<Vassal> expectedNewLoyalVassals = newLoyalVassals;
        List<Vassal> actualNewLoyalVassals = vassalRebellion.getLoyalVassals();
        assertEquals(expectedNewLoyalVassals, actualNewLoyalVassals);

        assertFalse(vassalRebellion.isKingdom());
        vassalRebellion.setKingdom(true);
        assertTrue(vassalRebellion.isKingdom());
    }
}