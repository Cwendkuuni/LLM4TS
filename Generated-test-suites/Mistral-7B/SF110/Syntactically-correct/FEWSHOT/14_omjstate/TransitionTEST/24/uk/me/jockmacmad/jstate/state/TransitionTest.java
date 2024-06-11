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

 transition = new Transition("testTransition", triggerEventMock, initialStateMock, outcomeStateMock);
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("testTransition", triggerEventMock, initialStateMock, outcomeStateMock);
 Transition transition2 = new Transition("testTransition", triggerEventMock, initialStateMock, outcomeStateMock);

 assertEquals(transition1, transition2);
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("testTransition", triggerEventMock, initialStateMock, outcomeStateMock);
 Transition transition2 = new Transition("testTransition", triggerEventMock, initialStateMock, outcomeStateMock);

 assertEquals(transition1.hashCode(), transition2.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("Transition{name='testTransition', initialState=MockState, outcomeState=MockState, triggerEvent=MockEvent}", transition.toString());
 }

 @Test
 public void testTriggerNullInitialState() {
 assertNull(transition.trigger(null, triggerEventMock));
 }

 @Test
 public void testTriggerNullEvent() {
 assertSame(initialStateMock, transition.trigger(initialStateMock, null));
 }

 @Test
 public void testTriggerNullTransition() {
 assertSame(initialStateMock, transition.trigger(initialStateMock, triggerEventMock));
 }

 @Test
 public void testTriggerSameInitialStateAndEvent() {
 when(initialStateMock.equals(initialStateMock)).thenReturn(true);
 when(outcomeStateMock.equals(outcomeStateMock)).thenReturn(true);

 IState state = mock(IState.class);
 when(state.equals(initialStateMock)).thenReturn(true);
 when(guardConditions.isEmpty()).thenReturn(true);

 assertEquals(outcomeStateMock, transition.trigger(state, triggerEventMock));
 }

 @Test
 public void testTriggerDifferentInitialStateAndEvent() {
 when(initialStateMock.equals(initialStateMock)).thenReturn(false);

 IState state = mock(IState.class);
 when(state.equals(initialStateMock)).thenReturn(false);

 assertSame(initialStateMock, transition.trigger(state, triggerEventMock));
 }

 @Test
 public void testTriggerWithGuards() {
 when(initialStateMock.equals(initialStateMock)).thenReturn(true);
 when(outcomeStateMock.equals(outcomeStateMock)).thenReturn(true);
 when(guardConditions.isEmpty()).thenReturn(false);

 IState state = mock(IState.class);
 when(state.equals(initialStateMock)).thenReturn(true);
 when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);

 GuardCondition guardMock = mock(GuardCondition.class);
 when(guardConditions.get(0).evaluate(triggerEventMock)).thenReturn(true);

 IState outcomeState = mock(IState.class);
 when(outcomeStateMock.equals(outcomeState)).thenReturn(true);

 when(guardConditions.get(0).evaluate(triggerEventMock)).thenReturn(true);

 assertEquals(outcomeState, transition.trigger(state, triggerEventMock));
 }

 @Test
 public void testTriggerWithFailingGuards() {
 when(initialStateMock.equals(initialStateMock)).thenReturn(true);
 when(outcomeStateMock.equals(outcomeStateMock)).thenReturn(true);
 when(guardConditions.isEmpty()).thenReturn(false);

 IState state = mock(IState.class);
 when(state.equals(initialStateMock)).thenReturn(true);
 when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);

 GuardCondition guardMock = mock(GuardCondition.class);
 when(guardConditions.get(0).evaluate(triggerEventMock)).thenReturn(false);

 assertSame(initialStateMock, transition.trigger(state, triggerEventMock));
 }

 @Test
 public void testWillTriggerNullInitialState() {
 assertFalse(transition.willTrigger(null, triggerEventMock));
 }

 @Test
 public void testWillTriggerNullEvent() {
 assertFalse(transition.willTrigger(initialStateMock, null));
 }

 @Test
 public void testWillTriggerNullTransition() {
 assertFalse(transition.willTrigger(initialStateMock, triggerEventMock));
 }

 @Test
 public void testWillTriggerSameInitialStateAndEvent() {
 when(initialStateMock.equals(initialStateMock)).thenReturn(true);
 when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);
 when(guardConditions.isEmpty()).thenReturn(true);

 assertTrue(transition.willTrigger(initialStateMock, triggerEventMock));
 }

 @Test
 public void testWillTriggerDifferentInitialStateAndEvent() {
 when(initialStateMock.equals(initialStateMock)).thenReturn(false);
 when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);

 assertFalse(transition.willTrigger(initialStateMock, triggerEventMock));
 }

 @Test
 public void testWillTriggerWithGuards() {
 when(initialStateMock.equals(initialStateMock)).thenReturn(true);
 when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);
 when(guardConditions.isEmpty()).thenReturn(false);

 GuardCondition guardMock = mock(GuardCondition.class);
 when(guardConditions.get(0).evaluate(triggerEventMock)).thenReturn(true);

 assertTrue(transition.willTrigger(initialStateMock, triggerEventMock));
 }

 @Test
 public void testWillTriggerWithFailingGuards() {
 when(initialStateMock.equals(initialStateMock)).thenReturn(true);
 when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);
 when(guardConditions.isEmpty()).thenReturn(false);

 GuardCondition guardMock = mock(GuardCondition.class);
 when(guardConditions.get(0).evaluate(triggerEventMock)).thenReturn(false);

 assertFalse(transition.willTrigger(initialStateMock, triggerEventMock));
 }

 @Test
 public void testAddGuardCondition() {
 guardConditions.add(mock(GuardCondition.class));
 assertEquals(1, transition.getGuardConditions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullGuardCondition() {
 transition.addGuardCondition(null);
 }
}