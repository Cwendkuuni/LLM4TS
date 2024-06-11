package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import lotus.core.Game;
import lotus.core.Phase;
import lotus.core.Phases;
import lotus.core.effect.ChangePhase;

public class PhaseTest {

 private Game game;
 private Phases phases;

 @Before
 public void setUp() {
 game = new Game();
 phases = new Phases();
 }

 @Test
 public void testUntapPhase() {
 Phase untapPhase = new UntapPhase();
 untapPhase.doPhase();
 assertEquals(UntapPhase.class, game.getCurrentPhase().getClass());
 }

 @Test
 public void testUpkeepPhase() {
 Phase upkeepPhase = new UpkeepPhase();
 Phase previousPhase = game.getCurrentPhase();
 untapPhase.doPhase();
 upkeepPhase.doPhase();
 assertEquals(UpkeepPhase.class, game.getCurrentPhase().getClass());
 assertEquals(previousPhase, game.getPreviousPhase());
 }

 @Test
 public void testDrawPhase() {
 Phase drawPhase = new DrawPhase();
 Phase previousPhase = game.getCurrentPhase();
 upkeepPhase.doPhase();
 drawPhase.doPhase();
 assertEquals(DrawPhase.class, game.getCurrentPhase().getClass());
 assertEquals(previousPhase, game.getPreviousPhase());
 }

 @Test
 public void testMain1Phase() {
 Phase main1Phase = new Main1Phase();
 Phase previousPhase = game.getCurrentPhase();
 drawPhase.doPhase();
 main1Phase.doPhase();
 assertEquals(Main1Phase.class, game.getCurrentPhase().getClass());
 assertEquals(previousPhase, game.getPreviousPhase());
 }

 @Test
 public void testCombatBeginningPhase() {
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase previousPhase = game.getCurrentPhase();
 main1Phase.doPhase();
 combatBeginningPhase.doPhase();
 assertEquals(CombatBeginningPhase.class, game.getCurrentPhase().getClass());
 assertEquals(previousPhase, game.getPreviousPhase());
 }

 @Test
 public void testDeclareAttackersPhase() {
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase previousPhase = game.getCurrentPhase();
 combatBeginningPhase.doPhase();
 declareAttackersPhase.doPhase();
 assertEquals(DeclareAttackersPhase.class, game.getCurrentPhase().getClass());
 assertEquals(previousPhase, game.getPreviousPhase());
 }

 @Test
 public void testDeclareBlockersPhase() {
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 Phase previousPhase = game.getCurrentPhase();
 declareAttackersPhase.doPhase();
 declareBlockersPhase.doPhase();
 assertEquals(DeclareBlockersPhase.class, game.getCurrentPhase().getClass());
 assertEquals(previousPhase, game.getPreviousPhase());
 }

 @Test
 public void testCombatDamagePhase() {
 Phase combatDamagePhase = new CombatDamagePhase();
 Phase previousPhase = game.getCurrentPhase();
 declareBlockersPhase.doPhase();
 combatDamagePhase.doPhase();
 assertEquals(CombatDamagePhase.class, game.getCurrentPhase().getClass());
 assertEquals(previousPhase, game.getPreviousPhase());
 }

 @Test
 public void testCombatEndPhase() {
 Phase combatEndPhase = new CombatEndPhase();
 Phase previousPhase = game.getCurrentPhase();
 combatDamagePhase.doPhase();
 combatEndPhase.doPhase();
 assertEquals(CombatEndPhase.class, game.getCurrentPhase().getClass());
 assertEquals(previousPhase, game.getPreviousPhase());
 }

 @Test
 public void testMain2Phase() {
 Phase main2Phase = new Main2Phase();
 Phase previousPhase = game.getCurrentPhase();
 combatEndPhase.doPhase();
 main2Phase.doPhase();
 assertEquals(Main2Phase.class, game.getCurrentPhase().getClass());
 assertEquals(previousPhase, game.getPreviousPhase());
 }

 @Test
 public void testEndOfTurnPhase() {
 Phase endOfTurnPhase = new EndOfTurnPhase();
 Phase previousPhase = game.getCurrentPhase();
 main2Phase.doPhase();
 endOfTurnPhase.doPhase();
 assertEquals(EndOfTurnPhase.class, game.getCurrentPhase().getClass());
 assertEquals(previousPhase, game.getPreviousPhase());
 }

 @Test
 public void testCleanupPhase() {
 Phase cleanupPhase = new CleanupPhase();
 Phase previousPhase = game.getCurrentPhase();
 endOfTurnPhase.doPhase();
 cleanupPhase.doPhase();
 assertEquals(CleanupPhase.class, game.getCurrentPhase().getClass());
 assertEquals(previousPhase, game.getPreviousPhase());
 }

 @Test
 public void testPlayerChangePhase() {
 Phase playerChangePhase = new PlayerChangePhase();
 Phase previousPhase = game.getCurrentPhase();
 cleanupPhase.doPhase();
 playerChangePhase.doPhase();
 assertEquals(UntapPhase.class, game.getCurrentPhase().getClass());
 assertNotNull(game.getPlayingPlayer());
 assertEquals(game.getPlayingPlayer() % 2 + 1, game.getPreviousPlayer());
 }

 @Test
 public void testNextPhase() {
 Phase[] phasesArray = { new UntapPhase(), new UpkeepPhase(), new DrawPhase(),
 new Main1Phase(), new CombatBeginningPhase(), new DeclareAttackersPhase(),
 new DeclareBlockersPhase(), new CombatDamagePhase(), new CombatEndPhase(),
 new Main2Phase(), new EndOfTurnPhase(), new CleanupPhase(), new PlayerChangePhase() };
 for (Phase phase : phasesArray) {
 Phase currentPhase = game.getCurrentPhase();
 game.nextPhase();
 assertEquals(phase.getClass(), game.getCurrentPhase().getClass());
 assertEquals(currentPhase, game.getPreviousPhase());
 }
 }
}