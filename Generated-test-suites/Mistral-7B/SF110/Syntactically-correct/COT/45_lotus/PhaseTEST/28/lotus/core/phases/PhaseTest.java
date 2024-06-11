import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.phases.Phase;
import lotus.core.effect.ChangePhase;

public class PhaseTest {

 private Phase currentPhase;

 @BeforeClass
 public static void setUp() {
 Game.currentPhase = new UntapPhase();
 }

 @Test
 public void testDoPhase_UntapPhase() {
 currentPhase = new UptapPhase();
 currentPhase.doPhase();
 assertFalse(Game.currentPhase instanceof UptapPhase);
 // Add assertions for specific behavior of UptapPhase if applicable
 }

 @Test
 public void testDoPhase_UpkeepPhase() {
 currentPhase = new UpkeepPhase();
 currentPhase.doPhase();
 assertFalse(Game.currentPhase instanceof UpkeepPhase);
 // Add assertions for specific behavior of UpkeepPhase if applicable
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_NoCurrentPhase() {
 Phase.nextPhase();
 fail("Expected IllegalStateException when no current phase");
 }

 @Test
 public void testNextPhase_ValidSequence() {
 Phase[] phases = {new UntapPhase(), new UpkeepPhase(), new DrawPhase(), new Main1Phase(), new CombatBeginningPhase(), new DeclareAttackersPhase(), new DeclareBlockersPhase(), new CombatDamagePhase(), new CombatEndPhase(), new Main2Phase(), new EndOfTurnPhase(), new CleanupPhase(), new PlayerChangePhase(), new UntapPhase()};
 for (int i = 0; i < phases.length; i++) {
 currentPhase = phases[i];
 Phase nextPhase = phases[i + 1];
 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 currentPhase.doPhase();
 }
 // Add assertions for specific behavior of each phase during its turn
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_InvalidSequence() {
 Phase[] invalidPhases = {new Main1Phase(), new CombatBeginningPhase(), new CombatDamagePhase(), new Main2Phase(), new EndOfTurnPhase(), new CleanupPhase(), new PlayerChangePhase(), new UntapPhase()};
 for (Phase invalidPhase : invalidPhases) {
 Phase previousPhase = new UpkeepPhase();
 previousPhase.doPhase();
 try {
 Phase.nextPhase();
 fail("Expected IllegalStateException when transitioning to invalid phase: " + invalidPhase.getClass().getSimpleName());
 } catch (IllegalStateException e) {
 // continue
 }
 assertEquals(previousPhase, Game.currentPhase);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChangePhase_NullPhase() {
 try {
 Phase.changePhase(null);
 fail("Expected IllegalArgumentException when trying to change phase to null");
 } catch (IllegalArgumentException e) {
 // continue
 }
 }
}