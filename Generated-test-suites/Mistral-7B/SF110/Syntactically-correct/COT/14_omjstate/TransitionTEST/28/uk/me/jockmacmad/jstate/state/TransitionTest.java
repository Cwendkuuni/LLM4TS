package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TransitionTest {

    private Transition transition;
    private IState initialState;
    private IState outcomeState;
    private Event triggerEvent;
    private List<IGuardCondition> guardConditions;

    @Before
    public void setup() {
        initialState = new State("InitialState");
        outcomeState = new State("OutcomeState");
        triggerEvent = new Event("TriggerEvent");
        guardConditions = new ArrayList<>();
        transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
    }

    // Test constructor
    @Test
    public void testTransitionConstructor() {
        assertEquals("TransitionName", transition.getName());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
        assertTrue(guardConditions.isEmpty());
    }

    // Test addGuardCondition method
    @Test
    public void testAddGuardCondition() {
        IPredicate predicate = new AndPredicate(new EqualsPredicate("eventAttribute", "eventValue"), new EqualsPredicate("anotherAttribute", "anotherValue"));
        IGuardCondition guardCondition = new GuardCondition(predicate);
        transition.addGuardCondition(guardCondition);
        assertEquals(1, transition.getGuardConditions().size());
    }

    // Test equals method
    @Test
    public void testEquals() {
        Transition transition1 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
        Transition transition3 = new Transition("DifferentName", triggerEvent, initialState, outcomeState);

        assertTrue(EqualsBuilder.reflectionEquals(transition1, transition1));
        assertTrue(EqualsBuilder.reflectionEquals(transition1, transition2));
        assertFalse(EqualsBuilder.reflectionEquals(transition1, transition3));

        transition1 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
        transition2.guardConditions.add(new ConstantGuardCondition("eventAttribute", "eventValue"));

        assertFalse(EqualsBuilder.reflectionEquals(transition1, transition2));
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
        Transition transition3 = new Transition("DifferentName", triggerEvent, initialState, outcomeState);

        int hashCode1 = HashCodeBuilder.reflectionHashCode(transition1);
        int hashCode2 = HashCodeBuilder.reflectionHashCode(transition2);
        int hashCode3 = HashCodeBuilder.reflectionHashCode(transition3);

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);

        transition1.guardConditions.add(new ConstantGuardCondition("eventAttribute", "eventValue"));
        transition2.guardConditions.add(new ConstantGuardCondition("anotherAttribute", "anotherValue"));

        assertNotEquals(hashCode1, hashCode2);
    }

    // Test toString method
    @Test
    public void testToString() {
        String expectedToString = "TransitionName{initialState=InitialState, outcomeState=OutcomeState, triggerEvent=TriggerEvent, guardConditions=[]}";
        String toString = ToStringBuilder.reflectionToString(transition);
        assertEquals(expectedToString, toString);
    }

    // Test trigger method
    @Test(expected = IllegalArgumentException.class)
    public void testTriggerIllegalArgumentException() {
        transition.trigger(null, triggerEvent);
    }

    @Test
    public void testTriggerNormalCase() {
        transition.addGuardCondition(new ConstantGuardCondition("eventAttribute", "eventValue"));

        IState currentState = new State("CurrentState");
        IState expectedOutcomeState = outcomeState;

        IState actualOutcomeState = transition.trigger(initialState, triggerEvent);
        assertEquals(expectedOutcomeState, actualOutcomeState);
    }

    @Test
    public void testTriggerDoesNotPassGuards() {
        transition.addGuardCondition(new AndPredicate(new EqualsPredicate("eventAttribute", "eventValue"), new EqualsPredicate("anotherAttribute", "anotherValue")));

        IState currentState = new State("CurrentState");
        IState expectedOutcomeState = initialState;

        IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
        assertEquals(expectedOutcomeState, actualOutcomeState);
    }

    // Test willTrigger method
    @Test
    public void testWillTriggerNormalCase() {
        transition.addGuardCondition(new ConstantGuardCondition("eventAttribute", "eventValue"));

        IState currentState = new State("CurrentState");
        boolean expectedWillTrigger = true;

        boolean actualWillTrigger = transition.willTrigger(initialState, triggerEvent);
        assertEquals(expectedWillTrigger, actualWillTrigger);
    }

    @Test
    public void testWillTriggerDoesNotPassGuards() {
        transition.addGuardCondition(new AndPredicate(new EqualsPredicate("eventAttribute", "eventValue"), new EqualsPredicate("anotherAttribute", "anotherValue")));

        IState currentState = new State("CurrentState");
        boolean expectedWillTrigger = false;

        boolean actualWillTrigger = transition.willTrigger(currentState, triggerEvent);
        assertEquals(expectedWillTrigger, actualWillTrigger);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWillTriggerIllegalArgumentException() {
        transition.willTrigger(null, triggerEvent);
    }
}