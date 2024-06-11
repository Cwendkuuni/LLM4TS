package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TransitionTest {

    private IState initialState;
    private IState outcomeState;
    private Event triggerEvent;
    private Transition transition;
    private List<IGuardCondition> guardConditions;

    @Before
    public void setUp() {
        initialState = mock(IState.class);
        outcomeState = mock(IState.class);
        triggerEvent = mock(Event.class);
        guardConditions = new ArrayList<>();
        transition = new Transition("testTransition", triggerEvent, initialState, outcomeState);
    }

    @Test
    public void testInitialization() {
        assertEquals("testTransition", transition.getName());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
        assertNull(transition.getGuardConditions());
    }

    @Test
    public void testAddingGuardConditions() {
        IPredicate predicateMock = mock(IPredicate.class);
        IGuardCondition guardCondition = new GuardCondition(predicateMock);

        transition.addGuardCondition(predicateMock);
        transition.addGuardCondition(guardCondition);

        assertEquals(2, transition.getGuardConditions().size());
    }

    @Test
    public void testTrigger() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        IGuardCondition guardConditionMock = mock(IGuardCondition.class);
        when(guardConditionMock.evaluate(triggerEvent)).thenReturn(true);
        guardConditions.add(guardConditionMock);
        transition.addGuardConditions(guardConditions);

        IState currentState = mock(IState.class);

        when(currentState.equals(initialState)).thenReturn(true);
        when(currentState.equals(outcomeState)).thenReturn(false);

        transition.evaluateGuardConditions();

        assertEquals(outcomeState, transition.trigger(currentState, triggerEvent));
    }

    @Test
    public void testWillTrigger() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        IGuardCondition guardConditionMock = mock(IGuardCondition.class);
        when(guardConditionMock.evaluate(triggerEvent)).thenReturn(true);
        guardConditions.add(guardConditionMock);
        transition.addGuardConditions(guardConditions);

        IState currentState = mock(IState.class);

        when(currentState.equals(initialState)).thenReturn(true);
        when(currentState.equals(outcomeState)).thenReturn(false);

        assertTrue(transition.willTrigger(currentState, triggerEvent));
    }

    @Test
    public void testTriggerWithFalseInitialState() {
        when(initialState.equals(initialState)).thenReturn(false);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        IState currentState = mock(IState.class);
        when(currentState.equals(initialState)).thenReturn(false);
        when(currentState.equals(outcomeState)).thenReturn(false);

        assertNotNull(transition.trigger(currentState, triggerEvent));
    }

    @Test
    public void testTriggerWithFalseTriggerEvent() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(triggerEvent.equals(triggerEvent)).thenReturn(false);

        IState currentState = mock(IState.class);
        when(currentState.equals(initialState)).thenReturn(true);
        when(currentState.equals(outcomeState)).thenReturn(false);

        assertNull(transition.trigger(currentState, triggerEvent));
    }

    @Test
    public void testWillTriggerWithFalseInitialState() {
        when(initialState.equals(initialState)).thenReturn(false);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        IState currentState = mock(IState.class);
        when(currentState.equals(initialState)).thenReturn(false);
        when(currentState.equals(outcomeState)).thenReturn(false);

        assertFalse(transition.willTrigger(currentState, triggerEvent));
    }

    @Test
    public void testWillTriggerWithFalseTriggerEventAndNoGuards() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(triggerEvent.equals(triggerEvent)).thenReturn(false);

        IState currentState = mock(IState.class);
        when(currentState.equals(initialState)).thenReturn(true);
        when(currentState.equals(outcomeState)).thenReturn(false);

        assertFalse(transition.willTrigger(currentState, triggerEvent));
    }
}