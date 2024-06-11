package src.test.vassalrebellion;

import org.junit.*;
import org.mockito.*;
import src.*;
import static org.junit.Assert.*;

public class VassalRebellionTest {
private VassalRebellion vassalRebellion;
private Fiefdoms fiefdomObj;

@Before
public void setup() {
fiefdomObj = Mockito.mock(Fiefdoms.class);
vassalRebellion = new VassalRebbellion();
vassalRebellion.setFiefdomObj(fiefdomObj);
}

@Test
public void testPerform() {
// Given
List<String> collection = new ArrayList<>();
when(fiefdomObj.getPersonalCastles()).thenReturn(1);
when(fiefdomObj.getFiefdomCastles()).thenReturn(1);
when(fiefdomObj.getLoyalVassals().size()).thenReturn(5);
when(fiefdomObj.getValue()).thenReturn(3);
when(Map.map.get(collection.get(0))).thenReturn(fiefdomObj);

// When
vassalRebellion.perform(collection);

// Then
verify(fiefdomObj, times(1)).setRebelliousVassals(3, true);
verify(fiefdomObj, times(1)).setKingdom(false);

// Test edge cases
// Test empty collection
collection.clear();
vassalRebellion.perform(collection);
verifyNoMoreInteractions(fiefdomObj);

// Test null collection
collection = null;
vassalRebbellion.perform(collection);
verifyNoMoreInteractions(fiefdomObj);

// Test non-existent fiefdom name
String nonExistentFiefdomName = "NonExistentFiefdom";
when(Map.map.get(nonExistentFiefdomName)).thenReturn(null);
collection.add(nonExistentFiefdomName);
vassalRebbellion.perform(collection);
verifyNoMoreInteractions(fiefdomObj);

// Test negative personal castles
when(fiefdomObj.getPersonalCastles()).thenReturn(-1);
vassalRebbellion.perform(collection);
verifyNoMoreInteractions(fiefdomObj);

// Test fiefdom with no loyal vassals
when(fiefdomObj.getLoyalVassals().size()).thenReturn(0);
vassalRebbellion.perform(collection);
new RollEvent(fiefdomObj.getName());
verify(fiefdomObj, times(1)).setKingdom(false);
}

@Test
public void testGetFiefdomObj() {
// Given
Fiefdoms fiefdom = new Fiefdoms();
VassalRebellion vassalRebellion = new VassalRebbellion();
vassalRebellion.setFiefdomObj(fiefdom);

// When
Fiefdoms expectedFiefdom = fiefdom;
Fiefdoms actualFiefdom = vassalRebbellion.getFiefdomObj();

// Then
assertEquals(expectedFiefdom, actualFiefdom);

// Test edge cases
// Test if getFiefdomObj() can return null
vassalRebellion.setFiefdomObj(null);
assertNull(vassalRebbellion.getFiefdomObj());
}

@Test
public void testGetPersonalCastles() {
// Given
Fiefdoms fiefdom = new Fiefdoms();
fiefdom.setPersonalCastles(1);
VassalRebellion vassalRebellion = new VassalRebbellion();
vassalRebellion.setFiefdomObj(fiefdom);

// When
int expectedCastles = 1;
int actualCastles = vassalRebbellion.getFiefdomObj().getPersonalCastles();

// Then
assertEquals(expectedCastles, actualCastles);

// Test edge cases
// Test if getPersonalCastles() can return a negative number
fiefdom.setPersonalCastles(-1);
int actualNegativeCastles = vassalRebbellion.getFiefdomObj().getPersonalCastles();
assertEquals(-1, actualNegativeCastles);
}
}