import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;

public class GameTest {

 private UserInterface ip1;
 private UserInterface ip2;
 private Player player1;
 private Player player2;
 private Game game;
 private CardCollection deck1;
 private CardCollection deck2;

 @Before
 public void setup() {
 ip1 = new DummyUserInterface();
 ip2 = new DummyUserInterface();
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game(ip1, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 assertEquals(player1, game.getPlayingPlayer());
 game.playingPlayer = 2;
 assertEquals(player2, game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 assertEquals(player2, game.getNonPlayingPlayer());
 game.playingPlayer = 1;
 assertEquals(player1, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 assertEquals(player2, game.getOtherPlayer(player1));
 assertEquals(player1, game.getOtherPlayer(player2));
 }

 @Test
 public void testInit() {
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertNotNull(game.player1);
 assertNotNull(game.player2);
 assertEquals(Game.player1, game.getPlayingPlayer());
 }

 @Test(expected = NullPointerException.class)
 public void testInitNullDeck() {
 game = new Game(ip1, ip2, "Player1", "Player2", null, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitNullPlayer2() {
 game = new Game(ip1, ip2, "Player1", null, deck1, deck2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitSameNamePlayers() {
 game = new Game(ip1, ip2, "Player1", "Player1", deck1, deck2);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 assertFalse(game.stack.empty());
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
}

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayerEmptyStack() {
 game.givePriorityToCurrentPlayer();
 game.stack = new Stack();
 game.givePriorityToCurrentPlayer();
 }

 @Test(expected = NullPointerException.class)
 public void testGivePriorityToCurrentPlayerNullUserInterface() {
 ip1 = null;
 game = new Game(ip1, ip2, "Player1", "Player2", deck1, deck2);
 game.givePriorityToCurrentPlayer();
 }
}