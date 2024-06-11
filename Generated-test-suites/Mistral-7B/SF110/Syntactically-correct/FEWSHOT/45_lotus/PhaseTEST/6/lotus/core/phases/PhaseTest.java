package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    @Test
    public void testNextPhase_UntapPhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        game.setCurrentPhase(untapPhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(UpkeepPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        game.setCurrentPhase(upkeepPhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(DrawPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_DrawPhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        game.setCurrentPhase(upkeepPhase);
        Phase.nextPhase();
        game.setCurrentPhase(drawPhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(Main1Phase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_Main1Phase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        game.setCurrentPhase(upkeepPhase);
        Phase.nextPhase();
        game.setCurrentPhase(drawPhase);
        Phase.nextPhase();
        game.setCurrentPhase(main1Phase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(CombatBeginningPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        game.setCurrentPhase(upkeepPhase);
        Phase.nextPhase();
        game.setCurrentPhase(drawPhase);
        Phase.nextPhase();
        game.setCurrentPhase(main1Phase);
        Phase.nextPhase();
        game.setCurrentPhase(combatBeginningPhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(DeclareAttackersPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        game.setCurrentPhase(upkeepPhase);
        Phase.nextPhase();
        game.setCurrentPhase(drawPhase);
        Phase.nextPhase();
        game.setCurrentPhase(main1Phase);
        Phase.nextPhase();
        game.setCurrentPhase(combatBeginningPhase);
        Phase.nextPhase();
        game.setCurrentPhase(declareAttackersPhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(DeclareBlockersPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        game.setCurrentPhase(upkeepPhase);
        Phase.nextPhase();
        game.setCurrentPhase(drawPhase);
        Phase.nextPhase();
        game.setCurrentPhase(main1Phase);
        Phase.nextPhase();
        game.setCurrentPhase(combatBeginningPhase);
        Phase.nextPhase();
        game.setCurrentPhase(declareAttackersPhase);
        Phase.nextPhase();
        game.setCurrentPhase(declareBlockersPhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(CombatDamagePhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_CombatDamagePhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase combatDamagePhase = new CombatDamagePhase();
        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        game.setCurrentPhase(upkeepPhase);
        Phase.nextPhase();
        game.setCurrentPhase(drawPhase);
        Phase.nextPhase();
        game.setCurrentPhase(main1Phase);
        Phase.nextPhase();
        game.setCurrentPhase(combatBeginningPhase);
        Phase.nextPhase();
        game.setCurrentPhase(declareAttackersPhase);
        Phase.nextPhase();
        game.setCurrentPhase(declareBlockersPhase);
        Phase.nextPhase();
        game.setCurrentPhase(combatDamagePhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(CombatEndPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_CombatEndPhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase combatDamagePhase = new CombatDamagePhase();
        Phase combatEndPhase = new CombatEndPhase();
        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        game.setCurrentPhase(upkeepPhase);
        Phase.nextPhase();
        game.setCurrentPhase(drawPhase);
        Phase.nextPhase();
        game.setCurrentPhase(main1Phase);
        Phase.nextPhase();
        game.setCurrentPhase(combatBeginningPhase);
        Phase.nextPhase();
        game.setCurrentPhase(declareAttackersPhase);
        Phase.nextPhase();
        game.setCurrentPhase(declareBlockersPhase);
        Phase.nextPhase();
        game.setCurrentPhase(combatDamagePhase);
        Phase.nextPhase();
        game.setCurrentPhase(combatEndPhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(Main2Phase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_Main2Phase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase combatDamagePhase = new CombatDamagePhase();
        Phase combatEndPhase = new CombatEndPhase();
        Phase main2Phase = new Main2Phase();
        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        game.setCurrentPhase(upkeepPhase);
        Phase.nextPhase();
        game.setCurrentPhase(drawPhase);
        Phase.nextPhase();
        game.setCurrentPhase(main1Phase);
        Phase.nextPhase();
        game.setCurrentPhase(combatBeginningPhase);
        Phase.nextPhase();
        game.setCurrentPhase(declareAttackersPhase);
        Phase.nextPhase();
        game.setCurrentPhase(declareBlockersPhase);
        Phase.nextPhase();
        game.setCurrentPhase(combatDamagePhase);
        Phase.nextPhase();
        game.setCurrentPhase(combatEndPhase);
        Phase.nextPhase();
        game.setCurrentPhase(main2Phase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(EndOfTurnPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_EndOfTurnPhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase combatDamagePhase = new CombatDamagePhase();
        Phase combatEndPhase = new CombatEndPhase();
        Phase main2Phase = new Main2Phase();
        Phase endOfTurnPhase = new EndOfTurnPhase();
        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        game.setCurrentPhase(upkeepPhase);
        Phase.nextPhase();
        game.setCurrentPhase(drawPhase);
        Phase.nextPhase();
        game.setCurrentPhase(main1Phase);
        Phase.nextPhase();
        game.setCurrentPhase(combatBeginningPhase);
        Phase.nextPhase();
        game.setCurrentPhase(declareAttackersPhase);
        Phase.nextPhase();
        game.setCurrentPhase(declareBlockersPhase);
        Phase.nextPhase();
        game.setCurrentPhase(combatDamagePhase);
        Phase.nextPhase();
        game.setCurrentPhase(combatEndPhase);
        Phase.nextPhase();
        game.setCurrentPhase(main2Phase);
        Phase.nextPhase();
        game.setCurrentPhase(endOfTurnPhase);

        Phase nextPhase = Phase.nextPhase();
        assertNull(nextPhase);
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase combatDamagePhase = new CombatDamagePhase();
        Phase combatEndPhase = new CombatEndPhase();
        Phase main2Phase = new Main2Phase();
        Phase endOfTurnPhase = new EndOfTurnPhase();
        Phase playerChangePhase = new PlayerChangePhase();
        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        game.setCurrentPhase(upkeepPhase);
        Phase.nextPhase();
        game.setCurrentPhase(drawPhase);
        Phase.nextPhase();
        game.setCurrentPhase(main1Phase);
        Phase.nextPhase();
        game.setCurrentPhase(combatBeginningPhase);
        Phase.nextPhase();
        game.setCurrentPhase(declareAttackersPhase);
        Phase.nextPhase();
        game.setCurrentPhase(declareBlockersPhase);
        Phase.nextPhase();
        game.setCurrentPhase(combatDamagePhase);
        Phase.nextPhase();
        game.setCurrentPhase(combatEndPhase);
        Phase.nextPhase();
        game.setCurrentPhase(main2Phase);
        Phase.nextPhase();
        game.setCurrentPhase(endOfTurnPhase);
        Phase.nextPhase();
        game.setCurrentPhase(playerChangePhase);
        game.setPlayingPlayer(1);
        Phase.nextPhase();
        assertEquals(UntapPhase.class, Phase.getCurrentPhase().getClass());
    }
}