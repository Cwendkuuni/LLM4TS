package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.*;
import java.util.*;

public class TransitionTest {

    @Test
    public void testTransition() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new State("initialState");
        IState outcomeState = new State("outcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(name, transition.getName());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertEquals(triggerEvent, transition.getTriggerEvent());

        // Test adding guard conditions
        IPredicate predicate1 = new ConstantPredicate(true);
        transition.addGuardCondition(predicate1);
        assertEquals(1, transition.getGuardConditions().size());
        IPredicate predicate2 = new ConstantPredicate(false);
        transition.addGuardCondition(predicate2);
        assertEquals(2, transition.getGuardConditions().size());

        // Test equals and hashCode
        Transition identicalTransition = new Transition(name, triggerEvent, initialState, outcomeState);
        assertEquals(transition, identicalTransition);
        assertEquals(transition.hashCode(), identicalTransition.hashCode());

        // Test trigger method
        IState currentState = new State("currentState");
        IState expectedOutcomeState = outcomeState;
        transition.trigger(currentState, triggerEvent);
        assertEquals(expectedOutcomeState, currentState);

        // Test willTrigger method
        assertTrue(transition.willTrigger(initialState, triggerEvent));
        assertFalse(transition.willTrigger(currentState, triggerEvent));

        // Test evaluateGuardConditions method
        transition.evaluateGuardConditions();

        // Add more tests for evaluateGuardConditions method based on your guard condition implementation
    }
}