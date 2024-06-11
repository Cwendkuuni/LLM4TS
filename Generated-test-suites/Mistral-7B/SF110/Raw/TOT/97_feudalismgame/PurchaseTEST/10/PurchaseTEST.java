import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Actions;
import src.CurrentPlayers;
import src.Knight;
import src.Purchase;
import src.Map;

import java.util.*;
import static org.junit.Assert.*;

public class PurchaseTest {

 private Purchase purchase;
 private CurrentPlayers currentPlayers;
 private Fiefdoms fiefdom;
 private List<String> args;

 @Before
 public void setup() {
 CurrentPlayers.currentPlayers = new HashMap<>();
 purchase = new Purchase();
 currentPlayers = Mockito.spy(new CurrentPlayers());
 fiefdom = new Fiefdoms();
 args = new ArrayList<>();
 }

 @Test
 public void testPerform_ValidArgs_ShouldInvokeCorrectMethod() throws Exception {
 // Arrange
 String methodName = "vassal";
 Iterator<String> iterator = args.iterator();
 iterator.next(); // vassal
 iterator.next(); // 5
 iterator.next(); // "Alice"
 iterator.next(); // "Alphaland"
 Fiefdoms expectedFiefdom = fiefdom;
 Knight expectedBuyer = new Knight("Alice", "Alice", 500, "knight");
 expectedFiefdom.setOwner(expectedBuyer);

 // Act
 purchase.perform(args);

 // Assert
 assertEquals(expectedFiefdom, fiefdom);
 Mockito.verify(currentPlayers).get(Mockito.eq("Alice"));
 Mockito.verify(currentPlayers, Mockito.times(1)).get(Mockito.eq("Alphaland")).get(Mockito.eq("owner"));
 Mockito.verify(expectedBuyer).setTotalMoney(5, false);
 Mockito.verify(expectedFiefdom).setLoyalVassals(5, true);
 }

 @Test
 public void testPerform_InvalidMethodName_ShouldThrowNoSuchMethodException() throws Exception {
 // Arrange
 String methodName = "invalidMethod";
 args.clear();
 args.add(methodName);
 args.add(Mockito.mock(Iterator.class));

 // Act and Assert
 assertThrows(NoSuchMethodException.class, () -> purchase.perform(args));
 }

 @Test
 public void testPerform_NullOrEmptyArgs_ShouldThrowIllegalArgumentException() {
 // Arrange
 args.clear();

 // Act and Assert
 assertThrows(IllegalArgumentException.class, () -> purchase.perform(args));
 }

 @Test
 public void testVassal_BuyVassalForOwnFiefdom_ShouldDecreaseBuyerMoneyAndIncreaseFiefdomVassals() {
 // Arrange
 String fiefdomName = "Alphaland";
 String buyerName = "Alice";
 int qty = 5;
 Fiefdoms expectedFiefdom = fiefdom;
 Knight expectedBuyer = new Knight("Alice", "Alice", 500, "knight");
 expectedFiefdom.setOwner(expectedBuyer);
 expectedFiefdom.setLoyalVassals(0, false);
 args.clear();
 args.add("vassal");
 args.add(argsToString(iterator -> iterator.next(), 5));
 args.add(buyerName);
 args.add(fiefdomName);

 // Act
 purchase.perform(args);

 // Assert
 assertEquals(expectedFiefdom, fiefdom);
 assertEquals(5, expectedFiefdom.getLoyalVassals());
 assertEquals(495, expectedBuyer.getTotalMoney());
 }

 @Test
 public void testVassal_BuyVassalForAnotherFiefdom_ShouldNotDecreaseBuyerMoneyOrIncreaseFiefdomVassals() {
 // Arrange
 String buyerName = "Alice";
 String fiefdomName = "BetaLand";
 int qty = 5;
 Fiefdoms expectedFiefdom = fiefdom;
 Knight expectedBuyer = new Knight("Alice", "Alice", 500, "knight");
 expectedFiefdom.setOwner(expectedBuyer);
 expectedFiefdom.setLoyalVassals(0, false);
 args.clear();
 args.add("vassal");
 args.add(argsToString(iterator -> iterator.next(), 5));
 args.add(buyerName);
 args.add(fiefdomName);

 // Act
 purchase.perform(args);

 // Assert
 assertEquals(expectedFiefdom, fiefdom);
 assertEquals(0, expectedFiefdom.getLoyalVassals());
 assertEquals(500, expectedBuyer.getTotalMoney());
 }

 @Test
 public void testCastleFiefdom_BuyCastleForOwnFiefdom_ShouldDecreaseBuyerMoneyAndIncreaseFiefdomCastles() {
 // Arrange
 String fiefdomName = "Alphaland";
 String buyerName = "Alice";
 int qty = 5;
 Fiefdoms expectedFiefdom = fiefdom;
 Knight expectedBuyer = new Knight("Alice", "Alice", 2500, "knight");
 expectedFiefdom.setOwner(expectedBuyer);
 expectedFiefdom.setBuyFiefdomCastles(0, false);
 args.clear();
 args.add("castleFiefdom");
 args.add(argsToString(iterator -> iterator.next(), 5));
 args.add(buyerName);
 args.add(fiefdomName);

 // Act
 purchase.perform(args);

 // Assert
 assertEquals(expectedFiefdom, fiefdom);
 assertEquals(5, expectedFiefdom.getBuyFiefdomCastles());
 assertEquals(2450, expectedBuyer.getTotalMoney());
 }

