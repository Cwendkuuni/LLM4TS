package src;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PurchaseTest {

    private Purchase purchase;
    private Iterator<String> argIterator;
    private Fiefdoms fiefdom;
    private Knight buyer, buyerObj, knight;

    @Before
    public void setup() {
        purchase = new Purchase();
        argIterator = Mockito.mock(Iterator.class);
        fiefdom = Mockito.mock(Fiefdoms.class);
        buyer = Mockito.mock(Knight.class);
        buyerObj = Mockito.mock(Knight.class);
        knight = Mockito.mock(Knight.class);
    }

    @Test
    public void testVassal() {
        when(argIterator.next()).thenReturn("3").thenReturn("John").thenReturn("Castle1").thenReturn(null);
        when(Map.map.get(Mockito.anyString())).thenReturn(fiefdom);
        when(CurrentPlayers.currentPlayers.get(Mockito.anyString())).thenReturn(buyerObj);
        when(buyerObj.getRank()).thenReturn("duke");
        when(fiefdom.getOwner()).thenReturn(null);
        when(fiefdom.getName()).thenReturn("Castle1");
        when(fiefdom.getVassals()).thenReturn(Collections.emptyList());

        purchase.perform(Collections.singletonList(argIterator));

        verify(buyerObj, Mockito.times(0)).setTotalMoney(Mockito.anyInt(), Mockito.anyBoolean());
        verify(fiefdom, Mockito.times(0)).setLoyalVassals(Mockito.anyInt(), Mockito.anyBoolean());
        verify(System.out, Mockito.times(1)).println("Fiefdom doesn't belong to the kingdom: Castle1 owner: null");
    }

    @Test
    public void testVassal_Success() {
        when(argIterator.next()).thenReturn("3").thenReturn("John").thenReturn("Castle1").thenReturn(buyer);
        when(Map.map.get(Mockito.anyString())).thenReturn(fiefdom);
        when(CurrentPlayers.currentPlayers.get(Mockito.anyString())).thenReturn(buyerObj);
        when(buyerObj.getRank()).thenReturn("duke");
        when(fiefdom.getOwner()).thenReturn(buyer);
        when(fiefdom.getName()).thenReturn("Castle1");
        when(buyer.getRank()).thenReturn("knight");
        when(buyer.isFiefdomMine(Mockito.anyString())).thenReturn(true);

        purchase.perform(Collections.singletonList(argIterator));

        verify(buyerObj, Mockito.times(1)).setTotalMoney(3, false);
        verify(fiefdom, Mockito.times(1)).setLoyalVassals(3, true);
    }

    @Test
    public void testCastleFiefdom() {
        when(argIterator.next()).thenReturn("3").thenReturn("John").thenReturn("Castle1").thenReturn(null);
        when(Map.map.get(Mockito.anyString())).thenReturn(fiefdom);
        when(CurrentPlayers.currentPlayers.get(Mockito.anyString())).thenReturn(buyerObj);
        when(buyerObj.getRank()).thenReturn("duke");
        when(fiefdom.getOwner()).thenReturn(null);

        purchase.perform(Collections.singletonList(argIterator));

        verify(buyerObj, Mockito.times(0)).setTotalMoney(Mockito.anyInt(), Mockito.anyBoolean());
        verify(fiefdom, Mockito.times(0)).buyFiefdomCastles(Mockito.anyInt());
        verify(System.out, Mockito.times(1)).println("Fiefdom doesn't belong to the kingdom: Castle1 owner: null");
    }

    @Test
    public void testCastleFiefdom_Success() {
        when(argIterator.next()).thenReturn("3").thenReturn("John").thenReturn("Castle1").thenReturn(buyerObj);
        when(Map.map.get(Mockito.anyString())).thenReturn(fiefdom);
        when(CurrentPlayers.currentPlayers.get(Mockito.anyString())).thenReturn(buyerObj);
        when(buyerObj.getRank()).thenReturn("king");
        when(fiefdom.getOwner()).thenReturn(null);
        when(fiefdom.getName()).thenReturn("Castle1");
        when(fiefdom.getViking()).thenReturn(Collections.emptyList());
        when(fiefdom.getMagyar()).thenReturn(Collections.emptyList());
        when(fiefdom.getMuslim()).thenReturn(Collections.emptyList());

        purchase.perform(Collections.singletonList(argIterator));

        verify(buyerObj, Mockito.times(1)).setTotalMoney(75, false);
        verify(fiefdom, Mockito.times(1)).buyFiefdomCastles(3);
    }

    @Test
    public void testCastlePersonal() {
        when(argIterator.next()).thenReturn("3").thenReturn("John").thenReturn("Castle1").thenReturn(null);
        when(Map.map.get(Mockito.anyString())).thenReturn(fiefdom);
        when(CurrentPlayers.currentPlayers.get(Mockito.anyString())).thenReturn(buyerObj);
        when(buyerObj.getRank()).thenReturn("duke");
        when(fiefdom.getOwner()).thenReturn(null);

        purchase.perform(Collections.singletonList(argIterator));

        verify(buyerObj, Mockito.times(0)).setTotalMoney(Mockito.anyInt(), Mockito.anyBoolean());
        verify(fiefdom, Mockito.times(0)).buyPersonalCastles(Mockito.anyInt());
        verify(System.out, Mockito.times(1)).println("Fiefdom doesn't belong to the kingdom: Castle1 owner: null");
    }

    @Test
    public void testCastlePersonal_Success() {
        when(argIterator.next()).thenReturn("3").thenReturn("John").thenReturn("Castle1").thenReturn(buyerObj);
        when(Map.map.get(Mockito.anyString())).thenReturn(fiefdom);
        when(CurrentPlayers.currentPlayers.get(Mockito.anyString())).thenReturn(buyerObj);
        when(buyerObj.getRank()).thenReturn("king");
        when(fiefdom.getOwner()).thenReturn(buyer);
        when(fiefdom.getName()).thenReturn("Castle1");
        when(fiefdom.getViking()).thenReturn(Collections.emptyList());
        when(fiefdom.getMagyar()).thenReturn(Collections.emptyList());
        when(fiefdom.getMuslim()).thenReturn(Collections.emptyList());

        purchase.perform(Collections.singletonList(argIterator));

        verify(buyerObj, Mockito.times(1)).setTotalMoney(30, false);
        verify(fiefdom, Mockito.times(1)).buyPersonalCastles(3);
    }

    @Test
    public void testMercenary() {
        when(argIterator.next()).thenReturn("3").thenReturn("John").thenReturn("Knight1").thenReturn(knight);
        when(CurrentPlayers.currentPlayers.get(Mockito.anyString())).thenReturn(buyerObj);
        when(CurrentPlayers.currentPlayers.get(Mockito.anyString())).thenReturn(buyer);

        purchase.perform(Collections.singletonList(argIterator));

        verify(buyerObj, Mockito.times(0)).setTotalMoney(Mockito.anyInt(), Mockito.anyBoolean());
        verify(knight, Mockito.times(0)).addMercenaries(Mockito.anyInt());
        verify(System.out, Mockito.times(1)).println("Inexistent Knight");
    }

    @Test
    public void testMercenary_Success() {
        when(argIterator.next()).thenReturn("3").thenReturn("John").thenReturn("Knight1").thenReturn(knight);
        when(CurrentPlayers.currentPlayers.get(Mockito.anyString())).thenReturn(buyerObj);
        when(CurrentPlayers.currentPlayers.get(Mockito.anyString())).thenReturn(buyer);
        when(buyerObj.getName()).thenReturn("John");
        when(buyer.getName()).thenReturn("John");

        purchase.perform(Collections.singletonList(argIterator));

        verify(buyerObj, Mockito.times(1)).setTotalMoney(3, false);
        verify(knight, Mockito.times(1)).addMercenaries(3);
    }
}