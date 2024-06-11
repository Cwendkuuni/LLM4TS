package src.test.util; // Make sure to create a package named "src.test.util" in the same directory as your test file

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.lang.reflect.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import src.Actions;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;
import src.Purchase;
import src.CurrentPlayers;

public class PurchaseTest {

    private Purchase purchase;
    private CurrentPlayers currentPlayers;
    private Map map;
    private GameAutoActions gameAutoActions;
    private Fiefdoms fiefdom;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        purchase = new Purchase();
        currentPlayers = mock(CurrentPlayers.class);
        map = mock(Map.class);
        gameAutoActions = mock(GameAutoActions.class);
        fiefdom = mock(Fiefdoms.class);

        when(map.get(anyString())).then(new Answer<Fiefdoms>() {
            @Override
            public Fiefdoms answer(InvocationOnMock invocation) throws Throwable {
                String fiefdomName = invocation.getArgumentAt(0, String.class);
                return fiefdom;
            }
        });

        when(currentPlayers.get(anyString())).then(new Answer<Knight>() {
            @Override
            public Knight answer(InvocationOnMock invocation) throws Throwable {
                String playerName = invocation.getArgumentAt(0, String.class);
                return new Knight("playerName", "rank", null, null, 0, 0, null, null, null, null, null, null, null, null);
            }
        });

