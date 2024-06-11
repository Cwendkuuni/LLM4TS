import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lotus.core.Card;
import lotus.core.Player;
import lotus.core.Stack;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;

public class GameTest {

 @Before
 public void setup() {
 player1 = new Player(Mockito.mock(UserInterface.class), "Player1", new ArrayList<>());
 player2 = new Player(Mockito.mock(UserInterface.class), "Player2", new ArrayList<>());
 IP1 = Mockito.mock(UserInterface.class);
 IP2 = Mockito.mock(UserInterface.class);

 when(IP1.init(Mockito.any(Player.class))).thenReturn(true);
 when(IP2.init(Mockito.any(Player.class))).thenReturn(true);

 Game.init(IP1, IP2, "Player1", "Player2", new ArrayList<>(), new ArrayList<>());
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
 public void testInit() {
 List<Card> deck1 = new ArrayList<>();
 List<Card> deck2 = new ArrayList<>();
 deck1.add(Mockito.mock(Card.class));
 deck2.add(Mockito.mock(Card.class));

 Game.init(IP1, IP2, "Player1", "Player2", deck1, deck2);

 verify(IP1).init(Game.player1);
 verify(IP2).init(Game.player2);

 assertEquals(Game.player1, Game.getPlayingPlayer());
 assertEquals(Game.player2, Game.getNonPlayingPlayer());
 }

 @Test(expected = IllegalStateException.class)
 public void testInitWithNullIPs() {
 Game.init(null, null, "Player1", "Player2", new ArrayList<>(), new ArrayList<>());
 }

 @Test(expected = IllegalStateException.class)
 public void testInitWithNullNames() {
 Game.init(IP1, IP2, null, null, new ArrayList<>(), new ArrayList<>());
 }

 @Test(expected = IllegalStateException.class)
 public void testInitWithNullDecks() {
 Game.init(IP1, IP2, "Player1", "Player2", null, new ArrayList<>());
 Game.init(IP1, IP2, "Player1", "Player2", new ArrayList<>(), null);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 Player player = new Player(Mockito.mock(UserInterface.class), "Player", new ArrayList<>());
 Game.init(IP1, IP2, player, player, new ArrayList<>(), new ArrayList<>());

 Stack stack = Mockito.mock(Stack.class);
 when(stack.empty()).thenReturn(false);
 when(stack.resolveLast()).thenReturn(true);

 Game.stack = stack;
 Game.givePriorityToCurrentPlayer();

 verify(stack).resolveLast();
 }

 @Test
 public void testGivePriorityToCurrentPlayerWhenNoSpeak() {
 Player player = new Player(Mockito.mock(UserInterface.class), "Player", new ArrayList<>());
 Game.init(IP1, IP2, player, player, new ArrayList<>(), new ArrayList<>());

 when(player.letPlayerSpeak()).thenReturn(false);
 Game.givePriorityToCurrentPlayer();

 verify(player, times(2)).letPlayerSpeak();
 }

 @Test
 public void testGivePriorityToCurrentPlayerWhenBothPlayersSpeak() {
 Player player1 = new Player(Mockito.mock(UserInterface.class), "Player1", new ArrayList<>());
 Player player2 = new Player(Mockito.mock(UserInterface.class), "Player2", new ArrayList<>());
 Game.init(IP1, IP2, player1, player2, new ArrayList<>(), new ArrayList<>());

 when(player1.letPlayerSpeak()).thenReturn(true);
 when(player2.letPlayerSpeak()).thenReturn(true);

 Game.givePriorityToCurrentPlayer();

 verify(player1).letPlayerSpeak();
 verify(player2).letPlayerSpeak();
 }

 @Test
 public void testGivePriorityToCurrentPlayerWhenStackIsEmpty() {
 Player player = new Player(Mockito.mock(UserInterface.class), "Player", new ArrayList<>());
 Game.init(IP1, IP2, player, player, new ArrayList<>(), new ArrayList<>());

 Stack stack = Mockito.mock(Stack.class);
 when(stack.empty()).thenReturn(true);

 Game.stack = stack;
 Game.givePriorityToCurrentPlayer();

 verify(stack, never()).resolveLast();
 }

 @Test
 public void testGetOtherPlayer() {
 Player player1 = new Player(Mockito.mock(UserInterface.class), "Player1", new ArrayList<>());
 Player player2 = new Player(Mockito.mock(UserInterface.class), "Player2", new ArrayList<>());
 Game.init(IP1, IP2, player1, player2, new ArrayList<>(), new ArrayList<>());

 assertEquals(player2, Game.getOtherPlayer(player1));
 assertEquals(player1, Game.getOtherPlayer(player2));
 }
}