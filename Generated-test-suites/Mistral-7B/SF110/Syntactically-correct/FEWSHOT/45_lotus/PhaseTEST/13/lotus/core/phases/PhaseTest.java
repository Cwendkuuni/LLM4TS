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
        when(untapPhase.getName()).thenReturn("UntapPhase");

        when(game.currentPhase).thenReturn(upkeepPhase);
        when(upkeepPhase.getName()).thenReturn("UpkeepPhase");

        when(game.currentPhase).thenReturn(drawPhase);
        when(drawPhase.getName()).thenReturn("DrawPhase");

        when(game.currentPhase).thenReturn(main1Phase);
        when(main1Phase.getName()).thenReturn("Main1Phase");

        when(game.currentPhase).thenReturn(combatBeginningPhase);
        when(combatBeginningPhase.getName()).thenReturn("CombatBeginningPhase");

        when(game.currentPhase).thenReturn(declareAttackersPhase);
        when(declareAttackersPhase.getName()).thenReturn("DeclareAttackersPhase");

        when(game.currentPhase).thenReturn(declareBlockersPhase);
        when(declareBlockersPhase.getName()).thenReturn("DeclareBlockersPhase");

        when(game.currentPhase).thenReturn(combatDamagePhase);
        when(combatDamagePhase.getName()).thenReturn("CombatDamagePhase");

        when(game.currentPhase).thenReturn(combatEndPhase);
        when(combatEndPhase.getName()).thenReturn("CombatEndPhase");

        when(game.currentPhase).thenReturn(main2Phase);
        when(main2Phase.getName()).thenReturn("Main2Phase");

        when(game.currentPhase).thenReturn(endOfTurnPhase);
        when(endOfTurnPhase.getName()).thenReturn("EndOfTurnPhase");

        when(game.currentPhase).thenReturn(cleanupPhase);
        when(cleanupPhase.getName()).thenReturn("CleanupPhase");

        when(game.currentPhase).thenReturn(playerChangePhase);
        when(playerChangePhase.getName()).thenReturn("PlayerChangePhase");
    }

    @Test
    public void testNextPhase_UntapPhase() {
        Phase.nextPhase(game);
        assertEquals("UpkeepPhase", game.currentPhase.getName());
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        Phase.nextPhase(game);
        assertEquals("DrawPhase", game.currentPhase.getName());
    }

    @Test
    public void testNextPhase_DrawPhase() {
        Phase.nextPhase(game);
        assertEquals("Main1Phase", game.currentPhase.getName());
    }

    @Test
    public void testNextPhase_Main1Phase() {
        Phase.nextPhase(game);
        assertEquals("CombatBeginningPhase", game.currentPhase.getName());
    }

    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Phase.nextPhase(game);
        assertEquals("DeclareAttackersPhase", game.currentPhase.getName());
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        Phase.nextPhase(game);
        assertEquals("DeclareBlockersPhase", game.currentPhase.getName());
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        Phase.nextPhase(game);
        assertEquals("CombatDamagePhase", game.currentPhase.getName());
    }

    @Test
    public void testNextPhase_CombatDamagePhase() {
        Phase.nextPhase(game);
        assertEquals("CombatEndPhase", game.currentPhase.getName());
    }

    @Test
    public void testNextPhase_CombatEndPhase() {
        Phase.nextPhase(game);
        assertEquals("Main2Phase", game.currentPhase.getName());
    }

    @Test
    public void testNextPhase_Main2Phase() {
        Phase.nextPhase(game);
        assertEquals("EndOfTurnPhase", game.currentPhase.getName());
    }

    @Test
    public void testNextPhase_EndOfTurnPhase() {
        Phase.nextPhase(game);
        assertEquals("CleanupPhase", game.currentPhase.getName());
    }

    @Test
    public void testNextPhase_CleanupPhase() {
        Phase.nextPhase(game);
        assertEquals("PlayerChangePhase", game.currentPhase.getName());
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
        Phase.nextPhase(game);
        assertEquals("UntapPhase", game.currentPhase.getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextPhase_UnknownPhase() {
        when(game.currentPhase).thenReturn(mock(Object.class));
        Phase.nextPhase(game);
    }
}