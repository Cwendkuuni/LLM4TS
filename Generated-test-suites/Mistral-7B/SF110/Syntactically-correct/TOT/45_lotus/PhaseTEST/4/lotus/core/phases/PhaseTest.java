import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Game game;
 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase, untapPhase2;

 @Before
 public void setUp() {
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
 untapPhase2 = new UntapPhase();
 }

 @Test
 public void testNextPhase_UntapPhase() {
 // Test normal flow
 assertNull(Game.currentPhase);
game.setCurrentPhase(untapPhase);
 untapPhase.doPhase();
 assertEquals(upkeepPhase, Game.currentPhase);
 // Test edge case: Game in an invalid phase
 assertNull(Game.currentPhase);
 game.setCurrentPhase(combatDamagePhase);
 combatDamagePhase.doPhase();
 assertNull(Game.currentPhase);
 // Test error scenario: null phase
 assertNull(Game.currentPhase);
 game.setCurrentPhase(null);
 assertThrows(NullPointerException.class, () -> game.nextPhase());
 }

 @Test
 public void testNextPhase_UpkeepPhase() {
 // Test normal flow
 assertEquals(upkeepPhase, Game.currentPhase);
 game.nextPhase();
 assertEquals(drawPhase, Game.currentPhase);
 // Test edge case: Game in an invalid phase
 assertNotNull(Game.currentPhase);
 game.setCurrentPhase(combatBeginningPhase);
 game.nextPhase();
 assertNotEquals(combatBeginningPhase, Game.currentPhase);
 // Test error scenario: null phase
 assertNull(Game.currentPhase);
 game.setCurrentPhase(null);
 assertThrows(NullPointerException.class, () -> game.nextPhase());
 }

 // ... Add similar test cases for each method in the Phase class

 @Test
 public void testChangePhase_PlayerChangePhase() {
 // Test normal flow
 assertEquals(playerChangePhase, Game.currentPhase);
 Phase previousPhase = Game.currentPhase;
 game.changePhase(untapPhase);
 assertNotEquals(playerChangePhase, Game.currentPhase);
 assertNotEquals(previousPhase, Game.currentPhase);
 assertEquals(untapPhase, Game.currentPhase);
 }

 // ... Add similar test cases for each method in the Phase class

 @Test
 public void testNextPhase_CleanupPhase() {
 // Test normal flow
 assertNull(Game.currentPhase);
 game.setCurrentPhase(cleanupPhase);
 cleanupPhase.doPhase();
 assertEquals(playerChangePhase, Game.currentPhase);
 // Test edge case: Game in an invalid phase
 assertNull(Game.currentPhase);
 game.setCurrentPhase(combatDamagePhase);
 combatDamagePhase.doPhase();
 assertNull(Game.currentPhase);
 // Test error scenario: null phase
 assertNull(Game.currentPhase);
 game.setCurrentPhase(null);
 assertThrows(NullPointerException.class, () -> game.nextPhase());
 }
}