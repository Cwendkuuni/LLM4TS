package lotus.core.phases;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import lotus.core.*;
import lotus.core.effect.*;

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
 public void testNextPhaseUntapPhase() {
 Phase currentPhase = untapPhase;
 game.setCurrentPhase(currentPhase);
 Phase expectedPhase = upkeepPhase;
 game.nextPhase();
 assertEquals(expectedPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhaseUpkeepPhase() {
 Phase currentPhase = upkeepPhase;
 game.setCurrentPhase(currentPhase);
 Phase expectedPhase = drawPhase;
 game.nextPhase();
 assertEquals(expectedPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhaseDrawPhase() {
 Phase currentPhase = drawPhase;
 game.setCurrentPhase(currentPhase);
 Phase expectedPhase = main1Phase;
 game.nextPhase();
 assertEquals(expectedPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhaseMain1Phase() {
 Phase currentPhase = main1Phase;
 game.setCurrentPhase(currentPhase);
 Phase expectedPhase = combatBeginningPhase;
 game.nextPhase();
 assertEquals(expectedPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhaseCombatBeginningPhase() {
 Phase currentPhase = combatBeginningPhase;
 game.setCurrentPhase(currentPhase);
 Phase expectedPhase = declareAttackersPhase;
 game.nextPhase();
 assertEquals(expectedPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhaseDeclareAttackersPhase() {
 Phase currentPhase = declareAttackersPhase;
 game.setCurrentPhase(currentPhase);
 Phase expectedPhase = declareBlockersPhase;
 game.nextPhase();
 assertEquals(expectedPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhaseDeclareBlockersPhase() {
 Phase currentPhase = declareBlockersPhase;
 game.setCurrentPhase(currentPhase);
 Phase expectedPhase = combatDamagePhase;
 game.nextPhase();
 assertEquals(expectedPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhaseCombatDamagePhase() {
 Phase currentPhase = combatDamagePhase;
 game.setCurrentPhase(currentPhase);
 Phase expectedPhase = combatEndPhase;
 game.nextPhase();
 assertEquals(expectedPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhaseCombatEndPhase() {
 Phase currentPhase = combatEndPhase;
 game.setCurrentPhase(currentPhase);
 Phase expectedPhase = main2Phase;
 game.nextPhase();
 assertEquals(expectedPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhaseMain2Phase() {
 Phase currentPhase = main2Phase;
 game.setCurrentPhase(currentPhase);
 Phase expectedPhase = endOfTurnPhase;
 game.nextPhase();
 assertEquals(expectedPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhaseEndOfTurnPhase() {
 Phase currentPhase = endOfTurnPhase;
 game.setCurrentPhase(currentPhase);
 Phase expectedPhase = cleanupPhase;
 game.nextPhase();
 assertEquals(expectedPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhaseCleanupPhase() {
 Phase currentPhase = cleanupPhase;
 game.setCurrentPhase(currentPhase);
 Phase expectedPhase = playerChangePhase;
 game.nextPhase();
 assertEquals(expectedPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhasePlayerChangePhase() {
 Phase currentPhase = playerChangePhase;
 game.setCurrentPhase(currentPhase);
 int expectedPlayer = (game.getPlayingPlayer() + 1) % 2;
 game.nextPhase();
 assertEquals(expectedPlayer, game.getPlayingPlayer());
 Phase expectedCurrentPhase = untapPhase2;
 assertEquals(expectedCurrentPhase, game.getCurrentPhase());
 }
}