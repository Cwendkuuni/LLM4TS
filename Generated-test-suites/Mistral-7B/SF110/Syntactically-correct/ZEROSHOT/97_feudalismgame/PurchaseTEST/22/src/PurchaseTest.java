package src.test.java;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms fiefdomMock;
 private Map mapMock;
 private CurrentPlayers currentPlayersMock;
 private Knight buyerMock;
 private Knight sellerMock;

 @Before
 public void setUp() {
 fiefdomMock = Mockito.mock(Fiefdoms.class);
 mapMock = Mockito.mock(Map.class);
 currentPlayersMock = Mockito.mock(CurrentPlayers.class);
 buyerMock = Mockito.mock(Knight.class);
 sellerMock = Mockito.mock(Knight.class);

 purchase = new Purchase();
 purchase.setActions(new Actions() {
 @Override
 public void perform(Collection args) {
 // empty implementation
 }
 });

 Mockito.when(mapMock.get(ArgumentMatchers.anyString())).thenReturn(fiefdomMock);
 Mockito.when(currentPlayersMock.get(ArgumentMatchers.anyString())).thenReturn(buyerMock);
 Mockito.when(currentPlayersMock.currentPlayers).thenReturn(new HashMap<>(){
 {
 put("player1", buyerMock);
 put("player2", sellerMock);
 }
 });

 purchase.setMap(mapMock);
 purchase.setCurrentPlayers(currentPlayersMock);
 }

 @Test
 public void testVassal() {
 final Iterator<String> args = Arrays.asList("10", "player1", "fiefdom1").iterator();
 purchase.vassal(args);

 assertEquals("player1", fiefdomMock.getOwner());
 assertEquals(10, fiefdomMock.getLoyalVassals());
 }

 @Test
 public void testVassal_InvalidFiefdom() {
 final Iterator<String> args = Arrays.asList("10", "player1", "unknownFiefdom").iterator();
 purchase.vassal(args);

 assertEquals(0, fiefdomMock.getLoyalVassals());
 assertEquals("Fiefdom doesn't belong to the kingdom", System.out.getMessage());
 }

 @Test
 public void testVassal_InsufficientFunds() {
 final Iterator<String> args = Arrays.asList("20", "player1", "fiefdom1").iterator();
 Mockito.when(buyerMock.getTotalMoney()).thenReturn(10);

 purchase.vassal(args);

 assertEquals(0, fiefdomMock.getLoyalVassals());
 assertEquals("You cannot buy more vassals, you have insufficient funds", System.out.getMessage());
 }

 @Test
 public void testVassal_BuyersRankIsKingOrFiefdomIsAlreadyMine() {
 final Iterator<String> args = Arrays.asList("5", "player1", "fiefdom1").iterator();
 Mockito.when(buyerMock.getRank()).thenReturn("king");
 Mockito.when(buyerMock.isFiefdomMine(fiefdomMock.getName())).thenReturn(true);

 purchase.vassal(args);

 assertEquals(5, fiefdomMock.getLoyalVassals());
 }

 @Test
 public void testVassal_BuyersRankIsCountOrDuke() {
 final Iterator<String> args = Arrays.asList("5", "player1", "fiefdom1").iterator();
 Mockito.when(buyerMock.getRank()).thenReturn("count");

 purchase.vassal(args);

 assertEquals(0, fiefdomMock.getLoyalVassals());
 assertEquals("You can only buy a castle for a fiefdom that belongs to you", System.out.getMessage());
 }

 @Test
 public void testCastleFiefdom() {
 final Iterator<String> args = Arrays.asList("5", "player1", "fiefdom1").iterator();
 purchase.castleFiefdom(args);

 assertEquals("player1", fiefdomMock.getOwner());
 assertEquals(5, fiefdomMock.getTotalCastles());
 }

 @Test
 public void testCastleFiefdom_InvalidFiefdom() {
 final Iterator<String> args = Arrays.asList("5", "player1", "unknownFiefdom").iterator();
 purchase.castleFiefdom(args);

 assertEquals(0, fiefdomMock.getTotalCastles());
 assertEquals("Fiefdom doesn't belong to the kingdom", System.out.getMessage());
 }

 @Test
 public void testCastleFiefdom_InsufficientFunds() {
 final Iterator<String> args = Arrays.asList("25", "player1", "fiefdom1").iterator();
 Mockito.when(buyerMock.getTotalMoney()).thenReturn(15);

 purchase.castleFiefdom(args);

 assertEquals(0, fiefdomMock.getTotalCastles());
 assertEquals("You cannot buy more Fiefdom castles, you have insufficient funds", System.out.getMessage());
 }

 @Test
 public void testCastleFiefdom_FiefdomIsAlreadyMine() {
 final Iterator<String> args = Arrays.asList("5", "player1", "fiefdom1").iterator();
 Mockito.when(buyerMock.getRank()).thenReturn("king");
 Mockito.when(buyerMock.isFiefdomMine(fiefdomMock.getName())).thenReturn(true);

 purchase.castleFiefdom(args);

 assertEquals(5, fiefdomMock.getTotalCastles());
 }

 @Test
 public void testCastleFiefdom_FiefdomIsOwnedByCountOrDuke() {
 final Iterator<String> args = Arrays.asList("5", "player1", "fiefdom1").iterator();
 Mockito.when(buyerMock.getRank()).thenReturn("count");

 purchase.castleFiefdom(args);

 assertEquals(0, fiefdomMock.getTotalCastles());
 assertEquals("You can only buy a castle for a fiefdom that belongs to you", System.out.getMessage());
 }

 @Test
 public void testCastlePersonal() {
 final Iterator<String> args = Arrays.asList("5", "player1", "fiefdom1").iterator();
 purchase.castlePersonal(args);

 assertEquals("player1", fiefdomMock.getOwner());
 assertEquals(5, fiefdomMock.getPersonalCastles());
 }

 @Test
 public void testCastlePersonal_InvalidFiefdom() {
 final Iterator<String> args = Arrays.asList("5", "player1", "unknownFiefdom").iterator();
 purchase.castlePersonal(args);

 assertEquals(0, fiefdomMock.getPersonalCastles());
 assertEquals("Fiefdom doesn't belong to the kingdom", System.out.getMessage());
 }

 @Test
 public void testCastlePersonal_InsufficientFunds() {
 final Iterator<String> args = Arrays.asList("15", "player1", "fiefdom1").iterator();
 Mockito.when(buyerMock.getTotalMoney()).thenReturn(10);

 purchase.castlePersonal(args);

 assertEquals(0, fiefdomMock.getPersonalCastles());
 assertEquals("You cannot buy more Personal castle, you have insufficient funds", System.out.getMessage());
 }

 @Test
 public void testCastlePersonal_FiefdomIsAlreadyMine() {
 final Iterator<String> args = Arrays.asList("5", "player1", "fiefdom1").iterator();
 Mockito.when(buyerMock.getRank()).thenReturn("king");
 Mockito.when(buyerMock.isFiefdomMine(fiefdomMock.getName())).thenReturn(true);

 purchase.castlePersonal(args);

 assertEquals(5, fiefdomMock.getPersonalCastles());
 }

 @Test
 public void testCastlePersonal_FiefdomIsOwnedByKing() {
 final Iterator<String> args = Arrays.asList("5", "player1", "fiefdom1").iterator();
 Mockito.when(buyerMock.getRank()).thenReturn("count");
 Mockito.when(currentPlayersMock.get(fiefdomMock.getOwner().getName())).thenReturn(Mockito.mock(Knight.class));

 purchase.castlePersonal(args);

 assertEquals(0, fiefdomMock.getPersonalCastles());
 assertEquals("You can only buy a castle for a fiefdom that belongs to you", System.out.getMessage());
 }

 @Test
 public void testMercenary() {
 final Iterator<String> args = Arrays.asList("5", "player1", "knight1").iterator();
 purchase.mercenary(args);

 assertEquals(5, sellerMock.getMercenaries());
 }

 @Test
 public void testMercenary_InexistentKnight() {
 final Iterator<String> args = Arrays.asList("5", "player1", "knightX").iterator();
 purchase.mercenary(args);

 assertEquals(0, sellerMock.getMercenaries());
 assertEquals("Inexistent Knight", System.out.getMessage());
 }

 @Test
 public void testMercenary_BuyerIsTheSameKnight() {
 final Iterator<String> args = Arrays.asList("5", "knight1", "knight1").iterator();
 purchase.mercenary(args);

 assertEquals(5, buyerMock.getMercenaries());
 }

 @Test
 public void testMercenary_BuyerIsKing() {
 final Iterator<String> args = Arrays.asList("5", "king", "knight1").iterator();
 purchase.mercenary(args);

 assertEquals(5, sellerMock.getMercenaries());
 }

 @Test
 public void testMercenary_InsufficientFunds() {
 final Iterator<String> args = Arrays.asList("10", "player1", "knight1").iterator();
 Mockito.when(buyerMock.getTotalMoney()).thenReturn(5);

 purchase.mercenary(args);

 assertEquals(0, sellerMock.getMercenaries());
 assertEquals("You cannot buy more mercenaries, you have insufficient funds", System.out.getMessage());
 }
}