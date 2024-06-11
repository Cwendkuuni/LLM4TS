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
        player1 = new Player(ip1, "Player1", new CardCollection());
        player2 = new Player(ip2, "Player2", new CardCollection());
        deck1 = new CardCollection();
        deck2 = new CardCollection();
        game = new Game();
        game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
    }

    @Test
    public void testGetPlayingPlayer() {
        assertEquals(player1, game.getPlayingPlayer());
        game.playingPlayer = 2;
        assertEquals(player2, game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        assertEquals(player2, game.getNonPlayingPlayer());
        game.playingPlayer = 1;
        assertEquals(player2, game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        assertEquals(player2, game.getOtherPlayer(player1));
        assertEquals(player1, game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        verify(ip1, times(1)).init(player1);
        verify(ip2, times(1)).init(player2);
        assertEquals(player1, game.getPlayingPlayer());
        assertEquals(player2, game.getNonPlayingPlayer());
        assertEquals(new UntapPhase(), game.currentPhase);
        assertEquals(1, game.playingPlayer);
        for (Card card : deck1) {
            assertEquals(player1, card.owner);
            assertEquals(player1.library, card.zone);
        }
        for (Card card : deck2) {
            assertEquals(player2, card.owner);
            assertEquals(player2.library, card.zone);
        }
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(true);
        game.givePriorityToCurrentPlayer();
        verify(player2, times(1)).letPlayerSpeak();
        verify(player1, times(1)).letPlayerSpeak();
        verify(game.stack, times(1)).resolveLast();
        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(false);
        game.givePriorityToCurrentPlayer();
        verify(player1, times(2)).letPlayerSpeak();
        verify(player2, times(1)).letPlayerSpeak();
        verify(game.stack, times(2)).resolveLast();
    }

    @Test(expected = AssertionError.class)
    public void testGivePriorityToCurrentPlayerWithEmptyStack() {
        game.givePriorityToCurrentPlayer();
    }
}