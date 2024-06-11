package src.test.java;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import src.Fiefdoms;
import src.Actions;
import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdoms fiefdomObj;
 private Actions actions;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, 5, new ArrayList<>());
 actions = new Actions();
 vassalRebellion = new VassalRebellion();
 }

 @Test
 public void testPerform_LoyalVassals_NoRebellion() {
 // Given: Fiefdom with no rebellious vassals
 vassalRebellion.perform(Collections.singletonList(fiefdomObj.getName()));

 // Then: No rebellion should occur
 assertTrue(fiefdomObj.isKingdom());
 assertTrue(fiefdomObj.getRebelliousVassals().isEmpty());
 }

 @Test
 public void testPerform_LoyalVassals_RebellionPossible() {
 // Given: Fiefdom with loyal vassals and conditions for rebellion
 ArrayList<String> loyalVassals = new ArrayList<>();
 loyalVassals.add("Vassal1");
 loyalVassals.add("Vassal2");
 fiefdomObj.setLoyalVassals(loyalVassals);
 fiefdomObj.setValue(3);

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(Collections.singletonList(fiefdomObj.getName()));

 // Then: Rebellion should occur and fiefdom should no longer be a kingdom
 assertFalse(fiefdomObj.isKingdom());
 assertTrue(fiefdomObj.getRebelliousVassals().size() > 0);
 }

 @Test
 public void testPerform_LoyalVassals_RebellionNotPossible() {
 // Given: Fiefdom with loyal vassals and conditions not suitable for rebellion
 ArrayList<String> loyalVassals = new ArrayList<>();
 loyalVassals.add("Vassal1");
 loyalVassals.add("Vassal2");
 fiefdomObj.setLoyalVassals(loyalVassals);
 fiefdomObj.setValue(3);
 fiefdomObj.setPersonalCastles(1);
 fiefdomObj.setFiefdomCastles(1);

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(Collections.singletonList(fiefdomObj.getName()));

 // Then: No rebellion should occur
 assertTrue(fiefdomObj.isKingdom());
 assertTrue(fiefdomObj.getRebelliousVassals().isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPerform_EmptyFiefdom() {
 // Given: Empty fiefdom

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(Collections.singletonList(fiefdomObj.getName()));

 // Then: An UnsupportedOperationException should be thrown since the fiefdom is empty
 }
}