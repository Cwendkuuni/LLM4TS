import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Phase currentPhase;
 private Game game;

 @Before
 public void setUp() {
 game = new Game();
 currentPhase = game.getPhase();
 }

 @Test
 public void testDoPhase_AbstractMethodThrowsException() {
 assertNotNull(currentPhase);
 try {
 currentPhase.doPhase();
 failure("doPhase should not be called on an abstract class");
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testNextPhase_TransitionsToCorrectPhases() {
 Phase[] phases = {new UntapPhase(), new UpkeepPhase(), new DrawPhase(), new Main1Phase(), new CombatBeginningPhase(), new DeclareAttackersPhase(), new DeclareBlockersPhase(), new CombatDamagePhase(), new CombatEndPhase(), new Main2Phase(), new EndOfTurnPhase(), new CleanupPhase(), new PlayerChangePhase()};
 for (int i = 0; i < phases.length; i++) {
 Phase phase = phases[i];
 Phase previousPhase = currentPhase;
 currentPhase = phase;
 Phase nextPhase = phases[i < phases.length - 1 ? i + 1 : 0];

 nextPhase();

 assertEquals(nextPhase, game.getPhase());
 assertEquals(previousPhase, game.getPreviousPhase());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_ThrowsExceptionWhenCurrentPhaseIsNull() {
 game.setCurrentPhase(null);
 nextPhase();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_ThrowsExceptionWhenCurrentPhaseIsNotInstanceOfPhase() {
 game.setCurrentPhase(new Object());
 nextPhase();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_ThrowsExceptionWhenCurrentPhaseIsEndOfTurnPhaseAndPreviousPhaseIsDeclareBlockersPhase() {
 game.setCurrentPhase(new EndOfTurnPhase());
 game.setPreviousPhase(new DeclareBlockersPhase());
 nextPhase();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_ThrowsExceptionWhenCurrentPhaseIsPlayerChangePhaseAndPreviousPhaseIsCleanupPhase() {
 game.setCurrentPhase(new PlayerChangePhase());
 game.setPreviousPhase(new CleanupPhase());
 nextPhase();
 }
}