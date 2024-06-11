package lotus.core.phases;

import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;

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
 public void testNextPhase_UntapPhase_to_UpkeepPhase() {
 changePhases(untapPhase, upkeepPhase);
 assertEquals(upkeepPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_UpkeepPhase_to_DrawPhase() {
 changePhases(upkeepPhase, drawPhase);
 assertEquals(drawPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_DrawPhase_to_Main1Phase() {
 changePhases(drawPhase, main1Phase);
 assertEquals(main1Phase, Game.currentPhase);
 }

 // Add test cases for the remaining methods here

 private void changePhases(Phase oldPhase, Phase newPhase) {
 Phase oldCurrentPhase = Game.currentPhase;
 game.nextPhase();
 assertEquals(oldCurrentPhase, oldPhase);
 assertEquals(newPhase, Game.currentPhase);
 }

}