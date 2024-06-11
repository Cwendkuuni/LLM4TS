import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.phases.Phase;
import lotus.core.effect.ChangePhase;

public class PhaseTest {

 private Phase currentPhaseMock;

 @Before
 public void setup() {
 Game.currentPhase = this.currentPhaseMock = mock(Phase.class);
 }

 @Test
 public void testDoPhase() {
 // This method is abstract, cannot be tested directly
 // You should create unit tests for the concrete implementation classes instead
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhaseWhenCurrentPhaseIsNull() {
 Game.currentPhase = null;
 Phase.nextPhase();
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsUntapPhase() {
 given(currentPhaseMock.getClass()).willReturn(UntapPhase.class);
 Phase.nextPhase();
 assertEquals(UpkeepPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsUpkeepPhase() {
 given(currentPhaseMock.getClass()).willReturn(UpkeepPhase.class);
 Phase.nextPhase();
 assertEquals(DrawPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsDrawPhase() {
 given(currentPhaseMock.getClass()).willReturn(DrawPhase.class);
 Phase.nextPhase();
 assertEquals(Main1Phase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsMain1Phase() {
 given(currentPhaseMock.getClass()).willReturn(Main1Phase.class);
 Phase.nextPhase();
 assertEquals(CombatBeginningPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsCombatBeginningPhase() {
 given(currentPhaseMock.getClass()).willReturn(CombatBeginningPhase.class);
 Phase.nextPhase();
 assertEquals(DeclareAttackersPhase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsDeclareAttackersPhase() {
 given(currentPhaseMock.getClass()).willReturn(DeclareAttackersPhase.class);
 Phase.nextPhase();
 assertEquals(DeclareBlockersPhase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsDeclareBlockersPhase() {
 given(currentPhaseMock.getClass()).willReturn(DeclareBlockersPhase.class);
 Phase.nextPhase();
 assertEquals(CombatDamagePhase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsCombatDamagePhase() {
 given(currentPhaseMock.getClass()).willReturn(CombatDamagePhase.class);
 Phase.nextPhase();
 assertEquals(CombatEndPhase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsCombatEndPhase() {
 given(currentPhaseMock.getClass()).willReturn(CombatEndPhase.class);
 Phase.nextPhase();
 assertEquals(Main2Phase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsMain2Phase() {
 given(currentPhaseMock.getClass()).willReturn(Main2Phase.class);
 Phase.nextPhase();
 assertEquals(EndOfTurnPhase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsEndOfTurnPhase() {
 given(currentPhaseMock.getClass()).willReturn(EndOfTurnPhase.class);
 Phase.nextPhase();
 assertEquals(CleanupPhase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsCleanupPhase() {
 given(currentPhaseMock.getClass()).willReturn(CleanupPhase.class);
 Phase.nextPhase();
 assertEquals(PlayerChangePhase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsPlayerChangePhase() {
 given(currentPhaseMock.getClass()).willReturn(PlayerChangePhase.class);
 Phase.nextPhase();
 assertNotSame(Game.playingPlayer, 0);
 assertEquals(UntapPhase.class, Game.currentPhase.getClass());
 }
}