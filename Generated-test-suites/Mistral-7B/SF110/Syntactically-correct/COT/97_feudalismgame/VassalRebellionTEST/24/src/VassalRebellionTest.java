package src.test.java;

import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class VassalRebellionTest {

 private VassalRebellion vassalRebellion;
 private Fiefdoms fiefdom;

 @Before
 public void setup() {
 fiefdom = new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>(), true, true);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdomObj(fiefdom);
 }

 // Test method: perform(Collection<String>)
 @Test
 public void testPerform() {
 Collection<String> collection = new ArrayList<>();
 collection.add("TestFiefdom");
 vassalRebellion.perform(collection);

 // Basic test case for perform method
 // Check if the fiefdom's loyalty status has been changed to rebellious
 assertTrue(fiefdom.isRebellious());
 }

 // Test edge cases and exceptions

 // Test case: fiefdom does not exist in the map
 @Test(expected = NullPointerException.class)
 public void testPerformWithNonExistentFiefdom() {
 Collection<String> collection = new ArrayList<>();
 collection.add("NonExistentFiefdom");
 vassalRebbellion.perform(collection);
 }

 // Test case: fiefdom has no loyal vassals
 @Test
 public void testPerformWithNoLoyalVassals() {
 fiefdom.setLoyalVassals(new ArrayList<>());
 vassalRebellion.perform(new ArrayList<>());

 // Check if no event was rolled
 assertFalse(fiefdom.hasNewEvent());
 }

 // Test case: personal castles and fiefdom castles are zero
 @Test
 public void testPerformWithZeroCastles() {
 vassalRebbellion.setFiefdomObj(new Fiefdoms("ZeroCastlesFiefdom", 0, 0, new ArrayList<>(), true, true));
 vassalRebbellion.perform(new ArrayList<>());

 // Check if no vassals have rebelled
 assertEquals(0, fiefdom.getRebelliousVassals().size());
 }

 // Test case: personal castles and fiefdom castles are negative
 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithNegativeCastles() {
 vassalRebbellion.setFiefdomObj(new Fiefdoms("NegativeCastlesFiefdom", -5, 3, new ArrayList<>(), true, true));
 vassalRebbellion.perform(new ArrayList<>());
 }

 // Test case: personal castles and fiefdom castles exceed the limit
 @Test
 public void testPerformWithTooManyCastles() {
 vassalRebbellion.setFiefdomObj(new Fiefdoms("TooManyCastlesFiefdom", 10, 51, new ArrayList<>(), true, true));
 vassalRebbellion.perform(new ArrayList<>());

 // Check if the number of rebellious vassals is equal to the limit
 assertEquals(50, fiefdom.getRebelliousVassals().size());
 }

 // Test case: personal castles and fiefdom castles are zero, but there are loyal vassals
 @Test
 public void testPerformWithZeroCastlesAndLoyalVassals() {
 fiefdom.setPersonalCastles(0);
 fiefdom.setFiefdomCastles(0);
 vassalRebellion.perform(new ArrayList<>());

 // Check if no vassals have rebelled
 assertEquals(0, fiefdom.getRebelliousVassals().size());
 }

 // Test case: personal castles and fiefdom castles are zero, but there are no loyal vassals
 @Test
 public void testPerformWithZeroCastlesAndNoLoyalVassals() {
 fiefdom.setPersonalCastles(0);
 fiefdom.setFiefdomCastles(0);
 fiefdom.setLoyalVassals(new ArrayList<>());
 vassalRebellion.perform(new ArrayList<>());

 // Check if no event was rolled
 assertFalse(fiefdom.hasNewEvent());
 }
}