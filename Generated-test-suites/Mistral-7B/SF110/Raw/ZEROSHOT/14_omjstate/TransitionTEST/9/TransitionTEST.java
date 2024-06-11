package uk.me.jockmacmad.jstate.state;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import static org.junit.Assert.*;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private IState currentState;

 @Before
 public void setUp() {
 initialState = new StateA();
 outcomeState = new StateB();
 triggerEvent = new EventA();
 transition = new Transition("TransitionA", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testInitialState() {
 IState actual = transition.getInitialState();
 assertEquals("Initial state is set incorrectly", initialState, actual);
 }

 @Test
 public void testOutcomeState() {
 IState actual = transition.getOutcomeState();
 assertEquals("Outcome state is set incorrectly", outcomeState, actual);
 }

 @Test
 public void testTriggerEvent() {
 Event actual = transition.getTriggerEvent();
 assertEquals("Trigger event is set incorrectly", triggerEvent, actual);
 }

 @Test
 public void testAddGuardConditionIPredicate() {
 IPredicate predicate = new AlwaysTruePredicate();
 transition.addGuardCondition(predicate);
 int sizeBefore = transition.getGuardConditions().size();
 transition.addGuardCondition(predicate);
 int sizeAfter = transition.getGuardConditions().size();
 assertEquals("Number of guard conditions should be the same after adding the same guard condition twice", sizeBefore, sizeAfter);
 }

 @Test
 public void testAddGuardConditionIGuardCondition() {
 IGuardCondition guardCondition = new GuardConditionA();
 transition.addGuardCondition(guardCondition);
 int sizeBefore = transition.getGuardConditions().size();
 transition.addGuardCondition(guardCondition);
 int sizeAfter = transition.getGuardConditions().size();
 assertEquals("Number of guard conditions should be the same after adding the same guard condition twice", sizeBefore, sizeAfter);
 }

 @Test
 public void testEqualsObject() {
 Transition transition1 = new Transition("TransitionA", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TransitionA", triggerEvent, initialState, outcomeState);
 assertTrue("Transitions should be equal", transition.equals(transition1));
 Transition transition3 = new Transition("TransitionB", triggerEvent, initialState, outcomeState);
 assertFalse("Transitions should not be equal", transition.equals(transition3));
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TransitionA", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TransitionA", triggerEvent, initialState, outcomeState);
 int hashCode1 = transition.hashCode();
 int hashCode2 = transition1.hashCode();
 assertEquals("Hashes should be the same for equal objects", hashCode1, hashCode2);
 Transition transition3 = new Transition("TransitionB", triggerEvent, initialState, outcomeState);
 assertNotSame("Hashes should not be the same for different objects", hashCode1, transition3.hashCode());
 }

 @Test
 public void testToString() {
 String actual = transition.toString();
 String expected = "TransitionA{initialState=StateA, outcomeState=StateB, triggerEvent=EventA, guardConditions=[], name='TransitionA'}";
 assertEquals("ToString method should return the expected string", expected, actual);
 }

 @Test
 public void testEvaluateGuardConditions() {
 // This method is not implemented and cannot be tested directly
 // However, it can be tested indirectly by testing the behavior of the trigge() and willTrigger() methods
 }

 @Test
 public void testTrigger() {
 currentState = initialState;
 IState actual = transition.trigger(currentState, triggerEvent);
 assertNotSame("Transition should not return the current state", currentState, actual);
 assertEquals("Transition should return the outcome state", outcomeState, actual);
 }

 @Test
 public void testWillTrigger() {
 currentState = initialState;
 boolean actual = transition.willTrigger(currentState, triggerEvent);
 assertFalse("Transition should not willTrigger when the current state is not the initial state", actual);
 currentState = initialState;
 transition.addGuardCondition(new AlwaysTruePredicate());
 actual = transition.willTrigger(currentState, triggerEvent);
 assertTrue("Transition should willTrigger when the current state is the initial state and all guard conditions pass", actual);
 }

 @Test
 public void testWillTriggerWithFailedGuardCondition() {
 currentState = initialState;
 IGuardCondition guardCondition = new GuardConditionA();
 transition.addGuardCondition(guardCondition);
 boolean actual = transition.willTrigger(currentState, triggerEvent);
 assertFalse("Transition should not willTrigger when a guard condition fails", actual);
 }

 @Test
 public void testWillTriggerWithMultipleFailedGuardConditions() {
 currentState = initialState;
 IGuardCondition guardCondition1 = new GuardConditionA();
 IGuardCondition guardCondition2 = new GuardConditionB();
 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);
 boolean actual = transition.willTrigger(currentState, triggerEvent);
 assertFalse("Transition should not willTrigger when multiple guard conditions fail", actual);
 }

 @Test
 public void testWillTriggerWithPassingGuardConditions() {
 currentState = initialState;
 IGuardCondition guardCondition1 = new GuardConditionA();
 IGuardCondition guardCondition2 = new GuardConditionC();
 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);
 boolean actual = transition.willTrigger(currentState, triggerEvent);
 assertTrue("Transition should willTrigger when all guard conditions pass", actual);
 }

 @Test
 public void testWillTriggerWithPassingGuardConditionArray() {
 currentState = initialState;
 IGuardCondition[] guardConditions = new IGuardCondition[]{new GuardConditionA(), new GuardConditionC()};
 transition.setGuardConditions(guardConditions);
 boolean actual = transition.willTrigger(currentState, triggerEvent);
 assertTrue("Transition should willTrigger when all guard conditions pass", actual);
 }

 @Test
 public void testWillTriggerWithPassingGuardConditionArrayWithDifferentOrder() {
 currentState = initialState;
 IGuardCondition[] guardConditions = new IGuardCondition[]{new GuardConditionC(), new GuardConditionA()};
 transition.setGuardConditions(guardConditions);
 boolean actual = transition.willTrigger(currentState, triggerEvent);
 assertTrue("Transition should willTrigger when all guard conditions pass, regardless of order", actual);
 }

 @Test
 public void testWillTriggerWithMixedPassingAndFailingGuardConditions() {
 currentState = initialState;
 IGuardCondition guardCondition1 = new GuardConditionA();
 IGuardCondition guardCondition2 = new GuardConditionB();
 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);
 boolean actual = transition.willTrigger(currentState, triggerEvent);
 assertFalse("Transition should not willTrigger when a guard condition fails", actual);
 currentState = new StateC();
 actual = transition.willTrigger(currentState, triggerEvent);
 assertTrue("Transition should willTrigger when the current state is the initial state and some guard conditions pass", actual);
 }

 @Test
 public void testTriggerWithFailedGuardConditions() {
 currentState = initialState;
 transition.addGuardCondition(new GuardConditionA());
 transition.addGuardCondition(new GuardConditionB());
 assertFalse("Transition should not trigger when guard conditions fail", transition.trigger(currentState, triggerEvent) instanceof OutcomeState);
 }

 @Test
 public void testTriggerWithPassingGuardConditions() {
 currentState = initialState;
 transition.addGuardCondition(new AlwaysTruePredicate());
 transition.addGuardCondition(new GuardConditionC());
 IState actual = transition.trigger(currentState, triggerEvent);
 assertEquals("Transition should return the outcome state when all guard conditions pass", outcomeState, actual);
 }

 @Test
 public void testTriggerWithPassingGuardConditionsAndMultipleInitialStates() {
 initialState = new StateA();
 IState state1 = initialState;
 IState state2 = new StateB();
 transition.addGuardCondition(new AlwaysTruePredicate());
 transition.addGuardCondition(new GuardConditionC());
 IState actual1 = transition.trigger(state1, triggerEvent);
 assertEquals("Transition should return the outcome state when all guard conditions pass and the initial state is StateA", outcomeState, actual1);
 IState actual2 = transition.trigger(state2, triggerEvent);
 assertNotSame("Transition should not return the outcome state when the initial state is not StateA", outcomeState, actual2);
 }

 @Test
 public void testTriggerWithPassingGuardConditionsAndMultipleTriggerEvents() {
 Event eventA = new EventA();
 Event eventB = new EventB();
 transition.addGuardCondition(new AlwaysTruePredicate());
 transition.addGuardCondition(new GuardConditionC());
 IState actual1 = transition.trigger(initialState, eventA);
 assertEquals("Transition should return the outcome state when all guard conditions pass and the trigger event is EventA", outcomeState, actual1);
 IState actual2 = transition.trigger(initialState, eventB);
 assertNotSame("Transition should not return the outcome state when the trigger event is not EventA", outcomeState, actual2);
 }

}