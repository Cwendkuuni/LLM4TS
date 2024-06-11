package lotus.core.phases;

import org.junit.Assert;
import org.junit.Test;
import lotus.core.Game;

public class PhaseTest {

 @Test
 public void testUntapPhase() {
 Phase untapPhase = new UntapPhase();
 Game game = new Game();
 game.setCurrentPhase(new UntapPhase());
 untapPhase.doPhase();

 // Typical use case
 Assert.assertEquals(untapPhase, game.getCurrentPhase());
 // Edge case: What happens if we're already in an UntapPhase?
 Game game2 = new Game();
 game2.setCurrentPhase(new UntapPhase());
 untapPhase.doPhase();
 Assert.assertEquals(untapPhase, game2.getCurrentPhase());
 // Error scenario: What happens if we try to call doPhase() on an instance of a different Phase?
 Phase drawPhase = new DrawPhase();
 game.setCurrentPhase(untapPhase);
 drawPhase.doPhase();
 Assert.assertNotEquals(drawPhase, game.getCurrentPhase());
 // Clean up
 game = null;
 game2 = null;
 untapPhase = null;
 drawPhase = null;
 }

 @Test
 public void testUpkeepPhase() {
 Phase upkeepPhase = new UpkeepPhase();
 Game game = new Game();
 game.setCurrentPhase(new UntapPhase());
 nextPhase();

 // Typical use case
 Assert.assertEquals(upkeepPhase, game.getCurrentPhase());
 // Edge case: What happens if we're already in an UpkeepPhase?
 Game game2 = new Game();
 game2.setCurrentPhase(upkeepPhase);
 nextPhase();
 Assert.assertEquals(drawPhase, game2.getCurrentPhase());
 // Error scenario: What happens if we try to call nextPhase() while in a different Phase than Upkeep?
 Phase main1Phase = new Main1Phase();
 game.setCurrentPhase(main1Phase);
 nextPhase();
 Assert.assertEquals(combatBeginningPhase, game.getCurrentPhase());

 // Clean up
 game = null;
 game2 = null;
 upkeepPhase = null;
 main1Phase = null;
 }

 // ... Continue this pattern for each remaining Phase.

 @Test
 public void testCleanupPhase() {
 Phase cleanupPhase = new CleanupPhase();
 Game game = new Game();
 game.setCurrentPhase(new EndOfTurnPhase());
 nextPhase();

 // Typical use case
 Assert.assertEquals(cleanupPhase, game.getCurrentPhase());
 // Edge case: What happens if we're already in a CleanupPhase?
 Game game2 = new Game();
 game2.setCurrentPhase(cleanupPhase);
 nextPhase();
 Assert.assertEquals(playerChangePhase, game2.getCurrentPhase());
 // Error scenario: What happens if we try to call nextPhase() while in a different Phase than Cleanup?
 Phase untapPhase = new UntapPhase();
 game.setCurrentPhase(untapPhase);
 nextPhase();
 Assert.assertEquals(upkeepPhase, game.getCurrentPhase());

 // Clean up
 game = null;
 game2 = null;
 cleanupPhase = null;
 untapPhase = null;
 }
}