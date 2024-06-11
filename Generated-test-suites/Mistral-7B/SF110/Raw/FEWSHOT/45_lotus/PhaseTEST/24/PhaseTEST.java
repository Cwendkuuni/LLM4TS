package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    @Test
    public void testNextPhase() {
        Game game = new Game();
        Phase currentPhase = game.getPhase();

        // Test transition from UntapPhase to UpkeepPhase
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        nextPhase(game, untapPhase);
        assertEquals(upkeepPhase, game.getPhase());

        // Test transition from other phases
        nextPhase(game, upkeepPhase);
        assertEquals(new DrawPhase(), game.getPhase());
        nextPhase(game, drawPhase);
        assertEquals(new Main1Phase(), game.getPhase());
        nextPhase(game, main1Phase);
        assertEquals(new CombatBeginningPhase(), game.getPhase());
        nextPhase(game, combatBeginningPhase);
        assertEquals(new DeclareAttackersPhase(), game.getPhase());
        nextPhase(game, declareAttackersPhase);
        assertEquals(new DeclareBlockersPhase(), game.getPhase());
        nextPhase(game, declareBlockersPhase);
        assertEquals(new CombatDamagePhase(), game.getPhase());
        nextPhase(game, combatDamagePhase);
        assertEquals(new CombatEndPhase(), game.getPhase());
        nextPhase(game, combatEndPhase);
        assertEquals(new Main2Phase(), game.getPhase());
        nextPhase(game, main2Phase);
        assertEquals(new EndOfTurnPhase(), game.getPhase());
        nextPhase(game, endOfTurnPhase);
        assertEquals(new CleanupPhase(), game.getPhase());
        nextPhase(game, cleanupPhase);
        assertEquals(new PlayerChangePhase(), game.getPhase());
        nextPhase(game, playerChangePhase);
        assertEquals(new UntapPhase(), game.getPhase());
    }

    @Test
    public void testChangePhase() {
        Game game = new Game();
        Phase currentPhase = game.getPhase();

        // Test null case
        nextPhase(game, null);
        assertEquals(currentPhase, game.getPhase());

        // Test valid phase transitions
        Phase untapPhase = new UntapPhase();
        nextPhase(game, untapPhase);
        assertEquals(new UpkeepPhase(), game.getPhase());
        nextPhase(game, new UpkeepPhase());
        assertEquals(new DrawPhase(), game.getPhase());
        nextPhase(game, new DrawPhase());
        // ... repeat for all phases
    }

    @Test
    public void testGetCurrentPhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        nextPhase(game, untapPhase);
        assertEquals(upkeepPhase, game.getPhase());
    }

    private void nextPhase(Game game, Phase nextPhase) {
        Phase currentPhase = game.getPhase();
        Phase newPhase = nextPhase;
        if (newPhase == null) {
            return;
        }
        Phase.changePhase(newPhase);
    }

    @Before
    public void setup() {
        Game game = new Game();
        Game.currentPhase = new UntapPhase();
    }

    @After
    public void tearDown() {
        Game.currentPhase = null;
    }
}