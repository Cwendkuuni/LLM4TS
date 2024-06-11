package src.test.java.org.apache.commons.cli; // adjust the package name if necessary

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class PurchaseTest {

    private Purchase purchase;
    private Iterator<String> argIterator;
    private Fiefdoms fiefdomMock;
    private Map mapMock;
    private CurrentPlayers currentPlayersMock;
    private Knight buyerMock;
    private Knight knightMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        purchase = new Purchase();
        fiefdomMock = mock(Fiefdoms.class);
        mapMock = mock(Map.class);
        currentPlayersMock = mock(CurrentPlayers.class);
        buyerMock = mock(Knight.class);
        knightMock = mock(Knight.class);
        when(mapMock.get(anyString())).thenReturn(fiefdomMock);
        when(currentPlayersMock.get(anyString())).thenReturn(buyerMock);
        when(currentPlayersMock.get(eq(knightMock.getName()))).thenReturn(knightMock);

        argIterator = Arrays.asList("1", "buyer", "fiefdom").iterator();
        purchase.perform(Arrays.asList(argIterator));
    }

    @Test
    public void testVassal() {
        when(fiefdomMock.getOwner()).thenReturn(null);
        when(buyerMock.getRank()).thenReturn("noble");
        when(buyerMock.isFiefdomMine(eq("fiefdom"))).thenReturn(false);

        argIterator = Arrays.asList("2", "buyer", "fiefdom").iterator();
        purchase.perform(Arrays.asList(argIterator));
        verify(buyerMock, never()).setTotalMoney(anyInt(), anyBoolean());
        verify(fiefdomMock, never()).setLoyalVassals(anyInt(), anyBoolean());

        when(buyerMock.getRank()).thenReturn("king");
        when(buyerMock.isFiefdomMine(eq("fiefdom"))).thenReturn(true);

        argIterator = Arrays.asList("3", "buyer", "fiefdom").iterator();
        purchase.perform(Arrays.asList(argIterator));
        verify(buyerMock).setTotalMoney(eq(3), anyBoolean());
        verify(fiefdomMock).setLoyalVassals(eq(3), anyBoolean());
    }

    @Test
    public void testCastleFiefdom() {
        when(fiefdomMock.getOwner()).thenReturn(null);
        when(buyerMock.getRank()).thenReturn("noble");
        when(buyerMock.isFiefdomMine(eq("fiefdom"))).thenReturn(false);

        argIterator = Arrays.asList("5", "buyer", "fiefdom").iterator();
        purchase.perform(Arrays.asList(argIterator));
        verify(buyerMock, never()).setTotalMoney(anyInt(), anyBoolean());

        when(buyerMock.getRank()).thenReturn("king");
        when(buyerMock.isFiefdomMine(eq("fiefdom"))).thenReturn(true);
        when(fiefdomMock.getViking().size()).thenReturn(0);
        when(fiefdomMock.getMagyar().size()).thenReturn(0);
        when(fiefdomMock.getMuslim().size()).thenReturn(0);

        argIterator = Arrays.asList("5", "buyer", "fiefdom").iterator();
        purchase.perform(Arrays.asList(argIterator));
        verify(buyerMock).setTotalMoney(eq(125), anyBoolean());
        verify(fiefdomMock).buyFiefdomCastles(eq(5));
    }

    @Test
    public void testCastlePersonal() {
        when(fiefdomMock.getOwner()).thenReturn(null);
        when(buyerMock.getRank()).thenReturn("noble");
        when(buyerMock.isFiefdomMine(eq("fiefdom"))).thenReturn(false);

        argIterator = Arrays.asList("5", "buyer", "fiefdom").iterator();
        purchase.perform(Arrays.asList(argIterator));
        verify(buyerMock, never()).setTotalMoney(anyInt(), anyBoolean());

        when(buyerMock.getRank()).thenReturn("king");
        when(buyerMock.isFiefdomMine(eq("fiefdom"))).thenReturn(true);

        argIterator = Arrays.asList("5", "buyer", "fiefdom").iterator();
        purchase.perform(Arrays.asList(argIterator));
        verify(buyerMock).setTotalMoney(eq(50), anyBoolean());
        verify(fiefdomMock).buyPersonalCastles(eq(5));
    }

    @Test
    public void testMercenary() {
        when(buyerMock.getRank()).thenReturn("noble");
        when(knightMock.getName()).thenReturn("knight");
        when(currentPlayersMock.containsKey(buyerMock.getName())).thenReturn(true);
        when(currentPlayersMock.containsKey(knightMock.getName())).thenReturn(true);
        when(buyerMock.getName()).thenReturn("buyer");

        argIterator = Arrays.asList("3", "buyer", "knight").iterator();
        purchase.perform(Arrays.asList(argIterator));
        verify(knightMock, never()).addMercenaries(anyInt());

        when(buyerMock.getRank()).thenReturn("king");

        argIterator = Arrays.asList("3", "buyer", "knight").iterator();
        purchase.perform(Arrays.asList(argIterator));
        verify(knightMock).addMercenaries(eq(3));
    }

    @Test(expected = SecurityException.class)
    public void testSecurityException() {
        argIterator = Arrays.asList("", "buyer", "fiefdom").iterator();
        purchase.perform(Arrays.asList(argIterator));
    }

    @Test(expected = NoSuchMethodException.class)
    public void testNoSuchMethodException() {
        argIterator = Arrays.asList("vassal", "buyer", "fiefdom").iterator();
        purchase.perform(Arrays.asList(argIterator));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        argIterator = Arrays.asList("vassal", null, "fiefdom").iterator();
        purchase.perform(Arrays.asList(argIterator));
    }

    @Test(expected = IllegalAccessException.class)
    public void testIllegalAccessException() {
        argIterator = Arrays.asList("vassal", "buyer", "null").iterator();
        purchase.perform(Arrays.asList(argIterator));
    }

    @Test(expected = InvocationTargetException.class)
    public void testInvocationTargetException() {
        when(fiefdomMock.getOwner()).thenThrow(new RuntimeException());
        argIterator = Arrays.asList("vassal", "buyer", "fiefdom").iterator();
        purchase.perform(Arrays.asList(argIterator));
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() {
        when(mapMock.get(anyString())).thenThrow(new ClassNotFoundException());
        argIterator = Arrays.asList("vassal", "buyer", "fiefdom").iterator();
        purchase.perform(Arrays.asList(argIterator));
    }
}