package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

public class TransitionTest {

    private Transition transition;
    private IState initialStateMock;
    private IState outcomeStateMock;
    private Event triggerEventMock;
    private List<IGuardCondition> guardConditionsMock;

    @Before
    public void setUp() {
        initialStateMock = mock(IState.class);
        outcomeStateMock = mock(IState.class);
        triggerEventMock = mock(Event.class);
        guardConditionsMock = new ArrayList<>();
        transition = new Transition("testName", triggerEventMock, initialStateMock, outcomeStateMock);
    }

    @Test
    public void testEquals() {
        Transition equalTransition = new Transition("testName", triggerEventMock, initialStateMock, outcomeStateMock);
        Transition notEqualTransition = new Transition("notTestName", triggerEventMock, initialStateMock, outcomeStateMock);
        Transition nullTransition = null;

        assertTrue(transition.equals(equalTransition));
        assertFalse(transition.equals(notEqualTransition));
        assertFalse(transition.equals(nullTransition));
    }

    @Test
    public void testHashCode() {
        Transition equalTransition = new Transition("testName", triggerEventMock, initialStateMock, outcomeStateMock);
        Transition notEqualTransition = new Transition("notTestName", triggerEventMock, initialStateMock, outcomeStateMock);

        int hashCode = transition.hashCode();
        int equalHashCode = equalTransition.hashCode();
        int notEqualHashCode = notEqualTransition.hashCode();

        assertEquals(hashCode, equalHashCode);
        assertNotEquals(hashCode, notEqualHashCode);
    }

    @Test
    public void testToString() {
        String expectedToString = "Transition{name='testName', initialState=MockState, outcomeState=MockState, triggerEvent=MockEvent, guardConditions=[]}";
        String actualToString = transition.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testTriggerWithEmptyGuardConditions() {
        when(initialStateMock.equals(any(IState.class))).thenReturn(true);
        when(triggerEventMock.equals(any(Event.class))).thenReturn(true);

        IState result = transition.trigger(initialStateMock, triggerEventMock);

        verify(outcomeStateMock).setState();
        assertEquals(outcomeStateMock, result);
    }

    @Test
    public void testTriggerWithPassingGuards() {
        when(initialStateMock.equals(any(IState.class))).thenReturn(true);
        when(triggerEventMock.equals(any(Event.class))).thenReturn(true);
        GuardCondition guardMock = mock(GuardCondition.class);
        when(guardMock.evaluate(any(Event.class))).thenReturn(true);
        guardConditionsMock.add(guardMock);
        transition.addGuardCondition(guardMock);

        IState result = transition.trigger(initialStateMock, triggerEventMock);

        verify(outcomeStateMock).setState();
        assertEquals(outcomeStateMock, result);
    }

    @Test
    public void testTriggerWithFailingGuards() {
        when(initialStateMock.equals(any(IState.class))).thenReturn(true);
        when(triggerEventMock.equals(any(Event.class))).thenReturn(true);
        GuardCondition guardMock = mock(GuardCondition.class);
        when(guardMock.evaluate(any(Event.class))).thenReturn(false);
        guardConditionsMock.add(guardMock);

        IState result = transition.trigger(initialStateMock, triggerEventMock);

        verifyNoMoreInteractions(outcomeStateMock);
        assertSame(initialStateMock, result);
    }

    @Test
    public void testWillTriggerWithEmptyGuardConditions() {
        when(initialStateMock.equals(any(IState.class))).thenReturn(true);
        when(triggerEventMock.equals(any(Event.class))).thenReturn(true);

        boolean result = transition.willTrigger(initialStateMock, triggerEventMock);

        assertTrue(result);
    }

    @Test
    public void testWillTriggerWithFailingGuards() {
        when(initialStateMock.equals(any(IState.class))).thenReturn(true);
        when(triggerEventMock.equals(any(Event.class))).thenReturn(true);
        GuardCondition guardMock = mock(GuardCondition.class);
        when(guardMock.evaluate(any(Event.class))).thenReturn(false);
        guardConditionsMock.add(guardMock);

        boolean result = transition.willTrigger(initialStateMock, triggerEventMock);

        assertFalse(result);
    }

    @Test
    public void testAddGuardCondition() {
        GuardCondition guardMock = mock(GuardCondition.class);

        transition.addGuardCondition(guardMock);

        assertEquals(1, guardConditionsMock.size());
        verify(guardConditionsMock.get(0)).setTransition(transition);
    }
}