package src.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PurchaseTest {

    private Purchase purchase;
    private Iterator mockedArgs;
    private Fiefdoms fiefdom;
    private Map map;
    private CurrentPlayers currentPlayers;
    private Knight buyerObj;
    private Knight buyerObj2;
    private Knight knight;

    @Before
    public void setUp() {
        map = Mockito.mock(Map.class);
        currentPlayers = Mockito.mock(CurrentPlayers.class);
        fiefdom = Mockito.mock(Fiefdoms.class);
        buyerObj = Mockito.mock(Knight.class);
        buyerObj2 = Mockito.mock(Knight.class);
        knight = Mockito.mock(Knight.class);
        when(map.get(Mockito.anyString())).thenReturn(fiefdom);
        when(currentPlayers.get(Mockito.anyString())).thenReturn(buyerObj);
        when(currentPlayers.get(Mockito.anyString())).thenReturn(buyerObj2);
        when(fiefdom.getOwner()).thenReturn(buyerObj2);
        when(fiefdom.getName()).thenReturn("testFiefdom");
        when(buyerObj.getName()).thenReturn("testBuyer");
        when(buyerObj.getRank()).thenReturn("count");
        when(buyerObj2.getName()).thenReturn("testOwner");
        when(buyerObj2.getRank()).thenReturn("king");
        when(buyerObj.isFiefdomMine(Mockito.anyString())).thenReturn(true);
        when(buyerObj.getTotalMoney()).thenReturn(100);
        when(knight.getName()).thenReturn("testKnight");

        purchase = new Purchase();
        mockedArgs = Arrays.asList(Mockito.mock(Iterator.class).mockConstructions(Collections.emptyList().iterator()),
                Mockito.mock(Iterator.class).mockConstructions(Arrays.asList("3", "testBuyer", "testFiefdom").iterator()))
                .iterator();
    }

    @Test
    public void testVassal() {
        purchase.perform(Arrays.asList(mockedArgs));
        verify(fiefdom, times(1)).setLoyalVassals(3, true);
        verify(buyerObj, times(1)).setTotalMoney(3, false);
    }

    @Test(expected = IllegalStateException.class)
    public void testVassalOwnerNotInTheKingdom() {
        when(fiefdom.getOwner()).thenReturn(null);
        purchase.perform(Arrays.asList(mockedArgs));
        fail("Expected an IllegalStateException, but nothing was thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVassalBuyerDoesntHaveTheMoney() {
        when(buyerObj.getTotalMoney()).thenReturn(0);
        purchase.perform(Arrays.asList(mockedArgs));
        fail("Expected an IllegalArgumentException, but nothing was thrown.");
    }

    @Test
    public void testCastleFiefdom() {
        purchase.perform(Arrays.asList(mockedArgs));
        verify(fiefdom, times(1)).buyFiefdomCastles(1);
        verify(buyerObj, times(1)).setTotalMoney(25, false);
    }

    @Test(expected = IllegalStateException.class)
    public void testCastleFiefdomFiefdomDoesntBelongToTheKingdom() {
        when(fiefdom.getOwner()).thenReturn(null);
        purchase.perform(Arrays.asList(mockedArgs));
        fail("Expected an IllegalStateException, but nothing was thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCastleFiefdomBuyerDoesntHaveTheMoney() {
        when(buyerObj.getTotalMoney()).thenReturn(0);
        purchase.perform(Arrays.asList(mockedArgs));
        fail("Expected an IllegalArgumentException, but nothing was thrown.");
    }

    @Test
    public void testCastlePersonal() {
        purchase.perform(Arrays.asList(mockedArgs));
        verify(fiefdom, times(1)).buyPersonalCastles(1);
        verify(buyerObj, times(1)).setTotalMoney(10, false);
    }

    @Test(expected = IllegalStateException.class)
    public void testCastlePersonalFiefdomDoesntBelongToTheKingdom() {
        when(fiefdom.getOwner()).thenReturn(null);
        purchase.perform(Arrays.asList(mockedArgs));
        fail("Expected an IllegalStateException, but nothing was thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCastlePersonalBuyerDoesntHaveTheMoney() {
        when(buyerObj.getTotalMoney()).thenReturn(0);
        purchase.perform(Arrays.asList(mockedArgs));
        fail("Expected an IllegalArgumentException, but nothing was thrown.");
    }

    @Test
    public void testMercenary() {
        purchase.perform(Arrays.asList(mockedArgs));
        verify(knight, times(1)).addMercenaries(3);
        verify(buyerObj, times(1)).setTotalMoney(3, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMercenaryBuyerDoesntHaveTheMoney() {
        when(buyerObj.getTotalMoney()).thenReturn(0);
        purchase.perform(Arrays.asList(mockedArgs));
        fail("Expected an IllegalArgumentException, but nothing was thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMercenaryBuyerIsTheSameKnight() {
        when(knight.getName()).thenReturn(buyerObj.getName());
        purchase.perform(Arrays.asList(mockedArgs));
        fail("Expected an IllegalArgumentException, but nothing was thrown.");
    }
}