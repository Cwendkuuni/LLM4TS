package src.test.java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class VassalRebellionTest {

 private VassalRebellion vassalRebellion;
 private Fiefdoms fiefdomObj;

 @Before
 public void setUp() {
 // Create a mock Fiefdoms object
 fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<String>(), new ArrayList<String>(), true, true);
 Map<String, Fiefdoms> map = new HashMap<String, Fiefdoms>();
 map.put("TestFiefdom", fiefdomObj);
 // Create a VassalRebellion object with the mocked Fiefdoms object
 vassalRebellion = new VassalRebellion();
 vassalRebellion.setMap(map);
 }

 @Test
 public void testPerform_NoLoyalVassals_ShouldRollEvent() {
 // Given no loyal vassals in the fiefdom
 // When performing the VassalRebellion action
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));
 // Then a RollEvent should be triggered
 assertTrue(vassalRebellion.fiefdomObj.getRollEventTriggered());
 }

 @Test
 public void testPerform_LoyalVassals_ShouldSetRebelliousVassals() {
 // Given some loyal vassals in the fiefdom
 fiefdomObj.setLoyalVassals(Collections.singletonList("TestVassal1"));
 // When performing the VassalRebellion action
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));
 // Then the number of rebellious vassals should be equal to the value of the fiefdom
 assertEquals(1, vassalRebellion.fiefdomObj.getRebelliousVassals().size());
}

 @Test
 public void testPerform_MoreLoyalVassalsThanValue_ShouldSetAllRebelliousVassals() {
 // Given more loyal vassals than the value of the fiefdom
 fiefdomObj.setLoyalVassals(Collections.singletonList("TestVassal1"));
 fiefdomObj.setValue(0);
 // When performing the VassalRebellion action
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));
 // Then all loyal vassals should be set as rebellious vassals
 assertEquals(1, vassalRebellion.fiefdomObj.getLoyalVassals().size());
 assertEquals(1, vassalRebellion.fiefdomObj.getRebelliousVassals().size());
}

 @Test
 public void testPerform_PersonalCastlesAndFiefdomCastles_ShouldSetSomeRebelliousVassals() {
 // Given some loyal vassals, personal castles and fiefdom castles in the fiefdom
 fiefdomObj.setLoyalVassals(new ArrayList<String>(Collections.nCopies(10, "TestVassal")));
 int personalCastles = 2;
 int fiefdomCastles = 1;
 vassalRebellion.fiefdomObj.setPersonalCastles(personalCastles);
 vassalRebellion.fiefdomObj.setFiefdomCastles(fiefdomCastles);
 // When performing the VassalRebellion action
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));
 // Then the number of rebellious vassals should be equal to the sum of personal castles and fiefdom castles multiplied by 10 and 25 respectively and subtracted from the number of loyal vassals
 int expectedRebelliousVassals = 10 * personalCastles + 25 * fiefdomCastles;
 assertEquals(expectedRebelliousVassals, vassalRebellion.fiefdomObj.getRebelliousVassals().size());
}

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_EmptyCollection_ShouldThrowIllegalArgumentException() {
 // Given an empty collection
 // When performing the VassalRebellion action
 vassalRebellion.perform(Collections.emptyList());
 // Then an IllegalArgumentException should be thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_NullCollection_ShouldThrowIllegalArgumentException() {
 // Given a null collection
 // When performing the VassalRebellion action
 vassalRebellion.perform(null);
 // Then an IllegalArgumentException should be thrown
 }

}