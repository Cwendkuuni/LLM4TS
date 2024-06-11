package lotus.core.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import lotus.core.Game;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.Player;
import lotus.core.card.Card;
import lotus.core.stack.Stack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameTest {

 private static UserInterface ip1;
 private static UserInterface ip2;
 private static Player player1;
 private static Player player2;
 private static Game game;

 @Before
 public void setUp() {
 ip1 = new DummyUserInterface();
 ip2 = new DummyUserInterface();
 player1 = new Player(ip1, "Player1", new ArrayList<>());
 player2 = new Player(ip2, "Player2", new ArrayList<>());
 game = new Game();
 }

 @Test
 public void testGetPlayingPlayer() {
 // Test when playing player is player1
 game.init(ip1, ip2, "Player1", "Player2", new ArrayList<>(), new ArrayList<>());
 assertEquals(player1, game.getPlayingPlayer());

 // Test when playing player is player2
 game.init(ip2, ip1, "Player2", "Player1", new ArrayList<>(), new ArrayList<>());
 assertEquals(player2, game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 // Test when non-playing player is player1
 game.init(ip1, ip2, "Player1", "Player2", new ArrayList<>(), new ArrayList<>());
 assertEquals(player2, game.getNonPlayingPlayer());

 // Test when non-playing player is player2
 game.init(ip2, ip1, "Player2", "Player1", new ArrayList<>(), new ArrayList<>());
 assertEquals(player1, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 // Test when other player is player1
 game.init(ip1, ip2, "Player1", "Player2", new ArrayList<>(), new ArrayList<>());
 assertEquals(player2, game.getOtherPlayer(player1));
 assertEquals(player1, game.getOtherPlayer(player2));

 // Test when other player is player2
 game.init(ip2, ip1, "Player2", "Player1", new ArrayList<>(), new ArrayList<>());
 assertEquals(player1, game.getOtherPlayer(player2));
 assertEquals(player2, game.getOtherPlayer(player1));
 }

 @Test
 public void testInit() {
 // Test initializing game with player1's deck and player2's deck
 List<Card> deck1 = new ArrayList<>();
 deck1.add(new Card("Card1", "Card1"));
 deck1.add(new Card("Card2", "Card2"));
 List<Card> deck2 = new ArrayList<>();
 deck2.add(new Card("Card3", "Card3"));
 deck2.add(new Card("Card4", "Card4"));

 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertEquals(player1, game.getPlayingPlayer());
 assertEquals(player2, game.getNonPlayingPlayer());
 assertEquals(2, player1.library.size());
 assertEquals(2, player2.library.size());

 // Test initializing game with player2's deck and player1's deck
 Collections.reverse(deck1);
 Collections.reverse(deck2);
 game.init(ip2, ip1, "Player2", "Player1", deck1, deck2);
 assertEquals(player2, game.getPlayingPlayer());
 assertEquals(player1, game.getNonPlayingPlayer());
 assertEquals(2, player1.library.size());
 assertEquals(2, player2.library.size());
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 // Test when no cards are on stack
 List<Card> deck1 = new ArrayList<>();
 List<Card> deck2 = new ArrayList<>();
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 game.givePriorityToCurrentPlayer();
 assertTrue(game.stack.isEmpty());

 // Test when there is a card on stack for player1
 deck1.add(new Card("Card1", "Card1"));
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 game.stack.push(new Card("Card1", "Card1"));
 game.givePriorityToCurrentPlayer();
 assertTrue(game.stack.isEmpty());

 // Test when there is a card on stack for player2
 deck2.add(new Card("Card2", "Card2"));
 game.init(ip2, ip1, "Player2", "Player1", deck1, deck2);
 game.stack.push(new Card("Card2", "Card2"));
 game.givePriorityToCurrentPlayer();
 assertFalse(game.stack.isEmpty());
 assertEquals(player1, game.getPlayingPlayer());

 // Test when there are cards on stack for both players
 deck1.add(new Card("Card1", "Card1"));
 deck2.add(new Card("Card2", "Card2"));
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 game.stack.push(new Card("Card1", "Card1"));
 game.stack.push(new Card("Card2", "Card2"));
 game.givePriorityToCurrentPlayer();
 assertFalse(game.stack.isEmpty());
 assertEquals(player2, game.getPlayingPlayer());
 }
}