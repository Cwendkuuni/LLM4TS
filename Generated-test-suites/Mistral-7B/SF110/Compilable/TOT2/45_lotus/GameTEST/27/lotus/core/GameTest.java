package lotus.core;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GameTest {
    @Test
    public void testGetPlayingPlayer() {
        Game game = new Game();
        Player player1 = new Player(null, "Player1", null);
        Player player2 = new Player(null, "Player2", null);
        game.init(null, null, "Player1", "Player2", null, null);
        assertEquals(player1, game.getPlayingPlayer());
    }

    // Edge case testing for getPlayingPlayer()
    @Test(expected = IllegalStateException.class)
    public void testGetPlayingPlayer_NoPlayingPlayer() {
        Game game = new Game();
        assertEquals(null, game.getPlayingPlayer());
    }
}