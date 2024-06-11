package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;

public class PurchaseTest {

 private static final String FIEFDOM_NAME = "testFiefdom";
 private static final String BUYER_NAME = "testBuyer";
 private static final String KNIGHT_NAME = "testKnight";
 private static final int QTY = 5;
 private static final float BUYER_INITIAL_MONEY = 100.0f;
 private static final float KNIGHT_INITIAL_MONEY = 50.0f;
 private static final Fiefdoms testFiefdom = Mockito.mock(Fiefdoms.class);
 private static final Knight testKnight = Mockito.mock(Knight.class);
 private static final CurrentPlayers testCurrentPlayers = Mockito.mock(CurrentPlayers.class);
 private static final Map testMap = Mockito.mock(Map.class);
 private static final Collection<String> testArgs = Arrays.asList("5", BUYER_NAME, FIEFDOM_NAME);

 @Test
 public void testVassal() {
 final Purchase purchase = new Purchase();
 when(testCurrentPlayers.get(BUYER_NAME)).thenReturn(new Knight(BUYER_NAME, "knight", BUYER_INITIAL_Money));
 when(testMap.get(FIEFDOM_NAME)).thenReturn(testFiefdom);
 when(testFiefdom.getOwner()).thenReturn(null);
 when(testFiefdom.getName()).thenReturn(FIEFDOM_NAME);
 when(testFiefdom.getViking().size()).thenReturn(0);
 when(testFiefdom.getMagyar().size()).thenReturn(0);
 when(testFiefdom.getMuslim().size()).thenReturn(0);

 purchase.perform(testArgs);
 verify(testCurrentPlayers, times(1)).get(BUYER_NAME);
 verify(testMap, times(1)).get(FIEFDOM_NAME);
 verify(testFiefdom, times(1)).getName();
 verify(testFiefdom, times(1)).getOwner();
 verifyNoMoreInteractions(testFiefdom);
 verifyNoMoreInteractions(testCurrentPlayers);

 // Check vassals purchase
 assertEquals(5, testCurrentPlayers.get(BUYER_NAME).getTotalVassals());
 assertEquals(5, testFiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalFail() {
 final Purchase purchase = new Purchase();
 when(testCurrentPlayers.get(BUYER_NAME)).thenReturn(new Knight(BUYER_NAME, "knight", BUYER_INITIAL_Money));
 when(testMap.get(FIEFDOM_NAME)).thenReturn(testFiefdom);
 when(testFiefdom.getOwner()).thenReturn(null);
 when(testFiefdom.getName()).thenReturn(FIEFDOM_NAME);
 when(testFiefdom.getViking().size()).thenReturn(0);
 when(testFiefdom.getMagyar().size()).thenReturn(0);
 when(testFiefdom.getMuslim().size()).thenReturn(0);
 when(testCurrentPlayers.get(BUYER_NAME)).thenReturn(new Knight(BUYER_NAME, "knight", BUYER_INITIAL_Money - 5));

 purchase.perform(testArgs);
 verify(testCurrentPlayers, times(1)).get(BUYER_NAME);
 verify(testMap, times(1)).get(FIEFDOM_NAME);
 verify(testFiefdom, times(1)).getName();
 verify(testFiefdom, times(1)).getOwner();
 verifyNoMoreInteractions(testFiefdom);
 verifyNoMoreInteractions(testCurrentPlayers);

 // Check insufficient funds
 assertEquals(0, testCurrentPlayers.get(BUYER_NAME).getTotalVassals());
 assertEquals(0, testFiefdom.getLoyalVassals());
 }

 @Test
 public void testCastleFiefdom() {
 final Purchase purchase = new Purchase();
 when(testCurrentPlayers.get(BUYER_NAME)).thenReturn(new Knight(BUYER_NAME, "knight", BUYER_INITIAL_Money * 2));
 when(testMap.get(FIEFDOM_NAME)).thenReturn(testFiefdom);
 when(testFiefdom.getOwner()).thenReturn(null);
 when(testFiefdom.getName()).thenReturn(FIEFDOM_NAME);
 when(testFiefdom.getViking().size()).thenReturn(0);
 when(testFiefdom.getMagyar().size()).thenReturn(0);
 when(testFiefdom.getMuslim().size()).thenReturn(0);

 purchase.perform(testArgs);
 verify(testCurrentPlayers, times(1)).get(BUYER_NAME);
 verify(testMap, times(1)).get(FIEFDOM_NAME);
 verify(testFiefdom, times(1)).getName();
 verify(testFiefdom, times(1)).getOwner();
 verify(testFiefdom, times(1)).buyFiefdomCastles(QTY);

 // Check castle purchase
 assertEquals(QTY, testFiefdom.getCastleNumber());
 }

 @Test
 public void testCastleFiefdomFail() {
 final Purchase purchase = new Purchase();
 when(testCurrentPlayers.get(BUYER_NAME)).thenReturn(new Knight(BUYER_NAME, "knight", BUYER_INITIAL_Money * 2));
 when(testMap.get(FIEFDOM_NAME)).thenReturn(testFiefdom);
 when(testFiefdom.getOwner()).thenReturn(null);
 when(testFiefdom.getName()).thenReturn(FIEFDOM_NAME);
 when(testFiefdom.getViking().size()).thenReturn(0);
 when(testFiefdom.getMagyar().size()).thenReturn(0);
 when(testFiefdom.getMuslim().size()).thenReturn(0);
 when(testCurrentPlayers.get(BUYER_NAME)).thenReturn(new Knight(BUYER_NAME, "knight", BUYER_INITIAL_Money * 2 - 25 * QTY));

 purchase.perform(testArgs);
 verify(testCurrentPlayers, times(1)).get(BUYER_NAME);
 verify(testMap, times(1)).get(FIEFDOM_NAME);
 verify(testFiefdom, times(1)).getName();
 verify(testFiefdom, times(1)).getOwner();
 verifyNoMoreInteractions(testFiefdom);
 verifyNoMoreInteractions(testCurrentPlayers);

 // Check insufficient funds
 assertEquals(0, testFiefdom.getCastleNumber());
 }

 @Test
 public void testCastlePersonal() {
 final Purchase purchase = new Purchase();
 when(testCurrentPlayers.get(BUYER_NAME)).thenReturn(new Knight(BUYER_NAME, "knight", BUYER_INITIAL_Money * 2));
 when(testMap.get(FIEFDOM_NAME)).thenReturn(testFiefdom);
 when(testCurrentPlayers.get(BUYER_NAME)).thenReturn(new Knight(BUYER_NAME, "knight", BUYER_INITIAL_Money * 2 - 10 * QTY));
 when(testMap.get(FIEFDOM_NAME)).thenReturn(testFiefdom);
 when(testFiefdom.getOwner()).thenReturn(null);
 when(testFiefdom.getName()).thenReturn(FIEFDOM_NAME);
 when(testFiefdom.getViking().size()).thenReturn(0);
 when(testFiefdom.getMagyar().size()).thenReturn(0);
 when(testFiefdom.getMuslim().size()).thenReturn(0);

 purchase.perform(testArgs);
 verify(testCurrentPlayers, times(2)).get(BUYER_NAME);
 verify(testMap, times(1)).get(FIEFDOM_NAME);
 verify(testFiefdom, times(1)).getName();
 verify(testFiefdom, times(1)).getOwner();
 verifyNoMoreInteractions(testFiefdom);
 verifyNoMoreInteractions(testCurrentPlayers);

 // Check insufficient funds for personal castle
 assertEquals(0, testCurrentPlayers.get(BUYER_NAME).getCastles());
 assertEquals(0, testFiefdom.getCastleNumber());
 }

 @Test
 public void testMercenary() {
 final Purchase purchase = new Purchase();
 when(testCurrentPlayers.get(BUYER_NAME)).thenReturn(new Knight(BUYER_NAME, "knight", BUYER_INITIAL_Money));
 when(testCurrentPlayers.get(KNIGHT_NAME)).thenReturn(new Knight(KNIGHT_NAME, "knight", KNIGHT_INITIAL_Money));

 purchase.perform(Arrays.asList("5", BUYER_NAME, KNIGHT_NAME));
 verify(testCurrentPlayers, times(1)).get(BUYER_NAME);
 verify(testCurrentPlayers, times(1)).get(KNIGHT_NAME);
 verify(testCurrentPlayers.get(BUYER_NAME), times(1)).addMercenaries(5);
 verify(testCurrentPlayers.get(KNIGHT_NAME), times(1)).subtractTotalMoney(5);
 }

 @Test
 public void testMercenaryFail() {
 final Purchase purchase = new Purchase();
 when(testCurrentPlayers.get(BUYER_NAME)).thenReturn(new Knight(BUYER_NAME, "knight", BUYER_INITIAL_Money));
 when(testCurrentPlayers.get(KNIGHT_NAME)).thenReturn(new Knight(KNIGHT_NAME, "knight", KNIGHT_INITIAL_Money - 5));

 purchase.perform(Arrays.asList("5", BUYER_NAME, KNIGHT_NAME));
 verify(testCurrentPlayers, times(1)).get(BUYER_NAME);
 verify(testCurrentPlayers, times(1)).get(KNIGHT_NAME);
 verifyNoMoreInteractions(testCurrentPlayers.get(BUYER_NAME));
 verifyNoMoreInteractions(testCurrentPlayers.get(KNIGHT_NAME));

 // Check insufficient funds
 assertEquals(0, testCurrentPlayers.get(BUYER_NAME).getMercenaries());
 assertEquals(0, testCurrentPlayers.get(KNIGHT_NAME).getMercenaries());
 }
}