package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PurchaseTest {

    private Collection args;
    private Purchase purchase;
    private Iterator iterator;
    private Fiefdoms mockFiefdom;
    private Knight mockBuyer;
    private Knight mockKnight;
    private CurrentPlayers mockCurrentPlayers;
    private Map mockMap;

    @Before
    public void setUp() {
        args = new ArrayList<>();
        purchase = new Purchase();
        iterator = mock(Iterator.class);
        mockFiefdom = Mockito.mock(Fiefdoms.class);
        mockBuyer = Mockito.mock(Knight.class);
        mockKnight = Mockito.mock(Knight.class);
        mockCurrentPlayers = Mockito.mock(CurrentPlayers.class);
        mockMap = Mockito.mock(Map.class);

        when(mockCurrentPlayers.get(anyString())).thenReturn(mockBuyer);
        when(mockCurrentPlayers.get(anyString())).thenReturn(mockKnight);
        when(mockMap.get(anyString())).thenReturn(mockFiefdom);
    }

    @After
    public void tearDown() {
        reset(mockFiefdom, mockBuyer, mockKnight, mockCurrentPlayers, mockMap);
    }

    @Test
    public void testPerform_ValidArguments() {
        String[] argsArray = {"vassal", iterator};
        args.addAll(Arrays.asList(argsArray));
        when(iterator.next()).thenReturn("1").thenReturn("Alice").thenReturn("Castle1").thenReturn(iterator);

        purchase.perform(args);

        verify(mockFiefdom, times(1)).setLoyalVassals(1, true);
    }

    @Test(expected = NoSuchElementException.class)
    public void testPerform_InvalidArguments() {
        String[] argsArray = {"vassal", iterator};
        args.addAll(Arrays.asList(argsArray));
        when(iterator.next()).thenThrow(NoSuchElementException.class);

        purchase.perform(args);
    }

    @Test(expected = SecurityException.class)
    public void testPerform_SecurityException() {
        String[] argsArray = {"vassal", iterator};
        args.addAll(Arrays.asList(argsArray));
        doThrow(SecurityException.class).when(mockBuyer).setTotalMoney(anyInt(), anyBoolean());

        purchase.perform(args);
    }

    @Test
    public void testVassal() {
        when(iterator.next()).thenReturn("2").thenReturn("Alice").thenReturn("Castle1").thenReturn("Alice").thenReturn(iterator);

        purchase.vassal(iterator);

        verify(mockFiefdom, times(1)).setLoyalVassals(2, true);
    }

    @Test
    public void testVassal_FiefdomNotBelongsToTheKingdom() {
        when(iterator.next()).thenReturn("2").thenReturn("Alice").thenReturn("Castle1").thenReturn("unknown").thenReturn(iterator);

        purchase.vassal(iterator);

        verify(stdOut, times(1)).println("Fiefdom doesn't belong to the kingdom: Castle1 owner: unknown");
    }

    @Test
    public void testVassal_BuyerIsNotAbleToBuy() {
        when(iterator.next()).thenReturn("1").thenReturn("Alice").thenReturn("Castle1").thenReturn("Alice").thenReturn(iterator);
        doThrow(IllegalArgumentException.class).when(mockBuyer).setTotalMoney(anyInt(), anyBoolean());

        purchase.vassal(iterator);

        verify(stdOut, times(1)).println("You cannot buy more vassals, you have insufficient funds");
    }

    @Test
    public void testVassal_BuyerIsANewPlayer() {
        when(iterator.next()).thenReturn("1").thenReturn("NewPlayer").thenReturn("Castle1").thenReturn("NewPlayer").thenReturn(iterator);

        purchase.vassal(iterator);

        verify(mockBuyer, times(1)).setTotalMoney(1, false);
        verify(mockFiefdom, times(1)).setLoyalVassals(1, true);
    }

    @Test
    public void testCastleFiefdom() {
        when(iterator.next()).thenReturn("25").thenReturn("Alice").thenReturn("Castle1").thenReturn("Alice").thenReturn(iterator);

        purchase.castleFiefdom(iterator);

        verify(mockFiefdom, times(1)).buyFiefdomCastles(25);
    }

    @Test
    public void testCastleFiefdom_FiefdomDoesntBelongToTheKingdom() {
        when(iterator.next()).thenReturn("25").thenReturn("Alice").thenReturn("CastleUnknown").thenReturn("Alice").thenReturn(iterator);

        purchase.castleFiefdom(iterator);

        verify(stdOut, times(1)).println("Fiefdom doesn't belong to the kingdom: CastleUnknown owner: unknown");
    }

    @Test
    public void testCastleFiefdom_BuyerIsNotAbleToBuy() {
        when(iterator.next()).thenReturn("25").thenReturn("Alice").thenReturn("Castle1").thenReturn("Alice").thenReturn(iterator);
        doThrow(IllegalArgumentException.class).when(mockBuyer).setTotalMoney(anyInt() * 25, anyBoolean());

        purchase.castleFiefdom(iterator);

        verify(stdOut, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
    }

    @Test
    public void testCastlePersonal() {
        when(iterator.next()).thenReturn("10").thenReturn("Alice").thenReturn("Castle1").thenReturn("Alice").thenReturn(iterator);

        purchase.castlePersonal(iterator);

        verify(mockFiefdom, times(1)).buyPersonalCastles(10);
    }

    @Test
    public void testCastlePersonal_BuyerIsNotAbleToBuy() {
        when(iterator.next()).thenReturn("10").thenReturn("Alice").thenReturn("Castle1").thenReturn("Alice").thenReturn(iterator);
        doThrow(IllegalArgumentException.class).when(mockBuyer).setTotalMoney(anyInt() * 10, anyBoolean());

        purchase.castlePersonal(iterator);

        verify(stdOut, times(1)).println("You cannot buy more Personal castle, you have insufficient funds");
    }

    @Test
    public void testMercenary() {
        when(iterator.next()).thenReturn("5").thenReturn("Alice").thenReturn("Knight1").thenReturn("Alice").thenReturn(iterator);

        purchase.mercenary(iterator);

        verify(mockKnight, times(1)).addMercenaries(5);
    }

    @Test
    public void testMercenary_BuyerIsNotAbleToBuy() {
        when(iterator.next()).thenReturn("5").thenReturn("Alice").thenReturn("Knight1").thenReturn("Alice").thenReturn(iterator);
        doThrow(IllegalArgumentException.class).when(mockBuyer).setTotalMoney(anyInt(), anyBoolean());

        purchase.mercenary(iterator);

        verify(stdOut, times(1)).println("You cannot buy more mercenaries, you have insufficient funds");
    }

    @Test
    public void testMercenary_BuyerIsTheSameKnight() {
        when(iterator.next()).thenReturn("5").thenReturn("Alice").thenReturn("Alice").thenReturn("Alice").thenReturn(iterator);

        purchase.mercenary(iterator);

        verify(mockBuyer, times(1)).addMercenaries(5);
    }

    @Test
    public void testMercenary_BuyerIsKing() {
        when(iterator.next()).thenReturn("5").thenReturn("King").thenReturn("Alice").thenReturn("Alice").thenReturn(iterator);

        purchase.mercenary(iterator);

        verify(stdOut, times(1)).println("You cannot buy mercenaries for a king");
    }

    @Test
    public void testMercenary_BuyerIsCountOrDuke() {
        when(iterator.next()).thenReturn("5").thenReturn("Count").thenReturn("Alice").thenReturn("Alice").thenReturn(iterator);

        purchase.mercenary(iterator);

        verify(stdOut, times(1)).println("You cannot buy mercenaries for counts or dukes");
    }
}