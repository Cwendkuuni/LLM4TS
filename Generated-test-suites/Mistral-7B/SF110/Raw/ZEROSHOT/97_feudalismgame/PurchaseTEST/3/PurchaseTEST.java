package src.test.java;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;;

public class PurchaseTest {

 private Purchase purchase;
 private List<String> argsList;
 private Iterator<String> argsIterator;
 private Fiefdoms fiefdom;
 private Knight buyer, knight;

 @Before
 public void setup() {
 purchase = new Purchase();
 argsList = new ArrayList<>();
 argsIterator = argsList.iterator();
 fiefdom = Mockito.mock(Fiefdoms.class);
 buyer = Mockito.mock(Knight.class);
 knight = Mockito.mock(Knight.class);
 }

 @Test
 public void testVassal() {
 argsList.add("10");
 argsList.add("player1");
 argsList.add("fiefdom1");
 when(Map.map.get(argsList.get(2))).thenReturn(fiefdom);
 when(CurrentPlayers.currentPlayers.get(argsList.get(1))).thenReturn(buyer);
 when(CurrentPlayers.currentPlayers.get(argsList.get(3))).thenReturn(knight);
 when(fiefdom.getOwner()).thenReturn(null);
 when(fiefdom.getName()).thenReturn("fiefdom1");
 when(buyer.getRank()).thenReturn("knight");
 when(buyer.getTotalMoney()).thenReturn(100);
 when(buyer.isFiefdomMine(argsList.get(2))).thenReturn(false);

 purchase.perform(argsList);

 ArgumentCaptor<Integer> qtyCaptor = ArgumentCaptor.forClass(Integer.class);
 ArgumentCaptor<Boolean> isLoyalCaptor = ArgumentCaptor.forClass(Boolean.class);
 verify(fiefdom).setLoyalVassals(qtyCaptor.capture(1), isLoyalCaptor.capture(true));
 assertEquals(10, qtyCaptor.getValue());
 assertTrue(isLoyalCaptor.getValue());
 }

 @Test
 public void testVassal_FiefdomBelongsToAnotherPlayer() {
 argsList.clear();
 argsList.add("10");
 argsList.add("player1");
 argsList.add("fiefdom1");
 when(Map.map.get(argsList.get(2))).thenReturn(fiefdom);
 when(CurrentPlayers.currentPlayers.get(argsList.get(1))).thenReturn(buyer);
 when(CurrentPlayers.currentPlayers.get(argsList.get(3))).thenReturn(knight);
 when(fiefdom.getOwner()).thenReturn(new Player("anotherPlayer"));

 purchase.perform(argsList);

 String expectedOutput = "You can only buy a vassal for a fiefdom that belongs to you";
 assertEquals(expectedOutput, purchase.toString());
 }

 @Test
 public void testVassal_InsufficientFunds() {
 argsList.clear();
 argsList.add("11");
 argsList.add("player1");
 argsList.add("fiefdom1");
 when(Map.map.get(argsList.get(2))).thenReturn(fiefdom);
 when(CurrentPlayers.currentPlayers.get(argsList.get(1))).thenReturn(buyer);
 when(CurrentPlayers.currentPlayers.get(argsList.get(3))).thenReturn(knight);
 when(fiefdom.getOwner()).thenReturn(null);
 when(buyer.getRank()).thenReturn("knight");
 when(buyer.getTotalMoney()).thenReturn(90);
 when(buyer.isFiefdomMine(argsList.get(2))).thenReturn(false);

 purchase.perform(argsList);

 String expectedOutput = "You cannot buy more vassals, you have insufficient funds";
 assertEquals(expectedOutput, purchase.toString());
 }

