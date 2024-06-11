package lotus.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import lotus.core.card.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import org.junit.Before;
import org.junit.Test;

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
        player1 = new Player(ip1, "Player1", new ArrayList<>());
        player2 = new Player(ip2, "Player2", new ArrayList<>());
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
        verify(ip1).init(player1);
        verify(ip2).init(player2);
        assertEquals(player1, game.getPlayingPlayer());
        assertEquals(player2, game.getNonPlayingPlayer());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Card card1 = new Card("Card1", player1);
        Card card2 = new Card("Card2", player2);
        deck1.add(card1);
        deck2.add(card2);
        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(false);
        game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        game.givePriorityToCurrentPlayer();
        verify(player1).letPlayerSpeak();
        verify(player2).letPlayerSpeak();
        verify(card1).resolve();
        verify(card2).resolve();
    }

    @Test
    public void testEmptyStack() {
        game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        game.givePriorityToCurrentPlayer();
        verifyNoMoreInteractions();
    }

    @Test
    public void testNonEmptyStack() {
        Card card1 = new Card("Card1", player1);
        Card card2 = new Card("Card2", player2);
        deck1.add(card1);
        deck2.add(card2);
        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(false);
        game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        game.stack.push(card1);
        game.givePriorityToCurrentPlayer();
        verify(player1).letPlayerSpeak();
        verify(player2).letPlayerSpeak();
        verify(card1).resolve();
        verify(card2).resolve();
        verify(card1).isResolved();
        verify(card2).isResolved();
    }
}