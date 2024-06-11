import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import lotus.core.phases.*;

public class PhaseTest {

 private static Game game = new Game(); // Assuming Game has a no-args constructor
 private static Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase, untapPhaseToTest;

 @Before
 public void setup() {
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
 untapPhaseToTest = untapPhase;
 }

 @Test
 public void testUntapPhase() {
 // Test typical use case
 game.nextPhase();
 assertEquals(UntapPhase.class, Game.currentPhase.getClass());
 game.nextPhase();
 assertEquals(UpkeepPhase.class, Game.currentPhase.getClass());

 // Test error scenario: current phase is not UntapPhase
 Game.currentPhase = new Main1Phase();
 game.nextPhase();
 assertNotEquals(UntapPhase.class, Game.currentPhase.getClass());

 // Test edge case: next phase after EndOfTurnPhase
 game.currentPhase = new EndOfTurnPhase();
 game.nextPhase();
 assertEquals(PlayerChangePhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testUpkeepPhase() {
 // Test typical use case
 game.nextPhase();
 assertEquals(UpkeepPhase.class, Game.currentPhase.getClass());
 game.nextPhase();
 assertEquals(DrawPhase.class, Game.currentPhase.getClass());

 // Test error scenario: current phase is not UpkeepPhase
 game.currentPhase = new Main1Phase();
 game.nextPhase();
 assertNotEquals(UpkeepPhase.class, Game.currentPhase.getClass());

 // Test edge case: next phase after EndOfTurnPhase
 game.currentPhase = new EndOfTurnPhase();
 game.nextPhase();
 assertEquals(PlayerChangePhase.class, Game.currentPhase.getClass());
 }

 // Add similar test methods for other phases with appropriate assertions

 @Test
 public void testChangePhase() {
 game.currentPhase = untapPhaseToTest;
 Phase expectedPhase = null;
 switch (game.currentPhase.getClass().getSimpleName()) {
 case "UntapPhase": expectedPhase = upkeepPhase; break;
 case "UpkeepPhase": expectedPhase = drawPhase; break;
 case "DrawPhase": expectedPhase = main1Phase; break;
 // Add similar case statements for other phases
 }
 assertSame(expectedPhase, Phase.changePhase(game.currentPhase));
 }
}