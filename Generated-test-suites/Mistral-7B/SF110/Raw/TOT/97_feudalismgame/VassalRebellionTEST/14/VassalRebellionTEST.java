package src.test.java;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import src.Fiefdoms;
import src.VassalRebellion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VassalRebellionTest {
    private Fiefdoms fiefdomObj;
    private VassalRebellion vassalRebellion;
    private List<String> collection;

    @Before
    public void setUp() {
        fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, 15);
        collection = new ArrayList<>();
        collection.add("TestFiefdom");
        vassalRebellion = new VassalRebellion();
    }

    @Test
    public void testPerform_LoyalVassalsAndCastles_Rebellion() {
        vassalRebellion.perform(collection);

        // Assertions for the TestFiefdom object
        assertEquals(false, fiefdomObj.isKingdom());
        assertTrue(fiefdomObj.isRebellious());
        assertEquals(15, fiefdomObj.getRebelliousVassals().size());
        
        // Check if personalCastles and fiefdomCastles variables are considered
        if (fiefdomObj.getPersonalCastles() > 0) {
            int expectedRebelliousVassals = fiefdomObj.getLoyalVassals().size() - (fiefdomObj.getPersonalCastles() * 10);
            assertEquals(expectedRebelliousVassals, fiefdomObj.getRebelliousVassals().size());
        }

        if (fiefdomObj.getFiefdomCastles() > 0) {
            int expectedRebelliousVassals = fiefdomObj.getLoyalVassals().size() - (fiefdomObj.getFiefdomCastles() * 25);
            assertEquals(expectedRebelliousVassals, fiefdomObj.getRebelliousVassals().size());
        }
    }

    @Test
    public void testPerform_NoLoyalVassals_RollEvent() {
        vassalRebellion.perform(collection);

        // Assertion for the RollEvent
        assertTrue(fiefdomObj.getRollEventCalled());
    }

    @Test
    public void testPerform_NegativeLoyalVassals_Error() {
        fiefdomObj.setLoyalVassals(Collections.emptyList());

        try {
            vassalRebellion.perform(collection);
            fail("Expected AssertionError or IllegalStateException");
        } catch (AssertionError | IllegalStateException e) {
            // Expected exception
        }
    }

    @After
    public void tearDown() {
        fiefdomObj = null;
        vassalRebellion = null;
        collection = null;
    }
}