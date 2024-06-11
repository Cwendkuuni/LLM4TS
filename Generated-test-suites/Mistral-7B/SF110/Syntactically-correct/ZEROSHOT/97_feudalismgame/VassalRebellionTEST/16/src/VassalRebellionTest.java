package src.test.java;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import src.Actions;
import src.Fiefdom;
import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdom fiefdom;
 private Fiefdoms fiefdoms;
 private Actions actions;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 fiefdoms = new Fiefdoms();
 actions = new Actions();
 fiefdom = new Fiefdom("TestFiefdom", 10, 5, actions, fiefdoms);
 fiefdoms.addFiefdom(fiefdom);
 vassalRebellion = new VassalRebbellion();
 vassalRebellion.setFiefdoms(fiefdoms);
 }

 @Test
 public void testPerform_LoyalVassals_NoCastles() {
 vassalRebellion.perform(Collections.singleton(fiefdom.getName()));
 assertTrue(fiefdom.isKingdom());
 }

 @Test
 public void testPerform_LoyalVassals_PersonalCastle() {
 fiefdom.setPersonalCastles(1);
 vassalRebellion.perform(Collections.singleton(fiefdom.getName()));
 int expectedRebelliousVassals = fiefdom.getLoyalVassals().size() - 10;
 assertEquals(expectedRebelliousVassals, fiefdom.getRebelliousVassals().size());
 }

 @Test
 public void testPerform_LoyalVassals_FiefdomCastles() {
 fiefdom.setFiefdomCastles(1);
 vassalRebellion.perform(Collections.singleton(fiefdom.getName()));
 int expectedRebelliousVassals = fiefdom.getLoyalVassals().size() - 25;
 assertEquals(expectedRebelliousVassals, fiefdom.getRebelliousVassals().size());
 }

 @Test
 public void testPerform_LoyalVassals_PersonalAndFiefdomCastles() {
 fiefdom.setPersonalCastles(1);
 fiefdom.setFiefdomCastles(1);
 vassalRebellion.perform(Collections.singleton(fiefdom.getName()));
 int expectedRebelliousVassals = fiefdom.getLoyalVassals().size() - 10 - 25;
 assertEquals(expectedRebelliousVassals, fiefdom.getRebelliousVassals().size());
 }

 @Test
 public void testPerform_NoLoyalVassals() {
 fiefdom.setLoyalVassals(Collections.emptyList());
 vassalRebellion.perform(Collections.singleton(fiefdom.getName()));
 assertTrue(fiefdom.isRollEventTriggered());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_NullCollection() {
 vassalRebellion.perform(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_EmptyCollection() {
 vassalRebbellion.perform(Collections.emptyList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_InvalidFiefdomName() {
 vassalRebbellion.perform(Collections.singleton("InvalidFiefdomName"));
 }
}