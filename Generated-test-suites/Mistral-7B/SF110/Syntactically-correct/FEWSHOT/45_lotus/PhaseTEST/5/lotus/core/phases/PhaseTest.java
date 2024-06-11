package lotus.core.phases;

import static org.junit.Assert.*;

import org.junit.Test;

public class PhaseTest {

    @Test
    public void testNextPhase_UntapPhase() {
        Phase currentPhase = new UntapPhase();
        Phase nextPhase = Phase.nextPhase(currentPhase);
        assertTrue(nextPhase instanceof UpkeepPhase);
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        Phase currentPhase = new UpkeepPhase();
        Phase nextPhase = Phase.nextPhase(currentPhase);
        assertTrue(nextPhase instanceof DrawPhase);
    }

    @Test
    public void testNextPhase_DrawPhase() {
        Phase currentPhase = new DrawPhase();
        Phase nextPhase = Phase.nextPhase(currentPhase);
        assertTrue(nextPhase instanceof Main1Phase);
    }

    @Test
    public void testNextPhase_Main1Phase() {
        Phase currentPhase = new Main1Phase();
        Phase nextPhase = Phase.nextPhase(currentPhase);
        assertTrue(nextPhase instanceof CombatBeginningPhase);
    }

    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Phase currentPhase = new CombatBeginningPhase();
        Phase nextPhase = Phase.nextPhase(currentPhase);
        assertTrue(nextPhase instanceof DeclareAttackersPhase);
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        Phase currentPhase = new DeclareAttackersPhase();
        Phase nextPhase = Phase.nextPhase(currentPhase);
        assertTrue(nextPhase instanceof DeclareBlockersPhase);
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        Phase currentPhase = new DeclareBlockersPhase();
        Phase nextPhase = Phase.nextPhase(currentPhase);
        assertTrue(nextPhase instanceof CombatDamagePhase);
    }

    @Test
    public void testNextPhase_CombatDamagePhase() {
        Phase currentPhase = new CombatDamagePhase();
        Phase nextPhase = Phase.nextPhase(currentPhase);
        assertTrue(nextPhase instanceof CombatEndPhase);
    }

    @Test
    public void testNextPhase_CombatEndPhase() {
        Phase currentPhase = new CombatEndPhase();
        Phase nextPhase = Phase.nextPhase(currentPhase);
        assertTrue(nextPhase instanceof Main2Phase);
    }

    @Test
    public void testNextPhase_Main2Phase() {
        Phase currentPhase = new Main2Phase();
        Phase nextPhase = Phase.nextPhase(currentPhase);
        assertTrue(nextPhase instanceof EndOfTurnPhase);
    }

    @Test
    public void testNextPhase_EndOfTurnPhase() {
        Phase currentPhase = new EndOfTurnPhase();
        Phase nextPhase = Phase.nextPhase(currentPhase);
        assertTrue(nextPhase instanceof CleanupPhase);
    }

    @Test
    public void testNextPhase_CleanupPhase() {
        Phase currentPhase = new CleanupPhase();
        Phase nextPhase = Phase.nextPhase(currentPhase);
        assertTrue(nextPhase instanceof PlayerChangePhase);
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
        Phase currentPhase = new PlayerChangePhase();
        Phase nextPhase = Phase.nextPhase(currentPhase);
        assertTrue(nextPhase instanceof UntapPhase);
    }

    @Test
    public void testNextPhase_NullPhase() {
        assertNull(Phase.nextPhase(null));
    }
}