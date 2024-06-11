package src.test.util; // Assuming a src.test package exists for testing purposes

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
 public void setUp() {
 fiefdomMock = Mockito.mock(Fiefdom.class);
 vassalRebellion = new VassalRebellion();
 }

 @Test
 public void testPerform_LoyalVassals_RebellionConditionMet() {
 // Given: A loyal fiefdom with vassals and castles
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(fiefdomMock.getValue()).thenReturn(2);
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(2);

 // When: The vassal rebellion is performed
 vassalRebellion.perform(Arrays.asList(fiefdomMock.getName()));

 // Then: The fiefdom is set to rebellious and kingdom is set to false
 verify(fiefdomMock).setRebelliousVassals(2, true);
 verify(fiefdomMock).setKingdom(false);
 }

 @Test
 public void testPerform_LoyalVassals_RebellionConditionNotMet() {
 // Given: A loyal fiefdom with no vassals and no castles
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);
 when(fiefdomMock.getValue()).thenReturn(1);
 when(fiefdomMock.getPersonalCastles()).thenReturn(0);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(0);

 // When: The vassal rebellion is performed
 vassalRebellion.perform(Arrays.asList(fiefdomMock.getName()));

 // Then: A roll event is triggered
 verify(fiefdomMock).setRebelliousVassals(0, true);
 verify(fiefdomMock).setKingdom(false);
 verify(fiefdomMock, times(0)).setRebelliousVassals(anyInt(), anyBoolean());
 }

 @Test
 public void testPerform_DisloyalFiefdom() {
 // Given: A disloyal fiefdom with no vassals and no castles
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);
 when(fiefdomMock.getValue()).thenReturn(1);
 when(fiefdomMock.getPersonalCastles()).thenReturn(0);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(0);
 when(fiefdomMock.isKingdom()).thenReturn(false);

 // When: The vassal rebellion is performed
 vassalRebellion.perform(Arrays.asList(fiefdomMock.getName()));

 // Then: Nothing happens
 verifyNoMoreInteractions(fiefdomMock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_NullCollection() {
 vassalRebellion.perform(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_NullFiefdomName() {
 vassalRebellion.perform(null);
 }
}