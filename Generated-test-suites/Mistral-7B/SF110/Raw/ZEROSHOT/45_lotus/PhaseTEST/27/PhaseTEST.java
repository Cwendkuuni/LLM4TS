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
 public void testNextPhase_Untap() {
 assertEquals(Game.Phase.Untap, game.getPhase());
 game.nextPhase();
 assertEquals(Game.Phase.Upkeep, game.getPhase());
 }

 @Test
 public void testNextPhase_Upkeep() {
 assertEquals(Game.Phase.Upkeep, game.getPhase());
 game.nextPhase();
 assertEquals(Game.Phase.Draw, game.getPhase());
 }

 @Test
 public void testNextPhase_Draw() {
 assertEquals(Game.Phase.Draw, game.getPhase());
 game.nextPhase();
 assertEquals(Game.Phase.Main1, game.getPhase());
 }

 @Test
 public void testNextPhase_Main1() {
 assertEquals(Game.Phase.Main1, game.getPhase());
 game.nextPhase();
 assertEquals(Game.Phase.CombatBeginning, game.getPhase());
 }

 @Test
 public void testNextPhase_CombatBeginning() {
 assertEquals(Game.Phase.CombatBeginning, game.getPhase());
 game.nextPhase();
 assertEquals(Game.Phase.DeclareAttackers, game.getPhase());
 }

 @Test
 public void testNextPhase_DeclareAttackers() {
 assertEquals(Game.Phase.DeclareAttackers, game.getPhase());
 game.nextPhase();
 assertEquals(Game.Phase.DeclareBlockers, game.getPhase());
 }

 @Test
 public void testNextPhase_DeclareBlockers() {
 assertEquals(Game.Phase.DeclareBlockers, game.getPhase());
 game.nextPhase();
 assertEquals(Game.Phase.CombatDamage, game.getPhase());
 }

 @Test
 public void testNextPhase_CombatDamage() {
 assertEquals(Game.Phase.CombatDamage, game.getPhase());
 game.nextPhase();
 assertEquals(Game.Phase.CombatEnd, game.getPhase());
 }

 @Test
 public void testNextPhase_CombatEnd() {
 assertEquals(Game.Phase.CombatEnd, game.getPhase());
 game.nextPhase();
 assertEquals(Game.Phase.Main2, game.getPhase());
 }

 @Test
 public void testNextPhase_Main2() {
 assertEquals(Game.Phase.Main2, game.getPhase());
 game.nextPhase();
 assertEquals(Game.Phase.EndOfTurn, game.getPhase());
 }

 @Test
 public void testNextPhase_EndOfTurn() {
 assertEquals(Game.Phase.EndOfTurn, game.getPhase());
 game.nextPhase();
 assertEquals(Game.Phase.Cleanup, game.getPhase());
 }

 @Test
 public void testNextPhase_Cleanup() {
 assertEquals(Game.Phase.Cleanup, game.getPhase());
 game.nextPhase();
 assertEquals(Game.Phase.PlayerChange, game.getPhase());
 }

 @Test
 public void testNextPhase_PlayerChange() {
 assertEquals(Game.Phase.PlayerChange, game.getPhase());
 game.nextPhase();
 assertEquals(Game.Phase.Untap, game.getPhase());
 }
}