package lotus.core.test;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import lotus.core.*;
import lotus.core.stack.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

 private UserInterface ip1;
 private UserInterface ip2;
 private String name1;
 private String name2;
 private CardCollection deck1;
 private CardCollection deck2;
 private Player player1;
 private Player player2;

 @Before
 public void setup() {
 ip1 = mock(UserInterface.class);
 ip2 = mock(UserInterface.class);
 name1 = "Player1";
 name2 = "Player2";
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 Game.init(ip1, ip2, name1, name2, deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 Player actual = Game.getPlayingPlayer();
 assertEquals(name1, actual.getName());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player actual = Game.getNonPlayingPlayer();
 assertEquals(name2, actual.getName());
 }

 @Test
 public void testGetOtherPlayer() {
 Player p = new Player(ip1, name1, deck1);
 Player actual = Game.getOtherPlayer(p);
 assertEquals(name2, actual.getName());
 }

 @Test
 public void testInit() {
 // Check that players have been initialized
 verify(ip1, times(1)).init(player1);
 verify(ip2, times(1)).init(player2);
 // Check that players' decks have been populated
 assertEquals(deck1.size(), 0); // Pre-condition
 player1.play(deck1.get(0));
 assertEquals(deck1.size() - 1, deck1.size()); // Post-condition
 }

 @Test
 public void testGivePriorityToCurrentPlayer() {
 // Test that both players have a chance to speak during their turns
 doAnswer(invocation -> {
 player1.letPlayerSpeak();
 return true;
 }).when(player1).letPlayerSpeak();
 doAnswer(invocation -> {
 player2.letPlayerSpeak();
 return true;
 }).when(player2).letPlayerSpeak();

 Game.givePriorityToCurrentPlayer();

 verify(player1, times(1)).letPlayerSpeak();
 verify(player2, times(1)).letPlayerSpeak();

 // Test that stack is resolved during each player's turn
 Game.givePriorityToCurrentPlayer();
 Card card = new Card("TestCard", "TestCardType");
 player1.play(card);
 stack.push(card);
 Game.givePriorityToCurrentPlayer();
 verify(stack, times(1)).resolveLast();
 }
}