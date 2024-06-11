package lotus.core.phases;

import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import org.junit.Test;

public class PhaseTest {

 private Game game = new Game();

 @Test
 public void testNextPhase_UntapPhase_UpkeepPhase() {
     Phase untapPhase = new UntapPhase();
     Phase upkeepPhase = new UpkeepPhase();
     game.currentPhase = untapPhase;
     Phase.nextPhase();
     assertEquals(upkeepPhase, game.currentPhase);
 }

 @Test
 public void testNextPhase_UpkeepPhase_DrawPhase() {
     Phase upkeepPhase = new UpkeepPhase();
     Phase drawPhase = new DrawPhase();
     game.currentPhase = upkeepPhase;
     Phase.nextPhase();
     assertEquals(drawPhase, game.currentPhase);
 }

 @Test
 public void testNextPhase_DrawPhase_Main1Phase() {
     Phase drawPhase = new DrawPhase();
     Phase main1Phase = new Main1Phase();
     game.currentPhase = drawPhase;
     Phase.nextPhase();
     assertEquals(main1Phase, game.currentPhase);
 }

 @Test
 public void testNextPhase_Main1Phase_CombatBeginningPhase() {
     Phase main1Phase = new Main1Phase();
     Phase combatBeginningPhase = new CombatBeginningPhase();
     game.currentPhase = main1Phase;
     Phase.nextPhase();
     assertEquals(combatBeginningPhase, game.currentPhase);
 }

 @Test
 public void testNextPhase_CombatBeginningPhase_DeclareAttackersPhase() {
     Phase combatBeginningPhase = new CombatBeginningPhase();
     Phase declareAttackersPhase = new DeclareAttackersPhase();
     game.currentPhase = combatBeginningPhase;
     Phase.nextPhase();
     assertEquals(declareAttackersPhase, game.currentPhase);
 }

 @Test
 public void testNextPhase_DeclareAttackersPhase_DeclareBlockersPhase() {
     Phase declareAttackersPhase = new DeclareAttackersPhase();
     Phase declareBlockersPhase = new DeclareBlockersPhase();
     game.currentPhase = declareAttackersPhase;
     Phase.nextPhase();
     assertEquals(declareBlockersPhase, game.currentPhase);
 }

 @Test
 public void testNextPhase_DeclareBlockersPhase_CombatDamagePhase() {
     Phase declareBlockersPhase = new DeclareBlockersPhase();
     Phase combatDamagePhase = new CombatDamagePhase();
     game.currentPhase = declareBlockersPhase;
     Phase.nextPhase();
     assertEquals(combatDamagePhase, game.currentPhase);
 }

 @Test
 public void testNextPhase_CombatDamagePhase_CombatEndPhase() {
     Phase combatDamagePhase = new CombatDamagePhase();
     Phase combatEndPhase = new CombatEndPhase();
     game.currentPhase = combatDamagePhase;
     Phase.nextPhase();
     assertEquals(combatEndPhase, game.currentPhase);
 }

 @Test
 public void testNextPhase_CombatEndPhase_Main2Phase() {
     Phase combatEndPhase = new CombatEndPhase();
     Phase main2Phase = new Main2Phase();
     game.currentPhase = combatEndPhase;
     Phase.nextPhase();
     assertEquals(main2Phase, game.currentPhase);
 }

 @Test
 public void testNextPhase_Main2Phase_EndOfTurnPhase() {
     Phase main2Phase = new Main2Phase();
     Phase endOfTurnPhase = new EndOfTurnPhase();
     game.currentPhase = main2Phase;
     Phase.nextPhase();
     assertEquals(endOfTurnPhase, game.currentPhase);
 }

 @Test
 public void testNextPhase_EndOfTurnPhase_CleanupPhase() {
     Phase endOfTurnPhase = new EndOfTurnPhase();
     Phase cleanupPhase = new CleanupPhase();
     game.currentPhase = endOfTurnPhase;
     Phase.nextPhase();
     assertEquals(cleanupPhase, game.currentPhase);
 }

 @Test
 public void testNextPhase_CleanupPhase_PlayerChangePhase() {
     Phase cleanupPhase = new CleanupPhase();
     Phase playerChangePhase = new PlayerChangePhase();
     game.currentPhase = cleanupPhase;
     Phase.nextPhase();
     assertEquals(playerChangePhase, game.currentPhase);
 }

 @Test
 public void testNextPhase_PlayerChangePhase_UntapPhase() {
     Phase playerChangePhase = new PlayerChangePhase();
     Phase untapPhase = new UntapPhase();
     game.currentPhase = playerChangePhase;
     Phase.nextPhase();
     assertEquals(untapPhase, game.currentPhase);
 }
}