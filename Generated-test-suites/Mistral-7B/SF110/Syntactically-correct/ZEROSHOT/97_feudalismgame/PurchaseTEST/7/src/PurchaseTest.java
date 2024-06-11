package src.test.java;

import static org.junit.Assert.*;
import static src.Actions.argsList;
import static src.Purchase.Map;
import static src.Purchase.CurrentPlayers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms fiefdom1;
 private Fiefdoms fiefdom2;
 private Knight knight1;
 private Knight knight2;
 private Knight player1;
 private Knight player2;

 @Before
 public void setUp() {
  purchase = new Purchase();
  fiefdom1 = new Fiefdoms("Fiefdom1", null);
  fiefdom2 = new Fiefdoms("Fiefdom2", null);
  knight1 = new Knight("Knight1", "Count", 1000, new ArrayList<>());
  knight2 = new Knight("Knight2", "Knight", 500, new ArrayList<>());
  player1 = new Knight("Player1", "King", 5000, new ArrayList<>());
  player2 = new Knight("Player2", "King", 6000, new ArrayList<>());
  CurrentPlayers.put(player1.getName(), player1);
  CurrentPlayers.put(player2.getName(), player2);
  Map.put(fiefdom1.getName(), fiefdom1);
  Map.put(fiefdom2.getName(), fiefdom2);
 }

 @After
 public void tearDown() {
  CurrentPlayers.clear();
  Map.clear();
 }

 @Test
 public void testVassal() {
  List<Object> args = argsList("5", "Player1", "Fiefdom1");
  purchase.perform(args);
  assertEquals(5, fiefdom1.getLoyalVassals());
  assertEquals(4950, player1.getTotalMoney());
 }

 @Test
 public void testVassalInsufficientFunds() {
  List<Object> args = argsList("6", "Player1", "Fiefdom1");
  purchase.perform(args);
  assertEquals(5, fiefdom1.getLoyalVassals());
  assertEquals(4950, player1.getTotalMoney());
  assertEquals("You cannot buy more vassals, you have insufficient funds", System.out.getClass().getProtectionDomain().getName());
 }

 @Test
 public void testVassalAlreadyOwner() {
  List<Object> args = argsList("1", "Player1", "Fiefdom1");
  purchase.perform(args);
  List<Object> args2 = argsList("1", "Player1", "Fiefdom1");
  purchase.perform(args2);
  assertEquals(5, fiefdom1.getLoyalVassals());
  assertEquals(4945, player1.getTotalMoney());
  assertEquals("Fiefdom doesn't belong to the kingdom: Fiefdom1 owner: null", System.out.getClass().getProtectionDomain().getName());
 }

 @Test
 public void testVassalWrongOwner() {
  List<Object> args = argsList("1", "Player2", "Fiefdom1");
  purchase.perform(args);
  assertEquals(0, fiefdom1.getLoyalVassals());
  assertEquals(5950, player2.getTotalMoney());
  assertEquals("You can only buy a castle for a fiefdom that belongs to you", System.out.getClass().getProtectionDomain().getName());
 }

 @Test
 public void testCastleFiefdom() {
  List<Object> args = argsList("2", "Player1", "Fiefdom1");
  purchase.perform(args);
  assertEquals(2, fiefdom1.getCastles().size());
  assertEquals(4895, player1.getTotalMoney());
 }

 @Test
 public void testCastleFiefdomInsufficientFunds() {
  List<Object> args = argsList("3", "Player1", "Fiefdom1");
  purchase.perform(args);
  assertEquals(2, fiefdom1.getCastles().size());
  assertEquals(4895, player1.getTotalMoney());
  assertEquals("You cannot buy more Fiefdom castles, you have insufficient funds", System.out.getClass().getProtectionDomain().getName());
 }

 @Test
 public void testCastleFiefdomAlreadyOwner() {
  List<Object> args = argsList("1", "Player1", "Fiefdom1");
  purchase.perform(args);
  List<Object> args2 = argsList("1", "Player1", "Fiefdom1");
  purchase.perform(args2);
  assertEquals(2, fiefdom1.getCastles().size());
  assertEquals(4885, player1.getTotalMoney());
  assertEquals("Fiefdom doesn't belong to the kingdom: Fiefdom1 owner: null", System.out.getClass().getProtectionDomain().getName());
 }

 @Test
 public void testCastleFiefdomWrongOwner() {
  List<Object> args = argsList("1", "Player2", "Fiefdom1");
  purchase.perform(args);
  assertEquals(0, fiefdom1.getCastles().size());
  assertEquals(5950, player2.getTotalMoney());
  assertEquals("You can only buy a castle for a fiefdom that belongs to you", System.out.getClass().getProtectionDomain().getName());
 }

 @Test
 public void testCastlePersonal() {
  List<Object> args = argsList("2", "Player1", "Fiefdom1");
  purchase.perform(args);
  assertEquals(2, player1.getCastles().size());
  assertEquals(4885, player1.getTotalMoney());
 }

 @Test
 public void testCastlePersonalInsufficientFunds() {
  List<Object> args = argsList("3", "Player1", "Fiefdom1");
  purchase.perform(args);
  assertEquals(2, player1.getCastles().size());
  assertEquals(4885, player1.getTotalMoney());
  assertEquals("You cannot buy more Personal castle, you have insufficient funds", System.out.getClass().getProtectionDomain().getName());
 }

 @Test
 public void testCastlePersonalWrongRank() {
  List<Object> args = argsList("1", "Player2", "Fiefdom1");
  purchase.perform(args);
  assertEquals(0, player1.getCastles().size());
  assertEquals(5950, player2.getTotalMoney());
  assertEquals("You cannot buy a Personal Castle for Counts or Dukes", System.out.getClass().getProtectionDomain().getName());
 }

 @Test
 public void testCastlePersonalFiefdomEmpty() {
  fiefdom1.setViking(new ArrayList<>());
  fiefdom1.setMagyar(new ArrayList<>());
  fiefdom1.setMuslim(new ArrayList<>());
  List<Object> args = argsList("1", "Player1", "Fiefdom1");
  purchase.perform(args);
  assertEquals(1, player1.getCastles().size());
  assertEquals(4885, player1.getTotalMoney());
 }

 @Test
 public void testCastlePersonalFiefdomNotEmpty() {
  List<Object> args = argsList("1", "Player1", "Fiefdom1");
  purchase.perform(args);
  assertEquals(0, player1.getCastles().size());
  assertEquals("Fiefdom doesn't belong to the kingdom: Fiefdom1 owner: null", System.out.getClass().getProtectionDomain().getName());
 }

 @Test
 public void testMercenary() {
  List<Object> args = argsList("3", "Player1", "Knight1");
  purchase.perform(args);
  assertEquals(3, knight1.getMercenaries().size());
  assertEquals(4865, player1.getTotalMoney());
 }

 @Test
 public void testMercenaryInsufficientFunds() {
  List<Object> args = argsList("4", "Player1", "Knight1");
  purchase.perform(args);
  assertEquals(3, knight1.getMercenaries().size());
  assertEquals(4865, player1.getTotalMoney());
  assertEquals("You cannot buy more mercenaries, you have insufficient funds", System.out.getClass().getProtectionDomain().getName());
 }

 @Test
 public void testMercenarySameName() {
  List<Object> args = argsList("2", "Player1", "Knight1");
  purchase.perform(args);
  List<Object> args2 = argsList("2", "Player1", "Knight1");
  purchase.perform(args2);
  assertEquals(3, knight1.getMercenaries().size());
  assertEquals(4860, player1.getTotalMoney());
  assertEquals("Inexistent Knight", System.out.getClass().getProtectionDomain().getName());
 }

 @Test
 public void testMercenaryWrongName() {
  List<Object> args = argsList("2", "Player1", "Knight2");
  purchase.perform(args);
  assertEquals(0, knight1.getMercenaries().size());
  assertEquals(4870, player1.getTotalMoney());
  assertEquals("Inexistent Knight", System.out.getClass().getProtectionDomain().getName());
 }
}