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
 public void testPerformWithLoyalVassalsAndFewCastles() {
 final String fiefdomName = "TestFiefdom";
 final int personalCastles = 1;
 final int fiefdomCastles = 1;
 final int loyalVassalsSize = 5;
 final int value = 3;

 when(fiefdomMock.getName()).thenReturn(fiefdomName);
 when(fiefdomMock.getPersonalCastles()).thenReturn(personalCastles);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(fiefdomCastles);
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(loyalVassalsSize);
 when(fiefdomMock.getValue()).thenReturn(value);

 vassalRebellion.perform( Arrays.asList(fiefdomName) );

 verify(fiefdomMock).setRebelliousVassals(value, true);
 verify(fiefdomMock).setKingdom(false);
 verifyNoMoreInteractions(fiefdomMock);
 }

 @Test
 public void testPerformWithLoyalVassalsAndManyCastles() {
 final String fiefdomName = "TestFiefdom";
 final int personalCastles = 2;
 final int fiefdomCastles = 3;
 final int loyalVassalsSize = 10;
 final int value = 3;

 when(fiefdomMock.getName()).thenReturn(fiefdomName);
 when(fiefdomMock.getPersonalCastles()).thenReturn(personalCastles);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(fiefdomCastles);
 when(fiefdomMock.getLoyalVassals().size()).thenReturn(loyalVassalsSize);
 when(fiefdomMock.getValue()).thenReturn(value);

 int expectedRebelliousVassalsSize = loyalVassalsSize - personalCastles * 10 - fiefdomCastles * 25;

 vassalRebellion.perform( Arrays.asList(fiefdomName) );

 verify(fiefdomMock).setRebelliousVassals(expectedRebelliousVassalsSize, true);
 verify(fiefdomMock).setKingdom(false);
 verifyNoMoreInteractions(fiefdomMock);
 }

 @Test
 public void testPerformWithoutLoyalVassals() {
 final String fiefdomName = "TestFiefdom";
 final int personalCastles = 1;
 final int fiefdomCastles = 1;

 when(fiefdomMock.getName()).thenReturn(fiefdomName);
 when(fiefdomMock.getPersonalCastles()).thenReturn(personalCastles);
 when(fiefdomMock.getFiefdomCastles()).thenReturn(fiefdomCastles);
 when(fiefdomMock.getLoyalVassals().isEmpty()).thenReturn(true);

 vassalRebellion.perform( Arrays.asList(fiefdomName) );

 verify(fiefdomMock).new RollEvent(fiefdomName.toString());
 verifyNoMoreInteractions(fiefdomMock);
 }
}