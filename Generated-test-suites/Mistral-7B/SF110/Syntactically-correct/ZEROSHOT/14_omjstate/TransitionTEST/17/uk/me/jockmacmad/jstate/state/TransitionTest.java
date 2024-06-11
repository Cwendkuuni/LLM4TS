package uk.me.jockmacmad.jstate.state;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import uk.me.jockmacmad.jstate.predicate.*;

import java.util.ArrayList;
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
 guardConditions = new ArrayList<>();
 guardConditions.add(Mockito.mock(IPredicate.class));
 guardConditions.add(Mockito.mock(IGuardCondition.class));

 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testTrigger_SameStateAndEvent_ReturnsOutcomeState() {
 Mockito.when(initialState.equals(initialState)).thenReturn(true);
 Mockito.when(initialState.equals(ArgumentMatchers.any(IState.class))).then(invocation -> initialState.equals(invocation.getArgument(0)));
 Mockito.when(outcomeState.equals(outcomeState)).thenReturn(true);
 Mockito.when(initialState.equals(outcomeState)).thenReturn(false);
 Mockito.when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 transition.addGuardCondition(Mockito.mock(IPredicate.class));
 transition.addGuardCondition(Mockito.mock(IGuardCondition.class));

 IState result = transition.trigger(initialState, triggerEvent);

 assertTrue(transition.getInitialState().equals(initialState));
 assertTrue(transition.getOutcomeState().equals(outcomeState));
 assertTrue(transition.getTriggerEvent().equals(triggerEvent));
 assertEquals(outcomeState, result);
 }

 @Test
 public void testTrigger_DifferentStatesAndEvent_ReturnsInitialState() {
 Mockito.when(initialState.equals(initialState)).thenReturn(true);
 Mockito.when(initialState.equals(ArgumentMatchers.any(IState.class))).then(invocation -> initialState.equals(invocation.getArgument(0)));
 Mockito.when(outcomeState.equals(outcomeState)).thenReturn(true);
 Mockito.when(initialState.equals(outcomeState)).thenReturn(false);
 Mockito.when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 IState differentState = Mockito.mock(IState.class);

 transition.addGuardCondition(Mockito.mock(IPredicate.class));
 transition.addGuardCondition(Mockito.mock(IGuardCondition.class));

 IState result = transition.trigger(differentState, triggerEvent);

 assertTrue(transition.getInitialState().equals(initialState));
 assertTrue(transition.getOutcomeState().equals(outcomeState));
 assertTrue(transition.getTriggerEvent().equals(triggerEvent));
 assertNotEquals(outcomeState, result);
 assertSame(differentState, result);
 }

 @Test
 public void testTrigger_SameStateAndEventButGuardsNotSatisfied_ReturnsInitialState() {
 Mockito.when(initialState.equals(initialState)).thenReturn(true);
 Mockito.when(initialState.equals(ArgumentMatchers.any(IState.class))).then(invocation -> initialState.equals(invocation.getArgument(0)));
 Mockito.when(outcomeState.equals(outcomeState)).thenReturn(true);
 Mockito.when(initialState.equals(outcomeState)).thenReturn(false);
 Mockito.when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 Mockito.when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(false);
 Mockito.when(guardConditions.get(1).evaluate(triggerEvent)).thenReturn(false);

 IState result = transition.trigger(initialState, triggerEvent);

 assertTrue(transition.getInitialState().equals(initialState));
 assertTrue(transition.getOutcomeState().equals(outcomeState));
 assertTrue(transition.getTriggerEvent().equals(triggerEvent));
 assertSame(initialState, result);
 }

 @Test
 public void testTrigger_SameStateAndEventButGuardSatisfied_ReturnsOutcomeState() {
 Mockito.when(initialState.equals(initialState)).thenReturn(true);
 Mockito.when(initialState.equals(ArgumentMatchers.any(IState.class))).then(invocation -> initialState.equals(invocation.getArgument(0)));
 Mockito.when(outcomeState.equals(outcomeState)).thenReturn(true);
 Mockito.when(initialState.equals(outcomeState)).thenReturn(false);
 Mockito.when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 Mockito.when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(true);
 Mockito.when(guardConditions.get(1).evaluate(triggerEvent)).thenReturn(true);

 IState result = transition.trigger(initialState, triggerEvent);

 assertTrue(transition.getInitialState().equals(initialState));
 assertTrue(transition.getOutcomeState().equals(outcomeState));
 assertTrue(transition.getTriggerEvent().equals(triggerEvent));
 assertEquals(outcomeState, result);
 }

 @Test
 public void testWillTrigger_SameStateAndEvent_ReturnsTrueWhenGuardsSatisfied() {
 Mockito.when(initialState.equals(initialState)).thenReturn(true);
 Mockito.when(initialState.equals(ArgumentMatchers.any(IState.class))).then(invocation -> initialState.equals(invocation.getArgument(0)));
 Mockito.when(outcomeState.equals(outcomeState)).thenReturn(true);
 Mockito.when(initialState.equals(outcomeState)).thenReturn(false);
 Mockito.when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 Mockito.when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(true);
 Mockito.when(guardConditions.get(1).evaluate(triggerEvent)).thenReturn(true);

 assertTrue(transition.willTrigger(initialState, triggerEvent));
 }

 @Test
 public void testWillTrigger_SameStateAndEventButGuardsNotSatisfied_ReturnsFalse() {
 Mockito.when(initialState.equals(initialState)).thenReturn(true);
 Mockito.when(initialState.equals(ArgumentMatchers.any(IState.class))).then(invocation -> initialState.equals(invocation.getArgument(0)));
 Mockito.when(outcomeState.equals(outcomeState)).thenReturn(true);
 Mockito.when(initialState.equals(outcomeState)).thenReturn(false);
 Mockito.when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 Mockito.when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(false);
 Mockito.when(guardConditions.get(1).evaluate(triggerEvent)).thenReturn(false);

 assertFalse(transition.willTrigger(initialState, triggerEvent));
 }

 @Test
 public void testWillTrigger_DifferentStatesAndEvent_ReturnsFalse() {
 Mockito.when(initialState.equals(initialState)).thenReturn(true);
 Mockito.when(initialState.equals(ArgumentMatchers.any(IState.class))).then(invocation -> initialState.equals(invocation.getArgument(0)));
 Mockito.when(outcomeState.equals(outcomeState)).thenReturn(true);
 Mockito.when(initialState.equals(outcomeState)).thenReturn(false);
 Mockito.when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 IState differentState = Mockito.mock(IState.class);

 assertFalse(transition.willTrigger(differentState, triggerEvent));
 }
}