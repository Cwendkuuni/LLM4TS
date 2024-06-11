import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Phase currentPhase;
 private Game game;

 @Before
 public void setup() {
 game = new Game();
 currentPhase = game.getPhase();
 }

 @Test
 public void testDoPhase_AbstractMethodThrowsException() {
 assertFalse("Abstract methods should not be called directly", currentPhase instanceof PhaseWithDoPhase);
 try {
 currentPhase.doPhase();
 fail("doPhase() should throw an UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextPhase_InvalidCurrentPhase() {
 Phase invalidPhase = new Object() {}; // Replace this with a valid Phase subclass
 game.setCurrentPhase(invalidPhase);
 PhaseTest.nextPhase(game);
 }

 @Test
 public void testNextPhase_ValidSequence() {
 Phase phases[] = { new UntapPhase(), new UpkeepPhase(), new DrawPhase(), new Main1Phase(), new CombatBeginningPhase(), new DeclareAttackersPhase(), new DeclareBlockersPhase(), new CombatDamagePhase(), new CombatEndPhase(), new Main2Phase(), new EndOfTurnPhase(), new CleanupPhase(), new PlayerChangePhase(), new UntapPhase() };
 for (Phase phase : phases) {
 assertEquals(phase.getClass().getSimpleName(), game.getPhase().getClass().getSimpleName());
 PhaseTest.nextPhase(game);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_InvalidTransition() {
 // Test the edge case where the current phase is already the last phase in the sequence (CleanupPhase)
 currentPhase = new CleanupPhase();
 PhaseTest.nextPhase(game);
 }

 @Test
 public void testChangePhase_ValidTransition() {
 Phase phases[] = { new UntapPhase(), new UpkeepPhase(), new DrawPhase(), new Main1Phase(), new CombatBeginningPhase(), new DeclareAttackersPhase(), new DeclareBlockersPhase(), new CombatDamagePhase(), new CombatEndPhase(), new Main2Phase(), new EndOfTurnPhase(), new CleanupPhase(), new PlayerChangePhase(), new UntapPhase() };
 for (int i = 0; i < phases.length; i++) {
 Phase current = phases[i];
 Phase next = phases[i + 1];
 game.setCurrentPhase(current);
 PhaseTest.nextPhase(game);
 assertEquals(next.getClass(), game.getPhase().getClass());
 }
 }

}