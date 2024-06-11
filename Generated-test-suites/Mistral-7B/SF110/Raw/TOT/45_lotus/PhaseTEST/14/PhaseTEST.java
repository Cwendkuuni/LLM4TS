import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Game game;
 private Player player1, player2;

 @Before
 public void setup() {
 game = new Game();
 player1 = new Player("Player1", game, 1);
 player2 = new Player("Player2", game, 2);
 game.addPlayer(player1);
 game.addPlayer(player2);
 }

 @Test
 public void testUntapPhase() {
 Phase preUntapPhase = game.getCurrentPhase();
 Phase untapPhase = new UntapPhase();

 // Test typical use case
 game.nextPhase();
 Phase currentPhase = game.getCurrentPhase();
 assertEquals(UntapPhase.class, currentPhase.getClass());

 // Test edge case - no phase change
 game.nextPhase();
 currentPhase = game.getCurrentPhase();
 assertEquals(UntapPhase.class, currentPhase.getClass());

 // Test error scenario - invalid phase
 Phase invalidPhase = new InvalidPhase();
 assertThrows(IllegalArgumentException.class, () -> game.changePhase(invalidPhase));

 // Test cleanup - player change
 game.changePhase(new PlayerChangePhase());
 game.nextPhase();
 currentPhase = game.getCurrentPhase();
 assertEquals(UntapPhase.class, currentPhase.getClass());
 }

 @Test
 public void testUpkeepPhase() {
 // Test typical use case
 game.changePhase(new UntapPhase());
 game.nextPhase();
 Phase currentPhase = game.getCurrentPhase();
 assertEquals(UpkeepPhase.class, currentPhase.getClass());

 // Test edge case - no phase change
 game.nextPhase();
 currentPhase = game.getCurrentPhase();
 assertEquals(UpkeepPhase.class, currentPhase.getClass());

 // Test error scenario - invalid phase
 assertThrows(IllegalArgumentException.class, () -> game.changePhase(new InvalidPhase()));
 }

 // Repeat the UpkeepPhase test for all phases until CleanupPhase

 @Test
 public void testCleanupPhase() {
 // Test typical use case
 game.changePhase(new EndOfTurnPhase());
 game.nextPhase();
 game.nextPhase(); // Player change
 game.nextPhase(); // UntapPhase
 Phase currentPhase = game.getCurrentPhase();
 assertEquals(CleanupPhase.class, currentPhase.getClass());

 // Test edge case - no phase change
 game.nextPhase();
 currentPhase = game.getCurrentPhase();
 assertEquals(CleanupPhase.class, currentPhase.getClass());

 // Test error scenario - invalid phase
 assertThrows(IllegalArgumentException.class, () -> game.changePhase(new InvalidPhase()));
 }
}