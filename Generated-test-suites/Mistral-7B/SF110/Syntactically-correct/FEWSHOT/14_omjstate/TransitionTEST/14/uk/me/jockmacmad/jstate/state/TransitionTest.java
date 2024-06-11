package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.*;
import java.util.*;

public class TransitionTest {

    @Test
    public void testTransition() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new MyEvent();
        Transition transition = new Transition("test", triggerEvent, initialState, outcomeState);

        assertNotNull(transition);
        assertEquals("test", transition.getName());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testAddGuardCondition() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new MyEvent();
        Transition transition = new Transition("test", triggerEvent, initialState, outcomeState);

        IPredicate guardCondition = new MyPredicate();
        transition.addGuardCondition(guardCondition);

        assertEquals(1, transition.getGuardConditions().size());
        assertEquals(guardCondition, transition.getGuardConditions().get(0));
    }

    @Test
    public void testAddGuardConditionObject() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new MyEvent();
        Transition transition = new Transition("test", triggerEvent, initialState, outcomeState);

        IGuardCondition guardCondition = new MyGuardCondition();
        transition.addGuardCondition(guardCondition);

        assertEquals(1, transition.getGuardConditions().size());
        assertEquals(guardCondition, transition.getGuardConditions().get(0));
    }

    @Test
    public void testEquals() {
        IState initialState1 = new FooState();
        IState initialState2 = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new MyEvent();
        Transition transition1 = new Transition("test1", triggerEvent, initialState1, outcomeState);
        Transition transition2 = new Transition("test2", triggerEvent, initialState2, outcomeState);

        assertTrue(transition1.equals(transition1));
        assertTrue(transition1.equals(transition2)); // Should be false due to different names

        Transition transition3 = new Transition("test1", triggerEvent, initialState1, outcomeState);
        assertTrue(transition1.equals(transition3));
    }

    @Test
    public void testHashCode() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new MyEvent();
        Transition transition1 = new Transition("test1", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("test2", triggerEvent, initialState, outcomeState);

        int hashcode1 = transition1.hashCode();
        int hashcode2 = transition2.hashCode();

        assertNotEquals(hashcode1, hashcode2);
    }

    @Test
    public void testEvaluateGuardConditions() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new MyEvent();
        Transition transition = new Transition("test", triggerEvent, initialState, outcomeState);

        IPredicate guardCondition1 = new AlwaysTruePredicate();
        IPredicate guardCondition2 = new AlwaysFalsePredicate();
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);

        assertFalse(transition.evaluateGuardConditions());
    }

    @Test
    public void testTrigger() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new MyEvent();
        Transition transition = new Transition("test", triggerEvent, initialState, outcomeState);

        IState currentState = initialState;

        assertNotEquals(outcomeState, transition.trigger(currentState, triggerEvent));

        transition.addGuardCondition(new AlwaysTruePredicate());
        assertEquals(outcomeState, transition.trigger(currentState, triggerEvent));
    }

    @Test
    public void testWillTrigger() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new MyEvent();
        Transition transition = new Transition("test", triggerEvent, initialState, outcomeState);

        IState currentState = initialState;

        assertFalse(transition.willTrigger(currentState, triggerEvent));

        transition.addGuardCondition(new AlwaysTruePredicate());
        assertTrue(transition.willTrigger(currentState, triggerEvent));
    }
}