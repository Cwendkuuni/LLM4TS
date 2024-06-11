package src.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import src.Fiefdoms;
import src.Actions;
import src.VassalRebellion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VassalRebellionTest {

 private Fiefdoms map;
 private VassalRebellion vassalRebellion;

 @Before
 public void setUp() {
 map = new Fiefdoms();
 vassalRebellion = new VassalRebellion();

 // Initialize the map with some test data
 map.put("FiefdomA", new Fiefdoms("FiefdomA", 5, 2, new ArrayList<>()));
 map.put("FiefdomB", new Fiefdoms("FiefdomB", 3, 1, new ArrayList<>()));
 map.put("FiefdomC", new Fiefdoms("FiefdomC", 0, 0, new ArrayList<>()));
 }

 @Test
 public void testPerformWithLoyalVassalsAndCastles() {
 // Test case for performing rebellion with loyal vassals and castles
 ArrayList<String> collection = new ArrayList<>();
 collection.add("FiefdomA");

 vassalRebellion.perform(collection);

 Fiefdoms fiefdomA = map.get("FiefdomA");

 // Check if the vassals have rebelled and the kingdom status is set to false
 assertTrue(fiefdomA.isRebellious());
 assertFalse(fiefdomA.isKingdom());

 // Check if the number of rebellious vassals is correct
 int expectedRebelliousVassals = 5 - 2; // 3 loyal vassals, value is 1
 int actualRebelliousVassals = fiefdomA.getRebelliousVassals().size();
 assertEquals(expectedRebelliousVassals, actualRebelliousVassals);

 // Check if personal castles and fiefdom castles have been deducted from the number of rebellious vassals
 int personalCastles = fiefdomA.getPersonalCastles();
 int fiefdomCastles = fiefdomA.getFiefdomCastles();
 int expectedDeductedVassals = expectedRebelliousVassals + (personalCastles * 10 + fiefdomCastles * 25);
 int actualDeductedVassals = fiefdomA.getLoyalVassals().size();
 assertEquals(expectedDeductedVassals, actualDeductedVassals);
 }

 @Test
 public void testPerformWithoutLoyalVassals() {
 // Test case for performing rebellion without loyal vassals
 ArrayList<String> collection = new ArrayList<>();
 collection.add("FiefdomC");

 vassalRebellion.perform(collection);

 Fiefdoms fiefdomC = map.get("FiefdomC");

 // Check if a new roll event has been triggered since there were no loyal vassals
 assertTrue(fiefdomC.hasRollEventBeenTriggered());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithNullCollection() {
 // Test case for performing rebellion with a null collection
 vassalRebellion.perform(null);
 }
}