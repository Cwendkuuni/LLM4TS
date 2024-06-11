import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase;

 @Before
 public void setUp() {
 Game.initGame();
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
 public void testDoPhase_AbstractMethod() {
 assertNull(untapPhase.doPhase());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChangePhase_InvalidPhase() {
 try {
 Phase invalidPhase = new Phase() {
 @Override
 public void doPhase() {
 }
 };
 Phase previousPhase = new UntapPhase();
 Phase nextPhase = new UntapPhase();
 Phase.changePhase(previousPhase);
 Phase.changePhase(invalidPhase);
 Phase.changePhase(nextPhase);
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testNextPhase_UntapPhaseToUpkeepPhase() {
 Phase previousPhase = new UntapPhase();
 Phase nextPhase = new UpkeepPhase();
 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_UpkeepPhaseToDrawPhase() {
 Phase previousPhase = new UpkeepPhase();
 Phase nextPhase = new DrawPhase();
 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_DrawPhaseToMain1Phase() {
 Phase previousPhase = new DrawPhase();
 Phase nextPhase = new Main1Phase();
 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_Main1PhaseToCombatBeginningPhase() {
 Phase previousPhase = new Main1Phase();
 Phase nextPhase = new CombatBeginningPhase();
 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CombatBeginningPhaseToDeclareAttackersPhase() {
 Phase previousPhase = new CombatBeginningPhase();
 Phase nextPhase = new DeclareAttackersPhase();
 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_DeclareAttackersPhaseToDeclareBlockersPhase() {
 Phase previousPhase = new DeclareAttackersPhase();
 Phase nextPhase = new DeclareBlockersPhase();
 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_DeclareBlockersPhaseToCombatDamagePhase() {
 Phase previousPhase = new DeclareBlockersPhase();
 Phase nextPhase = new CombatDamagePhase();
 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CombatDamagePhaseToCombatEndPhase() {
 Phase previousPhase = new CombatDamagePhase();
 Phase nextPhase = new CombatEndPhase();
 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CombatEndPhaseToMain2Phase() {
 Phase previousPhase = new CombatEndPhase();
 Phase nextPhase = new Main2Phase();
 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_Main2PhaseToEndOfTurnPhase() {
 Phase previousPhase = new Main2Phase();
 Phase nextPhase = new EndOfTurnPhase();
 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_EndOfTurnPhaseToCleanupPhase() {
 Phase previousPhase = new EndOfTurnPhase();
 Phase nextPhase = new CleanupPhase();
 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CleanupPhaseToPlayerChangePhase() {
 Phase previousPhase = new CleanupPhase();
 Phase nextPhase = new PlayerChangePhase();
 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_PlayerChangePhaseToUntapPhase() {
 Phase previousPhase = new PlayerChangePhase();
 Phase nextPhase = new UntapPhase();
 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_InvalidPhaseOrder() {
 try {
 Phase previousPhase = new DrawPhase();
 Phase nextPhase = new UntapPhase();
 Phase.nextPhase();
 Phase.nextPhase();
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testChangePhase_SamePhase() {
 Phase currentPhase = new UntapPhase();
 Phase newPhase = new UntapPhase();
 Phase.changePhase(currentPhase);
 assertEquals(currentPhase, Game.currentPhase);
 Phase.changePhase(newPhase);
 assertEquals(newPhase, Game.currentPhase);
 }
}