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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PurchaseTest {

 private Purchase purchase;
 private Actions actions;
 private GameAutoActions gameAutoActions;
 private Map map;
 private CurrentPlayers currentPlayers;
 private Fiefdoms fiefdom;
 private List<Knight> knights = new ArrayList<>();

 @Before
 public void setUp() {
 actions = mock(Actions.class);
 gameAutoActions = mock(GameAutoActions.class);
 map = mock(Map.class);
 currentPlayers = mock(CurrentPlayers.class);
 purchase = new Purchase(actions, gameAutoActions, map, currentPlayers);

 fiefdom = new Fiefdoms();
 fiefdom.setName("TestFiefdom");
 fiefdom.setOwner(null);

 knights.add(new Knight("TestKnight1", "knight", 1000, 0, null));
 knights.add(new Knight("TestKnight2", "knight", 500, 0, null));
 currentPlayers.setCurrentPlayers(knights);
 }

 @Test
 public void testVassal() {
 Iterator<Object> argIter = Arrays.asList(1, "TestBuyer1", "TestFiefdom").iterator();
 when(map.get(argIter.next())).thenReturn(fiefdom);
 when(currentPlayers.get(argIter.next())).thenReturn(currentPlayers.getCurrentPlayers().get(0));

 purchase.perform(Arrays.asList(argIter));

 verify(currentPlayers.getCurrentPlayers().get(0), times(1)).setTotalMoney(1, false);
 verify(fiefdom, times(1)).setLoyalVassals(1, true);
 }

 @Test
 public void testVassalInsufficientFunds() {
 Iterator<Object> argIter = Arrays.asList(2, "TestBuyer1", "TestFiefdom").iterator();
 when(map.get(argIter.next())).thenReturn(fiefdom);
 when(currentPlayers.get(argIter.next())).thenReturn(currentPlayers.getCurrentPlayers().get(0));

 purchase.perform(Arrays.asList(argIter));

 verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
 }

 @Test
 public void testVassalWrongOwner() {
 Iterator<Object> argIter = Arrays.asList(1, "TestBuyer2", "TestFiefdom").iterator();
 when(map.get(argIter.next())).thenReturn(fiefdom);
 when(currentPlayers.get(argIter.next())).thenReturn(currentPlayers.getCurrentPlayers().get(1));

 purchase.perform(Arrays.asList(argIter));

 verify(System.out, times(1)).println("You can only buy a vassal for a fiefdom that belongs to you");
 }

 @Test
 public void testCastleFiefdom() {
 Iterator<Object> argIter = Arrays.asList(2, "TestBuyer1", "TestFiefdom").iterator();
 when(map.get(argIter.next())).thenReturn(fiefdom);
 when(currentPlayers.get(argIter.next())).thenReturn(currentPlayers.getCurrentPlayers().get(0));

 purchase.perform(Arrays.asList(argIter));

 verify(currentPlayers.getCurrentPlayers().get(0), times(1)).setTotalMoney(25, false);
 verify(fiefdom, times(1)).buyFiefdomCastles(2);
 }

 @Test
 public void testCastleFiefdomInsufficientFunds() {
 Iterator<Object> argIter = Arrays.asList(4, "TestBuyer1", "TestFiefdom").iterator();
 when(map.get(argIter.next())).thenReturn(fiefdom);
 when(currentPlayers.get(argIter.next())).thenReturn(currentPlayers.getCurrentPlayers().get(0));

 purchase.perform(Arrays.asList(argIter));

 verify(System.out, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
 }

 @Test
 public void testCastleFiefdomWrongOwner() {
 Iterator<Object> argIter = Arrays.asList(2, "TestBuyer2", "TestFiefdom").iterator();
 when(map.get(argIter.next())).thenReturn(fiefdom);
 when(currentPlayers.get(argIter.next())).thenReturn(currentPlayers.getCurrentPlayers().get(1));

 purchase.perform(Arrays.asList(argIter));

 verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
 }

 @Test
 public void testCastlePersonal() {
 Iterator<Object> argIter = Arrays.asList(1, "TestBuyer1", "TestFiefdom").iterator();
 when(map.get(argIter.next())).thenReturn(fiefdom);
 when(currentPlayers.get(argIter.next())).thenReturn(currentPlayers.getCurrentPlayers().get(0));

 purchase.perform(Arrays.asList(argIter));

 verify(currentPlayers.getCurrentPlayers().get(0), times(1)).setTotalMoney(10, false);
 verify(fiefdom, times(1)).buyPersonalCastles(1);
 }

 @Test
 public void testCastlePersonalInsufficientFunds() {
 Iterator<Object> argIter = Arrays.asList(3, "TestBuyer1", "TestFiefdom").iterator();
 when(map.get(argIter.next())).thenReturn(fiefdom);
 when(currentPlayers.get(argIter.next())).thenReturn(currentPlayers.getCurrentPlayers().get(0));

 purchase.perform(Arrays.asList(argIter));

 verify(System.out, times(1)).println("You cannot buy more Personal castle, you have insufficient funds");
 }

 @Test
 public void testCastlePersonalWrongRank() {
 Iterator<Object> argIter = Arrays.asList(1, "TestCount", "TestFiefdom").iterator();
 when(map.get(argIter.next())).thenReturn(fiefdom);
 when(currentPlayers.get(argIter.next())).thenReturn(currentPlayers.getCurrentPlayers().get(0));

 purchase.perform(Arrays.asList(argIter));

 verify(System.out, times(1)).println("You cannot buy a Personal Castle for Counts or Dukes");
 }

 @Test
 public void testCastlePersonalWrongFiefdomOwner() {
 Iterator<Object> argIter = Arrays.asList(1, "TestBuyer1", "AnotherTestFiefdom").iterator();
 when(map.get(argIter.next())).thenReturn(new Fiefdoms());
 when(currentPlayers.get(argIter.next())).thenReturn(currentPlayers.getCurrentPlayers().get(0));

 purchase.perform(Arrays.asList(argIter));

 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom: AnotherTestFiefdom owner: null");
 }

 @Test
 public void testMercenary() {
 Iterator<Object> argIter = Arrays.asList(2, "TestBuyer1", "TestKnight1").iterator();
 when(currentPlayers.get(argIter.next())).thenReturn(currentPlayers.getCurrentPlayers().get(0));
 when(currentPlayers.get(argIter.next())).thenReturn(knights.get(0));

 purchase.perform(Arrays.asList(argIter));

 verify(knights.get(0), times(1)).addMercenaries(2);
 }

 @Test
 public void testMercenaryInsufficientFunds() {
 Iterator<Object> argIter = Arrays.asList(3, "TestBuyer1", "TestKnight1").iterator();
 when(currentPlayers.get(argIter.next())).thenReturn(currentPlayers.getCurrentPlayers().get(0));
 when(currentPlayers.get(argIter.next())).thenReturn(knights.get(0));

 purchase.perform(Arrays.asList(argIter));

 verify(System.out, times(1)).println("You cannot buy more mercenaries, you have insufficient funds");
 }

 @Test
 public void testMercenaryDifferentPlayer() {
 Iterator<Object> argIter = Arrays.asList(2, "TestBuyer2", "TestKnight1").iterator();
 when(currentPlayers.get(argIter.next())).thenReturn(currentPlayers.getCurrentPlayers().get(0));
 when(currentPlayers.get(argIter.next())).thenReturn(knights.get(0));

 purchase.perform(Arrays.asList(argIter));

 verify(System.out, times(1)).println("Inexistent Knight");
 }
}