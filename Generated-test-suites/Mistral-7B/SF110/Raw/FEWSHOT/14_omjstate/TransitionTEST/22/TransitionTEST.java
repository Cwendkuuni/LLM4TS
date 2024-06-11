package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import java.util.*;

public class TransitionTest {

    @Test
    public void testEquals() {
        IState initialState = new DummyState("Initial State");
        IState outcomeState = new DummyState("Outcome State");
        Event triggerEvent = new DummyEvent("Trigger Event");

        Transition transition1 = new Transition("Transition 1", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("Transition 1", triggerEvent, initialState, outcomeState);

        assertTrue(transition1.equals(transition2));

        Transition transition3 = new Transition("Transition 2", triggerEvent, initialState, outcomeState);
        assertFalse(transition1.equals(transition3));

        Transition transition4 = new Transition("Transition 1", null, initialState, outcomeState);
        assertFalse(transition1.equals(transition4));

        Transition transition5 = new Transition("Transition 1", triggerEvent, null, outcomeState);
        assertFalse(transition1.equals(transition5));

        Transition transition6 = new Transition("Transition 1", triggerEvent, initialState, null);
        assertFalse(transition1.equals(transition6));
    }

    @Test
    public void testHashCode() {
        IState initialState = new DummyState("Initial State");
        IState outcomeState = new DummyState("Outcome State");
        Event triggerEvent = new DummyEvent("Trigger Event");

        Transition transition1 = new Transition("Transition 1", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("Transition 1", triggerEvent, initialState, outcomeState);

        assertEquals(transition1.hashCode(), transition2.hashCode());

        Transition transition3 = new Transition("Transition 2", triggerEvent, initialState, outcomeState);
        assertNotEquals(transition1.hashCode(), transition3.hashCode());

        Transition transition4 = new Transition("Transition 1", null, initialState, outcomeState);
        assertNotEquals(transition1.hashCode(), transition4.hashCode());

        Transition transition5 = new Transition("Transition 1", triggerEvent, null, outcomeState);
        assertNotEquals(transition1.hashCode(), transition5.hashCode());

        Transition transition6 = new Transition("Transition 1", triggerEvent, initialState, null);
        assertNotEquals(transition1.hashCode(), transition6.hashCode());
    }

    @Test
    public void testAddGuardCondition() {
        IState initialState = new DummyState("Initial State");
        IState outcomeState = new DummyState("Outcome State");
        Event triggerEvent = new DummyEvent("Trigger Event");

        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);

        DummyPredicate guardCondition1 = new DummyPredicate(true);
        transition.addGuardCondition(guardCondition1);

        assertEquals(1, transition.getGuardConditions().size());

        DummyPredicate guardCondition2 = new DummyPredicate(false);
        transition.addGuardCondition(guardCondition2);

        assertEquals(2, transition.getGuardConditions().size());

        transition.addGuardCondition(guardCondition1);

        assertEquals(2, transition.getGuardConditions().size());
    }

    @Test
    public void testTrigger() {
        IState initialState = new DummyState("Initial State");
        IState outcomeState = new DummyState("Outcome State");
        Event triggerEvent = new DummyEvent("Trigger Event");

        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(initialState, triggerEvent);

        assertEquals(outcomeState, result);
    }

    @Test
    public void testTrigger_NoMatchingInitialStateOrTriggerEvent() {
        IState initialState = new DummyState("Initial State");
        IState outcomeState = new DummyState("Outcome State");
        Event triggerEvent = new DummyEvent("Trigger Event");

        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(new DummyState("Different State"), triggerEvent);

        assertEquals(initialState, result);
    }

    @Test
    public void testTrigger_NoMatchingTriggerEvent() {
        IState initialState = new DummyState("Initial State");
        IState outcomeState = new DummyState("Outcome State");
        Event triggerEvent1 = new DummyEvent("Trigger Event 1");
        Event triggerEvent2 = new DummyEvent("Trigger Event 2");

        Transition transition = new Transition("Transition", triggerEvent1, initialState, outcomeState);

        IState result = transition.trigger(initialState, triggerEvent2);

        assertEquals(initialState, result);
    }

    @Test
    public void testTrigger_NoMatchingInitialState() {
        IState initialState1 = new DummyState("Initial State 1");
        IState initialState2 = new DummyState("Initial State 2");
        IState outcomeState = new DummyState("Outcome State");
        Event triggerEvent = new DummyEvent("Trigger Event");

        Transition transition = new Transition("Transition", triggerEvent, initialState1, outcomeState);

        IState result = transition.trigger(initialState2, triggerEvent);

        assertEquals(initialState2, result);
    }

    @Test
    public void testTrigger_GuardsNotPassing() {
        IState initialState = new DummyState("Initial State");
        IState outcomeState = new DummyState("Outcome State");
        Event triggerEvent = new DummyEvent("Trigger Event");
        DummyPredicate guardCondition = new DummyPredicate(false);

        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(guardCondition);

        IState result = transition.trigger(initialState, triggerEvent);

        assertEquals(initialState, result);
    }

    @Test
    public void testWillTrigger() {
        IState initialState = new DummyState("Initial State");
        IState outcomeState = new DummyState("Outcome State");
        Event triggerEvent = new DummyEvent("Trigger Event");

        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);

        assertTrue(transition.willTrigger(initialState, triggerEvent));
    }

