package lotus.core.phases;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;

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
 public void testNextPhase() {
 Phase[] phases = {untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase, untapPhase};

 for (int i = 0; i < phases.length; i++) {
 Phase currentPhase = phases[i];
 Phase nextPhase = null;

 game.setCurrentPhase(currentPhase);
 nextPhase = Phase.nextPhase();

 assertEquals("Unexpected phase after " + currentPhase.getClass().getName(), phases[i + 1].getClass().getName(), nextPhase.getClass().getName());
 }
 }

 @Test
 public void testChangePhase() {
 Phase currentPhase = upkeepPhase;
 Phase expectedPhase = drawPhase;

 game.setCurrentPhase(currentPhase);
 Phase.changePhase(expectedPhase);

 assertEquals("Unexpected phase after change to " + expectedPhase.getClass().getName(), expectedPhase, game.getCurrentPhase());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChangePhaseToInvalidPhase() {
 Phase currentPhase = combatEndPhase;
 Phase expectedPhase = null;

 game.setCurrentPhase(currentPhase);
 Phase.changePhase(expectedPhase);
 }

 @Test
 public void testPlayerChange() {
 Phase currentPhase = main2Phase;
 int playingPlayerBefore = Game.getPlayingPlayer();

 game.setCurrentPhase(currentPhase);
 game.nextPhase();
 game.nextPhase();
 int playingPlayerAfter = Game.getPlayingPlayer();

 assertEquals("Unexpected player after change", (playingPlayerBefore % 2 + 1), playingPlayerAfter);
 }

}