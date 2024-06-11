package lotus.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lotus.core.card.Card;
import lotus.core.card.CardCollection;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.stack.Stack;

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
        stack = new Stack();
        game = new Game();
        game.init(ip1, ip2, "Player 1", "Player 2", new CardCollection(), new CardCollection());
    }

    @Test
    public void testGetPlayingPlayer() {
        assertSame(player1, game.getPlayingPlayer());
        game.playingPlayer = 2;
        assertSame(player2, game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        assertSame(player2, game.getNonPlayingPlayer());
        game.playingPlayer = 1;
        assertSame(player1, game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        assertSame(player2, game.getOtherPlayer(player1));
        assertSame(player1, game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        List<Card> deck1 = Arrays.asList(new Card("Card1"), new Card("Card2"), new Card("Card3"));
        List<Card> deck2 = Arrays.asList(new Card("Card4"), new Card("Card5"), new Card("Card6"));
        when(ip1.init(player1)).thenReturn(true);
        when(ip2.init(player2)).thenReturn(true);

        game.init(ip1, ip2, "Player 1", "Player 2", new CardCollection(deck1), new CardCollection(deck2));

        assertTrue(game.currentPhase instanceof UntapPhase);
        assertSame(player1, game.player1);
        assertSame(player2, game.player2);
        assertSame(deck1, game.player1.library);
        assertSame(deck2, game.player2.library);
        assertEquals(3, game.player1.deck.size());
        assertEquals(3, game.player2.deck.size());
        assertNull(game.stack.peek());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(false);

        game.givePriorityToCurrentPlayer();

        assertTrue(game.player1.spoken);
        assertFalse(game.player2.spoken);
        assertNull(game.stack.peek());

        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(true);

        game.givePriorityToCurrentPlayer();

        assertFalse(game.player1.spoken);
        assertTrue(game.player2.spoken);

        Card card1 = new Card("Card1");
        Card card2 = new Card("Card2");
        stack.push(card1);
        stack.push(card2);

        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(false);

        game.givePriorityToCurrentPlayer();

        assertSame(card2, stack.peek());
        assertSame(card1, stack.second());
    }
}