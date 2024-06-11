package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import lotus.core.*;
import lotus.core.effect.*;
import org.junit.Test;

public class PhaseTest {

 private static Phase getInstance(Class<? extends Phase> phaseClass) {
 try {
 return phaseClass.newInstance();
 } catch (InstantiationException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testNextPhaseOrder() {
 Phase[] phases = {
 new UntapPhase(),
 new UpkeepPhase(),
 new DrawPhase(),
 new Main1Phase(),
 new CombatBeginningPhase(),
 new DeclareAttackersPhase(),
 new DeclareBlockersPhase(),
 new CombatDamagePhase(),
 new CombatEndPhase(),
 new Main2Phase(),
 new EndOfTurnPhase(),
 new CleanupPhase(),
 new PlayerChangePhase()
 };

 Phase currentPhase = phases[0];
 for (int i = 1; i < phases.length; i++) {
 Phase nextPhase = phases[i];
 nextPhase.doPhase();
 currentPhase = nextPhase;
 assertEquals(nextPhase.getClass().getSimpleName(), currentPhase.getClass().getSimpleName());
 }
}

 @Test
 public void testChangePhase() {
 Phase[] phases = {
 new UntapPhase(),
 new UpkeepPhase(),
 new DrawPhase(),
 new Main1Phase(),
 new CombatBeginningPhase(),
 new DeclareAttackersPhase(),
 new DeclareBlockersPhase(),
 new CombatDamagePhase(),
 new CombatEndPhase(),
 new Main2Phase(),
 new EndOfTurnPhase(),
 new CleanupPhase(),
 new PlayerChangePhase()
 };
 Game.currentPhase = phases[0];

 for (int i = 1; i < phases.length; i++) {
 Phase nextPhase = phases[i];
 Utils.changePhase(nextPhase);
 assertEquals(nextPhase.getClass().getSimpleName(), Game.currentPhase.getClass().getSimpleName());
 }
}

 @Test
 public void testChangePhaseInstance() {
 Phase untapPhase = getInstance(UntapPhase.class);
 Phase upkeepPhase = getInstance(UpkeepPhase.class);
 Phase drawPhase = getInstance(DrawPhase.class);
 Phase main1Phase = getInstance(Main1Phase.class);

 Utils.changePhase(upkeepPhase);
 assertSame(upkeepPhase, Game.currentPhase);

 Utils.changePhase(drawPhase);
 assertSame(drawPhase, Game.currentPhase);

 Utils.changePhase(main1Phase);
 assertSame(main1Phase, Game.currentPhase);
}

 @Test
 public void testChangePhaseNull() {
 Utils.changePhase(null);
 assertNull(Game.currentPhase);
}
}