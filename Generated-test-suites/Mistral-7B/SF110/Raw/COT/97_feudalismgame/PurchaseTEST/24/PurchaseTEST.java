import org.junit.Test;
import org.junit.Assert;
import src.Purchase;
import src.Fiefdoms;
import src.Knight;
import src.CurrentPlayers;
import java.util.*;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms map;
 private CurrentPlayers currentPlayers;

 @Before
 public void setUp() {
 // Initialize objects used in tests
 purchase = new Purchase();
 map = new Fiefdoms();
 currentPlayers = new CurrentPlayers();
 }

 @Test
 public void testPerform() {
 // Test for empty args
 Collection<Object> emptyArgs = new ArrayList<>();
 purchase.perform(emptyArgs);
 // Test for correct method call
 Collection<Object> args = new ArrayList<>();
 args.add("vassal");
 args.add(map.iterator());
 purchase.perform(args);
 }

 @Test(expected = SecurityException.class)
 public void testPerform_SecurityException() {
 // Test for SecurityException
 Collection<Object> args = new ArrayList<>();
 args.add("unreachableMethod");
 purchase.perform(args);
 }

 @Test(expected = NoSuchMethodException.class)
 public void testPerform_NoSuchMethodException() {
 // Test for NoSuchMethodException
 Collection<Object> args = new ArrayList<>();
 args.add("unreachableMethod");
 args.add(map.iterator());
 purchase.perform(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_IllegalArgumentException() {
 // Test for IllegalArgumentException
 Collection<Object> args = new ArrayList<>();
 args.add("unreachableMethod");
 args.add(map.iterator());
 purchase.perform(args);
 }

 @Test(expected = IllegalAccessException.class)
 public void testPerform_IllegalAccessException() {
 // Test for IllegalAccessException
 Collection<Object> args = new ArrayList<>();
 args.add("unreachableMethod");
 args.add(map.iterator());
 purchase.perform(args);
 }

 @Test(expected = InvocationTargetException.class)
 public void testPerform_InvocationTargetException() {
 // Test for InvocationTargetException
 Collection<Object> args = new ArrayList<>();
 args.add("unreachableMethod");
 args.add(map.iterator());
 purchase.perform(args);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testPerform_ClassNotFoundException() {
 // Test for ClassNotFoundException
 Collection<Object> args = new ArrayList<>();
 args.add("unreachableClass");
 purchase.perform(args);
 }

 @Test
 public void testVassal() {
 // Test for valid call
 Collection<Object> args = new ArrayList<>();
 args.add(map.iterator());
 args.add("vassal");
 args.add("Alice");
 args.add("Albany");
 args.add("Albany");
 purchase.perform(args);
 Assert.assertEquals(1, Map.map.get("Albany").getLoyalVassals());
 // Test for invalid call - insufficient funds
 args.add(2);
 purchase.perform(args);
 Assert.assertEquals(1, Map.map.get("Albany").getLoyalVassals());
 // Test for invalid call - not owner
 args.clear();
 args.add(map.iterator());
 args.add("vassal");
 args.add("Alice");
 args.add("Dublin");
 args.add("Bob");
 purchase.perform(args);
 Assert.assertEquals(0, Map.map.get("Dublin").getLoyalVassals());
 // Test for invalid call - already owner
 args.clear();
 args.add(map.iterator());
 args.add("vassal");
 args.add("Alice");
 args.add("Albany");
 args.add("Alice");
 purchase.perform(args);
 Assert.assertEquals(1, Map.map.get("Albany").getLoyalVassals());
 }

 @Test
 public void testCastleFiefdom() {
 // Test for valid call
 Collection<Object> args = new ArrayList<>();
 args.add(map.iterator());
 args.add("castleFiefdom");
 args.add("Alice");
 args.add("Albany");
 args.add("Albany");
 purchase.perform(args);
 Assert.assertEquals(1, Map.map.get("Albany").getCastles().size());
 // Test for invalid call - insufficient funds
 args.clear();
 args.add(map.iterator());
 args.add("castleFiefdom");
 args.add("Alice");
 args.add("Albany");
 args.add("Albany");
 args.add(2);
 purchase.perform(args);
 Assert.assertEquals(1, Map.map.get("Albany").getCastles().size());
 // Test for invalid call - not owner
 args.clear();
 args.add(map.iterator());
 args.add("castleFiefdom");
 args.add("Alice");
 args.add("Dublin");
 args.add("Bob");
 purchase.perform(args);
 Assert.assertEquals(0, Map.map.get("Dublin").getCastles().size());
 // Test for invalid call - castle already owned
 args.clear();
 args.add(map.iterator());
 args.add("castleFiefdom");
 args.add("Alice");
 args.add("Albany");
 args.add("Alice");
 purchase.perform(args);
 Assert.assertEquals(1, Map.map.get("Albany").getCastles().size());
 }

 @Test
 public void testCastlePersonal() {
 // Test for valid call
 Collection<Object> args = new ArrayList<>();
 args.add(map.iterator());
 args.add("castlePersonal");
 args.add("Alice");
 args.add("Albany");
 args.add("Albany");
 purchase.perform(args);
 Assert.assertEquals(1, Map.map.get("Albany").getPersonalCastles().size());
 // Test for invalid call - insufficient funds
 args.clear();
 args.add(map.iterator());
 args.add("castlePersonal");
 args.add("Alice");
 args.add("Albany");
 args.add("Albany");
 args.add(2);
 purchase.perform(args);
 Assert.assertEquals(1, Map.map.get("Albany").getPersonalCastles().size());
 // Test for invalid call - not owner
 args.clear();
 args.add(map.iterator());
 args.add("castlePersonal");
 args.add("Alice");
 args.add("Dublin");
 args.add("Bob");
 purchase.perform(args);
 Assert.assertEquals(0, Map.map.get("Dublin").getPersonalCastles().size());
 // Test for invalid call - castle already owned by another player
 args.clear();
 args.add(map.iterator());
 args.add("castlePersonal");
 args.add("Alice");
 args.add("Albany");
 args.add("Charles");
 purchase.perform(args);
 Assert.assertEquals(1, Map.map.get("Albany").getPersonalCastles().size());
 }

 @Test
 public void testMercenary() {
 // Test for valid call
 Collection<Object> args = new ArrayList<>();
 args.add(map.iterator());
 args.add("mercenary");
 args.add(map.iterator());
 args.add("Alice");
 purchase.perform(args);
 Assert.assertEquals(1, CurrentPlayers.currentPlayers.get("Alice").getMercenaries().size());
 // Test for invalid call - not owner
 args.clear();
 args.add(map.iterator());
 args.add("mercenary");
 args.add(map.iterator());
 args.add("Bob");
 purchase.perform(args);
 Assert.assertEquals(0, CurrentPlayers.currentPlayers.get("Bob").getMercenaries().size());
 // Test for invalid call - not enough money
 args.clear();
 args.add(map.iterator());
 args.add("mercenary");
 args.add(map.iterator());
 args.add("Alice");
 args.add(3);
 purchase.perform(args);
 Assert.assertEquals(1, CurrentPlayers.currentPlayers.get("Alice").getMercenaries().size());
 }
}