package src.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class PurchaseTest {

 private Purchase purchase;
 private ArrayList<String> args;
 private Fiefdoms map;
 private CurrentPlayers currentPlayers;

 @Before
 public void setUp() {
 // Initialize test objects and data structures
 purchase = new Purchase();
 args = new ArrayList<>();
 map = new Fiefdoms();
 currentPlayers = new CurrentPlayers();
 }

 @Test
 public void testVassal() {
 // Test valid vassal purchase
 args.add("5");
 args.add("player1");
 args.add("fiefdom1");
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, null, null, null, null, null));
 currentPlayers.put("player1", new Knight("player1", "knight", 1000));
 currentPlayers.put("player2", new Knight("player2", "count", 500));
 map.get(args.get(2)).setOwner("player2");
 purchase.perform(args);
 assertEquals(7, map.get(args.get(2)).getLoyalVassals());

 // Test invalid vassal purchase - insufficient funds
 args.clear();
 args.add("5");
 args.add("player1");
 args.add("fiefdom1");
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, null, null, null, null, null));
 currentPlayers.put("player1", new Knight("player1", "king", 500));
 currentPlayers.put("player2", new Knight("player2", "count", 500));
 map.get(args.get(2)).setOwner("player2");
 args.add("player1");
 purchase.perform(args);
 assertEquals(2, currentPlayers.get("player1").getTotalMoney());

 // Test invalid vassal purchase - not player's fiefdom
 args.clear();
 args.add("5");
 args.add("player1");
 args.add("fiefdom2");
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, null, null, null, null, null));
 map.put("fiefdom2", new Fiefdoms("fiefdom2", null, null, null, null, null, null, null, null));
 currentPlayers.put("player1", new Knight("player1", "king", 1000));
 currentPlayers.put("player2", new Knight("player2", "count", 500));
 map.get("fiefdom1").setOwner("player1");
 map.get("fiefdom2").setOwner("player3");
 purchase.perform(args);
 assertFalse(map.get("fiefdom2").getLoyalVassals().isEmpty());

 // Test invalid vassal purchase - fiefdom not available
 args.clear();
 args.add("5");
 args.add("player1");
 args.add("fiefdom3");
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, null, null, null, null, null));
 map.put("fiefdom2", new Fiefdoms("fiefdom2", null, null, null, null, null, null, null, null));
 currentPlayers.put("player1", new Knight("player1", "king", 1000));
 currentPlayers.put("player2", new Knight("player2", "count", 500));
 map.get("fiefdom1").setOwner("player1");
 map.put("fiefdom3", null);
 purchase.perform(args);
 assertNull(map.get("fiefdom3"));
 }

 @Test
 public void testCastleFiefdom() {
 // Test valid castle purchase for fiefdom
 args.clear();
 args.add("3");
 args.add("player1");
 args.add("fiefdom1");
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, null, null, null, null, null));
 currentPlayers.put("player1", new Knight("player1", "king", 3500));
 currentPlayers.put("player2", new Knight("player2", "count", 500));
 map.get(args.get(2)).setOwner("player2");
 purchase.perform(args);
 assertEquals(3, map.get(args.get(2)).getCastleCount());

 // Test invalid castle purchase for fiefdom - insufficient funds
 args.clear();
 args.add("3");
 args.add("player1");
 args.add("fiefdom1");
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, null, null, null, null, null));
 currentPlayers.put("player1", new Knight("player1", "king", 3000));
 currentPlayers.put("player2", new Knight("player2", "count", 500));
 map.get(args.get(2)).setOwner("player2");
 purchase.perform(args);
 assertEquals(0, map.get(args.get(2)).getCastleCount());

 // Test invalid castle purchase for fiefdom - not player's fiefdom
 args.clear();
 args.add("3");
 args.add("player1");
 args.add("fiefdom2");
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, null, null, null, null, null));
 map.put("fiefdom2", new Fiefdoms("fiefdom2", null, null, null, null, null, null, null, null));
 currentPlayers.put("player1", new Knight("player1", "king", 3500));
 currentPlayers.put("player2", new Knight("player2", "count", 500));
 map.get("fiefdom1").setOwner("player1");
 map.get("fiefdom2").setOwner("player3");
 purchase.perform(args);
 assertEquals(0, map.get("fiefdom2").getCastleCount());

 // Test invalid castle purchase for fiefdom - fiefdom not available
 args.clear();
 args.add("3");
 args.add("player1");
 args.add("fiefdom3");
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, null, null, null, null, null));
 map.put("fiefdom2", new Fiefdoms("fiefdom2", null, null, null, null, null, null, null, null));
 currentPlayers.put("player1", new Knight("player1", "king", 3500));
 currentPlayers.put("player2", new Knight("player2", "count", 500));
 map.get("fiefdom1").setOwner("player1");
 map.put("fiefdom3", null);
 purchase.perform(args);
 assertNull(map.get("fiefdom3"));
 }

 @Test
 public void testCastlePersonal() {
 // Test valid castle purchase for personal
 args.clear();
 args.add("2");
 args.add("player1");
 args.add("fiefdom1");
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, null, null, null, null, null));
 currentPlayers.put("player1", new Knight("player1", "king", 4000));
 currentPlayers.put("player2", new Knight("player2", "count", 500));
 map.get(args.get(2)).setOwner("player2");
 purchase.perform(args);
 assertEquals(2, currentPlayers.get("player1").getCastleCount());

 // Test invalid castle purchase for personal - insufficient funds
 args.clear();
 args.add("2");
 args.add("player1");
 args.add("fiefdom1");
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, null, null, null, null, null));
 currentPlayers.put("player1", new Knight("player1", "king", 3500));
 currentPlayers.put("player2", new Knight("player2", "count", 500));
 map.get(args.get(2)).setOwner("player2");
 purchase.perform(args);
 assertEquals(0, currentPlayers.get("player1").getCastleCount());

 // Test invalid castle purchase for personal - not player's fiefdom
 args.clear();
 args.add("2");
 args.add("player1");
 args.add("fiefdom2");
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, null, null, null, null, null));
 map.put("fiefdom2", new Fiefdoms("fiefdom2", null, null, null, null, null, null, null, null));
 currentPlayers.put("player1", new Knight("player1", "king", 4000));
 currentPlayers.put("player2", new Knight("player2", "count", 500));
 map.get("fiefdom1").setOwner("player1");
 map.get("fiefdom2").setOwner("player3");
 purchase.perform(args);
 assertEquals(0, currentPlayers.get("player2").getCastleCount());

 // Test invalid castle purchase for personal - fiefdom not available
 args.clear();
 args.add("2");
 args.add("player1");
 args.add("fiefdom3");
 map.put("fiefdom1", new Fiefdoms("fiefdom1", null, null, null, null, null, null, null, null));
 map.put("fiefdom2", new Fiefdoms("fiefdom2", null, null, null, null, null, null, null, null));
 currentPlayers.put("player1", new Knight("player1", "king", 4000));
 currentPlayers.put("player2", new Knight("player2", "count", 500));
 map.get("fiefdom1").setOwner("player1");
 map.put("fiefdom3", null);
 purchase.perform(args);
 assertNull(map.get("fiefdom3"));
 }
}