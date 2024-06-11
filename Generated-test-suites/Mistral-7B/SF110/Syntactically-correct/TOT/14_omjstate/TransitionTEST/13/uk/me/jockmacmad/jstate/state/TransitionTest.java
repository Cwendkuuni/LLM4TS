package uk.me.jockmacmad.jstate.state;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TransitionTest {
    private Transition transition;
    private IState initialState;
    private IState outcomeState;
    private Event triggerEvent;
    
    @Before
    public void setUp() {
        initialState = new ConcreteState();
        outcomeState = new ConcreteState();
        triggerEvent = new ConcreteEvent();
        transition = new Transition("testTransition", triggerEvent, initialState, outcomeState);
    }
    
    @Test
    public void testTrigger_WithInitialStateAndTriggerEvent_ShouldReturnOutcomeState() {
        IState result = transition.trigger(initialState, triggerEvent);
        assertEquals(outcomeState, result);
    }
    
    @Test
    public void testWillTrigger_WithInitialStateAndTriggerEvent_ShouldReturnTrue() {
        boolean result = transition.willTrigger(initialState, triggerEvent);
        assertTrue(result);
    }
    
    @Test
    public void testTrigger_WithDifferentInitialStateAndTriggerEvent_ShouldReturnInitialState() {
        IState differentState = new ConcreteState();
        assertNotSame(outcomeState, transition.trigger(differentState, triggerEvent));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTrigger_WithNullInitialState_ShouldThrowIllegalArgumentException() {
        Transition transition = new Transition("testTransition", triggerEvent, null, outcomeState);
        transition.trigger(null, triggerEvent);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTrigger_WithNullTriggerEvent_ShouldThrowIllegalArgumentException() {
        Transition transition = new Transition("testTransition", null, initialState, outcomeState);
        transition.trigger(initialState, null);
    }
    
    @Test
    public void testWillTrigger_WithDifferentInitialStateAndTriggerEvent_ShouldReturnFalse() {
        assertFalse(transition.willTrigger(new ConcreteState(), triggerEvent));
    }
}