package lotus.core.phases;

import static org.junit.Assert.*;

import org.junit.Test;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

 private static Game game = new Game();

 @Test
 public void testUntapPhase() {
 // Test normal flow
 changePhase(new UntapPhase());
 assertEquals(UntapPhase.class, Game.currentPhase.getClass());
 doPhaseCallsCorrectly();

 // Test error: currentPhase is not an instance of UntapPhase
 changePhase(new UpkeepPhase());
 assertNotEquals(UntapPhase.class, Game.currentPhase.getClass());

 // Test edge case: currentPhase is already UntapPhase
 changePhase(new UntapPhase());
 changePhase(new UntapPhase());
 assertEquals(UntapPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testUpkeepPhase() {
 // Test normal flow
 changePhase(new UpkeepPhase());
 assertEquals(UpkeepPhase.class, Game.currentPhase.getClass());
 doPhaseCallsCorrectly();

 // Test error: currentPhase is not an instance of UpkeepPhase
 changePhase(new UntapPhase());
 assertNotEquals(UpkeepPhase.class, Game.currentPhase.getClass());

 // Test edge case: currentPhase is already UpkeepPhase
 changePhase(new UpkeepPhase());
 changePhase(new UpkeepPhase());
 assertEquals(UpkeepPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testDrawPhase() {
 // Test normal flow
 changePhase(new DrawPhase());
 assertEquals(DrawPhase.class, Game.currentPhase.getClass());
 doPhaseCallsCorrectly();

 // Test error: currentPhase is not an instance of DrawPhase
 changePhase(new UntapPhase());
 assertNotEquals(DrawPhase.class, Game.currentPhase.getClass());

 // Test edge case: currentPhase is already DrawPhase
 changePhase(new DrawPhase());
 changePhase(new DrawPhase());
 assertEquals(DrawPhase.class, Game.currentPhase.getClass());
 }

 // ... Repeat for the remaining methods: Main1Phase, CombatBeginningPhase, DeclareAttackersPhase, DeclareBlockersPhase, CombatDamagePhase, CombatEndPhase, Main2Phase, EndOfTurnPhase, and CleanupPhase

 private void doPhaseCallsCorrectly() {
 // Add assertions here to check that the doPhase() method of the current phase has been called
 }

 private void changePhase(Phase phase) {
 game.setCurrentPhase(phase);
 Phase.nextPhase();
 }

}