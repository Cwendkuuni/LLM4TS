package src.test.java;

import static org.junit.Assert.*;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;
import src.Purchase;
import src.CurrentPlayers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms fiefdom;
 private Map map;
 private CurrentPlayers currentPlayers;
 private GameAutoActions gameAutoActions;
 private Knight buyer, buyerObj, knight;

 @Before
 public void setup() {
 buyer = new Knight("John", "knight");
 buyerObj = new Knight("Richard", "king");
 knight = new Knight("Tom", "knight");
 currentPlayers = new CurrentPlayers();
 map = new Map();
 fiefdom = new Fiefdoms();
 gameAutoActions = new GameAutoActions();
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;
 Fiefdoms.fiefdoms = fiefdom;
 purchase = new Purchase();
 currentPlayers.addPlayer(buyer);
 currentPlayers.addPlayer(buyerObj);
 map.put("fiefdom1", fiefdom);
 }

 @After
 public void tearDown() {
 currentPlayers.clearPlayers();
 map.clearFiefdoms();
 }

 @Test
 public void testPerform() {
 List<Iterator<Object>> args = new ArrayList<>();
 args.add(Arrays.asList("feudalism.Purchase", Collections.singletonList(purchase.getClass().getDeclaredMethod("vassal", Iterator.class).getGenericParameterTypes()[0])).iterator());
 args.add(Collections.singletonList(args.iterator()).iterator());
 purchase.perform(args);
 }

 @Test(expected = SecurityException.class)
 public void testPerformWithSecurityException() {
 List<Iterator<Object>> args = new ArrayList<>();
 args.add(Collections.singletonList(new Exception("Security Exception").getClass()).iterator());
 args.add(Collections.singletonList(args.iterator()).iterator());
 purchase.perform(args);
 }

 @Test(expected = NoSuchMethodException.class)
 public void testPerformWithNoSuchMethodException() {
 List<Iterator<Object>> args = new ArrayList<>();
 args.add(Arrays.asList("feudalism.NonExistentMethod", Collections.singletonList(String.class)).iterator());
 args.add(Collections.singletonList(args.iterator()).iterator());
 purchase.perform(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithIllegalArgumentException() {
 List<Iterator<Object>> args = new ArrayList<>();
 args.add(Collections.singletonList(Arrays.asList(null, Iterator.class)).iterator());
 args.add(Collections.singletonList(args.iterator()).iterator());
 purchase.perform(args);
 }

 @Test(expected = IllegalAccessException.class)
 public void testPerformWithIllegalAccessException() {
 List<Iterator<Object>> args = new ArrayList<>();
 args.add(Arrays.asList("feudalism.Purchase", Collections.singletonList(Purchase.class)).iterator());
 args.add(Collections.singletonList(args.iterator()).iterator());
 try {
 purchase.perform(args);
 } catch (InvocationTargetException e) {
 throw e.getCause();
 }
 }

 @Test(expected = InvocationTargetException.class)
 public void testPerformWithInvocationTargetException() {
 List<Iterator<Object>> args = new ArrayList<>();
 args.add(Arrays.asList("feudalism.Purchase", Collections.singletonList(Purchase.class)).iterator());
 args.add(Collections.singletonList(new Exception("Invocation Target Exception").getClass()).iterator());
 purchase.perform(args);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testPerformWithClassNotFoundException() {
 List<Iterator<Object>> args = new ArrayList<>();
 args.add(Collections.singletonList(new ClassNotFoundException("Class not found").getClass()).iterator());
 args.add(Collections.singletonList(args.iterator()).iterator());
 purchase.perform(args);
 }

 @Test
 public void testVassal() {
 List<Object> args = Arrays.asList("1", "John", "fiefdom1");
 Iterator<Object> argIterator = Arrays.asList(args).iterator();
 purchase.vassal(argIterator);
 assertEquals(1, buyer.getVassals().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVassalWithNoFiefdom() {
 List<Object> args = Arrays.asList("1", "John", "nonexistentFiefdom");
 Iterator<Object> argIterator = Arrays.asList(args).iterator();
 purchase.vassal(argIterator);
 assertEquals(0, buyer.getVassals().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVassalWithNoMoney() {
 List<Object> args = Arrays.asList("2", "John", "fiefdom1");
 Iterator<Object> argIterator = Arrays.asList(args).iterator();
 buyer.setTotalMoney(0);
 purchase.vassal(argIterator);
 assertEquals(0, buyer.getVassals().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVassalWithSameName() {
 List<Object> args = Arrays.asList("1", "John", "fiefdom1");
 Iterator<Object> argIterator = Arrays.asList(args).iterator();
 buyerObj.setTotalMoney(100);
 purchase.vassal(argIterator);
 assertEquals(0, buyer.getVassals().size());
 }

 @Test
 public void testCastleFiefdom() {
 List<Object> args = Arrays.asList("2", "John", "fiefdom1");
 Iterator<Object> argIterator = Arrays.asList(args).iterator();
 purchase.castleFiefdom(argIterator);
 assertTrue(fiefdom.getCastleCount() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCastleFiefdomWithNoFiefdom() {
 List<Object> args = Arrays.asList("2", "John", "nonexistentFiefdom");
 Iterator<Object> argIterator = Arrays.asList(args).iterator();
 purchase.castleFiefdom(argIterator);
 assertTrue(fiefdom.getCastleCount() == 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCastleFiefdomWithNoMoney() {
 List<Object> args = Arrays.asList("2", "John", "fiefdom1");
 Iterator<Object> argIterator = Arrays.asList(args).iterator();
 buyer.setTotalMoney(0);
 purchase.castleFiefdom(argIterator);
 assertTrue(fiefdom.getCastleCount() == 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCastleFiefdomWithFiefdomBelongsToAnotherPlayer() {
 List<Object> args = Arrays.asList("2", "John", "fiefdom2");
 Iterator<Object> argIterator = Arrays.asList(args).iterator();
 purchase.castleFiefdom(argIterator);
 assertTrue(fiefdom.getCastleCount() == 0);
 }

 @Test
 public void testCastlePersonal() {
 List<Object> args = Arrays.asList("2", "John", "fiefdom1");
 Iterator<Object> argIterator = Arrays.asList(args).iterator();
 purchase.castlePersonal(argIterator);
 assertTrue(fiefdom.getPersonalCastles().size() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCastlePersonalWithNoFiefdom() {
 List<Object> args = Arrays.asList("2", "John", "nonexistentFiefdom");
 Iterator<Object> argIterator = Arrays.asList(args).iterator();
 purchase.castlePersonal(argIterator);
 assertTrue(fiefdom.getPersonalCastles().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCastlePersonalWithNoMoney() {
 List<Object> args = Arrays.asList("2", "John", "fiefdom1");
 Iterator<Object> argIterator = Arrays.asList(args).iterator();
 buyer.setTotalMoney(0);
 purchase.castlePersonal(argIterator);
 assertTrue(fiefdom.getPersonalCastles().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCastlePersonalWithFiefdomBelongsToAnotherPlayer() {
 List<Object> args = Arrays.asList("2", "John", "fiefdom2");
 Iterator<Object> argIterator = Arrays.asList(args).iterator();
 purchase.castlePersonal(argIterator);
 assertTrue(fiefdom.getPersonalCastles().isEmpty());
 }

 @Test
 public void testMercenary() {
 List<Object> args = Arrays.asList("1", "John", "tom");
 Iterator<Object> argIterator = Arrays.asList(args).iterator();
 purchase.mercenary(argIterator);
 assertTrue(knight.getMercenaries().size() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMercenaryWithNoMoney() {
 List<Object> args = Arrays.asList("2", "John", "Tom");
 Iterator<Object> argIterator = Arrays.asList(args).iterator();
 buyer.setTotalMoney(0);
 purchase.mercenary(argIterator);
 assertTrue(knight.getMercenaries().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMercenaryWithSamePlayer() {
 List<Object> args = Arrays.asList("1", "John", "John");
 Iterator<Object> argIterator = Arrays.asList(args).iterator();
 purchase.mercenary(argIterator);
 assertTrue(knight.getMercenaries().isEmpty());
 }

 @Test
 public void testMercenaryWithDifferentPlayer() {
 List<Object> args = Arrays.asList("1", "John", "Tom");
 Iterator<Object> argIterator = Arrays.asList(args).iterator();
 buyerObj.setTotalMoney(100);
 purchase.mercenary(argIterator);
 assertTrue(knight.getMercenaries().size() > 0);
 }
}