package src.test;

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

 @BeforeClass
 public static void setUpClass() {
 // Initialize static objects like Map, CurrentPlayers and GameAutoActions
 Map.map = new HashMap<>();
 CurrentPlayers.currentPlayers = new HashMap<>();
 GameAutoActions.saveAll = new ArrayList<>();
 }

 @Before
 public void setUp() {
 // Initialize the test objects
 map = new Fiefdoms();
 currentPlayers = new CurrentPlayers();
 purchase = new Purchase();
 gameAutoActions = new GameAutoActions();

 // Initialize some test data
 CurrentPlayers.currentPlayers.put("John", new Knight("John", "knight", 1000));
 CurrentPlayers.currentPlayers.put("Alice", new Knight("Alice", "duke", 5000));
 CurrentPlayers.currentPlayers.put("Bob", new Knight("Bob", "count", 2000));
 map.put("Fiefdom1", new Fiefdoms("Fiefdom1", null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
 map.put("Fiefdom2", new Fiefdoms("Fiefdom2", null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
 map.put("Fiefdom3", new Fiefdoms("Fiefdom3", null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
 }

 @Test
 public void testPerform() {
 // Test the perform method with valid arguments
 Collection<Object> args = new ArrayList<>();
 args.add("feudalism.Purchase");
 args.add(purchase.argsIterator());
 purchase.perform(args);
 // Test the perform method with invalid arguments
 Collection<Object> invalidArgs = new ArrayList<>();
 args.clear();
 args.add("invalidClass");
 args.add(purchase.argsIterator());
 try {
 purchase.perform(args);
 fail("Expected SecurityException or NoSuchMethodException");
 } catch (SecurityException e) {
 // Expected exception
 } catch (NoSuchMethodException e) {
 // Expected exception
 }
 }

 @Test
 public void testVassal() {
 // Test buying vassals for a knight with sufficient funds
 args = new ArrayList<>();
 args.add(purchase.iterator("vassal"));
 args.add(map.iterator());
 args.add(currentPlayers.iterator());
 args.add(currentPlayers.iterator());
 args.add("John");
 args.add("Fiefdom1");
 purchase.perform(args);
 assertEquals(1, currentPlayers.get("John").getTotalLoyalVassals());
 // Test buying vassals for a knight with insufficient funds
 args.clear();
 args.add(purchase.iterator("vassal"));
 args.add(map.iterator());
 args.add(currentPlayers.iterator());
 args.add(currentPlayers.iterator());
 args.add("John");
 args.add("Fiefdom1");
 currentPlayers.get("John").setTotalMoney(999);
 purchase.perform(args);
 assertEquals(0, currentPlayers.get("John").getTotalLoyalVassals());
 // Test buying vassals for a knight trying to buy for a fiefdom that doesn't belong to him
 args.clear();
 args.add(purchase.iterator("vassal"));
 args.add(map.iterator());
 args.add(currentPlayers.iterator());
 args.add(currentPlayers.iterator());
 args.add("John");
 args.add("Fiefdom3");
 purchase.perform(args);
 assertEquals(0, currentPlayers.get("John").getTotalLoyalVassals());
 // Test buying vassals for a knight trying to buy for a fiefdom that belongs to another player
 args.clear();
 args.add(purchase.iterator("vassal"));
 args.add(map.iterator());
 args.add(currentPlayers.iterator());
 args.add(currentPlayers.iterator());
 args.add("Alice");
 args.add("Fiefdom1");
 purchase.perform(args);
 assertEquals(0, currentPlayers.get("John").getTotalLoyalVassals());
 }

 @Test
 public void testCastleFiefdom() {
 // Test buying fiefdom castles for a knight with sufficient funds
 args = new ArrayList<>();
 args.add(purchase.iterator("castleFiefdom"));
 args.add(map.iterator());
 args.add(currentPlayers.iterator());
 args.add(currentPlayers.iterator());
 args.add("John");
 args.add("Fiefdom1");
 purchase.perform(args);
 assertTrue(map.get("Fiefdom1").getCastles().size() >= 1);
 // Test buying fiefdom castles for a knight with insufficient funds
 args.clear();
 args.add(purchase.iterator("castleFiefdom"));
 args.add(map.iterator());
 args.add(currentPlayers.iterator());
 args.add(currentPlayers.iterator());
 args.add("John");
 args.add("Fiefdom1");
 currentPlayers.get("John").setTotalMoney(999 * 25);
 purchase.perform(args);
 assertEquals(1, map.get("Fiefdom1").getCastles().size());
 // Test buying fiefdom castles for a knight trying to buy for a fiefdom that doesn't belong to him
 args.clear();
 args.add(purchase.iterator("castleFiefdom"));
 args.add(map.iterator());
 args.add(currentPlayers.iterator());
 args.add(currentPlayers.iterator());
 args.add("John");
 args.add("Fiefdom3");
 purchase.perform(args);
 assertEquals(0, map.get("Fiefdom3").getCastles().size());
 // Test buying fiefdom castles for a knight trying to buy for a fiefdom that belongs to another player
 args.clear();
 args.add(purchase.iterator("castleFiefdom"));
 args.add(map.iterator());
 args.add(currentPlayers.iterator());
 args.add(currentPlayers.iterator());
 args.add("Alice");
 args.add("Fiefdom1");
 purchase.perform(args);
 assertEquals(1, map.get("Fiefdom1").getCastles().size());
 }

 @Test
 public void testCastlePersonal() {
 // Test buying personal castles for a knight with sufficient funds
 args = new ArrayList<>();
 args.add(purchase.iterator("castlePersonal"));
 args.add(map.iterator());
 args.add(currentPlayers.iterator());
 args.add(currentPlayers.iterator());
 args.add("John");
 args.add("Fiefdom1");
 purchase.perform(args);
 assertTrue(currentPlayers.get("John").getTotalPersonalCastles() >= 1);
 // Test buying personal castles for a knight with insufficient funds
 args.clear();
 args.add(purchase.iterator("castlePersonal"));
 args.add(map.iterator());
 args.add(currentPlayers.iterator());
 args.add(currentPlayers.iterator());
 args.add("John");
 args.add("Fiefdom1");
 currentPlayers.get("John").setTotalMoney(999 * 10);
 purchase.perform(args);
 assertEquals(0, currentPlayers.get("John").getTotalPersonalCastles());
 // Test buying personal castles for a knight trying to buy for a fiefdom that doesn't belong to him
 args.clear();
 args.add(purchase.iterator("castlePersonal"));
 args.add(map.iterator());
 args.add(currentPlayers.iterator());
 args.add(currentPlayers.iterator());
 args.add("John");
 args.add("Fiefdom3");
 purchase.perform(args);
 assertEquals(0, currentPlayers.get("John").getTotalPersonalCastles());
 // Test buying personal castles for a knight trying to buy for a fiefdom that belongs to another player
 args.clear();
 args.add(purchase.iterator("castlePersonal"));
 args.add(map.iterator());
 args.add(currentPlayers.iterator());
 args.add(currentPlayers.iterator());
 args.add("Alice");
 args.add("Fiefdom1");
 purchase.perform(args);
 assertEquals(0, currentPlayers.get("John").getTotalPersonalCastles());
 }

 @Test
 public void testMercenary() {
 // Test buying mercenaries for a knight with sufficient funds
 args = new ArrayList<>();
 args.add(purchase.iterator("mercenary"));
 args.add(map.iterator());
 args.add(currentPlayers.iterator());
 args.add(currentPlayers.iterator());
 args.add("John");
 args.add(currentPlayers.iterator().next());
 purchase.perform(args);
 assertTrue(currentPlayers.get("John").getTotalMercenaries() >= 1);
 // Test buying mercenaries for a knight with insufficient funds
 args.clear();
 args.add(purchase.iterator("mercenary"));
 args.add(map.iterator());
 args.add(currentPlayers.iterator());
 args.add(currentPlayers.iterator());
 args.add("John");
 args.add(currentPlayers.iterator().next());
 currentPlayers.get("John").setTotalMoney(999);
 purchase.perform(args);
 assertEquals(0, currentPlayers.get("John").getTotalMercenaries());
 // Test buying mercenaries for a knight trying to buy for himself
 args.clear();
 args.add(purchase.iterator("mercenary"));
 args.add(map.iterator());
 args.add(currentPlayers.iterator());
 args.add(currentPlayers.iterator());
 args.add("John");
 args.add(currentPlayers.iterator().next());
 purchase.perform(args);
 assertEquals(0, currentPlayers.get("John").getTotalMercenaries());
 }

 @After
 public void tearDown() {
 // Reset the static objects for the next test
 CurrentPlayers.currentPlayers.clear();
 Map.map.clear();
 }

}