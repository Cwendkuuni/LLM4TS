package src.test;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class VassalRebellionTest {

 private Fiefdoms fiefdomObj;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 Map<String, Fiefdoms> map = new HashMap<>();
 fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>(), true, false);
 map.put("TestFiefdom", fiefdomObj);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.setMap(map);
 }

 @Test
 public void testPerform_LoyalVassals_NoRebellion() {
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));
 assertEquals(0, fiefdomObj.getRebelliousVassals().size());
 }

 @Test
 public void testPerform_LoyalVassals_Rebellion() {
 // Add some loyal vassals
 fiefdomObj.setLoyalVassals(new ArrayList<>(Arrays.asList("Vassal1", "Vassal2", "Vassal3")));
 // Set values for the conditions to trigger rebellion
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));
 // Check if the fiefdom has become rebellious
 assertEquals(true, fiefdomObj.isRebellious());
 // Check if the number of rebellious vassals is correct
 assertEquals(2, fiefdomObj.getRebelliousVassals().size());
 // Check if the loyal vassals list has been updated
 assertEquals(1, fiefdomObj.getLoyalVassals().size());
}

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_InvalidFiefdomName() {
 vassalRebellion.perform(Collections.singletonList("InvalidFiefdom"));
 }

 @Test
 public void testPerform_NoLoyalVassals() {
 // Set an empty list for loyal vassals
 fiefdomObj.setLoyalVassals(new ArrayList<>());
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));
 // Check if a roll event has been triggered
 assertTrue(new RollEvent("TestFiefdom").isRolled());
}

 @Test
 public void testPerform_PersonalCastlesAndFiefdomCastles() {
 // Set values for personal castles and fiefdom castles
 fiefdomObj.setPersonalCastles(1);
 fiefdomObj.setFiefdomCastles(1);
 // Set values for the number of loyal vassals
 fiefdomObj.setLoyalVassals(new ArrayList<>(Arrays.asList("Vassal1", "Vassal2", "Vassal3", "Vassal4", "Vassal5", "Vassal6", "Vassal7", "Vassal8", "Vassal9", "Vassal10")));
 // Perform the action
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));
 // Check if the number of rebellious vassals is correct
 assertEquals(6, fiefdomObj.getRebelliousVassals().size());
}

}