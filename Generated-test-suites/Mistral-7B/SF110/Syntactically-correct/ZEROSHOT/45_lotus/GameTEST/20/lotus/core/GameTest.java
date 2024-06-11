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
 private static String name1 = "Player1";
 private static String name2 = "Player2";
 private static CardCollection deck1;
 private static CardCollection deck2;
 private static Game game;

 @Before
 public void setUp() {
 ip1 = new StubUserInterface();
 ip2 = new StubUserInterface();
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
 }

 @Test
 public void testGetPlayingPlayer() {
 Player player = game.getPlayingPlayer();
 assertEquals(player, game.player1);
 game.playingPlayer = 2;
 player = game.getPlayingPlayer();
 assertEquals(player, game.player2);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player player1 = game.getPlayingPlayer();
 Player player2 = game.getNonPlayingPlayer();
 assertEquals(player1, game.player2);
 assertEquals(player2, game.player1);
 }

 @Test
 public void testInit() {
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 assertEquals(game.player1, Player.getActivePlayer(game));
 assertEquals(game.player2, Player.getOtherPlayer(game.player1));
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 Card card1 = new Card();
 Card card2 = new Card();
 deck1.add(card1);
 deck2.add(card2);
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 game.givePriorityToCurrentPlayer();
 assertTrue(card1.isInPlayingArea());
 assertTrue(card2.isInLibrary());
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayerWithEmptyStack() {
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 game.givePriorityToCurrentPlayer();
 game.stack.clear();
 game.givePriorityToCurrentPlayer();
 }

}