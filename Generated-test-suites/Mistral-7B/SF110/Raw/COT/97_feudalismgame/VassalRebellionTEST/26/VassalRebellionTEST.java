package src.test;

import org.junit.Before;
import org.junit.Test;

import src.Fiefdoms;
import src.VassalRebellion;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VassalRebellionTest {

 private Fiefdoms fiefdomObj;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 Map<String, Fiefdoms> map = new HashMap<>();
 fiefdomObj = new Fiefdoms("TestFiefdom", 5, 3, new ArrayList<>(), new ArrayList<>(), true);
 map.put("TestFiefdom", fiefdomObj);
 this.fiefdomObj = fiefdomObj;
 this.vassalRebellion = new VassalRebellion();
 this.vassalRebellion.setFiefdoms(map);
 }

 @Test
 public void testPerformWithLoyalVassalsAndEnoughCastles() {
 // Test case for performing rebellion with loyal vassals and enough castles
 ArrayList<String> collection = new ArrayList<>();
 collection.add("TestFiefdom");
 vassalRebellion.perform(collection);
 assertEquals(false, fiefdomObj.isKingdom());
 assertTrue(fiefdomObj.isRebellious());
 }

 @Test
 public void testPerformWithLoyalVassalsAndNotEnoughCastles() {
 // Test case for performing rebellion with loyal vassals but not enough castles
 int expectedRebelliousVassals = fiefdomObj.getLoyalVassals().size();
 vassalRebellion.perform(fiefdomObj.getLoyalVassals());
 assertEquals(expectedRebelliousVassals, fiefdomObj.getRebelliousVassals().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithEmptyCollection() {
 // Test case for performing rebellion with an empty collection
 ArrayList<String> emptyCollection = new ArrayList<>();
 vassalRebellion.perform(emptyCollection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithNonExistentFiefdom() {
 // Test case for performing rebellion with a non-existent fiefdom
 ArrayList<String> nonExistentCollection = new ArrayList<>();
 nonExistentCollection.add("NonExistentFiefdom");
 vassalRebellion.perform(nonExistentCollection);
 }

 @Test
 public void testPerformWithNoLoyalVassals() {
 // Test case for performing rebellion with no loyal vassals
 ArrayList<String> noLoyalVassalsCollection = new ArrayList<>();
 vassalRebellion.perform(noLoyalVassalsCollection);
 assertTrue(fiefdomObj.rollEventCalled());
 }
}