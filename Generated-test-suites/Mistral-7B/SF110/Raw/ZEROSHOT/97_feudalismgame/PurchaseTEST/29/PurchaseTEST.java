package src.test.java;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;

public class PurchaseTest {

    private Purchase purchase;
    private Fiefdoms fiefdom;
    private Map map;
    private CurrentPlayers currentPlayers;
    private Knight buyerObj;
    private Knight knight;

    @Before
    public void setup() {
        map = Mockito.mock(Map.class);
        currentPlayers = Mockito.mock(CurrentPlayers.class);
        purchase = new Purchase();
        purchase.setMap(map);
        purchase.setCurrentPlayers(currentPlayers);

        fiefdom = new Fiefdoms();
        fiefdom.setName("testFiefdom");
        fiefdom.setOwner(null);

        buyerObj = new Knight();
        buyerObj.setName("testBuyer");
        buyerObj.setRank("testRank");

        knight = new Knight();
        knight.setName("testKnight");
        knight.setRank("testKnightRank");

        CurrentPlayers.currentPlayers.put(buyerObj.getName(), buyerObj);
        CurrentPlayers.currentPlayers.put(knight.getName(), knight);
    }

    @Test
    public void testVassal() {
        Iterator<Object> arg = Arrays.asList("3", "testBuyer", "testFiefdom").iterator();

        when(map.get(arg.next())).thenReturn(fiefdom);

        purchase.vassal(arg);

        assertEquals(3, fiefdom.getLoyalVassals());
        verify(currentPlayers, times(1)).setTotalMoney(eq(3), eq(false));
    }

    @Test
    public void testVassal_InsufficientFunds() {
        Iterator<Object> arg = Arrays.asList("4", "testBuyer", "testFiefdom").iterator();

        when(map.get(arg.next())).thenReturn(fiefdom);

        purchase.vassal(arg);

        assertEquals(0, fiefdom.getLoyalVassals());
        verify(currentPlayers, never()).setTotalMoney(eq(4), eq(false));
        verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
    }

    @Test
    public void testVassal_NotOwnedFiefdom() {
        Iterator<Object> arg = Arrays.asList("3", "testBuyer", "testNotOwnedFiefdom").iterator();

        when(map.get(arg.next())).thenReturn(new Fiefdoms());

        purchase.vassal(arg);

        assertEquals(0, fiefdom.getLoyalVassals());
        verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
    }

    @Test
    public void testVassal_NotYourFiefdom() {
        Iterator<Object> arg = Arrays.asList("3", "testBuyer1", "testFiefdom").iterator();

        when(map.get(arg.next())).thenReturn(fiefdom);
        when(currentPlayers.get(fiefdom.getOwner())).thenReturn(new Knight());

        purchase.vassal(arg);

        assertEquals(0, fiefdom.getLoyalVassals());
        verify(System.out, times(1)).println("You can only buy a vassal for a fiefdom that belongs to you");
    }

    @Test
    public void testVassal_KingBuyer() {
        Iterator<Object> arg = Arrays.asList("3", "testKing", "testFiefdom").iterator();

        when(map.get(arg.next())).thenReturn(fiefdom);

        purchase.vassal(arg);

        assertEquals(3, fiefdom.getLoyalVassals());
        verify(currentPlayers, times(1)).setTotalMoney(eq(3), eq(false));
    }

    @Test
    public void testCastleFiefdom() {
        Iterator<Object> arg = Arrays.asList("3", "testBuyer", "testFiefdom").iterator();

        when(map.get(arg.next())).thenReturn(fiefdom);

        purchase.castleFiefdom(arg);

        assertEquals(3 * 25, fiefdom.getTotalCastles());
        verify(currentPlayers, times(1)).setTotalMoney(eq(75), eq(false));
    }

    // ... Add other test methods for castlePersonal, castleFiefdom, and mercenary here

    @Test
    public void testMercenary() {
        Iterator<Object> arg = Arrays.asList("2", "testBuyer", "testKnight").iterator();

        purchase.mercenary(arg);

        assertEquals(2, knight.getMercenaries());
        verify(currentPlayers, times(1)).setTotalMoney(eq(2), eq(false));
    }

    @Test
    public void testMercenary_InsufficientFunds() {
        Iterator<Object> arg = Arrays.asList("3", "testBuyer", "testKnight").iterator();

        purchase.mercenary(arg);

        assertEquals(0, knight.getMercenaries());
        verify(currentPlayers, never()).setTotalMoney(eq(3), eq(false));
        verify(System.out, times(1)).println("You cannot buy more mercenaries, you have insufficient funds");
    }

    @Test
    public void testMercenary_InvalidBuyer() {
        Iterator<Object> arg = Arrays.asList("2", "testInvalidBuyer", "testKnight").iterator();

        purchase.mercenary(arg);

        assertEquals(0, knight.getMercenaries());
        verify(System.out, times(1)).println("Inexistent Knight");
    }

    @Test
    public void testMercenary_InvalidBuyerRank() {
        Iterator<Object> arg = Arrays.asList("2", "testInvalidBuyerRank", "testKnight").iterator();

        purchase.mercenary(arg);

        assertEquals(0, knight.getMercenaries());
        verify(System.out, times(1)).println("You cannot buy a mercenary for a Count or a Duke");
    }
}