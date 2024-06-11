import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import lotus.core.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {
    
    private Game game;
    private UserInterface ip1;
    private UserInterface ip2;
    private Player player1;
    private Player player2;
    private CardCollection deck1;
    private CardCollection deck2;

    @Before
    public void setUp() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        deck1 = new CardCollection();
        deck2 = new CardCollection();
        player1 = new Player(ip1, "name1", deck1);
        player2 = new Player(ip2, "name2", deck2);
        game = new Game();
        game.init(ip1, ip2, "name1", "name2", deck1, deck2);
    }

    @Test
    public void testGetPlayingPlayer() {
        assertSame(player1, game.getPlayingPlayer());
        game.playingPlayer = 2;
        assertSame(player2, game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        assertSame(player2, game.getNonPlayingPlayer());
        game.playingPlayer = 1;
        assertSame(player1, game.getNonPlayingPlayer());
    }

    @Test
    public void testInit() {
        // Add some cards to decks for testing
        Card card1 = new Card("testCard1");
        Card card2 = new Card("testCard2");
        deck1.add(card1);
        deck1.add(card2);
        deck2.add(new Card("testCard3"));

        // Test initialization
        game.init(ip1, ip2, "name1", "name2", deck1, deck2);

        // Verify player and deck initialization
        verify(ip1, times(1)).init(player1);
        verify(ip2, times(1)).init(player2);

        // Verify player properties
        assertSame(player1, game.player1);
        assertSame(player2, game.player2);
        assertSame(ip1, player1.userInterface);
        assertSame(ip2, player2.userInterface);

        // Verify deck properties
        assertEquals(deck1, player1.library);
        assertEquals(deck2, player2.library);
    }
    
}