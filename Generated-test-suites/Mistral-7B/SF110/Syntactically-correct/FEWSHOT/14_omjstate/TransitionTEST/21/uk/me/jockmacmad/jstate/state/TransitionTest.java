package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import uk.me.jockmacmad.jstate.predicate.*;
import org.junit.Test;
import java.util.*;

public class TransitionTest {

 IState mockInitialState, mockOutcomeState;
 Event mockEvent;
 List<IGuardCondition> mockGuardConditions;
 Transition transition;

 @Before
 public void setUp() {
 mockInitialState = mock(IState.class);
 mockOutcomeState = mock(IState.class);
 mockEvent = mock(Event.class);
 mockGuardConditions = new ArrayList<>();
 transition = new Transition("transitionName", mockEvent, mockInitialState, mockOutcomeState);
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("transitionName", mockEvent, mockInitialState, mockOutcomeState);
 Transition transition2 = new Transition("transitionName", mockEvent, mockInitialState, mockOutcomeState);
 assertTrue(transition.equals(transition1));
 Transition transition3 = new Transition("transitionName2", mockEvent, mockInitialState, mockOutcomeState);
 assertFalse(transition.equals(transition3));
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("transitionName", mockEvent, mockInitialState, mockOutcomeState);
 Transition transition2 = new Transition("transitionName", mockEvent, mockInitialState, mockOutcomeState);
 assertEquals(transition.hashCode(), transition1.hashCode());
 Transition transition3 = new Transition("transitionName2", mockEvent, mockInitialState, mockOutcomeState);
 assertNotEquals(transition.hashCode(), transition3.hashCode());
 }

 @Test
 public void testToString() {
 String transitionString = "Transition{name='transitionName', triggerEvent=" + mockEvent + ", initialState=" + mockInitialState + ", outcomeState=" + mockOutcomeState + "}";
 assertEquals(transitionString, transition.toString());
 }

 @Test
 public void testAddGuardConditionIPredicate() {
 IPredicate mockPredicate = mock(IPredicate.class);
 transition.addGuardCondition(mockPredicate);
 assertEquals(1, transition.guardConditions.size());
 }

 @Test
 public void testAddGuardConditionIGuardCondition() {
 IGuardCondition mockGuardCondition = mock(IGuardCondition.class);
 transition.addGuardCondition(mockGuardCondition);
 assertEquals(1, transition.guardConditions.size());
 }

 @Test
 public void testEvaluateGuardConditions() {
 //Test empty guard conditions
 transition = new Transition("transitionName", mockEvent, mockInitialState, mockOutcomeState);
 assertTrue(transition.evaluateGuardConditions());

 //Test guard conditions
 IPredicate mockPredicate = mock(IPredicate.class);
 when(mockPredicate.evaluate(mockEvent)).thenReturn(true);
 transition.addGuardCondition(mockPredicate);
 transition.evaluateGuardConditions();
 verify(mockPredicate).evaluate(mockEvent);
 }

 @Test
 public void testGetInitialState() {
 assertEquals(mockInitialState, transition.getInitialState());
 }

 @Test
 public void testGetOutcomeState() {
 assertEquals(mockOutcomeState, transition.getOutcomeState());
 }

 @Test
 public void testGetTriggerEvent() {
 assertEquals(mockEvent, transition.getTriggerEvent());
 }

 @Test
 public void testTriggerNullCurrentState() {
 assertNull(transition.trigger(null, mockEvent));
 }

 @Test
 public void testTriggerNullEvent() {
 assertSame(mockInitialState, transition.trigger(mockInitialState, null));
 }

 @Test
 public void testTrigger() {
 when(mockInitialState.equals(mockInitialState)).thenReturn(true);
 when(mockInitialState.equals(mockOutcomeState)).thenReturn(false);
 when(mockGuardConditions.isEmpty()).thenReturn(true);
 when(mockGuardConditions.size()).thenReturn(1);
 when(mockGuardConditions.get(0).evaluate(mockEvent)).thenReturn(true);

 assertEquals(mockOutcomeState, transition.trigger(mockInitialState, mockEvent));
 }

 @Test
 public void testWillTriggerNullCurrentState() {
 assertFalse(transition.willTrigger(null, mockEvent));
 }

 @Test
 public void testWillTriggerNullEvent() {
 assertFalse(transition.willTrigger(mockInitialState, null));
 }

 @Test
 public void testWillTrigger() {
 when(mockInitialState.equals(mockInitialState)).thenReturn(true);
 when(mockInitialState.equals(mockOutcomeState)).thenReturn(false);
 when(mockGuardConditions.isEmpty()).thenReturn(true);
 when(mockGuardConditions.size()).thenReturn(1);
 when(mockGuardConditions.get(0).evaluate(mockEvent)).thenReturn(true);
 assertTrue(transition.willTrigger(mockInitialState, mockEvent));
 }

 @Test
 public void testWillTriggerWithFailedGuards() {
 when(mockGuardConditions.isEmpty()).thenReturn(false);
 when(mockGuardConditions.size()).thenReturn(1);
 when(mockGuardConditions.get(0).evaluate(mockEvent)).thenReturn(false);
 assertFalse(transition.willTrigger(mockInitialState, mockEvent));
 }
}