import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

 private static UserInterface mockUI1;
 private static UserInterface mockUI2;
 private static Player player1;
 private static Player player2;
 private static Game game;
 private static CardCollection deck1;
 private static CardCollection deck2;

 @BeforeClass
 public static void setUpBeforeClass() {
 mockUI1 = mock(UserInterface.class);
 mockUI2 = mock(UserInterface.class);
 player1 = new Player(mockUI1, "Player1", new CardCollection());
 player2 = new Player(mockUI2, "Player2", new CardCollection());
 game = new Game();
 deck1 = new CardCollection();
 deck2 = new CardCollection();
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

 @Test
 public void testInit() {
 game.init(mockUI1, mockUI2, "Player1Name", "Player2Name", deck1, deck2);
 assertTrue(game.player1 instanceof Player);
 assertTrue(game.player2 instanceof Player);
 assertEquals("Player1Name", game.player1.name);
 assertEquals("Player2Name", game.player2.name);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullPlayer1() {
 game.init(mockUI1, mockUI2, null, "Player2Name", deck1, deck2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullPlayer2() {
 game.init(mockUI1, mockUI2, "Player1Name", null, deck1, deck2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullDeck1() {
 game.init(mockUI1, mockUI2, "Player1Name", "Player2Name", null, deck2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullDeck2() {
 game.init(mockUI1, mockUI2, "Player1Name", "Player2Name", deck1, null);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 // Add tests for the given scenario and edge cases
 }

}