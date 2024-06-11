package src.test.java;

import static org.junit.Assert.*;
import src.Fiefdoms;
import src.GameAutoActions;
import src.CurrentPlayers;
import src.Map;
import src.Knight;
import src.Purchase;
import java.util.*;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms map;
 private CurrentPlayers currentPlayers;
 private GameAutoActions gameAutoActions;

 @BeforeClass
 public static void setupClass() {
 // Initialize the static GameAutoActions
 GameAutoActions.init();
 }

 @Before
 public void setup() {
 // Initialize objects
 map = new Fiefdoms();
 currentPlayers = new CurrentPlayers();
 purchase = new Purchase();
 gameAutoActions = new GameAutoActions();

 // Initialize Test data
 String kingName = "King1";
 String countName = "Count1";
 String fiefdomName = "Fiefdom1";
 String knightName = "Knight1";

 // Initialize Test data - Create Test Players
 currentPlayers.addPlayer(new Knight(kingName, "king"));
 currentPlayers.addPlayer(new Knight(countName, "count"));

 // Initialize Test data - Create Test Fiefdom
 map.put(fiefdomName, new Fiefdoms(fiefdomName, null));

 // Initialize Test data - Set up Fiefdom Owner
 ((Fiefdoms) map.get(fiefdomName)).setOwner(currentPlayers.getPlayerByName(kingName));
 }

 @Test
 public void testVassal() {
 // Test case for vassal purchase by a King
 Iterator<String> args = Arrays.asList(new String[]{"5", kingName, fiefdomName}).iterator();
 purchase.perform(Collections.singletonList(args));
 assertEquals(5, ((Fiefdoms) map.get(fiefdomName)).getLoyalVassals());

 // Test case for vassal purchase by a non-owner Count
 Iterator<String> argsCount = Arrays.asList(new String[]{"5", countName, fiefdomName}).iterator();
 purchase.perform(Collections.singletonList(argsCount));
 assertNotEquals(5, ((Fiefdoms) map.get(fiefdomName)).getLoyalVassals());
 }

 @Test
 public void testCastleFiefdom() {
 // Test case for castleFiefdom purchase by a King
 Iterator<String> argsKing = Arrays.asList(new String[]{"5", kingName, fiefdomName}).iterator();
 purchase.perform(Collections.singletonList(argsKing));
 assertEquals(5, ((Fiefdoms) map.get(fiefdomName)).getCastles().size());

 // Test case for castleFiefdom purchase by a Count who doesn't own the fiefdom
 Iterator<String> argsCountNoOwner = Arrays.asList(new String[]{"5", countName, fiefdomName}).iterator();
 purchase.perform(Collections.singletonList(argsCountNoOwner));
 assertNotEquals(5, ((Fiefdoms) map.get(fiefdomName)).getCastles().size());
 }

 @Test
 public void testCastlePersonal() {
 // Test case for castlePersonal purchase by a King
 Iterator<String> argsKing = Arrays.asList(new String[]{"5", kingName, fiefdomName}).iterator();
 purchase.perform(Collections.singletonList(argsKing));
 assertEquals(5, ((Fiefdoms) map.get(fiefdomName)).getPersonalCastles().size());

 // Test case for castlePersonal purchase by a non-owner Count
 Iterator<String> argsCountNoOwner = Arrays.asList(new String[]{"5", countName, fiefdomName}).iterator();
 purchase.perform(Collections.singletonList(argsCountNoOwner));
 assertNotEquals(5, ((Fiefdoms) map.get(fiefdomName)).getPersonalCastles().size());
 }

 @Test
 public void testMercenary() {
 // Test case for mercenary purchase by a King
 Iterator<String> argsKing = Arrays.asList(new String[]{"5", kingName, knightName}).iterator();
 purchase.perform(Collections.singletonList(argsKing));
 assertEquals(5, ((Knight) currentPlayers.getPlayerByName(knightName)).getMercenaries());

 // Test case for mercenary purchase by a non-existent Knight
 Iterator<String> argsInexistent = Arrays.asList(new String[]{"5", kingName, "KnightX"}).iterator();
 purchase.perform(Collections.singletonList(argsInexistent));
 assertNotEquals(5, ((Knight) currentPlayers.getPlayerByName("KnightX")).getMercenaries());
 }
}