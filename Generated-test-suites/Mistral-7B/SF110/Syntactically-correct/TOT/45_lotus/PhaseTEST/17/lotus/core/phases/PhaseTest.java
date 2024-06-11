import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import lotus.core.phases.*;

public class PhaseTest {

 private Game game;
 private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase, untapPhase2;

 @Before
 public void setup() {
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
 untapPhase2 = new UntapPhase();
 }

 @Test
 public void testNextPhase_UntapToUpkeep() {
 game.setCurrentPhase(untapPhase);
 Phase expectedPhase = upkeepPhase;
 Phase actualPhase = Phase.nextPhase(game);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_UpkeepToDraw() {
 game.setCurrentPhase(upkeepPhase);
 Phase expectedPhase = drawPhase;
 Phase actualPhase = Phase.nextPhase(game);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_DrawToMain1() {
 game.setCurrentPhase(drawPhase);
 Phase expectedPhase = main1Phase;
 Phase actualPhase = Phase.nextPhase(game);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_Main1ToCombatBeginning() {
 game.setCurrentPhase(main1Phase);
 Phase expectedPhase = combatBeginningPhase;
 Phase actualPhase = Phase.nextPhase(game);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_CombatBeginningToDeclareAttackers() {
 game.setCurrentPhase(combatBeginningPhase);
 Phase expectedPhase = declareAttackersPhase;
 Phase actualPhase = Phase.nextPhase(game);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_DeclareAttackersToDeclareBlockers() {
 game.setCurrentPhase(declareAttackersPhase);
 Phase expectedPhase = declareBlockersPhase;
 Phase actualPhase = Phase.nextPhase(game);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_DeclareBlockersToCombatDamage() {
 game.setCurrentPhase(declareBlockersPhase);
 Phase expectedPhase = combatDamagePhase;
 Phase actualPhase = Phase.nextPhase(game);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_CombatDamageToCombatEnd() {
 game.setCurrentPhase(combatDamagePhase);
 Phase expectedPhase = combatEndPhase;
 Phase actualPhase = Phase.nextPhase(game);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_CombatEndToMain2() {
 game.setCurrentPhase(combatEndPhase);
 Phase expectedPhase = main2Phase;
 Phase actualPhase = Phase.nextPhase(game);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_Main2ToEndOfTurn() {
 game.setCurrentPhase(main2Phase);
 Phase expectedPhase = endOfTurnPhase;
 Phase actualPhase = Phase.nextPhase(game);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_EndOfTurnToCleanup() {
 game.setCurrentPhase(endOfTurnPhase);
 Phase expectedPhase = cleanupPhase;
 Phase actualPhase = Phase.nextPhase(game);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_CleanupToPlayerChange() {
 game.setCurrentPhase(cleanupPhase);
 game.setPlayingPlayer(game.getPlayingPlayer() + 1);
 Phase expectedPhase = playerChangePhase;
 Phase actualPhase = Phase.nextPhase(game);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_PlayerChangeToUntap() {
 game.setCurrentPhase(playerChangePhase);
 Phase expectedPhase = untapPhase2;
 Phase actualPhase = Phase.nextPhase(game);
 assertEquals(expectedPhase, actualPhase);
 }

 @Test
 public void testNextPhase_WrongPhase() {
 Phase wrongPhase = new SomeRandomPhase();
 game.setCurrentPhase(wrongPhase);
 try {
 Phase.nextPhase(game);
 fail("Expected IllegalStateException to be thrown");
 } catch (IllegalStateException e) {
 // Expected
 }
 }
}