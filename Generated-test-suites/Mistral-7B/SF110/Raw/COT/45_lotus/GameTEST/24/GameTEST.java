import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.exceptions.*;

public class GameTest {

 private static Player player1;
 private static Player player2;
 private static Game game;
 private static UserInterface ip1;
 private static UserInterface ip2;
 private static CardCollection deck1;
 private static CardCollection deck2;

 @BeforeClass
 public static void setup() {
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 Player playingPlayer = game.getPlayingPlayer();
 assertEquals(player1, playingPlayer);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player nonPlayingPlayer = game.getNonPlayingPlayer();
 assertEquals(player2, nonPlayingPlayer);
 }

 @Test
 public void testGetOtherPlayer() {
 Player otherPlayer = game.getOtherPlayer(player1);
 assertEquals(player2, otherPlayer);
 otherPlayer = game.getOtherPlayer(player2);
 assertEquals(player1, otherPlayer);
 }

 @Test
 public void testInit_ValidArguments() {
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 assertNotNull(game.player1);
 assertNotNull(game.player2);
 assertNotNull(game.currentPhase);
 assertTrue(game.stack.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testInit_NullArguments() {
 game.init(null, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInit_NullInterface1() {
 game.init(ip1, null, "Player1", "Player2", deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInit_NullInterface2() {
 game.init(ip1, ip2, null, "Player2", deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInit_NullName1() {
 game.init(ip1, ip2, null, "Player2", deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInit_NullName2() {
 game.init(ip1, ip2, "Player1", null, deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInit_NullDeck1() {
 game.init(ip1, ip2, "Player1", "Player2", null, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInit_NullDeck2() {
 game.init(ip1, ip2, "Player1", "Player2", deck1, null);
 }

 @Test
 public void testGivePriorityToCurrentPlayer_NoCardsOnStack() {
 game.givePriorityToCurrentPlayer();
 assertTrue(game.stack.isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_CardsOnStack() {
 game.stack.push(new Card("TestCard", CardType.ACTION, Player.PlayerType.NEUTRAL));
 game.givePriorityToCurrentPlayer();
 assertFalse(game.stack.isEmpty());
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayer_CurrentPlayerDiscarded() {
 player1.discardCard(new Card("TestCard", CardType.ACTION, Player.PlayerType.NEUTRAL));
 game.givePriorityToCurrentPlayer();
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayer_CurrentPlayerHasNoCards() {
 player1.discardAllCards();
 game.givePriorityToCurrentPlayer();
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayer_NonPlayingPlayerDiscarded() {
 player2.discardCard(new Card("TestCard", CardType.ACTION, Player.PlayerType.NEUTRAL));
 game.givePriorityToCurrentPlayer();
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayer_NonPlayingPlayerHasNoCards() {
 player2.discardAllCards();
 game.givePriorityToCurrentPlayer();
 }

}