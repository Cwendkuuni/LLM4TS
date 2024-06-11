import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Phase currentPhase = null;
 private Game game = new Game();

 @Test
 public void testDoPhase() {
  currentPhase = new UntapPhase();
  currentPhase.doPhase();
  assertNotNull(currentPhase.doPhase());
  // Add more assertions based on the expected behavior of each phase
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhaseWhenCurrentPhaseIsNull() {
  nextPhase();
  fail("Current phase should not be null");
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsUntapPhase() {
  currentPhase = new UntapPhase();
  nextPhase();
  assertEquals(UpkeepPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsUpkeepPhase() {
  currentPhase = new UpkeepPhase();
  nextPhase();
  assertEquals(DrawPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsDrawPhase() {
  currentPhase = new DrawPhase();
  nextPhase();
  assertEquals(Main1Phase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsMain1Phase() {
  currentPhase = new Main1Phase();
  nextPhase();
  assertEquals(CombatBeginningPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsCombatBeginningPhase() {
  currentPhase = new CombatBeginningPhase();
  nextPhase();
  assertEquals(DeclareAttackersPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsDeclareAttackersPhase() {
  currentPhase = new DeclareAttackersPhase();
  nextPhase();
  assertEquals(DeclareBlockersPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsDeclareBlockersPhase() {
  currentPhase = new DeclareBlockersPhase();
  nextPhase();
  assertEquals(CombatDamagePhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsCombatDamagePhase() {
  currentPhase = new CombatDamagePhase();
  nextPhase();
  assertEquals(CombatEndPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsCombatEndPhase() {
  currentPhase = new CombatEndPhase();
  nextPhase();
  assertEquals(Main2Phase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsMain2Phase() {
  currentPhase = new Main2Phase();
  nextPhase();
  assertEquals(EndOfTurnPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsEndOfTurnPhase() {
  currentPhase = new EndOfTurnPhase();
  nextPhase();
  assertEquals(CleanupPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsCleanupPhase() {
  currentPhase = new CleanupPhase();
  nextPhase();
  assertEquals(PlayerChangePhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsPlayerChangePhase() {
  nextPhase();
  assertEquals(UntapPhase.class, Game.currentPhase.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextPhaseWhenCurrentPhaseIsNull() {
  nextPhase();
  fail("Current phase should not be null");
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhaseWhenCurrentPhaseIsNotOneOfThePhases() {
  currentPhase = new Object(); // Replace with a valid but unsupported phase
  nextPhase();
  fail("Unexpected phase reached");
 }
}