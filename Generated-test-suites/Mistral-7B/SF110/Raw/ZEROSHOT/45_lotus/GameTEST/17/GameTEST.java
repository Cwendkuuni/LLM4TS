package lotus.core.test;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import lotus.core.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

 private UserInterface ip1, ip2;
 private Player player1, player2;
 private Game game;
 private CardCollection deck1, deck2;

 @Before
 public void setup() {
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 player1 = new Player(ip1, "Player1", new CardCollection());
 player2 = new Player(ip2, "Player2", new CardCollection());
 game = new Game();
 deck1 = new CardCollection();
 deck2 = new CardCollection();

 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 assertEquals(player1, game.getPlayingPlayer());
 game.setPlayingPlayer(2);
 assertEquals(player2, game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 assertEquals(player2, game.getNonPlayingPlayer());
 game.setPlayingPlayer(1);
 assertEquals(player2, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 assertEquals(player2, game.getOtherPlayer(player1));
 assertEquals(player1, game.getOtherPlayer(player2));
 }

 @Test
 public void testInit() {
 verify(ip1).init(player1);
 verify(ip2).init(player2);
 assertEquals(player1, game.getPlayingPlayer());
 assertEquals(player2, game.getNonPlayingPlayer());
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 game.givePriorityToCurrentPlayer();
 verify(player1).letPlayerSpeak();
 verifyNoMoreInteractions();
 game.givePriorityToCurrentPlayer();
 verify(player2).letPlayerSpeak();
 verifyNoMoreInteractions();

 Card card1 = new Card("Card1", CardType.ACTION);
 player1.library.add(card1);
 Card card2 = new Card("Card2", CardType.ACTION);
 player2.library.add(card2);

 game.givePriorityToCurrentPlayer();
 verify(player1).letPlayerSpeak();
 verify(card1).resolve();
 game.givePriorityToCurrentPlayer();
 verify(player2).letPlayerSpeak();
 verify(card2).resolve();
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayerWithEmptyStack() {
 game.stack = new Stack();
 game.givePriorityToCurrentPlayer();
 }
}