package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang.notnull.Nullable;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.DummyPredicate;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.List;

public class TransitionTest {

    // Test case for the constructor
    @Test(expected = NullPointerException.class)
    public void testTransitionNullInitialState() {
        new Transition(null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testTransitionNullOutcomeState() {
        new Transition("TestName", null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testTransitionNullTriggerEvent() {
        new Transition("TestName", null, null, null);
    }

    @Test
    public void testTransitionValidConstruction() {
        IState initialState = new DummyState("InitialState");
        IState outcomeState = new DummyState("OutcomeState");
        Event triggerEvent = new DummyEvent("TestEvent");
        Transition transition = new Transition("TestName", triggerEvent, initialState, outcomeState);
        assertEquals("TestName", transition.getName());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    // Test case for addGuardCondition method
    @Test(expected = NullPointerException.class)
    public void testAddGuardConditionNullPredicate() {
        Transition transition = new Transition("TestName", null, null, null);
        transition.addGuardCondition(null);
    }

    @Test
    public void testAddGuardConditionValid() {
        Transition transition = new Transition("TestName", null, null, null);
        IPredicate predicate = new DummyPredicate();
        transition.addGuardCondition(predicate);
        assertEquals(1, transition.getGuardConditions().size());
        assertEquals(predicate, transition.getGuardConditions().get(0));
    }

    // Test case for equals method
    @Test
    public void testEqualsSameTransition() {
        IState initialState = new DummyState("InitialState");
        IState outcomeState = new DummyState("OutcomeState");
        Event triggerEvent = new DummyEvent("TestEvent");
        Transition transition1 = new Transition("TestName", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("TestName", triggerEvent, initialState, outcomeState);
        assertTrue(transition1.equals(transition2));
    }

    @Test
    public void testEqualsDifferentName() {
        IState initialState = new DummyState("InitialState");
        IState outcomeState = new DummyState("OutcomeState");
        Event triggerEvent = new DummyEvent("TestEvent");
        Transition transition1 = new Transition("TestName1", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("TestName2", triggerEvent, initialState, outcomeState);
        assertFalse(transition1.equals(transition2));
    }

    @Test
    public void testEqualsDifferentTriggerEvent() {
        IState initialState = new DummyState("InitialState");
        IState outcomeState = new DummyState("OutcomeState");
        Event triggerEvent1 = new DummyEvent("TestEvent1");
        Event triggerEvent2 = new DummyEvent("TestEvent2");
        Transition transition1 = new Transition("TestName", triggerEvent1, initialState, outcomeState);
        Transition transition2 = new Transition("TestName", triggerEvent2, initialState, outcomeState);
        assertFalse(transition1.equals(transition2));
    }

    @Test
    public void testEqualsDifferentInitialState() {
        IState initialState1 = new DummyState("InitialState1");
        IState initialState2 = new DummyState("InitialState2");
        IState outcomeState = new DummyState("OutcomeState");
        Event triggerEvent = new DummyEvent("TestEvent");
        Transition transition1 = new Transition("TestName", triggerEvent, initialState1, outcomeState);
        Transition transition2 = new Transition("TestName", triggerEvent, initialState2, outcomeState);
        assertFalse(transition1.equals(transition2));
    }

    @Test
    public void testEqualsSameValuesButDifferentObjects() {
        IState initialState = new DummyState("InitialState");
        IState outcomeState = new DummyState("OutcomeState");
        Event triggerEvent = new DummyEvent("TestEvent");
        Transition transition1 = new Transition("TestName", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("TestName", triggerEvent, initialState, outcomeState) {
        };
        assertFalse(transition1.equals(transition2));
    }

    // Test case for hashCode method
    @Test
    public void testHashCodeSameTransition() {
        IState initialState = new DummyState("InitialState");
        IState outcomeState = new DummyState("OutcomeState");
        Event triggerEvent = new DummyEvent("TestEvent");
        Transition transition1 = new Transition("TestName", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("TestName", triggerEvent, initialState, outcomeState);
        assertEquals(transition1.hashCode(), transition2.hashCode());
    }

    @Test
    public void testHashCodeDifferentTransition() {
        IState initialState = new DummyState("InitialState");
        IState outcomeState = new DummyState("OutcomeState");
        Event triggerEvent1 = new DummyEvent("TestEvent1");
        Event triggerEvent2 = new DummyEvent("TestEvent2");
        Transition transition1 = new Transition("TestName1", triggerEvent1, initialState, outcomeState);
        Transition transition2 = new Transition("TestName2", triggerEvent2, initialState, outcomeState);
        assertNotEquals(transition1.hashCode(), transition2.hashCode());
    }

    // Test case for toString method
    @Test
    public void testToString() {
        IState initialState = new DummyState("InitialState");
        IState outcomeState = new DummyState("OutcomeState");
        Event triggerEvent = new DummyEvent("TestEvent");
        Transition transition = new Transition("TestName", triggerEvent, initialState, outcomeState);
        String expected = "Transition{name='TestName', initialState=" + initialState.toString() + ", outcomeState=" + outcomeState.toString() + ", triggerEvent=" + triggerEvent.toString() + '}';
        assertEquals(expected, transition.toString());
    }

    // Test case for trigger method with valid state and event
    @Test
    public void testTriggerValidStateAndEvent() {
        IState initialState = new DummyState("InitialState");
        IState outcomeState = new DummyState("OutcomeState");
        Event triggerEvent = new DummyEvent("TestEvent");
        Transition transition = new Transition("TestName", triggerEvent, initialState, outcomeState);
        IState result = transition.trigger(initialState, triggerEvent);
        assertEquals(outcomeState, result);
    }

    // Test case for trigger method with invalid state and event
    @Test
    public void testTriggerInvalidStateAndEvent() {
        IState initialState = new DummyState("InitialState");
        IState outcomeState = new DummyState("OutcomeState");
        Event triggerEvent = new DummyEvent("TestEvent");
        Transition transition = new Transition("TestName", triggerEvent, initialState, outcomeState);
        IState differentState = new DummyState("DifferentState");
        IState result = transition.trigger(differentState, triggerEvent);
        assertNotEquals(outcomeState, result);
    }

    // Test case for willTrigger method with valid state and event
    @Test
    public void testWillTriggerValidStateAndEvent() {
        IState initialState = new DummyState("InitialState");
        IState outcomeState = new DummyState("OutcomeState");
        Event triggerEvent = new DummyEvent("TestEvent");
        Transition transition = new Transition("TestName", triggerEvent, initialState, outcomeState);
        boolean result = transition.willTrigger(initialState, triggerEvent);
        assertTrue(result);
    }

    // Test case for willTrigger method with invalid state and event
    @Test
    public void testWillTriggerInvalidStateAndEvent() {
        IState initialState = new DummyState("InitialState");
        IState outcomeState = new DummyState("OutcomeState");
        Event triggerEvent = new DummyEvent("TestEvent");
        Transition transition = new Transition("TestName", triggerEvent, initialState, outcomeState);
        IState differentState = new DummyState("DifferentState");
        boolean result = transition.willTrigger(differentState, triggerEvent);
        assertFalse(result);
    }

    // Test case for willTrigger method with valid state and different event
    @Test
    public void testWillTriggerValidStateAndDifferentEvent() {
        IState initialState = new DummyState("InitialState");
        IState outcomeState = new DummyState("OutcomeState");
        Event triggerEvent = new DummyEvent("TestEvent");
        Event differentEvent = new DummyEvent("DifferentEvent");
        Transition transition = new Transition("TestName", triggerEvent, initialState, outcomeState);
        boolean result = transition.willTrigger(initialState, differentEvent);
        assertFalse(result);
    }
}