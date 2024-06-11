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

 private static UserInterface ip1;
 private static UserInterface ip2;
 private static Player player1;
 private static Player player2;
 private static CardCollection deck1;
 private static CardCollection deck2;
 private static Game game;

 @Before
 public void setUp() {
 ip1 = new DummyUserInterface();
 ip2 = new DummyUserInterface();
 player1 = new Player(ip1, "Player1", new CardCollection());
 player2 = new Player(ip2, "Player2", new CardCollection());
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
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
 }

 @Test
 public void testInit() {
 Card card1 = new Card("TestCard1", CardType.ACTION, new ArrayList<>());
 Card card2 = new Card("TestCard2", CardType.ACTION, new ArrayList<>());
 deck1.add(card1);
 deck1.add(card2);
 deck2.add(new Card("TestCard3", CardType.ACTION, new ArrayList<>()));

 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertEquals(player1, game.getPlayingPlayer());
 assertEquals(player2, game.getNonPlayingPlayer());
 assertEquals(card1, player1.library.get(0));
 assertEquals(card2, player1.library.get(1));
 assertEquals(card2, player2.library.get(0));
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 game.givePriorityToCurrentPlayer();
 assertTrue(game.stack.empty());
 }

 @Test
 public void testGivePriorityToCurrentPlayerWithCardsOnStack() {
 Card card1 = new Card("TestCard1", CardType.ACTION, new ArrayList<>());
 Card card2 = new Card("TestCard2", CardType.ACTION, new ArrayList<>());
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 game.player1.library.add(card1);
 game.player2.library.add(card2);
 game.stack.push(card1);
 game.givePriorityToCurrentPlayer();
 assertEquals(card2, game.stack.peek());
 }
}