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
 fiefdom = new Fiefdom("TestFiefdom", 10, 5, new ArrayList<>());
 map.put("TestFiefdom", fiefdom);
 vassalRebellion = new VassalRebellion(map);
 }

 @Test
 public void testPerform_LoyalVassals_NoRebellion() {
 // Given: No rebellion and loyal vassals
 Collections.addAll(fiefdom.getLoyalVassals(), new Vassal("TestVassal1"), new Vassal("TestVassal2"));
 // When: Perform action
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 // Then: No rebellious vassals
 assertTrue(fiefdom.getRebelliousVassals().isEmpty());
 }

 @Test
 public void testPerform_LoyalVassals_Rebellion() {
 // Given: Rebellion condition met and loyal vassals
 Collections.addAll(fiefdom.getLoyalVassals(), new Vassal("TestVassal1"), new Vassal("TestVassal2"), new Vassal("TestVassal3"));
 fiefdom.setValue(5);
 // When: Perform action
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 // Then: Number of rebellious vassals equals the value
 assertEquals(5, fiefdom.getRebelliousVassals().size());
 }

 @Test
 public void testPerform_NoLoyalVassals() {
 // Given: No loyal vassals
 // When: Perform action
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 // Then: Roll event is triggered
 assertTrue(Map.map.get("TestFiefdom").getRollEvents().size() > 0);
 }

 @Test
 public void testPerform_Castles_NoRebellion() {
 // Given: No rebellion and castles
 fiefdom.setPersonalCastles(1);
 fiefdom.setFiefdomCastles(2);
 // When: Perform action
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 // Then: No rebellious vassals
 assertTrue(fiefdom.getRebelliousVassals().isEmpty());
 }

 @Test
 public void testPerform_Castles_Rebellion() {
 // Given: Rebellion condition met and castles
 fiefdom.setPersonalCastles(1);
 fiefdom.setFiefdomCastles(2);
 fiefdom.setValue(3);
 // When: Perform action
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));
 // Then: Number of rebellious vassals equals the castles' influence points multiplied by 10 for personal castles and 25 for fiefdom castles
 int expectedSize = (1 * 10) + (2 * 25);
 assertEquals(expectedSize, fiefdom.getRebelliousVassals().size());
 }
}