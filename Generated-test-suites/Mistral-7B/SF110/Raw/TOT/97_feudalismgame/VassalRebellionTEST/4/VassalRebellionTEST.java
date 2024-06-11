import org.junit.Before;
import org.junit.Test;
import src.Fiefdoms;
import src.VassalRebellion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VassalRebellionTest {

    private Fiefdoms fiefdomMap;
    private VassalRebellion vassalRebellion;

    @Before
    public void setUp() {
        fiefdomMap = new Fiefdoms();
        Map<String, Fiefdoms> map = new HashMap<>();
        map.put("FiefdomA", new Fiefdoms("FiefdomA", 10, 5, 5, 10, new ArrayList<>(), true));
        fiefdomMap.setMap(map);
        vassalRebellion = new VassalRebellion();
        vassalRebellion.setFiefdomObj(map.get("FiefdomA"));
    }

    @Test
    public void testPerformWithLoyalVassalsAndSufficientCastles() {
        List<String> collection = new ArrayList<>();
        collection.add("FiefdomA");

        vassalRebellion.perform(collection);

        assertTrue(vassalRebellion.getFiefdomObj().isRebellious());
        assertFalse(vassalRebellion.getFiefdomObj().isKingdom());
    }
}