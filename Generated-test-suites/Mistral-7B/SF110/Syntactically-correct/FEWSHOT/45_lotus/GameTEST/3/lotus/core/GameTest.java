package lotus.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lotus.core.card.Card;
import lotus.core.card.CardCollection;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.Phase;
import lotus.core.stack.Stack;
import lotus.core.Player;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);

        when(player1.ordinal()).thenReturn(1);
        when(player2.ordinal()).thenReturn(2);

        Game.player1 = player1;
        Game.player2 = player2;

        assertSame(player1, Game.getPlayingPlayer());
        assertSame(player2, Game.getPlayingPlayer(1));
        assertSame(player2, Game.getPlayingPlayer(2));
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);

        when(player1.ordinal()).thenReturn(1);
        when(player2.ordinal()).thenReturn(2);

        Game.player1 = player1;
        Game.player2 = player2;

        assertSame(player2, Game.getNonPlayingPlayer());
        assertSame(player1, Game.getNonPlayingPlayer(1));
        assertSame(player1, Game.getNonPlayingPlayer(2));
    }

    @Test
    public void testGetOtherPlayer() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);

        when(player1.ordinal()).thenReturn(1);
        when(player2.ordinal()).thenReturn(2);

        Game.player1 = player1;
        Game.player2 = player2;

        assertSame(player2, Game.getOtherPlayer(player1));
        assertSame(player1, Game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Player player1 = new Player(ip1, name1, deck1);
        Player player2 = new Player(ip2, name2, deck2);

        when(ip1.init(player1)).thenReturn(true);
        when(ip2.init(player2)).thenReturn(true);

        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        assertEquals(player1, Game.player1);
        assertEquals(player2, Game.player2);
        assertEquals(ip1, Game.player1.ui);
        assertEquals(ip2, Game.player2.ui);
        assertEquals(name1, Game.player1.name);
        assertEquals(name2, Game.player2.name);
        assertEquals(deck1, Game.player1.library);
        assertEquals(deck2, Game.player2.library);
    }

    @Test
    public void testGivePriorityToCurrentPlayer_NoPlayersSpeak() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Stack stack = new Stack();

        when(ip1.letPlayerSpeak()).thenReturn(false);
        when(ip2.letPlayerSpeak()).thenReturn(false);
        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(false);

        Game.init(ip1, ip2, "Player 1", "Player 2", new ArrayList<>(), new ArrayList<>());
        Game.player1 = player1;
        Game.player2 = player2;

        Game.givePriorityToCurrentPlayer();

        assertFalse(stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_PlayerSpeaks() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Stack stack = new Stack();
        Card card = mock(Card.class);

        when(ip1.letPlayerSpeak()).thenReturn(true);
        when(player1.letPlayerSpeak()).thenReturn(true);
        when(ip2.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(false);
        when(stack.empty()).thenReturn(true);
        when(stack.resolveLast()).thenReturn(card);

        Game.init(ip1, ip2, "Player 1", "Player 2", new ArrayList<>(), new ArrayList<>());
        Game.player1 = player1;
        Game.player2 = player2;

        Game.givePriorityToCurrentPlayer();

        assertTrue(stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_BothPlayersSpeak() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Stack stack = new Stack();

        when(ip1.letPlayerSpeak()).thenReturn(true);
        when(player1.letPlayerSpeak()).thenReturn(true);
        when(ip2.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(true);
        when(stack.empty()).thenReturn(true);

        Game.init(ip1, ip2, "Player 1", "Player 2", new ArrayList<>(), new ArrayList<>());
        Game.player1 = player1;
        Game.player2 = player2;

        Game.givePriorityToCurrentPlayer();

        assertFalse(stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_APSpeaksFirst() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Stack stack = new Stack();

        when(ip1.letPlayerSpeak()).thenReturn(true);
        when(player1.letPlayerSpeak()).thenReturn(true);
        when(ip2.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(true);
        when(stack.empty()).thenReturn(true);

        Game.init(ip1, ip2, "Player 1", "Player 2", new ArrayList<>(), new ArrayList<>());
        Game.player1 = player1;
        Game.player2 = player2;

        Game.givePriorityToCurrentPlayer();

        assertTrue(stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_NAPSpeaksFirst() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Stack stack = new Stack();

        when(ip1.letPlayerSpeak()).thenReturn(false);
        when(player1.letPlayerSpeak()).thenReturn(true);
        when(ip2.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(true);
        when(stack.empty()).thenReturn(true);

        Game.init(ip1, ip2, "Player 1", "Player 2", new ArrayList<>(), new ArrayList<>());
        Game.player1 = player1;
        Game.player2 = player2;

        Game.givePriorityToCurrentPlayer();

        assertFalse(stack.empty());
    }

    @Test
    public void testInit_NullUserInterfaces() {
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Game.init(null, null, name1, name2, deck1, deck2);

        assertNull(Game.player1);
        assertNull(Game.player2);
    }

    @Test
    public void testInit_NullNames() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Game.init(ip1, ip2, null, null, deck1, deck2);

        assertNull(Game.player1);
        assertNull(Game.player2);
    }

    @Test
    public void testInit_NullDecks() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        String name1 = "Player 1";
        String name2 = "Player 2";

        Game.init(ip1, ip2, name1, name2, null, null);

        assertNull(Game.player1);
        assertNull(Game.player2);
    }

    @Test
    public void testInit_EmptyDecks() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = Collections.emptyList();
        CardCollection deck2 = Collections.emptyList();

        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        assertSame(deck1, Game.player1.library);
        assertSame(deck2, Game.player2.library);
    }

    @Test
    public void testInit_DecksWithCards() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();
        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);

        deck1.add(card1);
        deck2.add(card2);

        when(ip1.init(Game.player1)).thenReturn(true);
        when(ip2.init(Game.player2)).thenReturn(true);

        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        assertSame(deck1, Game.player1.library);
        assertSame(deck2, Game.player2.library);
        assertSame(card1, Game.player1.library.get(0));
        assertSame(card2, Game.player2.library.get(0));
    }
}