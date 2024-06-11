package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import src.*;
import src.actions.*;
import src.exceptions.*;
import src.fiefs.Fiefdoms;
import src.knight.*;
import src.map.*;
import src.player.*;

public class PurchaseTest {

    private static final String KING_NAME = "king1";
    private static final String COUNT_NAME = "count1";
    private static final String DUKE_NAME = "duke1";
    private static final String VASSAL_OWNER_NAME = "owner1";
    private static final String CASTLE_OWNER_NAME = "owner2";
    private static final String FAKE_NAME = "fakeName";
    private static final String FAKE_FIEDOM_NAME = "fakeFiefdom";
    private static final int VASSAL_QTY = 3;
    private static final int CASTLE_QTY = 2;
    private static final int MERCENARY_QTY = 5;
    private static final String FAKE_KNIGHT_NAME = "fakeKnight";
    private static final String OTHER_KNIGHT_NAME = "otherKnight";
    private static final int FAKE_MONEY = 100;
    private static final int MAX_MONEY = 150;

    private Map map;
    private Fiefdoms fiefdoms;
    private CurrentPlayers currentPlayers;
    private Purchase purchase;
    private Actions actions;

    @Before
    public void setup() {
        map = Mockito.mock(Map.class);
        fiefdoms = new Fiefdoms();
        currentPlayers = new CurrentPlayers();
        purchase = new Purchase();
        actions = Mockito.mock(Actions.class);

        when(map.get(FAKE_FIEDOM_NAME)).thenReturn(fiefdoms);

        // Create players
        Player king = new Player(KING_NAME, "king", MAX_MONEY);
        Player count = new Player(COUNT_NAME, "count", MAX_MONEY);
        Player duke = new Player(DUKE_NAME, "duke", MAX_MONEY);
        Player vassalOwner = new Player(VASSAL_OWNER_NAME, "vassalOwner", MAX_MONEY);
        Player castleOwner = new Player(CASTLE_OWNER_NAME, "castleOwner", MAX_MONEY);

        // Add players to the map
        fiefdoms.addPlayer(king);
        fiefdoms.addPlayer(count);
        fiefdoms.addPlayer(duke);
        fiefdoms.addPlayer(vassalOwner);
        fiefdoms.addPlayer(castleOwner);

        // Add players to the current players
        currentPlayers.addPlayer(king);
        currentPlayers.addPlayer(count);
        currentPlayers.addPlayer(duke);
        currentPlayers.addPlayer(vassalOwner);
        currentPlayers.addPlayer(castleOwner);

        // Set up mocks
        when(map.get(FAKE_FIEDOM_NAME)).thenReturn(fiefdoms);
        when(currentPlayers.get(KING_NAME)).thenReturn(king);
        when(currentPlayers.get(COUNT_NAME)).thenReturn(count);
        when(currentPlayers.get(DUKE_NAME)).thenReturn(duke);
        when(currentPlayers.get(VASSAL_OWNER_NAME)).thenReturn(vassalOwner);
        when(currentPlayers.get(CASTLE_OWNER_NAME)).thenReturn(castleOwner);

        // Set up mocks for methods
        when(map.get(anyString())).thenCallRealMethod();
        when(currentPlayers.get(anyString())).thenCallRealMethod();

        // Set up purchase
        purchase.setMap(map);
        purchase.setCurrentPlayers(currentPlayers);
        purchase.setActions(actions);
    }

