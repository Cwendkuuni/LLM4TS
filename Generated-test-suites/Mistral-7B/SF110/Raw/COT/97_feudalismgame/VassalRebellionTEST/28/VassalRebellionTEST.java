package src.test; import org.junit.Test; import static org.junit.Assert.assertFalse; import static org.junit.Assert.assertNotNull; import static org.junit.Assert.assertNull; import static org.junit.Assert.assertTrue; import src.Fiefdoms; import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdoms fiefdomObject;
 private VassalRebbellion vassalRebellion;

 @Before
 public void setUp() {
 fiefdomObject = new Fiefdoms("TestFiefdom", 10, 5, 5);
 vassalRebbellion = new VassalRebbellion();
 }

 @Test
 public void testPerform_FiefdomHasLoyalVassals_RebelliousVassalsIncreased() {
 // Test with valid inputs
 vassalRebbellion.perform(Collections.singletonList(fiefdomObject.getName()));
 assertTrue(fiefdomObject.getRebelliousVassals().size() > 0);
 }

 @Test(expected = NullPointerException.class)
 public void testPerform_FiefdomNameNotFound_RollEventTriggered() {
 // Test with invalid input - Fiefdom not found
 vassalRebbellion.perform(Collections.singletonList("UnknownFiefdom"));
 }

 @Test
 public void testPerform_FiefdomHasNoLoyalVassals_RollEventTriggered() {
 // Test with valid input - Fiefdom has no loyal vassals
 fiefdomObject.setLoyalVassals(new ArrayList<>());
 vassalRebbellion.perform(Collections.singletonList(fiefdomObject.getName()));
 assertNull(fiefdomObject.getRebelliousVassals());
 }

 @Test
 public void testPerform_LoyalVassalsLessThanValue_RebelliousVassalsSetToLoyalVassalsSize() {
 // Test with valid input - Loyal vassals less than value
 fiefdomObject.setValue(3);
 vassalRebbellion.perform(Collections.singletonList(fiefdomObject.getName()));
 assertTrue(fiefdomObject.getRebelliousVassals().size() == fiefdomObject.getLoyalVassals().size());
 }

 @Test
 public void testPerform_LoyalVassalsEqualsValue_RebelliousVassalsSetToZero() {
 // Test with valid input - Loyal vassals equal to value
 fiefdomObject.setValue(10);
 vassalRebbellion.perform(Collections.singletonList(fiefdomObject.getName()));
 assertFalse(fiefdomObject.getRebelliousVassals().isEmpty());
 }

 @Test
 public void testPerform_PersonalCastlesAndFiefdomCastlesGreaterThanZero_RebelliousVassalsDecreased() {
 // Test with valid input - Personal castles and fiefdom castles greater than zero
 fiefdomObject.setPersonalCastles(2);
 fiefdomObject.setFiefdomCastles(3);
 vassalRebbellion.perform(Collections.singletonList(fiefdomObject.getName()));
 int initialSize = fiefdomObject.getLoyalVassals().size();
 int rebelsSize = fiefdomObject.getRebelliousVassals().size();
 int expectedSize = initialSize - (2 * 10 + 3 * 25);
 assertTrue(expectedSize >= fiefdomObject.getRebelliousVassals().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_PersonalCastlesAndFiefdomCastlesLessThanZero() {
 // Test with invalid input - Personal castles and fiefdom castles less than zero
 fiefdomObject.setPersonalCastles(-1);
 fiefdomObject.setFiefdomCastles(-2);
 vassalRebbellion.perform(Collections.singletonList(fiefdomObject.getName()));
 }
}