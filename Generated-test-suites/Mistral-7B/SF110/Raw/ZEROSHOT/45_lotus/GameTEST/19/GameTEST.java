package lotus.core.test;

import static org.junit.Assert.*;

import lotus.core.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import java.util.*;

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
 MockitoAnnotations.initMocks(this);
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 player1 = new Player(ip1, "player1");
 player2 = new Player(ip2, "player2");
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
 game.init(ip1, ip2, "player1", "player2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 assertEquals(player1, game.getPlayingPlayer());
 game.playingPlayer = 2;
 assertEquals(player2, game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 assertEquals(player2, game.getNonPlayingPlayer());
 game.playingPlayer = 1;
 assertEquals(player1, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 assertEquals(player2, game.getOtherPlayer(player1));
 assertEquals(player1, game.getOtherPlayer(player2));
 }

 @Test
 public void testInit() {
 List<Card> player1Deck = new ArrayList<>();
 List<Card> player2Deck = new ArrayList<>();
 for (int i = 0; i < 10; i++) {
 player1Deck.add(new Card("card" + i, "type" + i, null));
 player2Deck.add(new Card("card" + i + 10, "type" + i + 10, null));
 }
 game.init(ip1, ip2, "player1", "player2", new CardCollection(player1Deck), new CardCollection(player2Deck));
 assertEquals(player1, game.getPlayingPlayer());
 assertEquals(player2, game.getNonPlayingPlayer());
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 Mockito.when(ip1.letPlayerSpeak()).thenReturn(true);
 Mockito.when(ip2.letPlayerSpeak()).thenReturn(false);
 game.givePriorityToCurrentPlayer();
 verify(ip1, times(1)).letPlayerSpeak();
 verify(ip2, never()).letPlayerSpeak();
 }

 @Test
 public void testGivePriorityToCurrentPlayer_BothPlayersSpeak() {
 Mockito.when(ip1.letPlayerSpeak()).thenReturn(true);
 Mockito.when(ip2.letPlayerSpeak()).thenReturn(true);
 game.givePriorityToCurrentPlayer();
 verify(ip1, times(1)).letPlayerSpeak();
 verify(ip2, times(1)).letPlayerSpeak();
 }

 @Test
 public void testGivePriorityToCurrentPlayer_StackResolvesLast() {
 Card card = new Card("cardToResolve", "typeToResolve", null);
 game.stack.push(card);
 Mockito.when(ip1.letPlayerSpeak()).thenReturn(true);
 Mockito.when(ip2.letPlayerSpeak()).thenReturn(false);
 game.givePriorityToCurrentPlayer();
 verify(ip1, times(1)).letPlayerSpeak();
 verify(ip2, never()).letPlayerSpeak();
 verify(card).resolve();
 }
}