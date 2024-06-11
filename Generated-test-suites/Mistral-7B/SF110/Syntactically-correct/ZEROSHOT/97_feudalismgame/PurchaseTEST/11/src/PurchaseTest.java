package src.test.java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import src.Actions;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;
import src.Purchase;
import src.Player;

public class PurchaseTest {

 private Purchase purchase;
 private List<String> argsList;
 private Map<String, Fiefdoms> map;
 private GameAutoActions gameAutoActions;
 private List<Player> currentPlayers;

 @Before
 public void setUp() {
 map = new HashMap<>();
 gameAutoActions = new GameAutoActions();
 currentPlayers = new ArrayList<>();
 purchase = new Purchase();
 argsList = new ArrayList<>();
 }

 @Test
 public void testPerform() {
 // Test with valid arguments
 argsList.add("vassal");
 argsList.add(new ArrayList<String>() {{
 add("1");
 add("player1");
 add("fiefdom1");
 }}.iterator());
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, 0, 0, null));
 currentPlayers.add(new Knight("player1", "knight", 1000));
 currentPlayers.add(new Player("player2", "king", 5000, null));

 purchase.perform(argsList);
 assertTrue(map.get("fiefdom1").getLoyalVassals() == 1);
 assertTrue(currentPlayers.get(0).getTotalMoney() == 999);

 // Test with invalid arguments
 argsList.clear();
 argsList.add("invalidMethod");
 purchase.perform(argsList);
 fail("Invalid method call should have thrown an exception");
 }

 @Test
 public void testVassal() {
 // Test with sufficient funds
 argsList.clear();
 argsList.add("vassal");
 argsList.add(new ArrayList<String>() {{
 add("2");
 add("player1");
 add("fiefdom1");
 }}.iterator());
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, 0, 0, null));
 currentPlayers.set(0, new Knight("player1", "knight", 2000));

 purchase.perform(argsList);
 assertTrue(map.get("fiefdom1").getLoyalVassals() == 2);
 assertTrue(currentPlayers.get(0).getTotalMoney() == 1998);

 // Test with insufficient funds
 argsList.clear();
 argsList.add("vassal");
 argsList.add(new ArrayList<String>() {{
 add("3");
 add("player1");
 add("fiefdom1");
 }}.iterator());
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, 0, 0, null));
 currentPlayers.set(0, new Knight("player1", "knight", 1000));

 purchase.perform(argsList);
 assertFalse(map.get("fiefdom1").getLoyalVassals() > 1);
 assertTrue(currentPlayers.get(0).getTotalMoney() == 1000);

 // Test with non-existent fiefdom
 argsList.clear();
 argsList.add("vassal");
 argsList.add(new ArrayList<String>() {{
 add("1");
 add("player1");
 add("inexistentFiefdom");
 }}.iterator());
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, 0, 0, null));
 currentPlayers.set(0, new Knight("player1", "knight", 1500));

 purchase.perform(argsList);
 assertFalse(map.get("fiefdom1").getLoyalVassals() > 0);
 assertTrue(currentPlayers.get(0).getTotalMoney() == 1500);
 }

 @Test
 public void testCastleFiefdom() {
 // Test with sufficient funds
 argsList.clear();
 argsList.add("castleFiefdom");
 argsList.add(new ArrayList<String>() {{
 add("2");
 add("player1");
 add("fiefdom1");
 }}.iterator());
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, 0, 0, null));
 currentPlayers.set(0, new Knight("player1", "knight", 5000));

 purchase.perform(argsList);
 assertTrue(map.get("fiefdom1").getCastles() == 2);
 assertTrue(currentPlayers.get(0).getTotalMoney() == 4998);

 // Test with insufficient funds
 argsList.clear();
 argsList.add("castleFiefdom");
 argsList.add(new ArrayList<String>() {{
 add("3");
 add("player1");
 add("fiefdom1");
 }}.iterator());
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, 0, 0, null));
 currentPlayers.set(0, new Knight("player1", "knight", 2500));

 purchase.perform(argsList);
 assertFalse(map.get("fiefdom1").getCastles() > 1);
 assertTrue(currentPlayers.get(0).getTotalMoney() == 2500);

 // Test with non-existent fiefdom
 argsList.clear();
 argsList.add("castleFiefdom");
 argsList.add(new ArrayList<String>() {{
 add("1");
 add("player1");
 add("inexistentFiefdom");
 }}.iterator());
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, 0, 0, null));
 currentPlayers.set(0, new Knight("player1", "knight", 7500));

 purchase.perform(argsList);
 assertFalse(map.get("fiefdom1").getCastles() > 0);
 assertTrue(currentPlayers.get(0).getTotalMoney() == 7500);
 }

 @Test
 public void testCastlePersonal() {
 // Test with sufficient funds
 argsList.clear();
 argsList.add("castlePersonal");
 argsList.add(new ArrayList<String>() {{
 add("1");
 add("player1");
 add("player2Fiefdom");
 }}.iterator());
 map.put("player2Fiefdom", new Fiefdoms("player2Fiefdom", null, null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, 0, 0, null));
 currentPlayers.set(0, new Knight("player1", "knight", 10000));
 currentPlayers.set(1, new Player("player2", "knight", 0, null));

 purchase.perform(argsList);
 assertTrue(map.get("player2Fiefdom").getPersonalCastles() == 1);
 assertTrue(currentPlayers.get(0).getTotalMoney() == 9970);

 // Test with insufficient funds
 argsList.clear();
 argsList.add("castlePersonal");
 argsList.add(new ArrayList<String>() {{
 add("2");
 add("player1");
 add("player2Fiefdom");
 }}.iterator());
 map.put("player2Fiefdom", new Fiefdoms("player2Fiefdom", null, null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, 0, 0, null));
 currentPlayers.set(0, new Knight("player1", "knight", 7500));
 currentPlayers.set(1, new Player("player2", "knight", 0, null));

 purchase.perform(argsList);
 assertFalse(map.get("player2Fiefdom").getPersonalCastles() > 0);
 assertTrue(currentPlayers.get(0).getTotalMoney() == 7500);

 // Test with non-existent fiefdom
 argsList.clear();
 argsList.add("castlePersonal");
 argsList.add(new ArrayList<String>() {{
 add("1");
 add("player1");
 add("inexistentFiefdom");
 }}.iterator());
 map.put("player2Fiefdom", new Fiefdoms("player2Fiefdom", null, null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, 0, 0, null));
 currentPlayers.set(0, new Knight("player1", "knight", 12500));
 currentPlayers.set(1, new Player("player2", "knight", 0, null));

 purchase.perform(argsList);
 assertFalse(map.get("player2Fiefdom").getPersonalCastles() > 0);
 assertTrue(currentPlayers.get(0).getTotalMoney() == 12500);
 }

 @Test
 public void testMercenary() {
 // Test with sufficient funds
 argsList.clear();
 argsList.add("mercenary");
 argsList.add(new ArrayList<String>() {{
 add("2");
 add("player1");
 add("knight1");
 }}.iterator());
 currentPlayers.set(0, new Knight("player1", "knight", 15000));
 currentPlayers.add(new Knight("knight1", "knight", 500));

 purchase.perform(argsList);
 assertTrue(currentPlayers.get(1).getMercenaries() == 2);
 assertTrue(currentPlayers.get(0).getTotalMoney() == 14985);

 // Test with insufficient funds
 argsList.clear();
 argsList.add("mercenary");
 argsList.add(new ArrayList<String>() {{
 add("3");
 add("player1");
 add("knight1");
 }}.iterator());
 currentPlayers.set(0, new Knight("player1", "knight", 10000));
 currentPlayers.add(new Knight("knight1", "knight", 500));

 purchase.perform(argsList);
 assertFalse(currentPlayers.get(1).getMercenaries() > 1);
 assertTrue(currentPlayers.get(0).getTotalMoney() == 10000);

 // Test with non-existent knight
 argsList.clear();
 argsList.add("mercenary");
 argsList.add(new ArrayList<String>() {{
 add("1");
 add("player1");
 add("inexistentKnight");
 }}.iterator());
 currentPlayers.set(0, new Knight("player1", "knight", 12000));

 purchase.perform(argsList);
 assertFalse(currentPlayers.get(2).getMercenaries() > 0);
 assertTrue(currentPlayers.get(0).getTotalMoney() == 12000);
 }

 @After
 public void tearDown() {
 map.clear();
 gameAutoActions.clearAll();
 currentPlayers.clear();
 }
}