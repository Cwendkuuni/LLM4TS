import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {
    @Test
    public void testGetPlayingPlayer() {
        // Given
        Game game = new Game();
        Player player1 = new Player(new DummyUserInterface(), "Player1", new CardCollection());
        Player player2 = new Player(new DummyUserInterface(), "Player2", new CardCollection());
        game.init(game, game, player1, player2, new CardCollection(), new CardCollection());

        // When
        Player result = game.getPlayingPlayer();

        // Then
        assertEquals(player1, result);
        game.playingPlayer = 2;
        assertEquals(player2, result);
    }

    // ... (repeat this pattern for other methods)
}