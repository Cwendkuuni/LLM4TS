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
 private Fiefdoms fiefdom;
 private Map map;
 private CurrentPlayers currentPlayers;

 @Before
 public void setUp() {
 actions = Mockito.mock(Actions.class);
 gameAutoActions = Mockito.mock(GameAutoActions.class);
 purchase = new Purchase(actions, gameAutoActions);
 fiefdom = new Fiefdoms();
 map = new Map();
 currentPlayers = new CurrentPlayers();
 }

 @Test
 public void testVassal() {
 Iterator<String> arg = Arrays.asList("1", "John", "fiefdom1").iterator();
 Mockito.when(map.get(arg.next())).thenReturn(fiefdom);
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("John", "knight", 100));
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("king", "king", 500));
 Mockito.when(fiefdom.getOwner()).thenReturn(null);
 Mockito.when(fiefdom.getName()).thenReturn("fiefdom1");

 purchase.perform(Collections.singletonList(arg));
 assertEquals(1, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalNotEnoughMoney() {
 Iterator<String> arg = Arrays.asList("10", "John", "fiefdom1").iterator();
 Mockito.when(map.get(arg.next())).thenReturn(fiefdom);
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("John", "knight", 50));
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("king", "king", 500));
 Mockito.when(fiefdom.getOwner()).thenReturn(null);
 Mockito.when(fiefdom.getName()).thenReturn("fiefdom1");

 purchase.perform(Collections.singletonList(arg));
 assertEquals(0, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalNotOwnFiefdom() {
 Iterator<String> arg = Arrays.asList("1", "John", "fiefdom2").iterator();
 Mockito.when(map.get(arg.next())).thenReturn(new Fiefdoms());
 Mockito.when(map.get(arg.next())).thenReturn(new Fiefdoms());
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("John", "knight", 100));
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("king", "king", 500));

 purchase.perform(Collections.singletonList(arg));
 assertEquals(0, fiefdom.getLoyalVassals());
 }

 @Test
 public void testCastleFiefdom() {
 Iterator<String> arg = Arrays.asList("5", "John", "fiefdom1").iterator();
 Mockito.when(map.get(arg.next())).thenReturn(fiefdom);
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("John", "knight", 500));
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("king", "king", 1000));
 Mockito.when(fiefdom.getOwner()).thenReturn(null);
 Mockito.when(fiefdom.getName()).thenReturn("fiefdom1");

 purchase.perform(Collections.singletonList(arg));
 assertEquals(5, fiefdom.getCastles().size());
 }

 //Same as testVassalNotEnoughMoney but for castles

 @Test
 public void testCastleFiefdomNotOwn() {
 Iterator<String> arg = Arrays.asList("5", "John", "fiefdom2").iterator();
 Mockito.when(map.get(arg.next())).thenReturn(new Fiefdoms());
 Mockito.when(map.get(arg.next())).thenReturn(new Fiefdoms());
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("John", "knight", 500));
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("king", "king", 1000));

 purchase.perform(Collections.singletonList(arg));
 assertEquals(0, fiefdom.getCastles().size());
 }

 // Same as testVassal but for castlePersonal

 @Test
 public void testCastlePersonal() {
 Iterator<String> arg = Arrays.asList("1", "John", "fiefdom1").iterator();
 Mockito.when(map.get(arg.next())).thenReturn(fiefdom);
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("John", "knight", 100));
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("king", "king", 500));
 Mockito.when(fiefdom.getOwner()).thenReturn(null);
 Mockito.when(fiefdom.getName()).thenReturn("fiefdom1");

 purchase.perform(Collections.singletonList(arg));
 assertEquals(1, fiefdom.getPersonalCastles());
 }

 //Same as testVassalNotEnoughMoney but for personal castles

 @Test
 public void testCastlePersonalNotOwn() {
 Iterator<String> arg = Arrays.asList("1", "John", "fiefdom2").iterator();
 Mockito.when(map.get(arg.next())).thenReturn(new Fiefdoms());
 Mockito.when(map.get(arg.next())).thenReturn(new Fiefdoms());
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("John", "knight", 100));
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("king", "king", 500));

 purchase.perform(Collections.singletonList(arg));
 assertEquals(0, fiefdom.getPersonalCastles());
 }

 @Test
 public void testMercenary() {
 Iterator<String> arg = Arrays.asList("2", "John", "knight1").iterator();
 Mockito.when(map.get(arg.next())).thenReturn(map);
 Mockito.when(map.get(arg.next())).thenReturn(currentPlayers);
 Mockito.when(map.get(arg.next())).thenReturn(map);
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("John", "knight", 500));
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("knight1", "knight", 50));

 purchase.perform(Collections.singletonList(arg));
 assertEquals(2, ((Knight) map.get("knight1")).getMercenaries());
 }

 @Test
 public void testMercenaryBuyForYourself() {
 Iterator<String> arg = Arrays.asList("2", "John", "John").iterator();
 Mockito.when(map.get(arg.next())).thenReturn(map);
 Mockito.when(map.get(arg.next())).thenReturn(currentPlayers);
 Mockito.when(map.get(arg.next())).thenReturn(map);
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("John", "knight", 500));

 purchase.perform(Collections.singletonList(arg));
 assertEquals(2, ((Knight) map.get("John")).getMercenaries());
 }

 @Test(expected = SecurityException.class)
 public void testSecurityException() {
 Iterator<String> arg = Arrays.asList("1", "John", "fiefdom1", "notValidClass").iterator();
 Mockito.when(map.get(arg.next())).thenReturn(fiefdom);
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("John", "knight", 500));
 Mockito.when(currentPlayers.get(arg.next())).thenReturn(new Knight("king", "king", 1000));
 Mockito.when(fiefdom.getOwner()).thenReturn(null);
 Mockito.when(fiefdom.getName()).thenReturn("fiefdom1");

 purchase.perform(Collections.singletonList(arg));
 }
}