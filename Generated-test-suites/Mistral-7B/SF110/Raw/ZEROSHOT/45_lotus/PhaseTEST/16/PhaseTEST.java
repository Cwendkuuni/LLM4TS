package lotus.core.phases;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import lotus.core.Game;
import lotus.core.effect.ChangePhase;

import static org.junit.Assert.assertEquals;

public class PhaseTest {

 private Game game;
 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase;

 @Before
 public void setUp() {
 game = Mockito.spy(new Game());
 untapPhase = Mockito.spy(new UntapPhase());
 upkeepPhase = Mockito.spy(new UpkeepPhase());
 drawPhase = Mockito.spy(new DrawPhase());
 main1Phase = Mockito.spy(new Main1Phase());
 combatBeginningPhase = Mockito.spy(new CombatBeginningPhase());
 declareAttackersPhase = Mockito.spy(new DeclareAttackersPhase());
 declareBlockersPhase = Mockito.spy(new DeclareBlockersPhase());
 combatDamagePhase = Mockito.spy(new CombatDamagePhase());
 combatEndPhase = Mockito.spy(new CombatEndPhase());
 main2Phase = Mockito.spy(new Main2Phase());
 endOfTurnPhase = Mockito.spy(new EndOfTurnPhase());
 cleanupPhase = Mockito.spy(new CleanupPhase());
 playerChangePhase = Mockito.spy(new PlayerChangePhase());
 }

 @Test
 public void testNextPhase_UntapPhaseToUpkeepPhase() {
 // Given
 Game.currentPhase = untapPhase;

 // When
 Phase.nextPhase();

 // Then
 assertEquals(upkeepPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_UpkeepPhaseToDrawPhase() {
 // Given
 Game.currentPhase = upkeepPhase;

 // When
 Phase.nextPhase();

 // Then
 assertEquals(drawPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_DrawPhaseToMain1Phase() {
 // Given
 Game.currentPhase = drawPhase;

 // When
 Phase.nextPhase();

 // Then
 assertEquals(main1Phase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_Main1PhaseToCombatBeginningPhase() {
 // Given
 Game.currentPhase = main1Phase;

 // When
 Phase.nextPhase();

 // Then
 assertEquals(combatBeginningPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CombatBeginningPhaseToDeclareAttackersPhase() {
 // Given
 Game.currentPhase = combatBeginningPhase;

 // When
 Phase.nextPhase();

 // Then
 assertEquals(declareAttackersPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_DeclareAttackersPhaseToDeclareBlockersPhase() {
 // Given
 Game.currentPhase = declareAttackersPhase;

 // When
 Phase.nextPhase();

 // Then
 assertEquals(declareBlockersPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_DeclareBlockersPhaseToCombatDamagePhase() {
 // Given
 Game.currentPhase = declareBlockersPhase;

 // When
 Phase.nextPhase();

 // Then
 assertEquals(combatDamagePhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CombatDamagePhaseToCombatEndPhase() {
 // Given
 Game.currentPhase = combatDamagePhase;

 // When
 Phase.nextPhase();

 // Then
 assertEquals(combatEndPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CombatEndPhaseToMain2Phase() {
 // Given
 Game.currentPhase = combatEndPhase;

 // When
 Phase.nextPhase();

 // Then
 assertEquals(main2Phase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_Main2PhaseToEndOfTurnPhase() {
 // Given
 Game.currentPhase = main2Phase;

 // When
 Phase.nextPhase();

 // Then
 assertEquals(endOfTurnPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_EndOfTurnPhaseToCleanupPhase() {
 // Given
 Game.currentPhase = endOfTurnPhase;

 // When
 Phase.nextPhase();

 // Then
 assertEquals(cleanupPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CleanupPhaseToPlayerChangePhase() {
 // Given
 Game.currentPhase = cleanupPhase;

 // When
 Phase.nextPhase();

 // Then
 assertEquals(playerChangePhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_PlayerChangePhaseToUntapPhase() {
 // Given
 Game.currentPhase = playerChangePhase;

 // When
 Phase.nextPhase();

 // Then
 assertEquals(untapPhase, Game.currentPhase);
 }
}