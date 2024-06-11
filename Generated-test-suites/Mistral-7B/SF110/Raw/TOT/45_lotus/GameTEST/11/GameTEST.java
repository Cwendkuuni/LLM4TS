import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import lotus.core.*;
import static org.junit.Assert.*;

public class GameTest {

 private UserInterface ip1;
 private UserInterface ip2;
 private Player player1;
 private Player player2;
 private CardCollection deck1;
 private CardCollection deck2;
 private Game game;

 @Before
 public void setUp() {
 ip1 = Mockito.mock(UserInterface.class);
 ip2 = Mockito.mock(UserInterface.class);
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 player1 = new Player(ip1, "Player 1", deck1);
 player2 = new Player(ip2, "Player 2", deck2);
 game = new Game();
 }

 @Test
 public void testGetPlayingPlayer() {
 Game.init(ip1, ip2, "Player 1", "Player 2", deck1, deck2);
 assertEquals(player1, Game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Game.init(ip1, ip2, "Player 1", "Player 2", deck1, deck2);
 assertEquals(player2, Game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 Game.init(ip1, ip2, "Player 1", "Player 2", deck1, deck2);
 assertEquals(player2, Game.getOtherPlayer(player1));
 assertEquals(player1, Game.getOtherPlayer(player2));
 }

 @Test
 public void testInit() {
 ArgumentCaptor<UserInterface> ipCaptor1 = ArgumentCaptor.forClass(UserInterface.class);
 ArgumentCaptor<UserInterface> ipCaptor2 = ArgumentCaptor.forClass(UserInterface.class);
 ArgumentCaptor<Player> playerCaptor1 = ArgumentCaptor.forClass(Player.class);
 ArgumentCaptor<Player> playerCaptor2 = ArgumentCaptor.forClass(Player.class);
 ArgumentCaptor<CardCollection> deckCaptor1 = ArgumentCaptor.forClass(CardCollection.class);
 ArgumentCaptor<CardCollection> deckCaptor2 = ArgumentCaptor.forClass(CardCollection.class);

 Mockito.verify(ip1).init(playerCaptor1.capture());
 Mockito.verify(ip2).init(playerCaptor2.capture());
 assertEquals(player1, playerCaptor1.getValue());
 assertEquals(player2, playerCaptor2.getValue());
 Mockito.verify(game).playingPlayer = 1;
 Mockito.verify(game).currentPhase = Mockito.isA(UntapPhase.class);
 Mockito.verify(player1).library.addAll(deckCaptor1.capture());
 Mockito.verify(player2).library.addAll(deckCaptor2.capture());
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 Mockito.when(player1.letPlayerSpeak()).thenReturn(true);
 Mockito.when(player2.letPlayerSpeak()).thenReturn(false);
 game.init(ip1, ip2, "Player 1", "Player 2", deck1, deck2);
 game.stack.add(Mockito.mock(Card.class));
 game.givePriorityToCurrentPlayer();
 game.givePriorityToCurrentPlayer();
 Mockito.verify(ip1, Mockito.times(2)).showMessage("It's your turn.");
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayer_EmptyStack() {
 game.init(ip1, ip2, "Player 1", "Player 2", deck1, deck2);
 game.givePriorityToCurrentPlayer();
 game.stack.clear();
 game.givePriorityToCurrentPlayer();
 }
}