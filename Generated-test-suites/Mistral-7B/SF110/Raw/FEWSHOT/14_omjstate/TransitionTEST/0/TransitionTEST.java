package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.mockito.Mockito;
import uk.me.jockmacmad.jstate.predicate.Predicate;
import uk.me.jockmacmad.jstate.state.Transition;

public class TransitionTest {

    @Test
    public void testEquals() {
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        Event triggerEvent = Mockito.mock(Event.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        Transition transition1 = new Transition("Transition1", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("Transition1", triggerEvent, initialState, outcomeState);
        Transition transition3 = new Transition("Transition2", triggerEvent, initialState, outcomeState);

        assertEquals(transition1, transition1);
        assertEquals(transition1, transition2);
        assertNotEquals(transition1, transition3);

        assertNotEquals(transition1, null);
        assertNotEquals(transition1, new Object());
    }

    @Test
    public void testHashCode() {
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        Event triggerEvent = Mockito.mock(Event.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        Transition transition = new Transition("Transition1", triggerEvent, initialState, outcomeState);

        IState initialStateMock = Mockito.mock(IState.class);
        IState outcomeStateMock = Mockito.mock(IState.class);
        Event triggerEventMock = Mockito.mock(Event.class);
        List<IGuardCondition> guardConditions2 = new ArrayList<>();
        Transition transition2 = new Transition("Transition1", triggerEventMock, initialStateMock, outcomeStateMock);

        assertEquals(transition.hashCode(), transition.hashCode());
        assertEquals(transition.hashCode(), transition2.hashCode());

        Predicate predicate = Mockito.mock(Predicate.class);
        transition.addGuardCondition(predicate);

        int initialHashCode = transition.hashCode();
        transition.addGuardCondition(predicate);
        assertTrue(initialHashCode != transition.hashCode());
    }

    @Test
    public void testToString() {
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        Event triggerEvent = Mockito.mock(Event.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        Transition transition = new Transition("Transition1", triggerEvent, initialState, outcomeState);

        String transitionString = ReflectionToStringBuilder.reflectionToString(transition, ToStringStyle.SHORT_PREFIX_STYLE);
        String expectedTransitionString = "Transition1[initialState=..., outcomeState=..., triggerEvent=..., guardConditions=[]]";

        assertTrue(transitionString.startsWith(expectedTransitionString));

        Predicate predicate = Mockito.mock(Predicate.class);
        transition.addGuardCondition(predicate);

        String transitionWithGuardsString = ReflectionToStringBuilder.reflectionToString(transition, ToStringStyle.SHORT_PREFIX_STYLE);
        String expectedTransitionWithGuardsString = "Transition1[initialState=..., outcomeState=..., triggerEvent=..., guardConditions=[predicate]]]";

        assertTrue(transitionWithGuardsString.startsWith(expectedTransitionWithGuardsString));
    }

    @Test
    public void testAddGuardCondition() {
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        Event triggerEvent = Mockito.mock(Event.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        Transition transition = new Transition("Transition1", triggerEvent, initialState, outcomeState);

        IGuardCondition guardCondition = Mockito.mock(IGuardCondition.class);

        transition.addGuardCondition(guardCondition);
        assertEquals(1, guardConditions.size());
        verify(guardCondition).setTransition(transition);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullGuardCondition() {
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        Event triggerEvent = Mockito.mock(Event.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        Transition transition = new Transition("Transition1", triggerEvent, initialState, outcomeState);

        IGuardCondition nullGuardCondition = null;
        transition.addGuardCondition(nullGuardCondition);
    }

    @Test
    public void testTrigger() {
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        Event triggerEvent = Mockito.mock(Event.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        Transition transition = new Transition("Transition1", triggerEvent, initialState, outcomeState);

        when(initialState.equals(initialState)).thenReturn(true);
        when(outcomeState.equals(outcomeState)).thenReturn(true);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        IState currentState = Mockito.mock(IState.class);
        IState expectedOutcomeState = Mockito.mock(IState.class);
        when(currentState.equals(initialState)).thenReturn(true);
        when(currentState.equals(expectedOutcomeState)).thenReturn(true);

        when(guardConditions.isEmpty()).thenReturn(true);

        transition.evaluateGuardConditions();

        when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(true);

        IState result = transition.trigger(currentState, triggerEvent);

        assertEquals(expectedOutcomeState, result);
    }

    @Test
    public void testTriggerWhenInitialStateNotMatches() {
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        Event triggerEvent = Mockito.mock(Event.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        Transition transition = new Transition("Transition1", triggerEvent, initialState, outcomeState);

        IState currentState = Mockito.mock(IState.class);
        when(currentState.equals(initialState)).thenReturn(false);

        transition.evaluateGuardConditions();

        assertFalse(transition.trigger(currentState, triggerEvent) instanceof outcomeState);
    }

    @Test
    public void testTriggerWhenTriggerEventNotMatches() {
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        Event triggerEvent = Mockito.mock(Event.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        Transition transition = new Transition("Transition1", triggerEvent, initialState, outcomeState);

        IState currentState = Mockito.mock(IState.class);
        when(currentState.equals(initialState)).thenReturn(true);

        transition.evaluateGuardConditions();

        Event differentEvent = Mockito.mock(Event.class);
        when(triggerEvent.equals(differentEvent)).thenReturn(false);

        assertFalse(transition.trigger(currentState, differentEvent) instanceof outcomeState);
    }

    @Test
    public void testTriggerWhenGuardsNotPassed() {
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        Event triggerEvent = Mockito.mock(Event.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        Transition transition = new Transition("Transition1", triggerEvent, initialState, outcomeState);

        IState currentState = Mockito.mock(IState.class);
        when(currentState.equals(initialState)).thenReturn(true);

        transition.addGuardCondition(Mockito.mock(Predicate.class));

        transition.evaluateGuardConditions();

        when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(false);

        assertFalse(transition.trigger(currentState, triggerEvent) instanceof outcomeState);
    }

    @Test
    public void testWillTriggerWhenInitialStateNotMatches() {
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        Event triggerEvent = Mockito.mock(Event.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        Transition transition = new Transition("Transition1", triggerEvent, initialState, outcomeState);

        IState currentState = Mockito.mock(IState.class);
        when(currentState.equals(initialState)).thenReturn(false);

        assertFalse(transition.willTrigger(currentState, triggerEvent));
    }

    @Test
    public void testWillTriggerWhenTriggerEventNotMatches() {
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        Event triggerEvent = Mockito.mock(Event.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        Transition transition = new Transition("Transition1", triggerEvent, initialState, outcomeState);

        IState currentState = Mockito.mock(IState.class);
        when(currentState.equals(initialState)).thenReturn(true);

        Event differentEvent = Mockito.mock(Event.class);
        when(triggerEvent.equals(differentEvent)).thenReturn(false);

        assertFalse(transition.willTrigger(currentState, differentEvent));
    }

    @Test
    public void testWillTriggerWhenGuardsNotPassed() {
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        Event triggerEvent = Mockito.mock(Event.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        Transition transition = new Transition("Transition1", triggerEvent, initialState, outcomeState);

        IState currentState = Mockito.mock(IState.class);
        when(currentState.equals(initialState)).thenReturn(true);

        transition.addGuardCondition(Mockito.mock(Predicate.class));

        transition.evaluateGuardConditions();

        when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(false);

        assertFalse(transition.willTrigger(currentState, triggerEvent));
    }

    @Test
    public void testWillTriggerWhenInitialStateAndTriggerEventMatches() {
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        Event triggerEvent = Mockito.mock(Event.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        Transition transition = new Transition("Transition1", triggerEvent, initialState, outcomeState);

        IState currentState = Mockito.mock(IState.class);
        when(currentState.equals(initialState)).thenReturn(true);

        when(guardConditions.isEmpty()).thenReturn(true);

        IGuardCondition guardCondition = Mockito.mock(Predicate.class);
        transition.addGuardCondition(guardCondition);

        when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(true);

        assertTrue(transition.willTrigger(currentState, triggerEvent));
    }

    @Test
    public void testWillTriggerWhenGuardsPassed() {
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        Event triggerEvent = Mockito.mock(Event.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        Transition transition = new Transition("Transition1", triggerEvent, initialState, outcomeState);

        IState currentState = Mockito.mock(IState.class);
        when(currentState.equals(initialState)).thenReturn(true);

        transition.addGuardCondition(Mockito.mock(Predicate.class));
        transition.addGuardCondition(Mockito.mock(Predicate.class));

        transition.evaluateGuardConditions();

        when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(true);
        when(guardConditions.get(1).evaluate(triggerEvent)).thenReturn(true);

        assertTrue(transition.willTrigger(currentState, triggerEvent));
    }
}