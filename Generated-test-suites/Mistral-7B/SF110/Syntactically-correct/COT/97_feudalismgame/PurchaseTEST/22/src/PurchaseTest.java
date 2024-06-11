package src.test.java;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import src.Actions;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;
import src.Purchase;

public class PurchaseTest {

 private Purchase purchase;
 private Actions actions;
 private GameAutoActions gameAutoActions;
 private Map map;
 private List<Fiefdoms> mapList;
 private List<Knight> currentPlayers;

 @Before
 public void setUp() {
 purchase = new Purchase();
 actions = mock(Actions.class);
 gameAutoActions = mock(GameAutoActions.class);
 map = mock(Map.class);
 mapList = new ArrayList<>();
 currentPlayers = new ArrayList<>();
 when(map.map).thenReturn(mapList);
 when(actions.getClass()).thenReturn(actions.getClass());
 when(gameAutoActions.getClass()).thenReturn(gameAutoActions.getClass());
 }

 @After
 public void tearDown() {
 purchase = null;
 actions = null;
 gameAutoActions = null;
 map = null;
 mapList = null;
 currentPlayers = null;
 }

 @Test
 public void testPerform() {
 Iterator<Object> argIterator = Collections.emptyIterator().iterator();
 purchase.perform(argIterator);
 // Check if no exception is thrown
 }

 @Test(expected = SecurityException.class)
 public void testPerformWithSecurityException() {
 Iterator<Object> argIterator = Collections.emptyIterator().iterator();
 when(map.map.get(argIterator.next())).thenThrow(new SecurityException());
 purchase.perform(argIterator);
 }

