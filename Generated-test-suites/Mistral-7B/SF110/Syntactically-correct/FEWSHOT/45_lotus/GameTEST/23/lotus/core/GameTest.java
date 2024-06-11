package lotus.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void setup() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        player1 = new Player(ip1, "Player1", deck1 = new CardCollection());
        player2 = new Player(ip2, "Player2", deck2 = new CardCollection());
        game = new Game();
    }

    @Test
    public void testGetPlayingPlayer() {
        game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        assertEquals(player1, game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        assertEquals(player2, game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        assertEquals(player2, game.getOtherPlayer(player1));
        assertEquals(player1, game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        assertEquals(player1, game.getPlayingPlayer());
        assertEquals(player2, game.getNonPlayingPlayer());
        assertFalse(game.stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(true);
        when(game.stack.empty()).thenReturn(false);
        game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        game.givePriorityToCurrentPlayer();
        verify(player2).letPlayerSpeak();
        verify(player1).letPlayerSpeak();
        verify(game.stack).resolveLast();
        verify(game.stack).resolveLast();
    }

    @Test
    public void testGivePriorityToCurrentPlayer_EmptyStack() {
        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(false);
        when(game.stack.empty()).thenReturn(true);
        game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        game.givePriorityToCurrentPlayer();
        verifyNoMoreInteractions();
    }

    @Test
    public void testGetPlayingPlayer_NullInput() {
        assertNull(Game.getPlayingPlayer());
    }
}