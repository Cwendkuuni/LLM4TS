import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.phases.*;

public class PhaseTest {

 private static Game game = new Game();

 @Test
 public void testNextPhase_UntapPhaseToUpkeepPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();

 // Set the initial phase
 game.currentPhase = untapPhase;

 // Test the nextPhase() method
 Phase expectedPhase = upkeepPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);

 // Assert that the expected and actual phases match
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_UpkeepPhaseToDrawPhase() {
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();

 // Set the initial phase
 game.currentPhase = upkeepPhase;

 // Test the nextPhase() method
 Phase expectedPhase = drawPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);

 // Assert that the expected and actual phases match
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_DrawPhaseToMain1Phase() {
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();

 // Set the initial phase
 game.currentPhase = drawPhase;

 // Test the nextPhase() method
 Phase expectedPhase = main1Phase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);

 // Assert that the expected and actual phases match
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_Main1PhaseToCombatBeginningPhase() {
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();

 // Set the initial phase
 game.currentPhase = main1Phase;

 // Test the nextPhase() method
 Phase expectedPhase = combatBeginningPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);

 // Assert that the expected and actual phases match
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_CombatBeginningPhaseToDeclareAttackersPhase() {
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase declareAttackersPhase = new DeclareAttackersPhase();

 // Set the initial phase
 game.currentPhase = combatBeginningPhase;

 // Test the nextPhase() method
 Phase expectedPhase = declareAttackersPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);

 // Assert that the expected and actual phases match
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_DeclareAttackersPhaseToDeclareBlockersPhase() {
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase declareBlockersPhase = new DeclareBlockersPhase();

 // Set the initial phase
 game.currentPhase = declareAttackersPhase;

 // Test the nextPhase() method
 Phase expectedPhase = declareBlockersPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);

 // Assert that the expected and actual phases match
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_DeclareBlockersPhaseToCombatDamagePhase() {
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 Phase combatDamagePhase = new CombatDamagePhase();

 // Set the initial phase
 game.currentPhase = declareBlockersPhase;

 // Test the nextPhase() method
 Phase expectedPhase = combatDamagePhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);

 // Assert that the expected and actual phases match
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_CombatDamagePhaseToCombatEndPhase() {
 Phase combatDamagePhase = new CombatDamagePhase();
 Phase combatEndPhase = new CombatEndPhase();

 // Set the initial phase
 game.currentPhase = combatDamagePhase;

 // Test the nextPhase() method
 Phase expectedPhase = combatEndPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);

 // Assert that the expected and actual phases match
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_CombatEndPhaseToMain2Phase() {
 Phase combatEndPhase = new CombatEndPhase();
 Phase main2Phase = new Main2Phase();

 // Set the initial phase
 game.currentPhase = combatEndPhase;

 // Test the nextPhase() method
 Phase expectedPhase = main2Phase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);

 // Assert that the expected and actual phases match
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_Main2PhaseToEndOfTurnPhase() {
 Phase main2Phase = new Main2Phase();
 Phase endOfTurnPhase = new EndOfTurnPhase();

 // Set the initial phase
 game.currentPhase = main2Phase;

 // Test the nextPhase() method
 Phase expectedPhase = endOfTurnPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);

 // Assert that the expected and actual phases match
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_EndOfTurnPhaseToCleanupPhase() {
 Phase endOfTurnPhase = new EndOfTurnPhase();
 Phase cleanupPhase = new CleanupPhase();

 // Set the initial phase
 game.currentPhase = endOfTurnPhase;

 // Test the nextPhase() method
 Phase expectedPhase = cleanupPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);

 // Assert that the expected and actual phases match
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_CleanupPhaseToPlayerChangePhase() {
 Phase cleanupPhase = new CleanupPhase();
 Phase playerChangePhase = new PlayerChangePhase();

 // Set the initial phase
 game.currentPhase = cleanupPhase;

 // Test the nextPhase() method
 Phase expectedPhase = playerChangePhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);

 // Assert that the expected and actual phases match
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_PlayerChangePhaseToUntapPhase() {
 Phase playerChangePhase = new PlayerChangePhase();
 Phase untapPhase = new UntapPhase();

 // Set the initial phase
 game.currentPhase = playerChangePhase;

 // Test the nextPhase() method
 Phase expectedPhase = untapPhase;
 Phase actualPhase = Phase.nextPhase(game.currentPhase);

 // Assert that the expected and actual phases match
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_InvalidInitialPhase() {
 Phase unknownPhase = new Object(); // Replace with an instance of an unknown phase

 // Set the initial phase to an unknown phase
 game.currentPhase = unknownPhase;

 // Test that the nextPhase() method throws an exception
 try {
 Phase nextPhase = Phase.nextPhase(game.currentPhase);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}