package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TransitionTest {

    @Test
    public void testTransitionWithEmptyGuardConditions() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new State("testInitialState");
        IState outcomeState = new State("testOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(initialState, triggerEvent);
        assertEquals(outcomeState, result);
    }

    @Test
    public void testTransitionWithGuardConditions() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new State("testInitialState");
        IState outcomeState = new State("testOutcomeState");

        List<IPredicate> guardPredicates = new ArrayList<>();
        IPredicate guardPredicate1 = new ConstantPredicate(true);
        IPredicate guardPredicate2 = new ConstantPredicate(false);

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(new GuardCondition(guardPredicate1));
        transition.addGuardCondition(new GuardCondition(guardPredicate2));

        IState result = transition.trigger(initialState, triggerEvent);
        assertFalse(outcomeState.equals(result)); // Guard condition 2 failed

        guardPredicate2 = new ConstantPredicate(true);
        result = transition.trigger(initialState, triggerEvent);
        assertEquals(outcomeState, result); // All guard conditions passed
    }

    @Test
    public void testWillTriggerWithEmptyGuardConditions() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new State("testInitialState");
        IState outcomeState = new State("testOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        boolean result = transition.willTrigger(initialState, triggerEvent);
        assertTrue(result);
    }

    @Test
    public void testWillTriggerWithGuardConditions() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new State("testInitialState");
        IState outcomeState = new State("testOutcomeState");

        List<IPredicate> guardPredicates = new ArrayList<>();
        IPredicate guardPredicate1 = new ConstantPredicate(true);
        IPredicate guardPredicate2 = new ConstantPredicate(false);

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(new GuardCondition(guardPredicate1));
        transition.addGuardCondition(new GuardCondition(guardPredicate2));

        boolean result = transition.willTrigger(initialState, triggerEvent);
        assertFalse(result); // Guard condition 2 failed

        guardPredicate2 = new ConstantPredicate(true);
        result = transition.willTrigger(initialState, triggerEvent);
        assertTrue(result); // All guard conditions passed
    }

    @Test
    public void testEquals() {
        String name1 = "testTransition1";
        String name2 = "testTransition2";
        Event triggerEvent1 = new Event("testEvent1");
        Event triggerEvent2 = new Event("testEvent2");
        IState initialState1 = new State("testInitialState1");
        IState initialState2 = new State("testInitialState2");
        IState outcomeState1 = new State("testOutcomeState1");
        IState outcomeState2 = new State("testOutcomeState2");

        Transition transition1 = new Transition(name1, triggerEvent1, initialState1, outcomeState1);
        Transition transition2 = new Transition(name2, triggerEvent2, initialState2, outcomeState2);

        Transition identicalTransition = new Transition(name1, triggerEvent1, initialState1, outcomeState1);

        assertTrue(transition1.equals(identicalTransition));
        assertFalse(transition1.equals(transition2));
    }

    @Test
    public void testHashCode() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new State("testInitialState");
        IState outcomeState = new State("testOutcomeState");

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);

        int hashCode1 = transition1.hashCode();
        int hashCode2 = transition2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new State("testInitialState");
        IState outcomeState = new State("testOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        String result = transition.toString();
        String expected = "Transition{name='" + name + "', triggerEvent=Event{name='testEvent'}, initialState=State{name='testInitialState'}, outcomeState=State{name='testOutcomeState'}}";
        assertEquals(expected, result);
    }

    @Test
    public void testNullTransition() {
        IState initialState = new State("testInitialState");
        Event triggerEvent = new Event("testEvent");
        IState outcomeState = new State("testOutcomeState");

        Transition transition = new Transition(null, triggerEvent, initialState, outcomeState);
        assertNull(transition);
    }

    @Test
    public void testNullInitialState() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState outcomeState = new State("testOutcomeState");

        Transition transition = new Transition(name, triggerEvent, null, outcomeState);
        assertNull(transition);
    }

    @Test
    public void testNullOutcomeState() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new State("testInitialState");

        Transition transition = new Transition(name, triggerEvent, initialState, null);
        assertNull(transition);
    }
}