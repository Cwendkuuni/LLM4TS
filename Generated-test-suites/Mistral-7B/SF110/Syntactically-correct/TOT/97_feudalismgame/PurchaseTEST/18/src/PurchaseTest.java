package src.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PurchaseTest {

    private Purchase purchase;
    private Iterator<Object> argIterator;
    private Fiefdoms fiefdom;
    private Map map;
    private CurrentPlayers currentPlayers;
    private Knight buyer, knight, anotherPlayer;

    @Before
    public void setUp() {
        purchase = new Purchase();
        map = Mockito.mock(Map.class);
        currentPlayers = Mockito.mock(CurrentPlayers.class);

        fiefdom = new Fiefdoms("testFiefdom", "testOwner");
        buyer = new Knight("testBuyer", "testRank", 100);
        knight = new Knight("testKnight", "testRank", 50);
        anotherPlayer = new Knight("testAnotherPlayer", "testRank", 100);

        when(map.get(argThat(arg -> arg.equals("testFiefdom")))).thenReturn(fiefdom);
        when(currentPlayers.get(argThat(arg -> arg.equals("testBuyer")))).thenReturn(buyer);
        when(currentPlayers.get(argThat(arg -> arg.equals("testKnight")))).thenReturn(knight);
        when(currentPlayers.get(argThat(arg -> arg.equals("testAnotherPlayer")))).thenReturn(anotherPlayer);

        argIterator = Arrays.asList(new Object[]{"1", "testBuyer", "testFiefdom"}).iterator();
    }

    @Test
    public void testVassal() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(fiefdom.getViking().size()).thenReturn(0);
        when(fiefdom.getMagyar().size()).thenReturn(0);
        when(fiefdom.getMuslim().size()).thenReturn(0);

        purchase.perform(argIterator);
        verify(purchase, times(1)).vassal(argIterator);

        assertEquals(0, fiefdom.getLoyalVassals());
        assertEquals(100, buyer.getTotalMoney());
    }

    @Test
    public void testVassal_InsufficientFunds() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(fiefdom.getViking().size()).thenReturn(0);
        when(fiefdom.getMagyar().size()).thenReturn(0);
        when(fiefdom.getMuslim().size()).thenReturn(0);

        buyer.setTotalMoney(50);
        purchase.perform(argIterator);

        verify(purchase, times(1)).vassal(argIterator);
        verifyNoMoreInteractions(purchase);
        assertEquals(0, fiefdom.getLoyalVassals());
        assertEquals(50, buyer.getTotalMoney());
    }

    @Test
    public void testVassal_WrongFiefdomOwner() {
        when(fiefdom.getOwner()).thenReturn("testOwner2");

        purchase.perform(argIterator);

        verify(purchase, times(1)).vassal(argIterator);
        verifyNoMoreInteractions(purchase);
        assertEquals(0, fiefdom.getLoyalVassals());
    }

    @Test
    public void testVassal_BuyerIsKing() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(fiefdom.getViking().size()).thenReturn(0);
        when(fiefdom.getMagyar().size()).thenReturn(0);
        when(fiefdom.getMuslim().size()).thenReturn(0);

        buyer.setRank("king");

        purchase.perform(argIterator);

        verify(purchase, times(1)).vassal(argIterator);
        verifyNoMoreInteractions(purchase);
        assertEquals(1, fiefdom.getLoyalVassals());
        assertEquals(99, buyer.getTotalMoney());
    }

    @Test
    public void testVassal_BuyerOwnsFiefdom() {
        when(fiefdom.getOwner()).thenReturn(buyer.getName());

        purchase.perform(argIterator);

        verify(purchase, times(1)).vassal(argIterator);
        verifyNoMoreInteractions(purchase);
        assertEquals(1, fiefdom.getLoyalVassals());
        assertEquals(99, buyer.getTotalMoney());
    }

    @Test
    public void testVassal_CountOrDuke() {
        when(fiefdom.getOwner()).thenReturn(anotherPlayer.getName());

        purchase.perform(argIterator);

        verify(purchase, times(1)).vassal(argIterator);
        verifyNoMoreInteractions(purchase);
        assertEquals(0, fiefdom.getLoyalVassals());
        assertEquals(100, anotherPlayer.getTotalMoney());
    }

    @Test
    public void testCastleFiefdom() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(fiefdom.getViking().size()).thenReturn(0);
        when(fiefdom.getMagyar().size()).thenReturn(0);
        when(fiefdom.getMuslim().size()).thenReturn(0);

        purchase.perform(argIterator);
        verify(purchase, times(1)).castleFiefdom(argIterator);

        assertEquals(0, fiefdom.getCastleCount());
        assertEquals(100, buyer.getTotalMoney());
    }

    @Test
    public void testCastleFiefdom_InsufficientFunds() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(fiefdom.getViking().size()).thenReturn(0);
        when(fiefdom.getMagyar().size()).thenReturn(0);
        when(fiefdom.getMuslim().size()).thenReturn(0);

        buyer.setTotalMoney(50);
        purchase.perform(argIterator);

        verify(purchase, times(1)).castleFiefdom(argIterator);
        verifyNoMoreInteractions(purchase);
        assertEquals(0, fiefdom.getCastleCount());
        assertEquals(50, buyer.getTotalMoney());
    }

    @Test
    public void testCastleFiefdom_FiefdomAlreadyHasCastles() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(fiefdom.getViking().size()).thenReturn(0);
        when(fiefdom.getMagyar().size()).thenReturn(0);
        when(fiefdom.getMuslim().size()).thenReturn(0);
        when(fiefdom.getCastleCount()).thenReturn(1);

        purchase.perform(argIterator);

        verify(purchase, times(1)).castleFiefdom(argIterator);
        verifyNoMoreInteractions(purchase);
        assertEquals(1, fiefdom.getCastleCount());
        assertEquals(100, buyer.getTotalMoney());
    }

    @Test
    public void testCastleFiefdom_CountOrDuke() {
        when(fiefdom.getOwner()).thenReturn(anotherPlayer.getName());

        purchase.perform(argIterator);

        verify(purchase, times(1)).castleFiefdom(argIterator);
        verifyNoMoreInteractions(purchase);
        assertEquals(0, fiefdom.getCastleCount());
        assertEquals(100, anotherPlayer.getTotalMoney());
    }

    @Test
    public void testCastlePersonal() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(fiefdom.getViking().size()).thenReturn(0);
        when(fiefdom.getMagyar().size()).thenReturn(0);
        when(fiefdom.getMuslim().size()).thenReturn(0);

        purchase.perform(argIterator);
        verify(purchase, times(1)).castlePersonal(argIterator);

        assertEquals(0, fiefdom.getPersonalCastleCount());
        assertEquals(100, buyer.getTotalMoney());
    }

    @Test
    public void testCastlePersonal_InsufficientFunds() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(fiefdom.getViking().size()).thenReturn(0);
        when(fiefdom.getMagyar().size()).thenReturn(0);
        when(fiefdom.getMuslim().size()).thenReturn(0);

        buyer.setTotalMoney(50);
        purchase.perform(argIterator);

        verify(purchase, times(1)).castlePersonal(argIterator);
        verifyNoMoreInteractions(purchase);
        assertEquals(0, fiefdom.getPersonalCastleCount());
        assertEquals(50, buyer.getTotalMoney());
    }

    @Test
    public void testCastlePersonal_FiefdomAlreadyHasCastles() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(fiefdom.getViking().size()).thenReturn(0);
        when(fiefdom.getMagyar().size()).thenReturn(0);
        when(fiefdom.getMuslim().size()).thenReturn(0);
        when(fiefdom.getPersonalCastleCount()).thenReturn(1);

        purchase.perform(argIterator);

        verify(purchase, times(1)).castlePersonal(argIterator);
        verifyNoMoreInteractions(purchase);
        assertEquals(1, fiefdom.getPersonalCastleCount());
        assertEquals(100, buyer.getTotalMoney());
    }

    @Test
    public void testCastlePersonal_King() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(fiefdom.getViking().size()).thenReturn(0);
        when(fiefdom.getMagyar().size()).thenReturn(0);
        when(fiefdom.getMuslim().size()).thenReturn(0);

        buyer.setRank("king");

        purchase.perform(argIterator);

        verify(purchase, times(1)).castlePersonal(argIterator);
        verifyNoMoreInteractions(purchase);
        assertEquals(0, fiefdom.getPersonalCastleCount());
        assertEquals(100, buyer.getTotalMoney());
    }

    @Test
    public void testCastlePersonal_FiefdomDoesntBelongToKingdom() {
        when(fiefdom.getOwner()).thenReturn("testOwner2");

        purchase.perform(argIterator);

        verify(purchase, times(1)).castlePersonal(argIterator);
        verifyNoMoreInteractions(purchase);
        assertEquals(0, fiefdom.getPersonalCastleCount());
        assertEquals(100, buyer.getTotalMoney());
    }

    @Test
    public void testMercenary() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(knight.getTotalMoney()).thenReturn(100);

        purchase.perform(argIterator);
        verify(purchase, times(1)).mercenary(argIterator);

        assertEquals(0, knight.getMercenaries());
        assertEquals(95, buyer.getTotalMoney());
    }

    @Test
    public void testMercenary_InsufficientFunds() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(knight.getTotalMoney()).thenReturn(50);

        purchase.perform(argIterator);

        verify(purchase, times(1)).mercenary(argIterator);
        verifyNoMoreInteractions(purchase);
        assertEquals(0, knight.getMercenaries());
        assertEquals(50, buyer.getTotalMoney());
    }

    @Test
    public void testMercenary_BuyerIsKing() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(knight.getTotalMoney()).thenReturn(100);

        buyer.setRank("king");

        purchase.perform(argIterator);

        verify(purchase, times(1)).mercenary(argIterator);
        verifyNoMoreInteractions(purchase);
        assertEquals(1, knight.getMercenaries());
        assertEquals(99, buyer.getTotalMoney());
    }

    @Test
    public void testMercenary_InexistentKnight() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(currentPlayers.get(argThat(arg -> arg.equals("inexistentKnight")))).thenReturn(null);

        purchase.perform(argIterator);

        verify(purchase, times(1)).mercenary(argIterator);
        verifyNoMoreInteractions(purchase);
        assertEquals(0, knight.getMercenaries());
    }
}