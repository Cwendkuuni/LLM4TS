package lotus.core.test;

import static org.junit.Assert.*;

import lotus.core.Game;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.stack.Stack;
import lotus.core.card.Card;
import lotus.core.Player;
import lotus.core.deck.CardCollection;

import org.junit.Before;
import org.junit.Test;

public class GameTest {

 private UserInterface ip1;
 private UserInterface ip2;
 private CardCollection deck1;
 private CardCollection deck2;
 private Player player1;
 private Player player2;
 private Game game;

 @Before
 public void setup() {
 ip1 = new StubUserInterface();
 ip2 = new StubUserInterface();
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 player1 = new Player(ip1, "Player1", deck1);
 player2 = new Player(ip2, "Player2", deck2);
 game = new Game();
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 Player actual = game.getPlayingPlayer();
 assertEquals(player1, actual);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player actual = game.getNonPlayingPlayer();
 assertEquals(player2, actual);
 }

 @Test
 public void testGetOtherPlayer() {
 Player actual = game.getOtherPlayer(player1);
 assertEquals(player2, actual);
 }

 @Test
 public void testInit() {
 assertEquals(ip1, game.getPlayingPlayer().getUserInterface());
 assertEquals(ip2, game.getNonPlayingPlayer().getUserInterface());
 assertEquals("Player1", game.getPlayingPlayer().getName());
 assertEquals("Player2", game.getNonPlayingPlayer().getName());

 int deck1Size = deck1.size();
 int deck2Size = deck2.size();

 for (int i = 0; i < deck1Size; i++) {
 Card card = deck1.get(i);
 assertEquals(player1, card.getOwner());
 assertEquals(Stack.Library, card.getZone());
 }

 for (int i = 0; i < deck2Size; i++) {
 Card card = deck2.get(i);
 assertEquals(player2, card.getOwner());
 assertEquals(Stack.Library, card.getZone());
 }
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 assertEquals(UntapPhase.class, game.currentPhase.getClass());
 assertTrue(game.getPlayingPlayer().hasPriority());
 assertTrue(game.getNonPlayingPlayer().hasNotPriority());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_LetPlayerSpeakFalse() {
 Player p = game.getPlayingPlayer();
 do {
 p.setLetPlayerSpeakReturnValue(false);
 p = game.getOtherPlayer(p);
 } while (!p.equals(game.getPlayingPlayer()));

 assertTrue(game.stack.isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_LetPlayerSpeakTrue() {
 Player p = game.getPlayingPlayer();
 do {
 p.setLetPlayerSpeakReturnValue(true);
 p = game.getOtherPlayer(p);
 } while (!p.equals(game.getPlayingPlayer()));

 Card card = new Card("TestCard", "TestCardType", null);
 card.owner = game.getPlayingPlayer();
 card.zone = Stack.Hand;
 game.getPlayingPlayer().hand.add(card);
 game.stack.push(card);

 assertEquals(card, game.stack.peek());
 }
}