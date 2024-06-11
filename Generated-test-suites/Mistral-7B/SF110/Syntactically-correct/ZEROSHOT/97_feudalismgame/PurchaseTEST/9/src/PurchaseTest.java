package src.test.java;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class PurchaseTest {

 private Purchase purchase;
 private List<String> argsList;
 private Fiefdoms map;
 private CurrentPlayers currentPlayers;

 @Before
 public void setUp() {
 // Initialize test objects and data structures
 purchase = new Purchase();
 argsList = new ArrayList<>();
 map = new Fiefdoms();
 currentPlayers = new CurrentPlayers();
 }

 @After
 public void tearDown() {
 // Clear test objects and data structures
 purchase = null;
 argsList.clear();
 map.clear();
 currentPlayers.clear();
 }

 @Test
 public void testPerform() {
 // Test with valid arguments
 argsList.add("vassal");
 argsList.add(Arrays.asList(1, "buyer1", "fiefdom1").iterator());
 purchase.perform(argsList);
 // Test with invalid arguments or exceptions
 argsList.clear();
 argsList.add("castleFiefdom");
 argsList.add(Arrays.asList("invalidArg", Arrays.asList("fiefdom1", "buyer1").iterator()).iterator());
 purchase.perform(argsList);
 }

 @Test
 public void testVassal() {
 // Test with valid arguments and sufficient funds
 argsList.clear();
 argsList.add("vassal");
 argsList.add(Arrays.asList(2, "buyer1", "fiefdom1").iterator());
 addTestPlayersAndFiefdom();
 map.addFiefdom("fiefdom1", new Fiefdom("fiefdom1", "owner1"));
 currentPlayers.addPlayer("buyer1", new Knight("buyer1", "knight", 100));
 purchase.perform(argsList);
 assertEquals(1, map.get("fiefdom1").getLoyalVassals());
 // Test with insufficient funds
 argsList.clear();
 argsList.add("vassal");
 argsList.add(Arrays.asList(3, "buyer1", "fiefdom1").iterator());
 addTestPlayersAndFiefdom();
 map.addFiefdom("fiefdom1", new Fiefdom("fiefdom1", "owner1"));
 currentPlayers.addPlayer("buyer1", new Knight("buyer1", "knight", 50));
 purchase.perform(argsList);
 assertEquals(1, map.get("fiefdom1").getLoyalVassals());
 }

 @Test
 public void testCastleFiefdom() {
 // Test with valid arguments and sufficient funds
 argsList.clear();
 argsList.add("castleFiefdom");
 argsList.add(Arrays.asList(2, "buyer1", "fiefdom1").iterator());
 addTestPlayersAndFiefdom();
 map.addFiefdom("fiefdom1", new Fiefdom("fiefdom1", "owner1"));
 currentPlayers.addPlayer("buyer1", new Knight("buyer1", "knight", 200));
 purchase.perform(argsList);
 assertEquals(2, map.get("fiefdom1").getCastles().size());
 // Test with insufficient funds
 argsList.clear();
 argsList.add("castleFiefdom");
 argsList.add(Arrays.asList(3, "buyer1", "fiefdom1").iterator());
 addTestPlayersAndFiefdom();
 map.addFiefdom("fiefdom1", new Fiefdom("fiefdom1", "owner1"));
 currentPlayers.addPlayer("buyer1", new Knight("buyer1", "knight", 150));
 purchase.perform(argsList);
 assertEquals(2, map.get("fiefdom1").getCastles().size());
 }

 @Test
 public void testCastlePersonal() {
 // Test with valid arguments and sufficient funds
 argsList.clear();
 argsList.add("castlePersonal");
 argsList.add(Arrays.asList(2, "buyer1", "fiefdom1").iterator());
 addTestPlayersAndFiefdom();
 map.addFiefdom("fiefdom1", new Fiefdom("fiefdom1", "owner1"));
 currentPlayers.addPlayer("buyer1", new Knight("buyer1", "knight", 200));
 purchase.perform(argsList);
 assertEquals(2, currentPlayers.get("buyer1").getCastles().size());
 // Test with insufficient funds
 argsList.clear();
 argsList.add("castlePersonal");
 argsList.add(Arrays.asList(3, "buyer1", "fiefdom1").iterator());
 addTestPlayersAndFiefdom();
 map.addFiefdom("fiefdom1", new Fiefdom("fiefdom1", "owner1"));
 currentPlayers.addPlayer("buyer1", new Knight("buyer1", "knight", 150));
 purchase.perform(argsList);
 assertEquals(2, currentPlayers.get("buyer1").getCastles().size());
 }

 @Test
 public void testMercenary() {
 // Test with valid arguments and sufficient funds
 argsList.clear();
 argsList.add("mercenary");
 argsList.add(Arrays.asList(2, "buyer1", "knight1").iterator());
 addTestPlayers();
 currentPlayers.addPlayer("buyer1", new Knight("buyer1", "knight", 300));
 map.addPlayer("knight1", new Knight("knight1", "knight", 10));
 purchase.perform(argsList);
 assertEquals(2, currentPlayers.get("buyer1").getMercenaries().size());
 // Test with insufficient funds
 argsList.clear();
 argsList.add("mercenary");
 argsList.add(Arrays.asList(3, "buyer1", "knight1").iterator());
 addTestPlayers();
 currentPlayers.addPlayer("buyer1", new Knight("buyer1", "knight", 250));
 map.addPlayer("knight1", new Knight("knight1", "knight", 10));
 purchase.perform(argsList);
 assertEquals(2, currentPlayers.get("buyer1").getMercenaries().size());
 }

 @Test
 public void testInvalidArguments() {
 // Test with invalid arguments
 argsList.clear();
 argsList.add("invalidMethod");
 argsList.add(Arrays.asList("vassal", Arrays.asList(1, "buyer1", "fiefdom1").iterator()).iterator());
 purchase.perform(argsList);
 // Test with empty arguments
 argsList.clear();
 argsList.add("vassal");
 argsList.add(new ArrayList<>().iterator());
 purchase.perform(argsList);
 }

 private void addTestPlayers() {
 currentPlayers.addPlayer("knight1", new Knight("knight1", "knight", 10));
 currentPlayers.addPlayer("knight2", new Knight("knight2", "knight", 10));
 }

 private void addTestPlayersAndFiefdom() {
 addTestPlayers();
 map.addFiefdom("fiefdom1", new Fiefdom("fiefdom1", "owner1"));
 map.addFiefdom("fiefdom2", new Fiefdom("fiefdom2", "owner2"));
 }
}