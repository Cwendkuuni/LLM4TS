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
 public void testPerformWithLoyalVassalsAndEnoughCastles() {
 // Arrange
 when(fiefdomMock.getName()).thenReturn("TestFiefdom");
 when(fiefdomMock.getPersonalCastles()).thenReturn(2);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(3);
 when(fiefdomMock.getLoyalVassals()).thenReturn(new ArrayList<>());
 when(fiefdomMock.getValue()).thenReturn(10);
 when(Map.map.get("TestFiefdom")).thenReturn(fiefdomMock);

 // Act
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

 // Assert
 verify(fiefdomMock, times(1)).setRebelliousVassals(10, true);
 verify(fiefdomMock, times(1)).setKingdom(false);
 }

 @Test
 public void testPerformWithLoyalVassalsButNotEnoughCastles() {
 // Arrange
 when(fiefdomMock.getName()).thenReturn("TestFiefdom");
 when(fiefdomMock.getPersonalCastles()).thenReturn(2);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(2);
 when(fiefdomMock.getLoyalVassals()).thenReturn(new ArrayList<>());
 when(fiefdomMock.getValue()).thenReturn(11);
 when(Map.map.get("TestFiefdom")).thenReturn(fiefdomMock);

 // Act
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

 // Assert
 verify(fiefdomMock, times(1)).setRebelliousVassals(11, true);
 }

 @Test
 public void testPerformWithoutLoyalVassals() {
 // Arrange
 when(fiefdomMock.getName()).thenReturn("TestFiefdom");
 when(fiefdomMock.getPersonalCastles()).thenReturn(0);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(0);
 when(fiefdomMock.getLoyalVassals()).thenReturn(Collections.emptyList());
 when(Map.map.get("TestFiefdom")).thenReturn(fiefdomMock);

 // Act
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

 // Assert
 verify(fiefdomMock, times(1)).new RollEvent(eq("TestFiefdom"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPerformWithEmptyCollection() {
 // Act
 vassalRebbellion.perform(Collections.emptyList());

 // Assert: expected UnsupportedOperationException
 }
}