import org.junit.Test;
import org.junit.Assert.*;
import lotus.core.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

 private UserInterface ip1, ip2;
 private Player player1, player2;
 private CardCollection deck1, deck2;

 @Before
 public void setUp() {
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 player1 = new Player(ip1, "Player1", null);
 player2 = new Player(ip2, "Player2", null);
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 }

 @Test
 public void testGetPlayingPlayer() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertEquals(Game.player1, Game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertEquals(Game.player2, Game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));
 assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetPlayingPlayerBeforeInit() {
 assertEquals(null, Game.getPlayingPlayer());
 }

 @Test
 public void testInit() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertNotNull(Game.player1);
 assertNotNull(Game.player2);
 assertNotNull(Game.currentPhase);
 }

 @Test
 public void testGivePriorityToCurrentPlayerEmptyStack() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 Game.givePriorityToCurrentPlayer();
 assertTrue(Game.stack.isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayerNonEmptyStack() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 Card card = new Card();
 Game.stack.push(card);
 Game.givePriorityToCurrentPlayer();
 assertFalse(Game.stack.isEmpty());
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayerNullPlayingPlayer() {
 Game.playingPlayer = null;
 Game.givePriorityToCurrentPlayer();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetPlayingPlayerNullPlayingPlayer() {
 Game.playingPlayer = null;
 assertEquals(null, Game.getPlayingPlayer());
 }

 @Test
 public void testLetPlayerSpeakTrue() {
 when(player1.letPlayerSpeak()).thenReturn(true);
 when(player2.letPlayerSpeak()).thenReturn(true);
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 Game.givePriorityToCurrentPlayer();
 }

 @Test
 public void testLetPlayerSpeakFalsePlayer1() {
 when(player1.letPlayerSpeak()).thenReturn(false);
 when(player2.letPlayerSpeak()).thenReturn(true);
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 Game.givePriorityToCurrentPlayer();
 }

 @Test
 public void testLetPlayerSpeakFalsePlayer2() {
 when(player1.letPlayerSpeak()).thenReturn(true);
 when(player2.letPlayerSpeak()).thenReturn(false);
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 Game.givePriorityToCurrentPlayer();
 }

 @Test
 public void testLetPlayerSpeakBothFalse() {
 when(player1.letPlayerSpeak()).thenReturn(false);
 when(player2.letPlayerSpeak()).thenReturn(false);
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 Game.givePriorityToCurrentPlayer();
 }
}