import org.junit.Test;
import org.junit.Assert;
import java.util.*;
import src.Purchase;
import src.Fiefdoms;
import src.Knight;
import src.Actions;
import src.CurrentPlayers;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms map;
 private CurrentPlayers currentPlayers;
 private List<String> argsList;

 @Before
 public void setup() {
 // Initialize objects
 purchase = new Purchase();
 map = new Fiefdoms();
 currentPlayers = new CurrentPlayers();
 argsList = new ArrayList<>();

 // Initialize test data
 map.addFiefdom("fiefdom1", "owner1", 0, 0, 0, null);
 map.addFiefdom("fiefdom2", "owner2", 0, 0, 0, null);
 currentPlayers.addPlayer("player1", "king", 1000);
 currentPlayers.addPlayer("player2", "count", 500);
 currentPlayers.addPlayer("player3", "knight", 200);
 }

 @Test
 public void performTest() {
 Iterator<String> args = argsList.iterator();
 args.next(); // Purchase.class
 args.next(); // Iterator<String>

 try {
 purchase.perform(args);
 } catch (Exception e) {
 e.printStackTrace();
 Assert.fail("Unexpected exception: " + e.getMessage());
 }

 // Test for no-op case
 Assert.assertEquals(0, map.get("fiefdom1").getLoyalVassals());
 Assert.assertEquals(0, map.get("fiefdom1").getCastles().length);
 Assert.assertEquals(1000, currentPlayers.get("player1").getTotalMoney());
 }

 @Test
 public void vassalTest() {
 argsList.clear();
 argsList.add("vassal");
 argsList.add(map.getIterator());
 argsList.add("fiefdom1");
 argsList.add("player1");

 purchase.vassal(argsList.iterator());

 // Test for valid case
 Assert.assertEquals(1, map.get("fiefdom1").getLoyalVassals());
 Assert.assertEquals(999, currentPlayers.get("player1").getTotalMoney());

 // Test for insufficient funds
 argsList.clear();
 argsList.add("vassal");
 argsList.add(map.getIterator());
 argsList.add("fiefdom1");
 argsList.add("player1");
 argsList.add("3"); // Insufficient funds
 purchase.vassal(argsList.iterator());
 Assert.assertEquals(0, map.get("fiefdom1").getLoyalVassals());
 Assert.assertEquals(1000, currentPlayers.get("player1").getTotalMoney());
 }

 @Test
 public void castleFiefdomTest() {
 argsList.clear();
 argsList.add("castleFiefdom");
 argsList.add(map.getIterator());
 argsList.add("fiefdom1");
 argsList.add("player1");

 purchase.castleFiefdom(argsList.iterator());

 // Test for valid case
 Assert.assertEquals(1, map.get("fiefdom1").getCastles().length);
 Assert.assertEquals(975, currentPlayers.get("player1").getTotalMoney());

 // Test for insufficient funds
 argsList.clear();
 argsList.add("castleFiefdom");
 argsList.add(map.getIterator());
 argsList.add("fiefdom1");
 argsList.add("player1");
 argsList.add("3"); // Insufficient funds
 purchase.castleFiefdom(argsList.iterator());
 Assert.assertEquals(0, map.get("fiefdom1").getCastles().length);
 Assert.assertEquals(1000, currentPlayers.get("player1").getTotalMoney());
 }

 @Test
 public void castlePersonalTest() {
 argsList.clear();
 argsList.add("castlePersonal");
 argsList.add(map.getIterator());
 argsList.add("fiefdom1");
 argsList.add("player1");

 purchase.castlePersonal(argsList.iterator());

 // Test for valid case
 Assert.assertEquals(1, map.get("fiefdom1").getPersonalCastles().length);
 Assert.assertEquals(990, currentPlayers.get("player1").getTotalMoney());

 // Test for insufficient funds
 argsList.clear();
 argsList.add("castlePersonal");
 argsList.add(map.getIterator());
 argsList.add("fiefdom1");
 argsList.add("player1");
 argsList.add("3"); // Insufficient funds
 purchase.castlePersonal(argsList.iterator());
 Assert.assertEquals(0, map.get("fiefdom1").getPersonalCastles().length);
 Assert.assertEquals(1000, currentPlayers.get("player1").getTotalMoney());
 }

 @Test
 public void mercenaryTest() {
 argsList.clear();
 argsList.add("mercenary");
 argsList.add(currentPlayers.getIterator());
 argsList.add("knight1");
 argsList.add("player1");

 purchase.mercenary(argsList.iterator());

 // Test for valid case
 Assert.assertEquals(2, knight1.getMercenaries().size());
 Assert.assertEquals(980, currentPlayers.get("player1").getTotalMoney());

 // Test for same knight
 argsList.clear();
 argsList.add("mercenary");
 argsList.add(currentPlayers.getIterator());
 argsList.add("player1"); // Same player
 argsList.add("knight1");
 purchase.mercenary(argsList.iterator());
 Assert.assertEquals(3, knight1.getMercenaries().size());
 Assert.assertEquals(980, currentPlayers.get("player1").getTotalMoney());

 // Test for insufficient funds
 argsList.clear();
 argsList.add("mercenary");
 argsList.add(currentPlayers.getIterator());
 argsList.add("knight1");
 argsList.add("player1");
 argsList.add("5"); // Insufficient funds
 purchase.mercenary(argsList.iterator());
 Assert.assertEquals(2, knight1.getMercenaries().size());
 Assert.assertEquals(980, currentPlayers.get("player1").getTotalMoney());
 }

 @Test(expected = IllegalArgumentException.class)
 public void illegalArgumentExceptionTest() {
 argsList.clear();
 argsList.add("invalidMethod"); // Non-existent method
 argsList.add(map.getIterator());
 argsList.add("fiefdom1");
 argsList.add("player1");

 purchase.perform(args);
 }

 @Test(expected = IllegalAccessException.class)
 public void illegalAccessExceptionTest() {
 purchase = new Purchase() {
 @Override
 public void perform(final Collection args) {
 throw new IllegalAccessException();
 }
 };

 Iterator<String> args = argsList.iterator();
 args.next(); // Purchase.class
 args.next(); // Iterator<String>

 try {
 purchase.perform(args);
 } catch (Exception e) {
 // Expected
 }
 }

 @Test(expected = InvocationTargetException.class)
 public void invocationTargetExceptionTest() {
 purchase = new Purchase() {
 @Override
 public void vassal(final Iterator arg) {
 throw new InvocationTargetException(new RuntimeException("Test target exception"));
 }
 };

 argsList.clear();
 argsList.add("vassal");
 argsList.add(map.getIterator());
 argsList.add("fiefdom1");
 argsList.add("player1");
 purchase.vassal(argsList.iterator());
 }
}