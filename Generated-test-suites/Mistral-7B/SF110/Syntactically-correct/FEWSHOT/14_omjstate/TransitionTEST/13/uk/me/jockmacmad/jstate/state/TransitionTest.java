package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import uk.me.jockmacmad.jstate.predicate.*;
import org.apache.commons.lang.builder.*;
import java.util.*;

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
    }

    @Test
    public void testAddGuardCondition() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);
        IPredicate guardCondition = mock(IPredicate.class);

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        transition.addGuardCondition(guardCondition);

        assertEquals(1, transition.getGuardConditions().size());
        verify(transition).addGuardCondition((IGuardCondition)guardCondition);
    }

    @Test
    public void testTrigger() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        IGuardCondition guardCondition1 = mock(IGuardCondition.class);
        IGuardCondition guardCondition2 = mock(IGuardCondition.class);

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);

        when(initialState.equals(any())).then(invocationOnMock -> true);
        when(triggerEvent.equals(any())).then(invocationOnMock -> true);
        when(guardCondition1.evaluate(any())).thenReturn(true);
        when(guardCondition2.evaluate(any())).thenReturn(true);

        IState result = transition.trigger(initialState, triggerEvent);

        assertEquals(outcomeState, result);
    }

    @Test
    public void testWillTrigger_noGuards() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(initialState, triggerEvent);
        boolean willTrigger = transition.willTrigger(initialState, triggerEvent);

        assertEquals(outcomeState, result);
        assertTrue(willTrigger);
    }

    @Test
    public void testWillTrigger_guardsPass() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        IGuardCondition guardCondition1 = mock(IGuardCondition.class);
        IGuardCondition guardCondition2 = mock(IGuardCondition.class);

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);

        when(initialState.equals(any())).then(invocationOnMock -> true);
        when(triggerEvent.equals(any())).then(invocationOnMock -> true);
        when(guardCondition1.evaluate(any())).thenReturn(true);
        when(guardCondition2.evaluate(any())).thenReturn(true);

        boolean willTrigger = transition.willTrigger(initialState, triggerEvent);

        assertTrue(willTrigger);
    }

    @Test
    public void testWillTrigger_guardsFail() {
        String name = "testTransition";
        Event triggerEvent = mock(Event.class);
        IState initialState = mock(IState.class);
        IState outcomeState = mock(IState.class);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        IGuardCondition guardCondition1 = mock(IGuardCondition.class);
        IGuardCondition guardCondition2 = mock(IGuardCondition.class);

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);

        when(initialState.equals(any())).then(invocationOnMock -> true);
        when(triggerEvent.equals(any())).then(invocationOnMock -> true);
        when(guardCondition1.evaluate(any())).thenReturn(false);
        when(guardCondition2.evaluate(any())).thenReturn(false);

        boolean willTrigger = transition.willTrigger(initialState, triggerEvent);

        assertFalse(willTrigger);
    }
}