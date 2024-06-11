import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.phases.Phase;
import lotus.core.effect.ChangePhase;

public class PhaseTest {

 private Phase currentPhase;
 private Phase nextPhase;

 @Before
 public void setUp() {
 Game.currentPhase = new UntapPhase();
 currentPhase = Game.currentPhase;
 }

 @Test
 public void testDoPhaseAbstractMethodThrowsException() {
 try {
 currentPhase.doPhase();
 fail("doPhase method should not be called on abstract class");
 } catch (AssertionError e) {
 // Expected
 }
 }

 @Test
 public void testChangePhase() {
 nextPhase = null;

 // Test normal phase transition
 Phase[] phases = {new UpkeepPhase(), new DrawPhase(), new Main1Phase(), new CombatBeginningPhase(), new DeclareAttackersPhase(), new DeclareBlockersPhase(), new CombatDamagePhase(), new CombatEndPhase(), new Main2Phase(), new EndOfTurnPhase(), new CleanupPhase(), new PlayerChangePhase(), new UntapPhase()};
 for (Phase phase : phases) {
 nextPhase = phase;
 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 // Test edge cases
 Game.currentPhase = null;
 try {
 Phase.nextPhase();
 fail("nextPhase method should throw an exception when Game.currentPhase is null");
 } catch (NullPointerException e) {
 // Expected
 }

 Game.currentPhase = new Phase() {}; // Unknown phase
 try {
 nextPhase = new PlayerChangePhase();
 Phase.nextPhase();
 fail("nextPhase method should throw an exception when the next phase is not defined");
 } catch (AssertionError e) {
 // Expected
 }
 }

 @After
 public void tearDown() {
 Game.currentPhase = currentPhase;
 }

}