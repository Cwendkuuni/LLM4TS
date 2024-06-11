import static org.junit.Assert.*;
import lotus.core.Game;

public class GameTest {
    @Test
    public void testGetPlayingPlayer() {
        Game game = new Game();
        game.playingPlayer = 1;
        Player expectedPlayer = game.player1;
        Player actualPlayer = game.getPlayingPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }
}