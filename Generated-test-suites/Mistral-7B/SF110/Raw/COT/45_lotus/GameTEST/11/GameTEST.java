import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

 private static Game game;
 private static UserInterface ip1;
 private static UserInterface ip2;
 private static Player player1;
 private static Player player2;
 private static CardCollection deck1;
 private static CardCollection deck2;

 @BeforeClass
 public static void setup() {
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
 game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 Player expectedPlayer = player1;
 Player actualPlayer = game.getPlayingPlayer();
 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player expectedPlayer = player2;
 Player actualPlayer = game.getNonPlayingPlayer();
 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test
 public void testGetOtherPlayer() {
 Player expectedPlayer = (game.getPlayingPlayer() == player1) ? player2 : player1;
 Player actualPlayer = game.getOtherPlayer(game.getPlayingPlayer());
 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test
 public void testInit() {
 assertEquals(player1, game.getPlayingPlayer());
 assertEquals(player2, game.getNonPlayingPlayer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullIP() {
 Game game = new Game();
 assertNull(game.getUserInterface1());
 try {
 game.init(null, ip2, "Player1", "Player2", deck1, deck2);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullNamePlayer1() {
 Game game = new Game();
 try {
 game.init(ip1, ip2, null, "Player2", deck1, deck2);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullNamePlayer2() {
 Game game = new Game();
 try {
 game.init(ip1, ip2, "Player1", null, deck1, deck2);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullDeck1() {
 Game game = new Game();
 try {
 game.init(ip1, ip2, "Player1", "Player2", null, deck2);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNullDeck2() {
 Game game = new Game();
 try {
 game.init(ip1, ip2, "Player1", "Player2", deck1, null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 // To be implemented
 }

}