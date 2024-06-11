package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PurchaseTest {

 private Purchase purchase;
 private Iterator<Object> argsIter;
 private List<String> argList = new ArrayList<>();

 @Before
 public void setup() {
 purchase = new Purchase();
 argsIter = argsList.iterator();
 }

 @Test
 public void testPerform() throws Exception {
 final String className = "feudalism.Purchase";
 final Class<?> aMethod = Class.forName(className);
 final Method methodObj = aMethod.getMethod("vassal", Iterator.class);

 purchase.perform(argList);
 verify(purchase).vassal(argsIter);
 }

 @Test(expected = SecurityException.class)
 public void testPerformWithSecurityException() {
 final String className = "feudalism.Purchase";
 final Class<?> aMethod = Class.forName(className);
 final Method methodObj = aMethod.getMethod("vassal", Iterator.class);
 doThrow(new SecurityException()).when(purchase).vassal(argsIter);
 purchase.perform(argList);
 }

 @Test(expected = NoSuchMethodException.class)
 public void testPerformWithNoSuchMethodException() {
 final String className = "feudalism.GarbageClass";
 final Class<?> aMethod = Class.forName(className);
 final Method methodObj = aMethod.getMethod("vassal", Iterator.class);
 purchase.perform(argList);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVassalWithInsufficientFunds() {
 argList.add(1);
 argList.add("buyer1");
 argList.add("fiefdom1");
 argList.add("10000"); // insufficient funds

 purchase.vassal(argsIter);
 verify(System.out).println("You cannot buy more vassals, you have insufficient funds");
 }

 @Test
 public void testVassalWithValidArgs() {
 argList.clear();
 argList.add(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "5";
 }
 });
 argList.add("buyer1");
 argList.add("fiefdom1");

 purchase.vassal(argsIter);
 verify(CurrentPlayers.currentPlayers.get("buyer1"), times(1)).setTotalMoney(-5, false);
 verify(Map.map.get("fiefdom1"), times(1)).setLoyalVassals(5, true);
 }

 @Test
 public void testCastleFiefdomWithValidArgs() {
 argList.clear();
 argList.add(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "10";
 }
 });
 argList.add("buyer1");
 argList.add("fiefdom1");

 purchase.castlesFiefdom(argsIter);
 verify(CurrentPlayers.currentPlayers.get("buyer1"), times(1)).setTotalMoney(-100, false);
 verify(Map.map.get("fiefdom1"), times(1)).buyFiefdomCastles(10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCastleFiefdomWithInsufficientFunds() {
 argList.clear();
 argList.add(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "15";
 }
 });
 argList.add("buyer1");
 argList.add("fiefdom1");

 purchase.castlesFiefdom(argsIter);
 verify(System.out, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
 }

 @Test
 public void testCastlePersonalWithValidArgs() {
 argList.clear();
 argList.add(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "5";
 }
 });
 argList.add("buyer1");
 argList.add("fiefdom1");

 purchase.castlesPersonal(argsIter);
 verify(CurrentPlayers.currentPlayers.get("buyer1"), times(1)).setTotalMoney(-50, false);
 verify(Map.map.get("fiefdom1"), times(1)).buyPersonalCastles(5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCastlePersonalWithInsufficientFunds() {
 argList.clear();
 argList.add(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "10";
 }
 });
 argList.add("buyer1");
 argList.add("fiefdom1");

 purchase.castlesPersonal(argsIter);
 verify(System.out, times(1)).println("You cannot buy more Personal castle, you have insufficient funds");
 }

 @Test
 public void testMercenaryWithValidArgs() {
 argList.clear();
 argList.add(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "2";
 }
 });
 argList.add("buyer1");
 argList.add("knight1");
 argList.add("knight2");

 purchase.mercenary(argsIter);
 verify(CurrentPlayers.currentPlayers.get("buyer1"), times(1)).setTotalMoney(-20, false);
 verify(CurrentPlayers.currentPlayers.get("knight1"), times(1)).addMercenaries(2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMercenaryWithInsufficientFunds() {
 argList.clear();
 argList.add(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "5";
 }
 });
 argList.add("buyer1");
 argList.add("knight1");
 argList.add("knight2");

 purchase.mercenary(argsIter);
 verify(System.out, times(1)).println("You cannot buy more mercenaries, you have insufficient funds");
 }

 @Test
 public void testMercenaryWithSameKnight() {
 argList.clear();
 argList.add(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "2";
 }
 });
 argList.add("buyer1");
 argList.add("knight1");
 argList.add("knight1");

 purchase.mercenary(argsIter);
 verify(System.out, times(1)).println("You cannot buy mercenaries for yourself");
 }

 @Test
 public void testMercenaryWithKingBuyer() {
 argList.clear();
 argList.add(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "knight1";
 }
 });
 argList.add("king1");
 argList.add("knight2");
 argList.add("buyer1");

 purchase.mercenary(argsIter);
 verify(System.out, times(1)).println("You cannot buy mercenaries as a king");
 }

 @Test
 public void testMercenaryWithFiefdomOwner() {
 argList.clear();
 argList.add(new Iterator<Object>() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "knight1";
 }
 });
 argList.add("count1");
 argList.add("knight2");
 argList.add("buyer1");

 purchase.mercenary(argsIter);
 verify(System.out, times(1)).println("You can only buy mercenaries for your own fiefdom");
 }
}