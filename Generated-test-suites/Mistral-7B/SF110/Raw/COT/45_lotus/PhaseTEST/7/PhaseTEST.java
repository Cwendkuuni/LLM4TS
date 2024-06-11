import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase;

 @Before
 public void setup() {
 Game.reset();
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

 @Test
 public void testDoPhaseAbstractMethod() {
 assertNull("doPhase method should be abstract", untapPhase.doPhase);
 }

 @Test
 public void testChangePhaseMethod() {
 // Test normal flow
 Game.setCurrentPhase(new UntapPhase());
 Phase.nextPhase();
 assertInstanceOf("Current phase should be UpkeepPhase after NextPhase()", UpkeepPhase.class, Game.getCurrentPhase());

 // Test end of cycle phase change
 Game.setCurrentPhase(new EndOfTurnPhase());
 Phase.nextPhase();
 assertInstanceOf("Current phase should be PlayerChangePhase after EndOfTurnPhase", PlayerChangePhase.class, Game.getCurrentPhase());
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateExceptionWhenCurrentPhaseIsNull() {
 Game.setCurrentPhase(null);
 Phase.nextPhase();
 fail("IllegalStateException should be thrown when currentPhase is null");
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateExceptionWhenCurrentPhaseIsNotANamedPhase() {
 Game.setCurrentPhase(new Object());
 Phase.nextPhase();
 fail("IllegalStateException should be thrown when currentPhase is not an instance of a named phase");
 }

 @Test
 public void testChangePhaseMethodWithInvalidOrder() {
 Game.setCurrentPhase(new UpkeepPhase());
 // This should not change the current phase as UpkeepPhase is not following CombatDamagePhase
 Phase.nextPhase();
 assertSame("Current phase should still be UpkeepPhase after NextPhase()", UpkeepPhase.class, Game.getCurrentPhase());
}

 // Add more test cases for edge cases and specific exceptions as needed here...

}