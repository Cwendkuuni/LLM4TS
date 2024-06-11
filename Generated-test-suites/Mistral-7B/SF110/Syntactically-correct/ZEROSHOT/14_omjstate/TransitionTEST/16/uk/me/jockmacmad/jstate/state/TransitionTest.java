package uk.me.jockmacmad.jstate.state;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private List<IGuardCondition> guardConditions;

 @Before
 public void setup() {
 initialState = Mockito.mock(IState.class);
 outcomeState = Mockito.mock(IState.class);
 triggerEvent = Mockito.mock(Event.class);
 guardConditions = new ArrayList<>();

 transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testEquals() {
 Transition sameTransition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 Transition differentTransition = new Transition("DifferentTransitionName", triggerEvent, initialState, outcomeState);

 assertEquals(transition, sameTransition);
 assertNotEquals(transition, differentTransition);
 }

 @Test
 public void testHashCode() {
 Transition sameTransition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 Transition differentTransition = new Transition("DifferentTransitionName", triggerEvent, initialState, outcomeState);

 assertEquals(transition.hashCode(), sameTransition.hashCode());
 assertNotEquals(transition.hashCode(), differentTransition.hashCode());
 }

 @Test
 public void testToString() {
 String expectedToString = "TransitionName{initialState=" + initialState.toString() + ", outcomeState=" + outcomeState.toString() + ", triggerEvent=" + triggerEvent.toString() + ", guardConditions=" + guardConditions.toString() + '}';
 assertEquals(expectedToString, transition.toString());
 }

 @Test
 public void testTrigger() {
 when(initialState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(outcomeState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(triggerEvent.equals(ArgumentMatchers.any())).thenReturn(true);

 // No guard conditions
 transition.trigger(initialState, triggerEvent);
 assertEquals(outcomeState, transition.getOutcomeState());

 // Guard conditions passed
 GuardCondition guardConditionMock = Mockito.mock(GuardCondition.class);
 when(guardConditionMock.evaluate(triggerEvent)).thenReturn(true);
 transition.addGuardCondition(guardConditionMock);

 when(initialState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(outcomeState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(triggerEvent.equals(ArgumentMatchers.any())).thenReturn(true);
 for (IGuardCondition igc : guardConditions) {
 when(igc.evaluate(triggerEvent)).thenReturn(true);
 }

 transition.trigger(initialState, triggerEvent);
 assertEquals(outcomeState, transition.getOutcomeState());

 // Guard conditions failed
 GuardCondition guardConditionMockFailed = Mockito.mock(GuardCondition.class);
 when(guardConditionMockFailed.evaluate(triggerEvent)).thenReturn(false);
 transition.addGuardCondition(guardConditionMockFailed);

 when(initialState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(outcomeState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(triggerEvent.equals(ArgumentMatchers.any())).thenReturn(true);
 for (final IGuardCondition igc : guardConditions) {
 if (igc != guardConditionMockFailed) {
 when(igc.evaluate(triggerEvent)).thenReturn(true);
 }
 }

 transition.trigger(initialState, triggerEvent);
 assertNotEquals(outcomeState, transition.getOutcomeState());
 }

 @Test
 public void testWillTrigger() {
 when(initialState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(outcomeState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(triggerEvent.equals(ArgumentMatchers.any())).thenReturn(true);

 // No guard conditions
 assertFalse(transition.willTrigger(initialState, triggerEvent));

 // Guard conditions passed
 GuardCondition guardConditionMock = Mockito.mock(GuardCondition.class);
 when(guardConditionMock.evaluate(triggerEvent)).thenReturn(true);
 transition.addGuardCondition(guardConditionMock);

 when(initialState. equals(ArgumentMatchers.any())).thenReturn(true);
 when(outcomeState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(triggerEvent.equals(ArgumentMatchers.any())).thenReturn(true);
 for (IGuardCondition igc : guardConditions) {
 when(igc.evaluate(triggerEvent)).thenReturn(true);
 }

 assertTrue(transition.willTrigger(initialState, triggerEvent));

 // Guard conditions failed
 GuardCondition guardConditionMockFailed = Mockito.mock(GuardCondition.class);
 when(guardConditionMockFailed.evaluate(triggerEvent)).thenReturn(false);
 transition.addGuardCondition(guardConditionMockFailed);

 when(initialState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(outcomeState.equals(ArgumentMatchers.any())).thenReturn(true);
 when(triggerEvent.equals(ArgumentMatchers.any())).thenReturn(true);
 for (final IGuardCondition igc : guardConditions) {
 if (igc != guardConditionMockFailed) {
 when(igc.evaluate(triggerEvent)).thenReturn(true);
 }
 }

 when(initialState.equals(initialState)).thenReturn(true);
 when(outcomeState.equals(outcomeState)).thenReturn(true);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);
 when(guardConditionMockFailed.evaluate(triggerEvent)).thenReturn(false);

 assertFalse(transition.willTrigger(initialState, triggerEvent));
 }

 @Test
 public void testAddGuardCondition() {
 IPredicate predicateMock = Mockito.mock(IPredicate.class);
 IGuardCondition guardConditionMock = new GuardCondition(predicateMock);

 transition.addGuardCondition(guardConditionMock);
 assertEquals(1, transition.getGuardConditions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullGuardCondition() {
 transition.addGuardCondition(null);
 }

 @Test
 public void testAddDuplicateGuardCondition() {
 IPredicate predicateMock = Mockito.mock(IPredicate.class);
 IGuardCondition guardConditionMock = new GuardCondition(predicateMock);

 transition.addGuardCondition(guardConditionMock);
 transition.addGuardCondition(guardConditionMock);
 assertEquals(1, transition.getGuardConditions().size());
 }

 @Test
 public void testEvaluateGuardConditions() {
 GuardCondition guardConditionMock1 = Mockito.mock(GuardCondition.class);
 GuardCondition guardConditionMock2 = Mockito.mock(GuardCondition.class);
 IPredicate predicateMock1 = Mockito.mock(IPredicate.class);
 IPredicate predicateMock2 = Mockito.mock(IPredicate.class);

 when(predicateMock1.evaluate(ArgumentMatchers.any())).thenReturn(true);
 when(predicateMock2.evaluate(ArgumentMatchers.any())).thenReturn(false);

 when(guardConditionMock1.getPredicate()).thenReturn(predicateMock1);
 when(guardConditionMock2.getPredicate()).thenReturn(predicateMock2);

 transition.addGuardCondition(guardConditionMock1);
 transition.addGuardCondition(guardConditionMock2);

 transition.evaluateGuardConditions();
 assertFalse(transition.passesGuards());
 }

 @Test
 public void testPassesGuardsWhenAllConditionsPass() {
 GuardCondition guardConditionMock1 = Mockito.mock(GuardCondition.class);
 GuardCondition guardConditionMock2 = Mockito.mock(GuardCondition.class);
 IPredicate predicateMock1 = Mockito.mock(IPredicate.class);
 IPredicate predicateMock2 = Mockito.mock(IPredicate.class);

 when(predicateMock1.evaluate(ArgumentMatchers.any())).thenReturn(true);
 when(predicateMock2.evaluate(ArgumentMatchers.any())).thenReturn(true);

 when(guardConditionMock1.getPredicate()).thenReturn(predicateMock1);
 when(guardConditionMock2.getPredicate()).thenReturn(predicateMock2);

 transition.addGuardCondition(guardConditionMock1);
 transition.addGuardCondition(guardConditionMock2);

 assertTrue(transition.passesGuards());
 }

 @Test
 public void testGetGuardConditions() {
 List<IGuardCondition> expectedGuardConditions = Arrays.asList(guardConditionMock1, guardConditionMock2);

 IPredicate predicateMock1 = Mockito.mock(IPredicate.class);
 IPredicate predicateMock2 = Mockito.mock(IPredicate.class);
 GuardCondition guardConditionMock1 = new GuardCondition(predicateMock1);
 GuardCondition guardConditionMock2 = new GuardCondition(predicateMock2);

 when(predicateMock1.evaluate(ArgumentMatchers.any())).thenReturn(true);
 when(predicateMock2.evaluate(ArgumentMatchers.any())).thenReturn(true);

 transition.addGuardCondition(guardConditionMock1);
 transition.addGuardCondition(guardConditionMock2);

 List<IGuardCondition> actualGuardConditions = transition.getGuardConditions();
 assertEquals(expectedGuardConditions, actualGuardConditions);
 }
}