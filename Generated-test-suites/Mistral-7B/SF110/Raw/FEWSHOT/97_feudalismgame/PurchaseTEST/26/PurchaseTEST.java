package src.test.java;

import static org.junit.Assert.*;
import src.Purchase;
import src.Fiefdoms;
import src.Knight;
import src.Actions;
import src.GameAutoActions;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        Fiefdoms fiefdom = new Fiefdoms();
        Map map = new Map();
        map.put("testFiefdom", fiefdom);
        GameAutoActions gameAutoActions = new GameAutoActions();
        CurrentPlayers currentPlayers = new CurrentPlayers();
        Knight buyer1 = new Knight("buyer1", "count");
        Knight buyer2 = new Knight("buyer2", "king");
        currentPlayers.put("buyer1", buyer1);
        currentPlayers.put("buyer2", buyer2);
        Iterator<String> args = Arrays.asList("5", "buyer1", "testFiefdom").iterator();

        purchase.perform(Arrays.asList(args));

        assertEquals(5, fiefdom.getLoyalVassals());
        assertEquals(5, buyer1.getTotalMoney());

        Iterator<String> args2 = Arrays.asList("10", "buyer1", "testFiefdom").iterator();

        purchase.perform(Arrays.asList(args2));

        assertEquals(10, fiefdom.getLoyalVassals());
        assertEquals(0, buyer1.getTotalMoney());

        // Test buying for another player
        Knight buyer3 = new Knight("buyer3", "king");
        currentPlayers.put("buyer3", buyer3);

        Iterator<String> args3 = Arrays.asList("5", "buyer3", "testFiefdom").iterator();

        purchase.perform(Arrays.asList(args3));

        assertEquals(0, fiefdom.getLoyalVassals());
        assertEquals(5, buyer3.getTotalMoney());

        // Test buying for a count or duke
        Knight count = new Knight("count", "count");
        Knight duke = new Knight("duke", "duke");
        currentPlayers.put("count", count);
        currentPlayers.put("duke", duke);

        Iterator<String> args4 = Arrays.asList("5", "count", "testFiefdom").iterator();

        purchase.perform(Arrays.asList(args4));

        assertEquals(0, fiefdom.getLoyalVassals());
        assertEquals(5, count.getTotalMoney());

        Iterator<String> args5 = Arrays.asList("5", "duke", "testFiefdom").iterator();

        purchase.perform(Arrays.asList(args5));

        assertEquals(5, fiefdom.getLoyalVassals());
        assertEquals(5, duke.getTotalMoney() - 25);

        // Test buying for a fiefdom that doesn't belong to the kingdom
        Fiefdoms fiefdom2 = new Fiefdoms();
        Map map2 = new Map();
        map2.put("testFiefdom2", fiefdom2);
        fiefdom2.setOwner(null);
        Iterator<String> args6 = Arrays.asList("5", "buyer1", "testFiefdom2").iterator();

        purchase.perform(Arrays.asList(args6));

        assertEquals(0, fiefdom2.getLoyalVassals());
        assertEquals(5, buyer1.getTotalMoney());

        // Test buying for a fiefdom with vassals or castles
        Fiefdoms fiefdom3 = new Fiefdoms();
        fiefdom3.setOwner("owner");
        fiefdom3.setLoyalVassals(5);
        fiefdom3.setCastles(5);
        Map map3 = new Map();
        map3.put("testFiefdom3", fiefdom3);
        Iterator<String> args7 = Arrays.asList("5", "buyer1", "testFiefdom3").iterator();

        purchase.perform(Arrays.asList(args7));

        assertEquals(10, fiefdom3.getLoyalVassals());
        assertEquals(5, buyer1.getTotalMoney() - 25);
    }

    @Test
    public void testCastleFiefdom() {
        Purchase purchase = new Purchase();
        Fiefdoms fiefdom = new Fiefdoms();
        Map map = new Map();
        map.put("testFiefdom", fiefdom);
        GameAutoActions gameAutoActions = new GameAutoActions();
        CurrentPlayers currentPlayers = new CurrentPlayers();
        Knight buyer1 = new Knight("buyer1", "king");
        Knight buyer2 = new Knight("buyer2", "king");
        currentPlayers.put("buyer1", buyer1);
        currentPlayers.put("buyer2", buyer2);
        Iterator<String> args = Arrays.asList("5", "buyer1", "testFiefdom").iterator();

        purchase.perform(Arrays.asList(args));

        assertEquals(5, fiefdom.getCastles());
        assertEquals(5 * 25, buyer1.getTotalMoney());

        Iterator<String> args2 = Arrays.asList("5", "buyer1", "testFiefdom").iterator();

        purchase.perform(Arrays.asList(args2));

        assertEquals(10, fiefdom.getCastles());
        assertEquals(5 * 25 - 25 * 5, buyer1.getTotalMoney());

        // Test buying for another player
        Knight buyer3 = new Knight("buyer3", "king");
        currentPlayers.put("buyer3", buyer3);

        Iterator<String> args3 = Arrays.asList("5", "buyer3", "testFiefdom").iterator();

        purchase.perform(Arrays.asList(args3));

        assertEquals(5, fiefdom.getCastles());
        assertEquals(5 * 25, buyer3.getTotalMoney());

        // Test buying for a count or duke
        Knight count = new Knight("count", "count");
        Knight duke = new Knight("duke", "duke");
        currentPlayers.put("count", count);
        currentPlayers.put("duke", duke);

        Iterator<String> args4 = Arrays.asList("5", "count", "testFiefdom").iterator();

        purchase.perform(Arrays.asList(args4));

        assertEquals(5, fiefdom.getCastles());
        assertEquals(5 * 25, count.getTotalMoney());

        // Test buying for a fiefdom that doesn't belong to the kingdom
        Fiefdoms fiefdom2 = new Fiefdoms();
        fiefdom2.setOwner(null);
        Iterator<String> args5 = Arrays.asList("5", "buyer1", "testFiefdom2").iterator();

        purchase.perform(Arrays.asList(args5));

        assertEquals(5, fiefdom2.getCastles());
        assertEquals(5 * 25, buyer1.getTotalMoney());

        // Test buying for a fiefdom with castles
        Fiefdoms fiefdom3 = new Fiefdoms();
        fiefdom3.setOwner("owner");
        fiefdom3.setCastles(5);
        Iterator<String> args6 = Arrays.asList("5", "buyer1", "testFiefdom3").iterator();

        purchase.perform(Arrays.asList(args6));

        assertEquals(10, fiefdom3.getCastles());
        assertEquals(5 * 25 - 25 * 5, buyer1.getTotalMoney());
    }

    @Test
    public void testCastlePersonal() {
        Purchase purchase = new Purchase();
        Fiefdoms fiefdom = new Fiefdoms();
        Map map = new Map();
        map.put("testFiefdom", fiefdom);
        GameAutoActions gameAutoActions = new GameAutoActions();
        CurrentPlayers currentPlayers = new CurrentPlayers();
        Knight buyer1 = new Knight("buyer1", "king");
        Knight buyer2 = new Knight("buyer2", "king");
        Knight count = new Knight("count", "count");
        Knight duke = new Knight("duke", "duke");
        currentPlayers.put("buyer1", buyer1);
        currentPlayers.put("buyer2", buyer2);
        currentPlayers.put("count", count);
        currentPlayers.put("duke", duke);
        Iterator<String> args = Arrays.asList("5", "buyer1", "testFiefdom").iterator();

        purchase.perform(Arrays.asList(args));

        assertEquals(5, fiefdom.getCastlesPersonal());
        assertEquals(5 * 10, buyer1.getTotalMoney());

        Iterator<String> args2 = Arrays.asList("5", "buyer1", "testFiefdom").iterator();

        purchase.perform(Arrays.asList(args2));

        assertEquals(10, fiefdom.getCastlesPersonal());
        assertEquals(5 * 10 - 10 * 5, buyer1.getTotalMoney());

        // Test buying for another player
        Knight buyer3 = new Knight("buyer3", "king");
        currentPlayers.put("buyer3", buyer3);

        Iterator<String> args3 = Arrays.asList("5", "buyer3", "testFiefdom").iterator();

        purchase.perform(Arrays.asList(args3));

        assertEquals(5, fiefdom.getCastlesPersonal());
        assertEquals(5 * 10, buyer3.getTotalMoney());

        // Test buying for a count or duke
        Iterator<String> args4 = Arrays.asList("5", "count", "testFiefdom").iterator();

        purchase.perform(Arrays.asList(args4));

        assertEquals(0, fiefdom.getCastlesPersonal());
        assertEquals(5 * 10, count.getTotalMoney());

        Iterator<String> args5 = Arrays.asList("5", "duke", "testFiefdom").iterator();

        purchase.perform(Arrays.asList(args5));

        assertEquals(5, fiefdom.getCastlesPersonal());
        assertEquals(5 * 10, duke.getTotalMoney() - 5 * 10);

        // Test buying for a fiefdom that doesn't belong to the kingdom
        Fiefdoms fiefdom2 = new Fiefdoms();
        fiefdom2.setOwner(null);
        Iterator<String> args6 = Arrays.asList("5", "buyer1", "testFiefdom2").iterator();

        purchase.perform(Arrays.asList(args6));

        assertEquals(5, fiefdom2.getCastlesPersonal());
        assertEquals(5 * 10, buyer1.getTotalMoney());

        // Test buying for a fiefdom with castles
        Fiefdoms fiefdom3 = new Fiefdoms();
        fiefdom3.setOwner("owner");
        fiefdom3.setCastlesPersonal(5);
        Iterator<String> args7 = Arrays.asList("5", "buyer1", "testFiefdom3").iterator();

        purchase.perform(Arrays.asList(args7));

        assertEquals(10, fiefdom3.getCastlesPersonal());
        assertEquals(5 * 10 - 10 * 5, buyer1.getTotalMoney());
    }

    @Test
    public void testMercenary() {
        Purchase purchase = new Purchase();
        GameAutoActions gameAutoActions = new GameAutoActions();
        CurrentPlayers currentPlayers = new CurrentPlayers();
        Knight buyer1 = new Knight("buyer1", "king");
        Knight buyer2 = new Knight("buyer2", "king");
        Knight mercenary = new Knight("mercenary", "knight");
        currentPlayers.put("buyer1", buyer1);
        currentPlayers.put("buyer2", buyer2);
        Iterator<String> args = Arrays.asList("5", "buyer1", "mercenary").iterator();

        purchase.perform(Arrays.asList(args));

        assertEquals(5, mercenary.getMercenaries());
        assertEquals(5, buyer1.getTotalMoney());

        Iterator<String> args2 = Arrays.asList("5", "buyer1", "mercenary").iterator();

        purchase.perform(Arrays.asList(args2));

        assertEquals(10, mercenary.getMercenaries());
        assertEquals(5, buyer1.getTotalMoney() - 5);

        // Test buying for another player
        Knight buyer3 = new Knight("buyer3", "king");
        currentPlayers.put("buyer3", buyer3);

        Iterator<String> args3 = Arrays.asList("5", "buyer3", "mercenary").iterator();

        purchase.perform(Arrays.asList(args3));

        assertEquals(5, mercenary.getMercenaries());
        assertEquals(5, buyer3.getTotalMoney());

        // Test buying for a player with insufficient funds
        Iterator<String> args4 = Arrays.asList("5", "buyer1", "mercenary").iterator();

        purchase.perform(Arrays.asList(args4));

        assertEquals(5, mercenary.getMercenaries());
        assertEquals(0, buyer1.getTotalMoney());

        // Test buying for a non-existent knight
        Knight inexistentKnight = new Knight("inexistentKnight", "knight");
        currentPlayers.put("inexistentKnight", inexistentKnight);

        Iterator<String> args5 = Arrays.asList("5", "buyer1", "inexistentKnight").iterator();

        purchase.perform(Arrays.asList(args5));

        assertEquals(5, inexistentKnight.getMercenaries());
        assertEquals(5, buyer1.getTotalMoney());
    }
}