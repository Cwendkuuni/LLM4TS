package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PurchaseTest {

    private Purchase purchase;
    private Collection args;
    private Iterator iterator;
    private Fiefdoms fiefdomMock;
    private CurrentPlayers currentPlayersMock;
    private Map mapMock;

    @Before
    public void setUp() {
        purchase = new Purchase();
        args = mock(Collection.class);
        iterator = mock(Iterator.class);
        fiefdomMock = Mockito.mock(Fiefdoms.class);
        currentPlayersMock = Mockito.mock(CurrentPlayers.class);
        mapMock = Mockito.mock(Map.class);

        when(args.iterator()).thenReturn(iterator);
        when(mapMock.get(anyString())).thenReturn(fiefdomMock);
        when(currentPlayersMock.get(anyString())).thenAnswer(invocation -> CurrentPlayers.currentPlayers.get(invocation.getArgument(0)));

        purchase.setMap(mapMock);
        purchase.setCurrentPlayers(currentPlayersMock);
    }

    @Test
    public void testPerformEmptyArgs() {
        when(args.size()).thenReturn(0);

        purchase.perform(args);

        verify(purchase, times(1)).perform(args);
    }

    @Test
    public void testPerformVassal() {
        final String arg1 = "vassal";
        final int qty = 5;
        final String buyerName = "John";
        final String fiefdomName = "fiefdom1";
        final Fiefdoms fiefdom = new Fiefdoms();
        final Knight buyerObj = new Knight();
        final Knight vassal = new Knight();

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(fiefdomName);

        when(fiefdom.getOwner()).thenReturn(null);
        when(fiefdom.getName()).thenReturn(fiefdomName);
        when(buyerObj.getRank()).thenReturn("knight");
        when(buyerObj.getName()).thenReturn(buyerName);
        when(buyerObj.isFiefdomMine(fiefdomName)).thenReturn(false);

        purchase.perform(args);

        verify(purchase, times(1)).vassal(iterator);
    }

    @Test
    public void testPerformVassalFiefdomMine() {
        final String arg1 = "vassal";
        final int qty = 5;
        final String buyerName = "John";
        final String fiefdomName = "fiefdom1";
        final Fiefdoms fiefdom = new Fiefdoms();
        final Knight buyerObj = new Knight();
        final Knight vassal = new Knight();

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(fiefdomName);

        when(fiefdom.getOwner()).thenReturn(CurrentPlayers.currentPlayers.get("John"));
        when(fiefdom.getName()).thenReturn(fiefdomName);
        when(buyerObj.getRank()).thenReturn("knight");
        when(buyerObj.getName()).thenReturn(buyerName);
        when(buyerObj.isFiefdomMine(fiefdomName)).thenReturn(true);

        purchase.perform(args);

        verify(purchase, times(1)).vassal(iterator);
    }

    @Test
    public void testPerformVassalOwnerOtherKing() {
        final String arg1 = "vassal";
        final int qty = 5;
        final String buyerName = "John";
        final String fiefdomName = "fiefdom1";
        final Fiefdoms fiefdom = new Fiefdoms();
        final Knight buyerObj = new Knight();
        final Knight vassal = new Knight();

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(fiefdomName);

        when(fiefdom.getOwner()).thenReturn(CurrentPlayers.currentPlayers.get("Alice"));
        when(fiefdom.getName()).thenReturn(fiefdomName);
        when(buyerObj.getRank()).thenReturn("king");
        when(buyerObj.getName()).thenReturn(buyerName);

        purchase.perform(args);

        verify(purchase, times(1)).perform(args);
    }

    @Test
    public void testPerformVassalOwnerSameRank() {
        final String arg1 = "vassal";
        final int qty = 5;
        final String buyerName = "John";
        final String fiefdomName = "fiefdom1";
        final Fiefdoms fiefdom = new Fiefdoms();
        final Knight buyerObj = new Knight();
        final Knight vassal = new Knight();

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(fiefdomName);

        when(fiefdom.getOwner()).thenReturn(CurrentPlayers.currentPlayers.get(buyerName));
        when(fiefdom.getName()).thenReturn(fiefdomName);
        when(buyerObj.getRank()).thenReturn("knight");

        purchase.perform(args);

        verify(purchase, times(1)).perform(args);
    }

    @Test
    public void testPerformVassalOwnerCountOrDuke() {
        final String arg1 = "vassal";
        final int qty = 5;
        final String buyerName = "John";
        final String fiefdomName = "fiefdom1";
        final Fiefdoms fiefdom = new Fiefdoms();
        final Knight buyerObj = new Knight();

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(fiefdomName);

        when(fiefdom.getOwner()).thenReturn(CurrentPlayers.currentPlayers.get("Alice"));
        when(fiefdom.getName()).thenReturn(fiefdomName);
        when(buyerObj.getRank()).thenReturn("count");

        purchase.perform(args);

        verify(purchase, times(1)).perform(args);
    }

    @Test
    public void testPerformCastleFiefdom() {
        final String arg1 = "castleFiefdom";
        final int qty = 5;
        final String buyerName = "John";
        final String fiefdomName = "fiefdom1";
        final Fiefdoms fiefdom = new Fiefdoms();
        final Knight buyerObj = new Knight();

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(fiefdomName);

        when(fiefdom.getOwner()).thenReturn(CurrentPlayers.currentPlayers.get(buyerName));
        when(fiefdom.getName()).thenReturn(fiefdomName);
        when(fiefdom.getViking().size()).thenReturn(0);
        when(fiefdom.getMagyar().size()).thenReturn(0);
        when(fiefdom.getMuslim().size()).thenReturn(0);

        purchase.perform(args);

        verify(purchase, times(1)).castleFiefdom(iterator);
    }

    @Test
    public void testPerformCastleFiefdomFiefdomMine() {
        final String arg1 = "castleFiefdom";
        final int qty = 5;
        final String buyerName = "John";
        final String fiefdomName = "fiefdom1";
        final Fiefdoms fiefdom = new Fiefdoms();
        final Knight buyerObj = new Knight();

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(fiefdomName);

        when(fiefdom.getOwner()).thenReturn(CurrentPlayers.currentPlayers.get(buyerName));
        when(fiefdom.getName()).thenReturn(fiefdomName);
        when(fiefdom.getViking().size()).thenReturn(2);
        when(fiefdom.getMagyar().size()).thenReturn(3);
        when(fiefdom.getMuslim().size()).thenReturn(1);

        purchase.perform(args);

        verify(purchase, times(1)).perform(args);
    }

    @Test
    public void testPerformCastleFiefdomOwnerOtherKing() {
        final String arg1 = "castleFiefdom";
        final int qty = 5;
        final String buyerName = "John";
        final String fiefdomName = "fiefdom1";
        final Fiefdoms fiefdom = new Fiefdoms();
        final Knight buyerObj = new Knight();

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(fiefdomName);

        when(fiefdom.getOwner()).thenReturn(CurrentPlayers.currentPlayers.get("Alice"));
        when(fiefdom.getName()).thenReturn(fiefdomName);

        purchase.perform(args);

        verify(purchase, times(1)).perform(args);
    }

    @Test
    public void testPerformCastleFiefdomOwnerCountOrDuke() {
        final String arg1 = "castleFiefdom";
        final int qty = 5;
        final String buyerName = "John";
        final String fiefdomName = "fiefdom1";
        final Fiefdoms fiefdom = new Fiefdoms();
        final Knight buyerObj = new Knight();

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(fiefdomName);

        when(fiefdom.getOwner()).thenReturn(CurrentPlayers.currentPlayers.get("Alice"));
        when(fiefdom.getName()).thenReturn(fiefdomName);

        purchase.perform(args);

        verify(purchase, times(1)).perform(args);
    }

    @Test
    public void testPerformCastlePersonal() {
        final String arg1 = "castlePersonal";
        final int qty = 5;
        final String buyerName = "John";
        final String fiefdomName = "fiefdom1";
        final Fiefdoms fiefdom = new Fiefdoms();
        final Knight buyerObj = new Knight();

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(fiefdomName);

        when(fiefdom.getOwner()).thenReturn(CurrentPlayers.currentPlayers.get(buyerName));
        when(fiefdom.getName()).thenReturn(fiefdomName);

        purchase.perform(args);

        verify(purchase, times(1)).castlePersonal(iterator);
    }

    @Test
    public void testPerformCastlePersonalOwnerOtherRank() {
        final String arg1 = "castlePersonal";
        final int qty = 5;
        final String buyerName = "John";
        final String fiefdomName = "fiefdom1";
        final Fiefdoms fiefdom = new Fiefdoms();
        final Knight buyerObj = new Knight();

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(fiefdomName);

        when(fiefdom.getOwner()).thenReturn(CurrentPlayers.currentPlayers.get("Alice"));
        when(fiefdom.getName()).thenReturn(fiefdomName);

        purchase.perform(args);

        verify(purchase, times(1)).perform(args);
    }

    @Test
    public void testPerformCastlePersonalOwnerCountOrDuke() {
        final String arg1 = "castlePersonal";
        final int qty = 5;
        final String buyerName = "John";
        final String fiefdomName = "fiefdom1";
        final Fiefdoms fiefdom = new Fiefdoms();
        final Knight buyerObj = new Knight();

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(fiefdomName);

        when(fiefdom.getOwner()).thenReturn(CurrentPlayers.currentPlayers.get("Alice"));
        when(fiefdom.getName()).thenReturn(fiefdomName);

        purchase.perform(args);

        verify(purchase, times(1)).perform(args);
    }

    @Test
    public void testPerformMercenary() {
        final String arg1 = "mercenary";
        final int qty = 5;
        final String buyerName = "John";
        final String knightName = "Alice";

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(knightName);

        purchase.perform(args);

        verify(purchase, times(1)).mercenary(iterator);
    }

    @Test
    public void testPerformMercenaryBuyerMercenary() {
        final String arg1 = "mercenary";
        final int qty = 5;
        final String buyerName = "John";
        final String knightName = "John";

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(knightName);

        purchase.perform(args);

        verify(purchase, times(1)).perform(args);
    }

    @Test
    public void testPerformMercenaryBuyerKing() {
        final String arg1 = "mercenary";
        final int qty = 5;
        final String buyerName = "John";
        final String knightName = "Alice";

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(knightName);

        when(CurrentPlayers.currentPlayers.get(buyerName).getRank()).thenReturn("king");

        purchase.perform(args);

        verify(purchase, times(1)).perform(args);
    }

    @Test
    public void testPerformMercenaryKnightMercenary() {
        final String arg1 = "mercenary";
        final int qty = 5;
        final String buyerName = "Alice";
        final String knightName = "John";

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(knightName);

        purchase.perform(args);

        verify(purchase, times(1)).perform(args);
    }

    @Test
    public void testPerformMercenaryInexistentKnight() {
        final String arg1 = "mercenary";
        final int qty = 5;
        final String buyerName = "John";
        final String knightName = "Inexistent";

        when(iterator.next()).thenReturn(arg1).thenReturn(Integer.toString(qty)).thenReturn(buyerName).thenReturn(knightName);

        purchase.perform(args);

        verify(purchase, times(1)).perform(args);
    }
}