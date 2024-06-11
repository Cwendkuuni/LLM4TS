package uk.me.jockmacmad.jstate.state;
import org.junit.Test;
import static org.junit.Assert.*;

import uk.me.jockmacmad.jstate.event.DummyEvent;
import uk.me.jockmacmad.jstate.guardcondition.DummyGuardCondition;
import uk.me.jockmacmad.jstate.state.DummyInitialState;
import uk.me.jockmacmad.jstate.state.DummyOutcomeState;
import uk.me.jockmacmad.jstate.state.Transition;

public class TransitionTest {
    @Test
    public void testTrigger() {
        // Given
        String name = "testTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        // When
        IState result = transition.trigger(initialState, triggerEvent);

        // Then
        assertEquals(outcomeState, result);
    }

    @Test
    public void testWillTrigger() {
        // Given
        String name = "testTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        // When
        boolean result = transition.willTrigger(initialState, triggerEvent);

        // Then
        assertTrue(result);
    }

    @Test
    public void testTrigger_WrongInitialState() {
        // Given
        String name = "testTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState(); // Different initial state
        IState outcomeState = new DummyOutcomeState();
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        // When
        IState result = transition.trigger(initialState, triggerEvent);

        // Then
        assertNotEquals(outcomeState, result);
    }

    @Test
    public void testTrigger_WrongTriggerEvent() {
        // Given
        String name = "testTransition";
        Event triggerEvent1 = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();
        Transition transition = new Transition(name, triggerEvent1, initialState, outcomeState);

        // When
        Event triggerEvent2 = new DummyEvent(); // Different trigger event
        IState result = transition.trigger(initialState, triggerEvent2);

        // Then
        assertNotEquals(outcomeState, result);
    }
}