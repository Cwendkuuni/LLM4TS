import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.Actions;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;
import src.Purchase;
import src.CurrentPlayers;
import java.util.*;
import static org.junit.Assert.*;

public class PurchaseTest {

 private Purchase purchase;
 private Actions actions;
 private GameAutoActions gameAutoActions;
 private CurrentPlayers currentPlayers;
 private Map map;
 private Fiefdoms fiefdom;
 private Knight buyer, knight;

 @Before
 public void setUp() {
 actions = Mockito.spy(new Actions());
 gameAutoActions = Mockito.spy(new GameAutoActions());
 currentPlayers = new CurrentPlayers();
 map = new Map();
 fiefdom = new Fiefdoms();
 purchase = new Purchase();
 buyer = new Knight("John", "knight");
 knight = new Knight("Tom", "knight");
 }

 @Test
 public void testVassal() {
 Collection<Object> args = Arrays.asList("1", "John", "fiefdom1");
 Iterator<Object> iterator = args.iterator();
 Mockito.when(map.get(iterator.next())).thenReturn(fiefdom);
 Mockito.when(currentPlayers.get(iterator.next())).thenReturn(buyer);

 purchase.perform(args);

 assertEquals(1, fiefdom.getLoyalVassals());
 assertEquals(1, buyer.getTotalMoney());
 }

 @Test
 public void testVassal_InsufficientFunds() {
 Collection<Object> args = Arrays.asList("2", "John", "fiefdom1");
 Iterator<Object> iterator = args.iterator();
 Mockito.when(map.get(iterator.next())).thenReturn(fiefdom);
 Mockito.when(currentPlayers.get(iterator.next())).thenReturn(buyer);

 purchase.perform(args);

 assertEquals(0, fiefdom.getLoyalVassals());
 assertEquals(1000, buyer.getTotalMoney()); // assuming buyer starts with 1000
 }

 @Test
 public void testVassal_FiefdomDoesntBelongToKingdom() {
 Collection<Object> args = Arrays.asList("1", "John", "fiefdom2");
 Iterator<Object> iterator = args.iterator();
 Mockito.when(map.get(iterator.next())).thenReturn(new Fiefdoms("fiefdom2", null));

 purchase.perform(args);

 assertEquals(0, fiefdom.getLoyalVassals());
 assertEquals(1000, buyer.getTotalMoney());
 }

 @Test
 public void testVassal_BuyForAnotherPlayer() {
 Collection<Object> args = Arrays.asList("1", "Tom", "fiefdom1");
 Iterator<Object> iterator = args.iterator();
 Mockito.when(map.get(iterator.next())).thenReturn(fiefdom);
 Mockito.when(currentPlayers.get(iterator.next())).thenReturn(buyer);

 purchase.perform(args);

 assertEquals(0, fiefdom.getLoyalVassals());
 assertEquals(1000, buyer.getTotalMoney());
 assertEquals(0, knight.getTotalMoney());
 }

 @Test
 public void testCastleFiefdom() {
 Collection<Object> args = Arrays.asList("2", "John", "fiefdom1");
 Iterator<Object> iterator = args.iterator();
 Mockito.when(map.get(iterator.next())).thenReturn(fiefdom);
 Mockito.when(currentPlayers.get(iterator.next())).thenReturn(buyer);

 purchase.perform(args);

 assertEquals(50, fiefdom.getTotalCastles());
 assertEquals(8500, buyer.getTotalMoney()); // assuming buyer starts with 9000
 }

 @Test
 public void testCastleFiefdom_InsufficientFunds() {
 Collection<Object> args = Arrays.asList("3", "John", "fiefdom1");
 Iterator<Object> iterator = args.iterator();
 Mockito.when(map.get(iterator.next())).thenReturn(fiefdom);
 Mockito.when(currentPlayers.get(iterator.next())).thenReturn(buyer);

 purchase.perform(args);

 assertEquals(50, fiefdom.getTotalCastles());
 assertEquals(7000, buyer.getTotalMoney()); // assuming buyer starts with 9000
 }

