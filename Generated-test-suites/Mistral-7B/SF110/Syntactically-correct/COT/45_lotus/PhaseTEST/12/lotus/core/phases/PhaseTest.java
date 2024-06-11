import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.phases.*;

/**
 * PhaseTest.java - JUnit test file for the Phase class.
 */
public class PhaseTest {

 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase;

 /**
  * Setup method to create instances of all subclasses of Phase.
  */
 @Before
 public void setUp() {
 untapPhase = new UntapPhase();
 upkeepPhase = new UpkeepPhase();
 drawPhase = new DrawPhase();
 main1Phase = new Main1Phase();
 combatBeginningPhase = new CombatBeginningPhase();
 declareAttackersPhase = new DeclareAttackersPhase();
 declareBlockersPhase = new DeclareBlockersPhase();
 combatDamagePhase = new CombatDamagePhase();
 combatEndPhase = new CombatEndPhase();
 main2Phase = new Main2Phase();
 endOfTurnPhase = new EndOfTurnPhase();
 cleanupPhase = new CleanupPhase();
 playerChangePhase = new PlayerChangePhase();
 }

 /**
  * Test method for abstract doPhase() method.
  * Since it's an abstract method, we cannot instantiate it or call it directly.
  * Instead, we'll test the behavior of the class by checking if changing phases actually works.
  */
 @Test
 public void testDoPhase() {
 // Perform a series of phase changes and check if each phase is changed correctly.
 Phase[] phases = { untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase };
 Game.currentPhase = untapPhase;
 assertEquals(untapPhase, Game.currentPhase);
 for (Phase phase : phases) {
 nextPhase();
 assertEquals(phase, Game.currentPhase);
 }
 }

 /**
  * Test method to check the behavior of nextPhase() method when the current phase is the last phase (CleanupPhase).
  */
 @Test
 public void testNextPhaseLastPhase() {
 Game.currentPhase = cleanupPhase;
 nextPhase();
 assertEquals(playerChangePhase, Game.currentPhase);
 }

 /**
  * Test method to check the behavior of nextPhase() method when the current phase is not an instance of any Phase subclass.
  */
 @Test(expected = IllegalArgumentException.class)
 public void testNextPhaseInvalidPhase() {
 Game.currentPhase = new Object();
 nextPhase();
 fail("IllegalArgumentException expected but not thrown.");
 }
}