 @Test
 public void testCastleFiefdom() {
 argsList.clear();
 argsList.add("5");
 argsList.add("player1");
 argsList.add("fiefdom1");
 when(Map.map.get(argsList.get(2))).thenReturn(fiefdom);
 when(CurrentPlayers.currentPlayers.get(argsList.get(1))).thenReturn(buyer);
 when(CurrentPlayers.currentPlayers.get(argsList.get(3))).thenReturn(knight);
 when(fiefdom.getOwner()).thenReturn(null);
 when(buyer.getRank()).thenReturn("king");
 when(buyer.getTotalMoney()).thenReturn(150);
 when(buyer.isFiefdomMine(argsList.get(2))).thenReturn(false);

 purchase.perform(argsList);

 ArgumentCaptor<Integer> qtyCaptor = ArgumentCaptor.forClass(Integer.class);
 verify(fiefdom).buyFiefdomCastles(qtyCaptor.capture(1));
 assertEquals(5, qtyCaptor.getValue());
 }

 @Test
 public void testCastleFiefdom_FiefdomBelongsToAnotherPlayer() {
 argsList.clear();
 argsList.add("5");
 argsList.add("player1");
 argsList.add("fiefdom1");
 when(Map.map.get(argsList.get(2))).thenReturn(fiefdom);
 when(CurrentPlayers.currentPlayers.get(argsList.get(1))).thenReturn(buyer);
 when(CurrentPlayers.currentPlayers.get(argsList.get(3))).thenReturn(knight);
 when(fiefdom.getOwner()).thenReturn(new Player("anotherPlayer"));

 purchase.perform(argsList);

 String expectedOutput = "You can only buy a castle for a fiefdom that belongs to you";
 assertEquals(expectedOutput, purchase.toString());
 }

 @Test
 public void testCastleFiefdom_InsufficientFunds() {
 argsList.clear();
 argsList.add("6");
 argsList.add("player1");
 argsList.add("fiefdom1");
 when(Map.map.get(argsList.get(2))).thenReturn(fiefdom);
 when(CurrentPlayers.currentPlayers.get(argsList.get(1))).thenReturn(buyer);
 when(CurrentPlayers.currentPlayers.get(argsList.get(3))).thenReturn(knight);
 when(fiefdom.getOwner()).thenReturn(null);
 when(buyer.getRank()).thenReturn("king");
 when(buyer.getTotalMoney()).thenReturn(130);
 when(buyer.isFiefdomMine(argsList.get(2))).thenReturn(false);

 purchase.perform(argsList);

 String expectedOutput = "You cannot buy more Fiefdom castles, you have insufficient funds";
 assertEquals(expectedOutput, purchase.toString());
 }

 @Test
 public void testCastlePersonal() {
 argsList.clear();
 argsList.add("3");
 argsList.add("player1");
 argsList.add("fiefdom1");
 when(Map.map.get(argsList.get(2))).thenReturn(fiefdom);
 when(CurrentPlayers.currentPlayers.get(argsList.get(1))).thenReturn(buyer);
 when(CurrentPlayers.currentPlayers.get(argsList.get(3))).thenReturn(knight);
 when(fiefdom.getOwner()).thenReturn(null);
 when(buyer.getRank()).thenReturn("king");
 when(buyer.getTotalMoney()).thenReturn(150);
 when(buyer.isFiefdomMine(argsList.get(2))).thenReturn(false);
 when(knight.getRank()).thenReturn("knight");

 purchase.perform(argsList);

 ArgumentCaptor<Integer> qtyCaptor = ArgumentCaptor.forClass(Integer.class);
 verify(fiefdom).buyPersonalCastles(qtyCaptor.capture(1));
 assertEquals(3, qtyCaptor.getValue());
 }

 @Test
 public void testCastlePersonal_FiefdomBelongsToAnotherPlayer() {
 argsList.clear();
 argsList.add("3");
 argsList.add("player1");
 argsList.add("fiefdom1");
 when(Map.map.get(argsList.get(2))).thenReturn(fiefdom);
 when(CurrentPlayers.currentPlayers.get(argsList.get(1))).thenReturn(buyer);
 when(CurrentPlayers.currentPlayers.get(argsList.get(3))).thenReturn(knight);
 when(fiefdom.getOwner()).thenReturn(new Player("anotherPlayer"));

 purchase.perform(argsList);

 String expectedOutput = "You can only buy a castle for a fiefdom that belongs to you";
 assertEquals(expectedOutput, purchase.toString());
 }

