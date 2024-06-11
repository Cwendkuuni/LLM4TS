import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import src.Actions;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;
import src.Purchase;
import src.CurrentPlayers;

import java.util.*;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class PurchaseTest {

 private Purchase purchase;
 private Actions actions;
 private GameAutoActions gameAutoActions;
 private Map map;
 private CurrentPlayers currentPlayers;
 private Fiefdoms fiefdom;
 private Knight buyer, knight;

 @Before
 public void setup() {
 Map mockMap = Mockito.mock(Map.class);
 CurrentPlayers mockCurrentPlayers = Mockito.mock(CurrentPlayers.class);
 Fiefdoms mockFiefdoms = Mockito.mock(Fiefdoms.class);
 Knight mockBuyer = Mockito.mock(Knight.class);
 Knight mockKnight = Mockito.mock(Knight.class);

 actions = Mockito.spy(new Actions());
 gameAutoActions = Mockito.mock(GameAutoActions.class);
 map = mockMap;
 currentPlayers = mockCurrentPlayers;
 fiefdom = Mockito.mock(Fiefdoms.class);
 buyer = mockBuyer;
 knight = mockKnight;

 MockitoAnnotations.initMocks(this);
 purchase = new Purchase(actions, gameAutoActions, map, currentPlayers);
 }

 @Test
 public void testVassal() {
 final int qty = 3;
 final String buyerName = "John";
 final String fiefdomName = "Albion";
 final Fiefdoms expectedFiefdom = new Fiefdoms("Albion", null);
 final Knight expectedBuyer = new Knight("John", "count", 1000, 10);

 Mockito.when(map.get(fiefdomName)).thenReturn(expectedFiefdom);
 Mockito.when(currentPlayers.get(buyerName)).thenReturn(expectedBuyer);
 Mockito.when(expectedFiefdom.getOwner()).thenReturn(null);

 purchase.perform(Arrays.asList(fiefdomName.iterator(), Arrays.asList(qty, buyerName, fiefdomName.iterator()).iterator()));

 Mockito.verify(expectedBuyer, Mockito.times(1)).setTotalMoney(qty, false);
 Mockito.verify(expectedFiefdom, Mockito.times(1)).setLoyalVassals(qty, true);
 }

 @Test
 public void testVassal_InsufficientFunds() {
 final int qty = 10;
 final String buyerName = "John";
 final String fiefdomName = "Albion";
 final Fiefdoms expectedFiefdom = new Fiefdoms("Albion", null);
 final Knight expectedBuyer = new Knight("John", "count", 500, 10);

 Mockito.when(map.get(fiefdomName)).thenReturn(expectedFiefdom);
 Mockito.when(currentPlayers.get(buyerName)).thenReturn(expectedBuyer);
 Mockito.when(expectedFiefdom.getOwner()).thenReturn(null);

 purchase.perform(Arrays.asList(fiefdomName.iterator(), Arrays.asList(qty, buyerName, fiefdomName.iterator()).iterator()));

 Mockito.verify(System.out, Mockito.times(1)).println("You cannot buy more vassals, you have insufficient funds");
 }

 @Test
 public void testVassal_FiefdomBelongsToAnotherPlayer() {
 final int qty = 2;
 final String buyerName = "John";
 final String fiefdomName = "Albion";
 final Fiefdoms expectedFiefdom = new Fiefdoms("Albion", new Knight("Alice", "king", 2000, 20));
 final Knight expectedBuyer = new Knight("John", "count", 1000, 10);

 Mockito.when(map.get(fiefdomName)).thenReturn(expectedFiefdom);
 Mockito.when(currentPlayers.get(buyerName)).thenReturn(expectedBuyer);
 Mockito.when(expectedFiefdom.getOwner()).thenReturn(expectedBuyer);

 purchase.perform(Arrays.asList(fiefdomName.iterator(), Arrays.asList(qty, buyerName, fiefdomName.iterator()).iterator()));

 Mockito.verify(System.out, Mockito.times(1)).println("You can only buy a vassal for a fiefdom that does not belong to another player");
 }

 @Test
 public void testCastleFiefdom() {
 final int qty = 2;
 final String buyerName = "John";
 final String fiefdomName = "Albion";
 final Fiefdoms expectedFiefdom = new Fiefdoms("Albion", null);
 final Knight expectedBuyer = new Knight("John", "king", 5000, 20);

 Mockito.when(map.get(fiefdomName)).thenReturn(expectedFiefdom);
 Mockito.when(currentPlayers.get(buyerName)).thenReturn(expectedBuyer);
 Mockito.when(expectedFiefdom.getOwner()).thenReturn(null);

 purchase.perform(Arrays.asList(fiefdomName.iterator(), Arrays.asList(qty, buyerName, fiefdomName.iterator()).iterator()));

 Mockito.verify(expectedBuyer, Mockito.times(1)).setTotalMoney(qty * 25, false);
 Mockito.verify(expectedFiefdom, Mockito.times(1)).buyFiefdomCastles(qty);
 }

 @Test
 public void testCastleFiefdom_InsufficientFunds() {
 final int qty = 5;
 final String buyerName = "John";
 final String fiefdomName = "Albion";
 final Fiefdoms expectedFiefdom = new Fiefdoms("Albion", null);
 final Knight expectedBuyer = new Knight("John", "king", 3000, 20);

 Mockito.when(map.get(fiefdomName)).thenReturn(expectedFiefdom);
 Mockito.when(currentPlayers.get(buyerName)).thenReturn(expectedBuyer);
 Mockito.when(expectedFiefdom.getOwner()).thenReturn(null);

 purchase.perform(Arrays.asList(fiefdomName.iterator(), Arrays.asList(qty, buyerName, fiefdomName.iterator()).iterator()));

 Mockito.verify(System.out, Mockito.times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
 }

 @Test
 public void testCastleFiefdom_FiefdomBelongsToAnotherPlayer() {
 final int qty = 2;
 final String buyerName = "John";
 final String fiefdomName = "Albion";
 final Fiefdoms expectedFiefdom = new Fiefdoms("Albion", new Knight("Alice", "king", 5000, 20));
 final Knight expectedBuyer = new Knight("John", "king", 5000, 20);

 Mockito.when(map.get(fiefdomName)).thenReturn(expectedFiefdom);
 Mockito.when(currentPlayers.get(buyerName)).thenReturn(expectedBuyer);
 Mockito.when(expectedFiefdom.getOwner()).thenReturn(expectedBuyer);

 purchase.perform(Arrays.asList(fiefdomName.iterator(), Arrays.asList(qty, buyerName, fiefdomName.iterator()).iterator()));

 Mockito.verify(System.out, Mockito.times(1)).println("You can only buy a Fiefdom castle for a fiefdom that belongs to you");
 }

 @Test
 public void testCastlePersonal() {
 final int qty = 3;
 final String buyerName = "John";
 final String fiefdomName = "Albion";
 final Fiefdoms expectedFiefdom = new Fiefdoms("Albion", null);
 final Knight expectedBuyer = new Knight("John", "king", 7500, 20);

 Mockito.when(map.get(fiefdomName)).thenReturn(expectedFiefdom);
 Mockito.when(currentPlayers.get(buyerName)).thenReturn(expectedBuyer);
 Mockito.when(expectedFiefdom.getOwner()).thenReturn(null);

 purchase.perform(Arrays.asList(fiefdomName.iterator(), Arrays.asList(qty, buyerName, fiefdomName.iterator()).iterator()));

 Mockito.verify(expectedBuyer, Mockito.times(1)).setTotalMoney(qty * 10, false);
 Mockito.verify(expectedFiefdom, Mockito.times(1)).buyPersonalCastles(qty);
 }

 @Test
 public void testCastlePersonal_InsufficientFunds() {
 final int qty = 5;
 final String buyerName = "John";
 final String fiefdomName = "Albion";
 final Fiefdoms expectedFiefdom = new Fiefdoms("Albion", null);
 final Knight expectedBuyer = new Knight("John", "king", 4000, 20);

 Mockito.when(map.get(fiefdomName)).thenReturn(expectedFiefdom);
 Mockito.when(currentPlayers.get(buyerName)).thenReturn(expectedBuyer);
 Mockito.when(expectedFiefdom.getOwner()).thenReturn(null);

 purchase.perform(Arrays.asList(fiefdomName.iterator(), Arrays.asList(qty, buyerName, fiefdomName.iterator()).iterator()));

 Mockito.verify(System.out, Mockito.times(1)).println("You cannot buy more Personal castles, you have insufficient funds");
 }

 @Test
 public void testCastlePersonal_FiefdomBelongsToAnotherPlayer() {
 final int qty = 2;
 final String buyerName = "John";
 final String fiefdomName = "Albion";
 final Fiefdoms expectedFiefdom = new Fiefdoms("Albion", new Knight("Alice", "king", 7500, 20));
 final Knight expectedBuyer = new Knight("John", "king", 7500, 20);

 Mockito.when(map.get(fiefdomName)).thenReturn(expectedFiefdom);
 Mockito.when(currentPlayers.get(buyerName)).thenReturn(expectedBuyer);
 Mockito.when(expectedFiefdom.getOwner()).thenReturn(expectedBuyer);

 purchase.perform(Arrays.asList(fiefdomName.iterator(), Arrays.asList(qty, buyerName, fiefdomName.iterator()).iterator()));

 Mockito.verify(System.out, Mockito.times(1)).println("You can only buy a Personal castle for a fiefdom that belongs to you");
 }

 @Test
 public void testMercenary() {
 final int qty = 3;
 final String buyerName = "John";
 final String knightName = "Robert";
 final Knight expectedBuyer = new Knight("John", "count", 1500, 15);
 final Knight expectedKnight = new Knight("Robert", "knight", 10, 5);

 Mockito.when(currentPlayers.get(buyerName)).thenReturn(expectedBuyer);
 Mockito.when(currentPlayers.get(knightName)).thenReturn(expectedKnight);

 purchase.perform(Arrays.asList(knightName.iterator(), Arrays.asList(qty, buyerName, knightName.iterator()).iterator()));

 Mockito.verify(expectedBuyer, Mockito.times(1)).setTotalMoney(qty, false);
 Mockito.verify(expectedKnight, Mockito.times(1)).addMercenaries(qty);
 }

 @Test
 public void testMercenary_InsufficientFunds() {
 final int qty = 5;
 final String buyerName = "John";
 final String knightName = "Robert";
 final Knight expectedBuyer = new Knight("John", "count", 1000, 15);
 final Knight expectedKnight = new Knight("Robert", "knight", 10, 5);

 Mockito.when(currentPlayers.get(buyerName)).thenReturn(expectedBuyer);
 Mockito.when(currentPlayers.get(knightName)).thenReturn(expectedKnight);

 purchase.perform(Arrays.asList(knightName.iterator(), Arrays.asList(qty, buyerName, knightName.iterator()).iterator()));

 Mockito.verify(System.out, Mockito.times(1)).println("You cannot buy more mercenaries, you have insufficient funds");
 }

 @Test
 public void testMercenary_BuyerIsKnight() {
 final int qty = 2;
 final String buyerName = "Robert";
 final String knightName = "John";
 final Knight expectedBuyer = new Knight("Robert", "knight", 1500, 15);
 final Knight expectedKnight = new Knight("John", "knight", 10, 5);

 Mockito.when(currentPlayers.get(buyerName)).thenReturn(expectedBuyer);
 Mockito.when(currentPlayers.get(knightName)).thenReturn(expectedKnight);

 purchase.perform(Arrays.asList(knightName.iterator(), Arrays.asList(qty, buyerName, knightName.iterator()).iterator()));

 Mockito.verify(System.out, Mockito.times(1)).println("You can only hire mercenaries if you are a king");
 }

 @Test
 public void testMercenary_KnightBelongsToAnotherPlayer() {
 final int qty = 2;
 final String buyerName = "John";
 final String knightName = "Robert";
 final Knight expectedBuyer = new Knight("John", "count", 1500, 15);
 final Knight expectedKnight = new Knight("Robert", "knight", 10, 5);

 Mockito.when(currentPlayers.get(buyerName)).thenReturn(expectedBuyer);
 Mockito.when(currentPlayers.get(knightName)).thenReturn(expectedKnight);
 Mockito.when(expectedKnight.getName()).thenReturn("Alice");

 purchase.perform(Arrays.asList(knightName.iterator(), Arrays.asList(qty, buyerName, knightName.iterator()).iterator()));

 Mockito.verify(System.out, Mockito.times(1)).println("Inexistent Knight");
 }
}