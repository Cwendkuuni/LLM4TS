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
 public void testPerform_NoLoyalVassals() {
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);

 vassalRebellion.perform(Mockito.mock(Collection.class));

 verify(fiefdomMock).setRebelliousVassals(0, true);
 verify(fiefdomMock).setKingdom(false);
 verify(fiefdomMock, never()).setRebelliousVassals(anyInt(), anyBoolean());
 }

 @Test
 public void testPerform_LoyalVassalsButNotEnoughToRebel() {
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
 when(fiefdomMock.getValue()).thenReturn(3);

 vassalRebellion.perform(Mockito.mock(Collection.class));

 verify(fiefdomMock, never()).setRebelliousVassals(anyInt(), anyBoolean());
 }

 @Test
 public void testPerform_LoyalVassalsEnoughToRebel() {
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(10);
 when(fiefdomMock.getValue()).thenReturn(5);

 vassalRebellion.perform(Mockito.mock(Collection.class));

 verify(fiefdomMock).setRebelliousVassals(5, true);
 verify(fiefdomMock).setKingdom(false);
 }

 @Test
 public void testPerform_LoyalVassalsAndCastlesToRebel() {
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(15);
 when(fiefdomMock.getPersonalCastles()).thenReturn(2);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(3);

 vassalRebellion.perform(Mockito.mock(Collection.class));

 int expectedRebelliousVassals = 15 - (2 * 10 + 3 * 25);
 verify(fiefdomMock).setRebelliousVassals(expectedRebelliousVassals, true);
 verbify(fiefdomMock).setKingdom(false);
 }
}