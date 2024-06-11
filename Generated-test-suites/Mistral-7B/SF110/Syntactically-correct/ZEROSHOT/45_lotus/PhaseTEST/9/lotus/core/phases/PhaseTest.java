package lotus.core.phases;

import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.*;
import lotus.core.effect.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PhaseTest {

 private Game game;

 @Before
 public void setUp() {
 game = new Game();
 }

 @After
 public void tearDown() {
 game = null;
 }

 @Test
 public void testNextPhase_UntapPhaseToUpkeepPhase() {
 Phase currentPhase = game.getCurrentPhase();
 assertInstanceOf(UntapPhase.class, currentPhase);

 Phase nextPhase = Phase.nextPhase();
 assertInstanceOf(UpkeepPhase.class, nextPhase);

 assertEquals(UpkeepPhase.class, game.getCurrentPhase().getClass());
 }

 @Test
 public void testNextPhase_UpkeepPhaseToDrawPhase() {
 Phase currentPhase = Phase.nextPhase();
 assertInstanceOf(UpkeepPhase.class, currentPhase);

 currentPhase = game.getCurrentPhase();

 Phase nextPhase = Phase.nextPhase();
 assertInstanceOf(DrawPhase.class, nextPhase);

 assertEquals(DrawPhase.class, game.getCurrentPhase().getClass());
 }

 @Test
 public void testNextPhase_DrawPhaseToMain1Phase() {
 Phase currentPhase = Phase.nextPhase();
 assertInstanceOf(DrawPhase.class, currentPhase);

 currentPhase = game.getCurrentPhase();

 Phase nextPhase = Phase.nextPhase();
 assertInstanceof(Main1Phase.class, nextPhase);

 assertEquals(Main1Phase.class, game.getCurrentPhase().getClass());
 }

 @Test
 public void testNextPhase_Main1PhaseToCombatBeginningPhase() {
 Phase currentPhase = Phase.nextPhase();
 assertInstanceof(Main1Phase.class, currentPhase);

 currentPhase = game.getCurrentPhase();

 Phase nextPhase = Phase.nextPhase();
 assertInstanceOf(CombatBeginningPhase.class, nextPhase);

 assertEquals(CombatBeginningPhase.class, game.getCurrentPhase().getClass());
 }

 @Test
 public void testNextPhase_CombatBeginningPhaseToDeclareAttackersPhase() {
 Phase currentPhase = Phase.nextPhase();
 assertInstanceOf(CombatBeginningPhase.class, currentPhase);

 currentPhase = game.getCurrentPhase();

 Phase nextPhase = Phase.nextPhase();
 assertInstanceof(DeclareAttackersPhase.class, nextPhase);

 assertEquals(DeclareAttackersPhase.class, game.getCurrentPhase().getClass());
 }

 @Test
 public void testNextPhase_DeclareAttackersPhaseToDeclareBlockersPhase() {
 Phase currentPhase = Phase.nextPhase();
 assertInstanceof(DeclareAttackersPhase.class, currentPhase);

 currentPhase = game.getCurrentPhase();

 Phase nextPhase = Phase.nextPhase();
 assertInstanceOf(DeclareBlockersPhase.class, nextPhase);

 assertEquals(DeclareBlockersPhase.class, game.getCurrentPhase().getClass());
 }

 @Test
 public void testNextPhase_DeclareBlockersPhaseToCombatDamagePhase() {
 Phase currentPhase = Phase.nextPhase();
 assertInstanceof(DeclareBlockersPhase.class, currentPhase);

 currentPhase = game.getCurrentPhase();

 Phase nextPhase = Phase.nextPhase();
 assertInstanceOf(CombatDamagePhase.class, nextPhase);

 assertEquals(CombatDamagePhase.class, game.getCurrentPhase().getClass());
 }

 @Test
 public void testNextPhase_CombatDamagePhaseToCombatEndPhase() {
 Phase currentPhase = Phase.nextPhase();
 assertInstanceOf(CombatDamagePhase.class, currentPhase);

 currentPhase = game.getCurrentPhase();

 Phase nextPhase = Phase.nextPhase();
 assertInstanceOf(CombatEndPhase.class, nextPhase);

 assertEquals(CombatEndPhase.class, game.getCurrentPhase().getClass());
 }

 @Test
 public void testNextPhase_CombatEndPhaseToMain2Phase() {
 Phase currentPhase = Phase.nextPhase();
 assertInstanceOf(CombatEndPhase.class, currentPhase);

 currentPhase = game.getCurrentPhase();

 Phase nextPhase = Phase.nextPhase();
 assertInstanceOf(Main2Phase.class, nextPhase);

 assertEquals(Main2Phase.class, game.getCurrentPhase().getClass());
 }

 @Test
 public void testNextPhase_Main2PhaseToEndOfTurnPhase() {
 Phase currentPhase = Phase.nextPhase();
 assertInstanceOf(Main2Phase.class, currentPhase);

 currentPhase = game.getCurrentPhase();

 Phase nextPhase = Phase.nextPhase();
 assertInstanceOf(EndOfTurnPhase.class, nextPhase);

 assertEquals(EndOfTurnPhase.class, game.getCurrentPhase().getClass());
 }

 @Test
 public void testNextPhase_EndOfTurnPhaseToCleanupPhase() {
 Phase currentPhase = Phase.nextPhase();
 assertInstanceOf(EndOfTurnPhase.class, currentPhase);

 currentPhase = game.getCurrentPhase();

 Phase nextPhase = Phase.nextPhase();
 assertInstanceOf(CleanupPhase.class, nextPhase);

 assertEquals(CleanupPhase.class, game.getCurrentPhase().getClass());
 }

 @Test
 public void testNextPhase_CleanupPhaseToPlayerChangePhase() {
 Phase currentPhase = Phase.nextPhase();
 assertInstanceOf(CleanupPhase.class, currentPhase);

 currentPhase = game.getCurrentPhase();

 Phase nextPhase = Phase.nextPhase();
 assertInstanceOf(PlayerChangePhase.class, nextPhase);

 assertEquals(PlayerChangePhase.class, game.getCurrentPhase().getClass());
 }

 @Test
 public void testNextPhase_PlayerChangePhaseToUntapPhase() {
 Phase currentPhase = Phase.nextPhase();
 assertInstanceOf(PlayerChangePhase.class, currentPhase);

 currentPhase = game.getCurrentPhase();

 Phase nextPhase = Phase.nextPhase();
 assertInstanceOf(UntapPhase.class, nextPhase);

 assertEquals(UntapPhase.class, game.getCurrentPhase().getClass());
 }
}