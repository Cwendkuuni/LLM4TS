package uk.me.jockmacmad.jstate.state.test;

import static org.junit.Assert.*;

import org.apache.commons.lang.GenericTestUtils;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.*;

import java.util.*;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;

 @Test
 public void testTransitionConstructor() {
 // Test constructor with valid arguments
 String name = "TestTransition";
 Event triggerEvent = new Event("TestEvent");
 IState initialState = new State("TestInitialState");
 IState outcomeState = new State("TestOutcomeState");
 transition = new Transition(name, triggerEvent, initialState, outcomeState);

 // Test constructor with null arguments
 assertNull("Transition constructor should fail with null name", transition
 + "_when_name_is_null", () -> new Transition(null, triggerEvent, initialState, outcomeState));
 assertNull("Transition constructor should fail with null initialState", transition
 + "_when_initialState_is_null", () -> new Transition(name, triggerEvent, null, outcomeState));
 assertNull("Transition constructor should fail with null outcomeState", transition
 + "_when_outcomeState_is_null", () -> new Transition(name, triggerEvent, initialState, null));
 }

 @Test
 public void testAddGuardCondition() {
 // Test adding a guard condition with a valid predicate
 IPredicate predicate = new AlwaysTruePredicate();
 transition.addGuardCondition(predicate);
 assertFalse("Transition should not have empty guard conditions after adding a guard condition",
 transition._getGuardConditions().isEmpty());

 // Test adding a guard condition with a null predicate
 assertNull("Transition should not add a null guard condition", () -> transition
 + "_when_trying_to_add_a_null_guard_condition", () -> transition.addGuardCondition(null));
 }

 @Test
 public void testEquals() {
 // Test equals method with same object
 Transition equalTransition = new Transition("TestTransition", new Event("TestEvent"), new State("TestInitialState"), new State("TestOutcomeState"));
 assertTrue("Two equal Transition objects should be considered equal", transition.equals(equalTransition));

 // Test equals method with different classes
 assertFalse("Two different classes should not be considered equal", transition.equals(new Object()));

 // Test equals method with different Transition objects
 Transition differentTransition = new Transition("DifferentTransition", new Event("DifferentEvent"), new State("DifferentInitialState"), new State("DifferentOutcomeState"));
 assertFalse("Two different Transition objects should not be considered equal", transition.equals(differentTransition));
 }

 @Test
 public void testHashCode() {
 // Test hashCode method with same object
 int hashCode1 = transition.hashCode();
 Transition equalTransition = new Transition("TestTransition", new Event("TestEvent"), new State("TestInitialState"), new State("TestOutcomeState"));
 int hashCode2 = equalTransition.hashCode();
 assertEquals("Two equal Transition objects should have the same hash code", hashCode1, hashCode2);

 // Test hashCode method with different objects
 int hashCode3 = new Transition("DifferentTransition", new Event("DifferentEvent"), new State("DifferentInitialState"), new State("DifferentOutcomeState")).hashCode();
 assertNotEquals("Two different Transition objects should have different hash codes", hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 String expectedToString = "Transition{name='TestTransition', triggerEvent=TestEvent, initialState=TestInitialState, outcomeState=TestOutcomeState}";
 String toString = transition.toString();
 assertEquals("ToString method should return the expected string representation", expectedToString, toString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTriggerWithInvalidState() {
 // Test trigger method with an invalid initial state
 IState invalidInitialState = null;
 assertNull("Trigger method should return null when initial state is invalid", transition
 + "_when_initial_state_is_invalid", () -> transition.trigger(invalidInitialState, triggerEvent));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTriggerWithInvalidEvent() {
 // Test trigger method with an invalid trigger event
 Event invalidEvent = null;
 assertNull("Trigger method should return null when trigger event is invalid", transition
 + "_when_trigger_event_is_invalid", () -> transition.trigger(initialState, invalidEvent));
 }

 @Test
 public void testTriggerWithValidArguments() {
 // Test trigger method with valid arguments
 IState currentState = initialState;
 IState expectedOutcomeState = outcomeState;
 IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
 assertEquals("Transition should result in the expected outcome state when all guards pass", expectedOutcomeState, actualOutcomeState);
 }

 @Test
 public void testWillTriggerWithValidArguments() {
 // Test willTrigger method with valid arguments
 IState currentState = initialState;
 IState expectedWillTrigger = null;
 boolean actualWillTrigger = transition.willTrigger(currentState, triggerEvent);
 if (expectedWillTrigger == null) {
 assertFalse("Transition should not trigger when initial state and trigger event do not match", actualWillTrigger);
 }
 else {
 assertTrue("Transition should trigger when all guards pass and initial state and trigger event match", actualWillTrigger);
 }
 }

 @Test
 public void testWillTriggerWithInvalidArguments() {
 // Test willTrigger method with invalid arguments
 IState invalidState = new State("InvalidState");
 boolean actualWillTrigger = transition.willTrigger(invalidState, triggerEvent);
 assertFalse("Transition should not trigger with an invalid initial state", actualWillTrigger);

 Event invalidEvent = new Event("InvalidEvent");
 actualWillTrigger = transition.willTrigger(initialState, invalidEvent);
 assertFalse("Transition should not trigger with an invalid trigger event", actualWillTrigger);
 }

 @Test
 public void testEvaluateGuardConditions() {
 // Test evaluateGuardConditions method with empty guard conditions
 IState currentState = initialState;
 boolean expectedPassesGuards = true;
 boolean actualPassesGuards = transition.evaluateGuardConditions(currentState, triggerEvent);
 assertEquals("EvaluateGuardConditions method should return true when there are no guard conditions", expectedPassesGuards, actualPassesGuards);

 // Test evaluateGuardConditions method with multiple guard conditions
 List<IGuardCondition> guardConditions = Arrays.asList(new AlwaysFalsePredicate(), new AlwaysTruePredicate());
 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 transition.addAllGuardConditions(guardConditions);
 expectedPassesGuards = false;
 currentState = initialState;
 actualPassesGuards = transition.evaluateGuardConditions(currentState, triggerEvent);
 assertFalse("EvaluateGuardConditions method should return false when at least one guard condition fails", actualPassesGuards);

 IState expectedOutcomeState = new State("ExpectedOutcomeState");
 IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
 assertNotEquals("Transition should not result in the expected outcome state when at least one guard condition fails", expectedOutcomeState, actualOutcomeState);
 }

 private List<IGuardCondition> _getGuardConditions(Transition transition) {
 return transition.guardConditions;
 }
}