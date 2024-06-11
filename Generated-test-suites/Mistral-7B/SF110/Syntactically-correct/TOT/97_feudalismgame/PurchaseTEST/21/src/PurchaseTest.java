import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.Actions;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;
import src.Purchase;
import src.CurrentPlayers;

import java.util.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class PurchaseTest {

 private Purchase purchase;
 private Actions actions;
 private GameAutoActions gameAutoActions;
 private CurrentPlayers currentPlayers;
 private Map map;
 private Fiefdoms fiefdom;
 private Knight buyer, knight;

 @Before
 public void setup() {
 actions = Mockito.spy(new Actions());
 gameAutoActions = Mockito.spy(new GameAutoActions());
 currentPlayers = new CurrentPlayers();
 map = new Map();
 fiefdom = new Fiefdoms("testFiefdom", "testOwner");
 map.put("testFiefdom", fiefdom);
 buyer = new Knight("testBuyer", "testRank");
 currentPlayers.put("testBuyer", buyer);
 knight = new Knight("testKnight", "testRank");
 }

 @Test
 public void testVassal() {
 Iterator<Object> args = Arrays.asList(3, buyer, "testFiefdom").iterator();
 purchase = new Purchase();
 try {
 purchase.perform(args);
 fiefdom.setLoyalVassals(3, true);
 assertEquals(3, fiefdom.getLoyalVassals());
 } catch (Exception e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testVassalNoFunds() {
 Iterator<Object> args = Arrays.asList(4, buyer, "testFiefdom").iterator();
 purchase = new Purchase();
 try {
 purchase.perform(args);
 fail();
 } catch (Exception e) {
 assertFalse(buyer.getTotalMoney() >= 4);
 }
 }

 @Test
 public void testVassalOtherPlayer() {
 Iterator<Object> args = Arrays.asList(3, new Knight("testOtherPlayer", "testRank"), "testFiefdom").iterator();
 purchase = new Purchase();
 try {
 purchase.perform(args);
 fail();
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testCastleFiefdom() {
 Iterator<Object> args = Arrays.asList(3, buyer, "testFiefdom").iterator();
 try {
 purchase = new Purchase();
 purchase.perform(args);
 fiefdom.buyFiefdomCastles(3);
 assertEquals(3, fiefdom.getCastles().size());
 } catch (Exception e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testCastleFiefdomNoFunds() {
 Iterator<Object> args = Arrays.asList(4, buyer, "testFiefdom").iterator();
 try {
 purchase = new Purchase();
 purchase.perform(args);
 fail();
 } catch (Exception e) {
 assertFalse(buyer.getTotalMoney() >= 75);
 }
 }

 @Test
 public void testCastleFiefdomOtherPlayer() {
 Iterator<Object> args = Arrays.asList(3, new Knight("testOtherPlayer", "testRank"), "testFiefdom").iterator();
 purchase = new Purchase();
 try {
 purchase.perform(args);
 fail();
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testCastlePersonal() {
 Iterator<Object> args = Arrays.asList(3, buyer, "testFiefdom").iterator();
 try {
 purchase = new Purchase();
 purchase.perform(args);
 fiefdom.buyPersonalCastles(3);
 assertEquals(3, fiefdom.getPersonalCastles().size());
 } catch (Exception e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testCastlePersonalNoFunds() {
 Iterator<Object> args = Arrays.asList(4, buyer, "testFiefdom").iterator();
 try {
 purchase = new Purchase();
 purchase.perform(args);
 fail();
 } catch (Exception e) {
 assertFalse(buyer.getTotalMoney() >= 10);
 }
 }

 @Test
 public void testCastlePersonalOtherPlayer() {
 Iterator<Object> args = Arrays.asList(3, new Knight("testOtherPlayer", "testRank"), "testFiefdom").iterator();
 purchase = new Purchase();
 try {
 purchase.perform(args);
 fail();
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testMercenary() {
 Iterator<Object> args = Arrays.asList(3, buyer, knight).iterator();
 try {
 purchase = new Purchase();
 purchase.perform(args);
 knight.addMercenaries(3);
 assertEquals(3, knight.getMercenaries().size());
 } catch (Exception e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testMercenaryNoFunds() {
 Iterator<Object> args = Arrays.asList(4, buyer, knight).iterator();
 try {
 purchase = new Purchase();
 purchase.perform(args);
 fail();
 } catch (Exception e) {
 assertFalse(buyer.getTotalMoney() >= 3);
 }
 }

 @Test
 public void testMercenarySamePlayer() {
 Iterator<Object> args = Arrays.asList(3, buyer, buyer).iterator();
 try {
 purchase = new Purchase();
 purchase.perform(args);
 fail();
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testMercenaryOtherPlayer() {
 Iterator<Object> args = Arrays.asList(3, buyer, knight).iterator();
 purchase = new Purchase();
 try {
 purchase.perform(args);
 fail();
 } catch (Exception e) {
 // expected
 }
 }
}