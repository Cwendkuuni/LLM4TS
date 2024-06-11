import org.junit.Test;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.Transition;
import uk.me.jockmacmad.jstate.state.DummyInitialState;
import uk.me.jockmacmad.jstate.state.DummyOutcomeState;
import uk.me.jockmacmad.jstate.state.DummyGuardCondition;
import uk.me.jockmacmad.jstate.event.DummyEvent;

public class TransitionTest {
    @Test
    public void testTrigger() {
        // Given
        Transition transition = new Transition("Transition1", new DummyEvent("Event1"), new DummyInitialState(), new DummyOutcomeState());
        transition.addGuardCondition(new DummyGuardCondition());

        // When
        IState currentState = new DummyInitialState();
        IState expectedOutcomeState = new DummyOutcomeState();
        IState result = transition.trigger(currentState, new DummyEvent("Event1"));

        // Then
        assertEquals(expectedOutcomeState, result);
    }
}