    @Test
    public void testWillTrigger_NoMatchingInitialStateOrTriggerEvent() {
        IState initialState = new DummyState("Initial State");
        IState outcomeState = new DummyState("Outcome State");
        Event triggerEvent = new DummyEvent("Trigger Event");

        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);

        assertFalse(transition.willTrigger(new DummyState("Different State"), triggerEvent));
    }

    @Test
    public void testWillTrigger_NoMatchingTriggerEvent() {
        IState initialState = new DummyState("Initial State");
        IState outcomeState = new DummyState("Outcome State");
        Event triggerEvent1 = new DummyEvent("Trigger Event 1");
        Event triggerEvent2 = new DummyEvent("Trigger Event 2");

        Transition transition = new Transition("Transition", triggerEvent1, initialState, outcomeState);

        assertFalse(transition.willTrigger(initialState, triggerEvent2));
    }

    @Test
    public void testWillTrigger_NoMatchingInitialState() {
        IState initialState1 = new DummyState("Initial State 1");
        IState initialState2 = new DummyState("Initial State 2");
        IState outcomeState = new DummyState("Outcome State");
        Event triggerEvent = new DummyEvent("Trigger Event");

        Transition transition = new Transition("Transition", triggerEvent, initialState1, outcomeState);

        assertFalse(transition.willTrigger(initialState2, triggerEvent));
    }

    @Test
    public void testWillTrigger_GuardsNotPassing() {
        IState initialState = new DummyState("Initial State");
        IState outcomeState = new DummyState("Outcome State");
        Event triggerEvent = new DummyEvent("Trigger Event");
        DummyPredicate guardCondition = new DummyPredicate(false);

        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(guardCondition);

        assertFalse(transition.willTrigger(initialState, triggerEvent));
    }

    @Test
    public void testEvaluateGuardConditions() {
        IState initialState = new DummyState("Initial State");
        Event triggerEvent = new DummyEvent("Trigger Event");
        DummyPredicate guardCondition1 = new DummyPredicate(true);
        DummyPredicate guardCondition2 = new DummyPredicate(false);

        Transition transition = new Transition("Transition", triggerEvent, initialState, new DummyState("Outcome State"));
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);

        try {
            transition.evaluateGuardConditions();
        } catch (Exception e) {
            fail("Expected no exception to be thrown.");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEvaluateGuardConditions_GuardsPresent() {
        IState initialState = new DummyState("Initial State");
        Event triggerEvent = new DummyEvent("Trigger Event");
        DummyPredicate guardCondition = new DummyPredicate() {
            @Override
            public boolean evaluate(Object object) {
                throw new UnsupportedOperationException();
            }
        };

        Transition transition = new Transition("Transition", triggerEvent, initialState, new DummyState("Outcome State"));
        transition.addGuardCondition(guardCondition);

        transition.evaluateGuardConditions();
    }
}