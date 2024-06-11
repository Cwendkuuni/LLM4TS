package uk.me.jockmacmad.jstate.state;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Assert;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.List;

public class TransitionTest {

    private Transition validTransition;
    private Transition invalidTransition;

    @Test
    public void testTransitionConstructor() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("initialState");
        IState outcomeState = new ConcreteState("outcomeState");
        validTransition = new Transition(name, triggerEvent, initialState, outcomeState);
        Assert.assertEquals(name, validTransition.getName());
        Assert.assertEquals(initialState, validTransition.getInitialState());
        Assert.assertEquals(outcomeState, validTransition.getOutcomeState());
        Assert.assertEquals(triggerEvent, validTransition.getTriggerEvent());
    }

    @Test
    public void testAddGuardCondition() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("initialState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IPredicate predicate = new AlwaysTruePredicate();
        transition.addGuardCondition(predicate);
        Assert.assertTrue(transition.getGuardConditions().size() > 0);
        Assert.assertEquals(predicate, transition.getGuardConditions().get(0));
    }

    @Test
    public void testGetInitialState() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("initialState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        Assert.assertEquals(initialState, transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("initialState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        Assert.assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("initialState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        Assert.assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testEquals() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("initialState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);
        Assert.assertTrue(transition1.equals(transition2));
    }

    @Test
    public void testHashCode() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("initialState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);
        Assert.assertEquals(transition1.hashCode(), transition2.hashCode());
    }

    @Test
    public void testToString() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("initialState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        String expected = String.format("Transition{name='%s', triggerEvent=%s, initialState=%s, outcomeState=%s}", name, triggerEvent, initialState, outcomeState);
        Assert.assertEquals(expected, transition.toString());
    }

    @Test
    public void testTrigger_ValidTransition() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("initialState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        List<IPredicate> guardConditions = new ArrayList<>();
        transition.addGuardCondition(new AlwaysTruePredicate());
        IState currentState = initialState;
        IState expectedOutcomeState = outcomeState;
        IState result = transition.trigger(currentState, triggerEvent);
        Assert.assertEquals(expectedOutcomeState, result);
    }

    @Test
    public void testTrigger_InvalidInitialState() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("invalidState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        IState currentState = new ConcreteState("currentState");
        IState expectedCurrentState = currentState;
        IState result = transition.trigger(currentState, triggerEvent);
        Assert.assertEquals(expectedCurrentState, result);
    }

    @Test
    public void testTrigger_InvalidTriggerEvent() {
        String name = "testTransition";
        Event triggerEvent = new Event("invalidEvent");
        IState initialState = new ConcreteState("initialState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        IState currentState = initialState;
        IState expectedCurrentState = currentState;
        IState result = transition.trigger(currentState, new Event("testEvent"));
        Assert.assertEquals(expectedCurrentState, result);
    }

    @Test
    public void testTrigger_NoGuards() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("initialState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        List<IPredicate> guardConditions = new ArrayList<>();
        IState currentState = initialState;
        IState expectedCurrentState = currentState;
        try {
            transition.evaluateGuardConditions();
        } catch (NotImplementedException e) {
            // Expected
        }
        IState result = transition.trigger(currentState, triggerEvent);
        Assert.assertEquals(expectedCurrentState, result);
    }

    @Test
    public void testWillTrigger_ValidTransition() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("initialState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        List<IPredicate> guardConditions = new ArrayList<>();
        guardConditions.add(new AlwaysTruePredicate());
        IState currentState = initialState;
        boolean expectedResult = true;
        boolean result = transition.willTrigger(currentState, triggerEvent);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testWillTrigger_InvalidInitialState() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("invalidState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        List<IPredicate> guardConditions = new ArrayList<>();
        IState currentState = new ConcreteState("currentState");
        boolean expectedResult = false;
        boolean result = transition.willTrigger(currentState, triggerEvent);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testWillTrigger_InvalidTriggerEvent() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("initialState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        List<IPredicate> guardConditions = new ArrayList<>();
        IState currentState = initialState;
        boolean expectedResult = false;
        boolean result = transition.willTrigger(currentState, new Event("invalidEvent"));
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testWillTrigger_NoGuards() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("initialState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        List<IPredicate> guardConditions = new ArrayList<>();
        IState currentState = initialState;
        boolean expectedResult = false;
        boolean result = transition.willTrigger(currentState, triggerEvent);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testWillTrigger_NoMatchingGuards() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("initialState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        List<IPredicate> guardConditions = new ArrayList<>();
        guardConditions.add(new AlwaysFalsePredicate());
        IState currentState = initialState;
        boolean expectedResult = false;
        boolean result = transition.willTrigger(currentState, triggerEvent);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testWillTrigger_MatchingGuards() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new ConcreteState("initialState");
        IState outcomeState = new ConcreteState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        List<IPredicate> guardConditions = new ArrayList<>();
        guardConditions.add(new EventMatchesPredicate(triggerEvent));
        IState currentState = initialState;
        boolean expectedResult = true;
        boolean result = transition.willTrigger(currentState, triggerEvent);
        Assert.assertEquals(expectedResult, result);
    }

    @Before
    public void setUp() {
        validTransition = new Transition("testTransition", new Event("testEvent"), new ConcreteState("initialState"), new ConcreteState("outcomeState"));
        validTransition.addGuardCondition(new AlwaysTruePredicate());
        invalidTransition = new Transition("testInvalidTransition", new Event("testEvent"), new ConcreteState("invalidState"), new ConcreteState("outcomeState"));
    }
}