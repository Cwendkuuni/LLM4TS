package lotus.core;

import static org.junit.Assert.*;
import org.junit.Test;

import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.Player;
import lotus.core.stack.Stack;
import lotus.core.card.Card;
import java.util.ArrayList;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        String name1 = "player1";
        String name2 = "player2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();
        Player player1 = new Player(ip1, name1, deck1);
        Player player2 = new Player(ip2, name2, deck2);
        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        Player playingPlayer = Game.getPlayingPlayer();
        assertEquals(player1, playingPlayer);

        Game.playingPlayer = 2;
        Player otherPlayer = Game.getPlayingPlayer();
        assertEquals(player2, otherPlayer);
    }

    @Test
    public void testGetNonPlayingPlayer() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        String name1 = "player1";
        String name2 = "player2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();
        Player player1 = new Player(ip1, name1, deck1);
        Player player2 = new Player(ip2, name2, deck2);
        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        Player nonPlayingPlayer = Game.getNonPlayingPlayer();
        assertEquals(player2, nonPlayingPlayer);

        Player playingPlayer = player1;
        Player otherPlayer = Game.getOtherPlayer(playingPlayer);
        assertEquals(player2, otherPlayer);

        Player otherPlayer2 = player2;
        Player yetAnotherPlayer = Game.getOtherPlayer(otherPlayer2);
        assertEquals(player1, yetAnotherPlayer);
    }

    @Test
    public void testInit() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        String name1 = "player1";
        String name2 = "player2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Player player1 = new Player(ip1, name1, deck1);
        Player player2 = new Player(ip2, name2, deck2);

        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        assertEquals(player1, Game.player1);
        assertEquals(player2, Game.player2);
        assertNotNull(Game.currentPhase);
        assertEquals(UntapPhase.class, Game.currentPhase.getClass());
        assertEquals(1, Game.playingPlayer);

        for (Card card : deck1) {
            assertEquals(player1, card.owner);
            assertNotNull(card.zone);
            assertEquals(player1.library, card.zone);
        }

        for (Card card : deck2) {
            assertEquals(player2, card.owner);
            assertNotNull(card.zone);
            assertEquals(player2.library, card.zone);
        }

        assertEquals(0, Game.stack.size());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        String name1 = "player1";
        String name2 = "player2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Player player1 = new Player(ip1, name1, deck1);
        Player player2 = new Player(ip2, name2, deck2);
        Card card1 = new Card();
        Card card2 = new Card();
        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        Game.stack.push(card1);
        Game.stack.push(card2);

        Game.givePriorityToCurrentPlayer();
        assertEquals(card1, Game.stack.peek());
        Game.givePriorityToCurrentPlayer();
        assertEquals(card2, Game.stack.peek());
    }

}