package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Before;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.*;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private IState pCurrentState;

 @Before
 public void setUp() {
 initialState = new State("InitialState");
 outcomeState = new State("OutcomeState");
 transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 triggerEvent = new Event("TriggerEvent");
 pCurrentState = initialState;
 }

 @Test
 public void testConstructor() {
 Transition transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 assertEquals("TransitionName", transition.getName());
 assertEquals(initialState, transition.getInitialState());
 assertEquals(outcomeState, transition.getOutcomeState());
 assertEquals(triggerEvent, transition.getTriggerEvent());
 }

 @Test
 public void testAddGuardCondition() {
 IPredicate predicate = new AlwaysTruePredicate();
 transition.addGuardCondition(predicate);
 assertEquals(1, transition.getGuardConditions().size());
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("TransitionName1", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 assertFalse(EqualsBuilder.reflectionEquals(transition1, transition2));
 Transition transition3 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 assertTrue(EqualsBuilder.reflectionEquals(transition2, transition3));
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TransitionName1", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 assertNotEquals(HashCodeBuilder.reflectionHashCode(transition1), HashCodeBuilder.reflectionHashCode(transition2));
 Transition transition3 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 assertEquals(HashCodeBuilder.reflectionHashCode(transition2), HashCodeBuilder.reflectionHashCode(transition3));
 }

 @Test
 public void testToString() {
 assertEquals("TransitionName{initialState=InitialState, outcomeState=OutcomeState, triggerEvent=TriggerEvent, guardConditions=[]}", ToStringBuilder.reflectionToString(transition));
 }

 @Test
 public void testTrigger_EmptyGuardConditions() {
 IState retVal = transition.trigger(pCurrentState, triggerEvent);
 assertEquals(outcomeState, retVal);
 }

 @Test
 public void testTrigger_GuardsPass() {
 IPredicate predicate = new AlwaysTruePredicate();
 transition.addGuardCondition(predicate);
 IState retVal = transition.trigger(pCurrentState, triggerEvent);
 assertEquals(outcomeState, retVal);
 }

 @Test
 public void testTrigger_GuardsFail() {
 IPredicate predicate = new AlwaysFalsePredicate();
 transition.addGuardCondition(predicate);
 IState retVal = transition.trigger(pCurrentState, triggerEvent);
 assertEquals(pCurrentState, retVal);
 }

 @Test
 public void testWillTrigger_EmptyGuardConditions() {
 boolean retVal = transition.willTrigger(pCurrentState, triggerEvent);
 assertTrue(retVal);
 }

 @Test
 public void testWillTrigger_GuardsPass() {
 IPredicate predicate = new AlwaysTruePredicate();
 transition.addGuardCondition(predicate);
 boolean retVal = transition.willTrigger(pCurrentState, triggerEvent);
 assertTrue(retVal);
 }

 @Test
 public void testWillTrigger_GuardsFail() {
 IPredicate predicate = new AlwaysFalsePredicate();
 transition.addGuardCondition(predicate);
 boolean retVal = transition.willTrigger(pCurrentState, triggerEvent);
 assertFalse(retVal);
 }
}