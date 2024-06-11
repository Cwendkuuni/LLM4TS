package lotus.core.phases;

import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import org.junit.Before;
import org.junit.Test;

public class PhaseTest {

 private Game game;
 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase;

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
 }

 @Test
 public void testNextPhase_UntapPhaseToUpkeepPhase() {
 game.currentPhase = untapPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(upkeepPhase.getClass(), nextPhase.getClass());
 }

 @Test
 public void testNextPhase_UpkeepPhaseToDrawPhase() {
 game.currentPhase = upkeepPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(drawPhase.getClass(), nextPhase.getClass());
 }

 @Test
 public void testNextPhase_DrawPhaseToMain1Phase() {
 game.currentPhase = drawPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(main1Phase.getClass(), nextPhase.getClass());
 }

 @Test
 public void testNextPhase_Main1PhaseToCombatBeginningPhase() {
 game.currentPhase = main1Phase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(combatBeginningPhase.getClass(), nextPhase.getClass());
 }

 @Test
 public void testNextPhase_CombatBeginningPhaseToDeclareAttackersPhase() {
 game.currentPhase = combatBeginningPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(declareAttackersPhase.getClass(), nextPhase.getClass());
 }

 @Test
 public void testNextPhase_DeclareAttackersPhaseToDeclareBlockersPhase() {
 game.currentPhase = declareAttackersPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(declareBlockersPhase.getClass(), nextPhase.getClass());
 }

 @Test
 public void testNextPhase_DeclareBlockersPhaseToCombatDamagePhase() {
 game.currentPhase = declareBlockersPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(combatDamagePhase.getClass(), nextPhase.getClass());
 }

 @Test
 public void testNextPhase_CombatDamagePhaseToCombatEndPhase() {
 game.currentPhase = combatDamagePhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(combatEndPhase.getClass(), nextPhase.getClass());
 }

 @Test
 public void testNextPhase_CombatEndPhaseToMain2Phase() {
 game.currentPhase = combatEndPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(main2Phase.getClass(), nextPhase.getClass());
 }

 @Test
 public void testNextPhase_Main2PhaseToEndOfTurnPhase() {
 game.currentPhase = main2Phase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(endOfTurnPhase.getClass(), nextPhase.getClass());
 }

 @Test
 public void testNextPhase_EndOfTurnPhaseToCleanupPhase() {
 game.currentPhase = endOfTurnPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(cleanupPhase.getClass(), nextPhase.getClass());
 }

 @Test
 public void testNextPhase_CleanupPhaseToPlayerChangePhase() {
 game.currentPhase = cleanupPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(playerChangePhase.getClass(), nextPhase.getClass());
 }

 @Test
 public void testNextPhase_PlayerChangePhaseToUntapPhase() {
 game.currentPhase = playerChangePhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(untapPhase.getClass(), nextPhase.getClass());
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_InvalidCurrentPhase() {
 game.currentPhase = new Object(); // An invalid phase
 Phase nextPhase = Phase.nextPhase();
 fail("Should throw an IllegalStateException");
 }
}