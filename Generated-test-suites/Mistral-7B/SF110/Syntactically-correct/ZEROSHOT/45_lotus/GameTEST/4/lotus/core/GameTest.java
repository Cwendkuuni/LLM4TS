package lotus.core.test;

import static org.junit.Assert.*;

import lotus.core.Game;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.Player;
import lotus.core.Stack;
import lotus.core.card.Card;
import lotus.core.card.CardCollection;

import org.junit.Before;
import org.junit.Test;

public class GameTest {

 private static UserInterface ip1;
 private static UserInterface ip2;
 private static String name1 = "Player1";
 private static String name2 = "Player2";
 private static CardCollection deck1;
 private static CardCollection deck2;
 private static Game game;

 @Before
 public void setUp() {
 ip1 = new DummyUserInterface();
 ip2 = new DummyUserInterface();
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
 }

 @Test
 public void testInit() {
 // Test initializing game with valid inputs
 Card testCard1 = new Card("TestCard1");
 Card testCard2 = new Card("TestCard2");
 deck1.addCard(testCard1);
 deck2.addCard(testCard2);
 game.init(ip1, ip2, name1, name2, deck1, deck2);

 assertEquals(Game.player1, game.getPlayingPlayer());
 assertEquals(Game.player2, game.getNonPlayingPlayer());
 assertTrue(game.getPlayingPlayer().getName().equals(name1));
 assertTrue(game.getNonPlayingPlayer().getName().equals(name2));

 // Test initializing game with null inputs
 assertNull(game.getPlayingPlayer(), () -> game.init(null, ip2, name1, name2, null, deck2));
 assertNull(game.getNonPlayingPlayer(), () -> game.init(ip1, null, name1, name2, deck1, null));
 }

 @Test
 public void testGetPlayingPlayer() {
 // Test getting playing player
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 assertEquals(Game.player1, game.getPlayingPlayer());

 // Test getting playing player during other player's turn
 game.givePriorityToCurrentPlayer();
 assertEquals(Game.player2, game.getPlayingPlayer());
 assertEquals(Game.player1, game.getOtherPlayer(game.getPlayingPlayer()));
 }

 @Test
 public void testGetOtherPlayer() {
 // Test getting other player
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 assertEquals(Game.player2, game.getOtherPlayer(Game.player1));
 assertEquals(Game.player1, game.getOtherPlayer(Game.player2));
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 // Test giving priority to current player
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 game.givePriorityToCurrentPlayer();
 assertTrue(Game.stack.isEmpty());

 // Test giving priority to player after cards are put on stack
 Card testCard3 = new Card("TestCard3");
 Card testCard4 = new Card("TestCard4");
 deck1.addCard(testCard3);
 deck2.addCard(testCard4);
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 game.givePriorityToCurrentPlayer();
 assertFalse(Game.stack.isEmpty());
 assertEquals(testCard3, Game.stack.peek());
 game.givePriorityToCurrentPlayer();
 assertEquals(testCard4, Game.stack.peek());
 }
}