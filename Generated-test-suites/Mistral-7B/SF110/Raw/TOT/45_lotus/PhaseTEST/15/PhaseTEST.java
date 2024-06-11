import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import lotus.core.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Game game;

 @Before
 public void setUp() {
 game = new Game();
 }

 @Test
 public void testUntapPhase() {
 Phase untapPhase = new UntapPhase();
 untapPhase.doPhase();
 assertFalse("No phase should be set after UntapPhase", game.isCurrentPhase(UntapPhase.class));
 changePhase(new UpkeepPhase());
 assertTrue("UpkeepPhase should be set after UntapPhase", game.isCurrentPhase(UpkeepPhase.class));
 }

 @Test
 public void testUpkeepPhase() {
 Phase upkeepPhase = new UpkeepPhase();
 upkeepPhase.doPhase();
 assertFalse("No phase should be set after UpkeepPhase", game.isCurrentPhase(UpkeepPhase.class));
 changePhase(new DrawPhase());
 assertTrue("DrawPhase should be set after UpkeepPhase", game.isCurrentPhase(DrawPhase.class));
 }

 @Test
 public void testDrawPhase() {
 Phase drawPhase = new DrawPhase();
 drawPhase.doPhase();
 assertFalse("No phase should be set after DrawPhase", game.isCurrentPhase(DrawPhase.class));
 changePhase(new Main1Phase());
 assertTrue("Main1Phase should be set after DrawPhase", game.isCurrentPhase(Main1Phase.class));
 }

 @Test
 public void testMain1Phase() {
 Phase main1Phase = new Main1Phase();
 main1Phase.doPhase();
 assertFalse("No phase should be set after Main1Phase", game.isCurrentPhase(Main1Phase.class));
 changePhase(new CombatBeginningPhase());
 assertTrue("CombatBeginningPhase should be set after Main1Phase", game.isCurrentPhase(CombatBeginningPhase.class));
 }

 @Test
 public void testCombatBeginningPhase() {
 Phase combatBeginningPhase = new CombatBeginningPhase();
 combatBeginningPhase.doPhase();
 assertFalse("No phase should be set after CombatBeginningPhase", game.isCurrentPhase(CombatBeginningPhase.class));
 changePhase(new DeclareAttackersPhase());
 assertTrue("DeclareAttackersPhase should be set after CombatBeginningPhase", game.isCurrentPhase(DeclareAttackersPhase.class));
 }

 @Test
 public void testDeclareAttackersPhase() {
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 declareAttackersPhase.doPhase();
 assertFalse("No phase should be set after DeclareAttackersPhase", game.isCurrentPhase(DeclareAttackersPhase.class));
 changePhase(new DeclareBlockersPhase());
 assertTrue("DeclareBlockersPhase should be set after DeclareAttackersPhase", game.isCurrentPhase(DeclareBlockersPhase.class));
 }

 @Test
 public void testDeclareBlockersPhase() {
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 declareBlockersPhase.doPhase();
 assertFalse("No phase should be set after DeclareBlockersPhase", game.isCurrentPhase(DeclareBlockersPhase.class));
 changePhase(new CombatDamagePhase());
 assertTrue("CombatDamagePhase should be set after DeclareBlockersPhase", game.isCurrentPhase(CombatDamagePhase.class));
 }

 @Test
 public void testCombatDamagePhase() {
 Phase combatDamagePhase = new CombatDamagePhase();
 combatDamagePhase.doPhase();
 assertFalse("No phase should be set after CombatDamagePhase", game.isCurrentPhase(CombatDamagePhase.class));
 changePhase(new CombatEndPhase());
 assertTrue("CombatEndPhase should be set after CombatDamagePhase", game.isCurrentPhase(CombatEndPhase.class));
 }

 @Test
 public void testCombatEndPhase() {
 Phase combatEndPhase = new CombatEndPhase();
 combatEndPhase.doPhase();
 assertFalse("No phase should be set after CombatEndPhase", game.isCurrentPhase(CombatEndPhase.class));
 changePhase(new Main2Phase());
 assertTrue("Main2Phase should be set after CombatEndPhase", game.isCurrentPhase(Main2Phase.class));
 }

 @Test
 public void testMain2Phase() {
 Phase main2Phase = new Main2Phase();
 main2Phase.doPhase();
 assertFalse("No phase should be set after Main2Phase", game.isCurrentPhase(Main2Phase.class));
 changePhase(new EndOfTurnPhase());
 assertTrue("EndOfTurnPhase should be set after Main2Phase", game.isCurrentPhase(EndOfTurnPhase.class));
 }

 @Test
 public void testEndOfTurnPhase() {
 Phase endOfTurnPhase = new EndOfTurnPhase();
 endOfTurnPhase.doPhase();
 assertFalse("No phase should be set after EndOfTurnPhase", game.isCurrentPhase(EndOfTurnPhase.class));
 changePhase(new CleanupPhase());
 assertTrue("CleanupPhase should be set after EndOfTurnPhase", game.isCurrentPhase(CleanupPhase.class));
 }

 @Test
 public void testCleanupPhase() {
 Phase cleanupPhase = new CleanupPhase();
 cleanupPhase.doPhase();
 assertFalse("No phase should be set after CleanupPhase", game.isCurrentPhase(CleanupPhase.class));
 changePhase(new PlayerChangePhase());
 assertTrue("PlayerChangePhase should be set after CleanupPhase", game.isCurrentPhase(PlayerChangePhase.class));
 }

 @Test
 public void testPlayerChangePhase() {
 Phase playerChangePhase = new PlayerChangePhase();
 playerChangePhase.doPhase();
 assertFalse("No phase should be set after PlayerChangePhase", game.isCurrentPhase(PlayerChangePhase.class));
 changePhase(new UntapPhase());
 assertTrue("UntapPhase should be set after PlayerChangePhase", game.isCurrentPhase(UntapPhase.class));
 }

 @Test (expected = IllegalStateException.class)
 public void testIllegalStateException() {
 changePhase(null);
 }

 @Test (expected = IllegalStateException.class)
 public void testIllegalStateException1() {
 changePhase(new UntapPhase());
 changePhase(new UntapPhase());
 }
}