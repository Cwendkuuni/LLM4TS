import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import lotus.core.*;
import static org.junit.Assert.*;

public class GameTest {

 private UserInterface ip1, ip2;
 private Player player1, player2;
 private CardCollection deck1, deck2;

 @Before
 public void setUp() {
 ip1 = Mockito.mock(UserInterface.class);
 ip2 = Mockito.mock(UserInterface.class);
 player1 = new Player(ip1, "Player1", null);
 player2 = new Player(ip2, "Player2", null);
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 }

 @Test
 public void testGetPlayingPlayer() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertEquals(player1, Game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertEquals(player2, Game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertEquals(player2, Game.getOtherPlayer(player1));
 assertEquals(player1, Game.getOtherPlayer(player2));
 }

 @Test
 public void testInit() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertNotNull(Game.player1);
 assertNotNull(Game.player2);
 assertEquals(player1, Game.getPlayingPlayer());
 }

 @Test(expected = NullPointerException.class)
 public void testInitNullPlayer1() {
 Game.init(ip1, ip2, null, "Player2", deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitNullPlayer2() {
 Game.init(ip1, ip2, "Player1", null, deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitNullDeck1() {
 Game.init(ip1, ip2, "Player1", "Player2", null, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitNullDeck2() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, null);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 player1.letPlayerSpeak = Mockito.doReturn(true);
 player2.letPlayerSpeak = Mockito.doReturn(true);
 assertEquals(0, Game.stack.size());
 Game.givePriorityToCurrentPlayer();
 assertTrue(Game.stack.empty());

 player1.letPlayerSpeak = Mockito.doReturn(false);
 player2.letPlayerSpeak = Mockito.doReturn(true);
 Game.stack.push(new Card());
 assertEquals(1, Game.stack.size());
 Game.givePriorityToCurrentPlayer();
 assertTrue(Game.stack.empty());

 player1.letPlayerSpeak = Mockito.doReturn(true);
 player2.letPlayerSpeak = Mockito.doReturn(false);
 Game.stack.push(new Card());
 Game.stack.push(new Card());
 assertEquals(2, Game.stack.size());
 Game.givePriorityToCurrentPlayer();
 assertEquals(1, Game.stack.size());

 Game.stack.pop();
 player1.letPlayerSpeak = Mockito.doReturn(false);
 player2.letPlayerSpeak = Mockito.doReturn(true);
 Game.stack.push(new Card());
 assertEquals(1, Game.stack.size());
 Game.givePriorityToCurrentPlayer();
 assertTrue(Game.stack.empty());
 }

 @Test
 public void testGivePriorityToCurrentPlayerException() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 player1.letPlayerSpeak = Mockito.doThrow(new RuntimeException()).when(player1);
 player2.letPlayerSpeak = Mockito.doReturn(true);
 Game.stack.push(new Card());
 assertEquals(1, Game.stack.size());
 try {
 Game.givePriorityToCurrentPlayer();
 fail("Expected RuntimeException");
 } catch (RuntimeException e) {
 // Expected
 }
 assertTrue(Game.stack.empty());
 }
}