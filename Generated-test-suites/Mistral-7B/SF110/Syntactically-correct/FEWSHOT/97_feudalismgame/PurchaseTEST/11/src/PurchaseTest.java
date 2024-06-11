package src;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.JUnit4;

@RunWith(JUnit4.class)
public class PurchaseTest {

 private static final String KING_NAME = "KingName";
 private static final String COUNT_NAME = "CountName";
 private static final String DUKE_NAME = "DukeName";
 private static final String FIEFDOm_NAME = "FiefdomName";
 private static final int VASSAL_QTY = 5;
 private static final int CASTLE_QTY = 3;
 private static final int PERSONAL_CASTLE_QTY = 2;
 private static final int MERCENARY_QTY = 7;

 @Mock
 private Collection<Iterator<String>> args;
 @Mock
 private Fiefdoms fiefdom;
 @Mock
 private Map map;
 @Mock
 private Knight buyerObj;
 @Mock
 private Knight anotherKnight;
 @Mock
 private CurrentPlayers currentPlayers;

 @Test
 public void testVassal() {
 final Iterator<String> iter = mock(Iterator.class);
 when(args.iterator()).thenReturn(iter);
 when(map.get(eq(FIEFDOm_NAME))).thenReturn(fiefdom);
 when(currentPlayers.get(eq(KING_NAME))).thenReturn(buyerObj);
 when(buyerObj.getRank()).thenReturn("king");
 when(fiefdom.getOwner()).thenReturn(null);
 when(fiefdom.getLoyalVassals()).thenReturn(0);

 Purchase purchase = new Purchase();
 purchase.perform(args);

 verify(buyerObj, times(1)).setTotalMoney(eq(VASSAL_QTY), anyBoolean());
 verify(fiefdom, times(1)).setLoyalVassals(eq(VASSAL_QTY), anyBoolean());
 }

 @Test
 public void testVassal_InsufficientFunds() {
 final Iterator<String> iter = mock(Iterator.class);
 when(args.iterator()).thenReturn(iter);
 when(map.get(eq(FIEFDOm_NAME))).thenReturn(fiefdom);
 when(currentPlayers.get(eq(KING_NAME))).thenReturn(buyerObj);
 when(buyerObj.getRank()).thenReturn("king");
 when(fiefdom.getOwner()).thenReturn(null);
 when(fiefdom.getLoyalVassals()).thenReturn(0);
 when(buyerObj.getTotalMoney()).thenReturn(20);

 Purchase purchase = new Purchase();
 purchase.perform(args);

 verify(buyerObj, never()).setTotalMoney(anyInt(), anyBoolean());
 verify(fiefdom, never()).setLoyalVassals(anyInt(), anyBoolean());
 verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
 }

 @Test
 public void testVassal_FiefdomBelongsToAnotherPlayer() {
 final Iterator<String> iter = mock(Iterator.class);
 when(args.iterator()).thenReturn(iter);
 when(map.get(eq(FIEFDOm_NAME))).thenReturn(fiefdom);
 when(currentPlayers.get(eq(KING_NAME))).thenReturn(buyerObj);
 when(buyerObj.getRank()).thenReturn("king");
 when(fiefdom.getOwner()).thenReturn(anotherPlayer());

 Purchase purchase = new Purchase();
 purchase.perform(args);

 verify(buyerObj, never()).setTotalMoney(anyInt(), anyBoolean());
 verify(fiefdom, never()).setLoyalVassals(anyInt(), anyBoolean());
 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom: " + FIEFDOm_NAME + " owner: " + anotherPlayer().getName());
 }

 @Test
 public void testVassal_FiefdomAlreadyBelongsToPlayer() {
 final Iterator<String> iter = mock(Iterator.class);
 when(args.iterator()).thenReturn(iter);
 when(map.get(eq(FIEFDOm_NAME))).thenReturn(fiefdom);
 when(currentPlayers.get(eq(KING_NAME))).thenReturn(buyerObj);
 when(buyerObj.getRank()).thenReturn("king");
 when(fiefdom.getOwner()).thenReturn(buyerObj);
 when(buyerObj.getTotalMoney()).thenReturn(40);

 Purchase purchase = new Purchase();
 purchase.perform(args);

 verify(buyerObj, times(1)).setTotalMoney(eq(VASSAL_QTY), anyBoolean());
 verify(fiefdom, times(1)).setLoyalVassals(eq(VASSAL_QTY), anyBoolean());
 }

 // Test cases for other methods like castleFiefdom, castlePersonal, mercenary, etc. would look similar
 // to the structure above, but with different arguments and expected results.

 private Knight anotherPlayer() {
 final Knight anotherPlayer = mock(Knight.class);
 when(currentPlayers.get(anyString())).thenReturn(anotherPlayer);
 return anotherPlayer;
 }
}