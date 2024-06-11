package uk.me.jockmacmad.jstate.state.test;

import org.junit.Before;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.*;
import static org.junit.Assert.*;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;

 @Before
 public void setUp() {
 initialState = new FooState();
 outcomeState = new BarState();
 triggerEvent = new SomeEvent();
 transition = new Transition("testTransition", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testEquals() {
 Transition transition2 = new Transition("testTransition", triggerEvent, initialState, outcomeState);
 assertTrue(transition.equals(transition2));
 }

 @Test
 public void testHashCode() {
 Transition transition2 = new Transition("testTransition", triggerEvent, initialState, outcomeState);
 assertEquals(transition.hashCode(), transition2.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("Transition{name='testTransition', triggerEvent=SomeEvent, initialState=FooState, outcomeState=BarState}", transition.toString());
 }

 @Test
 public void testAddGuardCondition() {
 IPredicate predicate = new SomePredicate();
 transition.addGuardCondition(predicate);
 assertTrue(transition.guardConditions.contains(predicate));
 }

 @Test
 public void testAddGuardConditionType() {
 IGuardCondition guardCondition = new SomeGuardCondition();
 transition.addGuardCondition(guardCondition);
 assertTrue(transition.guardConditions.contains(guardCondition));
 }

 @Test
 public void testEvaluateGuardConditions() {
 // Adding a guard condition that always evaluates to true
 IGuardCondition alwaysTrue = new AlwaysTrueGuardCondition();
 transition.addGuardCondition(alwaysTrue);
 // Assuming the trigger event has a getter method named "getData"
 Object data = new Object();
 triggerEvent.setData(data);
 // Testing the transition with a state that satisfies all guards
 IState stateThatSatisfiesGuards = new StateThatSatisfiesGuards();
 IState expectedOutcomeState = outcomeState;
 IState actualOutcomeState = transition.trigger(stateThatSatisfiesGuards, triggerEvent);
 assertEquals(expectedOutcomeState, actualOutcomeState);
 }

 @Test
 public void testWillTrigger() {
 // Adding a guard condition that always evaluates to false
 IGuardCondition alwaysFalse = new AlwaysFalseGuardCondition();
 transition.addGuardCondition(alwaysFalse);
 // Testing the transition with a state that doesn't satisfy any guards
 IState stateThatDoesntSatisfyGuards = new StateThatDoesntSatisfyGuards();
 assertFalse(transition.willTrigger(stateThatDoesntSatisfyGuards, triggerEvent));
 }

 @Test
 public void testWillTriggerWithStateThatSatisfiesOneGuard() {
 // Assuming the trigger event has a getter method named "getData"
 Object data = new Object();
 triggerEvent.setData(data);
 // Testing the transition with a state that satisfies one guard
 IState stateThatSatisfiesOneGuard = new StateThatSatisfiesOneGuard();
 IPredicate guardThatIsSatisfied = new GuardThatIsSatisfied();
 transition.addGuardCondition(guardThatIsSatisfied);
 assertTrue(transition.willTrigger(stateThatSatisfiesOneGuard, triggerEvent));
 }

 @Test
 public void testWillTriggerWithStateThatSatisfiesMultipleGuards() {
 // Assuming the trigger event has a getter method named "getData"
 Object data = new Object();
 triggerEvent.setData(data);
 // Testing the transition with a state that satisfies multiple guards
 IState stateThatSatisfiesMultipleGuards = new StateThatSatisfiesMultipleGuards();
 IGuardCondition guard1 = new Guard1();
 IGuardCondition guard2 = new Guard2();
 transition.addGuardCondition(guard1);
 transition.addGuardCondition(guard2);
 assertTrue(transition.willTrigger(stateThatSatisfiesMultipleGuards, triggerEvent));
 }

 @Test
 public void testWillTriggerWithStateThatDoesntSatisfyAnyGuard() {
 // Assuming the trigger event has a getter method named "getData"
 Object data = new Object();
 triggerEvent.setData(data);
 // Testing the transition with a state that doesn't satisfy any guards
 IState stateThatDoesntSatisfyAnyGuard = new StateThatDoesntSatisfyAnyGuard();
 assertFalse(transition.willTrigger(stateThatDoesntSatisfyAnyGuard, triggerEvent));
 }
}