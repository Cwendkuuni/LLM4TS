package src.test.java;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.*;

public class PurchaseTest {

 private Purchase purchase;
 private List<String> args;
 private Fiefdoms map;
 private CurrentPlayers currentPlayers;

 @Before
 public void setUp() {
 purchase = new Purchase();
 args = new ArrayList<>();
 map = mock(Fiefdoms.class);
 currentPlayers = mock(CurrentPlayers.class);
 }

 @Test
 public void testPerform() {
 Iterator<String> argIterator = args.iterator();
 when(map.get(argIterator.next())).thenReturn(mock(Fiefdoms.class));
 when(currentPlayers.get(eq(argIterator.next()))).thenReturn(mock(Knight.class));
 when(currentPlayers.containsKey(eq(argIterator.next()))).thenReturn(true);
 when(currentPlayers.get(eq(argIterator.next()))).thenReturn(mock(Knight.class));

 args.add("vassal");
 args.add("1");
 args.add("fiefdom1");
 args.add("player1");
 purchase.perform(args);

 verify(map, times(1)).get(eq("fiefdom1"));
 verify(currentPlayers, times(1)).get(eq("player1"));
 verify(currentPlayers, times(1)).get(eq("fiefdom1"));
 verify(currentPlayers, atLeastOnce()).containsKey(eq("player1"));
 verify(currentPlayers, atLeastOnce()).containsKey(eq("fiefdom1"));
 }

 @Test(expected = SecurityException.class)
 public void testPerformSecurityException() {
 args.clear();
 args.add("vassal");
 args.add(Collections.emptyIterator());
 purchase.perform(args);
 }

