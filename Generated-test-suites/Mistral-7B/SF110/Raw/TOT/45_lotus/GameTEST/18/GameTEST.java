import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.interfaces.*;

public class GameTest {

 @Test
 public void testGetPlayingPlayer() {
 Player expectedPlayer;
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 expectedPlayer = Game.player1;
 assertEquals(expectedPlayer, Game.getPlayingPlayer());
 }

 @Test
 public void testGetPlayingPlayer_SecondPlayer() {
 Player expectedPlayer;
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player2Name", "Player1Name", new CardCollection(), new CardCollection());
 expectedPlayer = Game.player2;
 assertEquals(expectedPlayer, Game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player expectedPlayer;
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 expectedPlayer = Game.player2;
 assertEquals(expectedPlayer, Game.getNonPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer_FirstPlayer() {
 Player expectedPlayer;
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player2Name", "Player1Name", new CardCollection(), new CardCollection());
 expectedPlayer = Game.player1;
 assertEquals(expectedPlayer, Game.getOtherPlayer(expectedPlayer));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetPlayingPlayer_NotInitialized() {
 Game.stack = new Stack();
 assertNull(Game.getPlayingPlayer());
 }

 @Test
 public void testInit() {
 UserInterface ip1 = new DummyUserInterface();
 UserInterface ip2 = new DummyUserInterface();
 String name1 = "Player1Name";
 String name2 = "Player2Name";
 CardCollection deck1 = new CardCollection();
 CardCollection deck2 = new CardCollection();
 Game.init(ip1, ip2, name1, name2, deck1, deck2);
 assertTrue(Game.player1 instanceof Player);
 assertTrue(Game.player2 instanceof Player);
 assertEquals(name1, Game.player1.getName());
 assertEquals(name2, Game.player2.getName());
 assertNotNull(Game.currentPhase);
 assertEquals(UntapPhase.class, Game.currentPhase.getClass());
 assertEquals(0, Game.stack.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInit_NullUserInterface() {
 Game.init(null, new DummyUserInterface(), "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInit_NullName() {
 Game.init(new DummyUserInterface(), new DummyUserInterface(), null, "Player2Name", new CardCollection(), new CardCollection());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInit_NullName_SecondPlayer() {
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", null, new CardCollection(), new CardCollection());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInit_NullDeck() {
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", "Player2Name", null, new CardCollection());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInit_NullDeck_SecondPlayer() {
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", "Player2Name", new CardCollection(), null);
 }

 @Test
 public void testGivePriorityToCurrentPlayer_LetPlayerSpeak_BothPlayersSpeak() {
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 Game.givePriorityToCurrentPlayer();
 Game.givePriorityToCurrentPlayer();
 assertTrue(Game.stack.isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_LetPlayerSpeak_FirstPlayerSpeaksTwice() {
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 when(Game.player1.letPlayerSpeak()).thenReturn(true).thenReturn(false);
 Game.givePriorityToCurrentPlayer();
 Game.givePriorityToCurrentPlayer();
 assertTrue(Game.stack.isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_LetPlayerSpeak_SecondPlayerSpeaksTwice() {
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 when(Game.player2.letPlayerSpeak()).thenReturn(true).thenReturn(false);
 Game.givePriorityToCurrentPlayer();
 Game.givePriorityToCurrentPlayer();
 assertTrue(Game.stack.isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_LetPlayerSpeak_FirstPlayerSpeaksOnce() {
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 when(Game.player1.letPlayerSpeak()).thenReturn(true);
 Game.givePriorityToCurrentPlayer();
 Game.givePriorityToCurrentPlayer();
 assertFalse(Game.stack.isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_LetPlayerSpeak_SecondPlayerSpeaksOnce() {
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 when(Game.player2.letPlayerSpeak()).thenReturn(true);
 Game.givePriorityToCurrentPlayer();
 Game.givePriorityToCurrentPlayer();
 assertFalse(Game.stack.isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_LetPlayerSpeak_FirstPlayerSpeaksAndStackIsNotEmpty() {
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 Game.stack.push(new Card());
 when(Game.player1.letPlayerSpeak()).thenReturn(true);
 Game.givePriorityToCurrentPlayer();
 Game.givePriorityToCurrentPlayer();
 assertFalse(Game.stack.isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_LetPlayerSpeak_SecondPlayerSpeaksAndStackIsNotEmpty() {
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 Game.stack.push(new Card());
 when(Game.player2.letPlayerSpeak()).thenReturn(true);
 Game.givePriorityToCurrentPlayer();
 Game.givePriorityToCurrentPlayer();
 assertFalse(Game.stack.isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_LetPlayerSpeak_FirstPlayerSpeaksAndStacksAreNotEmpty() {
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 Game.player1.library.add(new Card());
 Game.player2.library.add(new Card());
 when(Game.player1.letPlayerSpeak()).thenReturn(true);
 Game.givePriorityToCurrentPlayer();
 Game.givePriorityToCurrentPlayer();
 assertTrue(Game.stack.isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayer_LetPlayerSpeak_SecondPlayerSpeaksAndStacksAreNotEmpty() {
 Game.init(new DummyUserInterface(), new DummyUserInterface(), "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 Game.player1.library.add(new Card());
 Game.player2.library.add(new Card());
 when(Game.player2.letPlayerSpeak()).thenReturn(true);
 Game.givePriorityToCurrentPlayer();
 Game.givePriorityToCurrentPlayer();
 assertTrue(Game.stack.isEmpty());
 }
}