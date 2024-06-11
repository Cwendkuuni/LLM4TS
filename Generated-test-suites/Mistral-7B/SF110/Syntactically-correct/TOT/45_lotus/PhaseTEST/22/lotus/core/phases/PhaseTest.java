import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Game game;

 @Before
 public void setUp() {
 game = new Game();
 }

 @After
 public void tearDown() {
 game = null;
 }

 @Test
 public void testUntapPhase() {
 Phase untapPhase = new UntapPhase();
 Phase previousPhase = Game.currentPhase;
 Phase.nextPhase();
 assertSame(untapPhase.getClass(), Game.currentPhase.getClass());
 assertNotSame(previousPhase.getClass(), Game.currentPhase.getClass());
 game.player1.createCard("TestCard", 1, CardType.CREATURE, 1, 1, 1);
 game.player1.playCard("TestCard");
 game.player1.creatureAt(0).untap();
 untapPhase.doPhase();
 assertTrue(game.player1.creatureAt(0).isTapped());
 }

 @Test
 public void testUpkeepPhase() {
 Phase upkeepPhase = new UpkeepPhase();
 Phase previousPhase = Game.currentPhase;
 Phase.nextPhase();
 assertSame(upkeepPhase.getClass(), Game.currentPhase.getClass());
 assertNotSame(previousPhase.getClass(), Game.currentPhase.getClass());
 game.player1.createCard("TestCard", 1, CardType.CREATURE, 1, 1, 1);
 game.player1.playCard("TestCard");
 upkeepPhase.doPhase();
 game.player1.creatureAt(0).triggerAbility("TestAbility");
 assertFalse(game.player1.creatureAt(0).isTapped());
 }

 @Test
 public void testDrawPhase() {
 Phase drawPhase = new DrawPhase();
 Phase previousPhase = Game.currentPhase;
 Phase.nextPhase();
 assertSame(drawPhase.getClass(), Game.currentPhase.getClass());
 assertNotSame(previousPhase.getClass(), Game.currentPhase.getClass());
 game.player1.createCard("TestCard", 1, CardType.CREATURE, 1, 1, 1);
 game.player1.playCard("TestCard");
 game.player1.handSize() < 7;
 drawPhase.doPhase();
 assertTrue(game.player1.handSize() >= 7);
 }

 @Test
 public void testMain1Phase() {
 Phase main1Phase = new Main1Phase();
 Phase previousPhase = Game.currentPhase;
 Phase.nextPhase();
 assertSame(main1Phase.getClass(), Game.currentPhase.getClass());
 assertNotSame(previousPhase.getClass(), Game.currentPhase.getClass());
 game.player1.createCard("TestCard1", 1, CardType.CREATURE, 1, 1, 1);
 game.player1.playCard("TestCard1");
 game.player1.createCard("TestCard2", 1, CardType.CREATURE, 1, 1, 1);
 game.player1.playCard("TestCard2");
 main1Phase.doPhase();
 assertFalse(game.player1.handIsEmpty());
 }

 @Test
 public void testCombatBeginningPhase() {
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase previousPhase = Game.currentPhase;
 Phase.nextPhase();
 assertSame(combatBeginningPhase.getClass(), Game.currentPhase.getClass());
 assertNotSame(previousPhase.getClass(), Game.currentPhase.getClass());
 game.player1.createCard("TestCard1", 1, CardType.CREATURE, 1, 1, 1);
 game.player1.playCard("TestCard1");
 game.player1.createCard("TestCard2", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.createCard("TestCard3", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.playCard("TestCard3");
 combatBeginningPhase.doPhase();
 assertFalse(game.combat.isCombatFinished());
 }

 @Test
 public void testDeclareAttackersPhase() {
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase previousPhase = Game.currentPhase;
 Phase.nextPhase();
 assertSame(declareAttackersPhase.getClass(), Game.currentPhase.getClass());
 assertNotSame(previousPhase.getClass(), Game.currentPhase.getClass());
 game.player1.createCard("TestCard1", 1, CardType.CREATURE, 1, 1, 1);
 game.player1.playCard("TestCard1");
 game.player1.createCard("TestCard2", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.createCard("TestCard3", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.playCard("TestCard3");
 declareAttackersPhase.doPhase();
 game.combat.attackerAt(0).setAttacking(true);
 game.combat.attackerAt(1).setAttacking(true);
 assertFalse(game.combat.isCombatFinished());
 }

 @Test
 public void testDeclareBlockersPhase() {
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 Phase previousPhase = Game.currentPhase;
 Phase.nextPhase();
 assertSame(declareBlockersPhase.getClass(), Game.currentPhase.getClass());
 assertNotSame(previousPhase.getClass(), Game.currentPhase.getClass());
 game.player1.createCard("TestCard1", 1, CardType.CREATURE, 1, 1, 1);
 game.player1.playCard("TestCard1");
 game.player1.createCard("TestCard2", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.createCard("TestCard3", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.playCard("TestCard3");
 game.combat.attackerAt(0).setAttacking(true);
 game.combat.attackerAt(1).setAttacking(true);
 game.combat.blockerAt(0).setBlocking(true);
 game.combat.blockerAt(1).setBlocking(true);
 declareBlockersPhase.doPhase();
 assertTrue(game.combat.isCombatFinished());
 }

 @Test
 public void testCombatDamagePhase() {
 Phase combatDamagePhase = new CombatDamagePhase();
 Phase previousPhase = Game.currentPhase;
 Phase.nextPhase();
 assertSame(combatDamagePhase.getClass(), Game.currentPhase.getClass());
 assertNotSame(previousPhase.getClass(), Game.currentPhase.getClass());
 game.player1.createCard("TestCard1", 1, CardType.CREATURE, 1, 1, 1);
 game.player1.playCard("TestCard1");
 game.player1.createCard("TestCard2", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.createCard("TestCard3", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.playCard("TestCard3");
 game.combat.attackerAt(0).setAttacking(true);
 game.combat.attackerAt(1).setAttacking(true);
 game.combat.blockerAt(0).setBlocking(true);
 game.combat.blockerAt(1).setBlocking(true);
 combatDamagePhase.doPhase();
 game.player1.creatureAt(0).setHealth(5);
 game.player1.creatureAt(1).setHealth(5);
 game.player2.creatureAt(0).setHealth(5);
 game.player2.creatureAt(1).setHealth(5);
 game.player1.creatureAt(0).attack(game.player2.creatureAt(0));
 game.player1.creatureAt(1).attack(game.player2.creatureAt(1));
 assertFalse(game.combat.isCombatFinished());
 }

 @Test
 public void testCombatEndPhase() {
 Phase combatEndPhase = new CombatEndPhase();
 Phase previousPhase = Game.currentPhase;
 Phase.nextPhase();
 assertSame(combatEndPhase.getClass(), Game.currentPhase.getClass());
 assertNotSame(previousPhase.getClass(), Game.currentPhase.getClass());
 game.player1.createCard("TestCard1", 1, CardType.CREATURE, 1, 1, 1);
 game.player1.playCard("TestCard1");
 game.player1.createCard("TestCard2", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.createCard("TestCard3", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.playCard("TestCard3");
 game.combat.attackerAt(0).setAttacking(true);
 game.combat.attackerAt(1).setAttacking(true);
 game.combat.blockerAt(0).setBlocking(true);
 game.combat.blockerAt(1).setBlocking(true);
 combatDamagePhase.doPhase();
 combatEndPhase.doPhase();
 assertTrue(game.combat.isCombatFinished());
 }

 @Test
 public void testMain2Phase() {
 Phase main2Phase = new Main2Phase();
 Phase previousPhase = Game.currentPhase;
 Phase.nextPhase();
 assertSame(main2Phase.getClass(), Game.currentPhase.class);
 assertNotSame(previousPhase.getClass(), Game.currentPhase.class);
 game.player1.createCard("TestCard1", 1, CardType.CREATURE, 1, 1, 1);
 game.player1.playCard("TestCard1");
 game.player1.createCard("TestCard2", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.createCard("TestCard3", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.playCard("TestCard3");
 main2Phase.doPhase();
 assertFalse(game.handIsEmpty());
 }

 @Test
 public void testEndOfTurnPhase() {
 Phase endOfTurnPhase = new EndOfTurnPhase();
 Phase previousPhase = Game.currentPhase;
 Phase.nextPhase();
 assertSame(endOfTurnPhase.getClass(), Game.currentPhase.class);
 assertNotSame(previousPhase.getClass(), Game.currentPhase.class);
 game.player1.createCard("TestCard1", 1, CardType.CREATURE, 1, 1, 1);
 game.player1.playCard("TestCard1");
 game.player1.createCard("TestCard2", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.createCard("TestCard3", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.playCard("TestCard3");
 endOfTurnPhase.doPhase();
 assertTrue(game.isTurnEnded());
 }

 @Test
 public void testCleanupPhase() {
 Phase cleanupPhase = new CleanupPhase();
 Phase previousPhase = Game.currentPhase;
 Phase.nextPhase();
 assertSame(cleanupPhase.getClass(), Game.currentPhase.class);
 assertNotSame(previousPhase.getClass(), Game.currentPhase.class);
 game.player1.createCard("TestCard1", 1, CardType.CREATURE, 1, 1, 1);
 game.player1.playCard("TestCard1");
 game.player1.createCard("TestCard2", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.createCard("TestCard3", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.playCard("TestCard3");
 cleanupPhase.doPhase();
 assertTrue(game.turnNumber() > 1);
 }

 @Test
 public void testPlayerChangePhase() {
 Phase playerChangePhase = new PlayerChangePhase();
 Phase previousPhase = Game.currentPhase;
 Phase.nextPhase();
 assertSame(playerChangePhase.getClass(), Game.currentPhase.class);
 assertNotSame(previousPhase.getClass(), Game.currentPhase.class);
 game.player1.createCard("TestCard1", 1, CardType.CREATURE, 1, 1, 1);
 game.player1.playCard("TestCard1");
 game.player1.createCard("TestCard2", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.createCard("TestCard3", 1, CardType.CREATURE, 1, 1, 1);
 game.player2.playCard("TestCard3");
 playerChangePhase.doPhase();
 assertEquals(2, Game.playingPlayer);
 Phase.nextPhase();
 assertEquals(1, Game.playingPlayer);
 }

}