 @Test(expected = NoSuchMethodException.class)
 public void testPerformNoSuchMethodException() {
 args.clear();
 args.add("invalidMethod");
 purchase.perform(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformIllegalArgumentException() {
 args.clear();
 args.add("vassal");
 args.add(null);
 purchase.perform(args);
 }

 @Test(expected = IllegalAccessException.class)
 public void testPerformIllegalAccessException() {
 args.clear();
 args.add("vassal");
 args.add(Collections.singletonList("arg").iterator());
 purchase.perform(args);
 }

 @Test(expected = InvocationTargetException.class)
 public void testPerformInvocationTargetException() {
 args.clear();
 args.add("vassal");
 args.add(Collections.singletonList(new Object() {}).iterator());
 purchase.perform(args);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testPerformClassNotFoundException() {
 args.clear();
 args.add("InvalidClass");
 purchase.perform(args);
 }

 @Test
 public void testVassal() {
 args.clear();
 args.add("vassal");
 args.add(mock(Iterator.class));
 when(args.next()).thenReturn("1");
 when(args.next()).thenReturn("player1");
 when(args.next()).thenReturn("fiefdom1");
 when(map.get(eq("fiefdom1"))).thenReturn(mock(Fiefdoms.class));
 when(currentPlayers.get(eq("player1"))).thenReturn(mock(Knight.class));
 when(map.map.get(eq("fiefdom1"))).thenReturn(mock(Fiefdoms.class));
 when(currentPlayers.currentPlayers.get(eq("player1"))).thenReturn(mock(Knight.class));

 purchase.vassal(args.iterator());

 verify(map, times(1)).get(eq("fiefdom1"));
 verify(currentPlayers, times(1)).get(eq("player1"));
 verify(currentPlayers.currentPlayers, times(1)).get(eq("player1"));
 verify(map, times(1)).get(eq("fiefdom1"));
 verify(map, times(1)).get(eq("fiefdom1")).setLoyalVassals(eq(1), anyBoolean());
 }

 @Test
 public void testCastleFiefdom() {
 args.clear();
 args.add("castleFiefdom");
 args.add(mock(Iterator.class));
 when(args.next()).thenReturn("1");
 when(args.next()).thenReturn("player1");
 when(args.next()).thenReturn("fiefdom1");
 when(map.get(eq("fiefdom1"))).thenReturn(mock(Fiefdoms.class));
 when(currentPlayers.get(eq("player1"))).thenReturn(mock(Knight.class));
 when(map.map.get(eq("fiefdom1"))).thenReturn(mock(Fiefdoms.class));
 when(currentPlayers.currentPlayers.get(eq("player1"))).thenReturn(mock(Knight.class));
 when(map.map.get(eq("fiefdom1")).getOwner()).thenReturn(null);

 purchase.castleFiefdom(args.iterator());

 verify(map, times(1)).get(eq("fiefdom1"));
 verify(currentPlayers, times(1)).get(eq("player1"));
 verify(map, times(1)).get(eq("fiefdom1"));
 verify(map, times(1)).get(eq("fiefdom1")).getOwner();
 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
 }

 @Test
 public void testCastlePersonal() {
 args.clear();
 args.add("castlePersonal");
 args.add(mock(Iterator.class));
 when(args.next()).thenReturn("1");
 when(args.next()).thenReturn("player1");
 when(args.next()).thenReturn("fiefdom1");
 when(map.get(eq("fiefdom1"))).thenReturn(mock(Fiefdoms.class));
 when(currentPlayers.get(eq("player1"))).thenReturn(mock(Knight.class));
 when(map.map.get(eq("fiefdom1"))).thenReturn(mock(Fiefdoms.class));
 when(currentPlayers.currentPlayers.get(eq("player1"))).thenReturn(mock(Knight.class));
 when(map.map.get(eq("fiefdom1")).getOwner()).thenReturn(null);
 when(map.map.get(eq("fiefdom1")).getOwner()).thenReturn(mock(Knight.class));
 when(map.map.get(eq("fiefdom1")).getOwner().getRank()).thenReturn("count");

 purchase.castlePersonal(args.iterator());

 verify(map, times(1)).get(eq("fiefdom1"));
 verify(currentPlayers, times(1)).get(eq("player1"));
 verify(map, times(1)).get(eq("fiefdom1"));
 verify(map, times(1)).get(eq("fiefdom1")).getOwner();
 verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
 }

 @Test
 public void testMercenary() {
 args.clear();
 args.add("mercenary");
 args.add(mock(Iterator.class));
 when(args.next()).thenReturn("1");
 when(args.next()).thenReturn("player1");
 when(args.next()).thenReturn("knight1");
 when(currentPlayers.get(eq("player1"))).thenReturn(mock(Knight.class));
 when(currentPlayers.get(eq("knight1"))).thenReturn(mock(Knight.class));

 purchase.mercenary(args.iterator());

 verify(currentPlayers, times(1)).get(eq("player1"));
 verify(currentPlayers, times(1)).get(eq("knight1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMercenaryInvalidKnight() {
 args.clear();
 args.add("mercenary");
 args.add(mock(Iterator.class));
 when(args.next()).thenReturn("1");
 when(args.next()).thenReturn("player1");
 when(args.next()).thenReturn("inexistentKnight");
 when(currentPlayers.get(eq("player1"))).thenReturn(mock(Knight.class));

 purchase.mercenary(args.iterator());

 verify(System.out, times(1)).println("Inexistent Knight");
 }

 @Test
 public void testMercenaryInsufficientFunds() {
 args.clear();
 args.add("mercenary");
 args.add(mock(Iterator.class));
 when(args.next()).thenReturn("1");
 when(args.next()).thenReturn("player1");
 when(args.next()).thenReturn("knight1");
 when(currentPlayers.get(eq("player1"))).thenReturn(mock(Knight.class));
 when(currentPlayers.get(eq("knight1"))).thenReturn(mock(Knight.class));
 when(currentPlayers.get(eq("player1")).getTotalMoney()).thenReturn(0);

 purchase.mercenary(args.iterator());

 verify(System.out, times(1)).println("You cannot buy more mercenaries, you have insufficient funds");
 }
}