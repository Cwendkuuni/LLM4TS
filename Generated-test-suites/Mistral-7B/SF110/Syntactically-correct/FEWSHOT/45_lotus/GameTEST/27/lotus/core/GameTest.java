package lotus.core;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.Player;
import lotus.core.card.Card;
import lotus.core.stack.Stack;

public class GameTest {

    private UserInterface ip1;
    private UserInterface ip2;
    private Player player1;
    private Player player2;
    private Game game;
    private Card card1, card2;

    @Before
    public void setUp() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        player1 = new Player(ip1, "Player1", new ArrayList<>());
        player2 = new Player(ip2, "Player2", new ArrayList<>());
        game = new Game();
        card1 = new Card("Card1");
        card2 = new Card("Card2");
    }

    @Test
    public void testGetPlayingPlayer() {
        game.player1 = this.player1;
        game.player2 = this.player2;
        assertEquals(this.player1, game.getPlayingPlayer());

        game.playingPlayer = 2;
        assertEquals(this.player2, game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        game.player1 = this.player1;
        game.player2 = this.player2;
        assertEquals(this.player2, game.getNonPlayingPlayer());

        game.playingPlayer = 1;
        assertEquals(this.player2, game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        game.player1 = this.player1;
        game.player2 = this.player2;
        assertEquals(this.player2, game.getOtherPlayer(this.player1));
        assertEquals(this.player1, game.getOtherPlayer(this.player2));
    }

    @Test
    public void testInit() {
        game.init(ip1, ip2, "Name1", "Name2", new ArrayList<>(), new ArrayList<>());
        assertEquals(this.player1, game.getPlayingPlayer());
        assertEquals(this.player2, game.getNonPlayingPlayer());

        verify(ip1).init(this.player1);
        verify(ip2).init(this.player2);

        assertEquals(UntapPhase.class, game.currentPhase.getClass());

        for (Card c : game.player1.deck) {
            assertEquals(this.player1, c.owner);
            assertEquals(game.player1.library, c.zone);
        }

        for (Card c : game.player2.deck) {
            assertEquals(this.player2, c.owner);
            assertEquals(game.player2.library, c.zone);
        }
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        game.init(ip1, ip2, "Name1", "Name2", new ArrayList<>(), new ArrayList<>());
        game.givePriorityToCurrentPlayer();

        verify(ip1).letPlayerSpeak();
        verify(ip2).letPlayerSpeak();

        if (!game.stack.empty()) {
            verify(game.stack).resolveLast();
            game.givePriorityToCurrentPlayer();
            verify(ip1, times(2)).letPlayerSpeak();
            verify(ip2, times(2)).letPlayerSpeak();
        }
    }

    @Test
    public void testInitWithCards() {
        game.init(ip1, ip2, "Name1", "Name2", Arrays.asList(card1, card2), Arrays.asList(card2, card1));
        assertEquals(this.player1, game.getPlayingPlayer());
        assertEquals(this.player2, game.getNonPlayingPlayer());

        verify(ip1).init(this.player1);
        verify(ip2).init(this.player2);

        assertEquals(UntapPhase.class, game.currentPhase.getClass());

        for (Card c : game.player1.library) {
            assertEquals(this.player1, c.owner);
        }

        for (Card c : game.player2.library) {
            assertEquals(this.player2, c.owner);
        }

        for (Card c : game.player1.deck) {
            assertEquals(this.player1, c.owner);
            assertEquals(game.player1.library, c.zone);
        }

        for (Card c : game.player2.deck) {
            assertEquals(this.player2, c.owner);
            assertEquals(game.player2.library, c.zone);
        }
    }
}