package src.test.java;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import static org.junit.Assert.*;

public class PurchaseTest {

    private Purchase purchase;
    private Fiefdoms fiefdom;
    private Knight buyer, knight;
    private CurrentPlayers currentPlayers;
    private Map map;

    @Before
    public void setUp() {
        // Initialize objects used in the tests
        purchase = new Purchase();
        fiefdom = new Fiefdoms("testFiefdom", null);
        buyer = new Knight("testBuyer", "knight", 100);
        knight = new Knight("testKnight", "knight", 50);
        currentPlayers = new CurrentPlayers();
        map = new Map();
        currentPlayers.addPlayer(buyer);
        currentPlayers.addPlayer(knight);
        map.addFiefdom(fiefdom);
    }

    @After
    public void tearDown() {
        // Clean up objects used in the tests
        purchase = null;
        fiefdom = null;
        buyer = null;
        knight = null;
        currentPlayers = null;
        map = null;
    }

    @Test
    public void testVassal() {
        Iterator<Object> args = new ArrayList<>(Arrays.asList("1", "testBuyer", "testFiefDom")).iterator();
        purchase.vassal(args);
        assertEquals(1, fiefdom.getLoyalVassals());
        args = new ArrayList<>(Arrays.asList("2", "testBuyer", "testFiefDom")).iterator();
        purchase.vassal(args);
        assertEquals(2, fiefdom.getLoyalVassals());

        args = new ArrayList<>(Arrays.asList("3", "testBuyer", "testFiefDom")).iterator();
        purchase.vassal(args);
        Assert.assertThat(buyer.getTotalMoney(), is(lessThan(0)));
        assertFalse(buyer.isTotalMoneyAvailable(3));
    }

    @Test
    public void testCastleFiefdom() {
        Iterator<Object> args = new ArrayList<>(Arrays.asList("1", "testBuyer", "testFiefDom")).iterator();
        purchase.castleFiefdom(args);
        assertEquals(1, fiefdom.getCastles().size());

        args = new ArrayList<>(Arrays.asList("2", "testBuyer", "testFiefDom")).iterator();
        purchase.castleFiefdom(args);
        assertEquals(2, fiefdom.getCastles().size());

        args = new ArrayList<>(Arrays.asList("3", "testBuyer", "testFiefDom")).iterator();
        purchase.castleFiefdom(args);
        Assert.assertThat(buyer.getTotalMoney(), is(lessThan(0)));
        assertFalse(buyer.isTotalMoneyAvailable(75));
    }

    @Test
    public void testCastlePersonal() {
        Iterator<Object> args = new ArrayList<>(Arrays.asList("1", "testBuyer", "testFiefDom")).iterator();
        purchase.castlePersonal(args);
        assertEquals(1, fiefdom.getPersonalCastles().size());

        args = new ArrayList<>(Arrays.asList("2", "testBuyer", "testFiefDom")).iterator();
        purchase.castlePersonal(args);
        assertEquals(2, fiefdom.getPersonalCastles().size());

        args = new ArrayList<>(Arrays.asList("3", "testBuyer", "testFiefDom")).iterator();
        purchase.castlePersonal(args);
        Assert.assertThat(buyer.getTotalMoney(), is(lessThan(0)));
        assertFalse(buyer.isTotalMoneyAvailable(10));
    }

    @Test
    public void testMercenary() {
        Iterator<Object> args = new ArrayList<>(Arrays.asList("1", "testBuyer", "testKnight")).iterator();
        purchase.mercenary(args);
        assertEquals(1, knight.getMercenaries());

        args = new ArrayList<>(Arrays.asList("2", "testBuyer", "testKnight")).iterator();
        purchase.mercenary(args);
        assertEquals(2, knight.getMercenaries());

        args = new ArrayList<>(Arrays.asList("3", "testBuyer", "testKnight")).iterator();
        purchase.mercenary(args);
        Assert.assertThat(buyer.getTotalMoney(), is(lessThan(0)));
        assertFalse(buyer.isTotalMoneyAvailable(3));
    }

    @Test(expected = IllegalAccessException.class)
    public void testIllegalAccessException() {
        Iterator<Object> args = new ArrayList<Object>().iterator();
        purchase.perform(args);
    }

    @Test(expected = NoSuchMethodException.class)
    public void testNoSuchMethodException() {
        Iterator<Object> args = new ArrayList<Object>().iterator();
        args.next();
        Method method = null;
        try {
            method = Purchase.class.getMethod("vassal", new Class[0]);
        } catch (NoSuchMethodException e) {
            assertTrue(true);
        }
        purchase.perform(args);
        purchase.vassal(args);
    }

    @Test(expected = InvocationTargetException.class)
    public void testInvocationTargetException() {
        Iterator<Object> args = new ArrayList<Object>().iterator();
        args.next();
        args.next();
        args.next();
        args.next();
        args.next();
        args.next();
        args.next();
        purchase.perform(args);
        purchase.vassal(args);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() {
        Iterator<Object> args = new ArrayList<Object>().iterator();
        args.next();
        args.next();
        args.next();
        args.next();
        args.next();
        args.next();
        args.next();
        args.next();
        String clazzName = "nonExistentClass";
        Class clazz = null;
        try {
            clazz = Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            assertTrue(true);
        }
        purchase.perform(args);
        purchase.vassal(args);
    }
}