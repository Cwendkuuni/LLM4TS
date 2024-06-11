package src.test.java;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.mockito.Mockito.*;

public class PurchaseTest {

    private Purchase purchase;
    private Iterator<Object> argIterator;
    private Map map;
    private CurrentPlayers currentPlayers;
    private Fiefdoms fiefdom;
    private Knight buyerObj, knight, buyerObj2;

    @Before
    public void setUp() {
        purchase = new Purchase();
        map = mock(Map.class);
        currentPlayers = mock(CurrentPlayers.class);
        fiefdom = mock(Fiefdoms.class);
        buyerObj = mock(Knight.class);
        knight = mock(Knight.class);
        buyerObj2 = mock(Knight.class);
        when(map.get(anyString())).thenReturn(fiefdom);
        when(currentPlayers.get(anyString())).thenReturn(buyerObj);
        when(currentPlayers.containsKey(buyerObj.getName())).thenReturn(true);
        when(currentPlayers.get(anyString())).thenReturn(buyerObj2, knight);
        when(buyerObj.getName()).thenReturn("TestBuyer");
        when(buyerObj.getRank()).thenReturn("TestRank");
        when(buyerObj2.getName()).thenReturn("TestBuyer2");
        when(buyerObj2.getRank()).thenReturn("TestRank2");
        when(knight.getName()).thenReturn("TestKnight");
        when(knight.getRank()).thenReturn("TestKnightRank");
        argIterator = Arrays.asList("1", "TestBuyer", "TestFiefdom").iterator();
    }

    @After
    public void tearDown() {
        reset(map, currentPlayers, fiefdom, buyerObj, knight, buyerObj2);
    }

    @Test
    public void testVassal() {
        // Given
        final int qty = 5;

        // When
        purchase.vassal(argIterator);

        // Then
        verify(fiefdom, times(1)).setLoyalVassals(qty, true);
        verify(buyerObj, times(1)).setTotalMoney(qty, false);
    }

    @Test
    public void testVassal_InsufficientFunds() {
        // Given
        final int qty = 6;
        when(buyerObj.getTotalMoney()).thenReturn(10);

        // When
        purchase.vassal(argIterator);

        // Then
        verify(buyerObj, times(1)).println("You cannot buy more vassals, you have insufficient funds");
    }

    @Test
    public void testVassal_InvalidFiefdom() {
        // Given
        when(map.get(argIterator.next())).thenReturn(null);

        // When
        purchase.vassal(argIterator);

        // Then
        verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
    }

    @Test
    public void testVassal_BuyerIsKing() {
        // Given
        when(buyerObj.getRank()).thenReturn("king");

        // When
        purchase.vassal(argIterator);

        // Then
        verify(fiefdom, times(1)).setLoyalVassals(qty, true);
        verify(buyerObj, times(1)).setTotalMoney(qty, false);
    }

    @Test
    public void testVassal_BuyerIsCountOrDuke() {
        // Given
        when(buyerObj.getRank()).thenReturn("count");

        // When
        purchase.vassal(argIterator);

        // Then
        verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
    }

    @Test
    public void testCastleFiefdom() {
        // Given
        final int qty = 2;

        // When
        purchase.castleFiefdom(argIterator);

        // Then
        verify(fiefdom, times(1)).buyFiefdomCastles(qty);
        verify(buyerObj, times(1)).setTotalMoney(qty * 25, false);
    }

