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

 private UserInterface ip1;
 private UserInterface ip2;
 private Game game;
 private Player player1;
 private Player player2;
 private CardCollection deck1;
 private CardCollection deck2;

 @Before
 public void setUp() {
 ip1 = new DummyUserInterface();
 ip2 = new DummyUserInterface();
 game = new Game();
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
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
 List<Card> player1Deck = player1.getDeck();
 List<Card> player2Deck = player2.getDeck();
 assertEquals(player1, game.getPlayingPlayer());
 assertEquals(player2, game.getNonPlayingPlayer());
 assertEquals(60, player1Deck.size());
 assertEquals(60, player2Deck.size());
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 game.givePriorityToCurrentPlayer();
 assertTrue(game.currentPhase instanceof UntapPhase);
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayerWithEmptyStack() {
 game.stack = new Stack();
 game.givePriorityToCurrentPlayer();
 fail("Exception not thrown");
 }

 @Test
 public void testGivePriorityToCurrentPlayerWithNonEmptyStack() {
 Card card1 = new Card();
 Card card2 = new Card();
 game.stack.push(card1);
 game.stack.push(card2);
 game.givePriorityToCurrentPlayer();
 assertTrue(game.stack.size() == 0);
 }
}