package src.test.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class PurchaseTest {

 private Purchase purchase;
 private Map<String, Fiefdoms> map;
 private HashMap<String, Knight> currentPlayers;

 @BeforeEach
 public void setup() {
 purchase = new Purchase();
 map = new HashMap<>();
 currentPlayers = new HashMap<>();
 }

 @Test
 public void testPerform() {
 // Test with valid arguments
 List<Object> args = new ArrayList<>();
 args.add("vassal");
 args.add(getIterator());
 performTest(args);

 // Test with invalid method name
 args.clear();
 args.add("invalidMethod");
 assertThrows(NoSuchMethodException.class, () -> perform(args));

 // Test with empty arguments
 args.clear();
 assertThrows(IllegalArgumentException.class, () -> perform(args));
 }

 @Test
 public void testVassal() {
 // Test buying vassals for a king
 testBuyVassals("king", 10, "JohnDoe", "JohnsFiefdom");

 // Test buying vassals for a count with a fiefdom that belongs to him
 testBuyVassals("count", 5, "JohnDoe", "JohnsFiefdom");

 // Test buying vassals for a count with a fiefdom that doesn't belong to him
 testBuyVassals("count", 5, "JohnDoe", "AnotherPlayerFiefdom");

 // Test buying vassals for a player with insufficient funds
 testBuyVassals("king", 11, "JohnDoe", "JohnsFiefdom");
 }

 @Test
 public void testCastleFiefdom() {
 // Test buying castles for a king
 testBuyCastles("king", 3, "JohnDoe", "JohnsFiefdom");

 // Test buying castles for a count with a fiefdom that belongs to him and no existing castles
 testBuyCastles("count", 2, "JohnDoe", "JohnsFiefdom");

 // Test buying castles for a count with a fiefdom that belongs to him and existing castles
 testBuyCastles("count", 2, "JohnDoe", "JohnsFiefdom");

 // Test buying castles for a player with insufficient funds
 testBuyCastles("king", 4, "JohnDoe", "JohnsFiefdom");
 }

 @Test
 public void testCastlePersonal() {
 // Test buying personal castles for a king
 testBuyPersonalCastles("king", 2, "JohnDoe", "JohnsFiefdom");

 // Test buying personal castles for a count with a fiefdom that belongs to him and no existing castles
 testBuyPersonalCastles("count", 1, "JohnDoe", "JohnsFiefdom");

 // Test buying personal castles for a count with a fiefdom that belongs to him and existing castles
 testBuyPersonalCastles("count", 1, "JohnDoe", "JohnsFiefdom");

 // Test buying personal castles for a player with insufficient funds
 testBuyPersonalCastles("king", 2, "JohnDoe", "JohnsFiefdom");
 }

 @Test
 public void testMercenary() {
 // Test buying mercenaries for a king
 testBuyMercenaries("king", 3, "JohnDoe", "KnightJohn");

 // Test buying mercenaries for a knight with the same name as the buyer
 testBuyMercenaries("king", 2, "JohnDoe", "JohnDoe");

 // Test buying mercenaries for a knight with a different name
 testBuyMercenaries("king", 3, "JohnDoe", "KnightOther");

 // Test buying mercenaries for a player with insufficient funds
 testBuyMercenaries("king", 4, "JohnDoe", "KnightOther");
 }

 private void performTest(List<Object> args) {
 try {
 purchase.perform(args);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Exception was thrown: " + e.getMessage());
 }
 }

 private void testBuyVassals(String rank, int qty, String buyerName, String fiefdomName) {
 setUpFiefdomAndPlayer(rank, fiefdomName, buyerName);
 performTest(getArgsForVassal(qty, buyerName, fiefdomName));
 }

 private void testBuyCastles(String rank, int qty, String buyerName, String fiefdomName) {
 setUpFiefdomAndPlayer(rank, fiefdomName, buyerName);
 performTest(getArgsForCastle(qty, buyerName, fiefdomName));
 }

 private void testBuyPersonalCastles(String rank, int qty, String buyerName, String fiefdomName) {
 setUpFiefdomAndPlayer(rank, fiefdomName, buyerName);
 performTest(getArgsForPersonalCastle(qty, buyerName, fiefdomName));
 }

 private void testBuyMercenaries(String rank, int qty, String buyerName, String knightName) {
 setUpFiefdomAndPlayers(rank, buyerName, knightName);
 performTest(getArgsForMercenary(qty, buyerName, knightName));
 }

 private void setUpFiefdomAndPlayer(String rank, String fiefdomName, String buyerName) {
 map.put(fiefdomName, new Fiefdoms(fiefdomName, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
 currentPlayers.put(buyerName, new Knight(buyerName, rank, 1000));
 map.get(fiefdomName).setOwner(currentPlayers.get(buyerName));
 }

 private void setUpFiefdomAndPlayers(String rank, String buyerName, String knightName) {
 map.put(fiefdomName, new Fiefdoms(fiefdomName, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
 currentPlayers.put(buyerName, new Knight(buyerName, rank, 1000));
 currentPlayers.put(knightName, new Knight(knightName, "knight", 500));
 map.get(fiefdomName).setOwner(currentPlayers.get(buyerName));
 }

 private Iterator<Object> getIterator() {
 return Collections.emptyIterator();
 }

 private List<Object> getArgsForVassal(int qty, String buyerName, String fiefdomName) {
 List<Object> args = new ArrayList<>();
 args.add("vassal");
 args.add(getIterator());
 args.add(getIterator());
 args.add(fiefdomName);
 args.add(currentPlayers.get(buyerName));
 args.add(getIterator());
 return args;
 }

 private List<Object> getArgsForCastle(int qty, String buyerName, String fiefdomName) {
 List<Object> args = new ArrayList<>();
 args.add("castleFiefdom");
 args.add(getIterator());
 args.add(qty);
 args.add(buyerName);
 args.add(fiefdomName);
 args.add(currentPlayers.get(buyerName));
 return args;
 }

 private List<Object> getArgsForPersonalCastle(int qty, String buyerName, String fiefdomName) {
 List<Object> args = new ArrayList<>();
 args.add("castlePersonal");
 args.add(getIterator());
 args.add(qty);
 args.add(buyerName);
 args.add(fiefdomName);
 args.add(currentPlayers.get(buyerName));
 return args;
 }

 private List<Object> getArgsForMercenary(int qty, String buyerName, String knightName) {
 List<Object> args = new ArrayList<>();
 args.add("mercenary");
 args.add(getIterator());
 args.add(qty);
 args.add(buyerName);
 args.add(currentPlayers.get(knightName));
 args.add(currentPlayers.get(buyerName));
 return args;
 }
}