package uk.me.jockmacmad.jstate.state;

import org.junit.Assert;
import org.junit.Test;

import uk.me.jockmacmad.jstate.state.Transition;
import uk.me.jockmacmad.jstate.state.Predicate.*;
import uk.me.jockmacmad.jstate.predicate.GuardCondition.*;

public class TransitionTest {
    

    @Test
    public void TransitionTest_Constructor() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        Assert.assertNotNull(transition);
    }

    @Test
    public void TransitionTest_AddGuardCondition() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IPredicate guard1 = new AlwaysTruePredicate();
        IGuardCondition guardCondition1 = new GuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return true;
            }
        };

        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guard1);

        Assert.assertEquals(2, transition.getGuardConditions().size());
    }

    @Test
    public void TransitionTest_Equals() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);

        Assert.assertTrue(transition1.equals(transition2));
    }

    @Test
    public void TransitionTest_HashCode() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);

        Assert.assertEquals(transition1.hashCode(), transition2.hashCode());
    }

    @Test
    public void TransitionTest_ToString() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        String expected = String.format("Transition{name='%s', triggerEvent=Event{name='%s'}, initialState=State{name='TestInitialState'}, outcomeState=State{name='TestOutcomeState'}}", name, triggerEvent.getName());
        Assert.assertEquals(expected, transition.toString());
    }

    @Test
    public void TransitionTest_Trigger_ValidArgs() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        IState currentState = new State("TestCurrentState");

        IState result = transition.trigger(currentState, triggerEvent);
        Assert.assertEquals(outcomeState, result);
    }

    @Test
    public void TransitionTest_WillTrigger_ValidArgs() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        IState currentState = new State("TestCurrentState");

        boolean result = transition.willTrigger(currentState, triggerEvent);
        Assert.assertTrue(result);
    }

    @Test
    public void TransitionTest_Trigger_InvalidInitialState() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = null;
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        IState currentState = new State("TestCurrentState");

        IState result = transition.trigger(currentState, triggerEvent);
        Assert.assertEquals(currentState, result);
    }

    @Test
    public void TransitionTest_Trigger_InvalidEvent() {
        String name = "TestTransition";
        Event triggerEvent = null;
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        IState currentState = new State("TestCurrentState");

        IState result = transition.trigger(currentState, new Event("TestEvent"));
        Assert.assertEquals(currentState, result);
    }

    @Test
    public void TransitionTest_Trigger_EmptyGuardConditions() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(initialState, triggerEvent);
        Assert.assertEquals(initialState, result);
    }

    @Test
    public void TransitionTest_Trigger_MixedGuardConditions() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IPredicate guard1 = new AlwaysTruePredicate();
        IGuardCondition guardCondition1 = new GuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return true;
            }
        };

        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guard1);

        IState result = transition.trigger(initialState, triggerEvent);
        Assert.assertEquals(outcomeState, result);
    }

    @Test
    public void TransitionTest_Trigger_FailingGuardConditions() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IPredicate guard1 = new AlwaysFalsePredicate();
        IGuardCondition guardCondition1 = new GuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return true;
            }
        };

        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guard1);

        IState result = transition.trigger(initialState, triggerEvent);
        Assert.assertEquals(initialState, result);
    }

    @Test
    public void TransitionTest_Trigger_NullInitialState() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = null;
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(null, triggerEvent);
        Assert.assertNull(result);
    }

    @Test
    public void TransitionTest_Trigger_NullOutcomeState() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = null;
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(initialState, triggerEvent);
        Assert.assertEquals(initialState, result);
    }

    @Test
    public void TransitionTest_Trigger_NullTriggerEvent() {
        String name = "TestTransition";
        Event triggerEvent = null;
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IState result = transition.trigger(initialState, null);
        Assert.assertEquals(initialState, result);
    }

    @Test
    public void TransitionTest_Trigger_NullGuardConditions() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        List<IGuardCondition> guardConditions = null;
        IState result = transition.trigger(initialState, triggerEvent);
        Assert.assertEquals(initialState, result);
    }

    
}