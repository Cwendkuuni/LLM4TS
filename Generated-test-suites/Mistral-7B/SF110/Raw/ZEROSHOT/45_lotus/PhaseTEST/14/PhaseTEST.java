package lotus.core.phases;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import lotus.core.Game;
import lotus.core.effect.ChangePhase;

public class PhaseTest {

 private Phase untapPhase;
 private Phase upkeepPhase;
 private Phase drawPhase;
 private Phase main1Phase;
 private Phase combatBeginningPhase;
 private Phase declareAttackersPhase;
 private Phase declareBlockersPhase;
 private Phase combatDamagePhase;
 private Phase combatEndPhase;
 private Phase main2Phase;
 private Phase endOfTurnPhase;
 private Phase cleanupPhase;
 private Phase playerChangePhase;
 private Game game;

 @Before
 public void setup() {
 untapPhase = mock(UntapPhase.class);
 upkeepPhase = mock(UpkeepPhase.class);
 drawPhase = mock(DrawPhase.class);
 main1Phase = mock(Main1Phase.class);
 combatBeginningPhase = mock(CombatBeginningPhase.class);
 declareAttackersPhase = mock(DeclareAttackersPhase.class);
 declareBlockersPhase = mock(DeclareBlockersPhase.class);
 combatDamagePhase = mock(CombatDamagePhase.class);
 combatEndPhase = mock(CombatEndPhase.class);
 main2Phase = mock(Main2Phase.class);
 endOfTurnPhase = mock(EndOfTurnPhase.class);
 cleanupPhase = mock(CleanupPhase.class);
 playerChangePhase = mock(PlayerChangePhase.class);
 game = mock(Game.class);
 }

 @Test
 public void testNextPhaseUntap() {
     Phase.nextPhase(game, untapPhase);
     assertNotNull(Game.currentPhase);
     verify(game).currentPhase = untapPhase;
     verify(playerChangePhase).resolve();
 }

 @Test
 public void testNextPhaseUpkeep() {
     Game.currentPhase = untapPhase;
     Phase.nextPhase(game, upkeepPhase);
     assertNotNull(Game.currentPhase);
     verify(game).currentPhase = upkeepPhase;
     verify(upkeepPhase).doPhase();
 }

 @Test
 public void testNextPhaseDraw() {
     Game.currentPhase = upkeepPhase;
     Phase.nextPhase(game, drawPhase);
     assertNotNull(Game.currentPhase);
     verify(game).currentPhase = drawPhase;
     verify(drawPhase).doPhase();
 }

 @Test
 public void testNextPhaseMain1() {
     Game.currentPhase = drawPhase;
     Phase.nextPhase(game, main1Phase);
     assertNotNull(Game.currentPhase);
     verify(game).currentPhase = main1Phase;
     verify(main1Phase).doPhase();
 }

 @Test
 public void testNextPhaseCombatBeginning() {
     Game.currentPhase = main1Phase;
     Phase.nextPhase(game, combatBeginningPhase);
     assertNotNull(Game.currentPhase);
     verify(game).currentPhase = combatBeginningPhase;
     verify(combatBeginningPhase).doPhase();
 }

 @Test
 public void testNextPhaseDeclareAttackers() {
     Game.currentPhase = combatBeginningPhase;
     Phase.nextPhase(game, declareAttackersPhase);
     assertNotNull(Game.currentPhase);
     verify(game).currentPhase = declareAttackersPhase;
     verify(declareAttackersPhase).doPhase();
 }

 @Test
 public void testNextPhaseDeclareBlockers() {
     Game.currentPhase = declareAttackersPhase;
     Phase.nextPhase(game, declareBlockersPhase);
     assertNotNull(Game.currentPhase);
     verify(game).currentPhase = declareBlockersPhase;
     verify(declareBlockersPhase).doPhase();
 }

 @Test
 public void testNextPhaseCombatDamage() {
     Game.currentPhase = declareBlockersPhase;
     Phase.nextPhase(game, combatDamagePhase);
     assertNotNull(Game.currentPhase);
     verify(game).currentPhase = combatDamagePhase;
     verify(combatDamagePhase).doPhase();
 }

 @Test
 public void testNextPhaseCombatEnd() {
     Game.currentPhase = combatDamagePhase;
     Phase.nextPhase(game, combatEndPhase);
     assertNotNull(Game.currentPhase);
     verify(game).currentPhase = combatEndPhase;
     verify(combatEndPhase).doPhase();
 }

 @Test
 public void testNextPhaseMain2() {
     Game.currentPhase = combatEndPhase;
     Phase.nextPhase(game, main2Phase);
     assertNotNull(Game.currentPhase);
     verify(game).currentPhase = main2Phase;
     verify(main2Phase).doPhase();
 }

 @Test
 public void testNextPhaseEndOfTurn() {
     Game.currentPhase = main2Phase;
     Phase.nextPhase(game, endOfTurnPhase);
     assertNotNull(Game.currentPhase);
     verify(game).currentPhase = endOfTurnPhase;
     verify(endOfTurnPhase).doPhase();
 }

 @Test
 public void testNextPhaseCleanup() {
     Game.currentPhase = endOfTurnPhase;
     Phase.nextPhase(game, cleanupPhase);
     assertNotNull(Game.currentPhase);
     verify(game).currentPhase = cleanupPhase;
     verify(cleanupPhase).doPhase();
 }

 @Test
 public void testNextPhasePlayerChange() {
     Game.currentPhase = cleanupPhase;
     Phase.nextPhase(game, playerChangePhase);
     assertNotNull(Game.currentPhase);
     verify(game).currentPhase = playerChangePhase;
     verify(playerChangePhase).resolve();
 }
}