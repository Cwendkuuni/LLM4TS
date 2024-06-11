package uk.me.jockmacmad.jstate.state.test;

import static org.junit.Assert.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.Predicate;
import uk.me.jockmacmad.jstate.state.IGuardCondition;
import uk.me.jockmacmad.jstate.state.IState;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransitionTest {

 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private Transition transition;

 @Test
 public void testTransitionInitialization() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 assertNotNull(transition);
 assertEquals("TestTransition", transition.getName());
 assertEquals(initialState, transition.getInitialState());
 assertEquals(outcomeState, transition.getOutcomeState());
 }

 @Test
 public void testTransitionAddGuardConditions() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 Predicate predicate = new Predicate() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("TestEvent");
 }
 };
 IGuardCondition guardCondition = new GuardCondition(predicate);
 transition.addGuardCondition(guardCondition);
 List<IGuardCondition> guardConditions = transition.getGuardConditions();
 assertEquals(1, guardConditions.size());
 }

 @Test
 public void testTransitionAddMultipleGuardConditions() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 Predicate predicate1 = new Predicate() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("TestEvent");
 }
 };
 IGuardCondition guardCondition1 = new GuardCondition(predicate1);
 Predicate predicate2 = new Predicate() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("TestEvent2");
 }
 };
 IGuardCondition guardCondition2 = new GuardCondition(predicate2);
 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);
 List<IGuardCondition> guardConditions = transition.getGuardConditions();
 assertEquals(2, guardConditions.size());
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("TestTransition1", new Event("TestEvent1"), initialState, outcomeState);
 Transition transition2 = new Transition("TestTransition2", new Event("TestEvent2"), initialState, outcomeState);
 Transition transition3 = new Transition("TestTransition1", new Event("TestEvent1"), initialState, outcomeState);
 assertEquals(transition1, transition1);
 assertNotEquals(transition1, transition2);
 assertEquals(transition1, transition3);
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TestTransition1", new Event("TestEvent1"), initialState, outcomeState);
 Transition transition2 = new Transition("TestTransition2", new Event("TestEvent2"), initialState, outcomeState);
 Transition transition3 = new Transition("TestTransition1", new Event("TestEvent1"), initialState, outcomeState);
 assertEquals(transition1.hashCode(), transition1.hashCode());
 assertNotEquals(transition1.hashCode(), transition2.hashCode());
 assertEquals(transition1.hashCode(), transition3.hashCode());
 }

 @Test
 public void testToString() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 String expectedToString = "Transition{name='TestTransition', initialState=" + initialState + ", outcomeState=" + outcomeState + ", triggerEvent=" + triggerEvent + ", guardConditions=[]}";
 String toString = transition.toString();
 assertEquals(expectedToString, toString);
 }

 @Test
 public void testEvaluateGuardConditions() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 Predicate predicate = new Predicate() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("TestEvent");
 }
 };
 IGuardCondition guardCondition = new GuardCondition(predicate);
 transition.addGuardCondition(guardCondition);
 assertTrue(transition.evaluateGuardConditions());
 }

 @Test
 public void testEvaluateGuardConditionsWithFailedGuard() {
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 Predicate predicate = new Predicate() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("TestEvent2");
 }
 };
 IGuardCondition guardCondition = new GuardCondition(predicate);
 transition.addGuardCondition(guardCondition);
 assertFalse(transition.evaluateGuardConditions());
 }

 @Test
 public void testTrigger() {
 IState currentState = new DummyState();
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 assertEquals(initialState, transition.trigger(currentState, triggerEvent));
 assertFalse(outcomeState.equals(currentState));
 transition.addGuardCondition(new GuardCondition(() -> true));
 assertEquals(outcomeState, transition.trigger(currentState, triggerEvent));
 assertNotSame(currentState, currentState);
 }

 @Test
 public void testWillTriggerWithEmptyGuardConditions() {
 IState currentState = new DummyState();
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 assertFalse(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testWillTriggerWithFailedGuards() {
 IState currentState = new DummyState();
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 Predicate predicate = new Predicate() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("TestEvent2");
 }
 };
 IGuardCondition guardCondition = new GuardCondition(predicate);
 transition.addGuardCondition(guardCondition);
 assertFalse(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testWillTriggerWithPassedGuards() {
 IState currentState = new DummyState();
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 Predicate predicate = new Predicate() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("TestEvent");
 }
 };
 IGuardCondition guardCondition = new GuardCondition(predicate);
 transition.addGuardCondition(guardCondition);
 assertTrue(transition.willTrigger(currentState, new Event("TestEvent")));
 }

 @Test
 public void testWillTriggerWithMultiplePassedGuards() {
 IState currentState = new DummyState();
 transition = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 Predicate predicate1 = new Predicate() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("TestEvent");
 }
 };
 Predicate predicate2 = new Predicate() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("TestEvent2");
 }
 };
 IGuardCondition guardCondition1 = new GuardCondition(predicate1);
 IGuardCondition guardCondition2 = new GuardCondition(predicate2);
 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);
 assertTrue(transition.willTrigger(currentState, new Event("TestEvent")));
 }

 private static class DummyState implements IState {
 @Override
 public String toString() {
 return "DummyState";
 }
 }

}