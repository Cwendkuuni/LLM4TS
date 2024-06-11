import org.junit.Assert;
import org.junit.Test;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {

    @Test
    public void testTrigger() {
        IState initialState = new State("initial");
        IState outcomeState = new State("outcome");
        Event triggerEvent = new Event("trigger");
        Transition transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
        IState expectedResult = outcomeState;
        IState actualResult = transition.trigger(initialState, triggerEvent);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = NullPointerException.class)
    public void testTriggerWithNullInitialState() {
        Event triggerEvent = new Event("trigger");
        Transition transition = new Transition("TransitionName", triggerEvent, null, new State("outcome"));
        Transition expectedResult = null;
        IState actualResult = transition.trigger(null, triggerEvent);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = NullPointerException.class)
    public void testTriggerWithNullOutcomeState() {
        IState initialState = new State("initial");
        Event triggerEvent = new Event("trigger");
        Transition transition = new Transition("TransitionName", triggerEvent, initialState, null);
        Transition expectedResult = null;
        IState actualResult = transition.trigger(initialState, triggerEvent);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = NullPointerException.class)
    public void testTriggerWithNullTriggerEvent() {
        IState initialState = new State("initial");
        IState outcomeState = new State("outcome");
        Transition transition = new Transition("TransitionName", null, initialState, outcomeState);
        Transition expectedResult = null;
        IState actualResult = transition.trigger(initialState, null);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testTriggerWithEmptyGuardConditions() {
        IState initialState = new State("initial");
        IState outcomeState = new State("outcome");
        Event triggerEvent = new Event("trigger");
        Transition transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
        transition.evaluateGuardConditions();
        IState expectedResult = outcomeState;
        IState actualResult = transition.trigger(initialState, triggerEvent);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWillTriggerWithInvalidTriggerEvent() {
        IState initialState = new State("initial");
        IState outcomeState = new State("outcome");
        Event triggerEvent = new Event("invalid");
        Transition transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
        boolean willTrigger = transition.willTrigger(initialState, triggerEvent);
        Assert.assertFalse(willTrigger);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTriggerWithInvalidInitialState() {
        IState initialState = null;
        IState outcomeState = new State("outcome");
        Event triggerEvent = new Event("trigger");
        Transition transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
        try {
            transition.trigger(initialState, triggerEvent);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}