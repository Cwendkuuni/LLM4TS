package src.test.java;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.*;

public class PurchaseTest {

 private Purchase purchase;
 private List<String> argsList;

 @Before
 public void setUp() {
 purchase = new Purchase();
 argsList = new ArrayList<>();
 }

 @Test
 public void testVassalMethod() {
 argsList.add("1");
 argsList.add("player1");
 argsList.add("fiefdom1");
 doReturn(new Fiefdoms(){{
 setOwner(null);
 setName("fiefdom1");
}}).when(Map.class).get(argsList.get(2));
 doReturn(new Knight(){{
 setName("player1");
 setRank("knight");
 setTotalMoney(100);
}}).when(CurrentPlayers.class).get(argsList.get(1));

 try {
 purchase.perform(argsList);
 verify(Map.class).get(argsList.get(2));
 verify(CurrentPlayers.class).get(argsList.get(1));
 purchase.vassal(argsList.iterator());
 } catch (Exception e) {
 e.printStackTrace();
 }

 verify(CurrentPlayers.class).get(argsList.get(1)).setTotalMoney(1, false);
 verify(Fiefdoms.class).setLoyalVassals(1, true);
 }

 @Test
 public void testVassalMethodWithInsufficientFunds() {
 argsList.clear();
 argsList.add("1");
 argsList.add("player1");
 argsList.add("fiefdom1");
 doReturn(new Fiefdoms(){{
 setOwner(null);
 setName("fiefdom1");
}}).when(Map.class).get(argsList.get(2));
 doReturn(new Knight(){{
 setName("player1");
 setRank("knight");
 setTotalMoney(5);
}}).when(CurrentPlayers.class).get(argsList.get(1));

 try {
 purchase.perform(argsList);
 purchase.vassal(argsList.iterator());
 } catch (Exception e) {
 e.printStackTrace();
 }

 verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
 }

 @Test
 public void testVassalMethodWithInvalidFiefdom() {
 argsList.clear();
 argsList.add("1");
 argsList.add("player1");
 argsList.add("fiefdomX");
 doReturn(null).when(Map.class).get(argsList.get(2));

 try {
 purchase.perform(argsList);
 purchase.vassal(argsList.iterator());
 } catch (Exception e) {
 e.printStackTrace();
 }

 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
 }

 @Test
 public void testCastleFiefdomMethod() {
 argsList.clear();
 argsList.add("1");
 argsList.add("player1");
 argsList.add("fiefdom1");
 doReturn(new Fiefdoms(){{
 setOwner(new Knight(){{
 setName("player2");
 setRank("count");
 }});
 setName("fiefdom1");
 setVikingSize(0);
 setMagyarSize(0);
 setMuslimSize(0);
}}).when(Map.class).get(argsList.get(2));
 doReturn(new Knight(){{
 setName("player1");
 setRank("king");
 setTotalMoney(500);
}}).when(CurrentPlayers.class).get(argsList.get(1));

 try {
 purchase.perform(argsList);
 purchase.castleFiefdom(argsList.iterator());
 } catch (Exception e) {
 e.printStackTrace();
 }

 verify(Map.class).get(argsList.get(2));
 verify(CurrentPlayers.class).get(argsList.get(1));
 verify(Fiefdoms.class).buyFiefdomCastles(1);
 }

