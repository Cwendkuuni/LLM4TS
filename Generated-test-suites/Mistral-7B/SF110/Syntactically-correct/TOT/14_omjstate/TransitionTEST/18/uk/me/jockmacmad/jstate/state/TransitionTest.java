package uk.me.jockmacmad.jstate.state;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.Transition;
import uk.me.jockmacmad.jstate.state.DummyState;
import uk.me.jockmacmad.jstate.event.DummyEvent;
import uk.me.jockmacmad.jstate.predicate.DummyPredicate;
import uk.me.jockmacmad.jstate.predicate.NullPredicate;

public class TransitionTest {

    @Test
    public void testTransition_HappyPath() {
        String name = "TestTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyState();
        IState outcomeState = new DummyState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState expectedResult = outcomeState;
        IState actualResult = transition.trigger(initialState, triggerEvent);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testWillTrigger_HappyPath() {
        String name = "TestTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyState();
        IState outcomeState = new DummyState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertTrue(transition.willTrigger(initialState, triggerEvent));
    }

    @Test
    public void testWillTrigger_ErrorScenario() {
        String name = "TestTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyState();
        IState outcomeState = new DummyState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(new NullPredicate()); // Adding an invalid guard condition

        assertFalse(transition.willTrigger(initialState, triggerEvent));
    }

    @Test
    public void testConstructor_HappyPath() {
        String name = "TestTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyState();
        IState outcomeState = new DummyState();

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(transition1, transition2);
    }

    @Test
    public void testEquals_DifferentObjects() {
        String name = "TestTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyState();
        IState outcomeState = new DummyState();

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("TestTransition2", triggerEvent, initialState, outcomeState);

        assertNotEquals(transition1, transition2);
    }
}