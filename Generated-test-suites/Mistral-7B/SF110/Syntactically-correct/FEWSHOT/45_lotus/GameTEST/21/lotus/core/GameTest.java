package lotus.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.Player;
import lotus.core.card.Card;
import lotus.core.stack.Stack;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameTest {

    private UserInterface ip1Mock;
    private UserInterface ip2Mock;
    private Player player1;
    private Player player2;
    private Game game;
    private Stack stack;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ip1Mock = mock(UserInterface.class);
        ip2Mock = mock(UserInterface.class);
        player1 = new Player(ip1Mock, "Player1", new ArrayList<>());
        player2 = new Player(ip2Mock, "Player2", new ArrayList<>());
        stack = new Stack();
        game = new Game();
        game.init(ip1Mock, ip2Mock, "Player1Name", "Player2Name", new ArrayList<>(), new ArrayList<>());
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
        verify(ip1Mock, times(1)).init(player1);
        verify(ip2Mock, times(1)).init(player2);
        assertEquals(player1, game.getPlayingPlayer());
        assertEquals(player2, game.getNonPlayingPlayer());
        assertEquals(Collections.emptyList(), player1.getHand());
        assertEquals(Collections.emptyList(), player2.getHand());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(true);

        game.givePriorityToCurrentPlayer();
        verify(player2).letPlayerSpeak();

        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(false);

        game.givePriorityToCurrentPlayer();
        verify(player1).letPlayerSpeak();

        // Test with stack resolution
        Card card1 = new Card("Card1", CardType.ACTION);
        Card card2 = new Card("Card2", CardType.ACTION);
        stack.push(card1);
        stack.push(card2);

        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(false);

        game.givePriorityToCurrentPlayer();
        verify(card2).resolve();
        verify(card1).resolve();

        game.givePriorityToCurrentPlayer();
        verify(player1, times(2)).letPlayerSpeak();
        verify(player2, times(1)).letPlayerSpeak();
    }
}