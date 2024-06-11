package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Before;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.*;

public class TransitionTest
{
    private Transition transition;
    private IState initialState;
    private IState outcomeState;
    private Event triggerEvent;

    @Before
    public void setUp()
    {
        initialState = new State("InitialState");
        outcomeState = new State("OutcomeState");
        triggerEvent = new Event("TriggerEvent");
        transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
    }

    @Test
    public void testConstructor()
    {
        assertNotNull(transition);
        assertEquals("TransitionName", transition.getName());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
        assertEquals(0, transition.getGuardConditions().size());
    }

    @Test
    public void testAddGuardCondition()
    {
        IPredicate predicate = new AlwaysPredicate();
        transition.addGuardCondition(predicate);
        assertEquals(1, transition.getGuardConditions().size());
    }

    @Test
    public void testEquals()
    {
        Transition transition1 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
        Transition transition3 = new Transition("AnotherTransitionName", triggerEvent, initialState, outcomeState);

        assertTrue(EqualsBuilder.reflectionEquals(transition, transition));
        assertTrue(EqualsBuilder.reflectionEquals(transition, transition1));
        assertFalse(EqualsBuilder.reflectionEquals(transition, transition3));
    }

    @Test
    public void testHashcode()
    {
        Transition transition1 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
        Transition transition3 = new Transition("AnotherTransitionName", triggerEvent, initialState, outcomeState);

        assertEquals(HashCodeBuilder.reflectionHashCode(transition), HashCodeBuilder.reflectionHashCode(transition1));
        assertNotEquals(HashCodeBuilder.reflectionHashCode(transition), HashCodeBuilder.reflectionHashCode(transition3));
    }

    @Test
    public void testToString()
    {
        assertEquals("TransitionName{initialState=InitialState, outcomeState=OutcomeState, triggerEvent=TriggerEvent}", ToStringBuilder.reflectionToString(transition));
    }

    @Test
    public void testTrigger_GivenInitialStateAndTriggerEvent_ReturnsOutcomeState()
    {
        transition.evaluateGuardConditions(); // assuming evaluateGuardConditions() does not throw any exception
        IState result = transition.trigger(initialState, triggerEvent);
        assertEquals(outcomeState, result);
    }

    @Test
    public void testTrigger_GivenInitialStateButDifferentTriggerEvent_ReturnsInitialState()
    {
        Event differentEvent = new Event("DifferentTriggerEvent");
        IState result = transition.trigger(initialState, differentEvent);
        assertEquals(initialState, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTrigger_GivenDifferentInitialState_ThrowsException()
    {
        IState differentState = new State("DifferentInitialState");
        transition.trigger(differentState, triggerEvent);
    }

    @Test
    public void testWillTrigger_GivenInitialStateAndTriggerEvent_ReturnsTrue()
    {
        transition.evaluateGuardConditions(); // assuming evaluateGuardConditions() does not throw any exception
        assertTrue(transition.willTrigger(initialState, triggerEvent));
    }

    @Test
    public void testWillTrigger_GivenInitialStateButDifferentTriggerEvent_ReturnsFalse()
    {
        assertFalse(transition.willTrigger(initialState, new Event("DifferentTriggerEvent")));
    }

    @Test
    public void testWillTrigger_GivenDifferentInitialState_ReturnsFalse()
    {
        assertFalse(transition.willTrigger(new State("DifferentInitialState"), triggerEvent));
    }
}