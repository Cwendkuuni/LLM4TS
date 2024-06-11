package src.test.vassalrebellion;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import src.Fiefdom;
import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdom fiefdomMock;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 fiefdomMock = Mockito.mock(Fiefdom.class);
 vassalRebellion = new VassalRebellion();
 }

 @Test
 public void testPerformWithLoyalVassals() {
 // Given: Fiefdom with loyal vassals
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(fiefdomMock.getValue()).thenReturn(3);

 // When: VassalRebellion is performed
 vassalRebellion.perform(Arrays.asList(fiefdomMock.getName()));

 // Then: Fiefdom is set as rebellious
 verify(fiefdomMock).setRebelliousVassals(3, true);
 // And: Kingdom is set to false
 verify(fiefdomMock).setKingdom(false);
 // And: If personal castles and fiefdom castles are greater than 0, rebellious vassals are added
 if (fiefdomMock.getPersonalCastles() > 0 && fiefdomMock.getFiefdomCastles() > 0) {
 int expectedRebelliousVassals = 5 - (fiefdomMock.getPersonalCastles() * 10 + fiefdomMock.getFiefdomCastles() * 25);
 verify(fiefdomMock).setRebelliousVassals(expectedRebellions.intValue(), true);
 }
 }

 @Test
 public void testPerformWithNoLoyalVassals() {
 // Given: Fiefdom with no loyal vassals
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);

 // When: VassalRebellion is performed
 vassalRebellion.perform(Arrays.asList(fiefdomMock.getName()));

 // Then: A new RollEvent is created for the fiefdom
 verify(fiefdomMock, times(1)).setRebelliousVassals(anyInt(), anyBoolean());
 verify(fiefdomMock, times(1)).setKingdom(anyBoolean());
 verify(fiefdomMock, times(1)).getName();
 verify(fiefdomMock, times(1)).new RollEvent(eq(fiefdomMock.getName()));
 }

 @Test(expected = NullPointerException.class)
 public void testPerformWithNullFiefdom() {
 vassalRebellion.perform(null);
 }
}