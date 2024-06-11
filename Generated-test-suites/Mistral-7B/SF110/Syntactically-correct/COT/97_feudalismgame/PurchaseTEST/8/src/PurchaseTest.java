package src.test.java;

import static org.junit.Assert.*;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;
import src.Purchase;
import src.CurrentPlayers;

import java.util.*;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms map;
 private CurrentPlayers currentPlayers;
 private GameAutoActions gameAutoActions;

 @Before
 public void setUp() {
 // Initialize test objects
 purchase = new Purchase();
 map = new Fiefdoms();
 currentPlayers = new CurrentPlayers();
 gameAutoActions = new GameAutoActions();
 }

 @Test
 public void testPerform() {
 // Test empty args
 Collection<Object> emptyArgs = new ArrayList();
 purchase.perform(emptyArgs);
 // Test valid method call
 Collection<Object> validArgs = new ArrayList();
 validArgs.add("vassal");
 validArgs.add(map.iterator());
 purchase.perform(validArgs);
 // Test invalid method call
 Collection<Object> invalidArgs = new ArrayList();
 invalidArgs.add("invalidMethod");
 invalidArgs.add(map.iterator());
 try {
 purchase.perform(invalidArgs);
 fail("Expected IllegalArgumentException or NoSuchMethodException");
 } catch (IllegalArgumentException e) {
 // Expected
 } catch (NoSuchMethodException e) {
 // Expected
 }
 }

 @Test
 public void testVassal() {
 // Test valid call with sufficient funds
 int qty = 1;
 String buyerName = "buyer1";
 Fiefdoms fiefdom = map;
 Knight buyerObj = new Knight("buyer1", "knight", 1000, 0, 0, 0, 0, 0);
 currentPlayers.put(buyerName, buyerObj);
 String fiefdomName = "testFiefdom";
 Fiefdom testFiefdom = new Fiefdom(fiefdomName, null, null, null, null, 0, null, null, null, 0, 0, 0, 0, 0, 0, 0);
 map.put(fiefdomName, testFiefdom);
 Collection<Object> vassalArgs = new ArrayList();
 vassalArgs.add(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 switch (count++) {
 case 0:
 return qty;
 case 1:
 return buyerName;
 case 2:
 return fiefdomName;
 default:
 return null;
 }
 }
 });
 purchase.perform(Arrays.asList(vassalArgs));
 assertEquals(1, testFiefdom.getLoyalVassals());
 assertEquals(999, buyerObj.getTotalMoney());

 // Test insufficient funds
 qty = 2;
 buyerObj.setTotalMoney(950);
 purchase.perform(vassalArgs);
 assertEquals(1, testFiefdom.getLoyalVassals());
 assertEquals(950, buyerObj.getTotalMoney());
 }

 @Test
 public void testCastleFiefdom() {
 // Test valid call with sufficient funds
 int qty = 1;
 String buyerName = "buyer1";
 Fiefdoms fiefdom = map;
 Knight buyerObj = new Knight("buyer1", "knight", 2500, 0, 0, 0, 0, 0);
 currentPlayers.put(buyerName, buyerObj);
 String fiefdomName = "testFiefdom";
 Fiefdom testFiefdom = new Fiefdom(fiefdomName, null, null, null, null, 0, null, null, null, 0, 0, 0, 0, 0, 0, 0);
 map.put(fiefdomName, testFiefdom);
 Collection<Object> castleFiefdomArgs = new ArrayList();
 castleFiefdomArgs.add(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 switch (count++) {
 case 0:
 return qty;
 case 1:
 return buyerName;
 case 2:
 return fiefdomName;
 default:
 return null;
 }
 }
 });
 purchase.perform(Arrays.asList(castleFiefdomArgs));
 assertEquals(1, testFiefdom.getCastles().size());
 assertEquals(2450, buyerObj.getTotalMoney());

 // Test insufficient funds
 buyerObj.setTotalMoney(2400);
 purchase.perform(castleFiefdomArgs);
 assertEquals(1, testFiefdom.getCastles().size());
 assertEquals(2400, buyerObj.getTotalMoney());
 }

 @Test
 public void testCastlePersonal() {
 // Test valid call with sufficient funds
 int qty = 1;
 String buyerName = "buyer1";
 Fiefdoms fiefdom = map;
 Knight buyerObj = new Knight("buyer1", "knight", 1000, 0, 0, 0, 0, 0);
 currentPlayers.put(buyerName, buyerObj);
 String fiefdomName = "testFiefdom";
 Fiefdom testFiefdom = new Fiefdom(fiefdomName, null, null, null, null, 0, null, null, null, 0, 0, 0, 0, 0, 0, 0);
 map.put(fiefdomName, testFiefdom);
 Collection<Object> castlePersonalArgs = new ArrayList();
 castlePersonalArgs.add(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 switch (count++) {
 case 0:
 return qty;
 case 1:
 return buyerName;
 case 2:
 return fiefdomName;
 default:
 return null;
 }
 }
 });
 purchase.perform(Arrays.asList(castlePersonalArgs));
 assertEquals(1, testFiefdom.getPersonalCastles().size());
 assertEquals(900, buyerObj.getTotalMoney());

 // Test insufficient funds
 buyerObj.setTotalMoney(950);
 purchase.perform(castlePersonalArgs);
 assertEquals(1, testFiefdom.getPersonalCastles().size());
 assertEquals(950, buyerObj.getTotalMoney());
 }

 @Test
 public void testMercenary() {
 // Test valid call with sufficient funds
 int qty = 1;
 String buyerName = "buyer1";
 String knightName = "knight1";
 Knight buyerObj = new Knight("buyer1", "knight", 1500, 0, 0, 0, 0, 0);
 currentPlayers.put(buyerName, buyerObj);
 Knight knightObj = new Knight("knight1", "knight", 1000, 0, 0, 0, 0, 0);
 currentPlayers.put(knightName, knightObj);
 Collection<Object> mercenaryArgs = new ArrayList();
 mercenaryArgs.add(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 switch (count++) {
 case 0:
 return qty;
 case 1:
 return buyerName;
 case 2:
 return knightName;
 default:
 return null;
 }
 }
 });
 purchase.perform(Arrays.asList(mercenaryArgs));
 assertEquals(1, knightObj.getMercenaries());
 assertEquals(1400, buyerObj.getTotalMoney());

 // Test insufficient funds
 buyerObj.setTotalMoney(1350);
 purchase.perform(mercenaryArgs);
 assertEquals(1, knightObj.getMercenaries());
 assertEquals(1350, buyerObj.getTotalMoney());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidBuyerName() {
 String invalidBuyerName = "inexistent";
 String fiefdomName = "testFiefdom";
 String knightName = "knight1";
 Knight buyerObj = new Knight("buyer1", "knight", 1000, 0, 0, 0, 0, 0);
 currentPlayers.put("buyer1", buyerObj);
 Knight knightObj = new Knight("knight1", "knight", 1000, 0, 0, 0, 0, 0);
 currentPlayers.put(knightName, knightObj);
 Collection<Object> mercenaryArgs = new ArrayList();
 mercenaryArgs.add(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 switch (count++) {
 case 0:
 return 1;
 case 1:
 return invalidBuyerName;
 case 2:
 return knightName;
 default:
 return null;
 }
 }
 });
 purchase.perform(Arrays.asList(mercenaryArgs));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFiefdomName() {
 String buyerName = "buyer1";
 String invalidFiefdomName = "inexistent";
 Knight buyerObj = new Knight("buyer1", "knight", 1000, 0, 0, 0, 0, 0);
 currentPlayers.put(buyerName, buyerObj);
 Collection<Object> vassalArgs = new ArrayList();
 vassalArgs.add(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 switch (count++) {
 case 0:
 return 1;
 case 1:
 return buyerName;
 case 2:
 return invalidFiefdomName;
 default:
 return null;
 }
 }
 });
 purchase.perform(Arrays.asList(vassalArgs));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidRank() {
 String buyerName = "buyer1";
 String fiefdomName = "testFiefdom";
 String invalidRank = "queen";
 Knight buyerObj = new Knight("buyer1", "knight", 1000, 0, 0, 0, 0, 0);
 currentPlayers.put(buyerName, buyerObj);
 Collection<Object> castlePersonalArgs = new ArrayList();
 castlePersonalArgs.add(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 switch (count++) {
 case 0:
 return 1;
 case 1:
 return buyerName;
 case 2:
 return fiefdomName;
 case 3:
 return invalidRank;
 default:
 return null;
 }
 }
 });
 purchase.perform(Arrays.asList(castlePersonalArgs));
 }

 @Test(expected = IllegalStateException.class)
 public void testBuyCastleForSomeoneElseFiefdom() {
 String buyerName = "buyer1";
 String fiefdomName = "testFiefdom";
 String otherPlayerName = "otherPlayer";
 Knight buyerObj = new Knight("buyer1", "knight", 1000, 0, 0, 0, 0, 0);
 Knight otherPlayerObj = new Knight("otherPlayer", "knight", 1000, 0, 0, 0, 0, 0);
 currentPlayers.put(buyerName, buyerObj);
 currentPlayers.put(otherPlayerName, otherPlayerObj);
 Fiefdom testFiefdom = new Fiefdom(fiefdomName, null, null, null, null, 0, null, null, null, 0, 0, 0, 0, 0, 0, 0);
 map.put(fiefdomName, testFiefdom);
 Collection<Object> castlePersonalArgs = new ArrayList();
 castlePersonalArgs.add(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 switch (count++) {
 case 0:
 return 1;
 case 1:
 return buyerName;
 case 2:
 return fiefdomName;
 default:
 return null;
 }
 }
 });
 purchase.perform(Arrays.asList(castlePersonalArgs));
 }

 @Test
 public void testBuyCastleForKing() {
 String buyerName = "king";
 String fiefdomName = "testFiefdom";
 Knight buyerObj = new Knight("king", "king", 10000, 0, 0, 0, 0, 0);
 currentPlayers.put(buyerName, buyerObj);
 Fiefdom testFiefdom = new Fiefdom(fiefdomName, null, null, null, null, 0, null, null, null, 0, 0, 0, 0, 0, 0, 0);
 map.put(fiefdomName, testFiefdom);
 Collection<Object> castlePersonalArgs = new ArrayList();
 castlePersonalArgs.add(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 switch (count++) {
 case 0:
 return 1;
 case 1:
 return buyerName;
 case 2:
 return fiefdomName;
 default:
 return null;
 }
 }
 });
 purchase.perform(Arrays.asList(castlePersonalArgs));
 assertFalse(testFiefdom.getCastles().isEmpty());
 }

 @Test
 public void testBuyPersonalCastleForCount() {
 String buyerName = "count";
 String fiefdomName = "testFiefdom";
 Knight buyerObj = new Knight("count", "knight", 1000, 0, 0, 0, 0, 0);
 currentPlayers.put(buyerName, buyerObj);
 Fiefdom testFiefdom = new Fiefdom(fiefdomName, null, null, null, null, 0, null, null, null, 0, 0, 0, 0, 0, 0, 0);
 map.put(fiefdomName, testFiefdom);
 Collection<Object> castlePersonalArgs = new ArrayList();
 castlePersonalArgs.add(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 switch (count++) {
 case 0:
 return 1;
 case 1:
 return buyerName;
 case 2:
 return fiefdomName;
 default:
 return null;
 }
 }
 });
 purchase.perform(Arrays.asList(castlePersonalArgs));
 assertFalse(testFiefdom.getPersonalCastles().isEmpty());
 }
}