package lotus.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameTest {

    private Player player1;
    private Player player2;
    private UserInterface ip1;
    private UserInterface ip2;
    private Game game;
    private Stack stack;

    @Before
    public void setUp() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        player1 = new Player(ip1, "Player 1");
        player2 = new Player(ip2, "Player 2");
        stack = new Stack();
        game = new Game();
        game.init(ip1, ip2, "Player 1", "Player 2", new CardCollection(), new CardCollection());
    }

    @Test
    public void testGetPlayingPlayer() {
        assertSame(player1, game.getPlayingPlayer());
        game.setPlayingPlayer(2);
        assertSame(player2, game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        assertSame(player2, game.getNonPlayingPlayer());
        game.setPlayingPlayer(1);
        assertSame(player2, game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        assertSame(player2, game.getOtherPlayer(player1));
        assertSame(player1, game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        List<Card> deck1 = new ArrayList<>();
        List<Card> deck2 = new ArrayList<>();
        deck1.add(mock(Card.class));
        deck2.add(mock(Card.class));
        when(ip1.init(player1)).thenReturn(true);
        when(ip2.init(player2)).thenReturn(true);

        game.init(ip1, ip2, "Player 1", "Player 2", new CardCollection(deck1), new CardCollection(deck2));

        assertTrue(game.player1 instanceof Player);
        assertTrue(game.player2 instanceof Player);
        assertSame(ip1, game.player1.ui);
        assertSame(ip2, game.player2.ui);
        assertSame(player1, game.getPlayingPlayer());
        assertSame(player2, game.getNonPlayingPlayer());
        assertEquals(1, game.playingPlayer);
        assertSame(UntapPhase.class, game.currentPhase.getClass());
        assertEquals(Collections.singletonList(deck1.get(0)), game.player1.library.getCards());
        assertEquals(Collections.singletonList(deck2.get(0)), game.player2.library.getCards());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        List<Card> deck1 = new ArrayList<>();
        List<Card> deck2 = new ArrayList<>();
        deck1.add(mock(Card.class));
        deck2.add(mock(Card.class));
        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(true);

        game.init(ip1, ip2, "Player 1", "Player 2", new CardCollection(deck1), new CardCollection(deck2));

        game.givePriorityToCurrentPlayer();

        assertFalse(game.getPlayingPlayer().letPlayerSpeak());
        assertFalse(game.getNonPlayingPlayer().letPlayerSpeak());
        assertFalse(game.stack.empty());

        game.givePriorityToCurrentPlayer();

        assertFalse(game.getPlayingPlayer().letPlayerSpeak());
        assertTrue(game.getNonPlayingPlayer().letPlayerSpeak());
        assertFalse(game.stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_EmptyStack() {
        List<Card> deck1 = new ArrayList<>();
        List<Card> deck2 = new ArrayList<>();
        deck1.add(mock(Card.class));
        deck2.add(mock(Card.class));
        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(false);

        game.init(ip1, ip2, "Player 1", "Player 2", new CardCollection(deck1), new CardCollection(deck2));

        game.givePriorityToCurrentPlayer();

        assertTrue(game.getPlayingPlayer().letPlayerSpeak());
        assertFalse(game.getNonPlayingPlayer().letPlayerSpeak());
        assertFalse(game.stack.empty());

        game.stack.resolveLast();
        game.givePriorityToCurrentPlayer();

        assertTrue(game.getPlayingPlayer().letPlayerSpeak());
        assertFalse(game.getNonPlayingPlayer().letPlayerSpeak());
        assertTrue(game.stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_BothSpeak() {
        List<Card> deck1 = new ArrayList<>();
        List<Card> deck2 = new ArrayList<>();
        deck1.add(mock(Card.class));
        deck2.add(mock(Card.class));
        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(true);

        game.init(ip1, ip2, "Player 1", "Player 2", new CardCollection(deck1), new CardCollection(deck2));

        game.givePriorityToCurrentPlayer();

        assertTrue(game.getPlayingPlayer().letPlayerSpeak());
        assertFalse(game.getNonPlayingPlayer().letPlayerSpeak());
        assertFalse(game.stack.empty());

        game.stack.resolveLast();
        game.givePriorityToCurrentPlayer();

        assertTrue(game.getPlayingPlayer().letPlayerSpeak());
        assertTrue(game.getNonPlayingPlayer().letPlayerSpeak());
        assertFalse(game.stack.empty());

        game.stack.resolveLast();
        game.givePriorityToCurrentPlayer();

        assertTrue(game.getPlayingPlayer().letPlayerSpeak());
        assertFalse(game.getNonPlayingPlayer().letPlayerSpeak());
        assertTrue(game.stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_NullPlayer1() {
        List<Card> deck1 = new ArrayList<>();
        List<Card> deck2 = new ArrayList<>();
        deck1.add(mock(Card.class));
        deck2.add(mock(Card.class));
        when(player1 = (Player) null).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(true);

        game.init(ip1, ip2, null, "Player 2", new CardCollection(deck1), new CardCollection(deck2));

        game.givePriorityToCurrentPlayer();

        assertNull(game.getPlayingPlayer());
        assertTrue(game.getNonPlayingPlayer().letPlayerSpeak());
        assertFalse(game.stack.empty());

        game.givePriorityToCurrentPlayer();

        assertNull(game.getPlayingPlayer());
        assertTrue(game.getNonPlayingPlayer().letPlayerSpeak());
        assertTrue(game.stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_NullPlayer2() {
        List<Card> deck1 = new ArrayList<>();
        List<Card> deck2 = new ArrayList<>();
        deck1.add(mock(Card.class));
        deck2.add(mock(Card.class));
        when(player2 = (Player) null).thenReturn(true);
        when(player1.letPlayerSpeak()).thenReturn(true);

        game.init(ip1, ip2, "Player 1", null, new CardCollection(deck1), new CardCollection(deck2));

        game.givePriorityToCurrentPlayer();

        assertSame(player1, game.getPlayingPlayer());
        assertNull(game.getNonPlayingPlayer());
        assertFalse(game.stack.empty());

        game.givePriorityToCurrentPlayer();

        assertSame(player1, game.getPlayingPlayer());
        assertNull(game.getNonPlayingPlayer());
        assertTrue(game.stack.empty());
    }
}