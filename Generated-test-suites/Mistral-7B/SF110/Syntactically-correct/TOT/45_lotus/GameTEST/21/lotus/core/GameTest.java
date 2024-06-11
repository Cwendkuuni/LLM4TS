import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import lotus.core.interfaces.UserInterface;
import lotus.core.Player;
import lotus.core.stack.Stack;
import lotus.core.Game;
import lotus.core.CardCollection;
import lotus.core.Card;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GameTest {

 @Before
 public void setUp() {
 UserInterface ip1 = mock(UserInterface.class);
 UserInterface ip2 = mock(UserInterface.class);
 String name1 = "Player1";
 String name2 = "Player2";
 CardCollection deck1 = new CardCollection();
 CardCollection deck2 = new CardCollection();

 Game.init(ip1, ip2, name1, name2, deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 Player expectedPlayer = Game.player1;
 Player actualPlayer = Game.getPlayingPlayer();

 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player expectedPlayer = Game.player2;
 Player actualPlayer = Game.getNonPlayingPlayer();

 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test
 public void testGetOtherPlayer() {
 Player p1 = Game.player1;
 Player p2 = Game.player2;
 Player expectedPlayer = (p1 == Game.player1) ? p2 : p1;
 Player actualPlayer = Game.getOtherPlayer(p1);

 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test
 public void testInit() {
 Game game = new Game();
 UserInterface ip = mock(UserInterface.class);
 String name = "Player";
 CardCollection deck = new CardCollection();

 game.init(ip, ip, name, name, deck, deck);

 verify(ip).init(Game.player1);
 verify(ip).init(Game.player2);
 }

 @Test
 public void testGivePriorityToCurrentPlayer_NoSpeak() {
 Player player1 = Game.player1;
 Player player2 = Game.player2;
 boolean player1Speak = false;
 boolean player2Speak = false;

 doAnswer(invocationOnMock -> {
 player1Speak = invocationOnMock.callRealMethod();
 return !player1Speak;
 }).when(player1).letPlayerSpeak();

 doAnswer(invocationOnMock -> {
 player2Speak = invocationOnMock.callRealMethod();
 return !player2Speak;
 }).when(player2).letPlayerSpeak();

 Game game = new Game();
 game.givePriorityToCurrentPlayer();

 verify(player1).letPlayerSpeak();
 verify(player2).letPlayerSpeak();
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayer_StackNotEmpty() {
 Player player1 = Game.player1;
 Player player2 = Game.player2;
 Stack stack = new Stack();
 stack.push(new Card());

 doAnswer(invocationOnMock -> player1.letPlayerSpeak()).when(player1).letPlayerSpeak();
 doAnswer(invocationOnMock -> player2.letPlayerSpeak()).when(player2).letPlayerSpeak();

 Game game = new Game();
 game.stack = stack;
 game.init(mock(UserInterface.class), mock(UserInterface.class), "Player1", "Player2", new CardCollection(), new CardCollection());
 game.givePriorityToCurrentPlayer();
 }
}