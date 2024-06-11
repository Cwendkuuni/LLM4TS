package uk.me.jockmacmad.jstate.state.test;

import static org.junit.Assert.*;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
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
 private IGuardCondition guardCondition;

 @Before
 public void setup() {
 initialState = new InitialState();
 outcomeState = new OutcomeState();
 triggerEvent = new TriggerEvent();
 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 currentState = new CurrentState();
 guardCondition = new GuardCondition();
 }

 @Test
 public void testTransitionCreation() {
 Transition transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 assertEquals("TestTransition", transition.getName());
 assertEquals(initialState, transition.getInitialState());
 assertEquals(outcomeState, transition.getOutcomeState());
 assertEquals(triggerEvent, transition.getTriggerEvent());
 }

 @Test
 public void testAddGuardCondition() {
 transition.addGuardCondition(guardCondition);
 assertEquals(1, transition.getGuardConditions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullGuardCondition() {
 transition.addGuardCondition(null);
 fail("Should not allow null guard condition");
 }

 @Test
 public void testAddMultipleGuardConditions() {
 final IGuardCondition guardCondition2 = new GuardCondition2();
 transition.addGuardCondition(guardCondition);
 transition.addGuardCondition(guardCondition2);
 assertEquals(2, transition.getGuardConditions().size());
 }

 @Test(expected = ClassCastException.class)
 public void testAddNonIGuardCondition() {
 transition.addGuardCondition(new Object());
 fail("Should not allow non-IGuardCondition object to be added as a guard condition");
 }

 @Test
 public void testEquals() {
 Transition transition2 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 assertEquals(transition, transition);
 assertEquals(transition, transition2);
 Transition transition3 = new Transition("TestTransition2", triggerEvent, initialState, outcomeState);
 assertNotEquals(transition, transition3);
 }

 @Test
 public void testHashCode() {
 Transition transition2 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 assertEquals(hashCode(), transition2.hashCode());
 Transition transition3 = new Transition("TestTransition2", triggerEvent, initialState, outcomeState);
 assertNotEquals(hashCode(), transition3.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("Transition[name=TestTransition, initialState=InitialState{}, outcomeState=OutcomeState{}, triggerEvent=TriggerEvent{}, guardConditions=[], hash=2225559]",
 ToStringBuilder.reflectionToString(transition));
 }

 @Test
 public void testTriggerNullState() {
 assertFalse(transition.trigger(null, triggerEvent) instanceof OutcomeState);
 }

 @Test
 public void testTriggerInitialState() {
 assertTrue(transition.trigger(initialState, triggerEvent) instanceof OutcomeState);
 }

 @Test
 public void testTriggerCurrentState() {
 // Add a guard that should pass for current state
 transition.addGuardCondition(new GuardCondition3(currentState));
 // Trigger the transition with the current state
 assertTrue(transition.trigger(currentState, triggerEvent) instanceof OutcomeState);
 }

 @Test
 public void testTriggerDifferentState() {
 assertFalse(transition.trigger(new DifferentState(), triggerEvent) instanceof OutcomeState);
 }

 @Test
 public void testWillTriggerNullState() {
 assertFalse(transition.willTrigger(null, triggerEvent));
 }

 @Test
 public void testWillTriggerInitialState() {
 assertTrue(transition.willTrigger(initialState, triggerEvent));
 }

 @Test
 public void testWillTriggerDifferentState() {
 assertFalse(transition.willTrigger(new DifferentState(), triggerEvent));
 }

 @Test
 public void testWillTriggerCurrentState() {
 // Add a guard that should pass for current state
 transition.addGuardCondition(new GuardCondition3(currentState));
 // Will the transition be triggered with current state and trigger event?
 assertTrue(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testEvaluateGuardsNullEvent() {
 assertFalse(transition.evaluateGuardConditions());
 }

 @Test
 public void testEvaluateGuardsEmptyGuards() {
 // There are no guards, so evaluation should pass
 assertTrue(transition.evaluateGuardConditions());
 }

 @Test
 public void testEvaluateGuardsAllGuardsPass() {
 // Add a guard that should pass for current state and event
 transition.addGuardCondition(new GuardCondition3(currentState));
 // Evaluate guards
 assertTrue(transition.evaluateGuardConditions());
 }

 @Test
 public void testEvaluateGuardsAnyGuardFails() {
 // Add a guard that should fail for current state and event
 transition.addGuardCondition(new FailingGuardCondition());
 // Evaluate guards
 assertFalse(transition.evaluateGuardConditions());
 }

 @Test
 public void testEvaluateGuardsMultipleGuards() {
 // Add multiple guards, some that pass and some that fail
 transition.addGuardCondition(new GuardCondition3(currentState));
 transition.addGuardCondition(new FailingGuardCondition());
 // Evaluate guards
 assertFalse(transition.evaluateGuardConditions());
 }

 private int hashCode() {
 return HashCodeBuilder.reflectionHashCode(this);
 }
}