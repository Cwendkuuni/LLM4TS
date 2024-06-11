package src.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import src.Fiefdoms;
import src.VassalRebellion;
import src.actions.Actions;
import src.events.RollEvent;
import src.map.Map;

public class VassalRebellionTest {

 private Fiefdoms fiefdomObj;
 private VassalRebellion vassalRebellion;
 private Map map;
 private List loyalVassals;

 @Before
 public void setup() {
 Map map = new Map();
 this.map = map;
 this.fiefdomObj = new Fiefdoms("TestFiefdom", 100, 50, new ArrayList<>(), new ArrayList<>());
 this.map.put("TestFiefdom", this.fiefdomObj);
 this.vassalRebellion = new VassalRebellion();
 }

 @Test
 public void testPerform_LoyalVassalsPresent_ValueGreaterThanZero() {
 // Test Case 1: Basic functional test for perform method with loyal vassals and positive value
 this.vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 assertTrue(this.fiefdomObj.isRebellious());
 assertTrue(this.fiefdomObj.isKingdom());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_LoyalVassalsPresent_ValueLessThanZero() {
 // Test Case 2: Test for IllegalArgumentException when perform method is called with negative value
 this.fiefdomObj.setValue(-1);
 this.vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 }

 @Test
 public void testPerform_LoyalVassalsAbsent() {
 // Test Case 3: Test for no rebellion when there are no loyal vassals
 this.fiefdomObj.setLoyalVassals(new ArrayList<>());
 this.vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 assertFalse(this.fiefdomObj.isRebellious());
 }

 @Test
 public void testPerform_CastlesPresent_LoyalVassalsEnough() {
 // Test Case 4: Test for rebellion when castles are present and loyal vassals are enough
 this.fiefdomObj.setPersonalCastles(1);
 this.fiefdomObj.setFiefdomCastles(1);
 this.vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 assertTrue(this.fiefdomObj.isRebellious());
 assertFalse(this.fiefdomObj.isKingdom());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_CastlesPresent_LoyalVassalsNotEnough() {
 // Test Case 5: Test for IllegalArgumentException when loyal vassals are not enough
 this.fiefdomObj.setPersonalCastles(1);
 this.fiefdomObj.setFiefdomCastles(1);
 this.fiefdomObj.setLoyalVassals(Collections.singletonList("TestVassal"));
 this.vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 }

 @Test
 public void testPerform_NoCastles() {
 // Test Case 6: Test for no rebellion when there are no castles
 this.fiefdomObj.setPersonalCastles(0);
 this.fiefdomObj.setFiefdomCastles(0);
 this.vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 assertFalse(this.fiefdomObj.isRebellious());
 }

 @Test
 public void testPerform_RollEventThrown_LoyalVassalsAbsent() {
 // Test Case 7: Test for RollEvent being thrown when there are no loyal vassals
 this.fiefdomObj.setLoyalVassals(new ArrayList<>());
 this.vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 assertEquals(1, this.map.get("TestFiefdom").getEventQueue().size());
 assertTrue(this.map.get("TestFiefdom").getEventQueue().get(0) instanceof RollEvent);
 }
}