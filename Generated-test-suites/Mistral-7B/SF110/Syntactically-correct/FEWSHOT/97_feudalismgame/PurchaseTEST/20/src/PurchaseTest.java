package src;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class PurchaseTest {

 private Purchase purchase;
 private Iterator<String> argIterator;
 private Map<String, Fiefdoms> map;
 private List<Knight> currentPlayers;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 purchase = new Purchase();
 argIterator = mock(Iterator.class);
 map = new HashMap<>();
 currentPlayers = new ArrayList<>();
 }

 @Test
 public void testVassal() {
 final Fiefdoms fiefdom = new Fiefdoms("name", "owner");
 final Knight buyer1 = new Knight("buyer1", "count");
 final Knight buyer2 = new Knight("buyer2", "king");
 map.put("name", fiefdom);
 currentPlayers.add(buyer1);
 currentPlayers.add(buyer2);

 when(argIterator.next()).thenReturn("3").thenReturn("buyer1").thenReturn("name");
 purchase.vassal(argIterator);

 assertEquals(3, fiefdom.getLoyalVassals());
 assertFalse(buyer1.isTotalMoneyExceeded());

 when(argIterator.next()).thenReturn("5").thenReturn("buyer2").thenReturn("name");
 purchase.vassal(argIterator);

 assertEquals(3, fiefdom.getLoyalVassals());
 assertTrue(buyer1.isTotalMoneyExceeded());

 when(argIterator.next()).thenThrow(NoSuchElementException.class);
 purchase.vassal(argIterator);

 assertFalse(buyer1.isTotalMoneyExceeded());
 assertFalse(fiefdom.isFiefdomMine(buyer1.getName()));
 }

 @Test
 public void testCastleFiefdom() {
 final Fiefdoms fiefdom = new Fiefdoms("name", "owner");
 final Knight buyer1 = new Knight("buyer1", "king");
 map.put("name", fiefdom);
 currentPlayers.add(buyer1);

 when(argIterator.next()).thenReturn("3").thenReturn("buyer1").thenReturn("name");
 purchase.castleFiefdom(argIterator);

 assertEquals(3, fiefdom.getCastles().size());
 assertFalse(buyer1.isTotalMoneyExceeded());

 when(argIterator.next()).thenReturn("5").thenReturn("buyer1").thenReturn("name");
 purchase.castleFiefdom(argIterator);

 assertEquals(3, fiefdom.getCastles().size());
 assertTrue(buyer1.isTotalMoneyExceeded());

 when(argIterator.next()).thenThrow(NoSuchElementException.class);
 purchase.castleFiefdom(argIterator);

 assertFalse(buyer1.isTotalMoneyExceeded());
 assertFalse(fiefdom.isFiefdomMine(buyer1.getName()));
 }

 @Test
 public void testCastlePersonal() {
 final Fiefdoms fiefdom = new Fiefdoms("name", "owner");
 final Knight buyer1 = new Knight("buyer1", "king");
 map.put("name", fiefdom);
 currentPlayers.add(buyer1);

 when(argIterator.next()).thenReturn("3").thenReturn("buyer1").thenReturn("name");
 purchase.castlePersonal(argIterator);

 assertEquals(3, fiefdom.getPersonalCastles().size());
 assertFalse(buyer1.isTotalMoneyExceeded());

 when(argIterator.next()).thenReturn("5").thenReturn("buyer1").thenReturn("name");
 purchase.castlePersonal(argIterator);

 assertEquals(3, fiefdom.getPersonalCastles().size());
 assertTrue(buyer1.isTotalMoneyExceeded());

 when(argIterator.next()).thenThrow(NoSuchElementException.class);
 purchase.castlePersonal(argIterator);

 assertFalse(buyer1.isTotalMoneyExceeded());
 assertFalse(fiefdom.isFiefdomMine(buyer1.getName()));
 }

 @Test
 public void testMercenary() {
 final Knight knight1 = new Knight("knight1", "knight");
 final Knight buyer1 = new Knight("buyer1", "king");
 currentPlayers.add(buyer1);
 currentPlayers.add(knight1);

 when(argIterator.next()).thenReturn("3").thenReturn("buyer1").thenReturn("knight1");
 purchase.mercenary(argIterator);

 assertEquals(3, knight1.getMercenaries().size());
 assertFalse(buyer1.isTotalMoneyExceeded());

 when(argIterator.next()).thenReturn("5").thenReturn("buyer1").thenReturn("knight1");
 purchase.mercenary(argIterator);

 assertEquals(3, knight1.getMercenaries().size());
 assertTrue(buyer1.isTotalMoneyExceeded());

 when(argIterator.next()).thenThrow(NoSuchElementException.class);
 purchase.mercenary(argIterator);

 assertFalse(buyer1.isTotalMoneyExceeded());
 assertFalse(knight1.getName().equals(buyer1.getName()));
 }

 @Test
 public void testPerform_FiefdomOwnerIsCountOrDuke() {
 final Fiefdoms fiefdom = new Fiefdoms("name", "owner");
 final Knight buyer1 = new Knight("buyer1", "king");
 map.put("name", fiefdom);
 currentPlayers.add(buyer1);

 when(argIterator.next()).thenReturn("command").thenReturn(args);
 doThrow(IllegalAccessException.class).when(purchase).perform(args);

 when(argIterator.next()).thenReturn("3").thenReturn("buyer1").thenReturn("name");
 purchase.vassal(argIterator);

 assertEquals(0, fiefdom.getLoyalVassals());
 assertFalse(buyer1.isTotalMoneyExceeded());
 }

 @Test
 public void testPerform_FiefdomDoesntBelongToTheKingdom() {
 final Fiefdoms fiefdom = new Fiefdoms("name", "owner");
 final Knight buyer1 = new Knight("buyer1", "king");
 map.put("name", fiefdom);

 when(argIterator.next()).thenReturn("command").thenReturn(args);
 doThrow(IllegalAccessException.class).when(purchase).perform(args);

 when(argIterator.next()).thenReturn("3").thenReturn("buyer1").thenReturn("otherName");
 purchase.vassal(argIterator);

 assertEquals(0, fiefdom.getLoyalVassals());
 assertFalse(buyer1.isTotalMoneyExceeded());
 }

 @Test
 public void testPerform_InsufficientFundsForVassals() {
 final Fiefdoms fiefdom = new Fiefdoms("name", "owner");
 final Knight buyer1 = new Knight("buyer1", "king");
 final int initialBuyerMoney = buyer1.getMoney();
 map.put("name", fiefdom);
 currentPlayers.add(buyer1);

 when(argIterator.next()).thenReturn("command").thenReturn(args);
 doThrow(IllegalAccessException.class).when(purchase).perform(args);

 when(argIterator.next()).thenReturn("5").thenReturn("buyer1").thenReturn("name");
 purchase.vassal(argIterator);

 assertEquals(0, fiefdom.getLoyalVassals());
 assertTrue(buyer1.isTotalMoneyExceeded());
 assertEquals(initialBuyerMoney, buyer1.getMoney());
 }

 @Test
 public void testPerform_FiefdomOwnerIsNotKingAndFiefdomIsntMine() {
 final Fiefdoms fiefdom = new Fiefdoms("name", "owner");
 final Knight buyer1 = new Knight("buyer1", "knight");
 map.put("name", fiefdom);
 currentPlayers.add(buyer1);

 when(argIterator.next()).thenReturn("command").thenReturn(args);
 doThrow(IllegalAccessException.class).when(purchase).perform(args);

 when(argIterator.next()).thenReturn("3").thenReturn("buyer1").thenReturn("name");
 purchase.vassal(argIterator);

 assertEquals(0, fiefdom.getLoyalVassals());
 assertFalse(buyer1.isTotalMoneyExceeded());
 }

 @Test
 public void testPerform_FiefdomOwnerIsCountOrDukeButFiefdomHasCastles() {
 final Fiefdoms fiefdom = new Fiefdoms("name", "owner");
 final Knight buyer1 = new Knight("buyer1", "king");
 final int initialCastlesSize = fiefdom.getCastles().size();
 map.put("name", fiefdom);
 currentPlayers.add(buyer1);

 when(argIterator.next()).thenReturn("command").thenReturn(args);
 doThrow(IllegalAccessException.class).when(purchase).perform(args);

 when(argIterator.next()).thenReturn("3").thenReturn("buyer1").thenReturn("name");
 purchase.castleFiefdom(argIterator);

 assertEquals(initialCastlesSize + 3, fiefdom.getCastles().size());
 assertFalse(buyer1.isTotalMoneyExceeded());
 }

 @Test
 public void testPerform_FiefdomOwnerIsNotKingAndFiefdomHasCastles() {
 final Fiefdoms fiefdom = new Fiefdoms("name", "owner");
 final Knight buyer1 = new Knight("buyer1", "knight");
 final int initialCastlesSize = fiefdom.getCastles().size();
 map.put("name", fiefdom);
 currentPlayers.add(buyer1);

 when(argIterator.next()).thenReturn("command").thenReturn(args);
 doThrow(IllegalAccessException.class).when(purchase).perform(args);

 when(argIterator.next()).thenReturn("3").thenReturn("buyer1").thenReturn("name");
 purchase.castleFiefdom(argIterator);

 assertEquals(initialCastlesSize, fiefdom.getCastles().size());
 assertFalse(buyer1.isTotalMoneyExceeded());
 }

 @Test
 public void testPerform_FiefdomOwnerIsKingButCastleAlreadyMine() {
 final Fiefdoms fiefdom = new Fiefdoms("name", "owner");
 final Knight buyer1 = new Knight("buyer1", "king");
 final int initialCastlesSize = fiefdom.getCastles().size();
 map.put("name", fiefdom);
 currentPlayers.add(buyer1);

 when(argIterator.next()).thenReturn("command").thenReturn(args);
 doThrow(IllegalAccessException.class).when(purchase).perform(args);

 when(argIterator.next()).thenReturn("3").thenReturn("buyer1").thenReturn("name");
 purchase.castlePersonal(argIterator);

 assertEquals(initialCastlesSize, fiefdom.getCastles().size());
 assertFalse(buyer1.isTotalMoneyExceeded());
 }

 @Test
 public void testPerform_FiefdomOwnerIsKingButCastleNotMine() {
 final Fiefdoms fiefdom = new Fiefdoms("name", "owner");
 final Knight buyer1 = new Knight("buyer1", "king");
 final int initialCastlesSize = fiefdom.getCastles().size();
 map.put("name", fiefdom);
 currentPlayers.add(buyer1);

 when(argIterator.next()).thenReturn("command").thenReturn(args);
 doThrow(IllegalAccessException.class).when(purchase).perform(args);

 when(argIterator.next()).thenReturn("3").thenReturn("buyer1").thenReturn("otherName");
 purchase.castlePersonal(argIterator);

 assertEquals(initialCastlesSize, fiefdom.getCastles().size());
 assertFalse(buyer1.isTotalMoneyExceeded());
 }
}