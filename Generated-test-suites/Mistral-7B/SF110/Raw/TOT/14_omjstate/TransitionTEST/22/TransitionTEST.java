import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.Transition;
import uk.me.jockmacmad.jstate.state.DummyInitialState;
import uk.me.jockmacmad.jstate.state.DummyOutcomeState;
import uk.me.jockmacmad.jstate.state.DummyGuardCondition;
import uk.me.jockmacmad.jstate.event.DummyEvent;

public class TransitionTest {
    private Transition transition;

    @Before
    public void setUp() {
        transition = new Transition("TestTransition", new DummyEvent("TestEvent"), new DummyInitialState(), new DummyOutcomeState());
        transition.addGuardCondition(new DummyGuardCondition());
    }

    @Test
    public void testTrigger_InitialStateAndEventMatch_GuardsPass_OutcomeStateReturned() {
        IState currentState = new DummyInitialState();
        IState expectedOutcomeState = new DummyOutcomeState();
        IState actualOutcomeState = transition.trigger(currentState, new DummyEvent());
        assertEquals("Unexpected outcome state", expectedOutcomeState, actualOutcomeState);
    }

    // Other test cases for edge cases and error scenarios can be added here.
}