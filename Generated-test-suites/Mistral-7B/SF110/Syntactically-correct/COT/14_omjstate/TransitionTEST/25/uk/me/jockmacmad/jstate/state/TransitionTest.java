package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.DummyPredicate;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.List;

public class TransitionTest
{
    @Test
    public void testTransitionConstructor()
    {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(name, transition.getName());
        assertEquals(triggerEvent, transition.getTriggerEvent());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testAddGuardCondition()
    {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IPredicate predicate = new DummyPredicate();
        transition.addGuardCondition(predicate);

        assertEquals(1, transition.getGuardConditions().size());
        assertEquals(predicate, transition.getGuardConditions().get(0));
    }

    @Test
    public void testEquals()
    {
        String name1 = "testTransition1";
        String name2 = "testTransition2";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");

        Transition transition1 = new Transition(name1, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name2, triggerEvent, initialState, outcomeState);

        assertFalse(EqualsBuilder.reflectionEquals(transition1, transition2));

        transition2.setName(name1);
        assertTrue(EqualsBuilder.reflectionEquals(transition1, transition2));
    }

    @Test
    public void testHashCode()
    {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);

        int hashCode1 = HashCodeBuilder.reflectionHashCode(transition1);
        int hashCode2 = HashCodeBuilder.reflectionHashCode(transition2);

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString()
    {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        String expectedToString = String.format("Transition{name='%s', triggerEvent=Event{name='%s'}, initialState=DummyState{stateName='initialState'}, outcomeState=DummyState{stateName='outcomeState'}}", name, triggerEvent.getName());
        String actualToString = ToStringBuilder.reflectionToString(transition);

        assertEquals(expectedToString, actualToString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTriggerInvalidInitialState()
    {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = null;
        IState outcomeState = new DummyState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = new DummyState("currentState");
        transition.trigger(currentState, triggerEvent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTriggerInvalidEvent()
    {
        String name = "testTransition";
        Event triggerEvent = null;
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = new DummyState("currentState");
        transition.trigger(currentState, new Event("invalidEvent"));
    }

    @Test(expected = IllegalStateException.class)
    public void testTriggerNoMatchingInitialState()
    {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("invalidInitialState");
        IState outcomeState = new DummyState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = new DummyState("currentState");
        transition.trigger(currentState, triggerEvent);
    }

    @Test(expected = IllegalStateException.class)
    public void testTriggerNoMatchingEvent()
    {
        String name = "testTransition";
        Event triggerEvent = new Event("invalidEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = new DummyState("currentState");
        transition.trigger(currentState, triggerEvent);
    }

    @Test
    public void testTrigger()
    {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = new DummyState("initialState");
        IState expectedOutcomeState = new DummyState("outcomeState");

        IState actualOutcomeState = transition.trigger(currentState, triggerEvent);

        assertEquals(expectedOutcomeState, actualOutcomeState);
    }

    @Test
    public void testWillTrigger()
    {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = new DummyState("initialState");
        boolean expectedWillTrigger = true;

        boolean actualWillTrigger = transition.willTrigger(currentState, triggerEvent);

        assertEquals(expectedWillTrigger, actualWillTrigger);
    }

    @Test
    public void testWillTriggerInvalidEvent()
    {
        String name = "testTransition";
        Event triggerEvent = new Event("invalidEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = new DummyState("currentState");

        boolean actualWillTrigger = transition.willTrigger(currentState, triggerEvent);

        assertFalse(actualWillTrigger);
    }

    @Test
    public void testWillTriggerNoMatchingInitialState()
    {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("invalidInitialState");
        IState outcomeState = new DummyState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = new DummyState("currentState");

        boolean actualWillTrigger = transition.willTrigger(currentState, triggerEvent);

        assertFalse(actualWillTrigger);
    }

    @Test
    public void testWillTriggerNoMatchingTransition()
    {
        String name = "testTransition";
        Event triggerEvent = new Event("invalidEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = new DummyState("currentState");

        boolean actualWillTrigger = transition.willTrigger(currentState, new Event("testEvent"));

        assertFalse(actualWillTrigger);
    }

    @Test
    public void testWillTrigger()
    {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = new DummyState("initialState");
        boolean expectedWillTrigger = true;

        boolean actualWillTrigger = transition.willTrigger(currentState, triggerEvent);

        assertEquals(expectedWillTrigger, actualWillTrigger);
    }
}