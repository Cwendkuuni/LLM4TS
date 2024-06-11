package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms map;
 private CurrentPlayers currentPlayers;
 private List<Knight> knights;

 @Before
 public void setUp() {
 purchase = new Purchase();
 map = Mockito.mock(Fiefdoms.class);
 currentPlayers = Mockito.mock(CurrentPlayers.class);
 knights = new ArrayList<>();
 when(map.get(anyString())).then(i -> knights.get(i.getIntValue()));
 }

 @Test
 public void testPerform() {
 final Collection args = Arrays.asList("vassal", Arrays.asList(1, "player1", "fiefdom1").iterator(),
         "castleFiefdom", Arrays.asList(2, "player1", "fiefdom1").iterator(),
         "castlePersonal", Arrays.asList(3, "player1", "fiefdom1").iterator(),
         "mercenary", Arrays.asList(4, "player1", "knight1").iterator());
 purchase.perform(args);
 verify(map, times(3)).get(anyString());
 verify(currentPlayers, times(3)).get(anyString());
 }

 @Test(expected = SecurityException.class)
 public void testPerformWithSecurityException() {
 final Collection args = Arrays.asList("unsafeMethod", Arrays.asList(new Object() {}).iterator());
 purchase.perform(args);
 }

 @Test(expected = NoSuchMethodException.class)
 public void testPerformWithNoSuchMethodException() {
 final Collection args = Arrays.asList("unknownMethod", Arrays.asList(new Object() {}.iterator()));
 purchase.perform(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithIllegalArgumentException() {
 final Collection args = Arrays.asList("vassal", null);
 purchase.perform(args);
 }

 @Test(expected = IllegalAccessException.class)
 public void testPerformWithIllegalAccessException() {
 final Collection args = Arrays.asList("vassal", Arrays.asList("", "player1", "fiefdom1").iterator());
 purchase.perform(args);
 }

 @Test(expected = InvocationTargetException.class)
 public void testPerformWithInvocationTargetException() {
 final Collection args = Arrays.asList("castlePersonal", Arrays.asList(5, "player1", "fiefdom1").iterator());
 doThrow(new RuntimeException()).when(map).get(anyString());
 purchase.perform(args);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testPerformWithClassNotFoundException() {
 final Collection args = Arrays.asList("unknownClass", Arrays.asList(new Object() {}.iterator()));
 purchase.perform(args);
 }

 @Test
 public void testVassal() {
 final Iterator arg = Arrays.asList(1, "player1", "fiefdom1").iterator();
 final Fiefdoms fiefdom = map;
 final Knight buyer = currentPlayers.get("player1");
 final Knight expectedBuyer = Mockito.mock(Knight.class);
 when(currentPlayers.get(arg.next())).thenReturn(expectedBuyer);
 when(map.get(arg.next())).thenReturn(fiefdom);
 when(fiefdom.getOwner()).thenReturn(null);
 purchase.vassal(arg);
 verify(buyer).setTotalMoney(1, false);
 verifyZeroInteractions(expectedBuyer);
 assertEquals(1, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalWithFiefdomOwnedByKing() {
 final Iterator arg = Arrays.asList(1, "player1", "fiefdom1").iterator();
 final Fiefdoms fiefdom = map;
 final Knight buyer = currentPlayers.get("player1");
 final Knight expectedBuyer = Mockito.mock(Knight.class);
 final King king = Mockito.mock(King.class);
 when(currentPlayers.get(arg.next())).thenReturn(expectedBuyer);
 when(map.get(arg.next())).thenReturn(fiefdom);
 when(fiefdom.getOwner()).thenReturn(king);
 purchase.vassal(arg);
 verify(buyer).setTotalMoney(1, false);
 verifyZeroInteractions(expectedBuyer);
 assertEquals(1, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalWithFiefdomOwnedByPlayer() {
 final Iterator arg = Arrays.asList(1, "player1", "fiefdom1").iterator();
 final Fiefdoms fiefdom = map;
 final Knight buyer = currentPlayers.get("player1");
 final Knight expectedBuyer = Mockito.mock(Knight.class);
 when(currentPlayers.get(arg.next())).thenReturn(expectedBuyer);
 when(map.get(arg.next())).thenReturn(fiefdom);
 when(fiefdom.getOwner()).thenReturn(currentPlayers.get("player2"));
 purchase.vassal(arg);
 verify(buyer).setTotalMoney(1, false);
 verifyZeroInteractions(expectedBuyer);
 assertEquals(0, fiefdom.getLoyalVassals());
 }

 @Test
 public void testCastleFiefdom() {
 final Iterator arg = Arrays.asList(2, "player1", "fiefdom1").iterator();
 final Fiefdoms fiefdom = map;
 final Knight buyer = currentPlayers.get("player1");
 final Knight expectedBuyer = Mockito.mock(Knight.class);
 when(currentPlayers.get(arg.next())).thenReturn(expectedBuyer);
 when(map.get(arg.next())).thenReturn(fiefdom);
 when(fiefdom.getOwner()).thenReturn(null);
 purchase.castleFiefdom(arg);
 verify(buyer).setTotalMoney(25, false);
 verifyZeroInteractions(expectedBuyer);
 assertEquals(2, fiefdom.getCastles());
 }

 @Test
 public void testCastleFiefdomWithFiefdomOwnedByKing() {
 final Iterator arg = Arrays.asList(2, "player1", "fiefdom1").iterator();
 final Fiefdoms fiefdom = map;
 final Knight buyer = currentPlayers.get("player1");
 final Knight king = Mockito.mock(King.class);
 when(currentPlayers.get(arg.next())).thenReturn(buyer);
 when(map.get(arg.next())).thenReturn(fiefdom);
 when(fiefdom.getOwner()).thenReturn(king);
 purchase.castleFiefdom(arg);
 verify(buyer).setTotalMoney(25, false);
 verifyZeroInteractions(king);
 assertEquals(2, fiefdom.getCastles());
 }

 @Test
 public void testCastleFiefdomWithFiefdomAlreadyHaveCastles() {
 final Iterator arg = Arrays.asList(2, "player1", "fiefdom1").iterator();
 final Fiefdoms fiefdom = map;
 final Knight buyer = currentPlayers.get("player1");
 when(currentPlayers.get(arg.next())).thenReturn(buyer);
 when(map.get(arg.next())).thenReturn(fiefdom);
 when(fiefdom.getOwner()).thenReturn(null);
 purchase.castleFiefdom(arg);
 purchase.castleFiefdom(arg);
 verify(buyer, times(1)).setTotalMoney(25, false);
 }

 @Test
 public void testCastlePersonal() {
 final Iterator arg = Arrays.asList(3, "player1", "fiefdom1").iterator();
 final Fiefdoms fiefdom = map;
 final Knight buyer = currentPlayers.get("player1");
 final Knight expectedBuyer = Mockito.mock(Knight.class);
 when(currentPlayers.get(arg.next())).thenReturn(expectedBuyer);
 when(map.get(arg.next())).thenReturn(fiefdom);
 when(fiefdom.getOwner()).thenReturn(null);
 purchase.castlePersonal(arg);
 verify(buyer).setTotalMoney(10, false);
 verifyZeroInteractions(expectedBuyer);
 assertEquals(3, fiefdom.getPersonalCastles());
 }

 @Test
 public void testCastlePersonalWithFiefdomOwnedByPlayer() {
 final Iterator arg = Arrays.asList(3, "player1", "fiefdom1").iterator();
 final Fiefdoms fiefdom = map;
 final Knight buyer = currentPlayers.get("player1");
 final Knight expectedBuyer = Mockito.mock(Knight.class);
 when(currentPlayers.get(arg.next())).thenReturn(expectedBuyer);
 when(map.get(arg.next())).thenReturn(fiefdom);
 when(fiefdom.getOwner()).thenReturn(currentPlayers.get("player2"));
 purchase.castlePersonal(arg);
 verify(buyer).setTotalMoney(10, false);
 verifyZeroInteractions(expectedBuyer);
 assertEquals(0, fiefdom.getPersonalCastles());
 }

 @Test
 public void testCastlePersonalWithInsufficientFunds() {
 final Iterator arg = Arrays.asList(3, "player1", "fiefdom1").iterator();
 final Fiefdoms fiefdom = map;
 final Knight buyer = currentPlayers.get("player1");
 when(currentPlayers.get(arg.next())).thenReturn(buyer);
 when(map.get(arg.next())).thenReturn(fiefdom);
 when(fiefdom.getOwner()).thenReturn(null);
 purchase.castlePersonal(arg);
 verify(buyer, never()).setTotalMoney(10, false);
 }

 @Test
 public void testMercenary() {
 final Iterator arg = Arrays.asList(4, "player1", "knight1").iterator();
 final Knight buyer = currentPlayers.get("player1");
 final Knight knight = currentPlayers.get("knight1");
 purchase.mercenary(arg);
 verify(buyer).setTotalMoney(4, false);
 verify(knight).addMercenaries(4);
 }

 @Test
 public void testMercenaryWithInsufficientFunds() {
 final Iterator arg = Arrays.asList(4, "player1", "knight1").iterator();
 final Knight buyer = currentPlayers.get("player1");
 final Knight knight = currentPlayers.get("knight1");
 when(buyer.getTotalMoney()).thenReturn(0);
 purchase.mercenary(arg);
 verify(buyer, never()).setTotalMoney(4, false);
 }

 @Test
 public void testMercenaryWithSameKnightBuyer() {
 final Iterator arg = Arrays.asList(4, "knight1", "knight1").iterator();
 final Knight buyer = currentPlayers.get("knight1");
 purchase.mercenary(arg);
 verify(buyer, never()).addMercenaries(4);
 }

 @Test
 public void testMercenaryWithKingBuyer() {
 final Iterator arg = Arrays.asList(4, "king", "knight1").iterator();
 final Knight buyer = currentPlayers.get("king");
 final Knight knight = currentPlayers.get("knight1");
 purchase.mercenary(arg);
 verify(buyer, never()).addMercenaries(4);
 }
}