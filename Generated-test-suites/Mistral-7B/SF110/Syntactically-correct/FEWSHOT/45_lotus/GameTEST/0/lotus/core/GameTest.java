package lotus.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class GameTest {

    private Player player1;
    private Player player2;
    private UserInterface ip1;
    private UserInterface ip2;
    private Combat combat;
    private Phase currentPhase;
    private Stack stack;
    private CardCollection deck1;
    private CardCollection deck2;

    @Before
    public void setup() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        player1 = new Player(ip1, "Player1", new CardCollection());
        player2 = new Player(ip2, "Player2", new CardCollection());
        combat = mock(Combat.class);
        currentPhase = mock(Phase.class);
        stack = new Stack();
        deck1 = new CardCollection();
        deck2 = new CardCollection();

        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(true);
    }

    @Test
    public void testGetPlayingPlayer() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);

        assertSame(player1, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);

        assertSame(player2, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);

        assertSame(player2, Game.getOtherPlayer(player1));
        assertSame(player1, Game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);

        assertSame(player1, Game.player1);
        assertSame(player2, Game.player2);
        assertSame(ip1, Game.player1.ui);
        assertSame(ip2, Game.player2.ui);
        assertSame(currentPhase, Game.currentPhase);
        assertSame(deck1, Game.player1.deck);
        assertSame(deck2, Game.player2.deck);
        assertEquals(1, Game.playingPlayer);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);

        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertFalse(player2.letPlayerSpeakCalled);

        when(player1.letPlayerSpeak()).thenReturn(true);
        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertTrue(player2.letPlayerSpeakCalled);

        when(player2.letPlayerSpeak()).thenReturn(true);
        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertTrue(player2.letPlayerSpeakCalled);

        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(false);

        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertFalse(player2.letPlayerSpeakCalled);

        player1.letPlayerSpeakCalled = true;
        player2.letPlayerSpeakCalled = true;

        Game.givePriorityToCurrentPlayer();
        assertTrue(player1.letPlayerSpeakCalled);
        assertFalse(player2.letPlayerSpeakCalled);

        Game.stack.push(mock(Card.class));
        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertFalse(player2.letPlayerSpeakCalled);

        Game.stack.resolveLast();
        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertFalse(player2.letPlayerSpeakCalled);

        player1.letPlayerSpeakCalled = false;
        player2.letPlayerSpeakCalled = false;
        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertFalse(player2.letPlayerSpeakCalled);
    }

    @Test
    public void testEmptyStack() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);

        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertFalse(player2.letPlayerSpeakCalled);
        assertFalse(combat.hasCombatStarted);
    }

    @Test
    public void testStackWithCards() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);

        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);
        Card card3 = mock(Card.class);

        when(card1.resolve()).thenReturn(card2);
        when(card2.resolve()).thenReturn(card3);

        Game.stack.push(card1);
        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertFalse(player2.letPlayerSpeakCalled);

        Game.stack.resolveLast();
        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertFalse(player2.letPlayerSpeakCalled);

        Game.stack.push(card2);
        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertFalse(player2.letPlayerSpeakCalled);

        Game.stack.resolveLast();
        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertFalse(player2.letPlayerSpeakCalled);

        Game.stack.push(card3);
        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertFalse(player2.letPlayerSpeakCalled);

        Game.stack.resolveLast();
        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertFalse(player2.letPlayerSpeakCalled);

        Game.stack.pop();
        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertFalse(player2.letPlayerSpeakCalled);

        Game.stack.pop();
        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertFalse(player2.letPlayerSpeakCalled);

        Game.stack.push(card1);
        Game.givePriorityToCurrentPlayer();
        assertFalse(player1.letPlayerSpeakCalled);
        assertFalse(player2.letPlayerSpeakCalled);
    }

    @Test
    public void testInitWithEmptyDecks() {
        Game.init(mock(UserInterface.class), mock(UserInterface.class), "Player1", "Player2", new CardCollection(), new CardCollection());

        assertNull(Game.player1);
        assertNull(Game.player2);
    }
}