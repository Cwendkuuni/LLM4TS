package src.test.java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class VassalRebellionTest {

 private VassalRebellion vassalRebellion;
 private Fiefdoms fiefdomObj;

 @Before
 public void setup() {
  Map<String, Fiefdoms> map = new HashMap<>();
  fiefdomObj = new Fiefdoms ("TestFiefdom", 10, 5, new ArrayList<>(), new ArrayList<>(), true, true);
  map.put("TestFiefdom", fiefdomObj);
  vassalRebellion = new VassalRebellion();
  vassalRebellion.setMap(map);
 }

 // Test method: perform(Collection<String>)
 @Test
 public void testPerform() {
  Collection<String> col = new ArrayList<>();
  col.add("TestFiefdom");
  vassalRebellion.perform(col);
  assertTrue(fiefdomObj.isRebellious());
  assertTrue(fiefdomObj.isKingdom());
  // Test when loyal vassals are greater than value
  fiefdomObj.setValue(15);
  col.clear();
  col.add("TestFiefdom");
  vassalRebellion.perform(col);
  assertTrue(fiefdomObj.isRebellious());
  assertFalse(fiefdomObj.isKingdom());
  // Test when loyal vassals are less than value
  fiefdomObj.setValue(5);
  col.clear();
  col.add("TestFiefdom");
  vassalRebellion.perform(col);
  assertTrue(fiefdomObj.isRebellious());
  assertFalse(fiefdomObj.isKingdom());
  // Test when personal castles and fiefdom castles are greater than 0 and loyal vassals are sufficient
  fiefdomObj.setValue(15);
  fiefdomObj.setPersonalCastles(1);
  fiefdomObj.setFiefdomCastles(1);
  col.clear();
  col.add("TestFiefdom");
  vassalRebellion.perform(col);
  assertTrue(fiefdomObj.isRebellious());
  assertFalse(fiefdomObj.isKingdom());
  // Test when personal castles and fiefdom castles are greater than 0 and loyal vassals are insufficient
  fiefdomObj.setPersonalCastles(2);
  fiefdomObj.setFiefdomCastles(2);
  col.clear();
  col.add("TestFiefdom");
  vassalRebellion.perform(col);
  assertFalse(fiefdomObj.isRebellious());
  assertTrue(fiefdomObj.isKingdom());
  // Test when no loyal vassals
  fiefdomObj.getLoyalVassals().clear();
  col.clear();
  col.add("TestFiefdom");
  vassalRebellion.perform(col);
  new RollEvent(fiefdomObj.getName());
  assertFalse(fiefdomObj.isRebellious());
  assertTrue(fiefdomObj.isKingdom());
 }

 // Test method: getter and setter for fiefdomObj
 @Test
 public void testGetAndSetFiefdomObj() {
  assertNull(vassalRebellion.getFiefdomObj());
  vassalRebellion.setFiefdomObj(fiefdomObj);
  assertEquals(fiefdomObj, vassalRebellion.getFiefdomObj());
 }

}