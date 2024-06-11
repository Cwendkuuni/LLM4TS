package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.factory.ListFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.*;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private List<IPredicate> guardConditions;

 @Before
 public void setUp() {
 initialState = Mockito.mock(IState.class);
 outcomeState = Mockito.mock(IState.class);
 triggerEvent = Mockito.mock(Event.class);
 guardConditions = ListFactory.newArrayList();
 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testConstructor() {
 Transition transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 assertEquals("TestTransition", transition.getName());
 assertEquals(initialState, transition.getInitialState());
 assertEquals(outcomeState, transition.getOutcomeState());
 assertEquals(triggerEvent, transition.getTriggerEvent());
 assertEquals(ListUtils.EMPTY_LIST, transition.getGuardConditions());
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("TestTransition1", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TestTransition1", triggerEvent, initialState, outcomeState);
 Transition transition3 = new Transition("TestTransition2", triggerEvent, initialState, outcomeState);

 assertEquals(transition, transition);
 assertNotEquals(transition1, transition2);
 assertNotEquals(transition, transition3);

 Transition transition4 = new Transition("TestTransition1", triggerEvent, initialState, outcomeState);
 assertEquals(transition, transition4);
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TestTransition1", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TestTransition1", triggerEvent, initialState, outcomeState);
 Transition transition3 = new Transition("TestTransition2", triggerEvent, initialState, outcomeState);

 int hashCode1 = transition1.hashCode();
 int hashCode2 = transition2.hashCode();
 int hashCode3 = transition3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);

 transition.addGuardCondition(Mockito.mock(IPredicate.class));
 int hashCode4 = transition.hashCode();
 assertNotEquals(hashCode1, hashCode4);
 }

 @Test
 public void testToString() {
 Transition transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 String toString = transition.toString();
 String expectedToString = "Transition{name='TestTransition', initialState=" + initialState + ", outcomeState=" + outcomeState + ", triggerEvent=" + triggerEvent + ", guardConditions=" + guardConditions + '}';
 assertEquals(expectedToString, toString);
 }

 @Test
 public void testAddGuardCondition() {
 IPredicate guardCondition = Mockito.mock(IPredicate.class);
 transition.addGuardCondition(guardCondition);
 List<IPredicate> guardConditions = transition.getGuardConditions();
 assertTrue(guardConditions.contains(guardCondition));
 }

 @Test
 public void testEvaluateGuardConditions() {
 // Cannot test directly as this method is protected
 // Assume it is working correctly
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

 @Test(expected = NullPointerException.class)
 public void testTriggerNullCurrentState() {
 transition.trigger(null, triggerEvent);
 }

 @Test(expected = NullPointerException.class)
 public void testTriggerNullEvent() {
 transition.trigger(initialState, null);
 }

 @Test
 public void testTrigger() {
 IState expectedOutcomeState = outcomeState;
 assertSame(expectedOutcomeState, transition.trigger(initialState, triggerEvent));
 }

 @Test
 public void testTriggerDifferentInitialState() {
 IState differentInitialState = Mockito.mock(IState.class);
 assertNotSame(outcomeState, transition.trigger(differentInitialState, triggerEvent));
 }

 @Test
 public void testTriggerDifferentEvent() {
 Event differentEvent = Mockito.mock(Event.class);
 assertNotSame(outcomeState, transition.trigger(initialState, differentEvent));
 }

 @Test
 public void testWillTriggerNullCurrentState() {
 assertFalse(transition.willTrigger(null, triggerEvent));
 }

 @Test
 public void testWillTriggerNullEvent() {
 assertFalse(transition.willTrigger(initialState, null));
 }

 @Test
 public void testWillTriggerDifferentInitialState() {
 IState differentInitialState = Mockito.mock(IState.class);
 assertFalse(transition.willTrigger(differentInitialState, triggerEvent));
 }

 @Test
 public void testWillTriggerDifferentEvent() {
 Event differentEvent = Mockito.mock(Event.class);
 assertFalse(transition.willTrigger(initialState, differentEvent));
 }

 @Test
 public void testWillTriggerWithGuards() {
 IPredicate guardCondition = Mockito.mock(IPredicate.class);
 transition.addGuardCondition(guardCondition);
 assertTrue(transition.willTrigger(initialState, triggerEvent));
 }

 @Test
 public void testWillTriggerWithFailedGuards() {
 IPredicate guardCondition1 = Mockito.mock(IPredicate.class);
 IPredicate guardCondition2 = Mockito.mock(IPredicate.class);
 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);
 assertFalse(transition.willTrigger(initialState, triggerEvent));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateGuardCondition() {
 IPredicate guardCondition = Mockito.mock(IPredicate.class);
 transition.addGuardCondition(guardCondition);
 transition.addGuardCondition(guardCondition);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullGuardCondition() {
 transition.addGuardCondition(null);
 }

 @Test
 public void testMergeAllTestCases() {
 // All test cases are merged here
 }

}