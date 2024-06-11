package uk.me.jockmacmad.jstate.state.test;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;
import uk.me.jockmacmad.jstate.state.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TransitionTest {

    @Test
    public void testTransitionConstructor() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        Assert.assertEquals(name, transition.getName());
        Assert.assertEquals(triggerEvent, transition.getTriggerEvent());
        Assert.assertEquals(initialState, transition.getInitialState());
        Assert.assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testAddGuardCondition() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        IPredicate predicate = new AlwaysTruePredicate();
        transition.addGuardCondition(predicate);
        List<IGuardCondition> guardConditions = transition.getGuardConditions();
        Assert.assertEquals(1, guardConditions.size());
        Assert.assertEquals(predicate, guardConditions.get(0));
    }

    @Test
    public void testEquals() {
        String name1 = "TestTransition1";
        String name2 = "TestTransition2";
        Event triggerEvent1 = new Event("TestEvent1");
        Event triggerEvent2 = new Event("TestEvent2");
        IState initialState1 = new State("TestInitialState1");
        IState initialState2 = new State("TestInitialState2");
        IState outcomeState1 = new State("TestOutcomeState1");
        IState outcomeState2 = new State("TestOutcomeState2");
        Transition transition1 = new Transition(name1, triggerEvent1, initialState1, outcomeState1);
        Transition transition2 = new Transition(name2, triggerEvent2, initialState1, outcomeState2);
        Transition transition3 = new Transition(name1, triggerEvent1, initialState1, outcomeState1);
        Assert.assertFalse(transition1.equals(transition2));
        Assert.assertTrue(transition1.equals(transition3));
    }

    @Test
    public void testHashCode() {
        String name1 = "TestTransition1";
        String name2 = "TestTransition2";
        Event triggerEvent1 = new Event("TestEvent1");
        Event triggerEvent2 = new Event("TestEvent2");
        IState initialState1 = new State("TestInitialState1");
        IState initialState2 = new State("TestInitialState2");
        IState outcomeState1 = new State("TestOutcomeState1");
        IState outcomeState2 = new State("TestOutcomeState2");
        Transition transition1 = new Transition(name1, triggerEvent1, initialState1, outcomeState1);
        Transition transition2 = new Transition(name2, triggerEvent2, initialState2, outcomeState2);
        Transition transition3 = new Transition(name1, triggerEvent1, initialState1, outcomeState1);
        Assert.assertNotSame(transition1.hashCode(), transition2.hashCode());
        Assert.assertEquals(transition1.hashCode(), transition3.hashCode());
    }

    @Test
    public void testToString() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        String expectedToString = "Transition{" +
                "name='" + name + '\'' +
                ", triggerEvent=" + triggerEvent +
                ", initialState=" + initialState +
                ", outcomeState=" + outcomeState +
                '}';
        String actualToString = transition.toString();
        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTriggerNullInitialState() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        Transition transition = new Transition(name, triggerEvent, null, new State("TestOutcomeState"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTriggerNullTriggerEvent() {
        String name = "TestTransition";
        IState initialState = new State("TestInitialState");
        Transition transition = new Transition(name, null, initialState, new State("TestOutcomeState"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTriggerDifferentInitialState() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState1 = new State("TestInitialState1");
        IState initialState2 = new State("TestInitialState2");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState1, outcomeState);
        Assert.assertFalse(transition.willTrigger(initialState2, triggerEvent));
        transition.trigger(initialState2, triggerEvent);
        Assert.assertEquals(initialState2, transition.getInitialState());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTriggerDifferentTriggerEvent() {
        String name = "TestTransition";
        IState initialState = new State("TestInitialState");
        Event triggerEvent1 = new Event("TestEvent1");
        Event triggerEvent2 = new Event("TestEvent2");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent1, initialState, outcomeState);
        Assert.assertFalse(transition.willTrigger(initialState, triggerEvent2));
        transition.trigger(initialState, triggerEvent2);
        Assert.assertEquals(initialState, transition.getInitialState());
    }

    @Test
    public void testTriggerSameInitialStateSameTriggerEvent() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(new AlwaysTruePredicate());
        Assert.assertTrue(transition.willTrigger(initialState, triggerEvent));
        IState result = transition.trigger(initialState, triggerEvent);
        Assert.assertEquals(outcomeState, result);
    }

    @Test
    public void testTriggerSameInitialStateDifferentTriggerEvent() {
        String name = "TestTransition";
        Event triggerEvent1 = new Event("TestEvent1");
        Event triggerEvent2 = new Event("TestEvent2");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent1, initialState, outcomeState);
        transition.addGuardCondition(new AlwaysFalsePredicate());
        Assert.assertFalse(transition.willTrigger(initialState, triggerEvent2));
        IState result = transition.trigger(initialState, triggerEvent2);
        Assert.assertEquals(initialState, result);
    }

    @Test
    public void testTriggerInitialStateNotMatched() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, new State("TestInitialState1"), outcomeState);
        Assert.assertFalse(transition.willTrigger(initialState, triggerEvent));
        IState result = transition.trigger(initialState, triggerEvent);
        Assert.assertEquals(initialState, result);
    }

    @Test
    public void testTriggerGuardConditions() {
        String name = "TestTransition";
        Event triggerEvent = new Event("TestEvent");
        IState initialState = new State("TestInitialState");
        IState outcomeState = new State("TestOutcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(new GuardConditionA());
        guardConditions.add(new GuardConditionB());
        transition.setGuardConditions(guardConditions);
        Assert.assertFalse(transition.willTrigger(initialState, triggerEvent));
        IState result = transition.trigger(initialState, triggerEvent);
        Assert.assertEquals(initialState, result);
        IState guardConditionAInitialState = new State("GuardConditionAInitialState");
        IState guardConditionBInitialState = new State("GuardConditionBInitialState");
        IState guardConditionAOutcomeState = new State("GuardConditionAOutcomeState");
        IState guardConditionBOutcomeState = new State("GuardConditionBOutcomeState");
        IPredicate guardConditionA = new GuardConditionAPredicate();
        IPredicate guardConditionB = new GuardConditionBPredicate();
        Transition guardConditionATransition = new Transition(name, triggerEvent, guardConditionAInitialState, guardConditionAOutcomeState);
        guardConditionATransition.addGuardCondition(guardConditionA);
        Transition guardConditionBTransition = new Transition(name, triggerEvent, guardConditionBInitialState, guardConditionBOutcomeState);
        guardConditionBTransition.addGuardCondition(guardConditionB);
        transition.addGuardCondition(guardConditionATransition);
        transition.addGuardCondition(guardConditionBTransition);
        Assert.assertTrue(transition.willTrigger(initialState, triggerEvent));
        result = transition.trigger(initialState, triggerEvent);
        Assert.assertEquals(outcomeState, result);
    }

    private static class AlwaysTruePredicate implements IPredicate {
        @Override
        public boolean evaluate(Event event) {
            return true;
        }
    }

    private static class AlwaysFalsePredicate implements IPredicate {
        @Override
        public boolean evaluate(Event event) {
            return false;
        }
    }

    private static class GuardConditionAPredicate implements IPredicate {
        @Override
        public boolean evaluate(Event event) {
            return Arrays.equals(event.getParameters(), new String[]{"TestParameterA"});
        }
    }

    private static class GuardConditionBPredicate implements IPredicate {
        @Override
        public boolean evaluate(Event event) {
            return Arrays.equals(event.getParameters(), new String[0]);
        }
    }

    private static class GuardConditionA implements IGuardCondition {
        @Override
        public boolean evaluate(Event event) {
            return Arrays.equals(event.getParameters(), new String[]{"TestParameterA"});
        }
    }

    private static class GuardConditionB implements IGuardCondition {
        @Override
        public boolean evaluate(Event event) {
            return Arrays.equals(event.getParameters(), new String[0]);
        }
    }

    private static class GuardCondition implements IGuardCondition {
        @Override
        public boolean evaluate(Event event) {
            return false;
        }
    }
}