 @Test
 public void testCastleFiefdom_BuyCastleForAnotherFiefdom_ShouldNotDecreaseBuyerMoneyOrIncreaseFiefdomCastles() {
 // Arrange
 String buyerName = "Alice";
 String fiefdomName = "BetaLand";
 int qty = 5;
 Fiefdoms expectedFiefdom = fiefdom;
 Knight expectedBuyer = new Knight("Alice", "Alice", 2500, "knight");
 expectedFiefdom.setOwner(expectedBuyer);
 expectedFiefdom.setBuyFiefdomCastles(0, false);
 args.clear();
 args.add("castleFiefdom");
 args.add(argsToString(iterator -> iterator.next(), 5));
 args.add(buyerName);
 args.add(fiefdomName);

 // Act
 purchase.perform(args);

 // Assert
 assertEquals(expectedFiefdom, fiefdom);
 assertEquals(0, expectedFiefdom.getBuyFiefdomCastles());
 assertEquals(2500, expectedBuyer.getTotalMoney());
 }

 @Test
 public void testCastlePersonal_BuyCastleForOwnFiefdom_ShouldDecreaseBuyerMoneyAndIncreaseFiefdomPersonalCastles() {
 // Arrange
 String fiefdomName = "Alphaland";
 String buyerName = "Alice";
 int qty = 5;
 Fiefdoms expectedFiefdom = fiefdom;
 Knight expectedBuyer = new Knight("Alice", "Alice", 1000, "knight");
 expectedFiefdom.setOwner(expectedBuyer);
 expectedFiefdom.setBuyPersonalCastles(0, false);
 args.clear();
 args.add("castlePersonal");
 args.add(argsToString(iterator -> iterator.next(), 5));
 args.add(buyerName);
 args.add(fiefdomName);

 // Act
 purchase.perform(args);

 // Assert
 assertEquals(expectedFiefdom, fiefdom);
 assertEquals(5, expectedFiefdom.getBuyPersonalCastles());
 assertEquals(950, expectedBuyer.getTotalMoney());
 }

 @Test
 public void testCastlePersonal_BuyCastleForAnotherFiefdom_ShouldNotDecreaseBuyerMoneyOrIncreaseFiefdomPersonalCastles() {
 // Arrange
 String buyerName = "Alice";
 String fiefdomName = "BetaLand";
 int qty = 5;
 Fiefdoms expectedFiefdom = fiefdom;
 Knight expectedBuyer = new Knight("Alice", "Alice", 1000, "knight");
 expectedFiefdom.setOwner(expectedBuyer);
 expectedFiefdom.setBuyPersonalCastles(0, false);
 args.clear();
 args.add("castlePersonal");
 args.add(argsToString(iterator -> iterator.next(), 5));
 args.add(buyerName);
 args.add(fiefdomName);

 // Act
 purchase.perform(args);

 // Assert
 assertEquals(expectedFiefdom, fiefdom);
 assertEquals(0, expectedFiefdom.getBuyPersonalCastles());
 assertEquals(1000, expectedBuyer.getTotalMoney());
 }

 @Test
 public void testMercenary_BuyMercenariesForSameKnight_ShouldDecreaseBuyerMoneyAndIncreaseKnightMercenaries() {
 // Arrange
 String knightName = "Alice";
 String buyerName = "Alice";
 int qty = 5;
 Knight expectedKnight = new Knight(knightName, buyerName, 500, "knight");
 expectedKnight.setMercenaries(0);
 args.clear();
 args.add("mercenary");
 args.add(argsToString(iterator -> iterator.next(), 5));
 args.add(knightName);
 args.add(buyerName);

 // Act
 purchase.perform(args);

 // Assert
 assertEquals(expectedKnight, CurrentPlayers.currentPlayers.get(knightName));
 assertEquals(5, expectedKnight.getMercenaries());
 assertEquals(495, expectedKnight.getTotalMoney());
 }

 @Test
 public void testMercenary_BuyMercenariesForDifferentKnight_ShouldNotDecreaseBuyerMoneyOrIncreaseKnightMercenaries() {
 // Arrange
 String knightName = "Alice";
 String buyerName = "Bob";
 int qty = 5;
 Knight expectedKnight = new Knight(knightName, buyerName, 500, "knight");
 args.clear();
 args.add("mercenary");
 args.add(argsToString(iterator -> iterator.next(), 5));
 args.add(knightName);
 args.add(buyerName);

 // Act
 purchase.perform(args);

 // Assert
 assertEquals(expectedKnight, CurrentPlayers.currentPlayers.get(knightName));
 assertEquals(0, expectedKnight.getMercenaries());
 assertEquals(500, expectedKnight.getTotalMoney());
 }

 private String argsToString(Function<Iterator<String>, String> func, int qty) {
 StringBuilder stringBuilder = new StringBuilder();
 for (int i = 0; i < qty; i++) {
 stringBuilder.append(func.apply(args.iterator()));
 stringBuilder.append(" ");
 }
 return stringBuilder.toString();
 }
}