import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Game game;
 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase;

 @Before
 public void setup() {
 game = new Game();
 untapPhase = new UntapPhase();
 upkeepPhase = new UpkeepPhase();
 drawPhase = new DrawPhase();
 main1Phase = new Main1Phase();
 combatBeginningPhase = new CombatBeginningPhase();
 declareAttackersPhase = new DeclareAttackersPhase();
 declareBlockersPhase = new DeclareBlockersPhase();
 combatDamagePhase = new CombatDamagePhase();
 combatEndPhase = new CombatEndPhase();
 main2Phase = new Main2Phase();
 endOfTurnPhase = new EndOfTurnPhase();
 cleanupPhase = new CleanupPhase();
 playerChangePhase = new PlayerChangePhase();
 }

 @Test
 public void testUntapPhase() {
 // Typical use case
 game.currentPhase = untapPhase;
 untapPhase.doPhase();
 assertNull(game.getPlayer(0).getCreaturesWithTaps(CardType.TAP).get(0));
 game.getPlayer(0).getCreature(0).setTapped(true);
 untapPhase.doPhase();
 assertNotNull(game.getPlayer(0).getCreaturesWithTaps(CardType.TAP).get(0));

 // Edge case: No creatures to untap
 game.getPlayer(0).setCreatures(new Creature[0]);
 untapPhase.doPhase();
 assertEquals(0, game.getPlayer(0).getCreatures().size());

 // Error scenario: Changing phase without calling nextPhase()
 game.currentPhase = untapPhase;
 assertThrows(IllegalStateException.class, () -> untapPhase.doPhase());
 }

 @Test
 public void testUpkeepPhase() {
 // Typical use case
 game.currentPhase = upkeepPhase;
 upkeepPhase.doPhase();
 // Upkeep effects are resolved here

 // Edge case: No upkeep effects
 game.currentPlayer = 0;
 game.addEffect(new NoOpEffect());
 upkeepPhase.doPhase();

 // Error scenario: Changing phase without calling nextPhase()
 game.currentPhase = upkeepPhase;
 assertThrows(IllegalStateException.class, () -> upkeepPhase.doPhase());
 }

 // ... Continue for other methods with similar test cases

 @Test
 public void testNextPhase() {
 // Test changing phases in order
 game.currentPhase = untapPhase;
 nextPhase();
 assertSame(upkeepPhase, game.currentPhase);
 nextPhase();
 assertSame(drawPhase, game.currentPhase);
 // Test changing phases out of order
 game.currentPhase = main1Phase;
 nextPhase();
 assertSame(combatBeginningPhase, game.currentPhase);
 nextPhase();
 assertSame(declareAttackersPhase, game.currentPhase);
 nextPhase();
 assertSame(declareBlockersPhase, game.currentPhase);
 nextPhase();
 assertSame(combatDamagePhase, game.currentPhase);
 nextPhase();
 assertSame(combatEndPhase, game.currentPhase);
 nextPhase();
 assertSame(main2Phase, game.currentPhase);
 nextPhase();
 assertSame(endOfTurnPhase, game.currentPhase);
 nextPhase();
 assertSame(cleanupPhase, game.currentPhase);
 nextPhase();
 assertSame(playerChangePhase, game.currentPhase);
 nextPhase();
 assertSame(untapPhase, game.currentPhase);
 }

}