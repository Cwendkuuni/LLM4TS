import org.junit.Before;
import org.junit.Test;
import src.Fiefdoms;
import src.VassalRebellion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VassalRebellionTest {
    private Fiefdoms map;
    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdom;

    @Before
    public void setup() {
        map = new Fiefdoms();
        map.put("FiefdomA", new Fiefdoms("FiefdomA", 5, 3, 5, 3));
        map.put("FiefdomB", new Fiefdoms("FiefdomB", 0, 3, 0, 3));
        vassalRebellion = new VassalRebbellion();
        fiefdom = map.get("FiefdomA");
    }

    @Test
    public void testPerformWithLoyalVassalsAndCastles() {
        // Given: FiefdomA has loyal vassals and castles
        List<String> collection = new ArrayList<>(Collections.singletonList("FiefdomA"));
        vassalRebellion.perform(collection);

        // Then: Check if FiefdomA becomes rebellious
        assertTrue(fiefdom.isRebellious());

        // Then: Check if FiefdomA's kingdom status is set to false
        assertFalse(fiefdom.isKingdom());

        // Then: Check if the number of rebellious vassals is correct
        int expectedRebelliousVassals = fiefdom.getLoyalVassals().size() - (fiefdom.getPersonalCastles() * 10 + fiefdom.getFiefdomCastles() * 25);
        assertEquals(expectedRebelliousVassals, fiefdom.getRebelliousVassals().size());
    }
}