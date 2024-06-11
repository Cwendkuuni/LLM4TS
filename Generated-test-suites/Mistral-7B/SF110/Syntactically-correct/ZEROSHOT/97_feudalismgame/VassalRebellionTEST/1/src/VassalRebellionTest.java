package src.test.java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class VassalRebellionTest {

 private Fiefdom fiefdom;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 fiefdom = new Fiefdom("TestFiefdom", 100, 5, new HashMap<>(), new ArrayList<>(), false);
 vassalRebellion = new VassalRebellion();
 Map<String, Fiefdom> map = new HashMap<>();
 map.put(fiefdom.getName(), fiefdom);
 vassalRebellion.setMap(map);
 }

 @Test
 public void testPerformWithNoLoyalVassals() {
 vassalRebellion.perform(Collections.singletonList(fiefdom.getName()));
 assertTrue(fiefdom.isKingdom());
 }

 @Test
 public void testPerformWithOneLoyalVassal() {
 addLoyalVassal(1);
 vassalRebellion.perform(Collections.singletonList(fiefdom.getName()));
 assertFalse(fiefdom.isKingdom());
 }

 @Test
 public void testPerformWithMultipleLoyalVassals() {
 addLoyalVassals(5);
 int loyalVassalsBeforeRebellion = fiefdom.getLoyalVassals().size();
 int personalCastles = fiefdom.getPersonalCastles();
 int fiefdomCastles = fiefdom.getFiefdomCastles();
 vassalRebellion.perform(Collections.singletonList(fiefdom.getName()));
 int loyalVassalsAfterRebellion = fiefdom.getLoyalVassals().size();

 if (personalCastles > 0 || fiefdomCastles > 0) {
 int vassalsToBeRemoved = personalCastles * 10 + fiefdomCastles * 25;
 assertEquals("The wrong number of rebellious vassals were added.", vassalsToBeRemoved, loyalVassalsAfterRebellion - loyalVassalsBeforeRebellion);
 }
 else {
 assertEquals("The wrong number of rebellious vassals were added.", 0, loyalVassalsAfterRebellion - loyalVassalsBeforeRebellion);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testPerformWithNegativeValueFiefdom() {
 fiefdom.setValue(-1);
 vassalRebellion.perform(Collections.singletonList(fiefdom.getName()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithNullCollection() {
 vassalRebellion.perform(null);
 }

 private void addLoyalVassals(int numberOfVassals) {
 for (int i = 0; i < numberOfVassals; i++) {
 fiefdom.addLoyalVassal();
 }
}

}