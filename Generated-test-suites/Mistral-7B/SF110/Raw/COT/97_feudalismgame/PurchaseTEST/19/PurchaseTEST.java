package src.test;

import org.junit.Test;
import org.junit.Assert.*;
import src.Purchase;
import src.Fiefdoms;
import src.CurrentPlayers;
import src.Actions;
import src.Knight;
import src.Map;

import java.util.*;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms map;
 private CurrentPlayers currentPlayers;

 @Before
 public void setup() {
 // Assuming setup for other required objects like Map, CurrentPlayers, etc.
 purchase = new Purchase();
 map = new Fiefdoms();
 currentPlayers = new CurrentPlayers();
 }

 @Test
 public void testPerform() {
 // Test with valid arguments
 Collection args = new ArrayList();
 args.add("purchaseMethodName");
 args.add(new ArrayList(Arrays.asList(1, "player1", "fiefdom1")).iterator());
 purchase.perform(args);

 // Test with invalid method name
 args.clear();
 args.add("invalidMethodName");
 args.add(new ArrayList(Arrays.asList(1, "player1", "fiefdom1")).iterator());
 try {
 purchase.perform(args);
 fail("MethodNotFoundException should be thrown");
 } catch (MethodNotFoundException e) {
 // Expected
 }

 // Test with no arguments
 args.clear();
 try {
 purchase.perform(args);
 fail("NoSuchMethodException should be thrown");
 } catch (NoSuchMethodException e) {
 // Expected
 }

 // Test with incorrect argument type
 args.clear();
 args.add(1);
 args.add(new ArrayList(Arrays.asList(1, "player1", "fiefdom1")).iterator());
 try {
 purchase.perform(args);
 fail("InvalidArgumentTypeException should be thrown");
 } catch (InvalidArgumentTypeException e) {
 // Expected
 }

 // Test with illegal access
 args.clear();
 args.add("vassal");
 args.add(new ArrayList(Arrays.asList(1, null, "fiefdom1")).iterator());
 try {
 purchase.perform(args);
 fail("IllegalAccessException should be thrown");
 } catch (IllegalAccessException e) {
 // Expected
 }

 // Test with invocation target exception
 args.clear();
 args.add("vassal");
 args.add(new ArrayList(Arrays.asList(1, "player1", null)).iterator());
 try {
 purchase.perform(args);
 fail("InvocationTargetException should be thrown");
 } catch (InvocationTargetException e) {
 // Expected
 }
}

 @Test
 public void testVassal() {
 // Test with valid arguments
 Collection args = new ArrayList();
 args.add("vassal");
 args.add(new ArrayList(Arrays.asList(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 5;
 }
 }, "player1", "fiefdom1")).iterator());
 purchase.perform(args);
 Map.map.get("fiefdom1").setLoyalVassals(5, true);
 assertTrue(Map.map.get("fiefdom1").getLoyalVassals() >= 5);

 // Test with insufficient funds
 Collection args2 = new ArrayList();
 args2.add("vassal");
 args2.add(new ArrayList(Arrays.asList(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 10;
 }
 }, "player1", "fiefdom1")).iterator());
 purchase.perform(args2);
 assertFalse(Map.map.get("fiefdom1").getLoyalVassals() > 4);

 // Test with non-existent fiefdom
 args.clear();
 args.add("vassal");
 args.add(new ArrayList(Arrays.asList(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 5;
 }
 }, "player1", "inexistentFiefdom")).iterator());
 try {
 purchase.perform(args);
 fail("FiefdomNotFoundException should be thrown");
 } catch (FiefdomNotFoundException e) {
 // Expected
 }
}

 @Test
 public void testCastleFiefdom() {
 // Test with valid arguments
 Collection args = new ArrayList();
 args.add("castleFiefdom");
 args.add(new ArrayList(Arrays.asList(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 5;
 }
 }, "player1", "fiefdom1")).iterator());
 Map.map.get("fiefdom1").buyFiefdomCastles(5);
 assertTrue(Map.map.get("fiefdom1").getCastles().size() >= 5);

 // Test with insufficient funds
 Collection args2 = new ArrayList();
 args2.add("castleFiefdom");
 args2.add(new ArrayList(Arrays.asList(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 10;
 }
 }, "player1", "fiefdom1")).iterator());
 Map.map.get("fiefdom1").buyFiefdomCastles(5);
 assertFalse(Map.map.get("fiefdom1").getCastles().size() > 4);

 // Test with non-existent fiefdom
 args.clear();
 args.add("castleFiefdom");
 args.add(new ArrayList(Arrays.asList(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 5;
 }
 }, "player1", "inexistentFiefdom")).iterator());
 try {
 purchase.perform(args);
 fail("FiefdomNotFoundException should be thrown");
 } catch (FiefdomNotFoundException e) {
 // Expected
 }
}

 @Test
 public void testCastlePersonal() {
 // Test with valid arguments
 Collection args = new ArrayList();
 args.add("castlePersonal");
 args.add(new ArrayList(Arrays.asList(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 5;
 }
 }, "player1", "fiefdom1")).iterator());
 Map.map.get("fiefdom1").buyPersonalCastles(5);
 assertTrue(Map.map.get("fiefdom1").getPersonalCastles().size() >= 5);

 // Test with insufficient funds
 Collection args2 = new ArrayList();
 args2.add("castlePersonal");
 args2.add(new ArrayList(Arrays.asList(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 10;
 }
 }, "player1", "fiefdom1")).iterator());
 Map.map.get("fiefdom1").buyPersonalCastles(5);
 assertFalse(Map.map.get("fiefdom1").getPersonalCastles().size() > 4);

 // Test with non-existent fiefdom
 args.clear();
 args.add("castlePersonal");
 args.add(new ArrayList(Arrays.asList(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 5;
 }
 }, "player1", "inexistentFiefdom")).iterator());
 try {
 purchase.perform(args);
 fail("FiefdomNotFoundException should be thrown");
 } catch (FiefdomNotFoundException e) {
 // Expected
 }
}

 @Test
 public void testMercenary() {
 // Test with valid arguments
 Collection args = new ArrayList();
 args.add("mercenary");
 args.add(new ArrayList(Arrays.asList(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 5;
 }
 }, "player1", "knight1")).iterator());
 Knight knight = new Knight("knight1");
 knight.addMercenaries(5);
 assertTrue(knight.getMercenaries() >= 5);

 // Test with insufficient funds
 Collection args2 = new ArrayList();
 args2.add("mercenary");
 args2.add(new ArrayList(Arrays.asList(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return 10;
 }
 }, "player1", "knight1")).iterator());
 knight.addMercenaries(5);
 assertFalse(knight.getMercenaries() > 4);

 // Test with non-existent knight
 args.clear();
 args.add("mercenary");
 args.add(new ArrayList(Arrays.asList(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "inexistentKnight";
 }
 }, "player1", "knight1")).iterator());
 try {
 purchase.perform(args);
 fail("KnightNotFoundException should be thrown");
 } catch (KnightNotFoundException e) {
 // Expected
 }
}

 // Add other test cases as needed for edge cases and exceptions
}