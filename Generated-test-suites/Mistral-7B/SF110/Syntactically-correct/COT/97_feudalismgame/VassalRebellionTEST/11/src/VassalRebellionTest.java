import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import src.Fiefdoms;
import src.VassalRebellion;
import java.util.*;

public class VassalRebellionTest {

 private Fiefdoms fiefdomObj;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<String>(), new ArrayList<String>());
 vassalRebellion = new VassalRebellion();
 }

 @Test
 public void testPerform_LoyalVassalsAndCastles_Rebellion() {
 // Given: Fiefdom with loyal vassals and castles
 int expectedRebelliousVassals = fiefdomObj.getLoyalVassals().size() + fiefdomObj.getValue();
 vassalRebellion.perform(Arrays.asList(fiefdomObj.getName()));
 // When: Perform vassal rebellion
 int actualRebelliousVassals = fiefdomObj.getRebelliousVassals().size();
 // Then: Check the number of rebellious vassals
 assertEquals(expectedRebelliousVassals, actualRebelliousVassals);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_InvalidFiefdomName() {
 // Given: Invalid fiefdom name
 vassalRebellion.perform(Arrays.asList("InvalidFiefdomName"));
 // Then: Expect an IllegalArgumentException
 }

 @Test(expected = IllegalStateException.class)
 public void testPerform_NoLoyalVassals() {
 // Given: Fiefdom with no loyal vassals
 fiefdomObj.setLoyalVassals(new ArrayList<String>());
 vassalRebellion.perform(Arrays.asList(fiefdomObj.getName()));
 // Then: Expect an IllegalStateException
 }

 @Test
 public void testPerform_NoCastles_NoRebellion() {
 // Given: Fiefdom with no castles and no rebellion
 vassalRebellion.perform(Arrays.asList(fiefdomObj.getName()));
 // Then: Check the number of rebellious vassals
 assertEquals(0, fiefdomObj.getRebelliousVassals().size());
 }

 @Test
 public void testPerform_LoyalVassalsAndCastles_NoRebellion() {
 // Given: Fiefdom with loyal vassals and castles, but no conditions for rebellion
 fiefdomObj.setValue(1);
 vassalRebellion.perform(Arrays.asList(fiefdomObj.getName()));
 // Then: Check the number of rebellious vassals
 assertEquals(0, fiefdomObj.getRebelliousVassals().size());
 }

 @Test(expected = IllegalStateException.class)
 public void testPerform_InsufficientLoyalVassals() {
 // Given: Fiefdom with insufficient loyal vassals
 fiefdomObj.setValue(11);
 vassalRebellion.perform(Arrays.asList(fiefdomObj.getName()));
 // Then: Expect an IllegalStateException
 }

 @Test(expected = IllegalStateException.class)
 public void testPerform_InsufficientPersonalCastlesAndFiefdomCastles() {
 // Given: Fiefdom with insufficient personal castles and fiefdom castles
 fiefdomObj.setValue(0);
 fiefdomObj.setPersonalCastles(0);
 fiefdomObj.setFiefdomCastles(0);
 vassalRebellion.perform(Arrays.asList(fiefdomObj.getName()));
 // Then: Expect an IllegalStateException
 }

 @Test
 public void testPerform_SufficientPersonalCastlesAndFiefdomCastles_Rebellion() {
 // Given: Fiefdom with sufficient personal castles and fiefdom castles
 fiefdomObj.setValue(0);
 fiefdomObj.setPersonalCastles(1);
 fiefdomObj.setFiefdomCastles(1);
 vassalRebellion.perform(Arrays.asList(fiefdomObj.getName()));
 // When: Perform vassal rebellion
 int expectedRebelliousVassals = fiefdomObj.getLoyalVassals().size() - (fiefdomObj.getPersonalCastles() * 10 + fiefdomObj.getFiefdomCastles() * 25);
 // Then: Check the number of rebellious vassals
 int actualRebelliousVassals = fiefdomObj.getRebelliousVassals().size();
 // Then: Check the number of rebellious vassals
 assertEquals(expectedRebelliousVassals, actualRebelliousVassals);
 }

 @Test
 public void testPerform_MultipleFiefdoms() {
 // Given: Multiple fiefdoms
 Fiefdoms fiefdom1 = new Fiefdoms("Fiefdom1", 10, 5, new ArrayList<String>(), new ArrayList<String>());
 Fiefdoms fiefdom2 = new Fiefdoms("Fiefdom2", 5, 10, new ArrayList<String>(), new ArrayList<String>());
 Map<String, Fiefdoms> map = new HashMap<>();
 map.put(fiefdom1.getName(), fiefdom1);
 map.put(fiefdom2.getName(), fiefdom2);
 vassalRebellion.perform(Arrays.asList(fiefdom1.getName()));
 vassalRebbellion.perform(Arrays.asList(fiefdom2.getName()));
 // Then: Check the number of rebellious vassals in each fiefdom
 assertEquals(25, fiefdom1.getRebelliousVassals().size());
 assertEquals(50, fiefdom2.getRebelliousVassals().size());
 }
}