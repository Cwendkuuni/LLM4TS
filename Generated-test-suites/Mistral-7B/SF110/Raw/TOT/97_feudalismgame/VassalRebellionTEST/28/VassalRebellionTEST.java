import org.junit.Before;
import org.junit.Test;
import src.Actions;
import src.Fiefdoms;
import static org.junit.Assert.*;

public class VassalRebellionTest {

 private VassalRebellion vassalRebellion;
 private Fiefdoms fiefdoms;

 @Before
 public void setup() {
 fiefdoms = new Fiefdoms();
 vassalRebellion = new VassalRebellion();
 fiefdoms.put("Fief1", new Fiefdom("Fief1", 10, 5, 5, 10));
 }

 @Test
 public void testPerform_LoyalVassalsAndBothCastleTypesAndNoRebellion_ShouldNotTriggerRebellion() {
 Collection<String> input = new ArrayList<>();
 input.add("Fief1");
 vassalRebellion.perform(input);
 assertEquals(false, fiefdoms.get("Fief1").isRebellious());
 }

 @Test
 public void testPerform_LoyalVassalsAndNoCastles_ShouldNotTriggerRebellion() {
 Collection<String> input = new ArrayList<>();
 input.add("Fief1");
 vassalRebellion.perform(input);
 assertEquals(false, fiefdoms.get("Fief1").isRebellious());
 }

 @Test
 public void testPerform_LoyalVassalsAndBothCastleTypes_ShouldTriggerRebellionWithProperSize() {
 Collection<String> input = new ArrayList<>();
 input.add("Fief1");

 fiefdoms.get("Fief1").setLoyalVassals(new ArrayList<>(Arrays.asList(new Vassal("Vassal1"), new Vassal("Vassal2"))));
 fiefdoms.get("Fief1").setValue(15);

 vassalRebellion.perform(input);
 assertEquals(15, fiefdoms.get("Fief1").getRebelliousVassals().size());
 }

 @Test
 public void testPerform_NoLoyalVassals_ShouldRollEvent() {
 Collection<String> input = new ArrayList<>();
 input.add("Fief1");
 vassalRebellion.perform(input);
 assertTrue(RollEvent.eventList.size() > 0);
 }

 @Test(expected = NullPointerException.class)
 public void testPerform_NullCollection_ShouldThrowNullPointerException() {
 vassalRebellion.perform(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_EmptyCollection_ShouldThrowIllegalArgumentException() {
 vassalRebbellion.perform(new ArrayList<>());
 }
}