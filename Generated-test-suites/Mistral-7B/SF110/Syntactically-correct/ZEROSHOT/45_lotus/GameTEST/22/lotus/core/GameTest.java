package lotus.core.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import lotus.core.Game;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.player.Player;
import lotus.core.stack.Stack;
import lotus.core.card.Card;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class GameTest {

 private UserInterface ip1;
 private UserInterface ip2;
 private Player player1;
 private Player player2;
 private Game game;

 @Before
 public void setUp() {
 ip1 = Mockito.mock(UserInterface.class);
 ip2 = Mockito.mock(UserInterface.class);
 player1 = new Player(ip1, "Player 1", new ArrayList<Card>());
 player2 = new Player(ip2, "Player 2", new ArrayList<Card>());
 game = new Game();
 game.init(ip1, ip2, "Player 1", "Player 2", new CardCollection(), new CardCollection());
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
 assertNotNull(game.player1);
 assertNotNull(game.player2);
 assertEquals(Game.player1, game.getPlayingPlayer());
 assertEquals(Game.player2, game.getNonPlayingPlayer());
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 game.playingPlayer = 1;
 Game.stack = new Stack();
 player1.letPlayerSpeak = Mockito.doReturn(true).thenReturn(false);
 player2.letPlayerSpeak = Mockito.doReturn(false).thenReturn(true);

 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();

 Card card1 = new Card("Card 1", Player.Color.BLUE, CardType.ACTION);
 Card card2 = new Card("Card 2", Player.Color.RED, CardType.ACTION);
 player1.library.add(card1);
 player2.library.add(card2);

 game.stack.push(card1);
 game.stack.push(card2);

 game.givePriorityToCurrentPlayer();

 assertEquals(1, player1.handSize());
 assertEquals(0, player2.handSize());
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayer_StackNotEmpty() {
 game.playingPlayer = 1;
 Game.stack = new Stack();
 player1.letPlayerSpeak = Mockito.doReturn(true).thenReturn(false);
 player2.letPlayerSpeak = Mockito.doReturn(false).thenReturn(true);

 game.givePriorityToCurrentPlayer();
 game.stack.push(new Card("Card 1", Player.Color.BLUE, CardType.ACTION));
 game.givePriorityToCurrentPlayer();

 assertFalse(game.stack.empty());
 }
}