package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import java.util.*;

public class TransitionTest {

    @Test
    public void testEquals() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new Event("eventName");
        String name = "transitionName";

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(transition1, transition2);
    }

    @Test
    public void testNotEquals() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new Event("eventName");
        String name = "transitionName";

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("differentName", triggerEvent, initialState, outcomeState);

        assertNotEquals(transition1, transition2);
    }

    @Test
    public void testHashCode() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new Event("eventName");
        String name = "transitionName";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        int hashCode1 = transition.hashCode();

        IState initialState2 = new FooState();
        IState outcomeState2 = new BarState();
        Event triggerEvent2 = new Event("eventName");
        String name2 = "transitionName";

        Transition transition2 = new Transition(name2, triggerEvent2, initialState2, outcomeState2);
        int hashCode2 = transition2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new Event("eventName");
        String name = "transitionName";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        String toString = transition.toString();

        assertTrue(toString.contains(name));
        assertTrue(toString.contains(triggerEvent.toString()));
        assertTrue(toString.contains(initialState.toString()));
        assertTrue(toString.contains(outcomeState.toString()));
    }

    @Test
    public void testTrigger() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new Event("eventName");
        String name = "transitionName";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(initialState, triggerEvent);
        assertSame(outcomeState, result);
    }

    @Test
    public void testTrigger_differentInitialState() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new Event("eventName");
        String name = "transitionName";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState differentInitialState = new DifferentState();
        IState result = transition.trigger(differentInitialState, triggerEvent);
        assertSame(initialState, result);
    }

    @Test
    public void testTrigger_differentEvent() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new Event("eventName");
        String name = "transitionName";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        Event differentEvent = new Event("differentEventName");
        IState result = transition.trigger(initialState, differentEvent);
        assertSame(initialState, result);
    }

    @Test
    public void testWillTrigger() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new Event("eventName");
        String name = "transitionName";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState result = transition.willTrigger(initialState, triggerEvent);
        assertTrue(result);
    }

    @Test
    public void testWillTrigger_differentInitialState() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new Event("eventName");
        String name = "transitionName";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState differentInitialState = new DifferentState();
        IState result = transition.willTrigger(differentInitialState, triggerEvent);
        assertFalse(result);
    }

    @Test
    public void testWillTrigger_differentEvent() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new Event("eventName");
        String name = "transitionName";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        Event differentEvent = new Event("differentEventName");
        IState result = transition.willTrigger(initialState, differentEvent);
        assertFalse(result);
    }

    @Test
    public void testAddGuardCondition() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new Event("eventName");
        String name = "transitionName";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        IPredicate guard = new GuardConditionA();

        transition.addGuardCondition(guard);
        assertTrue(transition.getGuardConditions().contains(guard));
    }

    @Test
    public void testEvaluateGuards() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new Event("eventName");
        String name = "transitionName";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        IPredicate guard1 = new GuardConditionA();
        IPredicate guard2 = new GuardConditionB();

        transition.addGuardCondition(guard1);
        transition.addGuardCondition(guard2);

        assertTrue(transition.evaluateGuards(triggerEvent));
    }

    @Test
    public void testEvaluateGuards_oneFailed() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new Event("eventName");
        String name = "transitionName";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        IPredicate guard1 = new GuardConditionA();
        IPredicate guard2 = new GuardConditionC();

        transition.addGuardCondition(guard1);
        transition.addGuardCondition(guard2);

        assertFalse(transition.evaluateGuards(triggerEvent));
    }

    @Test
    public void testEvaluateGuards_allPassed() {
        IState initialState = new FooState();
        IState outcomeState = new BarState();
        Event triggerEvent = new Event("eventName");
        String name = "transitionName";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        IPredicate guard1 = new GuardConditionA();
        IPredicate guard2 = new GuardConditionB();
        IPredicate guard3 = new GuardConditionD();

        transition.addGuardCondition(guard1);
        transition.addGuardCondition(guard2);
        transition.addGuardCondition(guard3);

        assertTrue(transition.evaluateGuards(triggerEvent));
    }

    private static class FooState implements IState {
        //Implement the IState interface here
    }

    private static class BarState implements IState {
        //Implement the IState interface here
    }

    private static class DifferentState implements IState {
        //Implement the IState interface here
    }

    private static class GuardConditionA implements IPredicate {
        //Implement the IPredicate interface here

        @Override
        public boolean evaluate(Event event) {
            //Implement the evaluate method here
            return true;
        }
    }

    private static class GuardConditionB implements IPredicate {
        //Implement the IPredicate interface here

        @Override
        public boolean evaluate(Event event) {
            //Implement the evaluate method here
            return false;
        }
    }

    private static class GuardConditionC implements IPredicate {
        //Implement the IPredicate interface here

        @Override
        public boolean evaluate(Event event) {
            //Implement the evaluate method here
            return true;
        }
    }

    private static class GuardConditionD implements IPredicate {
        //Implement the IPredicate interface here

        @Override
        public boolean evaluate(Event event) {
            //Implement the evaluate method here
            return true;
        }
    }

    private static class Event implements IEvent {
        //Implement the IEvent interface here

        @Override
        public String toString() {
            return "Event [name=eventName]";
        }
    }
}