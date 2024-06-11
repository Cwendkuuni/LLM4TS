package lotus.core;

import org.junit.Assert;
import org.junit.Test;

public class GameTest {
    @Test
    public void testGetPlayingPlayer() {
        // typical use case
        Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        Player expectedPlayer = new Player(new DummyUserInterface(), "Player1", new CardCollection());
        Player actualPlayer = Game.getPlayingPlayer();
        Assert.assertEquals(expectedPlayer, actualPlayer);

        // edge case: playingPlayer is already the other player
        Game.playingPlayer = 2;
        Player otherPlayer = Game.getPlayingPlayer();
        Assert.assertEquals(Game.player2, otherPlayer);

        // error scenario: getPlayingPlayer is called before init is called
        Assert.assertNull(Game.getPlayingPlayer());
    }

    // Test other methods here
}

class DummyUserInterface implements UserInterface {
    // empty implementation
}