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
 public void setup() {
 fiefdomMock = Mockito.mock(Fiefdom.class);
 vassalRebellion = new VassalRebellion();
 vassalRebellion.fiefdomObj = fiefdomMock;
 }

 @Test
 public void testPerform_LoyalVassals_NoRebellion() {
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);
 vassalRebellion.perform(Collections.singletonList("fiefdomName"));
 verifyNoInteractions(fiefdomMock);
 }

 @Test
 public void testPerform_LoyalVassals_Rebellion_NotEnoughLoyalVassals() {
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(1);
 when(fiefdomMock.getValue()).thenReturn(1);
 vassalRebellion.perform(Collections.singletonList("fiefdomName"));
 verify(fiefdomMock, times(1)).setRebelliousVassals(1, true);
 }

 @Test
 public void testPerform_LoyalVassals_Rebellion_EnoughLoyalVassals() {
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(fiefdomMock.getValue()).thenReturn(3);
 vassalRebellion.perform(Collections.singletonList("fiefdomName"));
 verify(fiefdomMock, times(1)).setRebelliousVassals(2, true);
 }

 @Test
 public void testPerform_LoyalVassals_Rebellion_Castles() {
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(fiefdomMock.getValue()).thenReturn(3);
 when(fiefdomMock.getPersonalCastles()).thenReturn(1);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(2);
 vassalRebellion.perform(Collections.singletonList("fiefdomName"));
 verify(fiefdomMock, times(1)).setRebelliousVassals(3, true);
 }

 @Test
 public void testPerform_NoLoyalVassals_RollEvent() {
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);
 vassalRebellion.perform(Collections.singletonList("fiefdomName"));
 verify(fiefdomMock, times(1)).getName();
 new RollEvent(fiefdomMock.getName());
 }
 }