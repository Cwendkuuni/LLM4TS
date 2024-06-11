package lotus.core.phases;

import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import org.junit.Before;
import org.junit.Test;

public class PhaseTest {

 private Game game;
 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase;

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
 }

 @Test
 public void testNextPhase_UntapPhase() {
 Phase currentPhase = untapPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(upkeepPhase, nextPhase);
 }

 @Test
 public void testNextPhase_UpkeepPhase() {
 Phase currentPhase = upkeepPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(drawPhase, nextPhase);
 }

 @Test
 public void testNextPhase_DrawPhase() {
 Phase currentPhase = drawPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(main1Phase, nextPhase);
 }

 @Test
 public void testNextPhase_Main1Phase() {
 Phase currentPhase = main1Phase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(combatBeginningPhase, nextPhase);
 }

 @Test
 public void testNextPhase_CombatBeginningPhase() {
 Phase currentPhase = combatBeginningPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(declareAttackersPhase, nextPhase);
 }

 @Test
 public void testNextPhase_DeclareAttackersPhase() {
 Phase currentPhase = declareAttackersPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(declareBlockersPhase, nextPhase);
 }

 @Test
 public void testNextPhase_DeclareBlockersPhase() {
 Phase currentPhase = declareBlockersPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(combatDamagePhase, nextPhase);
 }

 @Test
 public void testNextPhase_CombatDamagePhase() {
 Phase currentPhase = combatDamagePhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(combatEndPhase, nextPhase);
 }

 @Test
 public void testNextPhase_CombatEndPhase() {
 Phase currentPhase = combatEndPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(main2Phase, nextPhase);
 }

 @Test
 public void testNextPhase_Main2Phase() {
 Phase currentPhase = main2Phase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(endOfTurnPhase, nextPhase);
 }

 @Test
 public void testNextPhase_EndOfTurnPhase() {
 Phase currentPhase = endOfTurnPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(cleanupPhase, nextPhase);
 }

 @Test
 public void testNextPhase_CleanupPhase() {
 Phase currentPhase = cleanupPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(playerChangePhase, nextPhase);
 }

 @Test
 public void testNextPhase_PlayerChangePhase() {
 Phase currentPhase = playerChangePhase;
 game.setPlayingPlayer(1);
 Phase nextPhase = Phase.nextPhase();
 assertEquals(untapPhase, nextPhase);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_IllegalState() {
 Phase currentPhase = null;
 Phase nextPhase = Phase.nextPhase();
 fail("Next phase should throw an IllegalStateException when currentPhase is null");
 }
}