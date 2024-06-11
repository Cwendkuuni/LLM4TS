package uk.me.jockmacmad.jstate.state;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.*;
import java.util.*;

public class TransitionTest {
    
    Transition validTransition;
    Transition invalidInitialStateTransition;
    Transition invalidEventTransition;
    Transition nullInitialStateTransition;
    Transition nullOutcomeStateTransition;
    Transition nullGuardConditionTransition;
    Transition emptyGuardConditionsTransition;
    
    @Before
    public void setUp() {
        IState initialState = new StateA();
        IState outcomeState = new StateB();
        Event triggerEvent = new EventA();

        validTransition = new Transition("TransitionA", triggerEvent, initialState, outcomeState);
        invalidInitialStateTransition = new Transition("TransitionA", triggerEvent, null, outcomeState);
        invalidEventTransition = new Transition("TransitionA", null, initialState, outcomeState);
        nullInitialStateTransition = new Transition(null, triggerEvent, initialState, outcomeState);
        nullOutcomeStateTransition = new Transition("TransitionA", triggerEvent, initialState, null);
        nullGuardConditionTransition = new Transition("TransitionA", triggerEvent, initialState, outcomeState);
        GuardCondition guardCondition = new AndGuardCondition(new IsEqualPredicate("eventA", EventA.class), new IsEqualPredicate("stateB", StateB.class));
        emptyGuardConditionsTransition = new Transition("TransitionA", triggerEvent, initialState, outcomeState);
        emptyGuardConditionsTransition.addGuardCondition(guardCondition);
    }
    
    @Test
    public void testTransitionWithValidInitialStateAndEvent() {
        IState retVal = validTransition.trigger(new StateA(), new EventA());
        Assert.assertEquals(outcomeState, retVal);
    }
    
    @Test
    public void testTransitionWithInvalidInitialStateAndEvent() {
        IState retVal = invalidInitialStateTransition.trigger(null, new EventA());
        Assert.assertEquals(initialState, retVal);
    }
    
    @Test
    public void testTransitionWithGuardsPassing() {
        IState retVal = emptyGuardConditionsTransition.trigger(new StateA(), new EventA());
        Assert.assertEquals(outcomeState, retVal);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTransitionWithGuardsFailing() {
        IState retVal = emptyGuardConditionsTransition.trigger(new StateC(), new EventA());
        Assert.assertEquals(initialState, retVal);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTriggerWithNullInitialState() {
        try {
            nullInitialStateTransition.trigger(null, new EventA());
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Initial state cannot be null", e.getMessage());
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTriggerWithNullOutcomeState() {
        try {
            nullOutcomeStateTransition.trigger(new StateA(), new EventA());
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Outcome state cannot be null", e.getMessage());
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTriggerWithNullGuardCondition() {
        try {
            nullGuardConditionTransition.addGuardCondition(null);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Guard condition cannot be null", e.getMessage());
        }
    }
    
    @Test
    public void testTriggerWithEmptyGuardConditionsList() {
        IState retVal = emptyGuardConditionsTransition.trigger(new StateA(), new EventA());
        Assert.assertEquals(outcomeState, retVal);
    }
    
    
}