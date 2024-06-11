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

 private Game game;
 private UserInterface ui1;
 private UserInterface ui2;
 private Player player1;
 private Player player2;
 private CardCollection deck1;
 private CardCollection deck2;

 @Before
 public void setUp() {
 ui1 = new DummyUserInterface();
 ui2 = new DummyUserInterface();
 player1 = new Player(ui1, "Player1", new CardCollection());
 player2 = new Player(ui2, "Player2", new CardCollection());
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
 Game.init(ui1, ui2, "Player1", "Player2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 Player expectedPlayer = player1;
 Player actualPlayer = game.getPlayingPlayer();
 assertTrue(expectedPlayer.getName().equals(actualPlayer.getName()));
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player expectedPlayer = player2;
 Player actualPlayer = game.getNonPlayingPlayer();
 assertTrue(expectedPlayer.getName().equals(actualPlayer.getName()));
 }

 @Test
 public void testGetOtherPlayer() {
 Player expectedPlayer1 = player1;
 Player expectedPlayer2 = player2;
 Player actualPlayer1 = game.getOtherPlayer(expectedPlayer1);
 Player actualPlayer2 = game.getOtherPlayer(expectedPlayer2);
 assertTrue(expectedPlayer2.getName().equals(actualPlayer1.getName()));
 assertTrue(expectedPlayer1.getName().equals(actualPlayer2.getName()));
 }

 @Test
 public void testInit() {
 assertEquals(player1, game.getPlayingPlayer());
 assertEquals(player2, game.getNonPlayingPlayer());
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 game.givePriorityToCurrentPlayer();
 assertTrue(player1.letPlayerSpeak());
 game.givePriorityToCurrentPlayer();
 assertTrue(player2.letPlayerSpeak());
 game.givePriorityToCurrentPlayer();
 assertTrue(player1.letPlayerSpeak());
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayerWhenStackIsNotEmpty() {
 game.init(ui1, ui2, "Player1", "Player2", deck1, deck2);
 game.stack.push(new Card("TestCard", ElementType.CREATURE));
 game.givePriorityToCurrentPlayer();
 fail("Expected IllegalStateException when stack is not empty");
 }
}