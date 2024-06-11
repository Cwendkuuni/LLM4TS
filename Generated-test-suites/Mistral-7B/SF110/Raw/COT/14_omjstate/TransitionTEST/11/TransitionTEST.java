package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
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
 private IState expectedOutcomeState;
 private List<IGuardCondition> guardConditions;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 initialState = Mockito.mock(IState.class);
 outcomeState = Mockito.mock(IState.class);
 triggerEvent = new Event("testEvent");
 transition = new Transition("testTransition", triggerEvent, initialState, outcomeState);
 guardConditions = new ArrayList<>();
 }

 @Test
 public void testConstructor() {
 assertEquals("testTransition", transition.getName());
 assertEquals(initialState, transition.getInitialState());
 assertEquals(outcomeState, transition.getOutcomeState());
 assertEquals(triggerEvent, transition.getTriggerEvent());
 assertEquals(0, transition.getGuardConditions().size());
 }

 @Test
 public void testAddGuardCondition() {
 IPredicate predicateMock = Mockito.mock(IPredicate.class);
 guardConditions.add(new GuardCondition(predicateMock));
 transition.addGuardCondition(predicateMock);
 assertEquals(1, transition.getGuardConditions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullGuardCondition() {
 transition.addGuardCondition(null);
 }

 @Test
 public void testEquals() {
 Transition transition2 = new Transition("testTransition", triggerEvent, initialState, outcomeState);
 Transition transition3 = new Transition("testTransition2", triggerEvent, initialState, outcomeState);
 Transition transition4 = new Transition("testTransition", triggerEvent, initialState, outcomeState);

 assertTrue(EqualsBuilder.reflectionEquals(transition, transition));
 assertTrue(EqualsBuilder.reflectionEquals(transition, transition4));
 assertFalse(EqualsBuilder.reflectionEquals(transition, transition2));
 }

 @Test
 public void testHashCode() {
 Transition transition2 = new Transition("testTransition2", triggerEvent, initialState, outcomeState);
 assertEquals(HashCodeBuilder.reflectionHashCode(transition), HashCodeBuilder.reflectionHashCode(transition2));
 }

 @Test
 public void testEvaluateGuardsEmpty() {
 transition.evaluateGuardConditions();
 assertTrue(true);
 }

 @Test
 public void testEvaluateGuardsWithGuards() {
 IPredicate predicateMock = Mockito.mock(IPredicate.class);
 GuardCondition guardConditionMock = new GuardCondition(predicateMock);
 transition.addGuardCondition(guardConditionMock);
 Mockito.when(predicateMock.evaluate(triggerEvent)).thenReturn(true);
 transition.evaluateGuardConditions();
 assertTrue(true);
 }

 @Test
 public void testTriggerNullCurrentState() {
 assertNull(transition.trigger(null, triggerEvent));
 }

 @Test
 public void testTriggerWithInitialStateAndTriggerEvent() {
 expectedOutcomeState = outcomeState;
 transition.trigger(initialState, triggerEvent);
 assertEquals(expectedOutcomeState, transition.getOutcomeState());
 }

 @Test
 public void testTriggerWithInitialStateButDifferentTriggerEvent() {
 expectedOutcomeState = initialState;
 transition.trigger(initialState, new Event("differentEvent"));
 assertEquals(expectedOutcomeState, transition.getOutcomeState());
 }

 @Test
 public void testTriggerWithNonInitialStateAndTriggerEvent() {
 IState currentStateMock = Mockito.mock(IState.class);
 expectedOutcomeState = initialState;
 Mockito.when(currentStateMock.equals(initialState)).thenReturn(false);
 transition.trigger(currentStateMock, triggerEvent);
 assertEquals(expectedOutcomeState, transition.getOutcomeState());
 }

 @Test
 public void testTriggerWithInitialStateAndDifferentTriggerEventButPassingGuards() {
 IPredicate predicateMock = Mockito.mock(IPredicate.class);
 GuardCondition guardConditionMock = new GuardCondition(predicateMock);
 transition.addGuardCondition(guardConditionMock);
 Mockito.when(predicateMock.evaluate(triggerEvent)).thenReturn(true);
 expectedOutcomeState = outcomeState;
 transition.trigger(initialState, triggerEvent);
 assertEquals(expectedOutcomeState, transition.getOutcomeState());
 }

 @Test
 public void testTriggerWithInitialStateAndDifferentTriggerEventButFailingGuards() {
 IPredicate predicateMock = Mockito.mock(IPredicate.class);
 GuardCondition guardConditionMock = new GuardCondition(predicateMock);
 transition.addGuardCondition(guardConditionMock);
 Mockito.when(predicateMock.evaluate(triggerEvent)).thenReturn(false);
 expectedOutcomeState = initialState;
 transition.trigger(initialState, triggerEvent);
 assertEquals(expectedOutcomeState, transition.getOutcomeState());
 }

 @Test
 public void testWillTriggerNullCurrentState() {
 assertFalse(transition.willTrigger(null, triggerEvent));
 }

 @Test
 public void testWillTriggerWithInitialStateAndTriggerEvent() {
 expectedOutcomeState = outcomeState;
 assertTrue(transition.willTrigger(initialState, triggerEvent));
 }

 @Test
 public void testWillTriggerWithInitialStateButDifferentTriggerEvent() {
 expectedOutcomeState = initialState;
 assertFalse(transition.willTrigger(initialState, new Event("differentEvent")));
 }

 @Test
 public void testWillTriggerWithNonInitialStateAndTriggerEvent() {
 IState currentStateMock = Mockito.mock(IState.class);
 expectedOutcomeState = initialState;
 Mockito.when(currentStateMock.equals(initialState)).thenReturn(false);
 assertFalse(transition.willTrigger(currentStateMock, triggerEvent));
 }

 @Test
 public void testWillTriggerWithInitialStateAndDifferentTriggerEventButPassingGuards() {
 IPredicate predicateMock = Mockito.mock(IPredicate.class);
 GuardCondition guardConditionMock = new GuardCondition(predicateMock);
 transition.addGuardCondition(guardConditionMock);
 Mockito.when(predicateMock.evaluate(triggerEvent)).thenReturn(true);
 expectedOutcomeState = outcomeState;
 assertTrue(transition.willTrigger(initialState, triggerEvent));
 }

 @Test
 public void testWillTriggerWithInitialStateAndDifferentTriggerEventButFailingGuards() {
 IPredicate predicateMock = Mockito.mock(IPredicate.class);
 GuardCondition guardConditionMock = new GuardCondition(predicateMock);
 transition.addGuardCondition(guardConditionMock);
 Mockito.when(predicateMock.evaluate(triggerEvent)).thenReturn(false);
 expectedOutcomeState = initialState;
 assertFalse(transition.willTrigger(initialState, triggerEvent));
 }

 @Test
 public void testToString() {
 String expectedToString = "Transition{name='testTransition', initialState=" + initialState.toString() + ", outcomeState=" + outcomeState.toString() + ", triggerEvent=" + triggerEvent.toString() + ", guardConditions=" + guardConditions.toString() + '}';
 assertEquals(expectedToString, transition.toString());
 }
}