package lotus.core.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import lotus.core.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameTest {

 @Before
 public void setUp() {
 userInterface1 = mock(UserInterface.class);
 userInterface2 = mock(UserInterface.class);
 player1 = new Player(userInterface1, "Player1", new CardCollection());
 player2 = new Player(userInterface2, "Player2", new CardCollection());
 Game.init(userInterface1, userInterface2, "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 }

 @Test
 public void testGetPlayingPlayer() {
 assertSame(player1, Game.getPlayingPlayer());
 Game.playingPlayer = 2;
 assertSame(player2, Game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 assertSame(player2, Game.getNonPlayingPlayer());
 Game.playingPlayer = 1;
 assertSame(player1, Game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 assertSame(player2, Game.getOtherPlayer(player1));
 assertSame(player1, Game.getOtherPlayer(player2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullUserInterfaces() {
 try {
 Game.init(null, userInterface2, "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Should throw IllegalArgumentException", e);
 }
 Game.init(userInterface1, null, "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullNames() {
 try {
 Game.init(userInterface1, userInterface2, null, null, new CardCollection(), new CardCollection());
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Should throw IllegalArgumentException", e);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullDecks() {
 try {
 Game.init(userInterface1, userInterface2, "Player1Name", "Player2Name", null, null);
 } catch (NullPointerException e) {
 throw new AssertionError("Should throw NullPointerException", e);
 }
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 // Ensure the stack is empty before testing
 Game.stack = new Stack();
 List<Card> player1Cards = new ArrayList<>(Arrays.asList(new Card("Card1"), new Card("Card2"), new Card("Card3")));
 List<Card> player2Cards = new ArrayList<>(Arrays.asList(new Card("Card4"), new Card("Card5"), new Card("Card6")));
 player1.library.addAll(player1Cards);
 player2.library.addAll(player2Cards);
 when(userInterface1.init(player1)).thenReturn(true);
 when(userInterface2.init(player2)).thenReturn(true);

 // Test player1's turn
 Game.playingPlayer = 1;
 Game.givePriorityToCurrentPlayer();
 assertTrue(player1.letPlayerSpeak()); // Player speaks
 assertTrue(player2.letPlayerSpeak()); // Player speaks
 assertEquals(0, Game.stack.size()); // No cards on stack

 // Test player2's turn
 Game.playingPlayer = 2;
 when(player1.letPlayerSpeak()).thenReturn(true); // Allow player1 to speak during player2's turn
 when(player2.letPlayerSpeak()).thenReturn(false); // Player2 passes
 Game.givePriorityToCurrentPlayer();
 assertFalse(player1.letPlayerSpeak()); // Player1 no longer speaks
 assertFalse(player2.letPlayerSpeak()); // Player2 no longer speaks
 assertTrue(Game.stack.empty()); // No cards on stack

 // Test resolving a card during player2's turn
 Card card1 = player1.library.get(0);
 Card card2 = player2.library.get(0);
 Game.stack.push(card1);
 Game.stack.push(card2);
 Game.givePriorityToCurrentPlayer();
 assertEquals("Card1", Game.stack.peek().name); // Check the card at the top of the stack
 Game.stack.resolveLast();
 assertEquals(Collections.singletonList(card1), player1.hand.getCards()); // Check that card1 is now in player1's hand
 assertEquals(Collections.singletonList(card2), player2.hand.getCards()); // Check that card2 is now in player2's hand
 Game.givePriorityToCurrentPlayer();
 }

 private UserInterface userInterface1;
 private UserInterface userInterface2;
 private Player player1;
 private Player player2;
}