import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.Actions;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;
import src.Purchase;
import src.Player;
import src.Rank;

import java.util.*;

import static org.junit.Assert.*;

public class PurchaseTest {

 private Purchase purchase;
 private Actions actions;
 private GameAutoActions gameAutoActions;
 private Map map;
 private Fiefdoms fiefdoms;
 private List<Player> currentPlayers;

 @Before
 public void setUp() {
 actions = Mockito.spy(new Actions());
 gameAutoActions = Mockito.mock(GameAutoActions.class);
 map = Mockito.mock(Map.class);
 fiefdoms = Mockito.mock(Fiefdoms.class);
 currentPlayers = new ArrayList<>();
 purchase = new Purchase(actions, gameAutoActions, map, fiefdoms);
 }

 @Test
 public void testVassal() {
 Player buyer = new Player("John", Rank.Duke, 1000);
 currentPlayers.add(buyer);
 Fiefdoms fiefdom = new Fiefdoms("testFiefdom", "testOwner");
 map.put("testFiefdom", fiefdom);
 Knight buyerObj = currentPlayers.get(0);
 Iterator<Object> arg = Mockito.mock(Iterator.class).iterator();
 arg.next();
 arg.next();
 arg.next();
 when(map.get(arg.next())).thenReturn(fiefdom);
 when(currentPlayers.get(arg.next())).thenReturn(buyerObj);
 when(fiefdom.getOwner()).thenReturn(null);
 when(fiefdom.getLoyalVassals()).thenReturn(0);

 purchase.perform(Arrays.asList(arg).iterator());
 assertEquals(0, fiefdom.getLoyalVassals());

 arg.next();
 arg.next();
 arg.next();
 when(map.get(arg.next())).thenReturn(fiefdom);
 when(currentPlayers.get(arg.next())).thenReturn(buyerObj);
 when(fiefdom.getOwner()).thenReturn(null);
 when(fiefdom.getLoyalVassals()).thenReturn(1);
 when(buyerObj.getTotalMoney()).thenReturn(2000);

 purchase.perform(Arrays.asList(arg).iterator());
 assertEquals(1, fiefdom.getLoyalVassals());

 arg.next();
 arg.next();
 arg.next();
 when(map.get(arg.next())).thenReturn(fiefdom);
 when(currentPlayers.get(arg.next())).thenReturn(buyerObj);
 when(fiefdom.getOwner()).thenReturn(null);
 when(fiefdom.getLoyalVassals()).thenReturn(5);
 when(buyerObj.getTotalMoney()).thenReturn(1000);

 purchase.perform(Arrays.asList(arg).iterator());
 assertFalse(buyerObj.setTotalMoney(2, false));
 assertEquals(5, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalInvalidFiefdom() {
 Player buyer = new Player("John", Rank.Duke, 1000);
 currentPlayers.add(buyer);
 Fiefdoms fiefdom = new Fiefdoms("testFiefdom", "testOwner");
 map.put("testFiefdom", fiefdom);
 Knight buyerObj = currentPlayers.get(0);
 Iterator<Object> arg = Mockito.mock(Iterator.class).iterator();
 arg.next();
 arg.next();
 arg.next();
 arg.next();
 when(map.get(arg.next())).thenThrow(new RuntimeException("Fiefdom doesn't belong to the kingdom: " + "testFiefdom"));

 purchase.perform(Arrays.asList(arg).iterator());
 assertFalse(buyerObj.setTotalMoney(5, false));
 }

 // Add more test cases for other methods as needed

}