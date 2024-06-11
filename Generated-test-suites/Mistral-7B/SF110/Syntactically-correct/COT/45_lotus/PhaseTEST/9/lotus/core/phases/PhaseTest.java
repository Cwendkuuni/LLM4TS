import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.phases.Phase;
import lotus.core.effect.ChangePhase;

public class PhaseTest {

 private Phase currentPhaseMock;

 @Before
 public void setUp() {
 Game.currentPhase = this.currentPhaseMock = mock(Phase.class);
 }

 @Test
 public void testDoPhase_UntapPhase() {
 Phase untapPhase = mock(UntapPhase.class);
 when(untapPhase.doPhase()).thenReturn(true);

 Phase phaseUnderTest = new UntapPhase();
 phaseUnderTest.doPhase();

 verify(untapPhase).doPhase();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDoPhase_AbstractClass() {
 Phase phaseUnderTest = new Phase();
 phaseUnderTest.doPhase();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextPhase_NullCurrentPhase() {
 Phase.nextPhase(null);
 }

 @Test
 public void testNextPhase_SamePhase() {
 Phase phaseUnderTest = new UntapPhase();
 Phase currentPhase = phaseUnderTest;

 assertFalse("Should not change the phase when the current phase is the same", Phase.nextPhase(currentPhase));

 assertEquals("Current phase should not be changed", currentPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_EndOfTurnPhase() {
 Phase endOfTurnPhase = mock(EndOfTurnPhase.class);
 when(endOfTurnPhase.doPhase()).thenReturn(true);

 Phase currentPhase = new UntapPhase();
 ChangePhase changePhaseEffect = new ChangePhase(endOfTurnPhase);
 doReturn(changePhaseEffect).when(currentPhase).resolve();

 Phase phaseUnderTest = new CleanupPhase();
 phaseUnderTest.doPhase();

 verify(endOfTurnPhase).doPhase();
 verify(changePhaseEffect).resolve();

 assertEquals("Current phase should be CleanupPhase after nextPhase", phaseUnderTest, Game.currentPhase);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_InvalidCurrentPhase() {
 Phase currentPhase = mock(Phase.class);
 when(currentPhase.getClass()).thenReturn(DeclareAttackersPhase.class);

 try {
 Phase phaseUnderTest = new DeclareBlockersPhase();
 phaseUnderTest.doPhase();
 Phase.nextPhase(currentPhase);
 fail("Should throw IllegalStateException when nextPhase is an invalid phase transition");
 } catch (IllegalStateException e) {
 // Expected exception
 }
 }
}