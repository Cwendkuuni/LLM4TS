package lotus.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    private Stack stack;

    @Before
    public void setUp() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        player1 = new Player(ip1, "Player1", new ArrayList<>());
        player2 = new Player(ip2, "Player2", new ArrayList<>());
        deck1 = new CardCollection();
        deck2 = new CardCollection();
        stack = new Stack();
    }

    @Test
    public void testGetPlayingPlayer() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        assertEquals(player1, Game.getPlayingPlayer());
        Game.playingPlayer = 2;
        assertEquals(player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        assertEquals(player2, Game.getNonPlayingPlayer());
        Game.playingPlayer = 1;
        assertEquals(player2, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        assertEquals(player2, Game.getOtherPlayer(player1));
        assertEquals(player1, Game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        verify(ip1, times(1)).init(player1);
        verify(ip2, times(1)).init(player2);
        assertEquals(player1, Game.getPlayingPlayer());
        assertEquals(player2, Game.getNonPlayingPlayer());
        assertEquals(deck1, player1.getDeck());
        assertEquals(deck2, player2.getDeck());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(false);

        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        Game.givePriorityToCurrentPlayer();
        verify(ip1, times(1)).print(eq("It's Player1's turn.\n"));

        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(true);

        Game.givePriorityToCurrentPlayer();
        verify(ip2, times(1)).print(eq("It's Player2's turn.\n"));

        Game.givePriorityToCurrentPlayer();
        Game.givePriorityToCurrentPlayer();
        assertEquals(0, stack.size());
    }

    @Test
    public void testResolveLast() {
        Card card1 = new Card("Card1");
        Card card2 = new Card("Card2");
        stack.push(card1);
        stack.push(card2);

        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(false);

        Game.givePriorityToCurrentPlayer();

        Game.stack.resolveLast();
        verify(card1).resolve();
        verifyNoMoreInteractions(card1);

        Game.stack.resolveLast();
        verify(card2).resolve();
        verifyNoMoreInteractions(card2);

        assertEquals(0, stack.size());
    }
}