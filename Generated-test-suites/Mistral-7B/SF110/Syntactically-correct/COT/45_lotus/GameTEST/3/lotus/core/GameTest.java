import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.interfaces.*;
import java.util.*;

public class GameTest {

 private static Game game;
 private static UserInterface ip1;
 private static UserInterface ip2;
 private static Player player1;
 private static Player player2;
 private static CardCollection deck1;
 private static CardCollection deck2;

 @BeforeClass
 public static void setup() {
 game = new Game();
 ip1 = new MockUserInterface();
 ip2 = new MockUserInterface();
 player1 = new Player(ip1, "Player1", new CardCollection());
 player2 = new Player(ip2, "Player2", new CardCollection());
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 Player playingPlayer = game.getPlayingPlayer();
 assertTrue(playingPlayer == player1 || playingPlayer == player2);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player nonPlayingPlayer = game.getNonPlayingPlayer();
 assertTrue(nonPlayingPlayer == player1 || nonPlayingPlayer == player2);
 }

 @Test
 public void testGetOtherPlayer() {
 Player otherPlayer1 = game.getOtherPlayer(player1);
 assertTrue(otherPlayer1 == player2);
 Player otherPlayer2 = game.getOtherPlayer(player2);
 assertTrue(otherPlayer2 == player1);
 }

 @Test
 public void testInit() {
 assertEquals(player1, game.getPlayingPlayer());
 assertEquals(player2, game.getNonPlayingPlayer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullUserInterface() {
 game.init(null, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullName1() {
 game.init(ip1, null, null, "Player2", deck1, deck2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullName2() {
 game.init(ip1, ip2, "Player1", null, deck1, deck2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullDeck1() {
 game.init(ip1, ip2, "Player1", "Player2", null, deck2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullDeck2() {
 game.init(ip1, ip2, "Player1", "Player2", deck1, null);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 game.givePriorityToCurrentPlayer();
 // The resolution of the last card on the stack is not tested here
 // You may want to add an extra test case for this
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayerEmptyStack() {
 game.stack = new Stack();
 game.givePriorityToCurrentPlayer();
 }
}