package lotus.core.test;

import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

 private static UserInterface ip1 = new DummyUserInterface();
 private static UserInterface ip2 = new DummyUserInterface();
 private static String name1 = "Player1";
 private static String name2 = "Player2";
 private static CardCollection deck1 = new CardCollection();
 private static CardCollection deck2 = new CardCollection();

 @Test
 public void testGetPlayingPlayer() {
     Game.init(ip1, ip2, name1, name2, deck1, deck2);
     Player playingPlayer = Game.getPlayingPlayer();
     assertTrue(playingPlayer.getName().equals(name1));
 }

 @Test
 public void testGetNonPlayingPlayer() {
     Game.init(ip1, ip2, name1, name2, deck1, deck2);
     Player nonPlayingPlayer = Game.getNonPlayingPlayer();
     assertTrue(nonPlayingPlayer.getName().equals(name2));
 }

 @Test
 public void testGetOtherPlayer() {
     Game.init(ip1, ip2, name1, name2, deck1, deck2);
     Player p1 = Game.player1;
     Player p2 = Game.player2;
     Player otherPlayer = Game.getOtherPlayer(p1);
     assertTrue(otherPlayer.getName().equals(name2));

     otherPlayer = Game.getOtherPlayer(p2);
     assertTrue(otherPlayer.getName().equals(name1));
 }

 @Test
 public void testInit() {
     Game.init(ip1, ip2, name1, name2, deck1, deck2);
     assertEquals(2, Game.player1.getDeckSize());
     assertEquals(2, Game.player2.getDeckSize());
     assertNull(Game.player1.library.getSize());
     assertNull(Game.player2.library.getSize());
 }

 @Test(expected = NullPointerException.class)
 public void testInitNullUserInterface() {
     Game.init(null, ip2, name1, name2, deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitNullName1() {
     Game.init(ip1, ip2, null, name2, deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitNullName2() {
     Game.init(ip1, ip2, name1, null, deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitNullDeck1() {
     Game.init(ip1, ip2, name1, name2, null, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitNullDeck2() {
     Game.init(ip1, ip2, name1, name2, deck1, null);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
     Game.init(ip1, ip2, name1, name2, deck1, deck2);
     Game.givePriorityToCurrentPlayer();
     assertTrue(Game.stack.empty());
 }

 @Test
 public void testGivePriorityToCurrentPlayerWithCardsOnStack() {
     Game.init(ip1, ip2, name1, name2, deck1, deck2);
     Card card1 = new Card();
     Card card2 = new Card();
     Game.stack.push(card1);
     Game.stack.push(card2);
     Game.givePriorityToCurrentPlayer();
     assertFalse(Game.stack.empty());
     Card resolvedCard = Game.stack.resolveLast();
     assertNotNull(resolvedCard);
     Game.givePriorityToCurrentPlayer();
     assertFalse(Game.stack.empty());
     resolvedCard = Game.stack.resolveLast();
     assertNotNull(resolvedCard);
 }
}