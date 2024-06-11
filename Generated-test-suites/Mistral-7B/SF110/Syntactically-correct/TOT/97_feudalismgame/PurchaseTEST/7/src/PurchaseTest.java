package src.test;

import org.junit.Test;
import src.Fiefdoms;
import src.GameAutoActions;
import src.CurrentPlayers;
import src.Map;
import src.Actions;
import src.Knight;
import src.Purchase;
import java.util.*;

public class PurchaseTest {

 @BeforeClass
 public static void setup() {
 CurrentPlayers.currentPlayers = new HashMap<>();
 Map.map = new HashMap<>();
 GameAutoActions.saveAll = new ArrayList<>();
 }

 @AfterClass
 public static void teardown() {
 CurrentPlayers.currentPlayers.clear();
 Map.map.clear();
 GameAutoActions.saveAll.clear();
 }

 @Test
 public void testVassal() {
 Purchase purchase = new Purchase();
 CurrentPlayers.currentPlayers.put("player1", new Knight("player1", "knight", 100, new ArrayList<>(), null, null));
 CurrentPlayers.currentPlayers.put("player2", new Knight("player2", "king", 500, new ArrayList<>(), null, null));
 Map.map.put("fiefdom1", new Fiefdoms("fiefdom1", "player2", null));

 Iterator<String> args = new ArrayList<String>(Arrays.asList("1", "player1", "fiefdom1").iterator());
 purchase.perform(args);

 assert(purchase.vassal(args).equals("You cannot buy more vassals, you have insufficient funds"));
 args = new ArrayList<String>(Arrays.asList("2", "player1", "fiefdom1").iterator());
 purchase.perform(args);

 assert(purchase.vassal(args).equals("You can only buy a castle for a fiefdom that belongs to you"));
 args = new ArrayList<String>(Arrays.asList("player1", "fiefdom1.getOwner().getName().toString().iterator()")).iterator();
 purchase.perform(args);

 assert(purchase.vassal(args).equals("Fiefdom doesn't belong to the kingdom: fiefdom1 owner: null"));
 args = new ArrayList<String>(Collections.nCopies(3, "player1")).iterator();
 purchase.perform(args);

 assert(Map.map.get("fiefdom1").getLoyalVassals() == 3);
 }

 @Test
 public void testCastleFiefdom() {
 Purchase purchase = new Purchase();
 CurrentPlayers.currentPlayers.put("player1", new Knight("player1", "knight", 500, new ArrayList<>(), null, null));
 CurrentPlayers.currentPlayers.put("player2", new Knight("player2", "king", 1000, new ArrayList<>(), null, null));
 Map.map.put("fiefdom1", new Fiefdoms("fiefdom1", "player2", null));

 Iterator<String> args = new ArrayList<String>(Arrays.asList("1", "player1", "fiefdom1").iterator());
 purchase.perform(args);

 assert(purchase.castleFiefdom(args).equals("You cannot buy more Fiefdom castles, you have insufficient funds"));
 args = new ArrayList<String>(Arrays.asList("3", "player1", "fiefdom1").iterator());
 purchase.perform(args);

 assert(Map.map.get("fiefdom1").getCastlesFiefdom() == 2);
 args = new ArrayList<String>(Arrays.asList("player2", "fiefdom1.getOwner().getName().toString().iterator()")).iterator();
 purchase.perform(args);

 assert(purchase.castleFiefdom(args).equals("You can only buy a castle for a fiefdom that belongs to you"));
 }

 @Test
 public void testCastlePersonal() {
 Purchase purchase = new Purchase();
 CurrentPlayers.currentPlayers.put("player1", new Knight("player1", "knight", 500, new ArrayList<>(), null, null));
 CurrentPlayers.currentPlayers.put("player2", new Knight("player2", "king", 1000, new ArrayList<>(), null, null));
 Map.map.put("fiefdom1", new Fiefdoms("fiefdom1", "player2", null));

 Iterator<String> args = new ArrayList<String>(Arrays.asList("1", "player1", "fiefdom1").iterator());
 purchase.perform(args);

 assert(purchase.castlePersonal(args).equals("You cannot buy more Personal castle, you have insufficient funds"));
 args = new ArrayList<String>(Arrays.asList("2", "player1", "fiefdom1").iterator());
 purchase.perform(args);

 assert(Map.map.get("fiefdom1").getCastlesPersonal() == 2);
 args = new ArrayList<String>(Arrays.asList("player2", "fiefdom1.getOwner().getName().toString().iterator()")).iterator();
 purchase.perform(args);

 assert(purchase.castlePersonal(args).equals("You can only buy a castle for a fiefdom that belongs to you"));
 }

 @Test
 public void testMercenary() {
 Purchase purchase = new Purchase();
 CurrentPlayers.currentPlayers.put("player1", new Knight("player1", "knight", 500, new ArrayList<>(), null, null));
 CurrentPlayers.currentPlayers.put("player2", new Knight("player2", "knight", 500, new ArrayList<>(), null, null));

 Iterator<String> args = new ArrayList<String>(Arrays.asList("1", "player1", "player2").iterator());
 purchase.perform(args);

 assert(purchase.mercenary(args).equals("Inexistent Knight"));
 args = new ArrayList<String>(Arrays.asList("player1", "player1").iterator());
 purchase.perform(args);

 assert(CurrentPlayers.currentPlayers.get("player1").getMercenaries().size() == 1);
 args = new ArrayList<String>(Arrays.asList("player1", "player2").iterator());
 purchase.perform(args);

 assert(CurrentPlayers.currentPlayers.get("player1").getMercenaries().size() == 2);
 args = new ArrayList<String>(Arrays.asList("player1", "player1").iterator());
 purchase.perform(args);

 assert(CurrentPlayers.currentPlayers.get("player1").getMercenaries().size() == 3);
 }
}