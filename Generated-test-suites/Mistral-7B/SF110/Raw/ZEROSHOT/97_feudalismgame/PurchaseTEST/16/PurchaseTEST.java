package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;
import org.mockito.Mockito;

import feudalism.Fiefdoms;
import feudalism.GameAutoActions;
import feudalism.Knight;
import feudalism.Map;
import feudalism.Purchase;
import feudalism.CurrentPlayers;

public class PurchaseTest {

 private Purchase purchase;
 private Map map;
 private CurrentPlayers currentPlayers;
 private GameAutoActions gameAutoActions;

 @Before
 public void setUp() {
 map = Mockito.mock(Map.class);
 currentPlayers = Mockito.mock(CurrentPlayers.class);
 gameAutoActions = Mockito.mock(GameAutoActions.class);
 purchase = new Purchase(map, currentPlayers, gameAutoActions);
 }

 @Test
 public void testPerform_ValidArguments_InvokesCorrectMethod() throws Exception {
 // Given: A valid iterator with "vassal" command and valid arguments
 Iterator args = Arrays.asList("vassal", Arrays.asList(1, "player1", "fiefdom1")).iterator();
 // When: Perform method is called
 purchase.perform(Arrays.asList(args));
 // Then: The correct method (vassal) is invoked with the correct arguments
 verify(purchase).vassal(args);
 }

 @Test
 public void testPerform_InvalidArguments_ThrowsException() throws Exception {
 // Given: An invalid iterator
 Iterator args = Arrays.asList("invalidCommand", Arrays.asList("arg1", "arg2")).iterator();
 // When: Perform method is called
 try {
 purchase.perform(Arrays.asList(args));
 fail("Expected SecurityException not thrown");
 } catch (SecurityException e) {
 // Then: The expected SecurityException is thrown
 }
 }

 @Test
 public void testVassal_ValidArguments_BuysVassals() {
 // Given: A valid knight with sufficient funds
 Knight buyer = new Knight("player1", "knight", 1000);
 Fiefdoms fiefdom = new Fiefdoms("fiefdom1", "owner1");
 map.put("fiefdom1", fiefdom);
 CurrentPlayers.currentPlayers.put("player1", buyer);
 // When: Vassal method is called with valid arguments
 purchase.vassal(Arrays.asList(1, "player1", "fiefdom1").iterator());
 // Then: The expected number of vassals is bought
 assertEquals(1, fiefdom.getLoyalVassals());
 // And: The knight's total money is decreased
 assertEquals(950, buyer.getTotalMoney());
 }

 @Test
 public void testVassal_InvalidArguments_ThrowsException() {
 // Given: An invalid fiefdom
 Fiefdoms invalidFiefdom = new Fiefdoms("invalidFiefdom", null);
 map.put("invalidFiefdom", invalidFiefdom);
 // When: Vassal method is called with invalid arguments
 try {
 purchase.vassal(Arrays.asList(1, "player1", "invalidFiefdom").iterator());
 fail("Expected IllegalArgumentException not thrown");
 } catch (IllegalArgumentException e) {
 // Then: The expected IllegalArgumentException is thrown
 }
 }

 @Test
 public void testVassal_InsufficientFunds_ThrowsException() {
 // Given: A valid knight with insufficient funds
 Knight buyer = new Knight("player1", "knight", 500);
 Fiefdoms fiefdom = new Fiefdoms("fiefdom1", "owner1");
 map.put("fiefdom1", fiefdom);
 CurrentPlayers.currentPlayers.put("player1", buyer);
 // When: Vassal method is called with valid arguments
 try {
 purchase.vassal(Arrays.asList(2, "player1", "fiefdom1").iterator());
 fail("Expected IllegalArgumentException not thrown");
 } catch (IllegalArgumentException e) {
 // Then: The expected IllegalArgumentException is thrown
 }
 }

 @Test
 public void testCastleFiefdom_ValidArguments_BuysCastles() {
 // Given: A valid king with sufficient funds
 Knight buyer = new Knight("player1", "king", 5000);
 Fiefdoms fiefdom = new Fiefdoms("fiefdom1", "owner1");
 map.put("fiefdom1", fiefdom);
 CurrentPlayers.currentPlayers.put("player1", buyer);
 // When: CastleFiefdom method is called with valid arguments
 purchase.castleFiefdom(Arrays.asList(2, "player1", "fiefdom1").iterator());
 // Then: The expected number of castles is bought
 assertEquals(2, fiefdom.getCastles().size());
 // And: The king's total money is decreased
 assertEquals(4950, buyer.getTotalMoney());
 }

