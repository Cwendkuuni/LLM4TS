package src.test.java;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VassalRebellionTest {

 private VassalRebellion vassalRebellion;
 private Fiefdoms fiefdom;

 @Before
 public void setup() {
 fiefdom = new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>(), new ArrayList<>(), true, true);
 Map<String, Fiefdoms> map = new HashMap<>();
 map.put(fiefdom.getName(), fiefdom);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.setMap(map);
 }

 @Test
 public void testPerform_LoyalVassalsPresent_ValueGreaterThanZero() {
 // Test basic functionality
 vassalRebellion.perform(Collections.singletonList(fiefdom.getName()));
 // Test edge case: vassals greater than value
 Map<String, Fiefdoms> map = new HashMap<>();
 map.put(fiefdom.getName(), new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>(), new ArrayList<>(), true, true));
 vassalRebbellion.setMap(map);
 fiefdom.setValue(11);
 vassalRebellion.perform(Collections.singletonList(fiefdom.getName()));
 assertFalse(fiefdom.isKingdom());
 // Test edge case: vassals equal to value
 Map<String, Fiefdoms> map1 = new HashMap<>();
 map1.put(fiefdom.getName(), new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>(), new ArrayList<>(), true, true));
 vassalRebbellion.setMap(map1);
 vassalRebbellion.perform(Collections.singletonList(fiefdom.getName()));
 assertFalse(fiefdom.isKingdom());
 // Test exception handling: null fiefdom
 vassalRebbellion.setMap(null);
 vassalRebbellion.perform(Collections.singletonList(null));
 // Test exception handling: empty collection
 vassalRebbellion.setMap(map);
 vassalRebbellion.perform(Collections.emptyList());
 }

 @Test(expected = NullPointerException.class)
 public void testPerform_NullFiefdom() {
 // Test exception handling: null fiefdom in collection
 vassalRebbellion.setMap(map);
 vassalRebbellion.perform(Collections.singletonList(null));
 }

 @Test
 public void testPerform_LoyalVassalsAbsent() {
 vassalRebbellion.perform(Collections.singletonList("TestFiefdomAbsent"));
 assertFalse(Map.map.get("TestFiefdomAbsent").isKingdom());
 new RollEvent(Map.map.get("TestFiefdomAbsent").getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_InvalidFiefdomName() {
 vassalRebbellion.perform(Collections.singletonList("TestInvalidFiefdomName"));
 }

 @Test
 public void testPerform_NoVassals() {
 // Test basic functionality
 vassalRebbellion.perform(Collections.singletonList(fiefdom.getName()));
 fiefdom.setLoyalVassals(Collections.emptyList());
 vassalRebbellion.perform(Collections.singletonList(fiefdom.getName()));
 assertFalse(fiefdom.isKingdom());
 // Test edge case: no loyal vassals and personal castles or fiefdom castles present
 Map<String, Fiefdoms> map = new HashMap<>();
 map.put(fiefdom.getName(), new Fiefdoms("TestFiefdomNoVassals", 1, 1, new ArrayList<>(), new ArrayList<>(), true, true));
 vassalRebbellion.setMap(map);
 vassalRebbellion.perform(Collections.singletonList(fiefdom.getName()));
 assertFalse(fiefdom.isKingdom());
 }

 @Test
 public void testPerform_PersonalCastlesPresent() {
 vassalRebbellion.perform(Collections.singletonList(fiefdom.getName()));
 fiefdom.setPersonalCastles(2);
 vassalRebbellion.perform(Collections.singletonList(fiefdom.getName()));
 assertFalse(fiefdom.isKingdom());
 // Test edge case: personal castles greater than loyal vassals
 Map<String, Fiefdoms> map = new HashMap<>();
 map.put(fiefdom.getName(), new Fiefdoms("TestFiefdomPersonalCastlesGreater", 1, 3, new ArrayList<>(), new ArrayList<>(), true, true));
 vassalRebbellion.setMap(map);
 vassalRebbellion.perform(Collections.singletonList(fiefdom.getName()));
 assertFalse(fiefdom.isKingdom());
 }

 @Test
 public void testPerform_FiefdomCastlesPresent() {
 vassalRebbellion.perform(Collections.singletonList(fiefdom.getName()));
 fiefdom.setFiefdomCastles(2);
 vassalRebbellion.perform(Collections.singletonList(fiefdom.getName()));
 assertFalse(fiefdom.isKingdom());
 // Test edge case: fiefdom castles greater than loyal vassals
 Map<String, Fiefdoms> map = new HashMap<>();
 map.put(fiefdom.getName(), new Fiefdoms("TestFiefdomFiefdomCastlesGreater", 1, 1, new ArrayList<>(), new ArrayList<>(), true, true));
 vassalRebbellion.setMap(map);
 vassalRebbellion.perform(Collections.singletonList(fiefdom.getName()));
 assertFalse(fiefdom.isKingdom());
 }

 @Test
 public void testPerform_PersonalCastlesAndFiefdomCastlesPresent() {
 vassalRebbellion.perform(Collections.singletonList(fiefdom.getName()));
 fiefdom.setPersonalCastles(1);
 fiefdom.setFiefdomCastles(1);
 vassalRebbellion.perform(Collections.singletonList(fiefdom.getName()));
 assertFalse(fiefdom.isKingdom());
 // Test edge case: personal castles and fiefdom castles greater than loyal vassals
 Map<String, Fiefdoms> map = new HashMap<>();
 map.put(fiefdom.getName(), new Fiefdoms("TestFiefdomPersonalAndFiefdomCastlesGreater", 1, 1, new ArrayList<>(), new ArrayList<>(), true, true));
 vassalRebbellion.setMap(map);
 vassalRebbellion.perform(Collections.singletonList(fiefdom.getName()));
 assertFalse(fiefdom.isKingdom());
 }

 @Test
 public void testPerform_ValueLessThanZero() {
 vassalRebbellion.perform(Collections.singletonList(fiefdom.getName()));
 fiefdom.setValue(-1);
 vassalRebbellion.perform(Collections.singletonList(fiefdom.getName()));
 assertFalse(fiefdom.isKingdom());
 }
}