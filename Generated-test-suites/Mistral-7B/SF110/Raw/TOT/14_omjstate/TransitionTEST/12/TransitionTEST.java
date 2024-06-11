
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {
    private Transition validTransition;
    private IState initialState;
    private IState outcomeState;
    private Event event;

    @Before
    public void setup() {
        initialState = new MyInitialState();
        outcomeState = new MyOutcomeState();
        event = new MyEvent();
        validTransition = new Transition("validTransition", event, initialState, outcomeState);
    }

    @Test
    public void testValidTransition() {
        IState result = validTransition.trigger(initialState, event);
        assertEquals(outcomeState, result);
    }

    @Test
    public void testInvalidTransition() {
        IState otherState = new OtherState();
        IState result = validTransition.trigger(otherState, event);
        assertNotEquals(outcomeState, result);
    }

    @Test
    public void testWillTriggerValidTransition() {
        boolean result = validTransition.willTrigger(initialState, event);
        assertTrue(result);
    }

    @Test
    public void testWillNotTriggerInvalidTransition() {
        IState otherState = new OtherState();
        boolean result = validTransition.willTrigger(otherState, event);
        assertFalse(result);
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("test", new Event("test"), new State("test"), new State("test"));
        Transition transition2 = new Transition("test", new Event("test"), new State("test"), new State("test"));
        assertEquals(transition1, transition2);
    }

    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("test", new Event("test"), new State("test"), new State("test"));
        Transition transition2 = new Transition("test", new Event("test"), new State("test"), new State("test"));
        assertEquals(transition1.hashCode(), transition2.hashCode());
    }
}