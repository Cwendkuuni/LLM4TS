package src;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class PurchaseTest {

 private Purchase purchase;
 private Iterator<String> argIterator;
 private Fiefdoms fiefdom;
 private Knight buyerObj;
 private Knight knight;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 purchase = new Purchase();
 argIterator = mock(Iterator.class);
 fiefdom = mock(Fiefdoms.class);
 buyerObj = mock(Knight.class);
 knight = mock(Knight.class);
 }

 @Test
 public void testVassal() {
 // Given
 when(argIterator.next()).thenReturn("3").thenReturn("John").thenReturn("France").thenReturn(fiefdom);
 when(CurrentPlayers.currentPlayers.get(arg.next())).thenReturn(buyerObj);
 when(Map.map.get(arg.next())).thenReturn(fiefdom);
 // When
 purchase.vassal(argIterator);
 // Then
 verify(buyerObj, times(1)).setTotalMoney(3, false);
 verify(fiefdom, times(1)).setLoyalVassals(3, true);
 }

 @Test
 public void testVassal_InsufficientFunds() {
 // Given
 when(argIterator.next()).thenReturn("3").thenReturn("John").thenReturn("France").thenReturn(fiefdom);
 when(CurrentPlayers.currentPlayers.get(arg.next())).thenReturn(buyerObj);
 when(Map.map.get(arg.next())).thenReturn(fiefdom);
 when(buyerObj.getTotalMoney()).thenReturn(0);
 // When
 purchase.vassal(argIterator);
 // Then
 verify(console, times(1)).println("You cannot buy more vassals, you have insufficient funds");
 }

 @Test
 public void testVassal_FiefdomNotBelongsToKingdom() {
 // Given
 when(argIterator.next()).thenReturn("3").thenReturn("John").thenReturn("England").thenReturn(fiefdom);
 // When
 purchase.vassal(argIterator);
 // Then
 verify(console, times(1)).println("Fiefdom doesn't belong to the kingdom: England owner: null");
 }

 @Test
 public void testVassal_BuyerIsKing() {
 // Given
 when(argIterator.next()).thenReturn("3").thenReturn("John").thenReturn("France").thenReturn(fiefdom);
 when(CurrentPlayers.currentPlayers.get(arg.next())).thenReturn(new Knight("John", "king", 10000));
 when(Map.map.get(arg.next())).thenReturn(fiefdom);
 // When
 purchase.vassal(argIterator);
 // Then
 verify(buyerObj, times(1)).setTotalMoney(-3, false);
 // The total money should have been decreased by 3 * 100 for vassals
 // But since the perform method catches the exception, it's not possible to test this directly
 }

 @Test
 public void testVassal_BuyerIsCount() {
 // Given
 when(argIterator.next()).thenReturn("3").thenReturn("John").thenReturn("France").thenReturn(fiefdom);
 when(CurrentPlayers.currentPlayers.get(arg.next())).thenReturn(new Knight("John", "count", 1000));
 when(Map.map.get(arg.next())).thenReturn(fiefdom);
 // When
 purchase.vassal(argIterator);
 // Then
 verify(console, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
 }

 // Similar test cases can be written for other methods in the class

}