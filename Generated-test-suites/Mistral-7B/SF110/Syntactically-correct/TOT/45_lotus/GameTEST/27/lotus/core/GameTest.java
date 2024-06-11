import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*; // for Mockito library
import lotus.core.*;

public class GameTest {
    
    
    private UserInterface ip1, ip2;
    private Player player1, player2;
    private CardCollection deck1, deck2;
    private Game game;

    @Before
    public void setup() {
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
        assertSame(player1, game.getPlayingPlayer());
        game.playingPlayer = 2;
        assertSame(player2, game.getPlayingPlayer());
    }

    
}