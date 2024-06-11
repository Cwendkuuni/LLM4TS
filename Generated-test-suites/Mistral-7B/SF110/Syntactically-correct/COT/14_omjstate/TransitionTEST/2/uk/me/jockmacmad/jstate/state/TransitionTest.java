package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private Transition transition;

    @Test
    public void testTransitionConstruction() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);

        transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(name, transition.getName());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testAddGuardCondition() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        IPredicate predicate = Mockito.mock(IPredicate.class);
        IGuardCondition guardCondition = Mockito.spy(new GuardCondition(predicate));

        transition = new Transition(name, triggerEvent, initialState, outcomeState);

        transition.addGuardCondition(guardCondition);

        assertEquals(1, transition.getGuardConditions().size());
        verify(transition).evaluateGuardConditions();
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("TestTransition1", Mockito.mock(Event.class), Mockito.mock(IState.class), Mockito.mock(IState.class));
        Transition transition2 = new Transition("TestTransition1", Mockito.mock(Event.class), Mockito.mock(IState.class), Mockito.mock(IState.class));

        assertEquals(transition1, transition1);
        assertEquals(transition1, transition2);

        Transition transition3 = new Transition("TestTransition2", Mockito.mock(Event.class), Mockito.mock(IState.class), Mockito.mock(IState.class));

        assertNotEquals(transition1, transition3);
    }

    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("TestTransition1", Mockito.mock(Event.class), Mockito.mock(IState.class), Mockito.mock(IState.class));
        Transition transition2 = new Transition("TestTransition1", Mockito.mock(Event.class), Mockito.mock(IState.class), Mockito.mock(IState.class));

        assertEquals(transition1.hashCode(), transition1.hashCode());
        assertEquals(transition1.hashCode(), transition2.hashCode());

        Transition transition3 = new Transition("TestTransition2", Mockito.mock(Event.class), Mockito.mock(IState.class), Mockito.mock(IState.class));

        assertNotEquals(transition1.hashCode(), transition3.hashCode());
    }

    @Test
    public void testToString() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);

        transition = new Transition(name, triggerEvent, initialState, outcomeState);

        String expected = "Transition{name='" + name + "', triggerEvent=" + triggerEvent + ", initialState=" + initialState + ", outcomeState=" + outcomeState + "}";
        String actual = transition.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testTrigger_initialStateAndTriggerEvent_noGuards() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);

        transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = initialState;
        IState expectedOutcomeState = outcomeState;

        IState actualOutcomeState = transition.trigger(currentState, triggerEvent);

        assertEquals(expectedOutcomeState, actualOutcomeState);
    }

    @Test
    public void testTrigger_initialStateAndTriggerEvent_guardsPass() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        IPredicate predicate = Mockito.mock(IPredicate.class);
        IGuardCondition guardCondition = Mockito.spy(new GuardCondition(predicate));

        transition = new Transition(name, triggerEvent, initialState, outcomeState);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition);
        transition.setGuardConditions(guardConditions);

        IState currentState = initialState;
        IState expectedOutcomeState = outcomeState;

        Mockito.when(guardCondition.evaluate(triggerEvent)).thenReturn(true);

        IState actualOutcomeState = transition.trigger(currentState, triggerEvent);

        assertEquals(expectedOutcomeState, actualOutcomeState);
    }

    @Test
    public void testTrigger_initialStateAndTriggerEvent_guardsFail() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);
        IPredicate predicate = Mockito.mock(IPredicate.class);
        IGuardCondition guardCondition = Mockito.spy(new GuardCondition(predicate));

        transition = new Transition(name, triggerEvent, initialState, outcomeState);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition);
        transition.setGuardConditions(guardConditions);

        IState currentState = initialState;
        IState expectedCurrentState = initialState;

        Mockito.when(guardCondition.evaluate(triggerEvent)).thenReturn(false);

        IState actualOutcomeState = transition.trigger(currentState, triggerEvent);

        assertEquals(expectedCurrentState, currentState);
    }

    @Test
    public void testWillTrigger_initialStateAndTriggerEvent_noGuards() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);

        transition = new Transition(name, triggerEvent, initialState, Mockito.mock(IState.class));

        boolean expectedResult = false;
        boolean actualResult = transition.willTrigger(initialState, triggerEvent);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testWillTrigger_initialStateAndTriggerEvent_guardsPass() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IPredicate predicate = Mockito.mock(IPredicate.class);
        IGuardCondition guardCondition = Mockito.spy(new GuardCondition(predicate));

        transition = new Transition(name, triggerEvent, initialState, Mockito.mock(IState.class));
        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition);
        transition.setGuardConditions(guardConditions);

        IPredicate mockPredicate = Mockito.mock(IPredicate.class);
        Mockito.when(guardCondition.getPredicate()).thenReturn(mockPredicate);
        Mockito.when(mockPredicate.evaluate(triggerEvent)).thenReturn(true);

        boolean expectedResult = true;
        boolean actualResult = transition.willTrigger(initialState, triggerEvent);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testWillTrigger_initialStateAndTriggerEvent_guardsFail() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IPredicate predicate = Mockito.mock(IPredicate.class);
        IGuardCondition guardCondition = Mockito.spy(new GuardCondition(predicate));

        transition = new Transition(name, triggerEvent, initialState, Mockito.mock(IState.class));
        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition);
        transition.setGuardConditions(guardConditions);

        IPredicate mockPredicate = Mockito.mock(IPredicate.class);
        Mockito.when(guardCondition.getPredicate()).thenReturn(mockPredicate);
        Mockito.when(mockPredicate.evaluate(triggerEvent)).thenReturn(false);

        boolean expectedResult = false;
        boolean actualResult = transition.willTrigger(initialState, triggerEvent);

        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTrigger_nullCurrentState() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);

        transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = null;
        IState expectedOutcomeState = outcomeState;

        IState actualOutcomeState = transition.trigger(currentState, triggerEvent);

        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTrigger_nullTriggerEvent() {
        String name = "TestTransition";
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);

        transition = new Transition(name, null, initialState, outcomeState);

        IState currentState = initialState;
        IState expectedOutcomeState = outcomeState;

        IState actualOutcomeState = transition.trigger(currentState, null);

        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalStateException.class)
    public void testTrigger_initialStateNotEqualToCurrentState() {
        String name = "TestTransition";
        Event triggerEvent = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);

        transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = Mockito.mock(IState.class);
        IState expectedOutcomeState = outcomeState;

        IState actualOutcomeState = transition.trigger(currentState, triggerEvent);

        assertNotEquals(expectedOutcomeState, currentState);
        fail("Expected IllegalStateException");
    }

    @Test(expected = IllegalStateException.class)
    public void testTrigger_triggerEventNotEqualToTriggerEvent() {
        String name = "TestTransition";
        Event triggerEvent1 = Mockito.mock(Event.class);
        Event triggerEvent2 = Mockito.mock(Event.class);
        IState initialState = Mockito.mock(IState.class);
        IState outcomeState = Mockito.mock(IState.class);

        transition = new Transition(name, triggerEvent1, initialState, outcomeState);

        IState currentState = initialState;
        IState expectedOutcomeState = outcomeState;

        IState actualOutcomeState = transition.trigger(currentState, triggerEvent2);

        assertNotEquals(expectedOutcomeState, currentState);
        fail("Expected IllegalStateException");
    }
}