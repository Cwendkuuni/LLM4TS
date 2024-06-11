import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.Actions;
import src.Fiefdom;
import src.Fiefdoms;
import src.VassalRebellion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VassalRebellionTest {

 private VassalRebellion vassalRebellion;
 private Fiefdom fiefdom;
 private Fiefdoms fiefdoms;
 private Actions actions;

 @Before
 public void setUp() {
 fiefdoms = new Fiefdoms();
 actions = Mockito.spy(new Actions());
 fiefdom = new Fiefdom("TestFiefdom", 10, 5, 5, new ArrayList<>(), new ArrayList<>(), true, true);
 fiefdoms.addFiefdom(fiefdom.getName(), fiefdom);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.setActions(actions);
 vassalRebbellion.setFiefdoms(fiefdoms);
 }

 // Test method: perform(Collection<String>)
 @Test
 public void testPerformWithLoyalVassals() {
 // Test case with sufficient vassals and castles
 List<String> input = new ArrayList<>();
 input.add("TestFiefdom");
 Map<String, Fiefdom> map = fiefdoms.getMap();
 Fiefdom testFiefdom = map.get("TestFiefdom");
 testFiefdom.setValue(15);
 testFiefdom.setPersonalCastles(2);
 testFiefdom.setFiefdomCastles(3);
 vassalRebellion.perform(input);
 assertEquals(15 + 2 * 10 + 3 * 25, testFiefdom.getLoyalVassals().size());
 // Test case with insufficient vassals but sufficient castles
 input = new ArrayList<>();
 input.add("TestFiefdom");
 testFiefdom.setValue(5);
 testFiefdom.setPersonalCastles(2);
 testFiefdom.setFiefdomCastles(3);
 vassalRebellion.perform(input);
 assertEquals(testFiefdom.getValue(), testFiefdom.getLoyalVassals().size());
 // Test case with insufficient vassals and insufficient castles
 input = new ArrayList<>();
 input.add("TestFiefdom");
 testFiefdom.setValue(0);
 testFiefdom.setPersonalCastles(0);
 testFiefdom.setFiefdomCastles(0);
 vassalRebellion.perform(input);
 new RollEvent(testFiefdom.getName());
 // Test case with empty fiefdom
 input = new ArrayList<>();
 input.add("TestFiefdom");
 testFiefdom.setValue(0);
 testFiefdom.setPersonalCastles(0);
 testFiefdom.setFiefdomCastles(0);
 testFiefdom.getLoyalVassals().clear();
 vassalRebellion.perform(input);
 assertTrue(actions.getEventCount() > 0);
 }

 // Test method: perform(Collection<String>) with null input
 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithNullInput() {
 vassalRebbellion.perform(null);
 }

 // Test method: perform(Collection<String>) with empty input
 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithEmptyInput() {
 vassalRebbellion.perform(Collections.emptyList());
 }

 // Test method: perform(Collection<String>) with non-existent fiefdom
 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithNonExistentFiefdom() {
 List<String> input = new ArrayList<>();
 input.add("NonExistentFiefdom");
 vassalRebbellion.perform(input);
 }
}