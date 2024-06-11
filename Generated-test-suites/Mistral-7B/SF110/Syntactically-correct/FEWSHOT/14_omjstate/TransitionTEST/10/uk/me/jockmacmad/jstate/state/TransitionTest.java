package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.*;
import java.util.*;

public class TransitionTest {

 private Transition transition;
 private IState initialStateMock;
 private IState outcomeStateMock;
 private Event triggerEventMock;
 private List<IGuardCondition> guardConditions;

 @Before
 public void setup() {
 initialStateMock = mock(IState.class);
 outcomeStateMock = mock(IState.class);
 triggerEventMock = mock(Event.class);
 guardConditions = new ArrayList<>();
 transition = new Transition("TransitionName", triggerEventMock, initialStateMock, outcomeStateMock);
 }

 @Test
 public void testEquals() {
 Transition sameTransition = new Transition("TransitionName", triggerEventMock, initialStateMock, outcomeStateMock);
 assertTrue(transition.equals(sameTransition));
 Transition differentTransition = new Transition("DifferentName", triggerEventMock, initialStateMock, outcomeStateMock);
 assertFalse(transition.equals(differentTransition));
 }

 @Test
 public void testHashCode() {
 Transition sameTransition = new Transition("TransitionName", triggerEventMock, initialStateMock, outcomeStateMock);
 assertEquals(transition.hashCode(), sameTransition.hashCode());
 Transition differentTransition = new Transition("DifferentName", triggerEventMock, initialStateMock, outcomeStateMock);
 assertNotEquals(transition.hashCode(), differentTransition.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("TransitionName [initialState=MockIState@1, outcomeState=MockIState@2, triggerEvent=MockEvent@3]", transition.toString());
 }

 @Test
 public void testTrigger() {
 when(initialStateMock.equals(any())).thenReturn(true);
 when(outcomeStateMock.equals(any())).thenReturn(true);
 when(triggerEventMock.equals(any())).thenReturn(true);

 transition.addGuardCondition(mock(IPredicate.class));

 IState result = transition.trigger(initialStateMock, triggerEventMock);
 assertEquals(outcomeStateMock, result);

 verify(initialStateMock, times(1)).equals(initialStateMock);
 verify(outcomeStateMock, times(1)).equals(outcomeStateMock);
 verify(triggerEventMock, times(1)).equals(triggerEventMock);
 }

 @Test
 public void testWillTrigger() {
 when(initialStateMock.equals(any())).thenReturn(true);
 when(outcomeStateMock.equals(any())).thenReturn(true);
 when(triggerEventMock.equals(any())).thenReturn(true);

 transition.addGuardCondition(mock(IGuardCondition.class));

 boolean result = transition.willTrigger(initialStateMock, triggerEventMock);
 assertTrue(result);

 verify(initialStateMock, times(1)).equals(initialStateMock);
 verify(outcomeStateMock, times(1)).equals(outcomeStateMock);
 verify(triggerEventMock, times(1)).equals(triggerEventMock);
 }

 @Test
 public void testTriggerWithNoGuards() {
 when(initialStateMock.equals(any())).thenReturn(true);
 when(outcomeStateMock.equals(any())).thenReturn(true);
 when(triggerEventMock.equals(any())).thenReturn(true);

 IState result = transition.trigger(initialStateMock, triggerEventMock);
 assertEquals(outcomeStateMock, result);

 verify(initialStateMock, times(1)).equals(initialStateMock);
 verify(outcomeStateMock, times(1)).equals(outcomeStateMock);
 verify(triggerEventMock, times(1)).equals(triggerEventMock);
 verifyNoMoreInteractions(guardConditions);
 }

 @Test
 public void testTriggerWithGuards() {
 IState initialStateWithDifferentName = mock(IState.class);
 when(initialStateMock.equals(any())).thenReturn(false);
 when(initialStateWithDifferentName.equals(any())).thenReturn(true);

 Event eventWithDifferentName = mock(Event.class);
 when(triggerEventMock.equals(any())).thenReturn(false);
 when(eventWithDifferentName.equals(any())).thenReturn(true);

 IGuardCondition guardConditionMock = mock(IGuardCondition.class);
 when(guardConditionMock.evaluate(any())).thenReturn(true);
 transition.addGuardCondition(guardConditionMock);

 when(initialStateMock.equals(initialStateMock)).thenReturn(true);
 when(initialStateWithDifferentName.equals(initialStateMock)).thenReturn(false);
 when(outcomeStateMock.equals(any())).thenReturn(true);

 IState result = transition.trigger(initialStateMock, eventWithDifferentName);
 assertNotEquals(outcomeStateMock, result);

 verify(initialStateMock, times(1)).equals(initialStateMock);
 verify(initialStateWithDifferentName, times(1)).equals(initialStateWithDifferentName);
 verify(outcomeStateMock, times(0)).equals(outcomeStateMock);
 verify(triggerEventMock, times(1)).equals(eventWithDifferentName);
 verify(guardConditionMock, times(1)).evaluate(eventWithDifferentName);
 }

 @Test
 public void testWillTriggerWithNoGuards() {
 IState initialStateWithDifferentName = mock(IState.class);
 when(initialStateMock.equals(any())).thenReturn(false);
 when(initialStateWithDifferentName.equals(any())).thenReturn(true);

 Event eventWithDifferentName = mock(Event.class);
 when(triggerEventMock.equals(any())).thenReturn(false);
 when(eventWithDifferentName.equals(any())).thenReturn(true);

 boolean result = transition.willTrigger(initialStateWithDifferentName, triggerEventMock);
 assertFalse(result);

 verify(initialStateMock, times(0)).equals(initialStateMock);
 verify(initialStateWithDifferentName, times(1)).equals(initialStateWithDifferentName);
 verify(outcomeStateMock, times(0)).equals(outcomeStateMock);
 verify(triggerEventMock, times(1)).equals(eventWithDifferentName);
 }

 @Test
 public void testWillTriggerWithGuards() {
 IState initialStateWithDifferentName = mock(IState.class);
 when(initialStateMock.equals(any())).thenReturn(false);
 when(initialStateWithDifferentName.equals(any())).thenReturn(true);

 Event eventWithDifferentName = mock(Event.class);
 when(triggerEventMock.equals(any())).thenReturn(false);
 when(eventWithDifferentName.equals(any())).thenReturn(true);

 IGuardCondition guardConditionMock = mock(IGuardCondition.class);
 when(guardConditionMock.evaluate(any())).thenReturn(false);
 transition.addGuardCondition(guardConditionMock);

 when(initialStateMock.equals(initialStateMock)).thenReturn(true);
 when(initialStateWithDifferentName.equals(initialStateMock)).thenReturn(false);
 when(outcomeStateMock.equals(any())).thenReturn(true);

 boolean result = transition.willTrigger(initialStateMock, eventWithDifferentName);
 assertFalse(result);

 verify(initialStateMock, times(1)).equals(initialStateMock);
 verify(initialStateWithDifferentName, times(1)).equals(initialStateWithDifferentName);
 verify(outcomeStateMock, times(0)).equals(outcomeStateMock);
 verify(triggerEventMock, times(1)).equals(eventWithDifferentName);
 verify(guardConditionMock, times(1)).evaluate(eventWithDifferentName);
 }
}