 @Test(expected = NoSuchMethodException.class)
 public void testPerformWithNoSuchMethodException() {
 Iterator<Object> argIterator = Collections.emptyIterator().iterator();
 when(map.map.get(argIterator.next())).thenReturn(null);
 purchase.perform(argIterator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithIllegalArgumentException() {
 Iterator<Object> argIterator = Collections.emptyIterator().iterator();
 when(map.map.get(argIterator.next())).thenReturn(mapList.get(0));
 when(mapList.get(0).getOwner()).thenReturn(null);
 purchase.perform(argIterator);
 }

 @Test(expected = IllegalAccessException.class)
 public void testPerformWithIllegalAccessException() {
 Iterator<Object> argIterator = Collections.emptyIterator().iterator();
 when(map.map.get(argIterator.next())).thenReturn(mapList.get(0));
 when(mapList.get(0).getOwner()).thenReturn(mock(Knight.class));
 when(mapList.get(0).getOwner()).thenReturn(null); // Double check
 purchase.perform(argIterator);
 }

 @Test(expected = InvocationTargetException.class)
 public void testPerformWithInvocationTargetException() {
 Iterator<Object> argIterator = Collections.emptyIterator().iterator();
 when(map.map.get(argIterator.next())).thenThrow(new InvocationTargetException(new RuntimeException()));
 purchase.perform(argIterator);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testPerformWithClassNotFoundException() {
 Iterator<Object> argIterator = Collections.emptyIterator().iterator();
 when(map.map.get(argIterator.next())).thenThrow(new ClassNotFoundException());
 purchase.perform(argIterator);
 }

 @Test
 public void testVassal() {
 vassalTest("vassal", 5, "player1", "player1Fiefdom");
 }

 @Test
 public void testVassalWithFiefdomNotBelongingToTheKingdom() {
 vassalTest("vassal", 5, "player1", "enemyFiefdom");
 }

 @Test
 public void testVassalWithInsufficientFunds() {
 vassalTest("vassal", 10, "player1", "player1Fiefdom");
 }

 @Test
 public void testVassalWithFiefdomAlreadyBelongsToABuyer() {
 vassalTest("vassal", 5, "player2", "player1Fiefdom");
 }

 @Test
 public void testCastleFiefdom() {
 castleFiefdomTest("castleFiefdom", 5, "player1", "player1Fiefdom");
 }

 @Test
 public void testCastleFiefdomWithFiefdomNotBelongingToTheKingdom() {
 castleFiefdomTest("castleFiefdom", 5, "player1", "enemyFiefdom");
 }

 @Test
 public void testCastleFiefdomWithInsufficientFunds() {
 castleFiefdomTest("castleFiefdom", 10, "player1", "player1Fiefdom");
 }

 @Test
 public void testCastleFiefdomWithFiefdomAlreadyBelongsToABuyer() {
 castleFiefdomTest("castleFiefdom", 5, "player2", "player1Fiefdom");
 }

 @Test
 public void testCastlePersonal() {
 castlePersonalTest("castlePersonal", 5, "player1", "player1Fiefdom");
 }

 @Test
 public void testCastlePersonalWithFiefdomNotBelongingToTheKingdom() {
 castlePersonalTest("castlePersonal", 5, "player1", "enemyFiefdom");
 }

 @Test
 public void testCastlePersonalWithInsufficientFunds() {
 castlePersonalTest("castlePersonal", 10, "player1", "player1Fiefdom");
 }

 @Test
 public void testCastlePersonalWithKingBuyingForACountOrADuke() {
 castlePersonalTest("castlePersonal", 5, "king", "player1Fiefdom");
 }

 @Test
 public void testCastlePersonalWithCountOrDukeBuyingForThemselves() {
 castlePersonalTest("castlePersonal", 5, "count", "player1Fiefdom");
 }

 @Test
 public void testMercenary() {
 mercenaryTest("mercenary", 5, "player1", "knight1");
 }

 @Test
 public void testMercenaryWithInexistentKnight() {
 mercenaryTest("mercenary", 5, "player1", "inexistentKnight");
 }

 @Test
 public void testMercenaryWithInsufficientFunds() {
 mercenaryTest("mercenary", 10, "player1", "knight1");
 }

 @Test
 public void testMercenaryWithBuyerBeingTheSameAsKnight() {
 mercenaryTest("mercenary", 5, "player1", "player1");
 }

 private void vassalTest(String testName, int qty, String buyerName, String fiefdomName) {
 final Fiefdoms fiefdom = new Fiefdoms();
 fiefdom.setName(fiefdomName);
 fiefdom.setOwner(mock(Knight.class));
 mapList.add(fiefdom);
 final Knight buyer = new Knight();
 buyer.setName(buyerName);
 buyer.setRank("king");
 currentPlayers.add(buyer);
 final Knight vassal = new Knight();
 vassal.setName("vassal");
 when(map.map.get(argIterator.next())).thenReturn(mapList.get(0));
 when(mapList.get(0).getOwner()).thenReturn(mock(Knight.class));
 when(mapList.get(0).getOwner()).thenReturn(null); // Double check
 when(mapList.get(0).getVassals().size()).thenReturn(10);
 when(mapList.get(0).getLoyalVassals().size()).thenReturn(5);
 when(CurrentPlayers.currentPlayers.get(buyerName)).thenReturn(buyer);
 when(CurrentPlayers.currentPlayers.get(fiefdomName)).thenReturn(mapList.get(0).getOwner());
 purchase.perform(argIterator);
 assertFalse(buyer.getTotalMoney() > 0);
 purchase.vassal(argIterator);
 assertTrue(buyer.getTotalMoney() < 0);
 assertFalse(fiefdom.getLoyalVassals().size() > 5);
 assertFalse(vassal.isAlive());
 }

 private void castleFiefdomTest(String testName, int qty, String buyerName, String fiefdomName) {
 final Fiefdoms fiefdom = new Fiefdoms();
 fiefdom.setName(fiefdomName);
 fiefdom.setOwner(mock(Knight.class));
 mapList.add(fiefdom);
 final Knight buyer = new Knight();
 buyer.setName(buyerName);
 buyer.setRank("king");
 currentPlayers.add(buyer);
 when(map.map.get(argIterator.next())).thenReturn(mapList.get(0));
 when(mapList.get(0).getOwner()).thenReturn(mock(Knight.class));
 when(mapList.get(0).getOwner()).thenReturn(null); // Double check
 when(mapList.get(0).getVassals().size()).thenReturn(10);
 when(mapList.get(0).getCastle().size()).thenReturn(5);
 when(CurrentPlayers.currentPlayers.get(buyerName)).thenReturn(buyer);
 when(CurrentPlayers.currentPlayers.get(fiefdomName)).thenReturn(mapList.get(0).getOwner());
 purchase.perform(argIterator);
 assertFalse(buyer.getTotalMoney() > 0);
 purchase.castleFiefdom(argIterator);
 assertTrue(buyer.getTotalMoney() < 0);
 assertFalse(fiefdom.getCastle().size() > 5);
 }

 private void castlePersonalTest(String testName, int qty, String buyerName, String fiefdomName) {
 final Fiefdoms fiefdom = new Fiefdoms();
 fiefdom.setName(fiefdomName);
 fiefdom.setOwner(mock(Knight.class));
 mapList.add(fiefdom);
 final Knight buyer = new Knight();
 buyer.setName(buyerName);
 buyer.setRank("king");
 currentPlayers.add(buyer);
 final Knight knight = new Knight();
 knight.setName("knight1");
 currentPlayers.add(knight);
 when(map.map.get(argIterator.next())).thenReturn(mapList.get(0));
 when(mapList.get(0).getOwner()).thenReturn(mock(Knight.class));
 when(mapList.get(0).getOwner()).thenReturn(null); // Double check
 when(mapList.get(0).getVassals().size()).thenReturn(10);
 when(mapList.get(0).getPersonalCastles().size()).thenReturn(5);
 when(CurrentPlayers.currentPlayers.get(buyerName)).thenReturn(buyer);
 when(CurrentPlayers.currentPlayers.get(fiefdomName)).thenReturn(mapList.get(0).getOwner());
 when(CurrentPlayers.currentPlayers.get(knight.getName())).thenReturn(knight);
 purchase.perform(argIterator);
 assertFalse(buyer.getTotalMoney() > 0);
 purchase.castlePersonal(argIterator);
 assertTrue(buyer.getTotalMoney() < 0);
 assertFalse(fiefdom.getPersonalCastles().size() > 5);
 }

 private void mercenaryTest(String testName, int qty, String buyerName, String knightName) {
 final Knight buyer = new Knight();
 buyer.setName(buyerName);
 buyer.setRank("king");
 currentPlayers.add(buyer);
 final Knight knight = new Knight();
 knight.setName(knightName);
 currentPlayers.add(knight);
 when(map.map.get(argIterator.next())).thenReturn(null);
 when(CurrentPlayers.currentPlayers.get(buyerName)).thenReturn(buyer);
 when(CurrentPlayers.currentPlayers.get(knightName)).thenReturn(knight);
 purchase.perform(argIterator);
 assertFalse(buyer.getTotalMoney() > 0);
 purchase.mercenary(argIterator);
 assertTrue(buyer.getTotalMoney() < 0);
 assertTrue(knight.getMercenaries().size() > 0);
 }

}