import org.junit.Test;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {
    @Test
    public void testTrigger_InitialStateMatchesAndEventMatches_OutcomeStateShouldBeSet() {
        IState initialState = new ConcreteState(); // replace ConcreteState with actual initial state implementation
        IState outcomeState = new ConcreteState(); // replace ConcreteState with actual outcome state implementation
        Event triggerEvent = new ConcreteEvent(); // replace ConcreteEvent with actual event implementation
        Transition transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);

        IState currentState = initialState;

        IState actualOutcome = transition.trigger(currentState, triggerEvent);
        IState expectedOutcome = outcomeState;

        assertEquals(expectedOutcome, actualOutcome);
    }
}