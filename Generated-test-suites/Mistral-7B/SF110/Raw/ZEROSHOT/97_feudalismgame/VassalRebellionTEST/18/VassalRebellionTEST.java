package src.test.java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import src.Actions;
import src.Fiefdom;
import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdom fiefdom;
 private Actions actions;
 private VassalRebellion vassalRebellion;

 @Before
 public void setUp() {
 fiefdom = new Fiefdom("TestFiefdom", 10, 5, new ArrayList<>(), new ArrayList<>(), true);
 actions = new Actions();
 vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdomObj(fiefdom);
 }

 @Test
 public void testPerformWithLoyalVassalsAndCastles() {
 // Given: a fiefdom with loyal vassals and castles
 ArrayList<String> loyalVassals = new ArrayList<>();
 loyalVassals.add("Vassal1");
 loyalVassals.add("Vassal2");
 fiefdom.setLoyalVassals(loyalVassals);
 fiefdom.setPersonalCastles(1);
 fiefdom.setFiefdomCastles(2);

 // When: VassalRebellion is performed on the fiefdom
 vassalRebellion.perform(Collections.singletonList(fiefdom.getName()));

 // Then: the fiefdom's rebellious vassals should be set
 assertTrue(fiefdom.isRebellious());
 // Check number of rebellious vassals based on the conditions in the VassalRebellion class
 int expectedRebelliousVassals = 0;
 if (fiefdom.getLoyalVassals().size() > 0) {
 if (fiefdom.getLoyalVassals().size() - fiefdom.getValue() >= 0) {
 expectedRebelliousVassals = fiefdom.getValue();
 } else {
 expectedRebelliousVassals = fiefdom.getLoyalVassals().size();
 }
 }
 if ((fiefdom.getPersonalCastles() > 0 || fiefdom.getFiefdomCastles() > 0) && (fiefdom.getLoyalVassals().size() - (fiefdom.getPersonalCastles() * 10 + fiefdom.getFiefdomCastles() * 25) > 0)) {
 expectedRebelliousVassals += (fiefdom.getPersonalCastles() * 10 + fiefdom.getFiefdomCastles() * 25);
 }
 assertEquals(expectedRebelliousVassals, fiefdom.getRebelliousVassals().size());
 }

 @Test
 public void testPerformWithNoLoyalVassals() {
 // Given: a fiefdom with no loyal vassals
 fiefdom.setLoyalVassals(new ArrayList<>());

 // When: VassalRebellion is performed on the fiefdom
 vassalRebellion.perform(Collections.singletonList(fiefdom.getName()));

 // Then: a roll event should be triggered
 assertTrue(fiefdom.isRollEventTriggered());
 }
}