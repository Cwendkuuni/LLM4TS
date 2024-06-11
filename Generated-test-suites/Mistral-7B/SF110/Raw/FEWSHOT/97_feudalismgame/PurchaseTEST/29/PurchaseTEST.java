package src.test.java.org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PurchaseTest {

    private Purchase purchase;
    private Collection args;
    private Iterator argIterator;
    private Fiefdoms fiefdom;
    private Knight buyer, knight;
    private CurrentPlayers currentPlayers;
    private Map map;

    @Before
    public void setUp() {
        purchase = new Purchase();
        args = new ArrayList<>();
        argIterator = args.iterator();
        fiefdom = Mockito.mock(Fiefdoms.class);
        buyer = Mockito.mock(Knight.class);
        knight = Mockito.mock(Knight.class);
        currentPlayers = Mockito.mock(CurrentPlayers.class);
        map = Mockito.mock(Map.class);

        when(map.get(anyString())).thenReturn(fiefdom);
        when(currentPlayers.get(anyString())).thenReturn(buyer);
        when(currentPlayers.get(eq(knight.getName()))).thenReturn(knight);
        when(fiefdom.getOwner()).thenReturn(null);
    }

    @Test
    public void testPerform_EmptyArgs() {
        assertNull(purchase.perform(args));
    }

    @Test
    public void testPerform_InvalidMethod() {
        String invalidMethod = "invalidMethod";
        argIterator.next();
        argIterator.next();
        argIterator.next();
        argIterator.next();

        assertFalse(purchase.perform(args));
    }

    @Test
    public void testVassal_ValidArgs() {
        String fiefdomName = "testFiefdom";
        int qty = 2;
        String buyerName = "testBuyer";

        when(fiefdom.getName()).thenReturn(fiefdomName);
        when(fiefdom.getOwner()).thenReturn(null);
        when(buyer.getRank()).thenReturn("knight");

        String[] argsArray = {"vassal", Iterators.asList(argIterator), fiefdomName, buyerName};
        purchase.perform(Arrays.asList(argsArray));

        verify(fiefdom).setLoyalVassals(qty, true);
        verify(buyer).setTotalMoney(qty, false);
    }

    @Test
    public void testVassal_InsufficientFunds() {
        int qty = 3;
        String buyerName = "testBuyer";

        when(fiefdom.getName()).thenReturn("testFiefdom");
        when(fiefdom.getOwner()).thenReturn(null);
        when(buyer.getRank()).thenReturn("knight");
        when(buyer.getTotalMoney()).thenReturn(10);

        String[] argsArray = {"vassal", Iterators.asList(argIterator), fiefdom.getName(), buyerName, Integer.toString(qty)};
        purchase.perform(Arrays.asList(argsArray));

        verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
    }

    @Test
    public void testVassal_FiefdomAlreadyOwned() {
        String fiefdomName = "testFiefdom";
        int qty = 1;
        String buyerName = "testBuyer";
        String buyerNameOwningFiefdom = "testBuyerOwningFiefdom";

        when(fiefdom.getName()).thenReturn(fiefdomName);
        when(fiefdom.getOwner()).thenReturn(buyerNameOwningFiefdom);
        when(buyer.getRank()).thenReturn("knight");

        String[] argsArray = {"vassal", Iterators.asList(argIterator), fiefdom.getName(), buyerName};
        purchase.perform(Arrays.asList(argsArray));

        verify(System.out, times(1)).println("You can only buy a vassal for a fiefdom that doesn't belong to you");
    }

    @Test
    public void testCastleFiefdom_ValidArgs() {
        String fiefdomName = "testFiefdom";
        int qty = 2;
        String buyerName = "testBuyer";

        when(fiefdom.getName()).thenReturn(fiefdomName);
        when(fiefdom.getOwner()).thenReturn(null);
        when(buyer.getRank()).thenReturn("king");

        String[] argsArray = {"castleFiefdom", Iterators.asList(argIterator), fiefdom.getName(), buyerName};
        purchase.perform(Arrays.asList(argsArray));

        verify(fiefdom).buyFiefdomCastles(qty);
        verify(buyer).setTotalMoney(qty * 25, false);
    }

    @Test
    public void testCastleFiefdom_InsufficientFunds() {
        int qty = 3;
        String buyerName = "testBuyer";

        when(fiefdom.getName()).thenReturn("testFiefdom");
        when(fiefdom.getOwner()).thenReturn(null);
        when(buyer.getRank()).thenReturn("king");
        when(buyer.getTotalMoney()).thenReturn(50);

        String[] argsArray = {"castleFiefdom", Iterators.asList(argIterator), fiefdom.getName(), buyerName, Integer.toString(qty)};
        purchase.perform(Arrays.asList(argsArray));

        verify(System.out, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
    }

    @Test
    public void testCastlePersonal_ValidArgs() {
        String fiefdomName = "testFiefdom";
        int qty = 2;
        String buyerName = "testBuyer";

        when(fiefdom.getName()).thenReturn(fiefdomName);
        when(fiefdom.getOwner()).thenReturn(null);
        when(buyer.getRank()).thenReturn("knight");

        String[] argsArray = {"castlePersonal", Iterators.asList(argIterator), fiefdom.getName(), buyerName};
        purchase.perform(Arrays.asList(argsArray));

        verify(fiefdom).buyPersonalCastles(qty);
        verify(buyer).setTotalMoney(qty * 10, false);
    }

    @Test
    public void testCastlePersonal_InsufficientFunds() {
        int qty = 3;
        String buyerName = "testBuyer";

        when(fiefdom.getName()).thenReturn("testFiefdom");
        when(fiefdom.getOwner()).thenReturn(null);
        when(buyer.getRank()).thenReturn("knight");
        when(buyer.getTotalMoney()).thenReturn(5);

        String[] argsArray = {"castlePersonal", Iterators.asList(argIterator), fiefdom.getName(), buyerName, Integer.toString(qty)};
        purchase.perform(Arrays.asList(argsArray));

        verify(System.out, times(1)).println("You cannot buy more Personal castle, you have insufficient funds");
    }

    @Test
    public void testCastlePersonal_BuyerIsKing() {
        String fiefdomName = "testFiefdom";
        int qty = 2;
        String buyerName = "testBuyer";

        when(fiefdom.getName()).thenReturn(fiefdomName);
        when(fiefdom.getOwner()).thenReturn(null);
        when(buyer.getRank()).thenReturn("king");

        String[] argsArray = {"castlePersonal", Iterators.asList(argIterator), fiefdom.getName(), buyerName};
        purchase.perform(Arrays.asList(argsArray));

        verify(System.out, times(1)).println("You cannot buy a Personal Castle for Counts or Dukes");
    }

    @Test
    public void testCastlePersonal_FiefdomNotBelongedByBuyer() {
        String fiefdomName = "testFiefdom";
        int qty = 2;
        String buyerName = "testBuyer";
        String ownerName = "testOwner";

        when(fiefdom.getName()).thenReturn(fiefdomName);
        when(fiefdom.getOwner()).thenReturn(ownerName);
        when(buyer.getRank()).thenReturn("knight");

        String[] argsArray = {"castlePersonal", Iterators.asList(argIterator), fiefdom.getName(), buyerName};
        purchase.perform(Arrays.asList(argsArray));

        verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
    }

    @Test
    public void testMercenary_ValidArgs() {
        int qty = 2;
        String buyerName = "testBuyer";
        String knightName = "testKnight";

        when(buyer.getName()).thenReturn(buyerName);
        when(knight.getName()).thenReturn(knightName);
        when(buyer.getRank()).thenReturn("king");

        String[] argsArray = {"mercenary", Iterators.asList(argIterator), knight.getName(), buyerName, Integer.toString(qty)};
        purchase.perform(Arrays.asList(argsArray));

        verify(knight).addMercenaries(qty);
        verify(buyer).setTotalMoney(qty, false);
    }

    @Test
    public void testMercenary_InsufficientFunds() {
        int qty = 3;
        String buyerName = "testBuyer";
        String knightName = "testKnight";

        when(buyer.getName()).thenReturn(buyerName);
        when(knight.getName()).thenReturn(knightName);
        when(buyer.getRank()).thenReturn("king");
        when(buyer.getTotalMoney()).thenReturn(15);

        String[] argsArray = {"mercenary", Iterators.asList(argIterator), knight.getName(), buyerName, Integer.toString(qty)};
        purchase.perform(Arrays.asList(argsArray));

        verify(System.out, times(1)).println("You cannot buy more mercenaries, you have insufficient funds");
    }

    @Test
    public void testMercenary_BuyerIsSameKnight() {
        int qty = 2;
        String buyerName = "testBuyer";
        String knightName = "testKnight";

        when(buyer.getName()).thenReturn(buyerName);
        when(knight.getName()).thenReturn(knightName);
        when(buyer.getRank()).thenReturn("king");

        String[] argsArray = {"mercenary", Iterators.asList(argIterator), knightName, buyerName, Integer.toString(qty)};
        purchase.perform(Arrays.asList(argsArray));

        verify(knight, times(1)).addMercenaries(qty);
        verify(buyer, times(1)).setTotalMoney(qty, false);
    }

    @Test
    public void testMercenary_InvalidKnightName() {
        int qty = 2;
        String buyerName = "testBuyer";
        String invalidKnightName = "testInvalidKnight";

        when(buyer.getName()).thenReturn(buyerName);

        String[] argsArray = {"mercenary", Iterators.asList(argIterator), invalidKnightName, buyerName, Integer.toString(qty)};
        purchase.perform(Arrays.asList(argsArray));

        verify(System.out, times(1)).println("Inexistent Knight");
    }
}