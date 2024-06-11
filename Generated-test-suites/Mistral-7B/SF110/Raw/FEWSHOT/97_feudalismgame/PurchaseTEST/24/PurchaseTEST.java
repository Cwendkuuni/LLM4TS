package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseTest {

 private static final Map<String, Fiefdoms> Map = new HashMap<>();
 private static final List<Knight> CurrentPlayers = new ArrayList<>();

 @Test
 public void testPerformVassal() {
     Fiefdoms fiefdom = new Fiefdoms("France", "Louis XIV");
     Map.put("France", fiefdom);
     Knight buyer = new Knight("John Doe", "knight");
     CurrentPlayers.add(buyer);
     Iterator<String> args = Arrays.asList("2", "John Doe", "France").iterator();

     Purchase purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(2, fiefdom.getLoyalVassals());
     assertEquals(200, buyer.getTotalMoney());
 }

 @Test
 public void testPerformVassalInsufficientFunds() {
     Fiefdoms fiefdom = new Fiefdoms("France", "Louis XIV");
     Map.put("France", fiefdom);
     Knight buyer = new Knight("John Doe", "knight");
     CurrentPlayers.add(buyer);
     Iterator<String> args = Arrays.asList("3", "John Doe", "France").iterator();

     Purchase purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(0, fiefdom.getLoyalVassals());
     assertEquals(180, buyer.getTotalMoney());
 }

 @Test
 public void testPerformVassalFiefdomNotBelongToKing() {
     Fiefdoms fiefdom = new Fiefdoms("France", "Louis XIV");
     Map.put("France", fiefdom);
     Knight buyer = new Knight("John Doe", "knight");
     CurrentPlayers.add(buyer);
     Iterator<String> args = Arrays.asList("2", "John Doe", "Germany").iterator();

     Purchase purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(0, fiefdom.getLoyalVassals());
     assertEquals(190, buyer.getTotalMoney());
 }

 @Test
 public void testPerformVassalBelongToCountOrDuke() {
     Fiefdoms fiefdom = new Fiefdoms("France", "Louis XIV");
     Map.put("France", fiefdom);
     Knight buyer = new Knight("John Doe", "knight");
     CurrentPlayers.add(buyer);
     Knight king = new Knight("Louis XIV", "king");
     CurrentPlayers.add(king);
     Iterator<String> args = Arrays.asList("2", "John Doe", "France").iterator();

     when(CurrentPlayers.stream().filter(knight -> knight.getName().equalsIgnoreCase("Louis XIV")).findFirst().getRank()).thenReturn("count");

     Purchase purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(0, fiefdom.getLoyalVassals());
     assertEquals(190, buyer.getTotalMoney());
 }

 @Test
 public void testPerformCastleFiefdom() {
     Fiefdoms fiefdom = new Fiefdoms("France", "Louis XIV");
     Map.put("France", fiefdom);
     Knight buyer = new Knight("John Doe", "knight");
     CurrentPlayers.add(buyer);
     Iterator<String> args = Arrays.asList("2", "John Doe", "France").iterator();

     Purchase purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(0, fiefdom.getCastles().size());
     assertEquals(180, buyer.getTotalMoney());

     args = Arrays.asList("2", "John Doe", "France").iterator();
     purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(2, fiefdom.getCastles().size());
     assertEquals(160, buyer.getTotalMoney());
 }

 @Test
 public void testPerformCastleFiefdomInsufficientFunds() {
     Fiefdoms fiefdom = new Fiefdoms("France", "Louis XIV");
     Map.put("France", fiefdom);
     Knight buyer = new Knight("John Doe", "knight");
     CurrentPlayers.add(buyer);
     Iterator<String> args = Arrays.asList("3", "John Doe", "France").iterator();

     Purchase purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(0, fiefdom.getCastles().size());
     assertEquals(165, buyer.getTotalMoney());
 }

 @Test
 public void testPerformCastleFiefdomFiefdomNotBelongToKing() {
     Fiefdoms fiefdom = new Fiefdoms("France", "Louis XIV");
     Map.put("France", fiefdom);
     Knight buyer = new Knight("John Doe", "knight");
     CurrentPlayers.add(buyer);
     Iterator<String> args = Arrays.asList("2", "John Doe", "Germany").iterator();

     Purchase purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(0, fiefdom.getCastles().size());
     assertEquals(170, buyer.getTotalMoney());
 }

 @Test
 public void testPerformCastleFiefdomBelongToCountOrDuke() {
     Fiefdoms fiefdom = new Fiefdoms("France", "Louis XIV");
     Map.put("France", fiefdom);
     Knight buyer = new Knight("John Doe", "knight");
     CurrentPlayers.add(buyer);
     Knight king = new Knight("Louis XIV", "king");
     CurrentPlayers.add(king);
     Iterator<String> args = Arrays.asList("2", "John Doe", "France").iterator();

     when(CurrentPlayers.stream().filter(knight -> knight.getName().equalsIgnoreCase("Louis XIV")).findFirst().getRank()).thenReturn("count");

     Purchase purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(0, fiefdom.getCastles().size());
     assertEquals(170, buyer.getTotalMoney());
 }

 @Test
 public void testPerformCastlePersonal() {
     Fiefdoms fiefdom = new Fiefdoms("France", "Louis XIV");
     Map.put("France", fiefdom);
     Knight buyer = new Knight("John Doe", "knight");
     CurrentPlayers.add(buyer);
     Iterator<String> args = Arrays.asList("2", "John Doe", "France").iterator();

     Purchase purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(0, fiefdom.getPersonalCastles().size());
     assertEquals(180, buyer.getTotalMoney());

     args = Arrays.asList("2", "John Doe", "France").iterator();
     purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(2, fiefdom.getPersonalCastles().size());
     assertEquals(160, buyer.getTotalMoney());
 }

 @Test
 public void testPerformCastlePersonalInsufficientFunds() {
     Fiefdoms fiefdom = new Fiefdoms("France", "Louis XIV");
     Map.put("France", fiefdom);
     Knight buyer = new Knight("John Doe", "knight");
     CurrentPlayers.add(buyer);
     Iterator<String> args = Arrays.asList("3", "John Doe", "France").iterator();

     Purchase purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(0, fiefdom.getPersonalCastles().size());
     assertEquals(165, buyer.getTotalMoney());
 }

 @Test
 public void testPerformCastlePersonalFiefdomNotBelongToKing() {
     Fiefdoms fiefdom = new Fiefdoms("France", "Louis XIV");
     Map.put("France", fiefdom);
     Knight buyer = new Knight("John Doe", "knight");
     CurrentPlayers.add(buyer);
     Iterator<String> args = Arrays.asList("2", "John Doe", "Germany").iterator();

     Purchase purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(0, fiefdom.getPersonalCastles().size());
     assertEquals(170, buyer.getTotalMoney());
 }

 @Test
 public void testPerformCastlePersonalBelongToCountOrDuke() {
     Fiefdoms fiefdom = new Fiefdoms("France", "Louis XIV");
     Map.put("France", fiefdom);
     Knight buyer = new Knight("John Doe", "knight");
     CurrentPlayers.add(buyer);
     Knight king = new Knight("Louis XIV", "king");
     CurrentPlayers.add(king);
     Iterator<String> args = Arrays.asList("2", "John Doe", "France").iterator();

     when(CurrentPlayers.stream().filter(knight -> knight.getName().equalsIgnoreCase("Louis XIV")).findFirst().getRank()).thenReturn("count");

     Purchase purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(0, fiefdom.getPersonalCastles().size());
     assertEquals(170, buyer.getTotalMoney());
 }

 @Test
 public void testPerformMercenary() {
     Knight knight = new Knight("John Doe", "knight");
     CurrentPlayers.add(knight);
     Knight buyer = new Knight("Jane Doe", "knight");
     CurrentPlayers.add(buyer);
     Iterator<String> args = Arrays.asList("2", "Jane Doe", "John Doe").iterator();

     Purchase purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(2, knight.getMercenaries());
     assertEquals(182, buyer.getTotalMoney());
 }

 @Test
 public void testPerformMercenaryBuyerIsKing() {
     Knight knight = new Knight("John Doe", "knight");
     CurrentPlayers.add(knight);
     Knight buyer = new Knight("Louis XIV", "king");
     CurrentPlayers.add(buyer);
     Iterator<String> args = Arrays.asList("2", "Louis XIV", "John Doe").iterator();

     Purchase purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(2, knight.getMercenaries());
     assertEquals(182, buyer.getTotalMoney());
 }

 @Test
 public void testPerformMercenaryInvalidBuyer() {
     Knight knight = new Knight("John Doe", "knight");
     CurrentPlayers.add(knight);
     Iterator<String> args = Arrays.asList("2", "John Doe", "InexistentKnight").iterator();

     Purchase purchase = new Purchase();
     purchase.perform(Arrays.asList(args));

     assertEquals(0, knight.getMercenaries());
     assertEquals(180, CurrentPlayers.get(0).getTotalMoney());
 }
}