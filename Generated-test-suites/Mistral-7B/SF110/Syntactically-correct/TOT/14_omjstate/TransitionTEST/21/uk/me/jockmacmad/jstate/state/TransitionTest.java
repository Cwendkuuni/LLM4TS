import org.junit.Test;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {

    @Test
    public void testTrigger() {
        IState initialState = new DummyState();
        IState outcomeState = new DummyState();
        Event triggerEvent = new DummyEvent();

        Transition transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(initialState, triggerEvent);

        assertEquals(outcomeState, result);
    }

    @Test
    public void testWillTrigger_NoGuards() {
        IState initialState = new DummyState();
        IState outcomeState = new DummyState();
        Event triggerEvent = new DummyEvent();

        Transition transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);

        assertTrue(transition.willTrigger(initialState, triggerEvent));
    }

    @Test
    public void testWillTrigger_OneGuard() {
        IState initialState = new DummyState();
        IState outcomeState = new DummyState();
        Event triggerEvent = new DummyEvent();

        Transition transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);

        IGuardCondition guardCondition = new DummyGuardCondition();
        transition.addGuardCondition(guardCondition);

        assertTrue(transition.willTrigger(initialState, triggerEvent));
    }

    @Test
    public void testWillTrigger_MultipleGuards() {
        IState initialState = new DummyState();
        IState outcomeState = new DummyState();
        Event triggerEvent = new DummyEvent();

        Transition transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);

        IGuardCondition guardCondition1 = new DummyGuardCondition();
        transition.addGuardCondition(guardCondition1);

        IGuardCondition guardCondition2 = new DummyGuardCondition();
        transition.addGuardCondition(guardCondition2);

        assertTrue(transition.willTrigger(initialState, triggerEvent));
    }

    @Test
    public void testWillTrigger_GuardsFail() {
        IState initialState = new DummyState();
        IState outcomeState = new DummyState();
        Event triggerEvent = new DummyEvent();

        Transition transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);

        IGuardCondition guardCondition = new FailingGuardCondition();
        transition.addGuardCondition(guardCondition);

        assertFalse(transition.willTrigger(initialState, triggerEvent));
    }

    @Test
    public void testTrigger_GuardsFail() {
        IState initialState = new DummyState();
        IState outcomeState = new DummyState();
        Event triggerEvent = new DummyEvent();

        Transition transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);

        IGuardCondition guardCondition = new FailingGuardCondition();
        transition.addGuardCondition(guardCondition);

        IState result = transition.trigger(initialState, triggerEvent);

        assertEquals(initialState, result);
    }

    static class DummyState implements IState {
        @Override
        public boolean equals(Object o) {
            return true;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String toString() {
            return "DummyState";
        }
    }

    static class DummyEvent implements Event {
        @Override
        public boolean equals(Object o) {
            return true;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String toString() {
            return "DummyEvent";
        }
    }

    static class DummyGuardCondition implements IGuardCondition {
        @Override
        public boolean evaluate(Event event) {
            return true;
        }
    }

    static class FailingGuardCondition implements IGuardCondition {
        @Override
        public boolean evaluate(Event event) {
            return false;
        }
    }
}