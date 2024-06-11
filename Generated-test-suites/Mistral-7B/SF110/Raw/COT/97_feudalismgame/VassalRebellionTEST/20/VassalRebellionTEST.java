import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.Fiefdoms;
import src.VassalRebellion;

import java.util.*;

import static org.junit.Assert.*;

public class VassalRebellionTest {

 private VassalRebellion vassalRebellion;
 private Fiefdoms fiefdomObj;

 @Before
 public void setUp() {
 fiefdomObj = Mockito.mock(Fiefdoms.class);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdomObj(fiefdomObj);
 }

 // Test method: perform(Collection)
 @Test
 public void testPerform() {
 Collection<String> fiefdomNames = new ArrayList<>();
 fiefdomNames.add("TestFiefdom");
 vassalRebellion.perform(fiefdomNames);

 // Basic functionality test
 assertEquals(false, fiefdomObj.isKingdom());
 assertTrue(fiefdomObj.isRebelliousVassals());

 // Test edge cases and exceptions

 // Test when fiefdom does not exist
 Collection<String> emptyList = new ArrayList<>();
 vassalRebellion.perform(emptyList);
 assertFalse(fiefdomObj.isRebelliousVassals());
 // no need to check kingdom status as it should not have been set

 // Test when fiefdom has no loyal vassals
 Mockito.when(fiefdomObj.getLoyalVassals().size()).thenReturn(0);
 vassalRebellion.perform(fiefdomNames);
 assertTrue(fiefdomObj.isRebelliousVassals());
 // no need to check kingdom status as it should not have been set

 // Test when number of rebellious vassals exceeds total vassals
 Mockito.when(fiefdomObj.getValue()).thenReturn(1);
 vassalRebellion.perform(fiefdomNames);
 assertEquals(1, fiefdomObj.getRebelliousVassals().size());
 assertFalse(fiefdomObj.isKingdom());

 // Test when personal castles and fiefdom castles are zero
 Mockito.when(fiefdomObj.getPersonalCastles()).thenReturn(0);
 Mockito.when(fiefdomObj.getFiefdomCastles()).thenReturn(0);
 vassalRebellion.perform(fiefdomNames);
 assertFalse(fiefdomObj.isRebelliousVassals());
 // no need to check kingdom status as it should not have been set

 // Test when personal castles and fiefdom castles are not zero and total rebellious vassals exceed possible rebellion
 Mockito.when(fiefdomObj.getPersonalCastles()).thenReturn(1);
 Mockito.when(fiefdomObj.getFiefdomCastles()).thenReturn(1);
 Mockito.when(fiefdomObj.getLoyalVassals().size()).thenReturn(5);
 vassalRebellion.perform(fiefdomNames);
 assertEquals(2, fiefdomObj.getRebelliousVassals().size());
 assertFalse(fiefdomObj.isKingdom());
 }
}