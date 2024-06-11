package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import src.Actions;
import src.Fiefdom;
import src.VassalRebellion;

public class VassalRebellionTest {

 private Fiefdom fiefdomMock;
 private List<String> fiefdomNames;
 private Map<String, Fiefdom> map;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 fiefdomMock = Mockito.mock(Fiefdom.class);
 fiefdomNames = new ArrayList<>();
 fiefdomNames.add("TestFiefdom1");
 map = new HashMap<>();
 map.put("TestFiefdom1", fiefdomMock);
 vassalRebellion = new VassalRebellion() {
 @Override
 public void perform(Collection<String> col) {
 super.perform(col);
 }
 };
 vassalRebellion.setMap(map);
 }

 @Test
 public void testPerformWithLoyalVassalsAndCastles() {
 // Given: Fiefdom with loyal vassals and castles
 when(fiefdomMock.getName()).thenReturn("TestFiefdom1");
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(2);
 when(fiefdomMock.getLoyalVassals()).thenReturn(new ArrayList<>(Collections.singletonList("TestVassal1")));
 when(fiefdomMock.getValue()).thenReturn(10);

 // When: Vassal rebellion is triggered
 vassalRebellion.perform(fiefdomNames);

 // Then: Fiefdom is set as rebellious and vassals are increased
 verify(fiefdomMock).setRebelliousVassals(10, true);
 // Add assertions here for other expected changes in fiefdomMock

 // And: No roll event is triggered since loyal vassals are present
 verify(fiefdomMock, never()).newRollEvent();
 }

 @Test
 public void testPerformWithNoLoyalVassals() {
 // Given: Fiefdom with no loyal vassals
 when(fiefdomMock.getName()).thenReturn("TestFiefdom1");
 when(fiefdomMock.getPersonalCastles()).thenReturn(0);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(0);
 when(fiefdomMock.getLoyalVassals()).thenReturn(Collections.emptyList());

 // When: Vassal rebellion is triggered
 vassalRebellion.perform(fiefdomNames);

 // Then: Roll event is triggered since no loyal vassals are present
 verify(fiefdomMock).newRollEvent();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPerformWithNullCollection() {
 vassalRebellion.perform(null);
 }
}