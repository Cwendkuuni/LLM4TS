import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.phases.Phase;
import lotus.core.effect.ChangePhase;

public class PhaseTest {

 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase;

 @Before
 public void setUp() {
 Game.currentPhase = null;
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
 public void testDoPhaseAbstractMethod() {
 assertNull(Game.currentPhase);
 untapPhase.doPhase();
 assertNotNull(Game.currentPhase);
 assertSame(Game.currentPhase, untapPhase);
 }

 @Test(expected = IllegalStateException.class)
 public void testChangePhaseNullPhase() {
 Phase nullPhase = null;
 Phase previousPhase = new UntapPhase();
 Game.currentPhase = previousPhase;
 Phase newPhase = new UntapPhase();
 try {
 Phase.changePhase(nullPhase);
 } catch (IllegalStateException e) {
 e.printStackTrace();
 fail("Expected IllegalStateException but got " + e.getClass());
 }
}

 @Test
 public void testNextPhaseUntapPhaseToUpkeepPhase() {
 Game.currentPhase = untapPhase;
 Phase.nextPhase();
 assertSame(Game.currentPhase, upkeepPhase);
}

 @Test
 public void testNextPhaseUpkeepPhaseToDrawPhase() {
 Game.currentPhase = upkeepPhase;
 Phase.nextPhase();
 assertSame(Game.currentPhase, drawPhase);
}

 @Test
 public void testNextPhaseDrawPhaseToMain1Phase() {
 Game.currentPhase = drawPhase;
 Phase.nextPhase();
 assertSame(Game.currentPhase, main1Phase);
}

 @Test
 public void testNextPhaseMain1PhaseToCombatBeginningPhase() {
 Game.currentPhase = main1Phase;
 Phase.nextPhase();
 assertSame(Game.currentPhase, combatBeginningPhase);
}

 @Test
 public void testNextPhaseCombatBeginningPhaseToDeclareAttackersPhase() {
 Game.currentPhase = combatBeginningPhase;
 Phase.nextPhase();
 assertSame(Game.currentPhase, declareAttackersPhase);
}

 @Test
 public void testNextPhaseDeclareAttackersPhaseToDeclareBlockersPhase() {
 Game.currentPhase = declareAttackersPhase;
 Phase.nextPhase();
 assertSame(Game.currentPhase, declareBlockersPhase);
}

 @Test
 public void testNextPhaseDeclareBlockersPhaseToCombatDamagePhase() {
 Game.currentPhase = declareBlockersPhase;
 Phase.nextPhase();
 assertSame(Game.currentPhase, combatDamagePhase);
}

 @Test
 public void testNextPhaseCombatDamagePhaseToCombatEndPhase() {
 Game.currentPhase = combatDamagePhase;
 Phase.nextPhase();
 assertSame(Game.currentPhase, combatEndPhase);
}

 @Test
 public void testNextPhaseCombatEndPhaseToMain2Phase() {
 Game.currentPhase = combatEndPhase;
 Phase.nextPhase();
 assertSame(Game.currentPhase, main2Phase);
}

 @Test
 public void testNextPhaseMain2PhaseToEndOfTurnPhase() {
 Game.currentPhase = main2Phase;
 Phase.nextPhase();
 assertSame(Game.currentPhase, endOfTurnPhase);
}

 @Test
 public void testNextPhaseEndOfTurnPhaseToCleanupPhase() {
 Game.currentPhase = endOfTurnPhase;
 Phase.nextPhase();
 assertSame(Game.currentPhase, cleanupPhase);
}

 @Test
 public void testNextPhaseCleanupPhaseToPlayerChangePhase() {
 Game.currentPhase = cleanupPhase;
 Phase.nextPhase();
 assertSame(Game.currentPhase, playerChangePhase);
}

 @Test
 public void testNextPhasePlayerChangePhaseToUntapPhase() {
 Game.currentPhase = playerChangePhase;
 Phase.nextPhase();
 assertSame(Game.currentPhase, untapPhase);
}

 @Test(expected = IllegalStateException.class)
 public void testNextPhaseInvalidPhaseOrder() {
 Game.currentPhase = upkeepPhase;
 Phase.nextPhase(); // should be Main1Phase, but let it go to Main2Phase
 Phase.nextPhase(); // should be EndOfTurnPhase, but let it go to CleanupPhase
 Phase.nextPhase(); // should be PlayerChangePhase, but let it go to UntapPhase
 Phase.nextPhase(); // should be UntapPhase, but an IllegalStateException should be thrown
}

}