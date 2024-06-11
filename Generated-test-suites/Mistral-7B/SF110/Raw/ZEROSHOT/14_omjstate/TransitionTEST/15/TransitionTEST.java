package uk.me.jockmacmad.jstate.state.test;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.DummyPredicate;
import uk.me.jockmacmad.jstate.predicate.DummyGuardCondition;
import uk.me.jockmacmad.jstate.state.IState;
import uk.me.jockmacmad.jstate.state.Transition;
import uk.me.jockmacmad.jstate.event.DummyEvent;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;

 @Before
 public void setUp() {
 initialState = new DummyState("Initial State");
 outcomeState = new DummyState("Outcome State");
 triggerEvent = new DummyEvent("Trigger Event");
 transition = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testTransitionName() {
 assertEquals("Transition Name", transition.getName());
 }

 @Test
 public void testInitialState() {
 assertEquals(initialState, transition.getInitialState());
 }

 @Test
 public void testOutcomeState() {
 assertEquals(outcomeState, transition.getOutcomeState());
 }

 @Test
 public void testTriggerEvent() {
 assertEquals(triggerEvent, transition.getTriggerEvent());
 }

 @Test
 public void testAddGuardCondition() {
 DummyGuardCondition guard = new DummyGuardCondition();
 transition.addGuardCondition(guard);
 assertEquals(1, transition.getGuardConditions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullGuardCondition() {
 transition.addGuardCondition(null);
 }

 @Test
 public void testAddPredicateGuardCondition() {
 DummyPredicate predicate = new DummyPredicate();
 IGuardCondition guard = new DummyGuardCondition(predicate);
 transition.addGuardCondition(guard);
 assertEquals(1, transition.getGuardConditions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullPredicateGuardCondition() {
 DummyGuardCondition guard = new DummyGuardCondition(null);
 transition.addGuardCondition(guard);
 }

 @Test
 public void testEvaluateGuardConditions() {
 List<IGuardCondition> guardConditions = new ArrayList<IGuardCondition>();
 guardConditions.add(new DummyGuardCondition());
 transition.addGuardConditions(guardConditions);
 assertFalse(transition.evaluateGuardConditions());
 guardConditions = new ArrayList<IGuardCondition>();
 guardConditions.add(new DummyGuardCondition(new DummyPredicate()));
 transition.addGuardConditions(guardConditions);
 assertTrue(transition.evaluateGuardConditions());
 }

 @Test
 public void testTrigger() {
 IState result = transition.trigger(initialState, triggerEvent);
 assertNull(result);
 IState currentState = new DummyState("Current State");
 result = transition.trigger(currentState, triggerEvent);
 assertEquals(outcomeState, result);
 }

 @Test
 public void testWillTrigger() {
 assertFalse(transition.willTrigger(initialState, triggerEvent));
 assertTrue(transition.willTrigger(outcomeState, triggerEvent));
 }

 @Test
 public void testEquals() {
 Transition other = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
 assertEquals(transition, other);
 other = new Transition("Different Name", triggerEvent, initialState, outcomeState);
 assertNotEquals(transition, other);
 other = new Transition("Transition Name", triggerEvent, initialState, new DummyState("Different Outcome State"));
 assertNotEquals(transition, other);
 other = new Transition("Transition Name", new DummyEvent("Different Trigger Event"), initialState, outcomeState);
 assertNotEquals(transition, other);
 }

 @Test
 public void testHashCode() {
 int hashCode1 = transition.hashCode();
 Transition other = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
 int hashCode2 = other.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 String toString = ReflectionToStringBuilder.toString(transition);
 assertTrue(toString.contains("Transition Name"));
 assertTrue(toString.contains("Trigger Event"));
 assertTrue(toString.contains("Initial State"));
 assertTrue(toString.contains("Outcome State"));
 }

 @Test
 public void testAddMultipleGuardConditions() {
 List<IGuardCondition> guardConditions = new ArrayList<IGuardCondition>();
 guardConditions.add(new DummyGuardCondition());
 guardConditions.add(new DummyGuardCondition());
 transition.addGuardConditions(guardConditions);
 assertEquals(2, transition.getGuardConditions().size());
 }

 @Test
 public void testTriggerWithMultipleGuards() {
 List<IGuardCondition> guardConditions = new ArrayList<IGuardCondition>();
 guardConditions.add(new DummyGuardCondition(new DummyPredicate()));
 guardConditions.add(new DummyGuardCondition(new DummyPredicate()));
 transition.addGuardConditions(guardConditions);
 IState currentState = new DummyState("Current State");
 Event event = new DummyEvent("Trigger Event");
 IState result = transition.trigger(currentState, event);
 assertNull(result);
 IState initialState = new DummyState("Initial State");
 result = transition.trigger(initialState, event);
 assertEquals(outcomeState, result);
 }
}