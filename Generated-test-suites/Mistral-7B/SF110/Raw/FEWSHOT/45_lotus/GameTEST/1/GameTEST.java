package lotus.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.stack.Stack;
import lotus.core.card.Card;
import lotus.core.card.CardCollection;
import lotus.core.player.Player;

public class GameTest {

    private UserInterface ip1;
    private UserInterface ip2;
    private Player player1;
    private Player player2;
    private Card card1;
    private Card card2;
    private CardCollection deck1;
    private CardCollection deck2;
    private Stack stack;

    @Before
    public void setup() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        player1 = new Player(ip1, "Player1", new CardCollection());
        player2 = new Player(ip2, "Player2", new CardCollection());
        card1 = mock(Card.class);
        card2 = mock(Card.class);
        deck1 = new CardCollection();
        deck2 = new CardCollection();
        stack = new Stack();

        when(ip1.init(player1)).thenReturn(true);
        when(ip2.init(player2)).thenReturn(true);
        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(false);

        deck1.add(card1);
        deck2.add(card2);
    }

    @Test
    public void testGetPlayingPlayer() {
        assertSame(player1, Game.getPlayingPlayer());

        Game.playingPlayer = 2;
        assertSame(player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        assertSame(player2, Game.getNonPlayingPlayer());

        Game.playingPlayer = 1;
        assertSame(player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        assertSame(player2, Game.getOtherPlayer(player1));
        assertSame(player1, Game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);

        assertTrue(Game.player1 instanceof Player);
        assertTrue(Game.player2 instanceof Player);
        assertSame(ip1, Game.player1.ui);
        assertSame(ip2, Game.player2.ui);
        assertSame(player1, Game.getPlayingPlayer());
        assertSame(player2, Game.getNonPlayingPlayer());
        assertEquals(deck1, Game.player1.library);
        assertEquals(deck2, Game.player2.library);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);

        Game.givePriorityToCurrentPlayer();

        assertFalse(getPlayingPlayer().speakCount > 0);
        assertFalse(getNonPlayingPlayer().speakCount > 0);

        Game.stack.push(card1);
        Game.givePriorityToCurrentPlayer();

        assertTrue(getPlayingPlayer().speakCount > 0);
        assertFalse(getNonPlayingPlayer().speakCount > 0);

        Game.givePriorityToCurrentPlayer();

        assertFalse(getPlayingPlayer().speakCount > 1);
        assertFalse(getNonPlayingPlayer().speakCount > 0);

        Game.stack.push(card2);
        Game.givePriorityToCurrentPlayer();

        assertTrue(getPlayingPlayer().speakCount > 1);
        assertFalse(getNonPlayingPlayer().speakCount > 0);

        Game.givePriorityToCurrentPlayer();

        assertFalse(getPlayingPlayer().speakCount > 2);
        assertTrue(getNonPlayingPlayer().speakCount > 0);

        Game.stack.resolveLast();
        Game.givePriorityToCurrentPlayer();

        assertFalse(getPlayingPlayer().speakCount > 2);
        assertFalse(getNonPlayingPlayer().speakCount > 0);
    }
}