package src.test;

import org.junit.*;
import static org.junit.Assert.*;
import src.*;
import java.util.*;

public class VassalRebellionTest {
    private VassalRebellion vassalRebellionObj;
    private Fiefdoms fiefdomObj;

    @Before
    public void setUp() {
        // initialize objects and set up the expected state
        fiefdomObj = new Fiefdoms("testFiefdom", 10, 5, new ArrayList<>(), new ArrayList<>(), true, true);
        vassalRebellionObj = new VassalRebellion();
        Map.map.put("testFiefdom", fiefdomObj);
    }

    @Test
    public void testPerformWithLoyalVassalsAndEnoughCastles() {
        // Test case for typical use case
        vassalRebellionObj.perform(Arrays.asList("testFiefdom"));
        assertTrue(fiefdomObj.isRebellious());
        assertEquals(fiefdomObj.getLoyalVassals().size(), fiefdomObj.getLoyalVassals().size() - (fiefdomObj.getPersonalCastles() * 10 + fiefdomObj.getFiefdomCastles() * 25));
    }

    @Test
    public void testPerformWithLoyalVassalsButNotEnoughCastles() {
        // Test case for edge case: Loyal vassals but not enough castles
        fiefdomObj = new Fiefdoms("testFiefdom", 10, 2, new ArrayList<>(), new ArrayList<>(), true, true);
        vassalRebellionObj = new VassalRebellion();
        Map.map.put("testFiefdom", fiefdomObj);

        vassalRebellionObj.perform(Arrays.asList("testFiefdom"));
        assertFalse(fiefdomObj.isRebellious());
    }

    @Test (expected = NullPointerException.class)
    public void testPerformWithNonExistingFiefdom() {
        vassalRebellionObj.perform(Arrays.asList("nonExistingFiefdom"));
    }

    @Test
    public void testPerformWithNoLoyalVassals() {
        // Test case for edge case: No loyal vassals
        fiefdomObj = new Fiefdoms("testFiefdom", 10, 5, new ArrayList<>(), new ArrayList<>(), false, true);
        vassalRebellionObj = new VassalRebellion();
        Map.map.put("testFiefdom", fiefdomObj);

        vassalRebellionObj.perform(Arrays.asList("testFiefdom"));
        new RollEvent("testFiefdom");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testPerformWithNegativeNumberOfLoyalVassals() {
        vassalRebellionObj.perform(Arrays.asList("testFiefdom"));
        fiefdomObj.setLoyalVassals(Collections.nCopies(-1, new Vassal("testVassal", false)));
    }
}