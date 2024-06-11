package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import lotus.core.Game;
import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    @Test
    public void testNextPhase_UntapPhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();

        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();

        assertEquals(upkeepPhase.getClass(), game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();

        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        assertEquals(upkeepPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(drawPhase.getClass(), game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_DrawPhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();

        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        assertEquals(upkeepPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(drawPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(main1Phase.getClass(), game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_Main1Phase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();

        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        assertEquals(upkeepPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(drawPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(main1Phase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatBeginningPhase.getClass(), game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();

        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        assertEquals(upkeepPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(drawPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(main1Phase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatBeginningPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(declareAttackersPhase.getClass(), game.getCurrentPhase().getClass());
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
        Phase declareBlockersPhase = new DeclareBlockersPhase();

        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        assertEquals(upkeepPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(drawPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(main1Phase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatBeginningPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(declareAttackersPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(declareBlockersPhase.getClass(), game.getCurrentPhase().getClass());
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
        Phase combatDamagePhase = new CombatDamagePhase();

        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        assertEquals(upkeepPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(drawPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(main1Phase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatBeginningPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(declareAttackersPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(declareBlockersPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatDamagePhase.getClass(), game.getCurrentPhase().getClass());
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
        Phase combatEndPhase = new CombatEndPhase();

        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        assertEquals(upkeepPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(drawPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(main1Phase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatBeginningPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(declareAttackersPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(declareBlockersPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatDamagePhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatEndPhase.getClass(), game.getCurrentPhase().getClass());
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
        Phase main2Phase = new Main2Phase();

        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        assertEquals(upkeepPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(drawPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(main1Phase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatBeginningPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(declareAttackersPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(declareBlockersPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatDamagePhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatEndPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(main2Phase.getClass(), game.getCurrentPhase().getClass());
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
        Phase endOfTurnPhase = new EndOfTurnPhase();

        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        assertEquals(upkeepPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(drawPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(main1Phase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatBeginningPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(declareAttackersPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(declareBlockersPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatDamagePhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatEndPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(main2Phase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(endOfTurnPhase.getClass(), game.getCurrentPhase().getClass());
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
        Phase playerChangePhase = new PlayerChangePhase();

        game.setCurrentPhase(untapPhase);
        Phase.nextPhase();
        assertEquals(upkeepPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(drawPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(main1Phase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatBeginningPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(declareAttackersPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(declareBlockersPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatDamagePhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatEndPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(main2Phase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(endOfTurnPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(playerChangePhase.getClass(), game.getCurrentPhase().getClass());
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
        assertEquals(upkeepPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(drawPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(main1Phase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatBeginningPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(declareAttackersPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(declareBlockersPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatDamagePhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(combatEndPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(main2Phase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(endOfTurnPhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(playerChangePhase.getClass(), game.getCurrentPhase().getClass());

        Phase.nextPhase();
        assertEquals(untapPhase.getClass(), game.getCurrentPhase().getClass());
    }
}