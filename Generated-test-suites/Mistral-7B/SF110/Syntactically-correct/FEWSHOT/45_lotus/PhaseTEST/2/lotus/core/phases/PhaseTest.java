package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import lotus.core.Game;
import lotus.core.effect.ChangePhase;

public class PhaseTest {

    private Game game;

    @Before
    public void setUp() {
        game = new Game();
    }

    @Test
    public void testNextPhaseUntap() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();

        Phase.changePhase(untapPhase);
        assertEquals(untapPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(upkeepPhase, Game.currentPhase);
    }

    @Test
    public void testNextPhaseUpkeep() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();

        Phase.changePhase(untapPhase);
        assertEquals(untapPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(upkeepPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(drawPhase, Game.currentPhase);
    }

    @Test
    public void testNextPhaseDraw() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();

        Phase.changePhase(untapPhase);
        assertEquals(untapPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(upkeepPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(drawPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(main1Phase, Game.currentPhase);
    }

    @Test
    public void testNextPhaseMain1() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();

        Phase.changePhase(untapPhase);
        assertEquals(untapPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(upkeepPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(drawPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(main1Phase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatBeginningPhase, Game.currentPhase);
    }

    @Test
    public void testNextPhaseCombatBeginning() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();

        Phase.changePhase(untapPhase);
        assertEquals(untapPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(upkeepPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(drawPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(main1Phase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatBeginningPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(declareAttackersPhase, Game.currentPhase);
    }

    @Test
    public void testNextPhaseDeclareAttackers() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase declareBlockersPhase = new DeclareBlockersPhase();

        Phase.changePhase(untapPhase);
        assertEquals(untapPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(upkeepPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(drawPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(main1Phase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatBeginningPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(declareAttackersPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(declareBlockersPhase, Game.currentPhase);
    }

    @Test
    public void testNextPhaseDeclareBlockers() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase combatDamagePhase = new CombatDamagePhase();

        Phase.changePhase(untapPhase);
        assertEquals(untapPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(upkeepPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(drawPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(main1Phase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatBeginningPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(declareAttackersPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(declareBlockersPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatDamagePhase, Game.currentPhase);
    }

    @Test
    public void testNextPhaseCombatDamage() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase combatDamagePhase = new CombatDamagePhase();
        Phase combatEndPhase = new CombatEndPhase();

        Phase.changePhase(untapPhase);
        assertEquals(untapPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(upkeepPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(drawPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(main1Phase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatBeginningPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(declareAttackersPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(declareBlockersPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatDamagePhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatEndPhase, Game.currentPhase);
    }

    @Test
    public void testNextPhaseCombatEnd() {
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

        Phase.changePhase(untapPhase);
        assertEquals(untapPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(upkeepPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(drawPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(main1Phase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatBeginningPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(declareAttackersPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(declareBlockersPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatDamagePhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatEndPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(main2Phase, Game.currentPhase);
    }

    @Test
    public void testNextPhaseMain2() {
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

        Phase.changePhase(untapPhase);
        assertEquals(untapPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(upkeepPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(drawPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(main1Phase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatBeginningPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(declareAttackersPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(declareBlockersPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatDamagePhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatEndPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(main2Phase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(endOfTurnPhase, Game.currentPhase);
    }

    @Test
    public void testNextPhaseEndOfTurn() {
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

        Phase.changePhase(untapPhase);
        assertEquals(untapPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(upkeepPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(drawPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(main1Phase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatBeginningPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(declareAttackersPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(declareBlockersPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatDamagePhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(combatEndPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(main2Phase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(endOfTurnPhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(playerChangePhase, Game.currentPhase);

        Phase.nextPhase();
        assertEquals(new UntapPhase(), Game.currentPhase);
    }
}