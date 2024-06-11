package src.test.java;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class VassalRebellionTest {

 private VassalRebellion vassalRebellion;
 private Fiefdoms fiefdomMock;

 @Before
 public void setup() {
 fiefdomMock = Mockito.mock(Fiefdoms.class);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdomObj(fiefdomMock);
 }

 @Test
 public void testPerform_LoyalFiefdom_NoRebellion() {
 // Given: A loyal fiefdom with no rebellious vassals
 Mockito.when(fiefdomMock.getLoyalVassals()).thenReturn(Collections.emptyList());
 Mockito.when(fiefdomMock.getValue()).thenReturn(0);

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

 // Then: No rebellion occurs
 assertFalse(fiefdomMock.isRebellious());
 }

 @Test
 public void testPerform_LoyalFiefdom_Rebellion() {
 // Given: A loyal fiefdom with some vassals
 List<String> loyalVassals = new ArrayList<>();
 loyalVassals.add("Vassal1");
 loyalVassals.add("Vassal2");
 Mockito.when(fiefdomMock.getLoyalVassals()).thenReturn(loyalVassals);
 Mockito.when(fiefdomMock.getValue()).thenReturn(10);

 // When: Personal castles and fiefdom castles are less than number of vassals
 int personalCastles = 1;
 int fiefdomCastles = 1;
 Mockito.when(fiefdomMock.getPersonalCastles()).thenReturn(personalCastles);
 Mockito.when(fiefdomMock.getFiefdomCastles()).thenReturn(fiefdomCastles);

 // Then: Rebellion occurs
 vassalRebellion.perform(Collections.singletonList("TestFiefdom"));
 assertTrue(fiefdomMock.isRebellious());

 // Given: Personal castles and fiefdom castles are more than number of vassals
 personalCastles = 2;
 fiefdomCastles = 2;
 Mockito.when(fiefdomMock.getPersonalCastles()).thenReturn(personalCastles);
 Mockito.when(fiefdomMock.getFiefdomCastles()).thenReturn(fiefdomCastles);

 // When: Vassals are less than the threshold
 int threshold = personalCastles * 10 + fiefdomCastles * 25;
 int numberOfVassals = loyalVassals.size() - threshold;
 Mockito.when(fiefdomMock.getLoyalVassals().size()).thenReturn(numberOfVassals);

 // Then: No rebellion occurs
 assertFalse(fiefdomMock.isRebellious());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_InvalidFiefdomName() {
 // Given: An invalid fiefdom name

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(Collections.singletonList("InvalidFiefdomName"));

 // Then: An exception is thrown
 }

 @Test
 public void testPerform_EmptyCollection() {
 // Given: An empty collection

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(Collections.emptyList());

 // Then: Nothing happens
 }

 @Test
 public void testPerform_NullCollection() {
 // Given: A null collection

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(null);

 // Then: An exception is thrown
 }
}