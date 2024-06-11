import org.junit.Assert;
import org.junit.Test;
import lotus.core.Game;
import lotus.core.Player;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;

public class GameTest {

 @Test
 public void testGetPlayingPlayer() {
 Player playingPlayer = Game.getPlayingPlayer();
 Assert.assertNotNull(playingPlayer);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player nonPlayingPlayer = Game.getNonPlayingPlayer();
 Assert.assertNotNull(nonPlayingPlayer);
 }

 @Test
 public void testGetOtherPlayer() {
 Player p1 = new Player(null, "player1", null);
 Player p2 = new Player(null, "player2", null);
 Game.init(null, null, "player1", "player2", null, null);
 Player otherPlayer = Game.getOtherPlayer(p1);
 Assert.assertEquals(p2, otherPlayer);
 }

 @Test
 public void testInit() {
 UserInterface ip1 = null;
 UserInterface ip2 = null;
 String name1 = "player1";
 String name2 = "player2";
 CardCollection deck1 = new CardCollection();
 CardCollection deck2 = new CardCollection();
 Game.init(ip1, ip2, name1, name2, deck1, deck2);
 Player player1 = Game.player1;
 Player player2 = Game.player2;
 Assert.assertNotNull(player1);
 Assert.assertNotNull(player2);
 Assert.assertEquals(name1, player1.getName());
 Assert.assertEquals(name2, player2.getName());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_APdoneFirst() {
 Player p1 = new Player(null, "player1", null);
 Player p2 = new Player(null, "player2", null);
 UserInterface ip1 = null;
 UserInterface ip2 = null;
 String name1 = "player1";
 String name2 = "player2";
 CardCollection deck1 = new CardCollection();
 CardCollection deck2 = new CardCollection();
 Game game = new Game();
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 game.playingPlayer = 1;
 game.givePriorityToCurrentPlayer();
 Assert.assertTrue(p1.hasSpoken);
 }

 @Test
 public void testGivePriorityToCurrentPlayer_NAPdoneFirst() {
 Player p1 = new Player(null, "player1", null);
 Player p2 = new Player(null, "player2", null);
 UserInterface ip1 = null;
 UserInterface ip2 = null;
 String name1 = "player1";
 String name2 = "player2";
 CardCollection deck1 = new CardCollection();
 CardCollection deck2 = new CardCollection();
 Game game = new Game();
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 game.playingPlayer = 2;
 game.givePriorityToCurrentPlayer();
 Assert.assertTrue(p2.hasSpoken);
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayer_EmptyStack() {
 Player p1 = new Player(null, "player1", null);
 Player p2 = new Player(null, "player2", null);
 UserInterface ip1 = null;
 UserInterface ip2 = null;
 String name1 = "player1";
 String name2 = "player2";
 CardCollection deck1 = new CardCollection();
 CardCollection deck2 = new CardCollection();
 Game game = new Game();
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 game.stack = new Stack();
 game.givePriorityToCurrentPlayer();
 }

 @Test
 public void testGivePriorityToCurrentPlayer_StackResolved() {
 Player p1 = new Player(null, "player1", null);
 Player p2 = new Player(null, "player2", null);
 UserInterface ip1 = null;
 UserInterface ip2 = null;
 String name1 = "player1";
 String name2 = "player2";
 CardCollection deck1 = new CardCollection();
 CardCollection deck2 = new CardCollection();
 Game game = new Game();
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 Card card = new Card("testCard", null, null, null, null, null, null, null);
 game.stack.push(card);
 game.givePriorityToCurrentPlayer();
 Assert.assertTrue(game.stack.isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_APdoneThenNAPdone() {
 Player p1 = new Player(null, "player1", null);
 Player p2 = new Player(null, "player2", null);
 UserInterface ip1 = null;
 UserInterface ip2 = null;
 String name1 = "player1";
 String name2 = "player2";
 CardCollection deck1 = new CardCollection();
 CardCollection deck2 = new CardCollection();
 Game game = new Game();
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 game.playingPlayer = 1;
 game.givePriorityToCurrentPlayer();
 Assert.assertTrue(p1.hasSpoken);
 game.playingPlayer = 2;
 game.givePriorityToCurrentPlayer();
 Assert.assertTrue(p2.hasSpoken);
 }

 @Test
 public void testGivePriorityToCurrentPlayer_NAPdoneThenAPdone() {
 Player p1 = new Player(null, "player1", null);
 Player p2 = new Player(null, "player2", null);
 UserInterface ip1 = null;
 UserInterface ip2 = null;
 String name1 = "player1";
 String name2 = "player2";
 CardCollection deck1 = new CardCollection();
 CardCollection deck2 = new CardCollection();
 Game game = new Game();
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 game.playingPlayer = 2;
 game.givePriorityToCurrentPlayer();
 Assert.assertTrue(p2.hasSpoken);
 game.playingPlayer = 1;
 game.givePriorityToCurrentPlayer();
 Assert.assertTrue(p1.hasSpoken);
 }

 @Test
 public void testInitWithNullArguments() {
 UserInterface ip1 = null;
 UserInterface ip2 = null;
 String name1 = null;
 String name2 = null;
 CardCollection deck1 = null;
 CardCollection deck2 = null;
 try {
 Game.init(ip1, ip2, name1, name2, deck1, deck2);
 Assert.fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testInitWithEmptyDecks() {
 UserInterface ip1 = null;
 UserInterface ip2 = null;
 String name1 = "player1";
 String name2 = "player2";
 CardCollection deck1 = new CardCollection();
 CardCollection deck2 = new CardCollection();
 Game.init(ip1, ip2, name1, name2, deck1, deck2);
 Assert.assertEquals(0, deck1.size());
 Assert.assertEquals(0, deck2.size());
 }

 @Test
 public void testInitWithNullPlayerNames() {
 UserInterface ip1 = null;
 UserInterface ip2 = null;
 String name1 = null;
 String name2 = null;
 CardCollection deck1 = new CardCollection();
 CardCollection deck2 = new CardCollection();
 try {
 Game.init(ip1, ip2, name1, name2, deck1, deck2);
 Assert.fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testInitWithNullUserInterfaces() {
 String name1 = "player1";
 String name2 = "player2";
 CardCollection deck1 = new CardCollection();
 CardCollection deck2 = new CardCollection();
 try {
 Game.init(null, null, name1, name2, deck1, deck2);
 Assert.fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testInitWithNullDecks() {
 UserInterface ip1 = null;
 UserInterface ip2 = null;
 String name1 = "player1";
 String name2 = "player2";
 CardCollection deck1 = null;
 CardCollection deck2 = null;
 try {
 Game.init(ip1, ip2, name1, name2, deck1, deck2);
 Assert.fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

}