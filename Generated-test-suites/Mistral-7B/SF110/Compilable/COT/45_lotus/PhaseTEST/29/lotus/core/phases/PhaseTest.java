package lotus.core.phases;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.phases.*;

public class PhaseTest {

 private Phase[] phases = {new UntapPhase(), new UpkeepPhase(), new DrawPhase(), new Main1Phase(), new CombatBeginningPhase(), new DeclareAttackersPhase(), new DeclareBlockersPhase(), new CombatDamagePhase(), new CombatEndPhase(), new Main2Phase(), new EndOfTurnPhase(), new CleanupPhase(), new PlayerChangePhase()};

 @Test
 public void testDoPhase() {
 for (Phase phase : phases) {
 System.out.println("Testing phase: " + phase.getClass().getSimpleName());
 phase.doPhase();
 assertNotSame(phase, Game.currentPhase);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_CurrentPhaseIsNotUntapPhase() {
 Game.currentPhase = new UpkeepPhase();
 Phase.nextPhase();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_CurrentPhaseIsNotUpkeepPhase() {
 Game.currentPhase = new DrawPhase();
 Phase.nextPhase();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_CurrentPhaseIsNotDrawPhase() {
 Game.currentPhase = new Main1Phase();
 Phase.nextPhase();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_CurrentPhaseIsNotMain1Phase() {
 Game.currentPhase = new CombatBeginningPhase();
 Phase.nextPhase();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_CurrentPhaseIsNotCombatBeginningPhase() {
 Game.currentPhase = new DeclareAttackersPhase();
 Phase.nextPhase();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_CurrentPhaseIsNotDeclareAttackersPhase() {
 Game.currentPhase = new DeclareBlockersPhase();
 Phase.nextPhase();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_CurrentPhaseIsNotDeclareBlockersPhase() {
 Game.currentPhase = new CombatDamagePhase();
 Phase.nextPhase();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_CurrentPhaseIsNotCombatDamagePhase() {
 Game.currentPhase = new CombatEndPhase();
 Phase.nextPhase();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_CurrentPhaseIsNotCombatEndPhase() {
 Game.currentPhase = new Main2Phase();
 Phase.nextPhase();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_CurrentPhaseIsNotMain2Phase() {
 Game.currentPhase = new EndOfTurnPhase();
 Phase.nextPhase();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_CurrentPhaseIsNotEndOfTurnPhase() {
 Game.currentPhase = new CleanupPhase();
 Phase.nextPhase();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_CurrentPhaseIsNotCleanupPhase() {
 Game.currentPhase = new PlayerChangePhase();
 Phase.nextPhase();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_CurrentPhaseIsNotPlayerChangePhase() {
 Game.currentPhase = new UntapPhase();
 Phase.nextPhase();
 }

 @Test
 public void testNextPhase_CurrentPhaseIsUntapPhase() {
 Game.currentPhase = new UntapPhase();
 Phase.nextPhase();
 assertTrue(Game.currentPhase instanceof UpkeepPhase);
 }

}