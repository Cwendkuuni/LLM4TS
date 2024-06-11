package src.test.java;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import src.*;
import src.actions.*;
import src.map.*;
import src.currentPlayers.*;
import src.knight.*;
import static src.Purchase.*;

public class PurchaseTest {

    @Test
    public void testVassal() {
        // Given
        final Collection args = new ArrayList() {{
            add(new Integer(10));
            add("player1");
            add("fiefdom1");
        }};
        final Fiefdoms fiefdom = Mockito.mock(Fiefdoms.class);
        when(Map.map.get(arg.next())).thenReturn(fiefdom);
        final Knight buyer = new Knight("player1", "knight");
        CurrentPlayers.currentPlayers.put("player1", buyer);

        final Purchase purchase = new Purchase();

        // When
        purchase.perform(args);

        // Then
        verify(fiefdom, times(1)).setLoyalVassals(1, true);
        verify(buyer, times(1)).setTotalMoney(-10, false);
    }

    @Test
    public void testCastleFiefdom() {
        // Given
        final Collection args = new ArrayList() {{
            add(new Integer(5));
            add("player1");
            add("fiefdom1");
        }};

        final Fiefdoms fiefdom = Mockito.mock(Fiefdoms.class);
        when(Map.map.get(arg.next())).thenReturn(fiefdom);
        final Knight buyer = new Knight("player1", "knight");
        CurrentPlayers.currentPlayers.put("player1", buyer);

        final Purchase purchase = new Purchase();

        // When
        purchase.perform(args);

        // Then
        verify(fiefdom, times(1)).buyFiefdomCastles(1);
        verify(buyer, times(1)).setTotalMoney(-25, false);
    }

    @Test
    public void testCastlePersonal() {
        // Given
        final Collection args = new ArrayList() {{
            add(new Integer(3));
            add("player1");
            add("fiefdom1");
        }};

        final Fiefdoms fiefdom = Mockito.mock(Fiefdoms.class);
        when(Map.map.get(arg.next())).thenReturn(fiefdom);
        final Knight buyer = new Knight("player1", "knight");
        CurrentPlayers.currentPlayers.put("player1", buyer);

        final Purchase purchase = new Purchase();

        // When
        purchase.perform(args);

        // Then
        verify(fiefdom, times(1)).buyPersonalCastles(1);
        verify(buyer, times(1)).setTotalMoney(-10, false);
    }

    @Test
    public void testMercenary() {
        // Given
        final Collection args = new ArrayList() {{
            add(new Integer(2));
            add("player1");
            add("knight1");
        }};

        final Knight buyer = new Knight("player1", "knight");
        CurrentPlayers.currentPlayers.put("player1", buyer);
        final Knight knight = new Knight("knight1", "knight");
        CurrentPlayers.currentPlayers.put("knight1", knight);

        final Purchase purchase = new Purchase();

        // When
        purchase.perform(args);

        // Then
        verify(knight, times(1)).addMercenaries(2);
        verify(buyer, times(1)).setTotalMoney(-2, false);
    }

    @Test
    public void testVassalErrorCase() {
        // Given
        final Collection args = new ArrayList() {{
            add(new Integer(-5));
            add("player1");
            add("fiefdom1");
        }};

        final Fiefdoms fiefdom = Mockito.mock(Fiefdoms.class);
        final Knight buyer = new Knight("player1", "knight");
        CurrentPlayers.currentPlayers.put("player1", buyer);

        final Purchase purchase = new Purchase();

        // When
        purchase.perform(args);

        // Then
        // Assert that an exception is thrown and appropriate error message is printed
    }

    @Test
    public void testVassalEdgeCase() {
        // Given
        final Collection args = new ArrayList() {{
            add(new Integer(Integer.MAX_VALUE));
            add("player1");
            add("fiefdom1");
        }};

        final Fiefdoms fiefdom = Mockito.mock(Fiefdoms.class);
        final Knight buyer = new Knight("player1", "knight");
        CurrentPlayers.currentPlayers.put("player1", buyer);

        final Purchase purchase = new Purchase();

        // When
        purchase.perform(args);

        // Then
        // Assert that an exception is thrown and appropriate error message is printed
    }

    // Add more test cases for other methods here
}