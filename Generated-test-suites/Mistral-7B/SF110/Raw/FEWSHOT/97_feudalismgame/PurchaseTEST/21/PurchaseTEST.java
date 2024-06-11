package src.test.java; // Make sure to add the correct package name

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.lang.reflect.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.AddMockMethods;

import src.*;
import src.utils.*;
import src.exceptions.*;

@AddMockMethods(value = { Fiefdoms.class, Knight.class, CurrentPlayers.class, Map.class })
public class PurchaseTest {

    private Purchase purchase;
    private Iterator mockArgIterator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        purchase = new Purchase();
        mockArgIterator = mock(Iterator.class);
    }

    @Test
    public void testPerform() {
        // Test with valid arguments
        when(mockArgIterator.next()).thenReturn("vassal").thenReturn(Collections.EMPTY_LIST.iterator());
        when(Map.map.get("testFiefdom")).thenReturn(new Fiefdoms("testFiefdom", null));
        when(CurrentPlayers.currentPlayers.get("testBuyer")).thenReturn(new Knight("testBuyer", "testRank"));

        purchase.perform(Arrays.asList(mockArgIterator));

        verify(purchase, times(1)).vassal(mockArgIterator);

        // Test with invalid arguments
        when(mockArgIterator.next()).thenThrow(NoSuchElementException.class);

        try {
            purchase.perform(Arrays.asList(mockArgIterator));
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // Expected
        }
    }

    @Test
    public void testVassal() {
        when(mockArgIterator.next()).thenReturn("5");
        when(mockArgIterator.next()).thenReturn("testBuyer");
        when(Map.map.get("testFiefdom")).thenReturn(new Fiefdoms("testFiefdom", null));
        when(CurrentPlayers.currentPlayers.get("testBuyer")).thenReturn(new Knight("testBuyer", "testRank"));
        when(CurrentPlayers.currentPlayers.get("testFiefdomOwner")).thenReturn(new Knight("testFiefdomOwner", "testRank"));
        when(CurrentPlayers.currentPlayers.get("testFiefdomOwner").getFiefdoms()).thenReturn(Collections.singletonList(new Fiefdoms("testFiefdom", null)));

        purchase.vassal(mockArgIterator);

        verify(CurrentPlayers.currentPlayers.get("testBuyer"), times(1)).setTotalMoney(5, false);
        verify(CurrentPlayers.currentPlayers.get("testFiefdom"), times(1)).setLoyalVassals(5, true);
    }

    @Test
    public void testCastleFiefdom() {
        when(mockArgIterator.next()).thenReturn("5");
        when(mockArgIterator.next()).thenReturn("testBuyer");
        when(Map.map.get("testFiefdom")).thenReturn(new Fiefdoms("testFiefdom", null));
        when(CurrentPlayers.currentPlayers.get("testBuyer")).thenReturn(new Knight("testBuyer", "testRank"));
        when(CurrentPlayers.currentPlayers.get("testFiefdomOwner")).thenReturn(new Knight("testFiefdomOwner", "testRank"));
        when(CurrentPlayers.currentPlayers.get("testFiefdomOwner").getFiefdoms()).thenReturn(Collections.singletonList(new Fiefdoms("testFiefdom", null)));

        purchase.castleFiefdom(mockArgIterator);

        verify(CurrentPlayers.currentPlayers.get("testBuyer"), times(1)).setTotalMoney(25, false);
        verify(CurrentPlayers.currentPlayers.get("testFiefdom"), times(1)).buyFiefdomCastles(5);
    }

    @Test
    public void testCastlePersonal() {
        when(mockArgIterator.next()).thenReturn("5");
        when(mockArgIterator.next()).thenReturn("testBuyer");
        when(Map.map.get("testFiefdom")).thenReturn(new Fiefdoms("testFiefdom", null));
        when(CurrentPlayers.currentPlayers.get("testBuyer")).thenReturn(new Knight("testBuyer", "testRank"));
        when(CurrentPlayers.currentPlayers.get("testFiefdomOwner")).thenReturn(new Knight("testFiefdomOwner", "testRank"));
        when(CurrentPlayers.currentPlayers.get("testFiefdomOwner").getFiefdoms()).thenReturn(Collections.singletonList(new Fiefdoms("testFiefdom", null)));

        purchase.castlePersonal(mockArgIterator);

        verify(CurrentPlayers.currentPlayers.get("testBuyer"), times(1)).setTotalMoney(10, false);
        verify(CurrentPlayers.currentPlayers.get("testFiefdom"), times(1)).buyPersonalCastles(5);
    }

    @Test(expected = InsufficientFundsException.class)
    public void testInsufficientFunds() {
        when(mockArgIterator.next()).thenReturn("5");
        when(mockArgIterator.next()).thenReturn("testBuyer");
        when(Map.map.get("testFiefdom")).thenReturn(new Fiefdoms("testFiefdom", null));
        when(CurrentPlayers.currentPlayers.get("testBuyer")).thenReturn(new Knight("testBuyer", "testRank"));
        when(CurrentPlayers.currentPlayers.get("testBuyer").getTotalMoney()).thenReturn(10);

        purchase.vassal(mockArgIterator);
        fail("Expected InsufficientFundsException");
    }

    @Test(expected = UnownedFiefdomException.class)
    public void testUnownedFiefdom() {
        when(mockArgIterator.next()).thenReturn("5");
        when(mockArgIterator.next()).thenReturn("testBuyer");
        when(Map.map.get("testFiefdom")).thenReturn(new Fiefdoms("testFiefdom", "testOwner"));

        try {
            purchase.vassal(mockArgIterator);
            fail("Expected UnownedFiefdomException");
        } catch (UnownedFiefdomException e) {
            // Expected
        }
    }

    @Test(expected = RankMismatchException.class)
    public void testRankMismatch() {
        when(mockArgIterator.next()).thenReturn("5");
        when(mockArgIterator.next()).thenReturn("testBuyer");
        when(mockArgIterator.next()).thenReturn("testFiefdomOwner");
        when(Map.map.get("testFiefdom")).thenReturn(new Fiefdoms("testFiefdom", null));
        when(CurrentPlayers.currentPlayers.get("testBuyer")).thenReturn(new Knight("testBuyer", "testRank"));
        when(CurrentPlayers.currentPlayers.get("testFiefdomOwner")).thenReturn(new Knight("testFiefdomOwner", "testRankDifferent"));

        try {
            purchase.vassal(mockArgIterator);
            fail("Expected RankMismatchException");
        } catch (RankMismatchException e) {
            // Expected
        }
    }

    @Test(expected = InsufficientAvailableVassalsException.class)
    public void testInsufficientAvailableVassals() {
        when(mockArgIterator.next()).thenReturn("5");
        when(mockArgIterator.next()).thenReturn("testBuyer");
        when(Map.map.get("testFiefdom")).thenReturn(new Fiefdoms("testFiefdom", null));
        when(CurrentPlayers.currentPlayers.get("testBuyer")).thenReturn(new Knight("testBuyer", "testRank"));
        when(CurrentPlayers.currentPlayers.get("testFiefdomOwner")).thenReturn(new Knight("testFiefdomOwner", "testRank"));
        when(CurrentPlayers.currentPlayers.get("testFiefdomOwner").getFiefdoms()).thenReturn(Collections.singletonList(new Fiefdoms("testFiefdom", null)));
        when(CurrentPlayers.currentPlayers.get("testFiefdom").getLoyalVassals()).thenReturn(10);

        try {
            purchase.vassal(mockArgIterator);
            fail("Expected InsufficientAvailableVassalsException");
        } catch (InsufficientAvailableVassalsException e) {
            // Expected
        }
    }
}