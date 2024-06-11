import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {
    @Test
    public void testGetPlayingPlayer() {
        Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        Player expectedPlayer = Game.player1;
        Player actualPlayer = Game.getPlayingPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }
}