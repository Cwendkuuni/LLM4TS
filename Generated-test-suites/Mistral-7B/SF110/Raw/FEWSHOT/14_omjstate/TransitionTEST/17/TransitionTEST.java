package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.*;
import java.util.*;

public class TransitionTest {

 IState initialStateMock;
 IState outcomeStateMock;
 List<IGuardCondition> guardConditionsMock;
 Event triggerEventMock;
 Transition transition;

 @Before
 public void setUp() {
 initialStateMock = mock(IState.class);
 outcomeStateMock = mock(IState.class);
 guardConditionsMock = new ArrayList<>();
 triggerEventMock = mock(Event.class);
 transition = new Transition("TransitionName", triggerEventMock, initialStateMock, outcomeStateMock);
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("TransitionName", triggerEventMock, initialStateMock, outcomeStateMock);
 Transition transition2 = new Transition("TransitionName", triggerEventMock, initialStateMock, outcomeStateMock);

 assertTrue(transition.equals(transition1));
 assertFalse(transition.equals(null));
 assertFalse(transition.equals("string"));
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TransitionName1", triggerEventMock, initialStateMock, outcomeStateMock);
 Transition transition2 = new Transition("TransitionName2", triggerEventMock, initialStateMock, outcomeStateMock);

 assertNotEquals(transition1.hashCode(), transition2.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("TransitionName{initialState=" + initialStateMock + ", outcomeState=" + outcomeStateMock + ", triggerEvent=" + triggerEventMock + '}', transition.toString());
 }

 @Test
 public void testTrigger_initialStateAndEventMatches() {
 when(initialStateMock.equals(initialStateMock)).thenReturn(true);
 when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);

 IState expectedOutcomeState = outcomeStateMock;
 IState retVal = transition.trigger(initialStateMock, triggerEventMock);

 assertEquals(expectedOutcomeState, retVal);
 }

 @Test
 public void testTrigger_initialStateAndEventDoNotMatch() {
 when(initialStateMock.equals(initialStateMock)).thenReturn(false);
 when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);

 IState expectedInitialState = initialStateMock;
 IState retVal = transition.trigger(outcomeStateMock, triggerEventMock);

 assertEquals(expectedInitialState, retVal);
 }

 @Test
 public void testTrigger_initialStateAndEventAndGuardConditionsMatch() {
 when(initialStateMock.equals(initialStateMock)).thenReturn(true);
 when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);
 GuardCondition guardConditionMock = mock(IGuardCondition.class);
 guardConditionsMock.add(guardConditionMock);
 when(guardConditionMock.evaluate(triggerEventMock)).thenReturn(true);

 IState expectedOutcomeState = outcomeStateMock;
 IState retVal = transition.trigger(initialStateMock, triggerEventMock);

 assertEquals(expectedOutcomeState, retVal);
 }

 @Test
 public void testTrigger_initialStateAndEventAndGuardConditionsDoNotMatch() {
 when(initialStateMock.equals(initialStateMock)).thenReturn(true);
 when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);
 GuardCondition guardConditionMock = mock(IGuardCondition.class);
 guardConditionsMock.add(guardConditionMock);
 when(guardConditionMock.evaluate(triggerEventMock)).thenReturn(false);

 IState expectedInitialState = initialStateMock;
 IState retVal = transition.trigger(initialStateMock, triggerEventMock);

 assertEquals(expectedInitialState, retVal);
 }

 @Test
 public void testWillTrigger_initialStateAndEventMatches() {
 when(initialStateMock.equals(initialStateMock)).thenReturn(true);
 when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);

 boolean retVal = transition.willTrigger(initialStateMock, triggerEventMock);

 assertTrue(retVal);
 }

 @Test
 public void testWillTrigger_initialStateAndEventDoNotMatch() {
 when(initialStateMock.equals(initialStateMock)).thenReturn(false);
 when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);

 boolean retVal = transition.willTrigger(initialStateMock, triggerEventMock);

 assertFalse(retVal);
 }

 @Test
 public void testWillTrigger_initialStateAndEventAndGuardConditionsMatch() {
 when(initialStateMock.equals(initialStateMock)).thenReturn(true);
 when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);
 GuardCondition guardConditionMock = mock(IGuardCondition.class);
 guardConditionsMock.add(guardConditionMock);
 when(guardConditionMock.evaluate(triggerEventMock)).thenReturn(true);

 boolean retVal = transition.willTrigger(initialStateMock, triggerEventMock);

 assertTrue(retVal);
 }

 @Test
 public void testWillTrigger_initialStateAndEventAndGuardConditionsDoNotMatch() {
 when(initialStateMock.equals(initialStateMock)).thenReturn(true);
 when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);
 GuardCondition guardConditionMock = mock(IGuardCondition.class);
 guardConditionsMock.add(guardConditionMock);
 when(guardConditionMock.evaluate(triggerEventMock)).thenReturn(false);

 boolean retVal = transition.willTrigger(initialStateMock, triggerEventMock);

 assertFalse(retVal);
 }

 @Test
 public void testAddGuardCondition() {
 GuardCondition guardConditionMock = mock(IGuardCondition.class);
 transition.addGuardCondition(guardConditionMock);
 assertTrue(guardConditionsMock.contains(guardConditionMock));
 }
}