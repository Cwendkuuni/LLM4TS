package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

public class TransitionTest {

 private IState initialState;
 private IState outcomeState;
 private List<IGuardCondition> guardConditions;
 private Event triggerEvent;
 private Transition transition;

 @Before
 public void setUp() {
 initialState = mock(IState.class);
 outcomeState = mock(IState.class);
 guardConditions = new ArrayList<>();
 guardConditions.add(mock(IPredicate.class));
 guardConditions.add(mock(IGuardCondition.class));
 triggerEvent = mock(Event.class);

 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 transition.addGuardCondition(guardConditions.get(0));
 transition.addGuardCondition(guardConditions.get(1));
 }

 @Test
 public void testEquals() {
 Transition otherTransition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 otherTransition.addGuardCondition(guardConditions.get(0));
 otherTransition.addGuardCondition(guardConditions.get(1));

 assertTrue(transition.equals(otherTransition));
 }

 @Test
 public void testHashCode() {
 Transition otherTransition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 otherTransition.addGuardCondition(guardConditions.get(0));
 otherTransition.addGuardCondition(guardConditions.get(1));

 int expectedHashCode = transition.hashCode();
 int actualHashCode = otherTransition.hashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testToString() {
 String expectedToString = "Transition{name='TestTransition', triggerEvent=Event{name='testEvent'}, initialState=MockState{stateId='testId'}, outcomeState=MockState{stateId='testId'}}";
 String actualToString = transition.toString();

 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testTrigger() {
 when(initialState.equals(initialState)).thenReturn(true);
 when(outcomeState.equals(outcomeState)).thenReturn(true);
 when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(true);
 when(guardConditions.get(1).evaluate(triggerEvent)).thenReturn(true);

 IState retVal = transition.trigger(initialState, triggerEvent);

 assertEquals(outcomeState, retVal);
 }

 @Test
 public void testTrigger_NoMatchingGuardConditions() {
 when(initialState.equals(initialState)).thenReturn(true);
 when(outcomeState.equals(outcomeState)).thenReturn(true);
 when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(false);
 when(guardConditions.get(1).evaluate(triggerEvent)).thenReturn(false);

 IState retVal = transition.trigger(initialState, triggerEvent);

 assertEquals(initialState, retVal);
 }

 @Test
 public void testTrigger_InitialStateMismatch() {
 IState differentInitialState = mock(IState.class);
 when(initialState.equals(differentInitialState)).thenReturn(false);
 when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(true);
 when(guardConditions.get(1).evaluate(triggerEvent)).thenReturn(true);

 IState retVal = transition.trigger(differentInitialState, triggerEvent);

 assertEquals(differentInitialState, retVal);
 }

 @Test
 public void testTrigger_EventMismatch() {
 Event differentEvent = mock(Event.class);
 when(initialState.equals(initialState)).thenReturn(true);
 when(outcomeState.equals(outcomeState)).thenReturn(true);
 when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(true);
 when(guardConditions.get(1).evaluate(triggerEvent)).thenReturn(true);
 when(triggerEvent.equals(differentEvent)).thenReturn(false);

 IState retVal = transition.trigger(initialState, differentEvent);

 assertEquals(initialState, retVal);
 }

 @Test
 public void testWillTrigger() {
 when(initialState.equals(initialState)).thenReturn(true);
 when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(true);
 when(guardConditions.get(1).evaluate(triggerEvent)).thenReturn(true);

 boolean retVal = transition.willTrigger(initialState, triggerEvent);

 assertTrue(retVal);
 }

 @Test
 public void testWillTrigger_NoMatchingGuardConditions() {
 when(initialState.equals(initialState)).thenReturn(true);
 when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(false);
 when(guardConditions.get(1).evaluate(triggerEvent)).thenReturn(false);

 boolean retVal = transition.willTrigger(initialState, triggerEvent);

 assertFalse(retVal);
 }
}