package src.test.java;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import src.*;

public class VassalRebellionTest {

    private Fiefdoms fiefdomObj;
    private VassalRebellion vassalRebellion;

    @Before
    public void setup() {
        Map map = new Map();
        fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>());
        map.put("TestFiefdom", fiefdomObj);
        vassalRebellion = new VassalRebbellion(map);
    }

    @Test
    public void testPerformWithLoyalVassalsAndCastles() {
        // Given: Fiefdom with loyal vassals and castles
        int expectedRebelliousVassals = fiefdomObj.getLoyalVassals().size() - 5; // 5 vassals rebelled
        vassalRebellion.perform(Arrays.asList("TestFiefdom"));

        // Then: Fiefdom has rebellious vassals
        assertEquals(expectedRebelliousVassals, fiefdomObj.getRebelliousVassals().size());

        // And: Fiefdom is not in kingdom anymore
        assertFalse(fiefdomObj.isKingdom());

        // And: Check if personal castles and fiefdom castles affect the number of rebellious vassals
        int personalCastles = 2;
        int fiefdomCastles = 3;
        fiefdomObj = new Fiefdoms("TestFiefdom2", 10, 5, new ArrayList<>());
        map.put("TestFiefdom2", fiefdomObj);
        vassalRebellion = new VassalRebbellion(map);
        vassalRebellion.perform(Arrays.asList("TestFiefdom2"));
        int rebelliousVassalsWithCastles = fiefdomObj.getLoyalVassals().size() - (personalCastles * 10 + fiefdomCastles * 25);
        assertEquals(rebelliousVassalsWithCastles, fiefdomObj.getRebelliousVassals().size());
    }

    @Test
    public void testPerformWithEqualNumberOfLoyalVassalsAndValue() {
        // Given: Fiefdom with equal number of loyal vassals and value
        int expectedRebelliousVassals = 0;
        vassalRebellion.perform(Arrays.asList("TestFiefdom"));

        // Then: No rebellious vassals
        assertEquals(expectedRebelliousVassals, fiefdomObj.getRebelliousVassals().size());
    }

    @Test(expected = NullPointerException.class)
    public void testPerformWithNullFiefdom() {
        // Given: Null fiefdom object
        vassalRebellion.perform(Arrays.asList("TestFiefdomNull"));
    }
}