package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import lotus.core.Game;
import lotus.core.Player;
import lotus.core.effect.ChangePhase;
import lotus.core.phases.CombatBeginningPhase;
import lotus.core.phases.CombatDamagePhase;
import lotus.core.phases.CombatEndPhase;
import lotus.core.phases.DeclareAttackersPhase;
import lotus.core.phases.DeclareBlockersPhase;
import lotus.core.phases.DrawPhase;
import lotus.core.phases.EndOfTurnPhase;
import lotus.core.phases.Main1Phase;
import lotus.core.phases.Main2Phase;
import lotus.core.phases.PlayerChangePhase;
import lotus.core.phases.UntapPhase;

public class PhaseTest {

 private Game game;
 private Player player1, player2;

 @Before
 public void setUp() {
 game = new Game();
 player1 = new Player("Player 1", game);
 player2 = new Player("Player 2", game);
 game.addPlayer(player1);
 game.addPlayer(player2);
 game.startGame();
 }

 @Test
 public void testUntapPhase() {
 Phase untapPhase = new UntapPhase();
 assertNotNull(untapPhase);

 // Test doPhase method
 untapPhase.doPhase();
 // TODO: Add assertions to check the state of the game after the UntapPhase

 // Test nextPhase method
 Phase currentPhase = Game.currentPhase;
 untapPhase.nextPhase();
 assertEquals(currentPhase.getClass(), UpkeepPhase.class);
 }

 @Test
 public void testUpkeepPhase() {
 Phase upkeepPhase = new UpkeepPhase();
 assertNotNull(upkeepPhase);

 // Test doPhase method
 upkeepPhase.doPhase();
 // TODO: Add assertions to check the state of the game after the UpkeepPhase

 // Test nextPhase method
 currentPhase = Game.currentPhase;
 upkeepPhase.nextPhase();
 assertEquals(currentPhase.getClass(), DrawPhase.class);
 }

 @Test
 public void testDrawPhase() {
 Phase drawPhase = new DrawPhase();
 assertNotNull(drawPhase);

 // Test doPhase method
 drawPhase.doPhase();
 // TODO: Add assertions to check the state of the game after the DrawPhase

 // Test nextPhase method
 currentPhase = Game.currentPhase;
 drawPhase.nextPhase();
 assertEquals(currentPhase.getClass(), Main1Phase.class);
 }

 @Test
 public void testMain1Phase() {
 Phase main1Phase = new Main1Phase();
 assertNotNull(main1Phase);

 // Test doPhase method
 main1Phase.doPhase();
 // TODO: Add assertions to check the state of the game after the Main1Phase

 // Test nextPhase method
 currentPhase = Game.currentPhase;
 main1Phase.nextPhase();
 assertEquals(currentPhase.getClass(), CombatBeginningPhase.class);
 }

 @Test
 public void testCombatBeginningPhase() {
 Phase combatBeginningPhase = new CombatBeginningPhase();
 assertNotNull(combatBeginningPhase);

 // Test doPhase method
 combatBeginningPhase.doPhase();
 // TODO: Add assertions to check the state of the game after the CombatBeginningPhase

 // Test nextPhase method
 currentPhase = Game.currentPhase;
 combatBeginningPhase.nextPhase();
 assertEquals(currentPhase.getClass(), DeclareAttackersPhase.class);
 }

 @Test
 public void testDeclareAttackersPhase() {
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 assertNotNull(declareAttackersPhase);

 // Test doPhase method
 declareAttackersPhase.doPhase();
 // TODO: Add assertions to check the state of the game after the DeclareAttackersPhase

 // Test nextPhase method
 currentPhase = Game.currentPhase;
 declareAttackersPhase.nextPhase();
 assertEquals(currentPhase.getClass(), DeclareBlockersPhase.class);
 }

 @Test
 public void testDeclareBlockersPhase() {
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 assertNotNull(declareBlockersPhase);

 // Test doPhase method
 declareBlockersPhase.doPhase();
 // TODO: Add assertions to check the state of the game after the DeclareBlockersPhase

 // Test nextPhase method
 currentPhase = Game.currentPhase;
 declareBlockersPhase.nextPhase();
 assertEquals(currentPhase.getClass(), CombatDamagePhase.class);
 }

 @Test
 public void testCombatDamagePhase() {
 Phase combatDamagePhase = new CombatDamagePhase();
 assertNotNull(combatDamagePhase);

 // Test doPhase method
 combatDamagePhase.doPhase();
 // TODO: Add assertions to check the state of the game after the CombatDamagePhase

 // Test nextPhase method
 currentPhase = Game.currentPhase;
 combatDamagePhase.nextPhase();
 assertEquals(currentPhase.getClass(), CombatEndPhase.class);
 }

 @Test
 public void testCombatEndPhase() {
 Phase combatEndPhase = new CombatEndPhase();
 assertNotNull(combatEndPhase);

 // Test doPhase method
 combatEndPhase.doPhase();
 // TODO: Add assertions to check the state of the game after the CombatEndPhase

 // Test nextPhase method
 currentPhase = Game.currentPhase;
 combatEndPhase.nextPhase();
 assertEquals(currentPhase.getClass(), Main2Phase.class);
 }

 @Test
 public void testMain2Phase() {
 Phase main2Phase = new Main2Phase();
 assertNotNull(main2Phase);

 // Test doPhase method
 main2Phase.doPhase();
 // TODO: Add assertions to check the state of the game after the Main2Phase

 // Test nextPhase method
 currentPhase = Game.currentPhase;
 main2Phase.nextPhase();
 assertEquals(currentPhase.getClass(), EndOfTurnPhase.class);
 }

 @Test
 public void testEndOfTurnPhase() {
 Phase endOfTurnPhase = new EndOfTurnPhase();
 assertNotNull(endOfTurnPhase);

 // Test doPhase method
 endOfTurnPhase.doPhase();
 // TODO: Add assertions to check the state of the game after the EndOfTurnPhase

 // Test nextPhase method
 currentPhase = Game.currentPhase;
 endOfTurnPhase.nextPhase();
 assertEquals(currentPhase.getClass(), CleanupPhase.class);
 }

 @Test
 public void testCleanupPhase() {
 Phase cleanupPhase = new CleanupPhase();
 assertNotNull(cleanupPhase);

 // Test doPhase method
 cleanupPhase.doPhase();
 // TODO: Add assertions to check the state of the game after the CleanupPhase

 // Test nextPhase method
 currentPhase = Game.currentPhase;
 cleanupPhase.nextPhase();
 assertEquals(currentPhase.getClass(), PlayerChangePhase.class);
 }

 @Test
 public void testPlayerChangePhase() {
 Phase playerChangePhase = new PlayerChangePhase();
 assertNotNull(playerChangePhase);

 // Test doPhase method
 playerChangePhase.doPhase();
 // TODO: Add assertions to check the state of the game after the PlayerChangePhase

 // Test nextPhase method
 currentPhase = Game.currentPhase;
 playerChangePhase.nextPhase();
 assertEquals(currentPhase.getClass(), UntapPhase.class);
 }
}