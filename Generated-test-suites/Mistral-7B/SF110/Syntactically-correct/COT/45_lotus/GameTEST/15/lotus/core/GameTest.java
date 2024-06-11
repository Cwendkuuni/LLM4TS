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
 private CardCollection deck1;
 private CardCollection deck2;

 @Test
 public void testGetPlayingPlayer() {
 Player expectedPlayer;
 if (Game.playingPlayer == 1) {
 expectedPlayer = Game.player1;
 } else {
 expectedPlayer = Game.player2;
 }
 assertEquals("Expected wrong playing player", expectedPlayer, Game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player expectedPlayer;
 if (Game.playingPlayer == 1) {
 expectedPlayer = Game.player2;
 } else {
 expectedPlayer = Game.player1;
 }
 assertEquals("Expected wrong non-playing player", expectedPlayer, Game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 Player p = new Player(null, null, null); // any instance
 Player expectedPlayer;
 if (p == Game.player1) {
 expectedPlayer = Game.player2;
 } else {
 expectedPlayer = Game.player1;
 }
 assertEquals("Expected wrong other player", expectedPlayer, Game.getOtherPlayer(p));
 }

 @Test
 public void testInit() {
 // Initialize with valid arguments
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 assertNotNull("Game.player1 should not be null", Game.player1);
 assertNotNull("Game.player2 should not be null", Game.player2);
 assertEquals("Player1 name should be correct", "Player1Name", Game.player1.getName());
 assertEquals("Player2 name should be correct", "Player2Name", Game.player2.getName());

 // Check if IPs are initialized correctly
 UserInterface ip1 = Game.player1.getUserInterface();
 UserInterface ip2 = Game.player2.getUserInterface();
 assertNotNull("Player1 IP should not be null", ip1);
 assertNotNull("Player2 IP should not be null", ip2);

 // Test library cards
 Card card1 = new Card("TestCard", "TestType", null);
 Card card2 = new Card("TestCard2", "TestType2", null);
 deck1.addCard(card1);
 deck2.addCard(card2);
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", "Player2Name", deck1, deck2);
 List<Card> player1Library = Game.player1.library.getCards();
 List<Card> player2Library = Game.player2.library.getCards();
 assertEquals("Player1 library cards count should be correct", 1, player1Library.size());
 assertEquals("Player2 library cards count should be correct", 1, player2Library.size());
 assertEquals("Player1 library card should be correct", card1, player1Library.get(0));
 assertEquals("Player2 library card should be correct", card2, player2Library.get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullUserInterfaces() {
 Game.init(null, null, "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullNames() {
 Game.init(new DummyUserInterface(), new DummyUserInterface(), null, null, new CardCollection(), new CardCollection());
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullDecks() {
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", "Player2Name", null, new CardCollection());
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 // Given: Players have cards in their libraries
 player1 = new Player(new DummyUserInterface(), "Player1", new CardCollection());
 player2 = new Player(new DummyUserInterface(), "Player2", new CardCollection());
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
 player1.library.addCard(new Card("TestCard", "TestType", null));
 player2.library.addCard(new Card("TestCard2", "TestType2", null));

 // When: Give priority to current player
 Game.givePriorityToCurrentPlayer();

 // Then: Check that the last played card is resolved
 assertEquals("Last played card should be from the current player", player1.library.getCards().get(0), Game.stack.getTopCard());

 // When: Give priority to the current player again
 Game.givePriorityToCurrentPlayer();
 // Then: Check that no card is resolved, as the stack is empty
 assertNull("No card should be resolved", Game.stack.getTopCard());
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayer_WhenStackIsEmptyButPlayersHaveCards() {
 // Given: Players have cards in their libraries but the stack is empty
 player1 = new Player(new DummyUserInterface(), "Player1", new CardCollection());
 player2 = new Player(new DummyUserInterface(), "Player2", new CardCollection());
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
 player1.library.addCard(new Card("TestCard", "TestType", null));
 player2.library.addCard(new Card("TestCard2", "TestType2", null));

 // When: Give priority to the current player
 Game.givePriorityToCurrentPlayer();

 // Then: An exception should be thrown as the stack is empty and players have cards to play
 }
}