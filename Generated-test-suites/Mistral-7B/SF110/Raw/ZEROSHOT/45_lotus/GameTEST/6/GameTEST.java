package lotus.core.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import lotus.core.*;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class GameTest {

 private UserInterface ip1;
 private UserInterface ip2;
 private Player player1;
 private Player player2;
 private Game game;

 @Before
 public void setUp() {
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 player1 = new Player(ip1, "player1", new CardCollection());
 player2 = new Player(ip2, "player2", new CardCollection());
 game = new Game();
 }

 @Test
 public void testInit() {
 // Given
 CardCollection deck1 = new CardCollection();
 CardCollection deck2 = new CardCollection();

 // When
 game.init(ip1, ip2, "player1Name", "player2Name", deck1, deck2);

 // Then
 assertEquals(player1, game.getPlayingPlayer());
 assertEquals(player2, game.getNonPlayingPlayer());
 assertNotNull(game.getCurrentPhase());
 assertEquals(UntapPhase.class, game.getCurrentPhase().getClass());
 assertEquals(player1, game.getPlayingPlayer().getOwner());
 assertEquals(player2, game.getNonPlayingPlayer().getOwner());
 assertEquals(deck1, player1.getLibrary());
 assertEquals(deck2, player2.getLibrary());
 }

 @Test
 public void testGetPlayingPlayer() {
 // Given
 game.init(ip1, ip2, "player1Name", "player2Name", new CardCollection(), new CardCollection());

 // When
 // Then
 assertEquals(player1, game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 // Given
 game.init(ip1, ip2, "player1Name", "player2Name", new CardCollection(), new CardCollection());

 // When
 // Then
 assertEquals(player2, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 // Given
 game.init(ip1, ip2, "player1Name", "player2Name", new CardCollection(), new CardCollection());

 // When
 // Then
 assertEquals(player2, game.getOtherPlayer(player1));
 assertEquals(player1, game.getOtherPlayer(player2));
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 // Given
 game.init(ip1, ip2, "player1Name", "player2Name", new CardCollection(), new CardCollection());
 List<Card> cardsPlayer1 = new ArrayList<>();
 List<Card> cardsPlayer2 = new ArrayList<>();

 // When
 for (int i = 0; i < 5; i++) {
 cardsPlayer1.add(new Card());
 cardsPlayer2.add(new Card());
 player1.addToHand(cardsPlayer1.get(i));
 player2.addToHand(cardsPlayer2.get(i));
 }

 // When
 game.givePriorityToCurrentPlayer();

 // Then
 // TODO: Implement assertions to test the stack and letPlayerSpeak() methods
 }
}