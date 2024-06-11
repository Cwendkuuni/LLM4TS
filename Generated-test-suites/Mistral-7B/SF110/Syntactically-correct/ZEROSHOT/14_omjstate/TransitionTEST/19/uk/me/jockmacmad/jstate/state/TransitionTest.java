package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.List;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private IState currentState;
 private List<IPredicate> guardConditions;
 private IPredicate guardCondition;

 @Test
 public void testTransitionInstance() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 assertNotNull(transition);
 }

 @Test
 public void testEquals() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 Transition transitionCopy = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 assertTrue(EqualsBuilder.reflectionEquals(transition, transitionCopy));
 }

 @Test
 public void testHashCode() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 int firstHashCode = HashCodeBuilder.reflectionHashCode(transition);
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 int secondHashCode = HashCodeBuilder.reflectionHashCode(transition);
 assertEquals(firstHashCode, secondHashCode);
 }

 @Test
 public void testToString() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 String expectedToString = ToStringBuilder.reflectionToString(transition);
 String actualToString = transition.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testTriggerNullTransition() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 currentState = initialState;
 assertNull(transition.trigger(null, triggerEvent));
 }

 @Test
 public void testTriggerNullCurrentState() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 assertNull(transition.trigger(null, triggerEvent));
 }

 @Test
 public void testTriggerNullTriggerEvent() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 currentState = initialState;
 assertNull(transition.trigger(currentState, null));
 }

 @Test
 public void testTriggerInitialStateAndTriggerEvent() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 currentState = initialState;
 IState expectedOutcomeState = outcomeState;
 IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
 assertEquals(expectedOutcomeState, actualOutcomeState);
 }

 @Test
 public void testTriggerDifferentInitialState() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 currentState = new State("DifferentState");
 assertNotEquals(outcomeState, transition.trigger(currentState, triggerEvent));
 }

 @Test
 public void testTriggerDifferentTriggerEvent() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 currentState = initialState;
 Event differentEvent = new Event("DifferentEvent");
 assertNotEquals(outcomeState, transition.trigger(currentState, differentEvent));
 }

 @Test
 public void testWillTriggerNullTransition() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 currentState = initialState;
 assertFalse(transition.willTrigger(null, triggerEvent));
 }

 @Test
 public void testWillTriggerNullCurrentState() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 assertFalse(transition.willTrigger(null, triggerEvent));
 }

 @Test
 public void testWillTriggerNullTriggerEvent() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 currentState = initialState;
 assertFalse(transition.willTrigger(currentState, null));
 }

 @Test
 public void testWillTriggerInitialStateAndTriggerEvent() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 currentState = initialState;
 assertTrue(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testWillTriggerDifferentInitialState() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 currentState = new State("DifferentState");
 assertFalse(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testWillTriggerDifferentTriggerEvent() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 currentState = initialState;
 Event differentEvent = new Event("DifferentEvent");
 assertFalse(transition.willTrigger(currentState, differentEvent));
 }

 @Test
 public void testAddGuardConditionNullPredicate() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 try {
 transition.addGuardCondition(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testAddGuardConditionNullTransition() {
 guardCondition = new AndPredicate(new IsEqualPredicate("TestKey", "TestValue"), new IsEmptyPredicate("TestList"));
 try {
 guardConditions = new ArrayList<>();
 guardConditions.add(guardCondition);
 transition.addGuardCondition(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // pass
 }
 }

 @Test
 public void testAddGuardConditionToEmptyList() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 guardCondition = new AndPredicate(new IsEqualPredicate("TestKey", "TestValue"), new IsEmptyPredicate("TestList"));
 transition.addGuardCondition(guardCondition);
 List<IPredicate> expectedGuardConditions = new ArrayList<>();
 expectedGuardConditions.add(guardCondition);
 List<IPredicate> actualGuardConditions = transition.getGuardConditions();
 assertEquals(expectedGuardConditions, actualGuardConditions);
 }

 @Test
 public void testAddGuardConditionToList() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 guardCondition1 = new IsEqualPredicate("TestKey1", "TestValue1");
 guardCondition2 = new IsEmptyPredicate("TestList");
 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);
 List<IPredicate> expectedGuardConditions = new ArrayList<>();
 expectedGuardConditions.add(guardCondition1);
 expectedGuardConditions.add(guardCondition2);
 List<IPredicate> actualGuardConditions = transition.getGuardConditions();
 assertEquals(expectedGuardConditions, actualGuardConditions);
 }

 @Test
 public void testTriggerWithGuards() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 currentState = initialState;
 IPredicate guardCondition1 = new IsEqualPredicate("TestKey1", "TestValue1");
 IPredicate guardCondition2 = new IsEqualPredicate("TestKey2", "TestValue2");
 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);
 guardCondition1.setExpectedValue(new Object() {});
 guardCondition2.setExpectedValue(new Object() {});
 IState expectedOutcomeState = outcomeState;
 IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
 assertEquals(expectedOutcomeState, actualOutcomeState);
 }

 @Test
 public void testTriggerWithGuardsFalse() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 currentState = initialState;
 IPredicate guardCondition1 = new IsEqualPredicate("TestKey1", "TestValue1");
 IPredicate guardCondition2 = new IsEmptyPredicate("TestList");
 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);
 IPredicate guardCondition3 = new IsEqualPredicate("TestKey3", "TestValue3");
 guardCondition1.setExpectedValue(new Object() {});
 guardCondition2.setExpectedValue(new Object[] { new Object() {}});
 guardCondition3.setExpectedValue(new Object() {});
 assertFalse(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testTriggerWithGuardsEmptyList() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 currentState = initialState;
 List<IPredicate> emptyGuardConditions = new ArrayList<>();
 transition.addGuardCondition(emptyGuardConditions.toArray(new IPredicate[0]));
 IState expectedOutcomeState = outcomeState;
 IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
 assertEquals(expectedOutcomeState, actualOutcomeState);
 }
}