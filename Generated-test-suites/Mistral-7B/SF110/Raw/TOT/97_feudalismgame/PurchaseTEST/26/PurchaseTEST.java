import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Actions;
import src.CurrentPlayers;
import src.Knight;
import src.Purchase;
import src.Map;
import java.util.*;

public class PurchaseTest {

 private Purchase purchase;
 private Iterator<Object> argsIter;
 private Collection<Object> args;
 private GameAutoActions gameAutoActionsMock;
 private CurrentPlayers currentPlayersMock;
 private Fiefdoms fiefdomMock;
 private Knight buyerMock, knightMock, anotherBuyerMock;
 private Map mapMock;

 @Before
 public void setup() {
 gameAutoActionsMock = Mockito.mock(GameAutoActions.class);
 currentPlayersMock = Mockito.mock(CurrentPlayers.class);
 fiefdomMock = Mockito.mock(Fiefdoms.class);
 mapMock = Mockito.mock(Map.class);
 buyerMock = Mockito.mock(Knight.class);
 knightMock = Mockito.mock(Knight.class);
 anotherBuyerMock = Mockito.mock(Knight.class);

 purchase = new Purchase();
 purchase.setGameAutoActions(gameAutoActionsMock);
 purchase.setCurrentPlayers(currentPlayersMock);
 purchase.setMap(mapMock);

 args = new ArrayList<>();
 argsIter = args.iterator();
 }

 @Test
 public void testVassal() {
 argsIter.add("10");
 argsIter.add("buyer1");
 argsIter.add("fiefdom1");

 Mockito.when(mapMock.get(argIter.next())).thenReturn(fiefdomMock);
 Mockito.when(currentPlayersMock.get(argIter.next())).thenReturn(buyerMock);
 Mockito.when(currentPlayersMock.get(argIter.next())).thenReturn(null);
 Mockito.when(fiefdomMock.getOwner()).thenReturn(anotherBuyerMock);
 Mockito.when(anotherBuyerMock.getRank()).thenReturn("count");
 Mockito.when(buyerMock.getRank()).thenReturn("duke");
 Mockito.when(buyerMock.isFiefdomMine(argIter.next())).thenReturn(false);
 Mockito.when(buyerMock.getTotalMoney(0)).thenReturn(true);
 Mockito.when(fiefdomMock.getLoyalVassals(0)).thenReturn(5);

 purchase.perform(args);

 Mockito.verify(gameAutoActionsMock, Mockito.times(1)).saveAll();
 Mockito.verifyNoMoreInteractions(gameAutoActionsMock);
 Mockito.verify(buyerMock, Mockito.times(1)).setTotalMoney(10, false);
 Mockito.verify(fiefdomMock, Mockito.times(1)).setLoyalVassals(5, true);
 }

 @Test
 public void testVassalWithInsufficientFunds() {
 argsIter.clear();
 argsIter.add("20");
 argsIter.add("buyer1");
 argsIter.add("fiefdom1");

 Mockito.when(mapMock.get(argIter.next())).thenReturn(fiefdomMock);
 Mockito.when(currentPlayersMock.get(argIter.next())).thenReturn(buyerMock);
 Mockito.when(currentPlayersMock.get(argIter.next())).thenReturn(null);
 Mockito.when(fiefdomMock.getOwner()).thenReturn(anotherBuyerMock);
 Mockito.when(anotherBuyerMock.getRank()).thenReturn("count");
 Mockito.when(buyerMock.getRank()).thenReturn("duke");
 Mockito.when(buyerMock.isFiefdomMine(argIter.next())).thenReturn(false);
 Mockito.when(buyerMock.getTotalMoney(0)).thenReturn(false);

 purchase.perform(args);

 Mockito.verify(gameAutoActionsMock, Mockito.times(1)).saveAll();
 Mockito.verifyNoMoreInteractions(gameAutoActionsMock);
 Mockito.verifyNoInteractions(buyerMock);
 Mockito.verifyNoInteractions(fiefdomMock);
 Mockito.verify(System.out).println("You cannot buy more vassals, you have insufficient funds");
 }

 @Test
 public void testVassalWithInvalidFiefdom() {
 argsIter.clear();
 argsIter.add("10");
 argsIter.add("buyer1");
 argsIter.add("fiefdom2");

 Mockito.when(mapMock.get(argIter.next())).thenReturn(null);

 purchase.perform(args);

 Mockito.verify(gameAutoActionsMock, Mockito.times(1)).saveAll();
 Mockito.verifyNoMoreInteractions(gameAutoActionsMock);
 Mockito.verify(System.out).println("Fiefdom doesn't belong to the kingdom");
 }

 // Add similar test cases for castleFiefdom, castlePersonal, and mercenary methods

}