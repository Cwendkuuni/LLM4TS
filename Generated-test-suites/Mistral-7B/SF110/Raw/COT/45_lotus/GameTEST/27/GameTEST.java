package lotus.core.test;

import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

 private static UserInterface ip1;
 private static UserInterface ip2;
 private static Player player1;
 private static Player player2;
 private static Game game;
 private static CardCollection deck1;
 private static CardCollection deck2;

 @BeforeClass
 public static void setupClass() {
 ip1 = new StubUserInterface();
 ip2 = new StubUserInterface();
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
 }

 @Test
 public void testGetPlayingPlayer() {
 Player expectedPlayer1 = new Player(ip1, "Player1Name", deck1);
 Player expectedPlayer2 = new Player(ip2, "Player2Name", deck2);
 game.init(ip1, ip2, "Player1Name", "Player2Name", deck1, deck2);
 Player actualPlayer = game.getPlayingPlayer();
 assertEquals(expectedPlayer1, actualPlayer);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player expectedPlayer1 = new Player(ip1, "Player1Name", deck1);
 Player expectedPlayer2 = new Player(ip2, "Player2Name", deck2);
 game.init(ip1, ip2, "Player1Name", "Player2Name", deck1, deck2);
 Player actualPlayer = game.getNonPlayingPlayer();
 assertEquals(expectedPlayer2, actualPlayer);
 }

 @Test
 public void testGetOtherPlayer() {
 Player expectedPlayer1 = new Player(ip1, "Player1Name", deck1);
 Player expectedPlayer2 = new Player(ip2, "Player2Name", deck2);
 Player actualPlayer = game.getOtherPlayer(expectedPlayer1);
 assertEquals(expectedPlayer2, actualPlayer);
 }

 @Test
 public void testInitWithNullUserInterface() {
 assertNull(game.player1);
 assertNull(game.player2);

 try {
 game.init(null, ip2, "Player1Name", "Player2Name", deck1, deck2);
 fail("Should have thrown NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testInitWithNullDeck() {
 Player expectedPlayer1 = new Player(ip1, "Player1Name", deck1);
 Player expectedPlayer2 = new Player(ip2, "Player2Name", null);

 try {
 game.init(ip1, ip2, "Player1Name", "Player2Name", deck1, null);
 fail("Should have thrown NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }

 assertNull(game.player2);
 }

 @Test
 public void testGivePriorityToCurrentPlayer_NormalCase() {
 Card testCard = new Card("TestCard", Color.BLUE, Cost.ONE_MANA, new Ability(new AbilityType("TestAbility"), null));
 deck1.add(testCard);
 deck2.add(testCard);
 game.init(ip1, ip2, "Player1Name", "Player2Name", deck1, deck2);
 game.givePriorityToCurrentPlayer();
 assertTrue(testCard.isInPlayedZone(game.player1));
 assertFalse(testCard.isInPlayedZone(game.player2));
 }

 @Test
 public void testGivePriorityToCurrentPlayer_StackResolution() {
 Card testCard1 = new Card("TestCard1", Color.BLUE, Cost.ONE_MANA, new Ability(new AbilityType("TestAbility1"), null));
 Card testCard2 = new Card("TestCard2", Color.RED, Cost.ONE_MANA, new Ability(new AbilityType("TestAbility2"), null));
 deck1.add(testCard1);
 deck1.add(testCard2);
 deck2.add(testCard1);
 deck2.add(testCard2);
 game.init(ip1, ip2, "Player1Name", "Player2Name", deck1, deck2);
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 assertTrue(testCard1.isInPlayedZone(game.player1));
 assertTrue(testCard2.isInPlayedZone(game.player2));
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayer_StackNotEmpty() {
 Card testCard = new Card("TestCard", Color.BLUE, Cost.ONE_MANA, new Ability(new AbilityType("TestAbility"), null));
 deck1.add(testCard);
 deck2.add(testCard);
 game.init(ip1, ip2, "Player1Name", "Player2Name", deck1, deck2);
 game.stack.push(new Card("TestStackCard", Color.GREEN, Cost.ONE_MANA, new Ability(new AbilityType("TestStackAbility"), null)));
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 game.stack.resolveLast();
 game.givePriorityToCurrentPlayer();
 fail("Should have thrown IllegalStateException");
 }
}