package lotus.core.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import lotus.core.*;
import static org.junit.Assert.*;
import java.util.*;

public class GameTest {

 private UserInterface mockIP1;
 private UserInterface mockIP2;
 private Player player1;
 private Player player2;
 private Game game;
 private CardCollection deck1;
 private CardCollection deck2;

 @Before
 public void setUp() {
 mockIP1 = Mockito.mock(UserInterface.class);
 mockIP2 = Mockito.mock(UserInterface.class);
 player1 = new Player(mockIP1, "player1", new CardCollection());
 player2 = new Player(mockIP2, "player2", new CardCollection());
 game = new Game();
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 }

 @Test
 public void testGetPlayingPlayer() {
 game.init(mockIP1, mockIP2, "player1", "player2", deck1, deck2);
 assertEquals(player1, game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 game.init(mockIP1, mockIP2, "player1", "player2", deck1, deck2);
 assertEquals(player2, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 game.init(mockIP1, mockIP2, "player1", "player2", deck1, deck2);
 assertEquals(player2, game.getOtherPlayer(player1));
 assertEquals(player1, game.getOtherPlayer(player2));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetPlayingPlayerBeforeInit() {
 assertNull(game.getPlayingPlayer());
 game.givePriorityToCurrentPlayer();
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 game.init(mockIP1, mockIP2, "player1", "player2", deck1, deck2);
 Mockito.doReturn(true).when(player1).letPlayerSpeak();
 Mockito.doReturn(true).when(player2).letPlayerSpeak();
 game.givePriorityToCurrentPlayer();
 Mockito.verify(player1).letPlayerSpeak();
 Mockito.verify(player2).letPlayerSpeak();
 }

 @Test
 public void testResolveLastCard() {
 game.init(mockIP1, mockIP2, "player1", "player2", deck1, deck2);
 Card card = new Card();
 game.stack.push(card);
 game.givePriorityToCurrentPlayer();
 Mockito.verify(card).resolve();
 }

 @Test
 public void testResolveLastCardWhenStackIsEmpty() {
 game.init(mockIP1, mockIP2, "player1", "player2", deck1, deck2);
 game.givePriorityToCurrentPlayer();
 Mockito.verifyNoMoreInteractions(game.stack);
 }
}