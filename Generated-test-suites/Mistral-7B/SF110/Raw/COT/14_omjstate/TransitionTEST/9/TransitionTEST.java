package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import uk.me.jockmacmad.jstate.event.*;
import uk.me.jockmacmad.jstate.guard.condition.*;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private IState currentState;

 @Before
 public void setup() {
 initialState = new State("InitialState");
 outcomeState = new State("OutcomeState");
 triggerEvent = new Event("TestEvent");
 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 assertTrue(transition.equals(transition1));
 assertTrue(transition1.equals(transition));

 Transition transition3 = new Transition("TestTransition2", triggerEvent, initialState, outcomeState);
 assertFalse(transition.equals(transition3));
 assertFalse(transition3.equals(transition));
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 assertEquals(transition.hashCode(), transition1.hashCode());

 Transition transition3 = new Transition("TestTransition2", triggerEvent, initialState, outcomeState);
 assertNotEquals(transition.hashCode(), transition3.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("Transition{name='TestTransition', initialState=InitialState, outcomeState=OutcomeState, triggerEvent=TestEvent}", transition.toString());
 }

 @Test
 public void testGetInitialState() {
 assertEquals(initialState, transition.getInitialState());
 }

 @Test
 public void testGetOutcomeState() {
 assertEquals(outcomeState, transition.getOutcomeState());
 }

 @Test
 public void testGetTriggerEvent() {
 assertEquals(triggerEvent, transition.getTriggerEvent());
 }

 @Test
 public void testEvaluateGuardConditionsEmpty() {
 transition.evaluateGuardConditions();
 // No assertions here as this method does not throw any exceptions or return a value
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEvaluateGuardConditionsWithNullGuardConditionsList() {
 List<IGuardCondition> nullList = null;
 transition.setGuardConditions(nullList);
 try {
 transition.evaluateGuardConditions();
 } catch (IllegalArgumentException e) {
 fail("Expected UnsupportedOperationException but got IllegalArgumentException");
 }
 transition.evaluateGuardConditions();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEvaluateGuardConditionsWithEmptyGuardConditionsList() {
 transition.evaluateGuardConditions();
 // No assertions here as this method does not throw any exceptions or return a value
 }

 @Test
 public void testEvaluateGuardConditionsWithSinglePassingGuard() {
 AndGuardCondition passingGuard = new AndGuardCondition(new EqualsPredicate("TestAttribute", "TestValue"));
 transition.addGuardCondition(passingGuard);
 assertTrue(transition.evaluateGuardConditions());
 }

 @Test
 public void testEvaluateGuardConditionsWithSingleFailingGuard() {
 AndGuardCondition failingGuard = new AndGuardCondition(new EqualsPredicate("TestAttribute", "DifferentValue"));
 transition.addGuardCondition(failingGuard);
 assertFalse(transition.evaluateGuardConditions());
 }

 @Test
 public void testEvaluateGuardConditionsWithMultipleGuards() {
 AndGuardCondition passingGuard1 = new AndGuardCondition(new EqualsPredicate("TestAttribute1", "TestValue1"));
 AndGuardCondition passingGuard2 = new AndGuardCondition(new EqualsPredicate("TestAttribute2", "TestValue2"));
 transition.addGuardCondition(passingGuard1);
 transition.addGuardCondition(passingGuard2);
 assertTrue(transition.evaluateGuardConditions());
 }

 @Test
 public void testTrigerNullCurrentState() {
 assertNull(transition.trigger(null, triggerEvent));
 }

 @Test
 public void testTrigerNullTriggerEvent() {
 assertSame(currentState, transition.trigger(initialState, null));
 }

 @Test
 public void testTrigerSameCurrentStateAndEvent() {
 currentState = initialState;
 assertEquals(outcomeState, transition.trigger(currentState, triggerEvent));
 }

 @Test
 public void testTrigerDifferentCurrentState() {
 currentState = new State("DifferentState");
 assertSame(currentState, transition.trigger(currentState, triggerEvent));
 }

 @Test
 public void testWillTriggerNullCurrentState() {
 assertFalse(transition.willTrigger(null, triggerEvent));
 }

 @Test
 public void testWillTriggerNullTriggerEvent() {
 assertFalse(transition.willTrigger(initialState, null));
 }

 @Test
 public void testWillTriggerSameCurrentStateAndEvent() {
 currentState = initialState;
 assertTrue(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testWillTriggerDifferentCurrentState() {
 currentState = new State("DifferentState");
 assertFalse(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testWillTriggerWithGuards() {
 AndGuardCondition passingGuard = new AndGuardCondition(new EqualsPredicate("TestAttribute", "TestValue"));
 transition.addGuardCondition(passingGuard);
 currentState = new State("DifferentState");
 assertTrue(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testWillTriggerWithFailingGuards() {
 AndGuardCondition failingGuard = new AndGuardCondition(new EqualsPredicate("TestAttribute", "DifferentValue"));
 transition.addGuardCondition(failingGuard);
 currentState = new State("DifferentState");
 assertFalse(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testAddGuardConditionNullGuard() {
 try {
 transition.addGuardCondition(null);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException but got none");
 }
 }

 @Test
 public void testAddGuardConditionNullGuardList() {
 IGuardCondition[] nullGuards = new IGuardCondition[0];
 try {
 transition.setGuardConditions(nullGuards);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException but got none");
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGuardConditionNullGuardListWithNewMethod() {
 try {
 transition.setGuardConditions(null);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAddGuardConditionEmptyGuardList() {
 IGuardCondition[] emptyGuards = ArrayUtils.EMPTY_ARRAY;
 transition.setGuardConditions(emptyGuards);
 assertEquals(0, transition.getGuardConditions().size());
 }

 @Test
 public void testAddGuardConditionSingleGuard() {
 OrGuardCondition singleGuard = new OrGuardCondition(new EqualsPredicate("TestAttribute", "TestValue"));
 transition.addGuardCondition(singleGuard);
 assertEquals(1, transition.getGuardConditions().size());
 }

 @Test
 public void testAddGuardConditionMultipleGuards() {
 AndGuardCondition firstGuard = new AndGuardCondition(new EqualsPredicate("TestAttribute1", "TestValue1"));
 AndGuardCondition secondGuard = new AndGuardCondition(new EqualsPredicate("TestAttribute2", "TestValue2"));
 transition.addGuardCondition(firstGuard);
 transition.addGuardCondition(secondGuard);
 assertEquals(2, transition.getGuardConditions().size());
 }

}