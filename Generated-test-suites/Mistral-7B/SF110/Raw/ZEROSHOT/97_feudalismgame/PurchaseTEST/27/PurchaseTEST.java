package src.test.java;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import static org.mockito.Mockito.*;

public class PurchaseTest {

    private Purchase purchase;
    private List<String> argsList;
    private Iterator<String> argsIterator;
    private Fiefdoms fiefdom;
    private Knight buyer, knight;
    private CurrentPlayers currentPlayers;
    private Map map;

    @Before
    public void setUp() {
        purchase = new Purchase();
        argsList = new ArrayList<>();
        argsIterator = argsList.iterator();
        fiefdom = mock(Fiefdoms.class);
        buyer = mock(Knight.class);
        knight = mock(Knight.class);
        currentPlayers = mock(CurrentPlayers.class);
        map = mock(Map.class);

        when(map.get(anyString())).thenReturn(fiefdom);
        when(currentPlayers.get(anyString())).thenReturn(buyer);
        when(currentPlayers.get(eq(knight.getName()))).thenReturn(knight);
    }

    @After
    public void tearDown() {
        reset(fiefdom, buyer, knight, currentPlayers, map);
    }

    @Test
    public void testPerform() {
        argsList.add("vassal");
        argsList.add("1");
        argsList.add("CastleFiefdom");
        argsList.add("JohnDoe");

        purchase.perform(argsIterator);

        verify(purchase, times(1)).vassal(argsIterator);
    }

    @Test
    public void testVassal_validArguments() {
        argsList.clear();
        argsList.add("vassal");
        argsList.add("2");
        argsList.add("JohnDoe");
        argsList.add("CastleFiefdom");

        when(fiefdom.getOwner()).thenReturn(null);
        when(buyer.getRank()).thenReturn("knight");
        when(buyer.getTotalMoney()).thenReturn(50);
        when(buyer.isFiefdomMine(eq("CastleFiefdom"))).thenReturn(false);

        purchase.perform(argsIterator);

        verify(fiefdom, times(1)).setLoyalVassals(2, anyBoolean());
        verify(buyer, times(1)).setTotalMoney(-1, anyBoolean());
        verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
    }

    @Test
    public void testVassal_invalidArguments() {
        argsList.clear();
        argsList.add("vassal");
        argsList.add("0");
        argsList.add("JohnDoe");
        argsList.add("CastleFiefdom");

        when(fiefdom.getOwner()).thenReturn(null);
        when(buyer.getRank()).thenReturn("knight");
        when(buyer.getTotalMoney()).thenReturn(50);

        purchase.perform(argsIterator);

        verify(System.out, times(1)).println("Invalid arguments passed to Purchase.vassal method.");
    }

    @Test
    public void testVassal_unavailableFiefdom() {
        argsList.clear();
        argsList.add("vassal");
        argsList.add("1");
        argsList.add("JohnDoe");
        argsList.add("Fiefdom1");

        when(fiefdom.getOwner()).thenReturn(new Knight("King") {});
        when(buyer.getRank()).thenReturn("knight");
        when(buyer.getTotalMoney()).thenReturn(50);

        purchase.perform(argsIterator);

        verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
    }

    @Test
    public void testVassal_insufficientFunds() {
        argsList.clear();
        argsList.add("vassal");
        argsList.add("1");
        argsList.add("JohnDoe");
        argsList.add("CastleFiefdom");

        when(fiefdom.getOwner()).thenReturn(null);
        when(buyer.getRank()).thenReturn("knight");
        when(buyer.getTotalMoney()).thenReturn(10);

        purchase.perform(argsIterator);

        verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
    }

    @Test
    public void testVassal_alreadyMine() {
        argsList.clear();
        argsList.add("vassal");
        argsList.add("1");
        argsList.add("JohnDoe");
        argsList.add("CastleFiefdom");

        when(fiefdom.getOwner()).thenReturn(mock(Knight.class));
        when(buyer.getRank()).thenReturn("king");
        when(buyer.getTotalMoney()).thenReturn(50);
        when(buyer.isFiefdomMine("CastleFiefdom")).thenReturn(true);

        purchase.perform(argsIterator);

        verify(fiefdom, times(1)).setLoyalVassals(1, anyBoolean());
        verify(buyer, times(1)).setTotalMoney(-1, anyBoolean());
    }

    @Test
    public void testVassal_kingBuyingForCountOrDuke() {
        argsList.clear();
        argsList.add("vassal");
        argsList.add("1");
        argsList.add("JohnDoe");
        argsList.add("CastleFiefdom");

        when(fiefdom.getOwner()).thenReturn(mock(Knight.class));
        when(buyer.getRank()).thenReturn("king");
        when(currentPlayers.get(fiefdom.getOwner()).getRank()).thenReturn("count");

        purchase.perform(argsIterator);

        verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
    }

    @Test
    public void testCastleFiefdom_validArguments() {
        argsList.clear();
        argsList.add("castleFiefdom");
        argsList.add("2");
        argsList.add("JohnDoe");
        argsList.add("CastleFiefdom");

        when(fiefdom.getOwner()).thenReturn(null);
        when(buyer.getRank()).thenReturn("king");
        when(buyer.getTotalMoney()).thenReturn(100);
        when(buyer.isFiefdomMine("CastleFiefdom")).thenReturn(false);

        purchase.perform(argsIterator);

        verify(fiefdom, times(1)).buyFiefdomCastles(2);
        verify(buyer, times(1)).setTotalMoney(-50, anyBoolean());
    }

    @Test
    public void testCastleFiefdom_insufficientFunds() {
        argsList.clear();
        argsList.add("castleFiefdom");
        argsList.add("2");
        argsList.add("JohnDoe");
        argsList.add("CastleFiefdom");

        when(fiefdom.getOwner()).thenReturn(null);
        when(buyer.getRank()).thenReturn("king");
        when(buyer.getTotalMoney()).thenReturn(25);

        purchase.perform(argsIterator);

        verify(System.out, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
    }

    @Test
    public void testCastleFiefdom_alreadyMine() {
        argsList.clear();
        argsList.add("castleFiefdom");
        argsList.add("2");
        argsList.add("JohnDoe");
        argsList.add("CastleFiefdom");

        when(fiefdom.getOwner()).thenReturn(mock(Knight.class));
        when(buyer.getRank()).thenReturn("king");
        when(buyer.getTotalMoney()).thenReturn(100);
        when(buyer.isFiefdomMine("CastleFiefdom")).thenReturn(true);

        purchase.perform(argsIterator);

        verify(fiefdom, times(1)).buyFiefdomCastles(0);
    }

    @Test
    public void testCastleFiefdom_kingBuyingForCountOrDuke() {
        argsList.clear();
        argsList.add("castleFiefdom");
        argsList.add("2");
        argsList.add("JohnDoe");
        argsList.add("CastleFiefdom");

        when(fiefdom.getOwner()).thenReturn(mock(Knight.class));
        when(buyer.getRank()).thenReturn("king");
        when(currentPlayers.get(fiefdom.getOwner()).getRank()).thenReturn("count");

        purchase.perform(argsIterator);

        verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
    }

    // Add test cases for castlePersonal and mercenary methods here
}