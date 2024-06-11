package lotus.core.test;

import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.stack.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

 private static UserInterface dummyUI1;
 private static UserInterface dummyUI2;
 private static String player1Name;
 private static String player2Name;
 private static CardCollection deck1;
 private static CardCollection deck2;
 private static Player player1;
 private static Player player2;
 private static Game game;

 @Test
 public void testInit() {
  dummyUI1 = new DummyUI();
  dummyUI2 = new DummyUI();
  player1Name = "Player1";
  player2Name = "Player2";
  deck1 = new CardCollection();
  deck2 = new CardCollection();

  game = new Game();
  game.init(dummyUI1, dummyUI2, player1Name, player2Name, deck1, deck2);

  assertEquals(player1Name, game.getPlayingPlayer().getName());
  assertEquals(player2Name, game.getNonPlayingPlayer().getName());
 }

 @Test
 public void testGetPlayingPlayer() {
  game = new Game();
  game.init(dummyUI1, dummyUI2, player1Name, player2Name, deck1, deck2);

  assertEquals(player1, game.getPlayingPlayer());
  assertEquals(player2, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
  game = new Game();
  game.init(dummyUI1, dummyUI2, player1Name, player2Name, deck1, deck2);

  assertEquals(player2, game.getOtherPlayer(player1));
  assertEquals(player1, game.getOtherPlayer(player2));
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
  game = new Game();
  Player p1 = new Player(dummyUI1, player1Name, deck1);
  Player p2 = new Player(dummyUI2, player2Name, deck2);
  game.player1 = p1;
  game.player2 = p2;

  Card c1 = new Card("TestCard1");
  Card c2 = new Card("TestCard2");

  p1.library.push(c1);
  p2.library.push(c2);

  game.init(dummyUI1, dummyUI2, player1Name, player2Name, deck1, deck2);

  game.givePriorityToCurrentPlayer();

  assertTrue(p1.isSpeaking());
  assertFalse(p2.isSpeaking());

  game.givePriorityToCurrentPlayer();

  assertTrue(p2.isSpeaking());
  assertFalse(p1.isSpeaking());

  game.stack.push(new Ability());

  game.givePriorityToCurrentPlayer();

  game.stack.resolveLast();

  game.givePriorityToCurrentPlayer();
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayerWithEmptyStack() {
  game = new Game();
  Player p1 = new Player(dummyUI1, player1Name, deck1);
  Player p2 = new Player(dummyUI2, player2Name, deck2);
  game.player1 = p1;
  game.player2 = p2;

  game.init(dummyUI1, dummyUI2, player1Name, player2Name, deck1, deck2);

  game.givePriorityToCurrentPlayer();

  assertFalse(p1.isSpeaking());
  assertFalse(p2.isSpeaking());
 }

 @Before
 public void setUp() {
  dummyUI1 = new DummyUI();
  dummyUI2 = new DummyUI();
  player1Name = "Player1";
  player2Name = "Player2";
  deck1 = new CardCollection();
  deck2 = new CardCollection();
 }

}