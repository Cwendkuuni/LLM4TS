package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class VassalRebellionTest {

 private Fiefdom fiefdomMock;
 private VassalRebellion vassalRebellion;

 @Before
 public void setUp() {
 fiefdomMock = Mockito.mock(Fiefdom.class);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.fiefdomObj = fiefdomMock;
 }

 @Test
 public void testPerform_LoyalVassals_NoRebellion() {
 // Given: Fiefdom with no rebellious vassals and some loyal vassals
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(fiefdomMock.getValue()).thenReturn(3);
 when(fiefdomMock.getPersonalCastles()).thenReturn(2);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(1);

 // When: VassalRebellion is performed
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Then: No vassals should rebel
 verify(fiefdomMock, times(0)).setRebelliousVassals(anyInt(), anyBoolean());

 // And: No roll event should be triggered
 verify(fiefdomMock, times(0)).new RollEvent(anyString());
 }

 @Test
 public void testPerform_LoyalVassals_RebellionPossible() {
 // Given: Fiefdom with some loyal and rebellious vassals, and casting capacity
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(8);
 when(fiefdomMock.getValue()).thenReturn(2);
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(1);

 // When: VassalRebellion is performed
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Then: Some vassals should rebel
 verify(fiefdomMock, times(1)).setRebelliousVassals(anyInt(), anyBoolean());
 // And: Kingdom flag should be set to false
 verify(fiefdomMock, times(1)).setKingdom(false);

 // And: No roll event should be triggered if rebellion is possible
 verify(fiefdomMock, times(0)).new RollEvent(anyString());
 }

 @Test
 public void testPerform_LoyalVassals_RebellionNotPossible() {
 // Given: Fiefdom with some loyal vassals, but rebellion is not possible
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(6);
 when(fiefdomMock.getValue()).thenReturn(5);
 when(fiefdomMock.getPersonalCastles()).thenReturn(2);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(1);

 // When: VassalRebellion is performed
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Then: No vassals should rebel
 verify(fiefdomMock, times(0)).setRebelliousVassals(anyInt(), anyBoolean());

 // And: A roll event should be triggered
 verify(fiefdomMock, times(1)).new RollEvent(anyString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_NoFiefdomName() {
 // Given: No fiefdom name is provided
 vassalRebellion.perform(null);

 // Then: An IllegalArgumentException should be thrown
 }

 @Test
 public void testPerform_InvalidFiefdomName() {
 // Given: An invalid fiefdom name is provided
 when(Map.map.get("invalidName")).thenReturn(null);

 // When: VassalRebellion is performed
 vassalRebellion.perform(Arrays.asList("invalidName"));

 // Then: No action should be taken
 verify(fiefdomMock, times(0)).setRebelliousVassals(anyInt(), anyBoolean());
 verify(fiefdomMock, times(0)).setKingdom(false);
 verify(fiefdomMock, times(0)).new RollEvent(anyString());
 }
}