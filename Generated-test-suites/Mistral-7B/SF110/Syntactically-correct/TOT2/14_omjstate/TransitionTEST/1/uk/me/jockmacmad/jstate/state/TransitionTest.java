
import org.junit.Test;
import org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import uk.me.jockmacmad.jstate.state.Transition;
import uk.me.jockmacmad.jstate.state.DummyState;
import uk.me.jockmacmad.jstate.predicate.DummyPredicate;
import uk.me.jockmacmad.jstate.predicate.DummyGuardCondition;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class TransitionTest {

    @Test
    public void testTransitionConstructor() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new DummyState();
        IState outcomeState = new DummyState();
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        assertEquals(name, transition.getName());
        assertEquals(triggerEvent, transition.getTriggerEvent());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testAddGuardConditionIpredicate() {
        Transition transition = new Transition("TestTransition", new Event("TestEvent"), new DummyState(), new DummyState());
        IPredicate predicate = new DummyPredicate();
        transition.addGuardCondition(predicate);
        assertEquals(1, transition.getGuardConditions().size());
    }

    @Test
    public void testAddGuardConditionIGuardCondition() {
        Transition transition = new Transition("TestTransition", new Event("TestEvent"), new DummyState(), new DummyState());
        IGuardCondition guardCondition = new DummyGuardCondition();
        transition.addGuardCondition(guardCondition);
        assertEquals(1, transition.getGuardConditions().size());
    }

    @Test(expected = AssertionError.class)
    public void testIncorrectStateAndEvent() {
        Transition transition = new Transition("TestTransition", new Event("TestEvent1"), new DummyState(), new DummyState());
        IState incorrectState = new DummyState();
        assertEquals(initialState, transition.getInitialState()); // this should fail
    }

    @Test(expected = AssertionError.class)
    public void testIncorrectEvent() {
        Transition transition = new Transition("TestTransition", new Event("TestEvent1"), new DummyState(), new DummyState());
        Event incorrectEvent = new Event("TestEvent2");
        assertEquals(triggerEvent, transition.getTriggerEvent()); // this should fail
    }

    @Test
    public void testFalseGuardConditions() {
        Transition transition = new Transition("TestTransition", new Event("TestEvent"), new DummyState(), new DummyState());
        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(new DummyGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return false;
            }
        });
        transition.setGuardConditions(guardConditions);
        assertFalse(transition.willTrigger(new DummyState(), new Event("TestEvent")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaximumAllowedLimitOfGuardConditions() {
        Transition transition = new Transition("TestTransition", new Event("TestEvent"), new DummyState(), new DummyState());
        List<IGuardCondition> guardConditions = new ArrayList<>();
        for(int i = 0; i <= Integer.MAX_VALUE; i++) {
            guardConditions.add(new DummyGuardCondition());
            transition.setGuardConditions(guardConditions);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        String name = null;
        Event triggerEvent = null;
        IState initialState = null;
        IState outcomeState = null;
        try {
            new Transition(name, triggerEvent, initialState, outcomeState);
        } catch (NullPointerException e) {
            assertEquals("name", e.getMessage());
        }
    }
}
