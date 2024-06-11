package src;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class VassalRebellionTest {

 private Fiefdom fiefdomMock;
 private Map mapMock;
 private Collection collectionMock;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 mapMock = mock(Map.class);
 fiefdomMock = mock(Fiefdom.class);
 collectionMock = mock(Collection.class);
 when(mapMock.get(anyString())).thenReturn(fiefdomMock);

 vassalRebellion = new VassalRebellion() {
 @Override
 Map getMap() {
 return mapMock;
 }
 };
 }

 @Test
 public void testPerformWithLoyalVassalsAndCastles() {
 // Given: Fiefdom with loyal vassals and castles
 when(fiefdomMock.getName()).thenReturn("TestFiefdom");
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(2);
 when(fiefdomMock.getValue()).thenReturn(3);
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(collectionMock);

 // Then: Fiefdom is marked as rebellious and kingdom is set to false
 verify(fiefdomMock).setRebelliousVassals(anyInt(), true);
 verify(fiefdomMock).setKingdom(false);

 // And: Rebellious vassals are calculated based on personal castles and fiefdom castles
 int loyalVassalsBeforeRebellion = fiefdomMock.getLoyalVassals().size();
 int rebelliousVassals = vassalRebellion.getRebelliousVassals();
 int expectedRebelliousVassals = loyalVassalsBeforeRebellion - (1 * 10 + 2 * 25);
 assertEquals(expectedRebelliousVassals, rebelliousVassals);
 }

 @Test
 public void testPerformWithNoLoyalVassals() {
 // Given: Fiefdom with no loyal vassals
 when(fiefdomMock.getName()).thenReturn("TestFiefdom");
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(2);
 when(fiefdomMock.getValue()).thenReturn(3);
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(collectionMock);

 // Then: A roll event is triggered
 verify(fiefdomMock, never()).setRebelliousVassals(anyInt(), anyBoolean());
 verify(fiefdomMock).setKingdom(false);
 verify(fiefdomMock).setRebelliousVassals(anyInt(), anyBoolean());

 // And: A new roll event is triggered
 verify(mapMock).get(eq("TestFiefdom")).getLoyalVassals().isEmpty();
 verify(new RollEvent(eq("TestFiefdom")).isTriggered(), is(true));
 }
}