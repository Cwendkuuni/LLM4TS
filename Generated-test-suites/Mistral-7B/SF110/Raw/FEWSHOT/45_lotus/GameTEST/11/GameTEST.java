package lotus.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Game game = new Game();
        game.player1 = player1;
        game.player2 = player2;

        assertSame(player1, game.getPlayingPlayer());
        assertSame(player2, game.getPlayingPlayer(1));
        assertSame(player2, game.getPlayingPlayer(2));
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Game game = new Game();
        game.player1 = player1;
        game.player2 = player2;

        assertSame(player2, game.getNonPlayingPlayer());
        assertSame(player1, game.getNonPlayingPlayer(1));
        assertSame(player1, game.getNonPlayingPlayer(2));
    }

    @Test
    public void testGetOtherPlayer() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Game game = new Game();
        game.player1 = player1;
        game.player2 = player2;

        assertSame(player2, game.getOtherPlayer(player1));
        assertSame(player1, game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Game game = new Game();
        when(ip1.init(any(Player.class))).thenReturn(true);
        when(ip2.init(any(Player.class))).thenReturn(true);

        game.init(ip1, ip2, name1, name2, deck1, deck2);

        verify(ip1, times(1)).init(player1);
        verify(ip2, times(1)).init(player2);
        assertEquals(name1, player1.getName());
        assertEquals(name2, player2.getName());
        assertSame(deck1, player1.getDeck());
        assertSame(deck2, player2.getDeck());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        Player player1 = new Player(ip1, "Player 1");
        Player player2 = new Player(ip2, "Player 2");
        Game game = new Game();
        game.player1 = player1;
        game.player2 = player2;

        when(ip1.letPlayerSpeak()).thenReturn(true);
        when(ip2.letPlayerSpeak()).thenReturn(true);

        List<Card> player1Library = new ArrayList<>();
        List<Card> player2Library = new ArrayList<>();
        player1.library = player1Library;
        player2.library = player2Library;

        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);
        Card card3 = mock(Card.class);

        player1Library.add(card1);
        player2Library.add(card2);
        player2Library.add(card3);

        game.givePriorityToCurrentPlayer();

        verify(ip1, times(1)).letPlayerSpeak();
        verify(ip2, times(1)).letPlayerSpeak();
        assertFalse(player1Library.isEmpty());
        assertEquals(card1, player1Library.get(0));
        assertTrue(player1Library.size() > 1);
        verify(game.stack, times(1)).resolveLast();
    }

    @Test
    public void testGivePriorityToCurrentPlayer_NoCardsToResolve() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        Player player1 = new Player(ip1, "Player 1");
        Player player2 = new Player(ip2, "Player 2");
        Game game = new Game();
        game.player1 = player1;
        game.player2 = player2;

        when(ip1.letPlayerSpeak()).thenReturn(true);
        when(ip2.letPlayerSpeak()).thenReturn(true);

        List<Card> player1Library = new ArrayList<>();
        List<Card> player2Library = new ArrayList<>();
        player1.library = player1Library;
        player2.library = player2Library;

        game.givePriorityToCurrentPlayer();

        verify(ip1, times(1)).letPlayerSpeak();
        verify(ip2, times(1)).letPlayerSpeak();
        assertTrue(player1Library.isEmpty());
        assertTrue(player2Library.isEmpty());
        verify(game.stack, times(0)).resolveLast();
    }

    @Test
    public void testGivePriorityToCurrentPlayer_APSpeaksFirst() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        Player player1 = new Player(ip1, "Player 1");
        Player player2 = new Player(ip2, "Player 2");
        Game game = new Game();
        game.player1 = player1;
        game.player2 = player2;

        when(ip1.letPlayerSpeak()).thenReturn(true);
        when(ip2.letPlayerSpeak()).thenReturn(false);

        List<Card> player1Library = new ArrayList<>();
        List<Card> player2Library = new ArrayList<>();
        player1.library = player1Library;
        player2.library = player2Library;

        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);

        player1Library.add(card1);
        player2Library.add(card2);

        game.givePriorityToCurrentPlayer();

        verify(ip1, times(1)).letPlayerSpeak();
        verify(ip2, times(0)).letPlayerSpeak();
        assertFalse(player1Library.isEmpty());
        assertEquals(card1, player1Library.get(0));
        assertTrue(player1Library.size() > 1);
        verify(game.stack, times(0)).resolveLast();
    }

    @Test
    public void testGivePriorityToCurrentPlayer_NAPSpeaksFirst() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        Player player1 = new Player(ip1, "Player 1");
        Player player2 = new Player(ip2, "Player 2");
        Game game = new Game();
        game.player1 = player1;
        game.player2 = player2;

        when(ip1.letPlayerSpeak()).thenReturn(false);
        when(ip2.letPlayerSpeak()).thenReturn(true);

        List<Card> player1Library = new ArrayList<>();
        List<Card> player2Library = new ArrayList<>();
        player1.library = player1Library;
        player2.library = player2Library;

        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);

        player1Library.add(card1);
        player2Library.add(card2);

        game.givePriorityToCurrentPlayer();

        verify(ip1, times(0)).letPlayerSpeak();
        verify(ip2, times(1)).letPlayerSpeak();
        assertFalse(player1Library.isEmpty());
        assertEquals(card2, player1Library.get(0));
        assertTrue(player1Library.size() > 1);
        verify(game.stack, times(0)).resolveLast();
    }

    @Test
    public void testGivePriorityToCurrentPlayer_APAndNAPSpeakAtSameTime() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        Player player1 = new Player(ip1, "Player 1");
        Player player2 = new Player(ip2, "Player 2");
        Game game = new Game();
        game.player1 = player1;
        game.player2 = player2;

        when(ip1.letPlayerSpeak()).thenReturn(true);
        when(ip2.letPlayerSpeak()).thenReturn(true);

        List<Card> player1Library = new ArrayList<>();
        List<Card> player2Library = new ArrayList<>();
        player1.library = player1Library;
        player2.library = player2Library;

        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);

        player1Library.add(card1);
        player2Library.add(card2);

        game.givePriorityToCurrentPlayer();

        verify(ip1, times(1)).letPlayerSpeak();
        verify(ip2, times(1)).letPlayerSpeak();
        assertFalse(player1Library.isEmpty());
        assertEquals(card1, player1Library.get(0));
        assertTrue(player1Library.size() > 1);
        verify(game.stack, times(1)).resolveLast();
    }
}