 @Test
 public void testCastleFiefdomMethodWithInsufficientFunds() {
 argsList.clear();
 argsList.add("1");
 argsList.add("player1");
 argsList.add("fiefdom1");
 doReturn(new Fiefdoms(){{
 setOwner(new Knight(){{
 setName("player2");
 setRank("count");
 }});
 setName("fiefdom1");
 setVikingSize(0);
 setMagyarSize(0);
 setMuslimSize(0);
}}).when(Map.class).get(argsList.get(2));
 doReturn(new Knight(){{
 setName("player1");
 setRank("king");
 setTotalMoney(250);
}}).when(CurrentPlayers.class).get(argsList.get(1));

 try {
 purchase.perform(argsList);
 purchase.castleFiefdom(argsList.iterator());
 } catch (Exception e) {
 e.printStackTrace();
 }

 verify(System.out, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
 }

 @Test
 public void testCastleFiefdomMethodWithInvalidFiefdom() {
 argsList.clear();
 argsList.add("1");
 argsList.add("player1");
 argsList.add("fiefdomX");
 doReturn(null).when(Map.class).get(argsList.get(2));

 try {
 purchase.perform(argsList);
 purchase.castleFiefdom(argsList.iterator());
 } catch (Exception e) {
 e.printStackTrace();
 }

 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
 }

 @Test
 public void testCastlePersonalMethod() {
 argsList.clear();
 argsList.add("1");
 argsList.add("player1");
 argsList.add("fiefdom1");
 doReturn(new Fiefdoms(){{
 setOwner(null);
 setName("fiefdom1");
}}).when(Map.class).get(argsList.get(2));
 doReturn(new Knight(){{
 setName("player1");
 setRank("knight");
 setTotalMoney(100);
}}).when(CurrentPlayers.class).get(argsList.get(1));

 try {
 purchase.perform(argsList);
 purchase.castlePersonal(argsList.iterator());
 } catch (Exception e) {
 e.printStackTrace();
 }

 verify(Map.class).get(argsList.get(2));
 verify(CurrentPlayers.class).get(argsList.get(1));
 verify(Fiefdoms.class).buyPersonalCastles(1);
 }

 @Test
 public void testCastlePersonalMethodWithInsufficientFunds() {
 argsList.clear();
 argsList.add("1");
 argsList.add("player1");
 argsList.add("fiefdom1");
 doReturn(new Fiefdoms(){{
 setOwner(null);
 setName("fiefdom1");
}}).when(Map.class).get(argsList.get(2));
 doReturn(new Knight(){{
 setName("player1");
 setRank("knight");
 setTotalMoney(5);
}}).when(CurrentPlayers.class).get(argsList.get(1));

 try {
 purchase.perform(argsList);
 purchase.castlePersonal(argsList.iterator());
 } catch (Exception e) {
 e.printStackTrace();
 }

 verify(System.out, times(1)).println("You cannot buy more Personal castle, you have insufficient funds");
 }

 @Test
 public void testCastlePersonalMethodWithInvalidFiefdom() {
 argsList.clear();
 argsList.add("1");
 argsList.add("player1");
 argsList.add("fiefdomX");
 doReturn(null).when(Map.class).get(argsList.get(2));

 try {
 purchase.perform(argsList);
 purchase.castlePersonal(argsList.iterator());
 } catch (Exception e) {
 e.printStackTrace();
 }

 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
 }

 @Test
 public void testMercenaryMethod() {
 argsList.clear();
 argsList.add("1");
 argsList.add("player1");
 argsList.add("knight1");
 doReturn(new Knight(){{
 setName("player1");
 setRank("king");
 setTotalMoney(500);
}}).when(CurrentPlayers.class).get(argsList.get(1));
 doReturn(new Knight(){{
 setName("knight1");
 setRank("knight");
 setTotalMoney(5);
 setMercenaries(0);
}}).when(CurrentPlayers.class).get(argsList.get(2));

 try {
 purchase.perform(argsList);
 purchase.mercenary(argsList.iterator());
 } catch (Exception e) {
 e.printStackTrace();
 }

 verify(Map.class).get(argsList.get(1));
 verify(Map.class).get(argsList.get(2));
 verify(CurrentPlayers.class).get(argsList.get(1));
 verify(CurrentPlayers.class).get(argsList.get(2));
 verify(Knight.class).setTotalMoney(1, false);
 verify(Knight.class).addMercenaries(1);
 }

 @Test
 public void testMercenaryMethodWithInsufficientFunds() {
 argsList.clear();
 argsList.add("1");
 argsList.add("player1");
 argsList.add("knight1");
 doReturn(new Knight(){{
 setName("player1");
 setRank("king");
 setTotalMoney(500);
}}).when(CurrentPlayers.class).get(argsList.get(1));
 doReturn(new Knight(){{
 setName("knight1");
 setRank("knight");
 setTotalMoney(5);
 setMercenaries(0);
}}).when(CurrentPlayers.class).get(argsList.get(2));

 try {
 purchase.perform(argsList);
 purchase.mercenary(argsList.iterator());
 } catch (Exception e) {
 e.printStackTrace();
 }

 verify(System.out, times(1)).println("You cannot buy more mercenaries, you have insufficient funds");
 }

 @Test
 public void testMercenaryMethodWithInvalidKnight() {
 argsList.clear();
 argsList.add("1");
 argsList.add("player1");
 argsList.add("knightX");
 doReturn(new Knight(){{
 setName("player1");
 setRank("king");
 setTotalMoney(500);
}}).when(CurrentPlayers.class).get(argsList.get(1));
 doReturn(null).when(CurrentPlayers.class).get(argsList.get(2));

 try {
 purchase.perform(argsList);
 purchase.mercenary(argsList.iterator());
 } catch (Exception e) {
 e.printStackTrace();
 }

 verify(System.out, times(1)).println("Inexistent Knight");
 }
}