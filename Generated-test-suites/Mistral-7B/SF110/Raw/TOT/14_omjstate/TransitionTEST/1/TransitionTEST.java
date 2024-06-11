import org.junit.Test;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.Transition;
import uk.me.jockmacmad.jstate.state.DummyInitialState;
import uk.me.jockmacmad.jstate.state.DummyOutcomeState;
import uk.me.jockmacmad.jstate.state.DummyTransition;
import uk.me.jockmacmad.jstate.event.DummyEvent;
import java.util.ArrayList;

public class TransitionTest {
    @Test
    public void testTransition() {
        String name = "testTransition";
        Event triggerEvent = new DummyEvent();
        IState initialState = new DummyInitialState();
        IState outcomeState = new DummyOutcomeState();
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        // Test triggering the transition with valid initial state and event passing all guards
        IState actualState = transition.trigger(initialState, triggerEvent);
        IState expectedState = outcomeState;
        assertEquals(expectedState, actualState);

        // Test triggering the transition with valid initial state and event not passing any guard
        ArrayList<IPredicate> guardPredicates = new ArrayList<>();
        IPredicate guardPredicate = () -> false;
        transition.addGuardCondition(guardPredicate);
        actualState = transition.trigger(initialState, triggerEvent);
        assertNotEquals(expectedState, actualState);

        // Test triggering the transition with invalid initial state
        IState invalidInitialState = new DummyInitialState();
        actualState = transition.trigger(invalidInitialState, triggerEvent);
        assertNotEquals(expectedState, actualState);

        // Test triggering the transition with invalid event
        Event invalidEvent = new DummyEvent();
        actualState = transition.trigger(initialState, invalidEvent);
        assertNotEquals(expectedState, actualState);

        // Test triggering the transition with empty guard conditions
        transition.getGuardConditions().clear();
        actualState = transition.trigger(initialState, triggerEvent);
        assertEquals(expectedState, actualState);
    }
}