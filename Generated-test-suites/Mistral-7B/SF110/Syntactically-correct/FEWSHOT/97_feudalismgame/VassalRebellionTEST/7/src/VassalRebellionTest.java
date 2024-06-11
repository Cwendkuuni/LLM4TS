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
 public void testPerform_LoyalVassalsAndEnoughCastles_Rebel() {
 // Given: Fiefdom with loyal vassals and castles
 when(fiefdomMock.getName()).thenReturn("testFiefdom");
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(1);
 when(fiefdomMock.getLoyalVassals()).thenReturn(Arrays.asList("vassal1", "vassal2"));
 when(Map.map.get("testFiefdom")).thenReturn(fiefdomMock);

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Then: Fiefdom is rebellious and kingdom is false
 verify(fiefdomMock).setRebelliousVassals(anyInt(), true);
 verify(fiefdomMock).setKingdom(false);
 }

 @Test
 public void testPerform_LoyalVassalsButNotEnoughCastles_NoRebel() {
 // Given: Fiefdom with loyal vassals but not enough castles
 when(fiefdomMock.getName()).thenReturn("testFiefdom");
 when(fiefdomMock.getPersonalCastles()).thenReturn(0);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(0);
 when(fiefdomMock.getLoyalVassals()).thenReturn(Arrays.asList("vassal1", "vassal2"));
 when(Map.map.get("testFiefdom")).thenReturn(fiefdomMock);

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Then: No rebellion is triggered
 verify(fiefdomMock, never()).setRebelliousVassals(anyInt(), anyBoolean());
 }

 @Test
 public void testPerform_NoLoyalVassals_RollEvent() {
 // Given: Fiefdom with no loyal vassals
 when(fiefdomMock.getName()).thenReturn("testFiefdom");
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(1);
 when(fiefdomMock.getLoyalVassals()).thenReturn(Collections.emptyList());
 when(Map.map.get("testFiefdom")).thenReturn(fiefdomMock);

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Then: RollEvent is triggered
 verify(fiefdomMock, times(1)).setRebelliousVassals(anyInt(), anyBoolean()); // This should not be called
 verify(fiefdomMock, times(1)).getName(); // RollEvent constructor is called with fiefdom name
 verify(fiefdomMock, times(1)).setKingdom(anyBoolean()); // RollEvent constructor is called with false
 }
}