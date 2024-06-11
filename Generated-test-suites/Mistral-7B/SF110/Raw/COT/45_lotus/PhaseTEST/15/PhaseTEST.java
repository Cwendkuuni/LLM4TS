import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Phase currentPhase;
 private Game game;

 @Before
 public void setUp() {
 Game.currentPhase = null;
 game = new Game();
 currentPhase = new UntapPhase();
 }

 @Test
 public void testDoPhase_UntapPhase() {
 // Test for basic functionality of UntapPhase method
 currentPhase.doPhase();
 assertTrue(Game.currentPhase instanceof UntapPhase);
 }

 @Test(expected = IllegalStateException.class)
 public void testDoPhase_InvalidPhase() {
 // Test for exception handling when calling doPhase() on an invalid Phase object
 currentPhase = new Object(); // Replace with an invalid Phase object
 currentPhase.doPhase();
 }

 @Test
 public void testNextPhase_UntapPhase_UpkeepPhase() {
 // Test for basic functionality of nextPhase() method when current phase is UntapPhase
 Phase expectedNextPhase = new UpkeepPhase();
 PhaseTestUtil.changePhase(currentPhase, expectedNextPhase);
 assertEquals(expectedNextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_UpkeepPhase_DrawPhase() {
 // Test for basic functionality of nextPhase() method when current phase is UpkeepPhase
 Phase expectedNextPhase = new DrawPhase();
 PhaseTestUtil.changePhase(currentPhase, expectedNextPhase);
 assertEquals(expectedNextPhase, Game.currentPhase);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_InvalidPhaseOrder() {
 // Test for exception handling when attempting to change phase to an invalid phase in the wrong order
 Phase expectedNextPhase = new Main1Phase();
 PhaseTestUtil.changePhase(currentPhase, expectedNextPhase);
 PhaseTestUtil.changePhase(currentPhase, new UpkeepPhase()); // Change phase back to UpkeepPhase for test setup
}

 @Test
 public void testNextPhase_EndOfTurnPhase_PlayerChangePhase() {
 // Test for basic functionality of nextPhase() method when current phase is EndOfTurnPhase
 Phase expectedNextPhase = new PlayerChangePhase();
 PhaseTestUtil.changePhase(currentPhase, expectedNextPhase);
 assertEquals(expectedNextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CleanupPhase_UntapPhase() {
 // Test for basic functionality of nextPhase() method when current phase is CleanupPhase
 Phase expectedNextPhase = new UntapPhase();
 PhaseTestUtil.changePhase(currentPhase, expectedNextPhase);
 assertEquals(expectedNextPhase, Game.currentPhase);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChangePhase_NullPhase() {
 // Test for exception handling when attempting to change phase to null
 PhaseTestUtil.changePhase(currentPhase, null);
 }

 @Test
 public void testChangePhase_InvalidPhase() {
 // Test for exception handling when attempting to change phase to an invalid Phase object
 Phase invalidPhase = new Object(); // Replace with an invalid Phase object
 PhaseTestUtil.changePhase(currentPhase, invalidPhase);
 assertNotSame(invalidPhase, Game.currentPhase);
 }

 private static void PhaseTestUtil_changePhase(Phase currentPhase, Phase expectedNextPhase) {
 Phase.changePhase(currentPhase);
 while (Game.currentPhase != expectedNextPhase && Game.currentPhase != null) {
 PhaseTestUtil_changePhase(Game.currentPhase, expectedNextPhase);
 }
 }

}