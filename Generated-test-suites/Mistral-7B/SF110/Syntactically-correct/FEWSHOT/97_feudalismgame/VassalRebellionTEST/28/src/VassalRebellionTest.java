package src;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;

public class VassalRebellionTest {

 private Fiefdom fiefdomMock;
 private Map mapMock;
 private Collection<String> collectionMock;

 @Before
 public void setup() {
 fiefdomMock = Mockito.mock(Fiefdom.class);
 mapMock = Mockito.mock(Map.class);
 collectionMock = Mockito.mock(Collection.class);
 when(mapMock.get(anyString())).thenReturn(fiefdomMock);
 }

 @Test
 public void testPerformWithLoyalVassalsAndEnoughCastles() {
 // Given
 when(collectionMock.iterator().next()).thenReturn("TestFiefdom");
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(1);
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(fiefdomMock.getValue()).thenReturn(3);

 // When
 VassalRebellion vassalRebellion = new VassalRebellion();
 vassalRebellion.perform(collectionMock);

 // Then
 verify(fiefdomMock).setRebelliousVassals(3, true);
 verify(fiefdomMock).setKingdom(false);
 }

 @Test
 public void testPerformWithLoyalVassalsButNotEnoughCastles() {
 // Given
 when(collectionMock.iterator().next()).thenReturn("TestFiefdom");
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(0);
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(fiefdomMock.getValue()).thenReturn(3);

 // When
 VassalRebellion vassalRebellion = new VassalRebellion();
 vassalRebellion.perform(collectionMock);

 // Then
 verify(fiefdomMock).setRebelliousVassals(5, true);
 }

 @Test
 public void testPerformWithoutLoyalVassals() {
 // Given
 when(collectionMock.iterator().next()).thenReturn("TestFiefdom");
 when(fiefdomMock.getPersonalCastles()).thenReturn(0);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(0);
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);

 // When
 VassalRebellion vassalRebellion = new VassalRebellion();
 vassalRebellion.perform(collectionMock);

 // Then
 verify(fiefdomMock).setRebelliousVassals(0, true);
 verify(fiefdomMock).setKingdom(false);
 verify(fiefdomMock).new RollEvent(anyString());
 }

 @Test(expected = NullPointerException.class)
 public void testPerformWithNullCollection() {
 // Given

 // When
 VassalRebellion vassalRebellion = new VassalRebellion();
 vassalRebellion.perform(null);

 // Then
 // Expected exception
 }
}