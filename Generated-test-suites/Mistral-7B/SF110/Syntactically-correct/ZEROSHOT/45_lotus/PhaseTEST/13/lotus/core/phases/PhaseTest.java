import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import lotus.core.phases.*;

public class PhaseTest {

 private static Game game;
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
 public void testNextPhaseUntapPhase() {
 Phase currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), UntapPhase.class);
 game.nextPhase();
 currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), UpkeepPhase.class);
 }

 @Test
 public void testNextPhaseUpkeepPhase() {
 Phase currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), UpkeepPhase.class);
 game.nextPhase();
 currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), DrawPhase.class);
 }

 @Test
 public void testNextPhaseDrawPhase() {
 Phase currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), DrawPhase.class);
 game.nextPhase();
 currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), Main1Phase.class);
 }

 @Test
 public void testNextPhaseMain1Phase() {
 Phase currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), Main1Phase.class);
 game.nextPhase();
 currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), CombatBeginningPhase.class);
 }

 @Test
 public void testNextPhaseCombatBeginningPhase() {
 Phase currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), CombatBeginningPhase.class);
 game.nextPhase();
 currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), DeclareAttackersPhase.class);
 }

 @Test
 public void testNextPhaseDeclareAttackersPhase() {
 Phase currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), DeclareAttackersPhase.class);
 game.nextPhase();
 currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), DeclareBlockersPhase.class);
 }

 @Test
 public void testNextPhaseDeclareBlockersPhase() {
 Phase currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), DeclareBlockersPhase.class);
 game.nextPhase();
 currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), CombatDamagePhase.class);
 }

 @Test
 public void testNextPhaseCombatDamagePhase() {
 Phase currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), CombatDamagePhase.class);
 game.nextPhase();
 currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), CombatEndPhase.class);
 }

 @Test
 public void testNextPhaseCombatEndPhase() {
 Phase currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), CombatEndPhase.class);
 game.nextPhase();
 currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), Main2Phase.class);
 }

 @Test
 public void testNextPhaseMain2Phase() {
 Phase currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), Main2Phase.class);
 game.nextPhase();
 currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), EndOfTurnPhase.class);
 }

 @Test
 public void testNextPhaseEndOfTurnPhase() {
 Phase currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), EndOfTurnPhase.class);
 game.nextPhase();
 currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), CleanupPhase.class);
 }

 @Test
 public void testNextPhaseCleanupPhase() {
 Phase currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), CleanupPhase.class);
 game.nextPhase();
 currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), PlayerChangePhase.class);
 }

 @Test
 public void testNextPhasePlayerChangePhase() {
 Phase currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), PlayerChangePhase.class);
 game.nextPhase();
 currentPhase = game.getGamePhase();
 assertNotNull(currentPhase);
 assertEquals(currentPhase.getClass(), UntapPhase.class);
 }
}