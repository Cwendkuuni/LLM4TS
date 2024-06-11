package lotus.core;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.Player;
import lotus.core.CardCollection;
import lotus.core.Card;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class GameTest {

 private Player player1;
 private Player player2;
 private UserInterface ip1;
 private UserInterface ip2;
 private CardCollection deck1;
 private CardCollection deck2;

 @Before
 public void setup() {
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 player1 = new Player(ip1, "Player1");
 player2 = new Player(ip2, "Player2");
 deck1 = new ArrayList<>();
 deck2 = new ArrayList<>();
 }

 @Test
 public void testGetPlayingPlayer() {
 assertSame(player1, Game.getPlayingPlayer());
 Game.playingPlayer = 2;
 assertSame(player2, Game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 assertSame(player2, Game.getNonPlayingPlayer());
 Game.playingPlayer = 1;
 assertSame(player1, Game.getNonPlayingPlayer());
 }

 @Test
 public void testInit() {
 Game.init(ip1, ip2, "Player1Name", "Player2Name", deck1, deck2);
 assertEquals("Player1Name", player1.getName());
 assertEquals("Player2Name", player2.getName());
 verify(ip1, times(1)).init(player1);
 verify(ip2, times(1)).init(player2);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 Game.init(ip1, ip2, "Player1Name", "Player2Name", deck1, deck2);
 verify(player1, times(1)).letPlayerSpeak();
 verify(player2, times(1)).letPlayerSpeak();
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayer_EmptyStack() {
 Game.init(ip1, ip2, "Player1Name", "Player2Name", deck1, deck2);
 Game.stack = new Stack();
 Game.givePriorityToCurrentPlayer();
 }
}