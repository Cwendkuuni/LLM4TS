import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.Transition;
import uk.me.jockmacmad.jstate.state.DummyInitialState;
import uk.me.jockmacmad.jstate.state.DummyOutcomeState;
import uk.me.jockmacmad.jstate.state.DummyGuardCondition;
import uk.me.jockmacmad.jstate.event.DummyEvent;

public class TransitionTest {
    // 

    @Test
    public void testTransition_ValidTransition() {
        Transition transition = new Transition("TestTransition", new DummyEvent("TestEvent"), new DummyInitialState("Initial"), new DummyOutcomeState("Outcome"));
        transition.addGuardCondition(new DummyGuardCondition("GuardCondition", true));

        IState initialState = new DummyInitialState("Initial");
        IState outcomeState = new DummyOutcomeState("Outcome");
        Event event = new DummyEvent("TestEvent");

        IState result = transition.trigger(initialState, event);
        assertTrue("Transition should result in the expected outcome state", outcomeState.equals(result));
    }

    // 
}