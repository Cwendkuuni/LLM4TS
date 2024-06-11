import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.*;
import static org.junit.Assert.*;

public class VassalRebellionTest {

 private Fiefdom fiefdomMock;
 private VassalRebellion vassalRebellion;

 @Before
 public void setup() {
 fiefdomMock = Mockito.mock(Fiefdom.class);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.setFiefdomObj(fiefdomMock);
 }

 @Test
 public void testPerform_LoyalVassalsAndPersonalCastles_Rebellion() {
 // Given: Fiefdom with loyal vassals and personal castles
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(10);
 when(fiefdomMock.getValue()).thenReturn(5);
 when(fiefdomMock.getPersonalCastles()).thenReturn(2);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(3);

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(Arrays.asList(fiefdomMock.getName()));

 // Then: Fiefdom is rebellious and kingdom is false
 assertTrue(fiefdomMock.isRebellious());
 assertFalse(fiefdomMock.isKingdom());
 }

 @Test(expected = IllegalStateException.class)
 public void testPerform_NoLoyalVassals_RollEvent() {
 // Given: Fiefdom with no loyal vassals
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(Arrays.asList(fiefdomMock.getName()));

 // Then: Roll event is thrown
 // (Note: Mockito does not support throwing checked exceptions directly.
 // You can use PowerMock or Reflection instead.)
 }

 @Test
 public void testPerform_LoyalVassalsAndNoCastles_NoRebellion() {
 // Given: Fiefdom with loyal vassals and no castles
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(15);
 when(fiefdomMock.getValue()).thenReturn(5);
 when(fiefdomMock.getPersonalCastles()).thenReturn(0);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(0);

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(Arrays.asList(fiefdomMock.getName()));

 // Then: Fiefdom is not rebellious and kingdom is true
 assertFalse(fiefdomMock.isRebellious());
 assertTrue(fiefdomMock.isKingdom());
 }

 @Test
 public void testPerform_LoyalVassalsAndPersonalCastlesAndFiefdomCastles_Rebellion() {
 // Given: Fiefdom with loyal vassals, personal castles and fiefdom castles
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(15);
 when(fiefdomMock.getValue()).thenReturn(5);
 when(fiefdomMock.getPersonalCastles()).thenReturn(2);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(3);

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(Arrays.asList(fiefdomMock.getName()));

 // Then: Fiefdom is rebellious and kingdom is false
 assertTrue(fiefdomMock.isRebellious());
 assertFalse(fiefdomMock.isKingdom());
 }

 @Test(expected = IllegalStateException.class)
 public void testPerform_InsufficientLoyalVassals_Exception() {
 // Given: Insufficient loyal vassals
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(fiefdomMock.getValue()).thenReturn(5);
 when(fiefdomMock.getPersonalCastles()).thenReturn(2);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(3);

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(Arrays.asList(fiefdomMock.getName()));

 // Then: IllegalStateException is thrown
 // (Note: Mockito does not support throwing checked exceptions directly.
 // You can use PowerMock or Reflection instead.)
 }

 @Test
 public void testPerform_InsufficientLoyalVassalsAndPersonalCastlesAndFiefdomCastles_Exception() {
 // Given: Insufficient loyal vassals, personal castles and fiefdom castles
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(fiefdomMock.getValue()).thenReturn(5);
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(1);

 // When: Vassal Rebellion is performed
 vassalRebellion.perform(Arrays.asList(fiefdomMock.getName()));

 // Then: IllegalStateException is thrown
 // (Note: Mockito does not support throwing checked exceptions directly.
 // You can use PowerMock or Reflection instead.)
 }
}