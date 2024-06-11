import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.Actions;
import src.Fiefdoms;
import src.GameAutoActions;
import src.CurrentPlayers;
import src.Knight;
import src.Map;

import java.util.*;

import static org.junit.Assert.*;

public class PurchaseTest {

 private Purchase purchase;
 private Actions actions;
 private GameAutoActions gameAutoActions;
 private CurrentPlayers currentPlayers;
 private Fiefdoms fiefdom;
 private Map map;
 private List<String> argList;

 @Before
 public void setup() {
 actions = Mockito.spy(new Actions());
 purchase = new Purchase();
 purchase.setActions(actions);
 gameAutoActions = Mockito.mock(GameAutoActions.class);
 currentPlayers = new CurrentPlayers();
 fiefdom = new Fiefdoms();
 map = new Map();
 map.put("Fiefdom1", fiefdom);
 argList = new ArrayList<>();
 }

 @Test
 public void testVassal() {
 // Test valid input
 argList.add("5");
 argList.add("Player1");
 argList.add("Fiefdom1");
 fiefdom.setOwner(null);
 CurrentPlayers.currentPlayers.put("Player1", new Knight("Player1", "knight", 1000));
 CurrentPlayers.currentPlayers.put("Player2", new Knight("Player2", "knight", 1500));
 CurrentPlayers.currentPlayers.put("Player3", new Knight("Player3", "king", 5000));
 map.put("Fiefdom1", fiefdom);

 purchase.perform(argList.iterator());
 assertEquals(5, fiefdom.getLoyalVassals());

 // Test invalid input (insufficient funds)
 argList.clear();
 argList.add("10");
 argList.add("Player1");
 argList.add("Fiefdom1");
 CurrentPlayers.currentPlayers.put("Player1", new Knight("Player1", "knight", 1000));
 CurrentPlayers.currentPlayers.put("Player2", new Knight("Player2", "king", 5000));
 map.put("Fiefdom1", fiefdom);

 purchase.perform(argList.iterator());
 assertEquals(5, fiefdom.getLoyalVassals());
 assertEquals(900, CurrentPlayers.currentPlayers.get("Player1").getTotalMoney());

 // Test invalid input (fiefdom not owned by buyer or king)
 argList.clear();
 argList.add("5");
 argList.add("Player1");
 argList.add("Fiefdom2");
 CurrentPlayers.currentPlayers.put("Player1", new Knight("Player1", "knight", 1000));
 CurrentPlayers.currentPlayers.put("Player2", new Knight("Player2", "count", 1500));
 fiefdom.setOwner("Player2");
 map.put("Fiefdom1", fiefdom);

 purchase.perform(argList.iterator());
 assertEquals(5, fiefdom.getLoyalVassals());
 assertEquals(1000, CurrentPlayers.currentPlayers.get("Player1").getTotalMoney());
 }

 @Test
 public void testCastleFiefdom() {
 // Test valid input
 argList.clear();
 argList.add("3");
 argList.add("Player1");
 argList.add("Fiefdom1");
 CurrentPlayers.currentPlayers.put("Player1", new Knight("Player1", "king", 15000));
 CurrentPlayers.currentPlayers.put("Player2", new Knight("Player2", "count", 1500));
 fiefdom.setOwner(null);
 map.put("Fiefdom1", fiefdom);

 purchase.perform(argList.iterator());
 assertEquals(3, fiefdom.getCastles().size());

 // Test invalid input (insufficient funds)
 argList.clear();
 argList.add("10");
 argList.add("Player1");
 argList.add("Fiefdom1");
 CurrentPlayers.currentPlayers.put("Player1", new Knight("Player1", "king", 12000));
 map.put("Fiefdom1", fiefdom);

 purchase.perform(argList.iterator());
 assertEquals(3, fiefdom.getCastles().size());
 assertEquals(11000, CurrentPlayers.currentPlayers.get("Player1").getTotalMoney());

 // Test invalid input (fiefdom not owned by buyer)
 argList.clear();
 argList.add("3");
 argList.add("Player1");
 argList.add("Fiefdom2");
 CurrentPlayers.currentPlayers.put("Player1", new Knight("Player1", "king", 15000));
 fiefdom.setOwner("Player2");
 map.put("Fiefdom1", fiefdom);
 map.put("Fiefdom2", fiefdom);

 purchase.perform(argList.iterator());
 assertEquals(3, fiefdom.getCastles().size());
 assertEquals(15000, CurrentPlayers.currentPlayers.get("Player1").getTotalMoney());
 }

 @Test
 public void testCastlePersonal() {
 // Test valid input
 argList.clear();
 argList.add("2");
 argList.add("Player1");
 argList.add("Fiefdom1");
 CurrentPlayers.currentPlayers.put("Player1", new Knight("Player1", "king", 20000));
 CurrentPlayers.currentPlayers.put("Player2", new Knight("Player2", "count", 1500));
 fiefdom.setOwner(null);
 map.put("Fiefdom1", fiefdom);

 purchase.perform(argList.iterator());
 assertEquals(2, fiefdom.getCastlesPersonal().size());

 // Test invalid input (insufficient funds)
 argList.clear();
 argList.add("10");
 argList.add("Player1");
 argList.add("Fiefdom1");
 CurrentPlayers.currentPlayers.put("Player1", new Knight("Player1", "king", 17000));
 map.put("Fiefdom1", fiefdom);

 purchase.perform(argList.iterator());
 assertEquals(2, fiefdom.getCastlesPersonal().size());
 assertEquals(17000, CurrentPlayers.currentPlayers.get("Player1").getTotalMoney());

 // Test invalid input (fiefdom not owned by buyer)
 argList.clear();
 argList.add("2");
 argList.add("Player1");
 argList.add("Fiefdom2");
 CurrentPlayers.currentPlayers.put("Player1", new Knight("Player1", "king", 20000));
 fiefdom.setOwner("Player2");
 map.put("Fiefdom1", fiefdom);
 map.put("Fiefdom2", fiefdom);

 purchase.perform(argList.iterator());
 assertEquals(2, fiefdom.getCastlesPersonal().size());
 assertEquals(20000, CurrentPlayers.currentPlayers.get("Player1").getTotalMoney());
 }

 @Test
 public void testMercenary() {
 // Test valid input
 argList.clear();
 argList.add("5");
 argList.add("Player1");
 argList.add("Knight1");
 CurrentPlayers.currentPlayers.put("Player1", new Knight("Player1", "king", 20000));
 CurrentPlayers.currentPlayers.put("Knight1", new Knight("Knight1", "knight", 1000));

 purchase.perform(argList.iterator());
 assertEquals(5, Knight.getMercenaries("Player1").size());

 // Test invalid input (insufficient funds)
 argList.clear();
 argList.add("10");
 argList.add("Player1");
 argList.add("Knight1");
 CurrentPlayers.currentPlayers.put("Player1", new Knight("Player1", "king", 12000));

 purchase.perform(argList.iterator());
 assertEquals(5, Knight.getMercenaries("Player1").size());
 assertEquals(11000, CurrentPlayers.currentPlayers.get("Player1").getTotalMoney());

 // Test invalid input (buyer and knight are the same)
 argList.clear();
 argList.add("5");
 argList.add("Player1");
 argList.add("Player1");
 CurrentPlayers.currentPlayers.put("Player1", new Knight("Player1", "king", 20000));

 purchase.perform(argList.iterator());
 assertEquals(5, Knight.getMercenaries("Player1").size());
 assertEquals(19500, CurrentPlayers.currentPlayers.get("Player1").getTotalMoney());
 }
}