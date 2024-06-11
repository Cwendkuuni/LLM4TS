package uk.me.jockmacmad.jstate.state;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TransitionTest {

 @Test
 public void testTransitionCreation() {
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("TestInitialState"), new State("TestOutcomeState"));
 Assert.assertNotNull(transition);
 Assert.assertEquals("TestTransition", transition.getName());
 Assert.assertEquals(new State("TestInitialState"), transition.getInitialState());
 Assert.assertEquals(new State("TestOutcomeState"), transition.getOutcomeState());
 Assert.assertEquals(new Event("TestEvent"), transition.getTriggerEvent());
 Assert.assertTrue(transition.getGuardConditions().isEmpty());
 }

 @Test
 public void testTransitionAddGuardCondition() {
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("TestInitialState"), new State("TestOutcomeState"));
 IPredicate predicate = new AlwaysTruePredicate();
 transition.addGuardCondition(predicate);
 List<IGuardCondition> guardConditions = transition.getGuardConditions();
 Assert.assertFalse(guardConditions.isEmpty());
 Assert.assertEquals(1, guardConditions.size());
 Assert.assertEquals(predicate, guardConditions.get(0));
 }

 @Test
 public void testTransitionAddGuardConditionList() {
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("TestInitialState"), new State("TestOutcomeState"));
 List<IPredicate> predicates = Arrays.asList(new AlwaysTruePredicate(), new AlwaysFalsePredicate());
 transition.addGuardConditions(predicates.toArray(new IGuardCondition[0]));
 List<IGuardCondition> guardConditions = transition.getGuardConditions();
 Assert.assertFalse(guardConditions.isEmpty());
 Assert.assertEquals(2, guardConditions.size());
 Assert.assertTrue(ArrayUtils.containsAll(guardConditions.toArray(new IGuardCondition[0]), predicates.toArray(new IPredicate[0])));
 }

 @Test
 public void testTransitionEquals() {
 Transition transition1 = new Transition("TestTransition1", new Event("TestEvent1"), new State("TestInitialState1"), new State("TestOutcomeState1"));
 Transition transition2 = new Transition("TestTransition1", new Event("TestEvent1"), new State("TestInitialState1"), new State("TestOutcomeState1"));
 Transition transition3 = new Transition("TestTransition2", new Event("TestEvent1"), new State("TestInitialState1"), new State("TestOutcomeState1"));
 Assert.assertTrue(transition1.equals(transition2));
 Assert.assertFalse(transition1.equals(transition3));
 }

 @Test
 public void testTransitionHashCode() {
 Transition transition1 = new Transition("TestTransition1", new Event("TestEvent1"), new State("TestInitialState1"), new State("TestOutcomeState1"));
 Transition transition2 = new Transition("TestTransition1", new Event("TestEvent1"), new State("TestInitialState1"), new State("TestOutcomeState1"));
 Transition transition3 = new Transition("TestTransition2", new Event("TestEvent1"), new State("TestInitialState1"), new State("TestOutcomeState1"));
 Assert.assertEquals(transition1.hashCode(), transition2.hashCode());
 Assert.neq(transition1.hashCode(), transition3.hashCode());
 }

 @Test
 public void testTransitionToString() {
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("TestInitialState"), new State("TestOutcomeState"));
 Assert.assertNotNull(transition.toString());
 }

 @Test
 public void testTransitionTrigger() {
 State initialState = new State("TestInitialState");
 State outcomeState = new State("TestOutcomeState");
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 transition.addGuardCondition(new AlwaysTruePredicate());
 State currentState = initialState;
 Event event = new Event("TestEvent");
 State expectedOutcomeState = outcomeState;
 IState actualOutcomeState = transition.trigger(currentState, event);
 Assert.assertEquals(expectedOutcomeState, actualOutcomeState);
}

 @Test
 public void testTransitionWillTrigger() {
 State initialState = new State("TestInitialState");
 State outcomeState = new State("TestOutcomeState");
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 transition.addGuardCondition(new AlwaysTruePredicate());
 State currentState = initialState;
 Event event = new Event("TestEvent");
 boolean expectedWillTrigger = true;
 boolean actualWillTrigger = transition.willTrigger(currentState, event);
 Assert.assertEquals(expectedWillTrigger, actualWillTrigger);
 }

 @Test
 public void testTransitionWillNotTrigger() {
 State initialState = new State("TestInitialState");
 State outcomeState = new State("TestOutcomeState");
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 transition.addGuardCondition(new AlwaysFalsePredicate());
 State currentState = initialState;
 Event event = new Event("TestEvent");
 boolean expectedWillTrigger = false;
 boolean actualWillTrigger = transition.willTrigger(currentState, event);
 Assert.assertEquals(expectedWillTrigger, actualWillTrigger);
 }

 @Test
 public void testTransitionWillNotTriggerDifferentStates() {
 State initialState = new State("TestInitialState");
 State outcomeState = new State("TestOutcomeState");
 State currentState = new State("TestCurrentState");
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 transition.addGuardCondition(new AlwaysTruePredicate());
 boolean expectedWillTrigger = false;
 boolean actualWillTrigger = transition.willTrigger(currentState, new Event("TestEvent"));
 Assert.assertEquals(expectedWillTrigger, actualWillTrigger);
 }

 @Test
 public void testTransitionWillNotTriggerDifferentEvent() {
 State initialState = new State("TestInitialState");
 State outcomeState = new State("TestOutcomeState");
 Transition transition = new Transition("TestTransition", new Event("TestEvent1"), initialState, outcomeState);
 transition.addGuardCondition(new AlwaysTruePredicate());
 State currentState = initialState;
 boolean expectedWillTrigger = false;
 boolean actualWillTrigger = transition.willTrigger(currentState, new Event("TestEvent2"));
 Assert.assertEquals(expectedWillTrigger, actualWillTrigger);
 }

 @Test(expected = IllegalStateException.class)
 public void testTransitionTriggerIllegalState() {
 State initialState = new State("TestInitialState");
 State outcomeState = new State("TestOutcomeState");
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 transition.addGuardCondition(new AlwaysFalsePredicate());
 State currentState = new State("TestCurrentState");
 Event event = new Event("TestEvent");
 IState actualOutcomeState = transition.trigger(currentState, event);
 Assert.fail("Expected IllegalStateException");
 }
}