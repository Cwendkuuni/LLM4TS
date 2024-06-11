package src.test; import org.junit.jupiter.api.*; import static org.junit.jupiter.api.Assertions.*; import src.Fiefdoms; import src.VassalRebellion;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) public class VassalRebellionTest {

 private Fiefdoms map;
 private VassalRebellion vassalRebellion;
 private Fiefdom fiefdom;

 @BeforeAll static void setupClass() { map = new Fiefdoms(); }

 @BeforeEach void setup() { fiefdom = new Fiefdom("TestFiefdom", 10, 5, new ArrayList<>(), 3); map.put("TestFiefdom", fiefdom); vassalRebellion = new VassalRebellion(); }

 @Test void testPerformWithLoyalVassalsAndNoCastles() { vassalRebellion.perform(Arrays.asList("TestFiefdom")); assertFalse(fiefdom.isRebellious()); assertFalse(fiefdom.isKingdom()); }

 @Test void testPerformWithLoyalVassalsAndCastles() { fiefdom.setPersonalCastles(1); fiefdom.setFiefdomCastles(1); vassalRebellion.perform(Arrays.asList("TestFiefdom")); assertFalse(fiefdom.isRebellious()); assertFalse(fiefdom.isKingdom()); }

 @Test void testPerformWithLoyalVassalsAndMoreLoyalVassalsThanVassalValue() { fiefdom.setValue(2); vassalRebellion.perform(Arrays.asList("TestFiefdom")); assertFalse(fiefdom.isRebellious()); assertFalse(fiefdom.isKingdom()); }

 @Test void testPerformWithNoLoyalVassals() { map.remove("TestFiefdom"); vassalRebellion.perform(Arrays.asList("TestFiefdom")); assertTrue(new RollEvent("TestFiefdom").isRolled()); }

 @Test void testPerformWithMoreLoyalVassalsThanPersonalAndFiefdomCastles() { fiefdom.setPersonalCastles(0); fiefdom.setFiefdomCastles(0); fiefdom.setValue(5); vassalRebellion.perform(Arrays.asList("TestFiefdom")); assertFalse(fiefdom.isRebellious()); assertFalse(fiefdom.isKingdom()); }

 @Test void testPerformWithLoyalVassalsAndMoreLoyalVassalsThanPersonalAndFiefdomCastles() { fiefdom.setPersonalCastles(0); fiefdom.setFiefdomCastles(0); fiefdom.setValue(5); vassalRebellion.perform(Arrays.asList("TestFiefdom")); assertFalse(fiefdom.isRebellious()); assertFalse(fiefdom.isKingdom()); }

 @Test void testPerformWithLoyalVassalsAndNegativePersonalAndFiefdomCastles() { fiefdom.setPersonalCastles(-1); fiefdom.setFiefdomCastles(-2); vassalRebellion.perform(Arrays.asList("TestFiefdom")); assertFalse(fiefdom.isRebellious()); assertFalse(fiefdom.isKingdom()); }
}