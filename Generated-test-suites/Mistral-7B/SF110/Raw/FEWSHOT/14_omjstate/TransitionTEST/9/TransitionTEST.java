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
 private IState currentStateMock;
 private IGuardCondition guardConditionMock;
 private List<IGuardCondition> guardConditionsMock;

 @Before
 public void setUp() {
 initialStateMock = mock(IState.class);
 outcomeStateMock = mock(IState.class);
 triggerEventMock = mock(Event.class);
 currentStateMock = mock(IState.class);
 guardConditionMock = mock(IGuardCondition.class);
 guardConditionsMock = new ArrayList<>();
 guardConditionsMock.add(guardConditionMock);

 transition = new Transition("TransitionName", triggerEventMock, initialStateMock, outcomeStateMock);
 transition.addGuardCondition(guardConditionMock);
 }

 @Test
 public void testEquals() {
 Transition transition2 = new Transition("TransitionName", triggerEventMock, initialStateMock, outcomeStateMock);
 transition2.addGuardCondition(guardConditionMock);

 assertTrue(transition.equals(transition2));
 }

 @Test
 public void testHashCode() {
 Transition transition2 = new Transition("TransitionName", triggerEventMock, initialStateMock, outcomeStateMock);
 transition2.addGuardCondition(guardConditionMock);

 assertEquals(transition.hashCode(), transition2.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("TransitionName[initialState=mock(uk.me.jockmacmad.jstate.state.IState@683314c), outcomeState=mock(uk.me.jockmacmad.jstate.state.IState@683314c), guardConditions=[mock(uk.me.jockmacmad.jstate.predicate.IGuardCondition@683314e)], triggerEvent=mock(uk.me.jockmacmad.jstate.Event@683314f)]", transition.toString());
 }

 @Test
 public void testTriggerNullInitialState() {
 assertNull(transition.trigger(null, triggerEventMock));
 }

 @Test
 public void testTriggerNullTriggerEvent() {
 assertNull(transition.trigger(initialStateMock, null));
 }

 @Test
 public void testTriggerNullCurrentState() {
 assertSame(initialStateMock, transition.trigger(null, triggerEventMock));
 }

 @Test
 public void testTriggerNullOutcomeState() {
 assertSame(initialStateMock, transition.trigger(initialStateMock, triggerEventMock));
 }

 @Test
 public void testTrigger() {
 when(initialStateMock.equals(currentStateMock)).thenReturn(true);
 when(guardConditionMock.evaluate(triggerEventMock)).thenReturn(true);

 assertEquals(outcomeStateMock, transition.trigger(currentStateMock, triggerEventMock));

 verify(outcomeStateMock).setPreviousState(currentStateMock);
 }

 @Test
 public void testWillTriggerNullInitialState() {
 assertFalse(transition.willTrigger(null, triggerEventMock));
 }

 @Test
 public void testWillTriggerNullTriggerEvent() {
 assertFalse(transition.willTrigger(initialStateMock, null));
 }

 @Test
 public void testWillTriggerNullCurrentState() {
 assertTrue(transition.willTrigger(initialStateMock, triggerEventMock));
 }

 @Test
 public void testWillTriggerNullOutcomeState() {
 assertTrue(transition.willTrigger(initialStateMock, triggerEventMock));
 }

 @Test
 public void testWillTrigger() {
 when(initialStateMock.equals(currentStateMock)).thenReturn(true);
 when(guardConditionMock.evaluate(triggerEventMock)).thenReturn(true);

 assertTrue(transition.willTrigger(currentStateMock, triggerEventMock));
 }

 @Test
 public void testWillTriggerFalse() {
 when(initialStateMock.equals(currentStateMock)).thenReturn(true);
 when(guardConditionMock.evaluate(triggerEventMock)).thenReturn(false);

 assertFalse(transition.willTrigger(currentStateMock, triggerEventMock));
 }

 @Test
 public void testWillTriggerDifferentInitialState() {
 when(initialStateMock.equals(currentStateMock)).thenReturn(false);

 assertFalse(transition.willTrigger(currentStateMock, triggerEventMock));
 }

 @Test
 public void testWillTriggerDifferentTriggerEvent() {
 Transition transition2 = new Transition("TransitionName", triggerEventMock, initialStateMock, outcomeStateMock);
 transition2.addGuardCondition(guardConditionMock);

 assertFalse(transition.willTrigger(initialStateMock, triggerEventMock));
 assertFalse(transition2.willTrigger(initialStateMock, triggerEventMock));
 }
}