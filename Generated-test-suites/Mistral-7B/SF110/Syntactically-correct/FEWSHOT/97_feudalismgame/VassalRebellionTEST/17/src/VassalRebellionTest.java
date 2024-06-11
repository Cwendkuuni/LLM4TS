package src.test.vassalrebellion;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import src.Fiefdom;
import src.Actions;
import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdom fiefdomMock;
 private VassalRebellion vassalRebellion;

 @Before
 public void setUp() {
 fiefdomMock = Mockito.mock(Fiefdom.class);
 vassalRebellion = new VassalRebellion();
 Map<String, Fiefdom> map = new HashMap<>();
 map.put("testFiefdom", fiefdomMock);
 vassalRebellion.setMap(map);
 }

 @Test
 public void testPerform_LoyalVassals_NotRebelling() {
 // Given
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(2);
 when(fiefdomMock.getLoyalVassals()).thenReturn(Arrays.asList("vassal1", "vassal2"));
 when(fiefdomMock.getValue()).thenReturn(5);

 // When
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Then
 verify(fiefdomMock, times(1)).setRebelliousVassals(5, true);
 verify(fiefdomMock, times(1)).setKingdom(false);
 }

 @Test
 public void testPerform_LoyalVassals_Rebelling() {
 // Given
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(2);
 when(fiefdomMock.getLoyalVassals()).thenReturn(Arrays.asList("vassal1", "vassal2", "vassal3"));
 when(fiefdomMock.getValue()).thenReturn(3);

 // When
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Then
 verify(fiefdomMock, times(1)).setRebelliousVassals(3, true);
 verify(fiefdomMock, times(1)).setKingdom(false);
 }

 @Test
 public void testPerform_NoLoyalVassals() {
 // Given
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(2);
 when(fiefdomMock.getLoyalVassals()).thenReturn(Collections.emptyList());

 // When
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Then
 verify(fiefdomMock, times(1)).setRebelliousVassals(fiefdomMock.getLoyalVassals().size(), true);
 verify(fiefdomMock, times(1)).setKingdom(false);
 verify(fiefdomMock, times(1)).new RollEvent(eq("testFiefdom"));
 }

 @Test
 public void testPerform_LoyalVassals_RebellingWithCastles() {
 // Given
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(2);
 when(fiefdomMock.getLoyalVassals()).thenReturn(Arrays.asList("vassal1", "vassal2", "vassal3"));
 when(fiefdomMock.getValue()).thenReturn(6);

 // When
 vassalRebellion.perform(Arrays.asList("testFiefdom"));

 // Then
 int expectedSize = fiefdomMock.getLoyalVassals().size() - (1 * 10 + 2 * 25);
 verify(fiefdomMock, times(1)).setRebelliousVassals(expectedSize, true);
 }
}