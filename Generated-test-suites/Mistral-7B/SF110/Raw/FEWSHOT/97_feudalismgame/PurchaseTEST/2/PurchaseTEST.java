package src.test.java.org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class PurchaseTest {

    private Purchase purchase;
    private Map map;
    private List<Fiefdoms> fiefdoms;
    private List<CurrentPlayer> currentPlayers;
    private Iterator<Object> argIterator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.map = mock(Map.class);
        this.fiefdoms = new ArrayList<>();
        this.currentPlayers = new ArrayList<>();
        when(map.get(anyString())).thenAnswer(invocation -> fiefdoms.get(invocation.getArgument(0)));
        purchase = new Purchase();
        purchase.setMap(map);
        purchase.setCurrentPlayers(currentPlayers);
    }

    @Test
    public void testPerformWithValidArguments() {
        when(currentPlayers.iterator().next()).thenReturn("vassal");
        final Iterator<Object> args = Arrays.asList("1", "player1", "fiefdom1").iterator();
        purchase.perform(args);
        verify(map, times(1)).get(eq("fiefdom1"));
        verify(currentPlayers, times(1)).get(eq("player1"));
        verify(currentPlayers, times(1)).get(eq("player1")).setTotalMoney(eq(1), anyBoolean());
        verify(fiefdoms.get(0), times(1)).setLoyalVassals(eq(1), anyBoolean());
    }

    @Test
    public void testPerformWithInvalidArguments() {
        when(currentPlayers.iterator().next()).thenReturn("castleFiefdom");
        final Iterator<Object> args = Arrays.asList("1", "player1", "fiefdom1").iterator();
        doThrow(NoSuchMethodException.class).when(purchase).getClass();
        purchase.perform(args);
        verify(System.out, times(1)).println("Error in method invocation.");
    }

    @Test
    public void testVassal() {
        final int qty = 1;
        final String buyerName = "player1";
        final Fiefdoms fiefdom = mock(Fiefdoms.class);
        when(fiefdom.getOwner()).thenReturn(null);
        when(currentPlayers.iterator().next()).thenReturn("vassal");
        when(currentPlayers.iterator().next()).nextReturn("fiefdom1");
        when(currentPlayers.iterator().next()).nextReturn("player1");
        when(currentPlayers.get(buyerName).getRank()).thenReturn("duke");
        when(map.get(eq("fiefdom1"))).thenReturn(fiefdom);

        purchase.vassal(mock(Iterator.class));
        verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
    }

    @Test
    public void testVassalWithValidArguments() {
        final int qty = 1;
        final String buyerName = "player1";
        final Fiefdoms fiefdom = mock(Fiefdoms.class);
        when(fiefdom.getOwner()).thenReturn(null);
        when(currentPlayers.iterator().next()).thenReturn("vassal");
        when(currentPlayers.iterator().next()).nextReturn("fiefdom1");
        when(currentPlayers.iterator().next()).nextReturn("player1");
        when(currentPlayers.get(buyerName).getRank()).thenReturn("knight");
        when(map.get(eq("fiefdom1"))).thenReturn(fiefdom);

        purchase.vassal(mock(Iterator.class));
        verify(currentPlayers.get(buyerName), times(1)).setTotalMoney(eq(1), anyBoolean());
        verify(fiefdom, times(1)).setLoyalVassals(eq(1), anyBoolean());
    }

    @Test
    public void testCastleFiefdom() {
        final int qty = 1;
        final String buyerName = "player1";
        final Fiefdoms fiefdom = mock(Fiefdoms.class);
        when(fiefdom.getOwner()).thenReturn(new CurrentPlayer("king1", "king1", 1000));
        when(currentPlayers.iterator().next()).thenReturn("castleFiefdom");
        when(currentPlayers.iterator().next()).nextReturn("fiefdom1");
        when(currentPlayers.iterator().next()).nextReturn("player1");

        purchase.castleFiefdom(mock(Iterator.class));
        verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
    }

    @Test
    public void testCastleFiefdomWithValidArguments() {
        final int qty = 1;
        final String buyerName = "player1";
        final Fiefdoms fiefdom = mock(Fiefdoms.class);
        when(fiefdom.getOwner()).thenReturn(new CurrentPlayer("king1", "king1", 1000));
        when(currentPlayers.iterator().next()).thenReturn("castleFiefdom");
        when(currentPlayers.iterator().next()).nextReturn("fiefdom1");
        when(currentPlayers.iterator().next()).nextReturn("player1");
        when(currentPlayers.get(buyerName).getRank()).thenReturn("knight");
        when(map.get(eq("fiefdom1"))).thenReturn(fiefdom);

        purchase.castleFiefdom(mock(Iterator.class));
        verify(currentPlayers.get(buyerName), times(1)).setTotalMoney(eq(25), anyBoolean());
        verify(fiefdom, times(1)).buyFiefdomCastles(eq(1));
    }

    @Test
    public void testCastlePersonal() {
        final int qty = 1;
        final String buyerName = "player1";
        final Fiefdoms fiefdom = mock(Fiefdoms.class);
        when(fiefdom.getOwner()).thenReturn(new CurrentPlayer("king1", "king1", 1000));
        when(currentPlayers.iterator().next()).thenReturn("castlePersonal");
        when(currentPlayers.iterator().next()).nextReturn("fiefdom1");
        when(currentPlayers.iterator().next()).nextReturn("player1");
        when(currentPlayers.get(buyerName).getRank()).thenReturn("count");

        purchase.castlePersonal(mock(Iterator.class));
        verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
    }

    @Test
    public void testCastlePersonalWithValidArguments() {
        final int qty = 1;
        final String buyerName = "player1";
        final Fiefdoms fiefdom = mock(Fiefdoms.class);
        when(fiefdom.getOwner()).thenReturn(new CurrentPlayer("king1", "king1", 1000));
        when(currentPlayers.iterator().next()).thenReturn("castlePersonal");
        when(currentPlayers.iterator().next()).nextReturn("fiefdom1");
        when(currentPlayers.iterator().next()).nextReturn("player1");
        when(currentPlayers.get(buyerName).getRank()).thenReturn("knight");
        when(map.get(eq("fiefdom1"))).thenReturn(fiefdom);

        purchase.castlePersonal(mock(Iterator.class));
        verify(currentPlayers.get(buyerName), times(1)).setTotalMoney(eq(10), anyBoolean());
        verify(fiefdom, times(1)).buyPersonalCastles(eq(1));
    }

    @Test
    public void testMercenary() {
        final int qty = 1;
        final String buyerName = "player1";
        final String knightName = "knight1";
        final Knight knight = mock(Knight.class);
        when(currentPlayers.iterator().next()).thenReturn("mercenary");
        when(currentPlayers.iterator().next()).nextReturn("player1");
        when(currentPlayers.iterator().next()).nextReturn("knight1");
        when(currentPlayers.iterator().next()).nextReturn("player1");
        when(currentPlayers.get(buyerName).getName()).thenReturn(knightName);
        when(currentPlayers.get(buyerName).getRank()).thenReturn("knight");
        when(currentPlayers.get(knightName).getRank()).thenReturn("knight");

        purchase.mercenary(mock(Iterator.class));
        verify(currentPlayers.get(buyerName), times(1)).setTotalMoney(eq(1), anyBoolean());
        verify(knight, times(1)).addMercenaries(eq(1));
    }

    @Test
    public void testMercenaryWithInvalidArguments() {
        final int qty = 1;
        final String buyerName = "player1";
        final String knightName = "knight1";
        final Knight knight = mock(Knight.class);
        when(currentPlayers.iterator().next()).thenReturn("mercenary");
        when(currentPlayers.iterator().next()).nextReturn("player1");
        when(currentPlayers.iterator().next()).nextReturn("knight1");
        when(currentPlayers.iterator().next()).nextReturn("player2");
        when(currentPlayers.get(buyerName).getName()).thenReturn("player1");
        when(currentPlayers.get(buyerName).getRank()).thenReturn("knight");
        when(currentPlayers.get(knightName).getRank()).thenReturn("knight");

        purchase.mercenary(mock(Iterator.class));
        verify(System.out, times(1)).println("Inexistent Knight");
    }
}