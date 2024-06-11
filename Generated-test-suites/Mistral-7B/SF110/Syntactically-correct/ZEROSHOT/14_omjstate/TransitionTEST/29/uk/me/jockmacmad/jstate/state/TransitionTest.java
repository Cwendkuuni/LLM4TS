package uk.me.jockmacmad.jstate.state;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private List<IGuardCondition> guardConditions;

 @Before
 public void setUp() {
 initialState = Mockito.mock(IState.class);
 outcomeState = Mockito.mock(IState.class);
 triggerEvent = Mockito.mock(Event.class);
 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 assertEquals(transition, transition1);
 assertEquals(transition, transition2);
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 assertEquals(transition.hashCode(), transition1.hashCode());
 assertEquals(transition.hashCode(), transition2.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("Transition{name='TestTransition', initialState=null, outcomeState=null, triggerEvent=null, guardConditions=[]}", transition.toString());
 }

 @Test
 public void testTrigger() {
 when(initialState.equals(initialState)).thenReturn(true);
 when(initialState.equals(outcomeState)).thenReturn(false);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 transition.addGuardCondition(Mockito.mock(IPredicate.class));
 transition.addGuardCondition(Mockito.mock(IGuardCondition.class));

 IState currentState = Mockito.mock(IState.class);
 when(currentState.equals(initialState)).thenReturn(true);
 when(currentState.equals(outcomeState)).thenReturn(false);

 IState expectedOutcomeState = Mockito.mock(IState.class);
 when(outcomeState.equals(expectedOutcomeState)).thenReturn(true);

 IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
 assertEquals(expectedOutcomeState, actualOutcomeState);
 }

 @Test
 public void testWillTrigger() {
 when(initialState.equals(initialState)).thenReturn(true);
 when(initialState.equals(outcomeState)).thenReturn(false);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 transition.addGuardCondition(Mockito.mock(IPredicate.class));
 transition.addGuardCondition(Mockito.mock(IGuardCondition.class));

 IState currentState = Mockito.mock(IState.class);
 when(currentState.equals(initialState)).thenReturn(true);
 when(currentState.equals(outcomeState)).thenReturn(false);

 IState expectedOutcomeState = Mockito.mock(IState.class);
 when(outcomeState.equals(expectedOutcomeState)).thenReturn(true);

 when(guardConditions.isEmpty()).thenReturn(true);
 when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(true);
 when(guardConditions.get(1).evaluate(triggerEvent)).thenReturn(true);

 assertTrue(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testWillNotTrigger() {
 when(initialState.equals(initialState)).thenReturn(true);
 when(initialState.equals(outcomeState)).thenReturn(false);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 transition.addGuardCondition(Mockito.mock(IPredicate.class));
 transition.addGuardCondition(Mockito.mock(IGuardCondition.class));

 IState currentState = Mockito.mock(IState.class);
 when(currentState.equals(initialState)).thenReturn(true);
 when(currentState.equals(outcomeState)).thenReturn(false);

 IState expectedOutcomeState = Mockito.mock(IState.class);
 when(outcomeState.equals(expectedOutcomeState)).thenReturn(true);

 when(guardConditions.isEmpty()).thenReturn(false);
 when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(false);

 assertFalse(transition.willTrigger(currentState, triggerEvent));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInitialState() {
 transition = new Transition(null, triggerEvent, initialState, outcomeState);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullOutcomeState() {
 transition = new Transition("TestTransition", triggerEvent, initialState, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTriggerEvent() {
 transition = new Transition("TestTransition", null, initialState, outcomeState);
 }
}