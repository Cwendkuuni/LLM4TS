package src.test.java;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VassalRebellionTest {

 private VassalRebellion vassalRebellion;
 private Fiefdoms fiefdom;

 @Before
 public void setUp() {
  fiefdom = new Fiefdoms("TestFiefdom");
  vassalRebellion = new VassalRebellion();
  Map<String, Fiefdoms> map = new HashMap<>();
  map.put("TestFiefdom", fiefdom);
  VassalRebellion.setMap(map);
 }

 // Test for perform method with valid arguments
 @Test
 public void testPerformWithValidArguments() {
  ArrayList<String> collection = new ArrayList<>();
  collection.add("TestFiefdom");
  vassalRebellion.perform(collection);
  assertEquals(false, fiefdom.isKingdom());
  assertEquals(true, fiefdom.isRebelliousVassals());
 }

 // Test for perform method with empty collection
 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithEmptyCollection() {
  vassalRebellion.perform(Collections.emptyList());
 }

 // Test for perform method with null collection
 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithNullCollection() {
  vassalRebbellion.perform(null);
 }

 // Test for perform method with non-existent fiefdom name
 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithNonExistentFiefdom() {
  ArrayList<String> collection = new ArrayList<>();
  collection.add("NonExistentFiefdom");
  vassalRebellion.perform(collection);
 }

 // Test for perform method with no loyal vassals
 @Test
 public void testPerformWithNoLoyalVassals() {
  fiefdom.setLoyalVassals(new ArrayList<String>());
  ArrayList<String> collection = new ArrayList<>();
  collection.add("TestFiefdom");
  vassalRebellion.perform(collection);
  assertFalse(fiefdom.isRebelliousVassals());
 }

 // Test for perform method with less loyal vassals than required to rebel
 @Test
 public void testPerformWithLessLoyalVassalsThanRequired() {
  fiefdom.setLoyalVassals(new ArrayList<String>());
  fiefdom.setLoyalVassals(Collections.nCopies(5, "Vassal1"));
  ArrayList<String> collection = new ArrayList<>();
  collection.add("TestFiefdom");
  vassalRebellion.perform(collection);
  assertFalse(fiefdom.isRebelliousVassals());
 }

 // Test for perform method with enough personal castles and fiefdom castles and more loyal vassals than required to rebel
 @Test
 public void testPerformWithEnoughCastlesAndLoyalVassals() {
  fiefdom.setLoyalVassals(new ArrayList<String>());
  fiefdom.setLoyalVassals(Collections.nCopies(15, "Vassal1"));
  fiefdom.setCastles(1, 2);
  ArrayList<String> collection = new ArrayList<>();
  collection.add("TestFiefdom");
  vassalRebellion.perform(collection);
  assertTrue(fiefdom.isRebelliousVassals());
 }

 // Test for perform method with enough personal castles but less fiefdom castles and more loyal vassals than required to rebel
 @Test
 public void testPerformWithEnoughPersonalCastlesButLessFiefdomCastles() {
  fiefdom.setLoyalVassals(new ArrayList<String>());
  fiefdom.setLoyalVassals(Collections.nCopies(15, "Vassal1"));
  fiefdom.setCastles(1, 0);
  ArrayList<String> collection = new ArrayList<>();
  collection.add("TestFiefdom");
  vassalRebellion.perform(collection);
  assertFalse(fiefdom.isRebelliousVassals());
 }

 // Test for perform method with enough fiefdom castles but less personal castles and more loyal vassals than required to rebel
 @Test
 public void testPerformWithEnoughFiefdomCastlesButLessPersonalCastles() {
  fiefdom.setLoyalVassals(new ArrayList<String>());
  fiefdom.setLoyalVassals(Collections.nCopies(15, "Vassal1"));
  fiefdom.setCastles(0, 2);
  ArrayList<String> collection = new ArrayList<>();
  collection.add("TestFiefdom");
  vassalRebellion.perform(collection);
  assertFalse(fiefdom.isRebelliousVassals());
 }

 // Test for perform method with enough personal castles and fiefdom castles and equal number of loyal vassals required to rebel
 @Test
 public void testPerformWithEnoughPersonalCastlesFiefdomCastlesAndEqualLoyalVassals() {
  fiefdom.setLoyalVassals(new ArrayList<String>());
  fiefdom.setLoyalVassals(Collections.nCopies(10, "Vassal1"));
  fiefdom.setCastles(1, 2);
  ArrayList<String> collection = new ArrayList<>();
  collection.add("TestFiefdom");
  vassalRebellion.perform(collection);
  assertTrue(fiefdom.isRebelliousVassals());
 }

 // Test for perform method with more personal castles than required and enough fiefdom castles and loyal vassals
 @Test
 public void testPerformWithMorePersonalCastlesThanRequiredAndEnoughFiefdomCastlesAndLoyalVassals() {
  fiefdom.setLoyalVassals(new ArrayList<String>());
  fiefdom.setLoyalVassals(Collections.nCopies(10, "Vassal1"));
  fiefdom.setCastles(2, 2);
  ArrayList<String> collection = new ArrayList<>();
  collection.add("TestFiefdom");
  vassalRebellion.perform(collection);
  assertTrue(fiefdom.isRebelliousVassals());
 }

 // Test for perform method with more fiefdom castles than required and enough personal castles and loyal vassals
 @Test
 public void testPerformWithMoreFiefdomCastlesThanRequiredAndEnoughPersonalCastlesAndLoyalVassals() {
  fiefdom.setLoyalVassals(new ArrayList<String>());
  fiefdom.setLoyalVassals(Collections.nCopies(10, "Vassal1"));
  fiefdom.setCastles(1, 3);
  ArrayList<String> collection = new ArrayList<>();
  collection.add("TestFiefdom");
  vassalRebellion.perform(collection);
  assertTrue(fiefdom.isRebelliousVassals());
 }

}