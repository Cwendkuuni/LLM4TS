import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.Player;
import lotus.core.stack.Stack;
import java.util.ArrayList;

public class GameTest {

 private Player createDummyPlayer(String name, UserInterface ui) {
 return new Player(ui, name, new ArrayList<>());
 }

 @Test
 public void testGetPlayingPlayer() {
 // Test case for getPlayingPlayer with player1 as the current player
 Game.init(null, null, "Player1", "Player2", new ArrayList<>(), new ArrayList<>());
 assertEquals(Game.player1, Game.getPlayingPlayer());

 // Test case for getPlayingPlayer with player2 as the current player
 Game.playingPlayer = 2;
 assertEquals(Game.player2, Game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 // Test case for getNonPlayingPlayer with player1 as the non-playing player
 Game.init(null, null, "Player1", "Player2", new ArrayList<>(), new ArrayList<>());
 assertEquals(Game.player2, Game.getNonPlayingPlayer());

 // Test case for getNonPlayingPlayer with player2 as the non-playing player
 Game.playingPlayer = 2;
 assertEquals(Game.player1, Game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 // Test case for getOtherPlayer with player1 as the input player
 Game.init(null, null, "Player1", "Player2", new ArrayList<>(), new ArrayList<>());
 assertEquals(Game.player2, Game.getOtherPlayer(Game.player1));

 // Test case for getOtherPlayer with player2 as the input player
 Game.playingPlayer = 2;
 assertEquals(Game.player1, Game.getOtherPlayer(Game.player2));
 }

 @Test
 public void testInit() {
 // Test case for basic initialization
 UserInterface ui1 = new StubUserInterface();
 UserInterface ui2 = new StubUserInterface();
 String name1 = "Player1";
 String name2 = "Player2";
 CardCollection deck1 = new ArrayList<>();
 CardCollection deck2 = new ArrayList<>();
 Game.init(ui1, ui2, name1, name2, deck1, deck2);
 assertNull(Game.playingPlayer);
 assertNotNull(Game.player1);
 assertNotNull(Game.player2);
 assertSame(Game.player1, Game.getPlayingPlayer());
 assertSame(Game.player2, Game.getNonPlayingPlayer());

 // Test case for initialization with players having same name
 Game.init(ui1, ui2, name1, name1, deck1, deck2);
 assertSame(Game.player1, Game.player2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullPlayer1() {
 Game.init(null, null, null, "Player2", new ArrayList<>(), new ArrayList<>());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullPlayer2() {
 Game.init(null, null, "Player1", null, new ArrayList<>(), new ArrayList<>());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullDeck1() {
 Game.init(null, null, "Player1", "Player2", null, new ArrayList<>());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullDeck2() {
 Game.init(null, null, "Player1", "Player2", new ArrayList<>(), null);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 // Test case for basic functionality
 Game.init(null, null, "Player1", "Player2", new ArrayList<>(), new ArrayList<>());
 Game.stack.push(new Card("TestCard", "TestType"));
 Game.givePriorityToCurrentPlayer();
 assertFalse(Game.stack.empty());

 // Test case for both players having spoken
 Player p1 = createDummyPlayer("Player1", null);
 Player p2 = createDummyPlayer("Player2", null);
 Game.init(null, null, "Player1", "Player2", new ArrayList<>(), new ArrayList<>());
 p1.letPlayerSpeak = () -> true;
 p2.letPlayerSpeak = () -> true;
 Game.givePriorityToCurrentPlayer();
 Game.givePriorityToCurrentPlayer();
 assertTrue(Game.stack.empty());
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayerWithEmptyStack() {
 Game.init(null, null, "Player1", "Player2", new ArrayList<>(), new ArrayList<>());
 Game.givePriorityToCurrentPlayer();
 Game.givePriorityToCurrentPlayer();
 Game.stack.pop();
 Game.givePriorityToCurrentPlayer();
 }
}