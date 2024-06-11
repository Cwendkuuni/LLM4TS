import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.phases.Phase;
import lotus.core.Game;

public class PhaseTest {

  @Test
  public void testNextPhase() {
    Game game = new Game();
    Phase currentPhase = new UntapPhase();
    Phase nextPhase = new UpkeepPhase();

    Phase.nextPhase();
    assertEquals(nextPhase, Game.currentPhase);
  }
}