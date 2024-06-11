import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;

public class GameTest {

 private static Game game;
 private static UserInterface ip1;
 private static UserInterface ip2;
 private static String name1 = "Player1";
 private static String name2 = "Player2";
 private static CardCollection deck1;
 private static CardCollection deck2;

 @BeforeClass
 public static void setupClass() {
 Game.combat = null;
 Game.currentPhase = null;
 Game.playingPlayer = 0;
 Game.player1 = null;
 Game.player2 = null;
 Game.stack = new Stack();
 ip1 = new UserInterfaceStub();
 ip2 = new UserInterfaceStub();
 }

 @Before
 public void setup() {
 game = new Game();
 game.init(ip1, ip2, name1, name2, deck1 = new CardCollection(), deck2 = new CardCollection());
 }

 @Test
 public void testGetPlayingPlayer() {
 Player p1 = game.getPlayingPlayer();
 assertEquals(name1, p1.name);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player p2 = game.getNonPlayingPlayer();
 assertEquals(name2, p2.name);
 }

 @Test
 public void testGetOtherPlayer() {
 Player p1 = game.getOtherPlayer(game.player1);
 assertEquals(name2, p1.name);
 Player p2 = game.getOtherPlayer(game.player2);
 assertEquals(name1, p2.name);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetPlayingPlayerWhenGameIsNotInitialized() {
 game = new Game();
 try {
 Player p = game.getPlayingPlayer();
 fail("Expected IllegalStateException but got " + p);
 } catch (IllegalStateException e) {
 // pass
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testGetNonPlayingPlayerWhenGameIsNotInitialized() {
 game = new Game();
 try {
 Player p = game.getNonPlayingPlayer();
 fail("Expected IllegalStateException but got " + p);
 } catch (IllegalStateException e) {
 // pass
 }
 }

 @Test
 public void testInit() {
 assertEquals(2, game.getNumberOfPlayers());
 assertEquals(name1, game.getPlayingPlayer().name);
 assertEquals(name2, game.getNonPlayingPlayer().name);
 assertTrue(game.getPlayingPlayer().library.isEmpty());
 assertTrue(game.getNonPlayingPlayer().library.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testInitWhenNullUserInterfaceIsPassed() {
 try {
 Game game = new Game(null, ip2, name1, name2, deck1, deck2);
 fail("Expected NullPointerException but got a new Game object");
 } catch (NullPointerException e) {
 // pass
 }
 }

 @Test(expected = NullPointerException.class)
 public void testInitWhenNullNameIsPassed() {
 try {
 Game game = new Game(ip1, null, name1, name2, deck1, deck2);
 fail("Expected NullPointerException but got a new Game object");
 } catch (NullPointerException e) {
 // pass
 }
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 Player currentPlayer = game.getPlayingPlayer();
 Player nonCurrentPlayer = game.getNonPlayingPlayer();
 assertFalse(currentPlayer.speaking);
 assertFalse(nonCurrentPlayer.speaking);
 game.givePriorityToCurrentPlayer();
 assertTrue(currentPlayer.speaking);
 assertTrue(nonCurrentPlayer.speaking);
 currentPlayer.speak("I play a card");
 nonCurrentPlayer.speak("I respond");
 game.givePriorityToCurrentPlayer();
 assertTrue(currentPlayer.speaking);
 assertFalse(nonCurrentPlayer.speaking);
 }

 @Test
 public void testGivePriorityToCurrentPlayerWhenStackIsNotEmpty() {
 Card card = new Card("testCard", CardType.ACTION);
 game.stack.push(card);
 Player currentPlayer = game.getPlayingPlayer();
 Player nonCurrentPlayer = game.getNonPlayingPlayer();
 assertFalse(currentPlayer.speaking);
 assertFalse(nonCurrentPlayer.speaking);
 game.givePriorityToCurrentPlayer();
 assertFalse(currentPlayer.speaking);
 assertFalse(nonCurrentPlayer.speaking);
 game.stack.resolveLast();
 assertTrue(currentPlayer.speaking);
 assertFalse(nonCurrentPlayer.speaking());
 }

 @After
 public void tearDown() {
 game = null;
 ip1 = null;
 ip2 = null;
 }

}