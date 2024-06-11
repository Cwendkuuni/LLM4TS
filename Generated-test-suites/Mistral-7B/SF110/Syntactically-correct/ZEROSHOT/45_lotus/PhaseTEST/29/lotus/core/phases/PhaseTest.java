import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import lotus.core.Game;
import lotus.core.effect.ChangePhase;
import lotus.core.phases.Phase;
import lotus.core.phases.DrawPhase;
import lotus.core.phases.Main1Phase;
import lotus.core.phases.UntapPhase;

import static org.junit.Assert.assertEquals;

public class PhaseTest {

 private Game game;
 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase, untapPhaseSpy;

 @Before
 public void setUp() {
 game = Mockito.spy(new Game());
 untapPhase = Mockito.spy(new UntapPhase());
 upkeepPhase = Mockito.spy(new UpkeepPhase());
 drawPhase = Mockito.spy(new DrawPhase());
 main1Phase = Mockito.spy(new Main1Phase());
 combatBeginningPhase = Mockito.spy(new CombatBeginningPhase());
 declareAttackersPhase = Mockito.spy(new DeclareAttackersPhase());
 declareBlockersPhase = Mockito.spy(new DeclareBlockersPhase());
 combatDamagePhase = Mockito.spy(new CombatDamagePhase());
 combatEndPhase = Mockito.spy(new CombatEndPhase());
 main2Phase = Mockito.spy(new Main2Phase());
 endOfTurnPhase = Mockito.spy(new EndOfTurnPhase());
 cleanupPhase = Mockito.spy(new CleanupPhase());
 playerChangePhase = Mockito.spy(new PlayerChangePhase());

 untapPhaseSpy = Mockito.spy(new UntapPhase());

 Mockito.doNothing().when(game).currentPhase = untapPhase;
 Mockito.doNothing().when(game).currentPhase = upkeepPhase;
 Mockito.doNothing().when(game).currentPhase = drawPhase;
 Mockito.doNothing().when(game).currentPhase = main1Phase;
 Mockito.doNothing().when(game).currentPhase = combatBeginningPhase;
 Mockito.doNothing().when(game).currentPhase = declareAttackersPhase;
 Mockito.doNothing().when(game).currentPhase = declareBlockersPhase;
 Mockito.doNothing().when(game).currentPhase = combatDamagePhase;
 Mockito.doNothing().when(game).currentPhase = combatEndPhase;
 Mockito.doNothing().when(game).currentPhase = main2Phase;
 Mockito.doNothing().when(game).currentPhase = endOfTurnPhase;
 Mockito.doNothing().when(game).currentPhase = cleanupPhase;
 Mockito.doNothing().when(game).currentPhase = playerChangePhase;
 }

 @Test
 public void testNextPhaseUntapPhase() {
 game.currentPhase = untapPhase;
 Phase.nextPhase(game);
 assertEquals(upkeepPhase, game.currentPhase);
 }

 @Test
 public void testNextPhaseUpkeepPhase() {
 game.currentPhase = upkeepPhase;
 Phase.nextPhase(game);
 assertEquals(drawPhase, game.currentPhase);
 }

 @Test
 public void testNextPhaseDrawPhase() {
 game.currentPhase = drawPhase;
 Phase.nextPhase(game);
 assertEquals(main1Phase, game.currentPhase);
 }

 @Test
 public void testNextPhaseMain1Phase() {
 game.currentPhase = main1Phase;
 Phase.nextPhase(game);
 assertEquals(combatBeginningPhase, game.currentPhase);
 }

 @Test
 public void testNextPhaseCombatBeginningPhase() {
 game.currentPhase = combatBeginningPhase;
 Phase.nextPhase(game);
 assertEquals(declareAttackersPhase, game.currentPhase);
 }

 @Test
 public void testNextPhaseDeclareAttackersPhase() {
 game.currentPhase = declareAttackersPhase;
 Phase.nextPhase(game);
 assertEquals(declareBlockersPhase, game.currentPhase);
 }

 @Test
 public void testNextPhaseDeclareBlockersPhase() {
 game.currentPhase = declareBlockersPhase;
 Phase.nextPhase(game);
 assertEquals(combatDamagePhase, game.currentPhase);
 }

 @Test
 public void testNextPhaseCombatDamagePhase() {
 game.currentPhase = combatDamagePhase;
 Phase.nextPhase(game);
 assertEquals(combatEndPhase, game.currentPhase);
 }

 @Test
 public void testNextPhaseCombatEndPhase() {
 game.currentPhase = combatEndPhase;
 Phase.nextPhase(game);
 assertEquals(main2Phase, game.currentPhase);
 }

 @Test
 public void testNextPhaseMain2Phase() {
 game.currentPhase = main2Phase;
 Phase.nextPhase(game);
 assertEquals(endOfTurnPhase, game.currentPhase);
 }

 @Test
 public void testNextPhaseEndOfTurnPhase() {
 game.currentPhase = endOfTurnPhase;
 Phase.nextPhase(game);
 assertEquals(cleanupPhase, game.currentPhase);
 }

 @Test
 public void testNextPhaseCleanupPhase() {
 game.currentPhase = cleanupPhase;
 Phase.nextPhase(game);
 assertEquals(playerChangePhase, game.currentPhase);
 }

 @Test
 public void testNextPhasePlayerChangePhase() {
 game.currentPhase = playerChangePhase;
 Phase.nextPhase(game);
 assertEquals(untapPhase, game.currentPhase);
 }
}