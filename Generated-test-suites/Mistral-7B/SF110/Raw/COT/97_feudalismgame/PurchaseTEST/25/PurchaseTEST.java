package src.test.java;

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
 private Fiefdoms fiefdom;
 private Knight buyer, knight;
 private CurrentPlayers currentPlayers;
 private Map map;
 private GameAutoActions gameAutoActions;

 @Before
 public void setUp() {
 // Initialize objects
 purchase = new Purchase();
 fiefdom = new Fiefdoms();
 buyer = new Knight("John Doe", "knight");
 knight = new Knight("Jane Doe", "knight");
 currentPlayers = new CurrentPlayers();
 map = new Map();
 gameAutoActions = new GameAutoActions();

 // Set up initial data
 currentPlayers.setCurrentPlayers(Collections.singletonMap("John Doe", buyer));
 currentPlayers.setCurrentPlayers(Collections.singletonMap("Jane Doe", knight));
 map.setMap(Collections.singletonMap("fiefdom1", fiefdom));
 }

 @Test
 public void testPerform() {
 // Test empty arguments
 Collection<Object> emptyArgs = new ArrayList<>();
 purchase.perform(emptyArgs);
 // Test invalid method signature
 Collection<Object> invalidArgs = Arrays.asList("invalidMethod", Arrays.asList("arg1", new Iterator<Object>() {}).iterator());
 purchase.perform(invalidArgs);

 // Test vassal method with valid arguments
 Collection<Object> vassalArgs = Arrays.asList("3", "John Doe", "fiefdom1");
 purchase.perform(vassalArgs);
 verify(fiefdom).setLoyalVassals(3, true);

 // Test vassal method with insufficient funds
 Collection<Object> vassalInsufficientFundsArgs = Arrays.asList("4", "John Doe", "fiefdom1");
 purchase.perform(vassalInsufficientFundsArgs);
 assertEquals("You cannot buy more vassals, you have insufficient funds", System.out.getMessage());

 // Test castleFiefdom method with valid arguments
 Collection<Object> castleFiefdomArgs = Arrays.asList("2", "John Doe", "fiefdom1");
 purchase.perform(castleFiefdomArgs);
 verify(fiefdom).buyFiefdomCastles(2);

 // Test castleFiefdom method with insufficient funds
 Collection<Object> castleFiefdomInsufficientFundsArgs = Arrays.asList("3", "John Doe", "fiefdom1");
 purchase.perform(castleFiefdomInsufficientFundsArgs);
 assertEquals("You cannot buy more Fiefdom castles, you have insufficient funds", System.out.getMessage());

 // Test castlePersonal method with valid arguments
 Collection<Object> castlePersonalArgs = Arrays.asList("2", "John Doe", "fiefdom1");
 purchase.perform(castlePersonalArgs);
 verify(fiefdom).buyPersonalCastles(2);

 // Test castlePersonal method with insufficient funds
 Collection<Object> castlePersonalInsufficientFundsArgs = Arrays.asList("3", "John Doe", "fiefdom1");
 purchase.perform(castlePersonalInsufficientFundsArgs);
 assertEquals("You cannot buy more Personal castle, you have insufficient funds", System.out.getMessage());

 // Test mercenary method with valid arguments
 Collection<Object> mercenaryArgs = Arrays.asList("2", "John Doe", "knight1");
 purchase.perform(mercenaryArgs);
 verify(knight).addMercenaries(2);

 // Test mercenary method with insufficient funds
 Collection<Object> mercenaryInsufficientFundsArgs = Arrays.asList("3", "John Doe", "knight1");
 purchase.perform(mercenaryInsufficientFundsArgs);
 assertEquals("You cannot buy more mercenaries, you have insufficient funds", System.out.getMessage());

 // Test mercenary method with buying the same knight
 Collection<Object> mercenarySelfArgs = Arrays.asList("2", "John Doe", "John Doe");
 purchase.perform(mercenarySelfArgs);
 assertEquals("You cannot buy mercenaries for yourself", System.out.getMessage());

 // Test exception handling
 Collection<Object> nullIteratorArgs = Arrays.asList(null, "arg1", "fiefdom1");
 purchase.perform(nullIteratorArgs);
 // Add more test cases for SecurityException, NoSuchMethodException, IllegalArgumentException,
 // IllegalAccessException, InvocationTargetException, and ClassNotFoundException
 }

 @After
 public void tearDown() {
 // Reset initial data
 currentPlayers.setCurrentPlayers(Collections.emptyMap());
 map.setMap(Collections.emptyMap());
 }
}