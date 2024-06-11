import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.phases.Phase;
import lotus.core.effect.ChangePhase;

public class PhaseTest {

 private static Phase currentPhase;

 @Test
 public void testDoPhase() {
  Phase phase = null; // A test for null pointer exception
  assertNull(phase.doPhase());

  phase = new UntapPhase();
  assertNotNull(phase.doPhase());
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidPhaseTransition() {
  currentPhase = new UntapPhase();
  Phase nextPhase = null;
  Phase expectedException = new RuntimeException("Invalid phase transition");

  try {
   PhaseTest.nextPhase(nextPhase);
  } catch (Exception e) {
   assertEquals(expectedException.getMessage(), e.getMessage());
  }
 }

 @Test
 public void testNextPhase_UntapPhase_UpkeepPhase() {
  currentPhase = new UntapPhase();
  Phase nextPhase = new UpkeepPhase();

  PhaseTest.nextPhase(nextPhase);

  Phase actualPhase = Game.currentPhase;
  Phase expectedPhase = nextPhase;

  assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_UpkeepPhase_DrawPhase() {
  currentPhase = new UpkeepPhase();
  Phase nextPhase = new DrawPhase();

  PhaseTest.nextPhase(nextPhase);

  Phase actualPhase = Game.currentPhase;
  Phase expectedPhase = nextPhase;

  assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_DrawPhase_Main1Phase() {
  currentPhase = new DrawPhase();
  Phase nextPhase = new Main1Phase();

  PhaseTest.nextPhase(nextPhase);

  Phase actualPhase = Game.currentPhase;
  Phase expectedPhase = nextPhase;

  assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_Main1Phase_CombatBeginningPhase() {
  currentPhase = new Main1Phase();
  Phase nextPhase = new CombatBeginningPhase();

  PhaseTest.nextPhase(nextPhase);

  Phase actualPhase = Game.currentPhase;
  Phase expectedPhase = nextPhase;

  assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_CombatBeginningPhase_DeclareAttackersPhase() {
  currentPhase = new CombatBeginningPhase();
  Phase nextPhase = new DeclareAttackersPhase();

  PhaseTest.nextPhase(nextPhase);

  Phase actualPhase = Game.currentPhase;
  Phase expectedPhase = nextPhase;

  assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_DeclareAttackersPhase_DeclareBlockersPhase() {
  currentPhase = new DeclareAttackersPhase();
  Phase nextPhase = new DeclareBlockersPhase();

  PhaseTest.nextPhase(nextPhase);

  Phase actualPhase = Game.currentPhase;
  Phase expectedPhase = nextPhase;

  assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_DeclareBlockersPhase_CombatDamagePhase() {
  currentPhase = new DeclareBlockersPhase();
  Phase nextPhase = new CombatDamagePhase();

  PhaseTest.nextPhase(nextPhase);

  Phase actualPhase = Game.currentPhase;
  Phase expectedPhase = nextPhase;

  assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_CombatDamagePhase_CombatEndPhase() {
  currentPhase = new CombatDamagePhase();
  Phase nextPhase = new CombatEndPhase();

  PhaseTest.nextPhase(nextPhase);

  Phase actualPhase = Game.currentPhase;
  Phase expectedPhase = nextPhase;

  assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_CombatEndPhase_Main2Phase() {
  currentPhase = new CombatEndPhase();
  Phase nextPhase = new Main2Phase();

  PhaseTest.nextPhase(nextPhase);

  Phase actualPhase = Game.currentPhase;
  Phase expectedPhase = nextPhase;

  assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_Main2Phase_EndOfTurnPhase() {
  currentPhase = new Main2Phase();
  Phase nextPhase = new EndOfTurnPhase();

  PhaseTest.nextPhase(nextPhase);

  Phase actualPhase = Game.currentPhase;
  Phase expectedPhase = nextPhase;

  assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_EndOfTurnPhase_CleanupPhase() {
  currentPhase = new EndOfTurnPhase();
  Phase nextPhase = new CleanupPhase();

  PhaseTest.nextPhase(nextPhase);

  Phase actualPhase = Game.currentPhase;
  Phase expectedPhase = nextPhase;

  assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_CleanupPhase_PlayerChangePhase() {
  currentPhase = new CleanupPhase();
  Phase nextPhase = new PlayerChangePhase();

  PhaseTest.nextPhase(nextPhase);

  Phase actualPhase = Game.currentPhase;
  Phase expectedPhase = nextPhase;

  assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_PlayerChangePhase_UntapPhase() {
  currentPhase = new PlayerChangePhase();
  Phase nextPhase = new UntapPhase();

  PhaseTest.nextPhase(nextPhase);

  Phase actualPhase = Game.currentPhase;
  Phase expectedPhase = nextPhase;

  assertEquals(expectedPhase, actualPhase);
 }

 private static void nextPhase(Phase nextPhase) {
  PhaseTest.changePhase(currentPhase);
  Game.currentPhase = nextPhase;
  PhaseTest.changePhase(nextPhase);
 }

 private static void changePhase(Phase p) {
  Game.currentPhase = p;
  new ChangePhase(p).resolve();
 }
}