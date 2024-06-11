package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import src.Actions;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;

public class PurchaseTest {

 private Purchase purchase;
 private Actions actions;
 private GameAutoActions gameAutoActions;
 private Map map;
 private List<Fiefdoms> fiefdoms;
 private List<Knight> knights;
 private Map<String, Knight> currentPlayers;

 @Before
 public void setup() {
 purchase = new Purchase();
 actions = mock(Actions.class);
 gameAutoActions = mock(GameAutoActions.class);
 map = mock(Map.class);
 fiefdoms = new ArrayList<>();
 knights = new ArrayList<>();
 currentPlayers = new HashMap<>();

 when(map.getMap()).thenReturn(fiefdoms);
 when(actions.perform(Collections.emptyList())).thenReturn(actions);
 when(gameAutoActions.saveAll()).thenReturn(gameAutoActions);
 }

 @Test
 public void testVassal() {
 // Arrange
 Fiefdoms fiefdom = new Fiefdoms("France", null);
 Knight buyer = new Knight("John", "knight", 1000, 5);
 String fiefdomName = "France";
 String buyerName = "John";

 // Act
 purchase.vassal(getIteratorWithArguments(new Object[] { 2, buyerName, fiefdomName }));

 // Assert
 assertEquals(2, fiefdom.getLoyalVassals());
 assertEquals(880, buyer.getTotalMoney());
 }

 @Test
 public void testVassal_InsufficientFunds() {
 // Arrange
 Fiefdoms fiefdom = new Fiefdoms("France", null);
 Knight buyer = new Knight("John", "knight", 1000, 5);
 String fiefdomName = "France";
 String buyerName = "John";

 // Act
 purchase.vassal(getIteratorWithArguments(new Object[] { 3, buyerName, fiefdomName }));

 // Assert
 assertEquals(2, fiefdom.getLoyalVassals());
 assertEquals(1000, buyer.getTotalMoney());
 }

 @Test
 public void testVassal_NotOwnedFiefdom() {
 // Arrange
 Fiefdoms fiefdom = new Fiefdoms("France", new Knight("Alice", "king", 1000, 5));
 Knight buyer = new Knight("John", "knight", 1000, 5);
 String fiefdomName = "Germany";
 String buyerName = "John";

 // Act
 purchase.vassal(getIteratorWithArguments(new Object[] { 2, buyerName, fiefdomName }));

 // Assert
 assertEquals(0, fiefdom.getLoyalVassals());
 assertEquals(1000, buyer.getTotalMoney());
 }

 @Test
 public void testVassal_NotOwnedByPlayer() {
 // Arrange
 Fiefdoms fiefdom = new Fiefdoms("France", new Knight("Alice", "king", 1000, 5));
 Knight buyer = new Knight("Bob", "knight", 1000, 5);
 String fiefdomName = "France";
 String buyerName = "Bob";

 // Act
 purchase.vassal(getIteratorWithArguments(new Object[] { 2, buyerName, fiefdomName }));

 // Assert
 assertEquals(0, fiefdom.getLoyalVassals());
 assertEquals(1000, buyer.getTotalMoney());
 }

 @Test
 public void testVassal_CastlePresent() {
 // Arrange
 Fiefdoms fiefdom = new Fiefdoms("France", new Knight("Alice", "king", 1000, 5));
 Knight buyer = new Knight("John", "knight", 1000, 5);
 String fiefdomName = "France";
 String buyerName = "John";

 // Act
 purchase.vassal(getIteratorWithArguments(new Object[] { 2, buyerName, fiefdomName }));
 fiefdom.buyFiefdomCastles(1);

 // Assert
 assertEquals(1, fiefdom.getCastles().size());
 assertEquals(880, buyer.getTotalMoney());
 }

 // Add more test cases for other methods in the Purchase class
 // ...

 private Iterator<Object> getIteratorWithArguments(Object[] args) {
 Iterator<Object> iterator = Collections.emptyIterator();
 for (Object arg : args) {
 iterator = Collections.singletonList(arg).iterator();
 break;
 }
return iterator;
}
}