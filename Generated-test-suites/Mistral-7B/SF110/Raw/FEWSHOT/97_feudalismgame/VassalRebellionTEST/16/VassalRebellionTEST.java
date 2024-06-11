package src.test.unit;

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
 vassalRebellion.setFiefdomObj(fiefdomMock);
 }

 @Test
 public void testPerform_noLoyalVassals_newRollEvent() {
 final String fiefdomName = "testFiefdom";
 doReturn(null).when(fiefdomMock).getLoyalVassals();

 vassalRebellion.perform(Arrays.asList(fiefdomName));

 verify(fiefdomMock).setRebelliousVassals(0, true);
 verify(fiefdomMock).getKingdom();
 verify(fiefdomMock, times(1)).setKingdom(false);
 verify(fiefdomMock).getName();
 assertFalse(vassalRebellion.fiefdomObj.isKingdom());
 }

 @Test
 public void testPerform_loyalVassals_rebel() {
 final int value = 10;
 final int personalCastles = 2;
 final int fiefdomCastles = 3;
 final List loyalVassals = Arrays.asList("vassal1", "vassal2");
 doReturn(loyalVassals).when(fiefdomMock).getLoyalVassals();
 doReturn(value).when(fiefdomMock).getValue();
 doReturn(personalCastles).when(fiefdomMock).getPersonalCastles();
 doReturn(fiefdomCastles).when(fiefdomMock).getFiefdomCastles();

 vassalRebellion.perform(Arrays.asList(fiefdomName));

 assertEquals(value + 10, vassalRebellion.fiefdomObj.getValue());
 assertFalse(vassalRebellion.fiefdomObj.isKingdom());
 }

 @Test(expected = IllegalStateException.class)
 public void testPerform_notEnoughLoyalVassals_throwException() {
 final int value = 10;
 final int loyalVassalsSize = 5;
 final int personalCastles = 2;
 final int fiefdomCastles = 3;
 final List loyalVassals = Arrays.asList("vassal1", "vassal2", "vassal3", "vassal4", "vassal5");
 doReturn(loyalVassals).when(fiefdomMock).getLoyalVassals();
 doReturn(value).when(fiefdomMock).getValue();
 doReturn(personalCastles).when(fiefdomMock).getPersonalCastles();
 doReturn(fiefdomCastles).when(fiefdomMock).getFiefdomCastles();

 vassalRebellion.perform(Arrays.asList(fiefdomName));
 }

 @Test
 public void testPerform_loyalVassals_rebelWithCastles() {
 final int value = 10;
 final int personalCastles = 2;
 final int fiefdomCastles = 3;
 final int loyalVassalsSize = value + personalCastles * 10 + fiefdomCastles * 25;
 final List loyalVassals = Arrays.asList("vassal1", "vassal2", "vassal3");
 doReturn(loyalVassals).when(fiefdomMock).getLoyalVassals();
 doReturn(value).when(fiefdomMock).getValue();
 doReturn(personalCastles).when(fiefdomMock).getPersonalCastles();
 doReturn(fiefdomCastles).when(fiefdomMock).getFiefdomCastles();

 vassalRebellion.perform(Arrays.asList(fiefdomName));

 assertEquals(value - (personalCastles * 10 + fiefdomCastles * 25), vassalRebellion.fiefdomObj.getValue());
 assertFalse(vassalRebellion.fiefdomObj.isKingdom());
 }
}