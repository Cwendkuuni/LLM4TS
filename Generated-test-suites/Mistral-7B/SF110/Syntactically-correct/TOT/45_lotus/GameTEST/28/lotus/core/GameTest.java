package lotus.core;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*; // for Mockito framework

public class GameTest {
    Game game;
    UserInterface ip1, ip2;
    Player player1, player2;
    CardCollection deck1, deck2;

    @Before
    public void setUp() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        player1 = new Player(ip1, "Name1", new CardCollection());
        player2 = new Player(ip2, "Name2", new CardCollection());
        game = new Game();
        game.init(ip1, ip2, "Name1", "Name2", deck1, deck2);
    }

    @Test
    public void testGetPlayingPlayer() {
        assertSame(player1, game.getPlayingPlayer());
        game.playingPlayer = 2;
        assertSame(player2, game.getPlayingPlayer());
    }

    // Add more test cases for other methods here
}