import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import lotus.core.phases.*;

public class PhaseTest {

 private static Game game = new Game();

 @Test
 public void testUntapPhase() {
 // Test typical use case
 Phase untapPhase = new UntapPhase();
 Phase.nextPhase();
 assertNotNull(game.currentPhase);
 assertEquals(UntapPhase.class, game.currentPhase.getClass());
 untapPhase.doPhase();
 // Test edge cases
 Game.currentPhase = new EndOfTurnPhase();
 Phase.nextPhase();
 assertNull(game.currentPhase);
 Game.currentPhase = new CleanupPhase();
 Phase.nextPhase();
 assertNull(game.currentPhase);
 // Test error scenario
 try {
 Phase.nextPhase();
 fail("Game is already in EndOfTurnPhase, should not be able to call nextPhase()");
 } catch (IllegalStateException e) {
 // Expected exception
 }
 }

 @Test
 public void testUpkeepPhase() {
 // Test typical use case
 Phase upkeepPhase = new UpkeepPhase();
 Phase.nextPhase();
 assertNotNull(game.currentPhase);
 assertEquals(UpkeepPhase.class, game.currentPhase.getClass());
 upkeepPhase.doPhase();
 // Test edge cases
 Game.currentPhase = new UntapPhase();
 Phase.nextPhase();
 assertNotNull(game.currentPhase);
 assertEquals(UpkeepPhase.class, game.currentPhase.getClass());
 // Test error scenario
 try {
 Phase.nextPhase();
 fail("Game is already in UpkeepPhase, should not be able to call nextPhase()");
 } catch (IllegalStateException e) {
 // Expected exception
 }
 }

 // Add test cases for DrawPhase, Main1Phase, CombatBeginningPhase, DeclareAttackersPhase, DeclareBlockersPhase, CombatDamagePhase, CombatEndPhase, Main2Phase, EndOfTurnPhase, and CleanupPhase here

 @Test
 public void testChangePhase() {
 // Test typical use case
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Game.currentPhase = untapPhase;
 Phase.changePhase(upkeepPhase);
 assertNotNull(game.currentPhase);
 assertEquals(UpkeepPhase.class, game.currentPhase.getClass());
 // Test edge cases
 Game.currentPhase = new EndOfTurnPhase();
 try {
 Phase.changePhase(new UntapPhase());
 fail("Game is already in EndOfTurnPhase, should not be able to call changePhase() with UntapPhase");
 } catch (IllegalStateException e) {
 // Expected exception
 }
 // Test error scenario
 try {
 Phase.changePhase(null);
 fail("Cannot call changePhase() with null argument");
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test
 public void testNextPhase() {
 // Test typical use case
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
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
 Phase untapPhase2 = new UntapPhase();
 Game.currentPhase = untapPhase;
 Phase.nextPhase();
 assertNotNull(game.currentPhase);
 assertEquals(UpkeepPhase.class, game.currentPhase.getClass());
 Phase.nextPhase();
 assertNotNull(game.currentPhase);
 assertEquals(DrawPhase.class, game.currentPhase.class);
 // Test edge cases
 Game.currentPhase = new EndOfTurnPhase();
 Phase.nextPhase();
 assertNull(game.currentPhase);
 Game.currentPhase = new CleanupPhase();
 Phase.nextPhase();
 assertNull(game.currentPhase);
 // Test error scenario
 try {
 Phase.nextPhase();
 fail("Game is already in EndOfTurnPhase, should not be able to call nextPhase()");
 } catch (IllegalStateException e) {
 // Expected exception
 }
 }
}