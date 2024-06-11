package lotus.core.phases.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.card.Card;
import lotus.core.effect.ChangePhase;
import lotus.core.phases.Phase;
import lotus.core.phases.UntapPhase;
import lotus.core.phases.UpkeepPhase;
import lotus.core.phases.DrawPhase;
import lotus.core.phases.Main1Phase;
import lotus.core.phases.CombatBeginningPhase;
import lotus.core.phases.DeclareAttackersPhase;
import lotus.core.phases.DeclareBlockersPhase;
import lotus.core.phases.CombatDamagePhase;
import lotus.core.phases.CombatEndPhase;
import lotus.core.phases.Main2Phase;
import lotus.core.phases.EndOfTurnPhase;
import lotus.core.phases.CleanupPhase;
import lotus.core.phases.PlayerChangePhase;

public class PhaseTest {

 private Phase currentPhase;
 private Game game;

 @Before
 public void setup() {
 game = new Game();
 currentPhase = new UntapPhase();
 Game.setCurrentPhase(currentPhase);
 }

 @Test
 public void testUntapPhase() {
 // Test untap phase logic
 currentPhase.doPhase();
 assertEquals("UntapPhase", Game.getCurrentPhase().getClass().getSimpleName());

 // Test changing to upkeep phase
 Phase upkeepPhase = new UpkeepPhase();
 Phase.nextPhase();
 assertEquals("UpkeepPhase", Game.getCurrentPhase().getClass().getSimpleName());
 }

 @Test
 public void testUpkeepPhase() {
 // Test upkeep phase logic
 upkeepPhase.doPhase();
 assertEquals("UpkeepPhase", Game.getCurrentPhase().getClass().getSimpleName());

 // Test changing to draw phase
 Phase drawPhase = new DrawPhase();
 Phase.nextPhase();
 assertEquals("DrawPhase", Game.getCurrentPhase().getClass().getSimpleName());
 }

 @Test
 public void testDrawPhase() {
 // Test draw phase logic
 drawPhase.doPhase();
 assertEquals("DrawPhase", Game.getCurrentPhase().getClass().getSimpleName());

 // Test changing to main1 phase
 Phase main1Phase = new Main1Phase();
 Phase.nextPhase();
 assertEquals("Main1Phase", Game.getCurrentPhase().getClass().getSimpleName());
 }

 @Test
 public void testMain1Phase() {
 // Test main1 phase logic
 main1Phase.doPhase();
 assertEquals("Main1Phase", Game.getCurrentPhase().getClass().getSimpleName());

 // Test changing to combat beginning phase
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase.nextPhase();
 assertEquals("CombatBeginningPhase", Game.getCurrentPhase().getClass().getSimpleName());
 }

 @Test
 public void testCombatBeginningPhase() {
 // Test combat beginning phase logic
 combatBeginningPhase.doPhase();
 assertEquals("CombatBeginningPhase", Game.getCurrentPhase().getClass().getSimpleName());

 // Test changing to declare attackers phase
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase.nextPhase();
 assertEquals("DeclareAttackersPhase", Game.getCurrentPhase().getClass().getSimpleName());
 }

 @Test
 public void testDeclareAttackersPhase() {
 // Test declare attackers phase logic
 declareAttackersPhase.doPhase();
 assertEquals("DeclareAttackersPhase", Game.getCurrentPhase().getClass().getSimpleName());

 // Test changing to declare blockers phase
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 Phase.nextPhase();
 assertEquals("DeclareBlockersPhase", Game.getCurrentPhase().getClass().getSimpleName());
 }

 @Test
 public void testDeclareBlockersPhase() {
 // Test declare blockers phase logic
 declareBlockersPhase.doPhase();
 assertEquals("DeclareBlockersPhase", Game.getCurrentPhase().getClass().getSimpleName());

 // Test changing to combat damage phase
 Phase combatDamagePhase = new CombatDamagePhase();
 Phase.nextPhase();
 assertEquals("CombatDamagePhase", Game.getCurrentPhase().getClass().getSimpleName());
 }

 @Test
 public void testCombatDamagePhase() {
 // Test combat damage phase logic
 combatDamagePhase.doPhase();
 assertEquals("CombatDamagePhase", Game.getCurrentPhase().getClass().getSimpleName());

 // Test changing to combat end phase
 Phase combatEndPhase = new CombatEndPhase();
 Phase.nextPhase();
 assertEquals("CombatEndPhase", Game.getCurrentPhase().getClass().getSimpleName());
 }

 @Test
 public void testCombatEndPhase() {
 // Test combat end phase logic
 combatEndPhase.doPhase();
 assertEquals("CombatEndPhase", Game.getCurrentPhase().getClass().getSimpleName());

 // Test changing to main2 phase
 Phase main2Phase = new Main2Phase();
 Phase.nextPhase();
 assertEquals("Main2Phase", Game.getCurrentPhase().getClass().getSimpleName());
 }

 @Test
 public void testMain2Phase() {
 // Test main2 phase logic
 main2Phase.doPhase();
 assertEquals("Main2Phase", Game.getCurrentPhase().getClass().getSimpleName());

 // Test changing to end of turn phase
 Phase endOfTurnPhase = new EndOfTurnPhase();
 Phase.nextPhase();
 assertEquals("EndOfTurnPhase", Game.getCurrentPhase().getClass().getSimpleName());
 }

 @Test
 public void testEndOfTurnPhase() {
 // Test end of turn phase logic
 endOfTurnPhase.doPhase();
 assertEquals("EndOfTurnPhase", Game.getCurrentPhase().getClass().getSimpleName());

 // Test changing to cleanup phase
 Phase cleanupPhase = new CleanupPhase();
 Phase.nextPhase();
 assertEquals("CleanupPhase", Game.getCurrentPhase().getClass().getSimpleName());
 }

 @Test
 public void testCleanupPhase() {
 // Test cleanup phase logic
 cleanupPhase.doPhase();
 assertEquals("CleanupPhase", Game.getCurrentPhase().getClass().getSimpleName());

 // Test changing to player change phase
 Phase playerChangePhase = new PlayerChangePhase();
 Phase.nextPhase();
 assertEquals("PlayerChangePhase", Game.getCurrentPhase().getClass().getSimpleName());
 }

 @Test
 public void testPlayerChangePhase() {
 // Test player change phase logic
 playerChangePhase.doPhase();
 assertEquals("PlayerChangePhase", Game.getCurrentPhase().getClass().getSimpleName());

 // Test changing back to untap phase
 Phase untapPhase = new UntapPhase();
 Phase.nextPhase();
 assertEquals("UntapPhase", Game.getCurrentPhase().getClass().getSimpleName());
 }

 @Test
 public void testChangingPhasesMultipleTimes() {
 // Test changing phases multiple times
 for (int i = 0; i < 10; i++) {
 Phase nextPhase = null;
 if (Game.getCurrentPhase() instanceof UntapPhase) {
 nextPhase = new UpkeepPhase();
 }
 else if (Game.getCurrentPhase() instanceof UpkeepPhase) {
 nextPhase = new DrawPhase();
 }
 // ... continue for all phases
 Phase.nextPhase();
 assertEquals(nextPhase.getClass().getSimpleName(), Game.getCurrentPhase().getClass().getSimpleName());
 }
 }
}