package lotus.core.phases;

import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PhaseTest {

 private Game game;
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
 public void testNextPhase_UntapPhaseToUpkeepPhase() {
 game.setCurrentPhase(untapPhase);
 Phase expectedPhase = upkeepPhase;
 Phase actualPhase = Phase.nextPhase();
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_UpkeepPhaseToDrawPhase() {
 game.setCurrentPhase(upkeepPhase);
 Phase expectedPhase = drawPhase;
 Phase actualPhase = Phase.nextPhase();
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_DrawPhaseToMain1Phase() {
 game.setCurrentPhase(drawPhase);
 Phase expectedPhase = main1Phase;
 Phase actualPhase = Phase.nextPhase();
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_Main1PhaseToCombatBeginningPhase() {
 game.setCurrentPhase(main1Phase);
 Phase expectedPhase = combatBeginningPhase;
 Phase actualPhase = Phase.nextPhase();
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_CombatBeginningPhaseToDeclareAttackersPhase() {
 game.setCurrentPhase(combatBeginningPhase);
 Phase expectedPhase = declareAttackersPhase;
 Phase actualPhase = Phase.nextPhase();
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_DeclareAttackersPhaseToDeclareBlockersPhase() {
 game.setCurrentPhase(declareAttackersPhase);
 Phase expectedPhase = declareBlockersPhase;
 Phase actualPhase = Phase.nextPhase();
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_DeclareBlockersPhaseToCombatDamagePhase() {
 game.setCurrentPhase(declareBlockersPhase);
 Phase expectedPhase = combatDamagePhase;
 Phase actualPhase = Phase.nextPhase();
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_CombatDamagePhaseToCombatEndPhase() {
 game.setCurrentPhase(combatDamagePhase);
 Phase expectedPhase = combatEndPhase;
 Phase actualPhase = Phase.nextPhase();
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_CombatEndPhaseToMain2Phase() {
 game.setCurrentPhase(combatEndPhase);
 Phase expectedPhase = main2Phase;
 Phase actualPhase = Phase.nextPhase();
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_Main2PhaseToEndOfTurnPhase() {
 game.setCurrentPhase(main2Phase);
 Phase expectedPhase = endOfTurnPhase;
 Phase actualPhase = Phase.nextPhase();
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_EndOfTurnPhaseToCleanupPhase() {
 game.setCurrentPhase(endOfTurnPhase);
 Phase expectedPhase = cleanupPhase;
 Phase actualPhase = Phase.nextPhase();
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_CleanupPhaseToPlayerChangePhase() {
 game.setCurrentPhase(cleanupPhase);
 Phase expectedPhase = playerChangePhase;
 Phase actualPhase = Phase.nextPhase();
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }

 @Test
 public void testNextPhase_PlayerChangePhaseToUntapPhase() {
 game.setCurrentPhase(playerChangePhase);
 game.setPlayingPlayer(1);
 Phase expectedPhase = untapPhase;
 Phase actualPhase = Phase.nextPhase();
 assertEquals(expectedPhase.getClass(), actualPhase.getClass());
 }
}