 @Test
 public void testCastlePersonal_InsufficientFunds() {
 argsList.clear();
 argsList.add("4");
 argsList.add("player1");
 argsList.add("fiefdom1");
 when(Map.map.get(argsList.get(2))).thenReturn(fiefdom);
 when(CurrentPlayers.currentPlayers.get(argsList.get(1))).thenReturn(buyer);
 when(CurrentPlayers.currentPlayers.get(argsList.get(3))).thenReturn(knight);
 when(fiefdom.getOwner()).thenReturn(null);
 when(buyer.getRank()).thenReturn("king");
 when(buyer.getTotalMoney()).thenReturn(120);
 when(buyer.isFiefdomMine(argsList.get(2))).thenReturn(false);
 when(knight.getRank()).thenReturn("knight");

 purchase.perform(argsList);

 String expectedOutput = "You cannot buy more Personal castle, you have insufficient funds";
 assertEquals(expectedOutput, purchase.toString());
 }

 @Test
 public void testMercenary() {
 argsList.clear();
 argsList.add("2");
 argsList.add("player1");
 argsList.add("knight1");
 when(CurrentPlayers.currentPlayers.get(argsList.get(2))).thenReturn(knight);
 when(CurrentPlayers.currentPlayers.get(argsList.get(1))).thenReturn(buyer);
 when(buyer.getRank()).thenReturn("king");
 when(buyer.getTotalMoney()).thenReturn(200);
 when(buyer.getName()).thenReturn("player1");
 when(knight.getName()).thenReturn("knight1");

 purchase.perform(argsList);

 ArgumentCaptor<Integer> qtyCaptor = ArgumentCaptor.forClass(Integer.class);
 verify(knight).addMercenaries(qtyCaptor.capture(2));
 assertEquals(2, qtyCaptor.getValue());
 }

 @Test
 public void testMercenary_InsufficientFunds() {
 argsList.clear();
 argsList.add("3");
 argsList.add("player1");
 argsList.add("knight1");
 when(CurrentPlayers.currentPlayers.get(argsList.get(2))).thenReturn(knight);
 when(CurrentPlayers.currentPlayers.get(argsList.get(1))).thenReturn(buyer);
 when(buyer.getRank()).thenReturn("king");
 when(buyer.getTotalMoney()).thenReturn(180);
 when(buyer.getName()).thenReturn("player1");
 when(knight.getName()).thenReturn("knight1");

 purchase.perform(argsList);

 String expectedOutput = "You cannot buy more mercenaries, you have insufficient funds";
 assertEquals(expectedOutput, purchase.toString());
 }

 @Test
 public void testMercenary_InexistentKnight() {
 argsList.clear();
 argsList.add("2");
 argsList.add("player1");
 argsList.add("inexistentKnight");
 when(CurrentPlayers.currentPlayers.get(argsList.get(2))).thenReturn(null);
 when(CurrentPlayers.currentPlayers.get(argsList.get(1))).thenReturn(buyer);
 when(buyer.getRank()).thenReturn("king");
 when(buyer.getTotalMoney()).thenReturn(200);
 when(buyer.getName()).thenReturn("player1");

 purchase.perform(argsList);

 String expectedOutput = "Inexistent Knight";
 assertEquals(expectedOutput, purchase.toString());
 }

 @Test
 public void testMercenary_SamePlayer() {
 argsList.clear();
 argsList.add("2");
 argsList.add("player1");
 argsList.add("knight1");
 when(CurrentPlayers.currentPlayers.get(argsList.get(2))).thenReturn(buyer);
 when(CurrentPlayers.currentPlayers.get(argsList.get(1))).thenReturn(buyer);
 when(buyer.getRank()).thenReturn("king");
 when(buyer.getTotalMoney()).thenReturn(200);
 when(buyer.getName()).thenReturn("player1");
 when(knight.getName()).thenReturn("knight1");

 purchase.perform(argsList);

 String expectedOutput = "You cannot buy your own mercenaries";
 assertEquals(expectedOutput, purchase.toString());
 }
}