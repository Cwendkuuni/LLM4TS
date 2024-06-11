import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.phases.Phase;
import lotus.core.effect.ChangePhase;

public class PhaseTest {

 private Phase currentPhase;
 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase;

 @Before
 public void setUp() {
 Game.currentPhase = null;
 currentPhase = null;
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
 public void testDoPhase() {
 // Cannot test abstract method directly
 // Arrange
 currentPhase = untapPhase;
 // Act
 currentPhase.doPhase();
 // Assert
 assertNotNull(Game.currentPhase);
 assertTrue(Game.currentPhase instanceof UntapPhase);
 }

 @Test(expected = IllegalStateException.class)
 public void testChangePhaseNullCurrentPhase() {
 // Arrange
 Game.currentPhase = null;
 // Act & Assert
 Phase.changePhase(upkeepPhase);
 }

 @Test
 public void testChangePhaseSamePhase() {
 // Arrange
 currentPhase = upkeepPhase;
 // Act
 Phase.changePhase(upkeepPhase);
 // Assert
 assertSame(upkeepPhase, Game.currentPhase);
 }

 @Test
 public void testChangePhaseDifferentPhases() {
 // Arrange
 currentPhase = upkeepPhase;
 // Act
 Phase.changePhase(declareBlockersPhase);
 // Assert
 assertNotSame(upkeepPhase, Game.currentPhase);
 assertSame(declareBlockersPhase, Game.currentPhase);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChangePhaseInvalidPhaseOrder() {
 // Arrange
 currentPhase = upkeepPhase;
 // Act & Assert
 Phase.changePhase(combatBeginningPhase);
 }

 @Test
 public void testNextPhaseUntapPhase() {
 // Arrange
 currentPhase = untapPhase;
 // Act
 Phase.nextPhase();
 // Assert
 assertSame(upkeepPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhaseUpkeepPhase() {
 // Arrange
 currentPhase = upkeepPhase;
 // Act
 Phase.nextPhase();
 // Assert
 assertSame(drawPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhaseAfterEndOfTurnPhase() {
 // Arrange
 Game.currentPhase = endOfTurnPhase;
 // Act
 Phase.nextPhase();
 // Assert
 assertSame(playerChangePhase, Game.currentPhase);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhaseNoCurrentPhase() {
 // Arrange
 Game.currentPhase = null;
 // Act & Assert
 Phase.nextPhase();
 }
}