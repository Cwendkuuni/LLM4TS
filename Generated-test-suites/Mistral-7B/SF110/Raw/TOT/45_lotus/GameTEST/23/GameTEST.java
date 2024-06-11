import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import lotus.core.*;
import static org.junit.Assert.*;

public class GameTest {

 private UserInterface ip1, ip2;
 private Player player1, player2;
 private CardCollection deck1, deck2;
 private Game game;

 @Before
 public void setUp() {
 ip1 = Mockito.mock(UserInterface.class);
 ip2 = Mockito.mock(UserInterface.class);
 player1 = new Player(ip1, "Player1", new CardCollection());
 player2 = new Player(ip2, "Player2", new CardCollection());
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
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
 // Check initial state
 assertNull(game.getPlayingPlayer());
 assertNull(game.getNonPlayingPlayer());
 assertEquals(0, game.getPlayingPlayer().getHandSize());
 assertEquals(0, game.getNonPlayingPlayer().getHandSize());
 // Test initialization
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertEquals(player1, game.getPlayingPlayer());
 assertEquals(player2, game.getNonPlayingPlayer());
 // Check hand sizes
 assertEquals(56, deck1.size() + deck2.size()); // Assuming default deck size
 assertEquals(28, player1.getHandSize());
 assertEquals(28, player2.getHandSize());
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 // Test empty stack
 game.givePriorityToCurrentPlayer();
 // Test speaking players
 Mockito.when(player1.letPlayerSpeak()).thenReturn(true);
 Mockito.when(player2.letPlayerSpeak()).thenReturn(false);
 game.givePriorityToCurrentPlayer();
 Mockito.verify(player1).letPlayerSpeak();
 Mockito.verify(player2, never()).letPlayerSpeak();
 // Test resolving last card
 Mockito.when(stack.empty()).thenReturn(false);
 Mockito.when(stack.resolveLast()).thenThrow(new RuntimeException());
 Mockito.when(player1.letPlayerSpeak()).thenReturn(true);
 Mockito.when(player2.letPlayerSpeak()).thenReturn(true);
 game.givePriorityToCurrentPlayer();
 // Check that last card was resolved
 Mockito.verify(stack).resolveLast();
 }

}