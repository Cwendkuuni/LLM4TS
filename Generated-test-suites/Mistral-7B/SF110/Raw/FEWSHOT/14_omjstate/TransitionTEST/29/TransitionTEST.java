package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import java.util.*;

public class TransitionTest {

    @Test
    public void testEquals() {
        IState initialState = new FakeState("initialState");
        IState outcomeState = new FakeState("outcomeState");
        Event triggerEvent = new FakeEvent("triggerEvent");
        Transition transition1 = new Transition("transition1", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("transition1", triggerEvent, initialState, outcomeState);

        assertTrue(transition1.equals(transition2));
        assertTrue(transition1.hashCode() == transition2.hashCode());

        Transition transition3 = new Transition("transition2", triggerEvent, initialState, outcomeState);
        assertFalse(transition1.equals(transition3));
        assertFalse(transition1.hashCode() == transition3.hashCode());

        Transition transition4 = new Transition("transition1", null, initialState, outcomeState);
        assertFalse(transition1.equals(transition4));
        assertFalse(transition1.hashCode() == transition4.hashCode());

        Transition transition5 = new Transition("transition1", triggerEvent, null, outcomeState);
        assertFalse(transition1.equals(transition5));
        assertFalse(transition1.hashCode() == transition5.hashCode());

        Transition transition6 = new Transition("transition1", triggerEvent, initialState, null);
        assertFalse(transition1.equals(transition6));
        assertFalse(transition1.hashCode() == transition6.hashCode());
    }

    @Test
    public void testToString() {
        IState initialState = new FakeState("initialState");
        IState outcomeState = new FakeState("outcomeState");
        Event triggerEvent = new FakeEvent("triggerEvent");
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);

        String expectedToString = "Transition[name=transition, triggerEvent=triggerEvent, initialState=initialState, outcomeState=outcomeState]";
        String actualToString = transition.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testTrigger() {
        IState initialState = new FakeState("initialState");
        IState outcomeState = new FakeState("outcomeState");
        Event triggerEvent = new FakeEvent("triggerEvent");
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(initialState, triggerEvent);
        assertEquals(outcomeState, result);

        IState result2 = transition.trigger(outcomeState, triggerEvent);
        assertEquals(outcomeState, result2);

        IState result3 = transition.trigger(null, triggerEvent);
        assertNull(result3);

        IState result4 = transition.trigger(initialState, null);
        assertNull(result4);

        IState result5 = transition.trigger(initialState, new FakeEvent("anotherEvent"));
        assertEquals(initialState, result5);
    }

    @Test
    public void testWillTrigger() {
        IState initialState = new FakeState("initialState");
        IState outcomeState = new FakeState("outcomeState");
        Event triggerEvent = new FakeEvent("triggerEvent");
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);

        FakeGuardCondition guardCondition1 = new FakeGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return event.getName().equals("triggerEvent");
            }
        };
        transition.addGuardCondition(guardCondition1);

        IState result = transition.trigger(initialState, triggerEvent);
        assertTrue(transition.willTrigger(initialState, triggerEvent));
        assertEquals(outcomeState, result);

        IState result2 = transition.trigger(initialState, new FakeEvent("anotherEvent"));
        assertFalse(transition.willTrigger(initialState, triggerEvent));
        assertNull(result2);

        IState result3 = transition.trigger(outcomeState, triggerEvent);
        assertFalse(transition.willTrigger(initialState, triggerEvent));
        assertNull(result3);

        IState result4 = transition.trigger(null, triggerEvent);
        assertFalse(transition.willTrigger(initialState, triggerEvent));
        assertNull(result4);

        Transition transition2 = new Transition("transition2", triggerEvent, initialState, outcomeState);
        assertFalse(transition.willTrigger(initialState, triggerEvent));
        assertNull(transition.trigger(initialState, triggerEvent));
        assertEquals(initialState, transition2.getInitialState());
        assertEquals(outcomeState, transition2.getOutcomeState());
        assertTrue(transition2.willTrigger(initialState, triggerEvent));
        assertEquals(outcomeState, transition2.trigger(initialState, triggerEvent));
    }

    @Test
    public void testAddGuardCondition() {
        IState initialState = new FakeState("initialState");
        IState outcomeState = new FakeState("outcomeState");
        Event triggerEvent = new FakeEvent("triggerEvent");
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);

        FakeGuardCondition guardCondition1 = new FakeGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return event.getName().equals("triggerEvent");
            }
        };
        transition.addGuardCondition(guardCondition1);

        Transition transitionCopy = new Transition(transition);
        List<IGuardCondition> guardConditionsCopy = transitionCopy.getGuardConditions();
        List<IGuardCondition> guardConditions = transition.getGuardConditions();
        assertEquals(guardConditions.size(), guardConditionsCopy.size());
        for (int i = 0; i < guardConditions.size(); i++) {
            assertEquals(guardConditions.get(i), guardConditionsCopy.get(i));
        }

        FakeGuardCondition guardCondition2 = new FakeGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return event.getName().equals("anotherEvent");
            }
        };
        transition.addGuardCondition(guardCondition2);
        List<IGuardCondition> guardConditionsAfterAddition = transition.getGuardConditions();
        assertEquals(guardConditionsAfterAddition.size(), guardConditions.size() + 1);
        assertNotEquals(guardConditions.get(0), guardConditionsAfterAddition.get(0));
        assertNotEquals(guardConditions.get(0), guardConditionsAfterAddition.get(1));
        assertEquals(guardConditionsAfterAddition.get(1), guardCondition2);
    }

    @Test
    public void testEmptyGuardConditions() {
        IState initialState = new FakeState("initialState");
        IState outcomeState = new FakeState("outcomeState");
        Event triggerEvent = new FakeEvent("triggerEvent");
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(initialState, triggerEvent);
        assertTrue(transition.evaluateGuardConditions());
        assertEquals(outcomeState, result);

        transition.evaluateGuardConditions();
        assertTrue(transition.evaluateGuardConditions());

        List<IGuardCondition> guardConditions = transition.getGuardConditions();
        assertEquals(0, guardConditions.size());
    }

    @Test
    public void testMultipleGuardConditions() {
        IState initialState = new FakeState("initialState");
        IState outcomeState = new FakeState("outcomeState");
        Event triggerEvent = new FakeEvent("triggerEvent");
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);

        FakeGuardCondition guardCondition1 = new FakeGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return event.getName().equals("triggerEvent");
            }
        };
        transition.addGuardCondition(guardCondition1);

        FakeGuardCondition guardCondition2 = new FakeGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return event.getName().equals("anotherEvent");
            }
        };
        transition.addGuardCondition(guardCondition2);

        IState result = transition.trigger(initialState, triggerEvent);
        assertFalse(transition.evaluateGuardConditions());
        assertNull(result);

        IState result2 = transition.trigger(initialState, new FakeEvent("anotherEvent"));
        assertFalse(transition.evaluateGuardConditions());
        assertNull(result2);

        IState result3 = transition.trigger(outcomeState, triggerEvent);
        assertFalse(transition.evaluateGuardConditions());
        assertNull(result3);

        IState result4 = transition.trigger(outcomeState, new FakeEvent("anotherEvent"));
        assertFalse(transition.evaluateGuardConditions());
        assertNull(result4);

        IState result5 = transition.trigger(null, triggerEvent);
        assertFalse(transition.evaluateGuardConditions());
        assertNull(result5);

        IState result6 = transition.trigger(null, new FakeEvent("anotherEvent"));
        assertFalse(transition.evaluateGuardConditions());
        assertNull(result6);

        transition.evaluateGuardConditions();
        assertTrue(transition.evaluateGuardConditions());

        transition.addGuardCondition(new FakeGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return event.getName().equals("anotherEvent");
            }
        });
        assertFalse(transition.evaluateGuardConditions());

        transition.removeGuardCondition(guardCondition1);
        assertFalse(transition.evaluateGuardConditions());

        transition.removeGuardCondition(guardCondition2);
        assertFalse(transition.evaluateGuardConditions());
    }

    private static class FakeEvent implements Event {
        private String name;

        public FakeEvent(String pName) {
            this.name = pName;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private static class FakeGuardCondition implements IGuardCondition {
        boolean evaluate(Event event) {
            return true;
        }
    }

    private static class FakeState implements IState {
        private String name;

        public FakeState(String pName) {
            this.name = pName;
        }

        @Override
        public boolean equals(Object pState) {
            return this == pState;
        }

        @Override
        public int hashCode() {
            return this.name.hashCode();
        }

        @Override
        public String toString() {
            return name;
        }
    }
}