    @Test
    public void testCastleFiefdom_InsufficientFunds() {
        // Given
        final int qty = 4;
        when(buyerObj.getTotalMoney()).thenReturn(30);

        // When
        purchase.castleFiefdom(argIterator);

        // Then
        verify(buyerObj, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
    }

    @Test
    public void testCastleFiefdom_InvalidFiefdom() {
        // Given
        when(map.get(argIterator.next())).thenReturn(null);

        // When
        purchase.castleFiefdom(argIterator);

        // Then
        verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
    }

    @Test
    public void testCastleFiefdom_BuyerIsKing() {
        // Given
        when(buyerObj.getRank()).thenReturn("king");

        // When
        purchase.castleFiefdom(argIterator);

        // Then
        verify(fiefdom, times(1)).buyFiefdomCastles(qty);
        verify(buyerObj, times(1)).setTotalMoney(qty * 25, false);
    }

    @Test
    public void testCastleFiefdom_BuyerIsCountOrDuke() {
        // Given
        when(buyerObj.getRank()).thenReturn("count");

        // When
        purchase.castleFiefdom(argIterator);

        // Then
        verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
    }

    @Test
    public void testCastlePersonal() {
        // Given
        final int qty = 3;

        // When
        purchase.castlePersonal(argIterator);

        // Then
        verify(fiefdom, times(1)).buyPersonalCastles(qty);
        verify(buyerObj, times(1)).setTotalMoney(qty * 10, false);
    }

    @Test
    public void testCastlePersonal_InsufficientFunds() {
        // Given
        final int qty = 5;
        when(buyerObj.getTotalMoney()).thenReturn(15);

        // When
        purchase.castlePersonal(argIterator);

        // Then
        verify(buyerObj, times(1)).println("You cannot buy more Personal castle, you have insufficient funds");
    }

    @Test
    public void testCastlePersonal_InvalidFiefdom() {
        // Given
        when(map.get(argIterator.next())).thenReturn(null);

        // When
        purchase.castlePersonal(argIterator);

        // Then
        verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom: " + fiefdom.getName() + " owner: " + Map.map.get(fiefdom.getName()).getOwner());
    }

    @Test
    public void testCastlePersonal_BuyerIsKing() {
        // Given
        when(buyerObj.getRank()).thenReturn("king");

        // When
        purchase.castlePersonal(argIterator);

        // Then
        verify(System.out, times(1)).println("You cannot buy a Personal Castle for Counts or Dukes");
    }

    @Test
    public void testCastlePersonal_BuyerIsCountOrDukeOwnsFiefdom() {
        // Given
        when(buyerObj.getRank()).thenReturn("count");
        when(buyerObj.isFiefdomMine(anyString())).thenReturn(true);

        // When
        purchase.castlePersonal(argIterator);

        // Then
        verify(fiefdom, times(1)).buyPersonalCastles(qty);
        verify(buyerObj, times(1)).setTotalMoney(qty * 10, false);
    }

    @Test
    public void testMercenary() {
        // Given
        final int qty = 7;

        // When
        purchase.mercenary(argIterator);

        // Then
        verify(knight, times(1)).addMercenaries(qty);
        verify(buyerObj, times(1)).setTotalMoney(qty, false);
    }

    @Test
    public void testMercenary_InsufficientFunds() {
        // Given
        final int qty = 8;
        when(buyerObj.getTotalMoney()).thenReturn(5);

        // When
        purchase.mercenary(argIterator);

        // Then
        verify(buyerObj, times(1)).println("You cannot buy more mercenaries, you have insufficient funds");
    }

    @Test
    public void testMercenary_BuyerIsDifferentKnight() {
        // Given
        argIterator = Arrays.asList("1", "TestBuyer2", "TestKnight").iterator();

        // When
        purchase.mercenary(argIterator);

        // Then
        verify(System.out, times(1)).println("Inexistent Knight");
    }

    @Test
    public void testMercenary_BuyerIsHimself() {
        // Given
        argIterator = Arrays.asList("1", "TestBuyer", "TestKnight").iterator();

        // When
        purchase.mercenary(argIterator);

        // Then
        verify(knight, times(1)).addMercenaries(qty);
        verify(buyerObj, times(1)).setTotalMoney(qty, false);
    }

    @Test
    public void testMercenary_BuyerIsKing() {
        // Given
        when(buyerObj.getRank()).thenReturn("king");

        // When
        purchase.mercenary(argIterator);

        // Then
        verify(knight, times(1)).addMercenaries(qty);
        verify(buyerObj, times(1)).setTotalMoney(qty, false);
    }
}