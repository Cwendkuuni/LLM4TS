import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;

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
     assertSame(playingPlayer, Game.player1);
 }

 @Test
 public void testGetNonPlayingPlayer() {
     Game.init(ip1, ip2, name1, name2, deck1, deck2);
     Player nonPlayingPlayer = Game.getNonPlayingPlayer();
     assertSame(nonPlayingPlayer, Game.player2);
 }

 @Test
 public void testGetOtherPlayer() {
     Game.init(ip1, ip2, name1, name2, deck1, deck2);
     Player p = Game.player1;
     Player otherPlayer = Game.getOtherPlayer(p);
     assertSame(otherPlayer, Game.player2);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetPlayingPlayerBeforeInit() {
     Player playingPlayer = Game.getPlayingPlayer();
     fail("Game should not be initialized before calling init() method");
 }

 @Test
 public void testInit() {
     Game.init(ip1, ip2, name1, name2, deck1, deck2);
     assertEquals(2, Game.playerCount());
     assertEquals(Game.player1, Game.getPlayingPlayer());
     assertEquals(Game.player2, Game.getNonPlayingPlayer());
     assertTrue(Game.getPlayingPlayer().getName().equals(name1));
     assertTrue(Game.getNonPlayingPlayer().getName().equals(name2));
 }

 @Test
 public void testGivePriorityToCurrentPlayerEmptyStack() {
     Game.init(ip1, ip2, name1, name2, deck1, deck2);
     Game.givePriorityToCurrentPlayer();
     assertTrue(Game.stack.isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayerNonEmptyStack() {
     Game.init(ip1, ip2, name1, name2, deck1, deck2);
     Card card1 = new Card("TestCard1", CardType.ACTION, 0, 0);
     Card card2 = new Card("TestCard2", CardType.ACTION, 0, 0);
     Game.stack.push(card1);
     Game.stack.push(card2);
     Game.givePriorityToCurrentPlayer();
     assertFalse(Game.stack.isEmpty());
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayerNullStack() {
     Game.init(ip1, ip2, name1, name2, deck1, deck2);
     Game.stack = null;
     Game.givePriorityToCurrentPlayer();
     fail("Game stack should not be null");
 }

}