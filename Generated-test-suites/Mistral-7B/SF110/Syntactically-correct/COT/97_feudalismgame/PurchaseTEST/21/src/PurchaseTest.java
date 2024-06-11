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
 purchase = new Purchase();
 currentPlayers = new CurrentPlayers();
 map = new Map();
 gameAutoActions = new GameAutoActions();
 fiefdom = new Fiefdoms();
 buyer = new Knight("John", "knight");
 knight = new Knight("Alice", "knight");
 map.put("fiefdom1", fiefdom);
 currentPlayers.put("John", buyer);
 currentPlayers.put("Alice", knight);
 }

 @Test
 public void testPerform() {
 Iterator<Object> args = Arrays.asList("Purchase", Arrays.asList(1, "John", "fiefdom1")).iterator();
 purchase.perform(args);
 // Add more assertions here if needed
 }

 @Test
 public void testVassal() {
 Iterator<Object> args = Arrays.asList("vassal", Arrays.asList(1, "John", "fiefdom1")).iterator();
 purchase.vassal(args);
 assertFalse(fiefdom.getLoyalVassals() == 0);
 }

 @Test
 public void testVassal_InsufficientFunds() {
 Iterator<Object> args = Arrays.asList("vassal", Arrays.asList(5, "John", "fiefdom1")).iterator();
 purchase.vassal(args);
 assertEquals("You cannot buy more vassals, you have insufficient funds", System.out.getStackTraceElements()[1].getMethodName());
 }

 @Test
 public void testVassal_WrongOwner() {
 Iterator<Object> args = Arrays.asList("vassal", Arrays.asList(1, "Alice", "fiefdom1")).iterator();
 purchase.vassal(args);
 assertEquals("Fiefdom doesn't belong to the kingdom", System.out.getStackTraceElements()[1].getMethodName());
 }

 @Test
 public void testCastleFiefdom() {
 Iterator<Object> args = Arrays.asList("castleFiefdom", Arrays.asList(1, "John", "fiefdom1")).iterator();
 purchase.castleFiefdom(args);
 assertFalse(fiefdom.getCastleCount() == 0);
 }

 @Test
 public void testCastleFiefdom_InsufficientFunds() {
 Iterator<Object> args = Arrays.asList("castleFiefdom", Arrays.asList(2, "John", "fiefdom1")).iterator();
 purchase.castleFiefdom(args);
 assertEquals("You cannot buy more Fiefdom castles, you have insufficient funds", System.out.getStackTraceElements()[1].getMethodName());
 }

 @Test
 public void testCastleFiefdom_WrongOwner() {
 Iterator<Object> args = Arrays.asList("castleFiefdom", Arrays.asList(1, "Alice", "fiefdom1")).iterator();
 purchase.castleFiefdom(args);
 assertEquals("Fiefdom doesn't belong to the kingdom", System.out.getStackTraceElements()[1].getMethodName());
 }

 @Test
 public void testCastlePersonal() {
 Iterator<Object> args = Arrays.asList("castlePersonal", Arrays.asList(1, "John", "fiefdom1")).iterator();
 purchase.castlePersonal(args);
 assertFalse(fiefdom.getCastlePersonalCount() == 0);
 }

 @Test
 public void testCastlePersonal_InsufficientFunds() {
 Iterator<Object> args = Arrays.asList("castlePersonal", Arrays.asList(2, "John", "fiefdom1")).iterator();
 purchase.castlePersonal(args);
 assertEquals("You cannot buy more Personal castle, you have insufficient funds", System.out.getStackTraceElements()[1].getMethodName());
 }

 @Test
 public void testCastlePersonal_WrongOwner() {
 Iterator<Object> args = Arrays.asList("castlePersonal", Arrays.asList(1, "Alice", "fiefdom1")).iterator();
 purchase.castlePersonal(args);
 assertEquals("Fiefdom doesn't belong to the kingdom: fiefdom1 owner: null", System.out.getStackTraceElements()[1].getMethodName());
 }

 @Test
 public void testMercenary() {
 Iterator<Object> args = Arrays.asList("mercenary", Arrays.asList(1, "John", "Alice")).iterator();
 purchase.mercenary(args);
 assertFalse(knight.getMercenaries() == 0);
 }

 @Test
 public void testMercenary_InsufficientFunds() {
 Iterator<Object> args = Arrays.asList("mercenary", Arrays.asList(3, "John", "Alice")).iterator();
 purchase.mercenary(args);
 assertEquals("You cannot buy more mercenaries, you have insufficient funds", System.out.getStackTraceElements()[1].getMethodName());
 }

 @Test
 public void testMercenary_InexistentKnight() {
 Iterator<Object> args = Arrays.asList("mercenary", Arrays.asList(1, "Bob", "Alice")).iterator();
 purchase.mercenary(args);
 assertEquals("Inexistent Knight", System.out.getStackTraceElements()[1].getMethodName());
 }

 @Test
 public void testMercenary_SameName() {
 Iterator<Object> args = Arrays.asList("mercenary", Arrays.asList(1, "John", "John")).iterator();
 purchase.mercenary(args);
 assertEquals("You cannot buy mercenaries for yourself", System.out.getStackTraceElements()[1].getMethodName());
 }

 @Test
 public void testMercenary_KingBuyForKnight() {
 Iterator<Object> args = Arrays.asList("mercenary", Arrays.asList(1, "John", "knight")).iterator();
 purchase.mercenary(args);
 assertEquals("You cannot buy mercenaries directly for knights", System.out.getStackTraceElements()[1].getMethodName());
 }
}