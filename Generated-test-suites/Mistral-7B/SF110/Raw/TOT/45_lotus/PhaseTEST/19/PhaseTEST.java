package lotus.core.phases;

import static org.junit.Assert.*;
import lotus.core.*;
import org.junit.Before;
import org.junit.Test;

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
 public void testNextPhase_UntapPhase_UpkeepPhase() {
 changePhases(untapPhase, upkeepPhase);
 assertEquals(upkeepPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_UpkeepPhase_DrawPhase() {
 changePhases(upkeepPhase, drawPhase);
 assertEquals(drawPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_DrawPhase_Main1Phase() {
 changePhases(drawPhase, main1Phase);
 assertEquals(main1Phase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_Main1Phase_CombatBeginningPhase() {
 changePhases(main1Phase, combatBeginningPhase);
 assertEquals(combatBeginningPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CombatBeginningPhase_DeclareAttackersPhase() {
 changePhases(combatBeginningPhase, declareAttackersPhase);
 assertEquals(declareAttackersPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_DeclareAttackersPhase_DeclareBlockersPhase() {
 changePhases(declareAttackersPhase, declareBlockersPhase);
 assertEquals(declareBlockersPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_DeclareBlockersPhase_CombatDamagePhase() {
 changePhases(declareBlockersPhase, combatDamagePhase);
 assertEquals(combatDamagePhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CombatDamagePhase_CombatEndPhase() {
 changePhases(combatDamagePhase, combatEndPhase);
 assertEquals(combatEndPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CombatEndPhase_Main2Phase() {
 changePhases(combatEndPhase, main2Phase);
 assertEquals(main2Phase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_Main2Phase_EndOfTurnPhase() {
 changePhases(main2Phase, endOfTurnPhase);
 assertEquals(endOfTurnPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_EndOfTurnPhase_CleanupPhase() {
 changePhases(endOfTurnPhase, cleanupPhase);
 assertEquals(cleanupPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CleanupPhase_PlayerChangePhase() {
 changePhases(cleanupPhase, playerChangePhase);
 assertEquals(playerChangePhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_PlayerChangePhase_UntapPhase() {
 changePhases(playerChangePhase, untapPhase2);
 assertEquals(untapPhase2, Game.currentPhase);
 }

 @Test
 public void testNextPhase_EmptyPhaseList() {
 Game.currentPhase = null;
 assertEquals(untapPhase, nextPhase());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextPhase_NullPhase() {
 Game.currentPhase = null;
 nextPhase();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhase_InvalidPhase() {
 game.currentPhase = new Object();
 nextPhase();
 }

 private void changePhases(Phase currentPhase, Phase nextPhase) {
 game.changePhase(currentPhase);
 Phase oldPhase = Game.currentPhase;
 game.changePhase(nextPhase);
 assertEquals(nextPhase, Game.currentPhase);
 assertEquals(currentPhase, oldPhase);
 }

 private Phase nextPhase() {
 Phase currentPhase = Game.currentPhase;
 game.nextPhase();
 return currentPhase;
 }
}