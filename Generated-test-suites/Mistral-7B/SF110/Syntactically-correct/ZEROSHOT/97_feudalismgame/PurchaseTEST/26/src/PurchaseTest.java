package src.test.java;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class PurchaseTest {

    private static Purchase purchase;
    private static List<String> argsList;
    private static Map<String, Fiefdoms> map;
    private static CurrentPlayers currentPlayers;

    @Before
    public void setup() {
        map = new HashMap<>();
        currentPlayers = new CurrentPlayers();
        purchase = new Purchase();
        argsList = new ArrayList<>();

        // Initialize map and currentPlayers for testing
        // ...
    }

    @After
    public void teardown() {
        purchase = null;
        argsList = null;
        map = null;
        currentPlayers = null;
    }

    @Test
    public void testPerform() throws Exception {
        // Test valid arguments
        argsList.add("vassal");
        argsList.add(new ArrayList<Object>() {{
            add(1);
            add("player1");
            add("fiefdom1");
        }}.iterator());
        purchase.perform(argsList);

        // Test invalid arguments
        argsList.clear();
        argsList.add("invalidMethod");
        Assert.assertThrows(NoSuchMethodException.class, () -> purchase.perform(argsList.iterator()));

        // Test SecurityException
        argsList.clear();
        argsList.add("castlePersonal");
        argsList.add(new ArrayList<Object>() {{
            add("player1");
            add("fiefdom1");
        }}.iterator());
        argsList.add("count");
        Assert.assertThrows(SecurityException.class, () -> purchase.perform(argsList.iterator()));

        // Test NoSuchMethodException
        argsList.clear();
        argsList.add("castleFiefdom");
        argsList.add(new ArrayList<Object>() {{
            add(1);
            add("player1");
            add("fiefdom1");
        }}.iterator());
        Assert.assertThrows(NoSuchMethodException.class, () -> purchase.perform(argsList.iterator()));

        // Test IllegalArgumentException
        argsList.clear();
        argsList.add("vassal");
        argsList.add(new ArrayList<Object>() {{
            add("1");
            add("player1");
            add("fiefdom1");
        }}.iterator());
        Assert.assertThrows(IllegalArgumentException.class, () -> purchase.perform(argsList.iterator()));
    }

    @Test
    public void testVassal() {
        // Test valid call
        argsList.clear();
        argsList.add("vassal");
        argsList.add(new ArrayList<Object>() {{
            add(2);
            add("player1");
            add("fiefdom1");
        }}.iterator());
        purchase.perform(argsList.iterator());
        Assert.assertEquals(2, currentPlayers.get("player1").getNumVassals());

        // Test insufficient funds
        argsList.clear();
        argsList.add("vassal");
        argsList.add(new ArrayList<Object>() {{
            add(3);
            add("player1");
            add("fiefdom1");
        }}.iterator());
        purchase.perform(argsList.iterator());
        Assert.assertEquals(2, currentPlayers.get("player1").getNumVassals());

        // Test fiefdom not belonging to the kingdom
        argsList.clear();
        argsList.add("vassal");
        argsList.add(new ArrayList<Object>() {{
            add(3);
            add("player1");
            add("fiefdom2");
        }}.iterator());
        purchase.perform(argsList.iterator());
        Assert.assertEquals(2, currentPlayers.get("player1").getNumVassals());
        Assert.assertEquals(0, map.get("fiefdom2").getLoyalVassals());

        // Test buying vassals for another player's fiefdom
        argsList.clear();
        argsList.add("vassal");
        argsList.add(new ArrayList<Object>() {{
            add(3);
            add("player2");
            add("fiefdom1");
        }}.iterator());
        purchase.perform(argsList.iterator());
        Assert.assertEquals(0, currentPlayers.get("player1").getNumVassals());
        Assert.assertEquals(3, currentPlayers.get("player2").getNumVassals());
        Assert.assertEquals(3, map.get("fiefdom1").getLoyalVassals());
    }

    // Add similar tests for other methods: castleFiefdom, castlePersonal, mercenary
}