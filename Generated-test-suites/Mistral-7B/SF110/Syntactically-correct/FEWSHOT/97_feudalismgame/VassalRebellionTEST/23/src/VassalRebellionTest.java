package src;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;

public class VassalRebellionTest {

 private Fiefdom fiefdomMock;
 private List loyalVassalsMock;
 private List rebelliousVassalsMock;

 @Before
 public void setup() {
 fiefdomMock = Mockito.mock(Fiefdom.class);
 loyalVassalsMock = new ArrayList<>();
 rebelliousVassalsMock = new ArrayList<>();
 when(fiefdomMock.getLoyalVassals()).thenReturn(loyalVassalsMock);
 when(fiefdomMock.getValue()).thenReturn(10);
 when(fiefdomMock.getName()).thenReturn("TestFiefdom");
 when(fiefdomMock.getRebelliousVassals()).thenReturn(rebelliousVassalsMock);
 when(fiefdomMock.getPersonalCastles()).thenReturn(5);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(3);
 }

 @Test
 public void testPerform_LoyalVassalsPresent_LoyaltyCheckPassed_RebellionOccurs() {
 VassalRebellion vassalRebellion = new VassalRebellion();
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));

 assertEquals(13, rebelliousVassalsMock.size());
 verify(fiefdomMock).setRebelliousVassals(anyInt(), true);
 verify(fiefdomMock).setKingdom(false);
 }

 @Test
 public void testPerform_LoyalVassalsPresent_LoyaltyCheckFailed_MaxRebelliousVassalsReached() {
 loyalVassalsMock.add(new Vassal("TestVassal1"));
 loyalVassalsMock.add(new Vassal("TestVassal2"));

 VassalRebellion vassalRebellion = new VassalRebellion();
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));

 assertEquals(13, rebelliousVassalsMock.size());
 verify(fiefdomMock, times(2)).setRebelliousVassals(anyInt(), true);
 }

 @Test
 public void testPerform_LoyalVassalsAbsent_RollEventOccurs() {
 loyalVassalsMock.clear();

 VassalRebellion vassalRebellion = new VassalRebellion();
 vassalRebellion.perform(Arrays.asList("TestFiefdom"));

 verify(fiefdomMock).setRebelliousVassals(anyInt(), true);
 verify(fiefdomMock).setKingdom(false);
 verify(new RollEvent(eq("TestFiefdom")).roll(), times(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_NullFiefdomName() {
 VassalRebbellion vassalRebellion = new VassalRebbellion();
 vassalRebellion.perform(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_EmptyCollection() {
 VassalRebbellion vassalRebellion = new VassalRebbellion();
 vassalRebellion.perform(Collections.emptyList());
 }
}