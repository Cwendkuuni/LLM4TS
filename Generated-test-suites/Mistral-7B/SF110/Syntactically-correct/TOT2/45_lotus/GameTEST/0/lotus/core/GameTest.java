import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Game game = new Game();
        Player player1 = new Player(new DummyUserInterface(), "Player1", new CardCollection());
        Player player2 = new Player(new DummyUserInterface(), "Player2", new CardCollection());
        game.init(player1, player2, "Player1", "Player2", new CardCollection(), new CardCollection());

        assertEquals(player1, game.getPlayingPlayer());
        game.playingPlayer = 2;
        assertEquals(player2, game.getPlayingPlayer());
    }
}