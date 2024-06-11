package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import uk.me.jockmacmad.jstate.predicate.IPredicate;
import uk.me.jockmacmad.jstate.predicate.IGuardCondition;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TransitionTest {

    // Basic test case for constructor
    @Test
    public void testTransitionCreation() {
        String expectedName = "TestTransition";
        Event expectedTriggerEvent = Mockito.mock(Event.class);
        IState expectedInitialState = Mockito.mock(IState.class);
        IState expectedOutcomeState = Mockito.mock(IState.class);

        Transition transition = new Transition(expectedName, expectedTriggerEvent, expectedInitialState, expectedOutcomeState);

        assertEquals(expectedName, transition.getName());
        assertEquals(expectedTriggerEvent, transition.getTriggerEvent());
        assertEquals(expectedInitialState, transition.getInitialState());
        assertEquals(expectedOutcomeState, transition.getOutcomeState());
    }

    // Basic test case for getters
    @Test
    public void testGetters() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(name, transition.getName());
        assertEquals(triggerEvent, transition.getTriggerEvent());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
    }

    // Test case for equals and hashCode methods
    @Test
    public void testEqualsAndHashCodeMethods() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        IState differentInitialState = Mockito.mock(IState.class);
        IState differentOutcomeState = Mockito.mock(IState.class);

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);

        Transition transition3 = new Transition(name, triggerEvent, initialState, differentOutcomeState);
        Transition transition4 = new Transition(name, differentTriggerEvent, initialState, outcomeState);

        Transition transition5 = new Transition("differentName", triggerEvent, initialState, outcomeState);

        assertTrue(transition1.equals(transition2));
        assertFalse(transition1.equals(transition3));
        assertFalse(transition1.equals(transition4));
        assertFalse(transition1.equals(transition5));

        assertEquals(transition1.hashCode(), transition2.hashCode());
        assertNotSame(transition1.hashCode(), transition3.hashCode());
        assertNotSame(transition1.hashCode(), transition4.hashCode());
        assertNotSame(transition1.hashCode(), transition5.hashCode());
    }

    // Test case for addGuardCondition method
    @Test
    public void testAddGuardConditionMethod() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        List<IGuardCondition> expectedGuardConditions = Arrays.asList(Mockito.mock(IGuardCondition.class), Mockito.mock(IGuardCondition.class));

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        transition.addGuardCondition(Mockito.mock(IPredicate.class));
        transition.addGuardCondition(Mockito.mock(IGuardCondition.class));

        List<IGuardCondition> actualGuardConditions = transition.getGuardConditions();

        assertEquals(expectedGuardConditions, actualGuardConditions);
    }

    // Test case for trigger method with valid state and event
    @Test
    public void testTriggerMethodWithValidStateAndEvent() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState expectedOutcomeState = Mockito.mock(IState.class);

        Transition transition = new Transition(name, triggerEvent, initialState, expectedOutcomeState);

        IState currentState = Mockito.mock(IState.class);

        when(initialState.equals(currentState)).thenReturn(true);
        when(triggerEvent.equals(Mockito.any())).thenReturn(true);

        List<IGuardCondition> guardConditions = Arrays.asList(Mockito.mock(IGuardCondition.class), Mockito.mock(IGuardCondition.class));
        transition.setGuardConditions(guardConditions);

        for (IGuardCondition guardCondition : guardConditions) {
            when(guardCondition.evaluate(triggerEvent)).thenReturn(true);
        }

        IState result = transition.trigger(currentState, triggerEvent);

        assertEquals(expectedOutcomeState, result);
    }

    // Test case for trigger method with invalid state and event
    @Test
    public void testTriggerMethodWithInvalidStateAndEvent() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState expectedOutcomeState = Mockito.mock(IState.class);

        Transition transition = new Transition(name, triggerEvent, initialState, expectedOutcomeState);

        IState currentState = Mockito.mock(IState.class);

        when(initialState.equals(currentState)).thenReturn(false);

        List<IGuardCondition> guardConditions = Arrays.asList(Mockito.mock(IGuardCondition.class), Mockito.mock(IGuardCondition.class));
        transition.setGuardConditions(guardConditions);

        IState result = transition.trigger(currentState, triggerEvent);

        assertNotSame(expectedOutcomeState, result);
    }

    // Test case for willTrigger method with valid state and event
    @Test
    public void testWillTriggerMethodWithValidStateAndEvent() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState expectedOutcomeState = Mockito.mock(IState.class);

        Transition transition = new Transition(name, triggerEvent, initialState, expectedOutcomeState);

        IState currentState = Mockito.mock(IState.class);

        when(initialState.equals(currentState)).thenReturn(true);
        when(triggerEvent.equals(Mockito.any())).thenReturn(true);

        List<IGuardCondition> guardConditions = Arrays.asList(Mockito.mock(IGuardCondition.class), Mockito.mock(IGuardCondition.class));
        transition.setGuardConditions(guardConditions);

        for (IGuardCondition guardCondition : guardConditions) {
            when(guardCondition.evaluate(triggerEvent)).thenReturn(true);
        }

        boolean result = transition.willTrigger(currentState, triggerEvent);

        assertTrue(result);
    }

    // Test case for willTrigger method with invalid state and event
    @Test
    public void testWillTriggerMethodWithInvalidStateAndEvent() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState expectedOutcomeState = Mockito.mock(IState.class);

        Transition transition = new Transition(name, triggerEvent, initialState, expectedOutcomeState);

        IState currentState = Mockito.mock(IState.class);

        when(initialState.equals(currentState)).thenReturn(false);

        List<IGuardCondition> guardConditions = Arrays.asList(Mockito.mock(IGuardCondition.class), Mockito.mock(IGuardCondition.class));
        transition.setGuardConditions(guardConditions);

        boolean result = transition.willTrigger(currentState, triggerEvent);

        assertFalse(result);
    }

    // Test case for evaluateGuardConditions method
    @Test
    public void testEvaluateGuardConditionsMethod() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        List<IPredicate> predicates = Arrays.asList(Mockito.mock(IPredicate.class), Mockito.mock(IPredicate.class));

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        for (int i = 0; i < 2; i++) {
            IGuardCondition guardCondition = Mockito.mock(IGuardCondition.class);
            guardCondition.setPredicates(predicates.get(i));
            transition.addGuardCondition(guardCondition);
            guardConditions.add(guardCondition);
        }

        IState currentState = Mockito.mock(IState.class);

        when(initialState.equals(currentState)).thenReturn(true);
        when(triggerEvent.equals(Mockito.any())).thenReturn(true);

        for (IGuardCondition guardCondition : guardConditions) {
            when(guardCondition.evaluate(triggerEvent)).thenReturn(true);
        }

        transition.evaluateGuardConditions();

        // Verify that all guard conditions have been evaluated
        verify(guardConditions.get(0)).evaluate(triggerEvent);
        verify(guardConditions.get(1)).evaluate(triggerEvent);
    }

    // Test case for trigger method with null arguments
    @Test(expected = NullPointerException.class)
    public void testTriggerMethodWithNullArguments() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = null;
        Event event = null;

        transition.trigger(currentState, event);
    }

    // Test case for trigger method with empty guard conditions
    @Test
    public void testTriggerMethodWithEmptyGuardConditions() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState expectedOutcomeState = Mockito.mock(IState.class);

        Transition transition = new Transition(name, triggerEvent, initialState, expectedOutcomeState);
        List<IGuardCondition> emptyGuardConditions = new ArrayList<>();

        IState currentState = Mockito.mock(IState.class);
        when(initialState.equals(currentState)).thenReturn(true);
        when(triggerEvent.equals(Mockito.any())).thenReturn(true);

        transition.setGuardConditions(emptyGuardConditions);

        IState result = transition.trigger(currentState, triggerEvent);

        assertEquals(initialState, result);
    }

    // Test case for trigger method with null guard conditions
    @Test(expected = NullPointerException.class)
    public void testTriggerMethodWithNullGuardConditions() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState expectedOutcomeState = Mockito.mock(IState.class);

        Transition transition = new Transition(name, triggerEvent, initialState, expectedOutcomeState);

        List<IGuardCondition> nullGuardConditions = null;

        IState currentState = Mockito.mock(IState.class);
        when(initialState.equals(currentState)).thenReturn(true);
        when(triggerEvent.equals(Mockito.any())).thenReturn(true);

        transition.setGuardConditions(nullGuardConditions);
    }
}