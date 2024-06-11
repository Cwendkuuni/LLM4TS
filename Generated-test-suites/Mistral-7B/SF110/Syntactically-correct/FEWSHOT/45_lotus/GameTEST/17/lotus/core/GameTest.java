package lotus.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void setUp() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        player1 = new Player(ip1, "player1", Collections.emptyList());
        player2 = new Player(ip2, "player2", Collections.emptyList());
        stack = new Stack();
        game = new Game();
        game.init(ip1, ip2, "player1", "player2", new CardCollection(), new CardCollection());
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
        List<Card> deck1 = Collections.singletonList(mock(Card.class));
        List<Card> deck2 = Collections.singletonList(mock(Card.class));

        when(ip1.init(player1)).thenReturn(true);
        when(ip2.init(player2)).thenReturn(true);

        game.init(ip1, ip2, "player1", "player2", new CardCollection(deck1), new CardCollection(deck2));

        assertSame(ip1, game.player1.ui);
        assertSame(ip2, game.player2.ui);
        assertSame(player1, game.player1);
        assertSame(player2, game.player2);
        assertSame(UntapPhase.class, game.currentPhase.getClass());
        assertSame(deck1, player1.library);
        assertSame(deck2, player2.library);
        assertSame(player1, game.getPlayingPlayer());
        assertSame(player2, game.getNonPlayingPlayer());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(true);

        game.givePriorityToCurrentPlayer();

        assertFalse(player1.letPlayerSpeakWasCalled());
        assertFalse(player2.letPlayerSpeakWasCalled());
        assertNull(stack.peek());

        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(false);

        game.givePriorityToCurrentPlayer();

        assertFalse(player1.letPlayerSpeakWasCalled());
        assertFalse(player2.letPlayerSpeakWasCalled());
        assertNull(stack.peek());

        player1.addCardToStack(mock(Card.class));

        game.givePriorityToCurrentPlayer();

        assertFalse(player1.letPlayerSpeakWasCalled());
        assertFalse(player2.letPlayerSpeakWasCalled());
        assertSame(mock(Card.class), stack.peek());
        assertSame(player1, stack.getOwner());
    }
}