package src;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class VassalRebellionTest {

 private Fiefdom fiefdom;
 private VassalRebellion rebellion;
 private List loyalVassals;
 private int value;
 private int personalCastles;
 private int fiefdomCastles;

 @Before
 public void setUp() {
  MockitoAnnotations.initMocks(this);
  fiefdom = mock(Fiefdom.class);
  rebellion = new VassalRebellion();
  loyalVassals = new ArrayList<>();
  value = 5;
  personalCastles = 2;
  fiefdomCastles = 3;
  when(fiefdom.getName()).thenReturn("testFiefdom");
  when(fiefdom.getLoyalVassals()).thenReturn(loyalVassals);
  when(fiefdom.getPersonalCastles()).thenReturn(personalCastles);
  when(fiefdom.getFiefdomCastles()).thenReturn(fiefdomCastles);
  when(fiefdom.getLoyalVassals().size()).thenReturn(value);
  when(fiefdom.getValue()).thenReturn(value);
  rebellion.setFiefdomObj(fiefdom);
 }

 @Test
 public void testPerformWithLoyalVassalsAndEnoughLoyalty() {
  loyalVassals.add(new Vassal("testVassal1"));
  loyalVassals.add(new Vassal("testVassal2"));
  rebellion.perform(Collections.singletonList("testFiefdom"));
  verify(fiefdom, times(1)).setRebelliousVassals(value, true);
  verify(fiefdom, times(1)).setKingdom(false);
  if (personalCastles > 0 || fiefdomCastles > 0) {
   int remainingLoyalVassals = value - (personalCastles * 10 + fiefdomCastles * 25);
   verify(fiefdom, times(remainingLoyalVassals > 0 ? 1 : 0)).setRebelliousVassals(remainingLoyalVassals, true);
  }
  else {
   verifyNoMoreInteractions(fiefdom);
  }
 }

 @Test
 public void testPerformWithLoyalVassalsButInsufficientLoyalty() {
  loyalVassals.add(new Vassal("testVassal1"));
  loyalVassals.add(new Vassal("testVassal2"));
  value -= 2;
  rebellion.perform(Collections.singletonList("testFiefdom"));
  verify(fiefdom, times(1)).setRebelliousVassals(value, true);
  verify(fiefdom, times(1)).setRebelliousVassals(loyalVassals.size(), true);
  if (personalCastles > 0 || fiefdomCastles > 0) {
   int remainingLoyalVassals = value - (personalCastles * 10 + fiefdomCastles * 25);
   verify(fiefdom, times(remainingLoyalVassals > 0 ? 0 : 1)).setRebelliousVassals(remainingLoyalVassals, true);
  }
  else {
   verifyNoMoreInteractions(fiefdom);
  }
 }

 @Test
 public void testPerformWithoutLoyalVassals() {
  rebellion.perform(Collections.singletonList("testFiefdom"));
  verify(fiefdom, times(1)).new RollEvent(fiefdom.getName());
  verifyNoMoreInteractions(fiefdom);
 }
}