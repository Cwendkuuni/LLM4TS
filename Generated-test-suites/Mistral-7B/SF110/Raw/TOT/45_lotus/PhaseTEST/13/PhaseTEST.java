package lotus.core.phases;

import org.junit.Assert;
import org.junit.Test;
import lotus.core.*;
import static org.mockito.Mockito.*;

public class PhaseTest {

 private static Game game = mock(Game.class);
 private static Phase untapPhase = mock(UntapPhase.class);
 private static Phase upkeepPhase = mock(UpkeepPhase.class);
 private static Phase drawPhase = mock(DrawPhase.class);
 private static Phase main1Phase = mock(Main1Phase.class);
 private static Phase combatBeginningPhase = mock(CombatBeginningPhase.class);
 private static Phase declareAttackersPhase = mock(DeclareAttackersPhase.class);
 private static Phase declareBlockersPhase = mock(DeclareBlockersPhase.class);
 private static Phase combatDamagePhase = mock(CombatDamagePhase.class);
 private static Phase combatEndPhase = mock(CombatEndPhase.class);
 private static Phase main2Phase = mock(Main2Phase.class);
 private static Phase endOfTurnPhase = mock(EndOfTurnPhase.class);
 private static Phase cleanupPhase = mock(CleanupPhase.class);
 private static Phase playerChangePhase = mock(PlayerChangePhase.class);

 @Test
 public void testNextPhase_UntapPhase_UpkeepPhase() {
 // Given
 when(game.currentPhase).thenReturn(untapPhase);

 // When
 Phase.nextPhase(game);

 // Then
 verify(game).currentPhase = upkeepPhase;
 }

 @Test
 public void testNextPhase_UpkeepPhase_DrawPhase() {
 // Given
 when(game.currentPhase).thenReturn(upkeepPhase);

 // When
 Phase.nextPhase(game);

 // Then
 verify(game).currentPhase = drawPhase;
 }

 @Test
 public void testNextPhase_DrawPhase_Main1Phase() {
 // Given
 when(game.currentPhase).thenReturn(drawPhase);

 // When
 Phase.nextPhase(game);

 // Then
 verify(game).currentPhase = main1Phase;
 }

 @Test
 public void testNextPhase_Main1Phase_CombatBeginningPhase() {
 // Given
 when(game.currentPhase).thenReturn(main1Phase);

 // When
 Phase.nextPhase(game);

 // Then
 verify(game).currentPhase = combatBeginningPhase;
 }

 @Test
 public void testNextPhase_CombatBeginningPhase_DeclareAttackersPhase() {
 // Given
 when(game.currentPhase).thenReturn(combatBeginningPhase);

 // When
 Phase.nextPhase(game);

 // Then
 verify(game).currentPhase = declareAttackersPhase;
 }

 @Test
 public void testNextPhase_DeclareAttackersPhase_DeclareBlockersPhase() {
 // Given
 when(game.currentPhase).thenReturn(declareAttackersPhase);

 // When
 Phase.nextPhase(game);

 // Then
 verify(game).currentPhase = declareBlockersPhase;
 }

 @Test
 public void testNextPhase_DeclareBlockersPhase_CombatDamagePhase() {
 // Given
 when(game.currentPhase).thenReturn(declareBlockersPhase);

 // When
 Phase.nextPhase(game);

 // Then
 verify(game).currentPhase = combatDamagePhase;
 }

 @Test
 public void testNextPhase_CombatDamagePhase_CombatEndPhase() {
 // Given
 when(game.currentPhase).thenReturn(combatDamagePhase);

 // When
 Phase.nextPhase(game);

 // Then
 verify(game).currentPhase = combatEndPhase;
 }

 @Test
 public void testNextPhase_CombatEndPhase_Main2Phase() {
 // Given
 when(game.currentPhase).thenReturn(combatEndPhase);

 // When
 Phase.nextPhase(game);

 // Then
 verify(game).currentPhase = main2Phase;
 }

 @Test
 public void testNextPhase_Main2Phase_EndOfTurnPhase() {
 // Given
 when(game.currentPhase).thenReturn(main2Phase);

 // When
 Phase.nextPhase(game);

 // Then
 verify(game).currentPhase = endOfTurnPhase;
 }

 @Test
 public void testNextPhase_EndOfTurnPhase_CleanupPhase() {
 // Given
 when(game.currentPhase).thenReturn(endOfTurnPhase);

 // When
 Phase.nextPhase(game);
 // Then this should be the last phase, no next phase
 Assert.assertNull(game.currentPhase);
 }

 @Test
 public void testNextPhase_CleanupPhase_PlayerChangePhase() {
 // Given
 when(game.currentPhase).thenReturn(cleanupPhase);

 // When
 Phase.nextPhase(game);

 // Then
 verify(game).playingPlayer = 2;
 // PlayerChangePhase should be the first phase after CleanupPhase
 verify(game.currentPhase).equals(playerChangePhase);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_InvalidPhase() {
 // Given
 when(game.currentPhase).thenReturn(new Object());

 // When
 Phase.nextPhase(game);

 // Then
 verify(game).currentPhase = null; // this should throw an exception before reaching this point
 }
}