import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms fiefdom;
 private Knight buyer, anotherKnight, king;

 @Before
 public void setup() {
 purchase = new Purchase();
 fiefdom = Mockito.mock(Fiefdoms.class);
 buyer = Mockito.mock(Knight.class);
 anotherKnight = Mockito.mock(Knight.class);
 king = Mockito.mock(Knight.class);

 Mockito.when(fiefdom.getName()).thenReturn("testFiefdom");
 Mockito.when(fiefdom.getOwner()).thenReturn(king);
 Mockito.when(fiefdom.getViking().size()).thenReturn(0);
 Mockito.when(fiefdom.getMagyar().size()).thenReturn(0);
 Mockito.when(fiefdom.getMuslim().size()).thenReturn(0);
 }

 @Test
 public void testVassal() {
 Iterator<String> args = Arrays.asList("10", "buyer1", "testFiefdom").iterator();
 purchase.vassal(args);
 assertEquals(10, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalInsufficientFunds() {
 Iterator<String> args = Arrays.asList("11", "buyer1", "testFiefdom").iterator();
 purchase.vassal(args);
 assertEquals(0, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalNotOwnedByUser() {
 Iterator<String> args = Arrays.asList("1", "buyer1", "anotherTestFiefdom").iterator();
 purchase.vassal(args);
 assertEquals(0, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalUserIsKing() {
 Iterator<String> args = Arrays.asList("11", "king1", "testFiefdom").iterator();
 purchase.vassal(args);
 assertEquals(11, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalUserIsCountOrDuke() {
 Iterator<String> args = Arrays.asList("11", "count1", "testFiefdom").iterator();
 purchase.vassal(args);
 assertEquals(0, fiefdom.getLoyalVassals());
 }

 @Test
 public void testCastleFiefdom() {
 Iterator<String> args = Arrays.asList("5", "buyer1", "testFiefdom").iterator();
 purchase.castleFiefdom(args);
 assertEquals(5, fiefdom.getCastles().size());
 }

 @Test
 public void testCastleFiefdomInsufficientFunds() {
 Iterator<String> args = Arrays.asList("6", "buyer1", "testFiefdom").iterator();
 purchase.castleFiefdom(args);
 assertEquals(5, fiefdom.getCastles().size());
 }

 @Test
 public void testCastleFiefdomNotOwnedByUser() {
 Iterator<String> args = Arrays.asList("1", "buyer1", "anotherTestFiefdom").iterator();
 purchase.castleFiefdom(args);
 assertEquals(5, fiefdom.getCastles().size());
 }

 @Test
 public void testCastleFiefdomOwnerIsKing() {
 Iterator<String> args = Arrays.asList("6", "king1", "testFiefdom").iterator();
 purchase.castleFiefdom(args);
 assertEquals(11, fiefdom.getCastles().size());
 }

 @Test
 public void testCastleFiefdomOwnerIsCountOrDuke() {
 Iterator<String> args = Arrays.asList("6", "count1", "testFiefdom").iterator();
 purchase.castleFiefdom(args);
 assertEquals(5, fiefdom.getCastles().size());
 }

 @Test
 public void testCastlePersonal() {
 Iterator<String> args = Arrays.asList("3", "buyer1", "testFiefdom").iterator();
 purchase.castlePersonal(args);
 assertEquals(3, buyer.getPersonalCastles().size());
 }

 @Test
 public void testCastlePersonalInsufficientFunds() {
 Iterator<String> args = Arrays.asList("4", "buyer1", "testFiefdom").iterator();
 purchase.castlePersonal(args);
 assertEquals(3, buyer.getPersonalCastles().size());
 }

 @Test
 public void testCastlePersonalNotOwnedByUser() {
 Iterator<String> args = Arrays.asList("1", "buyer1", "anotherTestFiefdom").iterator();
 purchase.castlePersonal(args);
 assertEquals(3, buyer.getPersonalCastles().size());
 }

 @Test
 public void testCastlePersonalUserIsKing() {
 Iterator<String> args = Arrays.asList("3", "king1", "testFiefdom").iterator();
 purchase.castlePersonal(args);
 assertEquals(6, buyer.getPersonalCastles().size());
 }

 @Test
 public void testCastlePersonalUserIsCountOrDuke() {
 Iterator<String> args = Arrays.asList("3", "count1", "testFiefdom").iterator();
 purchase.castlePersonal(args);
 assertEquals(3, buyer.getPersonalCastles().size());
 }

 @Test
 public void testMercenary() {
 Iterator<String> args = Arrays.asList("2", "buyer1", "anotherKnight1").iterator();
 purchase.mercenary(args);
 assertEquals(2, anotherKnight.getMercenaries());
 }

 @Test
 public void testMercenaryInsufficientFunds() {
 Iterator<String> args = Arrays.asList("3", "buyer1", "anotherKnight1").iterator();
 purchase.mercenary(args);
 assertEquals(2, anotherKnight.getMercenaries());
 }

 @Test
 public void testMercenaryInexistentKnight() {
 Iterator<String> args = Arrays.asList("1", "buyer1", "inexistentKnight").iterator();
 purchase.mercenary(args);
 assertEquals(2, anotherKnight.getMercenaries());
 }

 @Test
 public void testMercenaryUserIsKing() {
 Iterator<String> args = Arrays.asList("3", "king1", "anotherKnight1").iterator();
 purchase.mercenary(args);
 assertEquals(3, anotherKnight.getMercenaries());
 }
}