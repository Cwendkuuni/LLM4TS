import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import lotus.core.*;
import java.util.Arrays;

public class GameTest {

 private Player player1;
 private Player player2;
 private Game game;
 private UserInterface ip1, ip2;
 private CardCollection deck1, deck2;

 @Before
 public void setUp() {
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 player1 = new Player(ip1, "Player 1", deck1);
 player2 = new Player(ip2, "Player 2", deck2);
 game = new Game();
 game.init(ip1, ip2, "Player 1", "Player 2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 assertSame(player1, game.getPlayingPlayer());
 game.playingPlayer = 2;
 assertSame(player2, game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 assertSame(player2, game.getNonPlayingPlayer());
 game.playingPlayer = 1;
 assertSame(player1, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 assertSame(player2, game.getOtherPlayer(player1));
 assertSame(player1, game.getOtherPlayer(player2));
 }

 @Test
 public void testInit() {
 // check if player1 and player2 are initialized
 verify(ip1).init(player1);
 verify(ip2).init(player2);
 // check if playingPlayer and currentPhase are set correctly
 assertEquals(1, game.playingPlayer);
 assertSame(new UntapPhase(), game.currentPhase);
 // check if player decks are assigned correctly
 assertEquals(deck1.size(), player1.library.size());
 assertEquals(deck2.size(), player2.library.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testInitWithNullUserInterfaces() {
 game = new Game();
 try {
 game.init(null, null, "Player 1", "Player 2", deck1, deck2);
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Expected IllegalStateException, but got " + e.getClass().getName());
 }
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullPlayerDecks() {
 game = new Game();
 try {
 game.init(ip1, ip2, "Player 1", "Player 2", null, null);
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Expected NullPointerException, but got " + e.getClass().getName());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithEmptyName() {
 game = new Game();
 try {
 game.init(ip1, ip2, "", "Player 2", deck1, deck2);
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Expected IllegalArgumentException, but got " + e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithSameName() {
 game = new Game();
 try {
 game.init(ip1, ip2, "Player 1", "Player 1", deck1, deck2);
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Expected IllegalArgumentException, but got " + e.getMessage());
 }
 }

 @Test
 public void testGivePriorityToCurrentPlayer_NoPlayersSpeak() {
 game.givePriorityToCurrentPlayer();
 verify(ip1).letPlayerSpeak();
 verify(ip2).letPlayerSpeak();
 }

 @Test
 public void testGivePriorityToCurrentPlayer_PlayersSpeakInOrder() {
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 game.playingPlayer = 2;
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 verify(ip1).letPlayerSpeak();
 verify(ip2).letPlayerSpeak().times(3);
 }

 @Test
 public void testGivePriorityToCurrentPlayer_PlayersSpeakOutOfOrder() {
 game.givePriorityToCurrentPlayer();
 game.playingPlayer = 2;
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 verify(ip1).letPlayerSpeak().atLeastOnce();
 verify(ip2).letPlayerSpeak().atLeast(3).times(2);
 }

 @Test
 public void testGivePriorityToCurrentPlayer_StackResolution() {
 Card card1 = new Card("TestCard1", CardType.ACTION);
 Card card2 = new Card("TestCard2", CardType.ACTION);
 game.stack.push(card1);
 game.stack.push(card2);
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 verify(card1).resolve(player1, game);
 verify(card2).resolve(player2, game);
 }

 @Test
 public void testGivePriorityToCurrentPlayer_StackEmpty() {
 game.givePriorityToCurrentPlayer();
 // no stack resolution since the stack is empty
 }
}