package src.test.java;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import src.Fiefdoms;
import src.Actions;
import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdoms fiefdomObj;
 private Actions actionsObj;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 // Create Fiefdoms object
 fiefdomObj = new Fiefdoms("TestFiefdom");
 actionsObj = new Actions();
 vassalRebellion = new VassalRebellion();

 // Set up values for testing
 fiefdomObj.setLoyalVassals(new ArrayList<String>() {{
 add("Vassal1");
 add("Vassal2");
 add("Vassal3");
 add("Vassal4");
 add("Vassal5");
}});
 fiefdomObj.setValue(2);
 fiefdomObj.setPersonalCastles(1);
 fiefdomObj.setFiefdomCastles(2);
 Map.map.put("TestFiefdom", fiefdomObj);
 }

 @Test
 public void testPerform_LoyalVassals_NoRebellion() {
 // Test when there are loyal vassals and no rebellion
 actionsObj.addAction(vassalRebellion, Arrays.asList("TestFiefdom"));
 vassalRebellion.perform(actionsObj.getActions());

 // Check that vassals remain loyal and kingdom is not set to false
 assertEquals(5, fiefdomObj.getLoyalVassals().size());
 assertFalse(fiefdomObj.isRebellious());
 assertFalse(fiefdomObj.isKingdom());
 }

 @Test
 public void testPerform_LoyalVassals_Rebellion() {
 // Test when there are loyal vassals and conditions for rebellion are met
 int expectedRebelliousVassals = 3;
 actionsObj.addAction(vassalRebellion, Arrays.asList("TestFiefdom"));
 vassalRebellion.perform(actionsObj.getActions());

 // Check that rebellious vassals are set and kingdom is set to false
 assertEquals(expectedRebelliousVassals, fiefdomObj.getRebelliousVassals().size());
 assertFalse(fiefdomObj.isKingdom());
 }

 @Test
 public void testPerform_NoLoyalVassals() {
 // Test when there are no loyal vassals
 actionsObj.addAction(vassalRebellion, Arrays.asList("TestFiefdom"));
 vassalRebellion.perform(actionsObj.getActions());

 // Check that a new RollEvent is created
 assertTrue(Map.map.get("TestFiefdom").getRollEventTriggered());
 }

 @Test
 public void testPerform_Castles_Rebellion() {
 // Test when there are loyal vassals, castles, and conditions for rebellion are met
 int expectedRebelliousVassals = 3;
 int personalCastles = 1;
 int fiefdomCastles = 2;

 // Set up values for testing
 fiefdomObj.setValue(2);
 fiefdomObj.setPersonalCastles(personalCastles);
 fiefdomObj.setFiefdomCastles(fiefdomCastles);
 Map.map.put("TestFiefdom", fiefdomObj);
 actionsObj.addAction(vassalRebellion, Arrays.asList("TestFiefdom"));
 vassalRebellion.perform(actionsObj.getActions());

 // Check that rebellious vassals are set and kingdom is set to false
 assertEquals(expectedRebelliousVassals, fiefdomObj.getRebelliousVassals().size());
 assertFalse(fiefdomObj.isKingdom());

 // Check that the correct number of vassals are set as rebellious based on castles
 int totalVassals = fiefdomObj.getLoyalVassals().size();
 int castlesThreshold = (personalCastles * 10) + (fiefdomCastles * 25);
 assertEquals(totalVassals - castlesThreshold, fiefdomObj.getRebelliousVassals().size());
 }

}