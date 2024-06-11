package src.test.java;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;

public class PurchaseTest {

    private Purchase purchase;
    private Iterator<Object> argIterator;
    private Fiefdoms fiefdom;
    private Knight buyer, knight;
    private CurrentPlayers currentPlayers;
    private Map map;

    @Before
    public void setUp() {
        purchase = new Purchase();
        argIterator = Mockito.mock(Iterator.class);
        fiefdom = Mockito.mock(Fiefdoms.class);
        buyer = Mockito.mock(Knight.class);
        knight = Mockito.mock(Knight.class);
        currentPlayers = Mockito.mock(CurrentPlayers.class);
        map = Mockito.mock(Map.class);

        Mockito.when(argIterator.next()).thenReturn("10").thenReturn("JohnDoe").thenReturn("fiefdom1").thenReturn(buyer).thenReturn(argIterator);
        Mockito.when(argIterator.hasNext()).thenReturn(true).thenReturn(false);
        Mockito.when(map.get(Mockito.eq("fiefdom1"))).thenReturn(fiefdom);
        Mockito.when(currentPlayers.get("JohnDoe")).thenReturn(buyer);
        Mockito.when(currentPlayers.get(fiefdom.getOwner())).thenReturn(Mockito.mock(Knight.class));
    }

    @Test
    public void testVassal() {
        purchase.vassal(argIterator);
        Mockito.verify(buyer, Mockito.times(1)).setTotalMoney(10, false);
        Mockito.verify(fiefdom, Mockito.times(1)).setLoyalVassals(1, true);

        argIterator = Mockito.mock(Iterator.class);
        Mockito.when(argIterator.next()).thenReturn("5").thenReturn("JohnDoe").thenReturn("fiefdom1").thenReturn(buyer);
        Mockito.when(argIterator.hasNext()).thenReturn(false);

        purchase.vassal(argIterator);
        Mockito.verify(buyer, Mockito.times(1)).setTotalMoney(5, false);
        Mockito.verify(fiefdom, Mockito.times(1)).setLoyalVassals(5, true);

        argIterator = Mockito.mock(Iterator.class);
        Mockito.when(argIterator.next()).thenReturn("20").thenReturn("JohnDoe").thenReturn("fiefdom1").thenReturn(buyer);

        Mockito.when(buyer.getTotalMoney()).thenReturn(50);

        purchase.vassal(argIterator);
        Mockito.verify(buyer, Mockito.times(1)).setTotalMoney(-15, false);
        Mockito.verify(System.out, Mockito.times(1)).println("You cannot buy more vassals, you have insufficient funds");
    }

    @Test
    public void testCastleFiefdom() {
        purchase.castleFiefdom(argIterator);
        Mockito.verify(buyer, Mockito.times(1)).setTotalMoney(25, false);
        Mockito.verify(fiefdom, Mockito.times(1)).buyFiefdomCastles(1);

        argIterator = Mockito.mock(Iterator.class);
        Mockito.when(argIterator.next()).thenReturn("5").thenReturn("JohnDoe").thenReturn("fiefdom1").thenReturn(buyer);
        Mockito.when(buyer.getTotalMoney()).thenReturn(50);

        purchase.castleFiefdom(argIterator);
        Mockito.verify(buyer, Mockito.times(1)).setTotalMoney(-125, false);
        Mockito.verify(System.out, Mockito.times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");

        Mockito.when(fiefdom.getOwner()).thenReturn(knight);

        purchase.castleFiefdom(argIterator);
        Mockito.verify(buyer, Mockito.times(1)).setTotalMoney(25, false);
        Mockito.verify(knight, Mockito.times(1)).isFiefdomMine("fiefdom1");
        Mockito.verify(fiefdom, Mockito.times(1)).buyFiefdomCastles(1);
    }

    @Test
    public void testCastlePersonal() {
        purchase.castlePersonal(argIterator);
        Mockito.verify(buyer, Mockito.times(1)).setTotalMoney(10, false);
        Mockito.verify(fiefdom, Mockito.times(1)).buyPersonalCastles(1);

        argIterator = Mockito.mock(Iterator.class);
        Mockito.when(argIterator.next()).thenReturn("5").thenReturn("JohnDoe").thenReturn("fiefdom1").thenReturn(buyer);
        Mockito.when(buyer.getTotalMoney()).thenReturn(50);

        purchase.castlePersonal(argIterator);
        Mockito.verify(buyer, Mockito.times(1)).setTotalMoney(-50, false);
        Mockito.verify(System.out, Mockito.times(1)).println("You cannot buy more Personal castle, you have insufficient funds");

        Mockito.when(fiefdom.getOwner()).thenReturn(knight);

        purchase.castlePersonal(argIterator);
        Mockito.verify(buyer, Mockito.times(1)).setTotalMoney(10, false);
        Mockito.verify(knight, Mockito.times(1)).isFiefdomMine("fiefdom1");
        Mockito.verify(fiefdom, Mockito.times(1)).buyPersonalCastles(1);

        Mockito.when(CurrentPlayers.currentPlayers.get(fiefdom.getOwner())).thenReturn(Mockito.mock(Knight.class).withNoArgsConstructor());

        purchase.castlePersonal(argIterator);
        Mockito.verify(buyer, Mockito.times(1)).setTotalMoney(10, false);
        Mockito.verify(fiefdom, Mockito.times(1)).buyPersonalCastles(1);
        Mockito.verify(CurrentPlayers.currentPlayers.get(fiefdom.getOwner()), Mockito.never()).isFiefdomMine("fiefdom1");
    }

    @Test
    public void testMercenary() {
        purchase.mercenary(argIterator);
        Mockito.verify(buyer, Mockito.times(1)).setTotalMoney(10, false);
        Mockito.verify(knight, Mockito.times(1)).addMercenaries(1);

        argIterator = Mockito.mock(Iterator.class);
        Mockito.when(argIterator.next()).thenReturn("5").thenReturn("JohnDoe").thenReturn("knight1").thenReturn(buyer);
        Mockito.when(buyer.getTotalMoney()).thenReturn(50);

        purchase.mercenary(argIterator);
        Mockito.verify(buyer, Mockito.times(1)).setTotalMoney(-10, false);
        Mockito.verify(System.out, Mockito.times(1)).println("You cannot buy more mercenaries, you have insufficient funds");

        Mockito.when(buyer.getName()).thenReturn("knight1");

        purchase.mercenary(argIterator);
        Mockito.verify(buyer, Mockito.times(1)).setTotalMoney(10, false);
        Mockito.verify(knight, Mockito.times(1)).addMercenaries(1);

        Mockito.when(buyer.getName()).thenReturn("JohnDoe");

        purchase.mercenary(argIterator);
        Mockito.verify(buyer, Mockito.times(1)).setTotalMoney(10, false);
        Mockito.verify(knight, Mockito.times(1)).addMercenaries(1);
        Mockito.verify(buyer, Mockito.times(1)).getName();
        Mockito.verify(System.out, Mockito.never()).println("You cannot buy a Personal Castle for Counts or Dukes");
    }
}