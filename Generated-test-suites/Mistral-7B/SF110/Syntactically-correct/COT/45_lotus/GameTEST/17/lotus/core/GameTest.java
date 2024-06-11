import org.junit.Assert;
import org.junit.Test;
import lotus.core.*;
import static org.mockito.Mockito.*;

public class GameTest {

 private UserInterface ip1;
 private UserInterface ip2;
 private Player player1;
 private Player player2;
 private CardCollection deck1;
 private CardCollection deck2;
 private Game game;

 @Before
 public void setUp() {
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 player1 = new Player(ip1, "Player1", new CardCollection());
 player2 = new Player(ip2, "Player2", new CardCollection());
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 Assert.assertEquals(player1, game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Assert.assertEquals(player2, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 Assert.assertEquals(player2, game.getOtherPlayer(player1));
 Assert.assertEquals(player1, game.getOtherPlayer(player2));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetPlayingPlayerWhenBothPlayersAreNull() {
 game = new Game();
 game.getPlayingPlayer();
 }

 @Test
 public void testInit() {
 // Check that players have been initialized
 Assert.assertNotNull(game.player1);
 Assert.assertNotNull(game.player2);
 // Check that interfaces have been initialized
 verify(ip1).init(game.player1);
 verify(ip2).init(game.player2);
 // Check that initial phase is set to UntapPhase
 Assert.assertEquals(new UntapPhase(), game.currentPhase);
 // Check that decks have been set to players
 for (Card card : deck1) {
 Assert.assertEquals(game.player1, card.owner);
 Assert.assertEquals(game.player1.library, card.zone);
 }
 for (Card card : deck2) {
 Assert.assertEquals(game.player2, card.owner);
 Assert.assertEquals(game.player2.library, card.zone);
 }
 }

 @Test
 public void testGivePriorityToCurrentPlayerWhenOnlyCurrentPlayerSpeaks() {
 doAnswer(invocation -> {
 boolean spoke = player1.letPlayerSpeak();
 return spoke;
 }).when(player1).letPlayerSpeak();

 doAnswer(invocation -> false).when(player2).letPlayerSpeak();

 game.givePriorityToCurrentPlayer();

 // Check that only one resolution occurred
 verify(stack, times(1)).resolveLast();
 }

 @Test
 public void testGivePriorityToCurrentPlayerWhenBothPlayersSpeak() {
 doAnswer(invocation -> {
 boolean spoke = player1.letPlayerSpeak();
 return !spoke;
 }).when(player1).letPlayerSpeak();

 doAnswer(invocation -> {
 boolean spoke = player2.letPlayerSpeak();
 return !spoke;
 }).when(player2).letPlayerSpeak();

 game.givePriorityToCurrentPlayer();

 // Check that a resolution occurred for both players
 verify(stack, times(2)).resolveLast();
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayerWhenBothPlayersSpeakButStackIsEmpty() {
 doAnswer(invocation -> {
 boolean spoke = player1.letPlayerSpeak();
 return !spoke;
 }).when(player1).letPlayerSpeak();

 doAnswer(invocation -> {
 boolean spoke = player2.letPlayerSpeak();
 return !spoke;
 }).when(player2).letPlayerSpeak();

 game.givePriorityToCurrentPlayer();
 game.stack.clear();
 game.givePriorityToCurrentPlayer();
 }
}