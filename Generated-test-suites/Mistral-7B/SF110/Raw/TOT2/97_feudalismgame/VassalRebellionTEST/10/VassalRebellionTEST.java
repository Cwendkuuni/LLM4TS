package src.test.java.src;

import org.junit.Before;
import org.junit.Test;
import src.Fiefdoms;
import src.VassalRebellion;

import java.util.*;

import static org.junit.Assert.*;

public class VassalRebellionTest {
    private Fiefdoms map;
    private VassalRebbellion vassalRebellion;

    @Before
    public void setup() {
        map = new Fiefdoms();
        vassalRebellion = new VassalRebbellion();
        map.put("testFiefdom", new Fiefdoms("testFiefdom", 10, 5, new ArrayList<>()));
        vassalRebbellion.setFiefdomObj(map.get("testFiefdom"));
    }

    @Test
    public void testPerformBasic() {
        List<String> collection = new ArrayList<>();
        collection.add("testFiefdom");
        vassalRebellion.perform(collection);
        assertTrue(vassalRebbellion.getFiefdomObj().isRebellious());
        assertFalse(vassalRebbellion.getFiefdomObj().isKingdom());
    }

    @Test
    public void testPerformEmptyCollection() {
        List<String> collection = Collections.emptyList();
        vassalRebbellion.perform(collection);
        assertFalse(vassalRebbellion.getFiefdomObj().isRebellious());
        assertFalse(vassalRebbellion.getFiefdomObj().isKingdom());
    }

    @Test(expected = NullPointerException.class)
    public void testPerformNullCollection() {
        List<String> collection = null;
        vassalRebbellion.perform(collection);
    }
}