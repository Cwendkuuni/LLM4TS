package lotus.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import lotus.core.interfaces.*;
import org.mockito.Mockito;
import org.junit.Test;

public class GameTest {

 Player player1, player2;
 UserInterface ip1, ip2;
 CardCollection deck1, deck2;
 Game game;

 @Before
 public void setup() {
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 player1 = new Player(ip1, "Player1", null);
 player2 = new Player(ip2, "Player2", null);
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 assertEquals(player1, game.getPlayingPlayer());
 game.playingPlayer = 2;
 assertEquals(player2, game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 assertEquals(player2, game.getNonPlayingPlayer());
 game.playingPlayer = 1;
 assertEquals(player1, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 assertEquals(player2, game.getOtherPlayer(player1));
 assertEquals(player1, game.getOtherPlayer(player2));
 }

 @Test
 public void testInit() {
 assertNotNull(game.combat);
 assertNotNull(game.currentPhase);
 assertEquals(player1, game.player1);
 assertEquals(player2, game.player2);
 assertEquals(ip1, game.player1.ui);
 assertEquals(ip2, game.player2.ui);
 assertNotNull(game.stack);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 // Test empty stack
 game.givePriorityToCurrentPlayer();
 assertTrue(game.stack.isEmpty());

 // Test non-empty stack
 Card card = new Card("TestCard", "TestType", null);
 game.stack.push(card);
 game.givePriorityToCurrentPlayer();
 verify(ip1, times(1)).letPlayerSpeak();
 game.givePriorityToCurrentPlayer();
 verify(ip2, times(1)).letPlayerSpeak();
 card = (Card) game.stack.peek();
 assertEquals("TestCard", card.name);
 }
}