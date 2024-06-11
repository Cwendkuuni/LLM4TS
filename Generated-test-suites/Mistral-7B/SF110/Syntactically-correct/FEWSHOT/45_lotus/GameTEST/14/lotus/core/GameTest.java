package lotus.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import lotus.core.interfaces.IUserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.Player;
import lotus.core.card.Card;
import lotus.core.stack.Stack;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        when(p1.getId()).thenReturn(1);
        when(p2.getId()).thenReturn(2);

        Game.init(mock(IUserInterface.class), mock(IUserInterface.class), "Player 1", "Player 2", new CardCollection(), new CardCollection());

        assertTrue(Game.getPlayingPlayer() == p1);
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        when(p1.getId()).thenReturn(1);
        when(p2.getId()).thenReturn(2);

        Game.init(mock(IUserInterface.class), mock(IUserInterface.class), "Player 1", "Player 2", new CardCollection(), new CardCollection());

        assertTrue(Game.getNonPlayingPlayer() == p2);
    }

    @Test
    public void testGetOtherPlayer() {
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        when(p1.getId()).thenReturn(1);
        when(p2.getId()).thenReturn(2);

        Game.init(mock(IUserInterface.class), mock(IUserInterface.class), "Player 1", "Player 2", new CardCollection(), new CardCollection());

        assertTrue(Game.getOtherPlayer(p1) == p2);
        assertTrue(Game.getOtherPlayer(p2) == p1);
    }

    @Test
    public void testInit() {
        IUserInterface ip1 = mock(IUserInterface.class);
        IUserInterface ip2 = mock(IUserInterface.class);
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = mock(CardCollection.class);
        CardCollection deck2 = mock(CardCollection.class);

        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        Player player1 = Game.player1;
        Player player2 = Game.player2;

        assertTrue(Game.combat == null);
        assertTrue(Game.currentPhase instanceof UntapPhase);
        assertTrue(Game.playingPlayer == 1);
        assertTrue(player1 != null);
        assertTrue(player2 != null);
        assertTrue(ip1.isInitialized());
        assertTrue(ip2.isInitialized());
        assertTrue(player1.getName().equals(name1));
        assertTrue(player2.getName().equals(name2));
        assertTrue(Game.player1 == player1);
        assertTrue(Game.player2 == player2);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        IUserInterface ip1 = mock(IUserInterface.class);
        IUserInterface ip2 = mock(IUserInterface.class);
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        Stack stack = new Stack();
        List<Card> deck1 = new ArrayList<>();
        List<Card> deck2 = new ArrayList<>();

        when(p1.getId()).thenReturn(1);
        when(p2.getId()).thenReturn(2);
        when(p1.letPlayerSpeak()).thenReturn(false);
        when(p2.letPlayerSpeak()).thenReturn(true);

        Game.init(ip1, ip2, "Player 1", "Player 2", deck1, deck2);

        Game.playingPlayer = 1;

        givePriorityToCurrentPlayer();

        assertFalse(p1.letPlayerSpeakCalled());
        assertFalse(p2.letPlayerSpeakCalled());
        assertFalse(stack.empty());

        Card card = mock(Card.class);
        when(stack.resolveLast()).thenReturn(card);

        givePriorityToCurrentPlayer();

        assertFalse(p1.letPlayerSpeakCalled());
        assertFalse(p2.letPlayerSpeakCalled());
        assertTrue(stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_BothPlayersPass() {
        IUserInterface ip1 = mock(IUserInterface.class);
        IUserInterface ip2 = mock(IUserInterface.class);
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        Stack stack = new Stack();

        when(p1.getId()).thenReturn(1);
        when(p2.getId()).thenReturn(2);
        when(p1.letPlayerSpeak()).thenReturn(false);
        when(p2.letPlayerSpeak()).thenReturn(false);

        Game.init(ip1, ip2, "Player 1", "Player 2", new CardCollection(), new CardCollection());

        Game.playingPlayer = 1;

        givePriorityToCurrentPlayer();
        givePriorityToCurrentPlayer();

        assertFalse(p1.letPlayerSpeakCalled());
        assertFalse(p2.letPlayerSpeakCalled());
        assertTrue(stack.empty());
    }

    @Test
    public void testGetPlayingPlayer_AfterInit() {
        IUserInterface ip1 = mock(IUserInterface.class);
        IUserInterface ip2 = mock(IUserInterface.class);
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);

        Game.init(ip1, ip2, "Player 1", "Player 2", new CardCollection(), new CardCollection());

        assertEquals(p1, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer_AfterInit() {
        IUserInterface ip1 = mock(IUserInterface.class);
        IUserInterface ip2 = mock(IUserInterface.class);
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);

        Game.init(ip1, ip2, "Player 1", "Player 2", new CardCollection(), new CardCollection());

        assertEquals(p2, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer_AfterInit() {
        IUserInterface ip1 = mock(IUserInterface.class);
        IUserInterface ip2 = mock(IUserInterface.class);
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);

        Game.init(ip1, ip2, "Player 1", "Player 2", new CardCollection(), new CardCollection());

        assertEquals(p2, Game.getOtherPlayer(p1));
        assertEquals(p1, Game.getOtherPlayer(p2));
    }

    @Test
    public void testInit_NullUserInterfaces() {
        IUserInterface ip1 = null;
        IUserInterface ip2 = null;
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = null;
        CardCollection deck2 = null;

        try {
            Game.init(ip1, ip2, name1, name2, deck1, deck2);
            fail("Initialization should have thrown an exception");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testInit_NullNames() {
        IUserInterface ip1 = mock(IUserInterface.class);
        IUserInterface ip2 = mock(IUserInterface.class);
        String name1 = null;
        String name2 = null;
        CardCollection deck1 = null;
        CardCollection deck2 = null;

        try {
            Game.init(ip1, ip2, name1, name2, deck1, deck2);
            fail("Initialization should have thrown an exception");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testInit_NullDecks() {
        IUserInterface ip1 = mock(IUserInterface.class);
        IUserInterface ip2 = mock(IUserInterface.class);
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = null;
        CardCollection deck2 = null;

        try {
            Game.init(ip1, ip2, name1, name2, deck1, deck2);
            fail("Initialization should have thrown an exception");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testInit_EmptyDecks() {
        IUserInterface ip1 = mock(IUserInterface.class);
        IUserInterface ip2 = mock(IUserInterface.class);
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();

        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        assertTrue(Game.player1 != null);
        assertTrue(Game.player2 != null);
        assertTrue(Game.playingPlayer == 1);
        assertTrue(Game.currentPhase instanceof UntapPhase);
        assertTrue(Game.stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_EmptyStack() {
        IUserInterface ip1 = mock(IUserInterface.class);
        IUserInterface ip2 = mock(IUserInterface.class);
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);

        when(p1.getId()).thenReturn(1);
        when(p2.getId()).thenReturn(2);
        when(p1.letPlayerSpeak()).thenReturn(false);
        when(p2.letPlayerSpeak()).thenReturn(false);

        Game.init(ip1, ip2, "Player 1", "Player 2", new CardCollection(), new CardCollection());

        Game.playingPlayer = 1;

        givePriorityToCurrentPlayer();

        assertFalse(p1.letPlayerSpeakCalled());
        assertFalse(p2.letPlayerSpeakCalled());
        assertTrue(stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_NonEmptyStackWithAPSpeaking() {
        IUserInterface ip1 = mock(IUserInterface.class);
        IUserInterface ip2 = mock(IUserInterface.class);
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        Stack stack = new Stack();
        Card card = mock(Card.class);

        when(p1.getId()).thenReturn(1);
        when(p2.getId()).thenReturn(2);
        when(p1.letPlayerSpeak()).thenReturn(false);
        when(p2.letPlayerSpeak()).thenReturn(true);
        when(stack.resolveLast()).thenReturn(card);

        Game.init(ip1, ip2, "Player 1", "Player 2", new CardCollection(), new CardCollection());

        Game.playingPlayer = 1;

        givePriorityToCurrentPlayer();

        assertFalse(p1.letPlayerSpeakCalled());
        assertFalse(p2.letPlayerSpeakCalled());
        assertFalse(stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_NonEmptyStackWithNAPSpeaking() {
        IUserInterface ip1 = mock(IUserInterface.class);
        IUserInterface ip2 = mock(IUserInterface.class);
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        Stack stack = new Stack();
        Card card = mock(Card.class);

        when(p1.getId()).thenReturn(1);
        when(p2.getId()).thenReturn(2);
        when(p1.letPlayerSpeak()).thenReturn(false);
        when(p2.letPlayerSpeak()).thenReturn(false);
        when(stack.resolveLast()).thenReturn(card);

        Game.init(ip1, ip2, "Player 1", "Player 2", new CardCollection(), new CardCollection());

        Game.playingPlayer = 1;

        givePriorityToCurrentPlayer();
        givePriorityToCurrentPlayer();

        assertFalse(p1.letPlayerSpeakCalled());
        assertFalse(p2.letPlayerSpeakCalled());
        assertTrue(stack.empty());
    }
}