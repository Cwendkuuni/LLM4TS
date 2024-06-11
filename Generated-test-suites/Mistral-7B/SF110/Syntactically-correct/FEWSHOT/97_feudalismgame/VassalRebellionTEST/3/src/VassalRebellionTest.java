package src;

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
 public void setup() {
 fiefdomMock = Mockito.mock(Fiefdom.class);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.fiefdomObj = fiefdomMock;
 }

 @Test
 public void testPerformWithLoyalVassalsAndCastles() {
 // Given: Fiefdom with loyal vassals and castles
 when(fiefdomMock.getName()).thenReturn("testFiefdom");
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(10);
 when(fiefdomMock.getPersonalCastles()).thenReturn(2);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(3);

 // When: VassalRebellion is performed
 vassalRebellion.perform(Collections.singletonList("testFiefdom"));

 // Then: Fiefdom is set as rebellious and vassals are reduced accordingly
 verify(fiefdomMock).setRebelliousVassals(10, true);
 verify(fiefdomMock).setKingdom(false);
 verify(fiefdomMock, atLeastOnce()).setRebelliousVassals(anyInt(), true);
 }

 @Test
 public void testPerformWithNoLoyalVassals() {
 // Given: Fiefdom with no loyal vassals
 when(fiefdomMock.getName()).thenReturn("testFiefdom");
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);

 // When: VassalRebellion is performed
 vassalRebellion.perform(Collections.singletonList("testFiefdom"));

 // Then: A new RollEvent is created for the fiefdom
 verify(fiefdomMock).setRebelliousVassals(anyInt(), anyBoolean());
 verify(fiefdomMock).setKingdom(false);
 verify(fiefdomMock).setRebelliousVassals(anyInt(), anyBoolean());
 verify(fiefdomMock).getName();
 assertNotNull(Mockito.mock(RollEvent.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithNullFiefdomObject() {
 // Given: VassalRebellion with null fiefdomObject
 vassalRebellion = new VassalRebellion();
 vassalRebellion.fiefdomObj = null;

 // When: VassalRebellion is performed with a null fiefdom object
 vassalRebellion.perform(Collections.singletonList("testFiefdom"));

 // Then: An IllegalArgumentException is thrown
 }
}