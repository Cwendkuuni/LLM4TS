package lotus.core.phases;

import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.*;
import lotus.core.effect.*;

import org.junit.Before;
import org.junit.Test;

public class PhaseTest {

 private Game game;

 @Before
 public void setUp() {
 game = new Game();
 }

 @Test
 public void testNextPhase_UntapPhaseToUpkeepPhase() {
 Phase currentPhase = new UntapPhase();
 Phase nextPhase = new UpkeepPhase();

 Phase.nextPhase();
 assertEquals(nextPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_UpkeepPhaseToDrawPhase() {
 Phase currentPhase = new UpkeepPhase();
 Phase nextPhase = new DrawPhase();

 Phase.nextPhase();
 assertEquals(nextPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_DrawPhaseToMain1Phase() {
 Phase currentPhase = new DrawPhase();
 Phase nextPhase = new Main1Phase();

 Phase.nextPhase();
 assertEquals(nextPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_Main1PhaseToCombatBeginningPhase() {
 Phase currentPhase = new Main1Phase();
 Phase nextPhase = new CombatBeginningPhase();

 Phase.nextPhase();
 assertEquals(nextPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_CombatBeginningPhaseToDeclareAttackersPhase() {
 Phase currentPhase = new CombatBeginningPhase();
 Phase nextPhase = new DeclareAttackersPhase();

 Phase.nextPhase();
 assertEquals(nextPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_DeclareAttackersPhaseToDeclareBlockersPhase() {
 Phase currentPhase = new DeclareAttackersPhase();
 Phase nextPhase = new DeclareBlockersPhase();

 Phase.nextPhase();
 assertEquals(nextPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_DeclareBlockersPhaseToCombatDamagePhase() {
 Phase currentPhase = new DeclareBlockersPhase();
 Phase nextPhase = new CombatDamagePhase();

 Phase.nextPhase();
 assertEquals(nextPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_CombatDamagePhaseToCombatEndPhase() {
 Phase currentPhase = new CombatDamagePhase();
 Phase nextPhase = new CombatEndPhase();

 Phase.nextPhase();
 assertEquals(nextPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_CombatEndPhaseToMain2Phase() {
 Phase currentPhase = new CombatEndPhase();
 Phase nextPhase = new Main2Phase();

 Phase.nextPhase();
 assertEquals(nextPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_Main2PhaseToEndOfTurnPhase() {
 Phase currentPhase = new Main2Phase();
 Phase nextPhase = new EndOfTurnPhase();

 Phase.nextPhase();
 assertEquals(nextPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_EndOfTurnPhaseToCleanupPhase() {
 Phase currentPhase = new EndOfTurnPhase();
 Phase nextPhase = new CleanupPhase();

 Phase.nextPhase();
 assertEquals(nextPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_CleanupPhaseToPlayerChangePhase() {
 Phase currentPhase = new CleanupPhase();
 Phase nextPhase = new PlayerChangePhase();

 Phase.nextPhase();
 assertEquals(nextPhase, game.getCurrentPhase());
 }

 @Test
 public void testNextPhase_PlayerChangePhaseToUntapPhase() {
 Phase currentPhase = new PlayerChangePhase();
 Phase nextPhase = new UntapPhase();

 Phase.nextPhase();
 assertEquals(nextPhase, game.getCurrentPhase());
 }

 @Test
 public void testChangePhase() {
 Phase oldPhase = new UntapPhase();
 Phase newPhase = new UpkeepPhase();

 Phase.changePhase(oldPhase);
 assertEquals(newPhase, game.getCurrentPhase());
 }
}