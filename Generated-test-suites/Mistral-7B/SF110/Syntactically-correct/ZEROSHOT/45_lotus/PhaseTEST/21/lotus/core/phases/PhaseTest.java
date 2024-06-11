package lotus.core.phases;

import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

 private Game game = new Game(); // Assuming Game has a no-arg constructor

 @Test
 public void testDoPhaseAndNextPhase() {
  Phase untapPhase = new UntapPhase();
  Phase upkeepPhase = new UpkeepPhase();
  // Test Untap Phase
  untapPhase.doPhase();
  assertEquals(untapPhase, game.getCurrentPhase());
  untapPhase.doPhase(); // Should not change the phase since UntapPhase is already done
  untapPhase.doPhase(); // Should not change the phase since UntapPhase is already done

  // Test Upkeep Phase
  untapPhase.doPhase(); // Change the phase to Upkeep Phase
  upkeepPhase.doPhase();
  assertEquals(upkeepPhase, game.getCurrentPhase());
  upkeepPhase.doPhase(); // Should not change the phase since UpkeepPhase is already done
  upkeepPhase.doPhase(); // Should not change the phase since UpkeepPhase is already done

  // Test other phases in the same way
  Phase drawPhase = new DrawPhase();
  Phase main1Phase = new Main1Phase();
  Phase combatBeginningPhase = new CombatBeginningPhase();
  Phase declareAttackersPhase = new DeclareAttackersPhase();
  Phase declareBlockersPhase = new DeclareBlockersPhase();
  Phase combatDamagePhase = new CombatDamagePhase();
  Phase combatEndPhase = new CombatEndPhase();
  Phase main2Phase = new Main2Phase();
  Phase endOfTurnPhase = new EndOfTurnPhase();
  Phase cleanupPhase = new CleanupPhase();
  Phase playerChangePhase = new PlayerChangePhase();

  // Test nextPhase method
  untapPhase.doPhase();
  upkeepPhase.doPhase();
  drawPhase.doPhase();
  main1Phase.doPhase();
  combatBeginningPhase.doPhase();
  declareAttackersPhase.doPhase();
  declareBlockersPhase.doPhase();
  combatDamagePhase.doPhase();
  combatEndPhase.doPhase();
  main2Phase.doPhase();
  endOfTurnPhase.doPhase();
  cleanupPhase.doPhase();
  playerChangePhase.doPhase();
  untapPhase.doPhase(); // Should change the phase to UntapPhase again
 }
}