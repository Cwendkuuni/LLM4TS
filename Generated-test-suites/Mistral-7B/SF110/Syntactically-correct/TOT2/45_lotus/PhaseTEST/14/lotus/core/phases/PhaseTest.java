import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.phases.Phase;
import lotus.core.phases.UntapPhase;
import lotus.core.phases.UpkeepPhase;
import lotus.core.phases.DrawPhase;
import lotus.core.phases.Main1Phase;
import lotus.core.phases.CombatBeginningPhase;
import lotus.core.phases.DeclareAttackersPhase;
import lotus.core.phases.DeclareBlockersPhase;
import lotus.core.phases.CombatDamagePhase;
import lotus.core.phases.CombatEndPhase;
import lotus.core.phases.Main2Phase;
import lotus.core.phases.EndOfTurnPhase;
import lotus.core.phases.CleanupPhase;
import lotus.core.phases.PlayerChangePhase;
import lotus.core.Game;

public class PhaseTest {
    @Test
    public void testNextPhase() {
        Game game = new Game();
        Phase currentPhase = new UntapPhase();
        Game.setCurrentPhase(currentPhase);
        PhaseTest.nextPhase(game);
        Phase nextPhase = Game.getCurrentPhase();
        assertTrue(nextPhase instanceof UpkeepPhase);
    }
}