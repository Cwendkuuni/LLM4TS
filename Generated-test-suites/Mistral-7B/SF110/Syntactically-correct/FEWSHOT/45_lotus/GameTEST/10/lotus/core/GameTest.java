package lotus.core;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class GameTest {

 Player player1, player2;
 UserInterface ip1, ip2;
 CardCollection deck1, deck2;

 @Test
 public void testGetPlayingPlayer() {
 player1 = mock(Player.class);
 player2 = mock(Player.class);
 when(player1.getNumber()).thenReturn(1);
 when(player2.getNumber()).thenReturn(2);
 Game.player1 = player1;
 Game.player2 = player2;

 assertSame(player1, Game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 player1 = mock(Player.class);
 player2 = mock(Player.class);
 when(player1.getNumber()).thenReturn(1);
 when(player2.getNumber()).thenReturn(2);
 Game.player1 = player1;
 Game.player2 = player2;

 assertSame(player2, Game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 player1 = mock(Player.class);
 player2 = mock(Player.class);
 when(player1.getNumber()).thenReturn(1);
 when(player2.getNumber()).thenReturn(2);

 assertSame(player2, Game.getOtherPlayer(player1));
 assertSame(player1, Game.getOtherPlayer(player2));
 }

 @Test
 public void testInit() {
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 player1 = mock(Player.class);
 player2 = mock(Player.class);
 deck1 = mock(CardCollection.class);
 deck2 = mock(CardCollection.class);

 when(player1.getNumber()).thenReturn(1);
 when(player2.getNumber()).thenReturn(2);
 when(ip1.init(player1)).thenReturn(true);
 when(ip2.init(player2)).thenReturn(true);
 when(deck1.size()).thenReturn(10);
 when(deck2.size()).thenReturn(10);

 Game.init(ip1, ip2, "Player1Name", "Player2Name", deck1, deck2);

 assertSame(player1, Game.player1);
 assertSame(player2, Game.player2);
 assertSame(ip1, Game.player1.getInterface());
 assertSame(ip2, Game.player2.getInterface());
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 player1 = mock(Player.class);
 player2 = mock(Player.Player.class);
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 stack = mock(Stack.class);
 when(player1.getNumber()).thenReturn(1);
 when(player2.getNumber()).thenReturn(2);
 when(player1.letPlayerSpeak()).thenReturn(true);
 when(player2.letPlayerSpeak()).thenReturn(true);
 when(stack.empty()).thenReturn(false);
 when(stack.resolveLast()).thenReturn(true);
 when(ip1.init(player1)).thenReturn(true);
 when(ip2.init(player2)).thenReturn(true);

 Game.init(ip1, ip2, "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 Game.stack = stack;

 Game.init(ip1, ip2, "Player1Name", "Player2Name", new CardCollection(), new CardCollection());

 Game.givePriorityToCurrentPlayer();
 }
}