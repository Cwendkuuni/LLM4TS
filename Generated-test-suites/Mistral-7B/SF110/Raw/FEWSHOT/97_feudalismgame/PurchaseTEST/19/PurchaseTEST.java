package src;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class PurchaseTest {

 private static final String KING_NAME = "King";
 private static final String COUNT_NAME = "Count";
 private static final String DUKE_NAME = "Duke";
 private static final String FIEFDOM_NAME = "Fiefdom";
 private static final String BUYER_NAME = "Buyer";
 private static final int QTY = 3;
 private static final int CASTLE_PRICE = 25;
 private static final int PERSONAL_CASTLE_PRICE = 10;
 private static final int MERCENARY_PRICE = 1;
 private static final String FIEFDOM_OWNER = "Owner";
 private static final Fiefdoms MOCK_FIEFDOMS = mock(Fiefdoms.class);
 private static final Map<String, Fiefdoms> MOCK_MAP = mock(Map.class);
 private static final CurrentPlayers MOCK_CURRENT_PLAYERS = mock(CurrentPlayers.class);
 private static final Knight MOCK_KNIGHT = mock(Knight.class);
 private static final Purchase sut = new Purchase();

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 when(MOCK_FIEFDOMS.getOwner()).thenReturn(FIEFDOM_OWNER);
 when(MOCK_MAP.get(FIEFDOM_NAME)).thenReturn(MOCK_FIEFDOMS);
 when(MOCK_CURRENT_PLAYERS.get(BUYER_NAME)).thenReturn(MOCK_KNIGHT);
 when(MOCK_CURRENT_PLAYERS.get(FIEFDOM_OWNER)).thenReturn(mock(Knight.class));
 }

 @Test
 public void testPerform_WithValidArguments_InvokesCorrectMethod() {
 Iterator<String> args = Arrays.asList("vassal", FIEFDOM_NAME, BUYER_NAME, "1", "Buyer").iterator();
 sut.perform(Arrays.asList(args));
 verify(MOCK_FIEFDOMS).getOwner();
 verify(MOCK_KNIGHT).setTotalMoney(1, false);
 verify(MOCK_FIEFDOMS).setLoyalVassals(1, true);
 }

 @Test
 public void testPerform_WithInvalidArguments_PrintsErrorMessage() {
 Iterator<String> args = Arrays.asList("invalidMethod", FIEFDOM_NAME, BUYER_NAME, "1", "Buyer").iterator();
 sut.perform(Arrays.asList(args));
 verifyNoMoreInteractions(MOCK_FIEFDOMS);
 verifyNoMoreInteractions(MOCK_KNIGHT);
 verify(System.out).println("Inexistent method: invalidMethod");
 }

 @Test
 public void testVassal_WithValidArguments_BuysVassals() {
 Iterator<String> args = Arrays.asList("vassal", FIEFDOM_NAME.iterator(), "1", BUYER_NAME, "Buyer").iterator();
 sut.perform(Arrays.asList(args));
 verify(MOCK_FIEFDOMS).getOwner();
 verify(MOCK_KNIGHT).setTotalMoney(1, false);
 verify(MOCK_FIEFDOMS).setLoyalVassals(1, true);
 }

 @Test
 public void testVassal_WithInvalidArguments_PrintsErrorMessage() {
 Iterator<String> args = Arrays.asList("vassal", FIEFDOM_NAME.iterator(), null, "1", "Buyer").iterator();
 sut.perform(Arrays.asList(args));
 verifyNoMoreInteractions(MOCK_FIEFDOMS);
 verifyNoMoreInteractions(MOCK_KNIGHT);
 verify(System.out).println("Fiefdom doesn't belong to the kingdom");
 }

 @Test
 public void testCastleFiefdom_WithValidArguments_BuysFiefdomCastles() {
 Iterator<String> args = Arrays.asList("castleFiefdom", FIEFDOM_NAME.iterator(), "1", BUYER_NAME, "Buyer").iterator();
 sut.perform(Arrays.asList(args));
 verify(MOCK_FIEFDOMS).getOwner();
 verify(MOCK_KNIGHT).setTotalMoney(CASTLE_PRICE, false);
 verify(MOCK_FIEFDOMS).buyFiefdomCastles(1);
 }

 @Test
 public void testCastleFiefdom_WithInsufficientFunds_PrintsErrorMessage() {
 Iterator<String> args = Arrays.asList("castleFiefdom", FIEFDOM_NAME.iterator(), "1", BUYER_NAME, "Buyer").iterator();
 doReturn(false).when(MOCK_KNIGHT).setTotalMoney(CASTLE_PRICE, false);
 sut.perform(Arrays.asList(args));
 verify(System.out).println("You cannot buy more Fiefdom castles, you have insufficient funds");
 }

 @Test
 public void testCastlePersonal_WithValidArguments_BuysPersonalCastles() {
 Iterator<String> args = Arrays.asList("castlePersonal", FIEFDOM_NAME.iterator(), "1", BUYER_NAME, "Buyer").iterator();
 sut.perform(Arrays.asList(args));
 verify(MOCK_FIEFDOMS).getOwner();
 verify(MOCK_KNIGHT).setTotalMoney(PERSONAL_CASTLE_PRICE, false);
 verify(MOCK_FIEFDOMS).buyPersonalCastles(1);
 }

 @Test
 public void testCastlePersonal_WithInsufficientFunds_PrintsErrorMessage() {
 Iterator<String> args = Arrays.asList("castlePersonal", FIEFDOM_NAME.iterator(), "1", BUYER_NAME, "Buyer").iterator();
 doReturn(false).when(MOCK_KNIGHT).setTotalMoney(PERSONAL_CASTLE_PRICE, false);
 sut.perform(Arrays.asList(args));
 verify(System.out).println("You cannot buy more Personal castle, you have insufficient funds");
 }

 @Test
 public void testMercenary_WithValidArguments_BuysMercenaries() {
 Iterator<String> args = Arrays.asList("mercenary", FIEFDOM_NAME.iterator(), BUYER_NAME, "1", "Knight").iterator();
 sut.perform(Arrays.asList(args));
 verify(MOCK_KNIGHT).addMercenaries(1);
 }

 @Test
 public void testMercenary_WithInvalidArguments_PrintsErrorMessage() {
 Iterator<String> args = Arrays.asList("mercenary", FIEFDOM_NAME.iterator(), null, "1", BUYER_NAME).iterator();
 sut.perform(Arrays.asList(args));
 verifyNoMoreInteractions(MOCK_KNIGHT);
 verify(System.out).println("Inexistent Knight");
 }

 @Test
 public void testMercenary_WithBuyerIsKing_BuysMercenaries() {
 Iterator<String> args = Arrays.asList("mercenary", FIEFDOM_NAME.iterator(), BUYER_NAME, "1", "King").iterator();
 sut.perform(Arrays.asList(args));
 verify(MOCK_KNIGHT).addMercenaries(1);
 }
}