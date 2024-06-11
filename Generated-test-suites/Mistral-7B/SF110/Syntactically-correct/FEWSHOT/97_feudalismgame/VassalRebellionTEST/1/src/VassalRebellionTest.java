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
 }

 @Test
 public void testPerform_LoyalVassalsPresent_LoyalVassalsSufficient_CastlesPresent_Rebel() {
 // Given
 when(this.fiefdomMock.getName()).thenReturn("TestFiefdom");
 when(this.fiefdomMock.getLoyalVassals()).thenReturn(Arrays.asList(new Vassal("TestVassal1"), new Vassal("TestVassal2")));
 when(this.fiefdomMock.getValue()).thenReturn(10);
 when(this.fiefdomMock.getPersonalCastles()).thenReturn(2);
 when(this.fiefdomMock.getFiefdomCastles()).thenReturn(3);

 // When
 this.vassalRebellion.perform(Arrays.asList(this.fiefdomMock.getName()));

 // Then
 verify(this.fiefdomMock, times(1)).setRebelliousVassals(10, true);
 verify(this.fiefdomMock, times(1)).setKingdom(false);
 }

 @Test
 public void testPerform_LoyalVassalsPresent_LoyalVassalsInsufficient_Rebel() {
 // Given
 when(this.fiefdomMock.getName()).thenReturn("TestFiefdom");
 when(this.fiefdomMock.getLoyalVassals()).thenReturn(Arrays.asList(new Vassal("TestVassal1")));
 when(this.fiefdomMock.getValue()).thenReturn(5);
 when(this.fiefdomMock.getPersonalCastles()).thenReturn(0);
 when(this.fiefdomMock.getFiefdomCastles()).thenReturn(0);

 // When
 this.vassalRebellion.perform(Arrays.asList(this.fiefdomMock.getName()));

 // Then
 verify(this.fiefdomMock, times(1)).setRebelliousVassals(1, true);
 verify(this.fiefdomMock, times(1)).setKingdom(false);
 }

 @Test
 public void testPerform_LoyalVassalsAbsent_RollEvent() {
 // Given
 when(this.fiefdomMock.getName()).thenReturn("TestFiefdom");
 when(this.fiefdomMock.getLoyalVassals()).thenReturn(Collections.emptyList());

 // When
 this.vassalRebellion.perform(Arrays.asList(this.fiefdomMock.getName()));

 // Then
 verify(this.vassalRebellion, times(1)).new RollEvent(this.fiefdomMock.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_EmptyCollection() {
 this.vassalRebellion.perform(Collections.emptyList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_NullFiefdom() {
 this.vassalRebellion.perform(Arrays.asList(null));
 }
}