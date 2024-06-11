import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.interfaces.*;

public class GameTest {

 private static UserInterface ip1 = new DummyUserInterface();
 private static UserInterface ip2 = new DummyUserInterface();
 private static String name1 = "Player1";
 private static String name2 = "Player2";
 private static CardCollection deck1 = new CardCollection();
 private static CardCollection deck2 = new CardCollection();
 private static Game game;

 @Test
 public void testGetPlayingPlayer() {
     game = new Game();
     Player p1 = new Player(ip1, name1, deck1);
     Player p2 = new Player(ip2, name2, deck2);
     game.init(ip1, ip2, name1, name2, deck1, deck2);
     assertEquals(p1, game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
     game = new Game();
     Player p1 = new Player(ip1, name1, deck1);
     Player p2 = new Player(ip2, name2, deck2);
     game.init(ip1, ip2, name1, name2, deck1, deck2);
     assertEquals(p2, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
     game = new Game();
     Player p1 = new Player(ip1, name1, deck1);
     Player p2 = new Player(ip2, name2, deck2);
     game.init(ip1, ip2, name1, name2, deck1, deck2);
     assertEquals(p2, game.getOtherPlayer(p1));
     assertEquals(p1, game.getOtherPlayer(p2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullUserInterface() {
     game = new Game();
     game.init(null, ip2, name1, name2, deck1, deck2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullName1() {
     game = new Game();
     game.init(ip1, null, null, name2, deck1, deck2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullName2() {
     game = new Game();
     game.init(ip1, ip2, null, null, deck1, deck2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullDeck1() {
     game = new Game();
     game.init(ip1, ip2, name1, name2, null, deck2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullDeck2() {
     game = new Game();
     game.init(ip1, ip2, name1, name2, deck1, null);
 }

 @Test
 public void testInit() {
     game = new Game();
     UserInterface ip = new DummyUserInterface();
     Player p1 = new Player(ip, name1, deck1);
     Player p2 = new Player(ip, name2, deck2);
     game.init(ip, ip, name1, name2, deck1, deck2);
     assertEquals(p1, game.getPlayingPlayer());
     assertEquals(p2, game.getNonPlayingPlayer());
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
     game = new Game();
     UserInterface ip = new DummyUserInterface();
     Player p1 = new Player(ip, name1, deck1);
     Player p2 = new Player(ip, name2, deck2);
     game.init(ip, ip, name1, name2, deck1, deck2);
     game.givePriorityToCurrentPlayer();
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayerEmptyStack() {
     game = new Game();
     UserInterface ip = new DummyUserInterface();
     Player p1 = new Player(ip, name1, deck1);
     Player p2 = new Player(ip, name2, deck2);
     game.init(ip, ip, name1, name2, deck1, deck2);
     game.stack.clear();
     game.givePriorityToCurrentPlayer();
 }
}