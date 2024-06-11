package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.DummyPredicate;
import uk.me.jockmacmad.jstate.predicate.DummyGuardCondition;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.List;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;

 @Before
 public void setUp() {
 initialState = new State("InitialState");
 outcomeState = new State("OutcomeState");
 triggerEvent = new Event("Event");
 transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 assertEquals(transition, transition1);

 Transition transition2 = new Transition("DifferentTransitionName", triggerEvent, initialState, outcomeState);
 assertNotEquals(transition, transition2);

 Transition transition3 = new Transition("TransitionName", triggerEvent, initialState, new State("OutcomeState2"));
 assertNotEquals(transition, transition3);
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 assertEquals(transition.hashCode(), transition1.hashCode());

 Transition transition3 = new Transition("DifferentTransitionName", triggerEvent, initialState, outcomeState);
 assertNotEquals(transition.hashCode(), transition3.hashCode());

 Transition transition4 = new Transition("TransitionName", triggerEvent, initialState, new State("OutcomeState2"));
 assertNotEquals(transition.hashCode(), transition4.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("TransitionName{initialState=InitialState, outcomeState=OutcomeState, triggerEvent=Event}", transition.toString());
 }

 @Test
 public void testTriggers() {
 IState currentState = initialState;
 assertTrue(transition.willTrigger(currentState, triggerEvent));
 currentState = outcomeState;
 assertFalse(transition.willTrigger(currentState, triggerEvent));

 currentState = initialState;
 transition.trigger(currentState, triggerEvent);
 assertEquals(outcomeState, currentState);
 }

 @Test
 public void testGuardConditions() {
 // Test no guard conditions
 transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 assertTrue(transition.willTrigger(initialState, triggerEvent));
 transition.trigger(initialState, triggerEvent);
 assertEquals(outcomeState, transition.getOutcomeState());

 // Test with dummy guard conditions
 List<IGuardCondition> guardConditions = new ArrayList<>();
 guardConditions.add(new DummyGuardCondition());
 transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 transition.addGuardCondition(guardConditions.get(0));
 assertTrue(transition.willTrigger(initialState, triggerEvent));
 transition.trigger(initialState, triggerEvent);
 assertEquals(outcomeState, transition.getOutcomeState());

 // Test with multiple dummy guard conditions
 guardConditions.add(new DummyGuardCondition());
 transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 transition.addAll(guardConditions);
 assertTrue(transition.willTrigger(initialState, triggerEvent));
 transition.trigger(initialState, triggerEvent);
 assertEquals(outcomeState, transition.getOutcomeState());
}

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullGuardCondition() {
 // Test adding a null guard condition
 transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 try {
 transition.addGuardCondition(null);
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
}
}