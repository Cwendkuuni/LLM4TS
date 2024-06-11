package src.test.util; // Make sure to create a package named "src.test.util" in your project before running the tests

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.lang.reflect.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import src.Actions;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;
import src.CurrentPlayers;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseTest {

    @Mock
    private Actions actions;

    @Mock
    private Fiefdoms map;

    @Mock
    private GameAutoActions gameAutoActions;

    @Mock
    private CurrentPlayers currentPlayers;

    private Purchase purchase;

    @Test
    public void testPerform() {
        when(map.get(anyString())).thenReturn(any(Fiefdoms.class));
        when(currentPlayers.get(anyString())).thenReturn(any(Knight.class));

        purchase = new Purchase() {
            @Override
            public void perform(final Collection<String> args) {
                super.perform(args);
                actions.perform(args);
            }
        };

        List<String> args = Arrays.asList("arg1", "arg2", "arg3");
        purchase.perform(args);

        verify(actions, times(1)).perform(args);
    }

    @Test
    public void testVassal() {
        Fiefdoms fiefdom = mock(Fiefdoms.class);
        when(map.get(anyString())).thenReturn(fiefdom);
        Knight buyerObj = mock(Knight.class);

        when(currentPlayers.get(anyString())).thenReturn(buyerObj);

        purchase = new Purchase();

        Iterator<String> arg = Arrays.asList("1", "buyerName", "fiefdomName").iterator();

        purchase.vassal(arg);

        verify(buyerObj, times(1)).setTotalMoney(1, false);
        verify(fiefdom, times(1)).setLoyalVassals(1, true);
    }

    @Test
    public void testVassal_InsufficientFunds() {
        Fiefdoms fiefdom = mock(Fiefdoms.class);
        when(map.get(anyString())).thenReturn(fiefdom);
        Knight buyerObj = mock(Knight.class);

        when(currentPlayers.get(anyString())).thenReturn(buyerObj);

        purchase = new Purchase();

        Iterator<String> arg = Arrays.asList("1", "buyerName", "fiefdomName").iterator();

        when(buyerObj.getTotalMoney()).thenReturn(0);

        purchase.vassal(arg);

        verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
    }

    @Test
    public void testVassal_FiefdomBelongsToOtherPlayer() {
        Fiefdoms fiefdom = mock(Fiefdoms.class);
        when(map.get(anyString())).thenReturn(fiefdom);
        Knight buyerObj = mock(Knight.class);

        when(currentPlayers.get(anyString())).thenReturn(buyerObj);

        purchase = new Purchase();

        Iterator<String> arg = Arrays.asList("1", "buyerName", "enemyFiefdomName").iterator();

        when(fiefdom.getOwner()).thenReturn("enemyPlayerName");

        purchase.vassal(arg);

        verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
    }

    @Test
    public void testVassal_BuyerIsKingOrHasPermission() {
        Fiefdoms fiefdom = mock(Fiefdoms.class);
        when(map.get(anyString())).thenReturn(fiefdom);
        Knight buyerObj = mock(Knight.class);
        Knight enemyObj = mock(Knight.class);

        when(currentPlayers.get(anyString())).thenReturn(buyerObj);

        when(currentPlayers.get(anyString())).thenReturn(enemyObj);

        purchase = new Purchase();

        Iterator<String> arg = Arrays.asList("1", "buyerName", "buyerFiefdomName").iterator();

        when(fiefdom.getOwner()).thenReturn(null);

        purchase.vassal(arg);

        verify(buyerObj, times(1)).setTotalMoney(1, false);
        verify(fiefdom, times(1)).setLoyalVassals(1, true);
        verify(gameAutoActions, times(1)).saveAll();
    }

    @Test
    public void testVassal_BuyerIsCountOrDuke() {
        Fiefdoms fiefdom = mock(Fiefdoms.class);
        when(map.get(anyString())).thenReturn(fiefdom);
        Knight buyerObj = mock(Knight.class);

        when(currentPlayers.get(anyString())).thenReturn(buyerObj);

        purchase = new Purchase();

        Iterator<String> arg = Arrays.asList("1", "buyerName", "buyerFiefdomName").iterator();

        when(currentPlayers.get(fiefdom.getOwner())).thenReturn(new Knight() {
            @Override
            public String getRank() {
                return "count";
            }
        });

        purchase.vassal(arg);

        verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
    }

    @Test
    public void testCastleFiefdom() {
        Fiefdoms fiefdom = mock(Fiefdoms.class);
        when(map.get(anyString())).thenReturn(fiefdom);
        Knight buyerObj = mock(Knight.class);

        when(currentPlayers.get(anyString())).thenReturn(buyerObj);

        purchase = new Purchase();

        Iterator<String> arg = Arrays.asList("1", "buyerName", "fiefdomName").iterator();

        purchase.castleFiefdom(arg);

        verify(buyerObj, times(1)).setTotalMoney(25, false);
        verify(fiefdom, times(1)).buyFiefdomCastles(1);
        verify(gameAutoActions, times(1)).saveAll();
    }

    // Add more test cases for the other methods: "castlePersonal", "mercenary"
}