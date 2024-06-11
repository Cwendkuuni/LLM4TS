import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import lotus.core.*;
import java.util.*;

public class GameTest {

 private static Player player1;
 private static Player player2;
 private static Game game;
 private UserInterface ip1;
 private UserInterface ip2;

 @Before
 public void setUp() {
 ip1 = Mockito.mock(UserInterface.class);
 ip2 = Mockito.mock(UserInterface.class);
 player1 = new Player(ip1, "Player1", new CardCollection());
 player2 = new Player(ip2, "Player2", new CardCollection());
 game = new Game();
 game.init(ip1, ip2, "Player1", "Player2", new CardCollection(), new CardCollection());
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
 List<Card> deck1 = Arrays.asList(new Card("Card1"), new Card("Card2"), new Card("Card3"));
 List<Card> deck2 = Arrays.asList(new Card("Card4"), new Card("Card5"), new Card("Card6"));
 game.init(ip1, ip2, "Player1", "Player2", new CardCollection(deck1), new CardCollection(deck2));
 assertEquals(player1, game.getPlayingPlayer());
 assertEquals(player2, game.getNonPlayingPlayer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullIP() {
 game = new Game();
 game.init(null, ip2, "Player1", "Player2", new CardCollection(), new CardCollection());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullName1() {
 game = new Game();
 game.init(ip1, ip2, null, "Player2", new CardCollection(), new CardCollection());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullName2() {
 game = new Game();
 game.init(ip1, ip2, "Player1", null, new CardCollection(), new CardCollection());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullDeck1() {
 game = new Game();
 game.init(ip1, ip2, "Player1", "Player2", null, new CardCollection());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullDeck2() {
 game = new Game();
 game.init(ip1, ip2, "Player1", "Player2", new CardCollection(), null);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 game.playingPlayer = 1;
 Mockito.when(player1.letPlayerSpeak()).thenReturn(true);
 Mockito.when(player2.letPlayerSpeak()).thenReturn(false);
 game.givePriorityToCurrentPlayer();
 verify(player1).speak();
 verify(player2).speak();
 // Check stack resolution
 Card card = new Card("Card");
 Stack stack = new Stack();
 stack.push(card);
 game.stack = stack;
 game.playingPlayer = 1;
 game.givePriorityToCurrentPlayer();
 verify(card).resolve();
 }

 @Test
 public void testGivePriorityToCurrentPlayer_EmptyStack() {
 game.playingPlayer = 1;
 game.givePriorityToCurrentPlayer();
 // Nothing should be executed, as the stack is empty
 verifyNoInteractions(player1);
 verifyNoInteractions(player2);
 }
}