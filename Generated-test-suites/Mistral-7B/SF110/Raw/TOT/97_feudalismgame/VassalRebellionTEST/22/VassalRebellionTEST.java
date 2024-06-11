import org.junit.Before;
import org.junit.Test;
import src.Fiefdoms;
import src.VassalRebellion;

import static org.junit.Assert.*;

public class VassalRebellionTest {
    private Fiefdoms map;
    private VassalRebellion vassalRebellion;
    private Fiefdom fiefdom;

    @Before
    public void setup() {
        map = new Fiefdoms();
        fiefdom = new Fiefdom("TestFiefdom", 10, 5, 5, 10);
        map.put("TestFiefdom", fiefdom);
        vassalRebellion = new VassalRebellion();
    }

    @Test
    public void testPerformWithRebellionConditionsMet() {
        // Given: A loyal fiefdom with enough vassals and castles
        vassalRebellion.perform(Arrays.asList("TestFiefdom"));

        // Then: The fiefdom should be rebellious and not part of the kingdom
        assertTrue(fiefdom.isRebellious());
        assertFalse(fiefdom.isKingdom());

        // And: The number of loyal vassals should be decreased by the rebellion size
        assertEquals(5, fiefdom.getLoyalVassals().size());

        // And: The personal castles and fiefdom castles should not affect the rebellion size
        vassalRebellion.perform(Arrays.asList("TestFiefdom")); // Perform the action again to test the condition with 0 personal and fiefdom castles
        assertEquals(5, fiefdom.getLoyalVassals().size());
    }
}