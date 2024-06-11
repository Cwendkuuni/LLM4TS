import org.junit.Test;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {

    @Test
    public void testTrigger_ValidTransition() {
        IState initialState = new ConcreteState(); // replace ConcreteState with the actual initial state class
        IState outcomeState = new ConcreteState(); // replace ConcreteState with the actual outcome state class
        Event triggerEvent = new ConcreteEvent(); // replace ConcreteEvent with the actual event class

        Transition transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);

        IState currentState = initialState;
        IState expectedOutcomeState = outcomeState;

        IState actualOutcomeState = transition.trigger(currentState, triggerEvent);

        assertEquals(expectedOutcomeState, actualOutcomeState);
    }
}