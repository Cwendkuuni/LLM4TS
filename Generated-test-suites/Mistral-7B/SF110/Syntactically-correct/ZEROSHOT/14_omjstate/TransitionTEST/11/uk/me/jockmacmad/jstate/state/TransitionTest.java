package uk.me.jockmacmad.jstate.state;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import uk.me.jockmacmad.jstate.predicate.*;
import java.util.ArrayList;
import java.util.Arrays;
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
 private IState currentState;
 private List<IGuardCondition> guardConditions;

 @Before
 public void setup() {
 initialState = Mockito.mock(IState.class);
 outcomeState = Mockito.mock(IState.class);
 triggerEvent = Mockito.mock(Event.class);
 currentState = Mockito.mock(IState.class);
 guardConditions = new ArrayList<>();

 transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testTransitionName() {
 String transitionName = "TransitionName";
 assertEquals(transitionName, transition.getName());
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
 public void testAddGuardConditionIPredicate() {
 IPredicate predicate = Mockito.mock(IPredicate.class);
 transition.addGuardCondition(predicate);
 assertEquals(1, transition.getGuardConditions().size());
 }

 @Test
 public void testAddGuardConditionIGuardCondition() {
 IGuardCondition guardCondition = Mockito.mock(IGuardCondition.class);
 transition.addGuardCondition(guardCondition);
 assertEquals(1, transition.getGuardConditions().size());
 }

 @Test
 public void testEvaluateGuardsEmpty() {
 Mockito.when(guardConditions.isEmpty()).thenReturn(true);
 Mockito.when(initialState.equals(currentState)).thenReturn(true);
 Mockito.when(triggerEvent.equals(Mockito.any(Event.class))).thenReturn(true);

 boolean result = transition.evaluateGuards(currentState, triggerEvent);
 assertTrue(result);
 }

 @Test
 public void testEvaluateGuardsFailure() {
 IPredicate predicate = Mockito.mock(IPredicate.class);
 guardConditions.add(predicate);
 Mockito.when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(false);
 Mockito.when(initialState.equals(currentState)).thenReturn(true);
 Mockito.when(triggerEvent.equals(Mockito.any(Event.class))).thenReturn(true);

 boolean result = transition.evaluateGuards(currentState, triggerEvent);
 assertFalse(result);
 }

 @Test
 public void testEvaluateGuardsSuccess() {
 IGuardCondition guardCondition = Mockito.mock(IGuardCondition.class);
 guardConditions.add(guardCondition);
 Mockito.when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(true);
 Mockito.when(initialState.equals(currentState)).thenReturn(true);
 Mockito.when(triggerEvent.equals(Mockito.any(Event.class))).thenReturn(true);

 boolean result = transition.evaluateGuards(currentState, triggerEvent);
 assertTrue(result);
 }

 @Test
 public void testTriggerNullInitialState() {
 assertNull(transition.trigger(null, triggerEvent));
 }

 @Test
 public void testTriggerNullOutcomeState() {
 Transition transition2 = new Transition("TransitionName2", triggerEvent, null, outcomeState);
 assertNull(transition2.trigger(initialState, triggerEvent));
 }

 @Test
 public void testTriggerNullTriggerEvent() {
 assertNull(transition.trigger(initialState, null));
 }

 @Test
 public void testTrigger() {
 Mockito.when(initialState.equals(currentState)).thenReturn(true);
 Mockito.when(triggerEvent.equals(Mockito.any(Event.class))).thenReturn(true);
 Mockito.when(guardConditions.isEmpty()).thenReturn(true);

 IState result = transition.trigger(currentState, triggerEvent);
 assertEquals(outcomeState, result);
 }

 @Test
 public void testWillTriggerNullOutcomeState() {
 assertFalse(transition.willTrigger(null, triggerEvent));
 }

 @Test
 public void testWillTriggerNullTriggerEvent() {
 assertFalse(transition.willTrigger(initialState, null));
 }

 @Test
 public void testWillTrigger() {
 Mockito.when(initialState.equals(currentState)).thenReturn(true);
 Mockito.when(triggerEvent.equals(Mockito.any(Event.class))).thenReturn(true);
 Mockito.when(guardConditions.isEmpty()).thenReturn(true);

 boolean result = transition.willTrigger(currentState, triggerEvent);
 assertTrue(result);
 }

 @Test
 public void testWillTriggerFailure() {
 IPredicate predicate = Mockito.mock(IPredicate.class);
 guardConditions.add(predicate);
 Mockito.when(initialState.equals(currentState)).thenReturn(true);
 Mockito.when(triggerEvent.equals(Mockito.any(Event.class))).thenReturn(true);
 Mockito.when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(false);

 boolean result = transition.willTrigger(currentState, triggerEvent);
 assertFalse(result);
 }

 @Test
 public void testToString() {
 String expected = "TransitionName{initialState=null, outcomeState=null, triggerEvent=null, guardConditions=[], name='TransitionName'}";
 String result = transition.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testHashCode() {
 Object obj = new Object();
 int expectedHashCode = obj.hashCode();
 int resultHashCode = transition.hashCode();
 assertEquals(expectedHashCode, resultHashCode);
 }

 @Test
 public void testEqualsNull() {
 assertFalse(transition.equals(null));
 }

 @Test
 public void testEqualsSelf() {
 assertTrue(transition.equals(transition));
 }

 @Test
 public void testEqualsDifferentTransition() {
 Transition otherTransition = new Transition("TransitionName2", triggerEvent, initialState, outcomeState);
 assertFalse(transition.equals(otherTransition));
 }

 @Test
 public void testEqualsSameTransition() {
 Transition otherTransition = transition;
 assertTrue(transition.equals(otherTransition));
 }
}