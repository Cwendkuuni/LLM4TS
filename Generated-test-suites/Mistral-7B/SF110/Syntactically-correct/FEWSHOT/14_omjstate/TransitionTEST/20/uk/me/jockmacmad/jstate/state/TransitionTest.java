package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.junit.Test;

public class TransitionTest {

    // Test constructor
    @Test
    public void testTransitionConstruction() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(name, transition.getName());
        assertEquals(triggerEvent, transition.getTriggerEvent());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
    }

    // Test equals and hashCode methods
    @Test
    public void testEqualsHashCode() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(transition1, transition2);
        assertTrue(transition1.hashCode() == transition2.hashCode());

        Transition transition3 = new Transition("anotherName", triggerEvent, initialState, outcomeState);

        assertNotEquals(transition1, transition3);
        assertNotEquals(transition1.hashCode(), transition3.hashCode());
    }

    // Test trigger method
    @Test
    public void testTriggerMethod() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = new DummyState("currentState");

        // Should not trigger when current state is not initial state or event is not trigger event
        assertSame(currentState, transition.trigger(currentState, new Event("otherEvent")));

        // Should trigger when current state is initial state and event is trigger event and no guards fail
        transition.addGuardCondition(new AlwaysTruePredicate());
        IState expectedOutcomeState = new DummyState("expectedOutcomeState");
        transition.setOutcomeState(expectedOutcomeState);
        IState result = transition.trigger(initialState, triggerEvent);
        assertEquals(expectedOutcomeState, result);

        // Should not trigger when current state is initial state and event is trigger event but one or more guards fail
        transition.addGuardCondition(new AlwaysFalsePredicate());
        assertSame(initialState, transition.trigger(initialState, triggerEvent));
    }

    // Test willTrigger method
    @Test
    public void testWillTriggerMethod() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = new DummyState("currentState");

        // Should not willTrigger when current state is not initial state or event is not trigger event
        assertFalse(transition.willTrigger(currentState, new Event("otherEvent")));

        // Should willTrigger when current state is initial state and event is trigger event and all guards pass
        transition.addGuardCondition(new AlwaysTruePredicate());
        assertTrue(transition.willTrigger(initialState, triggerEvent));

        // Should not willTrigger when current state is initial state and event is trigger event but one or more guards fail
        transition.addGuardCondition(new AlwaysFalsePredicate());
        assertFalse(transition.willTrigger(initialState, triggerEvent));
    }

    // Test adding guard conditions
    @Test
    public void testAddingGuardConditions() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        // Test adding IPredicate
        IPredicate guard1 = new AlwaysTruePredicate();
        transition.addGuardCondition(guard1);
        assertEquals(1, transition.getGuardConditions().size());
        assertEquals(guard1, transition.getGuardConditions().get(0));

        // Test adding IGuardCondition
        IGuardCondition guard2 = new GuardCondition("guard2");
        transition.addGuardCondition(guard2);
        assertEquals(2, transition.getGuardConditions().size());
        assertEquals(guard2, transition.getGuardConditions().get(1));
    }

    // Test getInitialState, getOutcomeState and getTriggerEvent methods
    @Test
    public void testGetters() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    // Test toString method
    @Test
    public void testToString() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new DummyState("initialState");
        IState outcomeState = new DummyState("outcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        String expected = String.format("Transition{name='%s', triggerEvent=Event{name='%s'}, initialState=DummyState{name='%s'}, outcomeState=DummyState{name='%s'}}", name, triggerEvent.getName(), initialState.getName(), outcomeState.getName());
        assertEquals(expected, transition.toString());
    }

    private static class DummyState implements IState {
        private String name;

        public DummyState(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            DummyState that = (DummyState) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return "DummyState{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}