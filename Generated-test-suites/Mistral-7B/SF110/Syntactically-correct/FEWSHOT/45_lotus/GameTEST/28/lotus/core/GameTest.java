package lotus.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import lotus.core.card.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import org.junit.Test;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Player player1 = new Player(mock(UserInterface.class), "Player1", new CardCollection());
        Player player2 = new Player(mock(UserInterface.class), "Player2", new CardCollection());
        Game.init(player1, player2, "Name1", "Name2", new CardCollection(), new CardCollection());
        assertEquals(player1, Game.getPlayingPlayer());
        Game.playingPlayer = 2;
        assertEquals(player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Player player1 = new Player(mock(UserInterface.class), "Player1", new CardCollection());
        Player player2 = new Player(mock(UserInterface.class), "Player2", new CardCollection());
        Game.init(player1, player2, "Name1", "Name2", new CardCollection(), new CardCollection());
        assertEquals(player2, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        Player player1 = new Player(mock(UserInterface.class), "Player1", new CardCollection());
        Player player2 = new Player(mock(UserInterface.class), "Player2", new CardCollection());
        Game.init(player1, player2, "Name1", "Name2", new CardCollection(), new CardCollection());
        assertEquals(player2, Game.getOtherPlayer(player1));
        assertEquals(player1, Game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        UserInterface IP1 = mock(UserInterface.class);
        UserInterface IP2 = mock(UserInterface.class);
        Player player1 = new Player(IP1, "Player1", new CardCollection());
        Player player2 = new Player(IP2, "Player2", new CardCollection());
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        when(IP1.init(player1)).thenReturn(true);
        when(IP2.init(player2)).thenReturn(true);

        Game.init(IP1, IP2, "Name1", "Name2", deck1, deck2);

        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertEquals("Name1", Game.player1.getName());
        assertEquals("Name2", Game.player2.getName());
        assertNotNull(Game.player1.library);
        assertNotNull(Game.player2.library);

        for (Card card : deck1) {
            assertNotNull(card);
            assertEquals(player1, card.owner);
            assertEquals(Game.player1.library, card.zone);
        }

        for (Card card : deck2) {
            assertNotNull(card);
            assertEquals(player2, card.owner);
            assertEquals(Game.player2.library, card.zone);
        }
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        UserInterface IP1 = mock(UserInterface.class);
        UserInterface IP2 = mock(UserInterface.class);
        Player player1 = new Player(IP1, "Player1", new CardCollection());
        Player player2 = new Player(IP2, "Player2", new CardCollection());
        Stack stack = new Stack();
        Game game = new Game();
        game.init(IP1, IP2, player1, player2, new CardCollection(), new CardCollection());

        when(IP1.letPlayerSpeak()).thenReturn(false);
        when(IP2.letPlayerSpeak()).thenReturn(true);

        game.givePriorityToCurrentPlayer();
        assertFalse(player1.hasSpoken);
        assertTrue(player2.hasSpoken);

        when(IP1.letPlayerSpeak()).thenReturn(true);
        when(IP2.letPlayerSpeak()).thenReturn(false);

        game.givePriorityToCurrentPlayer();
        assertTrue(player1.hasSpoken);
        assertFalse(player2.hasSpoken);

        Card card1 = new Card() {};
        Card card2 = new Card() {};
        stack.push(card1);
        stack.push(card2);

        when(IP1.letPlayerSpeak()).thenReturn(false);
        when(IP2.letPlayerSpeak()).thenReturn(false);

        game.givePriorityToCurrentPlayer();
        assertFalse(player1.hasSpoken);
        assertFalse(player2.hasSpoken);

        game.stack.resolveLast();
        assertEquals(card2, game.stack.peek());
        game.givePriorityToCurrentPlayer();
        assertFalse(player1.hasSpoken);
        assertFalse(player2.hasSpoken);
    }

    @Test
    public void testEmptyStack() {
        UserInterface IP1 = mock(UserInterface.class);
        UserInterface IP2 = mock(UserInterface.class);
        Player player1 = new Player(IP1, "Player1", new CardCollection());
        Player player2 = new Player(IP2, "Player2", new CardCollection());
        Stack stack = new Stack();
        Game game = new Game();
        game.init(IP1, IP2, player1, player2, new CardCollection(), new CardCollection());

        when(IP1.letPlayerSpeak()).thenReturn(false);
        when(IP2.letPlayerSpeak()).thenReturn(false);

        game.givePriorityToCurrentPlayer();
        assertFalse(player1.hasSpoken);
        assertFalse(player2.hasSpoken);

        assertEquals(0, stack.size());
        assertNull(stack.peek());
    }
}