 @Test
 public void testCastleFiefdom_FiefdomDoesntBelongToKingdom() {
 Collection<Object> args = Arrays.asList("2", "John", "fiefdom2");
 Iterator<Object> iterator = args.iterator();
 Mockito.when(map.get(iterator.next())).thenReturn(new Fiefdoms("fiefdom2", null));

 purchase.perform(args);

 assertEquals(50, fiefdom.getTotalCastles());
 equals(1000, buyer.getTotalMoney());
 }

 @Test
 public void testCastlePersonal() {
 Collection<Object> args = Arrays.asList("1", "John", "fiefdom1");
 Iterator<Object> iterator = args.iterator();
 Mockito.when(map.get(iterator.next())).thenReturn(fiefdom);
 Mockito.when(currentPlayers.get(iterator.next())).thenReturn(buyer);

 purchase.perform(args);

 assertEquals(1, fiefdom.getTotalPersonalCastles());
 assertEquals(9000 - 10, buyer.getTotalMoney()); // assuming buyer starts with 9000
 }

 @Test
 public void testCastlePersonal_InsufficientFunds() {
 Collection<Object> args = Arrays.asList("2", "John", "fiefdom1");
 Iterator<Object> iterator = args.iterator();
 Mockito.when(map.get(iterator.next())).thenReturn(fiefdom);
 Mockito.when(currentPlayers.get(iterator.next())).thenReturn(buyer);

 purchase.perform(args);

 assertEquals(1, fiefdom.getTotalPersonalCastles());
 assertEquals(8500, buyer.getTotalMoney()); // assuming buyer starts with 9000
 }

 @Test
 public void testCastlePersonal_FiefdomDoesntBelongToKingdom() {
 Collection<Object> args = Arrays.asList("1", "John", "fiefdom2");
 Iterator<Object> iterator = args.iterator();
 Mockito.when(map.get(iterator.next())).thenReturn(new Fiefdoms("fiefdom2", null));

 purchase.perform(args);

 assertEquals(1, fiefdom.getTotalPersonalCastles());
 assertEquals(1000, buyer.getTotalMoney());
 }

 @Test
 public void testMercenary() {
 Collection<Object> args = Arrays.asList("3", "John", "knight1");
 Iterator<Object> iterator = args.iterator();
 Mockito.when(currentPlayers.get(iterator.next())).thenReturn(buyer);
 Mockito.when(currentPlayers.get(iterator.next())).thenReturn(knight);

 purchase.perform(args);

 assertEquals(3, knight.getMercenaries());
 assertEquals(8700, buyer.getTotalMoney()); // assuming buyer starts with 9000
 }

 @Test
 public void testMercenary_InsufficientFunds() {
 Collection<Object> args = Arrays.asList("4", "John", "knight1");
 Iterator<Object> iterator = args.iterator();
 Mockito.when(currentPlayers.get(iterator.next())).thenReturn(buyer);
 Mockito.when(currentPlayers.get(iterator.next())).thenReturn(knight);

 purchase.perform(args);

 assertEquals(3, knight.getMercenaries());
 assertEquals(7000, buyer.getTotalMoney()); // assuming buyer starts with 9000
 }

 @Test
 public void testMercenary_SamePlayer() {
 Collection<Object> args = Arrays.asList("3", "John", "John");
 Iterator<Object> iterator = args.iterator();
 Mockito.when(currentPlayers.get(iterator.next())).thenReturn(buyer);
 Mockito.when(currentPlayers.get(iterator.next())).thenReturn(buyer);

 purchase.perform(args);

 assertEquals(3, knight.getMercenaries()); // assuming knight is the same object as buyer
 assertEquals(7000, buyer.getTotalMoney()); // assuming buyer starts with 9000
 }

 @Test
 public void testMercenary_WrongPlayer() {
 Collection<Object> args = Arrays.asList("3", "Tom", "knight1");
 Iterator<Object> iterator = args.iterator();
 Mockito.when(currentPlayers.get(iterator.next())).thenReturn(buyer);
 Mockito.when(currentPlayers.get(iterator.next())).thenReturn(knight);

 purchase.perform(args);

 assertEquals(3, knight.getMercenaries());
 assertEquals(1000, buyer.getTotalMoney());
 }
}