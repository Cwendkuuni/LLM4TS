package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.me.jockmacmad.jstate.state.GuardCondition;
import uk.me.jockmacmad.jstate.state.Predicate;

public class TransitionTest {

    @Test
    public void testTransitionCreation() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertSame(name, transition.getName());
        assertSame(triggerEvent, transition.getTriggerEvent());
        assertSame(initialState, transition.getInitialState());
        assertSame(outcomeState, transition.getOutcomeState());
        assertEquals(0, transition.getGuardConditions().size());
    }

    @Test
    public void testAddGuardCondition() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IPredicate predicate = new Predicate() {
            @Override
            public boolean evaluate(Event event) {
                return event.getName().equals("testEvent");
            }
        };
        GuardCondition guardCondition = new GuardCondition(predicate);
        transition.addGuardCondition(guardCondition);

        assertTrue(transition.getGuardConditions().size() == 1);
        assertSame(guardCondition, transition.getGuardConditions().get(0));
    }

    @Test
    public void testEvaluateGuardsWhenEmpty() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        boolean willTrigger = transition.willTrigger(initialState, triggerEvent);
        assertFalse(willTrigger);
    }

    @Test
    public void testEvaluateGuardsWhenAllPass() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        GuardCondition guardCondition = new GuardCondition(new Predicate() {
            @Override
            public boolean evaluate(Event event) {
                return event.getName().equals("testEvent");
            }
        });
        transition.addGuardCondition(guardCondition);

        boolean willTrigger = transition.willTrigger(initialState, triggerEvent);
        assertTrue(willTrigger);
    }

    @Test
    public void testEvaluateGuardsWhenAnyFail() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        GuardCondition guardCondition1 = new GuardCondition(new Predicate() {
            @Override
            public boolean evaluate(Event event) {
                return event.getName().equals("testEvent");
            }
        });
        GuardCondition guardCondition2 = new GuardCondition(new Predicate() {
            @Override
            public boolean evaluate(Event event) {
                return event.getName().equals("testEvent2");
            }
        });
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);

        boolean willTrigger = transition.willTrigger(initialState, triggerEvent);
        assertFalse(willTrigger);
    }

    @Test
    public void testTransitionExecution() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(initialState, triggerEvent);
        assertSame(outcomeState, result);
    }

    @Test
    public void testTransitionExecution_DifferentInitialState() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState differentInitialState = new MockState();

        IState result = transition.trigger(differentInitialState, triggerEvent);
        assertSame(initialState, result);
    }

    @Test
    public void testTransitionExecution_DifferentEvent() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        Event differentEvent = new Event("differentEvent");

        IState result = transition.trigger(initialState, differentEvent);
        assertSame(initialState, result);
    }

    @Test
    public void testToString() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        String expected = String.format("Transition{name='%s', triggerEvent=Event{name='testEvent'}, initialState=MockState{}, outcomeState=MockState{}, guardConditions=[], equals=%H, hashCode=%H}", name, transition);
        String result = transition.toString();
        assertEquals(expected, result);
    }

    private static class MockState implements IState {
        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(this);
        }

        @Override
        public String toString() {
            return "MockState{}";
        }
    }
}