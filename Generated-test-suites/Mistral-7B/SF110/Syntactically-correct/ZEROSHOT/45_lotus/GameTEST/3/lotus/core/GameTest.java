package lotus.core.test;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import lotus.core.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

 private UserInterface ip1;
 private UserInterface ip2;
 private Player player1;
 private Player player2;
 private CardCollection deck1;
 private CardCollection deck2;
 private Game game;

 @Before
 public void setUp() {
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 player1 = new Player(ip1, "Player1", new CardCollection());
 player2 = new Player(ip2, "Player2", new CardCollection());
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 assertEquals(player1, game.getPlayingPlayer());
 game.setPlayingPlayer(2);
 assertEquals(player2, game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 assertEquals(player2, game.getNonPlayingPlayer());
 game.setPlayingPlayer(1);
 assertEquals(player2, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 assertEquals(player2, game.getOtherPlayer(player1));
 assertEquals(player1, game.getOtherPlayer(player2));
 }

 @Test
 public void testInit() {
 verify(ip1, times(1)).init(player1);
 verify(ip2, times(1)).init(player2);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 game.setPlayingPlayer(1);
 verify(player1, times(2)).letPlayerSpeak();
 verify(player2, times(1)).letPlayerSpeak();
 game.givePriorityToCurrentPlayer();

 game.setPlayingPlayer(2);
 verify(player2, times(2)).letPlayerSpeak();
 verify(player1, times(1)).letPlayerSpeak();
 game.givePriorityToCurrentPlayer();

 Card card1 = new Card("Card1", "TestCard", null);
 player1.addToHand(card1);
 game.givePriorityToCurrentPlayer();
 verify(player1, times(1)).letPlayerSpeak();
 verify(card1, times(1)).resolveAbilities();

 Card card2 = new Card("Card2", "TestCard", null);
 player2.addToHand(card2);
 game.givePriorityToCurrentPlayer();
 verify(player2, times(1)).letPlayerSpeak();
 verify(card2, times(1)).resolveAbilities();
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayerWithEmptyStack() {
 game.setStack(new Stack());
 game.givePriorityToCurrentPlayer();
 }

 @Test
 public void testInitWithEmptyDecks() {
 game = new Game();
 game.init(ip1, ip2, "", "", new CardCollection(), new CardCollection());
 verify(ip1, times(1)).init(player1);
 verify(ip2, times(1)).init(player2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullUserInterface() {
 game = new Game();
 game.init(null, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullPlayerNames() {
 game = new Game();
 game.init(ip1, ip2, null, null, deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullDecks() {
 game = new Game();
 game.init(ip1, ip2, "Player1", "Player2", null, deck2);
 }
}