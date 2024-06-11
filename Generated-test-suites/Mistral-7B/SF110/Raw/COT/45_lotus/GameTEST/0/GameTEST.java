package lotus.core.test;

import org.junit.Assert;
import org.junit.Test;
import lotus.core.Game;
import lotus.core.Player;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameTest {

 private UserInterface ip1;
 private UserInterface ip2;
 private Player player1;
 private Player player2;
 private CardCollection deck1;
 private CardCollection deck2;

 @Before
 public void setUp() {
 ip1 = new DummyUserInterface();
 ip2 = new DummyUserInterface();
 player1 = new Player(ip1, "Player1", new CardCollection());
 player2 = new Player(ip2, "Player2", new CardCollection());
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 }

 @After
 public void tearDown() {
 Game.currentPhase = null;
 Game.playingPlayer = 0;
 }

 @Test
 public void testGetPlayingPlayer() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 Assert.assertEquals(player1, Game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 Assert.assertEquals(player2, Game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 Assert.assertEquals(player2, Game.getOtherPlayer(player1));
 Assert.assertEquals(player1, Game.getOtherPlayer(player2));
 }

 @Test
 public void testInit() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 Assert.assertNotNull(Game.player1);
 Assert.assertNotNull(Game.player2);
 Assert.assertEquals(player1, Game.getPlayingPlayer());
 Assert.assertEquals(player2, Game.getNonPlayingPlayer());
 Assert.assertEquals(UntapPhase.class, Game.currentPhase.getClass());
 }

 @Test(expected = NullPointerException.class)
 public void testInitNullUserInterface() {
 Game.init(null, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitNullPlayer() {
 Game.init(ip1, null, "Player1", "Player2", deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitNullDeck() {
 Game.init(ip1, ip2, "Player1", "Player2", null, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitNullDeck2() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, null);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 List<Card> player1Cards = new ArrayList<>(Arrays.asList(new Card("Card1"), new Card("Card2")));
 List<Card> player2Cards = new ArrayList<>(Arrays.asList(new Card("Card3"), new Card("Card4")));
 deck1.addAll(player1Cards);
 deck2.addAll(player2Cards);
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 // Simulate player actions
 player1.playCard(player1Cards.get(0));
 player2.playCard(player2Cards.get(0));
 player1.pass();
 player2.pass();
 // Test giving priority to current player
 Game.givePriorityToCurrentPlayer();
 // Check that the last action was performed by the current player
 Assert.assertEquals(player1, Game.getPlayingPlayer());
 Assert.assertEquals(player2, Game.getNonPlayingPlayer());
 // Check that the last action was resolved
 Assert.assertEquals(player1Cards.get(0), Game.stack.peek());
 }

 @Test
 public void testGivePriorityToCurrentPlayerEmptyStack() {
 Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 // Test giving priority to current player with empty stack
 Game.givePriorityToCurrentPlayer();
 // Check that no action was performed and the players remain the same
 Assert.assertEquals(player1, Game.getPlayingPlayer());
 Assert.assertEquals(player2, Game.getNonPlayingPlayer());
 Assert.assertEquals(Collections.emptyList(), Game.stack.getStack());
 }
}