import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import lotus.core.*;
import lotus.core.effect.*;
import static org.junit.Assert.*;

public class PhaseTest {

 private Game game;
 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase;

 @Before
 public void setUp() {
 game = Mockito.mock(Game.class);
 untapPhase = Mockito.spy(new UntapPhase());
 upkeepPhase = Mockito.spy(new UpkeepPhase());
 drawPhase = Mockito.spy(new DrawPhase());
 main1Phase = Mockito.spy(new Main1Phase());
 combatBeginningPhase = Mockito.spy(new CombatBeginningPhase());
 declareAttackersPhase = Mockito.spy(new DeclareAttackersPhase());
 declareBlockersPhase = Mockito.spy(new DeclareBlockersPhase());
 combatDamagePhase = Mockito.spy(new CombatDamagePhase());
 combatEndPhase = Mockito.spy(new CombatEndPhase());
 main2Phase = Mockito.spy(new Main2Phase());
 endOfTurnPhase = Mockito.spy(new EndOfTurnPhase());
 cleanupPhase = Mockito.spy(new CleanupPhase());
 playerChangePhase = Mockito.spy(new PlayerChangePhase());
 }

 @Test
 public void testDoPhase() {
 // We cannot test the abstract method directly, but we can test the subclasses
 verify(untapPhase, Mockito.times(1)).doPhase();
 verify(upkeepPhase, Mockito.times(1)).doPhase();
 verify(drawPhase, Mockito.times(1)).doPhase();
 // ... test other subclasses in a similar way
 }

 @Test
 public void testChangePhase() {
 // Test the initial phase
 assertEquals(Phase.UntapPhase.class, Game.currentPhase.getClass());
 Phase.nextPhase(game);
 assertEquals(Phase.UpkeepPhase.class, Game.currentPhase.getClass());
 // Test the other phases
 Phase.nextPhase(game);
 assertEquals(Phase.DrawPhase.class, Game.currentPhase.getClass());
 // ... test other phases in a similar way
 }

 @Test
 public void testNextPhaseChain() {
 Phase currentPhase = game.getCurrentPhase();
 for (int i = 0; i < 12; i++) {
 Phase.nextPhase(game);
 assertNotSame(currentPhase, Game.currentPhase);
 currentPhase = Game.currentPhase;
 }
}

 @Test
 public void testPlayerChange() {
 // Test player change when there are an odd number of players
 Game gameWithThreePlayers = Mockito.spy(new Game(3));
 Phase.nextPhase(gameWithThreePlayers);
 assertEquals(Phase.UntapPhase.class, Game.currentPhase.getClass());
 assertEquals(3, Game.playingPlayer);
 Phase.nextPhase(gameWithThreePlayers);
 assertEquals(Phase.UpkeepPhase.class, Game.currentPhase.class);
 assertEquals(1, Game.playingPlayer);
 // Test player change when there are an even number of players
 Game gameWithFourPlayers = Mockito.spy(new Game(4));
 Phase.nextPhase(gameWithFourPlayers);
 assertEquals(Phase.UntapPhase.class, Game.currentPhase.class);
 assertEquals(4, Game.playingPlayer);
 Phase.nextPhase(gameWithFourPlayers);
 assertEquals(Phase.UpkeepPhase.class, Game.currentPhase.class);
 assertEquals(2, Game.playingPlayer);
}
}