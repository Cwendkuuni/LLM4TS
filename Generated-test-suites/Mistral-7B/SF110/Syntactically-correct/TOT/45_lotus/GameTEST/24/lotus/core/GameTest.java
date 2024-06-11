package lotus.core.test;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import lotus.core.*;
import lotus.core.stack.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;

public class GameTest {
    private UserInterface ip1, ip2;
    private Player player1, player2;
    private CardCollection deck1, deck2;

    @Before
    public void setUp() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        player1 = new Player(ip1, "Player1", new CardCollection());
        player2 = new Player(ip2, "Player2", new CardCollection());
        when(ip1.init(player1)).thenReturn(true);
        when(ip2.init(player2)).thenReturn(true);
    }

    @Test
    public void testGetPlayingPlayer() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        assertEquals(player1, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        assertEquals(player2, Game.getNonPlayingPlayer());
    }

    @Test
    public void testInit() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        verify(ip1, times(1)).init(player1);
        verify(ip2, times(1)).init(player2);
        assertEquals(player1, Game.getPlayingPlayer());
        assertEquals(player2, Game.getNonPlayingPlayer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullDeck() {
        Game.init(ip1, ip2, "Player1", "Player2", null, deck2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullName1() {
        Game.init(ip1, ip2, null, "Player2", deck1, deck2);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        doReturn(true).when(player1).letPlayerSpeak();
        doReturn(true).when(player2).letPlayerSpeak();

        Game.givePriorityToCurrentPlayer();
        verify(player1, times(1)).letPlayerSpeak();
        verify(player2, times(1)).letPlayerSpeak();
        verify(stack, times(1)).resolveLast();

        Game.playingPlayer = 2;
        doReturn(true).when(player2).letPlayerSpeak();
        doReturn(true).when(player1).letPlayerSpeak();

        Game.givePriorityToCurrentPlayer();
        verify(player2, times(1)).letPlayerSpeak();
        verify(player1, times(1)).letPlayerSpeak();
        verify(stack, times(2)).resolveLast();
    }

    // Add tests for other methods
}