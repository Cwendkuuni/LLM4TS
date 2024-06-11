import org.junit.Assert;
import org.junit.Test;
import uk.me.jockmacmad.jstate.state.Transition;

public class TransitionTest {
    @Test
    public void testTransitionConstruction() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new DummyState(); // Assuming DummyState is an instance of IState
        IState outcomeState = new DummyState();
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        Assert.assertEquals(name, transition.getName());
        Assert.assertEquals(triggerEvent, transition.getTriggerEvent());
        Assert.assertEquals(initialState, transition.getInitialState());
        Assert.assertEquals(outcomeState, transition.getOutcomeState());
    }
}