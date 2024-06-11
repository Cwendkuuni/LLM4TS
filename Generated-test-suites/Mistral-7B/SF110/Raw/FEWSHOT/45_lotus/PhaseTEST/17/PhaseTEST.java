package lotus.core.phases;

import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    @Test
    public void testNextPhaseUntap() {
        Game game = new Game();
        Phase currentPhase = new UntapPhase();
        Phase nextPhase = new UpkeepPhase();

        assertEquals(currentPhase, game.getPhase());
        Phase.nextPhase();
        assertEquals(nextPhase, game.getPhase());
    }

    @Test
    public void testNextPhaseUpkeep() {
        Game game = new Game();
        Phase currentPhase = new UpkeepPhase();
        Phase nextPhase = new DrawPhase();

        assertEquals(currentPhase, game.getPhase());
        Phase.nextPhase();
        assertEquals(nextPhase, game.getPhase());
    }

    @Test
    public void testNextPhaseDraw() {
        Game game = new Game();
        Phase currentPhase = new DrawPhase();
        Phase nextPhase = new Main1Phase();

        assertEquals(currentPhase, game.getPhase());
        Phase.nextPhase();
        assertEquals(nextPhase, game.getPhase());
    }

    @Test
    public void testNextPhaseMain1() {
        Game game = new Game();
        Phase currentPhase = new Main1Phase();
        Phase nextPhase = new CombatBeginningPhase();

        assertEquals(currentPhase, game.getPhase());
        Phase.nextPhase();
        assertEquals(nextPhase, game.getPhase());
    }

    @Test
    public void testNextPhaseCombatBeginning() {
        Game game = new Game();
        Phase currentPhase = new CombatBeginningPhase();
        Phase nextPhase = new DeclareAttackersPhase();

        assertEquals(currentPhase, game.getPhase());
        Phase.nextPhase();
        assertEquals(nextPhase, game.getPhase());
    }

    @Test
    public void testNextPhaseDeclareAttackers() {
        Game game = new Game();
        Phase currentPhase = new DeclareAttackersPhase();
        Phase nextPhase = new DeclareBlockersPhase();

        assertEquals(currentPhase, game.getPhase());
        Phase.nextPhase();
        assertEquals(nextPhase, game.getPhase());
    }

    @Test
    public void testNextPhaseDeclareBlockers() {
        Game game = new Game();
        Phase currentPhase = new DeclareBlockersPhase();
        Phase nextPhase = new CombatDamagePhase();

        assertEquals(currentPhase, game.getPhase());
        Phase.nextPhase();
        assertEquals(nextPhase, game.getPhase());
    }

    @Test
    public void testNextPhaseCombatDamage() {
        Game game = new Game();
        Phase currentPhase = new CombatDamagePhase();
        Phase nextPhase = new CombatEndPhase();

        assertEquals(currentPhase, game.getPhase());
        Phase.nextPhase();
        assertEquals(nextPhase, game.getPhase());
    }

    @Test
    public void testNextPhaseCombatEnd() {
        Game game = new Game();
        Phase currentPhase = new CombatEndPhase();
        Phase nextPhase = new Main2Phase();

        assertEquals(currentPhase, game.getPhase());
        Phase.nextPhase();
        assertEquals(nextPhase, game.getPhase());
    }

    @Test
    public void testNextPhaseMain2() {
        Game game = new Game();
        Phase currentPhase = new Main2Phase();
        Phase nextPhase = new EndOfTurnPhase();

        assertEquals(currentPhase, game.getPhase());
        Phase.nextPhase();
        assertEquals(nextPhase, game.getPhase());
    }

    @Test
    public void testNextPhaseEndOfTurn() {
        Game game = new Game();
        Phase currentPhase = new EndOfTurnPhase();
        Phase nextPhase = new CleanupPhase();

        assertEquals(currentPhase, game.getPhase());
        Phase.nextPhase();
        assertEquals(nextPhase, game.getPhase());
    }

    @Test
    public void testNextPhaseCleanup() {
        Game game = new Game();
        Phase currentPhase = new CleanupPhase();
        Phase nextPhase = new PlayerChangePhase();

        assertEquals(currentPhase, game.getPhase());
        Phase.nextPhase();
        assertEquals(nextPhase, game.getPhase());
    }

    @Test
    public void testNextPhasePlayerChange() {
        Game game = new Game();
        Phase currentPhase = new PlayerChangePhase();
        int playingPlayer = game.getPlayingPlayer();

        assertEquals(currentPhase, game.getPhase());
        Phase.nextPhase();
        assertEquals(new UntapPhase(), game.getPhase());
        assertEquals(playingPlayer % 2 + 1, game.getPlayingPlayer());
    }
}