import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import lotus.core.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

 private Player player1;
 private Player player2;
 private UserInterface ip1;
 private UserInterface ip2;
 private CardCollection deck1;
 private CardCollection deck2;
 private Game game;

 @Before
 public void setup() {
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 player1 = new Player(ip1, "Player1", deck1);
 player2 = new Player(ip2, "Player2", deck2);
 game = new Game();
 }

 @Test
 public void testGetPlayingPlayer() {
 // Test normal case
 Player expectedPlayer = player1;
 Player actualPlayer = game.getPlayingPlayer();
 assertEquals(expectedPlayer, actualPlayer);

 // Test when playingPlayer is 2
 game.playingPlayer = 2;
 expectedPlayer = player2;
 actualPlayer = game.getPlayingPlayer();
 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 // Test normal case
 Player expectedPlayer = player2;
 Player actualPlayer = game.getNonPlayingPlayer();
 assertEquals(expectedPlayer, actualPlayer);

 // Test when playingPlayer is 1
 game.playingPlayer = 1;
 expectedPlayer = player1;
 actualPlayer = game.getNonPlayingPlayer();
 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test
 public void testGetOtherPlayer() {
 // Test normal case
 Player expectedPlayer = player2;
 Player actualPlayer = game.getOtherPlayer(player1);
 assertEquals(expectedPlayer, actualPlayer);

 // Test when player1 is otherPlayer
 expectedPlayer = player1;
 actualPlayer = game.getOtherPlayer(player2);
 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test(expected = IllegalStateException.class)
 public void testInitWithNullUserInterface() {
 game.init(null, ip2, "Name1", "Name2", deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullPlayerDeck() {
 game.init(ip1, ip2, "Name1", "Name2", null, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullNonPlayerDeck() {
 game.init(ip1, ip2, "Name1", "Name2", deck1, null);
 }

 @Test
 public void testInit() {
 // Test that players and user interfaces are initialized
 assertNotNull(game.player1);
 assertNotNull(game.player2);
 assertNotNull(game.ip1);
 assertNotNull(game.ip2);

 // Test that players' libraries are initialized
 assertNotNull(game.player1.library);
 assertNotNull(game.player2.library);

 // Test that playingPlayer is set to 1 and currentPhase is set to UntapPhase
 assertEquals(1, game.playingPlayer);
 assertInstanceOf(UntapPhase.class, game.currentPhase);

 // Test that deck1 and deck2 cards are added to their respective player's libraries
 Card testCard1 = new Card("Card1", CardType.ACTION, Player.Color.BLUE);
 Card testCard2 = new Card("Card2", CardType.ACTION, Player.Color.RED);

 deck1.addCard(testCard1);
 deck1.addCard(testCard2);

 when(ip1.init(player1)).thenReturn(true);
 when(ip2.init(player2)).thenReturn(true);

 game.init(ip1, ip2, "Name1", "Name2", deck1, deck2);

 assertEquals(testCard1, player1.library.getCards().get(0));
 assertEquals(testCard2, player2.library.getCards().get(0));
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 // Test that letPlayerSpeak returns false when player speaks
 when(ip1.letPlayerSpeak()).thenReturn(false);
 when(ip2.letPlayerSpeak()).thenReturn(true);

 game.init(ip1, ip2, "Name1", "Name2", deck1, deck2);
 game.init(ip1, ip2, "Name1", "Name2", deck1, deck2);

 game.givePriorityToCurrentPlayer();

 verify(ip1, times(1)).letPlayerSpeak();
 verify(ip2, times(1)).letPlayerSpeak();
 verifyNoMoreInteractions();

 // Test that stack is resolved when it is not empty
 Card testCard = new Card("TestCard", CardType.ACTION, Player.Color.BLUE);
 game.stack.push(testCard);

 when(ip1.letPlayerSpeak()).thenReturn(true);
 when(ip2.letPlayerSpeak()).thenReturn(false);

 game.givePriorityToCurrentPlayer();

 assertEquals(testCard, game.stack.top());
 game.stack.pop();

 verify(ip1, times(1)).letPlayerSpeak();
 verify(ip2, times(1)).letPlayerSpeak();
 verifyNoMoreInteractions();
 }
}