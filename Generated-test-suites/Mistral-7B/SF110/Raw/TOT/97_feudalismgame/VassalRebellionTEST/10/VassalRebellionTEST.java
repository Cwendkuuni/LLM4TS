package src.test;import org.junit.Before;import org.junit.Test;import src.Fiefdoms;import src.VassalRebellion;

import java.util.*;

import static org.junit.Assert.assertFalse;import static org.junit.Assert.assertNull;import static org.junit.Assert.assertTrue;

public class VassalRebbellionTest {

 private Fiefdoms fiefdomObj;
 private VassalRebbellion vassalRebellion;

 @Before
 public void setup() {
 fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>());
 vassalRebellion = new VassalRebbellion();
 }

 @Test
 public void testPerform_WithValidFiefdomNameAndValidValues_ShouldTriggerRebellion() {
 // Given: A valid fiefdom with loyal vassals and castles
 fiefdomObj.addLoyalVassal("Vassal1");
 fiefdomObj.addLoyalVassal("Vassal2");
 fiefdomObj.addCastle();
 fiefdomObj.addCastle();

 // When: Perform the action with the fiefdom name
 Collection<String> collection = new ArrayList<>();
 collection.add(fiefdomObj.getName());
 vassalRebellion.perform(collection);

 // Then: The fiefdom should be rebellious and the kingdom should be false
 assertTrue(fiefdomObj.isRebellious());
 assertFalse(fiefdomObj.isKingdom());
 }

 @Test
 public void testPerform_WithValidFiefdomNameAndNoLoyalVassals_ShouldRollEvent() {
 // Given: A valid fiefdom with no loyal vassals

 // When: Perform the action with the fiefdom name
 Collection<String> collection = new ArrayList<>();
 collection.add(fiefdomObj.getName());
 vassalRebellion.perform(collection);

 // Then: A roll event should be triggered
 assertNull(vassalRebbellion.fiefdomObj); // Since the fiefdomObj is set in perform method and not accessible in test
 assertTrue(RollEvent.eventTriggered);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_WithInvalidFiefdomName_ShouldThrowIllegalArgumentException() {
 // Given: An invalid fiefdom name
 Collection<String> collection = new ArrayList<>();
 collection.add("InvalidName");

 // When: Perform the action with the invalid fiefdom name
 vassalRebbellion.perform(collection);
 }
}