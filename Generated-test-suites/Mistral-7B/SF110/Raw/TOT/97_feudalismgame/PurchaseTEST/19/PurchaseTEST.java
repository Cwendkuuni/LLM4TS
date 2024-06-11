package src.test.junit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PurchaseTest {

 private Purchase purchase;
 private Collection<Iterator<Object>> args;
 private Map map;
 private CurrentPlayers currentPlayers;
 private Fiefdoms fiefdom;

 @Before
 public void setup() {
 Map map = new HashMap();
 CurrentPlayers currentPlayers = new CurrentPlayers();
 this.map = map;
 this.currentPlayers = currentPlayers;
 this.purchase = new Purchase();
 this.args = Arrays.asList(
 mock(Iterator.class),
 mock(Iterator.class),
 mock(Iterator.class),
 mock(Iterator.class)
 );

 when(args.get(0)).thenReturn("fiefdomName1");
 when(args.get(1)).thenReturn(Arrays.asList("buyer1", "10", "fiefdomName1").iterator());
 when(args.get(2)).thenReturn(map);
 when(args.get(3)).thenReturn(new Knight("buyer1", "knight"));

 fiefdom = new Fiefdoms("fiefdomName1", null);
 map.put("fiefdomName1", fiefdom);
 }

 @Test
 public void vassal_ValidScenario() {
 purchase.perform(args);
 verify(fiefdom, times(1)).setLoyalVassals(1, true);
 }

 @Test
 public void vassal_NotSufficientFunds() {
 when(args.get(1)).thenReturn(Arrays.asList("buyer1", "11", "fiefdomName1").iterator());
 purchase.perform(args);
 verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
 }

 @Test
 public void vassal_FiefdomDoesntBelongToTheKingdom() {
 when(args.get(2)).thenReturn(new Fiefdoms("notFiefdomName", null));
 purchase.perform(args);
 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
 }

 @Test
 public void castleFiefdom_ValidScenario() {
 purchase.perform(args);
 verify(fiefdom, times(1)).buyFiefdomCastles(1);
 }

 @Test
 public void castleFiefdom_NotSufficientFunds() {
 when(args.get(1)).thenReturn(Arrays.asList("buyer1", "1", "fiefdomName1").iterator());
 purchase.perform(args);
 verify(System.out, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
 }

 @Test
 public void castleFiefdom_FiefdomDoesntBelongToTheKingdom() {
 when(args.get(2)).thenReturn(new Fiefdoms("notFiefdomName", null));
 purchase.perform(args);
 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
 }

 @Test
 public void castlePersonal_ValidScenario() {
 purchase.perform(args);
 verify(fiefdom, times(1)).buyPersonalCastles(1);
 }

 @Test
 public void castlePersonal_NotSufficientFunds() {
 when(args.get(1)).thenReturn(Arrays.asList("buyer1", "1", "fiefdomName1").iterator());
 purchase.perform(args);
 verify(System.out, times(1)).println("You cannot buy more Personal castle, you have insufficient funds");
 }

 @Test
 public void mercenary_ValidScenario() {
 purchase.perform(args);
 verify(knight, times(1)).addMercenaries(1);
 }

 @Test
 public void mercenary_InvalidKnight() {
 args.set(2, null);
 purchase.perform(args);
 verify(System.out, times(1)).println("Inexistent Knight");
 }
}