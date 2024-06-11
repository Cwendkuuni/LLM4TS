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
 public void setUp() {
 fiefdomMock = Mockito.mock(Fiefdom.class);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.fiefdomObj = fiefdomMock;
 }

 @Test
 public void testPerform_LoyalVassalsAndEnoughCastles_SetsRebelliousVassalsAndKingdomToFalse() {
 // Given
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(fiefdomMock.getValue()).thenReturn(3);
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(2);

 // When
 vassalRebellion.perform( Arrays.asList("testFiefdom") );

 // Then
 verify(fiefdomMock).setRebelliousVassals(3, true);
 verify(fiefdomMock).setRebelliousVassals(2, true);
 verify(fiefdomMock).setKingdom(false);
 }

 @Test
 public void testPerform_LoyalVassalsAndNotEnoughCastles_DoesNotSetRebelliousVassals() {
 // Given
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(fiefdomMock.getValue()).thenReturn(3);
 when(fiefdomMock.getPersonalCastles()).thenReturn(0);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(0);

 // When
 vassalRebellion.perform( Arrays.asList("testFiefdom") );

 // Then
 verify(fiefdomMock, never()).setRebelliousVassals(anyInt(), anyBoolean());
 }

 @Test
 public void testPerform_NoLoyalVassals_RollsEvent() {
 // Given
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);

 // When
 vassalRebellion.perform( Arrays.asList("testFiefdom") );

 // Then
 verify(fiefdomMock, never()).setRebelliousVassals(anyInt(), anyBoolean());
 verify(fiefdomMock).getName();
 new VerifyRollEvent();
 }

}

 class VerifyRollEvent {
 @Test
 public void testVerifyRollEvent() {
 // This is a placeholder test case for the RollEvent that is invoked when there are no loyal vassals.
 // Replace this with the actual RollEvent test case when the RollEvent class is available.
 }
}