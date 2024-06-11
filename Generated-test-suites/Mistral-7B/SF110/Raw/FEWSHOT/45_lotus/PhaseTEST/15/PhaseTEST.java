package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import lotus.core.*;
import lotus.core.effect.*;
import org.junit.Before;
import org.junit.Test;

public class PhaseTest {

    private Game game;
    private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase;

    @Before
    public void setup() {
        game = mock(Game.class);
        untapPhase = mock(UntapPhase.class);
        upkeepPhase = mock(UpkeepPhase.class);
        drawPhase = mock(DrawPhase.class);
        main1Phase = mock(Main1Phase.class);
        combatBeginningPhase = mock(CombatBeginningPhase.class);
        declareAttackersPhase = mock(DeclareAttackersPhase.class);
        declareBlockersPhase = mock(DeclareBlockersPhase.class);
        combatDamagePhase = mock(CombatDamagePhase.class);
        combatEndPhase = mock(CombatEndPhase.class);
        main2Phase = mock(Main2Phase.class);
        endOfTurnPhase = mock(EndOfTurnPhase.class);
        cleanupPhase = mock(CleanupPhase.class);
        playerChangePhase = mock(PlayerChangePhase.class);

        when(game.currentPhase).thenReturn(untapPhase);
    }

    @Test
    public void testNextPhase_UntapPhase_UpkeepPhase() {
        when(untapPhase.doPhase()).thenReturnVoid();
        when(upkeepPhase.doPhase()).thenReturnVoid();

        Phase.nextPhase(game);

        assertEquals(upkeepPhase, game.currentPhase);
    }

    @Test
    public void testNextPhase_UpkeepPhase_DrawPhase() {
        when(upkeepPhase.doPhase()).thenReturnVoid();
        when(drawPhase.doPhase()).thenReturnVoid();

        Phase.nextPhase(game);

        assertEquals(drawPhase, game.currentPhase);
    }

    @Test
    public void testNextPhase_DrawPhase_Main1Phase() {
        when(drawPhase.doPhase()).thenReturnVoid();
        when(main1Phase.doPhase()).thenReturnVoid();

        Phase.nextPhase(game);

        assertEquals(main1Phase, game.currentPhase);
    }

    @Test
    public void testNextPhase_Main1Phase_CombatBeginningPhase() {
        when(main1Phase.doPhase()).thenReturnVoid();
        when(combatBeginningPhase.doPhase()).thenReturnVoid();

        Phase.nextPhase(game);

        assertEquals(combatBeginningPhase, game.currentPhase);
    }

    @Test
    public void testNextPhase_CombatBeginningPhase_DeclareAttackersPhase() {
        when(combatBeginningPhase.doPhase()).thenReturnVoid();
        when(declareAttackersPhase.doPhase()).thenReturnVoid();

        Phase.nextPhase(game);

        assertEquals(declareAttackersPhase, game.currentPhase);
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase_DeclareBlockersPhase() {
        when(declareAttackersPhase.doPhase()).thenReturnVoid();
        when(declareBlockersPhase.doPhase()).thenReturnVoid();

        Phase.nextPhase(game);

        assertEquals(declareBlockersPhase, game.currentPhase);
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase_CombatDamagePhase() {
        when(declareBlockersPhase.doPhase()).thenReturnVoid();
        when(combatDamagePhase.doPhase()).thenReturnVoid();

        Phase.nextPhase(game);

        assertEquals(combatDamagePhase, game.currentPhase);
    }

    @Test
    public void testNextPhase_CombatDamagePhase_CombatEndPhase() {
        when(combatDamagePhase.doPhase()).thenReturnVoid();
        when(combatEndPhase.doPhase()).thenReturnVoid();

        Phase.nextPhase(game);

        assertEquals(combatEndPhase, game.currentPhase);
    }

    @Test
    public void testNextPhase_CombatEndPhase_Main2Phase() {
        when(combatEndPhase.doPhase()).thenReturnVoid();
        when(main2Phase.doPhase()).thenReturnVoid();

        Phase.nextPhase(game);

        assertEquals(main2Phase, game.currentPhase);
    }

    @Test
    public void testNextPhase_Main2Phase_EndOfTurnPhase() {
        when(main2Phase.doPhase()).thenReturnVoid();
        when(endOfTurnPhase.doPhase()).thenReturnVoid();

        Phase.nextPhase(game);

        assertEquals(endOfTurnPhase, game.currentPhase);
    }

    @Test
    public void testNextPhase_EndOfTurnPhase_CleanupPhase() {
        when(endOfTurnPhase.doPhase()).thenReturnVoid();
        when(cleanupPhase.doPhase()).thenReturnVoid();

        Phase.nextPhase(game);

        assertEquals(cleanupPhase, game.currentPhase);
    }

    @Test
    public void testNextPhase_CleanupPhase_PlayerChangePhase() {
        when(cleanupPhase.doPhase()).thenReturnVoid();
        when(playerChangePhase.doPhase()).thenReturnVoid();

        Phase.nextPhase(game);

        assertEquals(playerChangePhase, game.currentPhase);
    }

    @Test
    public void testNextPhase_PlayerChangePhase_UntapPhase() {
        when(playerChangePhase.doPhase()).thenReturnVoid();
        when(untapPhase.doPhase()).thenReturnVoid();

        Phase.nextPhase(game);

        assertEquals(untapPhase, game.currentPhase);
    }
}