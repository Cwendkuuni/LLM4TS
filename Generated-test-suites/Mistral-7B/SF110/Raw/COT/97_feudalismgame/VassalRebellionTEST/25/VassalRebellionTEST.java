package src.test.java;

import static org.junit.Assert.*;

import org.junit.Test;

import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdoms fiefdomObj;
 private VassalRebellion vassalRebellion;

 @Test
 public void testPerform_ValidFiefdom_LoyalVassals_PersonalAndFiefdomCastles() {
  fiefdomObj = new Fiefdoms("TestFiefdom");
  fiefdomObj.setValue(10);
  fiefdomObj.setPersonalCastles(1);
  fiefdomObj.setFiefdomCastles(2);
  fiefdomObj.setLoyalVassals(Arrays.asList(new Vassal("TestVassal1"), new Vassal("TestVassal2")));
  vassalRebellion = new VassalRebellion();
  vassalRebellion.perform(Arrays.asList(fiefdomObj.getName()));
  assertTrue(fiefdomObj.isRebellious());
  assertEquals(13, fiefdomObj.getRebelliousVassals().size());
 }

 @Test(expected = RuntimeException.class)
 public void testPerform_InvalidFiefdom_NoLoyalVassals() {
  fiefdomObj = new Fiefdoms("TestFiefdom");
  vassalRebellion = new VassalRebellion();
  vassalRebellion.perform(Arrays.asList(fiefdomObj.getName()));
 }

 @Test
 public void testPerform_ValidFiefdom_NoLoyalVassals() {
  fiefdomObj = new Fiefdoms("TestFiefdom");
  vassalRebellion = new VassalRebellion();
  vassalRebellion.perform(Arrays.asList(fiefdomObj.getName()));
  assertFalse(fiefdomObj.isRebellious());
 }

 @Test
 public void testPerform_ValidFiefdom_NoPersonalNorFiefdomCastles() {
  fiefdomObj = new Fiefdoms("TestFiefdom");
  fiefdomObj.setValue(10);
  fiefdomObj.setPersonalCastles(0);
  fiefdomObj.setFiefdomCastles(0);
  fiefdomObj.setLoyalVassals(Arrays.asList(new Vassal("TestVassal1"), new Vassal("TestVassal2")));
  vassalRebellion = new VassalRebellion();
  vassalRebellion.perform(Arrays.asList(fiefdomObj.getName()));
  assertTrue(fiefdomObj.isRebellious());
  assertEquals(10, fiefdomObj.getRebelliousVassals().size());
 }

 @Test
 public void testPerform_ValidFiefdom_LoyalVassals_NoPersonalNorFiefdomCastles() {
  fiefdomObj = new Fiefdoms("TestFiefdom");
  fiefdomObj.setValue(10);
  fiefdomObj.setPersonalCastles(0);
  fiefdomObj.setFiefdomCastles(0);
  fiefdomObj.setLoyalVassals(Arrays.asList(new Vassal("TestVassal1"), new Vassal("TestVassal2")));
  vassalRebellion = new VassalRebellion();
  vassalRebellion.perform(Arrays.asList(fiefdomObj.getName()));
  assertFalse(fiefdomObj.isRebellious());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_InvalidCollection() {
  vassalRebellion = new VassalRebellion();
  vassalRebellion.perform(null);
 }

 @Test
 public void testPerform_EmptyCollection() {
  vassalRebellion = new VassalRebellion();
  vassalRebellion.perform(new ArrayList<>());
 }

 @Test
 public void testPerform_MultipleFiefdoms() {
  fiefdomObj1 = new Fiefdoms("TestFiefdom1");
  fiefdomObj2 = new Fiefdoms("TestFiefdom2");
  fiefdomObj1.setValue(10);
  fiefdomObj1.setPersonalCastles(1);
  fiefdomObj1.setFiefdomCastles(2);
  fiefdomObj1.setLoyalVassals(Arrays.asList(new Vassal("TestVassal1"), new Vassal("TestVassal2")));
  fiefdomObj2.setValue(15);
  fiefdomObj2.setPersonalCastles(2);
  fiefdomObj2.setFiefdomCastles(3);
  fiefdomObj2.setLoyalVassals(Arrays.asList(new Vassal("TestVassal3"), new Vassal("TestVassal4"), new Vassal("TestVassal5")));
  List<Fiefdoms> list = Arrays.asList(fiefdomObj1, fiefdomObj2);
  vassalRebellion = new VassalRebellion();
  vassalRebellion.perform(list);
  assertTrue(fiefdomObj1.isRebellious());
  assertTrue(fiefdomObj2.isRebellious());
  assertEquals(25, fiefdomObj1.getRebelliousVassals().size());
  assertEquals(24, fiefdomObj2.getRebelliousVassals().size());
 }
}