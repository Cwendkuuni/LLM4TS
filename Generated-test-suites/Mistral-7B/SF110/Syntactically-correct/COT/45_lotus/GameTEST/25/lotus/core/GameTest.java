import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;

public class GameTest {

 private static UserInterface ip1 = new DummyUserInterface();
 private static UserInterface ip2 = new DummyUserInterface();
 private static String name1 = "Player1";
 private static String name2 = "Player2";
 private static CardCollection deck1 = new CardCollection();
 private static CardCollection deck2 = new CardCollection();
 private static Game game;

 @BeforeClass
 public static void setup() {
 Game.init(ip1, ip2, name1, name2, deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 Player playingPlayer = Game.getPlayingPlayer();
 assertEquals(name1, playingPlayer.name);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player nonPlayingPlayer = Game.getNonPlayingPlayer();
 assertEquals(name2, nonPlayingPlayer.name);
 }

 @Test
 public void testGetOtherPlayer() {
 Player p1 = Game.getOtherPlayer(Game.player1);
 assertEquals(name2, p1.name);

 Player p2 = Game.getOtherPlayer(Game.player2);
 assertEquals(name1, p2.name);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetOtherPlayerWhenBothPlayersAreNull() {
 Game.player1 = null;
 Game.player2 = null;
 Game.getOtherPlayer(Game.player1);
 }

 @Test
 public void testInit() {
 assertEquals(2, Game.getNumberOfPlayers());
 assertEquals(name1, Game.player1.name);
 assertEquals(name2, Game.player2.name);

 assertEquals(deck1.size(), Game.player1.library.size());
 for (Card card : deck1) {
 assertEquals(Game.player1, card.owner);
 }

 assertEquals(deck2.size(), Game.player2.library.size());
 for (Card card : deck2) {
 assertEquals(Game.player2, card.owner);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullUserInterfaces() {
 Game.init(null, null, name1, name2, deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullNames() {
 Game.init(ip1, ip2, null, null, deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullDecks() {
 Game.init(ip1, ip2, name1, name2, null, null);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 boolean apDone = false;
 boolean napDone = false;
 do {
 apDone = Game.getPlayingPlayer().letPlayerSpeak();
 napDone = Game.getNonPlayingPlayer().letPlayerSpeak();
 } while (!apDone || !napDone);

 assertTrue(Game.stack.empty());
 }

 @Test
 public void testGivePriorityToCurrentPlayerWithCardsOnStack() {
 Card card1 = new Card("TestCard1");
 Card card2 = new Card("TestCard2");
 Game.stack.push(card1);
 Game.stack.push(card2);

 boolean apDone = false;
 boolean napDone = false;
 do {
 apDone = Game.getPlayingPlayer().letPlayerSpeak();
 napDone = Game.getNonPlayingPlayer().letPlayerSpeak();
 } while (!apDone || !napDone);

 assertEquals(card2.name, Game.stack.last().name);
 }
}