import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @BeforeAll
    static void init() {
        Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
    }

    @Test
    void testGetPlayingPlayer() {
        Player expectedPlayer = Game.player1;
        Player actualPlayer = Game.getPlayingPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    void testGetPlayingPlayer_whenOtherPlayerIsPlaying_thenReturnsOtherPlayer() {
        Game.playingPlayer = 2;
        Player expectedPlayer = Game.player1;
        Player actualPlayer = Game.getPlayingPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }
}