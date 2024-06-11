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

 private static Game game;
 private static UserInterface ip1;
 private static UserInterface ip2;
 private static String name1 = "Player1";
 private static String name2 = "Player2";
 private static CardCollection deck1;
 private static CardCollection deck2;

 @Before
 public void setUp() {
 ip1 = new DummyUserInterface();
 ip2 = new DummyUserInterface();
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
 }

 @Test
 public void testGetPlayingPlayer() {
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 Player player = game.getPlayingPlayer();
 assertEquals(name1, player.getName());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 Player otherPlayer = game.getNonPlayingPlayer();
 assertEquals(name2, otherPlayer.getName());
 }

 @Test
 public void testGetOtherPlayer() {
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 Player player1 = game.getPlayingPlayer();
 Player player2 = game.getOtherPlayer(player1);
 assertEquals(name2, player2.getName());
 Player player2_1 = game.getOtherPlayer(player2);
 assertEquals(name1, player2_1.getName());
 }

 @Test
 public void testInit() {
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 assertEquals(2, game.getPlayers().size());
 assertTrue(game.getPlayingPlayer().isAlive());
 assertTrue(game.getNonPlayingPlayer().isAlive());
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 game.givePriorityToCurrentPlayer();
 assertTrue(game.getPlayingPlayer().hasPriority());
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayer_whenStackIsNotEmpty() {
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 game.stack.push(new Card("testCard"));
 game.givePriorityToCurrentPlayer();
 fail("Expected IllegalStateException but it did not occur");
 }
}