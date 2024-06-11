import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import lotus.core.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameTest {

 private static final String PLAYER1_NAME = "Player1";
 private static final String PLAYER2_NAME = "Player2";
 private static final Card COLORLESS_CARD = new Card("ColorlessCard", 0);
 private static final Card MANA_CARD = new Card("ManaCard", 1);
 private static final Card COST_MORE_CARD = new Card("CostMoreCard", 2);
 private static final Card COST_LESS_CARD = new Card("CostLessCard", -1);
 private static final CardCollection DECK1 = new ArrayList<>(Arrays.asList(MANA_CARD, COST_MORE_CARD, COST_LESS_CARD));
 private static final CardCollection DECK2 = new ArrayList<>(Arrays.asList(COLORLESS_CARD, MANA_CARD, COLORLESS_CARD));
 private static final UserInterface IP1 = mock(UserInterface.class);
 private static final UserInterface IP2 = mock(UserInterface.class);
 private Game game;

 @Before
 public void setup() {
 game = new Game();
 game.init(IP1, IP2, PLAYER1_NAME, PLAYER2_NAME, DECK1, DECK2);
 }

 @Test
 public void testGetPlayingPlayer() {
 Player expectedPlayer = player1;
 Player actualPlayer = game.getPlayingPlayer();
 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player expectedPlayer = player2;
 Player actualPlayer = game.getNonPlayingPlayer();
 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test
 public void testGetOtherPlayer() {
 Player expectedPlayer1 = player1;
 Player expectedPlayer2 = player2;
 Player actualPlayer1 = game.getOtherPlayer(expectedPlayer1);
 Player actualPlayer2 = game.getOtherPlayer(expectedPlayer2);
 assertEquals(expectedPlayer2, actualPlayer1);
 assertEquals(expectedPlayer1, actualPlayer2);
 }

 @Test
 public void testInitWithDecks() {
 List<Card> player1Cards = player1.getLibrary();
 List<Card> player2Cards = player2.getLibrary();
 List<Card> expectedPlayer1Cards = new ArrayList<>(Arrays.asList(MANA_CARD, COST_MORE_CARD, COST_LESS_CARD));
 List<Card> expectedPlayer2Cards = new ArrayList<>(Arrays.asList(COLORLESS_CARD, MANA_CARD, COLORLESS_CARD));
 assertEquals(expectedPlayer1Cards, player1Cards);
 assertEquals(expectedPlayer2Cards, player2Cards);
 }

 @Test
 public void testGivePriorityToCurrentPlayer_NoCardsInStack() {
 // Given
 Player player1 = game.getPlayingPlayer();
 Player player2 = game.getNonPlayingPlayer();

 // When
 game.givePriorityToCurrentPlayer();

 // Then
 // No card should be resolved
 assertFalse(game.stack.hasCards());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_APSpeaksFirst() {
 // Given
 when(player1.letPlayerSpeak()).thenReturn(true);
 when(player2.letPlayerSpeak()).thenReturn(false);

 // When
 game.givePriorityToCurrentPlayer();

 // Then
 // The playing player has spoken
 assertTrue(player1.hasSpoken());
 // The non-playing player has not spoken
 assertFalse(player2.hasSpoken());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_NAPSpeaksFirst() {
 // Given
 when(player1.letPlayerSpeak()).thenReturn(false);
 when(player2.letPlayerSpeak()).thenReturn(true);

 // When
 game.givePriorityToCurrentPlayer();

 // Then
 // The non-playing player has spoken
 assertTrue(player2.hasSpoken());
 // The playing player has not spoken
 assertFalse(player1.hasSpoken());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_StackContainsCard() {
 // Given
 game.stack.push(MANA_CARD);

 // When
 game.givePriorityToCurrentPlayer();

 // Then
 // The last card in the stack should be resolved
 assertEquals(MANA_CARD, game.stack.peek());
 // The game should call givePriorityToCurrentPlayer again because there are still cards in the stack
 game.givePriorityToCurrentPlayer();
 }
}