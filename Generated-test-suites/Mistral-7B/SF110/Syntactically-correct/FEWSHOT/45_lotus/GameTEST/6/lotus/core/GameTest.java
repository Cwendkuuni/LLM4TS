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
        Player player1 = Mockito.mock(Player.class);
        Player player2 = Mockito.mock(Player.class);
        Game.init(Mockito.mock(UserInterface.class), Mockito.mock(UserInterface.class), "p1", "p2", new CardCollection(), new CardCollection());
        Game.player1 = player1;
        Game.player2 = player2;

        assertEquals(player1, Game.getPlayingPlayer());
        assertEquals(player2, Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        Player player1 = Mockito.mock(Player.class);
        Player player2 = Mockito.mock(Player.class);
        Game.init(Mockito.mock(UserInterface.class), Mockito.mock(UserInterface.class), "p1", "p2", new CardCollection(), new CardCollection());
        Game.player1 = player1;
        Game.player2 = player2;

        assertEquals(player2, Game.getOtherPlayer(player1));
        assertEquals(player1, Game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        UserInterface ip1 = Mockito.mock(UserInterface.class);
        UserInterface ip2 = Mockito.mock(UserInterface.class);
        String name1 = "p1";
        String name2 = "p2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();

        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        verify(ip1, times(1)).init(player1);
        verify(ip2, times(1)).init(player2);
        assertEquals(name1, player1.getName());
        assertEquals(name2, player2.getName());
        assertEquals(deck1, player1.getDeck());
        assertEquals(deck2, player2.getDeck());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        UserInterface ip1 = Mockito.mock(UserInterface.class);
        UserInterface ip2 = Mockito.mock(UserInterface.class);
        Player player1 = Mockito.mock(Player.class);
        Player player2 = Mockito.mock(Player.class);
        Stack stack = Mockito.mock(Stack.class);

        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(false);
        when(stack.empty()).thenReturn(false);
        when(stack.resolveLast()).thenReturn(null);

        Game.init(ip1, ip2, "p1", "p2", new CardCollection(), new CardCollection());
        Game.player1 = player1;
        Game.player2 = player2;
        Game.stack = stack;

        Game.givePriorityToCurrentPlayer();

        verify(player1, times(1)).letPlayerSpeak();
        verify(player2, times(1)).letPlayerSpeak();
        verify(stack, times(1)).resolveLast();
        verify(ip1, times(1)).showMessage(isA(String.class));
        verify(ip2, times(1)).showMessage(isA(String.class));
    }

    @Test
    public void testGivePriorityToCurrentPlayer_OnePlayerSpeaks() {
        UserInterface ip1 = Mockito.mock(UserInterface.class);
        UserInterface ip2 = Mockito.mock(UserInterface.class);
        Player player1 = Mockito.mock(Player.class);
        Player player2 = Mockito.mock(Player.class);
        Stack stack = Mockito.mock(Stack.class);

        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(true);
        when(stack.empty()).thenReturn(false);
        when(stack.resolveLast()).thenReturn(null);

        Game.init(ip1, ip2, "p1", "p2", new CardCollection(), new CardCollection());
        Game.player1 = player1;
        Game.player2 = player2;
        Game.stack = stack;

        Game.givePriorityToCurrentPlayer();

        verify(player1, times(1)).letPlayerSpeak();
        verify(player2, times(0)).letPlayerSpeak();
        verify(stack, times(0)).resolveLast();
        verify(ip1, times(1)).showMessage(isA(String.class));
        verify(ip2, times(1)).showMessage(isA(String.class));
    }
}