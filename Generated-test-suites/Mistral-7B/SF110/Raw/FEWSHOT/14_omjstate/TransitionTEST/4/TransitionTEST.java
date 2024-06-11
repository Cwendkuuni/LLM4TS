package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TransitionTest {

    @Test
    public void testTransitionCreation() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(name, transition.getName());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
        assertEquals(0, transition.getGuardConditions().size());
    }

    @Test
    public void testAddGuardCondition() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);
        IPredicate predicate = mock(IPredicate.class);

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        transition.addGuardCondition(predicate);

        assertEquals(1, transition.getGuardConditions().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullGuardCondition() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        transition.addGuardCondition(null);
    }

    @Test
    public void testEquals() {
        String name1 = "testTransition1";
        String name2 = "testTransition2";
        Event triggerEvent1 = mock(Event.class);
        Event triggerEvent2 = mock(Event.class);
        IState initialState1 = mock(IState.class);
        IState initialState2 = mock(IState.class);
        IState outcomeState1 = mock(IState.class);
        IState outcomeState2 = mock(IState.class);
        List<IGuardCondition> guardConditions1 = new ArrayList<>();
        List<IGuardCondition> guardConditions2 = new ArrayList<>();

        Transition transition1 = new Transition(name1, triggerEvent1, initialState1, outcomeState1);
        Transition transition2 = new Transition(name2, triggerEvent2, initialState2, outcomeState2);

        transition1.addGuardCondition(mock(IGuardCondition.class));
        transition2.addGuardCondition(mock(IGuardCondition.class));

        assertFalse(transition1.equals(transition2));

        Transition identicalTransition = new Transition(name1, triggerEvent1, initialState1, outcomeState1);
        identicalTransition.addGuardCondition(mock(IGuardCondition.class));

        assertTrue(transition1.equals(identicalTransition));
    }

    @Test
    public void testHashCode() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);

        transition1.addGuardCondition(mock(IGuardCondition.class));

        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);
        transition2.addGuardCondition(mock(IGuardCondition.class));

        assertEquals(transition1.hashCode(), transition2.hashCode());
    }

    @Test
    public void testEvaluateGuardConditions_EmptyList() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(mock(IGuardCondition.class));
        transition.addGuardCondition(mock(IGuardCondition.class));
        transition.evaluateGuardConditions();

        verifyNoInteractions(guardConditions);
    }

    @Test
    public void testTrigger_InitialStateEqualsCurrentStateAndTriggerEventMatches() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        IPredicate predicate = mock(IPredicate.class);

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(predicate);

        IState currentState = initialState;

        IState result = transition.trigger(currentState, triggerEvent);

        assertEquals(outcomeState, result);
    }

    @Test
    public void testTrigger_InitialStateDoesNotEqualCurrentState() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);
        IState currentState = mock(IState.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertFalse(initialState.equals(currentState));

        transition.evaluateGuardConditions();

        verify(guardConditions).forEach(transition::evaluateGuardCondition);

        IState result = transition.trigger(currentState, triggerEvent);

        assertNotEquals(outcomeState, result);
    }

    @Test
    public void testTrigger_TriggerEventDoesNotMatch() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        Event wrongTriggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        transition.evaluateGuardConditions();

        verify(guardConditions).forEach(transition::evaluateGuardCondition);

        IState result = transition.trigger(initialState, wrongTriggerEvent);

        assertNotEquals(outcomeState, result);
    }

    @Test
    public void testWillTrigger_InitialStateEqualsCurrentStateAndTriggerEventMatches() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        IPredicate predicate = mock(IPredicate.class);

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(predicate);

        IState currentState = initialState;

        boolean result = transition.willTrigger(currentState, triggerEvent);

        assertTrue(result);
    }

    @Test
    public void testWillTrigger_InitialStateDoesNotEqualCurrentState() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);
        IState currentState = mock(IState.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertFalse(initialState.equals(currentState));

        transition.evaluateGuardConditions();

        verify(guardConditions).forEach(transition::evaluateGuardCondition);

        boolean result = transition.willTrigger(currentState, triggerEvent);

        assertFalse(result);
    }

    @Test
    public void testWillTrigger_TriggerEventDoesNotMatch() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        Event wrongTriggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        transition.evaluateGuardConditions();

        verify(guardConditions).forEach(transition::evaluateGuardCondition);

        boolean result = transition.willTrigger(initialState, wrongTriggerEvent);

        assertFalse(result);
    }

    @Test
    public void testToString() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);
        List<IGuardCondition> guardConditions = Arrays.asList(mock(IGuardCondition.class), mock(IGuardCondition.class));

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.addAll(guardConditions);

        String expected = String.format("Transition{name='%s', triggerEvent=%s, initialState=%s, outcomeState=%s, guardConditions=%s}",
                name, triggerEvent, initialState, outcomeState, guardConditions);

        assertEquals(expected, transition.toString());
    }
}