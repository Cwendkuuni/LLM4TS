import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.phases.Phase;
import lotus.core.effect.ChangePhase;

public class PhaseTest {

 private Phase currentPhaseMock;

 @Before
 public void setUp() {
 currentPhaseMock = mock(Phase.class);
 Game.currentPhase = currentPhaseMock;
 }

 @Test
 public void testDoPhase_UntapPhase() {
 Given.given(currentPhaseMock.getClass()).willReturn(UntapPhase.class);
 When.when(currentPhaseMock.doPhase()).thenCallRealMethod();
 Then.then(Game.currentPhase).isInstanceOf(UpkeepPhase.class);
 }

 @Test(expected = IllegalStateException.class)
 public void testDoPhase_UnknownPhase() {
 Given.given(currentPhaseMock.getClass()).willReturn(new Object());
 When.thenThrow(new RuntimeException()).given(currentPhaseMock).doPhase();
 Then.thenThrows(IllegalStateException.class).given(Phase.class).nextPhase();
 }

 @Test
 public void testNextPhase_UntapPhaseToUpkeepPhase() {
 Given.given(currentPhaseMock.getClass()).willReturn(UntapPhase.class);
 When.when(Phase.class.nextPhase()).thenCallRealMethod();
 Then.then(Game.currentPhase).isInstanceOf(UpkeepPhase.class);
 }

 @Test
 public void testNextPhase_UpkeepPhaseToDrawPhase() {
 Given.given(currentPhaseMock.getClass()).willReturn(UpkeepPhase.class);
 When.when(currentPhaseMock.doPhase()).thenCallRealMethod();
 Given.given(Game.currentPhase.getClass()).willReturn(UpkeepPhase.class);
 When.when(Phase.class.nextPhase()).thenCallRealMethod();
 Then.then(Game.currentPhase).isInstanceOf(DrawPhase.class);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_DrawPhaseToMain1Phase_IncorrectOrder() {
 Given.given(currentPhaseMock.getClass()).willReturn(DrawPhase.class);
 Given.given(Game.currentPhase.getClass()).willReturn(DrawPhase.class);
 When.thenThrow(new RuntimeException()).given(currentPhaseMock).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenCallRealMethod().given(Phase.class).nextPhase();
 Then.thenThrows(IllegalStateException.class).given(Phase.class).nextPhase();
 }

 @Test
 public void testNextPhase_Main1PhaseToCombatBeginningPhase() {
 Given.given(currentPhaseMock.getClass()).willReturn(Main1Phase.class);
 When.when(Phase.class.nextPhase()).thenCallRealMethod();
 Then.then(Game.currentPhase).isInstanceOf(CombatBeginningPhase.class);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_CombatBeginningPhaseToCombatDamagePhase_IncorrectOrder() {
 Given.given(currentPhaseMock.getClass()).willReturn(CombatBeginningPhase.class);
 Given.given(Game.currentPhase.getClass()).willReturn(CombatBeginningPhase.class);
 When.thenThrow(new RuntimeException()).given(currentPhaseMock).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenCallRealMethod().given(Phase.class).nextPhase();
 Then.thenThrows(IllegalStateException.class).given(Phase.class).nextPhase();
 }

 @Test
 public void testNextPhase_CombatDamagePhaseToCombatEndPhase() {
 Given.given(currentPhaseMock.getClass()).willReturn(CombatDamagePhase.class);
 When.when(Phase.class.nextPhase()).thenCallRealMethod();
 Then.then(Game.currentPhase).isInstanceOf(CombatEndPhase.class);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_CombatEndPhaseToMain2Phase_IncorrectOrder() {
 Given.given(currentPhaseMock.getClass()).willReturn(CombatEndPhase.class);
 Given.given(Game.currentPhase.getClass()).willReturn(CombatEndPhase.class);
 When.thenThrow(new RuntimeException()).given(currentPhaseMock).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenCallRealMethod().given(Phase.class).nextPhase();
 Then.thenThrows(IllegalStateException.class).given(Phase.class).nextPhase();
 }

 @Test
 public void testNextPhase_Main2PhaseToEndOfTurnPhase() {
 Given.given(currentPhaseMock.getClass()).willReturn(Main2Phase.class);
 When.when(Phase.class.nextPhase()).thenCallRealMethod();
 Then.then(Game.currentPhase).isInstanceOf(EndOfTurnPhase.class);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_EndOfTurnPhaseToCleanupPhase_IncorrectOrder() {
 Given.given(currentPhaseMock.getClass()).willReturn(EndOfTurnPhase.class);
 Given.given(Game.currentPhase.getClass()).willReturn(EndOfTurnPhase.class);
 When.thenThrow(new RuntimeException()).given(currentPhaseMock).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenThrow(new RuntimeException()).given(Game.currentPhase).doPhase();
 When.thenCallRealMethod().given(Phase.class).nextPhase();
 Then.thenThrows(IllegalStateException.class).given(Phase.class).nextPhase();
 }

 @Test
 public void testNextPhase_EndOfTurnPhaseToPlayerChangePhase() {
 Given.given(currentPhaseMock.getClass()).willReturn(EndOfTurnPhase.class);
 When.when(Phase.class.nextPhase()).thenCallRealMethod();
 Then.then(Game.playingPlayer).isInstanceOf(Integer.class);
 Then.then(Game.playingPlayer).isEqualTo(1);
 }

 @Test
 public void testNextPhase_PlayerChangePhaseToUntapPhase() {
 Given.given(currentPhaseMock.getClass()).willReturn(PlayerChangePhase.class);
 When.when(Phase.class.nextPhase()).thenCallRealMethod();
 Then.then(Game.currentPhase).isInstanceOf(UntapPhase.class);
 }

}