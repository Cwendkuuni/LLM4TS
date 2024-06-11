package lotus.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import lotus.core. stack.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

 Player player1, player2;
 UserInterface ip1, ip2;
 Stack stack;
 Phase currentPhase;
 Combat combat;

 @Before
 public void setUp() {
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 player1 = new Player(ip1, "Player1", new CardCollection());
 player2 = new Player(ip2, "Player2", new CardCollection());
 stack = new Stack();
 currentPhase = mock(Phase.class);
 combat = mock(Combat.class);
 Game.init(ip1, ip2, "Player1Name", "Player2Name", new CardCollection(), new CardCollection());
 Game.combat = combat;
 Game.currentPhase = currentPhase;
 }

 @Test
 public void testGetPlayingPlayer() {
 assertEquals(player1, Game.getPlayingPlayer());
 Game.playingPlayer = 2;
 assertEquals(player2, Game.getPlayingPlayer());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 assertEquals(player2, Game.getNonPlayingPlayer());
 Game.playingPlayer = 1;
 assertEquals(player1, Game.getNonPlayingPlayer());
 }

 @Test
 public void testGetOtherPlayer() {
 assertEquals(player2, Game.getOtherPlayer(player1));
 assertEquals(player1, Game.getOtherPlayer(player2));
 }

 @Test
 public void testInit() {
 List<Card> deck1 = new ArrayList<>();
 List<Card> deck2 = new ArrayList<>();
 deck1.add(mock(Card.class));
 deck1.add(mock(Card.class));
 deck2.add(mock(Card.class));
 deck2.add(mock(Card.class));

 verify(ip1).init(player1);
 verify(ip2).init(player2);
 assertEquals(player1, Game.player1);
 assertEquals(player2, Game.player2);
 assertEquals(deck1.toArray(new Card[0]), player1.library.cards.toArray(new Card[0]));
 assertEquals(deck2.toArray(new Card[0]), player2.library.cards.toArray(new Card[0]));
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 Player currentPlayer = Game.getPlayingPlayer();
 Player otherPlayer = Game.getNonPlayingPlayer();
 Card card = mock(Card.class);
 when(currentPlayer.letPlayerSpeak()).thenReturn(false);
 when(otherPlayer.letPlayerSpeak()).thenReturn(true);
 stack.push(card);

 Game.givePriorityToCurrentPlayer();
 verify(currentPlayer, times(1)).letPlayerSpeak();
 verify(otherPlayer, times(1)).letPlayerSpeak();
 verify(stack, times(1)).resolveLast();
 }

 @Test
 public void testGivePriorityToCurrentPlayer_NoSpeak() {
 Player currentPlayer = Game.getPlayingPlayer();
 Player otherPlayer = Game.getNonPlayingPlayer();
 stack.push(mock(Card.class));

 Game.givePriorityToCurrentPlayer();
 verify(currentPlayer, times(1)).letPlayerSpeak();
 verify(otherPlayer, times(0)).letPlayerSpeak();
 }

 @Test
 public void testGivePriorityToCurrentPlayer_StackEmpty() {
 Game.givePriorityToCurrentPlayer();
 verifyNoInteractions(Game.stack);
 }
}