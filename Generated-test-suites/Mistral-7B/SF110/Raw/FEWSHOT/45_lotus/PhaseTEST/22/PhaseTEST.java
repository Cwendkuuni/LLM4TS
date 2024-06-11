package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import lotus.core.*;
import lotus.core.effect.*;
import org.junit.Test;

public class PhaseTest {

    private Game game = mock(Game.class);
    private Phase untapPhase = mock(UntapPhase.class);
    private Phase upkeepPhase = mock(UpkeepPhase.class);
    private Phase drawPhase = mock(DrawPhase.class);
    private Phase main1Phase = mock(Main1Phase.class);
    private Phase combatBeginningPhase = mock(CombatBeginningPhase.class);
    private Phase declareAttackersPhase = mock(DeclareAttackersPhase.class);
    private Phase declareBlockersPhase = mock(DeclareBlockersPhase.class);
    private Phase combatDamagePhase = mock(CombatDamagePhase.class);
    private Phase combatEndPhase = mock(CombatEndPhase.class);
    private Phase main2Phase = mock(Main2Phase.class);
    private Phase endOfTurnPhase = mock(EndOfTurnPhase.class);
    private Phase cleanupPhase = mock(CleanupPhase.class);
    private Phase playerChangePhase = mock(PlayerChangePhase.class);

    @Test
    public void testNextPhase_UntapPhaseToUpkeepPhase() {
        when(game.currentPhase).thenReturn(untapPhase);
        when(upkeepPhase.doPhase()).thenReturn(null);

        Phase.nextPhase(game);

        assertEquals(upkeepPhase, Game.currentPhase);
    }

    @Test
    public void testNextPhase_UpkeepPhaseToDrawPhase() {
        when(game.currentPhase).thenReturn(upkeepPhase);
        when(drawPhase.doPhase()).thenReturn(null);

        Phase.nextPhase(game);

        assertEquals(drawPhase, Game.currentPhase);
    }

    @Test
    public void testNextPhase_DrawPhaseToMain1Phase() {
        when(game.currentPhase).thenReturn(drawPhase);
        when(main1Phase.doPhase()).thenReturn(null);

        Phase.nextPhase(game);

        assertEquals(main1Phase, Game.currentPhase);
    }

    @Test
    public void testNextPhase_Main1PhaseToCombatBeginningPhase() {
        when(game.currentPhase).thenReturn(main1Phase);
        when(combatBeginningPhase.doPhase()).thenReturn(null);

        Phase.nextPhase(game);

        assertEquals(combatBeginningPhase, Game.currentPhase);
    }

    @Test
    public void testNextPhase_CombatBeginningPhaseToDeclareAttackersPhase() {
        when(game.currentPhase).thenReturn(combatBeginningPhase);
        when(declareAttackersPhase.doPhase()).thenReturn(null);

        Phase.nextPhase(game);

        assertEquals(declareAttackersPhase, Game.currentPhase);
    }

    @Test
    public void testNextPhase_DeclareAttackersPhaseToDeclareBlockersPhase() {
        when(game.currentPhase).thenReturn(declareAttackersPhase);
        when(declareBlockersPhase.doPhase()).thenReturn(null);

        Phase.nextPhase(game);

        assertEquals(declareBlockersPhase, Game.currentPhase);
    }

    @Test
    public void testNextPhase_DeclareBlockersPhaseToCombatDamagePhase() {
        when(game.currentPhase).thenReturn(declareBlockersPhase);
        when(combatDamagePhase.doPhase()).thenReturn(null);

        Phase.nextPhase(game);

        assertEquals(combatDamagePhase, Game.currentPhase);
    }

    @Test
    public void testNextPhase_CombatDamagePhaseToCombatEndPhase() {
        when(game.currentPhase).thenReturn(combatDamagePhase);
        when(combatEndPhase.doPhase()).thenReturn(null);

        Phase.nextPhase(game);

        assertEquals(combatEndPhase, Game.currentPhase);
    }

    @Test
    public void testNextPhase_CombatEndPhaseToMain2Phase() {
        when(game.currentPhase).thenReturn(combatEndPhase);
        when(main2Phase.doPhase()).thenReturn(null);

        Phase.nextPhase(game);

        assertEquals(main2Phase, Game.currentPhase);
    }

    @Test
    public void testNextPhase_Main2PhaseToEndOfTurnPhase() {
        when(game.currentPhase).thenReturn(main2Phase);
        when(endOfTurnPhase.doPhase()).thenReturn(null);

        Phase.nextPhase(game);

        assertEquals(endOfTurnPhase, Game.currentPhase);
    }

    @Test
    public void testNextPhase_EndOfTurnPhaseToCleanupPhase() {
        when(game.currentPhase).thenReturn(endOfTurnPhase);
        when(cleanupPhase.doPhase()).thenReturn(null);

        Phase.nextPhase(game);

        assertEquals(cleanupPhase, Game.currentPhase);
    }

    @Test
    public void testNextPhase_CleanupPhaseToPlayerChangePhase() {
        when(game.currentPhase).thenReturn(cleanupPhase);
        when(playerChangePhase.doPhase()).thenReturn(null);

        Phase.nextPhase(game);

        assertEquals(playerChangePhase, Game.currentPhase);
    }

    @Test
    public void testNextPhase_PlayerChangePhaseToUntapPhase() {
        when(game.playingPlayer).thenReturn(1);
        when(playerChangePhase.doPhase()).thenReturn(null);
        when(untapPhase.doPhase()).thenReturn(null);

        when(game.currentPhase).thenReturn(playerChangePhase);

        Phase.nextPhase(game);

        assertEquals(untapPhase, Game.currentPhase);

        when(game.playingPlayer).thenReturn(2);
        Phase.nextPhase(game);

        assertEquals(untapPhase, Game.currentPhase);
    }
}