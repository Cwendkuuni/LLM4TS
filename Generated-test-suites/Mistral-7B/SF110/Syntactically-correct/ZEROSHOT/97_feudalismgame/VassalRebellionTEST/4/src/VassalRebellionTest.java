package src.test.java;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class VassalRebellionTest {

 private Fiefdom fiefdom;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 Map<String, Fiefdom> map = new HashMap<>();
 fiefdom = new Fiefdom("TestFiefdom", 10, 5, 5, 5);
 map.put("TestFiefdom", fiefdom);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.setMap(map);
 }

 @Test
 public void testPerform_LoyalVassalsAndCastles_Rebellion() {
 // Given: Loyal vassals and castles in the fiefdom
 fiefdom.getValue().add(new Vassal("TestVassal1"));
 fiefdom.getValue().add(new Vassal("TestVassal2"));
 fiefdom.setPersonalCastles(1);
 fiefdom.setFiefdomCastles(2);

 // When: Perform VassalRebellion action
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

 // Then: Check if the fiefdom's vassals are rebellious
 assertTrue(fiefdom.getRebelliousVassals().size() > 0);
 // Check if the fiefdom's kingdom status is false
 assertFalse(fiefdom.isKingdom());

 // Given: Personal castles and fiefdom castles
 int personalCastles = 2;
 int fiefdomCastles = 3;
 fiefdom.setPersonalCastles(personalCastles);
 fiefdom.setFiefdomCastles(fiefdomCastles);

 // When: Perform VassalRebellion action
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

 // Then: Check if the number of rebellious vassals is correct
 int expectedRebelliousVassals = fiefdom.getLoyalVassals().size() - personalCastles * 10 - fiefdomCastles * 25;
 int actualRebelliousVassals = fiefdom.getRebelliousVassals().size();
 assertEquals(expectedRebelliousVassals, actualRebelliousVassals);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_NoLoyalVassals_RollEvent() {
 // Given: No loyal vassals in the fiefdom
 fiefdom.setValue(new ArrayList<>());

 // When: Perform VassalRebellion action
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

 // Then: Expect an IllegalArgumentException
 }
}