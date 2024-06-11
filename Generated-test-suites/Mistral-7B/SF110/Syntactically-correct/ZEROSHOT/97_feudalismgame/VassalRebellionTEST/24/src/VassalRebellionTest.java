package src.test.java;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class VassalRebellionTest {

 private Fiefdoms fiefdomObj;
 private VassalRebellion vassalRebellion;

 @Before
 public void setUp() {
 Map<String, Fiefdoms> map = new HashMap<>();
 fiefdomObj = new Fiefdoms("TestFiefdom", 5, 3, 10, 5, new ArrayList<>(), true);
 map.put(fiefdomObj.getName(), fiefdomObj);

 vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdoms(map);
 }

 @Test
 public void testPerform_LoyalVassalsAndCastles_Rebellion() {
 // given: Fiefdom with loyal vassals and castles
 fiefdomObj.setLoyalVassals(Arrays.asList(new Vassal("TestVassal1"), new Vassal("TestVassal2"), new Vassal("TestVassal3")));
 fiefdomObj.setPersonalCastles(2);
 fiefdomObj.setFiefdomCastles(1);

 // when: VassalRebellion is performed
 vassalRebellion.perform(Collections.singletonList(fiefdomObj.getName()));

 // then: Fiefdom is rebellious and kingdom is false
 assertTrue(fiefdomObj.isRebellious());
 assertFalse(fiefdomObj.isKingdom());

 // then: Number of rebellious vassals is equal to the value or the size of loyal vassals, whichever is smaller
 int expectedRebelliousVassals = Math.min(fiefdomObj.getValue(), fiefdomObj.getLoyalVassals().size());
 assertEquals(expectedRebelliousVassals, fiefdomObj.getRebelliousVassals().size());

 // then: If personal castles or fiefdom castles exist, the number of rebellious vassals is reduced accordingly
 int personalCastles = 2;
 int fiefdomCastles = 1;
 int expectedRebelliousVassalsAfterCastles = fiefdomObj.getLoyalVassals().size() - (personalCastles * 10 + fiefdomCastles * 25);
 assertEquals(expectedRebelliousVassalsAfterCastles, fiefdomObj.getRebelliousVassals().size());
 }

 @Test
 public void testPerform_NoLoyalVassals_RollEvent() {
 // given: Fiefdom without loyal vassals
 fiefdomObj.setLoyalVassals(new ArrayList<>());

 // when: VassalRebellion is performed
 vassalRebellion.perform(Collections.singletonList(fiefdomObj.getName()));

 // then: A RollEvent is triggered
 assertTrue(fiefdomObj.getEvents().size() > 0);
 }

 // Add more test cases as needed to cover edge cases and other possible scenarios
}