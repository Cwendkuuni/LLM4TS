import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.Transition;
import uk.me.jockmacmad.jstate.state.DummyInitialState;
import uk.me.jockmacmad.jstate.state.DummyOutcomeState;
import uk.me.jockmacmad.jstate.event.DummyEvent;

public class TransitionTest {
    @Test
    public void testTransitionConstruction() {
        String name = "TestTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(name, transition.getName());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }
}