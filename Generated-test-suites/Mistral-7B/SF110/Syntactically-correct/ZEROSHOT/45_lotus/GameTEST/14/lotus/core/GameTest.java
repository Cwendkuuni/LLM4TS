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
 private Player player1;
 private Player player2;
 private Game game;
 private CardCollection deck1;
 private CardCollection deck2;

 @Before
 public void setUp() {
 ip1 = new DummyUserInterface();
 ip2 = new DummyUserInterface();
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
 assertTrue(game.getPlayingPlayer().letPlayerSpeak());
 assertTrue(game.getNonPlayingPlayer().letPlayerSpeak());
 }

 @Test(expected = RuntimeException.class)
 public void testGivePriorityToCurrentPlayerStackNotEmpty() {
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 game.stack.push(new Card("TestCard", "TestType"));
 game.givePriorityToCurrentPlayer();
 fail("Should throw RuntimeException when stack is not empty");
 }
}