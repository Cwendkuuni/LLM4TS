package lotus.core;

import static org.junit.Assert.*;

import org.junit.Test;

import lotus.core.interfaces.UserInterface;
import lotus.core.phases.Phase;
import lotus.core.phases.UntapPhase;
import lotus.core.player.Player;
import lotus.core.stack.Stack;
import lotus.core.card.Card;
import lotus.core.card.CardCollection;

public class GameTest {

 private static UserInterface ip1;
 private static UserInterface ip2;
 private static String name1 = "Player 1";
 private static String name2 = "Player 2";
 private static CardCollection deck1;
 private static CardCollection deck2;
 private static Game game;

 @Test
 public void testInit() {
 // arrange
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();

 // act
 game.init(ip1, ip2, name1, name2, deck1, deck2);

 // assert
 assertEquals(name1, game.player1.getName());
 assertEquals(name2, game.player2.getName());
 assertEquals(ip1, game.player1.getUserInterface());
 assertEquals(ip2, game.player2.getUserInterface());
 assertEquals(new UntapPhase(), game.currentPhase);
 assertNull(game.playingPlayer);
 assertEquals(0, game.player1.library.size());
 assertEquals(0, game.player2.library.size());
 }

 @Test
 public void testGetPlayingPlayer() {
 // arrange
 game = new Game();
 Player p1 = new Player(ip1, name1, deck1);
 Player p2 = new Player(ip2, name2, deck2);
 game.init(ip1, ip2, name1, name2, deck1, deck2);

 // act
 Player playingPlayer = game.getPlayingPlayer();

 // assert
 assertEquals(p1, playingPlayer);
 }

 @Test
 public void testGetNonPlayingPlayer() {
 // arrange
 game = new Game();
 Player p1 = new Player(ip1, name1, deck1);
 Player p2 = new Player(ip2, name2, deck2);
 game.init(ip1, ip2, name1, name2, deck1, deck2);

 // act
 Player nonPlayingPlayer = game.getNonPlayingPlayer();

 // assert
 assertEquals(p2, nonPlayingPlayer);
 }

 @Test
 public void testGetOtherPlayer() {
 // arrange
 game = new Game();
 Player p1 = new Player(ip1, name1, deck1);
 Player p2 = new Player(ip2, name2, deck2);
 game.init(ip1, ip2, name1, name2, deck1, deck2);

 // act
 Player otherPlayer = game.getOtherPlayer(p1);

 // assert
 assertEquals(p2, otherPlayer);
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 // arrange
 game = new Game();
 UserInterface ip1 = mock(UserInterface.class);
 UserInterface ip2 = mock(UserInterface.class);
 Player p1 = new Player(ip1, name1, deck1);
 Player p2 = new Player(ip2, name2, deck2);
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 Stack stack = new Stack();
 game.stack = stack;
 Card card1 = new Card("Card1", CardType.ACTION);
 card1.owner = p1;
 card1.zone = p1.hand;
 Card card2 = new Card("Card2", CardType.ACTION);
 card2.owner = p2;
 card2.zone = p2.hand;
 Stack stackSpy = spy(stack);

 // act
 game.givePriorityToCurrentPlayer();

 // assert
 verify(stackSpy).resolveLast();
 }

 @Test(expected = IllegalStateException.class)
 public void testGivePriorityToCurrentPlayer_EmptyStack() {
 // arrange
 game = new Game();
 UserInterface ip1 = mock(UserInterface.class);
 UserInterface ip2 = mock(UserInterface.class);
 Player p1 = new Player(ip1, name1, deck1);
 Player p2 = new Player(ip2, name2, deck2);
 game.init(ip1, ip2, name1, name2, deck1, deck2);
 Stack stack = new Stack();
 game.stack = stack;

 // act and assert
 game.givePriorityToCurrentPlayer();
 assertEquals(0, stack.size());
 throw new IllegalStateException("Expected stack to have cards");
 }
}