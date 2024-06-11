package src.test.java;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseTest {

    @InjectMocks
    private Purchase purchase;
    @SuppressWarnings("unchecked")
    private final Iterator<Object> vassalArgs = Mockito.mock(Iterator.class);
    private final Iterator<Object> castleFiefdomArgs = Mockito.mock(Iterator.class);
    private final Iterator<Object> castlePersonalArgs = Mockito.mock(Iterator.class);
    private final Iterator<Object> mercenaryArgs = Mockito.mock(Iterator.class);
    private final String buyerName = "testBuyer";
    private final String fiefdomName = "testFiefdom";
    private final String knightName = "testKnight";
    private final Fiefdoms fiefdom = new Fiefdoms();
    private final Knight buyer = new Knight();
    private final Knight knight = new Knight();

    static {
        fiefdom.setName(fiefdomName);
        fiefdom.setOwner(null);
        buyer.setName(buyerName);
        buyer.setRank("testRank");
        knight.setName(knightName);
        knight.setRank("testKnightRank");
        CurrentPlayers.currentPlayers.put(buyerName, buyer);
        CurrentPlayers.currentPlayers.put(knightName, knight);
        Map.map.put(fiefdomName, fiefdom);
    }

    @Test
    public void testVassal() {
        final int qty = 1;
        when(vassalArgs.next()).thenReturn(Integer.toString(qty));
        when(vassalArgs.next()).thenReturn(buyerName);
        when(vassalArgs.next()).thenReturn(fiefdomName);

        purchase.vassal(vassalArgs);

        Mockito.verify(buyer, times(1)).setTotalMoney(qty, false);
        Mockito.verify(fiefdom, times(1)).setLoyalVassals(qty, true);

        verify(buyer, times(1)).getTotalMoney();
        assertEquals(buyer.getTotalMoney() - qty * 1, buyer.getTotalMoney());

        verify(buyer, times(1)).getName();
        assertEquals(buyerName, buyer.getName());

        verify(fiefdom, times(1)).getName();
        assertEquals(fiefdomName, fiefdom.getName());

        verify(fiefdom, times(1)).getOwner();
        assertNull(fiefdom.getOwner());

        verify(fiefdom, times(1)).getVassals().size();
        assertEquals(fiefdom.getVassals().size() + qty, fiefdom.getVassals().size());

        verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
        verify(System.out, never()).println("You can only buy a castle for a fiefdom that belongs to you");
        verify(System.out, never()).println("Fiefdom doesn't belong to the kingdom");
    }

    @Test
    public void testCastleFiefdom() {
        final int qty = 1;
        when(castleFiefdomArgs.next()).thenReturn(Integer.toString(qty));
        when(castleFiefdomArgs.next()).thenReturn(buyerName);
        when(castleFiefdomArgs.next()).thenReturn(fiefdomName);

        purchase.castleFiefdom(castleFiefdomArgs);

        Mockito.verify(buyer, times(1)).setTotalMoney(qty * 25, false);
        Mockito.verify(fiefdom, times(1)).buyFiefdomCastles(qty);

        verify(buyer, times(1)).getTotalMoney();
        assertEquals(buyer.getTotalMoney() - qty * 25, buyer.getTotalMoney());

        verify(buyer, times(1)).getName();
        assertEquals(buyerName, buyer.getName());

        verify(fiefdom, times(1)).getName();
        assertEquals(fiefdomName, fiefdom.getName());

        verify(fiefdom, times(1)).getOwner();
        assertNull(fiefdom.getOwner());

        verify(fiefdom, times(1)).getViking().size();
        verify(fiefdom, times(1)).getMagyar().size();
        verify(fiefdom, times(1)).getMuslim().size();
        assertEquals(fiefdom.getViking().size() + fiefdom.getMagyar().size() + fiefdom.getMuslim().size(), 0);

        verify(System.out, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
        verify(System.out, never()).println("You can only buy a castle for a fiefdom that belongs to you");
        verify(System.out, never()).println("Fiefdom doesn't belong to the kingdom");
    }

    @Test
    public void testCastlePersonal() {
        final int qty = 1;
        when(castlePersonalArgs.next()).thenReturn(Integer.toString(qty));
        when(castlePersonalArgs.next()).thenReturn(buyerName);
        when(castlePersonalArgs.next()).thenReturn(fiefdomName);

        purchase.castlePersonal(castlePersonalArgs);

        Mockito.verify(buyer, times(1)).setTotalMoney(qty * 10, false);
        Mockito.verify(fiefdom, times(1)).buyPersonalCastles(qty);

        verify(buyer, times(1)).getTotalMoney();
        assertEquals(buyer.getTotalMoney() - qty * 10, buyer.getTotalMoney());

        verify(buyer, times(1)).getName();
        assertEquals(buyerName, buyer.getName());

        verify(fiefdom, times(1)).getName();
        assertEquals(fiefdomName, fiefdom.getName());

        verify(fiefdom, times(1)).getOwner();
        assertNull(fiefdom.getOwner());

        verify(fiefdom, times(1)).getViking().size();
        verify(fiefdom, times(1)).getMagyar().size();
        verify(fiefdom, times(1)).getMuslim().size();
        assertEquals(fiefdom.getViking().size() + fiefdom.getMagyar().size() + fiefdom.getMuslim().size(), 0);

        verify(knight, times(1)).addMercenaries(0);
        verify(System.out, never()).println("You cannot buy a Personal Castle for Counts or Dukes");
        verify(System.out, never()).println("You can only buy a castle for a fiefdom that belongs to you");
        verify(System.out, never()).println("Fiefdom doesn't belong to the kingdom");
    }

    @Test
    public void testMercenary() {
        final int qty = 1;
        when(mercenaryArgs.next()).thenReturn(Integer.toString(qty));
        when(mercenaryArgs.next()).thenReturn(buyerName);
        when(mercenaryArgs.next()).thenReturn(knightName);

        purchase.mercenary(mercenaryArgs);

        Mockito.verify(buyer, times(1)).setTotalMoney(qty, false);
        Mockito.verify(knight, times(1)).addMercenaries(qty);

        verify(buyer, times(1)).getTotalMoney();
        assertEquals(buyer.getTotalMoney() - qty, buyer.getTotalMoney());

        verify(buyer, times(1)).getName();
        assertEquals(buyerName, buyer.getName());

        verify(knight, times(1)).getName();
        assertEquals(knightName, knight.getName());

        verify(knight, times(1)).getTotalMoney();
        assertEquals(knight.getTotalMoney() + qty, knight.getTotalMoney());

        verify(System.out, times(1)).println("Inexistent Knight");
        verify(System.out, never()).println("You cannot buy more mercenaries, you have insufficient funds");
        verify(System.out, never()).println("You cannot buy a Personal Castle for Counts or Dukes");
        verify(System.out, never()).println("You can only buy a castle for a fiefdom that belongs to you");
        verify(System.out, never()).println("Fiefdom doesn't belong to the kingdom");
    }
}