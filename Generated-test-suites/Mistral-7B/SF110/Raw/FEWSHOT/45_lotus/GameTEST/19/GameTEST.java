package lotus.core;

import static org.junit.Assert.*;
import lotus.core.stack.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

    private UserInterface ip1;
    private UserInterface ip2;
    private Player player1;
    private Player player2;
    private CardCollection deck1;
    private CardCollection deck2;
    private Game game;

    @Before
    public void setUp() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        deck1 = new ArrayList<>();
        deck2 = new ArrayList<>();
        player1 = new Player(ip1, "Player 1", deck1);
        player2 = new Player(ip2, "Player 2", deck2);
        game = new Game();
    }

    @Test
    public void testGetPlayingPlayer() {
        game.init(ip1, ip2, "Player 1", "Player 2", deck1, deck2);
        assertEquals(player1, game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        game.init(ip1, ip2, "Player 1", "Player 2", deck1, deck2);
        assertEquals(player2, game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        game.init(ip1, ip2, "Player 1", "Player 2", deck1, deck2);
        assertEquals(player2, game.getOtherPlayer(player1));
        assertEquals(player1, game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        game.init(ip1, ip2, "Player 1", "Player 2", deck1, deck2);
        assertEquals(player1, game.getPlayingPlayer());
        assertEquals(player2, game.getNonPlayingPlayer());
        assertEquals(0, deck1.size());
        assertEquals(0, deck2.size());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        game.init(ip1, ip2, "Player 1", "Player 2", deck1, deck2);
        game.givePriorityToCurrentPlayer();
        verify(ip1, times(1)).letPlayerSpeak();
        verifyNoMoreInteractions(ip2);
        assertFalse(game.stack.empty());
        game.givePriorityToCurrentPlayer();
        verify(ip1, times(1)).letPlayerSpeak();
        verifyNoMoreInteractions(ip2);
        assertTrue(game.stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayerWithStack() {
        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);
        game.stack.push(card1);
        game.stack.push(card2);
        game.init(ip1, ip2, "Player 1", "Player 2", deck1, deck2);
        game.givePriorityToCurrentPlayer();
        verify(ip1, times(1)).letPlayerSpeak();
        verifyNoMoreInteractions(ip2);
        assertFalse(game.stack.empty());
        game.givePriorityToCurrentPlayer();
        verify(ip1, times(1)).letPlayerSpeak();
        verifyNoMoreInteractions(ip2);
        assertEquals(card2, game.stack.peek());
    }
}