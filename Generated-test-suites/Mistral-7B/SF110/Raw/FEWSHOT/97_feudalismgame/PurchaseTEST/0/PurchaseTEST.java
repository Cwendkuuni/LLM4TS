package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PurchaseTest {

 private Purchase purchase;
 private Iterator<String> argIterator;
 private Map<String, Fiefdoms> map;
 private CurrentPlayers currentPlayers;

 @Before
 public void setup() {
 map = new HashMap<>();
 currentPlayers = Mockito.mock(CurrentPlayers.class);
 purchase = new Purchase() {
 @Override
 public void perform(Collection<String> args) {
 super.perform(args);
 }
 };
 argIterator = Mockito.mock(Iterator.class);
 when(argIterator.next()).thenAnswer(invocation -> invocation.getArgument(0));
 when(map.get(anyString())).thenAnswer(invocation -> map.get(invocation.getArgument(0)));
 }

 @Test
 public void testVassal() {
 when(argIterator.hasNext()).thenReturn(true, true, true, true, true, true, true);
 when(argIterator.next()).thenReturn("5", "player1", "fiefdom1");
 when(currentPlayers.get(eq("player1"))).thenReturn(new Knight("player1", "knight"));
 when(map.get("fiefdom1").getOwner()).thenReturn(null);
 when(map.get("fiefdom1").getViking().size()).thenReturn(0);
 when(map.get("fiefdom1").getMagyar().size()).thenReturn(0);
 when(map.get("fiefdom1").getMuslim().size()).thenReturn(0);
 purchase.vassal(argIterator);
 verify(currentPlayers, times(1)).get(eq("player1"));
 verify(map, times(1)).get(eq("fiefdom1"));
 verify(currentPlayers, times(1)).getRank();
 verify(currentPlayers, times(1)).setTotalMoney(eq(5), anyBoolean());
 verify(map, times(1)).getOwner();
 verify(map, times(1)).setLoyalVassals(eq(5), anyBoolean());
 }

 @Test
 public void testVassal_InsufficientFunds() {
 when(argIterator.hasNext()).thenReturn(true, true, true, true, true, true, true);
 when(argIterator.next()).thenReturn("5", "player1", "fiefdom1");
 when(currentPlayers.get(eq("player1"))).thenReturn(new Knight("player1", "knight"));
 when(map.get("fiefdom1").getOwner()).thenReturn(null);
 when(map.get("fiefdom1").getViking().size()).thenReturn(0);
 when(map.get("fiefdom1").getMagyar().size()).thenReturn(0);
 when(map.get("fiefdom1").getMuslim().size()).thenReturn(0);
 when(currentPlayers.get("player1").getTotalMoney()).thenReturn(0);
 purchase.vassal(argIterator);
 verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
 }

 @Test
 public void testVassal_FiefdomBelongsToAnotherPlayer() {
 when(argIterator.hasNext()).thenReturn(true, true, true, true, true, true, true);
 when(argIterator.next()).thenReturn("5", "player1", "fiefdom1");
 when(currentPlayers.get(eq("player1"))).thenReturn(new Knight("player1", "knight"));
 when(map.get("fiefdom1").getOwner()).thenReturn(new Knight("player2", "king"));
 purchase.vassal(argIterator);
 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
 }

 @Test
 public void testVassal_PlayerIsKing() {
 when(argIterator.hasNext()).thenReturn(true, true, true, true, true, true, true);
 when(argIterator.next()).thenReturn("5", "player1", "fiefdom1");
 when(currentPlayers.get(eq("player1"))).thenReturn(new Knight("player1", "king"));
 when(map.get("fiefdom1").getOwner()).thenReturn(null);
 purchase.vassal(argIterator);
 verify(map, times(1)).setLoyalVassals(eq(5), anyBoolean());
 }

 @Test
 public void testVassal_FiefdomIsOwnedByCountOrDuke() {
 when(argIterator.hasNext()).thenReturn(true, true, true, true, true, true, true);
 when(argIterator.next()).thenReturn("5", "player1", "fiefdom1");
 when(currentPlayers.get(eq("player1"))).thenReturn(new Knight("player1", "knight"));
 when(map.get("fiefdom1").getOwner()).thenReturn(new Knight("player2", "count"));
 purchase.vassal(argIterator);
 verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
 }

 @Test
 public void testCastleFiefdom() {
 when(argIterator.hasNext()).thenReturn(true, true, true, true, true, true, true);
 when(argIterator.next()).thenReturn("5", "player1", "fiefdom1");
 when(currentPlayers.get(eq("player1"))).thenReturn(new Knight("player1", "knight"));
 when(map.get("fiefdom1").getOwner()).thenReturn(null);
 when(map.get("fiefdom1").getViking().size()).thenReturn(0);
 when(map.get("fiefdom1").getMagyar().size()).thenReturn(0);
 when(map.get("fiefdom1").getMuslim().size()).thenReturn(0);
 purchase.castleFiefdom(argIterator);
 verify(currentPlayers, times(1)).get(eq("player1"));
 verify(map, times(1)).get(eq("fiefdom1"));
 verify(currentPlayers, times(1)).getRank();
 verify(currentPlayers, times(1)).setTotalMoney(eq(25 * 5), anyBoolean());
 verify(map, times(1)).buyFiefdomCastles(eq(5));
 }

 @Test
 public void testCastleFiefdom_InsufficientFunds() {
 when(argIterator.hasNext()).thenReturn(true, true, true, true, true, true, true);
 when(argIterator.next()).thenReturn("5", "player1", "fiefdom1");
 when(currentPlayers.get(eq("player1"))).thenReturn(new Knight("player1", "knight"));
 when(map.get("fiefdom1").getOwner()).thenReturn(null);
 when(map.get("fiefdom1").getViking().size()).thenReturn(0);
 when(map.get("fiefdom1").getMagyar().size()).thenReturn(0);
 when(map.get("fiefdom1").getMuslim().size()).thenReturn(0);
 when(currentPlayers.get("player1").getTotalMoney()).thenReturn(0);
 purchase.castleFiefdom(argIterator);
 verify(System.out, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
 }

 @Test
 public void testCastleFiefdom_FiefdomBelongsToAnotherPlayer() {
 when(argIterator.hasNext()).thenReturn(true, true, true, true, true, true, true);
 when(argIterator.next()).thenReturn("5", "player1", "fiefdom1");
 when(currentPlayers.get(eq("player1"))).thenReturn(new Knight("player1", "knight"));
 when(map.get("fiefdom1").getOwner()).thenReturn(new Knight("player2", "king"));
 purchase.castleFiefdom(argIterator);
 verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
 }

 @Test
 public void testCastlePersonal() {
 when(argIterator.hasNext()).thenReturn(true, true, true, true, true, true, true);
 when(argIterator.next()).thenReturn("5", "player1", "fiefdom1");
 when(currentPlayers.get(eq("player1"))).thenReturn(new Knight("player1", "knight"));
 when(map.get("fiefdom1").getOwner()).thenReturn(null);
 when(map.get("fiefdom1").getViking().size()).thenReturn(0);
 when(map.get("fiefdom1").getMagyar().size()).thenReturn(0);
 when(map.get("fiefdom1").getMuslim().size()).thenReturn(0);
 purchase.castlePersonal(argIterator);
 verify(currentPlayers, times(1)).get(eq("player1"));
 verify(map, times(1)).get(eq("fiefdom1"));
 verify(currentPlayers, times(1)).getRank();
 verify(currentPlayers, times(1)).setTotalMoney(eq(10 * 5), anyBoolean());
 verify(map, times(1)).buyPersonalCastles(eq(5));
 }

 @Test
 public void testCastlePersonal_InsufficientFunds() {
 when(argIterator.hasNext()).thenReturn(true, true, true, true, true, true, true);
 when(argIterator.next()).thenReturn("5", "player1", "fiefdom1");
 when(currentPlayers.get(eq("player1"))).thenReturn(new Knight("player1", "knight"));
 when(map.get("fiefdom1").getOwner()).thenReturn(null);
 when(map.get("fiefdom1").getViking().size()).thenReturn(0);
 when(map.get("fiefdom1").getMagyar().size()).thenReturn(0);
 when(map.get("fiefdom1").getMuslim().size()).thenReturn(0);
 when(currentPlayers.get("player1").getTotalMoney()).thenReturn(0);
 purchase.castlePersonal(argIterator);
 verify(System.out, times(1)).println("You cannot buy more Personal castle, you have insufficient funds");
 }

 @Test
 public void testCastlePersonal_FiefdomBelongsToAnotherPlayer() {
 when(argIterator.hasNext()).thenReturn(true, true, true, true, true, true, true);
 when(argIterator.next()).thenReturn("5", "player1", "fiefdom1");
 when(currentPlayers.get(eq("player1"))).thenReturn(new Knight("player1", "knight"));
 when(map.get("fiefdom1").getOwner()).thenReturn(new Knight("player2", "king"));
 purchase.castlePersonal(argIterator);
 verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
 }

 @Test
 public void testMercenary() {
 when(argIterator.hasNext()).thenReturn(true, true, true, true, true, true, true);
 when(argIterator.next()).thenReturn("5", "player1", "knight1");
 when(currentPlayers.get(eq("player1"))).thenReturn(new Knight("player1", "knight"));
 when(currentPlayers.get(eq("knight1"))).thenReturn(new Knight("knight1", "knight"));
 purchase.mercenary(argIterator);
 verify(currentPlayers, times(1)).get(eq("player1"));
 verify(currentPlayers, times(1)).get(eq("knight1"));
 verify(currentPlayers, times(1)).get(eq("player1")).setTotalMoney(eq(5), anyBoolean());
 verify(currentPlayers, times(1)).get(eq("knight1")).addMercenaries(eq(5));
 }

 @Test
 public void testMercenary_InsufficientFunds() {
 when(argIterator.hasNext()).thenReturn(true, true, true, true, true, true, true);
 when(argIterator.next()).thenReturn("5", "player1", "knight1");
 when(currentPlayers.get(eq("player1"))).thenReturn(new Knight("player1", "knight"));
 when(currentPlayers.get(eq("knight1"))).thenReturn(new Knight("knight1", "knight"));
 when(currentPlayers.get("player1").getTotalMoney()).thenReturn(0);
 purchase.mercenary(argIterator);
 verify(System.out, times(1)).println("You cannot buy more mercenaries, you have insufficient funds");
 }

 @Test
 public void testMercenary_SamePlayer() {
 when(argIterator.hasNext()).thenReturn(true, true, true, true, true, true, true);
 when(argIterator.next()).thenReturn("5", "player1", "knight1");
 when(currentPlayers.get(eq("player1"))).thenReturn(new Knight("player1", "knight"));
 when(currentPlayers.get(eq("knight1"))).thenReturn(new Knight("knight1", "knight"));
 purchase.mercenary(argIterator);
 verify(currentPlayers, times(1)).get(eq("player1"));
 verify(currentPlayers, times(1)).get(eq("knight1"));
 verify(currentPlayers, times(1)).get(eq("player1")).setTotalMoney(eq(-5), anyBoolean());
 verify(currentPlayers, times(1)).get(eq("knight1")).addMercenaries(eq(5));
 }

 @Test
 public void testMercenary_PlayerIsKing() {
 when(argIterator.hasNext()).thenReturn(true, true, true, true, true, true, true);
 when(argIterator.next()).thenReturn("5", "player1", "knight1");
 when(currentPlayers.get(eq("player1"))).thenReturn(new Knight("player1", "king"));
 when(currentPlayers.get(eq("knight1"))).thenReturn(new Knight("knight1", "knight"));
 purchase.mercenary(argIterator);
 verify(System.out, times(1)).println("You cannot buy a Personal Castle for Counts or Dukes");
 }
}