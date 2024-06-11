import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import lotus.core.*;
import static org.junit.Assert.*;

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
        ip1 = Mockito.mock(UserInterface.class);
        ip2 = Mockito.mock(UserInterface.class);
        player1 = new Player(ip1, "Player1", deck1 = new CardCollection());
        player2 = new Player(ip2, "Player2", deck2 = new CardCollection());
        game = new Game();
        game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        Mockito.when(ip1.init(player1)).thenReturn(true);
        Mockito.when(ip2.init(player2)).thenReturn(true);
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
    public void testInitGameWithSameNamePlayers() {
        String name = "Player1";
        Game game = new Game();
        assertThrows(IllegalArgumentException.class, () -> game.init(ip1, ip2, name, name, deck1, deck2));
    }

    @Test
    public void testInitGameWithNullDeck1() {
        Game game = new Game();
        assertThrows(NullPointerException.class, () -> game.init(ip1, ip2, "Player1", "Player2", null, deck2));
    }

    @Test
    public void testInitGameWithNullDeck2() {
        Game game = new Game();
        assertThrows(NullPointerException.class, () -> game.init(ip1, ip2, "Player1", "Player2", deck1, null));
    }

    @Test
    public void testGivePriorityToCurrentPlayerEmptyStack() {
        game.givePriorityToCurrentPlayer();
        assertTrue(game.stack.isEmpty());
    }

    @Test
    public void testGivePriorityToCurrentPlayerNonEmptyStack() {
        game.stack.push(Mockito.mock(Card.class));
        game.givePriorityToCurrentPlayer();
        assertTrue(game.stack.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testGivePriorityToCurrentPlayerNullPlayingPlayer() {
        game.playingPlayer = null;
        game.givePriorityToCurrentPlayer();
    }

    @Test(expected = IllegalStateException.class)
    public void testGivePriorityToCurrentPlayerNullNonPlayingPlayer() {
        game.nonPlayingPlayer = null;
        game.givePriorityToCurrentPlayer();
    }

    @Test(expected = NullPointerException.class)
    public void testInitWithNullUserInterface() {
        game.init(null, ip2, "Player1", "Player2", deck1, deck2);
    }

    @Test(expected = NullPointerException.class)
    public void testInitWithNullPlayer() {
        game.init(ip1, null, "Player1", "Player2", deck1, deck2);
    }

    @Test(expected = NullPointerException.class)
    public void testInitWithNullCardCollection() {
        game.init(ip1, ip2, "Player1", "Player2", null, deck2);
    }

    @Test
    public void testInitGameWithNullPlayer1() {
        game.init(ip1, ip2, null, "Player2", deck1, deck2);
        assertNull(game.player1);
    }

    @Test
    public void testInitGameWithNullPlayer2() {
        game.init(ip1, ip2, "Player1", null, deck1, deck2);
        assertNull(game.player2);
    }

    @Test
    public void testInitGameWithNullStack() {
        Game game = new Game();
        assertNotNull(game.stack);
    }
}