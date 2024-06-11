package lotus.core.phases;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

 private Game game;
 private Player player1, player2;
 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase;

 @Before
 public void setUp() {
 game = new Game();
 player1 = new Player("Player1", game);
 player2 = new Player("Player2", game);
 game.addPlayer(player1);
 game.addPlayer(player2);
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
 public void testNextPhaseUntapPhase() {
 Phase currentPhase = untapPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(upkeepPhase, nextPhase);
 game.setCurrentPhase(currentPhase);
 game.nextPhase();
 assertEquals(upkeepPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhaseUpkeepPhase() {
 Phase currentPhase = upkeepPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(drawPhase, nextPhase);
 game.setCurrentPhase(currentPhase);
 game.nextPhase();
 assertEquals(drawPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhaseDrawPhase() {
 Phase currentPhase = drawPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(main1Phase, nextPhase);
 game.setCurrentPhase(currentPhase);
 game.nextPhase();
 assertEquals(main1Phase, Game.currentPhase);
 }

 @Test
 public void testNextPhaseMain1Phase() {
 Phase currentPhase = main1Phase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(combatBeginningPhase, nextPhase);
 game.setCurrentPhase(currentPhase);
 game.nextPhase();
 assertEquals(combatBeginningPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhaseCombatBeginningPhase() {
 Phase currentPhase = combatBeginningPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(declareAttackersPhase, nextPhase);
 game.setCurrentPhase(currentPhase);
 game.nextPhase();
 assertEquals(declareAttackersPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhaseDeclareAttackersPhase() {
 Phase currentPhase = declareAttackersPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(declareBlockersPhase, nextPhase);
 game.setCurrentPhase(currentPhase);
 game.nextPhase();
 assertEquals(declareBlockersPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhaseDeclareBlockersPhase() {
 Phase currentPhase = declareBlockersPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(combatDamagePhase, nextPhase);
 game.setCurrentPhase(currentPhase);
 game.nextPhase();
 assertEquals(combatDamagePhase, Game.currentPhase);
 }

 @Test
 public void testNextPhaseCombatDamagePhase() {
 Phase currentPhase = combatDamagePhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(combatEndPhase, nextPhase);
 game.setCurrentPhase(currentPhase);
 game.nextPhase();
 assertEquals(combatEndPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhaseCombatEndPhase() {
 Phase currentPhase = combatEndPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(main2Phase, nextPhase);
 game.setCurrentPhase(currentPhase);
 game.nextPhase();
 assertEquals(main2Phase, Game.currentPhase);
 }

 @Test
 public void testNextPhaseMain2Phase() {
 Phase currentPhase = main2Phase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(endOfTurnPhase, nextPhase);
 game.setCurrentPhase(currentPhase);
 game.nextPhase();
 assertEquals(endOfTurnPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhaseEndOfTurnPhase() {
 Phase currentPhase = endOfTurnPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(cleanupPhase, nextPhase);
 game.setCurrentPhase(currentPhase);
 game.nextPhase();
 assertEquals(cleanupPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhaseCleanupPhase() {
 Phase currentPhase = cleanupPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(playerChangePhase, nextPhase);
 game.setCurrentPhase(currentPhase);
 game.nextPhase();
 assertEquals(playerChangePhase, Game.currentPhase);
 }

 @Test
 public void testNextPhasePlayerChangePhase() {
 Phase currentPhase = playerChangePhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(untapPhase, nextPhase);
 game.setCurrentPhase(currentPhase);
 game.nextPhase();
 assertEquals(untapPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsEndOfTurnPhase() {
 Phase currentPhase = endOfTurnPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(playerChangePhase, nextPhase);
 game.setCurrentPhase(currentPhase);
 game.nextPhase();
 assertEquals(playerChangePhase, Game.currentPhase);
 game.nextPhase();
 assertEquals(untapPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhaseWhenCurrentPhaseIsPlayerChangePhase() {
 Phase currentPhase = playerChangePhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(untapPhase, nextPhase);
 game.setCurrentPhase(currentPhase);
 game.nextPhase();
 assertEquals(untapPhase, Game.currentPhase);
 }
}