import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Phase currentPhase;
 private Game game;

 @Before
 public void setUp() {
 game = new Game();
 currentPhase = game.getPhase();
 }

 @Test
 public void testDoPhaseAbstractMethod() {
 assertFalse("doPhase method must be overridden in subclasses", currentPhase instanceof Phase && currentPhase.getClass().getMethod("doPhase") == null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChangePhaseMethodStatic() {
 Phase.changePhase(null);
 }

 @Test
 public void testNextPhaseUntapPhase() {
 assertEquals("UntapPhase should be replaced by UpkeepPhase", UpkeepPhase.class, Phase.nextPhase(new UntapPhase()));
 }

 @Test
 public void testNextPhaseUpkeepPhase() {
 assertEquals("UpkeepPhase should be replaced by DrawPhase", DrawPhase.class, Phase.nextPhase(new UpkeepPhase()));
 }

 @Test
 public void testNextPhaseDrawPhase() {
 assertEquals("DrawPhase should be replaced by Main1Phase", Main1Phase.class, Phase.nextPhase(new DrawPhase()));
 }

 @Test
 public void testNextPhaseMain1Phase() {
 assertEquals("Main1Phase should be replaced by CombatBeginningPhase", CombatBeginningPhase.class, Phase.nextPhase(new Main1Phase()));
 }

 @Test
 public void testNextPhaseCombatBeginningPhase() {
 assertEquals("CombatBeginningPhase should be replaced by DeclareAttackersPhase", DeclareAttackersPhase.class, Phase.nextPhase(new CombatBeginningPhase()));
 }

 @Test
 public void testNextPhaseDeclareAttackersPhase() {
 assertEquals("DeclareAttackersPhase should be replaced by DeclareBlockersPhase", DeclareBlockersPhase.class, Phase.nextPhase(new DeclareAttackersPhase()));
 }

 @Test
 public void testNextPhaseDeclareBlockersPhase() {
 assertEquals("DeclareBlockersPhase should be replaced by CombatDamagePhase", CombatDamagePhase.class, Phase.nextPhase(new DeclareBlockersPhase()));
 }

 @Test
 public void testNextPhaseCombatDamagePhase() {
 assertEquals("CombatDamagePhase should be replaced by CombatEndPhase", CombatEndPhase.class, Phase.nextPhase(new CombatDamagePhase()));
 }

 @Test
 public void testNextPhaseCombatEndPhase() {
 assertEquals("CombatEndPhase should be replaced by Main2Phase", Main2Phase.class, Phase.nextPhase(new CombatEndPhase()));
 }

 @Test
 public void testNextPhaseMain2Phase() {
 assertEquals("Main2Phase should be replaced by EndOfTurnPhase", EndOfTurnPhase.class, Phase.nextPhase(new Main2Phase()));
 }

 @Test
 public void testNextPhaseEndOfTurnPhase() {
 assertEquals("EndOfTurnPhase should be replaced by CleanupPhase", CleanupPhase.class, Phase.nextPhase(new EndOfTurnPhase()));
 }

 @Test
 public void testNextPhaseCleanupPhase() {
 assertEquals("CleanupPhase should be replaced by PlayerChangePhase", PlayerChangePhase.class, Phase.nextPhase(new CleanupPhase()));
 }

 @Test
 public void testNextPhasePlayerChangePhase() {
 assertEquals("PlayerChangePhase should be replaced by UntapPhase", UntapPhase.class, Phase.nextPhase(new PlayerChangePhase()));
 }

 @Test(expected = IllegalStateException.class)
 public void testNextPhaseInvalidCurrentPhase() {
 Phase invalidPhase = new Object(); // Replace with an invalid subclass of Phase
 Phase.nextPhase(invalidPhase);
 }

 @Test(expected = AssertionError.class)
 public void testGetCurrentPhaseNullGame() {
 Game nullGame = null;
 assertNull("getCurrentPhase should return null when Game is null", nullGame.getPhase());
 }

 @Test
 public void testGetCurrentPhaseValidGame() {
 Game validGame = new Game();
 Phase validPhase = new UntapPhase();
 validGame.setPhase(validPhase);
 assertEquals("getCurrentPhase should return the current phase", validPhase, validGame.getPhase());
 }
}