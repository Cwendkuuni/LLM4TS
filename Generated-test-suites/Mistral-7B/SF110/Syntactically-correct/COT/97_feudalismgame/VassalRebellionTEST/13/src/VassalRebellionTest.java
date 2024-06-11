package src.test;  // Make sure to import necessary packages and classes at the beginning of the test file

import static org.junit.Assert.*;

import src.Fiefdoms;
import src.VassalRebellion;
import java.util.*;

public class VassalRebellionTest {

    @org.junit.Before
    public void setup() {
        // Initialize fiefdom object and its attributes for testing
        Fiefdoms fiefdoms = new Fiefdoms();
        String fiefdomName = "testFiefdom";
        fiefdoms.addFiefdom(new Fiefdom(fiefdomName, 10, 5, 15, new ArrayList<>(), true));
        this.fiefdomObj = fiefdoms.getFiefdom(fiefdomName);
    }

    @org.junit.Test
    public void testPerform_LoyalVassalsAndCastles_Rebellion() {
        // Test performing rebellion on a fiefdom with loyal vassals and castles
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> rebellionTrigger = new ArrayList<>();
        rebellionTrigger.add(this.fiefdomObj.getName());
        vassalRebellion.perform(rebellionTrigger);

        // Check if rebellion was successful and fiefdom kingdom status was changed
        assertTrue(this.fiefdomObj.isRebellious());
        assertFalse(this.fiefdomObj.isKingdom());
    }

    @org.junit.Test
    public void testPerform_EmptyLoyalVassals_RollEvent() {
        // Test performing rebellion on a fiefdom with no loyal vassals
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> rebellionTrigger = new ArrayList<>();
        rebellionTrigger.add(this.fiefdomObj.getName());
        this.fiefdomObj.setLoyalVassals(new ArrayList<>());
        vassalRebellion.perform(rebellionTrigger);

        // Check if RollEvent was triggered
        assertTrue(this.fiefdomObj.getRollEvents().size() > 0);
    }

    @org.junit.Test
    public void testPerform_LoyalVassalsAndNoCastles_NoRebellion() {
        // Test performing rebellion on a fiefdom with loyal vassals but no castles
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> rebellionTrigger = new ArrayList<>();
        rebellionTrigger.add(this.fiefdomObj.getName());
        this.fiefdomObj.setPersonalCastles(0);
        this.fiefdomObj.setFiefdomCastles(0);
        vassalRebellion.perform(rebellionTrigger);

        // Check if no rebellion occurred
        assertFalse(this.fiefdomObj.isRebellious());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testPerform_NullFiefdomName() {
        // Test performing rebellion on a null fiefdom name
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> rebellionTrigger = new ArrayList<>();
        rebellionTrigger.add(null);
        vassalRebellion.perform(rebellionTrigger);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testPerform_NullFiefdomObject() {
        // Test performing rebellion on a null fiefdom object
        VassalRebellion vassalRebellion = new VassalRebbellion();
        this.fiefdomObj = null;
        Collection<String> rebellionTrigger = new ArrayList<>();
        rebellionTrigger.add(this.fiefdomObj.getName());
        vassalRebellion.perform(rebellionTrigger);
    }

    private Fiefdom fiefdomObj;
}