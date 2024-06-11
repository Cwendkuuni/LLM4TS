import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*; // For mocking UserInterfaces and Player classes
import lotus.core.*;

public class GameTest {
    
    private UserInterface ip1;
    private UserInterface ip2;
    private Player player1;
    private Player player2;
    private Game game;

    @Before
    public void setUp() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        player1 = new Player(ip1, "Player1", new CardCollection());
        player2 = new Player(ip2, "Player2", new CardCollection());
        game = new Game() {
            @Override
            public void init(UserInterface ip1, UserInterface ip2, String n1, String n2, CardCollection deck1, CardCollection deck2) {
                Game.player1 = player1;
                Game.player2 = player2;
                Game.ip1 = ip1;
                Game.ip2 = ip2;
                this.init(ip1, ip2, n1, n2, deck1, deck2);
            }
        };
    }

    @Test
    public void testGetPlayingPlayer() {
        assertEquals(player1, game.getPlayingPlayer());
        game.playingPlayer = 2;
        assertEquals(player2, game.getPlayingPlayer());
    }

    
}