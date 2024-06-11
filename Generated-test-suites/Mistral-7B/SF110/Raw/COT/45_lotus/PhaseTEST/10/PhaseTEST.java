import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.phases.Phase;
import lotus.core.effect.ChangePhase;

public class PhaseTest {

 private Phase currentPhase;

 @Before
 public void setUp() {
 Game.currentPhase = new UntapPhase();
 currentPhase = Game.currentPhase;
 }

 @Test
 public void testDoPhaseAbstractMethodThrowsException() {
 try {
 currentPhase.doPhase();
 fail("doPhase() should throw an UnsupportedOperationException.");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testNextPhaseChangePhaseCorrectly() {
 Phase[] phases = {new UntapPhase(), new UpkeepPhase(), new DrawPhase(), new Main1Phase(), new CombatBeginningPhase(), new DeclareAttackersPhase(), new DeclareBlockersPhase(), new CombatDamagePhase(), new CombatEndPhase(), new Main2Phase(), new EndOfTurnPhase(), new CleanupPhase(), new PlayerChangePhase()};

 for (Phase phase : phases) {
 Phase previousPhase = currentPhase;
 currentPhase = Phase.nextPhase();
 assertEquals(previousPhase.getClass(), currentPhase.getClass() == phase.getClass() ? previousPhase.getClass() : previousPhase.getClass().getSuperclass(), "Incorrect phase transition.");
 }

 // Test transitioning back to the same phase
 previousPhase = currentPhase;
 currentPhase = Phase.nextPhase();
 assertEquals(previousPhase.getClass(), currentPhase.getClass(), "Should not transition to a different phase when trying to transition back to the same phase.");
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhaseInvalidCurrentPhase() {
 Phase[] phases = {new UntapPhase(), new UpkeepPhase(), new DrawPhase(), new Main1Phase(), new CombatBeginningPhase(), new DeclareAttackersPhase(), new DeclareBlockersPhase(), new CombatDamagePhase(), new CombatEndPhase(), new Main2Phase(), new EndOfTurnPhase(), new CleanupPhase(), new PlayerChangePhase()};

 for (Phase phase : phases) {
 Phase previousPhase = currentPhase;
 for (int i = 0; i < phases.length; i++) {
 if (phase == phases[i]) {
 currentPhase = phases[i + 1];
 break;
 }
 currentPhase = Phase.nextPhase();
 }
 assertEquals(previousPhase.getClass(), currentPhase.getClass() == phase.getClass() ? previousPhase.getClass() : previousPhase.getClass().getSuperclass(), "Incorrect phase transition.");
 }

 // Test transitioning past the last phase
 Phase lastPhase = phases[phases.length - 1];
 Phase previousLastPhase = currentPhase;
 currentPhase = Phase.nextPhase();
 assertFalse(lastPhase.getClass().isInstance(currentPhase), "Should not be able to transition past the last phase.");
 assertEquals(previousLastPhase.getClass(), currentPhase.getClass(), "Should stay on the last phase when trying to transition past it.");
 }

}