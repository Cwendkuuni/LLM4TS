import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
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
 public static void setUp() {
 ip1 = new DummyUserInterface();
 ip2 = new DummyUserInterface();
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 Player expectedPlayer = player1;
 Player actualPlayer = game.getPlayingPlayer();
 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player expectedPlayer = player2;
 Player actualPlayer = game.getNonPlayingPlayer();
 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test
 public void testGetOtherPlayer() {
 Player expectedPlayer1 = player1;
 Player expectedPlayer2 = player2;
 Player actualPlayer1 = game.getOtherPlayer(expectedPlayer1);
 Player actualPlayer2 = game.getOtherPlayer(expectedPlayer2);
 assertEquals(expectedPlayer2, actualPlayer1);
 assertEquals(expectedPlayer1, actualPlayer2);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetPlayingPlayerWhenBothPlayersAreEmpty() {
 game = new Game();
 assertNull(game.getPlayingPlayer());
 }

 @Test
 public void testInit() {
 // Check if players and their interfaces are initialized
 assertEquals(ip1, game.getPlayer1().getUserInterface());
 assertEquals(ip2, game.getPlayer2().getUserInterface());
 // Check if players and their libraries are initialized
 assertFalse(player1.library.isEmpty());
 assertFalse(player2.library.isEmpty());
 // Check if the playing player is set correctly
 assertEquals(player1, game.getPlayingPlayer());
 // Check if the current phase is set correctly
 assertEquals(UntapPhase.class, game.currentPhase.getClass());
 // Check if the stacks are initialized correctly
 assertNotNull(game.stack);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 // Test when both players speak and there are no cards in the stack
 player1.speak("Test speak");
 player2.speak("Test speak");
 assertFalse(game.stack.isEmpty());
 Player expectedAP = player1;
 Player expectedNAP = player2;
 Player currentAP = game.getPlayingPlayer();
 Player currentNAP = game.getNonPlayingPlayer();
 assertEquals(expectedAP, currentAP);
 assertEquals(expectedNAP, currentNAP);
 // Test when one player speaks and the other doesn't
 player1.speak("Test speak");
 assertFalse(player2.letPlayerSpeak());
 Player expectedAP2 = player1;
 Player expectedNAP2 = player2;
 Player currentAP2 = game.getPlayingPlayer();
 Player currentNAP2 = game.getNonPlayingPlayer();
 assertEquals(expectedAP2, currentAP2);
 assertNotEquals(expectedAP2, currentNAP2);
 // Test when there are cards in the stack
 Card testCard = new Card("TestCard", CardType.ACTION);
 game.stack.push(testCard);
 player1.speak("Test speak");
 player2.speak("Test speak");
 testCard.resolve(game);
 Player expectedAP3 = player1;
 Player expectedNAP3 = player2;
 Player currentAP3 = game.getPlayingPlayer();
 Player currentNAP3 = game.getNonPlayingPlayer();
 assertEquals(expectedAP3, currentAP3);
 assertEquals(expectedNAP3, currentNAP3);
 }

 @AfterClass
 public static void tearDown() {
 ip1 = null;
 ip2 = null;
 player1 = null;
 player2 = null;
 game = null;
 deck1 = null;
 deck2 = null;
 }
}