        when(gameAutoActions.saveAll()).thenReturn(null);
    }

    @Test
    public void testPerform_ValidArguments() throws Exception {
        final Collection args = Arrays.asList("vassal", Arrays.asList(1, "player1", "fiefdom1"));
        purchase.perform(args);
        verify(purchase).vassal(args.iterator());
    }

    @Test
    public void testPerform_InvalidArguments() throws Exception {
        final Collection args = Arrays.asList("castleFiefdom", null);
        purchase.perform(args);
        verifyZeroInteractions(purchase);
    }

    @Test
    public void testPerform_NoSuchMethodException() throws Exception {
        final Collection args = Arrays.asList("castleFiefdomX", Arrays.asList(1, "player1", "fiefdom1"));
        doThrow(new NoSuchMethodException("No such method exception")).when(purchase).getClass();
        purchase.perform(args);
        verifyZeroInteractions(purchase);
    }

    @Test
    public void testPerform_SecurityException() throws Exception {
        final Collection args = Arrays.asList("castleFiefdom", Arrays.asList(1, "player1", "fiefdom1"));
        doThrow(new SecurityException("Security exception")).when(purchase).getClass();
        purchase.perform(args);
        verifyZeroInteractions(purchase);
    }

    @Test
    public void testVassal_ValidArguments() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(currentPlayers.get(buyerName)).thenReturn(new Knight("player1", "king", null, null, 0, 0, null, null, null, null, null, null, null, null));
        purchase.vassal(Collections.singletonIterator("vassalArguments"));
        verify(fiefdom).setLoyalVassals(1, true);
    }

    @Test
    public void testVassal_OwnerIsKing() {
        when(fiefdom.getOwner()).thenReturn("player1");
        when(currentPlayers.get(buyerName)).thenReturn(new Knight("player1", "king", null, null, 0, 0, null, null, null, null, null, null, null, null));
        purchase.vassal(Collections.singletonIterator("vassalArguments"));
        verifyZeroInteractions(fiefdom);
        verify(currentPlayers.get(buyerName)).setTotalMoney(1, false);
        verify(System.out).println("You cannot buy more vassals, you have insufficient funds");
    }

    @Test
    public void testVassal_OwnerIsCountOrDuke() {
        when(fiefdom.getOwner()).thenReturn("player1");
        when(currentPlayers.get(buyerName)).thenReturn(new Knight("player1", "count", null, null, 0, 0, null, null, null, null, null, null, null, null));
        purchase.vassal(Collections.singletonIterator("vassalArguments"));
        verifyZeroInteractions(fiefdom);
        verify(System.out).println("You can only buy a castle for a fiefdom that belongs to you");
    }

    @Test
    public void testVassal_FiefdomDoesntBelongToTheKingdom() {
        when(fiefdom.getOwner()).thenReturn("player1");
        when(currentPlayers.get(buyerName)).thenReturn(new Knight("player1", "knight", null, null, 0, 0, null, null, null, null, null, null, null, null));
        purchase.vassal(Collections.singletonIterator("vassalArguments"));
        verifyZeroInteractions(fiefdom);
        verify(System.out).println("Fiefdom doesn't belong to the kingdom");
    }

    @Test
    public void testCastleFiefdom_ValidArguments() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(currentPlayers.get(buyerName)).thenReturn(new Knight("player1", "king", null, null, 0, 0, null, null, null, null, null, null, null, null));
        purchase.castleFiefdom(Collections.singletonIterator("castleFiefdomArguments"));
        verify(fiefdom).buyFiefdomCastles(1);
    }

    @Test
    public void testCastleFiefdom_OwnerIsKing() {
        when(fiefdom.getOwner()).thenReturn("player1");
        when(currentPlayers.get(buyerName)).thenReturn(new Knight("player1", "king", null, null, 0, 0, null, null, null, null, null, null, null, null));
        purchase.castleFiefdom(Collections.singletonIterator("castleFiefdomArguments"));
        verifyZeroInteractions(fiefdom);
        verify(currentPlayers.get(buyerName)).setTotalMoney(25, false);
        verify(System.out).println("You cannot buy more Fiefdom castles, you have insufficient funds");
    }

    @Test
    public void testCastleFiefdom_OwnerIsCountOrDuke() {
        when(fiefdom.getOwner()).thenReturn("player1");
        when(currentPlayers.get(buyerName)).thenReturn(new Knight("player1", "count", null, null, 0, 0, null, null, null, null, null, null, null, null));
        purchase.castleFiefdom(Collections.singletonIterator("castleFiefdomArguments"));
        verifyZeroInteractions(fiefdom);
        verify(System.out).println("You can only buy a castle for a fiefdom that belongs to you");
    }

    @Test
    public void testCastleFiefdom_FiefdomDoesntBelongToTheKingdom() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(currentPlayers.get(buyerName)).thenReturn(new Knight("player1", "knight", null, null, 0, 0, null, null, null, null, null, null, null, null));
        purchase.castleFiefdom(Collections.singletonIterator("castleFiefdomArguments"));
        verifyZeroInteractions(fiefdom);
        verify(System.out).println("Fiefdom doesn't belong to the kingdom");
    }

    @Test
    public void testCastlePersonal_ValidArguments() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(currentPlayers.get(buyerName)).thenReturn(new Knight("player1", "king", null, null, 0, 0, null, null, null, null, null, null, null, null));
        purchase.castlePersonal(Collections.singletonIterator("castlePersonalArguments"));
        verify(fiefdom).buyPersonalCastles(1);
    }

    @Test
    public void testCastlePersonal_OwnerIsKingBuyingForCountOrDuke() {
        when(fiefdom.getOwner()).thenReturn("player1");
        when(currentPlayers.get(buyerName)).thenReturn(new Knight("player1", "count", null, null, 0, 0, null, null, null, null, null, null, null, null));
        purchase.castlePersonal(Collections.singletonIterator("castlePersonalArguments"));
        verifyZeroInteractions(fiefdom);
        verify(System.out).println("You can only buy a castle for a fiefdom that belongs to you");
    }

    @Test
    public void testCastlePersonal_OwnerIsKingBuyingForHimself() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(currentPlayers.get(buyerName)).thenReturn(new Knight("player1", "king", null, null, 0, 0, null, null, null, null, null, null, null, null));
        purchase.castlePersonal(Collections.singletonIterator("castlePersonalArguments"));
        verify(currentPlayers.get(buyerName)).setTotalMoney(10, false);
    }

    @Test
    public void testCastlePersonal_OwnerIsCountOrDukeBuyingForHimself() {
        when(fiefdom.getOwner()).thenReturn("player1");
        when(currentPlayers.get(buyerName)).thenReturn(new Knight("player1", "count", null, null, 0, 0, null, null, null, null, null, null, null, null));
        purchase.castlePersonal(Collections.singletonIterator("castlePersonalArguments"));
        verifyZeroInteractions(fiefdom);
        verify(System.out).println("You can only buy a castle for a fiefdom that belongs to you");
    }

    @Test
    public void testCastlePersonal_FiefdomDoesntBelongToTheKingdom() {
        when(fiefdom.getOwner()).thenReturn(null);
        when(currentPlayers.get(buyerName)).thenReturn(new Knight("player1", "knight", null, null, 0, 0, null, null, null, null, null, null, null, null));
        purchase.castlePersonal(Collections.singletonIterator("castlePersonalArguments"));
        verifyZeroInteractions(fiefdom);
        verify(System.out).println("Fiefdom doesn't belong to the kingdom: " + fiefdom.getName() + " owner: " + Map.map.get(fiefdom.getName()).getOwner());
    }

    @Test
    public void testMercenary_ValidArguments() {
        when(currentPlayers.containsKey(buyerName)).thenReturn(true);
        when(currentPlayers.containsKey(knight.getName())).thenReturn(true);
        when(currentPlayers.get(buyerName).getName()).thenReturn("player1");
        when(currentPlayers.get(buyerName)).thenReturn(new Knight("player1", "king", null, null, 0, 0, null, null, null, null, null, null, null, null));
        when(currentPlayers.get(knight.getName()).getName()).thenReturn("knightName");
        when(currentPlayers.get(knight.getName())).thenReturn(knight);
        purchase.mercenary(Collections.singletonIterator("mercenaryArguments"));
        verify(knight).addMercenaries(1);
    }

    @Test
    public void testMercenary_BuyerIsMercenaryOrKingBuyForHimself() {
        when(currentPlayers.containsKey(buyerName)).thenReturn(true);
        when(currentPlayers.containsKey(knight.getName())).thenReturn(true);
        when(currentPlayers.get(buyerName).getName()).thenReturn("player1");
        when(currentPlayers.get(buyerName)).thenReturn(new Knight("player1", "king", null, null, 0, 0, null, null, null, null, null, null, null, null));
        when(currentPlayers.get(knight.getName()).getName()).thenReturn("knightName");
        when(currentPlayers.get(knight.getName())).thenReturn(knight);
        purchase.mercenary(Collections.singletonIterator("mercenaryArguments"));
        verifyZeroInteractions(knight);
        verify(currentPlayers.get(buyerName)).setTotalMoney(1, false);
        verify(System.out).println("You cannot buy more mercenaries, you have insufficient funds");
    }

    @Test
    public void testMercenary_InvalidArguments() {
        when(currentPlayers.containsKey(buyerName)).thenReturn(true);
        when(currentPlayers.get(buyerName).getName()).thenReturn("player1");
        when(currentPlayers.get(buyerName)).thenReturn(new Knight("player1", "king", null, null, 0, 0, null, null, null, null, null, null, null, null));
        when(currentPlayers.containsKey(knight.getName())).thenReturn(false);
        purchase.mercenary(Collections.singletonIterator("mercenaryArguments"));
        verifyZeroInteractions(knight);
        verify(System.out).println("Inexistent Knight");
    }
}