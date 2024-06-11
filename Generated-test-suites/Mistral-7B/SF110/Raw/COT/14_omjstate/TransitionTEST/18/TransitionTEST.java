package uk.me.jockmacmad.jstate.state;

import org.junit.Test;
import org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.Transition;
import uk.me.jockmacmad.jstate.state.DummyInitialState;
import uk.me.jockmacmad.jstate.state.DummyOutcomeState;
import uk.me.jockmacmad.jstate.state.DummyGuardCondition;
import uk.me.jockmacmad.jstate.event.DummyEvent;

import java.util.ArrayList;
import java.util.List;

public class TransitionTest {

    @Test
    public void testTransitionConstructor() {
        String name = "testTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(name, transition.getName());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testAddGuardConditions() {
        String name = "testTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        List<IGuardCondition> guardConditions = new ArrayList<>();
        IGuardCondition guardCondition1 = new DummyGuardCondition();
        IGuardCondition guardCondition2 = new DummyGuardCondition();

        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);

        assertEquals(2, transition.getGuardConditions().size());
        assertTrue(transition.getGuardConditions().contains(guardCondition1));
        assertTrue(transition.getGuardConditions().contains(guardCondition2));
    }

    @Test
    public void testEquals() {
        String name1 = "testTransition1";
        String name2 = "testTransition2";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();

        Transition transition1 = new Transition(name1, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name2, triggerEvent, initialState, outcomeState);

        assertFalse(transition1.equals(transition2));

        Transition transition3 = new Transition(name1, triggerEvent, initialState, outcomeState);

        assertTrue(transition1.equals(transition3));
    }

    @Test
    public void testHashCode() {
        String name = "testTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        Transition transitionCopy = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(transition.hashCode(), transitionCopy.hashCode());
    }

    @Test
    public void testEvaluateGuardConditions() {
        String name = "testTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        List<IGuardCondition> guardConditions = new ArrayList<>();
        IGuardCondition guardCondition1 = new DummyGuardCondition();
        IGuardCondition guardCondition2 = new DummyGuardCondition();

        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);

        boolean passesGuards = false;
        Event pEvent = new DummyEvent();

        transition.evaluateGuardConditions();

        // Test empty guard condition list
        IState currentState = new DummyInitialState();

        IState retVal = transition.trigger(currentState, pEvent);
        assertNotSame(outcomeState, retVal);

        // Test passing all guard conditions
        passesGuards = true;
        guardCondition1.setPasses(passesGuards);
        guardCondition2.setPasses(passesGuards);

        currentState = initialState;

        retVal = transition.trigger(currentState, pEvent);
        assertEquals(outcomeState, retVal);

        // Test failing one guard condition
        passesGuards = false;
        guardCondition1.setPasses(passesGuards);

        currentState = initialState;

        retVal = transition.trigger(currentState, pEvent);
        assertNotSame(outcomeState, retVal);

        // Test failing all guard conditions
        passesGuards = false;
        guardCondition1.setPasses(passesGuards);
        guardCondition2.setPasses(passesGuards);

        currentState = initialState;

        retVal = transition.trigger(currentState, pEvent);
        assertNotSame(outcomeState, retVal);
    }

    @Test
    public void testGetInitialState() {
        String name = "testTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(initialState, transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        String name = "testTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        String name = "testTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testToString() {
        String name = "testTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        String expectedToString = "Transition{name='" + name + "', triggerEvent=" + triggerEvent + ", initialState=" + initialState + ", outcomeState=" + outcomeState + '}';
        String actualToString = transition.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTriggerNullInitialState() {
        String name = "testTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = null;
        IState outcomeState = new DummyOutcomeState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = null;
        transition.trigger(currentState, new DummyEvent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTriggerNullEvent() {
        String name = "testTransition";
        Event triggerEvent = null;
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = new DummyInitialState();
        transition.trigger(currentState, null);
    }

    @Test
    public void testWillTriggerSameState() {
        String name = "testTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        boolean retVal = transition.willTrigger(initialState, triggerEvent);

        assertTrue(retVal);
    }

    @Test
    public void testWillTriggerDifferentStates() {
        String name = "testTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState differentState = new DummyInitialState();

        boolean retVal = transition.willTrigger(differentState, triggerEvent);

        assertFalse(retVal);
    }

    @Test
    public void testWillTriggerNullState() {
        String name = "testTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        boolean retVal = transition.willTrigger(null, triggerEvent);

        assertFalse(retVal);
    }

    @Test
    public void testWillTriggerNullEvent() {
        String name = "testTransition";
        Event triggerEvent = null;
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        boolean retVal = transition.willTrigger(initialState, null);

        assertFalse(retVal);
    }

}