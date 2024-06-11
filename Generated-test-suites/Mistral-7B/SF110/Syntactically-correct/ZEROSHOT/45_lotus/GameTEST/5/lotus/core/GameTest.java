package lotus.core.test;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import lotus.core.*;
import lotus.core.interfaces.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

 private UserInterface ip1, ip2;
 private String name1, name2;
 private CardCollection deck1, deck2;
 private Player player1, player2;
 private Game game;

 @Before
 public void setup() {
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 name1 = "Player1";
 name2 = "Player2";
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 player1 = new Player(ip1, name1, deck1);
 player2 = new Player(ip2, name2, deck2);
 game = new Game();
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 assertSame(player1, game.getPlayingPlayer());
 game.playingPlayer = 2;
 assertSame(player2, game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 assertSame(player2, game.getNonPlayingPlayer());
 game.playingPlayer = 1;
 assertSame(player1, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 assertSame(player2, game.getOtherPlayer(player1));
 assertSame(player1, game.getOtherPlayer(player2));
 }

 @Test
 public void testInit() {
 verify(ip1, times(1)).init(player1);
 verify(ip2, times(1)).init(player2);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 game.givePriorityToCurrentPlayer();
 verify(player1, times(1)).letPlayerSpeak();
 verifyNoMoreInteractions();
 game.givePriorityToCurrentPlayer();
 verify(player2, times(1)).letPlayerSpeak();
 verifyNoMoreInteractions();
 }

 @Test(expected = IllegalStateException.class)
 public void testStackNotEmpty() {
 game.stack = new Stack();
 game.stack.add(new Card("TestCard", "TestType"));
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 game.stack.resolveLast();
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 }
}