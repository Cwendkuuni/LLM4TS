import org.junit.Test;
import static org.junit.Assert.*;
import src.Purchase;
import src.Fiefdoms;
import src.CurrentPlayers;
import src.Knight;
import src.Actions;
import java.util.*;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms map;
 private CurrentPlayers currentPlayers;

 @Before
 public void setup() {
 purchase = new Purchase();
 map = new Fiefdoms();
 currentPlayers = new CurrentPlayers();
 }

 @Test
 public void testPerform() {
 // Basic functionality test
 Collection args = new ArrayList();
 args.add("feudalism.Purchase");
 args.add(purchase.argsIterator());
 purchase.perform(args);
 // Test for NoSuchMethodException
 Collection wrongMethodArgs = new ArrayList();
 wrongMethodArgs.add("InvalidMethodName");
 wrongMethodArgs.add(purchase.argsIterator());
 expectThrows(NoSuchMethodException.class, () -> purchase.perform(wrongMethodArgs.iterator()));
 // Test for IllegalArgumentException
 Collection nullArg = new ArrayList();
 nullArg.add(null);
 nullArg.add(purchase.argsIterator());
 expectThrows(IllegalArgumentException.class, () -> purchase.perform(nullArg.iterator()));
 // Test for InvocationTargetException
 Collection invalidFiefdomArgs = new ArrayList();
 invalidFiefdomArgs.add("InvalidFiefdomName");
 invalidFiefdomArgs.add(purchase.argsIterator());
 expectThrows(InvocationTargetException.class, () -> purchase.perform(invalidFiefdomArgs.iterator()));
 }

 @Test
 public void testVassal() {
 // Basic functionality test
 Collection vassalArgs = new ArrayList();
 vassalArgs.add(map.iterator());
 vassalArgs.add(1);
 vassalArgs.add("Player1");
 vassalArgs.add("Fiefdom1");
 purchase.vassal(vassalArgs.iterator());
 // Test for InsufficientFundsException
 Collection insufficientFundsArgs = new ArrayList();
 insufficientFundsArgs.add(map.iterator());
 insufficientFundsArgs.add(2);
 insufficientFundsArgs.add("Player1");
 insufficientFundsArgs.add("Fiefdom1");
 expectThrows(RuntimeException.class, () -> purchase.vassal(insufficientFundsArgs.iterator()));
 // Test for FiefdomNotOwnedException
 Collection fiefdomNotOwnedArgs = new ArrayList();
 fiefdomNotOwnedArgs.add(map.iterator());
 fiefdomNotOwnedArgs.add(1);
 fiefdomNotOwnedArgs.add("Player1");
 fiefdomNotOwnedArgs.add("InvalidFiefdomName");
 expectThrows(RuntimeException.class, () -> purchase.vassal(fiefdomNotOwnedArgs.iterator()));
 // Test for FiefdomAlreadyOwnedException
 Collection fiefdomAlreadyOwnedArgs = new ArrayList();
 fiefdomAlreadyOwnedArgs.add(map.iterator());
 fiefdomAlreadyOwnedArgs.add(2);
 fiefdomAlreadyOwnedArgs.add("Player1");
 fiefdomAlreadyOwnedArgs.add("Fiefdom1");
 expectThrows(RuntimeException.class, () -> purchase.vassal(fiefdomAlreadyOwnedArgs.iterator()));
 }

 @Test
 public void testCastleFiefdom() {
 // Basic functionality test
 Collection castleFiefdomArgs = new ArrayList();
 castleFiefdomArgs.add(map.iterator());
 castleFiefdomArgs.add(1);
 castleFiefdomArgs.add("Player1");
 castleFiefdomArgs.add("Fiefdom1");
 purchase.castleFiefdom(castleFiefdomArgs.iterator());
 // Test for InsufficientFundsException
 Collection insufficientFundsCastleArgs = new ArrayList();
 insufficientFundsCastleArgs.add(map.iterator());
 insufficientFundsCastleArgs.add(2);
 insufficientFundsCastleArgs.add("Player1");
 insufficientFundsCastleArgs.add("Fiefdom1");
 expectThrows(RuntimeException.class, () -> purchase.castleFiefdom(insufficientFundsCastleArgs.iterator()));
 // Test for FiefdomNotOwnedException
 Collection fiefdomNotOwnedCastleArgs = new ArrayList();
 fiefdomNotOwnedCastleArgs.add(map.iterator());
 fiefdomNotOwnedCastleArgs.add(2);
 fiefdomNotOwnedCastleArgs.add("Player1");
 fiefdomNotOwnedCastleArgs.add("InvalidFiefdomName");
 expectThrows(RuntimeException.class, () -> purchase.castleFiefdom(fiefdomNotOwnedCastleArgs.iterator()));
 // Test for CastleAlreadyOwnedException
 Collection castleAlreadyOwnedArgs = new ArrayList();
 castleAlreadyOwnedArgs.add(map.iterator());
 castleAlreadyOwnedArgs.add(2);
 castleAlreadyOwnedArgs.add("Player1");
 castleAlreadyOwnedArgs.add("Fiefdom1");
 expectThrows(RuntimeException.class, () -> purchase.castleFiefdom(castleAlreadyOwnedArgs.iterator()));
 }

 @Test
 public void testCastlePersonal() {
 // Basic functionality test
 Collection castlePersonalArgs = new ArrayList();
 castlePersonalArgs.add(map.iterator());
 castlePersonalArgs.add(1);
 castlePersonalArgs.add("Player1");
 castlePersonalArgs.add("Fiefdom1");
 purchase.castlePersonal(castlePersonalArgs.iterator());
 // Test for InsufficientFundsException
 Collection insufficientFundsCastlePersonalArgs = new ArrayList();
 insufficientFundsCastlePersonalArgs.add(map.iterator());
 insufficientFundsCastlePersonalArgs.add(2);
 insufficientFundsCastlePersonalArgs.add("Player1");
 insufficientFundsCastlePersonalArgs.add("Fiefdom1");
 expectThrows(RuntimeException.class, () -> purchase.castlePersonal(insufficientFundsCastlePersonalArgs.iterator()));
 // Test for FiefdomNotOwnedException
 Collection fiefdomNotOwnedCastlePersonalArgs = new ArrayList();
 fiefdomNotOwnedCastlePersonalArgs.add(map.iterator());
 fiefdomNotOwnedCastlePersonalArgs.add(2);
 fiefdomNotOwnedCastlePersonalArgs.add("Player1");
 fiefdomNotOwnedCastlePersonalArgs.add("InvalidFiefdomName");
 expectThrows(RuntimeException.class, () -> purchase.castlePersonal(fiefdomNotOwnedCastlePersonalArgs.iterator()));
 // Test for RankMismatchException
 Collection rankMismatchArgs = new ArrayList();
 rankMismatchArgs.add(map.iterator());
 rankMismatchArgs.add(2);
 rankMismatchArgs.add("Player1");
 rankMismatchArgs.add("Fiefdom1");
 knight = new Knight("Knight1", "Count");
 currentPlayers.addPlayer(knight);
 expectThrows(RuntimeException.class, () -> purchase.castlePersonal(rankMismatchArgs.iterator()));
 }

 @Test
 public void testMercenary() {
 // Basic functionality test
 Collection mercenaryArgs = new ArrayList();
 mercenaryArgs.add(currentPlayers.iterator());
 mercenaryArgs.add(1);
 mercenaryArgs.add("Player1");
 mercenaryArgs.add("Knight1");
 purchase.mercenary(mercenaryArgs.iterator());
 // Test for InvalidPlayerException
 Collection invalidPlayerArgs = new ArrayList();
 invalidPlayerArgs.add(currentPlayers.iterator());
 invalidPlayerArgs.add(2);
 invalidPlayerArgs.add("InvalidPlayerName");
 invalidPlayerArgs.add("Knight1");
 expectThrows(RuntimeException.class, () -> purchase.mercenary(invalidPlayerArgs.iterator()));
 // Test for InsufficientFundsException
 Collection insufficientFundsMercenaryArgs = new ArrayList();
 insufficientFundsMercenaryArgs.add(currentPlayers.iterator());
 insufficientFundsMercenaryArgs.add(2);
 insufficientFundsMercenaryArgs.add("Player1");
 insufficientFundsMercenaryArgs.add("Knight1");
 expectThrows(RuntimeException.class, () -> purchase.mercenary(insufficientFundsMercenaryArgs.iterator()));
 }

}