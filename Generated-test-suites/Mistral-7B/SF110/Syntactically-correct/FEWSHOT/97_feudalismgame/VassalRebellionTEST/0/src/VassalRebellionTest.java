package src;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class VassalRebellionTest {

 private Fiefdom fiefdomMock;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 this.fiefdomMock = mock(Fiefdom.class);
 this.vassalRebellion = new VassalRebellion();
 this.vassalRebellion.setFiefdomObj(this.fiefdomMock);
 }

 @Test
 public void testPerformWithLoyalVassalsAndCastles() {
 // Given: Fiefdom with loyal vassals and castles
 when(this.fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(this.fiefdomMock.getValue()).thenReturn(3);
 when(this.fiefdomMock.getPersonalCastles()).thenReturn(2);
 when(this.fiefdomMock.getFiefdomCastles()).thenReturn(1);

 // When: VassalRebellion is performed
 this.vassalRebellion.perform(Arrays.asList("fiefdomName"));

 // Then: Rebellion is triggered and Fiefdom is set to rebellious
 verify(this.fiefdomMock).setRebelliousVassals(3, true);
 verify(this.fiefdomMock).setKingdom(false);

 // Additional checking for remaining vassals and castles
 int remainingVassals = this.fiefdomMock.getLoyalVassals().size() - 3;
 int remainingCastles = 2;
 if (remainingVassals > 0 && (2 > 0 || 1 > 0)) {
 int expectedRemainingVassals = remainingVassals - (2 * 10 + 1 * 25);
 assertEquals(expectedRemainingVassals, remainingVassals);
 verify(this.fiefdomMock).setRebelliousVassals(remainingVassals, true);
 } else {
 verifyNoMoreInteractions(this.fiefdomMock);
 }
 }

 @Test
 public void testPerformWithNoLoyalVassals() {
 // Given: Fiefdom with no loyal vassals
 when(this.fiefdomMock.getLoyalVassals().size()).thenReturn(0);

 // When: VassalRebellion is performed
 this.vassalRebellion.perform(Arrays.asList("fiefdomName"));

 // Then: RollEvent is triggered
 verify(this.fiefdomMock).setRebelliousVassals(0, true);
 verify(this.fiefdomMock).setKingdom(false);
 verify(this).new RollEvent(this.fiefdomMock.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithNullFiefdomObj() {
 this.vassalRebellion.setFiefdomObj(null);
 this.vassalRebellion.perform(Collections.emptyList());
 }
}