package lotus.core.phases;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import lotus.core.Game;
import lotus.core.*;
import lotus.core.effect.*;

import static org.junit.Assert.*;

public class PhaseTest {

 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase;
 private Game game;

 @Before
 public void setup() {
 game = Mockito.spy(new Game());
 untapPhase = Mockito.spy(new UntapPhase());
 upkeepPhase = Mockito.spy(new UpkeepPhase());
 drawPhase = Mockito.spy(new DrawPhase());
 main1Phase = Mockito.spy(new Main1Phase());
 combatBeginningPhase = Mockito.spy(new CombatBeginningPhase());
 declareAttackersPhase = Mockito.spy(new DeclareAttackersPhase());
 declareBlockersPhase = Mockito.spy(new DeclareBlockersPhase());
 combatDamagePhase = Mockito.spy(new CombatDamagePhase());
 combatEndPhase = Mockito.spy(new CombatEndPhase());
 main2Phase = Mockito.spy(new Main2Phase());
 endOfTurnPhase = Mockito.spy(new EndOfTurnPhase());
 cleanupPhase = Mockito.spy(new CleanupPhase());
 playerChangePhase = Mockito.spy(new PlayerChangePhase());
 }

 @Test
 public void testNextPhase_UntapPhase_UpkeepPhase() {
 Phase currentPhase = untapPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(upkeepPhase, nextPhase);
 Game.currentPhase = currentPhase;
 assertNull(Phase.currentPhase);
}

 @Test
 public void testNextPhase_UpkeepPhase_DrawPhase() {
 Phase currentPhase = upkeepPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(drawPhase, nextPhase);
 Game.currentPhase = currentPhase;
 assertNull(Phase.currentPhase);
}

 @Test
 public void testNextPhase_DrawPhase_Main1Phase() {
 Phase currentPhase = drawPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(main1Phase, nextPhase);
 Game.currentPhase = currentPhase;
 assertNull(Phase.currentPhase);
}

 @Test
 public void testNextPhase_Main1Phase_CombatBeginningPhase() {
 Phase currentPhase = main1Phase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(combatBeginningPhase, nextPhase);
 Game.currentPhase = currentPhase;
 assertNull(Phase.currentPhase);
}

 @Test
 public void testNextPhase_CombatBeginningPhase_DeclareAttackersPhase() {
 Phase currentPhase = combatBeginningPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(declareAttackersPhase, nextPhase);
 Game.currentPhase = currentPhase;
 assertNull(Phase.currentPhase);
}

 @Test
 public void testNextPhase_DeclareAttackersPhase_DeclareBlockersPhase() {
 Phase currentPhase = declareAttackersPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(declareBlockersPhase, nextPhase);
 Game.currentPhase = currentPhase;
 assertNull(Phase.currentPhase);
}

 @Test
 public void testNextPhase_DeclareBlockersPhase_CombatDamagePhase() {
 Phase currentPhase = declareBlockersPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(combatDamagePhase, nextPhase);
 Game.currentPhase = currentPhase;
 assertNull(Phase.currentPhase);
}

 @Test
 public void testNextPhase_CombatDamagePhase_CombatEndPhase() {
 Phase currentPhase = combatDamagePhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(combatEndPhase, nextPhase);
 Game.currentPhase = currentPhase;
 assertNull(Phase.currentPhase);
}

 @Test
 public void testNextPhase_CombatEndPhase_Main2Phase() {
 Phase currentPhase = combatEndPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(main2Phase, nextPhase);
 Game.currentPhase = currentPhase;
 assertNull(Phase.currentPhase);
}

 @Test
 public void testNextPhase_Main2Phase_EndOfTurnPhase() {
 Phase currentPhase = main2Phase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(endOfTurnPhase, nextPhase);
 Game.currentPhase = currentPhase;
 assertNull(Phase.currentPhase);
}

 @Test
 public void testNextPhase_EndOfTurnPhase_CleanupPhase() {
 Phase currentPhase = endOfTurnPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(cleanupPhase, nextPhase);
 Game.currentPhase = currentPhase;
 assertNull(Phase.currentPhase);
}

 @Test
 public void testNextPhase_CleanupPhase_PlayerChangePhase() {
 Phase currentPhase = cleanupPhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(playerChangePhase, nextPhase);
 Game.currentPhase = currentPhase;
 assertNull(Phase.currentPhase);
}

 @Test
 public void testNextPhase_PlayerChangePhase_UntapPhase() {
 Phase currentPhase = playerChangePhase;
 Phase nextPhase = Phase.nextPhase();
 assertEquals(untapPhase, nextPhase);
 Game.currentPhase = currentPhase;
 assertNull(Phase.currentPhase);
}

}