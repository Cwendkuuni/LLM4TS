package lotus.core;

import static org.junit.Assert.*;
import org.junit.Test;
import lotus.core.stack.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Player player1 = new Player(null, "Player1", new CardCollection());
        Player player2 = new Player(null, "Player2", new CardCollection());
        Game.init(null, null, "Player1", "Player2", new CardCollection(), new CardCollection());

        assertEquals(player1, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Player player1 = new Player(null, "Player1", new CardCollection());
        Player player2 = new Player(null, "Player2", new CardCollection());
        Game.init(null, null, "Player1", "Player2", new CardCollection(), new CardCollection());

        assertEquals(player2, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        Player player1 = new Player(null, "Player1", new CardCollection());
        Player player2 = new Player(null, "Player2", new CardCollection());
        Game.init(null, null, "Player1", "Player2", new CardCollection(), new CardCollection());

        assertEquals(player2, Game.getOtherPlayer(player1));
        assertEquals(player1, Game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        UserInterface IP1 = mock(UserInterface.class);
        UserInterface IP2 = mock(UserInterface.class);
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();

        when(IP1.init(any(Player.class))).thenReturn(true);
        when(IP2.init(any(Player.class))).thenReturn(true);

        Game.init(IP1, IP2, name1, name2, deck1, deck2);

        assertEquals(name1, Game.player1.getName());
        assertEquals(name2, Game.player2.getName());
        assertEquals(deck1, Game.player1.getDeck());
        assertEquals(deck2, Game.player2.getDeck());
        assertTrue(IP1.isInitialized());
        assertTrue(IP2.isInitialized());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        UserInterface IP1 = mock(UserInterface.class);
        UserInterface IP2 = mock(UserInterface.class);
        Player player1 = new Player(IP1, "Player1", new CardCollection());
        Player player2 = new Player(IP2, "Player2", new CardCollection());
        Game.init(IP1, IP2, "Player1", "Player2", new CardCollection(), new CardCollection());

        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(true);

        Game.givePriorityToCurrentPlayer();

        verify(player2).letPlayerSpeak();
        verify(player1).letPlayerSpeak();
        assertFalse(Game.stack.empty());

        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(false);

        Game.givePriorityToCurrentPlayer();

        verify(player1).letPlayerSpeak();
        verifyNoMoreInteractions();
    }

    @Test
    public void testGivePriorityToCurrentPlayer_EmptyStack() {
        UserInterface IP1 = mock(UserInterface.class);
        UserInterface IP2 = mock(UserInterface.class);
        Player player1 = new Player(IP1, "Player1", new CardCollection());
        Player player2 = new Player(IP2, "Player2", new CardCollection());
        Game.init(IP1, IP2, "Player1", "Player2", new CardCollection(), new CardCollection());

        Game.givePriorityToCurrentPlayer();

        verifyNoInteractions();
    }
}