package src.test.java;

import org.junit.Test;
import static org.junit.Assert.*;
import src.Purchase;
import src.Fiefdoms;
import src.CurrentPlayers;
import src.Knight;
import java.util.*;

public class PurchaseTest {

 @Test
 public void testVassal() {
 Purchase purchase = new Purchase();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 Fiefdoms fiefdom = new Fiefdoms();
 Knight buyerObj = new Knight("John", "knight", 500);
 Knight vassal = new Knight("Alice", "vassal", 300);
 currentPlayers.addPlayer(buyerObj.getName(), buyerObj);
 currentPlayers.addPlayer(vassal.getName(), vassal);
 fiefdom.setOwner("John");
 fiefdom.setName("TestFiefdom");
 List<Object> args = new ArrayList<>();
 args.add(new Integer(2));
 args.add(buyerObj.getName());
 args.add(fiefdom.getName());
 purchase.perform(args);
 assertEquals(2, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalInsufficientFunds() {
 Purchase purchase = new Purchase();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 Fiefdoms fiefdom = new Fiefdoms();
 Knight buyerObj = new Knight("John", "knight", 500);
 currentPlayers.addPlayer(buyerObj.getName(), buyerObj);
 fiefdom.setOwner("John");
 fiefdom.setName("TestFiefdom");
 List<Object> args = new ArrayList<>();
 args.add(new Integer(3));
 args.add(buyerObj.getName());
 args.add(fiefdom.getName());
 purchase.perform(args);
 assertEquals(2, fiefdom.getLoyalVassals());
 assertEquals("You cannot buy more vassals, you have insufficient funds", System.out.getMessage());
 }

 @Test
 public void testVassalNotOwnedFiefdom() {
 Purchase purchase = new Purchase();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 Fiefdoms fiefdom = new Fiefdoms();
 Knight buyerObj = new Knight("John", "knight", 500);
 currentPlayers.addPlayer(buyerObj.getName(), buyerObj);
 fiefdom.setOwner("Alice");
 fiefdom.setName("TestFiefdom");
 List<Object> args = new ArrayList<>();
 args.add(new Integer(2));
 args.add(buyerObj.getName());
 args.add(fiefdom.getName());
 purchase.perform(args);
 assertEquals(0, fiefdom.getLoyalVassals());
 assertEquals("Fiefdom doesn't belong to the kingdom", System.out.getMessage());
 }

 @Test
 public void testVassalNotOwnedByKing() {
 Purchase purchase = new Purchase();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 Fiefdoms fiefdom = new Fiefdoms();
 Knight buyerObj = new Knight("John", "knight", 500);
 Knight anotherPlayer = new Knight("Alice", "king", 1000);
 currentPlayers.addPlayer(buyerObj.getName(), buyerObj);
 currentPlayers.addPlayer(anotherPlayer.getName(), anotherPlayer);
 fiefdom.setOwner("Alice");
 fiefdom.setName("TestFiefdom");
 List<Object> args = new ArrayList<>();
 args.add(new Integer(2));
 args.add(buyerObj.getName());
 args.add(fiefdom.getName());
 purchase.perform(args);
 assertEquals(0, fiefdom.getLoyalVassals());
 assertEquals("You can only buy a castle for a fiefdom that belongs to you", System.out.getMessage());
 }

 @Test
 public void testCastleFiefdom() {
 Purchase purchase = new Purchase();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 Fiefdoms fiefdom = new Fiefdoms();
 Knight buyerObj = new Knight("John", "king", 2500);
 currentPlayers.addPlayer(buyerObj.getName(), buyerObj);
 fiefdom.setOwner("John");
 fiefdom.setName("TestFiefdom");
 List<Object> args = new ArrayList<>();
 args.add(new Integer(2));
 args.add(buyerObj.getName());
 args.add(fiefdom.getName());
 purchase.perform(args);
 assertEquals(2, fiefdom.getCastles());
 }

 @Test
 public void testCastleFiefdomInsufficientFunds() {
 Purchase purchase = new Purchase();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 Fiefdoms fiefdom = new Fiefdoms();
 Knight buyerObj = new Knight("John", "king", 2500);
 currentPlayers.addPlayer(buyerObj.getName(), buyerObj);
 fiefdom.setOwner("John");
 fiefdom.setName("TestFiefdom");
 List<Object> args = new ArrayList<>();
 args.add(new Integer(3));
 args.add(buyerObj.getName());
 args.add(fiefdom.getName());
 purchase.perform(args);
 assertEquals(2, fiefdom.getCastles());
 assertEquals("You cannot buy more Fiefdom castles, you have insufficient funds", System.out.getMessage());
 }

 @Test
 public void testCastleFiefdomNotOwned() {
 Purchase purchase = new Purchase();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 Fiefdoms fiefdom = new Fiefdoms();
 Knight buyerObj = new Knight("John", "king", 2500);
 currentPlayers.addPlayer(buyerObj.getName(), buyerObj);
 fiefdom.setOwner("Alice");
 fiefdom.setName("TestFiefdom");
 List<Object> args = new ArrayList<>();
 args.add(new Integer(2));
 args.add(buyerObj.getName());
 args.add(fiefdom.getName());
 purchase.perform(args);
 assertEquals(0, fiefdom.getCastles());
 assertEquals("Fiefdom doesn't belong to the kingdom", System.out.getMessage());
 }

 @Test
 public void testCastleFiefdomNotOwnedByKing() {
 Purchase purchase = new Purchase();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 Fiefdoms fiefdom = new Fiefdoms();
 Knight buyerObj = new Knight("John", "king", 2500);
 Knight anotherPlayer = new Knight("Alice", "count", 1000);
 currentPlayers.addPlayer(buyerObj.getName(), buyerObj);
 currentPlayers.addPlayer(anotherPlayer.getName(), anotherPlayer);
 fiefdom.setOwner("Alice");
 fiefdom.setName("TestFiefdom");
 List<Object> args = new ArrayList<>();
 args.add(new Integer(2));
 args.add(buyerObj.getName());
 args.add(fiefdom.getName());
 purchase.perform(args);
 assertEquals(0, fiefdom.getCastles());
 assertEquals("You can only buy a castle for a fiefdom that belongs to you", System.out.getMessage());
 }

 @Test
 public void testCastlePersonal() {
 Purchase purchase = new Purchase();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 Fiefdoms fiefdom = new Fiefdoms();
 Knight buyerObj = new Knight("John", "king", 5000);
 currentPlayers.addPlayer(buyerObj.getName(), buyerObj);
 fiefdom.setOwner("John");
 fiefdom.setName("TestFiefdom");
 List<Object> args = new ArrayList<>();
 args.add(new Integer(2));
 args.add(buyerObj.getName());
 args.add(fiefdom.getName());
 purchase.perform(args);
 assertEquals(2, buyerObj.getCastles());
 }

 @Test
 public void testCastlePersonalInsufficientFunds() {
 Purchase purchase = new Purchase();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 Fiefdoms fiefdom = new Fiefdoms();
 Knight buyerObj = new Knight("John", "king", 5000);
 currentPlayers.addPlayer(buyerObj.getName(), buyerObj);
 fiefdom.setOwner("John");
 fiefdom.setName("TestFiefdom");
 List<Object> args = new ArrayList<>();
 args.add(new Integer(3));
 args.add(buyerObj.getName());
 args.add(fiefdom.getName());
 purchase.perform(args);
 assertEquals(2, buyerObj.getCastles());
 assertEquals("You cannot buy more Personal castle, you have insufficient funds", System.out.getMessage());
 }

 @Test
 public void testCastlePersonalNotOwnedByKing() {
 Purchase purchase = new Purchase();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 Fiefdoms fiefdom = new Fiefdoms();
 Knight buyerObj = new Knight("John", "king", 5000);
 Knight anotherPlayer = new Knight("Alice", "count", 1000);
 currentPlayers.addPlayer(buyerObj.getName(), buyerObj);
 currentPlayers.addPlayer(anotherPlayer.getName(), anotherPlayer);
 fiefdom.setOwner("Alice");
 fiefdom.setName("TestFiefdom");
 List<Object> args = new ArrayList<>();
 args.add(new Integer(2));
 args.add(buyerObj.getName());
 args.add(fiefdom.getName());
 purchase.perform(args);
 assertEquals(0, buyerObj.getCastles());
 assertEquals("You cannot buy a Personal Castle for Counts or Dukes", System.out.getMessage());
 }

 @Test
 public void testCastlePersonalNotOwnedFiefdom() {
 Purchase purchase = new Purchase();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 Fiefdoms fiefdom = new Fiefdoms();
 Knight buyerObj = new Knight("John", "king", 5000);
 currentPlayers.addPlayer(buyerObj.getName(), buyerObj);
 fiefdom.setOwner("Alice");
 fiefdom.setName("TestFiefdom");
 List<Object> args = new ArrayList<>();
 args.add(new Integer(2));
 args.add(buyerObj.getName());
 args.add(fiefdom.getName());
 purchase.perform(args);
 assertEquals(0, buyerObj.getCastles());
 assertEquals("Fiefdom doesn't belong to the kingdom", System.out.getMessage());
 }

 @Test
 public void testMercenary() {
 Purchase purchase = new Purchase();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 Knight buyerObj = new Knight("John", "king", 10000);
 Knight knight = new Knight("Alice", "knight", 500);
 currentPlayers.addPlayer(buyerObj.getName(), buyerObj);
 currentPlayers.addPlayer(knight.getName(), knight);
 List<Object> args = new ArrayList<>();
 args.add(new Integer(3));
 args.add(buyerObj.getName());
 args.add(knight.getName());
 purchase.perform(args);
 assertEquals(3, knight.getMercenaries());
 }

 @Test
 public void testMercenaryInsufficientFunds() {
 Purchase purchase = new Purchase();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 Knight buyerObj = new Knight("John", "king", 10000);
 Knight knight = new Knight("Alice", "knight", 500);
 currentPlayers.addPlayer(buyerObj.getName(), buyerObj);
 currentPlayers.addPlayer(knight.getName(), knight);
 List<Object> args = new ArrayList<>();
 args.add(new Integer(4));
 args.add(buyerObj.getName());
 args.add(knight.getName());
 purchase.perform(args);
 assertEquals(3, knight.getMercenaries());
 assertEquals("You cannot buy more mercenaries, you have insufficient funds", System.out.getMessage());
 }

 @Test
 public void testMercenarySamePlayer() {
 Purchase purchase = new Purchase();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 Knight buyerObj = new Knight("John", "king", 10000);
 Knight knight = new Knight("John", "knight", 500);
 currentPlayers.addPlayer(buyerObj.getName(), buyerObj);
 currentPlayers.addPlayer(knight.getName(), knight);
 List<Object> args = new ArrayList<>();
 args.add(new Integer(3));
 args.add(buyerObj.getName());
 args.add(knight.getName());
 purchase.perform(args);
 assertEquals(3, knight.getMercenaries());
 }

 @Test
 public void testMercenaryNotOwnedPlayer() {
 Purchase purchase = new Purchase();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 Knight buyerObj = new Knight("John", "king", 10000);
 Knight knight = new Knight("Alice", "knight", 500);
 currentPlayers.addPlayer(buyerObj.getName(), buyerObj);
 currentPlayers.addPlayer(knight.getName(), knight);
 List<Object> args = new ArrayList<>();
 args.add(new Integer(3));
 args.add(buyerObj.getName());
 args.add(knight.getName());
 purchase.perform(args);
 assertEquals(0, knight.getMercenaries());
 assertEquals("Inexistent Knight", System.out.getMessage());
 }
}