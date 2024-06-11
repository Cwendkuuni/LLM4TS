import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import lotus.core.Card;
import lotus.core.Player;
import lotus.core.Stack;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class GameTest {

    private UserInterface ip1;
    private UserInterface ip2;
    private Player player1;
    private Player player2;
    private Game game;
    private Stack stack;

    @Before
    public void setUp() {
        ip1 = Mockito.mock(UserInterface.class);
        ip2 = Mockito.mock(UserInterface.class);
        player1 = new Player(ip1, "John", new CardCollection());
        player2 = new Player(ip2, "Mike", new CardCollection());
        stack = new Stack();
        game = new Game();
        game.init(ip1, ip2, "John", "Mike", new CardCollection(), new CardCollection());
    }

    @Test
    public void testGetPlayingPlayer() {
        assertEquals(player1, game.getPlayingPlayer());
        game.playingPlayer = 2;
        assertEquals(player2, game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        assertEquals(player2, game.getNonPlayingPlayer());
        game.playingPlayer = 1;
        assertEquals(player2, game.getNonPlayingPlayer());
        game.playingPlayer = 2;
        assertEquals(player1, game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        assertEquals(player2, game.getOtherPlayer(player1));
        assertEquals(player1, game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        List<Card> player1Deck = new ArrayList<>(Arrays.asList(new Card("Card1"), new Card("Card2")));
        List<Card> player2Deck = new ArrayList<>(Arrays.asList(new Card("Card3"), new Card("Card4")));

        game.init(ip1, ip2, "John", "Mike", new CardCollection(player1Deck), new CardCollection(player2Deck));

        assertEquals(player1, game.getPlayingPlayer());
        assertEquals(player2, game.getNonPlayingPlayer());
        assertEquals(ip1, player1.getUserInterface());
        assertEquals(ip2, player2.getUserInterface());
        assertEquals(player1Deck, player1.getLibrary());
        assertEquals(player2Deck, player2.getLibrary());
        assertNull(game.combat);
        assertNull(game.currentPhase);
        assertEquals(1, game.playingPlayer);
        assertEquals(new UntapPhase(), game.currentPhase);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Card card1 = new Card("Card1");
        Card card2 = new Card("Card2");
        player1.library.add(card1);
        player2.library.add(card2);

        game.init(ip1, ip2, "John", "Mike", new CardCollection(Collections.singletonList(card1)), new CardCollection(Collections.singletonList(card2)));
        game.givePriorityToCurrentPlayer();

        assertTrue(card1.isInPlay());
        assertFalse(card2.isInPlay());
    }

    @Test(expected = IllegalStateException.class)
    public void testGivePriorityToCurrentPlayerWithStackNotEmpty() {
        Card card1 = new Card("Card1");
        Card card2 = new Card("Card2");
        player1.library.add(card1);
        player2.library.add(card2);
        stack.add(card1);

        game.init(ip1, ip2, "John", "Mike", new CardCollection(Collections.singletonList(card1)), new CardCollection(Collections.singletonList(card2)));
        game.givePriorityToCurrentPlayer();

        fail("Expected IllegalStateException but did not receive");
    }
}