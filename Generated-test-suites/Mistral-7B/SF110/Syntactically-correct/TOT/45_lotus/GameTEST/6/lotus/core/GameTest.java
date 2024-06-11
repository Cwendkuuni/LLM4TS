import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
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
 private Game game;

 @Before
 public void setup() {
 ip1 = Mockito.mock(UserInterface.class);
 ip2 = Mockito.mock(UserInterface.class);
 player1 = new Player(ip1, "Player1", new ArrayList<>());
 player2 = new Player(ip2, "Player2", new ArrayList<>());
 game = new Game() {{
 init(ip1, ip2, "Player1", "Player2", new CardCollection(), new CardCollection());
 }};
 }

 @Test
 public void testGetPlayingPlayer() {
 ArgumentCaptor<String> playerNameCaptor = ArgumentCaptor.forClass(String.class);
 game.init(ip1, ip2, "Player1", "Player2", new CardCollection(), new CardCollection());
 Mockito.verify(ip1).init(player1);
 assertSame(player1, game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 assertSame(player2, game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 assertSame(player2, game.getOtherPlayer(player1));
 assertSame(player1, game.getOtherPlayer(player2));
 }

 @Test
 public void testInit() {
 List<Card> player1Deck = new ArrayList<>(Arrays.asList(new Card("Card1"), new Card("Card2")));
 List<Card> player2Deck = new ArrayList<>(Arrays.asList(new Card("Card3"), new Card("Card4")));
 when(ip1.init(any(Player.class)))
 .thenReturn(true);
 when(ip2.init(any(Player.class)))
 .thenReturn(true);
 game.init(ip1, ip2, "Player1", "Player2", new CardCollection(player1Deck), new CardCollection(player2Deck));
 verify(ip1, times(1)).init(player1);
 verify(ip2, times(1)).init(player2);
 }

 @Test(expected = IllegalStateException.class)
 public void testInitWithNullIP1() {
 game = new Game();
 try {
 game.init(null, ip2, "Player1", "Player2", new CardCollection(), new CardCollection());
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Expected IllegalStateException, but got " + e.getClass().getName());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testInitWithNullIP2() {
 game = new Game();
 try {
 game.init(ip1, null, "Player1", "Player2", new CardCollection(), new CardCollection());
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Expected IllegalStateException, but got " + e.getClass().getName());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithEmptyPlayerName1() {
 game = new Game();
 try {
 game.init(ip1, ip2, "", "Player2", new CardCollection(), new CardCollection());
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Expected IllegalArgumentException, but got " + e.getClass().getName());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithEmptyPlayerName2() {
 game = new Game();
 try {
 game.init(ip1, ip2, "Player1", "", new CardCollection(), new CardCollection());
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Expected IllegalArgumentException, but got " + e.getClass().getName());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullDeck1() {
 game = new Game();
 try {
 game.init(ip1, ip2, "Player1", "Player2", null, new CardCollection());
 } catch (IllegalArgumentException e) {
 throw new AssertionError("ExpectedIllegalArgumentException, but got " + e.getClass().getName());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullDeck2() {
 game = new Game();try {
 game.init(ip1, ip2, "Player1", "Player2", new CardCollection(), null);
 } catch (IllegalArgumentException e) {
 throw new AssertionError("ExpectedIllegalArgumentException, but got " + e.getClass().getName());
 }
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 Player currentPlayer = player1;
 Player nonPlayingPlayer = player2;
 do {
 boolean currentPlayerSpeaking = currentPlayer.letPlayerSpeak();
 boolean nonPlayingPlayerSpeaking = nonPlayingPlayer.letPlayerSpeak();
 if (currentPlayerSpeaking && nonPlayingPlayerSpeaking) {
 break;
 }
 currentPlayer = nonPlayingPlayer;
 nonPlayingPlayer = currentPlayer;
 } while (true);
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 assertFalse(game.stack.isEmpty());
 }

 @Test
 public void testGivePriorityToCurrentPlayerEmptyStack() {
 Player currentPlayer = player1;
 Player nonPlayingPlayer = player2;
 do {
 boolean currentPlayerSpeaking = currentPlayer.letPlayerSpeak();
 boolean nonPlayingPlayerSpeaking = nonPlayingPlayer.letPlayerSpeak();
 if (currentPlayerSpeaking && nonPlayingPlayerSpeaking) {
 break;
 }
 currentPlayer = nonPlayingPlayer;
 nonPlayingPlayer = currentPlayer;
 } while (true);
 game.givePriorityToCurrentPlayer();
 assertTrue(game.stack.isEmpty());
 }
}