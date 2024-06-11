package lotus.core.phases.test;

import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase;

 @Before
 public void setup() {
 Game game = new Game();
 untapPhase = game.getPhaseRegistry().getPhase("UntapPhase");
 upkeepPhase = game.getPhaseRegistry().getPhase("UpkeepPhase");
 drawPhase = game.getPhaseRegistry().getPhase("DrawPhase");
 main1Phase = game.getPhaseRegistry().getPhase("Main1Phase");
 combatBeginningPhase = game.getPhaseRegistry().getPhase("CombatBeginningPhase");
 declareAttackersPhase = game.getPhaseRegistry().getPhase("DeclareAttackersPhase");
 declareBlockersPhase = game.getPhaseRegistry().getPhase("DeclareBlockersPhase");
 combatDamagePhase = game.getPhaseRegistry().getPhase("CombatDamagePhase");
 combatEndPhase = game.getPhaseRegistry().getPhase("CombatEndPhase");
 main2Phase = game.getPhaseRegistry().getPhase("Main2Phase");
 endOfTurnPhase = game.getPhaseRegistry().getPhase("EndOfTurnPhase");
 cleanupPhase = game.getPhaseRegistry().getPhase("CleanupPhase");
 playerChangePhase = game.getPhaseRegistry().getPhase("PlayerChangePhase");
 }

 @Test
 public void testDoPhaseAbstractMethod() {
    assertNotNull(untapPhase);
    assertNotNull(upkeepPhase);
    // Abstract methods cannot be instantiated or invoked directly.
    // Uncomment the following lines if the methods in the subclasses are implemented and can be invoked.
    // untapPhase.doPhase();
    // upkeepPhase.doPhase();
    fail("doPhase() is an abstract method and cannot be tested directly.");
 }

 @Test
 public void testNextPhase_CurrentPhaseIsUntapPhase() {
    Game.currentPhase = untapPhase;
    Phase expectedNextPhase = upkeepPhase;
    Phase actualNextPhase = Phase.nextPhase();
    assertEquals(expectedNextPhase, actualNextPhase);
 }

 @Test
 public void testNextPhase_CurrentPhaseIsUpkeepPhase() {
    Game.currentPhase = upkeepPhase;
    Phase expectedNextPhase = drawPhase;
    Phase actualNextPhase = Phase.nextPhase();
    assertEquals(expectedNextPhase, actualNextPhase);
 }

 @Test
 public void testNextPhase_CurrentPhaseIsDrawPhase() {
    Game.currentPhase = drawPhase;
    Phase expectedNextPhase = main1Phase;
    Phase actualNextPhase = Phase.nextPhase();
    assertEquals(expectedNextPhase, actualNextPhase);
 }

 @Test
 public void testNextPhase_CurrentPhaseIsMain1Phase() {
    Game.currentPhase = main1Phase;
    Phase expectedNextPhase = combatBeginningPhase;
    Phase actualNextPhase = Phase.nextPhase();
    assertEquals(expectedNextPhase, actualNextPhase);
 }

 @Test
 public void testNextPhase_CurrentPhaseIsCombatBeginningPhase() {
    Game.currentPhase = combatBeginningPhase;
    Phase expectedNextPhase = declareAttackersPhase;
    Phase actualNextPhase = Phase.nextPhase();
    assertEquals(expectedNextPhase, actualNextPhase);
 }

 @Test
 public void testNextPhase_CurrentPhaseIsDeclareAttackersPhase() {
    Game.currentPhase = declareAttackersPhase;
    Phase expectedNextPhase = declareBlockersPhase;
    Phase actualNextPhase = Phase.nextPhase();
    assertEquals(expectedNextPhase, actualNextPhase);
 }

 @Test
 public void testNextPhase_CurrentPhaseIsDeclareBlockersPhase() {
    Game.currentPhase = declareBlockersPhase;
    Phase expectedNextPhase = combatDamagePhase;
    Phase actualNextPhase = Phase.nextPhase();
    assertEquals(expectedNextPhase, actualNextPhase);
 }

 @Test
 public void testNextPhase_CurrentPhaseIsCombatDamagePhase() {
    Game.currentPhase = combatDamagePhase;
    Phase expectedNextPhase = combatEndPhase;
    Phase actualNextPhase = Phase.nextPhase();
    assertEquals(expectedNextPhase, actualNextPhase);
 }

 @Test
 public void testNextPhase_CurrentPhaseIsCombatEndPhase() {
    Game.currentPhase = combatEndPhase;
    Phase expectedNextPhase = main2Phase;
    Phase actualNextPhase = Phase.nextPhase();
    assertEquals(expectedNextPhase, actualNextPhase);
 }

 @Test
 public void testNextPhase_CurrentPhaseIsMain2Phase() {
    Game.currentPhase = main2Phase;
    Phase expectedNextPhase = endOfTurnPhase;
    Phase actualNextPhase = Phase.nextPhase();
    assertEquals(expectedNextPhase, actualNextPhase);
 }

 @Test
 public void testNextPhase_CurrentPhaseIsEndOfTurnPhase() {
    Game.currentPhase = endOfTurnPhase;
    Phase expectedNextPhase = cleanupPhase;
    Phase actualNextPhase = Phase.nextPhase();
    assertEquals(expectedNextPhase, actualNextPhase);
 }

 @Test
 public void testNextPhase_CurrentPhaseIsCleanupPhase() {
    Game.currentPhase = cleanupPhase;
    Phase expectedNextPhase = playerChangePhase;
    Phase actualNextPhase = Phase.nextPhase();
    assertEquals(expectedNextPhase, actualNextPhase);
 }

 @Test
 public void testNextPhase_CurrentPhaseIsPlayerChangePhase() {
    Game.currentPhase = playerChangePhase;
    int expectedPlayingPlayer = 1;
    int actualPlayingPlayer = Game.playingPlayer;
    Phase.nextPhase();
    int actualNextPlayingPlayer = Game.playingPlayer;
    assertEquals(expectedPlayingPlayer, actualPlayingPlayer);
    assertEquals(2, actualNextPlayingPlayer);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_CurrentPhaseIsNull() {
    Game.currentPhase = null;
    Phase.nextPhase();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextPhase_PassedPhaseIsNull() {
    Phase nullPhase = null;
    Phase.nextPhase(nullPhase);
 }
}