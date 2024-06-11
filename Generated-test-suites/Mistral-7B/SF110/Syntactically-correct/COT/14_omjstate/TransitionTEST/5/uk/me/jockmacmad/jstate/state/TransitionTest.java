package uk.me.jockmacmad.jstate.state.test;

import org.junit.Test;
import org.junit.Assert.*;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.*;

import java.util.*;

public class TransitionTest {

    @Test
    public void testTransitionConstructor() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(name, transition.getName());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testAddGuardCondition() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IPredicate predicate = new AlwaysTruePredicate();
        transition.addGuardCondition(predicate);

        List<IGuardCondition> guardConditions = transition.getGuardConditions();
        assertEquals(1, guardConditions.size());
        assertEquals(predicate, guardConditions.get(0));
    }

    @Test
    public void testEquals() {
        String name1 = "TestTransition1";
        Event triggerEvent1 = new Event("TestEvent1");
        IState initialState1 = new State("TestInitialState1");
        IState outcomeState1 = new State("TestOutcomeState1");

        String name2 = "TestTransition2";
        Event triggerEvent2 = new Event("TestEvent2");
        IState initialState2 = new State("TestInitialState2");
        IState outcomeState2 = new State("TestOutcomeState2");

        Transition transition1 = new Transition(name1, triggerEvent1, initialState1, outcomeState1);
        Transition transition2 = new Transition(name2, triggerEvent2, initialState2, outcomeState2);

        Assert.assertNotSame(transition1, transition2);

        Transition transition3 = new Transition(name1, triggerEvent1, initialState1, outcomeState1);
        assertTrue(transition1.equals(transition3));
    }

    @Test
    public void testHashCode() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        IState initialStateCopy = new State("TestInitialState");
        Transition transition2 = new Transition(name, triggerEvent, initialStateCopy, outcomeState);

        int hashCode1 = transition1.hashCode();
        int hashCode2 = transition2.hashCode();

        assertNotSame(hashCode1, hashCode2);

        assertEquals(hashCode1, transition1.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTriggerNullInitialState() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = null;
        IState outcomeState = new State("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.trigger(null, triggerEvent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTriggerNullEvent() {
        String name = "TestTransition";
        Event triggerEvent = null;
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.trigger(initialState, null);
    }

    @Test
    public void testTriggerSameState() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        IState currentState = initialState;

        IState result = transition.trigger(currentState, triggerEvent);
        assertEquals(outcomeState, result);
    }

    @Test
    public void testTriggerDifferentStates() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        IState currentState = new State("TestDifferentState");

        IState result = transition.trigger(currentState, triggerEvent);
        assertNotEquals(outcomeState, result);
        assertEquals(initialState, result);
    }

    @Test
    public void testWillTriggerSameStates() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertTrue(transition.willTrigger(initialState, triggerEvent));
        assertFalse(transition.willTrigger(new State("TestDifferentState"), triggerEvent));
    }

    @Test
    public void testWillTriggerSameEventDifferentGuards() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent1");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);

        IPredicate guardCondition1 = new AlwaysTruePredicate();
        transition1.addGuardCondition(guardCondition1);

        assertTrue(transition1.willTrigger(initialState, triggerEvent));

        IPredicate guardCondition2 = new AlwaysFalsePredicate();
        transition1.addGuardCondition(guardCondition2);

        assertFalse(transition1.willTrigger(initialState, triggerEvent));
    }

    @Test
    public void testWillTriggerDifferentEvents() {
        String name = "TestTransition";
        Event triggerEvent1 = new Event("TestEvent1");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");

        Transition transition1 = new Transition(name, triggerEvent1, initialState, outcomeState);

        Event triggerEvent2 = new Event("TestEvent2");
        assertTrue(transition1.willTrigger(initialState, triggerEvent1));
        assertFalse(transition1.willTrigger(initialState, triggerEvent2));
    }
}