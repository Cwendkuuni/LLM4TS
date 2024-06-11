package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class TransitionTest {

 IState initialState = mock(IState.class);
 IState outcomeState = mock(IState.class);
 Event triggerEvent = mock(Event.class);
 List<IGuardCondition> guardConditions = new ArrayList<>();
 Transition transition;

 @Before
 public void setUp() {
 transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);

 assertTrue(EqualsBuilder.reflectionEquals(transition1, transition2));
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);

 assertEquals(HashCodeBuilder.reflectionHashCode(transition1), HashCodeBuilder.reflectionHashCode(transition2));
 }

 @Test
 public void testToString() {
 String expectedToString = "TransitionName{initialState=" + initialState + ", outcomeState=" + outcomeState + ", triggerEvent=" + triggerEvent + "}";
 assertEquals(expectedToString, ToStringBuilder.reflectionToString(transition));
 }

 @Test
 public void testTrigger_emptyGuardConditions() {
 when(initialState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(triggerEvent.equals(ArgumentMatchers.any())).thenReturn(true);

 IState retVal = transition.trigger(initialState, triggerEvent);
 assertEquals(outcomeState, retVal);
 }

 @Test
 public void testTrigger_guardConditionsPass() {
 when(initialState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(triggerEvent.equals(ArgumentMatchers.any())).thenReturn(true);
 when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(true);

 IState retVal = transition.trigger(initialState, triggerEvent);
 assertEquals(outcomeState, retVal);
 }

 @Test
 public void testTrigger_guardConditionsFail() {
 when(initialState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(triggerEvent.equals(ArgumentMatchers.any())).thenReturn(true);
 when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(false);

 IState retVal = transition.trigger(initialState, triggerEvent);
 assertNotNull(retVal);
 assertNotEquals(outcomeState, retVal);
 }

 @Test
 public void testWillTrigger_emptyGuardConditions() {
 when(initialState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(triggerEvent.equals(ArgumentMatchers.any())).thenReturn(true);

 boolean retVal = transition.willTrigger(initialState, triggerEvent);
 assertTrue(retVal);
 }

 @Test
 public void testWillTrigger_guardConditionsPass() {
 when(initialState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(triggerEvent.equals(ArgumentMatchers.any())).thenReturn(true);
 when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(true);

 boolean retVal = transition.willTrigger(initialState, triggerEvent);
 assertTrue(retVal);
 }

 @Test
 public void testWillTrigger_guardConditionsFail() {
 when(initialState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(triggerEvent.equals(ArgumentMatchers.any())).thenReturn(true);
 when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(false);

 boolean retVal = transition.willTrigger(initialState, triggerEvent);
 assertFalse(retVal);
 }

 @Test
 public void testAddGuardCondition() {
 IPredicate predicate = mock(IPredicate.class);
 transition.addGuardCondition(predicate);
 assertEquals(predicate, transition.getGuardCondition(0));
 }

 @Test
 public void testAddGuardCondition_multiple() {
 IPredicate predicate1 = mock(IPredicate.class);
 IPredicate predicate2 = mock(IPredicate.class);
 transition.addGuardCondition(predicate1);
 transition.addGuardCondition(predicate2);
 assertEquals(2, transition.getGuardConditions().size());
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
 public void testGetGuardConditions_empty() {
 assertNotNull(transition.getGuardConditions());
 assertEquals(0, transition.getGuardConditions().size());
 }

 @Test
 public void testGetGuardConditions_nonEmpty() {
 IPredicate predicate1 = mock(IPredicate.class);
 IPredicate predicate2 = mock(IPredicate.class);
 transition.addGuardCondition(predicate1);
 transition.addGuardCondition(predicate2);
 assertEquals(2, transition.getGuardConditions().size());
 }
}