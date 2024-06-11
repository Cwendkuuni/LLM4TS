import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.effect.ChangePhase;
import lotus.core.phases.CombatBeginningPhase;
import lotus.core.phases.CombatDamagePhase;
import lotus.core.phases.CombatEndPhase;
import lotus.core.phases.DeclareAttackersPhase;
import lotus.core.phases.DeclareBlockersPhase;
import lotus.core.phases.DrawPhase;
import lotus.core.phases.EndOfTurnPhase;
import lotus.core.phases.Main1Phase;
import lotus.core.phases.Main2Phase;
import lotus.core.phases.PlayerChangePhase;
import lotus.core.phases.UntapPhase;

public class PhaseTest {

 private Game game;

 @Before
 public void setup() {
 game = new Game();
 }

 @Test
 public void testUntapPhase() {
 Phase untapPhase = new UntapPhase();
 untapPhase.doPhase();
 assertEquals(UntapPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testDrawPhase() {
 Phase drawPhase = new DrawPhase();
 drawPhase.doPhase();
 assertEquals(DrawPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testMain1Phase() {
 Phase main1Phase = new Main1Phase();
 main1Phase.doPhase();
 assertEquals(Main1Phase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testCombatBeginningPhase() {
 Phase combatBeginningPhase = new CombatBeginningPhase();
 combatBeginningPhase.doPhase();
 assertEquals(CombatBeginningPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testDeclareAttackersPhase() {
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 declareAttackersPhase.doPhase();
 assertEquals(DeclareAttackersPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testDeclareBlockersPhase() {
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 declareBlockersPhase.doPhase();
 assertEquals(DeclareBlockersPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testCombatDamagePhase() {
 Phase combatDamagePhase = new CombatDamagePhase();
 combatDamagePhase.doPhase();
 assertEquals(CombatDamagePhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testCombatEndPhase() {
 Phase combatEndPhase = new CombatEndPhase();
 combatEndPhase.doPhase();
 assertEquals(CombatEndPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testMain2Phase() {
 Phase main2Phase = new Main2Phase();
 main2Phase.doPhase();
 assertEquals(Main2Phase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testEndOfTurnPhase() {
 Phase endOfTurnPhase = new EndOfTurnPhase();
 endOfTurnPhase.doPhase();
 assertEquals(EndOfTurnPhase.class, Game.currentPhase.getClass());
 }

 @Test
 public void testPlayerChangePhase() {
 Phase playerChangePhase = new PlayerChangePhase();
 playerChangePhase.doPhase();
 assertEquals(PlayerChangePhase.class, Game.currentPhase.getClass());
 assertEquals(1, Game.playingPlayer);
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidPhaseTransition() {
 if (Game.currentPhase instanceof CombatEndPhase) {
 changePhase(new DrawPhase());
 }
 fail("Should throw IllegalStateException");
 }
}