 @Test
 public void testCastleFiefdom_InvalidArguments_ThrowsException() {
 // Given: An invalid fiefdom
 Fiefdoms invalidFiefdom = new Fiefdoms("invalidFiefdom", null);
 map.put("invalidFiefdom", invalidFiefdom);
 // When: CastleFiefdom method is called with invalid arguments
 try {
 purchase.castleFiefdom(Arrays.asList(2, "player1", "invalidFiefdom").iterator());
 fail("Expected IllegalArgumentException not thrown");
 } catch (IllegalArgumentException e) {
 // Then: The expected IllegalArgumentException is thrown
 }
 }

 @Test
 public void testCastlePersonal_ValidArguments_BuysCastles() {
 // Given: A valid king with sufficient funds
 Knight buyer = new Knight("player1", "king", 5000);
 Fiefdoms fiefdom = new Fiefdoms("fiefdom1", "owner1");
 map.put("fiefdom1", fiefdom);
 CurrentPlayers.currentPlayers.put("player1", buyer);
 // When: CastlePersonal method is called with valid arguments
 purchase.castlePersonal(Arrays.asList(2, "player1", "fiefdom1").iterator());
 // Then: The expected number of castles is bought
 assertEquals(2, buyer.getCastles().size());
 // And: The king's total money is decreased
 assertEquals(4950, buyer.getTotalMoney());
 }

 @Test
 public void testCastlePersonal_InvalidArguments_ThrowsException() {
 // Given: An invalid fiefdom
 Fiefdoms invalidFiefdom = new Fiefdoms("invalidFiefdom", null);
 map.put("invalidFiefdom", invalidFiefdom);
 // When: CastlePersonal method is called with invalid arguments
 try {
 purchase.castlePersonal(Arrays.asList(2, "player1", "invalidFiefdom").iterator());
 fail("Expected IllegalArgumentException not thrown");
 } catch (IllegalArgumentException e) {
 // Then: The expected IllegalArgumentException is thrown
 }
 }

 @Test
 public void testMercenary_ValidArguments_BuysMercenaries() {
 // Given: A valid king with sufficient funds
 Knight buyer = new Knight("player1", "king", 5000);
 Knight knight = new Knight("knight1", "knight", 10);
 CurrentPlayers.currentPlayers.put("player1", buyer);
 CurrentPlayers.currentPlayers.put("knight1", knight);
 // When: Mercenary method is called with valid arguments
 purchase.mercenary(Arrays.asList(3, "player1", "knight1").iterator());
 // Then: The expected number of mercenaries is bought
 assertEquals(3, knight.getMercenaries());
 // And: The king's total money is decreased
 assertEquals(4950, buyer.getTotalMoney());
 }

 @Test
 public void testMercenary_InvalidArguments_ThrowsException() {
 // Given: An invalid knight
 Knight invalidKnight = new Knight("invalidKnight", "knight", 10);
 CurrentPlayers.currentPlayers.put("invalidKnight", invalidKnight);
 // When: Mercenary method is called with invalid arguments
 try {
 purchase.mercenary(Arrays.asList(3, "player1", "invalidKnight").iterator());
 fail("Expected IllegalArgumentException not thrown");
 } catch (IllegalArgumentException e) {
 // Then: The expected IllegalArgumentException is thrown
 }
 }

 @Test
 public void testMercenary_InsufficientFunds_ThrowsException() {
 // Given: A valid king with insufficient funds
 Knight buyer = new Knight("player1", "king", 500);
 Knight knight = new Knight("knight1", "knight", 10);
 CurrentPlayers.currentPlayers.put("player1", buyer);
 CurrentPlayers.currentPlayers.put("knight1", knight);
 // When: Mercenary method is called with valid arguments
 try {
 purchase.mercenary(Arrays.asList(3, "player1", "knight1").iterator());
 fail("Expected IllegalArgumentException not thrown");
 } catch (IllegalArgumentException e) {
 // Then: The expected IllegalArgumentException is thrown
 }
 }
}