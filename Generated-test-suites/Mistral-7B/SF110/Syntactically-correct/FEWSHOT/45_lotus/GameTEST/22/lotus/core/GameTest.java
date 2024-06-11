package lotus.core;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import lotus.core.interfaces.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        when(player1.getId()).thenReturn(1);
        when(player2.getId()).thenReturn(2);

        Game.init(mock(UserInterface.class), mock(UserInterface.class), "Player 1", "Player 2", new CardCollection(), new CardCollection());

        assertSame(player1, Game.getPlayingPlayer());
        assertSame(player2, Game.getPlayingPlayer(1)); // Testing with player ID
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        when(player1.getId()).thenReturn(1);
        when(player2.getId()).thenReturn(2);

        Game.init(mock(UserInterface.class), mock(UserInterface.class), "Player 1", "Player 2", new CardCollection(), new CardCollection());

        assertSame(player2, Game.getNonPlayingPlayer());
        assertSame(player1, Game.getOtherPlayer(player2));
        assertSame(player1, Game.getOtherPlayer(player1));
    }

    @Test
    public void testInit() {
        UserInterface IP1 = mock(UserInterface.class);
        UserInterface IP2 = mock(UserInterface.class);
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();

        Player player1 = new Player(IP1, name1, deck1);
        Player player2 = new Player(IP2, name2, deck2);

        when(IP1.init(player1)).thenReturn(true);
        when(IP2.init(player2)).thenReturn(true);

        Game.init(IP1, IP2, name1, name2, deck1, deck2);

        assertSame(player1, Game.player1);
        assertSame(player2, Game.player2);
        assertSame(IP1, Game.player1.getUI());
        assertSame(IP2, Game.player2.getUI());
        assertSame(deck1, Game.player1.getDeck());
        assertSame(deck2, Game.player2.getDeck());
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        UserInterface IP1 = mock(UserInterface.class);
        UserInterface IP2 = mock(UserInterface.class);
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);

        when(player1.getName()).thenReturn("Player 1");
        when(player2.getName()).thenReturn("Player 2");
        when(player1.letPlayerSpeak()).thenReturn(false);
        when(player2.letPlayerSpeak()).thenReturn(false);
        when(Game.stack.empty()).thenReturn(true);

        Game.init(IP1, IP2, "Player 1", "Player 2", new CardCollection(), new CardCollection());
        Game.givePriorityToCurrentPlayer();

        // Testing the first player's turn
        when(player1.getName()).thenReturn("Player X");
        when(player1.letPlayerSpeak()).thenReturn(true);

        Game.givePriorityToCurrentPlayer();
        assertSame(player1, Game.getPlayingPlayer());

        // Testing the second player's turn
        when(player2.getName()).thenReturn("Player Y");
        when(player2.letPlayerSpeak()).thenReturn(true);

        Game.givePriorityToCurrentPlayer();
        assertSame(player2, Game.getPlayingPlayer());
    }

    @Test
    public void testStackResolution() {
        UserInterface IP1 = mock(UserInterface.class);
        UserInterface IP2 = mock(UserInterface.class);
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);

        when(player1.getName()).thenReturn("Player 1");
        when(player2.getName()).thenReturn("Player 2");
        when(player1.letPlayerSpeak()).thenReturn(true);
        when(player2.letPlayerSpeak()).thenReturn(true);

        when(card1.getName()).thenReturn("Card 1");
        when(card2.getName()).thenReturn("Card 2");
        when(card1.getResponses()).thenReturn(Collections.singletonList("Response 1"));
        when(card2.getResponses()).thenReturn(Collections.singletonList("Response 2"));

        when(Game.stack.push(card1)).thenReturn(true);
        when(Game.stack.push(card2)).thenReturn(true);

        Game.init(IP1, IP2, "Player 1", "Player 2", new CardCollection(), new CardCollection());
        Game.givePriorityToCurrentPlayer();

        Game.stack.resolveLast();

        // Testing the first card resolution
        when(Game.stack.peek()).thenReturn(card1);
        Game.stack.resolveLast();
        assertSame(card1, Game.stack.peek());
        verify(card1).respond("Response 1");

        // Testing the second card resolution
        when(Game.stack.peek()).thenReturn(card2);
        Game.stack.resolveLast();
        assertSame(card2, Game.stack.peek());
        verify(card2).respond("Response 2");

        // Testing the empty stack
        when(Game.stack.peek()).thenReturn(null);
        Game.stack.resolveLast();
        assertNull(Game.stack.peek());
    }

    @Test
    public void testInitWithEmptyDecks() {
        UserInterface IP1 = mock(UserInterface.class);
        UserInterface IP2 = mock(UserInterface.class);
        String name1 = "Player 1";
        String name2 = "Player 2";

        Player player1 = new Player(IP1, name1, new CardCollection());
        Player player2 = new Player(IP2, name2, new CardCollection());

        when(IP1.init(player1)).thenReturn(true);
        when(IP2.init(player2)).thenReturn(true);

        Game.init(IP1, IP2, name1, name2, new CardCollection(), new CardCollection());

        assertSame(player1, Game.player1);
        assertSame(player2, Game.player2);
        assertSame(IP1, Game.player1.getUI());
        assertSame(IP2, Game.player2.getUI());
        assertSame(Collections.emptyList(), Game.player1.getDeck().getCards());
        assertSame(Collections.emptyList(), Game.player2.getDeck().getCards());
    }

    @Test
    public void testInitWithNonEmptyDecks() {
        UserInterface IP1 = mock(UserInterface.class);
        UserInterface IP2 = mock(UserInterface.class);
        String name1 = "Player 1";
        String name2 = "Player 2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        List<Card> cards1 = Arrays.asList(mock(Card.class), mock(Card.class), mock(Card.class));
        List<Card> cards2 = Arrays.asList(mock(Card.class), mock(Card.class), mock(Card.class));

        when(IP1.init(player1)).thenReturn(true);
        when(IP2.init(player2)).thenReturn(true);

        for (Card card : cards1) {
            when(card.getName()).thenReturn("Card X");
            deck1.addCard(card);
        }

        for (Card card : cards2) {
            when(card.getName()).thenReturn("Card Y");
            deck2.addCard(card);
        }

        Game.init(IP1, IP2, name1, name2, deck1, deck2);

        assertSame(player1, Game.player1);
        assertSame(player2, Game.player2);
        assertSame(IP1, Game.player1.getUI());
        assertSame(IP2, Game.player2.getUI());
        assertSame(cards1, Game.player1.getDeck().getCards());
        assertSame(cards2, Game.player2.getDeck().getCards());
    }
}