package src.test.java;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;

public class PurchaseTest {

    private Purchase purchase;
    private Map map;
    private CurrentPlayers currentPlayers;
    private Fiefdoms fiefdom;
    private Knight buyer, knight;

    @Before
    public void setup() {
        map = Mockito.mock(Map.class);
        currentPlayers = Mockito.mock(CurrentPlayers.class);
        purchase = new Purchase(map, currentPlayers);
        fiefdom = new Fiefdoms();
        buyer = new Knight("buyer");
        knight = new Knight("knight");
    }

    @Test
    public void testPerformWithValidArgs() {
        Iterator iterator = Arrays.asList("3", "buyer", "fiefdom").iterator();
        when(map.get(iterator.next())).thenReturn(fiefdom);
        when(currentPlayers.get(iterator.next())).thenReturn(buyer);

        purchase.perform(Arrays.asList(iterator));

        verify(map).get(iterator.next());
        verify(currentPlayers).get(iterator.next());
        verify(fiefdom).setLoyalVassals(3, true);
    }

    @Test
    public void testVassalWithInvalidArgs() {
        Iterator iterator = Arrays.asList("invalidArg", "buyer", "fiefdom").iterator();

        purchase.vassal(iterator);

        assertEquals("Invalid argument", iterator.next());
    }

    @Test
    public void testVassalWithInsufficientFunds() {
        Iterator iterator = Arrays.asList("3", "buyer", "fiefdom").iterator();
        when(map.get(iterator.next())).thenReturn(fiefdom);
        when(currentPlayers.get(iterator.next())).thenReturn(buyer);
        when(buyer.getTotalMoney()).thenReturn(0);

        purchase.vassal(iterator);

        assertEquals("You cannot buy more vassals, you have insufficient funds", System.out.println(iterator.next()));
    }

    @Test
    public void testVassalForSomeoneElseFiefdom() {
        Iterator iterator = Arrays.asList("3", "buyer", "someoneElseFiefdom").iterator();
        when(map.get(iterator.next())).thenReturn(new Fiefdoms());

        purchase.vassal(iterator);

        assertEquals("Fiefdom doesn't belong to the kingdom", System.out.println(iterator.next()));
    }

    @Test
    public void testVassalForSomeoneElse() {
        Iterator iterator = Arrays.asList("3", "someoneElse", "fiefdom").iterator();

        purchase.vassal(iterator);

        assertEquals("Inexistent Player", System.out.println(iterator.next()));
    }

    @Test
    public void testCastleFiefdomWithInvalidArgs() {
        Iterator iterator = Arrays.asList("invalidArg", "buyer", "fiefdom").iterator();

        purchase.castleFiefdom(iterator);

        assertEquals("Invalid argument", iterator.next());
    }

    @Test
    public void testCastleFiefdomWithInsufficientFunds() {
        Iterator iterator = Arrays.asList("3", "buyer", "fiefdom").iterator();
        when(map.get(iterator.next())).thenReturn(fiefdom);
        when(currentPlayers.get(iterator.next())).thenReturn(buyer);
        when(buyer.getTotalMoney()).thenReturn(0);

        purchase.castleFiefdom(iterator);

        assertEquals("You cannot buy more Fiefdom castles, you have insufficient funds", System.out.println(iterator.next()));
    }

    @Test
    public void testCastleFiefdomForSomeoneElseFiefdom() {
        Iterator iterator = Arrays.asList("3", "buyer", "someoneElseFiefdom").iterator();
        when(map.get(iterator.next())).thenReturn(new Fiefdoms());

        purchase.castleFiefdom(iterator);

        assertEquals("Fiefdom doesn't belong to the kingdom", System.out.println(iterator.next()));
    }

    @Test
    public void testCastleFiefdomForSomeoneElse() {
        Iterator iterator = Arrays.asList("3", "someoneElse", "fiefdom").iterator();

        purchase.castleFiefdom(iterator);

        assertEquals("Inexistent Player", System.out.println(iterator.next()));
    }

    @Test
    public void testCastlePersonalWithInvalidArgs() {
        Iterator iterator = Arrays.asList("invalidArg", "buyer", "fiefdom").iterator();

        purchase.castlePersonal(iterator);

        assertEquals("Invalid argument", iterator.next());
    }

    @Test
    public void testCastlePersonalWithInsufficientFunds() {
        Iterator iterator = Arrays.asList("3", "buyer", "fiefdom").iterator();
        when(map.get(iterator.next())).thenReturn(fiefdom);
        when(currentPlayers.get(iterator.next())).thenReturn(buyer);
        when(buyer.getTotalMoney()).thenReturn(0);

        purchase.castlePersonal(iterator);

        assertEquals("You cannot buy more Personal castle, you have insufficient funds", System.out.println(iterator.next()));
    }

    @Test
    public void testCastlePersonalForSomeoneElseFiefdom() {
        Iterator iterator = Arrays.asList("3", "buyer", "someoneElseFiefdom").iterator();
        when(map.get(iterator.next())).thenReturn(new Fiefdoms());

        purchase.castlePersonal(iterator);

        assertEquals("Fiefdom doesn't belong to the kingdom", System.out.println(iterator.next()));
    }

    @Test
    public void testCastlePersonalForSomeoneElse() {
        Iterator iterator = Arrays.asList("3", "someoneElse", "fiefdom").iterator();

        purchase.castlePersonal(iterator);

        assertEquals("Inexistent Player", System.out.println(iterator.next()));
    }

    @Test
    public void testMercenaryWithInvalidArgs() {
        Iterator iterator = Arrays.asList("invalidArg", "buyer", "knight").iterator();

        purchase.mercenary(iterator);

        assertEquals("Invalid argument", iterator.next());
    }

    @Test
    public void testMercenaryWithSomeoneElseKnight() {
        Iterator iterator = Arrays.asList("3", "buyer", "someoneElseKnight").iterator();

        purchase.mercenary(iterator);

        assertEquals("Inexistent Knight", System.out.println(iterator.next()));
    }

    @Test
    public void testMercenaryForBuyerHimself() {
        Iterator iterator = Arrays.asList("3", "buyer", "knight").iterator();

        purchase.mercenary(iterator);

        assertEquals("You cannot buy mercenaries for yourself", System.out.println(iterator.next()));
    }

    @Test
    public void testMercenaryWithInsufficientFunds() {
        Iterator iterator = Arrays.asList("3", "buyer", "knight").iterator();
        when(currentPlayers.get(iterator.next())).thenReturn(buyer);
        when(buyer.getTotalMoney()).thenReturn(0);

        purchase.mercenary(iterator);

        assertEquals("You cannot buy more mercenaries, you have insufficient funds", System.out.println(iterator.next()));
    }
}