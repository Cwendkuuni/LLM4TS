package src.test;import org.junit.Before;import org.junit.Test;import static org.mockito.Mockito.*;import java.util.*;

public class VassalRebellionTest { private Fiefdoms fiefdomObj; private VassalRebellion vassalRebellion;

@Before public void setup() { fiefdomObj = mock(Fiefdoms.class); vassalRebellion = new VassalRebbellion() { @Override public Fiefdoms getFiefdomObj() { return fiefdomObj; } };}

@Test public void testPerformWithLoyalVassalsAndCastles() { // Given: Fiefdom with loyal vassals and castles when (fiefdomObj.getLoyalVassals().size() > 0) && (personalCastles > 0 || fiefdomCastles > 0) then vassalRebellion.perform(Arrays.asList("testFiefdom")); // Assert: Rebellious vassals and kingdom set to false

verify(fiefdomObj, times(1)).setRebelliousVassals(anyInt(), true); verify(fiefdomObj, times(1)).setKingdom(false);}

@Test public void testPerformWithNoLoyalVassalsAndNoCastles() { // Given: Fiefdom with no loyal vassals and no castles when vassalRebellion.perform(Arrays.asList("testFiefdom")) then new RollEvent(eq("testFiefdom")) // Assert: RollEvent is triggered

verify(fiefdomObj, never()).setRebelliousVassals(anyInt(), anyBoolean()); verify(fiefdomObj, never()).setKingdom(anyBoolean());}

@Test(expected = IllegalArgumentException.class) public void testPerformWithInvalidFiefdomName() { // Given: Invalid fiefdom name when (vassalRebellion.perform(Arrays.asList("invalidFiefdomName"))) then // Assert: IllegalArgumentException is thrown

verifyZeroInteractions(fiefdomObj);}
}