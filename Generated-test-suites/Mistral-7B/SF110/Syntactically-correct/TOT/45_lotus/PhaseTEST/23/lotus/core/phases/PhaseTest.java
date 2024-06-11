import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Game game;
 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase, untapPhaseNextPlayer;

 @Before
 public void setUp() {
 game = new Game();
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
 untapPhaseNextPlayer = new UntapPhase();
 }

 @Test
 public void testUntapPhase() {
 // typical use case: no changes in game state
 Phase.nextPhase();
 assertSame(untapPhase, Game.currentPhase);
 game.doTurn();
 assertSame(untapPhase, Game.currentPhase);

 // edge case: game is already in untap phase
 Phase.nextPhase();
 assertSame(upkeepPhase, Game.currentPhase);

 // error scenario: game is in an invalid phase
 Game.currentPhase = new Object();
 assertThrows(IllegalStateException.class, () -> Phase.nextPhase());
 }

 // test other methods similarly with their corresponding use cases, edge cases, and error scenarios

 @Test
 public void testNextPhaseThroughAllPhases() {
 Phase currentPhase = untapPhase;
 for (int i = 0; i < 13; i++) {
 currentPhase = Phase.nextPhase();
 assertEquals(currentPhase.getClass(), getClassOfNextPhase(currentPhase));
 }
 assertEquals(untapPhase.getClass(), getClassOfNextPhase(currentPhase));
 }

 private Class<?> getClassOfNextPhase(Phase phase) {
 switch (phase.getClass().getSimpleName()) {
 case "UntapPhase": return UpkeepPhase.class;
 case "UpkeepPhase": return DrawPhase.class;
 case "DrawPhase": return Main1Phase.class;
 case "Main1Phase": return CombatBeginningPhase.class;
 case "CombatBeginningPhase": return DeclareAttackersPhase.class;
 case "DeclareAttackersPhase": return DeclareBlockersPhase.class;
 case "DeclareBlockersPhase": return CombatDamagePhase.class;
 case "CombatDamagePhase": return CombatEndPhase.class;
 case "CombatEndPhase": return Main2Phase.class;
 case "Main2Phase": return EndOfTurnPhase.class;
 case "EndOfTurnPhase": return CleanupPhase.class;
 case "CleanupPhase": return PlayerChangePhase.class;
 case "PlayerChangePhase": return UntapPhase.class;
 default: throw new IllegalStateException("Unknown phase: " + phase.getClass().getSimpleName());
 }
}

}