package uk.me.jockmacmad.jstate.state.test;

import org.junit.Test;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.*;
import uk.me.jockmacmad.jstate.state.test.*;
import uk.me.jockmacmad.jstate.event.*;
import uk.me.jockmacmad.jstate.predicate.*;

public class TransitionTest {

    @Test
    public void testTrigger() {
        // Given
        String transitionName = "TestTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();
        Transition transition = new Transition(transitionName, triggerEvent, initialState, outcomeState);

        // When
        IState result = transition.trigger(initialState, triggerEvent);

        // Then
        assertEquals(outcomeState, result);
    }

    @Test
    public void testWillTrigger() {
        // Given
        String transitionName = "TestTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();
        Transition transition = new Transition(transitionName, triggerEvent, initialState, outcomeState);

        // When
        boolean result = transition.willTrigger(initialState, triggerEvent);

        // Then
        assertTrue(result);
    }

    @Test
    public void testTrigger_initialStateDoesNotMatch() {
        // Given
        String transitionName = "TestTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();
        Transition transition = new Transition(transitionName, triggerEvent, initialState, outcomeState);
        IState differentState = new DummyInitialState();

        // When
        IState result = transition.trigger(differentState, triggerEvent);

        // Then
        assertNotSame(outcomeState, result);
        assertEquals(initialState, result);
    }

    @Test
    public void testTrigger_triggerEventDoesNotMatch() {
        // Given
        String transitionName = "TestTransition";
        Event triggerEvent1 = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();
        Transition transition = new Transition(transitionName, triggerEvent1, initialState, outcomeState);
        Event differentEvent = new DummyEvent();

        // When
        IState result = transition.trigger(initialState, differentEvent);

        // Then
        assertNotSame(outcomeState, result);
        assertEquals(initialState, result);
    }
}