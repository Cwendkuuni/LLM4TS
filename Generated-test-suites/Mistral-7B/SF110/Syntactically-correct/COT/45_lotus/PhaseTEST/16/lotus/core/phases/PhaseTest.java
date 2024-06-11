import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Phase currentPhase;
 private static Game game = new Game();

 @Before
 public void setUp() {
 currentPhase = new UntapPhase();
 Game.currentPhase = currentPhase;
 }

 @Test
 public void testDoPhase_UntapPhase() {
 // Test for UntapPhase method doPhase()
 currentPhase.doPhase();
 // Add assertions here to test the expected behavior of the UntapPhase method
 }

 @Test
 public void testDoPhase_UpkeepPhase() {
 // Test for UpkeepPhase method doPhase()
 currentPhase = new UpkeepPhase();
 Game.currentPhase = currentPhase;
 currentPhase.doPhase();
 // Add assertions here to test the expected behavior of the UpkeepPhase method
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_InvalidPhase() {
 // Test for nextPhase() method when Game.currentPhase is not an instance of any defined Phase
 Game.currentPhase = null;
 Phase.nextPhase();
 }

 @Test
 public void testNextPhase_ValidTransitions() {
 // Test for nextPhase() method with valid transitions between phases
 for (Phase phase : Phase.values()) {
 if (phase == currentPhase) {
 Game.currentPhase = Phase.values()[Phase.values().length - 1]; // Last phase
 }
 Phase expectedPhase = Phase.values()[Phase.values().length - 1 - Phase.values().length + phase.ordinal()];
 Phase actualPhase = Phase.nextPhase();
 assertEquals(expectedPhase, actualPhase);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_InvalidTransition() {
 // Test for nextPhase() method with an invalid transition between phases
 Game.currentPhase = new UntapPhase();
 Phase.nextPhase();
 }

 @Test
 public void testNextPhase_PlayerChange() {
 // Test for nextPhase() method when Game.playingPlayer changes
 Game.currentPhase = new PlayerChangePhase();
 Game.playingPlayer = 3; // An invalid value, should cause a change in Game.playingPlayer
 Game.currentPhase = new UntapPhase();
 Phase actualPhase = Phase.nextPhase();
 Phase expectedPhase = new UpkeepPhase();
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testChangePhase_InvalidPhase() {
 // Test for changePhase() method with an invalid Phase argument
 try {
 Phase.changePhase(null);
 fail("changePhase() method should not accept null as an argument.");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testChangePhase_ValidPhase() {
 // Test for changePhase() method with a valid Phase argument
 Phase validPhase = new UpkeepPhase();
 Phase.changePhase(validPhase);
 assertEquals(validPhase, Game.currentPhase);
 }
}