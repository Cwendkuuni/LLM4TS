package uk.me.jockmacmad.jstate.state;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private IState currentState;
 private List<IGuardCondition> guardConditions;

 @Before
 public void setup() {
 initialState = new FakeState("InitialState");
 outcomeState = new FakeState("OutcomeState");
 triggerEvent = new Event("TestEvent");
 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 guardConditions = new ArrayList<>();
 }

 @Test
 public void testConstructor() {
 // Basic functionality test for the constructor
 Transition testTransition = new Transition("TestTransition2", triggerEvent, initialState, outcomeState);
 Assert.assertNotEquals(transition, testTransition);
 }

 @Test
 public void testAddGuardCondition() {
 // Basic functionality test for adding a guard condition
 IPredicate predicate = new AlwaysTruePredicate();
 transition.addGuardCondition(predicate);
 Assert.assertEquals(1, transition.getGuardConditions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullGuardCondition() {
 // Test exception handling when trying to add a null guard condition
 transition.addGuardCondition(null);
 }

 @Test
 public void testEvaluateGuardConditions() {
 // Basic functionality test for evaluateGuardConditions, assuming all guard conditions evaluate to true
 transition.evaluateGuardConditions();
 }

 @Test(expected = IllegalStateException.class)
 public void testTriggerNullInitialState() {
 // Test exception handling when trying to trigger a transition with a null initial state
 currentState = null;
 Assert.assertNull(transition.trigger(currentState, triggerEvent));
 }

 @Test(expected = IllegalStateException.class)
 public void testTriggerNullOutcomeState() {
 // Test exception handling when trying to trigger a transition with a null outcome state
 outcomeState = null;
 transition.trigger(currentState, triggerEvent);
 outcomeState = null;
 Assert.assertNull(transition.trigger(currentState, triggerEvent));
 }

 @Test(expected = IllegalStateException.class)
 public void testTriggerNullTriggerEvent() {
 // Test exception handling when trying to trigger a transition with a null trigger event
 triggerEvent = null;
 transition.trigger(currentState, triggerEvent);
 triggerEvent = null;
 Assert.assertNull(transition.trigger(currentState, triggerEvent));
 }

 @Test(expected = IllegalStateException.class)
 public void testTriggerDifferentInitialState() {
 // Test exception handling when trying to trigger a transition with a different initial state
 IState differentInitialState = new FakeState("DifferentInitialState");
 currentState = initialState;
 transition.trigger(differentInitialState, triggerEvent);
 }

 @Test(expected = IllegalStateException.class)
 public void testTriggerDifferentTriggerEvent() {
 // Test exception handling when trying to trigger a transition with a different trigger event
 Event differentTriggerEvent = new Event("DifferentEvent");
 currentState = initialState;
 transition.trigger(currentState, differentTriggerEvent);
 }

 @Test
 public void testTriggerSameInitialStateSameTriggerEvent() {
 // Test functionality when triggering a transition with the same initial state and trigger event
 currentState = initialState;
 IState expectedOutcomeState = outcomeState;
 IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
 Assert.assertEquals(expectedOutcomeState, actualOutcomeState);
 }

 @Test
 public void testTriggerSameInitialStateDifferentTriggerEvent() {
 // Test functionality when triggering a transition with the same initial state but a different trigger event
 Event differentTriggerEvent = new Event("DifferentEvent");
 currentState = initialState;
 IState expectedOutcomeState = null;
 IState actualOutcomeState = transition.trigger(currentState, differentTriggerEvent);
 Assert.assertNull(actualOutcomeState);
 }

 @Test
 public void testWillTriggerSameInitialStateSameTriggerEvent() {
 // Test functionality when checking if a transition will trigger with the same initial state and trigger event
 boolean expectedWillTrigger = true;
 boolean actualWillTrigger = transition.willTrigger(currentState, triggerEvent);
 Assert.assertEquals(expectedWillTrigger, actualWillTrigger);
 }

 @Test
 public void testWillTriggerSameInitialStateDifferentTriggerEvent() {
 // Test functionality when checking if a transition will trigger with the same initial state but a different trigger event
 boolean expectedWillTrigger = false;
 boolean actualWillTrigger = transition.willTrigger(currentState, triggerEvent);
 Assert.assertEquals(expectedWillTrigger, actualWillTrigger);
 }

 @Test
 public void testWillTriggerDifferentInitialStateSameTriggerEvent() {
 // Test functionality when checking if a transition will trigger with a different initial state and the same trigger event
 IState differentInitialState = new FakeState("DifferentInitialState");
 boolean expectedWillTrigger = false;
 boolean actualWillTrigger = transition.willTrigger(differentInitialState, triggerEvent);
 Assert.assertEquals(expectedWillTrigger, actualWillTrigger);
 }

 @Test
 public void testWillTriggerDifferentInitialStateDifferentTriggerEvent() {
 // Test functionality when checking if a transition will trigger with a different initial state and a different trigger event
 IState differentInitialState = new FakeState("DifferentInitialState");
 Event differentTriggerEvent = new Event("DifferentEvent");
 boolean expectedWillTrigger = false;
 boolean actualWillTrigger = transition.willTrigger(differentInitialState, differentTriggerEvent);
 Assert.assertEquals(expectedWillTrigger, actualWillTrigger);
 }

 @Test
 public void testToString() {
 // Basic functionality test for toString method
 String expectedToString = "Transition{name='TestTransition', initialState=InitialState, outcomeState=OutcomeState, triggerEvent=TestEvent}";
 String actualToString = transition.toString();
 Assert.assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testGuardConditionsEmpty() {
 // Test functionality of transition when there are no guard conditions
 currentState = initialState;
 IState expectedOutcomeState = outcomeState;
 IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
 Assert.assertEquals(expectedOutcomeState, actualOutcomeState);
 }

 @Test
 public void testGuardConditionsOnePasses() {
 // Test functionality of transition when one guard condition passes
 IPredicate predicate1 = new AlwaysFalsePredicate();
 IPredicate predicate2 = new AlwaysTruePredicate();
 transition.addGuardCondition(predicate1);
 transition.addGuardCondition(predicate2);
 currentState = initialState;
 IState expectedOutcomeState = outcomeState;
 IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
 Assert.assertEquals(expectedOutcomeState, actualOutcomeState);
 }

 @Test
 public void testGuardConditionsAllFail() {
 // Test functionality of transition when all guard conditions fail
 IPredicate predicate1 = new AlwaysFalsePredicate();
 IPredicate predicate2 = new AlwaysFalsePredicate();
 transition.addGuardCondition(predicate1);
 transition.addGuardCondition(predicate2);
 currentState = initialState;
 IState expectedOutcomeState = initialState;
 IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
 Assert.assertEquals(expectedOutcomeState, actualOutcomeState);
 }

 @Test(expected = IllegalStateException.class)
 public void testGuardConditionsNullGuardCondition() {
 // Test exception handling when adding a null guard condition
 transition.addGuardCondition(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testGuardConditionsArraySizeMismatch() {
 // Test exception handling when the size of the guard conditions array does not match the expected size
 transition.addGuardCondition(predicate1);
 transition.addGuardCondition(predicate2);
 transition.addGuardCondition(predicate1); // duplicated guard condition
 }

 @Test(expected = IllegalStateException.class)
 public void testTriggerWithMultipleGuardsPassing() {
 // Test exception handling when multiple guards pass
 IPredicate predicate1 = new AlwaysTruePredicate();
 IPredicate predicate2 = new AlwaysTruePredicate();
 transition.addGuardCondition(predicate1);
 transition.addGuardCondition(predicate2);
 currentState = initialState;
 IState expectedOutcomeState = outcomeState;
 IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
 Assert.assertEquals(expectedOutcomeState, actualOutcomeState);
 // Add an extra guard condition and check that an IllegalStateException is thrown
 IPredicate predicate3 = new AlwaysTruePredicate();
 transition.addGuardCondition(predicate3);
 currentState = initialState;
 Assert.assertThrows(IllegalStateException.class, () -> transition.trigger(currentState, triggerEvent));
 }

}