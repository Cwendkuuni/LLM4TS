package src.test.java;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import java.util.*;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms fiefdom;
 private Knight buyer1, buyer2, knight;
 private CurrentPlayers currentPlayers;

 @Before
 public void setUp() {
 purchase = new Purchase();
 fiefdom = mock(Fiefdoms.class);
 buyer1 = mock(Knight.class);
 buyer2 = mock(Knight.class);
 knight = mock(Knight.class);
 currentPlayers = mock(CurrentPlayers.class);

 when(currentPlayers.get(eq(buyer1.getName()))).thenReturn(buyer1);
 when(currentPlayers.get(eq(buyer2.getName()))).thenReturn(buyer2);
 when(currentPlayers.get(eq(knight.getName()))).thenReturn(knight);
 when(Map.map.get(anyString())).thenReturn(fiefdom);
 }

 @Test
 public void testVassal() {
 Iterator<String> argIterator = Arrays.asList("1", "buyer1", "fiefdom1").iterator();
 when(argIterator.next()).thenReturn("1");
 when(argIterator.next()).thenReturn("buyer1");
 when(argIterator.next()).thenReturn("fiefdom1");
 purchase.vassal(argIterator);

 verify(fiefdom, times(1)).setLoyalVassals(1, true);
 verify(buyer1, times(1)).setTotalMoney(-1, false);
 }

 @Test
 public void testVassalNoFunds() {
 Iterator<String> argIterator = Arrays.asList("1", "buyer1", "fiefdom1").iterator();
 when(argIterator.next()).thenReturn("1");
 when(argIterator.next()).thenReturn("buyer1");
 when(argIterator.next()).thenReturn("fiefdom1");
 when(buyer1.getTotalMoney()).thenReturn(0);

 purchase.vassal(argIterator);

 verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
 }

 @Test
 public void testVassalNotOwnedFiefdom() {
 Iterator<String> argIterator = Arrays.asList("1", "buyer1", "notOwnedFiefdom").iterator();
 purchase.vassal(argIterator);

 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
 }

 @Test
 public void testVassalBuyersOwnFiefdom() {
 Iterator<String> argIterator = Arrays.asList("1", "buyer1", "buyer1Fiefdom").iterator();
 when(argIterator.next()).thenReturn("1");
 when(argIterator.next()).thenReturn("buyer1");
 when(argIterator.next()).thenReturn("buyer1Fiefdom");
 when(buyer1.getRank()).thenReturn("king");
 when(buyer1.isFiefdomMine("buyer1Fiefdom")).thenReturn(true);

 purchase.vassal(argIterator);

 verify(fiefdom, times(1)).setLoyalVassals(1, true);
 verify(buyer1, times(1)).setTotalMoney(-1, false);
 }

 @Test
 public void testVassalCountOrDukeFiefdom() {
 Iterator<String> argIterator = Arrays.asList("1", "buyer1", "buyer1Fiefdom").iterator();
 when(argIterator.next()).thenReturn("1");
 when(argIterator.next()).thenReturn("buyer1");
 when(argIterator.next()).thenReturn("buyer1Fiefdom");
 when(buyer1.getRank()).thenReturn("knight");
 when(currentPlayers.get(fiefdom.getOwner()).getRank()).thenReturn("count");

 purchase.vassal(argIterator);

 verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
 }

 @Test
 public void testCastleFiefdom() {
 Iterator<String> argIterator = Arrays.asList("2", "buyer1", "fiefdom1").iterator();
 when(argIterator.next()).thenReturn("2");
 when(argIterator.next()).thenReturn("buyer1");
 when(argIterator.next()).thenReturn("fiefdom1");
 purchase.castleFiefdom(argIterator);

 verify(fiefdom, times(1)).buyFiefdomCastles(2);
 verify(buyer1, times(1)).setTotalMoney(-50, false);
 }

 @Test
 public void testCastleFiefdomNoFunds() {
 Iterator<String> argIterator = Arrays.asList("2", "buyer1", "fiefdom1").iterator();
 when(argIterator.next()).thenReturn("2");
 when(argIterator.next()).thenReturn("buyer1");
 when(argIterator.next()).thenReturn("fiefdom1");
 when(buyer1.getTotalMoney()).thenReturn(0);

 purchase.castleFiefdom(argIterator);

 verify(System.out, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
 }

 @Test
 public void testCastleFiefdomNotOwnedFiefdom() {
 Iterator<String> argIterator = Arrays.asList("2", "buyer1", "notOwnedFiefdom").iterator();
 purchase.castleFiefdom(argIterator);

 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
 }

 @Test
 public void testCastleFiefdomKingBuysFromCountOrDuke() {
 Iterator<String> argIterator = Arrays.asList("2", "buyer1", "buyer1Fiefdom").iterator();
 when(argIterator.next()).thenReturn("2");
 when(argIterator.next()).thenReturn("buyer1");
 when(argIterator.next()).thenReturn("buyer1Fiefdom");
 when(buyer1.getRank()).thenReturn("king");
 when(currentPlayers.get(fiefdom.getOwner()).getRank()).thenReturn("count");

 purchase.castleFiefdom(argIterator);

 verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
 }

 @Test
 public void testCastleFiefdomValid() {
 Iterator<String> argIterator = Arrays.asList("2", "buyer1", "buyer1Fiefdom").iterator();
 when(argIterator.next()).thenReturn("2");
 when(argIterator.next()).thenReturn("buyer1");
 when(argIterator.next()).thenReturn("buyer1Fiefdom");
 when(buyer1.getRank()).thenReturn("king");
 when(buyer1.isFiefdomMine("buyer1Fiefdom")).thenReturn(true);
 when(fiefdom.getViking().size()).thenReturn(0);
 when(fiefdom.getMagyar().size()).thenReturn(0);
 when(fiefdom.getMuslim().size()).thenReturn(0);

 purchase.castleFiefdom(argIterator);

 verify(fiefdom, times(1)).buyFiefdomCastles(2);
 verify(buyer1, times(1)).setTotalMoney(-50, false);
 }

 @Test
 public void testCastlePersonal() {
 Iterator<String> argIterator = Arrays.asList("1", "buyer1", "fiefdom1").iterator();
 when(argIterator.next()).thenReturn("1");
 when(argIterator.next()).thenReturn("buyer1");
 when(argIterator.next()).thenReturn("fiefdom1");
 purchase.castlePersonal(argIterator);

 verify(fiefdom, times(1)).buyPersonalCastles(1);
 verify(buyer1, times(1)).setTotalMoney(-10, false);
 }

 @Test
 public void testCastlePersonalNoFunds() {
 Iterator<String> argIterator = Arrays.asList("1", "buyer1", "fiefdom1").iterator();
 when(argIterator.next()).thenReturn("1");
 when(argIterator.next()).thenReturn("buyer1");
 when(argIterator.next()).thenReturn("fiefdom1");
 when(buyer1.getTotalMoney()).thenReturn(0);

 purchase.castlePersonal(argIterator);

 verify(System.out, times(1)).println("You cannot buy more Personal castle, you have insufficient funds");
 }

 @Test
 public void testCastlePersonalNotKing() {
 Iterator<String> argIterator = Arrays.asList("1", "buyer1", "fiefdom1").iterator();
 when(argIterator.next()).thenReturn("1");
 when(argIterator.next()).thenReturn("buyer1");
 when(argIterator.next()).thenReturn("fiefdom1");
 when(buyer1.getRank()).thenReturn("count");

 purchase.castlePersonal(argIterator);

 verify(System.out, times(1)).println("You cannot buy a Personal Castle for Counts or Dukes");
 }

 @Test
 public void testCastlePersonalFiefdomNotOwned() {
 Iterator<String> argIterator = Arrays.asList("1", "buyer1", "notOwnedFiefdom").iterator();
 purchase.castlePersonal(argIterator);

 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom: notOwnedFiefdom owner: null");
 }

 @Test
 public void testMercenary() {
 Iterator<String> argIterator = Arrays.asList("2", "buyer1", "knight1").iterator();
 when(argIterator.next()).thenReturn("2");
 when(argIterator.next()).thenReturn("buyer1");
 when(argIterator.next()).thenReturn("knight1");
 purchase.mercenary(argIterator);

 verify(knight, times(1)).addMercenaries(2);
 verify(buyer1, times(1)).setTotalMoney(-2, false);
 }

 @Test
 public void testMercenaryNoFunds() {
 Iterator<String> argIterator = Arrays.asList("2", "buyer1", "knight1").iterator();
 when(argIterator.next()).thenReturn("2");
 when(argIterator.next()).thenReturn("buyer1");
 when(argIterator.next()).thenReturn("knight1");
 when(buyer1.getTotalMoney()).thenReturn(0);

 purchase.mercenary(argIterator);

 verify(System.out, times(1)).println("You cannot buy more mercenaries, you have insufficient funds");
 }

 @Test
 public void testMercenarySameKnight() {
 Iterator<String> argIterator = Arrays.asList("2", "buyer1", "buyer1").iterator();
 when(argIterator.next()).thenReturn("2");
 when(argIterator.next()).thenReturn("buyer1");
 when(argIterator.next()).thenReturn("buyer1");
 purchase.mercenary(argIterator);

 verify(knight, times(1)).addMercenaries(2);
 verify(buyer1, times(1)).setTotalMoney(-2, false);
 }

 @Test
 public void testMercenaryDifferentKnight() {
 Iterator<String> argIterator = Arrays.asList("2", "buyer1", "knight2").iterator();
 when(argIterator.next()).thenReturn("2");
 when(argIterator.next()).thenReturn("buyer1");
 when(argIterator.next()).thenReturn("knight2");
 purchase.mercenary(argIterator);

 verify(knight2, times(1)).addMercenaries(2);
 verify(buyer1, times(1)).setTotalMoney(-2, false);
 }

 @Test
 public void testMercenaryKingBuysMercenaryFromKnight() {
 Iterator<String> argIterator = Arrays.asList("2", "king", "knight1").iterator();
 when(argIterator.next()).thenReturn("2");
 when(argIterator.next()).thenReturn("king");
 when(argIterator.next()).thenReturn("knight1");
 purchase.mercenary(argIterator);

 verify(knight1, times(1)).addMercenaries(2);
 verify(king, times(1)).setTotalMoney(-2, false);
 }
}