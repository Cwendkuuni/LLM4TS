import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import lotus.core.phases.*;

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
 Phase expectedPhase = upkeepPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_UpkeepPhaseToDrawPhase() {
 game.currentPhase = upkeepPhase;
 Phase expectedPhase = drawPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_DrawPhaseToMain1Phase() {
 game.currentPhase = drawPhase;
 Phase expectedPhase = main1Phase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_Main1PhaseToCombatBeginningPhase() {
 game.currentPhase = main1Phase;
 Phase expectedPhase = combatBeginningPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_CombatBeginningPhaseToDeclareAttackersPhase() {
 game.currentPhase = combatBeginningPhase;
 Phase expectedPhase = declareAttackersPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_DeclareAttackersPhaseToDeclareBlockersPhase() {
 game.currentPhase = declareAttackersPhase;
 Phase expectedPhase = declareBlockersPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_DeclareBlockersPhaseToCombatDamagePhase() {
 game.currentPhase = declareBlockersPhase;
 Phase expectedPhase = combatDamagePhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_CombatDamagePhaseToCombatEndPhase() {
 game.currentPhase = combatDamagePhase;
 Phase expectedPhase = combatEndPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_CombatEndPhaseToMain2Phase() {
 game.currentPhase = combatEndPhase;
 Phase expectedPhase = main2Phase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_Main2PhaseToEndOfTurnPhase() {
 game.currentPhase = main2Phase;
 Phase expectedPhase = endOfTurnPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_EndOfTurnPhaseToCleanupPhase() {
 game.currentPhase = endOfTurnPhase;
 Phase expectedPhase = cleanupPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_CleanupPhaseToPlayerChangePhase() {
 game.currentPhase = cleanupPhase;
 Phase expectedPhase = playerChangePhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_PlayerChangePhaseToUntapPhase() {
 game.currentPhase = playerChangePhase;
 Phase expectedPhase = untapPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_InvalidPhase() {
 Phase invalidPhase = new InvalidPhase();
 assertThrows(IllegalArgumentException.class, () -> Phase.nextPhase(invalidPhase));
 }

 @Test
 public void testChangePhase_SamePhase() {
 Phase phase = upkeepPhase;
 Phase expectedPhase = upkeepPhase;
 Phase actualPhase = Phase.changePhase(phase);
 assertEquals(expectedPhase, actualPhase);
 }
}