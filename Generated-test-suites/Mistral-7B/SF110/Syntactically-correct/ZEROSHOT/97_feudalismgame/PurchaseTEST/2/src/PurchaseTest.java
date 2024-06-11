package src.test.java;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PurchaseTest {

    private Purchase purchase;
    private Iterator<Object> argsIterator;
    private Fiefdoms map;
    private CurrentPlayers currentPlayers;

    @Before
    public void setUp() {
        purchase = new Purchase();
        map = Mockito.mock(Fiefdoms.class);
        currentPlayers = Mockito.mock(CurrentPlayers.class);
        when(map.get(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        when(currentPlayers.get(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        purchase.setMap(map);
        purchase.setCurrentPlayers(currentPlayers);
    }

    @Test
    public void testPerform_vassal() {
        Iterator<Object> args = Arrays.asList(3, "player1", "fiefdom1").iterator();
        when(currentPlayers.get("player1")).thenReturn(new Knight("player1", "knight", 1000));
        when(map.get("fiefdom1")).thenReturn(new Fiefdoms("fiefdom1", "owner1", new ArrayList<>()));

        purchase.perform(Arrays.asList(args));

        verify(map, times(1)).get("fiefdom1");
        verify(currentPlayers, times(1)).get("player1");
        verify(currentPlayers, times(1)).get("owner1");
        verify(currentPlayers, times(1)).get("player1");
        verify(map, times(1)).getOwner();
        verify(map, times(1)).setLoyalVassals(3, true);
        verify(currentPlayers, times(1)).setTotalMoney(3, false);
    }

    @Test
    public void testPerform_vassal_insufficientFunds() {
        Iterator<Object> args = Arrays.asList(5, "player1", "fiefdom1").iterator();
        when(currentPlayers.get("player1")).thenReturn(new Knight("player1", "knight", 500));
        when(map.get("fiefdom1")).thenReturn(new Fiefdoms("fiefdom1", "owner1", new ArrayList<>()));

        purchase.perform(Arrays.asList(args));

        verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
    }

    @Test
    public void testPerform_vassal_notYourFiefdom() {
        Iterator<Object> args = Arrays.asList(2, "player1", "fiefdom2").iterator();
        when(currentPlayers.get("player1")).thenReturn(new Knight("player1", "knight", 2000));
        when(map.get("fiefdom2")).thenReturn(new Fiefdoms("fiefdom2", "owner2", new ArrayList<>()));

        purchase.perform(Arrays.asList(args));

        verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
    }

    @Test
    public void testPerform_castleFiefdom() {
        Iterator<Object> args = Arrays.asList(5, "player1", "fiefdom1").iterator();
        when(currentPlayers.get("player1")).thenReturn(new Knight("player1", "king", 5000));
        when(map.get("fiefdom1")).thenReturn(new Fiefdoms("fiefdom1", "owner1", new ArrayList<>()));

        purchase.perform(Arrays.asList(args));

        verify(map, times(1)).get("fiefdom1");
        verify(currentPlayers, times(1)).get("player1");
        verify(currentPlayers, times(1)).get("owner1");
        verify(currentPlayers, times(1)).get("player1");
        verify(map, times(1)).buyFiefdomCastles(5);
        verify(currentPlayers, times(1)).setTotalMoney(25 * 5, false);
    }

    @Test
    public void testPerform_castleFiefdom_insufficientFunds() {
        Iterator<Object> args = Arrays.asList(7, "player1", "fiefdom1").iterator();
        when(currentPlayers.get("player1")).thenReturn(new Knight("player1", "king", 3000));
        when(map.get("fiefdom1")).thenReturn(new Fiefdoms("fiefdom1", "owner1", new ArrayList<>()));

        purchase.perform(Arrays.asList(args));

        verify(System.out, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
    }

    @Test
    public void testPerform_castleFiefdom_notYourFiefdom() {
        Iterator<Object> args = Arrays.asList(4, "player1", "fiefdom2").iterator();
        when(currentPlayers.get("player1")).thenReturn(new Knight("player1", "knight", 4000));
        when(map.get("fiefdom2")).thenReturn(new Fiefdoms("fiefdom2", "owner2", new ArrayList<>()));

        purchase.perform(Arrays.asList(args));

        verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
    }

    @Test
    public void testPerform_castlePersonal() {
        Iterator<Object> args = Arrays.asList(4, "player1", "fiefdom1").iterator();
        when(currentPlayers.get("player1")).thenReturn(new Knight("player1", "king", 10000));
        when(map.get("fiefdom1")).thenReturn(new Fiefdoms("fiefdom1", null, new ArrayList<>()));

        purchase.perform(Arrays.asList(args));

        verify(map, times(1)).get("fiefdom1");
        verify(currentPlayers, times(1)).get("player1");
        verify(map, times(1)).buyPersonalCastles(4);
        verify(currentPlayers, times(1)).setTotalMoney(10 * 4, false);
    }

    @Test
    public void testPerform_castlePersonal_insufficientFunds() {
        Iterator<Object> args = Arrays.asList(6, "player1", "fiefdom1").iterator();
        when(currentPlayers.get("player1")).thenReturn(new Knight("player1", "king", 6000));
        when(map.get("fiefdom1")).thenReturn(new Fiefdoms("fiefdom1", null, new ArrayList<>()));

        purchase.perform(Arrays.asList(args));

        verify(System.out, times(1)).println("You cannot buy more Personal castle, you have insufficient funds");
    }

    @Test
    public void testPerform_mercenary() {
        Iterator<Object> args = Arrays.asList(3, "player1", "knight1").iterator();
        when(currentPlayers.get("player1")).thenReturn(new Knight("player1", "king", 15000));
        when(currentPlayers.get("knight1")).thenReturn(new Knight("knight1", "knight", 0));

        purchase.perform(Arrays.asList(args));

        verify(currentPlayers, times(1)).get("player1");
        verify(currentPlayers, times(1)).get("knight1");
        verify(currentPlayers, times(1)).get("player1");
        verify(currentPlayers, times(1)).setTotalMoney(3, false);
        verify(currentPlayers.get("knight1"), times(1)).addMercenaries(3);
    }

    @Test
    public void testPerform_mercenary_insufficientFunds() {
        Iterator<Object> args = Arrays.asList(5, "player1", "knight1").iterator();
        when(currentPlayers.get("player1")).thenReturn(new Knight("player1", "king", 7000));
        when(currentPlayers.get("knight1")).thenReturn(new Knight("knight1", "knight", 0));

        purchase.perform(Arrays.asList(args));

        verify(System.out, times(1)).println("You cannot buy more mercenaries, you have insufficient funds");
    }

    @Test
    public void testPerform_mercenary_samePlayer() {
        Iterator<Object> args = Arrays.asList(2, "player1", "knight1").iterator();
        when(currentPlayers.get("player1")).thenReturn(new Knight("player1", "knight", 5000));
        when(currentPlayers.get("knight1")).thenReturn(new Knight("knight1", "knight", 0));

        purchase.perform(Arrays.asList(args));

        verify(System.out, times(1)).println("You cannot buy mercenaries for yourself");
    }
}