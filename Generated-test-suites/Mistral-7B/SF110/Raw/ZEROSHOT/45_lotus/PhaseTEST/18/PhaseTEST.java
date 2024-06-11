package lotus.core.phases;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import lotus.core.Game;
import lotus.core.effect.ChangePhase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PhaseTest {

 private Phase untapPhase;
 private Phase upkeepPhase;
 private Phase drawPhase;
 // Add other phases as needed
 private Game game;

 @Before
 public void setUp() {
 untapPhase = Mockito.spy(new UntapPhase());
 upkeepPhase = Mockito.spy(new UpkeepPhase());
 drawPhase = Mockito.spy(new DrawPhase());
 // Initialize game object
 game = mock(Game.class);
 }

 @Test
 public void testNextPhase_UntapPhaseToUpkeepPhase() {
 // Set up initial state
 Mockito.when(game.currentPhase).thenReturn(untapPhase);
 // Execute test
 Phase.nextPhase(game);
 // Verify results
 Mockito.verify(game).currentPhase.equals(upkeepPhase);
 }

 @Test
 public void testNextPhase_UpkeepPhaseToDrawPhase() {
 // Set up initial state
 Mockito.when(game.currentPhase).thenReturn(upkeepPhase);
 // Execute test
 Phase.nextPhase(game);
 // Verify results
 Mockito.verify(game).currentPhase.equals(drawPhase);
 }

 // Add other test methods as needed to test all abstract methods and functionality in the Phase class

}