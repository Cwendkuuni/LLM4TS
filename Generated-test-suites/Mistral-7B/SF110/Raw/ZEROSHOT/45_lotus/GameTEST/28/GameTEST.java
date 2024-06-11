package lotus.core.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import lotus.core.*;
import lotus.core.card.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GameTest {

 @Before
 public void setUp() {
 player1 = Mockito.mock(Player.class);
 player2 = Mockito.mock(Player.class);
 userInterface1 = Mockito.mock(UserInterface.class);
 userInterface2 = Mockito.mock(UserInterface.class);
 String name1 = "Player 1";
 String name2 = "Player 2";
 CardCollection deck1 = new ArrayList<>();
 CardCollection deck2 = new ArrayList<>();

 Game.init(userInterface1, userInterface2, player1, name1, deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 assertEquals(player1, Game.getPlayingPlayer());
 Game.playingPlayer = 2;
 assertEquals(player2, Game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 assertEquals(player2, Game.getNonPlayingPlayer());
 Game.playingPlayer = 1;
 assertEquals(player1, Game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 assertEquals(player2, Game.getOtherPlayer(player1));
 assertEquals(player1, Game.getOtherPlayer(player2));
 }

 @Test
 public void testInit() {
 assertTrue(Game.player1 instanceof Player);
 assertTrue(Game.player2 instanceof Player);
 assertEquals(userInterface1, Game.player1.getUserInterface());
 assertEquals(userInterface2, Game.player2.getUserInterface());
 assertTrue(Game.player1.getName().equals(name1));
 assertTrue(Game.player2.getName().equals(name2));
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 Mockito.doReturn(true).when(player1).letPlayerSpeak();
 Mockito.doReturn(false).when(player2).letPlayerSpeak();

 // Test priority for Player 1
 Game.givePriorityToCurrentPlayer();
 Mockito.verify(player1, Mockito.times(2)).letPlayerSpeak();
 Mockito.verify(player2, Mockito.times(1)).letPlayerSpeak();

 // Test priority for Player 2
 Game.playingPlayer = 2;
 Mockito.doReturn(false).when(player1).letPlayerSpeak();
 Mockito.doReturn(true).when(player2).letPlayerSpeak();

 Game.givePriorityToCurrentPlayer();
 Mockito.verify(player1, Mockito.times(1)).letPlayerSpeak();
 Mockito.verify(player2, Mockito.times(2)).letPlayerSpeak();
 }

 @Test
 public void testInitWithEmptyDecks() {
 Game.init(userInterface1, userInterface2, player1, name1, new ArrayList<>(), new ArrayList<>());
 assertNull(Game.player1.library);
 assertNull(Game.player2.library);
 }

 @Test
 public void testInitWithCardsInDecks() {
 Card card1 = Mockito.mock(Card.class);
 Card card2 = Mockito.mock(Card.class);

 Game.init(userInterface1, userInterface2, player1, name1, Arrays.asList(card1), Arrays.asList(card2));
 assertTrue(Game.player1.library.contains(card1));
 assertTrue(Game.player2.library.contains(card2));
 }

 @Test(expected = IllegalStateException.class)
 public void testInitWithMoreThanTwoPlayers() {
 UserInterface IP3 = Mockito.mock(UserInterface.class);
 String name3 = "Player 3";
 Player p3 = new Player(IP3, name3, new ArrayList<>());

 Game.init(userInterface1, userInterface2, userInterface3, player1, name1, deck1, name3, deck2);
 // exception is thrown in the init method of the Game class
 }
}