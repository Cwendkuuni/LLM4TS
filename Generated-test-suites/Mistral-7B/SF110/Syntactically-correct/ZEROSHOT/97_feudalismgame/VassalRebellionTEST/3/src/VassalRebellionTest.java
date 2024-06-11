package src.test.java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import src.Actions;
import src.Fiefdom;
import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdom fiefdomObj;
 private Actions actionsObj;
 private Map<String, Fiefdom> map;

 @Before
 public void setUp() {
 map = new HashMap<>();
 actionsObj = new Actions();

 fiefdomObj = new Fiefdom("TestFiefdom", 10, 5, 5, new ArrayList<>(), true);
 map.put("TestFiefdom", fiefdomObj);
 }

 @Test
 public void testPerform_NoLoyalVassals() {
 VassalRebellion vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdomObj(this.fiefdomObj);
 vassalRebellion.perform(map.values());
 assertTrue(fiefdomObj.isKingdom());
 }

 @Test
 public void testPerform_LoyalVassals_NotRebellionConditionsMet() {
 fiefdomObj.setValue(15);
 fiefdomObj.getLoyalVassals().add(new Vassal("TestVassal1", 5));
 fiefdomObj.getLoyalVassals().add(new Vassal("TestVassal2", 3));

 VassalRebellion vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdomObj(this.fiefdomObj);
 vassalRebellion.perform(map.values());

 assertTrue(fiefdomObj.isRebelliousVassals());
 assertFalse(fiefdomObj.isKingdom());

 assertEquals(15, fiefdomObj.getLoyalVassals().size());
 assertEquals(12, fiefdomObj.getValue());
 }

 @Test
 public void testPerform_LoyalVassals_RebellionConditionsMet() {
 fiefdomObj.setValue(5);
 fiefdomObj.getLoyalVassals().add(new Vassal("TestVassal1", 5));
 fiefdomObj.getLoyalVassals().add(new Vassal("TestVassal2", 3));

 VassalRebellion vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdomObj(this.fiefdomObj);
 vassalRebellion.perform(map.values());

 assertTrue(fiefdomObj.isRebelliousVassals());
 assertFalse(fiefdomObj.isKingdom());

 assertEquals(5, fiefdomObj.getLoyalVassals().size());
 assertEquals(0, fiefdomObj.getValue());
 }

 @Test
 public void testPerform_Castles_NoRebellion() {
 fiefdomObj.setValue(5);
 fiefdomObj.getLoyalVassals().clear();
 fiefdomObj.setPersonalCastles(1);
 fiefdomObj.setFiefdomCastles(1);

 VassalRebellion vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdomObj(this.fiefdomObj);
 vassalRebellion.perform(map.values());

 assertTrue(fiefdomObj.isKingdom());
 }

 @Test
 public void testPerform_Castles_Rebellion() {
 fiefdomObj.setValue(5);
 fiefdomObj.getLoyalVassals().clear();
 fiefdomObj.setPersonalCastles(1);
 fiefdomObj.setFiefdomCastles(1);

 VassalRebellion vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdomObj(this.fiefdomObj);
 vassalRebellion.perform(map.values());

 assertFalse(fiefdomObj.isKingdom());
 }

 @Test
 public void testPerform_Castles_LoyalVassalsLessThanRebellionConditions() {
 fiefdomObj.setValue(5);
 fiefdomObj.getLoyalVassals().clear();
 fiefdomObj.setPersonalCastles(1);
 fiefdomObj.setFiefdomCastles(1);
 fiefdomObj.setRebelliousVassals(5, true);

 VassalRebellion vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdomObj(this.fiefdomObj);
 vassalRebellion.perform(map.values());

 assertEquals(0, fiefdomObj.getLoyalVassals().size());
 }

 @Test
 public void testPerform_Castles_LoyalVassalsMoreThanRebellionConditions() {
 fiefdomObj.setValue(5);
 fiefdomObj.getLoyalVassals().clear();
 fiefdomObj.setPersonalCastles(1);
 fiefdomObj.setFiefdomCastles(1);
 fiefdomObj.setRebelliousVassals(10, true);

 VassalRebellion vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdomObj(this.fiefdomObj);
 vassalRebellion.perform(map.values());

 assertEquals(5, fiefdomObj.getLoyalVassals().size());
 }
}