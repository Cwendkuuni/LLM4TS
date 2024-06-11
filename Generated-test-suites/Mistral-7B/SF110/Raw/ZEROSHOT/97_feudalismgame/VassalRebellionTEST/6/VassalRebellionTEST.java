package src.test.java;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class VassalRebellionTest {

 private Fiefdom fiefdom;
 private VassalRebellion vassalRebellion;

 @Before
 public void setUp() {
 Map<String, Fiefdom> map = new HashMap<>();
 fiefdom = new Fiefdom("TestFiefdom", 10, 5, new ArrayList<>(), false);
 map.put("TestFiefdom", fiefdom);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdoms(map);
 }

 @Test
 public void testPerformWithLoyalVassalsAndCastles() {
 // Given: Fiefdom with loyal vassals and castles
 List<Vassal> loyalVassals = new ArrayList<>();
 loyalVassals.add(new Vassal("TestVassal1", 5));
 loyalVassals.add(new Vassal("TestVassal2", 3));
 fiefdom.setLoyalVassals(loyalVassals);
 int personalCastles = 2;
 int fiefdomCastles = 1;
 fiefdom.setPersonalCastles(personalCastles);
 fiefdom.setFiefdomCastles(fiefdomCastles);

 // When: Perform VassalRebellion action
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

 // Then: Check if fiefdom is rebellious and kingdom is false
 assertTrue(fiefdom.isRebellious());
 assertFalse(fiefdom.isKingdom());

 // Given: Fiefdom with fewer vassals than the calculated rebels limit
 List<Vassal> fewerLoyalVassals = new ArrayList<>();
 fewerLoyalVassals.add(new Vassal("TestVassal1", 5));
 fiefdom.setLoyalVassals(fewerLoyalVassals);

 // When: Perform VassalRebellion action
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

 // Then: Check if fiefdom is rebellious and kingdom is false
 assertTrue(fiefdom.isRebellious());
 assertFalse(fiefdom.isKingdom());

 // Given: Fiefdom with no vassals
 List<Vassal> noLoyalVassals = new ArrayList<>();
 fiefdom.setLoyalVassals(noLoyalVassals);

 // When: Perform VassalRebellion action
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

 // Then: Check if a new RollEvent is triggered
 assertTrue(fiefdom.getLastEvent() instanceof RollEvent);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPerformWithEmptyCollection() {
 // Given: Empty Collection

 // When: Perform VassalRebellion action
 vassalRebellion.perform(Collections.emptyList());

 // Then: An UnsupportedOperationException is thrown
 }
}