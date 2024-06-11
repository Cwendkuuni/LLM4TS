package uk.me.jockmacmad.jstate.state;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private List<IPredicate> guardConditions;

 @Before
 public void setUp() {
 initialState = mock(IState.class);
 outcomeState = mock(IState.class);
 triggerEvent = mock(Event.class);
 guardConditions = new ArrayList<>();

 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);

 Assert.assertTrue(transition.equals(transition1));
 Assert.assertTrue(transition1.equals(transition));

 Transition transitionDifferentName = new Transition("AnotherTestTransition", triggerEvent, initialState, outcomeState);
 Transition transitionDifferentTriggerEvent = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);
 Transition transitionDifferentInitialState = new Transition("TestTransition", triggerEvent, new IState() {
 @Override
 public boolean equals(Object obj) {
 return false;
 }
 }, outcomeState);

 Assert.assertFalse(transition.equals(transitionDifferentName));
 Assert.assertFalse(transition.equals(transitionDifferentTriggerEvent));
 Assert.assertFalse(transition.equals(transitionDifferentInitialState));
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);

 Assert.assertEquals(transition.hashCode(), transition1.hashCode());
 }

 @Test
 public void testToString() {
 String transitionString = transition.toString();

 String expectedToString = "Transition{name='TestTransition', initialState=" + initialState.toString() + ", outcomeState=" + outcomeState.toString() + ", triggerEvent=" + triggerEvent.toString() + "}";

 Assert.assertEquals(expectedToString, transitionString);
 }

 @Test
 public void testAddGuardCondition() {
 IPredicate guardCondition = mock(IPredicate.class);

 transition.addGuardCondition(guardCondition);

 List<Object> guardConditionsList = transition.guardConditions.toArray();

 Assert.assertArrayEquals(new Object[]{guardCondition}, guardConditionsList);
 }

 @Test
 public void testAddGuardConditionObject() {
 IGuardCondition guardCondition = mock(IGuardCondition.class);

 transition.addGuardCondition(guardCondition);

 List<Object> guardConditionsList = transition.guardConditions.toArray();

 Assert.assertArrayEquals(new Object[]{guardCondition}, guardConditionsList);
 }

 @Test(expected = ClassCastException.class)
 public void testAddGuardConditionNotIGuardCondition() {
 IPredicate notIGuardCondition = mock(IPredicate.class);

 transition.addGuardCondition(notIGuardCondition);
 }

 @Test
 public void testEvaluateGuardsWhenNoGuards() {
 when(initialState.equals(initialState)).thenReturn(true);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 boolean passesGuards = transition.evaluateGuards(initialState, triggerEvent);

 Assert.assertTrue(passesGuards);
 }

 @Test
 public void testEvaluateGuardsWhenGuardsPresent() {
 when(initialState.equals(initialState)).thenReturn(true);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 IPredicate guardCondition1 = mock(IPredicate.class);
 IPredicate guardCondition2 = mock(IPredicate.class);

 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);

 doReturn(true).when(guardCondition1).evaluate(triggerEvent);
 doReturn(false).when(guardCondition2).evaluate(triggerEvent);

 boolean passesGuards = transition.evaluateGuards(initialState, triggerEvent);

 Assert.assertFalse(passesGuards);
 }

 @Test
 public void testTriggerWhenNoGuards() {
 when(initialState.equals(initialState)).thenReturn(true);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 IState stateToTransitionFrom = initialState;
 IState expectedStateToTransitionTo = outcomeState;

 IState stateTransited = transition.trigger(stateToTransitionFrom, triggerEvent);

 Assert.assertEquals(expectedStateToTransitionTo, stateTransited);
 }

 @Test
 public void testTriggerWhenGuardsPresentPass() {
 when(initialState.equals(initialState)).thenReturn(true);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 IPredicate guardCondition1 = mock(IPredicate.class);
 IPredicate guardCondition2 = mock(IPredicate.class);

 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);

 doReturn(true).when(guardCondition1).evaluate(triggerEvent);
 doReturn(true).when(guardCondition2).evaluate(triggerEvent);

 IState stateToTransitionFrom = initialState;
 IState expectedStateToTransitionTo = outcomeState;

 IState stateTransited = transition.trigger(stateToTransitionFrom, triggerEvent);

 Assert.assertEquals(expectedStateToTransitionTo, stateTransited);
 }

 @Test
 public void testTriggerWhenGuardsPresentFail() {
 when(initialState.equals(initialState)).thenReturn(true);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 IPredicate guardCondition1 = mock(IPredicate.class);
 IPredicate guardCondition2 = mock(IPredicate.class);

 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);

 doReturn(true).when(guardCondition1).evaluate(triggerEvent);
 doReturn(false).when(guardCondition2).evaluate(triggerEvent);

 IState stateToTransitionFrom = initialState;

 IState stateTransited = transition.trigger(stateToTransitionFrom, triggerEvent);

 Assert.assertEquals(stateToTransitionFrom, stateTransited);
 }

 @Test
 public void testWillTriggerWhenNoGuards() {
 when(initialState.equals(initialState)).thenReturn(true);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 boolean willTransition = transition.willTrigger(initialState, triggerEvent);

 Assert.assertTrue(willTransition);
 }

 @Test
 public void testWillTriggerWhenGuardsPresentPass() {
 when(initialState.equals(initialState)).thenReturn(true);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 IPredicate guardCondition1 = mock(IPredicate.class);
 IPredicate guardCondition2 = mock(IPredicate.class);

 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);

 doReturn(true).when(guardCondition1).evaluate(triggerEvent);
 doReturn(true).when(guardCondition2).evaluate(triggerEvent);

 boolean willTransition = transition.willTrigger(initialState, triggerEvent);

 Assert.assertTrue(willTransition);
 }

 @Test
 public void testWillTriggerWhenGuardsPresentFail() {
 when(initialState.equals(initialState)).thenReturn(true);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 IPredicate guardCondition1 = mock(IPredicate.class);
 IPredicate guardCondition2 = mock(IPredicate.class);

 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);

 doReturn(true).when(guardCondition1).evaluate(triggerEvent);
 doReturn(false).when(guardCondition2).evaluate(triggerEvent);

 boolean willTransition = transition.willTrigger(initialState, triggerEvent);

 Assert.assertFalse(willTransition);
 }
}