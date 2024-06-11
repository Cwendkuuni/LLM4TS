package lotus.core.phases;

import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

 // Test for nextPhase method when currentPhase is UntapPhase
 @Test
 public void testNextPhaseUntapPhase() {
 Game game = new Game();
 Phase untapPhase = new UntapPhase();
 Game.currentPhase = untapPhase;

 Phase expectedNextPhase = new UpkeepPhase();
 Phase actualNextPhase = Phase.nextPhase();

 assertEquals(expectedNextPhase, actualNextPhase);
 }

 // Test for nextPhase method when currentPhase is UpkeepPhase
 @Test
 public void testNextPhaseUpkeepPhase() {
 Game game = new Game();
 Phase upkeepPhase = new UpkeepPhase();
 Game.currentPhase = upkeepPhase;

 Phase expectedNextPhase = new DrawPhase();
 Phase actualNextPhase = Phase.nextPhase();

 assertEquals(expectedNextPhase, actualNextPhase);
 }

 // Test for nextPhase method when currentPhase is DrawPhase
 @Test
 public void testNextPhaseDrawPhase() {
 Game game = new Game();
 Phase drawPhase = new DrawPhase();
 Game.currentPhase = drawPhase;

 Phase expectedNextPhase = new Main1Phase();
 Phase actualNextPhase = Phase.nextPhase();

 assertEquals(expectedNextPhase, actualNextPhase);
 }

 // Test for nextPhase method when currentPhase is Main1Phase
 @Test
 public void testNextPhaseMain1Phase() {
 Game game = new Game();
 Phase main1Phase = new Main1Phase();
 Game.currentPhase = main1Phase;

 Phase expectedNextPhase = new CombatBeginningPhase();
 Phase actualNextPhase = Phase.nextPhase();

 assertEquals(expectedNextPhase, actualNextPhase);
 }

 // Test for nextPhase method when currentPhase is CombatBeginningPhase
 @Test
 public void testNextPhaseCombatBeginningPhase() {
 Game game = new Game();
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Game.currentPhase = combatBeginningPhase;

 Phase expectedNextPhase = new DeclareAttackersPhase();
 Phase actualNextPhase = Phase.nextPhase();

 assertEquals(expectedNextPhase, actualNextPhase);
 }

 // Test for nextPhase method when currentPhase is DeclareAttackersPhase
 @Test
 public void testNextPhaseDeclareAttackersPhase() {
 Game game = new Game();
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Game.currentPhase = declareAttackersPhase;

 Phase expectedNextPhase = new DeclareBlockersPhase();
 Phase actualNextPhase = Phase.nextPhase();

 assertEquals(expectedNextPhase, actualNextPhase);
 }

 // Test for nextPhase method when currentPhase is DeclareBlockersPhase
 @Test
 public void testNextPhaseDeclareBlockersPhase() {
 Game game = new Game();
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 Game.currentPhase = declareBlockersPhase;

 Phase expectedNextPhase = new CombatDamagePhase();
 Phase actualNextPhase = Phase.nextPhase();

 assertEquals(expectedNextPhase, actualNextPhase);
 }

 // Test for nextPhase method when currentPhase is CombatDamagePhase
 @Test
 public void testNextPhaseCombatDamagePhase() {
 Game game = new Game();
 Phase combatDamagePhase = new CombatDamagePhase();
 Game.currentPhase = combatDamagePhase;

 Phase expectedNextPhase = new CombatEndPhase();
 Phase actualNextPhase = Phase.nextPhase();

 assertEquals(expectedNextPhase, actualNextPhase);
 }

 // Test for nextPhase method when currentPhase is CombatEndPhase
 @Test
 public void testNextPhaseCombatEndPhase() {
 Game game = new Game();
 Phase combatEndPhase = new CombatEndPhase();
 Game.currentPhase = combatEndPhase;

 Phase expectedNextPhase = new Main2Phase();
 Phase actualNextPhase = Phase.nextPhase();

 assertEquals(expectedNextPhase, actualNextPhase);
 }

 // Test for nextPhase method when currentPhase is Main2Phase
 @Test
 public void testNextPhaseMain2Phase() {
 Game game = new Game();
 Phase main2Phase = new Main2Phase();
 Game.currentPhase = main2Phase;

 Phase expectedNextPhase = new EndOfTurnPhase();
 Phase actualNextPhase = Phase.nextPhase();

 assertEquals(expectedNextPhase, actualNextPhase);
 }

 // Test for nextPhase method when currentPhase is EndOfTurnPhase
 @Test
 public void testNextPhaseEndOfTurnPhase() {
 Game game = new Game();
 Phase endOfTurnPhase = new EndOfTurnPhase();
 Game.currentPhase = endOfTurnPhase;

 Phase expectedNextPhase = new CleanupPhase();
 Phase actualNextPhase = Phase.nextPhase();

 assertEquals(expectedNextPhase, actualNextPhase);
 }

 // Test for nextPhase method when currentPhase is CleanupPhase
 @Test
 public void testNextPhaseCleanupPhase() {
 Game game = new Game();
 Phase cleanupPhase = new CleanupPhase();
 Game.currentPhase = cleanupPhase;

 Phase expectedNextPhase = new PlayerChangePhase();
 Phase actualNextPhase = Phase.nextPhase();

 assertEquals(expectedNextPhase, actualNextPhase);
 }

 // Test for nextPhase method when currentPhase is PlayerChangePhase
 @Test
 public void testNextPhasePlayerChangePhase() {
 Game game = new Game();
 Phase playerChangePhase = new PlayerChangePhase();
 Game.currentPhase = playerChangePhase;

 assertEquals(game.playingPlayer, 1); // Test initial player

 Phase untapPhase = new UntapPhase();
 Phase.nextPhase();
 Phase nextPhase = Phase.nextPhase();

 assertEquals(nextPhase, untapPhase);
 assertEquals(game.playingPlayer, 2); // Test player change
 }

 // Test for nextPhase method when currentPhase is not an instance of any known Phase subclass
 @Test (expected = AssertionError.class)
 public void testNextPhaseUnknownPhase() {
 Game game = new Game();
 Phase unknownPhase = new Object();
 Game.currentPhase = unknownPhase;

 Phase.nextPhase();
 }
}