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
 public void testNextPhase_UntapPhase_UpkeepPhase() {
 game.setCurrentPhase(untapPhase);
 Phase nextPhase = Phase.nextPhase();
 assertEquals(upkeepPhase, nextPhase);
 }

 @Test
 public void testNextPhase_UpkeepPhase_DrawPhase() {
 game.setCurrentPhase(upkeepPhase);
 Phase nextPhase = Phase.nextPhase();
 assertEquals(drawPhase, nextPhase);
 }

 @Test
 public void testNextPhase_DrawPhase_Main1Phase() {
 game.setCurrentPhase(drawPhase);
 Phase nextPhase = Phase.nextPhase();
 assertEquals(main1Phase, nextPhase);
 }

 @Test
 public void testNextPhase_Main1Phase_CombatBeginningPhase() {
 game.setCurrentPhase(main1Phase);
 Phase nextPhase = Phase.nextPhase();
 assertEquals(combatBeginningPhase, nextPhase);
 }

 @Test
 public void testNextPhase_CombatBeginningPhase_DeclareAttackersPhase() {
 game.setCurrentPhase(combatBeginningPhase);
 Phase nextPhase = Phase.nextPhase();
 assertEquals(declareAttackersPhase, nextPhase);
 }

 @Test
 public void testNextPhase_DeclareAttackersPhase_DeclareBlockersPhase() {
 game.setCurrentPhase(declareAttackersPhase);
 Phase nextPhase = Phase.nextPhase();
 assertEquals(declareBlockersPhase, nextPhase);
 }

 @Test
 public void testNextPhase_DeclareBlockersPhase_CombatDamagePhase() {
 game.setCurrentPhase(declareBlockersPhase);
 Phase nextPhase = Phase.nextPhase();
 assertEquals(combatDamagePhase, nextPhase);
 }

 @Test
 public void testNextPhase_CombatDamagePhase_CombatEndPhase() {
 game.setCurrentPhase(combatDamagePhase);
 Phase nextPhase = Phase.nextPhase();
 assertEquals(combatEndPhase, nextPhase);
 }

 @Test
 public void testNextPhase_CombatEndPhase_Main2Phase() {
 game.setCurrentPhase(combatEndPhase);
 Phase nextPhase = Phase.nextPhase();
 assertEquals(main2Phase, nextPhase);
 }

 @Test
 public void testNextPhase_Main2Phase_EndOfTurnPhase() {
 game.setCurrentPhase(main2Phase);
 Phase nextPhase = Phase.nextPhase();
 assertEquals(endOfTurnPhase, nextPhase);
 }

 @Test
 public void testNextPhase_EndOfTurnPhase_CleanupPhase() {
 game.setCurrentPhase(endOfTurnPhase);
 Phase nextPhase = Phase.nextPhase();
 assertEquals(cleanupPhase, nextPhase);
 }

 @Test
 public void testNextPhase_CleanupPhase_PlayerChangePhase() {
 game.setCurrentPhase(cleanupPhase);
 game.getGameState().setPlayingPlayer(1);
 Phase nextPhase = Phase.nextPhase();
 assertEquals(playerChangePhase, nextPhase);
 }

 @Test
 public void testNextPhase_PlayerChangePhase_UntapPhase() {
 game.setCurrentPhase(playerChangePhase);
 Phase nextPhase = Phase.nextPhase();
 assertEquals(untapPhase, nextPhase);
 }
}