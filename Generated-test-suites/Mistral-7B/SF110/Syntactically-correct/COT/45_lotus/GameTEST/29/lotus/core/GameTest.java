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
 public static void setupClass() {
 game = new Game();
 ip1 = new DummyUserInterface();
 ip2 = new DummyUserInterface();
 player1 = new Player(ip1, "Player1", new CardCollection());
 player2 = new Player(ip2, "Player2", new CardCollection());
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 Player actual = game.getPlayingPlayer();
 assertEquals("Player1", actual.getName());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player actual = game.getNonPlayingPlayer();
 assertEquals("Player2", actual.getName());
 }

 @Test
 public void testGetOtherPlayer() {
 Player p1 = game.getPlayingPlayer();
 Player p2 = game.getOtherPlayer(p1);
 assertEquals("Player2", p2.getName());
 Player p3 = game.getOtherPlayer(p2);
 assertEquals("Player1", p3.getName());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetPlayingPlayerWhenPlayersNotInitialized() {
 game = new Game();
 game.getPlayingPlayer();
 }

 @Test
 public void testInit() {
 assertEquals(2, game.getPlayers().size());
 assertEquals(2, game.getDecks().size());
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullUserInterface() {
 Game game = new Game();
 game.init(null, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullName1() {
 Game game = new Game();
 game.init(ip1, ip2, null, "Player2", deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullName2() {
 Game game = new Game();
 game.init(ip1, ip2, "Player1", null, deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullDeck1() {
 Game game = new Game();
 game.init(ip1, ip2, "Player1", "Player2", null, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullDeck2() {
 Game game = new Game();
 game.init(ip1, ip2, "Player1", "Player2", deck1, null);
 }

 @Test
 public void testGivePriorityToCurrentPlayerWhenBothPlayersSpeak() {
 game.givePriorityToCurrentPlayer();
 assertTrue(game.getStack().isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayerWhenCurrentPlayerSpeaksTwice() {
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 assertTrue(game.getStack().isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayerWhenNonPlayingPlayerSpeaks() {
 Card card = new Card("TestCard", CardType.ACTION, null);
 player1.addCardToHand(card);
 game.givePriorityToCurrentPlayer();
 game.giveNonPlayingPlayerAPriority();
 game.givePriorityToCurrentPlayer();
 Card actual = game.getStack().peek();
 assertEquals(card, actual);
 }

 @Test
 public void testGivePriorityToCurrentPlayerWhenStackIsEmpty() {
 game.givePriorityToCurrentPlayer();
 assertTrue(game.getStack().isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayerWhenThereIsAnUnresolvedCardOnStack() {
 Card card1 = new Card("TestCard1", CardType.ACTION, null);
 Card card2 = new Card("TestCard2", CardType.ACTION, null);
 player1.addCardToHand(card1);
 player2.addCardToHand(card2);
 game.stack.push(card1);
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 Card actual = game.getStack().peek();
 assertEquals(card2, actual);
 }

 @AfterClass
 public static void cleanup() {
 game = null;
 ip1 = null;
 ip2 = null;
 player1 = null;
 player2 = null;
 deck1 = null;
 deck2 = null;
 }

}