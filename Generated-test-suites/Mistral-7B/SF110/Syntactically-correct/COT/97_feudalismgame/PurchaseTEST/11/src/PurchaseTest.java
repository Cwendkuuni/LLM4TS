package src.test.java;

import static org.junit.Assert.*;
import src.Fiefdoms;
import src.Map;
import src.Knight;
import src.CurrentPlayers;
import src.Purchase;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class PurchaseTest {

 private Purchase purchase;
 private Map map;
 private CurrentPlayers currentPlayers;
 private Fiefdoms fiefdom;
 private Knight buyer, knight;

 @Before
 public void setUp() {
 purchase = new Purchase();
 map = new Map();
 currentPlayers = new CurrentPlayers();
 fiefdom = new Fiefdoms();
 buyer = new Knight("BuyerName", "knight");
 knight = new Knight("KnightName", "knight");
 map.put("FiefdomName", fiefdom);
 currentPlayers.put("BuyerName", buyer);
 currentPlayers.put("KnightName", knight);
 }

 @Test
 public void testPerform() {
 Collection args = new ArrayList();
 args.add("feudalism.Purchase");
 args.add(args.iterator());
 purchase.perform(args);
 }

 @Test(expected = SecurityException.class)
 public void testPerformWithSecurityException() {
 Collection args = new ArrayList();
 args.add("feudalism.Purchase");
 args.add(args.iterator());
 ReflectionTestUtils.setFieldValue(purchase, "args", args); // set args in the test
 purchase.perform(Collections.emptyList()); // call the method with empty list
 }

 @Test(expected = NoSuchMethodException.class)
 public void testPerformWithNoSuchMethodException() {
 Collection args = new ArrayList();
 args.add("feudalism.NonExistentMethod");
 args.add(args.iterator());
 purchase.perform(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithIllegalArgumentException() {
 Collection args = new ArrayList();
 args.add("feudalism.Purchase");
 args.add(Collections.singletonList(null));
 purchase.perform(args);
 }

 @Test(expected = IllegalAccessException.class)
 public void testPerformWithIllegalAccessException() {
 Collection args = new ArrayList();
 args.add("feudalism.Purchase");
 args.add(args.iterator());
 ReflectionTestUtils.setFieldValue(purchase, "args", args); // set args in the test
 ReflectionTestUtils.setFieldValue(args.iterator().next(), "next", new Iterator() { // set next value to null
 @Override
 public boolean hasNext() {
 return false;
 }

 @Override
 public Object next() {
 return null;
 }
 });
 purchase.perform(Collections.emptyList()); // call the method
 }

 @Test(expected = InvocationTargetException.class)
 public void testPerformWithInvocationTargetException() {
 Collection args = new ArrayList();
 args.add("feudalism.InvalidClass");
 args.add(args.iterator());
 purchase.perform(args);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testPerformWithClassNotFoundException() {
 Collection args = new ArrayList();
 args.add("feudalism.InvalidClass");
 args.add(args.iterator());
 purchase.perform(args);
 }

 @Test
 public void testVassal() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "BuyerName", "FiefdomName").iterator());
 purchase.vassal(arg);
 assertEquals(5, fiefdom.getLoyalVassals());
 }

 @Test(expected = NumberFormatException.class)
 public void testVassalWithNumberFormatException() {
 final Iterator arg = new ArrayList(Arrays.asList("abc", "BuyerName", "FiefdomName").iterator());
 purchase.vassal(arg);
 }

 @Test
 public void testVassalWithFiefdomNotBelongingToKingdom() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "BuyerName", "InvalidFiefdomName").iterator());
 purchase.vassal(arg);
 assertEquals(0, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalWithOwnerNotBeingNull() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "BuyerName", "FiefdomName").iterator());
 map.put("FiefdomName", new Fiefdoms("OwnerName", null));
 purchase.vassal(arg);
 assertEquals(0, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalWithFiefdomBelongingToAnotherPlayer() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "BuyerName", "OtherPlayerNameFiefdom").iterator());
 purchase.vassal(arg);
 assertEquals(0, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalWithBuyerNotHavingSufficientFunds() {
 final Iterator arg = new ArrayList(Arrays.asList("6", "BuyerName", "FiefdomName").iterator());
 buyer.setTotalMoney(15);
 purchase.vassal(arg);
 assertEquals(5, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalWithBuyerBeingKing() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "KingName", "FiefdomName").iterator());
 purchase.vassal(arg);
 assertEquals(5, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassalWithBuyerBeingCountOrDuke() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "CountOrDukeName", "FiefdomName").iterator());
 purchase.vassal(arg);
 assertEquals(0, fiefdom.getLoyalVassals());
 }

 @Test
 public void testCastleFiefdom() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "BuyerName", "FiefdomName").iterator());
 purchase.castleFiefdom(arg);
 assertEquals(5, fiefdom.getCastles().size());
 }

 @Test(expected = NumberFormatException.class)
 public void testCastleFiefdomWithNumberFormatException() {
 final Iterator arg = new ArrayList(Arrays.asList("abc", "BuyerName", "FiefdomName").iterator());
 purchase.castleFiefdom(arg);
 }

 @Test
 public void testCastleFiefdomWithFiefdomNotBelongingToKingdom() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "BuyerName", "InvalidFiefdomName").iterator());
 purchase.castleFiefdom(arg);
 assertEquals(0, fiefdom.getCastles().size());
 }

 @Test
 public void testCastleFiefdomWithOwnerNotBeingNull() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "BuyerName", "FiefdomName").iterator());
 map.put("FiefdomName", new Fiefdoms("OwnerName", null));
 purchase.castleFiefdom(arg);
 assertEquals(0, fiefdom.getCastles().size());
 }

 @Test
 public void testCastleFiefdomWithFiefdomBelongingToAnotherPlayer() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "BuyerName", "OtherPlayerNameFiefdom").iterator());
 purchase.castleFiefdom(arg);
 assertEquals(0, fiefdom.getCastles().size());
 }

 @Test
 public void testCastleFiefdomWithBuyerNotHavingSufficientFunds() {
 final Iterator arg = new ArrayList(Arrays.asList("6", "BuyerName", "FiefdomName").iterator());
 buyer.setTotalMoney(15);
 purchase.castleFiefdom(arg);
 assertEquals(5, fiefdom.getCastles().size());
 }

 @Test
 public void testCastleFiefdomWithBuyerBeingKing() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "KingName", "FiefdomName").iterator());
 purchase.castleFiefdom(arg);
 assertEquals(5, fiefdom.getCastles().size());
 }

 @Test
 public void testCastleFiefdomWithBuyerBeingCountOrDuke() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "CountOrDukeName", "FiefdomName").iterator());
 purchase.castleFiefdom(arg);
 assertEquals(0, fiefdom.getCastles().size());
 }

 @Test
 public void testCastlePersonal() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "BuyerName", "FiefdomName").iterator());
 purchase.castlePersonal(arg);
 assertEquals(5, fiefdom.getPersonalCastles().size());
 }

 @Test(expected = NumberFormatException.class)
 public void testCastlePersonalWithNumberFormatException() {
 final Iterator arg = new ArrayList(Arrays.asList("abc", "BuyerName", "FiefdomName").iterator());
 purchase.castlePersonal(arg);
 }

 @Test
 public void testCastlePersonalWithFiefdomNotBelongingToKingdom() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "BuyerName", "InvalidFiefdomName").iterator());
 purchase.castlePersonal(arg);
 assertEquals(0, fiefdom.getPersonalCastles().size());
 }

 @Test
 public void testCastlePersonalWithOwnerNotBeingNull() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "BuyerName", "FiefdomName").iterator());
 map.put("FiefdomName", new Fiefdoms("OwnerName", null));
 purchase.castlePersonal(arg);
 assertEquals(0, fiefdom.getPersonalCastles().size());
 }

 @Test
 public void testCastlePersonalWithFiefdomBelongingToAnotherPlayer() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "BuyerName", "OtherPlayerNameFiefdom").iterator());
 purchase.castlePersonal(arg);
 assertEquals(0, fiefdom.getPersonalCastles().size());
 }

 @Test
 public void testCastlePersonalWithBuyerNotHavingSufficientFunds() {
 final Iterator arg = new ArrayList(Arrays.asList("6", "BuyerName", "FiefdomName").iterator());
 buyer.setTotalMoney(15);
 purchase.castlePersonal(arg);
 assertEquals(5, fiefdom.getPersonalCastles().size());
 }

 @Test
 public void testCastlePersonalWithKingBuyer() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "KingName", "FiefdomName").iterator());
 purchase.castlePersonal(arg);
 assertEquals(5, fiefdom.getPersonalCastles().size());
 }

 @Test
 public void testCastlePersonalWithCountOrDukeBuyer() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "CountOrDukeName", "FiefdomName").iterator());
 purchase.castlePersonal(arg);
 assertEquals(0, fiefdom.getPersonalCastles().size());
 }

 @Test
 public void testMercenary() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "BuyerName", "KnightName").iterator());
 purchase.mercenary(arg);
 assertEquals(5, knight.getMercenaries());
 }

 @Test(expected = NumberFormatException.class)
 public void testMercenaryWithNumberFormatException() {
 final Iterator arg = new ArrayList(Arrays.asList("abc", "BuyerName", "KnightName").iterator());
 purchase.mercenary(arg);
 }

 @Test
 public void testMercenaryWithInexistentKnight() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "BuyerName", "InexistentKnightName").iterator());
 purchase.mercenary(arg);
 assertEquals(0, knight.getMercenaries());
 }

 @Test
 public void testMercenaryWithBuyerNotHavingSufficientFunds() {
 final Iterator arg = new ArrayList(Arrays.asList("6", "BuyerName", "KnightName").iterator());
 buyer.setTotalMoney(15);
 purchase.mercenary(arg);
 assertEquals(5, knight.getMercenaries());
 }

 @Test
 public void testMercenaryWithBuyerBeingKing() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "KingName", "KnightName").iterator());
 purchase.mercenary(arg);
 assertEquals(5, knight.getMercenaries());
 }

 @Test
 public void testMercenaryWithBuyerAndKnightBeingTheSamePerson() {
 final Iterator arg = new ArrayList(Arrays.asList("5", "BuyerName", "BuyerName").iterator());
 purchase.mercenary(arg);
 assertEquals(5, knight.getMercenaries());
 }
}