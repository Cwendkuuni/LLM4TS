import org.junit.Test;
import static org.junit.Assert.*;
import src.Purchase;
import src.Fiefdoms;
import src.Map;
import src.CurrentPlayers;
import src.Knight;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms map;
 private CurrentPlayers currentPlayers;

 @Before
 public void setup() {
 // Initialize the Purchase object, Fiefdoms map, CurrentPlayers and knights
 // with necessary data for testing
 purchase = new Purchase();
 map = new Fiefdoms();
 currentPlayers = new CurrentPlayers();
 // Add some knights and fiefdoms for testing
 // ...
 }

 @Test
 public void testPerform() {
 // Test case for perform method with valid arguments
 Collection<Object> args = new ArrayList<>();
 args.add("vassal");
 args.add(map.iterator());
 purchase.perform(args);
 // Test case for perform method with an invalid method name
 args.clear();
 args.add("invalidMethod");
 args.add(map.iterator());
 try {
 purchase.perform(args);
 fail("Expected SecurityException or NoSuchMethodException");
 } catch (Exception e) {
 // Expected exception
 }
 // Test case for perform method with an invalid argument type
 args.clear();
 args.add("perform");
 args.add(new Object()[0]);
 try {
 purchase.perform(args);
 fail("Expected IllegalArgumentException or InvocationTargetException");
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test
 public void testVassal() {
 // Test case for vassal method with valid arguments
 Collection<Object> args = new ArrayList<>();
 args.add("vassal");
 args.add(map.iterator());
 args.add("Alfred");
 args.add("fiefdom1");
 purchase.perform(args);
 // Test case for vassal method with insufficient funds
 args.clear();
 args.add("vassal");
 args.add(map.iterator());
 args.add("Alfred");
 args.add("fiefdom1");
 args.add("10");
 currentPlayers.get("Alfred").setTotalMoney(100);
 currentPlayers.get("Alfred").setFiefdomsTotalMoney("fiefdom1", 100);
 purchase.perform(args);
 assertEquals(9, currentPlayers.get("Alfred").getTotalMoney());
 // Test case for vassal method with a non-existent fiefdom
 args.clear();
 args.add("vassal");
 args.add(map.iterator());
 args.add("Alfred");
 args.add("nonExistentFiefdom");
 purchase.perform(args);
 assertEquals("Fiefdom doesn't belong to the kingdom", System.out.getMessage());
 }

 @Test
 public void testCastleFiefdom() {
 // Test case for castleFiefdom method with valid arguments
 Collection<Object> args = new ArrayList<>();
 args.add("castleFiefdom");
 args.add(map.iterator());
 args.add("Alfred");
 args.add("fiefdom1");
 purchase.perform(args);
 // Test case for castleFiefdom method with insufficient funds
 args.clear();
 args.add("castleFiefdom");
 args.add(map.iterator());
 args.add("Alfred");
 args.add("fiefdom1");
 args.add("5");
 currentPlayers.get("Alfred").setTotalMoney(500);
 currentPlayers.get("Alfred").setFiefdomsTotalMoney("fiefdom1", 1000);
 purchase.perform(args);
 assertEquals(4500, currentPlayers.get("Alfred").getTotalMoney());
 // Test case for castleFiefdom method with a non-existent fiefdom
 args.clear();
 args.add("castleFiefdom");
 args.add(map.iterator());
 args.add("Alfred");
 args.add("nonExistentFiefdom");
 purchase.perform(args);
 assertEquals("Fiefdom doesn't belong to the kingdom", System.out.getMessage());
 }

 @Test
 public void testCastlePersonal() {
 // Test case for castlePersonal method with valid arguments
 Collection<Object> args = new ArrayList<>();
 args.add("castlePersonal");
 args.add(map.iterator());
 args.add("Alfred");
 args.add("fiefdom1");
 purchase.perform(args);
 // Test case for castlePersonal method with insufficient funds
 args.clear();
 args.add("castlePersonal");
 args.add(map.iterator());
 args.add("Alfred");
 args.add("fiefdom1");
 args.add("5");
 currentPlayers.get("Alfred").setTotalMoney(500);
 purchase.perform(args);
 assertEquals(475, currentPlayers.get("Alfred").getTotalMoney());
 // Test case for castlePersonal method with a non-existent fiefdom
 args.clear();
 args.add("castlePersonal");
 args.add(map.iterator());
 args.add("Alfred");
 args.add("nonExistentFiefdom");
 purchase.perform(args);
 assertEquals("Fiefdom doesn't belong to the kingdom: nonExistentFiefdom owner: null", System.out.getMessage());
 }

 @Test
 public void testMercenary() {
 // Test case for mercenary method with valid arguments
 Collection<Object> args = new ArrayList<>();
 args.add("mercenary");
 args.add(map.iterator());
 args.add(new Knight("Knight1", "knight"));
 args.add("Alfred");
 purchase.perform(args);
 // Test case for mercenary method with insufficient funds or same knight
 args.clear();
 args.add("mercenary");
 args.add(map.iterator());
 args.add(new Knight("Alfred", "king"));
 args.add("Alfred");
 purchase.perform(args);
 assertEquals(1000, currentPlayers.get("Alfred").getTotalMoney());
 // Test case for mercenary method with non-existent knight
 args.clear();
 args.add("mercenary");
 args.add(map.iterator());
 args.add(new Knight("InexistentKnight", "knight"));
 args.add("Alfred");
 purchase.perform(args);
 assertEquals("Inexistent Knight", System.out.getMessage());
 }
}