package lotus.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

public class GameTest {
    @Test
    public void testGetPlayingPlayer() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Game game = new Game();
        game.player1 = player1;
        game.player2 = player2;
        game.playingPlayer = 1;

        assertEquals(player1, game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Game game = new Game();
        game.player1 = player1;
        game.player2 = player2;

        assertEquals(player2, game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Game game = new Game();
        game.player1 = player1;
        game.player2 = player2;

        assertEquals(player2, game.getOtherPlayer(player1));
        assertEquals(player1, game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        UserInterface ui1 = mock(UserInterface.class);
        UserInterface ui2 = mock(UserInterface.class);
        String name1 = "player1";
        String name2 = "player2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Game game = new Game();
        Player player1 = new Player(ui1, name1, deck1);
        Player player2 = new Player(ui2, name2, deck2);

        game.init(ui1, ui2, name1, name2, deck1, deck2);

        verify(ui1).init(player1);
        verify(ui2).init(player2);
        assertEquals(player1, game.getPlayingPlayer());
        assertEquals(player2, game.getNonPlayingPlayer());
        assertNull(game.currentPhase);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Stack stack = mock(Stack.class);

        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(false);

        Game game = new Game();
        game.player1 = player1;
        game.player2 = player2;
        game.stack = stack;

        game.init(ip1, ip2, "player1", "player2", new ArrayList<>(), new ArrayList<>());

        game.givePriorityToCurrentPlayer();

        verify(player1, times(2)).letPlayerSpeak();
        verify(player2, times(2)).letPlayerSpeak();
        verify(stack).resolveLast();
    }
}