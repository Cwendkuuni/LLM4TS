package src.test.java; // Make sure to import necessary packages and classes here

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.lang.reflect.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import src.*;
import src.actions.Actions;
import src.actions.Fiefdoms;
import src.entities.Fiefdoms.Fiefdom;
import src.entities.Fiefdoms.FiefdomCastle;
import src.entities.Knight;
import src.entities.Map;
import src.entities.Player;
import src.exceptions.FiefdomNotFoundException;

public class PurchaseTest {

    private Purchase purchase;
    private Actions actions;
    private Fiefdoms fiefdoms;
    private Map map;
    private Player player1, player2, player3;
    private Fiefdom fiefdom1, fiefdom2, fiefdom3;
    private FiefdomCastle castle1, castle2;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        actions = mock(Actions.class);
        fiefdoms = mock(Fiefdoms.class);
        map = mock(Map.class);
        purchase = new Purchase(actions, fiefdoms, map);
        player1 = new Player("player1", "king");
        player2 = new Player("player2", "count");
        player3 = new Player("player3", "duke");
        fiefdom1 = new Fiefdom("fiefdom1", player1, 10);
        fiefdom2 = new Fiefdom("fiefdom2", player2, 20);
        fiefdom3 = new Fiefdom("fiefdom3", player3, 30);
        when(map.get(eq("fiefdom1"))).thenReturn(fiefdom1);
        when(map.get(eq("fiefdom2"))).thenReturn(fiefdom2);
        when(map.get(eq("fiefdom3"))).thenReturn(fiefdom3);
    }

    @Test
    public void testPerform_ValidArguments() {
        final Iterator args = Arrays.asList(1, "player1", "fiefdom1").iterator();
        purchase.perform(Collections.singletonList(args));
        verify(actions).saveAll();
        verify(fiefdom1).setLoyalVassals(1, true);
        assertEquals("player1", player1.getName());
        assertEquals(9, player1.getTotalMoney());
    }

    @Test(expected = NoSuchMethodException.class)
    public void testPerform_InvalidMethod() {
        final Iterator args = Arrays.asList("invalid", Collections.singletonList(Collections.emptyIterator())).iterator();
        purchase.perform(Collections.singletonList(args));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVassal_InvalidArguments() {
        final Iterator args = Arrays.asList(1, "player1", "nonexistentFiefdom").iterator();
        purchase.vassal(args);
    }

    @Test
    public void testVassal_KingBuy() {
        final Iterator args = Arrays.asList(2, "player1", "fiefdom1").iterator();
        purchase.vassal(args);
        verify(player1).setTotalMoney(2, false);
        verify(fiefdom1).setLoyalVassals(2, true);
        assertEquals("player1", player1.getName());
        assertEquals(8, player1.getTotalMoney());
    }

    @Test
    public void testVassal_BuyFromSelf() {
        final Iterator args = Arrays.asList(2, "player1", "fiefdom1").iterator();
        player1.setName("player2");
        purchase.vassal(args);
        verifyZeroInteractions(player1);
        verifyZeroInteractions(fiefdom1);
    }

    @Test
    public void testVassal_BuyFromAnotherPlayer() {
        final Iterator args = Arrays.asList(2, "player2", "fiefdom1").iterator();
        purchase.vassal(args);
        verifyZeroInteractions(player1);
        verifyZeroInteractions(fiefdom1);
        verifyZeroInteractions(player2);
    }

    @Test
    public void testVassal_BuyFromAnotherPlayer_FiefdomIsMine() {
        player1.setName("player2");
        fiefdom1.setOwner(player1);
        final Iterator args = Arrays.asList(2, "player3", "fiefdom1").iterator();
        purchase.vassal(args);
        verify(player2).setTotalMoney(2, false);
        verify(fiefdom1).setLoyalVassals(2, true);
        assertEquals("player3", player3.getName());
        assertEquals(8, player3.getTotalMoney());
    }

    @Test
    public void testVassal_BuyFromAnotherPlayer_FiefdomIsCountOrDuke() {
        player1.setName("player2");
        fiefdom1.setOwner(player2);
        final Iterator args = Arrays.asList(2, "player3", "fiefdom1").iterator();
        purchase.vassal(args);
        verifyZeroInteractions(player2);
        verifyZeroInteractions(player3);
    }

    @Test
    public void testVassal_FiefdomDoesntBelongToTheKingdom() {
        final Iterator args = Arrays.asList(2, "player1", "nonexistentFiefdom").iterator();
        purchase.vassal(args);
        verifyZeroInteractions(player1);
    }

    @Test
    public void testCastleFiefdom_ValidArguments() {
        final Iterator args = Arrays.asList(3, "player1", "fiefdom1").iterator();
        purchase.castleFiefdom(args);
        verify(player1).setTotalMoney(3 * 25, false);
        verify(fiefdom1).buyFiefdomCastles(3);
        assertEquals("player1", player1.getName());
        assertEquals(73, player1.getTotalMoney());
    }

    // Add more test cases for castleFiefdom and other methods similarly
    
    @Test
    public void testCastlePersonal_ValidArguments() {
        final Iterator args = Arrays.asList(3, "player1", "fiefdom1").iterator();
        purchase.castlePersonal(args);
        verify(player1).setTotalMoney(3 * 10, false);
        verify(fiefdom1).buyPersonalCastles(3);
        assertEquals("player1", player1.getName());
        assertEquals(70, player1.getTotalMoney());
    }

    @Test
    public void testMercenary_ValidArguments() {
        final Iterator args = Arrays.asList(3, "player1", "player2").iterator();
        purchase.mercenary(args);
        verify(player1).setTotalMoney(3, false);
        verify(player2).addMercenaries(3);
        assertEquals("player1", player1.getName());
        assertEquals(9, player1.getTotalMoney());
    }

    @Test
    public void testMercenary_BuyFromSelf() {
        final Iterator args = Arrays.asList(3, "player1", "player1").iterator();
        purchase.mercenary(args);
        verifyZeroInteractions(player1);
    }

    @Test
    public void testMercenary_BuyFromAnotherPlayer() {
        final Iterator args = Arrays.asList(3, "player1", "player2").iterator();
        player1.setName("player3");
        purchase.mercenary(args);
        verifyZeroInteractions(player1);
        verifyZeroInteractions(player2);
    }

    @Test
    public void testMercenary_BuyFromAnotherPlayer_NameIsTheSame() {
        final Iterator args = Arrays.asList(3, "player1", "player1").iterator();
        purchase.mercenary(args);
        verifyZeroInteractions(player1);
    }

    @Test(expected = IllegalAccessException.class)
    public void testPerform_AccessDenied() {
        final Iterator args = Arrays.asList("", null).iterator();
        purchase.perform(Collections.singletonList(args));
    }

    @Test
    public void testPerform_NoMethodFound() {
        final Iterator args = Arrays.asList("invalidMethod", null).iterator();
        purchase.perform(Collections.singletonList(args));
    }

    @Test(expected = InvocationTargetException.class)
    public void testPerform_ReflectionException() {
        final Iterator args = Arrays.asList("exceptionMethod", null).iterator();
        when(actions.saveAll()).thenThrow(new RuntimeException());
        purchase.perform(Collections.singletonList(args));
    }

    @Test
    public void testPerform_FiefdomNotFoundException() {
        final Iterator args = Arrays.asList(2, "player1", "nonexistentFiefdom").iterator();
        when(map.get(eq("nonexistentFiefdom"))).thenThrow(new FiefdomNotFoundException());
        purchase.perform(Collections.singletonList(args));
        verifyZeroInteractions(player1);
    }
}