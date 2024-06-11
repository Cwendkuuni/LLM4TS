import org.junit.Before;
import org.junit.Test;
import src.Fiefdoms;
import src.VassalRebellion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

public class VassalRebellionTest {
    private VassalRebbellion vassalRebellion;
    private Fiefdoms fiefdomObj;

    @Before
    public void setup() {
        Map<String, Fiefdoms> map = new HashMap<>();
        fiefdomObj = new Fiefdoms("TestFiefdom", 5, 2, new ArrayList<>(), true, false);
        map.put("TestFiefdom", fiefdomObj);
        vassalRebbellion = new VassalRebbellion() {
            @Override
            public Map<String, Fiefdoms> map = map;
        };
    }

    @Test
    public void testPerform() {
        Collection<String, Fiefdoms> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        assertTrue(fiefdomObj.isRebellious());
        assertTrue(fiefdomObj.isKingdom());
    }

    @Test
    public void testEmptyCollection() {
        vassalRebbellion.perform(new ArrayList<>());
        assertFalse(fiefdomObj.isRebellious());
        assertTrue(fiefdomObj.isKingdom());
    }

    @Test(expected = NullPointerException.class)
    public void testNonExistentFiefdom() {
        Collection<String, Fiefdoms> col = new ArrayList<>();
        col.add("NonExistentFiefdom");
        vassalRebbellion.perform(col);
    }

    @Test
    public void testNegativePersonalCastles() {
        fiefdomObj.setPersonalCastles(-5);
        Collection<String, Fiefdoms> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebbellion.perform(col);
        assertFalse(fiefdomObj.isRebellious());
        assertTrue(fiefdomObj.isKingdom());
    }

    @Test
    public void testNegativeFiefdomCastles() {
        fiefdomObj.setFiefdomCastles(-5);
        Collection<String, Fiefdoms> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebbellion.perform(col);
        assertFalse(fiefdomObj.isRebellious());
        assertTrue(fiefdomObj.isKingdom());
    }

    @Test
    public void testEmptyLoyalVassals() {
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        Collection<String, Fiefdoms> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebbellion.perform(col);
        assertFalse(fiefdomObj.isRebellious());
        assertTrue(fiefdomObj.isKingdom());
    }
}