import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sar.Fiefdoms;
import sar.actions.Actions;
import sar.events.RollEvent;
import sar.map.Map;

import java.util.*;

import static org.junit.Assert.*;

public class VassalRebellionTest {

 private Actions actions;
 private Fiefdoms fiefdoms;
 private Map map;
 private VassalRebellion vassalRebellion;

 @Before
 public void setUp() {
 actions = Mockito.spy(new Actions());
 fiefdoms = new Fiefdoms();
 map = new Map();
 map.put("TestFiefdom", fiefdoms);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdoms(fiefdoms);
 vassalRebellion.setActions(actions);
 vassalRebellion.setMap(map);
 }

 @Test
 public void testPerform_ValidFiefdom_RebellionOccurs() {
 // Given
 Fiefdom fiefdom = Mockito.mock(Fiefdom.class);
 when(fiefdom.getName()).thenReturn("TestFiefdom");
 when(fiefdom.getPersonalCastles()).thenReturn(1);
 when(fiefdom.getFiefdomCastles()).thenReturn(1);
 when(fiefdom.getLoyalVassals()).thenReturn(Collections.singletonList(Mockito.mock(Vassal.class)));
 when(fiefdom.getValue()).thenReturn(5);
 map.put("TestFiefdom", fiefdom);
 Collection<String> collection = Arrays.asList("TestFiefdom");

 // When
 vassalRebellion.perform(collection);

 // Then
 verify(fiefdom, times(1)).setRebelliousVassals(5, true);
 verify(fiefdom, times(1)).setKingdom(false);
 assertEquals(6, fiefdom.getRebelliousVassals().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_EmptyInput() {
 // Given
 Collection<String> collection = Collections.emptyList();

 // When
 vassalRebellion.perform(collection);

 // Then
 fail("Perform method should throw IllegalArgumentException for empty input.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_InvalidFiefdom() {
 // Given
 String invalidFiefdom = "NonExistentFiefdom";
 Collection<String> collection = Arrays.asList(invalidFiefdom);

 // When
 vassalRebellion.perform(collection);

 // Then
 RollEvent rollEvent = Mockito.spy(new RollEvent(invalidFiefdom));
 verify(actions, times(1)).perform(rollEvent);
 }

 @Test
 public void testPerform_NoLoyalVassals() {
 // Given
 Fiefdom fiefdom = Mockito.mock(Fiefdom.class);
 when(fiefdom.getName()).thenReturn("TestFiefdom");
 when(fiefdom.getPersonalCastles()).thenReturn(1);
 when(fiefdom.getFiefdomCastles()).thenReturn(1);
 when(fiefdom.getLoyalVassals()).thenReturn(Collections.emptyList());
 map.put("TestFiefdom", fiefdom);
 Collection<String> collection = Arrays.asList("TestFiefdom");

 // When
 vassalRebellion.perform(collection);

 // Then
 verify(actions, times(1)).perform(Mockito.isA(RollEvent.class));
 }

 @Test
 public void testPerform_ValidFiefdom_NoRebellion() {
 // Given
 Fiefdom fiefdom = Mockito.mock(Fiefdom.class);
 when(fiefdom.getName()).thenReturn("TestFiefdom");
 when(fiefdom.getPersonalCastles()).thenReturn(0);
 when(fiefdom.getFiefdomCastles()).thenReturn(0);
 when(fiefdom.getLoyalVassals()).thenReturn(Collections.singletonList(Mockito.mock(Vassal.class)));
 when(fiefdom.getValue()).thenReturn(5);
 map.put("TestFiefdom", fiefdom);
 Collection<String> collection = Arrays.asList("TestFiefdom");

 // When
 vassalRebellion.perform(collection);

 // Then
 verify(fiefdom, never()).setRebelliousVassals(anyInt(), anyBoolean());
 verify(fiefdom, never()).setKingdom(anyBoolean());
 }
}