import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.phases.*;

public class PhaseTest {

 private static Game game = new Game();

 @Test
 public void testUntapPhase() {
 // Test typical use case
 changePhase(new UntapPhase());
 assertTrue(Game.currentPhase instanceof UntapPhase);
 doPhaseTest();

 // Test edge case: Game already in UntapPhase
 changePhase(new UntapPhase());
 assertTrue(Game.currentPhase instanceof UntapPhase);

 // Test error scenario: Phase not found
 changePhase(new Object());
 assertNotNull(Game.currentPhase); // Assert that a new phase was created and assigned, even if it's not the expected one
 assertFalse(Game.currentPhase instanceof UntapPhase);
 }

 @Test
 public void testUpkeepPhase() {
 // Test typical use case
 changePhase(new UpkeepPhase());
 assertTrue(Game.currentPhase instanceof UpkeepPhase);
 doPhaseTest();

 // Test edge case: Game already in UpkeepPhase
 changePhase(new UpkeepPhase());
 assertTrue(Game.currentPhase instanceof UpkeepPhase);

 // Test error scenario: Phase not found
 changePhase(new Object());
 assertNotNull(Game.currentPhase); // Assert that a new phase was created and assigned, even if it's not the expected one
 assertFalse(Game.currentPhase instanceof UpkeepPhase);
 }

 // ... Repeat test cases for DrawPhase, Main1Phase, CombatBeginningPhase, DeclareAttackersPhase, DeclareBlockersPhase, CombatDamagePhase, CombatEndPhase, Main2Phase, EndOfTurnPhase, and CleanupPhase

 private void doPhaseTest() {
 // Add testing logic for each method's behavior here
 }

}