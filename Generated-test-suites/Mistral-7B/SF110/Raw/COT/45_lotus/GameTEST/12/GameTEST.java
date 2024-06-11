import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;

public class GameTest {

 private static Game game;
 private static UserInterface ip1;
 private static UserInterface ip2;
 private static Player player1;
 private static Player player2;
 private static CardCollection deck1;
 private static CardCollection deck2;

 @BeforeClass
 public static void setUp() {
 ip1 = new StubUserInterface();
 ip2 = new StubUserInterface();
 game = new Game();
 player1 = new Player(ip1, "Player1", deck1 = new CardCollection());
 player2 = new Player(ip2, "Player2", deck2 = new CardCollection());
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 Player expectedPlayer = player1;
 Player actualPlayer = game.getPlayingPlayer();
 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player expectedPlayer = player2;
 Player actualPlayer = game.getNonPlayingPlayer();
 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test
 public void testGetOtherPlayer() {
 Player expectedPlayer = (game.getPlayingPlayer() == player1) ? player2 : player1;
 Player actualPlayer = game.getOtherPlayer(game.getPlayingPlayer());
 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetPlayingPlayerWhenBothPlayersAreEmpty() {
 game.init(ip1, ip2, null, null, null, null);
 try {
 game.getPlayingPlayer();
 } catch (IllegalStateException e) {
 fail("Expected IllegalStateException but got: " + e.getMessage());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testGetPlayingPlayerWhenBothPlayersAreNull() {
 game.init(null, null, null, null, null, null);
 try {
 game.getPlayingPlayer();
 } catch (IllegalStateException e) {
 fail("Expected IllegalStateException but got: " + e.getMessage());
 }
 }

 @Test
 public void testInit() {
 assertEquals(player1, game.getPlayingPlayer());
 assertEquals(player2, game.getNonPlayingPlayer());
 assertNotNull(game.currentPhase);
 assertNotNull(game.stack);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 // The method does not return anything, but we can check some side effects
 // Add some tests to check if the players have spoken and if the stack has been resolved
 // If the stack is not empty, the method should call resolveLast() and recursively call itself
 // Add tests to check if the method correctly determines the speaking order
 }

}