    @Test
    public void testPerform() {
        // Test with valid arguments
        Collection args = Arrays.asList("vassal", iteratorWith("3", "king1", "fiefdom1"));
        purchase.perform(args);
        verify(actions).saveAll();

        // Test with invalid arguments
        Collection invalidArgs = Arrays.asList("x", iteratorWith("3", "king1", "fiefdom1"));
        try {
            purchase.perform(invalidArgs);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Collection emptyArgs = Arrays.asList();
        try {
            purchase.perform(emptyArgs);
            fail();
        } catch (NoSuchMethodException e) {
            // Expected
        }
    }

    @Test
    public void testVassal() {
        // Test with valid arguments
        Collection args = Arrays.asList("vassal", iteratorWith("3", "king1", "fiefdom1"));
        purchase.vassal(args.iterator());
        assertEquals(3, fiefdoms.get(FAKE_FIEDOM_NAME).getLoyalVassals());

        // Test with invalid arguments
        Collection invalidArgs = Arrays.asList("vassal", iteratorWith("3", null, "fiefdom1"));
        try {
            purchase.vassal(args.iterator());
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        Collection invalidFiefdomArgs = Arrays.asList("vassal", iteratorWith("3", "king1", null));
        try {
            purchase.vassal(args.iterator());
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCastleFiefdom() {
        // Test with valid arguments
        Collection args = Arrays.asList("castleFiefdom", iteratorWith("3", "king1", "fiefdom1"));
        purchase.castleFiefdom(args.iterator());
        assertEquals(3, fiefdoms.get(FAKE_FIEDOM_NAME).getCastles().size());

        // Test with insufficient funds
        Collection insufficientMoneyArgs = Arrays.asList("castleFiefdom", iteratorWith("3", "king1", "fiefdom1"));
        when(king.getTotalMoney()).thenReturn(FAKE_MONEY);
        purchase.castleFiefdom(args.iterator());
        assertEquals(FAKE_MONEY, king.getTotalMoney());

        // Test with invalid arguments
        Collection invalidArgs = Arrays.asList("castleFiefdom", iteratorWith("3", null, "fiefdom1"));
        try {
            purchase.castleFiefdom(args.iterator());
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        Collection invalidFiefdomArgs = Arrays.asList("castleFiefdom", iteratorWith("3", "king1", null));
        try {
            purchase.castleFiefdom(args.iterator());
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with fiefdom not belonging to the kingdom
        Collection invalidFiefdomOwnerArgs = Arrays.asList("castleFiefdom", iteratorWith("3", "king1", "fakeFiefdom"));
        try {
            purchase.castleFiefdom(args.iterator());
            fail();
        } catch (FiefdomNotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testCastlePersonal() {
        // Test with valid arguments
        Collection args = Arrays.asList("castlePersonal", iteratorWith("3", "king1", "fiefdom1"));
        purchase.castlePersonal(args.iterator());
        assertEquals(3, fiefdoms.get(FAKE_FIEDOM_NAME).getPersonalCastles().size());

        // Test with insufficient funds
        Collection insufficientMoneyArgs = Arrays.asList("castlePersonal", iteratorWith("3", "king1", "fiefdom1"));
        when(king.getTotalMoney()).thenReturn(FAKE_MONEY);
        purchase.castlePersonal(args.iterator());
        assertEquals(FAKE_MONEY, king.getTotalMoney());

        // Test with invalid arguments
        Collection invalidArgs = Arrays.asList("castlePersonal", iteratorWith("3", null, "fiefdom1"));
        try {
            purchase.castlePersonal(args.iterator());
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        Collection invalidFiefdomArgs = Arrays.asList("castlePersonal", iteratorWith("3", "king1", null));
        try {
            purchase.castlePersonal(args.iterator());
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with fiefdom not belonging to the kingdom
        Collection invalidFiefdomOwnerArgs = Arrays.asList("castlePersonal", iteratorWith("3", "king1", "fakeFiefdom"));
        try {
            purchase.castlePersonal(args.iterator());
            fail();
        } catch (FiefdomNotFoundException e) {
            // Expected
        }

        // Test with knight not being a king
        Collection invalidRankArgs = Arrays.asList("castlePersonal", iteratorWith("3", "count1", "fiefdom1"));
        try {
            purchase.castlePersonal(args.iterator());
            fail();
        } catch (InvalidRankException e) {
            // Expected
        }
    }

    @Test
    public void testMercenary() {
        // Test with valid arguments
        Collection args = Arrays.asList("mercenary", iteratorWith("5", "king1", "knight1"));
        purchase.mercenary(args.iterator());
        assertEquals(5, knight1.getMercenaries());

        // Test with insufficient funds
        Collection insufficientMoneyArgs = Arrays.asList("mercenary", iteratorWith("5", "king1", "knight1"));
        when(king.getTotalMoney()).thenReturn(FAKE_MONEY);
        purchase.mercenary(args.iterator());
        assertEquals(FAKE_MONEY, king.getTotalMoney());

        // Test with invalid arguments
        Collection invalidArgs = Arrays.asList("mercenary", iteratorWith("5", null, "knight1"));
        try {
            purchase.mercenary(args.iterator());
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        Collection invalidKnightArgs = Arrays.asList("mercenary", iteratorWith("5", "king1", null));
        try {
            purchase.mercenary(args.iterator());
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with invalid knight name
        Collection invalidKnightNameArgs = Arrays.asList("mercenary", iteratorWith("5", "king1", FAKE_KNIGHT_NAME));
        try {
            purchase.mercenary(args.iterator());
            fail();
        } catch (KnightNotFoundException e) {
            // Expected
        }

        // Test with knights being the same
        Collection sameKnightArgs = Arrays.asList("mercenary", iteratorWith("5", "king1", "king1"));
        try {
            purchase.mercenary(args.iterator());
            fail();
        } catch (InvalidMercenaryException e) {
            // Expected
        }
    }

    private Iterator iteratorWith(String... args) {
        return Arrays.asList(args).iterator();
    }
}