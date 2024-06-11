package src.test;

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
 fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, 5, new ArrayList<>(), new ArrayList<>());
 vassalRebellion = new VassalRebellion();
 }

 @Test
 public void testPerform_LoyalVassalsAndCastles_Rebellion() {
 // Given: Fiefdom with loyal vassals and castles
 int loyalVassalsSizeBefore = fiefdomObj.getLoyalVassals().size();
 int personalCastles = fiefdomObj.getPersonalCastles();
 int fiefdomCastles = fiefdomObj.getFiefdomCastles();

 // When: Vassal Rebellion is triggered
 vassalRebellion.perform(Collections.singletonList(fiefdomObj.getName()));

 // Then: Fiefdom becomes rebellious and loyal vassals decrease
 int loyalVassalsSizeAfter = fiefdomObj.getLoyalVassals().size();
 assertTrue(fiefdomObj.isRebellious());
 assertTrue(personalCastles > 0 || fiefdomCastles > 0);
 assertTrue(personalCastles * 10 + fiefdomCastles * 25 <= loyalVassalsSizeBefore);
 assertTrue(loyalVassalsSizeAfter < loyalVassalsSizeBefore);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_NoLoyalVassals_RollEvent() {
 // Given: Fiefdom with no loyal vassals

 // When: Vassal Rebellion is triggered
 vassalRebellion.perform(Collections.singletonList(fiefdomObj.getName()));

 // Then: RollEvent is triggered instead
 assertTrue(fiefdomObj.isRollEventTriggered());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_NegativeLoyalVassals_IllegalArgumentException() {
 // Given: Fiefdom with negative loyal vassals
 fiefdomObj.setLoyalVassals(Collections.singletonList(-1));

 // When: Vassal Rebellion is triggered
 vassalRebellion.perform(Collections.singletonList(fiefdomObj.getName()));

 // Then: IllegalArgumentException is thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_EmptyCollection_IllegalArgumentException() {
 // Given: Empty collection is passed to perform method

 // When: Vassal Rebellion is triggered
 vassalRebellion.perform(new ArrayList<>());

 // Then: IllegalArgumentException is thrown
 }
}