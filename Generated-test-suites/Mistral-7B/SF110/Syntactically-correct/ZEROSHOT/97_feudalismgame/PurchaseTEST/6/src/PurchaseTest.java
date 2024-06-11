package src.test.java;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import src.map.Fiefdoms;
import src.map.Map;
import src.player.Knight;
import src.player.Rank;
import src.player.Player;
import java.util.*;

@RunWith(Parameterized.class)
public class PurchaseTest {

 private Purchase purchase;
 private List<Object> args;

 @Parameterized.Parameters(name = "{index}: test_{0}_{1}_{2}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // vassal test cases
 {"vassal", 5, "player1", "fiefdom1", Rank.DUKE},
 {"vassal", 10, "player1", "fiefdom1", Rank.KING},
 {"vassal", 5, "player1", "fiefdom1", Rank.COUNT},
 {"vassal", 5, "player1", "fiefdom2", Rank.KING},
 {"vassal", 5, "player1", "fiefdom2", Rank.COUNT},
 {"vassal", 5, "player1", "fiefdom3", null},
 // castleFiefdom test cases
 {"castleFiefdom", 3, "player1", "fiefdom1", Rank.KING},
 {"castleFiefdom", 2, "player1", "fiefdom1", Rank.COUNT},
 {"castleFiefdom", 1, "player1", "fiefdom2", Rank.KING},
 {"castleFiefdom", 1, "player1", "fiefdom2", Rank.COUNT},
 {"castleFiefdom", 1, "player1", "fiefdom3", null},
 // castlePersonal test cases
 {"castlePersonal", 2, "player1", "fiefdom1", Rank.KING},
 {"castlePersonal", 1, "player1", "fiefdom1", Rank.COUNT},
 {"castlePersonal", 1, "player1", "fiefdom2", Rank.KING},
 {"castlePersonal", 1, "player1", "fiefdom2", Rank.COUNT},
 {"castlePersonal", 1, "player1", "fiefdom3", null},
 // mercenary test cases
 {"mercenary", 3, "player1", "knight1", "player2"},
 {"mercenary", 1, "player1", "knight1", "player1"},
 {"mercenary", 2, "player1", "knight2", "player1"},
 {"mercenary", 1, "player1", "knight2", null},
 {"mercenary", 1, "player1", null, "knight1"},
 {"mercenary", 1, null, "knight1", "player1"}
 });
 }

 @Parameterized.Parameter
 public String methodName;

 @Parameterized.Parameter(value = 0)
 public int qty;

 @Parameterized.Parameter(value = 1)
 public String buyerName;

 @Parameterized.Parameter(value = 2)
 public String fiefdomName;

 @Parameterized.Parameter(value = 3)
 public Rank expectedRank;

 @Before
 public void setup() {
 Map map = Mockito.mock(Map.class);
 Fiefdoms fiefdom = Mockito.mock(Fiefdoms.class);
 Player player1 = new Player("player1", Rank.DUKE);
 Player player2 = new Player("player2", Rank.COUNT);
 Knight knight1 = new Knight("knight1", Rank.KNIGHT);
 Knight buyerObj = new Knight(buyerName, Rank.DUKE);
 CurrentPlayers.currentPlayers.put(player1.getName(), player1);
 CurrentPlayers.currentPlayers.put(player2.getName(), player2);
 CurrentPlayers.currentPlayers.put(buyerName, buyerObj);
 CurrentPlayers.currentPlayers.put(knight1.getName(), knight1);
 Map.map = map;
 Map.map.put(fiefdomName, fiefdom);
 fiefdom.setOwner(map.get(fiefdomName));
 purchase = new Purchase();
 args = new ArrayList<>();
 args.add(Arrays.asList(qty, new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 switch (methodName) {
 case "vassal":
 return buyerName;
 case "castleFiefdom":
 return fiefdomName;
 case "castlePersonal":
 return fiefdomName;
 case "mercenary":
 return buyerName;
 case "mercenary":
 return knight1.getName();
 default:
 return null;
 }
 }

 @Override
 public Iterator<Object> iterator() {
 return this;
 }
 }));
 }

 @Test
 public void test() {
 try {
 purchase.perform(args);
 } catch (Exception e) {
 e.printStackTrace();
 }
 // Add your assertions here
 if (expectedRank != null) {
 assertNotNull(CurrentPlayers.currentPlayers.get(buyerName));
 assertEquals(expectedRank, CurrentPlayers.currentPlayers.get(buyerName).getRank());
 }
 }

}