package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class TransitionTest {

    @Test
    public void testTransitionConstruction() {
        // Expert 1 test case
    }

    // Expert 2 test cases

    @Test
    public void testAddGuardCondition() {
    }

    @Test
    public void testAddDuplicateGuardCondition() {
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullGuardCondition() {
    }

    // Expert 3 test cases

    @Test
    public void testTriggerHappyPath() {
        // Expert 3 test case 1
    }

    @Test
    public void testTriggerNoGuards() {
        // Expert 3 test case 2
    }

    @Test
    public void testTriggerOneFailedGuard() {
        // Expert 3 test case 3
    }

    @Test
    public void testWillTriggerHappyPath() {
        // Expert 3 test case 4
    }

    @Test
    public void testWillTriggerNoGuards() {
        // Expert 3 test case 5
    }

    @Test
    public void testWillTriggerOneFailedGuard() {
        // Expert 3 test case 6
    }
}