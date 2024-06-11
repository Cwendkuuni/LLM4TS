package uk.me.jockmacmad.jstate.state;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.predicate.*;
import java.util.ArrayList;
import java.util.List;

public class TransitionTest
{
    private Transition transition;
    private IState initialState;
    private IState outcomeState;
    private Event triggerEvent;
    private IPredicate predicate;
    private IGuardCondition guardCondition;

    @Before
    public void setUp()
    {
        this.initialState = new State("InitialState");
        this.outcomeState = new State("OutcomeState");
        this.triggerEvent = new Event("TriggerEvent");
        this.predicate = new AlwaysTruePredicate();
        this.guardCondition = new GuardCondition(this.predicate);
        this.transition = new Transition("TransitionName", this.triggerEvent, this.initialState, this.outcomeState);
        this.transition.addGuardCondition(this.guardCondition);
    }

    @Test
    public void testConstructor()
    {
        assertNotNull(this.transition);
        assertEquals("TransitionName", this.transition.getName());
        assertEquals(this.triggerEvent, this.transition.getTriggerEvent());
        assertEquals(this.initialState, this.transition.getInitialState());
        assertEquals(this.outcomeState, this.transition.getOutcomeState());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullTransitionName()
    {
        Transition transition = new Transition(null, this.triggerEvent, this.initialState, this.outcomeState);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullTriggerEvent()
    {
        Transition transition = new Transition("TransitionName", null, this.initialState, this.outcomeState);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullInitialState()
    {
        Transition transition = new Transition("TransitionName", this.triggerEvent, null, this.outcomeState);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullOutcomeState()
    {
        Transition transition = new Transition("TransitionName", this.triggerEvent, this.initialState, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullGuardCondition()
    {
        this.transition.addGuardCondition(null);
    }

    @Test
    public void testAddGuardCondition()
    {
        this.transition.addGuardCondition(this.guardCondition);
        List<IGuardCondition> guardConditions = this.transition.getGuardConditions();
        assertEquals(1, guardConditions.size());
        assertEquals(this.guardCondition, guardConditions.get(0));
    }

    @Test
    public void testAddMultipleGuardConditions()
    {
        IPredicate predicate2 = new AlwaysFalsePredicate();
        IGuardCondition guardCondition2 = new GuardCondition(predicate2);
        this.transition.addGuardCondition(this.guardCondition);
        this.transition.addGuardCondition(guardCondition2);
        List<IGuardCondition> guardConditions = this.transition.getGuardConditions();
        assertEquals(2, guardConditions.size());
        assertEquals(this.guardCondition, guardConditions.get(0));
        assertEquals(guardCondition2, guardConditions.get(1));
    }

    @Test
    public void testEquals()
    {
        Transition transition2 = new Transition("TransitionName", this.triggerEvent, this.initialState, this.outcomeState);
        transition2.addGuardCondition(this.guardCondition);
        assertTrue(this.transition.equals(this.transition));
        assertTrue(this.transition.equals(transition2));
        Transition transition3 = new Transition("TransitionName2", this.triggerEvent, this.initialState, this.outcomeState);
        assertFalse(this.transition.equals(transition3));
        Transition transition4 = new Transition("TransitionName", null, this.initialState, this.outcomeState);
        assertFalse(this.transition.equals(transition4));
    }

    @Test
    public void testHashCode()
    {
        Transition transition2 = new Transition("TransitionName", this.triggerEvent, this.initialState, this.outcomeState);
        transition2.addGuardCondition(this.guardCondition);
        int hashCode1 = this.transition.hashCode();
        int hashCode2 = transition2.hashCode();
        assertEquals(hashCode1, hashCode2);
        Transition transition3 = new Transition("TransitionName2", this.triggerEvent, this.initialState, this.outcomeState);
        transition3.addGuardCondition(this.guardCondition);
        assertNotEquals(hashCode1, hashCode3);
        Transition transition4 = new Transition("TransitionName", null, this.initialState, this.outcomeState);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testEvaluateGuardConditions()
    {
        this.transition.evaluateGuardConditions();
    }

    @Test
    public void testGetInitialState()
    {
        assertEquals(this.initialState, this.transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState()
    {
        assertEquals(this.outcomeState, this.transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent()
    {
        assertEquals(this.triggerEvent, this.transition.getTriggerEvent());
    }

    @Test
    public void testToString()
    {
        String transitionString = this.transition.toString();
        assertTrue(transitionString.contains("TransitionName"));
        assertTrue(transitionString.contains("TriggerEvent"));
        assertTrue(transitionString.contains("InitialState"));
        assertTrue(transitionString.contains("OutcomeState"));
    }

    @Test
    public void testTrigger()
    {
        this.transition.trigger(this.initialState, this.triggerEvent);
        assertEquals(this.outcomeState, this.transition.getOutcomeState());
        this.transition.trigger(this.outcomeState, this.triggerEvent);
        assertEquals(this.outcomeState, this.transition.getOutcomeState());
        this.transition.trigger(this.initialState, new Event("InvalidEvent"));
        assertEquals(this.initialState, this.transition.getOutcomeState());
    }

    @Test
    public void testWillTrigger()
    {
        assertTrue(this.transition.willTrigger(this.initialState, this.triggerEvent));
        assertFalse(this.transition.willTrigger(this.outcomeState, this.triggerEvent));
        assertFalse(this.transition.willTrigger(this.initialState, new Event("InvalidEvent")));
    }
}