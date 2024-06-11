import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;

public class GameTest {

 private static UserInterface ip1;
 private static UserInterface ip2;
 private static Player player1;
 private static Player player2;
 private static CardCollection deck1;
 private static CardCollection deck2;

 @BeforeClass
 public static void setupClass() {
 ip1 = new DummyUserInterface();
 ip2 = new DummyUserInterface();
 }

 @Before
 public void setup() {
 Game.init(ip1, ip2, "Player1Name", "Player2Name", deck1 = new CardCollection(), deck2 = new CardCollection());
 }

 @Test
 public void testGetPlayingPlayer() {
 Player expectedPlayer = player1;
 Player actualPlayer = Game.getPlayingPlayer();
 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player expectedPlayer = player2;
 Player actualPlayer = Game.getNonPlayingPlayer();
 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test
 public void testGetOtherPlayer() {
 Player expectedPlayer = (Game.playingPlayer == 1) ? player2 : player1;
 Player actualPlayer = Game.getOtherPlayer(player1);
 assertEquals(expectedPlayer, actualPlayer);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetPlayingPlayerWhenNoPlayersInitialized() {
 Game.player1 = null;
 Game.player2 = null;
 Game.getPlayingPlayer();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetNonPlayingPlayerWhenNoPlayersInitialized() {
 Game.player1 = null;
 Game.player2 = null;
 Game.getNonPlayingPlayer();
 }

 @Test
 public void testInit() {
 assertNotNull(Game.player1);
 assertNotNull(Game.player2);
 assertNotNull(Game.currentPhase);
 assertTrue(Game.stack.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullUserInterfaces() {
 Game.init(null, ip2, "Player1Name", "Player2Name", deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullPlayerNames() {
 Game.init(ip1, null, null, null, deck1, deck2);
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullDecks() {
 Game.init(ip1, ip2, "Player1Name", "Player2Name", null, null);
 }

 @Test
 public void testGivePriorityToCurrentPlayer_NoSpeak() {
 Game.givePriorityToCurrentPlayer();
 assertTrue(Game.stack.isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_Speak() {
 player1.setCanSpeak(true);
 Game.givePriorityToCurrentPlayer();
 assertFalse(Game.stack.isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_StackResolution() {
 player1.setCanSpeak(true);
 player2.setCanSpeak(true);
 Game.givePriorityToCurrentPlayer();
 Game.stack.resolveLast();
 Game.givePriorityToCurrentPlayer();
 assertFalse(Game.stack.isEmpty());
 }
}