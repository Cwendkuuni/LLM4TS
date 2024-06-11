package lotus.core.phases;

import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.effect.ChangePhase;
import lotus.core.phases.Phase;
import lotus.core.phases.UntapPhase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PhaseTest {

 private Game game;

 @Before
 public void setUp() {
 game = new Game();
 }

 @After
 public void tearDown() {
 // Reset the game state between tests
 game = new Game();
 }

 @Test
 public void testNextPhase_UntapPhase_UpkeepPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();

 Phase currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), UntapPhase.class);

 Phase.nextPhase();
 currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), UpkeepPhase.class);
 }

 @Test
 public void testNextPhase_UpkeepPhase_DrawPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();

 Phase currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), UpkeepPhase.class);

 Phase.nextPhase();
 currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), DrawPhase.class);
 }

 @Test
 public void testNextPhase_DrawPhase_Main1Phase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();

 Phase currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), DrawPhase.class);

 Phase.nextPhase();
 currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), Main1Phase.class);
 }

 @Test
 public void testNextPhase_Main1Phase_CombatBeginningPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();

 Phase currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), Main1Phase.class);

 Phase.nextPhase();
 currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), CombatBeginningPhase.class);
 }

 @Test
 public void testNextPhase_CombatBeginningPhase_DeclareAttackersPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase declareAttackersPhase = new DeclareAttackersPhase();

 Phase currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), CombatBeginningPhase.class);

 Phase.nextPhase();
 currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), DeclareAttackersPhase.class);
 }

 @Test
 public void testNextPhase_DeclareAttackersPhase_DeclareBlockersPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase declareBlockersPhase = new DeclareBlockersPhase();

 Phase currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), DeclareAttackersPhase.class);

 Phase.nextPhase();
 currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), DeclareBlockersPhase.class);
 }

 @Test
 public void testNextPhase_DeclareBlockersPhase_CombatDamagePhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 Phase combatDamagePhase = new CombatDamagePhase();

 Phase currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), DeclareBlockersPhase.class);

 Phase.nextPhase();
 currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), CombatDamagePhase.class);
 }

 @Test
 public void testNextPhase_CombatDamagePhase_CombatEndPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 Phase combatDamagePhase = new CombatDamagePhase();
 Phase combatEndPhase = new CombatEndPhase();

 Phase currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), CombatDamagePhase.class);

 Phase.nextPhase();
 currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), CombatEndPhase.class);
 }

 @Test
 public void testNextPhase_CombatEndPhase_Main2Phase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 Phase combatDamagePhase = new CombatDamagePhase();
 Phase combatEndPhase = new CombatEndPhase();
 Phase main2Phase = new Main2Phase();

 Phase currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), CombatEndPhase.class);

 Phase.nextPhase();
 currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), Main2Phase.class);
 }

 @Test
 public void testNextPhase_Main2Phase_EndOfTurnPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 Phase combatDamagePhase = new CombatDamagePhase();
 Phase combatEndPhase = new CombatEndPhase();
 Phase main2Phase = new Main2Phase();
 Phase endOfTurnPhase = new EndOfTurnPhase();

 Phase currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), Main2Phase.class);

 Phase.nextPhase();
 currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), EndOfTurnPhase.class);
 }

 @Test
 public void testNextPhase_EndOfTurnPhase_PlayerChangePhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 Phase combatDamagePhase = new CombatDamagePhase();
 Phase combatEndPhase = new CombatEndPhase();
 Phase main2Phase = new Main2Phase();
 Phase endOfTurnPhase = new EndOfTurnPhase();
 Phase playerChangePhase = new PlayerChangePhase();

 Phase currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), EndOfTurnPhase.class);

 Phase.nextPhase();
 currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), PlayerChangePhase.class);
 }

 @Test
 public void testNextPhase_PlayerChangePhase_UntapPhase() {
 Phase untapPhase = new UntapPhase();
 Phase upkeepPhase = new UpkeepPhase();
 Phase drawPhase = new DrawPhase();
 Phase main1Phase = new Main1Phase();
 Phase combatBeginningPhase = new CombatBeginningPhase();
 Phase declareAttackersPhase = new DeclareAttackersPhase();
 Phase declareBlockersPhase = new DeclareBlockersPhase();
 Phase combatDamagePhase = new CombatDamagePhase();
 Phase combatEndPhase = new CombatEndPhase();
 Phase main2Phase = new Main2Phase();
 Phase endOfTurnPhase = new EndOfTurnPhase();
 Phase playerChangePhase = new PlayerChangePhase();
 Phase untapPhaseAfterPlayerChange = new UntapPhase();

 Phase currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), PlayerChangePhase.class);

 Phase.nextPhase();
 currentPhase = game.getCurrentPhase();
 assertEquals(currentPhase.getClass(), UntapPhase.class);
 assertEquals(game.getPlayingPlayer(), 1);
 }
}