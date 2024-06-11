package src.test;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.*;

import src.Actions;
import src.Fiefdoms;
import src.GameAutoActions;
import src.CurrentPlayers;
import src.Knight;
import src.Map;

public class PurchaseTest {

 private Purchase purchase;
 private Iterator<Object> argsIter;
 private Fiefdoms fiefdom;
 private CurrentPlayers currentPlayers;
 private GameAutoActions gameAutoActions;
 private Map map;

 @Before
 public void setUp() {
 purchase = new Purchase();
 argsIter = Arrays.asList(new Object[]{"1", "player1", "fiefdom1"}).iterator();
 fiefdom = new Fiefdoms();
 currentPlayers = new CurrentPlayers();
 gameAutoActions = new GameAutoActions();
 map = new Map();
 map.put("fiefdom1", fiefdom);
 currentPlayers.put("player1", new Knight("player1", "knight"));
 currentPlayers.put("player2", new Knight("player2", "knight"));
 }

 @Test
 public void testVassal() {
 //test with valid arguments
 purchase.vassal(argsIter);
 fiefdom.setLoyalVassals(1, true);
 verify(currentPlayers, times(1)).get(eq("player1"));
 verify(currentPlayers, times(1)).get(eq("player1")).setTotalMoney(1, false);
 verify(fiefdom, times(1)).setLoyalVassals(1, true);

 //test with insufficient funds
 purchase.vassal(argsIter);
 argsIter.next(); //qty = 2
 verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
 }

 @Test
 public void testCastleFiefdom() {
 //test with valid arguments
 purchase.castleFiefdom(argsIter);
 fiefdom.buyFiefdomCastles(1);
 verify(currentPlayers, times(1)).get(eq("player1"));
 verify(currentPlayers, times(1)).get(eq("player1")).setTotalMoney(25, false);
 verify(fiefdom, times(1)).buyFiefdomCastles(1);

 //test with insufficient funds
 purchase.castleFiefdom(argsIter);
 argsIter.next(); //qty = 2
 verify(System.out, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
 }

 @Test
 public void testCastlePersonal() {
 //test with valid arguments
 purchase.castlePersonal(argsIter);
 fiefdom.buyPersonalCastles(1);
 verify(currentPlayers, times(1)).get(eq("player1"));
 verify(currentPlayers, times(1)).get(eq("player1")).setTotalMoney(10, false);
 verify(fiefdom, times(1)).buyPersonalCastles(1);

 //test with insufficient funds
 purchase.castlePersonal(argsIter);
 argsIter.next(); //qty = 2
 verify(System.out, times(1)).println("You cannot buy more Personal castle, you have insufficient funds");
 }

 @Test
 public void testMercenary() {
 //test with valid arguments
 purchase.mercenary(argsIter);
 verify(currentPlayers, times(1)).get(eq("player1"));
 verify(currentPlayers, times(1)).get(eq("knight1"));
 verify(currentPlayers, times(1)).get(eq("player1")).setTotalMoney(1, false);
 verify(currentPlayers, times(1)).get(eq("knight1")).addMercenaries(1);

 //test with self purchase
 purchase.mercenary(argsIter);
 argsIter.next(); //knightName = "player1"
 argsIter.next(); //buyerName = "player1"
 verify(System.out, times(1)).println("You cannot buy mercenaries for yourself");
 }

 @Test
 public void testInvalidArguments() {
 //test with non-existent fiefdom
 purchase.vassal(argsIter);
 argsIter.next(); //"nonExistentFiefdom"
 verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
 }
}