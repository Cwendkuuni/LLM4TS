package src.test.java;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdoms fiefdomObj;
 private VassalRebellion vassalRebellion;

 @Before
 public void setUp() {
 fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, 5, new ArrayList<>());
 vassalRebellion = new VassalRebellion();
 }

 // Test for perform method with valid arguments
 @Test
 public void testPerformWithValidArguments() {
 vassalRebellion.perform(Arrays.asList(fiefdomObj.getName()));
 assertTrue(fiefdomObj.isRebellious());
 assertTrue(fiefdomObj.isKingdom());
 }

 // Test for perform method with empty collection
 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithEmptyCollection() {
 vassalRebbellion.perform(new ArrayList<>());
 }

 // Test for perform method with null argument
 @Test(expected = NullPointerException.class)
 public void testPerformWithNullArgument() {
 vassalRebbellion.perform(null);
 }

 // Test for perform method with non-existent fiefdom
 @Test(expected = NoSuchElementException.class)
 public void testPerformWithNonExistentFiefdom() {
 vassalRebbellion.perform(Arrays.asList("NonExistentFiefdom"));
 }

 // Test for perform method with loyal vassals and sufficient castles
 @Test
 public void testPerformWithLoyalVassalsAndSufficientCastles() {
 fiefdomObj.addLoyalVassal("TestVassal1");
 fiefdomObj.addLoyalVassal("TestVassal2");
 vassalRebellion.perform(Arrays.asList(fiefdomObj.getName()));
 int initialSize = fiefdomObj.getLoyalVassals().size();
 int personalCastles = fiefdomObj.getPersonalCastles();
 int fiefdomCastles = fiefdomObj.getFiefdomCastles();
 assertTrue(fiefdomObj.isRebellious());
 assertTrue(fiefdomObj.isKingdom());
 assertEquals(initialSize - personalCastles * 10 - fiefdomCastles * 25, fiefdomObj.getLoyalVassals().size());
 }

 // Test for perform method with loyal vassals and insufficient castles
 @Test(expected = IllegalStateException.class)
 public void testPerformWithLoyalVassalsAndInsufficientCastles() {
 fiefdomObj.addLoyalVassal("TestVassal1");
 fiefdomObj.addLoyalVassal("TestVassal2");
 fiefdomObj.setPersonalCastles(0);
 fiefdomObj.setFiefdomCastles(0);
 vassalRebellion.perform(Arrays.asList(fiefdomObj.getName()));
 }
}