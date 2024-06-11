import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Phase currentPhase;
 private Game game;

 @Before
 public void setup() {
 game = new Game();
 currentPhase = game.getPhase();
 }

 @Test
 public void testDoPhaseAbstractMethodThrowsException() {
 try {
 currentPhase.doPhase();
 fail("doPhase() should not be called on an abstract class");
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test(expected = AssertionError.class)
 public void testNextPhaseThrowsExceptionWhenCurrentPhaseIsNull() {
 game = new Game();
 Phase[] phases = new Phase[0];
 Game.setCurrentPhase(null);
 Game.setPhases(phases);
 Phase.nextPhase();
 }

 @Test
 public void testNextPhaseUntapPhaseToUpkeepPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();

 game.setCurrentPhase(untapPhase);
 game.setPhases(new Phase[] { untapPhase, upkeepPhase });
 Phase.nextPhase();

 assertEquals(upkeepPhase, game.getPhase());
 }

 @Test
 public void testNextPhaseUpkeepPhaseToDrawPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();

 game.setCurrentPhase(upkeepPhase);
 game.setPhases(new Phase[] { untapPhase, upkeepPhase, drawPhase });
 Phase.nextPhase();

 assertEquals(drawPhase, game.getPhase());
 }

 @Test
 public void testNextPhaseDrawPhaseToMain1Phase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();

 game.setCurrentPhase(drawPhase);
 game.setPhases(new Phase[] { untapPhase, upkeepPhase, drawPhase, main1Phase });
 Phase.nextPhase();

 assertEquals(main1Phase, game.getPhase());
 }

 @Test
 public void testNextPhaseMain1PhaseToCombatBeginningPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();

 game.setCurrentPhase(main1Phase);
 game.setPhases(new Phase[] { untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase });
 Phase.nextPhase();

 assertEquals(combatBeginningPhase, game.getPhase());
 }

 @Test
 public void testNextPhaseCombatBeginningPhaseToDeclareAttackersPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase declareAttackersPhase = new DeclareAttackersPhase();

 game.setCurrentPhase(combatBeginningPhase);
 game.setPhases(new Phase[] { untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase });
 Phase.nextPhase();

 assertEquals(declareAttackersPhase, game.getPhase());
 }

 @Test
 public void testNextPhaseDeclareAttackersPhaseToDeclareBlockersPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase declareBlockersPhase = new DeclareBlockersPhase();

 game.setCurrentPhase(declareAttackersPhase);
 game.setPhases(new Phase[] { untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase });
 Phase.nextPhase();

 assertEquals(declareBlockersPhase, game.getPhase());
 }

 @Test
 public void testNextPhaseDeclareBlockersPhaseToCombatDamagePhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 Phase combatDamagePhase = new CombatDamagePhase();

 game.setCurrentPhase(declareBlockersPhase);
 game.setPhases(new Phase[] { untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase });
 Phase.nextPhase();

 assertEquals(combatDamagePhase, game.getPhase());
 }

 @Test
 public void testNextPhaseCombatDamagePhaseToCombatEndPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 Phase combatDamagePhase = new CombatDamagePhase();
 Phase combatEndPhase = new CombatEndPhase();

 game.setCurrentPhase(combatDamagePhase);
 game.setPhases(new Phase[] { untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase });
 Phase.nextPhase();

 assertEquals(combatEndPhase, game.getPhase());
 }

 @Test
 public void testNextPhaseCombatEndPhaseToMain2Phase() {
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

 game.setCurrentPhase(combatEndPhase);
 game.setPhases(new Phase[] { untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase });
 Phase.nextPhase();

 assertEquals(main2Phase, game.getPhase());
 }

 @Test
 public void testNextPhaseMain2PhaseToEndOfTurnPhase() {
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

 game.setCurrentPhase(main2Phase);
 game.setPhases(new Phase[] { untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase });
 Phase.nextPhase();

 assertEquals(endOfTurnPhase, game.getPhase());
 }

 @Test
 public void testNextPhaseEndOfTurnPhaseToCleanupPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 Phase combatDamagePhase = new CombatDamagePhase();
 Phase combatEndPhase = new CombatEndPhase();
 Phase endOfTurnPhase = new EndOfTurnPhase();
 Phase cleanupPhase = new CleanupPhase();

 game.setCurrentPhase(endOfTurnPhase);
 game.setPhases(new Phase[] { untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, endOfTurnPhase, cleanupPhase });
 Phase.nextPhase();

 assertEquals(cleanupPhase, game.getPhase());
 }

 @Test
 public void testNextPhaseCleanupPhaseToPlayerChangePhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 Phase combatDamagePhase = new CombatDamagePhase();
 Phase combatEndPhase = new CombatEndPhase();
 Phase endOfTurnPhase = new EndOfTurnPhase();
 Phase cleanupPhase = new CleanupPhase();
 Phase playerChangePhase = new PlayerChangePhase();

 game.setCurrentPhase(cleanupPhase);
 game.setPhases(new Phase[] { untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, endOfTurnPhase, cleanupPhase, playerChangePhase });
 Phase.nextPhase();

 assertEquals(playerChangePhase, game.getPhase());
 }

 @Test
 public void testNextPhasePlayerChangePhaseToUntapPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 Phase combatDamagePhase = new CombatDamagePhase();
 Phase combatEndPhase = new CombatEndPhase();
 Phase endOfTurnPhase = new EndOfTurnPhase();
 Phase cleanupPhase = new CleanupPhase();
 Phase playerChangePhase = new PlayerChangePhase();
 Phase untapPhaseAfterPlayerChange = new UntapPhase();

 game.setCurrentPhase(playerChangePhase);
 game.setPhases(new Phase[] { untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, endOfTurnPhase, cleanupPhase, playerChangePhase, untapPhaseAfterPlayerChange });
 Phase.nextPhase();

 assertEquals(untapPhase, game.getPhase());
 }

}