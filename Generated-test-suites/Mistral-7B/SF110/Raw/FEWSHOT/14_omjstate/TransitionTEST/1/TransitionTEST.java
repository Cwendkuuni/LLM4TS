package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.predicate.*;
import org.apache.commons.lang3.builder.*;
import java.util.*;

public class TransitionTest {

    @Test
    public void testEquals() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition3 = new Transition("differentName", triggerEvent, initialState, outcomeState);

        Transition transition4 = transition1;

        assertEquals(transition1, transition1);
        assertEquals(transition1, transition2);
        assertNotEquals(transition1, transition3);
        assertNotEquals(transition1, null);
        assertNotEquals(transition1, new Object());
        assertEquals(transition1, transition4);
    }

    @Test
    public void testHashCode() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition3 = new Transition("differentName", triggerEvent, initialState, outcomeState);

        int hashCode1 = transition1.hashCode();
        int hashCode2 = transition2.hashCode();
        int hashCode3 = transition3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        String expectedToString = "Transition{name='" + name + "', triggerEvent=" + triggerEvent + ", initialState=" + initialState + ", outcomeState=" + outcomeState + '}';
        String actualToString = transition.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testTrigger_stateAndEventMatch() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = initialState;

        IState result = transition.trigger(currentState, triggerEvent);

        assertEquals(outcomeState, result);
    }

    @Test
    public void testTrigger_stateDoesNotMatch() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = new MockState();

        IState result = transition.trigger(currentState, triggerEvent);

        assertNotEquals(outcomeState, result);
    }

    @Test
    public void testTrigger_eventDoesNotMatch() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        Event differentEvent = new MockEvent();
        String name = "testTransition";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = initialState;

        IState result = transition.trigger(currentState, differentEvent);

        assertNotEquals(outcomeState, result);
    }

    @Test
    public void testTrigger_guardsPass() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        IGuardCondition guard1 = new MockGuardCondition();
        IGuardCondition guard2 = new MockGuardCondition();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(guard1);
        transition.addGuardCondition(guard2);

        IState currentState = initialState;

        IState result = transition.trigger(currentState, triggerEvent);

        assertEquals(outcomeState, result);
    }

    @Test
    public void testTrigger_guardsFail() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        IGuardCondition guard1 = new MockGuardCondition();
        IGuardCondition guard2 = new MockGuardCondition();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(guard1);
        transition.addGuardCondition(guard2);

        IState currentState = initialState;

        IGuardCondition guardFailureMock = mock(IGuardCondition.class);
        when(guardFailureMock.evaluate(triggerEvent)).thenReturn(false);

        transition.addGuardCondition(guardFailureMock);

        IState result = transition.trigger(currentState, triggerEvent);

        assertNotEquals(outcomeState, result);
    }

    @Test
    public void testWillTrigger_stateAndEventMatch() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = initialState;

        boolean result = transition.willTrigger(currentState, triggerEvent);

        assertTrue(result);
    }

    @Test
    public void testWillTrigger_stateDoesNotMatch() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = new MockState();

        boolean result = transition.willTrigger(currentState, triggerEvent);

        assertFalse(result);
    }

    @Test
    public void testWillTrigger_eventDoesNotMatch() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        Event differentEvent = new MockEvent();
        String name = "testTransition";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState currentState = initialState;

        boolean result = transition.willTrigger(currentState, differentEvent);

        assertFalse(result);
    }

    @Test
    public void testWillTrigger_guardsPass() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        IGuardCondition guard1 = new MockGuardCondition();
        IGuardCondition guard2 = new MockGuardCondition();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(guard1);
        transition.addGuardCondition(guard2);

        IState currentState = initialState;

        IGuardCondition guardMock = mock(IGuardCondition.class);
        when(guardMock.evaluate(triggerEvent)).thenReturn(true);

        transition.addGuardCondition(guardMock);

        boolean result = transition.willTrigger(currentState, triggerEvent);

        assertTrue(result);
    }

    @Test
    public void testWillTrigger_guardsFail() {
        IState initialState = new MockState();
        IState outcomeState = new MockState();
        Event triggerEvent = new MockEvent();
        String name = "testTransition";

        IGuardCondition guard1 = new MockGuardCondition();
        IGuardCondition guard2 = new MockGuardCondition();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(guard1);
        transition.addGuardCondition(guard2);

        IState currentState = initialState;

        IGuardCondition guardFailureMock = mock(IGuardCondition.class);
        when(guardFailureMock.evaluate(triggerEvent)).thenReturn(false);

        transition.addGuardCondition(guardFailureMock);

        boolean result = transition.willTrigger(currentState, triggerEvent);

        assertFalse(result);
    }

    private static class MockState implements IState {
        @Override
        public boolean equals(Object object) {
            return super.equals(object);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return "MockState{}";
        }
    }

    private static class MockEvent implements Event {
        @Override
        public boolean equals(Object object) {
            return super.equals(object);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return "MockEvent{}";
        }
    }

    private static class MockGuardCondition implements IGuardCondition {
        @Override
        public boolean evaluate(Event event) {
            return true;
        }
    }
}