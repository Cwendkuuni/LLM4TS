package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    @SuppressWarnings("unchecked")
    private static <T extends Phase> T getPhaseInstance(Class<? extends Phase> clazz) {
        try {
            return (T) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testNextPhase_UntapPhase() {
        final Phase untapPhase = getPhaseInstance(UntapPhase.class);
        final Phase upkeepPhase = getPhaseInstance(UpkeepPhase.class);

        Game game = new Game();
        game.setCurrentPhase(untapPhase);

        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(UpkeepPhase.class, Game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        final Phase untapPhase = getPhaseInstance(UntapPhase.class);
        final Phase upkeepPhase = getPhaseInstance(UpkeepPhase.class);
        final Phase drawPhase = getPhaseInstance(DrawPhase.class);

        Game game = new Game();
        game.setCurrentPhase(upkeepPhase);

        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(DrawPhase.class, Game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_DrawPhase() {
        final Phase untapPhase = getPhaseInstance(UntapPhase.class);
        final Phase upkeepPhase = getPhaseInstance(UpkeepPhase.class);
        final Phase drawPhase = getPhaseInstance(DrawPhase.class);
        final Phase main1Phase = getPhaseInstance(Main1Phase.class);

        Game game = new Game();
        game.setCurrentPhase(drawPhase);

        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(Main1Phase.class, Game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_Main1Phase() {
        final Phase untapPhase = getPhaseInstance(UntapPhase.class);
        final Phase upkeepPhase = getPhaseInstance(UpkeepPhase.class);
        final Phase drawPhase = getPhaseInstance(DrawPhase.class);
        final Phase main1Phase = getPhaseInstance(Main1Phase.class);
        final Phase combatBeginningPhase = getPhaseInstance(CombatBeginningPhase.class);

        Game game = new Game();
        game.setCurrentPhase(main1Phase);

        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(CombatBeginningPhase.class, Game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_CombatBeginningPhase() {
        final Phase untapPhase = getPhaseInstance(UntapPhase.class);
        final Phase upkeepPhase = getPhaseInstance(UpkeepPhase.class);
        final Phase drawPhase = getPhaseInstance(DrawPhase.class);
        final Phase main1Phase = getPhaseInstance(Main1Phase.class);
        final Phase combatBeginningPhase = getPhaseInstance(CombatBeginningPhase.class);
        final Phase declareAttackersPhase = getPhaseInstance(DeclareAttackersPhase.class);

        Game game = new Game();
        game.setCurrentPhase(combatBeginningPhase);

        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(DeclareAttackersPhase.class, Game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        final Phase untapPhase = getPhaseInstance(UntapPhase.class);
        final Phase upkeepPhase = getPhaseInstance(UpkeepPhase.class);
        final Phase drawPhase = getPhaseInstance(DrawPhase.class);
        final Phase main1Phase = getPhaseInstance(Main1Phase.class);
        final Phase combatBeginningPhase = getPhaseInstance(CombatBeginningPhase.class);
        final Phase declareAttackersPhase = getPhaseInstance(DeclareAttackersPhase.class);
        final Phase declareBlockersPhase = getPhaseInstance(DeclareBlockersPhase.class);

        Game game = new Game();
        game.setCurrentPhase(declareAttackersPhase);

        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(DeclareBlockersPhase.class, Game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        final Phase untapPhase = getPhaseInstance(UntapPhase.class);
        final Phase upkeepPhase = getPhaseInstance(UpkeepPhase.class);
        final Phase drawPhase = getPhaseInstance(DrawPhase.class);
        final Phase main1Phase = getPhaseInstance(Main1Phase.class);
        final Phase combatBeginningPhase = getPhaseInstance(CombatBeginningPhase.class);
        final Phase declareAttackersPhase = getPhaseInstance(DeclareAttackersPhase.class);
        final Phase declareBlockersPhase = getPhaseInstance(DeclareBlockersPhase.class);
        final Phase combatDamagePhase = getPhaseInstance(CombatDamagePhase.class);

        Game game = new Game();
        game.setCurrentPhase(declareBlockersPhase);

        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(CombatDamagePhase.class, Game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_CombatDamagePhase() {
        final Phase untapPhase = getPhaseInstance(UntapPhase.class);
        final Phase upkeepPhase = getPhaseInstance(UpkeepPhase.class);
        final Phase drawPhase = getPhaseInstance(DrawPhase.class);
        final Phase main1Phase = getPhaseInstance(Main1Phase.class);
        final Phase combatBeginningPhase = getPhaseInstance(CombatBeginningPhase.class);
        final Phase declareAttackersPhase = getPhaseInstance(DeclareAttackersPhase.class);
        final Phase declareBlockersPhase = getPhaseInstance(DeclareBlockersPhase.class);
        final Phase combatDamagePhase = getPhaseInstance(CombatDamagePhase.class);
        final Phase combatEndPhase = getPhaseInstance(CombatEndPhase.class);

        Game game = new Game();
        game.setCurrentPhase(combatDamagePhase);

        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(CombatEndPhase.class, Game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_CombatEndPhase() {
        final Phase untapPhase = getPhaseInstance(UntapPhase.class);
        final Phase upkeepPhase = getPhaseInstance(UpkeepPhase.class);
        final Phase drawPhase = getPhaseInstance(DrawPhase.class);
        final Phase main1Phase = getPhaseInstance(Main1Phase.class);
        final Phase combatBeginningPhase = getPhaseInstance(CombatBeginningPhase.class);
        final Phase declareAttackersPhase = getPhaseInstance(DeclareAttackersPhase.class);
        final Phase declareBlockersPhase = getPhaseInstance(DeclareBlockersPhase.class);
        final Phase combatDamagePhase = getPhaseInstance(CombatDamagePhase.class);
        final Phase combatEndPhase = getPhaseInstance(CombatEndPhase.class);
        final Phase main2Phase = getPhaseInstance(Main2Phase.class);

        Game game = new Game();
        game.setCurrentPhase(combatEndPhase);

        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(Main2Phase.class, Game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_Main2Phase() {
        final Phase untapPhase = getPhaseInstance(UntapPhase.class);
        final Phase upkeepPhase = getPhaseInstance(UpkeepPhase.class);
        final Phase drawPhase = getPhaseInstance(DrawPhase.class);
        final Phase main1Phase = getPhaseInstance(Main1Phase.class);
        final Phase combatBeginningPhase = getPhaseInstance(CombatBeginningPhase.class);
        final Phase declareAttackersPhase = getPhaseInstance(DeclareAttackersPhase.class);
        final Phase declareBlockersPhase = getPhaseInstance(DeclareBlockersPhase.class);
        final Phase combatDamagePhase = getPhaseInstance(CombatDamagePhase.class);
        final Phase combatEndPhase = getPhaseInstance(CombatEndPhase.class);
        final Phase main2Phase = getPhaseInstance(Main2Phase.class);
        final Phase endOfTurnPhase = getPhaseInstance(EndOfTurnPhase.class);

        Game game = new Game();
        game.setCurrentPhase(main2Phase);

        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(EndOfTurnPhase.class, Game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_EndOfTurnPhase() {
        final Phase untapPhase = getPhaseInstance(UntapPhase.class);
        final Phase upkeepPhase = getPhaseInstance(UpkeepPhase.class);
        final Phase drawPhase = getPhaseInstance(DrawPhase.class);
        final Phase main1Phase = getPhaseInstance(Main1Phase.class);
        final Phase combatBeginningPhase = getPhaseInstance(CombatBeginningPhase.class);
        final Phase declareAttackersPhase = getPhaseInstance(DeclareAttackersPhase.class);
        final Phase declareBlockersPhase = getPhaseInstance(DeclareBlockersPhase.class);
        final Phase combatDamagePhase = getPhaseInstance(CombatDamagePhase.class);
        final Phase combatEndPhase = getPhaseInstance(CombatEndPhase.class);
        final Phase main2Phase = getPhaseInstance(Main2Phase.class);
        final Phase endOfTurnPhase = getPhaseInstance(EndOfTurnPhase.class);
        final Phase playerChangePhase = getPhaseInstance(PlayerChangePhase.class);

        Game game = new Game();
        game.setCurrentPhase(endOfTurnPhase);

        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(PlayerChangePhase.class, Game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
        final Phase untapPhase = getPhaseInstance(UntapPhase.class);
        final Phase upkeepPhase = getPhaseInstance(UpkeepPhase.class);
        final Phase drawPhase = getPhaseInstance(DrawPhase.class);
        final Phase main1Phase = getPhaseInstance(Main1Phase.class);
        final Phase combatBeginningPhase = getPhaseInstance(CombatBeginningPhase.class);
        final Phase declareAttackersPhase = getPhaseInstance(DeclareAttackersPhase.class);
        final Phase declareBlockersPhase = getPhaseInstance(DeclareBlockersPhase.class);
        final Phase combatDamagePhase = getPhaseInstance(CombatDamagePhase.class);
        final Phase combatEndPhase = getPhaseInstance(CombatEndPhase.class);
        final Phase main2Phase = getPhaseInstance(Main2Phase.class);
        final Phase endOfTurnPhase = getPhaseInstance(EndOfTurnPhase.class);
        final Phase playerChangePhase = getPhaseInstance(PlayerChangePhase.class);

        Game game = new Game();
        game.setCurrentPhase(playerChangePhase);

        Phase.nextPhase();
        assertNotNull(Game.currentPhase);
        assertEquals(UntapPhase.class, Game.currentPhase.getClass());
    }
}