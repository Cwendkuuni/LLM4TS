import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.*;

import java.util.*;

public class VassalRebellionTest {

 private Fiefdom fiefdom;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 fiefdom = new Fiefdom("TestFiefdom", 10, 5);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdomObj(fiefdom);
 }

 @Test
 public void testPerformWithLoyalVassalsAndCastles() {
 // Given: A fiefdom with loyal vassals and castles
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));

 // Then: Check if the fiefdom's rebellious vassals have been set
 assertEquals(10, fiefdom.getRebelliousVassals().size());

 // Then: Check if the fiefdom's kingdom status has been set to false
 assertFalse(fiefdom.isKingdom());

 // Then: Check if the personal castles and fiefdom castles have been considered
 assertFalse(fiefdom.hasPersonalCastles());
 assertFalse(fiefdom.hasFiefdomCastles());
 }

 @Test
 public void testPerformWithNoLoyalVassals() {
 // Given: A fiefdom with no loyal vassals
 fiefdom = new Fiefdom("TestFiefdom", 0, 0);
 vassalRebellion.setFiefdomObj(fiefdom);

 // When: The perform method is called
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));

 // Then: A RollEvent should be triggered
 Mockito.verify(Mockito.mock(RollEvent.class)).newRollEvent(fiefdom.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithInvalidFiefdomName() {
 // Given: An invalid fiefdom name
 vassalRebbellion.perform(Arrays.asList("InvalidFiefdomName"));

 // Then: An IllegalArgumentException should be thrown
 }

 @Test(expected = IllegalStateException.class)
 public void testPerformWithNullFiefdomObj() {
 // Given: A null Fiefdom object
 vassalRebbellion.setFiefdomObj(null);

 // When: The perform method is called
 vassalRebbellion.perform(Arrays.asList("TestFiefdom"));

 // Then: An IllegalStateException should be thrown
 }
}