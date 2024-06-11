import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lotus.core.Card;
import lotus.core.CardCollection;
import lotus.core.Game;
import lotus.core.Player;
import lotus.core.Stack;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;

public class GameTest {

 @Before
 public void setUp() {
 player1 = new Player(mock(UserInterface.class), "Player1", new CardCollection());
 player2 = new Player(mock(UserInterface.class), "Player2", new CardCollection());
 Game.init(Mockito.mock(UserInterface.class), Mockito.mock(UserInterface.class), "Player1", "Player2", new CardCollection(), new CardCollection());
 }

 @Test
 public void testGetPlayingPlayer() {
 assertSame(Game.player1, Game.getPlayingPlayer());
 Game.playingPlayer = 2;
 assertSame(Game.player2, Game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 assertSame(Game.player2, Game.getNonPlayingPlayer());
 Game.playingPlayer = 1;
 assertSame(Game.player1, Game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 assertSame(Game.player2, Game.getOtherPlayer(Game.player1));
 assertSame(Game.player1, Game.getOtherPlayer(Game.player2));
 }

 @Test
 public void testInit() {
 List<Card> deck1 = new ArrayList<>();
 List<Card> deck2 = new ArrayList<>();
 deck1.add(new Card("Card1", Player.COLOR_BLUE));
 deck2.add(new Card("Card2", Player.COLOR_RED));

 when(player1.getName()).thenReturn("Player1");
 when(player2.getName()).thenReturn("Player2");
 when(player1.getUserInterface()).thenReturn(Mockito.mock(UserInterface.class));
 when(player2.getUserInterface()).thenReturn(Mockito.mock(UserInterface.class));

 Game.init(Mockito.mock(UserInterface.class), Mockito.mock(UserInterface.class), "Player1", "Player2", new CardCollection(deck1), new CardCollection(deck2));

 assertEquals("Player1", Game.player1.getName());
 assertEquals("Player2", Game.player2.getName());
 assertEquals(deck1, Game.player1.library);
 assertEquals(deck2, Game.player2.library);
 }

 @Test
 public void testGivePriorityToCurrentPlayer_APdone_NAPdone() {
 Player player1Spy = Mockito.spy(Game.player1);
 Player player2Spy = Mockito.spy(Game.player2);

 doReturn(true).when(player1Spy).letPlayerSpeak();
 doReturn(true).when(player2Spy).letPlayerSpeak();
 doReturn(Collections.singletonList(new Card("Card1"))).when(Game.stack).resolveLast();

 Game.init(Mockito.mock(UserInterface.class), Mockito.mock(UserInterface.class), "Player1", "Player2", new CardCollection(), new CardCollection());

 Game.givePriorityToCurrentPlayer();
 Game.givePriorityToCurrentPlayer();

 verify(player1Spy, times(1)).letPlayerSpeak();
 verify(player2Spy, times(1)).letPlayerSpeak();
 verify(Game.stack, times(1)).resolveLast();
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayer_StackNotEmpty() {
 Player player1Spy = Mockito.spy(Game.player1);
 Player player2Spy = Mockito.spy(Game.player2);

 doReturn(true).when(player1Spy).letPlayerSpeak();
 doReturn(true).when(player2Spy).letPlayerSpeak();
 doReturn(Collections.singletonList(new Card("Card1"))).when(Game.stack).resolveLast();

 Game.init(Mockito.mock(UserInterface.class), Mockito.mock(UserInterface.class), "Player1", "Player2", new CardCollection(), new CardCollection());

 Game.stack.push(new Card("Card2"));
 Game.givePriorityToCurrentPlayer();

 verify(player1Spy, never()).letPlayerSpeak();
 verify(player2Spy, never()).letPlayerSpeak();
 verify(Game.stack, times(1)).resolveLast();
 }
}