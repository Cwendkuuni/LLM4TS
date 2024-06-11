package src;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PurchaseTest {

 private Purchase purchase;
 private Iterator<String> argsIterator;
 private Collection<String> args;
 private Fiefdoms map;
 private CurrentPlayers currentPlayers;

 @Before
 public void setup() {
 purchase = new Purchase();
 args = new ArrayList<>();
 argsIterator = args.iterator();
 map = Mockito.mock(Fiefdoms.class);
 currentPlayers = Mockito.mock(CurrentPlayers.class);
 when(map.get(anyString())).thenAnswer(invocation -> map.get(invocation.getArgument(0)));
 when(currentPlayers.get(anyString())).thenAnswer(invocation -> currentPlayers.get(invocation.getArgument(0)));
 }

 @Test
 public void testPerformNullArgs() {
 purchase.perform(null);
 verify(System.out, times(1)).println("Error: null argument list");
 }

 @Test
 public void testPerformEmptyArgs() {
 purchase.perform(args);
 verify(System.out, never()).println("Error: null argument list");
 }

 @Test
 public void testVassalInvalidFiefdom() {
 args.add("1");
 args.add("player1");
 args.add("invalidFiefdom");
 purchase.perform(args.subList(0, args.size()).iterator());
 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom: invalidFiefdom owner: null");
 }

 @Test
 public void testVassalInvalidBuyer() {
 args.clear();
 args.add("2");
 args.add("invalidPlayer");
 args.add("fiefdom1");
 purchase.perform(args.subList(0, args.size()).iterator());
 verify(System.out, times(1)).println("Inexistent Player: invalidPlayer");
 }

 @Test
 public void testVassalInvalidOwner() {
 args.clear();
 args.add("3");
 args.add("player1");
 args.add("invalidFiefdom");
 purchase.perform(args.subList(0, args.size()).iterator());
 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom: invalidFiefdom owner: null");
 }

 @Test
 public void testVassalInsufficientFunds() {
 Knight buyerObj = new Knight("player1", "knight", 100, 20, 10, 5);
 when(currentPlayers.get("player1")).thenReturn(buyerObj);
 args.clear();
 args.add("4");
 args.add("player1");
 args.add("fiefdom1");
 purchase.perform(args.subList(0, args.size()).iterator());
 verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
 }

 @Test
 public void testVassalBuyVassals() {
 Knight buyerObj = new Knight("player1", "knight", 150, 20, 10, 5);
 when(currentPlayers.get("player1")).thenReturn(buyerObj);
 args.clear();
 args.add("5");
 args.add("player1");
 args.add("fiefdom1");
 purchase.perform(args.subList(0, args.size()).iterator());
 verify(fiefdom1, times(1)).setLoyalVassals(5, true);
 }

 @Test
 public void testCastleFiefdomInvalidFiefdom() {
 args.clear();
 args.add("6");
 args.add("player1");
 args.add("invalidFiefdom");
 purchase.perform(args.subList(0, args.size()).iterator());
 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom: invalidFiefdom owner: null");
 }

 @Test
 public void testCastleFiefdomInsufficientFunds() {
 Knight buyerObj = new Knight("player1", "king", 150, 20, 10, 5);
 when(currentPlayers.get("player1")).thenReturn(buyerObj);
 args.clear();
 args.add("7");
 args.add("player1");
 args.add("fiefdom1");
 purchase.perform(args.subList(0, args.size()).iterator());
 verify(System.out, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
 }

 @Test
 public void testCastleFiefdomBuyCastles() {
 Knight buyerObj = new Knight("player1", "king", 300, 20, 10, 5);
 when(currentPlayers.get("player1")).thenReturn(buyerObj);
 Fiefdoms fiefdom = new Fiefdoms("fiefdom1", "player2", null, null, null, null);
 when(map.get("fiefdom1")).thenReturn(fiefdom);
args.clear();
args.add("8");
args.add("player1");
args.add("fiefdom1");
purchase.perform(args.subList(0, args.size()).iterator());
verify(fiefdom, times(1)).buyFiefdomCastles(5);
}

 @Test
 public void testCastlePersonalInvalidBuyer() {
 args.clear();
 args.add("9");
 args.add("invalidPlayer");
 args.add("fiefdom1");
 purchase.perform(args.subList(0, args.size()).iterator());
 verify(System.out, times(1)).println("Inexistent Player: invalidPlayer");
 }

 @Test
 public void testCastlePersonalInvalidOwner() {
 args.clear();
 args.add("10");
 args.add("player1");
 args.add("invalidFiefdom");
 purchase.perform(args.subList(0, args.size()).iterator());
 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom: invalidFiefdom owner: null");
 }

 @Test
 public void testCastlePersonalInsufficientFunds() {
 Knight buyerObj = new Knight("player1", "knight", 100, 20, 10, 5);
 when(currentPlayers.get("player1")).thenReturn(buyerObj);
 args.clear();
 args.add("11");
 args.add("player1");
 args.add("fiefdom1");
 purchase.perform(args.subList(0, args.size()).iterator());
 verify(System.out, times(1)).println("You cannot buy more Personal castle, you have insufficient funds");
 }

 @Test
 public void testCastlePersonalBuyCastles() {
 Knight buyerObj = new Knight("player1", "king", 300, 20, 10, 5);
 when(currentPlayers.get("player1")).thenReturn(buyerObj);
 Fiefdoms fiefdom = new Fiefdoms("fiefdom1", "player2", null, null, null, null);
 when(map.get("fiefdom1")).thenReturn(fiefdom);
args.clear();
args.add("12");
args.add("player1");
args.add("fiefdom1");
purchase.perform(args.subList(0, args.size()).iterator());
verify(fiefdom, times(1)).buyPersonalCastles(5);
}

 @Test
 public void testMercenaryInvalidBuyer() {
 args.clear();
 args.add("13");
 args.add("invalidPlayer");
 args.add("knight1");
 purchase.perform(args.subList(0, args.size()).iterator());
 verify(System.out, times(1)).println("Inexistent Player: invalidPlayer");
 }

 @Test
 public void testMercenaryInvalidKnight() {
 args.clear();
 args.add("14");
 args.add("player1");
 args.add("knightInvalid");
 purchase.perform(args.subList(0, args.size()).iterator());
 verify(System.out, times(1)).println("Inexistent Knight: knightInvalid");
 }

 @Test
 public void testMercenaryInsufficientFunds() {
 Knight buyerObj = new Knight("player1", "knight", 100, 20, 10, 5);
 when(currentPlayers.get("player1")).thenReturn(buyerObj);
 Knight knight = new Knight("knight1", "knight", 20, 20, 10, 5);
 when(currentPlayers.get("knight1")).thenReturn(knight);
 args.clear();
 args.add("15");
 args.add("player1");
 args.add("knight1");
 purchase.perform(args.subList(0, args.size()).iterator());
 verify(System.out, times(1)).println("You cannot buy more mercenaries, you have insufficient funds");
 }

 @Test
 public void testMercenaryBuyMercenaries() {
 Knight buyerObj = new Knight("player1", "king", 200, 20, 10, 5);
 when(currentPlayers.get("player1")).thenReturn(buyerObj);
 Knight knight = new Knight("knight1", "knight", 10, 20, 10, 5);
 when(currentPlayers.get("knight1")).thenReturn(knight);
 args.clear();
 args.add("16");
 args.add("player1");
 args.add("knight1");
 purchase.perform(args.subList(0, args.size()).iterator());
 verify(knight, times(1)).addMercenaries(5);
 }
}