package src.test.java;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class VassalRebellionTest {

 private Fiefdom fiefdom;
 private Actions actions;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 Map<String, Fiefdom> map = new HashMap<>();
 fiefdom = new Fiefdom("TestFiefdom", 10, 5, 5, new ArrayList<>(), true, true);
 map.put(fiefdom.getName(), fiefdom);
 actions = new Actions();
 vassalRebellion = new VassalRebellion() {
 @Override
 public void perform(Collection<String> col) {
 super.perform(col);
 }
 };
 vassalRebellion.setFiefdomObj(fiefdom);
 }

 @Test
 public void testPerform_LoyalVassals_NoRebellion() {
 List<String> col = new ArrayList<>();
 col.add(fiefdom.getName());
 vassalRebellion.perform(col);
 assertTrue(fiefdom.isKingdom());
 assertEquals(0, fiefdom.getRebelliousVassals().size());
 }

 @Test
 public void testPerform_LoyalVassals_Rebellion() {
 List<String> col = new ArrayList<>();
 col.add(fiefdom.getName());
 fiefdom.setLoyalVassals(new ArrayList<Vassal>(Arrays.asList(new Vassal("TestVassal1"), new Vassal("TestVassal2"))));
 vassalRebellion.perform(col);
 assertFalse(fiefdom.isKingdom());
 assertEquals(2, fiefdom.getRebelliousVassals().size());
 }

 @Test
 public void testPerform_LoyalVassals_RebellionWithCastles() {
 List<String> col = new ArrayList<>();
 col.add(fiefdom.getName());
 fiefdom.setLoyalVassals(new ArrayList<Vassal>(Arrays.asList(new Vassal("TestVassal1"), new Vassal("TestVassal2"))));
 fiefdom.setPersonalCastles(1);
 fiefdom.setFiefdomCastles(2);
 vassalRebellion.perform(col);
 assertFalse(fiefdom.isKingdom());
 assertEquals(2 + (1 * 10 + 2 * 25), fiefdom.getRebelliousVassals().size());
 }

 @Test(expected = RuntimeException.class)
 public void testPerform_NoFiefdom() {
 List<String> col = new ArrayList<>();
 col.add("NonExistentFiefdom");
 vassalRebellion.perform(col);
 }
}