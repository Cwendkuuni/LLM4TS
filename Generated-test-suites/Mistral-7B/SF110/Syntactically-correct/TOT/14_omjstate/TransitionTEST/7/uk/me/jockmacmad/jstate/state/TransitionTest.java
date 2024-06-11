package uk.me.jockmacmad.jstate.state.test;

import static org.junit.Assert.*;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {
    @Test
    public void testTrigger() {
        // Test successful transition
        IState initialState = new StateA();
        IState outcomeState = new StateB();
        Transition transition = new Transition("Transition1", new Event("EventA"), initialState, outcomeState);
        IPredicate guardCondition = new AlwaysTruePredicate();
        transition.addGuardCondition(guardCondition);
        IState actualState = transition.trigger(initialState, new Event("EventA"));
        assertEquals(outcomeState, actualState);

        // Test failure transition
        IState anotherInitialState = new StateC();
        actualState = transition.trigger(anotherInitialState, new Event("EventA"));
        assertNotEquals(outcomeState, actualState);
    }

    @Test
    public void testWillTrigger() {
        // Test successful scenario
        IState initialState = new StateA();
        IState outcomeState = new StateB();
        Transition transition = new Transition("Transition1", new Event("EventA"), initialState, outcomeState);
        IPredicate guardCondition = new AlwaysTruePredicate();
        transition.addGuardCondition(guardCondition);
        boolean actual = transition.willTrigger(initialState, new Event("EventA"));
        assertTrue(actual);

        // Test failure scenario
        IState anotherInitialState = new StateC();
        actual = transition.willTrigger(anotherInitialState, new Event("EventA"));
        assertFalse(actual);

        // Test another event
        IState anotherStateA = new StateA();
        actual = transition.willTrigger(anotherStateA, new Event("EventB"));
        assertFalse(actual);

        // Test empty guardConditions
        Transition emptyTransition = new Transition("Transition2", new Event("EventA"), initialState, outcomeState);
        boolean emptyWillTrigger = emptyTransition.willTrigger(initialState, new Event("EventA"));
        assertTrue(emptyWillTrigger);
    }

    @Test
    public void testEvaluateGuardConditions() {
        IState initialState = new StateA();
        Transition transition = new Transition("Transition1", new Event("EventA"), initialState, new StateB());
        GuardCondition guardCondition = new AlwaysFalseGuardCondition();
        transition.addGuardCondition(guardCondition);

        // Test successful scenario
        GuardCondition alwaysTrueGuard = new AlwaysTrueGuardCondition();
        transition.addGuardCondition(alwaysTrueGuard);
        IState actualState = transition.trigger(initialState, new Event("EventA"));
        assertEquals(transition.getOutcomeState(), actualState);

        // Test failure scenario
        boolean passesGuards = false;
        for (final IGuardCondition ig : transition.getGuardConditions()) {
            passesGuards = ig.evaluate(new Event("EventA"));
        }
        assertFalse(passesGuards);
    }
}