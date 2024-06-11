package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class PurchaseTest {

    private Map<String, Fiefdoms> map;
    private Set<String> currentPlayers;
    private Purchase purchase;

    @Before
    public void setup() {
        map = new HashMap<>();
        currentPlayers = new HashSet<>();
        purchase = new Purchase();
    }

    @Test
    public void testVassal() {
        Fiefdoms fiefdom = new Fiefdoms("testFiefdom", "testOwner");
        Knight buyerObj = new Knight("testBuyer", "knight");
        when(map.get("testFiefdom")).thenReturn(fiefdom);
        when(currentPlayers.contains("testBuyer")).thenReturn(true);

        Iterator<String> args = Collections.singletonList("vassal 1 testFiefdom testBuyer").iterator();
        purchase.perform(args);

        assertEquals(1, fiefdom.getLoyalVassals());
        assertTrue(buyerObj.getTotalMoney() < 1);
    }

    @Test
    public void testVassalInsufficientFunds() {
        Fiefdoms fiefdom = new Fiefdoms("testFiefdom", "testOwner");
        Knight buyerObj = new Knight("testBuyer", "knight");
        when(map.get("testFiefdom")).thenReturn(fiefdom);
        when(currentPlayers.contains("testBuyer")).thenReturn(true);

        Iterator<String> args = Collections.singletonList("vassal 2 testBuyer testFiefdom").iterator();
        purchase.perform(args);

        assertEquals(1, fiefdom.getLoyalVassals());
        assertTrue(buyerObj.getTotalMoney() >= 0);
    }

    @Test
    public void testVassalNotYourFiefdom() {
        Fiefdoms fiefdom1 = new Fiefdoms("testFiefdom1", "testOwner1");
        Fiefdoms fiefdom2 = new Fiefdoms("testFiefdom2", "testOwner2");
        Knight buyerObj = new Knight("testBuyer", "knight");
        when(map.get("testFiefdom1")).thenReturn(fiefdom1);
        when(map.get("testFiefdom2")).thenReturn(fiefdom2);
        when(currentPlayers.contains("testBuyer")).thenReturn(true);

        Iterator<String> args = Collections.singletonList("vassal 1 testBuyer testFiefdom2").iterator();
        purchase.perform(args);

        assertEquals(0, fiefdom2.getLoyalVassals());
        assertEquals(0, buyerObj.getTotalMoney());
    }

    @Test
    public void testVassalNotYourFiefdomOrNotKing() {
        Fiefdoms fiefdom = new Fiefdoms("testFiefdom", "testOwner");
        Knight buyerObj1 = new Knight("testBuyer1", "knight");
        Knight buyerObj2 = new Knight("testBuyer2", "count");
        when(map.get("testFiefdom")).thenReturn(fiefdom);
        when(currentPlayers.contains("testBuyer1")).thenReturn(true);
        when(currentPlayers.contains("testBuyer2")).thenReturn(true);

        Iterator<String> args1 = Collections.singletonList("vassal 1 testBuyer1 testFiefdom").iterator();
        purchase.perform(args1);

        Iterator<String> args2 = Collections.singletonList("vassal 1 testBuyer2 testFiefdom").iterator();
        purchase.perform(args2);

        assertEquals(1, fiefdom.getLoyalVassals());
        assertEquals(0, buyerObj1.getTotalMoney());
        assertEquals(0, buyerObj2.getTotalMoney());
    }

    @Test
    public void testCastleFiefdom() {
        Fiefdoms fiefdom = new Fiefdoms("testFiefdom", "testOwner");
        Knight buyerObj = new Knight("testBuyer", "knight");
        when(map.get("testFiefdom")).thenReturn(fiefdom);
        when(currentPlayers.contains("testBuyer")).thenReturn(true);

        Iterator<String> args = Collections.singletonList("castleFiefdom 1 testBuyer testFiefdom").iterator();
        purchase.perform(args);

        assertEquals(1, fiefdom.getCastles().size());
        assertTrue(buyerObj.getTotalMoney() < 25);
    }

    @Test
    public void testCastleFiefdomInsufficientFunds() {
        Fiefdoms fiefdom = new Fiefdoms("testFiefdom", "testOwner");
        Knight buyerObj = new Knight("testBuyer", "knight");
        when(map.get("testFiefdom")).thenReturn(fiefdom);
        when(currentPlayers.contains("testBuyer")).thenReturn(true);

        Iterator<String> args = Collections.singletonList("castleFiefdom 2 testBuyer testFiefdom").iterator();
        purchase.perform(args);

        assertEquals(1, fiefdom.getCastles().size());
        assertTrue(buyerObj.getTotalMoney() >= 0);
    }

    @Test
    public void testCastleFiefdomNotYourFiefdom() {
        Fiefdoms fiefdom1 = new Fiefdoms("testFiefdom1", "testOwner1");
        Fiefdoms fiefdom2 = new Fiefdoms("testFiefdom2", "testOwner2");
        Knight buyerObj = new Knight("testBuyer", "knight");
        when(map.get("testFiefdom1")).thenReturn(fiefdom1);
        when(map.get("testFiefdom2")).thenReturn(fiefdom2);
        when(currentPlayers.contains("testBuyer")).thenReturn(true);

        Iterator<String> args = Collections.singletonList("castleFiefdom 1 testBuyer testFiefdom2").iterator();
        purchase.perform(args);

        assertEquals(0, fiefdom2.getCastles().size());
        assertEquals(0, buyerObj.getTotalMoney());
    }

    @Test
    public void testCastleFiefdomNotYourFiefdomOrNotKing() {
        Fiefdoms fiefdom = new Fiefdoms("testFiefdom", "testOwner");
        Knight buyerObj1 = new Knight("testBuyer1", "knight");
        Knight buyerObj2 = new Knight("testBuyer2", "count");
        when(map.get("testFiefdom")).thenReturn(fiefdom);
        when(currentPlayers.contains("testBuyer1")).thenReturn(true);
        when(currentPlayers.contains("testBuyer2")).thenReturn(true);

        Iterator<String> args1 = Collections.singletonList("castleFiefdom 1 testBuyer1 testFiefdom").iterator();
        purchase.perform(args1);

        Iterator<String> args2 = Collections.singletonList("castleFiefdom 1 testBuyer2 testFiefdom").iterator();
        purchase.perform(args2);

        assertEquals(1, fiefdom.getCastles().size());
        assertEquals(0, buyerObj1.getTotalMoney());
        assertEquals(0, buyerObj2.getTotalMoney());
    }

    @Test
    public void testCastlePersonal() {
        Fiefdoms fiefdom = new Fiefdoms("testFiefdom", "testOwner");
        Knight buyerObj = new Knight("testBuyer", "knight");
        when(map.get("testFiefdom")).thenReturn(fiefdom);
        when(currentPlayers.contains("testBuyer")).thenReturn(true);

        Iterator<String> args = Collections.singletonList("castlePersonal 1 testBuyer testFiefdom").iterator();
        purchase.perform(args);

        assertEquals(1, fiefdom.getPersonalCastles().size());
        assertTrue(buyerObj.getTotalMoney() < 10);
    }

    @Test
    public void testCastlePersonalInsufficientFunds() {
        Fiefdoms fiefdom = new Fiefdoms("testFiefdom", "testOwner");
        Knight buyerObj = new Knight("testBuyer", "knight");
        when(map.get("testFiefdom")).thenReturn(fiefdom);
        when(currentPlayers.contains("testBuyer")).thenReturn(true);

        Iterator<String> args = Collections.singletonList("castlePersonal 2 testBuyer testFiefdom").iterator();
        purchase.perform(args);

        assertEquals(1, fiefdom.getPersonalCastles().size());
        assertTrue(buyerObj.getTotalMoney() >= 0);
    }

    @Test
    public void testCastlePersonalNotYourFiefdom() {
        Fiefdoms fiefdom1 = new Fiefdoms("testFiefdom1", "testOwner1");
        Fiefdoms fiefdom2 = new Fiefdoms("testFiefdom2", "testOwner2");
        Knight buyerObj = new Knight("testBuyer", "knight");
        when(map.get("testFiefdom1")).thenReturn(fiefdom1);
        when(map.get("testFiefdom2")).thenReturn(fiefdom2);
        when(currentPlayers.contains("testBuyer")).thenReturn(true);

        Iterator<String> args = Collections.singletonList("castlePersonal 1 testBuyer testFiefdom2").iterator();
        purchase.perform(args);

        assertEquals(0, fiefdom2.getPersonalCastles().size());
        assertEquals(0, buyerObj.getTotalMoney());
    }

    @Test
    public void testCastlePersonalNotYourFiefdomOrNotKing() {
        Fiefdoms fiefdom = new Fiefdoms("testFiefdom", "testOwner");
        Knight buyerObj1 = new Knight("testBuyer1", "knight");
        Knight buyerObj2 = new Knight("testBuyer2", "count");
        when(map.get("testFiefdom")).thenReturn(fiefdom);
        when(currentPlayers.contains("testBuyer1")).thenReturn(true);
        when(currentPlayers.contains("testBuyer2")).thenReturn(true);

        Iterator<String> args1 = Collections.singletonList("castlePersonal 1 testBuyer1 testFiefdom").iterator();
        purchase.perform(args1);

        Iterator<String> args2 = Collections.singletonList("castlePersonal 1 testBuyer2 testFiefdom").iterator();
        purchase.perform(args2);

        assertEquals(1, fiefdom.getPersonalCastles().size());
        assertEquals(0, buyerObj1.getTotalMoney());
        assertEquals(0, buyerObj2.getTotalMoney());
    }

    @Test
    public void testMercenary() {
        Knight knight = new Knight("testKnight", "knight");
        Knight buyerObj = new Knight("testBuyer", "knight");
        when(currentPlayers.contains("testBuyer")).thenReturn(true);
        when(currentPlayers.contains("testKnight")).thenReturn(true);

        Iterator<String> args = Collections.singletonList("mercenary 1 testBuyer testKnight").iterator();
        purchase.perform(args);

        assertEquals(1, knight.getMercenaries().size());
        assertTrue(buyerObj.getTotalMoney() < 1);
    }

    @Test
    public void testMercenaryInsufficientFunds() {
        Knight knight = new Knight("testKnight", "knight");
        Knight buyerObj = new Knight("testBuyer", "knight");
        when(currentPlayers.contains("testBuyer")).thenReturn(true);
        when(currentPlayers.contains("testKnight")).thenReturn(true);

        Iterator<String> args = Collections.singletonList("mercenary 2 testBuyer testKnight").iterator();
        purchase.perform(args);

        assertEquals(1, knight.getMercenaries().size());
        assertTrue(buyerObj.getTotalMoney() >= 0);
    }

    @Test
    public void testMercenaryNotYourKnight() {
        Knight knight1 = new Knight("testKnight1", "knight");
        Knight knight2 = new Knight("testKnight2", "knight");
        Knight buyerObj = new Knight("testBuyer", "knight");
        when(currentPlayers.contains("testBuyer")).thenReturn(true);
        when(currentPlayers.contains("testKnight1")).thenReturn(true);
        when(currentPlayers.contains("testKnight2")).thenReturn(true);

        Iterator<String> args = Collections.singletonList("mercenary 1 testBuyer testKnight2").iterator();
        purchase.perform(args);

        assertEquals(0, knight2.getMercenaries().size());
        assertEquals(0, buyerObj.getTotalMoney());
    }

    @Test
    public void testMercenaryNotYourKnightOrNotYourTurn() {
        Knight knight = new Knight("testKnight", "knight");
        Knight buyerObj1 = new Knight("testBuyer1", "knight");
        Knight buyerObj2 = new Knight("testBuyer2", "knight");
        when(currentPlayers.contains("testBuyer1")).thenReturn(true);
        when(currentPlayers.contains("testBuyer2")).thenReturn(true);
        when(currentPlayers.contains("testKnight")).thenReturn(true);

        Iterator<String> args1 = Collections.singletonList("mercenary 1 testBuyer1 testKnight").iterator();
        purchase.perform(args1);

        Iterator<String> args2 = Collections.singletonList("mercenary 1 testBuyer2 testKnight").iterator();
        purchase.perform(args2);

        assertEquals(1, knight.getMercenaries().size());
        assertEquals(0, buyerObj1.getTotalMoney());
        assertEquals(0, buyerObj2.getTotalMoney());
    }
}