package src.test.java;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdoms fiefdomObj;
 private VassalRebellion vassalRebellion;

 @Before
 public void setUp() {
 fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, 5, new ArrayList<>(), new ArrayList<>(), false, false);
 vassalRebellion = new VassalRebellion();
 }

 // Test for perform method with valid arguments
 @Test
 public void testPerformValidArguments() {
 Collection<String> col = new ArrayList<>();
 col.add("TestFiefdom");
 vassalRebellion.perform(col);
 assertFalse(fiefdomObj.isKingdom());
 assertTrue(fiefdomObj.isRebellious());
 }

 // Test for perform method with empty collection
 @Test(expected = IllegalArgumentException.class)
 public void testPerformEmptyCollection() {
 vassalRebellion.perform(new ArrayList<>());
 }

 // Test for perform method with null collection
 @Test(expected = IllegalArgumentException.class)
 public void testPerformNullCollection() {
 vassalRebbellion.perform(null);
 }

 // Test for perform method with non-existent fiefdom
 @Test(expected = IllegalArgumentException.class)
 public void testPerformNonExistentFiefdom() {
 Collection<String> col = new ArrayList<>();
 col.add("NonExistentFiefdom");
 vassalRebellion.perform(col);
 }

 // Test for perform method with fiefdom having no loyal vassals
 @Test
 public void testPerformNoLoyalVassals() {
 Collection<String> col = new ArrayList<>();
 col.add("TestFiefdom");
 fiefdomObj.setLoyalVassals(new ArrayList<>());
 vassalRebellion.perform(col);
 assertFalse(new RollEvent("TestFiefdom").isRolled());
 }

 // Test for perform method with fiefdom having no value
 @Test
 public void testPerformNoValue() {
 Collection<String> col = new ArrayList<>();
 col.add("TestFiefdom");
 fiefdomObj.setValue(0);
 vassalRebellion.perform(col);
 assertFalse(new RollEvent("TestFiefdom").isRolled());
 }

 // Test for perform method with personal castles and fiefdom castles greater than vassals
 @Test
 public void testPerformMoreVassalsThanCastles() {
 Collection<String> col = new ArrayList<>();
 col.add("TestFiefdom");
 int personalCastles = 5;
 int fiefdomCastles = 2;
 fiefdomObj.setPersonalCastles(personalCastles);
 fiefdomObj.setFiefdomCastles(fiefdomCastles);
 fiefdomObj.setLoyalVassals(new ArrayList<>());
 for (int i = 0; i < personalCastles * 10 + fiefdomCastles * 25 + 1; i++) {
 fiefdomObj.addLoyalVassal();
 }
 vassalRebellion.perform(col);
 assertFalse(new RollEvent("TestFiefdom").isRolled());
 }

 // Test for perform method with personal castles and fiefdom castles less than or equal to vassals
 @Test
 public void testPerformLessVassalsThanCastles() {
 Collection<String> col = new ArrayList<>();
 col.add("TestFiefdom");
 int personalCastles = 2;
 int fiefdomCastles = 3;
 fiefdomObj.setPersonalCastles(personalCastles);
 fiefdomObj.setFiefdomCastles(fiefdomCastles);
 fiefdomObj.setLoyalVassals(new ArrayList<>());
 for (int i = 0; i < personalCastles * 10 + fiefdomCastles * 25; i++) {
 fiefdomObj.addLoyalVassal();
 }
 vassalRebellion.perform(col);
 assertTrue(new RollEvent("TestFiefdom").isRolled());
 }
}