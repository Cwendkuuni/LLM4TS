package lotus.core.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

 private Game game;
 private UserInterface ip1;
 private UserInterface ip2;
 private Player player1;
 private Player player2;
 private CardCollection deck1;
 private CardCollection deck2;

 @Before
 public void setUp() {
 ip1 = new TestUserInterface();
 ip2 = new TestUserInterface();
 player1 = new Player(ip1, "Player 1", new CardCollection());
 player2 = new Player(ip2, "Player 2", new CardCollection());
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
 game.init(ip1, ip2, "Player 1", "Player 2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 Player playingPlayer = game.getPlayingPlayer();
 assertEquals(player1, playingPlayer);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player nonPlayingPlayer = game.getNonPlayingPlayer();
 assertEquals(player2, nonPlayingPlayer);
 }

 @Test
 public void testGetOtherPlayer() {
 Player otherPlayer = game.getOtherPlayer(player1);
 assertEquals(player2, otherPlayer);
 otherPlayer = game.getOtherPlayer(player2);
 assertEquals(player1, otherPlayer);
 }

 @Test
 public void testInit() {
 assertEquals(game.player1, player1);
 assertEquals(game.player2, player2);
 assertEquals(game.getPlayingPlayer(), player1);
 assertEquals(game.getNonPlayingPlayer(), player2);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 game.givePriorityToCurrentPlayer();
 assertTrue(game.stack.empty());

 player1.addCardToHand(new Card("Test Card", "Test Type"));
 player2.addCardToHand(new Card("Test Card", "Test Type"));
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();

 Card testCard1 = player1.getCardFromHand(0);
 Card testCard2 = player2.getCardFromHand(0);
 Card resolvedCard = game.stack.peek();

 assertEquals(testCard1.name, resolvedCard.name);
 assertNull(player1.getCardFromHand(0));
 assertNull(player2.getCardFromHand(0));
 assertEquals(player2, game.getPlayingPlayer());
 }

 @Test(expected = AssertionError.class)
 public void testGivePriorityToCurrentPlayerWithEmptyStack() {
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 game.stack.pop();
 game.givePriorityToCurrentPlayer();
 }
}