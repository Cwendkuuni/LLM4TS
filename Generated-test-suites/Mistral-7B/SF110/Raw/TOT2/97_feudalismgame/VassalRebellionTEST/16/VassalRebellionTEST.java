package src.test.vassalRebellion;

import org.junit.Before;
import org.junit.Test;
import src.VassalRebellion;
import src.Fiefdoms;
import java.util.*;

public class VassalRebellionTest {
    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdoms;

    @Before
    public void setup() {
        fiefdoms = new Fiefdoms();
        vassalRebellion = new VassalRebellion();
        Map<String, Fiefdoms> map = fiefdoms.getMap();
        map.put("TestFiefdom", new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>()));
        vassalRebellion.setMap(map);
    }

    @Test
    public void testPerform() {
        Collection<String> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);

        // Check if the fiefdom's kingdom status has been set to false
        assertEquals(false, fiefdoms.getMap().get("TestFiefdom").isKingdom());

        // Check if the fiefdom's rebellious vassals have been set
        assertEquals(expectedSize, fiefdoms.getMap().get("TestFiefdom").getRebelliousVassals().size());
    }

    @Test
    public void testEmptyCollection() {
        vassalRebellion.perform(new ArrayList<>());

        // Check if no exception is thrown
        assertNull(assertThrows(NullPointerException.class, () -> vassalRebellion.perform(null)));
    }

    @Test
    public void testNonExistentFiefdom() {
        Collection<String> col = new ArrayList<>();
        col.add("NonExistentFiefdom");
        vassalRebellion.perform(col);

        // Check if no rebellion has occurred in the non-existent fiefdom
        assertFalse(fiefdoms.getMap().get("NonExistentFiefdom").isKingdom());
        assertTrue(fiefdoms.getMap().get("NonExistentFiefdom").getRebelliousVassals().isEmpty());
    }

    @Test
    public void testNoLoyalVassals() {
        Map<String, Fiefdoms> map = fiefdoms.getMap();
        map.put("TestFiefdom", new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>()));
        vassalRebellion.setMap(map);
        Collection<String> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);

        // Check if no rebellion has occurred in a fiefdom with no loyal vassals
        assertFalse(fiefdoms.getMap().get("TestFiefdom").isKingdom());
        assertTrue(fiefdoms.getMap().get("TestFiefdom").getRebelliousVassals().isEmpty());
    }

    // Add test cases for edge cases and exception handling with no castles, negative loyal vassals, etc.
}