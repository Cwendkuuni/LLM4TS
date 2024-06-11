package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

 private static Phase getPhaseByName(Class<? extends Phase> clazz) {
 try {
 return clazz.newInstance();
 } catch (InstantiationException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testNextPhase_UntapPhase() {
 Phase currentPhase = getPhaseByName(UntapPhase.class);
 Phase nextPhase = getPhaseByName(UpkeepPhase.class);

 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_UpkeepPhase() {
 Phase currentPhase = getPhaseByName(UpkeepPhase.class);
 Phase nextPhase = getPhaseByName(DrawPhase.class);

 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_DrawPhase() {
 Phase currentPhase = getPhaseByName(DrawPhase.class);
 Phase nextPhase = getPhaseByName(Main1Phase.class);

 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_Main1Phase() {
 Phase currentPhase = getPhaseByName(Main1Phase.class);
 Phase nextPhase = getPhaseByName(CombatBeginningPhase.class);

 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CombatBeginningPhase() {
 Phase currentPhase = getPhaseByName(CombatBeginningPhase.class);
 Phase nextPhase = getPhaseByName(DeclareAttackersPhase.class);

 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_DeclareAttackersPhase() {
 Phase currentPhase = getPhaseByName(DeclareAttackersPhase.class);
 Phase nextPhase = getPhaseByName(DeclareBlockersPhase.class);

 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_DeclareBlockersPhase() {
 Phase currentPhase = getPhaseByName(DeclareBlockersPhase.class);
 Phase nextPhase = getPhaseByName(CombatDamagePhase.class);

 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CombatDamagePhase() {
 Phase currentPhase = getPhaseByName(CombatDamagePhase.class);
 Phase nextPhase = getPhaseByName(CombatEndPhase.class);

 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CombatEndPhase() {
 Phase currentPhase = getPhaseByName(CombatEndPhase.class);
 Phase nextPhase = getPhaseByName(Main2Phase.class);

 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_Main2Phase() {
 Phase currentPhase = getPhaseByName(Main2Phase.class);
 Phase nextPhase = getPhaseByName(EndOfTurnPhase.class);

 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_EndOfTurnPhase() {
 Phase currentPhase = getPhaseByName(EndOfTurnPhase.class);
 Phase nextPhase = getPhaseByName(CleanupPhase.class);

 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_CleanupPhase() {
 Phase currentPhase = getPhaseByName(CleanupPhase.class);
 Phase nextPhase = getPhaseByName(PlayerChangePhase.class);

 Phase.nextPhase();
 assertEquals(nextPhase, Game.currentPhase);
 }

 @Test
 public void testNextPhase_PlayerChangePhase() {
 Phase currentPhase = getPhaseByName(PlayerChangePhase.class);
 int playerCount = Game.playerCount;

 Phase.nextPhase();
 assertEquals(getPhaseByName(UntapPhase.class), Game.currentPhase);
 assertEquals(playerCount % 2 + 1, Game.playingPlayer);
 }

 @Test (expected = IllegalStateException.class)
 public void testNextPhase_InvalidPhase() {
 Phase currentPhase = getPhaseByName(new Object(){}.getClass());
 Phase nextPhase = getPhaseByName(UntapPhase.class);

 Phase.nextPhase();
 fail("Next phase should not be called with an invalid phase.");
 }

 @Test
 public void testChangePhase() {
 Phase currentPhase = getPhaseByName(UntapPhase.class);
 Phase newPhase = getPhaseByName(UpkeepPhase.class);

 Phase.changePhase(newPhase);
 assertEquals(newPhase, Game.currentPhase);
 }
}