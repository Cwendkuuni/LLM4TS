package lotus.core;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class GameTest {

    private static UserInterface ip1, ip2;
    private static Player player1, player2;
    private static CardCollection deck1, deck2;
    private static Game game;

    @Before
    public void setup() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        player1 = new Player(ip1, "Player1", null);
        player2 = new Player(ip2, "Player2", null);
        deck1 = new CardCollection();
        deck2 = new CardCollection();
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
        verify(ip1, times(1)).init(player1);
        verify(ip2, times(1)).init(player2);
        assertEquals(player1, game.getPlayingPlayer());
        assertEquals(player2, game.getNonPlayingPlayer());
        assertEquals(deck1, player1.deck);
        assertEquals(deck2, player2.deck);
        assertEquals(new UntapPhase(), game.currentPhase);
        assertEquals(1, game.playingPlayer);
        assertTrue(game.stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(false);
        game.givePriorityToCurrentPlayer();
        verify(ip1, times(1)).showMessage("It is Player1's turn.");
        verify(ip2, times(1)).showMessage("Player2, please wait.");
        assertTrue(game.stack.empty());

        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(true);
        game.givePriorityToCurrentPlayer();
        verify(ip2, times(1)).showMessage("It is Player2's turn.");
        verify(ip1, times(1)).showMessage("Player1, please wait.");
        assertTrue(game.stack.empty());

        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(false);
        Card card = new Card("TestCard");
        game.stack.push(card);
        game.givePriorityToCurrentPlayer();
        verify(ip1, times(1)).showMessage("It is Player1's turn.");
        verify(ip2, times(1)).showMessage("Player2, please wait.");
        assertEquals(card, game.stack.peek());

        game.stack.pop();
        game.givePriorityToCurrentPlayer();
        verify(ip1, times(1)).showMessage("It is Player1's turn.");
        verify(ip2, times(1)).showMessage("Player2, please wait.");
        assertTrue(game.stack.empty());
    }
}