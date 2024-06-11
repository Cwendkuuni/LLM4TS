package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.predicate.*;
import org.junit.Test;

public class TransitionTest {

    @Test
    public void testEquals() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);

        assertTrue(transition1.equals(transition2));
        assertTrue(transition1.hashCode() == transition2.hashCode());

        Transition transition3 = new Transition("differentName", triggerEvent, initialState, outcomeState);
        assertFalse(transition1.equals(transition3));
        assertFalse(transition1.hashCode() == transition3.hashCode());

        Transition transition4 = new Transition(name, new MockEvent(), initialState, outcomeState);
        assertFalse(transition1.equals(transition4));
        assertFalse(transition1.hashCode() == transition4.hashCode());

        Transition transition5 = new Transition(name, triggerEvent, null, outcomeState);
        assertFalse(transition1.equals(transition5));
        assertFalse(transition1.hashCode() == transition5.hashCode());
    }

    @Test
    public void testHashCode() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        int hashCode1 = transition1.hashCode();

        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);
        int hashCode2 = transition2.hashCode();

        assertEquals(hashCode1, hashCode2);

        Transition transition3 = new Transition("differentName", triggerEvent, initialState, outcomeState);
        int hashCode3 = transition3.hashCode();

        assertNotEquals(hashCode1, hashCode3);

        Transition transition4 = new Transition(name, triggerEvent, initialState, null);
        int hashCode4 = transition4.hashCode();

        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testToString() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        String expected = String.format("Transition{name='%s', initialState=%s, outcomeState=%s, triggerEvent=%s}", name, initialState, outcomeState, triggerEvent);
        String result = transition.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testTrigger() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(initialState, triggerEvent);
        assertEquals(outcomeState, result);

        result = transition.trigger(outcomeState, triggerEvent);
        assertEquals(outcomeState, result);

        result = transition.trigger(initialState, new MockEvent());
        assertEquals(initialState, result);

        result = transition.trigger(outcomeState, new MockEvent());
        assertEquals(outcomeState, result);
    }

    @Test
    public void testWillTrigger() {
        IState initialState = new MockState();
        IState outcomeState = new MockEvent();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        boolean result = transition.willTrigger(initialState, triggerEvent);
        assertFalse(result);

        result = transition.willTrigger(outcomeState, triggerEvent);
        assertFalse(result);

        result = transition.willTrigger(initialState, new MockEvent());
        assertFalse(result);

        result = transition.willTrigger(outcomeState, new MockEvent());
        assertFalse(result);

        IPredicate mockPredicate = new MockPredicate();
        transition.addGuardCondition(mockPredicate);

        result = transition.willTrigger(initialState, triggerEvent);
        assertFalse(result);

        MockEvent mockEventWithPredicateTrue = new MockEvent();
        mockPredicate.setEvent(mockEventWithPredicateTrue);

        result = transition.willTrigger(initialState, mockEventWithPredicateTrue);
        assertTrue(result);
    }

    @Test
    public void testAddGuardCondition() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IPredicate mockPredicate1 = new MockPredicate();
        IPredicate mockPredicate2 = new MockPredicate();

        transition.addGuardCondition(mockPredicate1);
        transition.addGuardCondition(mockPredicate2);

        assertEquals(2, transition.getGuardConditions().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullGuardCondition() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        transition.addGuardCondition(null);
    }

    private static class MockState implements IState {
        // Add any necessary implementations for the IState interface here
    }

    private static class MockEvent implements Event {
        // Add any necessary implementations for the Event interface here

        private IPredicate eventPredicate;

        public IPredicate getEventPredicate() {
            return eventPredicate;
        }

        public void setEventPredicate(IPredicate pEventPredicate) {
            eventPredicate = pEventPredicate;
        }
    }

    private static class MockPredicate implements IPredicate {
        // Add any necessary implementations for the IPredicate interface here

        private Event event;

        public void evaluate(Event event) {
            this.event = event;
        }

        public boolean isSatisfiedBy(Event event) {
            return this.event.equals(event);
        }
    }
}