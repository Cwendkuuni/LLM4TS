package src.test.java;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import src.Fiefdoms;
import src.actions.Actions;
import src.events.RollEvent;

public class VassalRebellionTest {

 private VassalRebellion vassalRebellion;
 private Actions actions;
 private Fiefdoms fiefdomObj;

 @Before
 public void setUp() {
 actions = new Actions();
 fiefdomObj = new Fiefdoms("TestFiefdom");
 Map.map.put("TestFiefdom", fiefdomObj);
 vassalRebellion = new VassalRebllion(actions);
 }

 @Test
 public void testPerform_LoyalVassals_NoRebellion() {
 Collection<String> col = new ArrayList<>();
 col.add("TestFiefdom");
 vassalRebellion.perform(col);
 assertEquals(false, fiefdomObj.isRebellious());
 assertEquals(0, fiefdomObj.getRebelliousVassals().size());
 }

 @Test
 public void testPerform_LoyalVassals_Rebellion() {
 Collection<String> col = new ArrayList<>();
 col.add("TestFiefdom");

 // Set up fiefdom with loyal vassals and values
 fiefdomObj.setLoyalVassals(new ArrayList<>(Arrays.asList("Vassal1", "Vassal2", "Vassal3")));
 fiefdomObj.setValue(3);

 // Perform VassalRebellion action
 vassalRebellion.perform(col);

 // Assert rebellion state and number of rebellious vassals
 assertEquals(false, fiefdomObj.isKingdom());
 assertEquals(true, fiefdomObj.isRebellious());
 assertEquals(3, fiefdomObj.getRebelliousVassals().size());
 }

 @Test
 public void testPerform_NoLoyalVassals_RollEvent() {
 Collection<String> col = new ArrayList<>();
 col.add("TestFiefdom");

 // Set up fiefdom with no loyal vassals
 fiefdomObj.setLoyalVassals(new ArrayList<>());

 // Perform VassalRebellion action
 vassalRebellion.perform(col);

 // Assert that RollEvent was triggered
 assertTrue(fiefdomObj.getRollEvent() instanceof RollEvent);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPerform_NullCollection() {
 vassalRebellion.perform(null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPerform_EmptyCollection() {
 Collection<String> col = new ArrayList<>();
 vassalRebbellion.perform(col);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_InvalidFiefdom() {
 Collection<String> col = new ArrayList<>();
 col.add("InvalidFiefdom");
 vassalRebellion.perform(col);
 }
}