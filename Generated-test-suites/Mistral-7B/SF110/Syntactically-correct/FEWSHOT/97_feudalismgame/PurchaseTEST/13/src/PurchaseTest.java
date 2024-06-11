package src.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import src.*;
import src.actions.*;
import src.models.*;

public class PurchaseTest {

    private Purchase purchase;
    private Iterator<String> argsIteratorMock;
    private Fiefdoms mapMock;
    private CurrentPlayers currentPlayersMock;

    @Before
    public void setUp() {
        argsIteratorMock = mock(Iterator.class);
        mapMock = mock(Fiefdoms.class);
        currentPlayersMock = mock(CurrentPlayers.class);
        purchase = new Purchase() {
            @Override
            public void perform(Collection args) {
                this.getClass();
                when(args.iterator()).thenReturn(argsIteratorMock);
                super.perform(args);
            }
        };
        when(mapMock.get(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            String fiefdomName = invocation.getArgument(0);
            switch (fiefdomName) {
                case "fiefdom1":
                    return new Fiefdoms("fiefdom1", "owner1", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null, null, null, null);
                case "fiefdom2":
                    return new Fiefdoms("fiefdom2", "owner2", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null, null, null, null);
                default:
                    return null;
            }
        });
        when(currentPlayersMock.get(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            String playerName = invocation.getArgument(0);
            switch (playerName) {
                case "player1":
                    return new Knight("player1", "king");
                case "player2":
                    return new Knight("player2", "count");
                default:
                    return null;
            }
        });
    }

    @Test
    public void testVassal() {
        when(argsIteratorMock.next()).thenReturn("3").thenReturn("player1").thenReturn("fiefdom1").thenReturn("owner1");
        purchase.vassal(argsIteratorMock);
        verify(mapMock, times(1)).get(eq("fiefdom1"));
        verify(currentPlayersMock, times(1)).get(eq("player1"));
        verify(currentPlayersMock, times(1)).get(eq("owner1"));
        verify(mapMock, times(1)).getOwner();
        verify(currentPlayersMock, times(1)).getRank();
        verify(currentPlayersMock, times(1)).isFiefdomMine;
        verify(currentPlayersMock, times(1)).setTotalMoney;
        verify(mapMock, times(1)).setLoyalVassals;
        verify(System.out, times(1)).println;

        when(argsIteratorMock.next()).thenReturn("5").thenReturn("player1").thenReturn("fiefdom1").thenReturn("owner1");
        purchase.vassal(argsIteratorMock);
        verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
    }

    @Test
    public void testCastleFiefdom() {
        when(argsIteratorMock.next()).thenReturn("2").thenReturn("player1").thenReturn("fiefdom1").thenReturn("owner1");
        purchase.castleFiefdom(argsIteratorMock);
        verify(mapMock, times(1)).get(eq("fiefdom1"));
        verify(currentPlayersMock, times(1)).get(eq("player1"));
        verify(currentPlayersMock, times(1)).get(eq("owner1"));
        verify(mapMock, times(1)).getOwner;
        verify(currentPlayersMock, times(1)).getRank;
        verify(currentPlayersMock, times(1)).isFiefdomMine;
        verify(currentPlayersMock, times(1)).setTotalMoney;
        verify(mapMock, times(1)).buyFiefdomCastles;
        verify(System.out, times(1)).println;

        when(argsIteratorMock.next()).thenReturn("4").thenReturn("player1").thenReturn("fiefdom1").thenReturn("owner1");
        purchase.castleFiefdom(argsIteratorMock);
        verify(System.out, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
    }

    @Test
    public void testCastlePersonal() {
        when(argsIteratorMock.next()).thenReturn("1").thenReturn("player1").thenReturn("fiefdom1").thenReturn("owner1");
        purchase.castlePersonal(argsIteratorMock);
        verify(mapMock, times(1)).get(eq("fiefdom1"));
        verify(currentPlayersMock, times(1)).get(eq("player1"));
        verify(currentPlayersMock, times(1)).get(eq("owner1"));
        verify(mapMock, times(1)).getOwner;
        verify(currentPlayersMock, times(1)).getRank;
        verify(currentPlayersMock, times(1)).isFiefdomMine;
        verify(currentPlayersMock, times(1)).setTotalMoney;
        verify(mapMock, times(1)).buyPersonalCastles;
        verify(System.out, times(1)).println;

        when(argsIteratorMock.next()).thenReturn("2").thenReturn("player1").thenReturn("fiefdom1").thenReturn("owner1");
        purchase.castlePersonal(argsIteratorMock);
        verify(System.out, times(1)).println("You cannot buy a Personal Castle for Counts or Dukes");
    }

    @Test
    public void testMercenary() {
        when(argsIteratorMock.next()).thenReturn("2").thenReturn("player1").thenReturn("knight1").thenReturn("player2");
        purchase.mercenary(argsIteratorMock);
        verify(currentPlayersMock, times(1)).get(eq("player1"));
        verify(currentPlayersMock, times(1)).get(eq("player2"));
        verify(currentPlayersMock, times(1)).get(eq("knight1"));
        verify(currentPlayersMock, times(1)).getName;
        verify(currentPlayersMock, times(1)).getRank;
        verify(currentPlayersMock, times(1)).setTotalMoney;
        verify(currentPlayersMock, times(1)).addMercenaries;
        verify(System.out, times(1)).println;

        when(argsIteratorMock.next()).thenReturn("3").thenReturn("player1").thenReturn("knight2").thenReturn("player3");
        purchase.mercenary(argsIteratorMock);
        verify(System.out, times(1)).println("Inexistent Knight");
    }
}