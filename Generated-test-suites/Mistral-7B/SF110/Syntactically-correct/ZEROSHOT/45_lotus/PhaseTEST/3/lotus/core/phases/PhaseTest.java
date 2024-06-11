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

     Phase.nextPhase();
     assertEquals(upkeepPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_UpkeepPhase_DrawPhase() {
     Phase untapPhase = new UntapPhase();
     Phase upkeepPhase = new UpkeepPhase();
     Phase drawPhase = new DrawPhase();

     game.setCurrentPhase(untapPhase);
     Phase.nextPhase();
     assertEquals(upkeepPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(drawPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_DrawPhase_Main1Phase() {
     Phase untapPhase = new UntapPhase();
     Phase upkeepPhase = new UpkeepPhase();
     Phase drawPhase = new DrawPhase();
     Phase main1Phase = new Main1Phase();

     game.setCurrentPhase(untapPhase);
     Phase.nextPhase();
     assertEquals(upkeepPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(drawPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(main1Phase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_Main1Phase_CombatBeginningPhase() {
     Phase untapPhase = new UntapPhase();
     Phase upkeepPhase = new UpkeepPhase();
     Phase drawPhase = new DrawPhase();
     Phase main1Phase = new Main1Phase();
     Phase combatBeginningPhase = new CombatBeginningPhase();

     game.setCurrentPhase(untapPhase);
     Phase.nextPhase();
     assertEquals(upkeepPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(drawPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(main1Phase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatBeginningPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_CombatBeginningPhase_DeclareAttackersPhase() {
     Phase untapPhase = new UntapPhase();
     Phase upkeepPhase = new UpkeepPhase();
     Phase drawPhase = new DrawPhase();
     Phase main1Phase = new Main1Phase();
     Phase combatBeginningPhase = new CombatBeginningPhase();
     Phase declareAttackersPhase = new DeclareAttackersPhase();

     game.setCurrentPhase(untapPhase);
     Phase.nextPhase();
     assertEquals(upkeepPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(drawPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(main1Phase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatBeginningPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareAttackersPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_DeclareAttackersPhase_DeclareBlockersPhase() {
     Phase untapPhase = new UntapPhase();
     Phase upkeepPhase = new UpkeepPhase();
     Phase drawPhase = new DrawPhase();
     Phase main1Phase = new Main1Phase();
     Phase combatBeginningPhase = new CombatBeginningPhase();
     Phase declareAttackersPhase = new DeclareAttackersPhase();
     Phase declareBlockersPhase = new DeclareBlockersPhase();

     game.setCurrentPhase(untapPhase);
     Phase.nextPhase();
     assertEquals(upkeepPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(drawPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(main1Phase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatBeginningPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareAttackersPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareBlockersPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_DeclareBlockersPhase_CombatDamagePhase() {
     Phase untapPhase = new UntapPhase();
     Phase upkeepPhase = new UpkeepPhase();
     Phase drawPhase = new DrawPhase();
     Phase main1Phase = new Main1Phase();
     Phase combatBeginningPhase = new CombatBeginningPhase();
     Phase declareAttackersPhase = new DeclareAttackersPhase();
     Phase declareBlockersPhase = new DeclareBlockersPhase();
     Phase combatDamagePhase = new CombatDamagePhase();

     game.setCurrentPhase(untapPhase);
     Phase.nextPhase();
     assertEquals(upkeepPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(drawPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(main1Phase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatBeginningPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareAttackersPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareBlockersPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatDamagePhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_CombatDamagePhase_CombatEndPhase() {
     Phase untapPhase = new UntapPhase();
     Phase upkeepPhase = new UpkeepPhase();
     Phase drawPhase = new DrawPhase();
     Phase main1Phase = new Main1Phase();
     Phase combatBeginningPhase = new CombatBeginningPhase();
     Phase declareAttackersPhase = new DeclareAttackersPhase();
     Phase declareBlockersPhase = new DeclareBlockersPhase();
     Phase combatDamagePhase = new CombatDamagePhase();
     Phase combatEndPhase = new CombatEndPhase();

     game.setCurrentPhase(untapPhase);
     Phase.nextPhase();
     assertEquals(upkeepPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(drawPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(main1Phase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatBeginningPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareAttackersPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareBlockersPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatDamagePhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatEndPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_CombatEndPhase_Main2Phase() {
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

     game.setCurrentPhase(untapPhase);
     Phase.nextPhase();
     assertEquals(upkeepPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(drawPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(main1Phase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatBeginningPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareAttackersPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareBlockersPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatDamagePhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatEndPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(main2Phase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_Main2Phase_EndOfTurnPhase() {
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

     game.setCurrentPhase(untapPhase);
     Phase.nextPhase();
     assertEquals(upkeepPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(drawPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(main1Phase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatBeginningPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareAttackersPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareBlockersPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatDamagePhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatEndPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(main2Phase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(endOfTurnPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_EndOfTurnPhase_CleanupPhase() {
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
     Phase cleanupPhase = new CleanupPhase();

     game.setCurrentPhase(untapPhase);
     Phase.nextPhase();
     assertEquals(upkeepPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(drawPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(main1Phase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatBeginningPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareAttackersPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareBlockersPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatDamagePhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatEndPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(main2Phase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(endOfTurnPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(cleanupPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_CleanupPhase_PlayerChangePhase() {
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
     Phase cleanupPhase = new CleanupPhase();
     Phase playerChangePhase = new PlayerChangePhase();

     game.setCurrentPhase(untapPhase);
     Phase.nextPhase();
     assertEquals(upkeepPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(drawPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(main1Phase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatBeginningPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareAttackersPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareBlockersPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatDamagePhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatEndPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(main2Phase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(endOfTurnPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(cleanupPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(playerChangePhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_PlayerChangePhase_UntapPhase() {
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
     Phase cleanupPhase = new CleanupPhase();
     Phase playerChangePhase = new PlayerChangePhase();
     Phase untapPhase2 = new UntapPhase();

     game.setCurrentPhase(untapPhase);
     Phase.nextPhase();
     assertEquals(upkeepPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(drawPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(main1Phase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatBeginningPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareAttackersPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(declareBlockersPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatDamagePhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(combatEndPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(main2Phase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(endOfTurnPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(cleanupPhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(playerChangePhase, game.getCurrentPhase());
     Phase.nextPhase();
     assertEquals(untapPhase2, game.getCurrentPhase());
 }
}