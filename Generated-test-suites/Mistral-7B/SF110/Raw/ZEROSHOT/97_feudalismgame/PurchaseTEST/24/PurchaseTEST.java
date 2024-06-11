package src.test.java;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PurchaseTest {

    private Purchase purchase;
    private Fiefdoms fiefdom;
    private Knight buyer, knight;
    private Iterator<Object> argsIterator;

    @Before
    public void setup() {
        fiefdom = mock(Fiefdoms.class);
        buyer = mock(Knight.class);
        knight = mock(Knight.class);
        purchase = new Purchase();
    }

    @Test
    public void testPerformWithValidIteratorArguments() throws Exception {
        when(argsIterator.next()).thenReturn("vassal").thenReturn(Collections.singletonList(1).iterator());
        when(argsIterator.next()).thenReturn("John Doe");
        when(argsIterator.next()).thenReturn("fiefdom1");
        purchase.perform(Arrays.asList(argsIterator).iterator());
        verify(purchase).vassal(argThat(a -> a.equals(Collections.singletonList(1).iterator())));
        verifyNoMoreInteractions(purchase);
    }

    @Test
    public void testPerformWithInvalidMethodName() throws Exception {
        when(argsIterator.next()).thenReturn("invalidMethod");
        purchase.perform(Arrays.asList(argsIterator).iterator());
        verifyZeroInteractions(purchase);
    }

    @Test
    public void testVassalWithValidArguments() {
        when(argsIterator.next()).thenReturn(Collections.singletonList(2).iterator());
        when(argsIterator.next()).thenReturn("John Doe");
        when(argsIterator.next()).thenReturn("fiefdom1");
        doReturn(new Fiefdom()).when(fiefdom).get(ArgumentsMatchers.eq("fiefdom1"));
        doReturn(buyer).when(CurrentPlayers.currentPlayers).get(ArgumentsMatchers.eq("John Doe"));
        doReturn(null).when(buyer).getRank();
        doReturn(true).when(buyer).isFiefdomMine(ArgumentsMatchers.eq("fiefdom1"));

        purchase.vassal(Arrays.asList(argsIterator).iterator());
        verify(fiefdom).setLoyalVassals(ArgumentsMatchers.eq(2), ArgumentsMatchers.eq(true));
    }

    @Test
    public void testVassalWithInsufficientFunds() {
        when(argsIterator.next()).thenReturn(Collections.singletonList(2).iterator());
        when(argsIterator.next()).thenReturn("John Doe");
        when(argsIterator.next()).thenReturn("fiefdom1");
        doReturn(new Fiefdom()).when(fiefdom).get(ArgumentsMatchers.eq("fiefdom1"));
        doReturn(buyer).when(CurrentPlayers.currentPlayers).get(ArgumentsMatchers.eq("John Doe"));
        doReturn("knight").when(buyer).getRank();

        purchase.vassal(Arrays.asList(argsIterator).iterator());
        verify(System.out).println(ArgumentsMatchers.eq("You cannot buy more vassals, you have insufficient funds"));
    }

    @Test
    public void testVassalWhenFiefdomIsNotMine() {
        when(argsIterator.next()).thenReturn(Collections.singletonList(2).iterator());
        when(argsIterator.next()).thenReturn("John Doe");
        when(argsIterator.next()).thenReturn("fiefdom2");
        doReturn(new Fiefdom()).when(fiefdom).get(ArgumentsMatchers.eq("fiefdom2"));

        purchase.vassal(Arrays.asList(argsIterator).iterator());
        verify(System.out).println(ArgumentsMatchers.eq("Fiefdom doesn't belong to the kingdom"));
    }

    @Test
    public void testCastleFiefdomWithValidArguments() {
        when(argsIterator.next()).thenReturn(Collections.singletonList(2).iterator());
        when(argsIterator.next()).thenReturn("John Doe");
        when(argsIterator.next()).thenReturn("fiefdom1");
        doReturn(new Fiefdom()).when(fiefdom).get(ArgumentsMatchers.eq("fiefdom1"));
        doReturn(buyer).when(CurrentPlayers.currentPlayers).get(ArgumentsMatchers.eq("John Doe"));
        doReturn("king").when(buyer).getRank();

        purchase.castleFiefdom(Arrays.asList(argsIterator).iterator());
        verify(fiefdom).buyFiefdomCastles(ArgumentsMatchers.eq(2));
    }

    @Test
    public void testCastleFiefdomWithInsufficientFunds() {
        when(argsIterator.next()).thenReturn(Collections.singletonList(2).iterator());
        when(argsIterator.next()).thenReturn("John Doe");
        when(argsIterator.next()).thenReturn("fiefdom1");
        doReturn(new Fiefdom()).when(fiefdom).get(ArgumentsMatchers.eq("fiefdom1"));
        doReturn(buyer).when(CurrentPlayers.currentPlayers).get(ArgumentsMatchers.eq("John Doe"));
        doReturn("king").when(buyer).getRank();

        purchase.castleFiefdom(Arrays.asList(argsIterator).iterator());
        verify(System.out).println(ArgumentsMatchers.eq("You cannot buy more Fiefdom castles, you have insufficient funds"));
    }

    @Test
    public void testCastleFiefdomWhenFiefdomIsNotMine() {
        when(argsIterator.next()).thenReturn(Collections.singletonList(2).iterator());
        when(argsIterator.next()).thenReturn("John Doe");
        when(argsIterator.next()).thenReturn("fiefdom2");
        doReturn(new Fiefdom()).when(fiefdom).get(ArgumentsMatchers.eq("fiefdom2"));
        doReturn(CurrentPlayers.currentPlayers.get("king")).when(CurrentPlayers.currentPlayers).get(ArgumentsMatchers.eq("John Doe"));

        purchase.castleFiefdom(Arrays.asList(argsIterator).iterator());
        verify(System.out).println(ArgumentsMatchers.eq("You can only buy a castle for a fiefdom that belongs to you"));
    }

    @Test
    public void testCastlePersonalWithValidArguments() {
        when(argsIterator.next()).thenReturn(Collections.singletonList(2).iterator());
        when(argsIterator.next()).thenReturn("John Doe");
        when(argsIterator.next()).thenReturn("fiefdom1");
        doReturn(new Fiefdom()).when(fiefdom).get(ArgumentsMatchers.eq("fiefdom1"));
        doReturn(buyer).when(CurrentPlayers.currentPlayers).get(ArgumentsMatchers.eq("John Doe"));
        doReturn("king").when(buyer).getRank();
        doReturn(true).when(buyer).isFiefdomMine(ArgumentsMatchers.eq("fiefdom1"));

        purchase.castlePersonal(Arrays.asList(argsIterator).iterator());
        verify(fiefdom).buyPersonalCastles(ArgumentsMatchers.eq(2));
    }

    @Test
    public void testCastlePersonalWithInsufficientFunds() {
        when(argsIterator.next()).thenReturn(Collections.singletonList(2).iterator());
        when(argsIterator.next()).thenReturn("John Doe");
        when(argsIterator.next()).thenReturn("fiefdom1");
        doReturn(new Fiefdom()).when(fiefdom).get(ArgumentsMatchers.eq("fiefdom1"));
        doReturn(buyer).when(CurrentPlayers.currentPlayers).get(ArgumentsMatchers.eq("John Doe"));
        doReturn("king").when(buyer).getRank();

        purchase.castlePersonal(Arrays.asList(argsIterator).iterator());
        verify(System.out).println(ArgumentsMatchers.eq("You cannot buy more Personal castle, you have insufficient funds"));
    }

    @Test
    public void testCastlePersonalWhenFiefdomIsNotMine() {
        when(argsIterator.next()).thenReturn(Collections.singletonList(2).iterator());
        when(argsIterator.next()).thenReturn("John Doe");
        when(argsIterator.next()).thenReturn("fiefdom2");
        doReturn(new Fiefdom()).when(fiefdom).get(ArgumentsMatchers.eq("fiefdom2"));

        purchase.castlePersonal(Arrays.asList(argsIterator).iterator());
        verify(System.out).println(ArgumentsMatchers.eq("Fiefdom doesn't belong to the kingdom"));
    }

    @Test
    public void testMercenaryWithValidArguments() {
        when(argsIterator.next()).thenReturn(Collections.singletonList(2).iterator());
        when(argsIterator.next()).thenReturn("John Doe");
        when(argsIterator.next()).thenReturn("knight1");

        purchase.mercenary(Arrays.asList(argsIterator).iterator());
        verify(knight).addMercenaries(ArgumentsMatchers.eq(2));
    }

    @Test
    public void testMercenaryWhenBuyerIsTheSameKnight() {
        when(argsIterator.next()).thenReturn(Collections.singletonList(2).iterator());
        when(argsIterator.next()).thenReturn("knight1");
        when(argsIterator.next()).thenReturn("knight1");

        purchase.mercenary(Arrays.asList(argsIterator).iterator());
        verify(knight).addMercenaries(ArgumentsMatchers.eq(2));
    }

    @Test
    public void testMercenaryWhenBuyerIsKing() {
        when(argsIterator.next()).thenReturn(Collections.singletonList(2).iterator());
        when(argsIterator.next()).thenReturn("king");
        when(argsIterator.next()).thenReturn("knight1");

        purchase.mercenary(Arrays.asList(argsIterator).iterator());
        verify(System.out).println(ArgumentsMatchers.eq("You cannot buy a Personal Castle for Counts or Dukes"));
    }

    @Test
    public void testMercenaryWhenKnightDoesntExist() {
        when(argsIterator.next()).thenReturn(Collections.singletonList(2).iterator());
        when(argsIterator.next()).thenReturn("John Doe");
        when(argsIterator.next()).thenReturn("knightInexistent");

        purchase.mercenary(Arrays.asList(argsIterator).iterator());
        verify(System.out).println(ArgumentsMatchers.eq("Inexistent Knight"));
    }
}