import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameTest {

    Game game;
    Player player1;
    Player player2;

    @Before
    public void setup() {
        player1 = new Player();
        player2 = new Player();
        game = new Game(player1, player2, new Stack());
    }

    @Test
    public void testGetPlayingPlayer() {
        assertEquals(player1, Game.getPlayingPlayer());
        game.setPlayingPlayer(2);
        assertEquals(player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        assertEquals(player2, Game.getNonPlayingPlayer());
        game.setPlayingPlayer(1);
        assertEquals(player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testInit() {
        List<Card> deck1 = new ArrayList<>();
        List<Card> deck2 = new ArrayList<>();
        Card card1 = new Card("TestCard1");
        Card card2 = new Card("TestCard2");
        deck1.add(card1);
        deck2.add(card2);

        game.init(deck1, deck2);

        assertEquals(player1, game.getPlayer1());
        assertEquals(player2, game.getPlayer2());
        assertEquals(deck1, player1.getLibrary());
        assertEquals(deck2, player2.getLibrary());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        player1.setLetPlayerSpeak(true);
        player2.setLetPlayerSpeak(false);

        game.givePriorityToCurrentPlayer();

        assertTrue(game.getStack().isEmpty());

        player1.setLetPlayerSpeak(false);
        player2.setLetPlayerSpeak(true);

        game.givePriorityToCurrentPlayer();

        assertFalse(game.getStack().isEmpty());
    }
}