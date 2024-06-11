package lotus.core;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameTest {

 private UserInterface ip1;
 private UserInterface ip2;
 private Player player1;
 private Player player2;
 private Game game;
 private CardCollection deck1;
 private CardCollection deck2;
 private Stack stack;

 @Before
 public void setUp() {
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 player1 = new Player(ip1, "Player 1", new CardCollection());
 player2 = new Player(ip2, "Player 2", new CardCollection());
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
 game.init(ip1, ip2, "Player 1", "Player 2", deck1, deck2);
 stack = new Stack();
 game.stack = stack;
 }

 @Test
 public void testGetPlayingPlayer() {
 Assert.assertEquals(player1, game.getPlayingPlayer());
 game.playingPlayer = 2;
 Assert.assertEquals(player2, game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Assert.assertEquals(player2, game.getNonPlayingPlayer());
 game.playingPlayer = 1;
 Assert.assertEquals(player1, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 Assert.assertEquals(player2, game.getOtherPlayer(player1));
 Assert.assertEquals(player1, game.getOtherPlayer(player2));
 }

 @Test
 public void testInit() {
 List<Card> deck1Cards = new ArrayList<>();
 deck1Cards.add(Mockito.mock(Card.class));
 game.init(ip1, ip2, "Player 1", "Player 2", new CardCollection(deck1Cards), new CardCollection());

 List<Card> player1Library = player1.getLibrary();
 List<Card> player2Library = player2.getLibrary();

 Assert.assertEquals(player1, game.getPlayingPlayer());
 Assert.assertEquals(player2, game.getNonPlayingPlayer());
 Assert.assertEquals(ip1, player1.getUserInterface());
 Assert.assertEquals(ip2, player2.getUserInterface());
 Assert.assertEquals(deck1Cards, player1.getLibrary());
 Assert.assertNull(player1.getGraveyard());
 Assert.assertNull(player1.getHand());
 Assert.assertEquals(Collections.emptyList(), player1.getExile());

 Assert.assertEquals(player2, game.getNonPlayingPlayer());
 Assert.assertEquals(ip2, player2.getUserInterface());
 Assert.assertEquals(Collections.emptyList(), player2.getLibrary());
 Assert.assertNull(player2.getGraveyard());
 Assert.assertNull(player2.getHand());
 Assert.assertEquals(Collections.emptyList(), player2.getExile());
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 player1.letPlayerSpeak = Mockito.mock(Boolean.class.getMethod(0).getType()).asSubclass(Boolean.class);
 player2.letPlayerSpeak = Mockito.mock(Boolean.class.getMethod(0).getType()).asSubclass(Boolean.class);

 verify(player1, Mockito.times(1)).letPlayerSpeak();
 verify(player2, Mockito.times(1)).letPlayerSpeak();

 game.givePriorityToCurrentPlayer();
 }
}