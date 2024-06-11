import org.junit.Test;
import static org.junit.Assert.*;

public class TransitionTest {
    @Test
    public void testTransitionConstructor() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertEquals("TestTransition", transition.getName());
        assertEquals(triggerEvent, transition.getTriggerEvent());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
    }
}