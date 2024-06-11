import org.junit.Before;
import org.junit.Test;
import src.Actions;
import src.Fiefdom;
import src.Map;
import static org.junit.Assert.*;

public class VassalRebellionTest {

 private VassalRebellion vassalRebellion;
 private Fiefdom fiefdom;
 private Map map;

 @Before
 public void setUp() {
 Map map = new Map();
 fiefdom = new Fiefdom("TestFiefdom", 100, 5, new ArrayList<>(), true, false);
 map.put("TestFiefdom", fiefdom);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdomObj(map.get("TestFiefdom"));
 }

 // Test method: perform(Collection)
 @Test
 public void testPerform() {
 Collection<String> col = new ArrayList<>();
 col.add("TestFiefdom");
 vassalRebellion.perform(col);

 // Basic functionality test
 assertTrue(vassalRebellion.getFiefdomObj().isRebellious());
 assertTrue(vassalRebbellion.getFiefdomObj().isKingdom());

 // Test edge cases and exceptions
 // Test when fiefdom has no loyal vassals
 testPerformWhenFiefdomHasNoLoyalVassals();

 // Test when fiefdom value is less than the number of loyal vassals
 testPerformWhenFiefdomValueLessThanLoyalVassals();

 // Test when there are no personal castles or fiefdom castles
 testPerformWhenNoCastles();

 // Test when the number of loyal vassals is less than the calculation for rebellious vassals
 testPerformWhenLoyalVassalsLessThanCalculatedRebelliousVassals();
 }

 private void testPerformWhenFiefdomHasNoLoyalVassals() {
 vassalRebellion.setFiefdomObj(new Fiefdom("TestFiefdom", 100, 5, new ArrayList<>(), false, false));
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

 assertTrue(vassalRebellion.getFiefdomObj().getLoyalVassals().isEmpty());
 assertTrue(vassalRebbellion.getFiefdomObj().isKingdom());
 assertFalse(vassalRebbellion.getFiefdomObj().isRebellious());
 }

 private void testPerformWhenFiefdomValueLessThanLoyalVassals() {
 vassalRebellion.setFiefdomObj(new Fiefdom("TestFiefdom", 5, 5, new ArrayList<>(), true, false));
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

 assertEquals(5, vassalRebellion.getFiefdomObj().getLoyalVassals().size());
 assertFalse(vassalRebbellion.getFiefdomObj().isRebellious());
 }

 private void testPerformWhenNoCastles() {
 vassalRebellion.setFiefdomObj(new Fiefdom("TestFiefdom", 100, 0, new ArrayList<>(), true, false));
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

 assertEquals(100, vassalRebellion.getFiefdomObj().getLoyalVassals().size());
 assertFalse(vassalRebbellion.getFiefdomObj().isRebellious());
 }

 private void testPerformWhenLoyalVassalsLessThanCalculatedRebelliousVassals() {
 vassalRebellion.setFiefdomObj(new Fiefdom("TestFiefdom", 100, 5, new ArrayList<>(), true, false));
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

 assertEquals(5, vassalRebbellion.getFiefdomObj().getLoyalVassals().size());
 assertTrue(vassalRebbellion.getFiefdomObj().isRebellious());
 }
}