package lotus.core.phases;

import lotus.core.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PhaseTest {

 Phase mockPhase;
 Game mockGame;

 @Before
 public void setup() {
 mockGame = new Game();
 mockPhase = new UntapPhase();
 }

 @Test
 public void testUntapPhase() {
 Phase previousPhase = Game.currentPhase;
 assertEquals(previousPhase, mockPhase.getClass());
 mockPhase.doPhase();
 assertNotSame(previousPhase, Game.currentPhase);
 assertEquals(Game.UntapPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testUpkeepPhase() {
 ChangePhase effect = new ChangePhase(mockPhase);
 mockPhase.doPhase();
 changePhaseAndAssertPhase(UpkeepPhase.class);
 effect.resolve();
 }

 @Test
 public void testDrawPhase() {
 ChangePhase effect = new ChangePhase(mockPhase);
 mockPhase.doPhase();
 changePhaseAndAssertPhase(DrawPhase.class);
 effect.resolve();
 }

 @Test
 public void testMain1Phase() {
 ChangePhase effect = new ChangePhase(mockPhase);
 mockPhase.doPhase();
 changePhaseAndAssertPhase(Main1Phase.class);
 effect.resolve();
 }

 @Test
 public void testCombatBeginningPhase() {
 ChangePhase effect = new ChangePhase(mockPhase);
 mockPhase.doPhase();
 changePhaseAndAssertPhase(CombatBeginningPhase.class);
 effect.resolve();
 }

 @Test
 public void testDeclareAttackersPhase() {
 ChangePhase effect = new ChangePhase(mockPhase);
 mockPhase.doPhase();
 changePhaseAndAssertPhase(DeclareAttackersPhase.class);
 effect.resolve();
 }

 @Test
 public void testDeclareBlockersPhase() {
 ChangePhase effect = new ChangePhase(mockPhase);
 mockPhase.doPhase();
 changePhaseAndAssertPhase(DeclareBlockersPhase.class);
 effect.resolve();
 }

 @Test
 public void testCombatDamagePhase() {
 ChangePhase effect = new ChangePhase(mockPhase);
 mockPhase.doPhase();
 changePhaseAndAssertPhase(CombatDamagePhase.class);
 effect.resolve();
 }

 @Test
 public void testCombatEndPhase() {
 ChangePhase effect = new ChangePhase(mockPhase);
 mockPhase.doPhase();
 changePhaseAndAssertPhase(CombatEndPhase.class);
 effect.resolve();
 }

 @Test
 public void testMain2Phase() {
 ChangePhase effect = new ChangePhase(mockPhase);
 mockPhase.doPhase();
 changePhaseAndAssertPhase(Main2Phase.class);
 effect.resolve();
 }

 @Test
 public void testEndOfTurnPhase() {
 ChangePhase effect = new ChangePhase(mockPhase);
 mockPhase.doPhase();
 changePhaseAndAssertPhase(EndOfTurnPhase.class);
 effect.resolve();
 }

 @Test
 public void testCleanupPhase() {
 ChangePhase effect = new ChangePhase(mockPhase);
 mockPhase.doPhase();
 changePhaseAndAssertPhase(CleanupPhase.class);
 effect.resolve();
 }

 @Test
 public void testPlayerChangePhase() {
 ChangePhase effect = new ChangePhase(mockPhase);
 mockPhase.doPhase();
 changePhaseAndAssertPhase(PlayerChangePhase.class);
 assertEquals(Game.PLAYER1, Game.playingPlayer);
 mockPhase = new UntapPhase();
 mockPhase.doPhase();
 assertEquals(Game.PLAYER2, Game.playingPlayer);
 }

 private void changePhaseAndAssertPhase(Class<?> expectedPhase) {
 Phase previousPhase = Game.currentPhase;
 assertEquals(previousPhase, mockPhase.getClass());
 Phase newPhase = nextPhase();
 assertEquals(expectedPhase, newPhase.getClass());
 assertEquals(previousPhase, Game.currentPhase);
 }

}