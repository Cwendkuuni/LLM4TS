package lotus.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class GameTest {

    private UserInterface ip1;
    private UserInterface ip2;
    private Player player1;
    private Player player2;
    private Game game;
    private Stack stack;

    @Before
    public void setup() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        player1 = new Player(ip1, "Player 1", new CardCollection());
        player2 = new Player(ip2, "Player 2", new CardCollection());
        game = new Game();
        game.stack = new Stack();
    }

    @Test
    public void testGetPlayingPlayer() {
        game.player1 = player1;
        game.player2 = player2;
        assertSame(player1, game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        game.player1 = player1;
        game.player2 = player2;
        assertSame(player2, game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        game.player1 = player1;
        game.player2 = player2;
        assertSame(player2, game.getOtherPlayer(player1));
        assertSame(player1, game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        List<Card> deck1 = new ArrayList<>();
        List<Card> deck2 = new ArrayList<>();
        when(ip1.init(player1)).thenReturn(true);
        when(ip2.init(player2)).thenReturn(true);

        game.init(ip1, ip2, "Name1", "Name2", deck1, deck2);

        assertTrue(game.player1.isAlive());
        assertTrue(game.player2.isAlive());
        assertSame(game.player1, game.getPlayingPlayer());
        assertSame(game.player2, game.getNonPlayingPlayer());
        for (Card card : deck1) {
            assertSame(game.player1, card.owner);
            assertSame(game.player1.library, card.zone);
        }
        for (Card card : deck2) {
            assertSame(game.player2, card.owner);
            assertSame(game.player2.library, card.zone);
        }
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        player1.canSpeak = () -> true;
        player2.canSpeak = () -> true;
        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(false);

        game.init(ip1, ip2, "Name1", "Name2", new CardCollection(), new CardCollection());
        game.givePriorityToCurrentPlayer();

        assertFalse(player1.letPlayerSpeakWasCalled);
        assertFalse(player2.letPlayerSpeakWasCalled);
        assertTrue(game.stack.empty());

        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(false);

        game.givePriorityToCurrentPlayer();

        assertFalse(player1.letPlayerSpeakWasCalled);
        assertFalse(player2.letPlayerSpeakWasCalled);
        assertNull(game.stack.top);

        when(player2.letPlayerSpeak()).thenReturn(true);
        when(player1.letPlayerSpeak()).thenReturn(false);

        game.givePriorityToCurrentPlayer();

        assertTrue(player1.letPlayerSpeakWasCalled);
        assertFalse(player2.letPlayerSpeakWasCalled);
        assertNull(game.stack.top);

        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(true);

        game.givePriorityToCurrentPlayer();

        assertFalse(player1.letPlayerSpeakWasCalled);
        assertTrue(player2.letPlayerSpeakWasCalled);
        assertNull(game.stack.top);

        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(true);
        Card card = new Card("Card1", CardType.ATTACK);
        when(player1.playCard(card)).thenReturn(true);
        when(player2.playCard(card)).thenReturn(false);

        game.init(ip1, ip2, "Name1", "Name2", new CardCollection(), new CardCollection());
        game.givePriorityToCurrentPlayer();

        assertTrue(player1.letPlayerSpeakWasCalled);
        assertFalse(player2.letPlayerSpeakWasCalled);
        assertNull(game.stack.top);

        player1.letPlayerSpeakWasCalled = false;
        player2.letPlayerSpeakWasCalled = false;

        game.givePriorityToCurrentPlayer();

        assertFalse(player1.letPlayerSpeakWasCalled);
        assertFalse(player2.letPlayerSpeakWasCalled);
        assertNull(game.stack.top);

        when(player2.letPlayerSpeak()).thenReturn(true);

        game.givePriorityToCurrentPlayer();

        assertTrue(player1.letPlayerSpeakWasCalled);
        assertFalse(player2.letPlayerSpeakWasCalled);
        assertNull(game.stack.top);

        when(player2.letPlayerSpeak()).thenReturn(false);
        when(player1.playCard(card)).thenReturn(false);
        when(player2.playCard(card)).thenReturn(true);

        game.givePriorityToCurrentPlayer();

        assertTrue(player1.letPlayerSpeakWasCalled);
        assertFalse(player2.letPlayerSpeakWasCalled);
        assertNull(game.stack.top);

        when(player2.letPlayerSpeak()).thenReturn(true);
        when(player1.playCard(card)).thenReturn(true);

        game.givePriorityToCurrentPlayer();

        assertFalse(player1.letPlayerSpeakWasCalled);
        assertTrue(player2.letPlayerSpeakWasCalled);
        assertSame(card, game.stack.top);
    }

    @Test(expected = IllegalStateException.class)
    public void testGivePriorityToCurrentPlayerWithEmptyStackAndBothPlayersSpeakFalse() {
        player1.canSpeak = () -> false;
        player2.canSpeak = () -> false;
        game.init(ip1, ip2, "Name1", "Name2", new CardCollection(), new CardCollection());
        game.givePriorityToCurrentPlayer();

        assertFalse(player1.letPlayerSpeakWasCalled);
        assertFalse(player2.letPlayerSpeakWasCalled);
        assertTrue(game.stack.empty());

        game.givePriorityToCurrentPlayer();

        assertFalse(player1.letPlayerSpeakWasCalled);
        assertFalse(player2.letPlayerSpeakWasCalled);
        assertTrue(game.stack.empty());

        throw new IllegalStateException("Expected exception to be thrown");
    }
}