package lotus.core.test;

import static org.junit.Assert.*;

import lotus.core.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class GameTest {

 private UserInterface ip1;
 private UserInterface ip2;
 private String name1 = "Player1";
 private String name2 = "Player2";
 private CardCollection deck1;
 private CardCollection deck2;
 private Game game;

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
 // Test initializing the game with valid arguments
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 // Check if players and user interfaces are initialized
 assertEquals(name1, game.player1.getName());
 assertEquals(name2, game.player2.getName());
 // Check if player decks are assigned to the correct players
 assertEquals(deck1, game.player1.getDeck());
 assertEquals(deck2, game.player2.getDeck());
 // Check if player libraries are initialized
 assertNotNull(game.player1.getLibrary());
 assertNotNull(game.player2.getLibrary());
 // Check if current phase is set to UntapPhase
 assertEquals(game.currentPhase.getClass(), UntapPhase.class);
 }

 @Test
 public void testGetPlayingPlayer() {
 // Test getting the playing player
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 game.playingPlayer = 1;
 assertTrue(game.getPlayingPlayer() instanceof Player);
 assertEquals(game.player1, game.getPlayingPlayer());
 // Test getting the non-playing player
 game.playingPlayer = 2;
 assertTrue(game.getNonPlayingPlayer() instanceof Player);
 assertEquals(game.player2, game.getNonPlayingPlayer());
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 // Test letting players speak and resolving the stack
 // (Note: DummyUserInterface does not implement letPlayerSpeak() method,
 // so this test may not pass if you use it as is. Please implement
 // this method in a real UserInterface class for proper testing.)
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 Card card1 = new Card("TestCard1", CardType.ACTION);
 Card card2 = new Card("TestCard2", CardType.ACTION);
 deck1.add(card1);
 deck1.add(card2);
 deck2.add(new Card("TestCard3", CardType.ACTION));
 game.player1.hand.add(card1);
 game.player2.hand.add(card2);
 game.player1.library.add(card2);
 game.player2.library.add(card1);
 game.givePriorityToCurrentPlayer();
 // Check if players' libraries are empty after giving priority
 assertTrue(game.player1.getLibrary().isEmpty());
 assertTrue(game.player2.getLibrary().isEmpty());
 // Test giving priority again
 game.givePriorityToCurrentPlayer();
 // Check if stack is empty after resolving last card
 assertTrue(game.stack.isEmpty());
 }

}