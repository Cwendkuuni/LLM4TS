package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.me.jockmacmad.jstate.predicate.*;

@RunWith(MockitoJUnitRunner.class)
public class TransitionTest {

    @Mock
    private IState initialState;

    @Mock
    private IState outcomeState;

    @Mock
    private Event triggerEvent;

    @Mock
    private IPredicate predicate1;

    @Mock
    private IGuardCondition guardCondition1;

    @InjectMocks
    private Transition transition;

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("Test1", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("Test1", triggerEvent, initialState, outcomeState);

        assertTrue(transition1.equals(transition2));
        assertTrue(transition2.equals(transition1));

        Transition transition3 = new Transition("Test2", triggerEvent, initialState, outcomeState);

        assertFalse(transition1.equals(transition3));
        assertFalse(transition3.equals(transition1));
    }

    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("Test1", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("Test1", triggerEvent, initialState, outcomeState);

        assertEquals(transition1.hashCode(), transition2.hashCode());

        Transition transition3 = new Transition("Test2", triggerEvent, initialState, outcomeState);

        assertNotEquals(transition1.hashCode(), transition3.hashCode());
    }

    @Test
    public void testToString() {
        String expectedToString = "Transition{name='Test1', triggerEvent=TestEvent, initialState=TestState, outcomeState=TestState}";
        when(initialState.toString()).thenReturn("TestState");
        when(outcomeState.toString()).thenReturn("TestState");
        when(triggerEvent.toString()).thenReturn("TestEvent");

        String actualToString = transition.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testTrigger() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(initialState.equals(outcomeState)).thenReturn(false);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        transition.addGuardCondition(predicate1);

        transition.guardConditions.add(guardCondition1);

        when(predicate1.evaluate(triggerEvent)).thenReturn(true);
        when(guardCondition1.evaluate(triggerEvent)).thenReturn(true);

        IState currentState = mock(IState.class);

        IState expectedOutcomeState = outcomeState;

        IState result = transition.trigger(currentState, triggerEvent);

        assertEquals(expectedOutcomeState, result);
    }

    @Test
    public void testTrigger_initialStateNotEqual() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(initialState.equals(outcomeState)).thenReturn(false);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        transition.addGuardCondition(predicate1);

        transition.guardConditions.add(guardCondition1);

        when(predicate1.evaluate(triggerEvent)).thenReturn(true);
        when(guardCondition1.evaluate(triggerEvent)).thenReturn(false);

        IState currentState = mock(IState.class);

        IState expectedCurrentState = currentState;

        IState result = transition.trigger(currentState, triggerEvent);

        assertSame(expectedCurrentState, result);
    }

    @Test
    public void testTrigger_triggerEventNotEqual() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(initialState.equals(outcomeState)).thenReturn(false);
        when(triggerEvent.equals(triggerEvent)).thenReturn(false);

        transition.addGuardCondition(predicate1);

        transition.guardConditions.add(guardCondition1);

        when(predicate1.evaluate(triggerEvent)).thenReturn(true);
        when(guardCondition1.evaluate(triggerEvent)).thenReturn(true);

        IState currentState = mock(IState.class);

        IState result = transition.trigger(currentState, triggerEvent);

        assertSame(currentState, result);
    }

    @Test
    public void testTrigger_noGuards() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(initialState.equals(outcomeState)).thenReturn(false);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        transition.addGuardCondition(predicate1);

        transition.guardConditions.clear();

        IState currentState = mock(IState.class);

        IState result = transition.trigger(currentState, triggerEvent);

        assertSame(currentState, result);
    }

    @Test
    public void testWillTrigger() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(initialState.equals(outcomeState)).thenReturn(false);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        transition.addGuardCondition(predicate1);

        transition.guardConditions.add(guardCondition1);

        when(predicate1.evaluate(triggerEvent)).thenReturn(true);
        when(guardCondition1.evaluate(triggerEvent)).thenReturn(true);

        boolean result = transition.willTrigger(initialState, triggerEvent);

        assertTrue(result);
    }

    @Test
    public void testWillTrigger_initialStateNotEqual() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(initialState.equals(outcomeState)).thenReturn(false);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        transition.addGuardCondition(predicate1);

        transition.guardConditions.add(guardCondition1);

        when(predicate1.evaluate(triggerEvent)).thenReturn(true);
        when(guardCondition1.evaluate(triggerEvent)).thenReturn(false);

        boolean result = transition.willTrigger(mock(IState.class), triggerEvent);

        assertFalse(result);
    }

    @Test
    public void testWillTrigger_triggerEventNotEqual() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(initialState.equals(outcomeState)).thenReturn(false);
        when(triggerEvent.equals(triggerEvent)).thenReturn(false);

        transition.addGuardCondition(predicate1);

        transition.guardConditions.add(guardCondition1);

        when(predicate1.evaluate(triggerEvent)).thenReturn(true);
        when(guardCondition1.evaluate(triggerEvent)).thenReturn(true);

        boolean result = transition.willTrigger(initialState, triggerEvent);

        assertFalse(result);
    }

    @Test
    public void testWillTrigger_noGuards() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(initialState.equals(outcomeState)).thenReturn(false);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        transition.addGuardCondition(predicate1);

        transition.guardConditions.clear();

        boolean result = transition.willTrigger(initialState, triggerEvent);

        assertFalse(result);
    }
}