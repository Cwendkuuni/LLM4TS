import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.Transition;

public class TransitionTest {
    @Test
    public void testTransitionConstruction() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals(name, transition.getName());
        assertEquals(triggerEvent, transition.getTriggerEvent());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertTrue(transition.getGuardConditions().isEmpty());
    }
}