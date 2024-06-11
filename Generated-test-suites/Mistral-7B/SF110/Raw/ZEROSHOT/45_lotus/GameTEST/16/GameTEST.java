package lotus.core.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

 private UserInterface ui1;
 private UserInterface ui2;
 private Player player1;
 private Player player2;
 private Game game;
 private CardCollection deck1;
 private CardCollection deck2;

 @Before
 public void setUp() {
 ui1 = new UserInterfaceMock();
 ui2 = new UserInterfaceMock();
 deck1 = new CardCollection();
 deck2 = new CardCollection();
 game = new Game();
 game.init(ui1, ui2, "Player1", "Player2", deck1, deck2);
 }

 @Test
 public void testGetPlayingPlayer() {
 Player p = game.getPlayingPlayer();
 assertEquals("Player1", p.getName());
 }

 @Test
 public void testGetNonPlayingPlayer() {
 Player p = game.getNonPlayingPlayer();
 assertEquals("Player2", p.getName());
 }

 @Test
 public void testGetOtherPlayer() {
 Player p1 = game.getPlayer1();
 Player p2 = game.getPlayer2();
 Player p = game.getOtherPlayer(p1);
 assertEquals("Player2", p.getName());
 p = game.getOtherPlayer(p2);
 assertEquals("Player1", p.getName());
 }

 @Test
 public void testInit() {
 assertNotNull(game.combat);
 assertNotNull(game.currentPhase);
 assertNotNull(game.playingPlayer);
 assertNotNull(game.player1);
 assertNotNull(game.player2);
 assertNotNull(game.stack);
 }

 @Test
 public void testGivePriorityToCurrentPlayer_NoCardsOnStack() {
 game.givePriorityToCurrentPlayer();
 assertTrue(game.currentPhase.phaseName.equals("UntapPhase"));
 }

 @Test
 public void testGivePriorityToCurrentPlayer_WithCardsOnStack() {
 Card card1 = new CardMock("TestCard1", CardType.Creature);
 Card card2 = new CardMock("TestCard2", CardType.Creature);
 game.stack.push(card1);
 game.stack.push(card2);
 game.givePriorityToCurrentPlayer();
 // Resolution logic is not covered in this test, it depends on specific card interactions
 // that are not mocked in this test
 assertTrue(game.stack.empty());
 }
}