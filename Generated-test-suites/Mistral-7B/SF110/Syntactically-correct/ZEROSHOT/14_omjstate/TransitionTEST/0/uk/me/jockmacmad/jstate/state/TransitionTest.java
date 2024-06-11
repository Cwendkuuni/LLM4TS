package uk.me.jockmacmad.jstate.state;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.me.jockmacmad.jstate.predicate.DummyPredicate;
import uk.me.jockmacmad.jstate.predicate.GuardCondition;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TransitionTest {

    private Transition transition;
    private IState initialState;
    private IState outcomeState;
    private Event triggerEvent;

    @Before
    public void setUp() {
        initialState = Mockito.mock(IState.class);
        outcomeState = Mockito.mock(IState.class);
        triggerEvent = new Event("testEvent");
        transition = new Transition("testName", triggerEvent, initialState, outcomeState);
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("testName", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("testName", triggerEvent, initialState, outcomeState);
        Transition transition3 = new Transition("testName2", triggerEvent, initialState, outcomeState);
        Transition transition4 = new Transition("testName", triggerEvent, initialState, outcomeState);

        assertEquals(transition, transition1);
        assertEquals(transition, transition4);
        assertNotEquals(transition, transition2);
        assertNotEquals(transition, transition3);
    }

    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("testName", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("testName", triggerEvent, initialState, outcomeState);
        Transition transition3 = new Transition("testName2", triggerEvent, initialState, outcomeState);

        assertEquals(transition.hashCode(), transition1.hashCode());
        assertNotEquals(transition.hashCode(), transition2.hashCode());
        assertNotEquals(transition.hashCode(), transition3.hashCode());
    }

    @Test
    public void testToString() {
        Transition transition = new Transition("testName", triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(new DummyPredicate());

        String expected = "Transition{name='testName', triggerEvent=Event{name='testEvent'}, initialState=MockState{}, outcomeState=MockState{}, guardConditions=[DummyPredicate]}" +
                "\nAt uk.me.jockmacmad.jstate.state.Transition";
        String actual = transition.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testAddGuardCondition() {
        Transition transition = new Transition("testName", triggerEvent, initialState, outcomeState);

        GuardCondition guardCondition = Mockito.mock(GuardCondition.class);
        transition.addGuardCondition(guardCondition);

        List<IGuardCondition> guardConditions = transition.getGuardConditions();
        assertEquals(1, guardConditions.size());
        assertEquals(guardCondition, guardConditions.get(0));
    }

    @Test
    public void testTrigger() {
        Transition transition = new Transition("testName", triggerEvent, initialState, outcomeState);

        when(initialState.equals(initialState)).thenReturn(true);
        when(initialState.equals(outcomeState)).thenReturn(false);
        when(outcomeState.equals(outcomeState)).thenReturn(true);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        IState state = initialState;
        IState expectedOutcomeState = outcomeState;

        transition.addGuardCondition(new DummyPredicate());

        IState result = transition.trigger(state, triggerEvent);

        assertEquals(expectedOutcomeState, result);
    }

    @Test
    public void testWillTrigger() {
        Transition transition = new Transition("testName", triggerEvent, initialState, outcomeState);

        when(initialState.equals(initialState)).thenReturn(true);
        when(initialState.equals(outcomeState)).thenReturn(false);
        when(outcomeState.equals(outcomeState)).thenReturn(true);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        boolean willTrigger = transition.willTrigger(initialState, triggerEvent);

        assertTrue(willTrigger);

        when(initialState.equals(initialState)).thenReturn(false);

        willTrigger = transition.willTrigger(initialState, triggerEvent);

        assertFalse(willTrigger);
    }

    @Test
    public void testEvaluateGuardConditions() {
        Transition transition = new Transition("testName", triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(new DummyPredicate());

        Event event = new Event("testEvent");

        assertTrue(transition.evaluateGuardConditions(event));
    }

    @Test(expected = IllegalStateException.class)
    public void testTriggerWithoutInitialStateMatch() {
        Transition transition = new Transition("testName", triggerEvent, initialState, outcomeState);
        IState differentState = Mockito.mock(IState.class);

        IState result = transition.trigger(differentState, triggerEvent);

        fail("Expected IllegalStateException not thrown");
    }

    @Test(expected = IllegalStateException.class)
    public void testTriggerWithoutEventMatch() {
        Transition transition = new Transition("testName", triggerEvent, initialState, outcomeState);
        Event differentEvent = new Event("testEvent2");

        IState result = transition.trigger(initialState, differentEvent);

        fail("Expected IllegalStateException not thrown");
    }
}