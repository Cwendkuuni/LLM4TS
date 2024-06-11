package lotus.core.test;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import lotus.core.*;
import lotus.core.card.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.stack.*;
import java.util.*;

public class GameTest {

  @Test
  public void testGetPlayingPlayer() {
    Game game = new Game();
    Player player1 = new Player(new StubUserInterface(), "Player1", new CardCollection());
    Player player2 = new Player(new StubUserInterface(), "Player2", new CardCollection());
    game.init(new StubUserInterface(), new StubUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
    game.setPlayingPlayer(1);
    assertEquals(player1, game.getPlayingPlayer());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetPlayingPlayerWhenPlayingPlayerIsNotSet() {
    Game game = new Game();
    Player player1 = new Player(new StubUserInterface(), "Player1", new CardCollection());
    Player player2 = new Player(new StubUserInterface(), "Player2", new CardCollection());
    game.init(new StubUserInterface(), new StubUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
    assertNotSame(player1, game.getPlayingPlayer());
    assertNotSame(player2, game.getPlayingPlayer());
    // This test case should throw an IllegalStateException since the playingPlayer is not set
  }

  @Test
  public void testGetNonPlayingPlayer() {
    Game game = new Game();
    Player player1 = new Player(new StubUserInterface(), "Player1", new CardCollection());
    Player player2 = new Player(new StubUserInterface(), "Player2", new CardCollection());
    game.init(new StubUserInterface(), new StubUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
    assertEquals(player2, game.getNonPlayingPlayer());
  }

  @Test
  public void testGetNonPlayingPlayerWhenOtherPlayerIsPlayer1() {
    Game game = new Game();
    Player player1 = new Player(new StubUserInterface(), "Player1", new CardCollection());
    Player player2 = new Player(new StubUserInterface(), "Player2", new CardCollection());
    game.init(new StubUserInterface(), new StubUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
    assertEquals(player1, game.getOtherPlayer(player2));
  }

  @Test
  public void testInit() {
    Game game = new Game();
    UserInterface ip1 = new StubUserInterface();
    UserInterface ip2 = new StubUserInterface();
    String name1 = "Player1";
    String name2 = "Player2";
    CardCollection deck1 = new CardCollection();
    CardCollection deck2 = new CardCollection();
    game.init(ip1, ip2, name1, name2, deck1, deck2);
    assertSame(ip1, game.getPlayer1().getUserInterface());
    assertSame(ip2, game.getPlayer2().getUserInterface());
    assertEquals(name1, game.getPlayer1().getName());
    assertEquals(name2, game.getPlayer2().getName());
    assertSame(deck1, game.getPlayer1().getDeck());
    assertSame(deck2, game.getPlayer2().getDeck());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInitWhenDeckIsEmpty() {
    Game game = new Game();
    UserInterface ip1 = new StubUserInterface();
    UserInterface ip2 = new StubUserInterface();
    String name1 = "Player1";
    String name2 = "Player2";
    CardCollection deck1 = new CardCollection();
    CardCollection deck2 = new CardCollection();
    deck1.clear();
    game.init(ip1, ip2, name1, name2, deck1, deck2);
    // This test case should throw an IllegalArgumentException since a player's deck cannot be empty
  }

  @Test
  public void testGivePriorityToCurrentPlayer() {
    Game game = spy(new Game());
    Player player1 = mock(Player.class);
    Player player2 = mock(Player.class);
    when(game.getPlayingPlayer()).thenReturn(player1);
    when(game.getNonPlayingPlayer()).thenReturn(player2);
    when(player1.letPlayerSpeak()).thenReturn(true);
    when(player2.letPlayerSpeak()).thenReturn(true);
    when(game.stack.isEmpty()).thenReturn(false);
    doCallRealMethod().when(game).givePriorityToCurrentPlayer();
    verify(game, times(1)).givePriorityToCurrentPlayer();
    verify(player1, times(1)).letPlayerSpeak();
    verify(player2, times(1)).letPlayerSpeak();
    // Edge case: givePriorityToCurrentPlayer when stack is empty
    doCallRealMethod().when(game).givePriorityToCurrentPlayer();
    verify(game, times(1)).givePriorityToCurrentPlayer();
    verify(player1, never()).letPlayerSpeak();
    verify(player2, never()).letPlayerSpeak();
  }

  @Test(expected = NullPointerException.class)
  public void testGivePriorityToCurrentPlayerWhenGetPlayingPlayerReturnsNull() {
    Game game = new Game();
    Player player1 = new Player(new StubUserInterface(), "Player1", new CardCollection());
    Player player2 = new Player(new StubUserInterface(), "Player2", new CardCollection());
    game.init(new StubUserInterface(), new StubUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
    game.setPlayingPlayer(null);
    game.givePriorityToCurrentPlayer();
    // This test case should throw a NullPointerException since getPlayingPlayer() should never return null
  }
}