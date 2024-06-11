import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdoms fiefdomObj;
 private VassalRebellion vassalRebellion;

 @Before
 public void setUp() {
 // Initialize fiefdom object and vassal rebellion object here
 }

 // Test method: perform
 @Test
 public void testPerform() {
 // Test case for normal scenario
 List<String> fiefdomsList = new ArrayList<>();
 fiefdomsList.add("TestFiefdom");
 Map<String, Fiefdoms> map = new HashMap<>();
 map.put("TestFiefdom", fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, 15, 5));
 vassalRebellion = new VassalRebellion();
 vassalRebellion.perform(fiefdomsList);
 assertTrue(fiefdomObj.isRebellious());
 assertTrue(fiefdomObj.isKingdom() == false);
 // Test case for empty fiefdom
 List<String> emptyFiefdomsList = new ArrayList<>();
 vassalRebellion.perform(emptyFiefdomsList);
 assertFalse(Map.map.containsValue(fiefdomObj));
 // Test case for no loyal vassals
 fiefdomObj = new Fiefdoms("TestFiefdom", 0, 5, 15, 5);
 vassalRebellion.perform(fiefdomsList);
 assertFalse(fiefdomObj.isRebellious());
 // Test case for no loyal vassals and no castles
 Fiefdoms noLoyalVassalsOrCastlesFiefdom = new Fiefdoms("TestFiefdom", 0, 0, 15, 5);
 vassalRebellion.perform(fiefdomsList);
 new RollEvent(noLoyalVassalsOrCastlesFiefdom.getName());
 // Test case for insufficient loyal vassals
 fiefdomObj.setValue(5);
 vassalRebellion.perform(fiefdomsList);
 assertFalse(fiefdomObj.isRebellious());
 // Test case for insufficient loyal vassals and castles
 fiefdomObj.setValue(5);
 fiefdomObj.setPersonalCastles(1);
 vassalRebbellion.perform(fiefdomsList);
 assertFalse(fiefdomObj.isRebellious());
 }

 // Test method: getFiefdoms() (assuming this is a method in the VassalRebellion class)
 @Test
 public void testGetFiefdoms() {
 // Test case for getting the list of fiefdoms
 List<Fiefdoms> expectedFiefdomsList = new ArrayList<>();
 expectedFiefdomsList.add(fiefdomObj);
 List<Fiefdoms> actualFiefdomsList = vassalRebellion.getFiefdoms();
 assertEquals(expectedFiefdomsList, actualFiefdomsList);
 }

 // ... Add more test cases for other methods in the VassalRebellion class if necessary

}