package lotus.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lotus.core.card.Card;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.stack.Stack;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        when(player1.getId()).thenReturn(1);
        when(player2.getId()).thenReturn(2);

        Game.init(mock(UserInterface.class), mock(UserInterface.class), "p1", "p2", new CardCollection(), new CardCollection());

        assertTrue(Game.getPlayingPlayer() == player1);
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        when(player1.getId()).thenReturn(1);
        when(player2.getId()).thenReturn(2);

        Game.init(mock(UserInterface.class), mock(UserInterface.class), "p1", "p2", new CardCollection(), new CardCollection());

        assertTrue(Game.getNonPlayingPlayer() == player2);
    }

    @Test
    public void testGetOtherPlayer() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        when(player1.getId()).thenReturn(1);
        when(player2.getId()).thenReturn(2);

        Game.init(mock(UserInterface.class), mock(UserInterface.class), "p1", "p2", new CardCollection(), new CardCollection());

        assertTrue(Game.getOtherPlayer(player1) == player2);
        assertTrue(Game.getOtherPlayer(player2) == player1);
    }

    @Test
    public void testInit() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        CardCollection deck1 = mock(CardCollection.class);
        CardCollection deck2 = mock(CardCollection.class);

        when(ip1.init(player1)).thenReturn(true);
        when(ip2.init(player2)).thenReturn(true);

        Game.init(ip1, ip2, "p1", "p2", deck1, deck2);

        assertTrue(Game.player1 == player1);
        assertTrue(Game.player2 == player2);
        assertSame(ip1, Game.player1.getUserInterface());
        assertSame(ip2, Game.player2.getUserInterface());
        assertTrue(Game.playingPlayer == 1);
        assertTrue(Game.currentPhase instanceof UntapPhase);
    }

    @Test
    public void testGivePriorityToCurrentPlayer_NoCardsInStack() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);

        Game.init(ip1, ip2, player1, player2, new CardCollection(), new CardCollection());

        Game.givePriorityToCurrentPlayer();

        // Check that each player spoke during priority passing
        verify(player1).letPlayerSpeak();
        verify(player2).letPlayerSpeak();

        // Check that no cards were resolved
        assertTrue(Game.stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_CardsInStack() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        Stack stack = new Stack();
        stack.push(mock(Card.class));

        Game.init(ip1, ip2, player1, player2, new CardCollection(), new CardCollection());
        Game.givePriorityToCurrentPlayer();

        // Check that each player spoke during priority passing
        verify(player1).letPlayerSpeak();
        verify(player2).letPlayerSpeak();

        // Check that the top card was resolved
        Card card = mock(Card.class);
        when(stack.pop()).thenReturn(card);
        verify(card).resolve();

        // Check that priority passing was repeated
        verify(player1).letPlayerSpeak();
        verify(player2).letPlayerSpeak();
        verify(card).resolve();
    }

    @Test
    public void testLetPlayerSpeak_ReturnsFalse_WhenPlayerCannotSpeak() {
        Player player = mock(Player.class);

        when(player.canSpeak()).thenReturn(false);

        assertTrue(player.letPlayerSpeak());

        // Check that no interaction occurred with the user interface
        verifyNoInteractions(player.getUserInterface());
    }

    @Test
    public void testLetPlayerSpeak_ReturnsFalse_WhenPlayerSpeaksAndStacksANewCard() {
        UserInterface ip = mock(UserInterface.class);
        Player player = mock(Player.class);
        Card card = mock(Card.class);

        when(player.getUserInterface()).thenReturn(ip);
        when(ip.displayText("You cannot speak now.")).thenReturn(false);
        when(player.canSpeak()).thenReturn(true);

        Game game = new Game();
        game.init(ip, ip, player, null, new CardCollection(), new CardCollection());
        game.stack = new Stack();
        game.stack.push(card);

        assertFalse(player.letPlayerSpeak());

        // Check that the card was not resolved
        verify(card, never()).resolve();
    }

    @Test
    public void testLetPlayerSpeak_ReturnsTrue_WhenPlayerSpeaksAndNoNewCardIsAdded() {
        UserInterface ip = mock(UserInterface.class);
        Player player = mock(Player.class);
        Card card = mock(Card.class);

        when(player.getUserInterface()).thenReturn(ip);
        when(ip.displayText("You cannot speak now.")).thenReturn(false);
        when(player.canSpeak()).thenReturn(true);

        Game game = new Game();
        game.init(ip, ip, player, null, new CardCollection(), new CardCollection());

        assertTrue(player.letPlayerSpeak());

        // Check that no interaction occurred with the user interface
        verifyNoInteractions(ip);
    }

    @Test
    public void testLetPlayerSpeak_ReturnsTrue_WhenPlayerSpeaksAndAddsACard() {
        UserInterface ip = mock(UserInterface.class);
        Player player = mock(Player.class);
        Card card = mock(Card.class);

        when(player.getUserInterface()).thenReturn(ip);
        when(ip.displayText("You cannot speak now.")).thenReturn(false);
        when(player.canSpeak()).thenReturn(true);

        Game game = new Game();
        game.init(ip, ip, player, null, new CardCollection(), new CardCollection());
        game.stack = new Stack();

        player.speak("Play a card.");
        game.stack.push(card);

        assertTrue(player.letPlayerSpeak());

        // Check that the card was resolved
        verify(card).resolve();
    }
}