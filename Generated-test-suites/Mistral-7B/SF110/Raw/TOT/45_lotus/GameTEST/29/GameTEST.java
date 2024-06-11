package lotus.core;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GameTest {
    
    
    @Test
    public void testGetPlayingPlayer() {
        UserInterface ip1 = new DummyUserInterface();
        UserInterface ip2 = new DummyUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
         Game.init(ip1, ip2, name1, name2, deck1, deck2);
        Player expectedPlayer = Game.player1;
         Player actualPlayer = Game.getPlayingPlayer();
         assertEquals(expectedPlayer, actualPlayer);
    }

    
}