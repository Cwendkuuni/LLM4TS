package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import feudalism.Fiefdoms;
import feudalism.Map;
import feudalism.Player;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms fiefdoms;
 private Map map;
 private Player buyer, buyer2, fiefdomOwner, knight;

 @Before
 public void setup() {
 purchase = new Purchase();
 fiefdoms = Mockito.mock(Fiefdoms.class);
 map = Mockito.mock(Map.class);
 buyer = Mockito.mock(Player.class, RETURNS_DEEP_STUBS);
 buyer2 = Mockito.mock(Player.class, RETURNS_DEEP_STUBS);
 fiefdomOwner = Mockito.mock(Player.class, RETURNS_DEEP_STUBS);
 knight = Mockito.mock(Player.class, RETURNS_DEEP_STUBS);

 when(map.get(Mockito.anyString())).thenReturn(fiefdoms);
 when(fiefdoms.getOwner()).thenReturn(fiefdomOwner);
 when(fiefdoms.getName()).thenReturn("testFiefdom");
 when(CurrentPlayers.currentPlayers.get(buyer.getName())).thenReturn(buyer);
 when(CurrentPlayers.currentPlayers.get(buyer2.getName())).thenReturn(buyer2);
 when(CurrentPlayers.currentPlayers.get(fiefdomOwner.getName())).thenReturn(fiefdomOwner);
 when(CurrentPlayers.currentPlayers.get(knight.getName())).thenReturn(knight);
 }

 @Test
 public void testVassal() {
 Collection args = Arrays.asList(new Integer(1), buyer, "testFiefdom");
 purchase.perform(args);
 verify(buyer).setTotalMoney(1, false);
 verify(fiefdoms).setLoyalVassals(1, true);

 args = Arrays.asList(new Integer(1), buyer, "testFiefdom");
 purchase.perform(args);
 verify(buyer, times(2)).setTotalMoney(1, false);
 verify(fiefdoms, times(2)).setLoyalVassals(1, true);

 args = Arrays.asList(new Integer(2), buyer, "testFiefdom");
 purchase.perform(args);
 verify(buyer).setTotalMoney(2, false);
 verify(fiefdoms).setLoyalVassals(2, true);

 args = Arrays.asList(new Integer(1), buyer2, "testFiefdom");
 purchase.perform(args);
 verify(buyer2).setTotalMoney(1, false);
 verify(fiefdoms).setLoyalVassals(1, true);
 }

 @Test
 public void testVassalInsufficientFunds() {
 Collection args = Arrays.asList(new Integer(3), buyer, "testFiefdom");
 purchase.perform(args);
 verify(buyer, never()).setTotalMoney(3, false);
 verify(fiefdoms, never()).setLoyalVassals(3, true);
 }

 @Test
 public void testCastleFiefdom() {
 Collection args = Arrays.asList(new Integer(1), buyer, "testFiefdom");
 purchase.perform(args);
 verify(buyer).setTotalMoney(25, false);
 verify(fiefdoms).buyFiefdomCastles(1);

 args = Arrays.asList(new Integer(1), buyer, "testFiefdom");
 purchase.perform(args);
 verify(buyer, times(2)).setTotalMoney(25, false);
 verify(fiefdoms, times(2)).buyFiefdomCastles(1);

 args = Arrays.asList(new Integer(2), buyer, "testFiefdom");
 purchase.perform(args);
 verify(buyer).setTotalMoney(50, false);
 verify(fiefdoms).buyFiefdomCastles(2);
 }

 @Test
 public void testCastleFiefdomOwner() {
 Collection args = Arrays.asList(new Integer(1), buyer, "testFiefdom");
 purchase.perform(args);
 verify(buyer, never()).setTotalMoney(25, false);
 verify(fiefdoms, never()).buyFiefdomCastles(1);
 }

 @Test
 public void testCastlePersonal() {
 Collection args = Arrays.asList(new Integer(1), buyer, "testFiefdom");
 purchase.perform(args);
 verify(buyer).setTotalMoney(10, false);
 verify(fiefdoms).buyPersonalCastles(1);

 args = Arrays.asList(new Integer(1), buyer, "testFiefdom");
 purchase.perform(args);
 verify(buyer, times(2)).setTotalMoney(10, false);
 verify(fiefdoms, times(2)).buyPersonalCastles(1);

 args = Arrays.asList(new Integer(2), buyer, "testFiefdom");
 purchase.perform(args);
 verify(buyer).setTotalMoney(20, false);
 verify(fiefdoms).buyPersonalCastles(2);
 }

 @Test
 public void testCastlePersonalKing() {
 Collection args = Arrays.asList(new Integer(1), buyer, "testFiefdom");
 purchase.perform(args);
 verify(buyer, never()).setTotalMoney(10, false);
 verify(fiefdoms, never()).buyPersonalCastles(1);
 }

 @Test
 public void testCastlePersonalFiefdomOwner() {
 Collection args = Arrays.asList(new Integer(1), buyer, "testFiefdom");
 purchase.perform(args);
 verify(buyer, never()).setTotalMoney(10, false);
 verify(fiefdoms, never()).buyPersonalCastles(1);
 }

 @Test
 public void testMercenary() {
 Collection args = Arrays.asList(new Integer(1), buyer, knight);
 purchase.perform(args);
 verify(buyer).setTotalMoney(1, false);
 verify(knight).addMercenaries(1);

 args = Arrays.asList(new Integer(1), buyer, knight);
 purchase.perform(args);
 verify(buyer, times(2)).setTotalMoney(1, false);
 verify(knight, times(2)).addMercenaries(1);

 args = Arrays.asList(new Integer(2), buyer, knight);
 purchase.perform(args);
 verify(buyer).setTotalMoney(2, false);
 verify(knight).addMercenaries(2);
 }

 @Test
 public void testMercenaryBuyerIsKnight() {
 Collection args = Arrays.asList(new Integer(1), knight, buyer);
 purchase.perform(args);
 verify(knight, never()).addMercenaries(1);
 }

 @Test
 public void testMercenaryInsufficientFunds() {
 Collection args = Arrays.asList(new Integer(3), buyer, knight);
 purchase.perform(args);
 verify(buyer, never()).setTotalMoney(3, false);
 verify(knight, never()).addMercenaries(3);
 }

 @Test
 public void testMercenarySamePlayer() {
 Collection args = Arrays.asList(new Integer(1), buyer, buyer);
 purchase.perform(args);
 verify(buyer, never()).setTotalMoney(1, false);
 verify(buyer, never()).addMercenaries(1);
 }
}