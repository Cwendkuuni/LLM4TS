package src.test.java;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdoms fiefdomMock;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 fiefdomMock = mock(Fiefdoms.class);
 vassalRebellion = new VassalRebellion();
 }

 @Test
 public void testPerform_FiefdomHasLoyalVassalsAndValuesToRebel() {
 // Given
 when(fiefdomMock.getPersonalCastles()).thenReturn(3);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(5);
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(10);
 when(fiefdomMock.getValue()).thenReturn(2);
 when(Map.map.get(anyString())).thenReturn(fiefdomMock);

 // When
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Then
 verify(fiefdomMock).setRebelliousVassals(anyInt(), eq(true));
 verify(fiefdomMock).setKingdom(eq(false));
 }

 @Test(expected = IllegalStateException.class)
 public void testPerform_FiefdomHasNoLoyalVassals() {
 // Given
 when(fiefdomMock.getPersonalCastles()).thenReturn(0);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(0);
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);
 when(Map.map.get(anyString())).thenReturn(fiefdomMock);

 // When
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Then
 // An exception should be thrown
 }

 @Test(expected = IllegalStateException.class)
 public void testPerform_PersonalCastlesAndFiefdomCastlesGreaterThanAvailableLoyalVassals() {
 // Given
 when(fiefdomMock.getPersonalCastles()).thenReturn(5);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(10);
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(Map.map.get(anyString())).thenReturn(fiefdomMock);

 // When
 vassalRebbellion.perform(Arrays.asList("testFiefdom"));

 // Then
 // An exception should be thrown
 }

 @Test
 public void testPerform_FiefdomHasLoyalVassalsButNotEnoughToRebel() {
 // Given
 when(fiefdomMock.getPersonalCastles()).thenReturn(0);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(0);
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(Map.map.get(anyString())).thenReturn(fiefdomMock);

 // When
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Then
 // No rebellion should occur
 }

 @Test
 public void testPerform_FiefdomHasNoPersonalCastlesNorFiefdomCastles() {
 // Given
 when(fiefdomMock.getPersonalCastles()).thenReturn(0);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(0);
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(Map.map.get(anyString())).thenReturn(fiefdomMock);

 // When
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Then
 // No RollEvent should be thrown
 }
}