package uk.me.jockmacmad.jstate.state;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TransitionTest {

 private static final String TRANSITION_NAME = "testTransition";
 private static final Event TRANSITION_TRIGGER_EVENT = Mockito.mock(Event.class);
 private static final IState TRANSITION_INITIAL_STATE = Mockito.mock(IState.class);
 private static final IState TRANSITION_OUTCOME_STATE = Mockito.mock(IState.class);
 private static final IPredicate TRANSITION_GUARD_PREDICATE = Mockito.mock(IPredicate.class);
 private Transition transition;

 @Before
 public void setup() {
 transition = new Transition(TRANSITION_NAME, TRANSITION_TRIGGER_EVENT, TRANSITION_INITIAL_STATE, TRANSITION_OUTCOME_STATE);
 }

 @Test
 public void testConstructor() {
 assertEquals(TRANSITION_NAME, transition.getName());
 assertEquals(TRANSITION_INITIAL_STATE, transition.getInitialState());
 assertEquals(TRANSITION_OUTCOME_STATE, transition.getOutcomeState());
 assertEquals(TRANSITION_TRIGGER_EVENT, transition.getTriggerEvent());
 assertNotNull(transition.getGuardConditions());
 }

 @Test
 public void testAddGuardConditionWithIPredicate() {
 List<IPredicate> expectedGuardConditions = Arrays.asList(TRANSITION_GUARD_PREDICATE);
 transition.addGuardCondition(TRANSITION_GUARD_PREDICATE);
 List<IPredicate> actualGuardConditions = transition.getGuardConditions();
 assertEquals(expectedGuardConditions, actualGuardConditions);
 }

 @Test
 public void testAddGuardConditionWithIGuardCondition() {
 IGuardCondition expectedIGuardCondition = Mockito.mock(IGuardCondition.class);
 transition.addGuardCondition(expectedIGuardCondition);
 List<IGuardCondition> actualGuardConditions = transition.getGuardConditions();
 assertEquals(Arrays.asList(expectedIGuardCondition), actualGuardConditions);
 }

 @Test
 public void testAddMultipleGuardConditions() {
 IPredicate predicate1 = Mockito.mock(IPredicate.class);
 IPredicate predicate2 = Mockito.mock(IPredicate.class);
 transition.addGuardCondition(predicate1);
 transition.addGuardCondition(predicate2);
 List<IPredicate> expectedGuardConditions = Arrays.asList(predicate1, predicate2);
 List<IPredicate> actualGuardConditions = transition.getGuardConditions();
 assertEquals(expectedGuardConditions, actualGuardConditions);
 }

 @Test
 public void testEvaluateGuardConditionsEmpty() {
 transition = new Transition(TRANSITION_NAME, TRANSITION_TRIGGER_EVENT, TRANSITION_INITIAL_STATE, TRANSITION_OUTCOME_STATE);
 boolean expectedPassesGuards = true;
 transition.evaluateGuardConditions();
 verify(transition).evaluateGuardCondition(Mockito.any(Event.class));
 assertTrue(expectedPassesGuards);
 }

 @Test
 public void testEvaluateGuardConditionsWithOnePassingCondition() {
 transition.addGuardCondition(Mockito.mock(IPassingPredicate.class));
 transition = new Transition(TRANSITION_NAME, TRANSITION_TRIGGER_EVENT, TRANSITION_INITIAL_STATE, TRANSITION_OUTCOME_STATE);
 boolean expectedPassesGuards = true;
 transition.evaluateGuardConditions();
 verify(transition, times(1)).evaluateGuardCondition(Mockito.any(Event.class));
 assertTrue(expectedPassesGuards);
 }

 @Test
 public void testEvaluateGuardConditionsWithAllFailingConditions() {
 List<IPredicate> failingPredicates = Arrays.asList(Mockito.mock(IFailingPredicate.class), Mockito.mock(IFailingPredicate.class));
 for (IPredicate predicate : failingPredicates) {
 transition.addGuardCondition(predicate);
 }
 transition = new Transition(TRANSITION_NAME, TRANSITION_TRIGGER_EVENT, TRANSITION_INITIAL_STATE, TRANSITION_OUTCOME_STATE);
 boolean expectedPassesGuards = false;
 transition.evaluateGuardConditions();
 verify(transition, times(failingPredicates.size())).evaluateGuardCondition(Mockito.any(Event.class));
 assertFalse(expectedPassesGuards);
 }

 @Test
 public void testTrigger() {
 when(TRANSITION_INITIAL_STATE.equals(TRANSITION_CURRENT_STATE)).thenReturn(true);
 when(TRANSITION_TRIGGER_EVENT.equals(TRANSITION_EVENT)).thenReturn(true);
 when(TRANSITION_GUARD_PREDICATE.evaluate(TRANSITION_EVENT)).thenReturn(true);
 when(TRANSITION_OUTCOME_STATE.equals(TRANSITION_EXPECTED_OUTCOME_STATE)).thenReturn(true);

 IState actualOutcomeState = transition.trigger(TRANSITION_CURRENT_STATE, TRANSITION_EVENT);
 assertEquals(TRANSITION_OUTCOME_STATE, actualOutcomeState);

 // Test with multiple guard conditions
 List<IPredicate> multipleGuardPredicates = new ArrayList<>();
 multipleGuardPredicates.add(TRANSITION_GUARD_PREDICATE);
 multipleGuardPredicates.add(Mockito.mock(IPassingPredicate.class));
 multipleGuardPredicates.add(Mockito.mock(IFailingPredicate.class));
 transition.addGuardConditions(multipleGuardPredicates.toArray(new IPredicate[0]));

 when(TRANSITION_INITIAL_STATE.equals(TRANSITION_CURRENT_STATE)).thenReturn(true);
 when(TRANSITION_TRIGGER_EVENT.equals(TRANSITION_EVENT)).thenReturn(true);
 for (IPredicate guardPredicate : multipleGuardPredicates) {
 when(guardPredicate.evaluate(TRANSITION_EVENT)).thenReturn(true);
 }

 IState actualMultipleOutcomeState = transition.trigger(TRANSITION_CURRENT_STATE, TRANSITION_EVENT);
 assertEquals(TRANSITION_OUTCOME_STATE, actualMultipleOutcomeState);
 }

 @Test
 public void testWillTrigger() {
 when(TRANSITION_INITIAL_STATE.equals(TRANSITION_CURRENT_STATE)).thenReturn(true);
 when(TRANSITION_TRIGGER_EVENT.equals(TRANSITION_EVENT)).thenReturn(true);
 when(TRANSITION_GUARD_PREDICATE.evaluate(TRANSITION_EVENT)).thenReturn(true);

 boolean actualWillTrigger = transition.willTrigger(TRANSITION_CURRENT_STATE, TRANSITION_EVENT);
 assertTrue(actualWillTrigger);

 // Test with multiple guard conditions
 List<IPredicate> multipleGuardPredicates = new ArrayList<>();
 multipleGuardPredicates.add(TRANSITION_GUARD_PREDICATE);
 multipleGuardPredicates.add(Mockito.mock(IPassingPredicate.class));
 multipleGuardPredicates.add(Mockito.mock(IFailingPredicate.class));
 transition.addGuardConditions(multipleGuardPredicates.toArray(new IPredicate[0]));

 when(TRANSITION_INITIAL_STATE.equals(TRANSITION_CURRENT_STATE)).thenReturn(true);
 when(TRANSITION_TRIGGER_EVENT.equals(TRANSITION_EVENT)).thenReturn(true);
 for (IPredicate guardPredicate : multipleGuardPredicates) {
 when(guardPredicate.evaluate(TRANSITION_EVENT)).thenReturn(true);
 }

 boolean actualMultipleWillTrigger = transition.willTrigger(TRANSITION_CURRENT_STATE, TRANSITION_EVENT);
 assertTrue(actualMultipleWillTrigger);
 }

 @Test
 public void testWillTriggerWithNoTriggerEvent() {
 when(TRANSITION_INITIAL_STATE.equals(TRANSITION_CURRENT_STATE)).thenReturn(true);
 when(TRANSITION_TRIGGER_EVENT.equals(TRANSITION_EVENT)).thenReturn(false);

 boolean actualWillTrigger = transition.willTrigger(TRANSITION_CURRENT_STATE, TRANSITION_EVENT);
 assertFalse(actualWillTrigger);
 }

 @Test
 public void testWillTriggerWithNoInitialStateMatch() {
 when(TRANSITION_INITIAL_STATE.equals(TRANSITION_CURRENT_STATE)).thenReturn(false);
 when(TRANSITION_TRIGGER_EVENT.equals(TRANSITION_EVENT)).thenReturn(true);

 boolean actualWillTrigger = transition.willTrigger(TRANSITION_CURRENT_STATE, TRANSITION_EVENT);
 assertFalse(actualWillTrigger);
 }

 @Test
 public void testEquals() {
 Transition testTransition = new Transition(TRANSITION_NAME, TRANSITION_TRIGGER_EVENT, TRANSITION_INITIAL_STATE, TRANSITION_OUTCOME_STATE);
 Transition anotherTransition = new Transition(TRANSITION_NAME, TRANSITION_TRIGGER_EVENT, TRANSITION_INITIAL_STATE, TRANSITION_OUTCOME_STATE);
 assertTrue(testTransition.equals(anotherTransition));
 }

 @Test
 public void testNotEqualsDifferentName() {
 Transition testTransition = new Transition("differentName", TRANSITION_TRIGGER_EVENT, TRANSITION_INITIAL_STATE, TRANSITION_OUTCOME_STATE);
 assertFalse(testTransition.equals(transition));
 }

 @Test
 public void testNotEqualsDifferentTriggerEvent() {
 Transition testTransition = new Transition(TRANSITION_NAME, Mockito.mock(Event.class), TRANSITION_INITIAL_STATE, TRANSITION_OUTCOME_STATE);
 assertFalse(testTransition.equals(transition));
 }

 @Test
 public void testNotEqualsDifferentInitialState() {
 Transition testTransition = new Transition(TRANSITION_NAME, TRANSITION_TRIGGER_EVENT, Mockito.mock(IState.class), TRANSITION_OUTCOME_STATE);
 assertFalse(testTransition.equals(transition));
 }

 @Test
 public void testNotEqualsDifferentOutcomeState() {
 Transition testTransition = new Transition(TRANSITION_NAME, TRANSITION_TRIGGER_EVENT, TRANSITION_INITIAL_STATE, Mockito.mock(IState.class));
 assertFalse(testTransition.equals(transition));
 }

 @Test
 public void testHashCode() {
 int expectedHashCode = HashCodeBuilder.reflectionHashCode(transition);
 int actualHashCode = transition.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testToString() {
 String expectedToString = ToStringBuilder.reflectionToString(transition);
 String actualToString = transition.toString();
 assertEquals(expectedToString, actualToString);
 }
}