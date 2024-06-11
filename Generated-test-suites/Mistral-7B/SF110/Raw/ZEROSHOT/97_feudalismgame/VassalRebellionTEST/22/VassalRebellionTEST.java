package src.test.java;

import static org.junit.Assert.*;
import src.Fiefdoms;
import src.VassalRebellion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class VassalRebellionTest {

 private Fiefdoms fiefdomObj;
 private VassalRebellion vassalRebellion;

 @Before
 public void setUp() {
 fiefdomObj = new Fiefdoms("TestFiefdom", 5, 3, 10);
 vassalRebellion = new VassalRebllion();
 }

 @Test
 public void testPerformWithLoyalVassalsAndCastles() {
 // Given: Fiefdom with loyal vassals and castles
 List<String> loyalVassals = new ArrayList<>();
 loyalVassals.add("Vassal1");
 loyalVassals.add("Vassal2");
 fiefdomObj.setLoyalVassals(loyalVassals);
 int personalCastles = 2;
 int fiefdomCastles = 1;
 Map<String, Fiefdoms> map = new HashMap<>();
 map.put("TestFiefdom", fiefdomObj);

 // When: Vassal rebellion is performed
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

 // Then: Fiefdom's vassals are rebellious and kingdom is false
 assertTrue(fiefdomObj.isRebellious());
 assertFalse(fiefdomObj.isKingdom());

 // And: Number of rebellious vassals is equal to the value of the fiefdom
 int expectedRebelliousVassals = fiefdomObj.getValue();
 int actualRebelliousVassals = fiefdomObj.getRebelliousVassals().size();
 assertEquals("Number of rebellious vassals is not as expected", expectedRebelliousVassals, actualRebelliousVassals);

 // And: Personal castles and fiefdom castles reduce the number of loyal vassals
 int expectedLoyalVassals = fiefdomObj.getLoyalVassals().size() - (personalCastles * 10 + fiefdomCastles * 25);
 int actualLoyalVassals = fiefdomObj.getLoyalVassals().size();
 assertEquals("Number of loyal vassals is not as expected", expectedLoyalVassals, actualLoyalVassals);
 }

 @Test
 public void testPerformWithNoLoyalVassals() {
 // Given: Fiefdom with no loyal vassals
 fiefdomObj.setLoyalVassals(Collections.emptyList());

 // When: Vassal rebellion is performed
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

 // Then: A new roll event is created
 assertTrue(Map.map.get("TestFiefdom").getRollEvents().size() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithEmptyCollection() {
 // Given: Empty collection

 // When: Vassal rebellion is performed
 vassalRebellion.perform(Collections.emptyList());

 // Then: An IllegalArgumentException is thrown
 }

 @Test(expected = NullPointerException.class)
 public void testPerformWithNullFiefdom() {
 // Given: Null fiefdom
 vassalRebellion.perform(Collections.singletonList(null));

 // Then: A NullPointerException is thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithUnknownFiefdom() {
 // Given: Unknown fiefdom

 // When: Vassal rebellion is performed
 vassalRebellion.perform(Collections.singletonList("UnknownFiefdom"));

 // Then: An IllegalArgumentException is thrown
 }
}