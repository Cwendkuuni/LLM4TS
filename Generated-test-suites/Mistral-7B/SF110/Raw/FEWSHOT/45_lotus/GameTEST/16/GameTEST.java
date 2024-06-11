package lotus.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.Player;
import lotus.core.stack.Stack;
import lotus.core.card.Card;
import java.util.ArrayList;
import java.util.List;

public class GameTest {

    private UserInterface ip1;
    private UserInterface ip2;
    private Player player1;
    private Player player2;
    private Game game;

    @Before
    public void setup() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        player1 = new Player(ip1, "Player 1", new ArrayList<>());
        player2 = new Player(ip2, "Player 2", new ArrayList<>());
        game = new Game();
        game.init(ip1, ip2, "Player 1", "Player 2", new ArrayList<>(), new ArrayList<>());
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
        assertEquals(player1, game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        assertEquals(player2, game.getOtherPlayer(player1));
        assertEquals(player1, game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        verify(ip1).init(player1);
        verify(ip2).init(player2);
        assertEquals(player1, game.getPlayingPlayer());
        assertEquals(player2, game.getNonPlayingPlayer());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        doNothing().when(ip1).letPlayerSpeak();
        doNothing().when(ip2).letPlayerSpeak();
        game.givePriorityToCurrentPlayer();
        verify(ip1).letPlayerSpeak();
        verify(ip2).letPlayerSpeak();

        doThrow(RuntimeException.class).when(ip1).letPlayerSpeak();
        game.givePriorityToCurrentPlayer();
        verify(ip2).letPlayerSpeak();

        doThrow(RuntimeException.class).when(ip2).letPlayerSpeak();
        game.givePriorityToCurrentPlayer();
        verify(ip1).letPlayerSpeak();

        doThrow(RuntimeException.class).when(ip1).letPlayerSpeak();
        doThrow(RuntimeException.class).when(ip2).letPlayerSpeak();
        game.givePriorityToCurrentPlayer();
        // Resolve last action on stack
        Card card1 = new Card("Card 1");
        Card card2 = new Card("Card 2");
        game.stack.push(card1);
        game.stack.push(card2);
        game.givePriorityToCurrentPlayer();
        verify(card1).resolve();
        verify(card2).resolve();
    }
}