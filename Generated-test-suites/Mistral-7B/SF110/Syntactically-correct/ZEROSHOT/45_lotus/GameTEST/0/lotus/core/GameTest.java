package lotus.core.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import lotus.core.Game;
import lotus.core.Player;
import lotus.core.stack.Stack;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.card.Card;
import lotus.core.card.CardCollection;

public class GameTest {

 private UserInterface ip1;
 private UserInterface ip2;
 private String name1 = "Player1";
 private String name2 = "Player2";
 private CardCollection deck1;
 private CardCollection deck2;
 private Player player1;
 private Player player2;
 private Game game;

 @Before
 public void setUp() {
 ip1 = new DummyUserInterface();
 ip2 = new DummyUserInterface();
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 player1 = new Player(ip1, name1, deck1);
 player2 = new Player(ip2, name2, deck2);
 game = new Game();
 }

 @Test
 public void testInit() {
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 assertEquals(player1, game.getPlayingPlayer());
 assertEquals(player2, game.getNonPlayingPlayer());
 assertEquals(2, game.getPlayers().size());
 }

 @Test
 public void testGetPlayingPlayer() {
 assertEquals(player1, game.getPlayingPlayer());
 game.setPlayingPlayer(2);
 assertEquals(player2, game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 assertEquals(player2, game.getNonPlayingPlayer());
 game.setPlayingPlayer(1);
 assertEquals(player1, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 assertEquals(player2, game.getOtherPlayer(player1));
 assertEquals(player1, game.getOtherPlayer(player2));
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 game.givePriorityToCurrentPlayer();
 assertFalse(game.getStack().empty());
 game.givePriorityToCurrentPlayer();
 assertTrue(game.getStack().empty());
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayerEmptyStack() {
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer(); // this one should throw an exception
 }

 @Test
 public void testInitWithEmptyDecks() {
 game.init(ip1, ip2, name1, name2, new CardCollection(), new CardCollection());
 assertEquals(0, game.getPlayers().size());
 }

 @Test
 public void testInitWithNullUserInterface() {
 try {
 game.init(null, ip2, name1, name2, deck1, deck2);
 fail("Should throw an exception");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testInitWithNullName() {
 try {
 game.init(ip1, ip2, null, name2, deck1, deck2);
 fail("Should throw an exception");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testInitWithNullDeck() {
 try {
 game.init(ip1, ip2, name1, name2, null, deck2);
 fail("Should throw an exception");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testInitWithNullDeck2() {
 try {
 game.init(ip1, ip2, name1, name2, deck1, null);
 fail("Should throw an exception");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testStack() {
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 Card c = new Card("TestCard", "TestType");
 c.owner = player1;
 game.getStack().push(c);
 assertEquals(player1, game.getPlayingPlayer());
 game.givePriorityToCurrentPlayer();
 assertEquals(c, game.getStack().peek());
 game.givePriorityToCurrentPlayer();
 assertEquals(player2, game.getPlayingPlayer());
 assertEquals(null, game.getStack().peek());
 }

 @Test
 public void testStackEmpty() {
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 assertEquals(player2, game.getPlayingPlayer());
 assertEquals(null, game.getStack().peek());
 }

 @Test
 public void testStackResolution() {
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 Card c1 = new Card("TestCard1", "TestType1");
 Card c2 = new Card("TestCard2", "TestType2");
 c1.owner = player1;
 c2.owner = player2;
 game.getStack().push(c1);
 game.getStack().push(c2);
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 assertEquals(c2, game.getStack().peek());
 game.givePriorityToCurrentPlayer();
 assertEquals(null, game.getStack().peek());
 }

 @Test
 public void testStackResolutionWithOneCard() {
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 Card c = new Card("TestCard", "TestType");
 c.owner = player1;
 game.getStack().push(c);
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 assertEquals(c, game.getStack().peek());
 game.givePriorityToCurrentPlayer();
 assertEquals(null, game.getStack().peek());
 }

 @Test
 public void testStackResolutionWithEmptyStack() {
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 assertEquals(null, game.getStack().peek());
 }
}