package uk.me.jockmacmad.jstate.state.test;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.MockPredicate;
import uk.me.jockmacmad.jstate.state.Transition;
import uk.me.jockmacmad.jstate.state.State;
import uk.me.jockmacmad.jstate.state.StateImpl;
import uk.me.jockmacmad.jstate.event.MockEvent;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransitionTest
{
    private Transition transition;
    private IState initialState;
    private IState outcomeState;
    private Event triggerEvent;
    private List<IGuardCondition> guardConditions;

    @Before
    public void setup()
    {
        this.initialState = new StateImpl("Initial State");
        this.outcomeState = new StateImpl("Outcome State");
        this.triggerEvent = new MockEvent("Mock Event");
        this.guardConditions = new ArrayList<>();

        this.transition = new Transition("Mock Transition", this.triggerEvent, this.initialState, this.outcomeState);
    }

    @Test
    public void testInitialization()
    {
        assertEquals(this.initialState, this.transition.getInitialState());
        assertEquals(this.outcomeState, this.transition.getOutcomeState());
        assertEquals(this.triggerEvent, this.transition.getTriggerEvent());
        assertEquals("Mock Transition", this.transition.getName());
        assertTrue(this.transition.getGuardConditions().isEmpty());
    }

    @Test
    public void testAddGuardConditions()
    {
        IPredicate mockPredicate = new MockPredicate();
        this.transition.addGuardCondition(mockPredicate);
        this.transition.addGuardCondition(new MockGuardCondition());

        assertEquals(2, this.transition.getGuardConditions().size());
        assertTrue(this.transition.getGuardConditions().contains(mockPredicate));
        assertTrue(this.transition.getGuardConditions().contains(new MockGuardCondition()));
    }

    @Test
    public void testEquals()
    {
        Transition transition1 = new Transition("Mock Transition", this.triggerEvent, this.initialState, this.outcomeState);
        Transition transition2 = new Transition("Mock Transition", this.triggerEvent, this.initialState, this.outcomeState);

        assertEquals(transition1, transition1);
        assertEquals(transition1, transition2);
        assertFalse(transition1.equals(null));
        assertFalse(transition1.equals("Mock String"));

        transition1.addGuardCondition(new MockGuardCondition());
        assertFalse(transition1.equals(transition2));
    }

    @Test
    public void testHashCode()
    {
        Transition transition1 = new Transition("Mock Transition", this.triggerEvent, this.initialState, this.outcomeState);
        Transition transition2 = new Transition("Mock Transition", this.triggerEvent, this.initialState, this.outcomeState);

        assertEquals(transition1.hashCode(), transition1.hashCode());
        assertEquals(transition1.hashCode(), transition2.hashCode());
        assertFalse(transition1.hashCode() == 1L);

        transition1.addGuardCondition(new MockGuardCondition());
        assertFalse(transition1.hashCode() == transition2.hashCode());
    }

    @Test
    public void testToString()
    {
        String expectedToString = "Transition{name='Mock Transition', initialState=Initial State, outcomeState=Outcome State, triggerEvent=Mock Event, guardConditions=[]}";
        String actualToString = this.transition.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testTrigger()
    {
        State currentState = new StateImpl("Current State");

        this.transition.addGuardCondition(new MockGuardCondition() {
            @Override
            public boolean evaluate(Event event)
            {
                return true;
            }
        });

        IState expectedOutcomeState = this.outcomeState;
        IState actualOutcomeState = this.transition.trigger(currentState, this.triggerEvent);

        assertEquals(expectedOutcomeState, actualOutcomeState);
    }

    @Test
    public void testWillTrigger()
    {
        State currentState = new StateImpl("Current State");

        this.transition.addGuardCondition(new MockGuardCondition() {
            @Override
            public boolean evaluate(Event event)
            {
                return true;
            }
        });

        boolean expectedWillTrigger = true;
        boolean actualWillTrigger = this.transition.willTrigger(currentState, this.triggerEvent);

        assertEquals(expectedWillTrigger, actualWillTrigger);
    }

    @Test
    public void testWillNotTrigger()
    {
        State currentState = new StateImpl("Current State");

        this.transition.addGuardCondition(new MockGuardCondition() {
            @Override
            public boolean evaluate(Event event)
            {
                return false;
            }
        });

        boolean expectedWillTrigger = false;
        boolean actualWillTrigger = this.transition.willTrigger(currentState, this.triggerEvent);

        assertEquals(expectedWillTrigger, actualWillTrigger);
    }
}

class MockGuardCondition implements IGuardCondition
{
    @Override
    public boolean evaluate(Event event)
    {
        return true;
    }
}