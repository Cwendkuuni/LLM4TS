package lotus.core;

import static org.junit.Assert.*;
import org.junit.Test;

import lotus.core.card.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.Player;
import lotus.core.stack.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Player player1 = new Player(new DummyUserInterface(), "player1", new CardCollection());
        Player player2 = new Player(new DummyUserInterface(), "player2", new CardCollection());
        Game game = new Game();
        game.player1 = player1;
        game.player2 = player2;
        game.playingPlayer = 1;

        assertEquals(player1, game.getPlayingPlayer());
        assertEquals(player2, game.getNonPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Player player1 = new Player(new DummyUserInterface(), "player1", new CardCollection());
        Player player2 = new Player(new DummyUserInterface(), "player2", new CardCollection());
        Game game = new Game();
        game.player1 = player1;
        game.player2 = player2;
        game.playingPlayer = 1;

        assertEquals(player2, game.getNonPlayingPlayer());
        assertEquals(player1, game.getOtherPlayer(player2));
        assertEquals(player1, game.getOtherPlayer(player1));
    }

    @Test
    public void testInit() {
        UserInterface ip1 = new DummyUserInterface();
        UserInterface ip2 = new DummyUserInterface();
        String name1 = "player1";
        String name2 = "player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();

        Game game = new Game();
        game.init(ip1, ip2, name1, name2, deck1, deck2);

        assertEquals(ip1, game.player1.getUI());
        assertEquals(name1, game.player1.getName());
        assertEquals(deck1, game.player1.getLibrary());

        assertEquals(ip2, game.player2.getUI());
        assertEquals(name2, game.player2.getName());
        assertEquals(deck2, game.player2.getLibrary());

        assertNotNull(game.combat);
        assertNotNull(game.currentPhase);
        assertEquals(1, game.playingPlayer);

        for (Card card : deck1) {
            assertEquals(game.player1, card.owner);
            assertEquals(game.player1.library, card.zone);
        }

        for (Card card : deck2) {
            assertEquals(game.player2, card.owner);
            assertEquals(game.player2.library, card.zone);
        }
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        UserInterface ip1 = new DummyUserInterface();
        UserInterface ip2 = new DummyUserInterface();
        Player player1 = new Player(ip1, "player1", new CardCollection());
        Player player2 = new Player(ip2, "player2", new CardCollection());
        Game game = new Game();
        game.player1 = player1;
        game.player2 = player2;
        game.playingPlayer = 1;

        Card card1 = new Card("card1", CardType.CREATURE, player1);
        Card card2 = new Card("card2", CardType.CREATURE, player2);

        game.stack.push(card1);
        game.stack.push(card2);

        game.givePriorityToCurrentPlayer();

        assertEquals(player2, card1.controller);
        assertEquals(player1, card2.controller);

        card1.resolve();
        game.givePriorityToCurrentPlayer();

        assertEquals(player1, card1.controller);
        assertEquals(player2, card2.controller);
    }
}