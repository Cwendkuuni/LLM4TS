package lotus.core.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.stack.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

 private UserInterface ip1, ip2;
 private Player player1, player2;
 private Game game;
 private CardCollection deck1, deck2;

 @Before
 public void setUp() {
 ip1 = new StubUserInterface();
 ip2 = new StubUserInterface();
 player1 = new Player(ip1, "Player1", new CardCollection());
 player2 = new Player(ip2, "Player2", new CardCollection());
 game = new Game();
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 }

 @Test
 public void testGetPlayingPlayer() {
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertEquals(player1, game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertEquals(player2, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertEquals(player2, game.getOtherPlayer(player1));
 assertEquals(player1, game.getOtherPlayer(player2));
 }

 @Test
 public void testInit() {
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertEquals(player1, game.getPlayingPlayer());
 assertEquals(player2, game.getNonPlayingPlayer());
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 game.givePriorityToCurrentPlayer();
 fail("Expected Stack resolution or player speak to complete");
 }

 @Test
 public void testGivePriorityToCurrentPlayer_EmptyStack() {
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 game.stack = new Stack();
 game.givePriorityToCurrentPlayer();
 fail("Expected Stack resolution or player speak to complete");
 }

 @Test
 public void testGivePriorityToCurrentPlayer_ResolveLast() {
 Card card1 = new Card("TestCard1", 0, new ArrayList<Ability>());
 Card card2 = new Card("TestCard2", 0, new ArrayList<Ability>());
 deck1.add(card1);
 deck1.add(card2);
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 game.stack.push(new StackEntry(card1, player1));
 game.stack.push(new StackEntry(card2, player2));
 game.givePriorityToCurrentPlayer();
 assertEquals(card1, game.stack.peek().card);
 }

 @Test
 public void testGivePriorityToCurrentPlayer_BothPlayersSpeak() {
 Game.playingPlayer = 1;
 ip1.respondWith("Speak");
 ip2.respondWith("Speak");
 game.givePriorityToCurrentPlayer();
 fail("Expected Stack resolution or player speak to complete");
 }

 @Test
 public void testGivePriorityToCurrentPlayer_APSpeaksFirst() {
 Game.playingPlayer = 1;
 ip1.respondWith("Speak");
 ip2.respondWith("Pass");
 game.givePriorityToCurrentPlayer();
 fail("Expected Stack resolution or player speak to complete");
 }

 @Test
 public void testGivePriorityToCurrentPlayer_NAPSpeaksFirst() {
 Game.playingPlayer = 2;
 ip2.respondWith("Speak");
 ip1.respondWith("Pass");
 game.givePriorityToCurrentPlayer();
 fail("Expected Stack resolution or player speak to complete");
 }

 @Test
 public void testGivePriorityToCurrentPlayer_APPasses() {
 Game.playingPlayer = 1;
 ip1.respondWith("Pass");
 ip2.respondWith("Speak");
 game.givePriorityToCurrentPlayer();
 fail("Expected Stack resolution or player speak to complete");
 }

 @Test
 public void testGivePriorityToCurrentPlayer_NAPPasses() {
 Game.playingPlayer = 2;
 ip2.respondWith("Pass");
 ip1.respondWith("Speak");
 game.givePriorityToCurrentPlayer();
 fail("Expected Stack resolution or player speak to complete");
 }

 @Test
 public void testGivePriorityToCurrentPlayer_APWins() {
 Game.playingPlayer = 1;
 ip1.respondWith("Attack");
 ip2.respondWith("Block");
 game.givePriorityToCurrentPlayer();
 fail("Expected Stack resolution or player speak to complete");
 }

 @Test
 public void testGivePriorityToCurrentPlayer_NAPWins() {
 Game.playingPlayer = 2;
 ip2.respondWith("Attack");
 ip1.respondWith("Block");
 game.givePriorityToCurrentPlayer();
 fail("Expected Stack resolution or player speak to complete");
 }
}