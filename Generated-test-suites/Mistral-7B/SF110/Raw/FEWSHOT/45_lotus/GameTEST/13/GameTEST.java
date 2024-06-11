package lotus.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

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
        deck1 = new CardCollection();
        deck2 = new CardCollection();
        player1 = new Player(ip1, "Player1", deck1);
        player2 = new Player(ip2, "Player2", deck2);
        game = new Game();
        game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
    }

    @Test
    public void testGetPlayingPlayer() {
        Player playingPlayer = game.getPlayingPlayer();
        assertEquals(player1, playingPlayer);

        game.playingPlayer = 2;
        playingPlayer = game.getPlayingPlayer();
        assertEquals(player2, playingPlayer);
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Player nonPlayingPlayer = game.getNonPlayingPlayer();
        assertEquals(player2, nonPlayingPlayer);

        game.playingPlayer = 1;
        nonPlayingPlayer = game.getNonPlayingPlayer();
        assertEquals(player2, nonPlayingPlayer);
    }

    @Test
    public void testGetOtherPlayer() {
        Player otherPlayer = game.getOtherPlayer(player1);
        assertEquals(player2, otherPlayer);

        otherPlayer = game.getOtherPlayer(player2);
        assertEquals(player1, otherPlayer);
    }

    @Test
    public void testInit() {
        assertEquals(player1, game.getPlayingPlayer());
        assertEquals(player2, game.getNonPlayingPlayer());

        verify(ip1, times(1)).init(player1);
        verify(ip2, times(1)).init(player2);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        doReturn(false).when(player1).letPlayerSpeak();
        doReturn(false).when(player2).letPlayerSpeak();

        game.givePriorityToCurrentPlayer();
        assertTrue(game.stack.empty());

        doReturn(true).when(player1).letPlayerSpeak();
        doReturn(false).when(player2).letPlayerSpeak();

        game.givePriorityToCurrentPlayer();
        assertFalse(game.stack.empty());
        verify(stack, times(1)).resolveLast();

        doReturn(true).when(player2).letPlayerSpeak();
        doReturn(false).when(player1).letPlayerSpeak();

        game.givePriorityToCurrentPlayer();
        assertFalse(game.stack.empty());
        verify(stack, times(1)).resolveLast();
    }
}