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
 public void testNextPhase_UntapToUpkeep() {
 assertEquals(Game.Phase.Untap, game.getCurrentPhase());
 game.nextPhase();
 assertEquals(Game.Phase.Upkeep, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_UpkeepToDraw() {
 assertEquals(Game.Phase.Upkeep, game.getCurrentPhase());
 game.nextPhase();
 assertEquals(Game.Phase.Draw, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_DrawToMain1() {
 assertEquals(Game.Phase.Draw, game.getCurrentPhase());
 game.nextPhase();
 assertEquals(Game.Phase.Main1, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_Main1ToCombatBeginning() {
 assertEquals(Game.Phase.Main1, game.getCurrentPhase());
 game.nextPhase();
 assertEquals(Game.Phase.CombatBeginning, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_CombatBeginningToDeclareAttackers() {
 assertEquals(Game.Phase.CombatBeginning, game.getCurrentPhase());
 game.nextPhase();
 assertEquals(Game.Phase.DeclareAttackers, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_DeclareAttackersToDeclareBlockers() {
 assertEquals(Game.Phase.DeclareAttackers, game.getCurrentPhase());
 game.nextPhase();
 assertEquals(Game.Phase.DeclareBlockers, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_DeclareBlockersToCombatDamage() {
 assertEquals(Game.Phase.DeclareBlockers, game.getCurrentPhase());
 game.nextPhase();
 assertEquals(Game.Phase.CombatDamage, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_CombatDamageToCombatEnd() {
 assertEquals(Game.Phase.CombatDamage, game.getCurrentPhase());
 game.nextPhase();
 assertEquals(Game.Phase.CombatEnd, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_CombatEndToMain2() {
 assertEquals(Game.Phase.CombatEnd, game.getCurrentPhase());
 game.nextPhase();
 assertEquals(Game.Phase.Main2, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_Main2ToEndOfTurn() {
 assertEquals(Game.Phase.Main2, game.getCurrentPhase());
 game.nextPhase();
 assertEquals(Game.Phase.EndOfTurn, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_EndOfTurnToCleanup() {
 assertEquals(Game.Phase.EndOfTurn, game.getCurrentPhase());
 game.nextPhase();
 assertEquals(Game.Phase.Cleanup, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_CleanupToPlayerChange() {
 assertEquals(Game.Phase.Cleanup, game.getCurrentPhase());
 game.nextPhase();
 assertEquals(Game.Phase.PlayerChange, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_PlayerChangeToUntap() {
 assertEquals(Game.Phase.PlayerChange, game.getCurrentPhase());
 game.nextPhase();
 assertEquals(Game.Phase.Untap, game.getCurrentPhase());
 }
}