package lotus.core.phases;

import static org.junit.Assert.assertSame;

import lotus.core.*;
import lotus.core.effect.*;
import org.junit.Test;

public class PhaseTest {

 Phase mockPhase1 = new Phase() {
 @Override
 public void doPhase() {
 // implementation goes here
 }
 };

 Phase mockPhase2 = new Phase() {
 @Override
 public void doPhase() {
 // implementation goes here
 }
 };

 @Test
 public void testNextPhaseUntapPhase() {
 Game.currentPhase = new UntapPhase();
 assertSame(UntapPhase.class, Game.currentPhase.getClass());
 Phase.nextPhase();
 assertSame(UpkeepPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testNextPhaseUpkeepPhase() {
 Game.currentPhase = new UpkeepPhase();
 assertSame(UpkeepPhase.class, Game.currentPhase.getClass());
 Phase.nextPhase();
 assertSame(DrawPhase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseDrawPhase() {
 Game.currentPhase = new DrawPhase();
 assertSame(DrawPhase.class, Game.currentPhase.getClass());
 Phase.nextPhase();
 assertSame(Main1Phase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseMain1Phase() {
 Game.currentPhase = new Main1Phase();
 assertSame(Main1Phase.class, Game.currentPhase.getClass());
 Phase.nextPhase();
 assertSame(CombatBeginningPhase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseCombatBeginningPhase() {
 Game.currentPhase = new CombatBeginningPhase();
 assertSame(CombatBeginningPhase.class, Game.currentPhase.getClass());
 Phase.nextPhase();
 assertSame(DeclareAttackersPhase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseDeclareAttackersPhase() {
 Game.currentPhase = new DeclareAttackersPhase();
 assertSame(DeclareAttackersPhase.class, Game.currentPhase.getClass());
 Phase.nextPhase();
 assertSame(DeclareBlockersPhase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseDeclareBlockersPhase() {
 Game.currentPhase = new DeclareBlockersPhase();
 assertSame(DeclareBlockersPhase.class, Game.currentPhase.getClass());
 Phase.nextPhase();
 assertSame(CombatDamagePhase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseCombatDamagePhase() {
 Game.currentPhase = new CombatDamagePhase();
 assertSame(CombatDamagePhase.class, Game.currentPhase.getClass());
 Phase.nextPhase();
 assertSame(CombatEndPhase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseCombatEndPhase() {
 Game.currentPhase = new CombatEndPhase();
 assertSame(CombatEndPhase.class, Game.currentPhase.getClass());
 Phase.nextPhase();
 assertSame(Main2Phase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseMain2Phase() {
 Game.currentPhase = new Main2Phase();
 assertSame(Main2Phase.class, Game.currentPhase.getClass());
 Phase.nextPhase();
 assertSame(EndOfTurnPhase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseEndOfTurnPhase() {
 Game.currentPhase = new EndOfTurnPhase();
 assertSame(EndOfTurnPhase.class, Game.currentPhase.getClass());
 Phase.nextPhase();
 assertSame(CleanupPhase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhaseCleanupPhase() {
 Game.currentPhase = new CleanupPhase();
 assertSame(CleanupPhase.class, Game.currentPhase.getClass());
 Phase.nextPhase();
 assertSame(PlayerChangePhase.class, Game.currentPhase.class);
 }

 @Test
 public void testNextPhasePlayerChangePhase() {
 Game.currentPhase = new PlayerChangePhase();
 assertSame(PlayerChangePhase.class, Game.currentPhase.getClass());
 Phase.nextPhase();
 assertSame(UntapPhase.class, Game.currentPhase.class);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhaseNullPhase() {
 Game.currentPhase = null;
 Phase.nextPhase();
 }

 @Test
 public void testNextPhaseWrongPhaseOrder() {
 Game.currentPhase = new UpkeepPhase();
 assertSame(UpkeepPhase.class, Game.currentPhase.getClass());
 Phase.nextPhase(); // should be Main1Phase, but testing nextPhase directly
 assertSame(DeclareAttackersPhase.class, Game.currentPhase.class);
 }
}