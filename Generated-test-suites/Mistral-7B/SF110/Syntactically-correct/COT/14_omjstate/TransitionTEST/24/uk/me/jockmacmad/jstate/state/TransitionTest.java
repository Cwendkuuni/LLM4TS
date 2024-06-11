package uk.me.jockmacmad.jstate.state.test;

import org.junit.Test;
import org.junit.Assert.*;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.*;

import java.util.*;

public class TransitionTest {

    @Test
    public void testTransitionConstructor() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new DummyState("TestInitialState");
        IState outcomeState = new DummyState("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(name, transition.getName());
        assertEquals(triggerEvent, transition.getTriggerEvent());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testAddGuardCondition() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new DummyState("TestInitialState");
        IState outcomeState = new DummyState("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IPredicate guard = new DummyPredicate();
        transition.addGuardCondition(guard);

        assertEquals(1, transition.getGuardConditions().size());
        assertEquals(guard, transition.getGuardConditions().get(0));
    }

    @Test
    public void testTriggerTransitionsWithValidInitialStateAndEvent() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new DummyState("TestInitialState");
        IState outcomeState = new DummyState("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(initialState, triggerEvent);

        assertEquals(outcomeState, result);
    }

    @Test
    public void testTriggerTransitionsWithInvalidInitialState() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new DummyState("TestInvalidInitialState");
        IState outcomeState = new DummyState("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(initialState, triggerEvent);

        assertNotEquals(outcomeState, result);
    }

    @Test
    public void testTriggerTransitionsWithInvalidEvent() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestInvalidEvent");
        IState initialState = new DummyState("TestInitialState");
        IState outcomeState = new DummyState("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(initialState, triggerEvent);

        assertNotEquals(outcomeState, result);
    }

    @Test
    public void testWillTriggerTransitionsWithValidInitialStateAndEvent() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new DummyState("TestInitialState");
        IState outcomeState = new DummyState("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        boolean result = transition.willTrigger(initialState, triggerEvent);

        assertTrue(result);
    }

    @Test
    public void testWillTriggerTransitionsWithInvalidInitialState() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new DummyState("TestInvalidInitialState");
        IState outcomeState = new DummyState("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        boolean result = transition.willTrigger(initialState, triggerEvent);

        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullGuardCondition() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new DummyState("TestInitialState");
        IState outcomeState = new DummyState("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        transition.addGuardCondition(null);
    }

    @Test
    public void testEvaluateGuardConditionsWithValidEvent() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new DummyState("TestInitialState");
        IState outcomeState = new DummyState("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IPredicate guard = new DummyPredicate();
        transition.addGuardCondition(guard);

        boolean result = transition.evaluateGuardConditions(triggerEvent);

        assertTrue(result);
    }

    @Test
    public void testEvaluateGuardConditionsWithInvalidEvent() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestInvalidEvent");
        IState initialState = new DummyState("TestInitialState");
        IState outcomeState = new DummyState("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IPredicate guard = new DummyPredicate();
        transition.addGuardCondition(guard);

        boolean result = transition.evaluateGuardConditions(triggerEvent);

        assertFalse(result);
    }

    private static class DummyPredicate implements IPredicate {
        @Override
        public boolean evaluate(Event event) {
            return true;
        }
    }

    private static class DummyState implements IState {
        private String name;

        public DummyState(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof IState)) return false;
            IState state = (IState) o;
            return name.equals(state.getName());
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public String getName() {
            return name;
        }
    }
}