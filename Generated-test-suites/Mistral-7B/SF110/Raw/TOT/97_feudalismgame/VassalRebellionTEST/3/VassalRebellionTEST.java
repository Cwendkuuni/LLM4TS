import org.junit.Before;
import org.junit.Test;
import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {
    private Fiefdoms fiefdomObj;
    private VassalRebellion vassalRebellion;

    @Before
    public void setup() {
        fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, 5, new ArrayList<>());
        vassalRebellion = new VassalRebellion();
        Map.map.put("TestFiefdom", fiefdomObj);
        fiefdomObj.addLoyalVassal();
        fiefdomObj.addLoyalVassal();
        fiefdomObj.addLoyalVassal();
    }

    @Test
    public void testPerformWithRebellionConditionsMet() {
        vassalRebellion.perform(Arrays.asList("TestFiefdom"));
        assertTrue(fiefdomObj.isRebellious());
        assertFalse(fiefdomObj.isKingdom());
        assertEquals(3, fiefdomObj.getRebelliousVassals().size());
    }
}